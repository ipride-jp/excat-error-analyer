/*
 * Error Analyzer Tool for Java
 *
 * Created on 2007/10/9
 *
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.common.setting.dialog;



import jp.co.ipride.excat.common.ApplicationResource;
import jp.co.ipride.excat.common.Message;
import jp.co.ipride.excat.common.setting.SettingManager;
import jp.co.ipride.excat.common.utility.ExcatMessageUtilty;
import jp.co.ipride.excat.common.utility.Utility;
import jp.co.ipride.excat.configeditor.util.ViewerUtil;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * �ݒ�_�C�A���O
 * @author tatebayashi
 *
 */
public class SettingDialog extends Dialog {

	/** �^�u�t�H���_ */
	private CTabFolder tabFolder;
	/** �C���|�[�g */
	private Button importBtn;
	/** �G�N�X�|�[�g */
	private Button exportBtn;

	private Button saveBtn;

	private Button cancelBtn;

	/** �u�\�[�X���N���X�p�X�v�^�O�̃I�u�W�F�N�g */
	private SourcePathSettingView sourcePathSettingView;
	/** �u�_���v�t�@�C���r���[�A�v�^�O�̃I�u�W�F�N�g */
	private RootPathSettingView rootPathSettingView;
	/** �u�\�[�X���|�W�g���v�^�O�̃I�u�W�F�N�g */
	private SourceRepositorySettingView sourceRepositorySettingView;

	/** �u�\�[�X�t�H���_�v�̃t���O */
	public static final String FILE_SOURCE = "File.Source";
	/** �u�N���X�p�X�v�̃t���O */
	public static final String FILE_CLASS = "File.Class";
	/** �u�D�揇�v�̃t���O */
	public static final String FILE_PRIORITY = "File.Priority";
	/** �u�\�[�X�R�[�h��Enconding�ݒ�v�̃t���O */
	public static final String SOURCE_ENCODING = "File.Encoding";
	/** �u���[�g�p�X�ꗗ�̖��O�v�̃t���O */
	public static final String DUMP_FILEVIEW_NAMEPATH = "Dump.Fileview.name";
	/** �u���[�g�p�X�ꗗ�̃��[�g�p�X�v�̃t���O */
	public static final String DUMP_FILEVIEW_ROOTPATH = "Dump.Fileview.rootPath";
	/** �u�\������g���q�v�̃t���O */
	public static final String DUMP_FILEVIEW_EXTENSION = "Dump.Fileview.Extension";
	/** �u�\�[�X���|�W�g��URL�v�̃t���O */
	public static final String SOURCE_REPOSITORY_URL = "Source.Repository.URL";
	/** �u�A�J�E���g�v�̃t���O */
	public static final String SOURCE_REPOSITORY_ACCOUNT = "Source.Repository.Account";
	/** �u�p�X���[�h�v�̃t���O */
	public static final String SOURCE_REPOSITORY_PASSWORD = "Source.Repository.Password";
	/** �u��ƃt�H���_�v�̃t���O */
	public static final String SOURCE_REPOSITORY_FOLDER = "Source.Repository.Folder";

	/**
	 * �R���X�g���N�^
	 * @param parentShell �{�_�C�A���O�̐e�ƂȂ�Shell
	 */
	public SettingDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * �_�C�A���O�̃R���e���c�𐶐����܂��B
	 * override method.
	 *
	 */
	protected Control createContents(Composite parent) {
		this.getShell().setText(
				ApplicationResource.getResource("SettingDialog.ShellText"));

		//set dialog area.
		Composite area = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		area.setLayout(layout);
		layout.marginLeft=ViewerUtil.getMarginWidth(area);
		layout.marginRight=ViewerUtil.getMarginWidth(area);
		layout.marginTop=ViewerUtil.getMarginHeight(area);
		layout.marginBottom=ViewerUtil.getMarginHeight(area);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        area.setLayoutData(gridData);

        createTabFolder(area);
        createTabItems();
        createButtons(area);
        addListeners();

		return area;
	}

