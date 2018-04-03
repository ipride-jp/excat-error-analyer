package jp.co.ipride.excat.analyzer.viewer.propertyviewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jp.co.ipride.excat.analyzer.common.DumpDocument;
import jp.co.ipride.excat.analyzer.common.DumpFileXmlConstant;
import jp.co.ipride.excat.common.utility.HelperFunc;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.dom4j.io.DOMReader;

/**
 * XML���e���\�z����w���p�[�N���X
 *
 * @author sai
 * @since 2009/10/15
 */
public class DomBuildHelper {
	// DOM�̃p�b�P�[�W��
	private static final String PACKAGE_PRE = "org.apache.xerces.dom";
	private static final String PACKAGE_PRE_SUN = "com.sun.org.apache.xerces.internal.dom";
	private static final String DIVISION = ".";

	// DOM�̊e�����N���X��
	public static final String NOTATION_NODE_TYPE = "DeferredNotationImpl";
	public static final String PROCESSING_INSTRUCTION_NODE_TYPE = "DeferredProcessingInstructionImpl";
	public static final String PROCESSING_INSTRUCTION_SUPER_NODE_TYPE = "ProcessingInstructionImpl";
	public static final String ENTITY_REFERENCE_NODE_TYPE = "DeferredEntityReferenceImpl";
	public static final String ENTITY_NODE_TYPE = "DeferredEntityImpl";
	public static final String DOCUMENT_NODE_TYPE = "DeferredDocumentImpl";
	public static final String DOCUMENTTYPE_NODE_TYPE = "DeferredDocumentTypeImpl";
	public static final String DOCUMENTTYPE_SUPER_NODE_TYPE = "DocumentTypeImpl";
	public static final String CORE_DOCUMENT_NODE_TYPE = "CoreDocumentImpl";
	public static final String COMMENT_NODE_TYPE = "DeferredCommentImpl";
	public static final String CDATA_NODE_TYPE = "DeferredCDATASectionImpl";
	public static final String TEXT_NODE_TYPE = "DeferredTextImpl";
	public static final String CHARACTER_DATA_NODE_TYPE = "CharacterDataImpl";
	public static final String ATTR_NODE_TYPE = "DeferredAttrImpl";
	public static final String ELEMENT_NODE_TYPE = "DeferredElementImpl";
	public static final String ELEMENT_SUPER_NODE_TYPE = "ElementImpl";

	// �e��񂪊܂܂�鑮����
	public static final String PROCESSING_INSTRUCTION_ATTR_TARGET = "target";
	public static final String DOCUMENTTYPE_ATTR_NAME = "name";
	public static final String DOCUMENTTYPE_ATTR_INTERNAL_SUBSET = "internalSubset";
	public static final String DOCUMENTTYPE_ATTR_SYSTEM_ID = "systemID";
	public static final String DOCUMENTTYPE_ATTR_PUBLIC_ID = "publicID";
	public static final String XML_ATTR_ENCODING = "encoding";
	public static final String ELEMENT_ATTR_VALUE = "value";
	public static final String PROCESSING_INSTRUCTION_ATTR_DATA = "data";

	// XML���z��̃t�B�[���h��
	public static final String FNODE_EXTRA = "fNodeExtra";
	public static final String FNODE_URI = "fNodeURI";
	public static final String FNODE_PARENT = "fNodeParent";
	public static final String FNODE_PREV_SIB = "fNodePrevSib";
	public static final String FNODE_LAST_CHILD = "fNodeLastChild";
	public static final String FNODE_VALUE = "fNodeValue";
	public static final String FNODE_NAME = "fNodeName";
	public static final String FNODE_TYPE = "fNodeType";
	public static final String FNODE_INDEX = "fNodeIndex";

	public static final int DEFAULT_VALUE = -1;
	public static final int INVALID_NODE_TYPE = -2;

	// �N���X�t�[�����R���N�V����
	public static Map<String, String> CLASS_NAMES = new HashMap<String, String>();
	public static Map<String, String> CLASS_NAMES_SUN = new HashMap<String, String>();

	private static final int FLG_INT = 0;
	private static final int FLG_OBJ = 1;
	private static Map<String, Node> objPoolById = new HashMap<String, Node>();
	private static Map<String, org.dom4j.Element> objPoolByNodeIndex = new HashMap<String, org.dom4j.Element>();

