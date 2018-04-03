/*
 * Error Analyzer Tool for Java
 *
 * Created on 2007/10/9
 *
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.analyzer.action.finder;

import jp.co.ipride.excat.analyzer.action.finder.FindCondition;
import jp.co.ipride.excat.analyzer.action.finder.IFindProvider;
import jp.co.ipride.excat.common.ApplicationResource;
import jp.co.ipride.excat.common.Message;
import jp.co.ipride.excat.common.utility.ExcatMessageUtilty;
import jp.co.ipride.excat.common.utility.Utility;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * �����_�C�A���O
 *
 * �{�_�C�A���O�̓��[�h���X�ȃ_�C�A���O�Ƃ��ĕ\������܂��B
 *
 * @author tatebayashi
 */
public class FindDialog extends Dialog {

	private IFindProvider findProvider;
	private final static int MAX_HISTORY = 10;

	private Combo findStringCombo;
	private Button dirNextButton;
	private Button dirPrevButton;
	private Button optCaseSensitive;
	private Button optCircularSearch;
	private Button optWordSearch;
	private Button findButton;
	private Button closeButton;

	private String[] _findStringHistory = {};
	private FindCondition _condition = new FindCondition("", true, false,
			false, false, false);

	/**
	 * �R���X�g���N�^
	 *
	 * @param arg0
	 */
	public FindDialog(Shell arg0) {
		super(arg0);
		// ���[�h���X�_�C�A���O�Ƃ��Ďw��
		this.setShellStyle(SWT.DIALOG_TRIM | SWT.MODELESS);
	}

