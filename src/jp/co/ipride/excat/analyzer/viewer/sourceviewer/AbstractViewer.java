/*
 * Error Anaylzer Tool for Java
 *
 * Created on 2006/4/1
 *
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.analyzer.viewer.sourceviewer;

import jp.co.ipride.excat.MainViewer;
import jp.co.ipride.excat.analyzer.viewer.localviewer.VariableTable;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;

/**
 * ���ۃ\�[�X�r���[�A
 *
 * @author �j�̐V
 * @since 2006/9/17
 */
public abstract class AbstractViewer {
	// �r���[�A�E�^�C�v
	public final static int JAVA_SOURCE = 0;
	public final static int BYTE_CODE = 1;
	public final static int SOURCE_REPOSITORY = 2;
	public final static int DUMMY = 4;

	// SourceViewerPlatform�̑���
	protected CTabItem item = null;
	// table folder
//	protected CTabFolder tabFolder = null;
	// ���[�J���ϐ��r���[�A
	protected VariableTable variableTable = null;
	// �eform
	protected SashForm parent = null;
	// window
	protected MainViewer appWindow = null;
	// �Ή��̃��\�b�h���
	protected MethodInfo methodInfo = null;
	// �r���[�A�E�^�C�v
	protected int type;
	// �r���[�A�̃\�[�X�p�[�X
	protected String sourcePath = null;

	//key of tab items
	private String tabKey = null;


	/**
	 * �\�[�X�r���[�A�쐬
	 */
	protected abstract void createSrcViewer();

	/**
	 * �r���[�A�E�^�C�v���擾
	 *
	 * @return
	 */
	public abstract int getType();

	/**
	 * �I�����C����`��
	 *
	 * @param lineNum
	 */
	public abstract void highlight(int lineNum);

	/**
	 * �I�����C�����N���A
	 *
	 */
	public abstract void clearSelectLine();

	/**
	 * ���s�����s��\������悤��Viewer��Scroll����
	 *
	 */
	public abstract void showCalledPlace(MethodInfo methodInfo);

	/**
	 * �N���X/���\�b�h��錾����ӏ���\������B
	 */
	public void showDeclaredPlace(MethodInfo methodInfo){

	}

	/**
	 * �t�B�[���h��錾����ӏ���\������B
	 * @param matchField
	 * @version 3.0
	 * @date 2009/10/28
	 * @author tu-ipride
	 */
	public void showDeclaredPlace(MatchField matchField){

	}

	/**
	 * �Y���r���[�A�̎���
	 *
	 * @param methodInfo
	 * @return
	 */
	public boolean identify(MethodInfo methodInfo) {
		return this.methodInfo.identify(methodInfo.getNode());
	}

	/**
	 *
	 * @return
	 */
	public CTabItem getTabItem() {
		return item;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	/**
	 * �Y���r���[�A�̃��\�b�h�����擾
	 *
	 * @return
	 */
	public MethodInfo getMethodInfo() {
		return methodInfo;
	}

	/**
	 * add a listner to tree.
	 *
	 * @param listener
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {

	}

	public String getTabKey() {
		return tabKey;
	}

	public void setTabKey(String tabKey) {
		this.tabKey = tabKey;
	}
}
