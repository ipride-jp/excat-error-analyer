package jp.co.ipride.excat.analyzer.viewer.propertyviewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import jp.co.ipride.excat.MainViewer;
import jp.co.ipride.excat.analyzer.common.DumpFileXmlConstant;

import org.dom4j.Element;
import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultCDATA;
import org.dom4j.tree.DefaultComment;
import org.dom4j.tree.DefaultDocumentType;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.DefaultProcessingInstruction;
import org.dom4j.tree.DefaultText;
import org.w3c.dom.Node;

/**
 * XML���e���\�z����r���_�[�N���X
 *
 * @author sai
 * @since 2009/10/15
 */
public class DomBuilder {
	private boolean isCompletely = true;
	private DomArrayBean domArray = null;
	private Node currentNode = null;
	private Map<String, org.dom4j.Document> documentMap = new HashMap<String, org.dom4j.Document>();
	private Map<String, Map<String, org.dom4j.Element>> elementMap = new HashMap<String, Map<String, org.dom4j.Element>>();

	/**
	 * Document�Ώۂ�XML�������擾���郁�\�b�h
	 *
	 * @param node Document��񂪊i�[����Ă���m�[�h
	 * @return Document�I�u�W�F�N�g���畜������XML����
	 */
	public String getDocumentXml(Node node) {
		this.currentNode = node;
		try {
			if (getDocument().getRootElement() == null) {
				// �L���b�V�����Ȃ��ꍇ�A�z���I�u�W�F�N�g�Ɋi�[���ꂽ��񂩂�XML�����𕜌�����B
				// �I�������m�[�h��DOM4J��Element�I�u�W�F�N�g�ɕϊ�����B
				org.dom4j.Element jNode = DomBuildHelper.convertDomObject(node);

				// ObjectPool�𐮗�����B
				DomBuildHelper.packObjectPool(jNode);

				// Document�I�u�W�F�N�g���\�z����B
				buildDocumentObject(jNode);
			}

			// �\�z����Document�I�u�W�F�N�g��XML������ԋp����B
			this.isCompletely = true;
			return getDocument().asXML();

		} catch (Exception e) {
			// ��O�����������ꍇ�A���S���t���O��False�ɐݒ肷��B
			this.isCompletely = false;
			MainViewer.win.logException(e);
		}

		return null;
	}

	/**
	 * Document�I�u�W�F�N�g���\�z���郁�\�b�h
	 *
	 * @param rootNode �g�b�v���x���̃h�L�������g�I�u�W�F�N�g
	 * @throws ParserConfigurationException
	 */
	private void buildDocumentObject(org.dom4j.Element rootNode)
			throws ParserConfigurationException {
		// XML���i�[�p�̔z��̓��e���擾����B
		domArray = DomBuildHelper.getDomArray(rootNode);

		// XML�������\�z����B
		buildContent(getDocument(), null, domArray, 0, true);
	}

