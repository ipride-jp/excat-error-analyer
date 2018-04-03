/*
 * Error Analyzer Tool for Java
 *
 * Created on 2007/10/9
 *
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.common.setting.dialog;

import java.io.File;
import java.util.ArrayList;

import jp.co.ipride.excat.common.ApplicationResource;
import jp.co.ipride.excat.common.setting.Setting;
import jp.co.ipride.excat.common.setting.SettingManager;
import jp.co.ipride.excat.common.utility.Utility;
import jp.co.ipride.excat.configeditor.util.ViewerUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;

/**
 * �\�[�X�p�X�E�N���X�p�X�ݒ�r���[
 *
 * 2007/10/19 �ύX �ݒ�_�C�A���O�𓝈ꂵ���B
 * ���̍ہA�^�u�̓��e�Ƃ��Đݒ肷�邽�߁A�_�C�A���O����Composite�ɕύX���܂����B
 * 2009/9/14�ύX�_
 * �P�D�N���X�ꗗ���t�@�C���ꗗ�ɓ���
 * �Q�D�e���v���[�g�E�^�u��ǉ�
 *
 * @author tatebayashi
 * @date 2009/9/14 ���� tu weixin
 *
 */
public class SourcePathSettingView extends Composite implements SettingViewListener {

	public static final int BUTTON_WIDTH = 80;
    public static final int HORIZONTAL_SPACING = 300;
    private static final int LISTBOX_WIDTH = 240;
    public static final int MARGIN_WIDTH = 0;
    public static final int MARGIN_HEIGHT = 200;

    //�\�[�X�R�[�h��Encoding�̐ݒ�
    private Combo encodingCombo;
    private String encodingSelect;

    private Button addFileBtn;
    private Button addFolderBtn;
    private Button removeFileBtn;
    private Button priorityPrevBtn;
    private Button priorityNextBtn;
    private Button filePrevBtn;
    private Button fileNextBtn;

    protected List fileList;
    protected List priorityList;

    // �\�[�X���X�g�̃Z���N�V�����C���f�F�N�X
    private int[] sourceListIndex;

    private SourcePathSettingView view;

    /**
     * �R���X�g���N�^
     * @param parent
     * @param style
     */
	public SourcePathSettingView(Composite parent, int style) {
		super(parent, style);
		view = this;
		//set dialog area.
		GridLayout layout = new GridLayout(1, false);
		this.setLayout(layout);
		layout.marginLeft=ViewerUtil.getMarginWidth(this);
		layout.marginRight=ViewerUtil.getMarginWidth(this);
		layout.marginTop=ViewerUtil.getMarginHeight(this);
		layout.marginBottom=ViewerUtil.getMarginHeight(this);

        GridData bodyData = new GridData(GridData.FILL_VERTICAL);
        this.setLayoutData(bodyData);

        createFileEditGroup(this);

        createPriorityGroup(this);

        createEncodingConBox(this);

        addListeners();

        init();
	}

	/**
	 * java�\�[�X�t�@�C���̃G���A���i�[����ꏊ
	 * @param c
	 */
	private void createFileEditGroup(Composite c){
        Group group = new Group(c, SWT.NONE);
        group.setText(ApplicationResource.getResource("SourcePathSettingView.SourceFileLabel"));
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(data);
		GridLayout layout = new GridLayout(2, false);
        group.setLayout(layout);

        createFileGroup(group);

        createFileButtonsGroup(group);
	}

