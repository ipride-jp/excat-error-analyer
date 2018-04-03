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
 * ��������
 * 
 * @author tatebayashi
 * 
 */
public class FindCondition {
	/** �����Ώۂ̕����� */
	private String targetString;

	/** �������� */
	private boolean forwardSearch;

	/** ��/�������̋�� */
	private boolean caseSensitive;
	/** �z���� */
	private boolean circularSearch;
	/** �P��P�ʌ��� */
	private boolean wordSearch;
	/** ���K�\�����p */
	private boolean regixSearch;

	/**
	 * �R���X�g���N�^(������̂ݎw��)
	 * 
	 * @param targetStr
	 *            �����Ώۂ̕�����
	 */
	public FindCondition(String targetStr) {
		this(targetStr, true, false, false, false, false);
	}

	/**
	 * �R���X�g���N�^(�����Ώە�����{�����������w��)
	 * 
	 * @param targetStr
	 * @param direction
	 * @param caseSensitive
	 * @param circularSearch
	 * @param wordSearch
	 * @param regixSearch
	 */
	public FindCondition(String targetStr, boolean forwardSearch,
			boolean caseSensitive, boolean circularSearch, boolean wordSearch,
			boolean regixSearch) {
		targetString = targetStr;
		this.forwardSearch = forwardSearch;
		this.caseSensitive = caseSensitive;
		this.circularSearch = circularSearch;
		this.wordSearch = wordSearch;
		this.regixSearch = regixSearch;
	}

	/**
	 * �R���X�g���N�^(FindCondition�̕���)
	 * 
	 * @param condition
	 */
	public FindCondition(FindCondition condition) {
		targetString = condition.getTargetString();
		forwardSearch = condition.isForwardSearch();
		caseSensitive = condition.isCaseSensitive();
		circularSearch = condition.isCircularSearch();
		wordSearch = condition.isWordSearch();
		regixSearch = condition.isRegixSearch();
	}

	/**
	 * ��/����������ʂ��邩�ǂ������擾���܂��B
	 * 
	 * @return
	 */
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/**
	 * ��/����������ʂ��邩�ǂ�����ݒ肵�܂��B
	 */
	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	/**
	 * �z�������s�����ǂ������擾���܂��B
	 * 
	 * @return
	 */
	public boolean isCircularSearch() {
		return circularSearch;
	}

	/**
	 * �z�������s�����ǂ�����ݒ肵�܂��B
	 */
	public void setCircularSearch(boolean search) {
		circularSearch = search;
	}

	/**
	 * ���K�\���𗘗p���Č������s�����ǂ������擾���܂��B
	 * 
	 * @return
	 */
	public boolean isRegixSearch() {
		return regixSearch;
	}

	/**
	 * ���K�\���𗘗p���Č������s�����ǂ�����ݒ肵�܂��B
	 */
	public void setRegixSearch(boolean search) {
		regixSearch = search;
	}

	/**
	 * �����Ώە�������擾���܂��B
	 * 
	 * @return
	 */
	public String getTargetString() {
		return targetString;
	}

	/**
	 * �����Ώە������ݒ肵�܂��B
	 * 
	 * @return
	 */
	public void setTargetString(String string) {
		targetString = string;
	}

	/**
	 * �����������O�������ł��邩�ǂ������擾���܂��B
	 * 
	 * @return <code>true</code>�F�O�������A<code>false</code>:�������
	 */
	public boolean isForwardSearch() {
		return forwardSearch;
	}

	/**
	 * ����������ݒ肵�܂��B
	 */
	public void setForwardSearch(boolean forwardSearch) {
		this.forwardSearch = forwardSearch;
	}

	/**
	 * �P��P�ʂŌ������邩�ǂ������擾���܂��B
	 * 
	 * @return <code>true</code>:�P��P�ʂŌ�������A<code>false</code>:�P��P�ʂŌ������Ȃ��B
	 */
	public boolean isWordSearch() {
		return wordSearch;
	}

	/**
	 * �P��P�ʂŌ������邩�ǂ�����ݒ肵�܂��B
	 * 
	 * @param search
	 *            <code>true</code>:�P��P�ʂŌ�������A<code>false</code>:�P��P�ʂŌ������Ȃ��B
	 */
	public void setWordSearch(boolean search) {
		wordSearch = search;
	}
}
