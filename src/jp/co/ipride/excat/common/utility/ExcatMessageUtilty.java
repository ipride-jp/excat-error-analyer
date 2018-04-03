package jp.co.ipride.excat.common.utility;

import jp.co.ipride.excat.common.dialog.ExcatConfirmDialog;
import jp.co.ipride.excat.common.dialog.ExcatMessageDialog;

import org.eclipse.swt.widgets.Shell;

/**
 * �S�Ẵ��b�Z�[�W�E�_�C�A���[�O
 * @author tu-ipride
 * @version 2.0
 * @date 2009/10/17 update
 */
public class ExcatMessageUtilty {

	/**
	 * ��O���̃��b�Z�[�W�\��
	 * @param e
	 */
	public static void showErrorMessage(Shell shell,Throwable e) {
		HelperFunc.logException(e);
//		showMessage(shell, e.toString());
		ExcatMessageDialog dialog = new ExcatMessageDialog(shell,e.toString());
		dialog.open();
	}

	/**
	 * ��ʂ̃��b�Z�[�W�\��
	 * @param msg
	 */
	public static void showMessage(Shell shell, String msg) {
//		try{
//			URL url = MainViewer.class.getResource(IconFilePathConstant.EXCAT_TM_SMALL_16);
//			org.eclipse.jface.dialogs.MessageDialog message = new org.eclipse.jface.dialogs.MessageDialog(
//					null, Message.get("MessageDialog.Title"),
//					ImageDescriptor.createFromURL(url).createImage(), msg, 0,
//					new String[] { Message.get("MessageDialog.Button") }, 1);
//			message.open();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		ExcatMessageDialog dialog = new ExcatMessageDialog(shell,msg);
		dialog.open();
	}


	/**
	 * ���[�U�[�Ɋm�F�_�C�A���O�{�b�N�X��\������
	 * @param title�@�^�C�g��
	 * @param msg�@���b�Z�[�W���e
	 * @return ���[�U�̑I��
	 */
	public static boolean showConfirmDialogBox(Shell shell,String msg){
//		MessageBox msgBox = new MessageBox(parent,SWT.YES|SWT.NO);
//		msgBox.setMessage(msg);
//		msgBox.setText(title);
//		int result = msgBox.open();

		ExcatConfirmDialog dialog = new ExcatConfirmDialog(shell, msg);
		dialog.open();
		return dialog.result;
	}
}