	static {
		// �N���X�t�[�����R���N�V�����̏�����
		CLASS_NAMES.put(DOCUMENT_NODE_TYPE, PACKAGE_PRE.concat(DIVISION).concat(DOCUMENT_NODE_TYPE));
		CLASS_NAMES.put(NOTATION_NODE_TYPE, PACKAGE_PRE.concat(DIVISION).concat(NOTATION_NODE_TYPE));
		CLASS_NAMES.put(PROCESSING_INSTRUCTION_NODE_TYPE, PACKAGE_PRE.concat(DIVISION).concat(PROCESSING_INSTRUCTION_NODE_TYPE));
		CLASS_NAMES.put(PROCESSING_INSTRUCTION_SUPER_NODE_TYPE, PACKAGE_PRE.concat(DIVISION).concat(PROCESSING_INSTRUCTION_SUPER_NODE_TYPE));
		CLASS_NAMES.put(ENTITY_REFERENCE_NODE_TYPE, PACKAGE_PRE.concat(DIVISION).concat(ENTITY_REFERENCE_NODE_TYPE));
		CLASS_NAMES.put(ENTITY_NODE_TYPE, PACKAGE_PRE.concat(DIVISION).concat(ENTITY_NODE_TYPE));
		CLASS_NAMES.put(DOCUMENTTYPE_NODE_TYPE, PACKAGE_PRE.concat(DIVISION).concat(DOCUMENTTYPE_NODE_TYPE));
		CLASS_NAMES.put(DOCUMENTTYPE_SUPER_NODE_TYPE, PACKAGE_PRE.concat(DIVISION).concat(DOCUMENTTYPE_SUPER_NODE_TYPE));
		CLASS_NAMES.put(CORE_DOCUMENT_NODE_TYPE, PACKAGE_PRE.concat(DIVISION).concat(CORE_DOCUMENT_NODE_TYPE));
		CLASS_NAMES.put(COMMENT_NODE_TYPE, PACKAGE_PRE.concat(DIVISION).concat(COMMENT_NODE_TYPE));
		CLASS_NAMES.put(CDATA_NODE_TYPE, PACKAGE_PRE.concat(DIVISION).concat(CDATA_NODE_TYPE));
		CLASS_NAMES.put(TEXT_NODE_TYPE, PACKAGE_PRE.concat(DIVISION).concat(TEXT_NODE_TYPE));
		CLASS_NAMES.put(CHARACTER_DATA_NODE_TYPE, PACKAGE_PRE.concat(DIVISION).concat(CHARACTER_DATA_NODE_TYPE));
		CLASS_NAMES.put(ATTR_NODE_TYPE, PACKAGE_PRE.concat(DIVISION).concat(ATTR_NODE_TYPE));
		CLASS_NAMES.put(ELEMENT_NODE_TYPE, PACKAGE_PRE.concat(DIVISION).concat(ELEMENT_NODE_TYPE));
		CLASS_NAMES.put(ELEMENT_SUPER_NODE_TYPE, PACKAGE_PRE.concat(DIVISION).concat(ELEMENT_SUPER_NODE_TYPE));
		CLASS_NAMES_SUN.put(DOCUMENT_NODE_TYPE, PACKAGE_PRE_SUN.concat(DIVISION).concat(DOCUMENT_NODE_TYPE));
		CLASS_NAMES_SUN.put(NOTATION_NODE_TYPE, PACKAGE_PRE_SUN.concat(DIVISION).concat(NOTATION_NODE_TYPE));
		CLASS_NAMES_SUN.put(PROCESSING_INSTRUCTION_NODE_TYPE, PACKAGE_PRE_SUN.concat(DIVISION).concat(PROCESSING_INSTRUCTION_NODE_TYPE));
		CLASS_NAMES_SUN.put(PROCESSING_INSTRUCTION_SUPER_NODE_TYPE, PACKAGE_PRE_SUN.concat(DIVISION).concat(PROCESSING_INSTRUCTION_SUPER_NODE_TYPE));
		CLASS_NAMES_SUN.put(ENTITY_REFERENCE_NODE_TYPE, PACKAGE_PRE_SUN.concat(DIVISION).concat(ENTITY_REFERENCE_NODE_TYPE));
		CLASS_NAMES_SUN.put(ENTITY_NODE_TYPE, PACKAGE_PRE_SUN.concat(DIVISION).concat(ENTITY_NODE_TYPE));
		CLASS_NAMES_SUN.put(DOCUMENTTYPE_NODE_TYPE, PACKAGE_PRE_SUN.concat(DIVISION).concat(DOCUMENTTYPE_NODE_TYPE));
		CLASS_NAMES_SUN.put(DOCUMENTTYPE_SUPER_NODE_TYPE, PACKAGE_PRE_SUN.concat(DIVISION).concat(DOCUMENTTYPE_SUPER_NODE_TYPE));
		CLASS_NAMES_SUN.put(CORE_DOCUMENT_NODE_TYPE, PACKAGE_PRE_SUN.concat(DIVISION).concat(CORE_DOCUMENT_NODE_TYPE));
		CLASS_NAMES_SUN.put(COMMENT_NODE_TYPE, PACKAGE_PRE_SUN.concat(DIVISION).concat(COMMENT_NODE_TYPE));
		CLASS_NAMES_SUN.put(CDATA_NODE_TYPE, PACKAGE_PRE_SUN.concat(DIVISION).concat(CDATA_NODE_TYPE));
		CLASS_NAMES_SUN.put(TEXT_NODE_TYPE, PACKAGE_PRE_SUN.concat(DIVISION).concat(TEXT_NODE_TYPE));
		CLASS_NAMES_SUN.put(CHARACTER_DATA_NODE_TYPE, PACKAGE_PRE_SUN.concat(DIVISION).concat(CHARACTER_DATA_NODE_TYPE));
		CLASS_NAMES_SUN.put(ATTR_NODE_TYPE, PACKAGE_PRE_SUN.concat(DIVISION).concat(ATTR_NODE_TYPE));
		CLASS_NAMES_SUN.put(ELEMENT_NODE_TYPE, PACKAGE_PRE_SUN.concat(DIVISION).concat(ELEMENT_NODE_TYPE));
		CLASS_NAMES_SUN.put(ELEMENT_SUPER_NODE_TYPE, PACKAGE_PRE_SUN.concat(DIVISION).concat(ELEMENT_SUPER_NODE_TYPE));
	}

