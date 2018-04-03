package jp.co.ipride.excat.analyzer.dialog.search;

import jp.co.ipride.excat.MainViewer;
import jp.co.ipride.excat.analyzer.viewer.searchviewer.ConditionUnit;
import jp.co.ipride.excat.common.ApplicationResource;
import jp.co.ipride.excat.common.utility.Utility;

import org.eclipse.jface.dialogs.Dialog;
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

/**
 * �����񌟍�
 * @author tu-ipride
 * @version 3.0
 * @date 2009/10/8
 */
public class TextSearchDialog extends Dialog {

	private MainViewer appWindow;

	/** �^�u�t�H���_ */
	private CTabFolder tabFolder;
	private CTabItem fileViewItem;
	private CTabItem stackViewItem;

	private FileViewSearchTab fileViewSearchTab;
	private StackViewSearchTab stackViewSearchTab;

	private Button runBtn;
	private Button cancelBtn;

	//private ConditionUnit conditionUnit = null;
	private ConditionUnit conditionFileView = null;
	private ConditionUnit conditionStackView = null;

	/**
	 *
	 * @param parentShell
	 */
	public TextSearchDialog(MainViewer appWindow) {
		super(appWindow.getShell());
		this.appWindow = appWindow;
	}

	/**
	 * �_�C�A���O�̃{�^���̈���̃{�^���𐶐����܂��B
	 * override method.
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		GridLayout layout = new GridLayout(4, false);
        parent.setLayout(layout);

		// �L�����Z�����m��{�^���̍쐬
        runBtn = Utility.createButton(parent, SWT.PUSH,
				ApplicationResource.getResource("Search.Dialog.Run"),
				Utility.BUTTON_WIDTH,1);

        cancelBtn = Utility.createButton(parent, SWT.PUSH,
				ApplicationResource.getResource("Search.Dialog.Cancel"),
				Utility.BUTTON_WIDTH,1);

        runBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				updateToMode();
				okPressed();
			}
		});
        runBtn.setEnabled(false);

        cancelBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				cancelPressed();
			}
		});
	}

	/**
	 * �_�C�A���O�̃R���e���c�𐶐����܂��B
	 * override method.
	 *
	 */
	protected Control createContents(Composite parent) {

		Control contents = super.createContents(parent);

		String text = ApplicationResource.getResource("Search.Dialog.Title");
		this.getShell().setText(text);

		//set dialog area.
		Composite area = (Composite)this.getDialogArea();
		GridLayout layout = new GridLayout(1, false);
		area.setLayout(layout);

		// Body Composite
        GridData bodyData = new GridData(GridData.FILL_VERTICAL);
        area.setLayoutData(bodyData);

        // �^�u�t�H���_�[�𐶐����܂��B
        createTabFolder(area);

        // �^�u�A�C�e�����^�u�t�H���_�[���ɐ������܂��B
        createTabItems();

        //cancelBtn.setFocus();

		return contents;
	}

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
		tabFolder.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
				checkRunStatus();
				updateFocus();
			}

			public void widgetSelected(SelectionEvent arg0) {
				checkRunStatus();
				updateFocus();
			}
		});
	}

	/**
	 * �^�u�A�C�e���𐶐����܂��B
	 *
	 * �^�u�A�C�e���ɐݒ肷��R���g���[���N���X�͕K���AISettingView�����N���X�ł���K�v������܂��B
	 */
	protected void createTabItems() {
		String text;

		// �_���v�f�[�^����
		text = ApplicationResource.getResource("Search.Dialog.Tab.StackView.Name");
		stackViewItem = new CTabItem(tabFolder, SWT.NONE);
		stackViewItem.setText(text);
		stackViewSearchTab = new StackViewSearchTab(this, tabFolder, SWT.None);
		stackViewItem.setControl(stackViewSearchTab);

		// �_���v�t�@�C������
		fileViewItem = new CTabItem(tabFolder, SWT.NONE);
		text = ApplicationResource.getResource("Search.Dialog.Tab.FileView.Name");
		fileViewItem.setText(text);
		fileViewSearchTab = new FileViewSearchTab(appWindow, this, tabFolder, SWT.NONE);
		fileViewItem.setControl(fileViewSearchTab);

		//�X�^�b�N�r���[�A�̏�Ԃ��m�F���A�ǂ̃A�C�e����\�����邱�Ƃ�����
		String curPath = appWindow.analyzerform.stackTree.getPath();
		if (curPath == null){
			tabFolder.setSelection(fileViewItem);
		}else{
			tabFolder.setSelection(stackViewItem);
		}

		updateUI();
		updateFocus();
	}

	private void updateToMode(){
		//conditionUnit = new ConditionUnit();
		if (tabFolder.getSelection() == fileViewItem){
			conditionFileView = new ConditionUnit();
			fileViewSearchTab.updateToMode(conditionFileView);
		}else{
			conditionStackView = new ConditionUnit();
			stackViewSearchTab.updateToMode(conditionStackView);
		}
	}

	/**
	 * �e�^�u����Ă΂��A���s�̃{�^���𐧌䂷��B
	 */
	public void checkRunStatus(){
		if (tabFolder.getSelection() == fileViewItem){
			if (fileViewSearchTab.checkRunStatus()){
				runBtn.setEnabled(true);
			}else{
				runBtn.setEnabled(false);
			}
		}else{
			if (stackViewSearchTab.checkRunStatus()){
				runBtn.setEnabled(true);
			}else{
				runBtn.setEnabled(false);
			}
		}
	}

	public ConditionUnit getTextSearchUnit(){
		if (tabFolder.getSelection() == fileViewItem){
			return conditionFileView;
		}else{
			return conditionStackView;
		}
	}

	public void setHistoryContiditions(ConditionUnit conditionFileView, ConditionUnit conditionStackView){
		this.conditionFileView = conditionFileView;
		this.conditionStackView = conditionStackView;
	}

	private void updateUI() {
		if (conditionFileView != null) {
			fileViewSearchTab.updateUI(conditionFileView);
		}
		if (conditionStackView != null){
			stackViewSearchTab.updateUI(conditionStackView);
		}
	}

	private void updateFocus() {
		if (tabFolder.getSelection().getControl() instanceof FileViewSearchTab) {
			fileViewSearchTab.updateFocus();
		} else if (tabFolder.getSelection().getControl() instanceof StackViewSearchTab) {
			stackViewSearchTab.updateFocus();
		}

	}

	public ConditionUnit[] getHistoryConditions() {
		return new ConditionUnit[]{conditionFileView, conditionStackView};
	}
}
