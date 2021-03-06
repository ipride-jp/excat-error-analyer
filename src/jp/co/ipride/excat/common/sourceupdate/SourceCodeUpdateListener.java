package jp.co.ipride.excat.common.sourceupdate;

/**
 * 受信側（ﾀﾞｲｱﾛｰｸﾞ）が実装するもの
 * @author tu-ipride
 * @version 2.0
 * @date 2009/10/17
 */
public interface SourceCodeUpdateListener {

	public void updatingMsgNotified(String msg);

	public void updateCloseButton(boolean flag);

	public boolean updatingConfirm(String msg);
}
