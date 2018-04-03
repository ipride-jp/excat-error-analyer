/*
 * Error Analyzer Tool for Java
 * 
 * Created on 2007/10/9
 * 
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.analyzer.viewer.fileviewer;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * �t�@�C���c���[�̓��e���擾�����v���o�C�_
 * 
 * @author tatebayashiy
 * 
 */
public class FileTreeContentProvider implements ITreeContentProvider {

	/**
	 * �q�m�[�h�̈ꗗ���擾���܂��B
	 */
	public Object[] getChildren(Object element) {
		return ((BaseFileTreeItem) element).getChildren();
	}

	/**
	 * �e�m�[�h���擾���܂��B
	 */
	public Object getParent(Object element) {
		return ((BaseFileTreeItem) element).getParent();
	}

	/**
	 * �q�m�[�h�������ǂ�����ԋp���܂��B
	 */
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	/**
	 * �q�m�[�h�̈ꗗ���擾���܂��B
	 */
	public Object[] getElements(Object element) {
		return getChildren(element);
	}

	/**
	 * ������
	 */
	public void dispose() {

	}

	/**
	 * ������
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}
