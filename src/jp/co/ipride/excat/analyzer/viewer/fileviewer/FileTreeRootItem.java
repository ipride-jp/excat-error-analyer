/*
 * Error Analyzer Tool for Java
 *
 * Created on 2007/10/9
 *
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.analyzer.viewer.fileviewer;

import java.io.File;

import jp.co.ipride.excat.common.setting.dialog.RootPathItem;

/**
 * �t�@�C���c���[�̃��[�g�A�C�e��
 *
 * @author tatebayashiy
 *
 */
public class FileTreeRootItem extends BaseFileTreeItem {

	/** �e�m�[�h�̃A�C�e�� */
	private FileTreeTopItem parent = new FileTreeTopItem();

	/** ���[�g�p�X�A�C�e�� */
	private RootPathItem item;

	/**
	 * �R���X�g���N�^
	 *
	 * @param item
	 *            �ݒ�_�C�A���O�ɂĎw�肵���A�C�e��
	 */
	public FileTreeRootItem(RootPathItem item) {
		super(new File(item.getPath()));
		this.item = item;
	}

	/**
	 * @see jp.co.ipride.excat.analyzer.viewer.fileviewer.BaseFileTreeItem#getChildren()
	 */
	public BaseFileTreeItem[] getChildren() {
		File[] files = file.listFiles();

		if (files == null) {
			return new BaseFileTreeItem[0];
		} else {
			FileTreeFileItem[] children = new FileTreeFileItem[files.length];
			for (int i = 0; i < children.length; i++) {
				children[i] = new FileTreeFileItem(files[i], this);
			}
			return children;
		}
	}

	/**
	 * @see jp.co.ipride.excat.analyzer.viewer.fileviewer.BaseFileTreeItem#getParent()
	 */
	public BaseFileTreeItem getParent() {
		return parent;
	}

	/**
	 * @see jp.co.ipride.excat.analyzer.viewer.fileviewer.BaseFileTreeItem#getText()
	 */
	public String getText() {
		return item.getName() + " [" + item.getPath() + "]";
	}

	public String getPathName() {
		if (item == null) {
			return null;
		}
		return item.getName();
	}

	/**
	 * @date 2009/10/17
	 * @author tu-ipride
	 * @return
	 */
	public RootPathItem getRootPathItem(){
		return item;
	}

}
