package jp.co.ipride.excat.analyzer.viewer.sourceviewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * �\�[�X�t�@�C���ɒ�`�����N���X�̏��
 * @author jiang
 * @date 2009/9/23 add for v3 by tu
 */
public class ClassTypeInfo{

	/**
	 * �N���X��
	 */
	private String className = null;

	/**
	 * �N���X���̊J�n�ʒu
	 */
	private int classNameStart = 0;

	/**
	 * �N���X���̂̒���
	 */
	private int classNameLength = 0;

	/**
	 * �N���X��`�̊J�n�ʒu
	 */
	private int classValidFrom = 0;

	/**
	 * �N���X��`�̗L���ʒu
	 */
	private int classValidLength = 0;

	/**
	 * �e�N���X
	 */
	private ClassTypeInfo father = null;

	/**
	 * �C���i�[�N���X�ł���ꍇ�A�e�N���X���܂ރN���X��
	 * �C���i�[�N���X���ƃA�E�g�N���X���̊Ԃ�"."�ł���.
	 * �p�b�P�[�W�����ӂ��܂Ȃ�
	 */
	private String fullInnerClassName = null;

	/**
	 * �C���i�[�N���X�ł���ꍇ�A�e�N���X���܂ރN���X��
	 * �C���i�[�N���X���ƃA�E�g�N���X���̊Ԃ�"$"�ł���B
	 * �p�b�P�[�W�����ӂ��܂Ȃ�
	 */
	private String fullInnerClassName2 = null;

	/**
	 * ���\�b�h�̃��X�g
	 */
	private List<MatchMethodInfo> methodList = new ArrayList<MatchMethodInfo>();

	/**
	 * �t�B�[���h�̃��X�g
	 */
	private List<MatchField> fieldList = new ArrayList<MatchField>();

	/**
	 * �X�[�p�[�N���X�̒��ԃ^�C�v
	 */
	private MiddleType superClassMType = null;

	/**
	 * �X�[�p�[�N���X�̊��S�C����
	 */
	private String superClassName = null;

	/**
	 * �t�[���N���X��
	 */
	private String fullClassName = null;

	/**
	 * �C���^�[�t�F�[�X�ł��邩�ǂ���
	 */
	private boolean interfaceType = false;

	/**
	 * abstract
	 */
	private boolean abstractType = false;

	/**
	 * ���������C���^�[�t�F�[�X�̃��X�g
	 */
	private List<String> interfaceTypeList = null;

	/**
	 * ���������C���^�t�F�[�X�̒��ԃ^�C�v�̃��X�g
	 */
	private List<MiddleType> interfaceMTypeList = null;

	/**
	 * �N���X��.java
	 */
	private String sourceFileName = null;

	/**
	 * add for v3
	 */
	private String jarPathForClassFile = null;

	/**
	 * add for v3
	 */
	private String jarPathForJavaFile = null;

	/**
	 * add for v3
	 */
	private String classFileFullPath = null;

	/**
	 * add for v3
	 */
	private String javaFileFullPath = null;

	/**
	 * add for v3
	 */
	private List<ClassTypeInfo> implClassList = null;

	/**
	 * add for v3
	 */
	private boolean exceptionType = false;

	/**
	 * add for v3
	 */
	private Map<String, List<String>> possibleInterfaceType = null;

	public void addPossibleInterface(String simpleName, String fullName) {
		if (possibleInterfaceType == null) {
			possibleInterfaceType = new HashMap<String, List<String>>();
		}
		List<String> typeList = possibleInterfaceType.get(simpleName);
		if (typeList == null) {
			typeList = new ArrayList<String>();
			typeList.add(fullName);
			possibleInterfaceType.put(simpleName, typeList);
		} else {
			typeList.add(fullName);
		}
	}

	public Map<String, List<String>> getPossibleInterfaceType() {
		return possibleInterfaceType;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getClassNameLength() {
		return classNameLength;
	}

	public void setClassNameLength(int classNameLength) {
		this.classNameLength = classNameLength;
	}

	public int getClassNameStart() {
		return classNameStart;
	}

	public void setClassNameStart(int classNameStart) {
		this.classNameStart = classNameStart;
	}

	/**
	 * �C���i�[�N���X���ƃA�E�g�N���X���̊Ԃ�"."�ł���.
	 * @return
	 */
	public String getFullInnerClassName() {
		return fullInnerClassName;
	}

	public void setFullInnerClassName(String fullInnerClassName) {
		this.fullInnerClassName = fullInnerClassName;
	}

	/**
	 * �C���i�[�N���X���ƃA�E�g�N���X���̊Ԃ�"$"�ł���.
	 * @return
	 */
	public String getFullInnerClassName2() {
		return fullInnerClassName2;
	}

	public void setFullInnerClassName2(String fullInnerClassName2) {
		this.fullInnerClassName2 = fullInnerClassName2;
	}

	public void addMethod(MatchMethodInfo mi){
		methodList.add(mi);
	}

	public void addField(MatchField mf){
		fieldList.add(mf);

	}

	public MatchMethodInfo getMethodBySignature(String name,String sig){

		for(int i = 0; i< methodList.size();i++){
			MatchMethodInfo mi = methodList.get(i);
			String declareName = mi.getMethodName();
			String declareSig = mi.getMethodSignature();
			if(name.equals(declareName) && sig.equals(declareSig)){
				return mi;
			}
		}

		return null;
	}

	/**
	 * to be changed
	 * @param methodName
	 * @param
	 * @return
	 */
	public MatchMethodInfo getMethod(String methodName,List<String> paramsList){
		//�܂��A���\�b�h���Ō���
		int paramNumber = paramsList.size();
		List<MatchMethodInfo> firstMatchedList = getMethodByeNameAndParamNum(methodName,paramNumber);
		int firstMatchedSize = firstMatchedList.size();
		if(firstMatchedSize == 0){
			return null;
		}
		//�P�̂�
		if(firstMatchedSize == 1){
			return (MatchMethodInfo)firstMatchedList.get(0);
		}

		//�������\�b�h������
		MatchMethodInfo goodMethod = null;
		int minRank = Integer.MAX_VALUE;
		for(int i = 0; i< firstMatchedList.size();i++){
			MatchMethodInfo mi = (MatchMethodInfo)firstMatchedList.get(i);
            int rank = mi.getParamsMatchRank(paramsList);
            if(minRank > rank){
            	minRank = rank;
            	goodMethod = mi;
            }
		}
		return goodMethod;
	}

	/**
	 * ���\�b�h���ƈ����̐��Ń��\�b�h����������
	 * @param methodName
	 * @param paramNumber
	 * @return
	 */
	private List<MatchMethodInfo> getMethodByeNameAndParamNum(String methodName,int paramNumber){
		List<MatchMethodInfo> matchedMethodList = new ArrayList<MatchMethodInfo>();
		for(int i = 0; i< methodList.size();i++){
			MatchMethodInfo mi = (MatchMethodInfo)methodList.get(i);
			if(methodName.equals(mi.getMethodName()) &&
					paramNumber== mi.getParamNumber()){
				matchedMethodList.add(mi);
			}
		}

		return matchedMethodList;
	}

	public String getSuperClassName() {
		return superClassName;
	}

	public void setSuperClassName(String superClassName) {
		this.superClassName = superClassName;
	}

	public String getFullClassName() {
		return fullClassName;
	}

	public void setFullClassName(String fullClassName) {
		this.fullClassName = fullClassName;
	}

	public String getSourceFileName() {
		return sourceFileName;
	}

	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

	/**
	 * �w�肵���ꏊ���������郁�\�b�h���擾����
	 * @param offset
	 * @param length
	 * @return
	 */
	private MatchMethodInfo getMatchedMethod(int offset,int length){

		for(int i= 0; i <  methodList.size();i++){
			MatchMethodInfo methodInfo = (MatchMethodInfo)methodList.get(i);
			if(offset >= methodInfo.getOffset() &&
					offset + length <= methodInfo.getOffset() + methodInfo.getLength()){
				return methodInfo;
			}
		}

		return null;
	}

	/**
	 * �Y���ϐ��̒�`���擾
	 * @param offset
	 * @param length
	 * @param word
	 * @return
	 */
	public MatchVariableInfo getMatchedVar(int offset,int length,String word){

		//check if inside a method
		MatchMethodInfo methodInfo = getMatchedMethod(offset,length);

		if(methodInfo == null){
			return null;
		}

		//check if word is a variable
		MatchVariableInfo var = methodInfo.getVariableInfo(offset,length,word);
		return var;
	}

	/**
	 * �Y���t�B�[���h�̒�`�����擾
	 * @param word
	 * @return
	 */
	public  MatchField getMatchedField(String word){

		if(word == null){
			return null;
		}
		for(int i= 0; i <  fieldList.size();i++){
			MatchField matchField = (MatchField)fieldList.get(i);
			if(word.equals(matchField.getFieldName())){
				return matchField;
			}
		}
		return null;
	}

	public int getClassValidFrom() {
		return classValidFrom;
	}

	public void setClassValidFrom(int classValidFrom) {
		this.classValidFrom = classValidFrom;
	}

	public int getClassValidLength() {
		return classValidLength;
	}

	public void setClassValidLength(int classValidLength) {
		this.classValidLength = classValidLength;
	}

	public ClassTypeInfo getFather() {
		return father;
	}

	public void setFather(ClassTypeInfo father) {
		this.father = father;
	}

	public MiddleType getSuperClassMType() {
		return superClassMType;
	}

	public void setSuperClassMType(MiddleType superClassMType) {
		this.superClassMType = superClassMType;
	}

	public List<MatchField> getFieldList() {
		return fieldList;
	}

	public List<MatchMethodInfo> getMethodList() {
		return methodList;
	}

	public List<String> getInterfaceTypeList() {
		return interfaceTypeList;
	}

	public void setInterfaceTypeList(List<String> interfaceTypeList) {
		this.interfaceTypeList = interfaceTypeList;
	}

	public List<MiddleType> getInterfaceMTypeList() {
		return interfaceMTypeList;
	}

	public void setInterfaceMTypeList(List<MiddleType> interfaceMTypeList) {
		this.interfaceMTypeList = interfaceMTypeList;
	}

	public boolean isInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(boolean interfaceType) {
		this.interfaceType = interfaceType;
	}

	public String getClassFileFullPath() {
		return classFileFullPath;
	}

	public void setClassFileFullPath(String classFileFullPath) {
		this.classFileFullPath = classFileFullPath;
	}

	public String getJavaFileFullPath() {
		return javaFileFullPath;
	}

	public void setJavaFileFullPath(String javaFileFullPath) {
		this.javaFileFullPath = javaFileFullPath;
	}

	public String getJarPathForClassFile() {
		return jarPathForClassFile;
	}

	public void setJarPathForClassFile(String jarPathForClassFile) {
		this.jarPathForClassFile = jarPathForClassFile;
	}

	public String getJarPathForJavaFile() {
		return jarPathForJavaFile;
	}

	public void setJarPathForJavaFile(String jarPathForJavaFile) {
		this.jarPathForJavaFile = jarPathForJavaFile;
	}

	public boolean isAbstractType() {
		return abstractType;
	}

	public void setAbstractType(boolean abstractType) {
		this.abstractType = abstractType;
	}

	public List<ClassTypeInfo> getImplClassList(){
		return implClassList;
	}

	/**
	 * �w��̊Y���N���X���p�������S�ẴT�u�N���X�̈ꗗ���擾
	 * @version 3.0
	 * @return
	 */
	public List<String> getAllImplClassNameList(){
		ClassTypeInfo classInfo = null;

		if (implClassList == null){
			return null;
		}

		List<String> classNameList = new ArrayList<String>();

		for (int i =0; i<implClassList.size(); i++){
			classInfo = implClassList.get(i);
			if (classInfo.interfaceType){
				continue;
			}
			classNameList.add(classInfo.getFullClassName());
		}

		for (int i=0; i<implClassList.size(); i++){
			classInfo = implClassList.get(i);
			List<String> childList = classInfo.getAllImplClassNameList();
			if (childList != null){
				classNameList.addAll(childList);
			}
		}
		return classNameList;
	}

	/**
	 * this�I�u�W�F�N�g���p�����Ă��鑼�̃C���^�[�t�F�[�X�Ⴕ���̓N���X��ǉ�
	 * @version 3.0
	 * @since 2009/9/23
	 * @param cu
	 */
	public void addimplClass(ClassTypeInfo cu){
		if (implClassList == null){
			implClassList = new ArrayList<ClassTypeInfo>();
		}
		if (!implClassList.contains(cu)){
			implClassList.add(cu);
		}
	}

	/**
	 * �C���^�[�t�F�[�X�̃��\�b�h�����������N���X�𒊏o����B
	 */
	public void extraImplClassOfMethods(){
		if (interfaceType){
			for (int i=0; i<methodList.size(); i++){
				MatchMethodInfo method = methodList.get(i);
				if (!method.isStatic()){
					method.extraImplClassList();
				}
			}
		}
	}

	/**
	 * ���̃��\�b�h�͌p����̃C���^�[�t�F�[�X�̃��\�b�h����Ă΂��B
	 * �P�D�����̃��\�b�h���`�F�b�N����B
	 * �Q�D�T�u�E�N���X�̃��\�b�h���`�F�b�N����B
	 * @param classMethod
	 * @return
	 */
	public void getImplClassListForThisMethod(MatchMethodInfo Method,
											List<ClassTypeInfo> result){
		//1.�����̃��\�b�h���`�F�b�N����B
		MatchMethodInfo myMethod = this.getMethodBySignature(
											Method.getMethodName(),
											Method.getMethodSignature());
		if (myMethod != null && !this.isInterfaceType()
							&& !myMethod.isNative()
							&& !myMethod.isAbstract()
							&& !result.contains(this)){
			result.add(this);
		}
		//2.�Y���N���X���p�������N���X���`�F�b�N
		if (implClassList != null){
			for (int i=0; i<implClassList.size(); i++){
				ClassTypeInfo classInfo = implClassList.get(i);
				classInfo.getImplClassListForThisMethod(Method, result);
			}
		}
	}

	public void setExceptionType(boolean flag){
		this.exceptionType = flag;
		if (implClassList !=null){
			for (int i=0; i<implClassList.size(); i++){
				ClassTypeInfo classInfo = (ClassTypeInfo)implClassList.get(i);
				classInfo.setExceptionType(true);
			}
		}
	}

	public void setExceptionType() {
		if ("java.lang.Throwable".equals(className)
				|| "java.lang.Exception".equals(className)
				|| "java.lang.RuntimeException".equals(className)
				|| "java.lang.Throwable".equals(superClassName)
				|| "java.lang.Exception".equals(superClassName)
				|| "java.lang.RuntimeException".equals(superClassName)
				){
			setExceptionType(true);
		}
	}

	public boolean isExceptionType() {
		return exceptionType;
	}

	public String getPackageName(){
		int index;
		String packageName;
		index= fullClassName.lastIndexOf(".");
		if (index <0){
			packageName = "";
		}else{
			packageName = fullClassName.substring(0, index);
		}
		return packageName;
	}

}
