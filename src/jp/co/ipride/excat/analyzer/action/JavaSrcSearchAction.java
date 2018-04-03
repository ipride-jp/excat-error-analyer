package jp.co.ipride.excat.analyzer.action;

import java.net.URL;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;

import jp.co.ipride.excat.MainViewer;
import jp.co.ipride.excat.analyzer.dialog.search.JavaSrcSearchDialog;
import jp.co.ipride.excat.analyzer.viewer.searchviewer.ConditionUnit;
import jp.co.ipride.excat.common.ApplicationResource;
import jp.co.ipride.excat.common.action.BaseAction;
import jp.co.ipride.excat.common.icons.IconFilePathConstant;

/**
 * ���|�W�g���ɂ���Java�\�[�X��ΏۂɌ�������B
 * @author tu-ipride
 * @version 3.0
 * @date 2009/10/27
 */
public class JavaSrcSearchAction extends BaseAction {

	private ConditionUnit conditionHistory = null;

	public JavaSrcSearchAction(MainViewer appWindow) {
		super(appWindow);
		String text = ApplicationResource.getResource("Menu.support.JavaSrcSearch");
		setText(text);
		//modified by Qiu Song on 20091215 for �ĩ`��Щ`�α�ʾ����
		String toolTipText = ApplicationResource
		.getResource("Menu.support.JavaSrcSearch.ToolTip");
		this.setToolTipText(toolTipText);
        //end of modified by Qiu Song on 20091215 for �ĩ`��Щ`�α�ʾ����
		URL url = MainViewer.class.getResource(IconFilePathConstant.SVN_REF);
		setImageDescriptor(ImageDescriptor.createFromURL(url));
		setDisabledImageDescriptor(ImageDescriptor.createFromURL(url));
	}

	@Override
	public void doJob() throws Throwable {
		JavaSrcSearchDialog dlg = new JavaSrcSearchDialog(appWindow);
		dlg.setHistoryContidition(conditionHistory);
		if (dlg.open()==Dialog.OK){
			conditionHistory = dlg.getHistoryCondition();
			appWindow.analyzerform.searchText(dlg.getTextSearchUnit());
		}
	}

}
