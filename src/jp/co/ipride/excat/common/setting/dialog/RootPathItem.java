/*
 * Error Analyzer Tool for Java
 * 
 * Created on 2007/10/9
 * 
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.common.setting.dialog;

import java.io.Serializable;

import jp.co.ipride.excat.common.utility.HelperFunc;

/**
 * ���[�g�p�X�ݒ�A�C�e��
 * 
 * @author tatebayashiy
 * 
 */
public class RootPathItem implements Serializable {

	/** �V���A���C�Y�o�[�W����ID */
	private static final long serialVersionUID = 1L;

	/** ���[�g�p�X�̃��[�U��`���� */
	private String name;

	/** �f�B���N�g���p�X������(��΃p�X) */
	private String path;

	/**
	 * ���[�g�p�X�̃��[�U��`���̂��擾���܂��B
	 * 
	 * @return ���[�U��`����
	 */
	public String getName() {
		return name;
	}

	/**
	 * ���[�g�p�X�̃��[�U��`���̂�ݒ肵�܂��B
	 * 
	 * @param name
	 *            ���[�U��`����
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * �f�B���N�g���p�X������(��΃p�X)���擾���܂��B
	 * 
	 * @return �f�B���N�g���p�X������(��΃p�X)
	 */
	public String getPath() {
		return path;
	}

	/**
	 * �f�B���N�g���p�X������(��΃p�X)��ݒ�܂��B
	 * 
	 * @param path
	 *            �f�B���N�g���p�X������(��΃p�X)
	 */
	public void setPath(String path) {
		this.path = path;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof RootPathItem)) {
			return false;
		}
		
		RootPathItem other = (RootPathItem) obj;
		return HelperFunc.compareObject(name, other.name)
				&& HelperFunc.compareObject(path, other.path);
	}
}
