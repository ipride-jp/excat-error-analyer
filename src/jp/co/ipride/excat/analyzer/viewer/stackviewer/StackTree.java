/*
 * Error Anaylzer Tool for Java
 *
 * Created on 2006/4/1
 *
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.analyzer.viewer.stackviewer;

import jp.co.ipride.excat.MainViewer;
import jp.co.ipride.excat.analyzer.action.finder.IFindStringProvider;
import jp.co.ipride.excat.analyzer.common.DumpDocument;
import jp.co.ipride.excat.analyzer.common.DumpFileXmlConstant;
import jp.co.ipride.excat.analyzer.function.pdf.PdfGenerator;
import jp.co.ipride.excat.analyzer.function.printer.TreeViewPrinter;
import jp.co.ipride.excat.common.utility.HelperFunc;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * this is a tree of dump data.
 *
 * @author GuanXH
 *
 */
public class StackTree implements IFindStringProvider {

	public static boolean displayAllLocalVar = false;

	static {
		System.getProperties().setProperty(
				"javax.xml.parsers.DocumentBuilderFactory",
				"org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
	}

	private MainViewer appWindow;

	private TreeViewer treeViewer;

	//document of viewer.
//	private Object object=null;
	private Document document = null;

	private String currentPath = null;

	/**
	 * construct
	 *
	 * @param appWindow
	 * @param parent
	 * @param style
	 */
	public StackTree(MainViewer appWindow, Composite parent, int style) {
		this.appWindow = appWindow;
		treeViewer = new TreeViewer(parent, style);
		treeViewer.setContentProvider(new StackContentProvider());
		treeViewer.setLabelProvider(new StackLabelProvider(this.appWindow));
		treeViewer.addFilter(new StackViewerFilter());
		treeViewer.getControl().setMenu(createPopupMenu().createContextMenu(treeViewer.getControl()));
		addSelectionListener();
	}

	private void addSelectionListener(){
		treeViewer.getTree().addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
			public void widgetSelected(SelectionEvent selectionevent) {
				//add by Qiu Song on 20090216 for StatusBar�ɃG���[���b�Z�[�W�̕\��
				appWindow.resetStatusErr();
				//end of add by Qiu Song on 20090216 for StatusBar�ɃG���[���b�Z�[�W�̕\��
				Tree tree = StackTree.this.treeViewer.getTree();
				if(tree.getSelectionCount() == 1) {
					TreeItem item = tree.getSelection()[0];
					Node node=(Node)item.getData();
					if (checkType(node)){
						appWindow.getObjectReferenceAction().setEnabled(true);
					}else{
						appWindow.getObjectReferenceAction().setEnabled(false);
					}
				}
			}
		});
		
