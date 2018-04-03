/*
 * Error Anaylzer Tool for Java
 *
 * Created on 2006/4/1
 *
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.common.action;

import java.net.URL;
import java.util.ArrayList;

import jp.co.ipride.excat.MainViewer;
import jp.co.ipride.excat.analyzer.viewer.sourceviewer.declare.ClassRepository;
import jp.co.ipride.excat.common.ApplicationResource;
import jp.co.ipride.excat.common.Message;
import jp.co.ipride.excat.common.icons.IconFilePathConstant;
import jp.co.ipride.excat.common.setting.SettingManager;
import jp.co.ipride.excat.common.setting.dialog.RootPathItem;
import jp.co.ipride.excat.common.setting.dialog.SettingDialog;
import jp.co.ipride.excat.common.utility.HelperFunc;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * Java��Jar�̃p�X��ݒu����A�N�V����
 *
 * @author �j �̐V
 * @since 2006/9/17
 */
public class SettingAction extends BaseAction {

	/**
	 * @param appWindow
	 */
	public SettingAction(MainViewer appWindow) {
		super(appWindow);

		String text = ApplicationResource
				.getResource("Menu.Tools.Setting.Text");
		setText(text);
		this.setToolTipText(text);
		URL url = MainViewer.class.getResource(IconFilePathConstant.SETTING);
		setImageDescriptor(ImageDescriptor.createFromURL(url));
	}

	/**
	 * �ݒ��ʂ�\������B
	 *
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void doJob() {
		HelperFunc.getLogger().info(
				Message.get("SettingAction.Start"));
		HelperFunc.getLogger().info(
				Message.get("SettingAction.SourceFolder")
						+ getSourceFolderMsg());

		SettingDialog dialog = new SettingDialog(appWindow.getShell());

		if (IDialogConstants.OK_ID == dialog.open()) {

			appWindow.updateFileViewer();

			// �ݒ���e��ۑ�
			if(SettingManager.isChanged()) {
				SettingManager.update(appWindow.getShell(),true);
				// ��Q #531
				appWindow.analyzerform.sourceViewerPlatform.refreshAll();
			}
		}
		HelperFunc.getLogger().info(
				Message.get("SettingAction.End"));
		HelperFunc.getLogger().info(
				Message.get("SettingAction.SourceFolder")
						+ getSourceFolderMsg());
		HelperFunc.getLogger().info(
						Message.get("SettingAction.RootPath")
								+ getRootPathMsg());
	}

	public String getSourceFolderMsg() {
		ArrayList<String> list = SettingManager.getSetting().getPathList();
		if (list != null) {
			StringBuffer result = new StringBuffer();
			for (int i=0; i<list.size(); i++) {
				String folder = list.get(i);
				result.append(folder + ";");
			}
			if (result.length() > 0) {
				result.deleteCharAt(result.length() - 1);
			}
			return result.toString();
		}
		return "";
	}

	/**
	 * ���ݐݒ肳��Ă��郋�[�g�p�X���ڂ̃��O�o�͗p���b�Z�[�W���擾���܂��B
	 *
	 * @return ���O�p���b�Z�[�W
	 */
	public String getRootPathMsg() {
		ArrayList<RootPathItem> list = SettingManager.getSetting().getRootPathList();
		if (list != null) {
			StringBuffer result = new StringBuffer();
			for (int i=0; i<list.size(); i++) {
				RootPathItem item = list.get(i);
				result.append(item.getName());
				result.append("[");
				result.append(item.getPath());
				result.append("];");
			}
			if (result.length() > 0) {
				result.deleteCharAt(result.length() - 1);
			}
			return result.toString();
		}
		return "";
	}
}
