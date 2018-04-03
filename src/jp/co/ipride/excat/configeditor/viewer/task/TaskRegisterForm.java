package jp.co.ipride.excat.configeditor.viewer.task;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import jp.co.ipride.excat.configeditor.model.ConfigModel;
import jp.co.ipride.excat.configeditor.model.task.ITask;
import jp.co.ipride.excat.configeditor.util.ViewerUtil;

/**
 * ��̃^�X�N�̒�`
 *
 * @author tu
 * @since 2007/11/11
 * @date 2009/9/6 V3�̂��߉���
 */
public class TaskRegisterForm implements ITaskForm{

	private SashForm appWindow = null;
	private CTabFolder tabFolder = null;
	private CTabItem tabItem = null;
	private ScrolledComposite scroller = null;
	private Composite form = null;

	//private TriggerForm triggerForm = null;
	private DumpDataForm dumpDataForm = null;
	private OtherForm otherForm = null;

	private AutoMonitorExceptionTaskForm autoTaskForm = null;
	private MonitorExceptionTaskForm specificTaskForm = null;
	private MonitorMethodTaskForm methodTaskForm = null;
	private MonitorSignalTaskForm signalTaskForm = null;

	private TaskCommonForm taskCommonForm = null;

	private Button comGroupCheck;

	private ITask task = null;

	/**
	 * �V�K�ǉ���
	 * @param appWindow
	 * @param tabFolder
	 * @param index
	 */
	public TaskRegisterForm(SashForm appWindow,
					CTabFolder tabFolder,	int taskType){

		this.appWindow = appWindow;
		this.tabFolder = tabFolder;
		this.task = ConfigModel.createNewTask(taskType);
		tabItem = new CTabItem(tabFolder, SWT.NULL);

		//add by v3
		tabItem.setData(task.getIdentfyKey());
		createBaseForm();

	}

	/**
	 * �����^�X�N��Ǎ��ݎ�
	 * @param appWindow
	 * @param tabFolder
	 * @param task
	 */
	public TaskRegisterForm(SashForm appWindow,CTabFolder tabFolder,ITask task){
		this.appWindow = appWindow;
		this.tabFolder = tabFolder;
		this.task=task;
		tabItem = new CTabItem(tabFolder, SWT.NULL);
		tabItem.setData(task.getIdentfyKey());
		createBaseForm();
	}

	private void createBaseForm(){

		scroller = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL | SWT.RESIZE);
		scroller.setMinSize(
				ViewerUtil.getScrollMinRectangle(appWindow).width,
				ViewerUtil.getScrollMinRectangle(appWindow).height);
		tabItem.setControl(scroller);

		form = new Composite(scroller, SWT.NONE);
		scroller.setContent(form);
		scroller.setExpandHorizontal(true);
		scroller.setExpandVertical(true);

		GridLayout layout = new GridLayout();
		layout.marginTop = ViewerUtil.getMarginHeight(appWindow);
		layout.marginBottom = ViewerUtil.getMarginHeight(appWindow);
		layout.marginLeft = ViewerUtil.getMarginWidth(appWindow);
		layout.marginRight = ViewerUtil.getMarginWidth(appWindow);
		layout.numColumns = 2;
		form.setLayout(layout);
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_BEGINNING);
		gridData.verticalAlignment=SWT.BEGINNING;
        form.setLayoutData(gridData);

		switch(task.getTaskType()){
		case 0:
			autoTaskForm = new AutoMonitorExceptionTaskForm(appWindow, form, this);
			taskCommonForm = autoTaskForm;
			break;
		case 1:
			specificTaskForm = new MonitorExceptionTaskForm(appWindow, form, this);
			taskCommonForm = specificTaskForm;
			break;
		case 2:
			methodTaskForm = new MonitorMethodTaskForm(appWindow, form, this);
			taskCommonForm = methodTaskForm;
			break;
		case 3:
			signalTaskForm = new MonitorSignalTaskForm(appWindow, form, this);
			taskCommonForm = signalTaskForm;
			break;
		}

		Composite comGroup = new Composite(form, SWT.NONE);
		GridLayout comGrouplayout = new GridLayout();
		comGroup.setLayout(comGrouplayout);
		gridData = new GridData();
		comGroup.setLayoutData(gridData);

	    //����
		dumpDataForm = new DumpDataForm(appWindow,comGroup,this,task.getTaskType());
		otherForm = new OtherForm(appWindow,comGroup,this);

	}

	public boolean isThis(String key){
		if (this.tabItem.getData().equals(key)){
			return true;
		}else{
			return false;
		}
	}

	public void selectDumpStackBySignal(){
		if (dumpDataForm != null){
			dumpDataForm.selectDumpStackBySignal();
		}
	}

	public void selectDumpObjectBySignal(){
		if (dumpDataForm != null){
			dumpDataForm.selectDumpObjectBySignal();
		}
	}

	public ITask getTask(){
		return task;
	}

	public CTabItem getTabItem(){
		return tabItem;
	}

	/**
	 * ��{�ݒ肩��̃N���[�Y�ʒm
	 *
	 */
	public void closeMailNotice(){
		otherForm.closeMailNotice();
	}

	/**
	 * �ۑ�����O�̍��ڃ`�F�b�N
	 * @return
	 */
	public boolean checkItems(){
		boolean result=true;

		if (!taskCommonForm.checkItems()){
			result=false;
		}
		if (!dumpDataForm.checkItems()){
			result=false;
		}
		if (!otherForm.checkItems()){
			result=false;
		}

		return result;
	}
}

