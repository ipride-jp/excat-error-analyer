/*
 * Error Analyzer Tool for Java
 * 
 * Created on 2007/10/9
 * 
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.analyzer.viewer.fileviewer;

import org.eclipse.jface.viewers.IElementComparer;

/**
 * �t�@�C���c���[�A�C�e���̈�v��r���W�b�N��񋟂���N���X
 * 
 * @author tatebayashiy
 * 
 */
public class FileTreeItemComparer implements IElementComparer {

	/**
	 * ��v��r���s���܂��B �t�@�C���p�X������̏ꍇ�A��v����Ƃ݂Ȃ���܂��B (�������A���[�g�A�C�e���̏ꍇ�́A���̂���v����Γ���Ɣ��f���܂��B)
	 */
	public boolean equals(Object element1, Object element2) {
		// null�̔�r�͈�v�Ɣ��f
		if (element1 == null && element2 == null) {
			return true;
		}
		// null�Ɣ�null�̔�r�͕s��v�Ƃ���
		if (element1 == null && element2 != null) {
			return false;
		}
		if (element1 != null && element2 == null) {
			return false;
		}
		// �C���X�^���X�s��v�͕s��v�Ɣ��f
		if (!element1.getClass().equals(element2.getClass())) {
			return false;
		}

		if (element1 instanceof FileTreeRootItem) {
			// �A�C�e����FileTreeRootItem�̏ꍇ�́A�����ɕێ��̖��O�ň�v�m�F���s���B
			FileTreeRootItem rootItem1 = (FileTreeRootItem) element1;
			FileTreeRootItem rootItem2 = (FileTreeRootItem) element2;

			return rootItem1.getPathName().equals(rootItem2.getPathName());
		}

		// ���̑��͓����ɕێ����Ă���File�I�u�W�F�N�g�̈�v�ɂĔ��f
		BaseFileTreeItem item1 = (BaseFileTreeItem) element1;
		BaseFileTreeItem item2 = (BaseFileTreeItem) element2;
		if (item1.getFile() == null && item2.getFile() == null) {
			return true;
		}
		if (item1.getFile() == null && item2.getFile() != null) {
			return false;
		}
		if (item1.getFile() != null && item2.getFile() == null) {
			return false;
		}

		// �����ɕێ����Ă���File�I�u�W�F�N�g�ɂē�����r���܂��B
		return item1.getFile().equals(item2.getFile());
	}

	/**
	 * �n�b�V���R�[�h���擾���܂��B �t�@�C���p�X������̏ꍇ�A�����n�b�V���R�[�h��Ԃ��܂��B
	 * (���[�g�A�C�e���̏ꍇ�́A���̂̃n�b�V���R�[�h��Ԃ��܂��B)
	 */
	public int hashCode(Object element) {
		// �p�X����������Γ����n�b�V���R�[�h��Ԃ�
		if (element == null)
			return 0;
		BaseFileTreeItem item = (BaseFileTreeItem) element;
		if (item.getFile() == null)
			return 0;
		if (element instanceof FileTreeRootItem) {
			FileTreeRootItem rootItem = (FileTreeRootItem) element;
			// FileTreeRootItem�̏ꍇ�͖��̂̃n�b�V���R�[�h��Ԃ��܂��B
			return rootItem.getPathName().hashCode();
		}
		return item.getFile().hashCode();
	}

}