	/**
	 * file and folder list
	 *
	 * @param c
	 */
	private void createFileGroup(Composite c){
        fileList = new List(c, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        GridData griddata = new GridData(GridData.FILL_BOTH);
        griddata.verticalSpan = 4;
        griddata.widthHint = LISTBOX_WIDTH;
        griddata.heightHint = fileList.getItemHeight() * 10;
        fileList.setLayoutData(griddata);
	}

	/**
	 * �\�[�X�̒ǉ��E�폜�̃{�^��
	 * @param c
	 */
	private void createFileButtonsGroup(Composite c){
		Composite right_c = new Composite(c,SWT.NONE);
        GridData right_data = new GridData(GridData.FILL_HORIZONTAL);
        right_data.widthHint = 10;
        right_c.setLayoutData(right_data);
        GridLayout right_layout = new GridLayout(1, false);
        right_c.setLayout(right_layout);

//        GridData data_1 = new GridData(GridData.FILL_VERTICAL);
        addFileBtn = Utility.createButton(right_c, SWT.PUSH, ApplicationResource.getResource("SourcePathSettingView.AddFile"),
        		BUTTON_WIDTH,1);
        addFileBtn.setEnabled(true);

//        GridData data_2 = new GridData(GridData.FILL_VERTICAL);
        addFolderBtn = Utility.createButton(right_c, SWT.PUSH, ApplicationResource.getResource("SourcePathSettingView.AddFolder"),
        		BUTTON_WIDTH,1);
        addFolderBtn.setEnabled(true);

//        GridData data_3 = new GridData(GridData.FILL_VERTICAL);
        removeFileBtn = Utility.createButton(right_c, SWT.PUSH, ApplicationResource.getResource("SourcePathSettingView.Delete"),
        		BUTTON_WIDTH,1);
        removeFileBtn.setEnabled(false);

//        GridData data_4 = new GridData(GridData.FILL_VERTICAL);
        filePrevBtn = Utility.createButton(right_c, SWT.PUSH, ApplicationResource.getResource("SourcePathSettingView.PrevBtn"),
        		BUTTON_WIDTH,1);
        filePrevBtn.setEnabled(false);

//        GridData data_5 = new GridData(GridData.FILL_VERTICAL);
        fileNextBtn = Utility.createButton(right_c, SWT.PUSH, ApplicationResource.getResource("SourcePathSettingView.nextBtn"),
        		BUTTON_WIDTH,1);
        fileNextBtn.setEnabled(false);
	}


	/**
	 * �t�@�C���I���`����\������O���[�v
	 * @param c
	 */
	private void createPriorityGroup(Composite c){
		GridLayout layout = new GridLayout(2, false);
        Group group = new Group(c, SWT.NONE);
        group.setText(ApplicationResource.getResource("SettingDialog.priorityOrder"));
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(data);
        group.setLayout(layout);
        // �t�@�C���I���`����\�����郊�X�g
        setPriorityList(group);
        // �t�@�C���I���`����\������{�^��
        setPriorityButtons(group);
	}
	/**
	 * �t�@�C���I���`����\�����郊�X�g
	 * @param group
	 */
	private void setPriorityList(Group group){
		priorityList = new List(group,SWT.MULTI|SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        GridData data_1 = new GridData(GridData.FILL_BOTH);
        data_1.verticalSpan = 2;
        data_1.widthHint = LISTBOX_WIDTH;
        data_1.heightHint = priorityList.getItemHeight() * 2;
        priorityList.setLayoutData(data_1);
	}

	/**
	 * �\�[�X�R�[�h��Encoding�̐ݒ�EEncoding�I���ł���R���{�{�b�N�X��ǉ�����
	 * @param c
	 */
	private void createEncodingConBox(Composite c){
		GridLayout layout = new GridLayout(4, false);
        Group group = new Group(c, SWT.NONE);
        group.setText(ApplicationResource.getResource("SettingDialog.EncodingCombo"));
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(data);
        group.setLayout(layout);
        encodingCombo = new Combo(group, SWT.READ_ONLY|SWT.V_SCROLL);
        encodingCombo.setBounds(10, 10, 100, 80);
        // Encoding���A�v���P�[�V�������\�[�X����擾
        String encodingItemsStr = ApplicationResource.getResource("SettingDialog.EncodingCombo.Items");
        String[] items = encodingItemsStr.split(",");
        encodingCombo.setItems(items);
	}

	private void addListeners(){
//        fileList.addFocusListener(new FocusListener(){
//			public void focusGained(FocusEvent arg0) {
//				priorityList.setSelection(-1);
//			    priorityPrevBtn.setEnabled(false);
//			    priorityNextBtn.setEnabled(false);
//			}
//			public void focusLost(FocusEvent e) {
//			}
//        });
        fileList.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
			public void widgetSelected(SelectionEvent arg0) {
				if(hasSelection(fileList)){
					removeFileBtn.setEnabled(true);
				}
				// �u��ցv�Ɓu���ցv�{�^���̏�Ԃ�ݒ肷��B
				sourceListIndex = fileList.getSelectionIndices();
				setUpperDownButtonState(fileList,filePrevBtn,fileNextBtn);
			}
        });
        priorityList.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent arg0) {
				fileList.setSelection(-1);
				removeFileBtn.setEnabled(false);
				filePrevBtn.setEnabled(false);
				fileNextBtn.setEnabled(false);
			}
			public void focusLost(FocusEvent arg0) {
			}
        });
        priorityList.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
			public void widgetSelected(SelectionEvent arg0) {
				// �u��ցv�Ɓu���ցv�{�^���̏�Ԃ�ݒ肷��B
				setUpperDownButtonState(priorityList,priorityPrevBtn,priorityNextBtn);
			}
        });
        encodingCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String selectedText = encodingCombo.getText();
				if(!encodingSelect.equals(selectedText)){
					SettingManager.getSetting().setEncoding(selectedText);
				}
			}
		});
        encodingCombo.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent arg0) {
				priorityList.setSelection(-1);
				fileList.setSelection(-1);
				filePrevBtn.setEnabled(false);
				fileNextBtn.setEnabled(false);
				removeFileBtn.setEnabled(false);
			    priorityPrevBtn.setEnabled(false);
			    priorityNextBtn.setEnabled(false);
			}
			public void focusLost(FocusEvent e) {
			}
        });
        addFileBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				filePrevBtn.setEnabled(false);
				fileNextBtn.setEnabled(false);
				//add file
				FileDialog dialog = new FileDialog(view.getShell(),SWT.OPEN|SWT.MULTI);
				//�t�@�C���C���q
