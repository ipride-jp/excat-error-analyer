package jp.co.ipride.excat.analyzer.viewer.propertyviewer;

/**
 * DomBuilder���쐬����t�@�N�g��
 *
 * @author sai
 * @since 2009/10/15
 */
public class DomBuilderFactory {

	private static DomBuilderFactory instance;

	protected DomBuilderFactory() {
	}

	/**
	 * �t�@�N�g���̃C���X�^���X���擾���郁�\�b�h
	 *
	 * @return �t�@�N�g���̃C���X�^���X
	 */
	public static synchronized DomBuilderFactory getInstance() {
		if (instance == null) {
			instance = new DomBuilderFactory();
		}
		return instance;
	}

	/**
	 * DomBuilder���쐬���郁�\�b�h
	 *
	 * @return DomBuilder
	 */
	public DomBuilder newDomBuilder() {
		return new DomBuilder();
	}
}
