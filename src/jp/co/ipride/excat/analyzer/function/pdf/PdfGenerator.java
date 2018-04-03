/*
 * Error Anaylzer Tool for Java
 *
 * Created on 2007/10/05
 *
 * Copyright (c) 2006-2009 iPride Co.,Ltd.
 * All rights reserved.
 */
package jp.co.ipride.excat.analyzer.function.pdf;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import jp.co.ipride.excat.MainViewer;
import jp.co.ipride.excat.analyzer.common.DumpFileXmlConstant;
import jp.co.ipride.excat.common.Message;
import jp.co.ipride.excat.common.icons.IconFilePathConstant;
import jp.co.ipride.excat.common.setting.SettingManager;
import jp.co.ipride.excat.common.utility.ExcatMessageUtilty;
import jp.co.ipride.excat.common.utility.HelperFunc;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TreeItem;
import org.w3c.dom.Node;

import com.ibm.icu.util.StringTokenizer;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * PDF�ւ̏o�͗p�N���X
 *
 * @version 1.0 2007/10/05
 * @author �� ����
 */

public class PdfGenerator {

	/**
	 * �O���[�o���ϐ��̐錾
	 */
	private final static String INDENX_SPACE = "   "; // �e�Ǝq�m�[�h�Ԃ̕�����̃C���f���g

	private final static String FONT_REGULAR = "KozMinPro-Regular"; // �t�H���g

	private final static String FONT_LANGUGE = "UniJIS-UCS2-H"; // �G���R�[�f�B���O��

	private final static int INDENT = 20; // ���Ɛ��̉��Ԋu

	private final static int LINE_LENGTH = 12; // �����̒���

	private final static int LINE_HEIGHT = 18; // �c���̍���(�s�̍���)

	private final static int PLUS_AND_MINUS_LOCAL_X = 5; // "+"��"-"�A�C�R���͐��Ƃ̑���X���ʒu

	private final static int PLUS_AND_MINUS_LOCAL_Y = 5; // "+"��"-"�A�C�R���͐��Ƃ̑���Y���ʒu

	private final static int START_Y = 42; // ��Ԃ̐���Y���ł̐��l

	private final static int START_X = 45; // ��Ԃ̐���X���ł̐��l

	private int intstartY = 42; // �e�m�[�h��Y���ł̐��l

	private int newPageFlag = 1; // ���y�[�W�̂��ǂ����̃t���O

	private int intPageMaxLine = 41; // �y�[�W�̍ő�s��

	private PdfWriter writer = null; // Pdf�̏�����

	private Document document = null; // pdf�o�͗p�̃h�L�������g

	private BasicStroke bs = null; // BasicStroke�̐錾

	private Font ��ontJapan = null; // �����̃t�H���g(���{��)

	private Paragraph paragraph = null; // �p���O���t�̐錾

	private Graphics2D g2d = null; // �O���t�B�N�X�̐錾

	private BaseFont bf = null; // �t�H���g

	private Rectangle recSelected = null; // �y�[�W�T�C�Y��ݒ�p�̒����`

	private ArrayList<String> alLineSize = new ArrayList<String>(); // ���ׂĂ̍s�̃T�C�Y�̊i�[

	private MainViewer appWindow;

