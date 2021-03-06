package jp.co.ipride.excat.analyzer.action;

import java.net.URL;

import jp.co.ipride.excat.MainViewer;
import jp.co.ipride.excat.common.ApplicationResource;
import jp.co.ipride.excat.common.action.BaseAction;
import jp.co.ipride.excat.common.dialog.LicenseDialog;
import jp.co.ipride.excat.common.icons.IconFilePathConstant;
import jp.co.ipride.excat.common.utility.HelperFunc;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * ライセンスのアクション
 * @author 盛申
 * @since 2006/12/20
 *         2006/12/24 屠　アクション、アイコンの追加
 */
public class LicenseAction extends BaseAction {

	public LicenseAction(MainViewer appWindow) {
		super(appWindow);
		try {
			String text = ApplicationResource.getResource("Menu.License.Text");
			setText(text);
			//modified by Qiu Song on 20091215 for ･ﾄｩ`･�･ﾐｩ`､ﾎｱ�ﾊｾｸﾄﾉﾆ
			String toolTipText = ApplicationResource
			.getResource("Menu.License.ToolTip");
			this.setToolTipText(toolTipText);
            //end of modified by Qiu Song on 20091215 for ･ﾄｩ`･�･ﾐｩ`､ﾎｱ�ﾊｾｸﾄﾉﾆ

			URL url =
			MainViewer.class.getResource(IconFilePathConstant.LICENSE);
			setImageDescriptor(ImageDescriptor.createFromURL(url));
		} catch (Exception e) {
			HelperFunc.getLogger().error("LicenseAction", e);
		}
	}

	public void doJob() {
		LicenseDialog dialog = new LicenseDialog(appWindow.getShell());
		dialog.open();
	}
}
