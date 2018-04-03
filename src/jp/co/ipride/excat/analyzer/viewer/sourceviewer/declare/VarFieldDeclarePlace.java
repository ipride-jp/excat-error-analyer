package jp.co.ipride.excat.analyzer.viewer.sourceviewer.declare;

/**
 * �ϐ�/�N���X���t�B�[���h�̂̐錾���J��
 * @author jiang
 *
 */
public class VarFieldDeclarePlace extends BaseDeclarePlace 
    implements IGoToDeclare{

	/**
	 * Constructor
	 * @param typeName
	 * @param startPosition
	 * @param length
	 */
	public VarFieldDeclarePlace(String typeName,int startPosition,
			int length){
		this.typeName = typeName;
		this.startPosition = startPosition;
		this.length = length;
	}
	
	/**
	 * �v�f��錾����\�[�X�^�o�C�g�R�[�h���J��
	 * @throws SourceNotFoundException
	 */
	public void gotoDeclarePlace() throws SourceNotFoundException,
	    NoClassInPathException{
        	gotoDeclareInside(startPosition,length);
	}
}
