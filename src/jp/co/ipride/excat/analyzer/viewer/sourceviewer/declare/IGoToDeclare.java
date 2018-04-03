package jp.co.ipride.excat.analyzer.viewer.sourceviewer.declare;

import jp.co.ipride.excat.analyzer.viewer.sourceviewer.JavaSourceViewer;

/**
 * �I�����ꂽ�v�f�̐錾���J��
 * @author jiang
 *
 */
public interface IGoToDeclare {

	/**
	 * �v�f��錾����\�[�X�^�o�C�g�R�[�h���J��
	 * @throws SourceNotFoundException
	 */
	public void gotoDeclarePlace() throws SourceNotFoundException,NoClassInPathException;
	
	/**
	 * Source viewer�̐ݒ�
	 * @param sourceViewer
	 */
	public void setJavaSourceViewer(JavaSourceViewer javaSourceViewer);
}
