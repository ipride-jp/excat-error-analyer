package jp.co.ipride.excat.analyzer.viewer.sourceviewer;

import java.util.ArrayList;
import java.util.List;

import jp.co.ipride.excat.analyzer.viewer.sourceviewer.declare.ClassRepository;
import jp.co.ipride.excat.common.utility.HelperFunc;

/**
 * AST tree����擾�������\�b�h���
 * @author jiang
 *
 */
public class MatchMethodInfo{

	/**
	 * �����K���̃����N
	 */
	private static int RANK_SAME = 0;
	private static int RANK_NULL = 0;
	private static int RANK_SUBTYPE = 1;
	private static int RANK_CAST = 2;
	private static int RANK_UNRESOLVED = 10;
	private static int RANK_UNMATCHED = 100;
	/**
	 * ���\�b�h�̊J�n�ʒu
	 */
	private int offset = 0;

	/**
	 * ���\�b�h�̏I���ʒu
	 */
	private int length = 0;

	/**
	 * ���\�b�h��
	 */
	private String methodName = null;

	/**
	 * �����\�b�h��������N���X��
	 */
	private String className = null;

	/**
	 * ���N���X��������p�b�P�[�W��
	 */
	private String packageName = null;

	/**
	 * �����̌�
	 */
	private int paramNumber = 0;

	/**
	 * �ϐ��̃��X�g
	 */
	private List<MatchVariableInfo> variableList = new ArrayList<MatchVariableInfo>();

	/**
	 * ���\�b�h���̊J�n�ʒu
	 */
	private int methodNameStartPos = 0;

	/**
	 * ���\�b�h���̒���
	 */
	private int methodNameOffet = 0;

	/**
	 * �߂�l�̒��ԃ^�C�v
	 */
	private MiddleType returnMType = null;

	/**
	 * �߂�l�̃^�C�v�i�p�b�P�[�W�����܂ށj
	 */
	private String returnType = null;

	/**
	 * �����̃^�C�v�i��͂��݁j��ۑ����郊�X�g
	 */
	private List<String> paramTypeList = null;

	/**
	 * ���\�b�hSignature
	 */
	private String methodSignature = null;

	//v3
	private boolean isNative = false;

	//v3
	private boolean isAbstract = false;

	//v3
	private boolean isStatic = false;

	//if this method is interface or abstract,it will be filled. v3
	private List<ClassTypeInfo> implClassList = new ArrayList<ClassTypeInfo>();

	//v3
	private ClassTypeInfo myClassTypeInfo=null;


	public MatchMethodInfo(){

	}
	/**
	 * Signature�̐ݒ�
	 * ���ӁF�����̃^�C�v�Ɩ߂�l�̃^�C�v���m�肵�Ă���
	 * ���̃��\�b�h���Ăяo��
	 *
	 */
	public void generateSignature(){
		if(paramTypeList == null){
			return;
		}
		StringBuffer buf = new StringBuffer();
		buf.append('(');
		for(int i = 0; i < paramTypeList.size();i++ ){
			String paramType = paramTypeList.get(i);
			buf.append(paramType);
			if(i < paramTypeList.size() -1){
				buf.append(',');
			}
		}

		buf.append(')');

		buf.append(returnType);
		methodSignature = buf.toString();
	}

	public void addVariable(MatchVariableInfo var){
		variableList.add(var);
	}

	/**
	 * ����Signature�ł��邩�ǂ����𔻒f����
	 * @param signature
	 * @return
	 */
	public boolean isSameSignature(String signature){
		if(signature == null){
			return false;
		}

		int posOfLeft = signature.indexOf('(');
		if(posOfLeft < 0){
			return false;
		}

		int posOfRight = signature.indexOf(')');
		if(posOfRight < 0){
			return false;
		}

		//no param
		if(posOfRight - posOfLeft == 1){
			if(paramNumber == 0){
				return true;
			}else{
				return false;
			}
		}
		String paramsString = signature.substring(posOfLeft + 1,
				posOfRight);
		String[] params = paramsString.split(",");
		if(params.length != paramNumber){
			return false;
		}

		for(int i = 0; i < paramNumber;i++){
			MatchVariableInfo var  = (MatchVariableInfo)variableList.get(i);
			String typeOfAst = var.getType();
			String typeOfDump = params[i];
			if(typeOfAst.equals(typeOfDump)){
				continue;
			}else{
				//�_���v�t�@�C������擾����Signature���p�b�P�[�W���t��
				int posOfLastPoint = typeOfDump.lastIndexOf('.');
				if(posOfLastPoint <=0){
					return false;
				}else{
					String simpleName = typeOfDump.substring(
							posOfLastPoint + 1);
					// ���̌^�̏ꍇ�A�^�C�v���������r����i��Q #529�j
					int index = typeOfAst.indexOf("<");
					if (index > 0) {
						typeOfAst = typeOfAst.substring(0, index);
					}
					if(typeOfAst.equals(simpleName)){
						continue;
					}else{
						return false;
					}
				}

			}
		}
		return true;
	}

