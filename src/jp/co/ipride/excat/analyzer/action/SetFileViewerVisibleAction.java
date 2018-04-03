package jp.co.ipride.excat.analyzer.action;

import java.net.URL;

import jp.co.ipride.excat.MainViewer;
import jp.co.ipride.excat.common.ApplicationResource;
import jp.co.ipride.excat.common.action.BaseAction;
import jp.co.ipride.excat.common.icons.IconFilePathConstant;
import jp.co.ipride.excat.common.utility.ExcatMessageUtilty;
import jp.co.ipride.excat.common.utility.HelperFunc;

import org.eclipse.jface.resource.ImageDescriptor;

public class SetFileViewerVisibleAction extends BaseAction {

	// �\��/��\�����
	private boolean visibleState = true;

	/**
	 * �R���X�g���N�^
	 *
	 * @param appWindow
	 */
	public SetFileViewerVisibleAction(MainViewer appWindow) {
		super(appWindow);
		try {
			// �K��l�͕\�������ԂƂ���
			setDisplayMenuInvisible();
		} catch (Exception e) {
			HelperFunc.getLogger().debug(e);
		}
	}

	/**
	 * �؂�ւ�
	 *
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void doJob() {
		try {
			if (visibleState) {
				// ��\�����������s
				((MainViewer) appWindow).setFileViewerVisible(false);
				visibleState = false;
				// ���j���[���u�\������v�ɐ؂芷����
				setDisplayMenuVisible();
			} else {
				// �\�����������s
				((MainViewer) appWindow).setFileViewerVisible(true);
				// ���j���[���u�\�����Ȃ��v�ɐ؂芷����
				visibleState = true;
				setDisplayMenuInvisible();
			}
		} catch (Exception e) {
			HelperFunc.getLogger().error("SetFileViewerVisibleAction", e);
			ExcatMessageUtilty.showErrorMessage(this.appWindow.getShell(),e);
		}
	}

	/**
	 * �u�t�@�C���r���[��\�����郁�j���[�v��ݒ肷��
	 *
	 * @throws Exception
	 */
	private void setDisplayMenuVisible() throws Exception {
		String text = ApplicationResource
				.getResource("Menu.View.showFileViewer.Text");
		setText(text);
		this.setToolTipText(text);

		URL url = MainViewer.class
				.getResource(IconFilePathConstant.FILE_VIEWER_VISIBLE);
		setImageDescriptor(ImageDescriptor.createFromURL(url));
	}

	/**
	 * �u�t�@�C���r���[��\�����Ȃ��v���j���[��ݒ肷��
	 *
	 * @throws Exception
	 */
	private void setDisplayMenuInvisible() throws Exception {
		String text = ApplicationResource
				.getResource("Menu.View.hideFileViewer.Text");
		setText(text);
		this.setToolTipText(text);

		URL url = MainViewer.class
				.getResource(IconFilePathConstant.FILE_VIEWER_INVISIBLE);
		setImageDescriptor(ImageDescriptor.createFromURL(url));
	}

}