	/**
	 * �^�u�t�H���_�[�𐶐����܂��B
	 * @param parent
	 */
	protected void createTabFolder(Composite parent) {
		tabFolder = new CTabFolder(parent, SWT.BORDER );
		tabFolder.setTabHeight(24);
		tabFolder.setSelectionBackground(
		  new Color[]{
				  parent.getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND),
				  parent.getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT)},
		  new int[] {90},
		  true
		);
		tabFolder.setSelectionForeground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
	}

	/**
	 * �^�u�A�C�e���𐶐����܂��B
	 *
	 * �^�u�A�C�e���ɐݒ肷��R���g���[���N���X�͕K���AISettingView�����N���X�ł���K�v������܂��B
	 */
	protected void createTabItems() {
		CTabItem item;

		// �\�[�X�p�X�ݒ�r���[�̃A�C�e���ǉ�
		item = new CTabItem(tabFolder, SWT.NONE);
		item.setText(
				ApplicationResource.getResource("SourcePathSettingView.Tab.Text"));
		sourcePathSettingView= new SourcePathSettingView(tabFolder, SWT.None);
		item.setControl(sourcePathSettingView);

		// �ۊǃ��[�g�p�X�ݒ�r���[�̃A�C�e���ǉ�
		item = new CTabItem(tabFolder, SWT.NONE);
		item.setText(
				ApplicationResource.getResource("RootPathSettingView.Tab.Text"));
		rootPathSettingView = new RootPathSettingView(tabFolder, SWT.NONE);
		item.setControl(rootPathSettingView);
		// �\�[�X���|�W�g���ݒ�r���[�̃A�C�e���ǉ�
		item = new CTabItem(tabFolder, SWT.NONE);
		item.setText(
				ApplicationResource.getResource("SourceRepositorySettingView.Tab.Text"));
		sourceRepositorySettingView = new SourceRepositorySettingView(tabFolder, SWT.NONE);
		item.setControl(sourceRepositorySettingView);
	}

	private void createButtons(Composite composite){
		Composite buttonSite = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout(4, false);
		buttonSite.setLayout(layout);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		buttonSite.setLayoutData(gd);

		importBtn = Utility.createButton(buttonSite, SWT.PUSH,
				ApplicationResource.getResource("SettingDialog.ImportButton"),
				Utility.BUTTON_WIDTH,1);
		exportBtn = Utility.createButton(buttonSite, SWT.PUSH,
				ApplicationResource.getResource("SettingDialog.ExportButton"),
				Utility.BUTTON_WIDTH,1);

		saveBtn = Utility.createButton(buttonSite, SWT.PUSH,
				ApplicationResource.getResource("Dialog.Button.Enter.Text"),
				Utility.BUTTON_WIDTH,1);

		cancelBtn = Utility.createButton(buttonSite, SWT.PUSH,
				ApplicationResource.getResource("Dialog.Button.Cancel.Text"),
				Utility.BUTTON_WIDTH,1);
	}

	private void addListeners(){
		importBtn.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
				dialog.setFilterExtensions(new String[] { "*.setting" });
				String path = SettingManager.getSetting().getCurrentImportFilePath();
				if (path != null) {
					dialog.setFilterPath(path);
				}
				String inputPath = dialog.open();
				if (inputPath != null) {
					SettingManager.inputSetting(inputPath);
					sourcePathSettingView.refresh();
					rootPathSettingView.refresh();
					sourceRepositorySettingView.refresh();
				}
			}
		});
		exportBtn.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
				dialog.setFilterExtensions(new String[] { "*.setting" });
				String path = SettingManager.getSetting().getCurrentImportFilePath();
				if (path != null) {
					dialog.setFilterPath(path);
				}
				String outputPath = dialog.open();
				if (outputPath != null) {
					SettingManager.outputSetting(outputPath);
				}
			}
		});
		cancelBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				cancelProcess();
			}
		});
		saveBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				okProcess();
			}
		});
	}


	/**
	 * �m��{�^���I�����̏���
	 *
	 * �ݒ�𔽉f���܂��B
	 */
	protected void okProcess() {
		// �e�r���[�ʂ̊m��O�������s���܂��B
		for(int i=0;i<tabFolder.getItemCount();i++) {
			SettingViewListener view = (SettingViewListener)tabFolder.getItem(i).getControl();
			if(!view.preOkProcessed()) {
				// �Ώۂ̃r���[��I��
				tabFolder.setSelection(i);
			}
		}

		super.okPressed();
	}

	/**
	 * �L�����Z���{�^���I�����̏���
	 *
	 * �ݒ���m�肷�邩�ǂ����̖₢���킹���s���A�ݒ�̊m��܂��̓L�����Z�������s���܂��B
	 */
	protected void cancelProcess(){
//		// �e�r���[�ʂ̃L�����Z���O�������s���܂��B
//		for(int i=0;i<tabFolder.getItemCount();i++) {
//			SettingViewListener view = (SettingViewListener)tabFolder.getItem(i).getControl();
//			if(!view.preCancelProcessed()) {
//				// false���ԋp���ꂽ�ꍇ�́A���̃^�u��I�����A�m�菈���𒆒f����B
//				tabFolder.setSelection(i);
//				return;
//			}
//		}

		// �L�����Z���{�^�������ɂĐݒ�̕ۑ����m�F����
//		if (SettingManager.isChanged()){
//			MessageBox msgBox = new MessageBox(this.getShell(),SWT.YES|SWT.NO);
//			msgBox.setMessage(ApplicationResource.getResource("SettingDialog.ConfirmToSaveSetting"));
//			msgBox.setText(ApplicationResource.getResource("SettingDialog.ConfirmMessage"));
//			int result = msgBox.open();
//			if (result == SWT.YES){
//				SettingManager.update(true);
//			}else{
//				SettingManager.update(false);
//			}
//		}
		SettingManager.cancelChanges();
		super.cancelPressed();
	}

	/**
	 * �_�C�A���O�����ۂ̏���
	 *
	 * �ݒ���m�肷�邩�ǂ����̖₢���킹���s���A�ݒ�̊m��܂��̓L�����Z�������s���܂��B
	 */
	public boolean close(){
//		if (SettingManager.isChanged()){
//			boolean result = ExcatMessageUtilty.showConfirmDialogBox(
//					this.getShell(),
//					Message.get("SettingDialog.ConfirmToSaveSetting"));
//
//			if (result){
//				SettingManager.update(this.getShell(),true);
//			}else{
//				SettingManager.update(this.getShell(),false);
//			}
//		}
		//add by Qiu Song 20091113 for �o�O#499
		if(getReturnCode() == IDialogConstants.CANCEL_ID){
			SettingManager.cancelChanges();
		}
		//end of add by Qiu Song 20091113 for �o�O#499
		return super.close();
	}
}