		//add by Qiu Song on 20090216 for StatusBar�ɃG���[���b�Z�[�W�̕\��
		treeViewer.getTree().addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e) {
			}
			public void focusLost(FocusEvent e) {
				appWindow.resetStatusErr();
			}
		});
		
		treeViewer.getTree().addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				appWindow.resetStatusErr();
			}
		});
		
		treeViewer.getTree().addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent arg0) {
			}

			public void mouseDown(MouseEvent arg0) {
				appWindow.resetStatusErr();				
			}

			public void mouseUp(MouseEvent arg0) {
			}
		});
		//end of add by Qiu Song on 20090216 for StatusBar�ɃG���[���b�Z�[�W�̕\��
	}

	/**
	 * �_���v�t�@�C���iXML�j��ǂݍ����Tree�ɃZ�b�g����B
	 * @param path�@�_���v�t�@�C���̃p�[�X�iXML�j
	 */
	public boolean  setXmlFile(String path) throws Exception{
		if (path == null) {
			return false;
		}
		this.currentPath = path;
		boolean ret = DumpDocument.createDocument(path);
		if(ret){
			document = DumpDocument.getDocument();
			treeViewer.setInput(document);
			appWindow.setPdfPrintState(true);

			// sai 2009/10/21
			// Dom��̓N���X������������B
			appWindow.analyzerform.propertyTable.initDomBuilder();
		}

		return ret;
	}

	/**
	 * Tree����PDF�o��
	 * @param appWindow�@�A�v���P�V�����E�B���h�E
	 * @return �Ȃ�
	 * @throws Exception
	 */
	public void outputToPdf(MainViewer appWindow) throws Exception{
		Tree tr = treeViewer.getTree();

		// �c���[�̓��e�͋�̏ꍇ�APDF���o�͂��Ȃ��B
		if(tr == null || tr.getItems().length == 0){
	    	return;
	    }
		TreeItem tiRoot = tr.getItem(0);
		new PdfGenerator(tiRoot,appWindow);
	}

	/**
	 * expand first layer of dump file.
	 * add by tu for bug no.34
	 * 2007.4.30
	 */
	public void expandTopNode(){
		treeViewer.expandToLevel(3);
	}

	/**
	 * close call.
	 * @return
	 */
	public boolean closeXmlFile() {
		if (document != null){
			treeViewer.remove(document);
			document = null;
			DumpDocument.clear();
		}
		currentPath = null;
		appWindow.setPdfPrintState(false);
		System.gc();
		return true;
	}

	/**
	 * add a listner to tree.
	 * @param listener
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		treeViewer.addSelectionChangedListener(listener);
	}

	public void addDoubleClickListener(IDoubleClickListener listener){
		treeViewer.addDoubleClickListener(listener);
	}
	/**
	 * ignore invalid call.
	 *
	 */
	public void displayValidLocalVar(){
		displayAllLocalVar = false;
		treeViewer.refresh();
	}

	/**
	 * display Invalid Variable call.
	 *
	 */
	public void displayAllLocalVar(){
		displayAllLocalVar = true;
		treeViewer.refresh();
	}

	/**
	 * StackTree�̃|�b�v�A�b�v���j���[�𐶐����܂��B�v
	 * @return �����������j���[�}�l�[�W��
	 */
	protected MenuManager createPopupMenu(){
		// �|�b�v�A�b�v���j���[�𐶐�
		MenuManager mm = new MenuManager("#PopupMenu");
		mm.setRemoveAllWhenShown(true);
		mm.addMenuListener(new IMenuListener(){
			public void menuAboutToShow(IMenuManager manager) {
				Tree tree = StackTree.this.treeViewer.getTree();
				if(tree.getSelectionCount() == 1) {
					TreeItem item = tree.getSelection()[0];
					Node node=(Node)item.getData();
					if (checkType(node)){
						manager.add(appWindow.getObjectReferenceAction());
					}
				}
			}
		});
		return mm;
	}

	/**
	 * �w�肳�ꂽ�m�[�g�̓I�u�W�F�N�g�ł��邩���`�F�b�N����B
	 * @version 3.0
	 * @date 2009/10/7 tu
	 * @param node ����Ώۃm�[�h
	 * @return <code>true</code>:�擾�Ώۂ̃m�[�h, <code>false</code>:�擾�ΏۊO�̃m�[�h
	 */
	protected boolean checkType(Node node){
		if(!DumpFileXmlConstant.NODE_ARGUMENT.equals(node.getNodeName())
			&& !DumpFileXmlConstant.NODE_VARIABLE.equals(node.getNodeName())
			&& !DumpFileXmlConstant.NODE_THIS.equals(node.getNodeName())
			&& !DumpFileXmlConstant.NODE_ITEM.equals(node.getNodeName())
			&& !DumpFileXmlConstant.NODE_ATTRIBUTE.equals(node.getNodeName())) {
			return false;
		}
		NamedNodeMap attrMap = node.getAttributes();
		Node typeNode = attrMap.getNamedItem(DumpFileXmlConstant.ATTR_DEF_TYPE);
		String type = typeNode.getTextContent();
		if ( "boolean".equals(type)
			|| "byte".equals(type)
			|| "int".equals(type)
			|| "long".equals(type)
			|| "char".equals(type)	){
			return false;
		}
		return true;
	}

//	/**
//	 * �I�𒆂̃m�[�h���猟���Ώۂ̕�������擾���܂��B
//	 * @return �����Ώۂ̕�����
//	 */
//	public String getFindString() {
//		String target = null;
//		Tree tree = StackTree.this.treeViewer.getTree();
//		if(tree.getSelectionCount() == 1) {
//			Node node = (Node)tree.getSelection()[0].getData();
//			if(isFindStringProviderNode(node)){
//				target = CommonNodeLabel.getText(node);
//			}
//		}
//		return target;
//	}

	/**
	 * ���
	 */
	public void print() {
		Tree tr = treeViewer.getTree();

		// �c���[�̓��e�͋�̏ꍇ�APDF���o�͂��Ȃ��B
		if(tr == null || tr.getItems().length == 0){
	    	return;
	    }
		TreeItem tiRoot = tr.getItem(0);
		TreeViewPrinter printer = new TreeViewPrinter(tiRoot, appWindow.getShell());
		printer.print();
//		PrintPage page = new PrintPage(tiRoot);
//		page.actionPerformed(null);
	}

	public String getFindString() {
		return null;
	}

	public TreeViewer getTreeViewer(){
		return treeViewer;
	}

	public Document getDocument(){
		return document;
	}

	public String getPath(){
		return this.currentPath;
	}
}
