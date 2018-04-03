/*
 * Error Anaylzer Tool for Java
 *
 * Created on 2006/4/1
 *
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.common.setting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import jp.co.ipride.excat.common.ApplicationResource;
import jp.co.ipride.excat.common.dialog.SourceUpdateDialog;
import jp.co.ipride.excat.common.setting.repository.Repository;
import jp.co.ipride.excat.common.sourceupdate.SourceCodeUpdater;
import jp.co.ipride.excat.common.sourceupdate.SvnSourceCodeUpdater;
import jp.co.ipride.excat.common.utility.ExcatMessageUtilty;
import jp.co.ipride.excat.common.utility.HelperFunc;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;

/**
 * �Z�b�e�B���O���Ǘ�����B
 *
 * @author �j�̐V
 * @since 2006/9/17
 */
public class SettingManager {
	// Setting�I�u�W�F�N�g��ۑ�����ꏊ
	private static final String SETTING_FILE_PATH = System
			.getProperty("user.dir")
			+ File.separator + "Setting";

	// �Z�b�e�B���O�������I�u�W�F�N�g
	private static Setting setting = null;
	private static Setting oldSetting = null;

	/**
	 * �c�[���N�����A�Z�b�e�B���O�������[�h����B
	 */
	public static void load() {
		setting = createSettingFromFile(SETTING_FILE_PATH);
		oldSetting = createSettingFromFile(SETTING_FILE_PATH);
	}

	/**
	 * �Z�b�e�B���O�����t�@�C���ɕۑ�����B �^�C�~���O�Ƃ��ẮA�ύX���ꂽ�ꍇSourceManager�ɌĂ΂��B
	 * �I������viewer�ɌĂ΂��B
	 */
	public static void save() {
		saveSettingToFile(SETTING_FILE_PATH);
		oldSetting = createSettingFromFile(SETTING_FILE_PATH);
	}

	public static void outputSetting(String filePath){
		saveSettingToFile(filePath);
	}

	public static void inputSetting(String filePath){
		setting = createSettingFromFile(filePath);
		// ��Q489
		//oldSetting = createSettingFromFile(filePath);
		//saveSettingToFile(SETTING_FILE_PATH);
	}

	private static Setting createSettingFromFile(String filePath) {
		Setting setting = null;
		ObjectInputStream in = null;

		try {
			in = new ObjectInputStream(new FileInputStream(filePath));
			setting = (Setting) in.readObject();
		} catch (Exception e) {
			Logger.getLogger("viewerLogger").debug(e);
			setting = new Setting();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}

		return setting;
	}

	private static void saveSettingToFile(String filePath) {
		if (setting == null) {
			return;
		}

		ObjectOutputStream out = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}

			out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(setting);
		} catch (Exception e) {
			HelperFunc.getLogger().error("SettingManager", e);
			ExcatMessageUtilty.showErrorMessage(null,e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static Setting getSetting() {
		return setting;
	}

	public static Repository getRepository(){
		return Repository.getInstance();
	}

	public static void creatRepository(Shell shell, String title){
		SourceCodeUpdater sourceCodeUpdater = new SvnSourceCodeUpdater();
		SourceUpdateDialog dialog = new SourceUpdateDialog(
				shell, title, sourceCodeUpdater);
		dialog.openDialog();
		return;
	}

	/**
	 * �Z�b�e�B���O���𓯊�����B
	 *
	 * @param flag
	 *            True�̏ꍇ�A�t�@�C�����X�V�BFalse�̏ꍇ�A���������X�V
	 * @date tu add for v3
	 */
	public static void update(Shell shell,boolean flag) {
		if (flag) {
			save();
		} else {
			load();
		}
		creatRepository(
				shell,
				ApplicationResource.getResource("SourceUpdateProgressDialog.Update.Text")
				);  //v3
	}

	/**
	 * �Z�b�e�B���O���͕ύX�L����
	 *
	 * @return
	 */
	public static boolean isChanged() {
		return !HelperFunc.compareObject(setting, oldSetting);
	}

	/**
	 * �\�[�X�p�X�A�N���X�p�X�̐ݒ肪�ύX����Ă��邩�ǂ���
	 * @return true for changed.
	 */
	public static boolean isPathChanged(){
		ArrayList<String> sourcePathList = setting.getPathList();
		ArrayList<String> oldSourcePathList = oldSetting.getPathList();
		SourceRepositorySetting repo = setting.getSourceRepositorySetting();
		SourceRepositorySetting oldRepo = oldSetting.getSourceRepositorySetting();
		ArrayList<String>  priority = setting.getPriorityInfo();
		ArrayList<String>  oldPriority = oldSetting.getPriorityInfo();
		if (!HelperFunc.compareList(sourcePathList,oldSourcePathList)
				|| HelperFunc.compareList(priority,oldPriority)
				|| HelperFunc.compareObject(repo, oldRepo)){
			return true;
		}else{
			return false;
		}

//		return !HelperFunc.compareList(sourcePathList,oldSourcePathList);
	}


	/**
	 * �ύX���L�����Z�����郁�\�b�h
	 */
	public static void cancelChanges() {
		setting = oldSetting;
		//add by Qiu Song 20091113 for �o�O#499
		oldSetting = createSettingFromFile(SETTING_FILE_PATH);
		//end of add by Qiu Song 20091113 for �o�O#499
	}
}