//				String[] filters = new String[]{"*.zip;*.jar;*.war;*.ear;*.class"};
				dialog.setFilterExtensions(
						new String[]{"*.zip;*.jar;*.class;*.java"});
				String currentPath = SettingManager.getSetting().getCurrentFilePath();
				if ( currentPath != null){
					dialog.setFilterPath(currentPath);
				}

				if (dialog.open() != null){
					SettingManager.getSetting().setCurrentFilePath(dialog.getFilterPath());
					String[] fileNames = dialog.getFileNames();
					for(int i = 0;i < fileNames.length;i++){
						fileNames[i] = dialog.getFilterPath() + File.separator + fileNames[i];
					}
					SettingManager.getSetting().addFilePaths(fileNames);
					updateFileList();
				}

			}
		});
        addFolderBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(view.getShell());
				if (SettingManager.getSetting().getCurrentFolderPath() != null){
					dialog.setFilterPath(SettingManager.getSetting().getCurrentFolderPath());
				}
				String path = dialog.open();
				if (path != null){
					SettingManager.getSetting().setCurrentFolderPath(path);
					SettingManager.getSetting().addFolderPath(path);
					updateFileList();
				}
			}
		});
        removeFileBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int[] indices;
				indices = fileList.getSelectionIndices();
				if (indices.length > 0){
					SettingManager.getSetting().removeFiles(indices);
					fileList.remove(indices);
				}
				removeFileBtn.setEnabled(false);
				filePrevBtn.setEnabled(false);
				fileNextBtn.setEnabled(false);
			}
		});
        filePrevBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setChangedList(fileList,-1);
				setUpperDownButtonState(fileList,filePrevBtn,fileNextBtn);
				saveChangeSourceClassArrList();
				fileList.setFocus();
				removeFileBtn.setEnabled(true);
			}
		});
        fileNextBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setChangedList(fileList,1);
				setUpperDownButtonState(fileList,filePrevBtn,fileNextBtn);
				saveChangeSourceClassArrList();
				fileList.setFocus();
				removeFileBtn.setEnabled(true);
			}
		});
		priorityPrevBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setChangedList(priorityList,-1);
				setUpperDownButtonState(priorityList,priorityPrevBtn,priorityNextBtn);
				savePriority();
			}
		});
        priorityNextBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setChangedList(priorityList,1);
				setUpperDownButtonState(priorityList,priorityPrevBtn,priorityNextBtn);
				savePriority();
			}
		});
	}

	private void init(){
		fileList.removeAll();
		priorityList.removeAll();
        ArrayList<String> list = SettingManager.getSetting().getPathList();
        for (int i=0; i<list.size(); i++){
        	fileList.add(list.get(i));
        }
        ArrayList<String> priority = SettingManager.getSetting().getPriorityInfo();
        for (int i=0; i<priority.size(); i++){
        	priorityList.add(
        			getPriorityItemText(priority.get(i)));
        }
        encodingSelect = SettingManager.getSetting().getEncoding();
    	encodingCombo.setText(encodingSelect);
		priorityPrevBtn.setEnabled(false);
        priorityNextBtn.setEnabled(false);
	}

	private void updateFileList(){
		fileList.removeAll();
		ArrayList<String> list = SettingManager.getSetting().getPathList();
		for (int i=0; i<list.size();i++){
			fileList.add(list.get(i));
		}
	}

	private void saveChangeSourceClassArrList() {
		//fileList.removeAll();
		ArrayList<String> list = SettingManager.getSetting().getPathList();
		list.clear();
		for(int i = 0; i < fileList.getItems().length; i++){
			String path = fileList.getItem(i);
			list.add(path);
		}
	}

	/**
	 * �t�@�C���I���`����\������{�^��
	 * @param group
	 */
	private void setPriorityButtons(Group group){
		Composite right_c = new Composite(group,SWT.NONE);
        GridData right_data = new GridData(GridData.FILL_HORIZONTAL);
        right_data.widthHint = 10;
        right_c.setLayoutData(right_data);
        GridLayout right_layout = new GridLayout(1, false);
        right_c.setLayout(right_layout);
		priorityPrevBtn = Utility.createButton(right_c, SWT.PUSH, ApplicationResource.getResource("SourcePathSettingView.PrevBtn"),
				Utility.BUTTON_WIDTH,1);

        priorityNextBtn = Utility.createButton(right_c, SWT.PUSH, ApplicationResource.getResource("SourcePathSettingView.nextBtn"),
        		Utility.BUTTON_WIDTH,1);
	}

	/**
	 * �{�^��������Ԃ̐ݒ�
	 * (�O������Ă�)
	 */
	public void setButtonInitialState(){
		removeFileBtn.setEnabled(false);
		filePrevBtn.setEnabled(false);
		fileNextBtn.setEnabled(false);

		priorityPrevBtn.setEnabled(false);
		priorityNextBtn.setEnabled(false);
	}


	/**
	 * �t�@�C���I���`����\�����郊�X�g����ݒ肷��B
	 * @param priorities
	 * @return
	 * @return
	 */
