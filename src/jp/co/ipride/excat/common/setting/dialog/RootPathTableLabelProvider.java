/*
 * Error Analyzer Tool for Java
 * 
 * Created on 2007/10/9
 * 
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.common.setting.dialog;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * ���[�g�p�X�e�[�u���̃��x���v���o�C�_
 * 
 * @author tatebayashiy
 * 
 */
public class RootPathTableLabelProvider implements ITableLabelProvider {

	/**
	 * �C���[�W���擾���܂��B ���[�g�p�X�e�[�u���̐ݒ�ł̓C���[�W�͎w�肵�܂���B
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage()
	 */
	public Image getColumnImage(Object element, int columnNum) {
		return null;
	}

	/**
	 * �e�L�X�g���擾���܂��B
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText()
	 */
	public String getColumnText(Object element, int columnNum) {
		RootPathItem item = (RootPathItem) element;
		switch (columnNum) {
		case 0:
			return item.getName();
		case 1:
			return item.getPath();
		default:
			return null;
		}
	}

	/**
	 * ������
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#addListener()
	 */
	public void addListener(ILabelProviderListener listener) {

	}

	/**
	 * ������
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#dispose()
	 */
	public void dispose() {
	}

	/**
	 * ������
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#isLabelProperty()
	 */
	public boolean isLabelProperty(Object element, String columnNum) {
		return false;
	}

	/**
	 * ������
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#removeListener()
	 */
	public void removeListener(ILabelProviderListener listener) {
	}

}
