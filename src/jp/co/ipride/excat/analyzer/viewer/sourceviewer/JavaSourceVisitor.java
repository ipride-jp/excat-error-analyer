package jp.co.ipride.excat.analyzer.viewer.sourceviewer;

import java.util.ArrayList;
import java.util.List;

import jp.co.ipride.excat.analyzer.viewer.sourceviewer.declare.ClassRepository;
import jp.co.ipride.excat.common.setting.SettingManager;
import jp.co.ipride.excat.common.utility.HelperFunc;

import org.apache.bcel.classfile.JavaClass;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

/**
 *
 * visitor to visit ast tree of java source
 * @author jiang
 *
 */
public class JavaSourceVisitor extends BaseVisitor{
	/**
	 * �C���|�[�g���̃��X�g
	 */
	private List<ImportClassInfo> importList = new ArrayList<ImportClassInfo>();


	/**
	 * ���̃t�@�C���ɂ���N���X�錾�̃��X�g
	 */
	protected List<ClassTypeInfo> classList = new ArrayList<ClassTypeInfo>();

	/**
	 * ���N���X�̒�`���܂ރ\�[�X�t�@�C�����A�p�X�����܂ނȂ�
	 * ��FAClass.java
	 * �ړI�́A�C���i�[�N���X�̒�`�t�@�C����T�����߂ł���B
	 */
	protected String sourceFileName = null;

	/**
	 * ���̃t�@�C���ɂ��郁�\�b�h�̃��X�g
	 */
	private List<MatchMethodInfo> methodList = new ArrayList<MatchMethodInfo>();

	/**
	 * ���̃t�@�C���ɂ���t�B�[���h�̃��X�g
	 */
	private List<MatchField> fieldList = new ArrayList<MatchField>();


	/**
	 * ���͂��Ă��郁�\�b�h
	 */
	private MatchMethodInfo curMethod = null;

	/**
	 * �X�[�p�[�N���X�A�t�B�[���h�A�ϐ��̃^�C�v����͂��āAClassReposity�ɓo�^����B<br>
	 *�@���ӁFvisit���I��������ɁA�����\�b�h���Ăяo���B<br>
	 *        �������Ȃ��ƁA�\�[�X�t�@�C���̌㕔�Ő錾���ꂽ�N���X������͂ł��Ȃ��B
	 */
	public void resolveType(){

		ClassRepository instance = ClassRepository.getInstance();
		for(int i = 0; i < classList.size();i++){
			ClassTypeInfo classTypeInfo =(ClassTypeInfo) classList.get(i);
			classTypeInfo.setSourceFileName(sourceFileName);

			//super class����͂���
			MiddleType mt = classTypeInfo.getSuperClassMType();
			String superClassName = null;
			if(mt != null){
				superClassName = analyzeType(mt);
			}else{
				if(!classTypeInfo.isInterfaceType()){
					if(!"java.lang.Object".equals(classTypeInfo.getFullClassName())){
						superClassName = "java.lang.Object";
					}
				}
			}
			classTypeInfo.setSuperClassName(superClassName);

			//�C���^�[�t�F�[�X����͂���
			List<MiddleType> interfaceMTypeList = classTypeInfo.getInterfaceMTypeList();
			List<String> interfaceTypeList = new ArrayList<String>();
			if(interfaceMTypeList != null && interfaceMTypeList.size() > 0){
				for(int ii = 0; ii < interfaceMTypeList.size();ii++){
					MiddleType imt = (MiddleType)interfaceMTypeList.get(ii);
					String itype = analyzeType(imt);
					interfaceTypeList.add(itype);
				}
				//interfaceMTypeList���J��
				interfaceMTypeList.clear();
				classTypeInfo.setInterfaceMTypeList(null);
			}
			classTypeInfo.setInterfaceTypeList(interfaceTypeList);

			//�t�B�[���h�^�C�v����͂���
			List<MatchField> fList = classTypeInfo.getFieldList();
			for(int j = 0; j < fList.size();j++){
				MatchField mf = (MatchField)fList.get(j);
				MiddleType fmtype = mf.getFieldMType();
				String fullFieldTypeStr = analyzeType(fmtype);
				mf.setFullFieldType(fullFieldTypeStr);
			}

			//���\�b�h�̃^�C�v����͂���
			List<MatchMethodInfo> mList= classTypeInfo.getMethodList();
			for(int j = 0; j < mList.size();j++){
				MatchMethodInfo mmi = mList.get(j);
				MiddleType rmt = mmi.getReturnMType();
				String returnTypeStr = null;
				if(rmt == null){
					//constructor
					returnTypeStr = "void";
				}else{
					returnTypeStr = analyzeType(rmt);
				}

				mmi.setReturnType(returnTypeStr);

				//�ϐ�����͂���
				List<MatchVariableInfo> vList = mmi.getVariableList();
				List<String> paramTypeList = new ArrayList<String>();
				int paramNumber = mmi.getParamNumber();
				for(int k = 0; k < vList.size(); k++){
					MatchVariableInfo var = (MatchVariableInfo)vList.get(k);
					MiddleType varMType = var.getVarMType();
					String fullVarType = analyzeType(varMType);
					var.setFullType(fullVarType);
					if(k < paramNumber){
						paramTypeList.add(fullVarType);
					}
				}

				//�����̃��X�g
				mmi.setParamTypeList(paramTypeList);
				//signature�̐ݒ�
				mmi.generateSignature();
				// ��Q #539
				//construct name
				String className = HelperFunc.getPureClassName(classTypeInfo.getFullClassName());
				if (className.equals(mmi.getMethodName())){
					mmi.setMethodName("<init>");
				}
			}

			//�N���X���|�W�g���ɓo�^����
			instance.addClassUnitFromSource(classTypeInfo);
		}
	}

