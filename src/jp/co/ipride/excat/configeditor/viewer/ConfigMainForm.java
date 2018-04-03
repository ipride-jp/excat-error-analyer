package jp.co.ipride.excat.configeditor.viewer;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import jp.co.ipride.excat.MainViewer;
import jp.co.ipride.excat.common.ApplicationResource;
import jp.co.ipride.excat.common.icons.IconFilePathConstant;
import jp.co.ipride.excat.configeditor.viewer.baseinfo.BaseInfoForm;
import jp.co.ipride.excat.configeditor.viewer.instance.ObjectRegisterForm;
import jp.co.ipride.excat.configeditor.viewer.task.TaskRegisterForm;
import jp.co.ipride.excat.configeditor.viewer.template.TemplateRegisterForm;
import jp.co.ipride.excat.configeditor.model.ConfigContant;
import jp.co.ipride.excat.configeditor.model.ConfigModel;
import jp.co.ipride.excat.configeditor.model.task.ITask;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TreeItem;

/**
 * ���ʂ̃t���[��
 * @author tu
 * @since 2007/11/10
 *
 */
public class ConfigMainForm{

	private MainViewer mainViewer;  //MainViewer
	private CTabFolder topTabFolder;  //�u�ݒ�v�A�u���́v�̃^�u
	private CTabItem configTabItem = null; //�u�ݒ�v�̃A�C�e��

	private int[] weights = new int[] { 2, 8 };

	private SashForm configForm;  	//leftForm and rightForm���}�[�W����
	private SashForm leftForm;   	//cfgtree�̃x�[�X�E�t�H�[��
	private SashForm rightForm;  	//�e�^�X�N�̕ҏW�t�H�[��
	public CTabFolder configEditFolder = null; //�e�^�X�N�̕ҏW�t�H�[�����Ǘ�����B


	public ConfigTree configTree = null;

	private List<TaskRegisterForm> taskFormList = new ArrayList<TaskRegisterForm>();
	private BaseInfoForm basicInfoForm = null;
	private ObjectRegisterForm objectRegisterForm = null;
	private TemplateRegisterForm templeRegisterForm = null;

	/**
	 * �R���X�g���N�^
	 * @param appWindow
	 */
	public ConfigMainForm(MainViewer view,CTabFolder tabFolder){
		this.mainViewer = view;
		this.topTabFolder = tabFolder;
		this.configTabItem = new CTabItem(tabFolder, SWT.NULL, 0);
		this.configTabItem.setText(ApplicationResource.getResource("tab.config.Title"));
		URL url = MainViewer.class.getResource(IconFilePathConstant.TAB_CONFIG);
		configTabItem.setImage(ImageDescriptor.createFromURL(url).createImage());

		configForm = new SashForm(this.topTabFolder, SWT.NONE);
		this.configTabItem.setControl(configForm);
		leftForm = new SashForm(configForm, SWT.NONE);
		rightForm = new SashForm(configForm, SWT.NONE);
		configEditFolder = new CTabFolder(rightForm,SWT.BORDER );
		createBaseForm();
	}

