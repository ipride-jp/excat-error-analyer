/*
 * Error Anaylzer Tool for Java
 * 
 * Created on 2006/4/1
 * 
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.analyzer.viewer.stackviewer;

import jp.co.ipride.excat.analyzer.common.DumpDocument;
import jp.co.ipride.excat.analyzer.common.DumpFileXmlConstant;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Tree�\���̊e�m�[�h�̕\�����J�X�^�}�C�Y����B
 * @version 	1.0 2004/12/01
 * @author 	�� �ŉ�
 */
public class StackContentProvider implements ITreeContentProvider {
	
	/**
	 * �q�m�[�h�̔z���Ԃ�
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object parentElement) {
	    return getChildrenNodeArray((Node)parentElement);
	}
	
	/**
	 * �e�m�[�h��Ԃ��B
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object element) {
	    return ((Node)element).getParentNode();
	}
	
	/**
	 * �q�m�[�h�������ǂ���
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object element) {
	    return ((Node)element).hasChildNodes();
	}
	
	/**
	 * �q�m�[�h�̗v�f���擾
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		return getChildrenNodeArray((Node)inputElement);
	}
	
	/**
	 * �������Ȃ��B
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
	}
	
	/**
	 * �������Ȃ��B
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
	
	/**
	 * �q�m�[�h�z����擾
	 * �����q�m�[�h�̖��O�́hObjectPool�h�̏ꍇ�APool����擾
	 * @param node �e�m�[�h
	 * @return
	 */
	private Node[] getChildrenNodeArray(Node node) {
		NodeList nodeList;
		nodeList = node.getChildNodes();
		Node[] nodeArray = new Node[nodeList.getLength()];
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node item = nodeList.item(i);
			if (!DumpFileXmlConstant.NODE_OBJECT_POOL.equals(item.getNodeName())){
				nodeArray[i] = DumpDocument.replaceObject(item);
			}
		}
		return nodeArray;
	}
	
}