	/**
	 * Element�Ώۂ�XML�������擾���郁�\�b�h
	 *
	 * @param node Element��񂪊i�[����Ă���m�[�h
	 * @return Element�I�u�W�F�N�g���畜������XML����
	 */
	public String getElementXml(Node node) {
		org.dom4j.Element elementNode = null;

		try {
			if (getDocument().getRootElement() == null) {
				// �L���b�V�����Ȃ��ꍇ�A�z���I�u�W�F�N�g�Ɋi�[���ꂽ��񂩂�XML�����𕜌�����B
				// �I�������m�[�h��DOM4J��Element�I�u�W�F�N�g�ɕϊ�����B
				org.dom4j.Element jNode = DomBuildHelper.convertDomObject(node);

				// ObjectPool�𐮗�����B
				DomBuildHelper.packObjectPool(null);

				// Element���܂܂��g�b�v���x���̃h�L�������g�I�u�W�F�N�g���擾����B
				org.dom4j.Element ownerDoc = null;
				org.dom4j.Element ownerDocAttribute = DomBuildHelper.getRefObjectNodeByAttribute(
						jNode, "ParentNode", "ownerDocument");
				if (ownerDocAttribute != null) {
					ownerDoc = DomBuildHelper.getRefObjectNode(ownerDocAttribute);
				}
				if (ownerDoc != null) {
					// Document�I�u�W�F�N�g���\�z����B
					buildDocumentObject(ownerDoc);
				}
			}

			// �h�L�������g�I�u�W�F�N�g���瓖�YElement�v�f���擾����B
			org.dom4j.Element element = DomBuildHelper.convertDomObject(node);
			List<org.dom4j.Element> listAttr = element
					.elements(DumpFileXmlConstant.NODE_ATTRIBUTE);
			for (int j = 0; j < listAttr.size(); j++) {
				if (DomBuildHelper.FNODE_INDEX.equals(listAttr.get(j).attributeValue(
						DumpFileXmlConstant.ATTR_NAME))) {
					String index = listAttr.get(j).attributeValue(
							DumpFileXmlConstant.ATTR_VALUE);
					if (index != null) {
						elementNode = getElementByNodeIndex(index);
					}
				}
			}

			// �擾����Element��XML������ԋp����B
			if (elementNode != null) {
				this.isCompletely = true;
				return elementNode.asXML();
			}
		} catch (Exception e) {
			// ��O�����������ꍇ�A���S���t���O��False�ɐݒ肷��B
			this.isCompletely = false;
			MainViewer.win.logException(e);
		}
		return null;
	}

	/**
	 * Document�Ώۂ��擾���郁�\�b�h
	 *
	 * @return Document�Ώ�
	 */
	private org.dom4j.Document getDocument(){
		// �I������Ă���m�[�h��ObjectId�ɂ����Document�Ώۂ��擾����B
		org.dom4j.Document document = documentMap.get(DomBuildHelper.getObjectId(currentNode));
		if (document == null) {
			// �擾�ł��Ȃ��ꍇ�A�V�K�쐬����B
			document = new org.dom4j.tree.DefaultDocument();
			String objectId = DomBuildHelper.getObjectId(currentNode);
			if (objectId != null) {
				documentMap.put(objectId, document);
			}
		}
		return document;
	}

	/**
	 * Element�Ώۂ��擾���郁�\�b�h
	 *
	 * @param index �m�[�h�̃C���f�b�N�X
	 * @return Element�Ώ�
	 */
	private Element getElementByNodeIndex(String index) {
		return elementMap.get(DomBuildHelper.getObjectId(currentNode)).get(index);
	}

	/**
	 * Element�Ώۂ�ۑ����郁�\�b�h
	 *
	 * @param index �m�[�h�̃C���f�b�N�X
	 * @param element �ۑ�����Element�Ώ�
	 */
	private void putElementByNodeIndex(int index,
			org.dom4j.Element element) {
		Map<String, org.dom4j.Element> elements = elementMap.get(DomBuildHelper.getObjectId(currentNode));
		if (elements == null) {
			elements = new HashMap<String, org.dom4j.Element>();
			elementMap.put(DomBuildHelper.getObjectId(currentNode), elements);
		}
		elements.put(String.valueOf(index), element);
	}

	/**
	 * ���S���t���O���擾���郁�\�b�h
	 *
	 * @return ���S���t���O
	 */
	public boolean isCompletely() {
		return isCompletely;
	}

	/**
	 * ���S���t���O��ݒ肷�郁�\�b�h
	 *
	 * @param isCompletely ���S���t���O
	 */
	public void setCompletely(boolean isCompletely) {
		this.isCompletely = isCompletely;
	}

