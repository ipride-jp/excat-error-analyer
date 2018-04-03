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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.co.ipride.excat.common.setting.SettingManager;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * �_���v�t�@�C���r���[���̃t�B���^�[
 *
 * @author tatebayashiy
 *
 */
public class DumpFileViewerFilter extends ViewerFilter {

	/** �\���t�@�C���̊g���q */
	// private static final String[] TARGET_EXTENTION = {".dat", ".DAT"};
	private String[] targetExtentions = { ".dat", ".zip"};

	private List<String> filteringList = new ArrayList<String>();

	/**
	 * �t�B���^�����O�̃��W�b�N�B
	 *
	 * �t�H���_�A�C�e���ƁA�_���v�t�@�C���p�̊g���q(.dat)�̃t�@�C���� �A�C�e���݂̂��\�������悤�Ƀt�B���^�����O���s���܂��B
	 */
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element == null)
			return false;

		boolean targeted = false;

		if (isDirectoryItem(element)) {
			if (isIncludesTargetString(element)) {
				targeted = true;
			} else {
				targeted = false;
			}
		} else if (isTargetExtentionFileItem(element)) {
			if (isIncludesTargetString(element)) {
				targeted = true;
			} else {
				targeted = false;
			}
		}

		return targeted;
	}

	/**
	 * �f�B���N�g����\�킷�A�C�e�����ǂ������肵�܂��B
	 *
	 * @param element
	 * @return
	 */
	private boolean isDirectoryItem(Object element) {
		BaseFileTreeItem item = (BaseFileTreeItem) element;
		File file = item.getFile();

		if (item instanceof FileTreeTopItem) {
			return true;
		}

		if (item instanceof FileTreeRootItem) {
			return true;
		}

		if (file != null) {
			// �f�B���N�g���̏ꍇ�͕\������
			if (file.isDirectory()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �Ώۂ̊g���q�����t�@�C���A�C�e���ł��邩�ǂ������肵�܂��B
	 *
	 * @param element
	 * @return
	 */
	private boolean isTargetExtentionFileItem(Object element) {

		BaseFileTreeItem item = (BaseFileTreeItem) element;
		File file = item.getFile();

		String[] filterExtentions = SettingManager.getSetting().getFilterExtentions();
		if (filterExtentions != null) {
			targetExtentions = SettingManager.getSetting()
					.getFilterExtentions();
		}

		if (file != null) {
			if (file.isFile()) {
				// �Ώۂ̊g���q�݂̂�ΏۂƂ���B
				for (int i = 0; i < targetExtentions.length; i++) {
					if (file.getPath().toLowerCase().endsWith(targetExtentions[i])) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * �ݒ肳��Ă��镶������p�X���Ɏ����Ă��邩�ǂ����𔻒肵�܂��B
	 *
	 * @param element
	 * @return
	 */
	private boolean isIncludesTargetString(Object element) {

		if (filteringList.size() > 0) {
			BaseFileTreeItem item = (BaseFileTreeItem) element;
			File file = item.getFile();
			if (file != null) {
				String path = file.getPath();

				for (Iterator<String> it = filteringList.iterator(); it.hasNext();) {
					String targetStr = (String) it.next();
					if (path.indexOf(targetStr) >= 0) {
						// �ΏۃA�C�e���̃p�X�ɑΏە�������܂ޏꍇ��true��ԋp���܂��B
						return true;
					} else {
						// �ΏۃA�C�e���̎q�m�[�h���ɂP�ł��\���Ώۂ����݂���ꍇ�́A
						// �\���ΏۂƂ���K�v������̂ŁA�q�m�[�h�̒T�����s���܂��B
						BaseFileTreeItem[] children = item.getChildren();
						if (children != null) {
							for (int i = 0; i < children.length; i++) {
								if (isIncludesTargetString(children[i])) {
									return true;
								}
							}
						}
					}
				}
			}
		} else {
			// ���ݒ�̏ꍇ�͍s���܂���B
			return true;
		}
		return false;
	}

	/**
	 * �t�B���^�����O�ɂĕ\���ΏۂƂ���A�C�e���̕������ǉ����܂��B
	 *
	 * @param str
	 */
	public void addFilteringString(String str) {
		filteringList.add(str);
	}

	/**
	 * �t�B���^�����O�ɂĕ\���ΏۂƂ��镶����̃��X�g��ݒ肵�܂��B
	 *
	 * @param filterStringList
	 */
	public void setFilteringStringList(List<String> filterStringList) {
		filteringList = filterStringList;
	}

	/**
	 * �\���Ώۂ̊g���q�̔z���ݒ肵�܂��B
	 *
	 * @param extentions
	 */
	public void setFilteringExtentions(String[] extentions) {
		targetExtentions = extentions;
	}

}
