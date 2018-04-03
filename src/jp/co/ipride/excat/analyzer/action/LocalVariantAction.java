/*
 * Error Anaylzer Tool for Java
 *
 * Created on 2006/4/1
 *
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.analyzer.action;

import java.net.URL;

import jp.co.ipride.excat.MainViewer;
import jp.co.ipride.excat.common.ApplicationResource;
import jp.co.ipride.excat.common.Message;
import jp.co.ipride.excat.common.action.BaseAction;
import jp.co.ipride.excat.common.icons.IconFilePathConstant;
import jp.co.ipride.excat.common.utility.ExcatMessageUtilty;
import jp.co.ipride.excat.common.utility.HelperFunc;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;


/**
 * ���[�J���ϐ��\���̐؂�ւ��A�N�V����
 * @author �j�̐V
 *
 */
public class LocalVariantAction extends BaseAction{

	//�����ȃ��[�J���ϐ��\���L��
	private boolean displayValidState=true;

	/**
	 * �f�B�t�H���g�͕\�����Ȃ�
	 * @param appWindow
	 */
	public LocalVariantAction(MainViewer appWindow){
		super(appWindow);
		try{
			setDisplayValidLocalVar();
		}catch(Exception e){
			HelperFunc.getLogger().debug(e);
		}
	}


	/**
	 * �؂�ւ�
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void doJob(){
		try{
			HelperFunc.getLogger().info(Message.get("LocalVariantAction.Start"));
			if (displayValidState){
				setDisplayAllLocalVar();
				this.appWindow.displayAllLocalVar();
			}else{
				setDisplayValidLocalVar();
				appWindow.displayValidLocalVar();
			}
			HelperFunc.getLogger().info(Message.get("LocalVariantAction.Start"));
		}catch(Exception e){
			ExcatMessageUtilty.showErrorMessage(appWindow.getShell(),e);
		}
	}

	/**
	 * �����ȃ��[���ϐ���\�����Ȃ�
	 * @throws Exception
	 */
	private void setDisplayValidLocalVar()throws Exception{
		displayValidState=true;
		String text = ApplicationResource.getResource("Menu.edit.displayInvalidVariable.Text");
		setText(text);
		this.setToolTipText(text);

		URL url = MainViewer.class.getResource(IconFilePathConstant.DISPLAY_INVALID_VARIABLE);
		setImageDescriptor(ImageDescriptor.createFromURL(url));
	}

	/**
	 * �����ȃ��[�J���ϐ���\������
	 * @throws Exception
	 */
	private void setDisplayAllLocalVar()throws Exception{
		displayValidState=false;
		String text = ApplicationResource.getResource("Menu.edit.ignoreInvalidVariable.Text");
		setText(text);
		this.setToolTipText(text);

		URL url = MainViewer.class.getResource(IconFilePathConstant.IGORE_INVALID_VARIABLE);
		setImageDescriptor(ImageDescriptor.createFromURL(url));
	}
}
