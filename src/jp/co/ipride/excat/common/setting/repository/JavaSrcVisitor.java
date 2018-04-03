package jp.co.ipride.excat.common.setting.repository;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import jp.co.ipride.excat.analyzer.viewer.sourceviewer.BaseVisitor;
import jp.co.ipride.excat.analyzer.viewer.sourceviewer.ClassTypeInfo;
import jp.co.ipride.excat.analyzer.viewer.sourceviewer.ImportClassInfo;
import jp.co.ipride.excat.analyzer.viewer.sourceviewer.MatchField;
import jp.co.ipride.excat.analyzer.viewer.sourceviewer.MatchMethodInfo;
import jp.co.ipride.excat.analyzer.viewer.sourceviewer.MatchVariableInfo;
import jp.co.ipride.excat.analyzer.viewer.sourceviewer.MiddleType;
import jp.co.ipride.excat.common.utility.HelperFunc;

/**
 * this visitor is for building a Repository.
 * @author tu-ipride
 * @version 3.0
 */
public class JavaSrcVisitor extends BaseVisitor{

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
	 * create list of ClassInfo
	 * @return
	 */
	public List<ClassTypeInfo> getClassList(){
		return classList;
	}

	public void resolveType(){
		for(int i = 0; i < classList.size();i++){
			ClassTypeInfo classTypeInfo =(ClassTypeInfo) classList.get(i);
			classTypeInfo.setSourceFileName(sourceFileName);

			//super class����͂���
			MiddleType mt = classTypeInfo.getSuperClassMType();
			String superClassName = null;
			if(mt != null){
				superClassName = analyzeType(mt, null);
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
					List<String> possibleInterfaceTypeList = new ArrayList<String>();
					String itype = analyzeType(imt, possibleInterfaceTypeList);
					if (itype == null) {
						for (int index = 0; index < possibleInterfaceTypeList.size(); index++) {
							classTypeInfo.addPossibleInterface(imt.getTypeString(), possibleInterfaceTypeList.get(index));
						}
					} else {
						interfaceTypeList.add(itype);
					}
				}
				//interfaceMTypeList���J��
				interfaceMTypeList.clear();
				classTypeInfo.setInterfaceMTypeList(null);
			}
			classTypeInfo.setInterfaceTypeList(interfaceTypeList);

			//�t�B�[���h�^�C�v����͂���
			List<MatchField> fList = classTypeInfo.getFieldList();
			for(int j = 0; j < fList.size();j++){
				MatchField mf = fList.get(j);
				MiddleType fmtype = mf.getFieldMType();
				String fullFieldTypeStr = analyzeType(fmtype, null);
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
					returnTypeStr = analyzeType(rmt, null);
				}

				mmi.setReturnType(returnTypeStr);

				//�ϐ�����͂���
				List<MatchVariableInfo> vList = mmi.getVariableList();
				List<String> paramTypeList = new ArrayList<String>();
				int paramNumber = mmi.getParamNumber();
				for(int k = 0; k < vList.size(); k++){
					MatchVariableInfo var = vList.get(k);
					MiddleType varMType = var.getVarMType();
					String fullVarType = analyzeType(varMType, null);
					var.setFullType(fullVarType);
					if(k < paramNumber){
						paramTypeList.add(fullVarType);
					}
				}

				//�����̃��X�g
				mmi.setParamTypeList(paramTypeList);
				//signature�̐ݒ�
				mmi.generateSignature();
				//construct name
				String className = HelperFunc.getPureClassName(classTypeInfo.getFullClassName());
				if (className.equals(mmi.getMethodName())){
					mmi.setMethodName("<init>");
				}
			}
		}

	}

	public void clear(){
		methodList.clear();
		fieldList.clear();
		importList.clear();
		classList.clear();
	}

	/******************************* visitor **********************************/

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
	 * visit class declaration
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
				SingleVariableDeclaration param = listParams.get(i);
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
		//add by Qiu Song on 20090224 for 
		if(currentClass == null){
			return false;
		}
		//end of add by Qiu Song on 20090224 for 
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

	/***************************** private function **********************************/
	/**
	 * node���܂ނ���N���X���擾
	 * @param node �m�[�g
	 * @return�@�m�[�h���܂ރN���X
	 */
	private ClassTypeInfo getDeclareClass(ASTNode node){

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
	private ClassTypeInfo getClassTypeInfoByName(String className,
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
	 * �^�C�v������͂���
	 * @param type
	 * @return
	 */
	protected String analyzeType(MiddleType type, List<String> possibleTypeList) {
		if(type == null){
			return null;
		}

		if(type.isArrayType()){
			Type elementType = type.getElementType();
			MiddleType eMT = new MiddleType(elementType);
			String elementTypeStr = analyzeType(eMT, possibleTypeList);
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
		return analyzeClassType(type, possibleTypeList);
	}

	/**
	 * �N���X�^�C�v����͂���
	 * @param classType
	 * @return �X�[�p�[�N���X���i�p�b�P�[�W�����܂ށj
	 */
	protected String analyzeClassType(MiddleType classType, List<String> possibleTypeList){

		String typeName = classType.getTypeString();

		// ��Q#501 ���̌^�Ή�
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
//			JavaClass clazz = SettingManager.getSetting(
//			    ).getByteCodeContents(typeName);
//			if(clazz != null){
//				return 	typeName;
//			}
			Object ci = RepositoryFactory.getDefinedClassMap().get(typeName);
			if (ci != null){
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
		fullClassName= HelperFunc.getFullClassName(packageName,	typeName);
		if (possibleTypeList != null) {
			possibleTypeList.add(fullClassName);
		}
		Object ci = RepositoryFactory.getDefinedClassMap().get(fullClassName);
		if (ci != null){
			return 	fullClassName;
		}
//		String fileContents = SettingManager.getSetting()
//		    .getJavaSourceContent(fullClassName);
//		if(fileContents != null){
//			return fullClassName;
//		}

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
//					JavaClass clazz = SettingManager.getSetting(
//					).getByteCodeContents(fullClassName);
//					if(clazz != null){
//						return fullClassName;
//					}
					if (possibleTypeList != null) {
						possibleTypeList.add(fullClassName);
					}
					Object o = RepositoryFactory.getDefinedClassMap().get(fullClassName);
					if (o != null){
						return 	fullClassName;
					}
				}

			}
		}
		return null;
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

}
