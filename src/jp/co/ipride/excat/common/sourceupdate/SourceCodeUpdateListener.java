package jp.co.ipride.excat.common.sourceupdate;

/**
 * ��M���i�޲�۰�ށj�������������
 * @author tu-ipride
 * @version 2.0
 * @date 2009/10/17
 */
public interface SourceCodeUpdateListener {

	public void updatingMsgNotified(String msg);

	public void updateCloseButton(boolean flag);

	public boolean updatingConfirm(String msg);
}