	/**
	 * �I�u�W�F�N�g�v�[���𐮗����郁�\�b�h
	 *
	 * @param rootElement Document�I�u�W�F�N�g
	 * @throws ParserConfigurationException
	 */
	public static void packObjectPool(org.dom4j.Element rootElement)
		throws ParserConfigurationException {

		// �q�m�[�h�̃��X�g���擾����B
		NodeList list = DumpDocument.getPoolNode().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			NamedNodeMap attrMap = node.getAttributes();
			if (attrMap != null) {
				Node item = attrMap
						.getNamedItem(DumpFileXmlConstant.ATTR_OBJECT_ID);
				if (item != null && item.getNodeValue().length() != 0) {
					// ID�����̒l���L�[�Ƃ��ăm�[�h���}�b�v�ɕۑ�����B
					objPoolById.put(item.getNodeValue(), node);
				}
				item = attrMap.getNamedItem(DumpFileXmlConstant.ATTR_REAL_TYPE);
				if (item != null && item.getNodeValue() != null
						&& (isDomClass(item.getNodeValue()))) {
					org.dom4j.Element element = convertDomObject(node);
					List<org.dom4j.Element> listAttr = element
							.elements(DumpFileXmlConstant.NODE_ATTRIBUTE);
					for (int j = 0; j < listAttr.size(); j++) {
						if (FNODE_INDEX.equals(listAttr.get(j).attributeValue(
								DumpFileXmlConstant.ATTR_NAME))) {
							String index = listAttr.get(j).attributeValue(
									DumpFileXmlConstant.ATTR_VALUE);
							if (index != null) {
								// fNodeIndex�����̒l���L�[�Ƃ��ăm�[�h���}�b�v�ɕۑ�����B
								objPoolByNodeIndex.put(index, element);
							}
						}
					}
				}
			}
		}
		if (rootElement != null) {
			// Document�m�[�h��ۑ�����B
			objPoolByNodeIndex.put(String.valueOf(0), rootElement);
		}
	}

	/**
	 * �N���X����DOM�̎����N���X�ł��邩�𔻒f���郁�\�b�h
	 *
	 * @param className �N���X��
	 * @return DOM�̎����N���X�̏ꍇ�FTrue�A�ȊO�̏ꍇ�FFlase
	 */
	private static boolean isDomClass(String className) {
		return CLASS_NAMES.values().contains(className) || CLASS_NAMES_SUN.values().contains(className);
	}

	/**
	 * org.w3c.dom.Element����org.dom4j.Element�֕ϊ����郁�\�b�h
	 *
	 * @param node org.w3c.dom.Element�I�u�W�F�N�g
	 * @return org.dom4j.Element�I�u�W�F�N�g
	 * @throws ParserConfigurationException
	 */
	public static org.dom4j.Element convertDomObject(Node node) throws ParserConfigurationException {
		// org.w3c.dom.Element����org.dom4j.Element�֕ϊ�����B
		if (node instanceof Element) {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			org.w3c.dom.Document doc = builder.newDocument();
			Node newNode = doc.importNode(node, true);
			doc.appendChild(newNode);
			DOMReader reader = new DOMReader();
			org.dom4j.Document doc2 = reader.read(doc);

			return doc2.getRootElement();
		}
		return null;
	}

	/**
	 * XML��񂪊i�[����Ă���z��̓��e���擾���郁�\�b�h
	 *
	 * @param node Document�I�u�W�F�N�g
	 * @return �z����e��ێ�����Bean�N���X
	 * @throws ParserConfigurationException
	 */
	public static DomArrayBean getDomArray(org.dom4j.Element node) throws ParserConfigurationException {
		List<org.dom4j.Element> attrNodeList = getChildren(node,
				DumpFileXmlConstant.NODE_ATTRIBUTE);
		// XML���z��̓��e���擾���āABean�N���X�ɕۑ�����B
		DomArrayBean bean = new DomArrayBean();
		for (int i = 0; i < attrNodeList.size(); i++) {
			org.dom4j.Element attrNode = attrNodeList.get(i);
			String attrValue = getAttributeValue(attrNode,
					DumpFileXmlConstant.ATTR_NAME);
			if (attrValue == null)
				continue;

			if (attrValue.equals(FNODE_TYPE)) {
				bean.setFNodeType((int[][]) buildArray(attrNode,
						DomBuildHelper.FLG_INT));
			} else if (attrValue.equals(FNODE_NAME)) {
				bean.setFNodeName((Object[][]) buildArray(attrNode,
						DomBuildHelper.FLG_OBJ));
			} else if (attrValue.equals(FNODE_VALUE)) {
				bean.setFNodeValue((Object[][]) buildArray(attrNode,
						DomBuildHelper.FLG_OBJ));
			} else if (attrValue.equals(FNODE_LAST_CHILD)) {
				bean.setFNodeLastChild((int[][]) buildArray(attrNode,
						DomBuildHelper.FLG_INT));
			} else if (attrValue.equals(FNODE_PREV_SIB)) {
				bean.setFNodePrevSib((int[][]) buildArray(attrNode,
						DomBuildHelper.FLG_INT));
			} else if (attrValue.equals(FNODE_PARENT)) {
				bean.setFNodeParent((int[][]) buildArray(attrNode,
						DomBuildHelper.FLG_INT));
			} else if (attrValue.equals(FNODE_URI)) {
				bean.setFNodeURI((Object[][]) buildArray(attrNode,
						DomBuildHelper.FLG_OBJ));
			} else if (attrValue.equals(FNODE_EXTRA)) {
				bean.setFNodeExtra((int[][]) buildArray(attrNode,
						DomBuildHelper.FLG_INT));
			}
		}
		return bean;
	}

	/**
	 * Dom�I�u�W�F�N�g����z��̓��e�𕜌����郁�\�b�h
	 *
	 * @param attrNode Dom�I�u�W�F�N�g
	 * @param flag �z����e�̃^�C�v
	 * @return ���������z��
	 * @throws ParserConfigurationException
	 */
	private static Object[] buildArray(org.dom4j.Element attrNode, int flag)
		throws ParserConfigurationException {

		// Dump�t�@�C������eXML���z��̃t�B�[���h���ɂ���āA���e�𒊏o���āA�z��𕜌�����B
		Object[] typeArray = null;
		if (getAttributeValue(attrNode, DumpFileXmlConstant.ATTR_OBJECT_REF) != null) {
			// �Q�Ɛ�Element���擾����B
			org.dom4j.Element refNode = getRefObjectNode(attrNode);
			if (refNode == null)
				return null;

			// �T�C�Y�����擾���āA�z����쐬����B
			int size = Integer.parseInt(getAttributeValue(refNode,
					DumpFileXmlConstant.ATTR_ITEM_NUMBER));
			if (flag == DomBuildHelper.FLG_INT) {
				typeArray = (Object[]) new int[size][];
			} else {
				typeArray = new Object[size][];
			}

			// �쐬�����z��ɏ��𖄂ߍ��ށB
			List<org.dom4j.Element> itemList = getChildren(refNode,
					DumpFileXmlConstant.NODE_ITEM);
			for (int j = 0; j < itemList.size(); j++) {
				org.dom4j.Element itemNode = itemList.get(j);
				if (getAttributeValue(itemNode,
						DumpFileXmlConstant.ATTR_OBJECT_REF) != null) {
					// �Q�Ɛ�Element������e���擾����B
					org.dom4j.Element refNodeArray = getRefObjectNode(itemNode);
					if (refNodeArray != null) {
						int sizeArray = Integer.parseInt(getAttributeValue(
								refNodeArray,
								DumpFileXmlConstant.ATTR_ITEM_NUMBER));
						Object typeArrayItem = null;
						if (flag == DomBuildHelper.FLG_INT) {
							typeArrayItem = (Object) new int[sizeArray];
						} else {
							typeArrayItem = new Object[sizeArray];
						}

						List<org.dom4j.Element> itemList2 = getChildren(
								refNodeArray, DumpFileXmlConstant.NODE_ITEM);
						int nullIndex = 0;
						for (int k = 0; k < itemList2.size(); k++) {
							org.dom4j.Element itemNode2 = itemList2.get(k);
							if (flag == DomBuildHelper.FLG_INT) {
								int itemValue = Integer
										.parseInt(getAttributeValue(itemNode2,
												DumpFileXmlConstant.ATTR_VALUE));
								((int[]) typeArrayItem)[k] = itemValue;
							} else {
								String itemValue = null;
								if (getAttributeValue(itemNode2,
										DumpFileXmlConstant.ATTR_OBJECT_REF) != null) {
									// �Q�Ɛ�Element������e���擾����B
									itemValue = getValueFromRefObj(itemNode2);
								}
								((Object[]) typeArrayItem)[k] = itemValue;
							}
							nullIndex = k;
						}

						// ��̗v�f�ɑ΂��āA�f�t�H���g�l��ݒ肷��B
						for (int k = nullIndex; k < sizeArray; k++) {
							if (flag == DomBuildHelper.FLG_INT) {
								((int[]) typeArrayItem)[k] = DEFAULT_VALUE;
							} else {
								((Object[]) typeArrayItem)[k] = null;
							}
						}

						typeArray[j] = typeArrayItem;
					}
				}
			}

			return typeArray;
		}

		return null;
	}

	/**
	 * �w�肳���Element�̐e�N���X�̑������擾���郁�\�b�h
	 *
	 * @param targetElement �w�肳���Element
	 * @param superClassName �w�肳���e�N���X��
	 * @param attrName �擾���鑮����
	 * @return �����l
	 * @throws ParserConfigurationException
	 */
	public static String getSuperAttributeFromDomObj(
			org.dom4j.Element targetElement, String superClassName,
			String attrName) throws ParserConfigurationException {
		// �e�N���X�̑�����\���m�[�h���擾����B
		org.dom4j.Element elementAttr =
			getRefObjectNodeByAttribute(targetElement, superClassName, attrName);
		if (elementAttr != null) {
			// �����l���擾����B
			return getValueFromRefObj(elementAttr);
		}
		return null;
	}

	/**
	 * �w�肳���Element�̐e�N���X�̑����ɎQ�Ƃ����Element���擾���郁�\�b�h
	 *
	 * @param targetElement �w�肳���Element
	 * @param superClassName �w�肳���e�N���X��
	 * @param attrName �w�肳��鑮����
	 * @return �Q�Ƃ����Element
	 */
	public static org.dom4j.Element getRefObjectNodeByAttribute(
			org.dom4j.Element targetElement, String superClassName,
			String attrName) {
		// �qElement���擾����B
		List<org.dom4j.Element> list = getChildren(targetElement,
				DumpFileXmlConstant.NODE_SUPERCLASS);
		for (int i = 0; i < list.size(); i++) {
			org.dom4j.Element array_element = list.get(i);
			String sig = getAttributeValue(array_element,
					DumpFileXmlConstant.ATTR_SIG);
			if (sig != null && checkEquals(sig, superClassName)) {
				// �e�N���X�̑�����\��Element�̃��X�g���擾����B
				List<org.dom4j.Element> listAttr = getChildren(array_element,
						DumpFileXmlConstant.NODE_ATTRIBUTE);
				for (int j = 0; j < listAttr.size(); j++) {
					org.dom4j.Element elementAttr = listAttr.get(j);
					String name = getAttributeValue(elementAttr,
							DumpFileXmlConstant.ATTR_NAME);
					if (name != null && name.equals(attrName)) {
						// ���������w�肳�ꂽ�������ƈ�v����ꍇ�A���YElement��ԋp����B
						return elementAttr;
					}
				}
			} else {
				// �e�N���X�̐e�N���X���ċA�I�ɒT���B
				return getRefObjectNodeByAttribute(array_element, superClassName, attrName);
			}
		}
		return null;
	}

	/**
	 * �N���X���̈�v����p���\�b�h
	 *
	 * @param className ���ۂ̃N���X��
	 * @param classNameTag �w�肵���N���X��
	 * @return ��v�̏ꍇ�FTrue�A�s��v�̏ꍇ�AFalse
	 */
	public static boolean checkEquals(String className, String classNameTag) {
		return className.equals(DomBuildHelper.CLASS_NAMES.get(classNameTag))
				|| className.equals(DomBuildHelper.CLASS_NAMES_SUN.get(classNameTag));
	}

	/**
	 * �w�肳���m�[�h���Ԃ���Element���擾���郁�\�b�h
	 *
	 * @param currentIndex �m�[�h����
	 * @return Element�I�u�W�F�N�g
	 */
	public static org.dom4j.Element getDomObject(int currentIndex) {
		return objPoolByNodeIndex.get(String.valueOf(currentIndex));
	}


	/**
	 * Element�Ώۂ̃e�L�X�g�idata�����j���擾���郁�\�b�h
	 *
	 * @param element �w�肳���Element�I�u�W�F�N�g
	 * @return �����udata�v�̒l
	 * @throws ParserConfigurationException
	 */
	public static String getElementText(org.dom4j.Element element) throws ParserConfigurationException {
		List<org.dom4j.Element> list = getElementAttrList(element);
		for (int i = 0; i < list.size(); i++) {
			org.dom4j.Element attrElement = list.get(i);
			String name = getAttributeValue(attrElement,
					DumpFileXmlConstant.ATTR_NAME);
			if (name != null && name.equals(PROCESSING_INSTRUCTION_ATTR_DATA)) {
				return getValueFromRefObj(attrElement);
			}
		}
		return null;
	}


	/**
	 * �w�肳���Element�̎Q�Ɛ�Element���瑮���uValue�v�̒l���擾���郁�\�b�h
	 *
	 * @param attrElement �w�肳���Element
	 * @return �����uValue�v�̒l
	 * @throws ParserConfigurationException
	 */
	private static String getValueFromRefObj(org.dom4j.Element attrElement) throws ParserConfigurationException {
		org.dom4j.Element elementAttrRef = getRefObjectNode(attrElement);
		if (elementAttrRef != null) {
			String itemValue = getAttributeValue(elementAttrRef,
					DumpFileXmlConstant.ATTR_VALUE);
			if (itemValue != null) {
				return HelperFunc.getStringFromNumber(itemValue);
			}
		}
		return null;
	}


	/**
	 * Element�I�u�W�F�N�g�̑����m�[�h���擾���郁�\�b�h
	 *
	 * @param element Element�I�u�W�F�N�g
	 * @return �����m�[�h�̃��X�g
	 */
	private static List<org.dom4j.Element> getElementAttrList(
			org.dom4j.Element element) {
		ArrayList<org.dom4j.Element> list = new ArrayList<org.dom4j.Element>();
		list.addAll(getChildren(element, DumpFileXmlConstant.NODE_ATTRIBUTE));
		List<org.dom4j.Element> elementSupers = getChildren(element,
				DumpFileXmlConstant.NODE_SUPERCLASS);
		for (int i = 0; i < elementSupers.size(); i++) {
			org.dom4j.Element elementSuper = elementSupers.get(i);
			list.addAll(getElementAttrList(elementSuper));
		}

		return list;
	}

	/**
	 * ���������擾���郁�\�b�h
	 *
	 * @param element ������񂪊i�[����Ă���Element�I�u�W�F�N�g
	 * @return �������e�̔z��@[0]:�������A[1]:�����l
	 * @throws ParserConfigurationException
	 */
	public static String[] getElementAttibute(org.dom4j.Element element) throws ParserConfigurationException {
		String attrValue = null;
		String attrName = null;
		List<org.dom4j.Element> list = getElementAttrList(element);
		for (int i = 0; i < list.size(); i++) {
			org.dom4j.Element attrElement = list.get(i);
			String name = getAttributeValue(attrElement,
					DumpFileXmlConstant.ATTR_NAME);
			if (name != null && name.equals(DOCUMENTTYPE_ATTR_NAME)) {
				attrName = getValueFromRefObj(attrElement);
			}
			name = getAttributeValue(attrElement, DumpFileXmlConstant.ATTR_NAME);
			if (name != null && name.equals(ELEMENT_ATTR_VALUE)) {
				attrValue = getValueFromRefObj(attrElement);
			}
		}

		if (attrName != null && attrValue != null) {
			return new String[] { attrName, attrValue };
		}
		return null;
	}

	/**
	 * �Q�Ɛ�Element�I�u�W�F�N�g���擾���郁�\�b�h
	 *
	 * @param referenceNode �Q�ƌ�Element�I�u�W�F�N�g
	 * @return �Q�Ɛ�Element�I�u�W�F�N�g
	 * @throws ParserConfigurationException
	 */
	public static org.dom4j.Element getRefObjectNode(
			org.dom4j.Element referenceNode) throws ParserConfigurationException {
		String objectId = getAttributeValue(referenceNode,
				DumpFileXmlConstant.ATTR_OBJECT_REF);

		return convertDomObject(objPoolById.get(objectId));
	}

	/**
	 * �w�薼�O�ŎqElement�I�u�W�F�N�g���擾���郁�\�b�h
	 *
	 * @param node �eElement�I�u�W�F�N�g
	 * @param name �qElement�̖��O
	 * @return �qElement�I�u�W�F�N�g�̃��X�g
	 */
	private static List<org.dom4j.Element> getChildren(org.dom4j.Element node,
			String name) {
		List<org.dom4j.Element> attrNodeList = node.elements(name);
		return attrNodeList;
	}

	/**
	 * ���ׂĂ̎qElement�I�u�W�F�N�g���擾���郁�\�b�h
	 *
	 * @param node �eElement�I�u�W�F�N�g
	 * @return �qElement�I�u�W�F�N�g�̃��X�g
	 */
	private static List<org.dom4j.Element> getChildren(org.dom4j.Element node) {
		List<org.dom4j.Element> list = node.elements();
		return list;
	}

	/**
	 * �����l���擾���郁�\�b�h
	 *
	 * @param objNode Element�I�u�W�F�N�g
	 * @param name ������
	 * @return �����l
	 */
	public static String getAttributeValue(org.dom4j.Element objNode,
			String name) {
		String value = objNode.attributeValue(name);
		return value;
	}

	/**
	 * ObjectId���擾���郁�\�b�h
	 *
	 * @param node Object�m�[�h
	 * @return ObjectId�̒l
	 */
	public static String getObjectId(Node node) {
		if (node == null) {
			return null;
		}
		NamedNodeMap attrMap = node.getAttributes();
		if (attrMap != null) {
			Node item = attrMap.getNamedItem(DumpFileXmlConstant.ATTR_OBJECT_ID);
			if (item != null && item.getNodeValue().length() != 0) {
				return item.getNodeValue();
			}
		}
		return null;
	}
}
