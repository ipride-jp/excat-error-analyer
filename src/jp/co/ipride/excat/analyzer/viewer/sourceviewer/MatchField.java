package jp.co.ipride.excat.analyzer.viewer.sourceviewer;

/**
 * AST tree����擾����Field���
 * @author jiang
 *
 */
public class MatchField {


	/**
	 * ��������N���X�̊J�n�ʒu
	 */
	private int offset = 0;

	/**
	 * ��������N���X�̏I���ʒu
	 */
	private int length = 0;

	/**
	 * field name
	 */
	private String fieldName = null;

	/**
	 * field type
	 */
	private String fieldType = null;

	/**
	 * �t�B�[���h�̒��ԃ^�C�v
	 */
	private MiddleType fieldMType = null;

	/**
	 * field type with package name
	 */
	private String fullFieldType = null;

	/**
	 * ��field��������N���X��
	 */
	private String className = null;

	/**
	 * field�̐錾�̊J�n�ʒu
	 */
	private int startPosition = 0;

	/**
	 * field�̐錾�̒���
	 */
	private int fieldLength = 0;

	/**
	 * ���N���X��������p�b�P�[�W��
	 */
	private String packageName = null;


	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(int fieldLength) {
		this.fieldLength = fieldLength;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	public String getFullFieldType() {
		return fullFieldType;
	}

	public void setFullFieldType(String fullFieldType) {
		this.fullFieldType = fullFieldType;
	}

	public MiddleType getFieldMType() {
		return fieldMType;
	}

	public void setFieldMType(MiddleType fieldMType) {
		this.fieldMType = fieldMType;
	}

	public void setFullClassName(String fullName){
		int pos = fullName.lastIndexOf(".");
		if (pos>0){
			packageName = fullName.substring(0, pos);
			className = fullName.substring(pos+1);
		}else{
			packageName="";
			className=fullName;
		}
	}

	public String getFullClassName(){
		return packageName + "." + className;
	}
}
