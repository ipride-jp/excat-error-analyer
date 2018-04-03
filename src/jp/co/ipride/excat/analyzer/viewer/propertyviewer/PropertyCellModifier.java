/*
 * Error Anaylzer Tool for Java
 *
 * Created on 2006/4/1
 *
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.analyzer.viewer.propertyviewer;

import jp.co.ipride.excat.common.clipboard.ExcatClipboard;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

/**
 * XML�c���[�̃Z���ݒu
 * @author GuanXH
 * @since 2006/9/17
 */
public class PropertyCellModifier implements ICellModifier {

	//�����r���[�A
	private PropertyTable propertyTable;

	/**
	 *
	 */
	public PropertyCellModifier(PropertyTable propertyTable) {
		super();

		this.propertyTable = propertyTable;
	}

	/**
	 * �ύX�\���ݒ�
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
	 */
	public boolean canModify(Object element, String property) {
		//Find the index of column
		int columnIndex = propertyTable.getColumnNames().indexOf(property);

		return columnIndex != 0; //column 0 is readonly.
	}

	/**
	 * �l���擾
	 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
	 */
	public Object getValue(Object element, String property) {
		PropertyItem propertyItem = (PropertyItem)element;
//		ExcatClipboard.setPasteCandidate(propertyItem);

		//Find the index of column
		int columnIndex = propertyTable.getColumnNames().indexOf(property);

		Object result;
		switch (columnIndex) {
			case 0:
				result = propertyItem.getName();
				break;
			case 1:
				result = propertyItem.getValue();
				ExcatClipboard.setCopyCandidate(propertyItem.getValue());
				break;
			default:
				 result = "";
		}

		return result;
	}

	/**
	 * �I�����ꂽ�ꍇ�A�����r���[�A���X�V
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public void modify(Object element, String property, Object value) {
		// Find the index of the column
		int columnIndex	= propertyTable.getColumnNames().indexOf(property);

		TableItem item = (TableItem) element;
		PropertyItem propertyItem = (PropertyItem) item.getData();

		switch (columnIndex) {
			case 1:
//				propertyItem.setValue((String)value);
				break;
		}

		propertyTable.update(propertyItem);
	}
}
