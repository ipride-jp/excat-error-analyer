package jp.co.ipride.excat.analyzer.viewer.threadmap;

/**
 * 1�g�̃_���v�f�[�^��\������B
 * @author tu-ipride
 * @version 3.0
 * @date 2009/10/5
 */
public class ThreadMapItemTypeA {

	private String filePath;

	// �X���b�h��
	private String threadName;
	// ����
	private String dumpTime;
	// ���
	private String status;
	// �D��x
	private String priority;
	// �ҋ@����
	private String cPUTime;
	//�@�ҋ@���R
	private String waitReason;
	// ���j�^�[
	private String monitorObject;
	//���j�^�[���g�p���Ă���X���b�h��
	private String waitThread;

	public String getWaitThread() {
		return waitThread;
	}
	public void setWaitThread(String waitThread) {
		this.waitThread = waitThread;
	}
	public String getThreadName() {
		return threadName;
	}
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	public String getDumpTime() {
		return dumpTime;
	}
	public void setDumpTime(String dumpTime) {
		this.dumpTime = dumpTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCPUTime() {
		return cPUTime;
	}
	public void setCPUTime(String time) {
		this.cPUTime = time;
	}
	public String getWaitReason() {
		return waitReason;
	}
	public void setWaitReason(String waitReason) {
		this.waitReason = waitReason;
	}
	public String getMonitorObject() {
		return monitorObject;
	}
	public void setMonitorObject(String monitorObject) {
		this.monitorObject = monitorObject;
	}


	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}


}
