/*
 * Error Anaylzer Tool for Java
 *
 * Created on 2006/4/1
 *
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.analyzer.action;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import jp.co.ipride.excat.MainViewer;
import jp.co.ipride.excat.common.ApplicationResource;
import jp.co.ipride.excat.common.Message;
import jp.co.ipride.excat.common.action.BaseAction;
import jp.co.ipride.excat.common.utility.ExcatMessageUtilty;
import jp.co.ipride.excat.configeditor.model.ConfigModel;
import jp.co.ipride.excat.configeditor.util.ViewerUtil;


/**
 * �I�����̃A�N�V����
 * @version 1.0 2004/11/29
 * @author �� �ŉ�
 */
public class ExitAction extends BaseAction {

	public ExitAction(MainViewer appWindow) {
		super(appWindow);

		setText(ApplicationResource.getResource("Menu.File.Exit.Text"));
		setToolTipText(ApplicationResource.getResource("Menu.File.Exit.ToolTip"));
	}

	/**
	 * �A�N�V�����ɂ���ČĂ΂��B
	 */
	public void doJob() {
		if (ConfigModel.isChanged()) {
			int n = ViewerUtil.reloadConfig(appWindow);
			switch(n){
			case -2:
				//�ω��Ȃ�
				ConfigModel.noChanged();
				appWindow.close();
				break;
			case 0:
				//�ۑ�����
				if (!((MainViewer)appWindow).checkItems()){
					return;
				}
				if (ConfigModel.isNewConfig()){
					FileDialog saveDialog = new FileDialog(appWindow.getShell(),SWT.SAVE);
					saveDialog.setFilterExtensions(new String[]{"*.config"});
					String saveFile = saveDialog.open();
					if (saveFile != null){
						ConfigModel.saveAsNewConfig(saveFile);
						ExcatMessageUtilty.showMessage(
								this.appWindow.getShell(),
								Message.get("Tool.Save.Text"));
						ConfigModel.noChanged();
						appWindow.close();
					}
				}else{
					if (ConfigModel.update()) {
						ExcatMessageUtilty.showMessage(
								this.appWindow.getShell(),
								Message.get("Tool.Update.Text"));
						ConfigModel.noChanged();
						appWindow.close();
					}
				}
				break;
			case 2:
				//�ۑ�����
				ConfigModel.noChanged();
				appWindow.close();
				break;
			case 1:
				//�L�����Z��

			}
		} else {
			appWindow.close();
		}
	}
}