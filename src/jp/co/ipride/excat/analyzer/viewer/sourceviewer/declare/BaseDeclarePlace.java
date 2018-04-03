package jp.co.ipride.excat.analyzer.viewer.sourceviewer.declare;

import jp.co.ipride.excat.analyzer.viewer.sourceviewer.JavaSourceViewer;
import jp.co.ipride.excat.analyzer.viewer.sourceviewer.JavaSourceVisitor;
import jp.co.ipride.excat.analyzer.viewer.sourceviewer.MatchField;
import jp.co.ipride.excat.analyzer.viewer.sourceviewer.MethodInfo;
import jp.co.ipride.excat.analyzer.viewer.sourceviewer.SourceViewerPlatform;
import jp.co.ipride.excat.common.setting.repository.Repository;

/**
 * �錾���J�����邽�߂̃x�[�X�N���X
 * @author iPride_Demo
 *
 */
public class BaseDeclarePlace {

	/**
	 * visitor of java source file
	 */
	protected JavaSourceVisitor javaSourceVisitor = null;

	/**
	 * ���̃\�[�X�t�@�C����\������r���[
	 */
	protected JavaSourceViewer javaSourceViewer = null;

	/**
	 * type name
	 */
	protected String typeName = null;

	/**
	 * �I�����ꂽ�v�f�̊J�n�ʒu
	 */
	protected int startPosition = 0;

	/**
	 * �I�����ꂽ�v�f�̒���
	 */
	protected int length = 0;

	/**
	 * �p�b�P�[�W�����擾
	 * @param fullClassName
	 * @return
	 */
	protected String getPackageName(String fullClassName){

		if(fullClassName == null){
			return null;
		}
		int pos = fullClassName.lastIndexOf('.');
		if(pos < 0){
			return null;
		}

		return fullClassName.substring(0,pos);
	}

	/**
	 * �N���X�����擾
	 * @param fullClassName
	 * @return
	 */
	protected String getClassNameWithoudPackage(String fullClassName){

		if(fullClassName == null){
			return null;
		}
		int pos = fullClassName.lastIndexOf('.');
		if(pos < 0){
			return fullClassName;
		}

		return fullClassName.substring(pos + 1);
	}


	/**
	 * �\�[�X�t�@�C���O�ɂ���N���X�Ⴕ���̓��\�b�h�̐錾���J��
	 * @param fullClassName
	 * @throws SourceNotFoundException
	 */
	protected void gotoClassDeclare(String fullClassName) {
		MethodInfo methodInfo = new MethodInfo();
		methodInfo.setClassSig(fullClassName);
		SourceViewerPlatform.getInstance().showDelcareViewer(methodInfo);
	}

	/**
	 * �t�B�[���h��錾����ӏ���\������B
	 * �����A�Y���׽�ɂȂ���΁A�e�N���X��T���B
	 * @param matchField
	 * @version 3.0
	 * @date 2009/10/28
	 * @author tu-ipride
	 */
	public void gotoFieldDeclare(MatchField matchField){
		SourceViewerPlatform.getInstance().showDelcareViewer(matchField);
	}

	/**
	 * �\�[�X�t�@�C�����ɂ���錾���J���B
	 * @param classTypeInfo
	 */
	protected void gotoDeclareInside(int start,int length){

		int end = start + length;
		javaSourceViewer.setSelectionWithTrace(start, end);

	}

	/**
	 * Source viewer�̐ݒ�
	 * @param sourceViewer
	 */
	public void setJavaSourceViewer(JavaSourceViewer javaSourceViewer){
		this.javaSourceViewer = javaSourceViewer;
		this.javaSourceVisitor = javaSourceViewer.getSourceVisitor();
	}

	public String getTypeName() {
		return typeName;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public int getLength() {
		return length;
	}
}
