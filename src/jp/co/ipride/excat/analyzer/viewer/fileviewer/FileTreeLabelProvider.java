/*
 * Error Analyzer Tool for Java
 * 
 * Created on 2007/10/9
 * 
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.analyzer.viewer.fileviewer;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * �t�@�C���r���[�̃��x���v���o�C�_
 * 
 * @author tatebayashiy
 * 
 */
public class FileTreeLabelProvider extends LabelProvider {

	/**
	 * �\���e�L�X�g���擾���܂��B
	 */
	public String getText(Object element) {
		return ((BaseFileTreeItem) element).getText();
	}

	/**
	 * ���x���ɒǉ�����C���[�W���擾���܂��B
	 */
	public Image getImage(Object element) {
		BaseFileTreeItem item = (BaseFileTreeItem) element;
		ImageRegistry imageRegistry = FileViewerImageUtil.getImageRegistry();
		if (item instanceof FileTreeRootItem) {
			return imageRegistry.get(FileViewerImageUtil.IMGID_DRIVE);
		}
		if (item instanceof FileTreeFileItem) {
			// �t�@�C���A�f�B���N�g���̂݃A�C�R���\��
			if (item.getFile().isFile()) {
				// �t�@�C��
				return imageRegistry.get(FileViewerImageUtil.IMGID_FILE);
			} else if (item.getFile().isDirectory()) {
				// �f�B���N�g��
				return imageRegistry.get(FileViewerImageUtil.IMGID_FOLDER);
			} else {
				// ���̑�
			}
		}
		return super.getImage(element);
	}

}
