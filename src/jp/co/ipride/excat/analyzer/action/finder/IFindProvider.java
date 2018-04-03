/*
 * Error Analyzer Tool for Java
 * 
 * Created on 2007/10/9
 * 
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.analyzer.action.finder;

/**
 * �����񋟌��C���^�t�F�[�X
 * 
 * ������񋟂���N���X�͖{�C���^�t�F�[�X����������B
 * 
 * @author tatebayashi
 * 
 */
public interface IFindProvider {
	/**
	 * �������s
	 * 
	 * @param condition
	 *            ��������
	 * @return <code>true</code>:���������ꍇ�A<code>false</code>������Ȃ������ꍇ
	 */
	public boolean find(FindCondition condition);
}