	/**
	 * �R���X�g���N�^
	 *
	 * @param tiRoot
	 *            PDF�o�̓��[�g�m�[�h
	 * @param appWindow
	 *            �A�v���P�V�����E�B���h�E
	 * @return �Ȃ�
	 * @throws Exception
	 */
	public PdfGenerator(TreeItem tiRoot, MainViewer appWindow) throws Exception {
		this.appWindow = appWindow;

		// PDF�ۑ���t�H���_�p�X�̎擾
		String strPdfPath = getPdfOutPath(appWindow);
		// PDF�ۑ���t�H���_��I�����Ȃ��ꍇ�APDF�t�@�C�����쐬���Ȃ��B
		if (strPdfPath == null || "".equals(strPdfPath)) {
			return;
		}
		//�t�@�C���̑��݃`�F�b�N
		File file = new File(strPdfPath);
		if(file.exists()){
			//���[�U�Ɋm�F
			boolean ret = ExcatMessageUtilty.showConfirmDialogBox(
					appWindow.getShell(),
					Message.getMsgWithParam("File.exist",strPdfPath));

			if(!ret ){
				return;
			}
		}

		appWindow.getShell().setCursor(new Cursor(null, SWT.CURSOR_WAIT));
		// ������̓��{��̃t�H���g��`�悷��B
		��ontJapan = setFont(PdfGenerator.FONT_REGULAR,
				PdfGenerator.FONT_LANGUGE, BaseFont.NOT_EMBEDDED);
		document = new Document(PageSize.A4);
		setPageSize(tiRoot);
		document = new Document(recSelected);
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(
					strPdfPath));
		} catch (Exception ex) {
			// �I�������p�X�̃t�H���_�͓ǂݎ���p�̏ꍇ�A
			// �܂��́A�t�@�C�����J�����ꍇ�A�G���[���b�Z�[�W�_�C�A���O��\������B
			ExcatMessageUtilty.showMessage(
					appWindow.getShell(),
					Message.get("DirectoryDialog.Pdf.Path"));
			return;
		}
		// �h�L�������g���I�[�v������B
		document.open();

		// �L�����o�X���擾����B
		PdfContentByte cb = writer.getDirectContent();
		g2d = cb
				.createGraphics(recSelected.getWidth(), recSelected.getHeight());
		bs = setLineStyle(0.5f);
		// ���̃X�^�C����ݒ肷��B
		g2d.setStroke(bs);
		outputPdfInit(tiRoot);
		g2d.dispose();
		document.add(paragraph);
		// �h�L�������g���N���[�Y����B
		document.close();
		// PDF�o�͊����̊m�F���b�Z�[�W��\������B
		ExcatMessageUtilty.showMessage(
				appWindow.getShell(),
				Message.get("Dialog.Pdf.OK.Text"));
	}

	/**
	 * ���̕`��
	 *
	 * @param parent
	 *            �m�[�h
	 * @param array
	 *            �m�[�h���
	 * @param intstartX
	 *            ����X�����l
	 * @param intstartY
	 *            ����Y�����l
	 * @param i
	 *            �m�[�h�ԍ�
	 * @return �Ȃ�
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws DocumentException
	 */
	private void drawNodeLine(TreeItem parent, ArrayList<String> array, int intstartX,
			int intstartY, int i) {
		int intEndX = intstartX;
		int intEndY = intstartY + PdfGenerator.LINE_HEIGHT;
		// ��ԉ��̃m�[�h�̏ꍇ�A"��"�̂悤�Ȑ���`�悷��B
		// ��L�ȊO�̏ꍇ�A"��"�̂悤�Ȑ���`�悷��B
		if (i == parent.getItems().length - 1) {
			int intEndX2 = intstartX;
			int intEndY2 = intstartY + PdfGenerator.LINE_HEIGHT / 2;
			int intstartX3 = intstartX;
			int intstartY3 = intEndY2;
			int intEndX3 = intstartX + PdfGenerator.LINE_LENGTH;
			int intEndY3 = intstartY3;
			g2d.drawLine(intstartX, intstartY, intEndX2, intEndY2);
			g2d.drawLine(intstartX3, intstartY3, intEndX3, intEndY3);
		} else {
			int intstartX4 = intstartX;
			int intstartY4 = (intstartY + intEndY) / 2;
			int intEndX4 = intstartX + PdfGenerator.LINE_LENGTH;
			int intEndY4 = intstartY4;
			g2d.drawLine(intstartX, intstartY, intEndX, intEndY);
			g2d.drawLine(intstartX4, intstartY4, intEndX4, intEndY4);
		}
		if (array == null || array.size() == 0) {
			array.add("0");
		}
		// �Y���m�[�h�̑O�̈�Ԑ��̈ȍ~�̐�"��"��`�悷��B
		int k = 1;
		for (int j = array.size() - 1; 0 <= j; j--) {
			if (array.get(j).equals("1")) {
				int intstartX5 = intstartX - k * PdfGenerator.INDENT;
				int intstartY5 = intstartY;
				int intEndX5 = intstartX5;
				int intEndY5 = intEndY;
				g2d.drawLine(intstartX5, intstartY5, intEndX5, intEndY5);
			}
			k++;
		}
	}

	/**
	 * "+"��"-"�A�C�R���̕`��
	 *
	 * @param parent
	 *            �m�[�h�A�C�e��
	 * @param intstartX
	 *            ����X�����l
	 * @param intstartY
	 *            ����Y�����l
	 * @return �Ȃ�
	 */
	private void drawPlusMinusIcon(TreeItem childTrItem, int intstartX,
			int intstartY) {
		Node targetNode = (Node) childTrItem.getData();

		if (targetNode.getChildNodes().getLength() != 0) {
			java.awt.Image imageManasuTasu = null;
			if (childTrItem.getExpanded()) {
				imageManasuTasu = Toolkit.getDefaultToolkit().getImage(
						MainViewer.class
								.getResource(IconFilePathConstant.TREE_MINUS));
				g2d.drawImage(imageManasuTasu, intstartX
						- PdfGenerator.PLUS_AND_MINUS_LOCAL_X, intstartY
						+ PdfGenerator.LINE_HEIGHT / 2
						- PdfGenerator.PLUS_AND_MINUS_LOCAL_Y, null);

			} else {
				imageManasuTasu = Toolkit.getDefaultToolkit().getImage(
						MainViewer.class
								.getResource(IconFilePathConstant.TREE_PLUS));
				g2d.drawImage(imageManasuTasu, intstartX
						- PdfGenerator.PLUS_AND_MINUS_LOCAL_X, intstartY
						+ PdfGenerator.LINE_HEIGHT / 2
						- PdfGenerator.PLUS_AND_MINUS_LOCAL_Y, null);
			}
		}
	}

	/**
	 * �c�[���m�[�h�̃A�C�R���̃p�X�̎擾
	 *
	 * @param targetNode
	 *            �m�[�h
	 * @return �A�C�R���̃p�X
	 */
	private URL getIconUrl(Node targetNode) {
		URL url = null;
		// �m�[�h�̓��[�g�m�[�h�̏ꍇ�A�_���v�̃A�C�R����URL��ԋp����B
		if (DumpFileXmlConstant.NODE_DUMP.equalsIgnoreCase(targetNode
				.getNodeName())) {
			url = MainViewer.class.getResource(IconFilePathConstant.TREE_DUMP);
			return url;
		}
		// �m�[�h�̓X�^�b�N�g���[�X�m�[�h�̏ꍇ�A�X�^�b�N�g���[�X��URL��ԋp����B
		if (DumpFileXmlConstant.NODE_STACKTRACE.equalsIgnoreCase(targetNode
				.getNodeName())) {

			url = MainViewer.class
					.getResource(IconFilePathConstant.TREE_STACKTRACE);
			return url;
		}
		// �m�[�h�̓��b�\�h�m�[�h�̏ꍇ�A���b�\�h�̃A�C�R����URL��ԋp����B
		if (DumpFileXmlConstant.NODE_METHOD.equalsIgnoreCase(targetNode
				.getNodeName())) {
			url = MainViewer.class
					.getResource(IconFilePathConstant.TREE_METHOD);
			return url;
		}
		// �m�[�h�͕ϐ��m�[�h�̏ꍇ�A�ϐ��̃A�C�R����URL��ԋp����B
		if (DumpFileXmlConstant.NODE_VARIABLE.equalsIgnoreCase(targetNode
				.getNodeName())) {
			url = MainViewer.class
					.getResource(IconFilePathConstant.TREE_VARIABLE);
			return url;
		}
		// �m�[�h�̓X�[�p�[�N���X�m�[�h�̏ꍇ�A�X�[�p�[�N���X�̃A�C�R����URL��ԋp����B
		if (DumpFileXmlConstant.NODE_SUPERCLASS.equalsIgnoreCase(targetNode
				.getNodeName())) {
			url = MainViewer.class
					.getResource(IconFilePathConstant.TREE_SUPERCLASS);
			return url;
		}
		// �m�[�h�͑����m�[�h�̏ꍇ�A�����̃A�C�R����URL��ԋp����B
		if (DumpFileXmlConstant.NODE_ATTRIBUTE.equalsIgnoreCase(targetNode
				.getNodeName())) {
			url = MainViewer.class
					.getResource(IconFilePathConstant.TREE_ATTRIBUTE);
			return url;
		}
		// �m�[�h�̓p�����[�^�m�[�h�̏ꍇ�A�p�����[�^�̃A�C�R����URL��ԋp����B
		if (DumpFileXmlConstant.NODE_ARGUMENT.equalsIgnoreCase(targetNode
				.getNodeName())) {
			url = MainViewer.class
					.getResource(IconFilePathConstant.TREE_ARGUMENT);
			return url;
		}

         //�m�[�h��this�̏ꍇ�Athis�̃A�C�R����URL��ԋp����B
		if (DumpFileXmlConstant.NODE_THIS.equalsIgnoreCase(targetNode
				.getNodeName())) {
			url = MainViewer.class
					.getResource(IconFilePathConstant.TREE_THIS);
			return url;
		}

		// �m�[�h�͍��ڃm�[�h�̏ꍇ�A���ڂ̃A�C�R����URL��ԋp����B
		if (DumpFileXmlConstant.NODE_ITEM.equalsIgnoreCase(targetNode
				.getNodeName())) {
			url = MainViewer.class.getResource(IconFilePathConstant.TREE_ITEM);
			return url;
		}
		//�@ �m�[�h�̓C���X�^���X�m�[�h�̏ꍇ�A�C���X�^���X�̃A�C�R����URL��ԋp����B
		if (DumpFileXmlConstant.NODE_INSTANCE.equalsIgnoreCase(targetNode
				.getNodeName())) {
			url = MainViewer.class.getResource(IconFilePathConstant.INSTANCE_ITEM);
			return url;
		}
		// �m�[�h�̓��j�^�[�I�u�W�F�N�g�m�[�h�̏ꍇ�A���j�^�[�I�u�W�F�N�g�̃A�C�R����URL��ԋp����B
		if (DumpFileXmlConstant.NODE_MONITOROBJECT.equalsIgnoreCase(targetNode
				.getNodeName())) {
			url = MainViewer.class.getResource(IconFilePathConstant.MONITOROBJECT_ITEM);
			return url;
		}

        //�m�[�h��this�̏ꍇ�AMonitor Object�̃A�C�R����URL��ԋp����B
		if (DumpFileXmlConstant.NODE_MONITOROBJECT.equalsIgnoreCase(targetNode
				.getNodeName())) {
			url = MainViewer.class
					.getResource(IconFilePathConstant.MONITOROBJECT_ITEM);
			return url;
		}

        //�m�[�h��this�̏ꍇ�AInstance�̃A�C�R����URL��ԋp����B
		if (DumpFileXmlConstant.NODE_INSTANCE.equalsIgnoreCase(targetNode
				.getNodeName())) {
			url = MainViewer.class
					.getResource(IconFilePathConstant.INSTANCE_ITEM);
			return url;
		}
		return url;
	}

	/**
	 * ���ׂẴm�[�h�̏��̐ݒ�
	 *
	 * @param parent
	 *            �m�[�h
	 * @param strSpace
	 *            �e�Ǝq�m�[�h�̃C���f���g
	 * @param intstartX
	 *            ����X�����l
	 * @return �Ȃ�
	 * @throws DocumentException
	 */
	private void getLineDate(TreeItem parent, String strSpace, int intstartX)
			throws MalformedURLException, IOException, DocumentException {
		for (int i = 0; i < parent.getItems().length; i++) {
			String strIndenx = strSpace + PdfGenerator.INDENX_SPACE;
			Node targetNode = (Node) parent.getItem(i).getData();
			URL url = null;
			if (targetNode != null) {
				url = getIconUrl(targetNode);
			}
			if (url != null) {
				// PDF�̍s�̃T�C�Y���i�[����B
				saveLineSize(parent.getItem(i), intstartX);
			}
			if (parent.getItem(i).getExpanded()) {
				// �q�m�[�h�𑀍삷��B
				getLineDate(parent.getItem(i), strIndenx
						+ PdfGenerator.INDENX_SPACE, intstartX
						+ PdfGenerator.INDENT);
			}
		}
	}

	/**
	 * �s�̓��e�̍ő�T�C�Y�̎擾
	 *
	 * @param �Ȃ�
	 * @return �Ȃ�
	 */
	private float getMaxLineSize() {
		float fMaxSize = (float) 0.0;
		String str = "";
		boolean bol = false;
		// �e�m�[�h�̍ő�T�C�Y�������Ń\�[�g�������B
		for (int j = 0; j < alLineSize.size() - 1; j++) {
			bol = false;
			for (int i = 0; i < alLineSize.size() - 1; i++) {
				String str0 = (String) alLineSize.get(i);
				String str1 = (String) alLineSize.get(i + 1);
				if (Float.parseFloat(str0) > Float.parseFloat(str1)) {
					str = alLineSize.get(i).toString();
					alLineSize.set(i, alLineSize.get(i + 1));
					alLineSize.set(i + 1, str);
					bol = true;
				}
			}
			if (!bol) {
				break;
			}
		}
		// �m�[�h�̍ő�T�C�Y�Ƀ\�[�g�����z��̍Ō�̌��f��ݒ肷��B
		fMaxSize = Float.parseFloat(alLineSize.get(alLineSize.size() - 1)
				.toString());
		return fMaxSize;
	}

	/**
	 * Dialog��PDF�t�@�C�������p�X�̎擾
	 *
	 * @param appWindow
	 *            �A�v���P�V�����̃E�C���h�E
	 * @return �t�@�C���ۑ��̃p�X
	 */
	private String getPdfOutPath(ApplicationWindow appWindow) {
		FileDialog dialog = new FileDialog(appWindow.getShell(), SWT.SAVE);
		dialog.setFilterExtensions(new String[] { "*.pdf" });
		String currentPdfPath = SettingManager.getSetting().getCurrentPdfPath();
		if (currentPdfPath != null) {
			File file = new File(currentPdfPath);
			if (file.isFile()) {
				currentPdfPath = currentPdfPath.substring(0, currentPdfPath.indexOf(file.getName()));
			}
		}
		if (currentPdfPath != null) {
			dialog.setFilterPath(currentPdfPath);
		}
		String path = dialog.open();
		if (path == null) {
			return null;
		}
		SettingManager.getSetting().setCurrentPDFPath(path);
		return path;
	}

	/**
	 * PDF�t�@�C���ւ̏o��
	 *
	 * @param parent
	 *            �m�[�h
	 * @param strSpace
	 *            �e�Ǝq�m�[�h�̃C���f���g
	 * @param intstartX
	 *            ����X�����l
	 * @param array
	 *            �m�[�h���
	 * @return �Ȃ�
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws DocumentException
	 */
	private void outputPdfContents(TreeItem parent, String strSpace,
			int intstartX, ArrayList<String> array) throws MalformedURLException,
			IOException, DocumentException {
		for (int i = 0; i < parent.getItems().length; i++) {
			// �y�[�W�ɕ������41�s�𒴂����ꍇ�A���y�[�W���s���B
			SetNewPage(newPageFlag);
			String strIndenx = strSpace + PdfGenerator.INDENX_SPACE;
			intstartY = intstartY + PdfGenerator.LINE_HEIGHT;
			Node targetNode = (Node) parent.getItem(i).getData();
			URL url = null;
			if (targetNode != null) {
				url = getIconUrl(targetNode);
			}
			if (url != null) {
				// ������ƃA�C�R�����o�͂���B
				outputCharacter(url, strIndenx, parent.getItem(i), intstartX);
				// ����`�悷��B
				drawNodeLine(parent, array, intstartX, intstartY, i);
				// "+"��"-"�A�C�R����`�悷��B
				drawPlusMinusIcon(parent.getItem(i), intstartX, intstartY);
				newPageFlag++;
			}
			if (parent.getItem(i).getExpanded()) {

				ArrayList<String> cloneAl = (ArrayList<String>) array.clone();
				// �m�[�h�̏�Ԃ�cloneAl�Ɋi�[����B
				if (i == parent.getItems().length - 1) {
					cloneAl.add("0");
				} else {
					cloneAl.add("1");
				}
				// �q�m�[�h��`�悷��B
				outputPdfContents(parent.getItem(i), strIndenx
						+ PdfGenerator.INDENX_SPACE, intstartX
						+ PdfGenerator.INDENT, cloneAl);
			}
		}
	}

	/**
	 * ������ƃA�C�R���̏o��
	 *
	 * @param url
	 *            �A�C�R����URL
	 * @param strIndenx
	 *            ������̍����̗]��
	 * @param childTi
	 *            �q�m�[�h
	 * @param intstartX
	 *            ����X�����l
	 * @return �Ȃ�
	 * @throws BadElementException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private void outputCharacter(URL url, String strIndenx, TreeItem childTi,
			int intstartX) throws BadElementException, MalformedURLException,
			IOException {
		Paragraph objParag = new Paragraph();
		Image image1 = Image.getInstance(url);
		Chunk ch1 = new Chunk(strIndenx);
		Chunk ch2 = new Chunk(image1, 1, -5, true);
		Chunk ch3 = new Chunk(" " + childTi.getText().replaceAll("\r\n", ""), ��ontJapan);
		objParag.add(ch1);
		objParag.add(ch2);
		objParag.add(ch3);
		paragraph.add(objParag);
	}

	/**
	 * PDF�t�@�C���ւ̏o�͂̏������ݒ�
	 *
	 * @param parent
	 *            �m�[�h
	 * @return �Ȃ�
	 * @throws IOException
	 * @throws Exception
	 */
	private void outputPdfInit(TreeItem parent) throws Exception {
		//�A�C�R��URL�̎擾
		Node targetNode = (Node) parent.getData();
		URL url = getIconUrl(targetNode);

		//�A�C�R���ƕ�����̏o��
		paragraph = new Paragraph("");
		Paragraph childParagraph = new Paragraph();
		Image image = Image.getInstance(url);
		childParagraph.add(new Chunk(image, 1, -5, true));
		childParagraph.add(new Chunk(" " + parent.getText().replaceAll("\r\n", ""), ��ontJapan));
		paragraph.add(childParagraph);

		// ���[�g�m�[�h�̐���`�悷��B
		int intstartRootX = PdfGenerator.START_X - PdfGenerator.INDENT;
		int intstartRootY = intstartY;
		int intEndRootX = intstartRootX + PdfGenerator.LINE_LENGTH;
		int intEndRootY = intstartRootY;
		g2d.drawLine(intstartRootX, intstartRootY + PdfGenerator.LINE_HEIGHT
				/ 2, intEndRootX, intEndRootY + PdfGenerator.LINE_HEIGHT / 2);

		// ���[�g�m�[�h��"+"��"-"�A�C�R����`�悷��B
		drawPlusMinusIcon(parent, intstartRootX, intstartRootY);
		if (parent.getExpanded()) {
			// pdfOutTemp(parent, 1);
			ArrayList<String> array = new ArrayList<String>();
			array.add("0");
			outputPdfContents(parent, PdfGenerator.INDENX_SPACE,
					PdfGenerator.START_X, array);
		}
	}

	/**
	 * PDF�̍s�̃T�C�Y�̊i�[
	 *
	 * @param childTi
	 *            �m�[�h
	 * @param intstartX
	 *            ����X�����l
	 */
	private void saveLineSize(TreeItem childTi, int intstartX) {
		float leftMargin = document.leftMargin();
		float rightMargin = document.rightMargin();
		float spaceWidth = bf.getWidthPoint(" ", 12);
		float leftWidth = (float) (intstartX + PdfGenerator.LINE_LENGTH
				+ PdfGenerator.LINE_HEIGHT + spaceWidth);
		float rigthWidth = bf.getWidthPoint(childTi.getText(), 12);
		float maxLength = leftMargin + leftWidth + rigthWidth
				+ rightMargin;
		alLineSize.add(Float.toString(maxLength));
	}

	/**
	 * ���ɂ���āA����������h���t�H���g�ݒ�
	 *
	 * @param strRegu
	 *            �t�H���g
	 * @param strLanguType
	 *            �G���R�[�f�B���O��
	 * @param bol
	 *            �L��
	 * @return font �t�H���g
	 * @throws IOException
	 * @throws DocumentException
	 */
	private Font setFont(String strRegu, String strLanguType, boolean bol)
			throws DocumentException, IOException {
		bf = BaseFont.createFont(strRegu, strLanguType, bol);
		Font font = new Font(bf, 12, Font.NORMAL);

		return font;
	}

	/**
	 * ���̃X�^�C���̐ݒ�
	 *
	 * @param width
	 *            ���̕�
	 * @return bs ���̃X�^�C����BasicStroke�I�u�W�F�N�g
	 */
	private BasicStroke setLineStyle(float width) {
		float dash[] = { 1.0f };
		BasicStroke bs = new BasicStroke(width, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		return bs;
	}

	/**
	 * PDF�̉��y�[�W�̑���
	 *
	 * @param newPageFlag
	 *            ���y�[�W�t���O
	 * @return �Ȃ�
	 * @throws DocumentException
	 */
	private void SetNewPage(int newPageFlag) throws DocumentException {
		if (newPageFlag % intPageMaxLine == 0) {
			g2d.dispose();
			document.add(paragraph);
			document.newPage();
			PdfContentByte cb = writer.getDirectContent();
			g2d = cb.createGraphics(recSelected.getWidth(), recSelected
					.getHeight());
			g2d.setStroke(bs);
			paragraph = new Paragraph("");
			intstartY = PdfGenerator.START_Y - PdfGenerator.LINE_HEIGHT;
		}
	}

	/**
	 * �y�[�W�T�C�Y�̐ݒ�
	 *
	 * @param tiRoot
	 *            �m�[�h
	 * @return �Ȃ�
	 * @throws DocumentException
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	private void setPageSize(TreeItem tiRoot) throws MalformedURLException,
			IOException, DocumentException {
		getLineDate(tiRoot, PdfGenerator.INDENX_SPACE, PdfGenerator.START_X);
		float fMaxSize = getMaxLineSize();
		// �y�[�W�T�C�Y�̎��
		Rectangle[] rec = { PageSize.A4, PageSize.B4, PageSize.A3, PageSize.B3,
				PageSize.A2, PageSize.B2, PageSize.A1, PageSize.B1,
				PageSize.A0, PageSize.B0 };
		// �y�[�W�T�C�Y�̎�ނɂ���āA�s���̕\��
		int intPageMaxLineArr[] = { 41, 50, 61, 73, 88, 106, 127, 152, 182, 217 };

		// �c���[�̃m�[�h�̍ő�T�C�Y�ɂ���āA�y�[�W�T�C�Y��ݒ肷��B
		for (int i = 0; i < rec.length; i++) {
			// �m�[�h�̍ő�T�C�Y��PageSize.B0�𒴂��Ȃ������ꍇ�A
			// �y�[�W�̕W����ނ�ݒ肷��B
			if (fMaxSize < rec[i].getWidth()) {
				recSelected = rec[i];
				intPageMaxLine = intPageMaxLineArr[i];
				break;
			}
			// �m�[�h�̍ő�T�C�Y��PageSize.B0�𒴂����ꍇ�A
			// �ő�T�C�Y�ɂ���āA����`�Ńy�[�W�̃T�C�Y��ݒ肷��B
			if (i == rec.length - 1) {
				if (rec[i].getWidth() < fMaxSize) {
					float heigth = fMaxSize * rec[0].getHeight()
							/ rec[0].getWidth();
					recSelected = new Rectangle(fMaxSize, heigth);
					intPageMaxLine = (int) (Math.floor((heigth
							- document.topMargin() - document.bottomMargin())
							/ PdfGenerator.LINE_HEIGHT)) - 1;
				}
			}
		}
	}
}