	/**
	 * �����̓K�������N�̕]��
	 * �����^�C�v�F0 <br>
	 * �T�[�u�^�C�v:1 <br>
	 * Cast�F2<br>
	 * "null":0
	 * �����null(������):100
	 * @param invokerParamsList
	 * @return
	 */
	public int getParamsMatchRank(List<String> invokerParamsList){
		int rank = 0;

		for(int i = 0; i < invokerParamsList.size();i++){
			String invokerType = invokerParamsList.get(i);
			String definedType = (String)paramTypeList.get(i);
			if(invokerType == null || definedType == null){
				//������
				rank += RANK_UNRESOLVED ;
			}else if("null".equals(invokerType)){
				//�n���ꂽ������null�ł���
				rank += RANK_NULL ;
			}else if(invokerType.equals(definedType)){
				//�n���ꂽ�������錾�Ɠ���
				rank += RANK_SAME ;
			}else {
				ClassRepository crep = ClassRepository.getInstance();
				if(HelperFunc.isPrimitive(invokerType)){
					if(crep.isWidenPrimitiveConversion(invokerType,definedType)){
						rank += RANK_CAST;
					}else{
						rank += RANK_UNMATCHED;
					}
				}else if(crep.isSubType(invokerType, definedType)){
					rank += RANK_SUBTYPE ;
				}else{
					rank += RANK_UNMATCHED;
				}
			}
		}

		return rank;
	}



	public MatchVariableInfo getVariableInfo(int offset,int length,String word){

		for(int i = 0; i < variableList.size(); i++){
			MatchVariableInfo var = (MatchVariableInfo)variableList.get(i);
			if(var.getName().equals(word)){
				int validFrom = var.getValidFrom();
				int validTo = var.getValidTo();
				if(offset >= validFrom && offset + length -1 <= validTo){
					return var;
				}
			}
		}
		return null;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

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

	public int getParamNumber() {
		return paramNumber;
	}

	public void setParamNumber(int paramNumber) {
		this.paramNumber = paramNumber;
	}

	public int getMethodNameOffet() {
		return methodNameOffet;
	}

	public void setMethodNameOffet(int methodNameOffet) {
		this.methodNameOffet = methodNameOffet;
	}

	public int getMethodNameStartPos() {
		return methodNameStartPos;
	}

	public void setMethodNameStartPos(int methodNameStartPos) {
		this.methodNameStartPos = methodNameStartPos;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getMethodSignature() {
		return methodSignature;
	}

	public void setMethodSignature(String methodSignature) {
		this.methodSignature = methodSignature;
	}

	public MiddleType getReturnMType() {
		return returnMType;
	}

	public void setReturnMType(MiddleType returnMType) {
		this.returnMType = returnMType;
	}

	public List<MatchVariableInfo> getVariableList() {
		return variableList;
	}

	public List<String> getParamTypeList() {
		return paramTypeList;
	}

	public void setParamTypeList(List<String> paramTypeList) {
		this.paramTypeList = paramTypeList;
	}

	public boolean isNative() {
		return isNative;
	}

	public void setNative(boolean isNative) {
		this.isNative = isNative;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	/**
	 * �Y�����\�b�h�����C���^�[�t�F�[�X�����������N���X���������A
	 * �Y�����\�b�h�����������N���X�𒊏o
	 * @date 2009/9/25
	 */
	public void extraImplClassList(){
		List<ClassTypeInfo> subClassList = myClassTypeInfo.getImplClassList();
		if (subClassList != null){
			for (int i=0; i<subClassList.size(); i++){
				ClassTypeInfo classInfo = subClassList.get(i);
				classInfo.getImplClassListForThisMethod(this, implClassList);
			}
		}
	}


	public List<ClassTypeInfo> getImplClassList(){
		return implClassList;
	}

	public void setMyClassTypeInfo(ClassTypeInfo classTypeInfo){
		this.myClassTypeInfo = classTypeInfo;
	}

}
