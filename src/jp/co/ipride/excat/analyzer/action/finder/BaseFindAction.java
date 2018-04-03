/*
 * Error Analyzer Tool for Java
 *
 * Created on 2007/10/9
 *
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.analyzer.action.finder;


import jp.co.ipride.excat.MainViewer;
import jp.co.ipride.excat.analyzer.action.finder.FindDialog;
import jp.co.ipride.excat.common.action.BaseAction;

/**
 * �����֘A�̃A�N�V�����̃x�[�X�N���X
 *
 * @author tatebayashi
 *
 */
public abstract class BaseFindAction extends BaseAction {

	/** �����_�C�A���O */
	protected FindDialog dialog;
	/** �����񋟌� */
	protected IFindProvider findProvider;
	/** ����������񋟌� */
	protected IFindStringProvider findStrProvider;

	/** �R���X�g���N�^ */
	public BaseFindAction(MainViewer appWindow) {
		super(appWindow);
		dialog = new FindDialog(appWindow.getShell());
	}

	/**
	 * �����_�C�A���O��ݒ肵�܂��B
	 *
	 * @param dialog
	 */
	public void setFindDialog(FindDialog dialog) {
		this.dialog = dialog;
	}

	/**
	 * �����񋟌���ݒ肵�܂��B
	 *
	 * @param findProvider
	 */
	public void setFindProvider(IFindProvider findProvider) {
		this.findProvider = findProvider;
	}

	/**
	 * ����������񋟌���ݒ肵�܂��B
	 *
	 * @param findStrProvider
	 */
	public void setFindStringProvider(IFindStringProvider findStrProvider) {
		this.findStrProvider = findStrProvider;
	}

	/**
	 * �����_�C�A���O��\�����܂�
	 *
	 * @param findProvider
	 * @param findStrProvider
	 */
	protected void openFindDialog() {
		dialog.setFindProvider(findProvider);
		String str = findStrProvider.getFindString();

		if (str != null && !"".equals(str)) {
			dialog.setFindString(str);
		}
		dialog.open();
	}

}
