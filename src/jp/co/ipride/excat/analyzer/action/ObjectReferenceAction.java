package jp.co.ipride.excat.analyzer.action;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.TreeItem;

import jp.co.ipride.excat.MainViewer;
import jp.co.ipride.excat.common.ApplicationResource;
import jp.co.ipride.excat.common.action.BaseAction;
import jp.co.ipride.excat.common.icons.IconFilePathConstant;

/**
 * �I�u�W�F�N�g�Q�Ƃ̃A�N�V����
 * @author tu-ipride
 * @version 3.0
 * @date 2009/10/7
 */
public class ObjectReferenceAction extends BaseAction{

	public ObjectReferenceAction(MainViewer appWindow) {
		super(appWindow);
		String text = ApplicationResource.getResource("PopupMenu.StackViewer.Reference");
		setText(text);
		//modified by Qiu Song on 20091215 for �ĩ`��Щ`�α�ʾ����
		String toolTipText = ApplicationResource
		.getResource("PopupMenu.StackViewer.Reference.ToolTip");
		this.setToolTipText(toolTipText);
        //end of modified by Qiu Song on 20091215 for �ĩ`��Щ`�α�ʾ����
		URL url = MainViewer.class.getResource(IconFilePathConstant.OBJECT_REFERENCE);
		setImageDescriptor(ImageDescriptor.createFromURL(url));
		this.setEnabled(false);
	}

	@Override
	public void doJob() throws Throwable {
		TreeItem[] items = appWindow.analyzerform.stackTree.getTreeViewer().getTree().getSelection();
		if (items == null || items.length ==0){
			this.setEnabled(false);
		}else{
			TreeItem item = items[0];
			appWindow.analyzerform.searchReferenceObject(item);
		}
	}

}
