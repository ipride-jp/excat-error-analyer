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

import jp.co.ipride.excat.common.ApplicationResource;
import jp.co.ipride.excat.common.Message;
import jp.co.ipride.excat.common.utility.ExcatMessageUtilty;
import jp.co.ipride.excat.common.utility.Utility;
import jp.co.ipride.excat.configeditor.ExcatText;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * ���[�g�p�X�ҏW/�ǉ��_�C�A���O
 *
 * @author tatebayashiy
 *
 */
public class RootPathEditDialog extends Dialog implements SelectionListener {

	private ExcatText nameText;
	private ExcatText pathText;
	private Button pathSelectButton;
	private Button okButton;
	private Button cancelButton;

	private RootPathItem rootPathItem;
	private String[] registedNames;

	private boolean additionalMode;

	/**
	 * �R���X�g���N�^
	 *
	 * @param parentShell
	 *            �e�V�F��
	 * @param additionalMode
	 *            �ǉ����[�h�ŕ\�����邩�ǂ���
	 * @param registedNames
	 *            �o�^�ς݂̖��̂̔z��
	 */
	protected RootPathEditDialog(Shell parentShell, boolean additionalMode,
			String[] registedNames) {
		super(parentShell);

		this.additionalMode = additionalMode;
		this.registedNames = registedNames;
	}

	/**
	 * �R���X�g���N�^
	 *
	 * @param parentShell
	 *            �e�V�F��
	 * @param additionalMode
	 *            �ǉ����[�h�ŕ\�����邩�ǂ���
	 * @param defaultItem
	 *            �����\���̐ݒ�A�C�e��
	 * @param registedNames
	 *            �o�^�ς݂̖��̂̔z��
	 */
	protected RootPathEditDialog(Shell parentShell, boolean additionalMode,
			RootPathItem defaultItem, String[] registedNames) {
		this(parentShell, additionalMode, registedNames);
		rootPathItem = defaultItem;
	}

	/**
	 * �_�C�A���O�̃{�^���̈�̃{�^���𐶐�����B
	 */
	protected void createButtons(Composite parent) {
		Composite buttonSite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(4, false);
		buttonSite.setLayout(layout);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		buttonSite.setLayoutData(gd);

		okButton = Utility.createButton(
				buttonSite,
				SWT.PUSH,
				ApplicationResource.getResource("RootPathEditDialog.OKButton.Text"),
				Utility.BUTTON_WIDTH,1);
		okButton.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
			public void widgetSelected(SelectionEvent arg0) {
				okPressed();
			}
		});
		cancelButton = Utility.createButton(
				buttonSite,
				SWT.PUSH,
				ApplicationResource.getResource("RootPathEditDialog.CancelButtonl.Text"),
				Utility.BUTTON_WIDTH,1);
		cancelButton.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
			public void widgetSelected(SelectionEvent arg0) {
				cancelPressed();
			}
		});

	}

	/**
	 * �_�C�A���O�̃R���e���c�𐶐����� override method.
	 *
	 */
	protected Control createContents(Composite parent) {

//		Control contents = super.createContents(parent);

		if (additionalMode) {
			this.getShell().setText(ApplicationResource
					.getResource("RootPathEditDialog.Add.ShellText"));
		} else {
			this.getShell().setText(ApplicationResource
					.getResource("RootPathEditDialog.Edit.ShellText"));
		}

		// set dialog area.
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		composite.setLayout(layout);
		GridData bodyData = new GridData(GridData.FILL_VERTICAL);
		composite.setLayoutData(bodyData);

		//set text box�B
		createPathSettingArea(composite);

		createButtons(composite);

		initializeData();

		return composite;
	}

	/**
	 * OK�{�^���������̏���
	 *
	 * RootPathItem�̐ݒ�
	 *
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		String rootName = nameText.getText();
		while (rootName.length() > 0)
		{
			char ch = rootName.charAt(0);
			if (ch == ' '  || ch == '�@')
			{
				rootName = rootName.substring(1);
			}
			else
			{
				break;
			}
		}
		while (rootName.length() > 0)
		{
			char ch = rootName.charAt(rootName.length()-1);
			if (ch == ' '  || ch == '�@')
			{
				rootName = rootName.substring(0, rootName.length()-1);
			}
			else
			{
				break;
			}
		}
		nameText.setText(rootName);

		if (rootName.length() == 0)
		{
			ExcatMessageUtilty.showMessage(
					this.getShell(),
					Message.get("RootPathEditDialog.NameIsSpace"));
			nameText.setText("");
			nameText.setFocus();
			return;
		}

		if (rootPathItem == null) {
			rootPathItem = new RootPathItem();
		}
		if (additionalMode) {
			if (registedNames != null) {
				for (int i = 0; i < registedNames.length; i++) {
					if (registedNames[i].equals(nameText.getText())) {
						ExcatMessageUtilty.showMessage(
								this.getShell(),
								Message.get("RootPathEditDialog.Registed"));
						return;
					}
				}
			}
		}

		File directoryPath = new File(pathText.getText());
		if (!directoryPath.exists()) {
			ExcatMessageUtilty.showMessage(
					this.getShell(),
					Message.get("RootPathEditDialog.PathNotFound"));
			pathText.setFocus();
			pathText.setSelection(0, pathText.getText().length());
			return;
		}
		if (directoryPath.isFile()) {
			ExcatMessageUtilty.showMessage(
					this.getShell(),
					Message.get("RootPathEditDialog.PathNotFound"));
			pathText.setFocus();
			pathText.setSelection(0, pathText.getText().length());
			return;
		}

		rootPathItem.setName(nameText.getText());
		rootPathItem.setPath(pathText.getText());

		super.okPressed();
	}

	/**
	 * �p�X�ǉ��ҏW�̈�𐶐��B
	 *
	 * @param parent
	 */
	protected void createPathSettingArea(Composite parent) {
		Composite area = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout(3, false);
		area.setLayout(layout);

		// ���O
		Label label = new Label(area, SWT.NONE);
		label.setText(ApplicationResource
				.getResource("RootPathEditDialog.NameLabel.Text"));

		nameText = new ExcatText(area, SWT.SINGLE | SWT.BORDER);
		GridData layoutData = new GridData(GridData.BEGINNING);
		layoutData.widthHint = 100;
		layoutData.horizontalSpan = 2;
		nameText.setLayoutData(layoutData);
		nameText.setEditable(additionalMode);
		nameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (nameText.getText() == null || "".equals(nameText.getText())
						|| pathText.getText() == null
						|| "".equals(pathText.getText())) {
					okButton.setEnabled(false);
				} else {
					okButton.setEnabled(true);
				}

			}
		});

		// ���[�g�p�X
		label = new Label(area, SWT.NONE);
		label.setText(ApplicationResource
				.getResource("RootPathEditDialog.PathLabel.Text"));

		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.widthHint = 300;
		pathText = new ExcatText(area, SWT.SINGLE | SWT.BORDER);
		pathText.setLayoutData(layoutData);
		pathText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (nameText.getText() == null || "".equals(nameText.getText())
						|| pathText.getText() == null
						|| "".equals(pathText.getText())) {
					okButton.setEnabled(false);
				} else {
					okButton.setEnabled(true);
				}

			}
		});

		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		pathSelectButton = new Button(area, SWT.PUSH);
		pathSelectButton.setText(ApplicationResource
				.getResource("RootPathEditDialog.PathSelectButton.Text"));
		layoutData = new GridData();
		layoutData.widthHint=Utility.BUTTON_WIDTH;
		pathSelectButton.setLayoutData(layoutData);
		pathSelectButton.addSelectionListener(this);

		if (!additionalMode) {
			pathText.setFocus();
		}
	}

	/**
	 * �����f�[�^��ݒ�
	 */
	protected void initializeData() {
		if (rootPathItem != null) {
			nameText.setText(rootPathItem.getName());
			pathText.setText(rootPathItem.getPath());
		}
	}

	/**
	 * ���[�g�p�X�A�C�e�����擾���܂��B
	 *
	 * @return the rootPathItem
	 */
	public RootPathItem getRootPathItem() {
		return rootPathItem;
	}

	/**
	 * ���[�g�p�X�A�C�e����ݒ肵�܂��B
	 *
	 * @param pathItem
	 *            the rootPathItem to set
	 */
	public void setRootPathItem(RootPathItem pathItem) {
		rootPathItem = pathItem;
	}

	/**
	 * �I���C�x���g����
	 */
	public void widgetSelected(SelectionEvent event) {
		// �I���{�^���I��������
		if (event.getSource() == pathSelectButton) {
			DirectoryDialog dialog = new DirectoryDialog(this.getShell());
			String path = dialog.open();
			if (path != null) {
				pathText.setText(path);
			}
		}
	}

	/**
	 * ������
	 */
	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

}