	/**
	 * �w�肵��node���ϐ����^�t�B�[���h���ł��邩�ǂ����𔻒f
	 * @param node
	 * @return
	 */
	public boolean isFieldOrVariable(ASTNode node){

		if(!(node instanceof SimpleName)){
			return false;
		}
		ASTNode parent = node.getParent();
		if(parent == null){
			return false;
		}
		//���\�b�h���ł��邩�ǂ���
		if(parent instanceof MethodInvocation){
			MethodInvocation mi = (MethodInvocation)parent;
			if(node == mi.getName()){
				return false;
			}
		}

		//�^�C�v�ł��邩�ǂ���
		if(parent instanceof Type){
			return false;
		}

		return true;
	}

	/**
	 * to check if the word is a variable or field
	 * @param offset the position of the word
	 * @param length the length of the word
	 * @param word the word
	 * @return true if the word is a varable of field
	 */
	public String getObjectToShowInfo(int offset,int length,String word){

		//first to check if it is a variable
		String varInfo = getVariableObjectInfo(offset,length,word);
		if(varInfo != null){
			return varInfo;
		}

		//if it is not a varialbe,check if it is a field
		MatchField matchField = getMatchedField(offset,length,word);
		if(matchField != null){
			String fieldInfo = StackTreeAccess.getFieldInfo(matchField);

            if(fieldInfo != null){
            	return fieldInfo;
            }else{
            	return matchField.getFieldType() + " " + matchField.getFieldName();
            }

		}else{
			return null;
		}
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
	 * @date 2009/10/23 tu
	 * @param methodInfo
	 * @return
	 */
	public MatchMethodInfo getMatchedMethod(MethodInfo methodInfo){
		// ��Q #529
//		for (MatchMethodInfo matchM : methodList){
//			if (matchM.getMethodName().equals(methodInfo.getMethodName())
//				&& matchM.getMethodSignature().equals(methodInfo.getMethodSig())){
//				return matchM;
//			}
//		}
		for (MatchMethodInfo matchM : methodList){
			if (matchM.getMethodName().equals(methodInfo.getMethodName())
				&& (matchM.getMethodSignature().equals(methodInfo.getMethodSig())
						|| matchM.isSameSignature(methodInfo.getMethodSig()))){
				return matchM;
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
	 * �w�肳�ꂽWord�͕ϐ��ł���΁A���̕ϐ��̏���\����������擾����B
	 * @param offset
	 * @param length
	 * @param word
	 * @return
	 */
	public String getVariableObjectInfo(int offset,int length,String word){

		//check if inside a method
		MatchMethodInfo methodInfo = getMatchedMethod(offset,length);

		if(methodInfo == null){
			return null;
		}

		//check if word is a variable
		MatchVariableInfo var = methodInfo.getVariableInfo(offset,length,word);
		if(var != null){
			String text = StackTreeAccess.getVariableInfo(methodInfo,
					var);
			if(text != null){
				return text;
			}else{
				return var.getType() + " " + var.getName();
			}

		}

		return null;
	}

	/**
	 * �Y���t�B�[���h�̒�`�����擾
	 * @param offset
	 * @param length
	 * @param word
	 * @return
	 */
	public  MatchField getMatchedField(int offset,int length,String word){

		if(word == null){
			return null;
		}

		MatchField lastField = null;
		int minLength = 0;
		for(int i= 0; i <  fieldList.size();i++){
			MatchField matchField = (MatchField)fieldList.get(i);
			if(word.equals(matchField.getFieldName())){
				if(offset >= matchField.getOffset() &&
						offset + length <= matchField.getOffset() + matchField.getLength()){
					if(minLength == 0){
						minLength = matchField.getLength();
						lastField = matchField;
					}
					if(matchField.getLength() < minLength){
						minLength = matchField.getLength();
						lastField = matchField;
					}

				}
			}
		}
		return lastField;
	}

	/**
	 * �N���X����p���āA�Y���C���|�[�g������������B
	 * @param className
	 * @return
	 */
	private String getSingleImportType(String className){
		if(className == null){
			return null;
		}
		String pointClassName = "." + className;
		for(int i = 0; i < importList.size();i++){
			ImportClassInfo importClassInfo =(ImportClassInfo) importList.get(i);
			if(!importClassInfo.isOnDemand()){
				String importName = importClassInfo.getName();
				if(importName.endsWith(pointClassName)){
					return importName;
				}
			}

		}

		return null;
	}


	/**
	 * �����^�C�v���C���|�[�g����p�b�P�[�W���̃��X�g���擾
	 * @return
	 */
	private List<String> getMultiImportType(){
		List<String> list = new ArrayList<String>();
		for(int i = 0; i < importList.size();i++){
			ImportClassInfo importClassInfo =(ImportClassInfo) importList.get(i);
			if(importClassInfo.isOnDemand()){
				String importName = importClassInfo.getName();
				list.add(importName);
			}

		}

		return list;
	}

	/**
	 * node���܂ނ���N���X���擾
	 * @param node �m�[�g
	 * @return�@�m�[�h���܂ރN���X
	 */
	public ClassTypeInfo getDeclareClass(ASTNode node){

	    ASTNode classType = node.getParent();
	    ClassTypeInfo cu = null;
	    int from = node.getStartPosition();
	    int length = node.getLength();
		while(classType != null){
			if(classType instanceof TypeDeclaration){
				TypeDeclaration classDeclaration = (TypeDeclaration)classType;
				//�N���X��
				String curClassName = null;
				SimpleName sn = classDeclaration.getName();
				curClassName = sn.getIdentifier();
				cu = getClassTypeInfoByName(curClassName,from,length);
				break;

			}else{
				classType = classType.getParent();
			}
		}
		return cu;
	}

	/**
	 * ���\�b�h�̒�`�ӏ���ݒ肷��B
	 * @param methodInfo
	 */
	public void setMethodDeclarePos(MethodInfo methodInfo){

		ClassTypeInfo classTypeInfo = null;
		for(int i = 0; i < classList.size();i++){
			classTypeInfo =(ClassTypeInfo) classList.get(i);
			if(methodInfo.getClassSig().equals(classTypeInfo.getFullClassName())){
				break;
			}
		}

		if(classTypeInfo != null){
			String fullClassName = classTypeInfo.getFullClassName();
			if(fullClassName != null){
				classTypeInfo = ClassRepository.getInstance().getClassUnit(fullClassName);
			}
			if(classTypeInfo != null){
				MatchMethodInfo mi =  classTypeInfo.getMethodBySignature(
						methodInfo.getMethodName(),methodInfo.getMethodSig());
				if(mi != null){
					methodInfo.setStartPosition(mi.getMethodNameStartPos());
					methodInfo.setOffset(mi.getMethodNameOffet());
				}
			}

		}
	}

	/**
	 * �^�C�v������͂���
	 * @param type
	 * @return
	 */
	protected String analyzeType(MiddleType type) {
		if(type == null){
			return null;
		}

		if(type.isArrayType()){
			Type elementType = type.getElementType();
			MiddleType eMT = new MiddleType(elementType);
			String elementTypeStr = analyzeType(eMT);
			if(elementTypeStr != null){
				StringBuffer buf = new StringBuffer(elementTypeStr);
				for(int i = 0; i < type.getDimensions();i++){
					buf.append("[]");
				}
				return buf.toString();
			}else{
				return null;
			}
		}

		if(type.isPrimitiveType()){
			return type.getTypeString();
		}


		if(type.isVoid()){
			return type.getTypeString();
		}
		return analyzeClassType(type);
	}

	/**
	 * �N���X�^�C�v����͂���
	 * @param classType
	 * @return �X�[�p�[�N���X���i�p�b�P�[�W�����܂ށj
	 */
	protected String analyzeClassType(MiddleType classType){

		String typeName = classType.getTypeString();
		int index = typeName.indexOf("<");
		if (index > 0) {
			typeName = typeName.substring(0, index);
		}

		//�����\�[�X�t�@�C���ɂ��邩�ǂ���
		String fullClassName =null;
		ClassTypeInfo cu = getClassTypeInfoByName(typeName,classType.getStartPosition(),
				classType.getLength());
		if(cu != null){
			//�����t�@�C���ɂ������N���X�����݂���\��������
			fullClassName = cu.getFullClassName();
			return fullClassName;
		}

		//import ���ɂ��邩�ǂ������`�F�b�N����
		String importClassName = getSingleImportType(typeName);
		if(importClassName != null){
			return importClassName;

		}

		//?
		if(classType.isQualifiedType()){
			JavaClass clazz = SettingManager.getSetting(
			    ).getByteCodeContents(typeName);
			if(clazz != null){
				return 	typeName;
			}
		}

		//java.lang�̃N���X�ł��邩�ǂ���
		//��Q#529
		//String langClass = "java.lang." + typeName;
		String langClass = typeName.startsWith("java.lang.") ? typeName : "java.lang." + typeName;
		if(HelperFunc.isJavaLangClass(langClass)){
			return langClass;
		}

        //�����p�b�P�[�W�ɂ��邩�H
		fullClassName= HelperFunc.getFullClassName(packageName,
				typeName);
		String fileContents = SettingManager.getSetting()
		    .getJavaSourceContent(fullClassName);
		if(fileContents != null){
			return fullClassName;
		}

		//�����̃^�C�v���C���|�[�g����p�b�P�[�W���̃��X�g�̎擾
		List<String> listImportM = getMultiImportType();
		if(listImportM.size() != 0 && typeName.lastIndexOf(".") <= 0){
			//import xxx.*�̏ꍇ�A
			for(int i = 0;i < listImportM.size();i++){
				String importPackageName = (String)listImportM.get(i);
				fullClassName= HelperFunc.getFullClassName(importPackageName,
						typeName);
				if(listImportM.size() == 1){
					return fullClassName;
				}else{
					//������import xxxx.*������ꍇ�A�ݒ肳�ꂽ�N���X�p�X
					//�ɂ���N���X��L���ȑΏۃN���X�Ƃ���H
					JavaClass clazz = SettingManager.getSetting(
					).getByteCodeContents(fullClassName);
					if(clazz != null){
						return fullClassName;
					}
				}

			}
		}

		// ��Q #529
		//return null;
		return typeName;
	}

	/**
	 * visit import sentence
	 */
	public boolean visit(ImportDeclaration node){

		ImportClassInfo importClassInfo = new ImportClassInfo();
		importList.add(importClassInfo);

		importClassInfo.setOnDemand(node.isOnDemand());
		Name name = node.getName();
		importClassInfo.setName(name.getFullyQualifiedName());
		return super.visit(node);
	}

	/**
	 * visit class declaratioin
	 */
	public boolean visit(TypeDeclaration node){

		SimpleName sn = node.getName();

		ClassTypeInfo classTypeInfo = new ClassTypeInfo();
		classList.add(classTypeInfo);

		//source file name
		classTypeInfo.setSourceFileName(sourceFileName);
		//class name
		String className = sn.getIdentifier();
		classTypeInfo.setClassName(className);
		//start position and length
		classTypeInfo.setClassNameStart(sn.getStartPosition());
		classTypeInfo.setClassNameLength(sn.getLength());
		classTypeInfo.setClassValidFrom(node.getStartPosition());
		classTypeInfo.setClassValidLength(node.getLength());

		//get inner class name with format of a.b
		String fullInnerClassName = getDeclareClassName(node,
				".");
		classTypeInfo.setFullInnerClassName(fullInnerClassName);

		//get inner class name with format of a$b
		String fullInnerClassName2 = getDeclareClassName(node,
		       "$");
		classTypeInfo.setFullInnerClassName2(fullInnerClassName2);

		//�e
		ClassTypeInfo father = getDeclareClass(node);
		classTypeInfo.setFather(father);

	    //���S�C����
		String fullClassName = HelperFunc.getFullClassName(packageName,
				fullInnerClassName2);
		classTypeInfo.setFullClassName(fullClassName);

		//get modifiers
		int modifiers = node.getModifiers();
		if((modifiers & Modifier.PUBLIC) == Modifier.PUBLIC ){
			if(className.equals(fullInnerClassName)){
				sourceFileName = className + ".java";
			}
		}else if((modifiers & Modifier.ABSTRACT) == Modifier.ABSTRACT ){
			classTypeInfo.setAbstractType(true);
		}

		//super class
		Type superType = node.getSuperclassType();
		if(superType != null){
			MiddleType mt = new MiddleType(superType);
			classTypeInfo.setSuperClassMType(mt);
		}

		//�C���^�t�F�[�X
		classTypeInfo.setInterfaceType(node.isInterface());
		List<Type> listInterface = node.superInterfaceTypes();
		List<MiddleType> listInterfaceTypes = new ArrayList<MiddleType>();
		for(int i = 0; i <listInterface.size();i++ ){
			Type interfaceType = (Type)listInterface.get(i);
			MiddleType mt = new MiddleType(interfaceType);
			listInterfaceTypes.add(mt);
		}
		classTypeInfo.setInterfaceMTypeList(listInterfaceTypes);
		return super.visit(node);
	}

	/**
	 * visit method declaration
	 */
	public boolean visit(MethodDeclaration node){

		curMethod = new MatchMethodInfo();
		methodList.add(curMethod);
		ClassTypeInfo  currentClass = getDeclareClass(node);
		currentClass.addMethod(curMethod);
		curMethod.setMyClassTypeInfo(currentClass);  //tu add for v3

		//�߂�l�^�C�v
		Type returnType= node.getReturnType2();
		if(returnType != null){
		    MiddleType mt = new MiddleType(returnType,node.getExtraDimensions());
		    curMethod.setReturnMType(mt);
		}
		int modifiers = node.getModifiers();
		if((modifiers & Modifier.NATIVE) == Modifier.NATIVE ){
			curMethod.setNative(true);
		}else if((modifiers & Modifier.ABSTRACT) == Modifier.ABSTRACT ){
			curMethod.setAbstract(true);
		}else if((modifiers & Modifier.STATIC) == Modifier.STATIC ){
			curMethod.setStatic(true);
		}


		//�J�n�ʒu
		curMethod.setOffset(node.getStartPosition());

		//���\�b�h��
		curMethod.setLength(node.getLength());

		//���\�b�h��
		SimpleName sname = node.getName();
		curMethod.setMethodName(sname.getIdentifier());
        curMethod.setMethodNameStartPos(sname.getStartPosition());
        curMethod.setMethodNameOffet(sname.getLength());


		//�p�����[�^
		List<SingleVariableDeclaration> listParams = node.parameters();
		if(listParams != null){
			curMethod.setParamNumber(listParams.size());
			for(int i = 0;i < listParams.size();i++){
				//�������ϐ��ł���
				SingleVariableDeclaration param = (SingleVariableDeclaration)listParams.get(i);
				accessSingleVarDeclar(param,
						node.getStartPosition() + node.getLength() - 1 );
			}
		}

		//�N���X��
		String curClassName = getDeclareClassName(node.getParent(),"$");
		curMethod.setClassName(curClassName);

		curMethod.setPackageName(packageName);

		return super.visit(node);

	}

	/**
	 * �C���i�[�N���X�����A�\�[�X�t�@�C���Œ�`�����N���X���̎擾
	 * @param className �C���i�[�N���X�̏ꍇ�AA$B�̌`
	 * @return
	 * ���ӁF�\�[�X�t�@�C����S��Visit������ɁA���̃��\�b�h���ĂԂׂ�
	 * �������Ȃ��ƁA�A�N�Z�X���Ă��Ȃ��N���X�����݂��Ă��A�����ł��Ȃ�
	 */
	public ClassTypeInfo getClassTypeInfoByInnerName(String className){
		if(className == null){
			return null;
		}
		for(int i = 0; i < classList.size();i++){
			ClassTypeInfo classTypeInfo =(ClassTypeInfo) classList.get(i);
			if(className.equals(classTypeInfo.getFullInnerClassName2())){
				return classTypeInfo;
			}
		}

		return null;
	}

	/**
	 * �N���X�����A�\�[�X�t�@�C���Œ�`�����N���X���̎擾<br>
	 * @param className �N���X���A�N���X���ɂ͈ȉ��̉\��������܂��B<br>
	 *   MyInternalClass 1��Identifier�݂̂̃N���X��<br>
	 *   TestClass.MyInternalClass �C���i�[�N���X��<br>
	 *   abc.TestClass.MyInternalClass �p�b�P�[�W�����܂ރC���i�[�N���X��<br>
	 *   abc.TestClass$MyInternalClass �o�C�g�R�[�h����̃t�[���N���X��
	 * @param from�@�Y���N���X�̒�`�Ɋ܂܂��v�f�̊J�n�ʒu
	 * @param length�@�Y���N���X�̒�`�Ɋ܂܂��v�f�̒���
	 * @return
	 * ���ӁF�\�[�X�t�@�C����S��Visit������ɁA���̃��\�b�h���ĂԂׂ�
	 * �������Ȃ��ƁA�A�N�Z�X���Ă��Ȃ��N���X�����݂��Ă��A�����ł��Ȃ�
	 */
	public ClassTypeInfo getClassTypeInfoByName(String className,
			int from,int length){
		if(className == null){
			return null;
		}
		List<ClassTypeInfo> listClass = new ArrayList<ClassTypeInfo>();
		for(int i = 0; i < classList.size();i++){
			ClassTypeInfo classTypeInfo =(ClassTypeInfo) classList.get(i);
			String className1 = classTypeInfo.getClassName();
			String className2 = classTypeInfo.getFullInnerClassName();
			String className3 = HelperFunc.getFullClassName(packageName,
					className2);
			String className4 = classTypeInfo.getFullClassName();

			if(className.equals(className1)||className.equals(className2)||
					className.equals(className3)||className.equals(className4)){

				if(isInValidScope(classTypeInfo,from,length)){

					listClass.add(classTypeInfo);
				}
			}
		}

		if(listClass.size() <= 0){
			return null;
		}
		//1�݂̂̏ꍇ
		if(listClass.size() == 1){
			return (ClassTypeInfo)listClass.get(0);
		}
		//����������ꍇ�A�C���i�[�N���X���D��
		for(int i = 0; i < listClass.size();i++){
			ClassTypeInfo classTypeInfo =(ClassTypeInfo) listClass.get(i);
			if(classTypeInfo.getFather() != null){
				return classTypeInfo;
			}
		}

		return null;
	}

	/**
	 * �w�肵���v�f���N���X��Scope�ɂ��邩�ǂ���
	 * @param classTypeInfo
	 * @param from
	 * @param length
	 * @return
	 */
	private boolean isInValidScope(ClassTypeInfo classTypeInfo,
			int from,int length){
		ClassTypeInfo father = classTypeInfo.getFather();
		if(father == null){
			//top�N���X�̏ꍇ�A�S�\�[�X�ɗL��
			return true;
		}
		//�C���i�[�N���X�̏ꍇ�A�e�N���X�͈̔͂ɂ͗L��
		if(from >= father.getClassValidFrom() &&
				from + length <= father.getClassValidFrom() + father.getClassValidLength()){
			return true;
		}

		return false;
	}

	/**
	 * visit expression which delcares variable
	 */
	public boolean visit(VariableDeclarationExpression node){
		Type varType = node.getType();
		List fragList = node.fragments();
		accessFragments(varType,fragList);
		return super.visit(node);
	}

	/**
	 * visit declaration of variable
	 */
	public boolean visit(VariableDeclarationStatement node){
		Type varType = node.getType();
		List fragList = node.fragments();

		accessFragments(varType,fragList);
		return super.visit(node);
	}

	/**
	 * visit catch statement
	 */
	public boolean visit(CatchClause node){

		SingleVariableDeclaration sd = node.getException();

		int validToPos = node.getStartPosition() + node.getLength() - 1;

		accessSingleVarDeclar(sd,validToPos);
		return super.visit(node);
	}

	/**
	 * Single variable's declaring
	 * @param param
	 * @param validToPosition
	 */
	private void accessSingleVarDeclar(SingleVariableDeclaration param,
			int validToPosition){

		Type typeParam = param.getType();
		String typeString = typeParam.toString();

		MatchVariableInfo var = new MatchVariableInfo();
		curMethod.addVariable(var);

		//�ϐ���
		SimpleName sn = param.getName();
		var.setName(sn.getIdentifier());

		//�ϐ��^�C�v
		var.setType(typeString);
		MiddleType varMType = null;
		if(typeParam != null){
			varMType = new MiddleType(typeParam,param.getExtraDimensions());
		}
		var.setVarMType(varMType);

		//�ϐ��̒�`�ӏ�
		var.setStartPosition(sn.getStartPosition());
		var.setLength(sn.getLength());

		//�L���͈͂̊J�n�ʒu
		var.setValidFrom(param.getStartPosition());

		//�L���͈͂̏I���ʒu:���\�b�h�̏I���ʒu
		var.setValidTo(validToPosition);
	}

	/**
	 * �ϐ���`���X�g���A�N�Z�X����
	 * @param fragList
	 */
	private void accessFragments(Type varType,List<VariableDeclarationFragment> fragList){

		if(fragList == null){
			return;
		}

		if(curMethod == null){
			return;
		}


		String typeString = varType.toString();
		for(int i= 0;i < fragList.size();i++){
			VariableDeclarationFragment frag = fragList.get(i);
			accessFrag(typeString,frag,varType);
		}
	}

	private void accessFrag(String typeString,
			VariableDeclarationFragment frag,Type varType){

		MatchVariableInfo var = new MatchVariableInfo();
		curMethod.addVariable(var);

		//�ϐ���
		SimpleName sn = frag.getName();
		var.setName(sn.getIdentifier());

		//�ϐ��^�C�v
		var.setType(typeString);
		//�ϐ��^�C�v
		MiddleType varMType = null;
		if(varType != null){
			varMType = new MiddleType(varType,frag.getExtraDimensions());
		}
		var.setVarMType(varMType);

		//�ϐ��̒�`�ӏ�
		var.setStartPosition(sn.getStartPosition());
		var.setLength(sn.getLength());

		//�L���͈͂̊J�n�ʒu
		var.setValidFrom(frag.getStartPosition());

		//���ϐ����J�[�o�[����Block��T��
		ASTNode parent =  frag.getParent();
		while(parent != null && parent.getNodeType()!= ASTNode.BLOCK){
			parent = parent.getParent();
		}

		if(parent != null){
			var.setValidTo((parent.getStartPosition() + parent.getLength() - 1));
		}

	}


	/**
	 * visit the node of field declaration
	 */
	public boolean visit(FieldDeclaration node){
		Type fieldType = node.getType();
		String fieldTypeStr = fieldType.toString();

		//�t�B�[���h�^�C�v
		MiddleType fieldMType = null;
		if(fieldType != null){
			fieldMType = new MiddleType(fieldType);
		}

		ClassTypeInfo  currentClass = getDeclareClass(node);
		List<VariableDeclarationFragment> listFragments = node.fragments();
		if(listFragments != null && listFragments.size() > 0){
			for(int i= 0;i < listFragments.size();i++){
				VariableDeclarationFragment frag = listFragments.get(i);
				SimpleName filedSimpleName = frag.getName();
				String fieldName = filedSimpleName.getIdentifier();

				MatchField matchField = new MatchField();
				matchField.setFieldName(fieldName);
				matchField.setFieldType(fieldTypeStr);
				matchField.setFieldMType(fieldMType);
				ASTNode parent = node.getParent();
				matchField.setOffset(parent.getStartPosition());
				matchField.setLength(parent.getLength());
				matchField.setStartPosition(filedSimpleName.getStartPosition());
				matchField.setFieldLength(filedSimpleName.getLength());
				//�N���X��
				String curClassName = getDeclareClassName(
						node.getParent(),"$");
				matchField.setClassName(curClassName);

				matchField.setPackageName(packageName);
				fieldList.add(matchField);

				currentClass.addField(matchField);
			}
		}

		return super.visit(node);
	}

	/**
	 * create list of ClassInfo
	 * @version 3.0
	 * @date 2009/9/20
	 * @return
	 */
	public List<ClassTypeInfo> getClassList(){
		return classList;
	}

	public List<MatchField> getFieldList(){
		return this.fieldList;
	}

	/**
	 * get superclass
	 * @version 3.0
	 * @date 2009/10/28
	 * @param type full class name
	 * @return
	 */
	public String getSuperClassName(String type){
		for (ClassTypeInfo classTypeInfo: classList){
			if (type.equals(classTypeInfo.getFullClassName())){
				return classTypeInfo.getSuperClassName();
			}
		}
		return null;
	}

	/**
	 * visit static block declaration
	 * ��Q#502
	 */
	public boolean visit(Initializer node){

		curMethod = new MatchMethodInfo();
		methodList.add(curMethod);
		ClassTypeInfo  currentClass = getDeclareClass(node);
		currentClass.addMethod(curMethod);
		curMethod.setMyClassTypeInfo(currentClass);  //tu add for v3

		//�߂�l�^�C�v
		Type returnType= node.getAST().newPrimitiveType(PrimitiveType.VOID);
		if(returnType != null){
		    MiddleType mt = new MiddleType(returnType, 0);
		    curMethod.setReturnMType(mt);
		}
		int modifiers = node.getModifiers();
		if((modifiers & Modifier.NATIVE) == Modifier.NATIVE ){
			curMethod.setNative(true);
		}else if((modifiers & Modifier.ABSTRACT) == Modifier.ABSTRACT ){
			curMethod.setAbstract(true);
		}else if((modifiers & Modifier.STATIC) == Modifier.STATIC ){
			curMethod.setStatic(true);
		}


		//�J�n�ʒu
		curMethod.setOffset(node.getStartPosition());

		//���\�b�h��
		curMethod.setLength(node.getLength());

		//���\�b�h��
		curMethod.setMethodName("<clinit>");
        //curMethod.setMethodNameStartPos(sname.getStartPosition());
        //curMethod.setMethodNameOffet(sname.getLength());

		//�N���X��
		String curClassName = getDeclareClassName(node.getParent(),"$");
		curMethod.setClassName(curClassName);

		curMethod.setPackageName(packageName);

		return super.visit(node);

	}
}
