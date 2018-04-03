package jp.co.ipride.excat.configeditor.model.task;

import jp.co.ipride.excat.configeditor.model.ConfigContant;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


/**
 * ��O�⃁�\�b�h�E�^�[�Q�b�g�̒��ۃN���X
 * @author Administrator
 *
 */
public abstract class Monitoring {


	protected boolean use = true;

	/**
	 * ����Config����ǂݍ��ށB
	 * @param taskNode
	 */
	public abstract void inputDocument(Node taskNode);

	/**
	 * Config Document�ɏ������ށB
	 * @param parentNode
	 */
	public abstract void outputDocument(Node parentNode);

	/**
	 * ���g�̃R�s�[
	 * @param clone
	 */
	public abstract void copyTo(Monitoring clone);

	public boolean isUse(){
		return use;
	}

	public void setUse(boolean use){
		this.use = use;
	}

	protected void inputValidAttribute(Node node){
		Node attrNode;
		NamedNodeMap map = node.getAttributes();

		attrNode = map.getNamedItem(ConfigContant.Filed_VALID);
		if (attrNode != null){
			use = Boolean.parseBoolean(attrNode.getNodeValue());
		}

	}
}