	/**
	 * �_�C�A���O�̓��e�𐶐����܂��B
	 */
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);

		// �_�C�A���O�̃^�C�g���o�[��ݒ�
		String text = ApplicationResource.getResource("FindDialog.ShellText");
		this.getShell().setText(text);

		// �_�C�A���O�̗̈�̃��C�A�E�g�}�l�[�W�����w��B
		Composite area = (Composite) this.getDialogArea();
		GridLayout layout = new GridLayout(1, false);
		area.setLayout(layout);

		GridData bodyData = new GridData(GridData.FILL_VERTICAL);
		area.setLayoutData(bodyData);

		// �����񌟍������w��
		createSearchArea(area);

		// ��������
		createDirectionArea(area);

		// �����I�v�V����
		createOptionArea(area);

		return contents;
	}

	/**
	 * ����������̈�𐶐����܂��B
	 *
	 * @param parent
	 */
	private void createSearchArea(Composite parent) {
		Composite searchArea = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		searchArea.setLayout(layout);

		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		searchArea.setLayoutData(data);

		Label label = new Label(searchArea, SWT.NONE);
		label.setText(ApplicationResource
				.getResource("FindDialog.FindLabelText"));
		findStringCombo = new Combo(searchArea, SWT.DROP_DOWN);
		GridData searchComboData = new GridData();
		searchComboData.widthHint = 260;
		findStringCombo.setLayoutData(searchComboData);
		findStringCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				Combo source = (Combo) arg0.getSource();
				if (source.getText() == null || "".equals(source.getText())) {
					findButton.setEnabled(false);
				} else {
					findButton.setEnabled(true);
				}
			}
		});
		findStringCombo.setItems(_findStringHistory);
		findStringCombo.setText(_condition.getTargetString());
	}

	/**
	 * ���������̈�𐶐����܂��B
	 *
	 * @param parent
	 */
	private void createDirectionArea(Composite parent) {
		GridLayout layout = new GridLayout(1, false);
		Group group = new Group(parent, SWT.NONE);
		group.setText(ApplicationResource
				.getResource("FindDialog.DirectionGroup.Text"));
		GridData data = new GridData(GridData.FILL_VERTICAL);
		group.setLayoutData(data);
		group.setLayout(layout);

		dirNextButton = new Button(group, SWT.RADIO);
		dirNextButton.setText(ApplicationResource
				.getResource("FindDialog.DirectionNext.Text"));
		dirNextButton.setSelection(_condition.isForwardSearch());

		dirPrevButton = new Button(group, SWT.RADIO);
		dirPrevButton.setText(ApplicationResource
				.getResource("FindDialog.DirectionPrev.Text"));
		dirPrevButton.setSelection(!_condition.isForwardSearch());
	}

	/**
	 * �I�v�V�����ݒ�̈�𐶐����܂��B
	 *
	 * @param parent
	 */
	private void createOptionArea(Composite parent) {
		GridLayout layout = new GridLayout(2, false);
		Group group = new Group(parent, SWT.NONE);
		group.setText(ApplicationResource
				.getResource("FindDialog.OptionGroup.Text"));
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(data);
		group.setLayout(layout);

		optCaseSensitive = new Button(group, SWT.CHECK);
		optCaseSensitive.setText(ApplicationResource
				.getResource("FindDialog.Option.CaseSensitive.Text"));
		optCaseSensitive.setSelection(_condition.isCaseSensitive());

		optCircularSearch = new Button(group, SWT.CHECK);
		optCircularSearch.setText(ApplicationResource
				.getResource("FindDialog.Option.CircularSearch.Text"));
		optCircularSearch.setSelection(_condition.isCircularSearch());

		optWordSearch = new Button(group, SWT.CHECK);
		optWordSearch.setText(ApplicationResource
				.getResource("FindDialog.Option.WordSearch.Text"));
		optWordSearch.setSelection(_condition.isWordSearch());
	}

	/**
	 * �{�^���o�[�̈�𐶐����܂��B
	 */
	protected Control createButtonBar(Composite arg0) {
		Composite buttonsArea = new Composite(arg0, SWT.NONE);
		GridLayout layout = new GridLayout(4, false);
		buttonsArea.setLayout(layout);

		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		buttonsArea.setLayoutData(data);

		findButton = Utility.createButton(buttonsArea, SWT.NULL,
				ApplicationResource.getResource("FindDialog.Find.Text"), 100,1);
		findButton.addSelectionListener(new FindButtonSelectinListener());
		closeButton = Utility.createButton(buttonsArea, SWT.NULL,
				ApplicationResource.getResource("FindDialog.Close.Text"), 100,1);
		closeButton
				.addSelectionListener(new CloseButtonSelectionListener(this));
		getShell().setDefaultButton(findButton);
		return buttonsArea;
	}

	/**
	 * ���͂��ꂽ�����������擾���܂��B
	 */
	public FindCondition getFindCondition() {
		// �\�����̏ꍇ��x�t�B�[���h�֕ۑ�
		if (this.getShell() != null) {
			saveFindData();
		}
		return _condition;
	}

	/**
	 * �w�肳�ꂽ����������UI�ɐݒ肵�܂��B
	 *
	 * @param condition
	 */
	public void setFindCondition(FindCondition condition) {
		findStringCombo.setText(condition.getTargetString());
		dirNextButton.setSelection(condition.isForwardSearch());
		optCaseSensitive.setSelection(condition.isCaseSensitive());
		optCircularSearch.setSelection(condition.isCircularSearch());
		optWordSearch.setSelection(condition.isWordSearch());
	}

	/**
	 * �_�C�A���O�\�����Ɍ����������ݒ肵�܂��B
	 *
	 * @param targetStr
	 */
	public void setFindString(String targetStr) {
		if (this.getShell() != null) {
			// �\�����͒��ڐݒ肷��B
			findStringCombo.setText(targetStr);

		}
		_condition.setTargetString(targetStr);
	}

	/**
	 * �����ݒ���e��ێ����܂��B
	 *
	 * ����\�����ɕ��������悤�ɁA���e��<code>_initializeCondition</code>�ɕێ����܂��B
	 */
	private void saveFindData() {
		String findStr = findStringCombo.getText();
		_condition.setTargetString(findStr);
		if (!("".equals(findStr))) {
			// �����ɓ���̕����񂪑��݂���ꍇ�͍폜
			for (int i = 0; i < findStringCombo.getItemCount(); i++) {
				if (findStringCombo.getItem(i).equals(findStr)) {
					findStringCombo.remove(i);
					break;
				}
			}

			// �o�^����Ă��闚��������𒴂��Ă���ꍇ�͖������폜����
			if (findStringCombo.getItemCount() >= MAX_HISTORY) {
				findStringCombo.remove(MAX_HISTORY - 1);
			}
			// �����̐擪�ɒǉ�
			findStringCombo.add(findStr, 0);
			// ��������I������������ł������ꍇ�A���X�g����폜�����Ƌ�ƂȂ��Ă��܂����߁A
			// �e�L�X�g�ɍ����߂��B
			findStringCombo.setText(findStr);
		}

		// ����\�����ɐݒ肪���f�����悤�����l�ێ��Ƀt�B�[���h���X�V����
		_findStringHistory = findStringCombo.getItems();
		_condition.setForwardSearch(dirNextButton.getSelection());
		_condition.setCaseSensitive(optCaseSensitive.getSelection());
		_condition.setCircularSearch(optCircularSearch.getSelection());
		_condition.setWordSearch(optWordSearch.getSelection());
	}

	/**
	 * ���������̒񋟌��I�u�W�F�N�g���擾���܂��B
	 *
	 * @param provider
	 */
	public void setFindProvider(IFindProvider provider) {
		findProvider = provider;
	}

	/**
	 * ���������̒񋟌��I�u�W�F�N�g��ݒ肵�܂��B
	 *
	 * @param provider
	 */
	public IFindProvider getFindProvider() {
		return findProvider;
	}

	/**
	 * �����{�^���̗��p�ۂ�ݒ肵�܂��B
	 *
	 * @param enable
	 */
	public void setFindButtonEnable(boolean enable) {
		if (this.getShell() != null) {
			findButton.setEnabled(enable);
		}
	}

	/**
	 * �����{�^���I�����X�i�[
	 *
	 * @author tatebayashi
	 *
	 */
	private class FindButtonSelectinListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent arg0) {
			saveFindData();
			// �����������s���܂��B
			if (findProvider != null) {
				boolean found = findProvider.find(getFindCondition());
				if (!found) {
//					ExcatMessageUtilty.showMessage(this.getShell(),Message.get("FindString.Info.NotFound"));
				}
			}
		}
	}

	/**
	 * ����{�^���I�����X�i�[
	 *
	 * @author tatebayashi
	 *
	 */
	private class CloseButtonSelectionListener extends SelectionAdapter {
		private Dialog dialog;

		public CloseButtonSelectionListener(Dialog d) {
			dialog = d;
		}

		public void widgetSelected(SelectionEvent arg0) {
			// �_�C�A���O����܂��B
			saveFindData();
			dialog.close();
		}
	}

	/**
	 * �_�C�A���O��������ۂ̃C�x���g����
	 *
	 * @author tatebayashi
	 *
	 */
	protected void handleShellCloseEvent() {
		saveFindData();
		super.handleShellCloseEvent();
	}

}
