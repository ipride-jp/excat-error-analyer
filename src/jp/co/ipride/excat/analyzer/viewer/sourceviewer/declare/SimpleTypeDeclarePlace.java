package jp.co.ipride.excat.analyzer.viewer.sourceviewer.declare;

import jp.co.ipride.excat.analyzer.viewer.sourceviewer.ClassTypeInfo;


/**
 * Simple type�̐錾���J���N���X
 * @author jiang
 *
 */
public class SimpleTypeDeclarePlace extends BaseDeclarePlace
    implements IGoToDeclare{

	/**
	 * �R���X�g���N�^
	 * @param nodeOfMySelf
	 */
	public SimpleTypeDeclarePlace(String typeName,int startPosition,int length){
		this.typeName = typeName;
		this.startPosition = startPosition;
		this.length = length;
	}


	/**
	 * �v�f��錾����\�[�X�^�o�C�g�R�[�h���J��
	 * @throws SourceNotFoundException
	 */
	public void gotoDeclarePlace() throws SourceNotFoundException,NoClassInPathException{
		//�����\�[�X�t�@�C���ɂ��邩�ǂ���
		ClassTypeInfo cu = super.javaSourceVisitor.getClassTypeInfoByName(
				typeName,startPosition,length);
		if(cu != null){
	        gotoDeclareInside(cu.getClassNameStart(), cu.getClassNameLength());
		}else{
			gotoClassDeclare(typeName);
		}

	}

}
