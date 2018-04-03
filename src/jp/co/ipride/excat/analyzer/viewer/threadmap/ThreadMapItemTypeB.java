package jp.co.ipride.excat.analyzer.viewer.threadmap;

import java.util.ArrayList;
import java.util.List;

/**
 * �X���b�h�_�E���Q�g�ȏ�ɓK�p����B
 * @author tu-ipride
 * @version 3.0
 * @date 2009/10/6
 */
public class ThreadMapItemTypeB {

	public static int listSize = 10;

	// �X���b�h��
	private String threadName;

	//�t�@�C���̃p�X
	private List<String> filePathList = null;

	// ���
	private List<String> statusList = null;

	public ThreadMapItemTypeB(){
		filePathList = new ArrayList<String>();
		statusList = new ArrayList<String>();
		for (int i=0; i<listSize; i++){
			filePathList.add("");
			statusList.add("");
		}
	}

	public String getFilePath(int index) {
		return filePathList.get(index);
	}
	public void setFilePathList(int index, String filePath) {
		this.filePathList.set(index,filePath);
	}
	public String getThreadName() {
		return threadName;
	}
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	public String getStatus(int index) {
		return statusList.get(index);
	}
	public void setStatus(int index, String status) {
		this.statusList.set(index,status);
	}

}