	/**
	 * XML�������\�z���郁�\�b�h
	 *
	 * @param doc Document�I�u�W�F�N�g
	 * @param rootNode ���[�g�m�[�h
	 * @param domArray XML��񂪊i�[����Ă���z��Bean�N���X
	 * @param indexOfLastChild �Ō�̎q�v�f�̃C���f�b�N�X
	 * @param needBuildSib �Z��v�f�̍\�z�v�ۃt���O
	 */
	private void buildContent(org.dom4j.Document doc,
			org.dom4j.Element rootNode, DomArrayBean domArray,
			int indexOfLastChild, boolean needBuildSib) {
		try {
			DomArrayItemBean item = domArray.getItem(indexOfLastChild);
			boolean needBuildText = true;

			// �m�[�h�^�C�v�ɂ����Document�I�u�W�F�N�g���\�z����B
			switch (item.getFNodeType()) {
			case Node.TEXT_NODE:
				// Text�̏ꍇ
				String value = (String) item.getFNodeValue();
				if (value != null) {
					if (rootNode == null) {
						// Document���x���̃R�����g�̏ꍇ
						doc.content().add(0, new DefaultText(value));
					} else {
						// Element���x���̃R�����g�̏ꍇ
						rootNode.content().add(0, new DefaultText(value));
					}
				} else {
					// XML��񂪔z�񂩂�擾�ł��Ȃ��ꍇ�A�I�u�W�F�N�g����擾����B
					recoverArray(domArray, indexOfLastChild);
					buildContent(doc, rootNode, domArray, indexOfLastChild, false);
					needBuildText = false;
				}
				break;
			case Node.CDATA_SECTION_NODE:
				// CDATA�̏ꍇ
				String cdataValue = (String) item.getFNodeValue();
				if (cdataValue != null) {
					if (rootNode == null) {
						// Document���x����CDATA�̏ꍇ
						doc.content().add(0, new DefaultCDATA(cdataValue));
					} else {
						// Element���x����CDATA�̏ꍇ
						rootNode.content().add(0, new DefaultCDATA(cdataValue));
					}
				} else {
					// XML��񂪔z�񂩂�擾�ł��Ȃ��ꍇ�A�I�u�W�F�N�g����擾����B
					recoverArray(domArray, indexOfLastChild);
					buildContent(doc, rootNode, domArray, indexOfLastChild, false);
				}
				break;
			case Node.COMMENT_NODE:
				// Comment�̏ꍇ
				String commentValue = (String) item.getFNodeValue();
				if (commentValue != null) {
					if (rootNode == null) {
						doc.content().add(0, new DefaultComment(commentValue));
					} else {
						rootNode.content().add(0, new DefaultComment(commentValue));
					}
				} else {
					recoverArray(domArray, indexOfLastChild);
					buildContent(doc, rootNode, domArray, indexOfLastChild, false);
				}
				break;
			case Node.ATTRIBUTE_NODE:
				// Attribute�̏ꍇ
				rootNode.attributes().add(0, new DefaultAttribute((String) item.getFNodeName(), (String) item
						.getFNodeValue()));
				break;
			case Node.PROCESSING_INSTRUCTION_NODE:
				// ProcessingInstruction�̏ꍇ
				if (rootNode == null) {
					doc.content().add(0, new DefaultProcessingInstruction((String) item.getFNodeName(),
							(String) item.getFNodeValue()));
				} else {
					rootNode.content().add(0, new DefaultProcessingInstruction((String) item.getFNodeName(),
							(String) item.getFNodeValue()));
				}
				break;
			case Node.ELEMENT_NODE:
				// Element�̏ꍇ
				org.dom4j.Element element = new DefaultElement((String) item
						.getFNodeName());

				// �qElement���\�z����B
				int index = item.getFNodeLastChild();
				if (index > DomBuildHelper.DEFAULT_VALUE) {
					buildContent(doc, element, domArray, index, true);
				}

				// Attribute���\�z����B
				int extraIndexOfAttr = item.getFNodeExtra();
				if (extraIndexOfAttr > DomBuildHelper.DEFAULT_VALUE) {
					DomArrayItemBean extraBean = domArray.getItem(extraIndexOfAttr);
					if (extraBean != null) {
						do
			            {
							extraBean = domArray.getItem(extraIndexOfAttr);
							String extraName = (String)extraBean.getFNodeName();
							String extraValue = (String)extraBean.getFNodeValue();
							element.attributes().add(0, new DefaultAttribute(extraName, extraValue));
							extraIndexOfAttr = domArray.getItem(extraIndexOfAttr).getFNodePrevSib();
			            } while(extraIndexOfAttr != DomBuildHelper.DEFAULT_VALUE);
					}
				}

				if (rootNode != null) {
					// �qElement�̏ꍇ�A�eElement�ɒǉ�����B
					rootNode.content().add(0, element);
				} else {
					// �g�b�vElement�̏ꍇ�ADocument�ɒǉ�����B
					doc.setRootElement(element);
				}

				// �\�z����Element���L���b�V������B
				putElementByNodeIndex(indexOfLastChild, element);
				break;
			case Node.DOCUMENT_TYPE_NODE:
				// DocumentType�̏ꍇ
				DefaultDocumentType docType = new DefaultDocumentType((String) item
						.getFNodeName(), (String) item.getFNodeValue(),
						(String) item.getFNodeURI());
				List<String> list = new ArrayList<String>();
				if (item.getInternalSubset().length() != 0) {
					list.add(item.getInternalSubset());
					docType.setInternalDeclarations(list);
				} else {
					int extraIndex = item.getFNodeExtra();
					if (extraIndex > DomBuildHelper.DEFAULT_VALUE) {
						DomArrayItemBean extraBean = domArray.getItem(extraIndex);
						if (extraBean != null) {
							String extra = (String)extraBean.getFNodeValue();
							list.add(extra);
							docType.setInternalDeclarations(list);
						}
					}
				}
				doc.setDocType(docType);
				break;
			case Node.DOCUMENT_NODE:
				// Document�̏ꍇ
				int index1 = item.getFNodeLastChild();
				if (index1 > DomBuildHelper.DEFAULT_VALUE) {
					// �qElement���\�z����B
					buildContent(doc, rootNode, domArray, index1, true);
				}

				// Encoding�����\�z����B
				org.dom4j.Element targetElement = DomBuildHelper.getDomObject(indexOfLastChild);
				String encoding = DomBuildHelper.getSuperAttributeFromDomObj(
						targetElement, DomBuildHelper.CORE_DOCUMENT_NODE_TYPE, DomBuildHelper.XML_ATTR_ENCODING);
				if (encoding != null) {
					doc.setXMLEncoding(encoding);
				}
				break;
			case DomBuildHelper.DEFAULT_VALUE:
				// �m�[�h�^�C�v�����f�ł��Ȃ��ꍇ�A
				// �I�u�W�F�N�g��������擾���āAXML���z��ɕ⑫����B
				if (indexOfLastChild == 0 || !item.isEmpty()) {
					recoverArray(domArray, indexOfLastChild);
					// item = domArray.getItem(indexOfLastChild);
					buildContent(doc, rootNode, domArray, indexOfLastChild, false);
				}
				break;
			default:
				break;
			}

			// ���O�̌Z��m�[�h���\�z����B
			if (needBuildSib) {
				int indexOfPrevSib = item.getFNodePrevSib();
				if (indexOfPrevSib > DomBuildHelper.DEFAULT_VALUE && indexOfPrevSib != indexOfLastChild) {
					if (!needBuildText) {
						int i = indexOfLastChild;
						// �m�[�h�^�C�v��Text��EntityReference�̏ꍇ�A�Z��m�[�h�̍\�z����K�v���Ȃ��B
						while (indexOfPrevSib > DomBuildHelper.DEFAULT_VALUE
								&& (domArray.getItem(i).getFNodeType() == Node.TEXT_NODE || domArray.getItem(i).getFNodeType() == Node.ENTITY_REFERENCE_NODE)
								&& (domArray.getItem(indexOfPrevSib).getFNodeType() == Node.TEXT_NODE || domArray.getItem(indexOfPrevSib).getFNodeType() == Node.ENTITY_REFERENCE_NODE)) {
							int j = indexOfPrevSib;
							indexOfPrevSib = domArray.getItem(i).getFNodePrevSib();
							i = j;
						}
						needBuildText = true;
					}
					if (indexOfPrevSib > DomBuildHelper.DEFAULT_VALUE) {
						buildContent(doc, rootNode, domArray, indexOfPrevSib, true);
					}
				}
			}
		} catch (Exception e) {
			// �\�z���s�̏ꍇ�A���S���t���O��False�ɐݒ肵�āA���̃m�[�h���\�z����B
			this.isCompletely = false;
			MainViewer.win.logException(e);
		}
	}

