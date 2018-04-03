package jp.co.ipride.excat.analyzer.viewer.sourceviewer;

/**
 * ���\�b�h�ɂ���ϐ��̏��
 * @author jiang
 *
 */
public class MatchVariableInfo {

	/**
	 * �ϐ���
	 */
	private String name = null;

	/**
	 * �ϐ��^�C�v
	 */
	private String type = null;
	
	/**
	 * �ϐ��̒��ԃ^�C�v
	 */
	private MiddleType varMType= null;
	
	/**
	 * �ϐ��^�C�v�̊��S�C�����i�p�b�P�[�W�����܂ށj
	 */
	private String fullType = null;
	
	/**
	 * �ϐ��̗L���͈͂̊J�n�ʒu
	 */
	private int validFrom = 0;
	
	/**
	 * �ϐ��̗L���͈͂̏I���ʒu
	 */
	private int validTo = 0;
	
	/**
	 * �ϐ��̐錾�̊J�n�ʒu
	 */
	private int startPosition = 0;
	
	/**
	 * �ϐ��̐錾�̒���
	 */
	private int length = 0;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(int validFrom) {
		this.validFrom = validFrom;
	}

	public int getValidTo() {
		return validTo;
	}

	public void setValidTo(int validTo) {
		this.validTo = validTo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	public String getFullType() {
		return fullType;
	}

	public void setFullType(String fullType) {
		this.fullType = fullType;
	}

	public MiddleType getVarMType() {
		return varMType;
	}

	public void setVarMType(MiddleType varMType) {
		this.varMType = varMType;
	}
	
	
}
