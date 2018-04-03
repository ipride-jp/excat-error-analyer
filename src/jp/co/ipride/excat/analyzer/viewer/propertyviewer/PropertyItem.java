/*
 * Error Anaylzer Tool for Java
 * 
 * Created on 2006/4/1
 * 
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.analyzer.viewer.propertyviewer;

/**
 * XML�c���[�̃Z���̗v�f
 * @author GuanXH
 * @since 2006/9/17
 */
public class PropertyItem {
	
	//�v�f��
	private String name;
	//�v�f�̒l
	private String value;
	
	public PropertyItem() {
		name = "";
		value = "";
	}
	
	public PropertyItem(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * �v�f���擾
	 * @return  Returns the name.
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * �v�f�̒l���擾
	 * @return  Returns the value.
	 * @uml.property  name="value"
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * @param name  The name to set.
	 * @uml.property  name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param value  The value to set.
	 * @uml.property  name="value"
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}