	private void createBaseForm(){
		leftForm.setOrientation(SWT.VERTICAL);
		configForm.setWeights(weights);
		configEditFolder.setTabHeight(0);
		configEditFolder.setSimple(false);
		configEditFolder.marginHeight = 0;
		configEditFolder.marginWidth = 0;

		configTree = new ConfigTree(this.mainViewer, leftForm, SWT.BORDER);

		configTree.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				String treeNodeIdentfy="";
				mainViewer.deleteTaskAction.setEnabled(false);

				if(event.getSelection().isEmpty()){
					mainViewer.deleteTaskAction.setEnabled(false);
					return;
				}
				TreeItem item = (TreeItem)configTree.getTree().getSelection()[0];
				if (item.getData() instanceof String){
					treeNodeIdentfy = (String)item.getData();
				}else if(item.getData() instanceof ITask){
					//��Q #440 �Ή� begin
					mainViewer.deleteTaskAction.setEnabled(true);
					//��Q #440 �Ή� end
					treeNodeIdentfy=((ITask)item.getData()).getIdentfyKey();
				}
				CTabItem[] items = configEditFolder.getItems();
				//look out edit form using identfy.
				for (int j = 0; j < items.length; j++) {
					if (treeNodeIdentfy.equals(items[j].getData().toString())){
						configEditFolder.setSelection(items[j]);
						break;
					}
				}
			}
		});
	}

	/**
	 * �^�u�E�t�H���_�[�Ƀ��X�i�[��ǉ�
	 * @param listener
	 */
	public void addSelectionListenerToTabFolder(SelectionListener listener){
		topTabFolder.addSelectionListener(listener);
	}

	/**
	 * �V�K�R���t�B�O���쐬�B
	 * (���j���[�A�N�V��������ǂݏo��)
	 */
	public void createNewConfig(){
		moveAll();
		basicInfoForm = new BaseInfoForm(rightForm, configEditFolder);
		templeRegisterForm = new TemplateRegisterForm(rightForm, configEditFolder);
		objectRegisterForm = new ObjectRegisterForm(rightForm, configEditFolder);
		topTabFolder.setSelection(0);
	}

	/**
	 * �V�K�^�X�N��ǉ�
	 * (���j���[�A�N�V��������ǂݏo��)
	 */
	public void addNewTask(int taskType){
		//�^�X�N�E�t�H�[���̒ǉ�
		TaskRegisterForm newTaskForm =
			new TaskRegisterForm(rightForm, configEditFolder, taskType);
		taskFormList.add(newTaskForm);
		configTree.refrash();
		configTree.selectMonitorTask(newTaskForm.getTask());
		configEditFolder.setSelection(newTaskForm.getTabItem());
	}



	/**
	 * �����R���t�B�O��ҏW
	 * (���j���[�A�N�V��������ǂݏo��)
	 */
	public void createOldConfig(){
		moveAll();
		basicInfoForm = new BaseInfoForm(rightForm, configEditFolder);
		templeRegisterForm = new TemplateRegisterForm(rightForm, configEditFolder);
		objectRegisterForm = new ObjectRegisterForm(rightForm, configEditFolder);

		Vector<ITask> tasks = ConfigModel.getTaskList().getTasks();
		for (int i=0; i<tasks.size(); i++){
			TaskRegisterForm taskForm = new TaskRegisterForm(rightForm, configEditFolder, tasks.get(i));
			taskFormList.add(taskForm);
		}
		configTree.refrash();
	}

	/**
	 * ���݂̃^�X�N���폜����B
	 * @data 2009/9/13
	 */
	public void deleteCurrentTask(){
		TreeItem item = configTree.getSelectItem();
		Object data = item.getData();
		if (data instanceof ITask){
			ITask task = (ITask)data;
			ConfigModel.getTaskList().removeTask(task);
			String key = task.getIdentfyKey();
			for (int index = 0; index < taskFormList.size(); index++){
				TaskRegisterForm taskForm = (TaskRegisterForm)taskFormList.get(index);
				if (taskForm.isThis(key)){
					taskFormList.remove(index);
					configEditFolder.setSelection(0);
					configTree.refrash();
					ConfigModel.setChanged();
					break;
				}
			}
		}
	}

	public boolean hasTask(){
		if (taskFormList.isEmpty()){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * �����_�\������Ă���Tab�̓^�X�N�����m�F����B
	 * @return
	 */
	public boolean canDeleteThisTask(){
		Object data = configTree.getSelectItem().getData();
		if (data instanceof ITask){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * ��{�ݒ肩��̃N���[�Y�ʒm
	 *
	 */
	public void closeMailNotice(){
		for (int i=0; i<taskFormList.size(); i++){
			TaskRegisterForm task = (TaskRegisterForm)taskFormList.get(i);
			task.closeMailNotice();
		}
	}

	/**
	 * �ۑ��O�̍��ڃ`�F�b�N
	 * ���ӁF�^�[�Q�b�g�̍��ڃ`�F�b�N�͓o�^���Ƀ`�F�b�N���Ă��邽�߁A
	 * �����ł́A�`�F�b�N���Ă��Ȃ��B
	 * @return
	 */
	public boolean checkItems(){
		boolean result= true;
		//�P��ʂ̃`�F�b�N
		result = basicInfoForm.checkItems();
		if (!result){
			//topTabFolder.setSelection(0);
			configTree.getTreeViewer().setSelection(
					new StructuredSelection(ConfigContant.Tree_Item_BaseInfo));
			return result;
		}

		for (int i=0; i<taskFormList.size(); i++){
			TaskRegisterForm taskForm = (TaskRegisterForm)taskFormList.get(i);
			result = taskForm.checkItems();
			if (!result){
				//topTabFolder.setSelection(i+1);
				configTree.selectMonitorTask(taskForm.getTask());
				configEditFolder.setSelection(taskForm.getTabItem());
				if (taskFormList.size()>1){
					//appWindow.setDeleteTaskAction(true);
				}
				return result;
			}
		}

		result = objectRegisterForm.checkItems();
		if (!result){
			//topTabFolder.setSelection(taskFormList.size() + 2);
			configTree.getTreeViewer().setSelection(
					new StructuredSelection(ConfigContant.Tree_Item_Object_Register));
			return result;
		}

		result=templeRegisterForm.checkItems();
		if (!result){
			//topTabFolder.setSelection(taskFormList.size() + 1);
			configTree.getTreeViewer().setSelection(
					new StructuredSelection(ConfigContant.Tree_Item_Template_Register));
			return result;
		}

		//��ʊ֘A�`�F�b�N�i�^�X�N�ԁj
		//���ɓo�^���Ƀ`�F�b�N�ł����B

		return result;
	}

	public void moveAll(){

//IT-00-001�Ή�		ConfigModel.getTaskList().removeAll();
		CTabItem[] items = configEditFolder.getItems();
		for (int i=0; i<items.length; i++){
			items[i].dispose();
		}
		taskFormList.clear();
		refrash();
	}

	/**
	 * �^�X�N�̗L���t���O��؂�ւ��鏈��
	 * @date 2009/9/12
	 */
	public void refrash(){
		configTree.refrash();
	}

	public void selectConfig(){
		topTabFolder.setSelection(configTabItem);
	}
}