package jp.co.ipride.excat.common.clipboard;

import jp.co.ipride.excat.analyzer.viewer.sourceviewer.MethodInfo;

/**
 * �ꎞ�i�[����I�u�W�F�N�g
 * @author tu-ipride
 * @version 3.0
 * @date 2009/10/26
 */
public class ExcatTemplate {

	private static MethodInfo methodInfo = null;

	/**
	 * �擾���ĊY���I�u�W�F�N�g���폜����B
	 * @return
	 */
	public static MethodInfo getMethodInfo() {
		MethodInfo m = methodInfo;
		methodInfo = null;
		return m;
	}

	/**
	 * �I�u�W�F�N�g���Z�b�g����B
	 * @param methodInfo
	 */
	public static void setMethodInfo(MethodInfo methodInfo) {
		ExcatTemplate.methodInfo = methodInfo;
	}



}
