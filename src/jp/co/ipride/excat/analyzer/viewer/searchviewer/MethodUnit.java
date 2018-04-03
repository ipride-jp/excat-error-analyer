package jp.co.ipride.excat.analyzer.viewer.searchviewer;

import java.util.ArrayList;
import java.util.List;

import jp.co.ipride.excat.analyzer.viewer.stackviewer.CommonNodeLabel;

import org.w3c.dom.Node;

/**
 * ���\�b�h�P�ʂ̌�������
 * @author tu-ipride
 * @version 3.0
 * @date 2009/10/12
 */
public class MethodUnit {

	public static String SEQ = "0";

	private int sequence;

	private Node methodNode;

	//this method directly reference object(super-object, argument, variant)
	private List<ObjectUnit> localVarList = new ArrayList<ObjectUnit>();


	public void setSequence(int no){
		this.sequence = no;
	}

	public int getSequence(){
		return this.sequence;
	}

	public void setMethodNode(Node node){
		this.methodNode=node;
	}

	public void addParaList(List<ObjectUnit> nodes){
		localVarList.addAll(nodes);
	}

	public void addParaUnit(ObjectUnit unit){
		localVarList.add(unit);
	}

	public int getParaSize(){
		return localVarList.size();
	}

	public List<ObjectUnit> getVariantList(){
		return localVarList;
	}

	public String methodName(){
		return CommonNodeLabel.getText(methodNode);
	}

	/**
	 * �e���[�J���ϐ����`�F�b�N����B
	 * ���[�g�𓞒B����܂ł��A�l�X�g�K�w�ɒB����ꍇ�A��A�Q�Ƃ̏ꍇ�ɏI��
	 * @return
	 */
	public MethodCell extracRoot(){
		int depth =0;
		MethodCell me = new MethodCell(this);
		for (int i=0; i<localVarList.size(); i++){
			ObjectUnit objectUnit = localVarList.get(i);
			ObjectCell ret = objectUnit.extracRoot(depth);
			if (ret != null){
				me.addChild(ret);
				ret.setParent(me);
			}
		}
		return me;
	}


	public void clear(){
		localVarList.clear();
		localVarList=null;
		methodNode=null;
	}
}
