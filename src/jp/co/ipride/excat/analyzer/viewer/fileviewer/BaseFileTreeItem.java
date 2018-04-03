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

/**
 * �_���v�t�@�C���r���[���̃c���[�A�C�e���Ƃ��Đݒ肷��A�C�e���̒��ۃN���X
 *
 * @author tatebayashiy
 *
 */
public abstract class BaseFileTreeItem {

	/** �t�@�C���p�X���i�[����t�@�C���I�u�W�F�N�g */
	protected File file = null;

	/**
	 * �R���X�g���N�^
	 *
	 * @param file
	 *            �{�A�C�e����\�킷�Ώۃt�@�C��
	 */
	public BaseFileTreeItem(File file) {
		this.file = file;
	}

	/**
	 * �t�@�C���p�X���i�[�����t�@�C���I�u�W�F�N�g���擾���܂��B
	 *
	 * @return �{�A�C�e����\�킷�Ώۃt�@�C���I�u�W�F�N�g
	 */
	public File getFile() {
		return file;
	}

	/**
	 * �q�m�[�h���擾���܂��B
	 *
	 * @return
	 */
	public abstract BaseFileTreeItem[] getChildren();

	/**
	 * �e�m�[�h���擾���܂��B
	 *
	 * @return
	 */
	public abstract BaseFileTreeItem getParent();

	/**
	 * �\���e�L�X�g���擾���܂��B
	 *
	 * @return
	 */
	public abstract String getText();

	public String getFilePath(){
		return file.getPath();
	}

}