//	private void setPriorityInfo(ArrayList priorities){
//		for (int i=0; i<priorities.size(); i++){
//        	String id = priorities.get(i).toString();
//        	String priority = getPriorityItemText(id);
//        	priorityList.add(priority);
//        }
//	}

	/**
	 * ���X�g�ɗv�f���I������Ă��邩�ǂ����𔻒f
	 * @param list
	 * @return
	 */
	private boolean hasSelection(List list){
		int[] selectionIndexs = list.getSelectionIndices();
		if(selectionIndexs == null || selectionIndexs.length == 0){
			return false;
		}
		return true;
	}

	/**
	 * �u��ցv�Ɓu���ցv�{�^���̏�Ԃ�ݒ肷��B
	 * @param list �Ώۃ��X�g
	 * @param revBtn ��ւ̃{�^��
	 * @param nextBtn ���ւ̃{�^��
	 * @return �Ȃ�
	 */
	private void setUpperDownButtonState(List list, Button revBtn, Button nextBtn){
		revBtn.setEnabled(true);
		nextBtn.setEnabled(true);
		int lines = list.getItemCount();
		int[] selectedLines =list.getSelectionIndices();
		for (int i=0; i<selectedLines.length; i++){
			if (selectedLines[i]==0){
				revBtn.setEnabled(false);
			}
			if (selectedLines[i]==lines-1){
				nextBtn.setEnabled(false);
			}
		}
	}

	/**
	 * �t�@�C���I�����X�g�̏��̕ύX��ۑ�����B
	 * @param �Ȃ�
	 */
	private void savePriority(){
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < priorityList.getItems().length; i++){
			String id = getPriorityItemId(priorityList.getItem(i));
			list.add(id);
		}
		SettingManager.getSetting().setPriority(list);
	}

	/**
	 * �ύX�����t�@�C���I�����X�g��ݒ肷��B
	 * @param move  -1�F�㏸/�P�F�~��
	 */
	private void setChangedList(List list,int move){
		int[] selectionIndexs = list.getSelectionIndices();
		//change [select] and [another]
		for (int i=0; i<selectionIndexs.length; i++){
			String select = list.getItem(selectionIndexs[i]);
			String another = list.getItem(selectionIndexs[i]+move);
			list.setItem(selectionIndexs[i], another);
			list.setItem(selectionIndexs[i]+move, select);
		}
		//set selection
		for (int i=0; i<selectionIndexs.length; i++){
			selectionIndexs[i]=selectionIndexs[i]+move;
		}
		list.setSelection(selectionIndexs);
	}


	/**
	 * don't use.
	 * @param arg0
	 */
	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	/**
	 * �L�����Z���O����
	 */
	public boolean preCancelProcessed() {
		return true;
	}

	/**
	 * �m��O����
	 */
	public boolean preOkProcessed() {
		return true;
	}

	/**
	 * �u�\�[�X���N���X�p�X�v�^�O�̏����t�@�C���ɃG�N�X�|�[�g����B
	 * @param lines
	 * @param fileFlagArr
	 */
	public void getSourcePathInfo(ArrayList<String> lines, ArrayList<String> fileFlagArr) {
		getListInfo(lines,fileList,SettingDialog.FILE_SOURCE,fileFlagArr);
		getListInfo(lines,priorityList,SettingDialog.FILE_PRIORITY,fileFlagArr);
		getEncodingInfo(lines,encodingCombo,SettingDialog.SOURCE_ENCODING,fileFlagArr);
	}

	/**
	 * �u�\�[�X���N���X�p�X�v�^�O���̃��X�g�̏����擾����B
	 * @param lines
	 * @param objlist
	 * @param infoFlag
	 * @param fileFlagArr
	 */
	private void getListInfo(ArrayList<String> lines, List objlist, String infoFlag,
			ArrayList<String> fileFlagArr) {
		for (int i = 0; i < objlist.getItems().length; i++) {
			String str = objlist.getItem(i);
			fileFlagArr.add(infoFlag);
			if (!SettingDialog.FILE_PRIORITY.equals(infoFlag)) {
				lines.add(str);
			} else {
				String strListInfo = getPriorityItemId(str);
				lines.add(strListInfo);
			}
		}
	}

	/**
	 * �u�\�[�X���N���X�p�X�v�^�O���́u�D�揇�v���X�g�̓��e��Id�ɕϊ�����B
	 * @param str
	 */
	private String getPriorityItemId(String str) {
		String priority = null;
		if(str.equals(ApplicationResource.getResource("SettingDialog.priorityOrder.list.localsource"))){
			priority = Setting.FILE_PRIORITY[0];
		}else if(str.equals(ApplicationResource.getResource("SettingDialog.priorityOrder.list.localclass"))){
			priority = Setting.FILE_PRIORITY[1];
		}else if(str.equals(ApplicationResource.getResource("SettingDialog.priorityOrder.list.svnsource"))){
			priority = Setting.FILE_PRIORITY[2];
		}
		return priority;
	}
	/**
	 * �u�D�揇�v��Id���u�\�[�X���N���X�p�X�v�^�O���́u�D�揇�v���X�g�̓��e�ɕϊ�����B
	 * @param str
	 */
	private String getPriorityItemText(String str) {
		String priority = null;
    	if(str.equals(Setting.FILE_PRIORITY[0])){
    		priority = ApplicationResource.getResource("SettingDialog.priorityOrder.list.localsource");
    	}else if(str.equals(Setting.FILE_PRIORITY[1])){
    		priority = ApplicationResource.getResource("SettingDialog.priorityOrder.list.localclass");
    	}else if(str.equals(Setting.FILE_PRIORITY[2])){
    		priority = ApplicationResource.getResource("SettingDialog.priorityOrder.list.svnsource");
    	}
		return priority;
	}
	/**
	 * �\�[�X�R�[�h��Enconding�ݒ�̑I�������A�C�e�����擾����B
	 * @param lines
	 * @param objlist
	 * @param infoFlag
	 * @param fileFlagArr
	 */
	private void getEncodingInfo(ArrayList<String> lines, Combo objlist,String infoFlag,ArrayList<String> fileFlagArr){
		String str = encodingCombo.getItem(encodingCombo.getSelectionIndex());
		fileFlagArr.add(infoFlag);
		lines.add(str);
	}

	/**
	 * �L�����Z���㏈��
	 */
	public void postCancelProcessed() {

	}

	/**
	 * �m��㏈��
	 */
	public void postOkProcessed() {
	}

	public void refresh() {
		init();
	}
}