	/**
	 * XML����z��ɕ⑫���郁�\�b�h
	 *
	 * @param domArray XML��񂪊i�[����Ă���z��Bean�N���X
	 * @param index �\�z���Ă���v�f�̃C���f�b�N�X
	 * @throws ParserConfigurationException
	 */
	private static void recoverArray(DomArrayBean domArray, int index) throws ParserConfigurationException {
		int currentIndex = index;
		DomArrayItemBean itemBeanCurrent = domArray.getItem(currentIndex);
		if (itemBeanCurrent.getFNodeLastChild() == DomBuildHelper.DEFAULT_VALUE) {
			int childIndex = currentIndex + 1;
			boolean continueFlg = true;

			// fNodeParent�z��̓��e�ɂ���āAfNodeLastChild��fNodePrevSib�z��̓��e��⑫����B
			while (continueFlg) {
				DomArrayItemBean itemChildBean = domArray.getItem(childIndex);
				DomArrayItemBean itemChildBeanNext = domArray
						.getItem(childIndex + 1);
				if (itemChildBean == null
						|| (itemChildBean.isEmpty()
								&& (itemChildBeanNext == null || itemChildBeanNext.isEmpty()))) {
					// �󔒂���A���ł���ꍇ�A���~����B
					continueFlg = false;
				} else {
					if (itemChildBean.getFNodeParent() != DomBuildHelper.DEFAULT_VALUE) {
						DomArrayItemBean itemBeanParent = domArray
								.getItem(itemChildBean.getFNodeParent());
						if (itemBeanParent.getFNodeLastChild() < childIndex) {
							itemChildBean.setFNodePrevSib(itemBeanParent
									.getFNodeLastChild());
							itemBeanParent.setFNodeLastChild(childIndex);
						}
					}
				}
				childIndex++;
			}
		}

		if (index == 0) {
			// �C���f�b�N�X���[���̏ꍇ�A�m�[�h�^�C�v��Document�ɐݒ肷��B
			itemBeanCurrent.setFNodeType(Node.DOCUMENT_NODE);
			return;
		}

		// �I�u�W�F�N�g�̃N���X���ɂ���āA�e����⑫����B
		org.dom4j.Element targetElement = DomBuildHelper.getDomObject(currentIndex);
		String className = DomBuildHelper.getAttributeValue(targetElement,
				DumpFileXmlConstant.ATTR_REAL_TYPE);
		className = className != null ? className : DomBuildHelper.getAttributeValue(
				targetElement, DumpFileXmlConstant.ATTR_DEF_TYPE);
		if (className != null) {
			if (DomBuildHelper.checkEquals(className, DomBuildHelper.ELEMENT_NODE_TYPE)) {
				// �^�O���̍\�z
				itemBeanCurrent.setFNodeType(Node.ELEMENT_NODE);
				if (itemBeanCurrent.getFNodeName() == null) {
					itemBeanCurrent.setFNodeName(DomBuildHelper.getSuperAttributeFromDomObj(
							targetElement, DomBuildHelper.ELEMENT_SUPER_NODE_TYPE, DomBuildHelper.DOCUMENTTYPE_ATTR_NAME));
				}
			} else if (DomBuildHelper.checkEquals(className, DomBuildHelper.ATTR_NODE_TYPE)) {
				itemBeanCurrent.setFNodeType(Node.ATTRIBUTE_NODE);
				// Attribute�̍\�z
				if (itemBeanCurrent.getFNodeName() == null) {
					String[] attibute = DomBuildHelper.getElementAttibute(targetElement);
					if (attibute != null) {
						itemBeanCurrent.setFNodeName(attibute[0]);
						itemBeanCurrent.setFNodeValue(attibute[1]);
					}
				}
			} else if (DomBuildHelper.checkEquals(className, DomBuildHelper.TEXT_NODE_TYPE)
					|| DomBuildHelper.checkEquals(className, DomBuildHelper.CDATA_NODE_TYPE)) {
				// Text�̍\�z
				if (itemBeanCurrent.getFNodeValue() == null) {
					itemBeanCurrent
							.setFNodeValue(DomBuildHelper.getElementText(targetElement));
				}
			} else if (DomBuildHelper.checkEquals(className, DomBuildHelper.COMMENT_NODE_TYPE)) {
				// Comment�̍\�z
				itemBeanCurrent.setFNodeType(Node.COMMENT_NODE);
				if (itemBeanCurrent.getFNodeValue() == null) {
					itemBeanCurrent
							.setFNodeValue(DomBuildHelper.getElementText(targetElement));
				}
			} else if (DomBuildHelper.checkEquals(className, DomBuildHelper.DOCUMENTTYPE_NODE_TYPE)) {
				// DocumentType�̍\�z
				itemBeanCurrent.setFNodeType(Node.DOCUMENT_TYPE_NODE);
				if (itemBeanCurrent.getFNodeName() == null) {
					itemBeanCurrent.setFNodeName(DomBuildHelper.getSuperAttributeFromDomObj(
							targetElement,DomBuildHelper.DOCUMENTTYPE_SUPER_NODE_TYPE, DomBuildHelper.DOCUMENTTYPE_ATTR_NAME));
				}
				if (itemBeanCurrent.getFNodeValue() == null) {
					itemBeanCurrent.setFNodeValue(DomBuildHelper.getSuperAttributeFromDomObj(
							targetElement, DomBuildHelper.DOCUMENTTYPE_SUPER_NODE_TYPE, DomBuildHelper.DOCUMENTTYPE_ATTR_PUBLIC_ID));
				}
				if (itemBeanCurrent.getFNodeURI() == null) {
					itemBeanCurrent.setFNodeURI(DomBuildHelper.getSuperAttributeFromDomObj(
							targetElement, DomBuildHelper.DOCUMENTTYPE_SUPER_NODE_TYPE, DomBuildHelper.DOCUMENTTYPE_ATTR_SYSTEM_ID));
				}
				if (itemBeanCurrent.getInternalSubset().length() == 0) {//Modified by Qiu Song on 2010.02.16
					itemBeanCurrent.setInternalSubset(DomBuildHelper.getSuperAttributeFromDomObj(
							targetElement, DomBuildHelper.DOCUMENTTYPE_SUPER_NODE_TYPE, DomBuildHelper.DOCUMENTTYPE_ATTR_INTERNAL_SUBSET));
				}
			} else if (DomBuildHelper.checkEquals(className, DomBuildHelper.ENTITY_NODE_TYPE)) {
				// Entity�̍\�z
				itemBeanCurrent.setFNodeType(Node.ENTITY_NODE);
			} else if (DomBuildHelper.checkEquals(className, DomBuildHelper.ENTITY_REFERENCE_NODE_TYPE)) {
				// EntityReference�̍\�z
				itemBeanCurrent.setFNodeType(Node.ENTITY_REFERENCE_NODE);
			} else if (DomBuildHelper.checkEquals(className, DomBuildHelper.PROCESSING_INSTRUCTION_NODE_TYPE)) {
				// ProcessingInstruction�̍\�z
				itemBeanCurrent.setFNodeType(Node.PROCESSING_INSTRUCTION_NODE);
				if (itemBeanCurrent.getFNodeName() == null) {
					itemBeanCurrent.setFNodeName(DomBuildHelper.getSuperAttributeFromDomObj(
							targetElement, DomBuildHelper.PROCESSING_INSTRUCTION_SUPER_NODE_TYPE, DomBuildHelper.PROCESSING_INSTRUCTION_ATTR_TARGET));
				}
				if (itemBeanCurrent.getFNodeValue() == null) {
					itemBeanCurrent.setFNodeValue(DomBuildHelper.getSuperAttributeFromDomObj(
							targetElement, DomBuildHelper.CHARACTER_DATA_NODE_TYPE, DomBuildHelper.PROCESSING_INSTRUCTION_ATTR_DATA));
				}
			} else if (DomBuildHelper.checkEquals(className, DomBuildHelper.NOTATION_NODE_TYPE)) {
				// Notation�̍\�z
				itemBeanCurrent.setFNodeType(Node.NOTATION_NODE);
			} else {
				itemBeanCurrent.setFNodeType(DomBuildHelper.INVALID_NODE_TYPE);
			}
		}
	}
}
