/*
 * Error Analyzer Tool for Java
 * 
 * Created on 2007/10/9
 * 
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.common.setting.dialog;

/**
 * �ݒ�_�C�A���O�̐ݒ�r���[�̃N���X���������ׂ������̃C���^�t�F�[�X��񋟂��܂��B
 * @author tatebayashiy
 *
 */
public interface SettingViewListener {
	/**
	 * �ݒ�m��O�̏������������܂��B
	 */
	public boolean preOkProcessed();
	/**
	 * �ݒ�L�����Z���O�̏������������܂��B
	 */
	public boolean preCancelProcessed();
	/**
	 * �ݒ�m���̏������������܂��B
	 */
	public void postOkProcessed();
	/**
	 * �ݒ�L�����Z����̏������������܂��B
	 */
	public void postCancelProcessed();
}
