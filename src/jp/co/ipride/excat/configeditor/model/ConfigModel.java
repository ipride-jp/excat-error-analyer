package jp.co.ipride.excat.configeditor.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.Writer;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jp.co.ipride.excat.common.Message;
import jp.co.ipride.excat.common.utility.ExcatMessageUtilty;
import jp.co.ipride.excat.configeditor.model.baseinfo.BaseInfo;
import jp.co.ipride.excat.configeditor.model.instance.ObjectLine;
import jp.co.ipride.excat.configeditor.model.instance.ObjectLineList;
import jp.co.ipride.excat.configeditor.model.task.ExceptionRegisterTask;
import jp.co.ipride.excat.configeditor.model.task.ITask;
import jp.co.ipride.excat.configeditor.model.task.MonitorMethodTask;
import jp.co.ipride.excat.configeditor.model.task.MonitoringException;
import jp.co.ipride.excat.configeditor.model.task.MonitoringMethod;
import jp.co.ipride.excat.configeditor.model.task.TaskList;
import jp.co.ipride.excat.configeditor.model.template.Template;
import jp.co.ipride.excat.configeditor.model.template.TemplateList;
import org.apache.log4j.Logger;
import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * �R���t�B�O�E�t�@�C���̃f�[�^�E���f��
 * @author tu
 * @since 2007/11/18
 */
public class ConfigModel {

	private static String xmi = "http://www.w3.org/2001/XMLSchema-instance";

	private static String schema = "Configuration.xsd";

	private static String encoding = "UTF-8";

	private static TaskList taskList = new TaskList();

	private static BaseInfo baseInfo = new BaseInfo();

	private static ObjectLineList objectLineList = new ObjectLineList();

	private static TemplateList templateList = new TemplateList();

	private static Document document = null;

	private static String filePath = null;

	private static boolean change = false;

	private static boolean fileOpening = false;

	/**
	 * ������
	 */
	public static void removeAll(){
		taskList.removeAll();
		baseInfo = new BaseInfo();
		objectLineList = new ObjectLineList();
		templateList = new TemplateList();
		document = null;
		filePath = null;
		change = false;
	}

	/**
	 * �������ς݂̐V�K�R���t�B�O���쐬
	 *
	 */
	public static void createNewConfig(){
		taskList.init();
		//update by v3
		//taskList.addNewTask();
		baseInfo.init();
		objectLineList.init();
		templateList.init();
		document = null;
		filePath = null;
		change = false;
	}

	/**
	 * �����R���t�B�O��ǂݍ��݁A�f�[�^�I�u�W�F�N�g���쐬
	 * @param config_path
	 */
	public static boolean openOldConfig(String config_path){
		filePath = config_path;
		byte[] total;
		InputStream stream = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			stream = new FileInputStream(config_path);
			total = new byte[stream.available()];
			stream.read(total);
			String result = new String(total, "UTF-8");
			document = builder.parse(new InputSource(new StringReader(result)));
			inputConfig();
			change = false;
			return true;
		}catch (Exception e){
			Logger.getLogger("ConfigLogger").error("openOldConfig",e);
			ExcatMessageUtilty.showMessage(
					null,
					Message.get("Dialog.Error.OpenXML.Text"));
			return false;
		} finally {
			if (stream != null){
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * �w��t�@�C���ŕۑ�����B
	 * @param new_path
	 */
	public static boolean saveAsNewConfig(String new_path){
		filePath = new_path;
		outputDocument();
		if (saveToFile()) {
			change = false;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * �����R���t�B�O�E�t�@�C�����X�V
	 *
	 */
	public static boolean update(){
		outputDocument();
		if (saveToFile()) {
			change = false;
			return true;
		} else {
			return false;
		}

	}

	public static boolean checkConfigModel(){
		return taskList.checkTasks();
	}

	public static BaseInfo getBaseInfo() {
		return baseInfo;
	}

	public static ObjectLineList getObjectLineList() {
		return objectLineList;
	}

	public static TaskList getTaskList() {
		return taskList;
	}

	public static TemplateList getTemplateList() {
		return templateList;
	}

	public static Document getDocument(){
		return document;
	}

	public static ITask createNewTask(int taskType){
		return taskList.createNewTask(taskType);
	}

	/**
	 * [Config]�^�O��Ǎ��݁A�c�n�l���쐬����B
	 */
	private static void inputConfig(){
		NodeList nodeList = document.getChildNodes().item(0).getChildNodes();
		templateList.init();
		baseInfo.init();
		taskList.init();
		objectLineList.init();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			String name = node.getNodeName();
			if (ConfigContant.Tag_OTHERS.equals(name)){
				baseInfo.inputDocument(node);
			}else if (ConfigContant.Tag_OBJECT_ELEMENT.equals(name)){
				templateList.inputDocument(node);
			}else if (ConfigContant.Tag_TASK.equals(name)){
				taskList.inputDocument(node);
			}else if (ConfigContant.Tag_DUMP_INSTANCE.equals(name)){
				objectLineList.inputDocument(node);
			}
		}
	}


	/**
	 * �������ꂽDOM���t�@�C���ɕۑ�����B.
	 * @param filePath
	 */
	private static boolean saveToFile(){
		Writer writer = null;
		FileOutputStream fileOutStream = null;
		try{
			fileOutStream = new FileOutputStream(filePath);
			XmlWriter xmlWriter = new XmlWriter();
			xmlWriter.setOutput(fileOutStream,encoding);
			xmlWriter.write(document);
			return true;
		}catch (Exception e){
			Logger.getLogger("ConfigLogger").error("saveToFile",e);
			ExcatMessageUtilty.showMessage(
					null,
					Message.get("Dialog.Error.Text"));
			return false;
		}finally{
			if (fileOutStream != null){
				try {
					fileOutStream.close();
				} catch (IOException e) {
				}
			}
			if (writer != null){
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * �I�u�W�F�N�g����c�n�l���쐬����B
	 *
	 */
	private static void outputDocument(){
		document = new DocumentImpl();
		Node root = document.createElement(ConfigContant.Tag_CONFIG);
		((Element)root).setAttribute(ConfigContant.Field_XSI, xmi);
		((Element)root).setAttribute(ConfigContant.Field_SCHEMA, schema);
		taskList.outputDocument(root);
		objectLineList.outputDocument(root);
		templateList.outputDocument(root);
		baseInfo.outputDocument(root);
		document.appendChild(root);
	}

	/** ------------ �ȉ��͊֘A�`�F�b�N�֐�-------------------**/


	/**
	 * �V���O�i���Ď��^�X�N�͈�݂̂ł��邱�ƁB
	 * �`�F�b�N�̃^�C�~���O�F�u�V���O�i���Ď��v�ɑI��������
	 * @return
	 */
	public static boolean hasOtherMonitoringSignalTask(ITask myTask){
		for(int i=0; i<taskList.getTasks().size(); i++){
			ITask task = taskList.getTasks().get(i);
			if (task.getTaskType() == ITask.MONITOR_SIGNAL){
				if (!task.equals(myTask)){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 1�D�����̓o�^���ł́A�N���X�E���[�h���w�肵�Ă��Ȃ��ꍇ�A
	 *    �����N���X�̐V�K�o�^�ł́A�N���X���[�h���w�肷�邱�Ƃ͂ł��Ȃ��B
	 * 2�D�����̓o�^���ł́A�N���X�E���[�h���w�肵�Ă���ꍇ�A
	 * �@ �����N���X�̐V�K�o�^�ł́A�N���X���[�h���w�肷�邱�ƁB
	 * 3. �N���X���ƃ��[�h�N���X���������ł���ꍇ�A�g���q���𓝈ꂷ�邱�ƁB
	 * 3.�`�F�b�N�^�C�~���O�F
	 * �@�@�E�Y����ʂ̕ۑ��{�^���B
	 *
	 * @param myMethod�@�ҏW���̏��
	 * @param orgMethod �ҏW���̏��
	 * @return
	 */
//	public static int checkClassDefForMonitiringMethod(MonitoringMethod myMethod, MonitoringMethod orgMethod){
//		for (int i=0; i<taskList.getTasks().size(); i++){
//			ITask task = taskList.getTasks().get(i);
//			if (task instanceof MonitorMethodTask){
//				MonitorMethodTask monitorMethodTask = (MonitorMethodTask)task;
//				for (int j=0; j<monitorMethodTask.getMonitoringMethodList().size(); j++){
//					MonitoringMethod method = (MonitoringMethod)monitorMethodTask.getMonitoringMethodList().get(j);
//					//������̂Ȃ�
//					if (method.equals(orgMethod)){
//					  continue;
//					}
//					if (method.getClassName().equals(myMethod.getClassName())){
//						//������`�̃N���X���[�h���Ȃ��ꍇ
//						if ("".equals(method.getClassLoaderURL())){
//							if (!"".equals(myMethod.getClassLoaderURL())){
//								return 1;
//							}
//						}else{
//							//������`�̃N���X���[�h������ꍇ
//							//�������Ȃ��ꍇ�B
//							if ("".equals(myMethod.getClassLoaderURL())){
//								return 2;
//							}
//							//�N���X�E���[�h������v���Ă���
//							if (DocumetUtil.checkClassLoaderURL(method.getClassLoaderURL(),myMethod.getClassLoaderURL())){
//								//�g���q����v���Ă��Ȃ�
//								if (!method.getClassLoader_suffix().equals(myMethod.getClassLoader_suffix())){
//									return 3;
//								}
//							}
//						}
//						return 0;
//					}
//				}
//			}
//		}
//		return 0;
//	}

	/**
	 * �P�D�N���X�A�N���X�E���[�h�A���\�b�h��������`�����ɂ���ꍇ
	 * �@�@�P�D�P�D�������\�b�h�̃V�O�l�`����������΁A����`�ł͖����B
	 * �@�@�P�D�Q�D�������\�b�h�̃V�O�l�`��������΁A����`�̃V�O�l�`���͊�����`�ƈقȂ�ׂ�
	 * �R�D�`�F�b�N�̃^�C�~���O�F�Y����ʁu�o�^�v�{�^����������
	 *
	 * @param myMethod�@�ҏW���̏��
	 * @param orgMethod �ҏW���̏��
	 * @return
	 */
	public static int checkMethodDefForMonitiringMethod(MonitoringMethod myMethod, MonitoringMethod orgMethod){
		for (int i=0; i<taskList.getTasks().size(); i++){
			ITask task = taskList.getTasks().get(i);
			if (task instanceof MonitorMethodTask){
				MonitorMethodTask monitorMethodTask = (MonitorMethodTask)task;
				for (int j=0; j<monitorMethodTask.getMonitoringMethodList().size(); j++){
					MonitoringMethod method = (MonitoringMethod)monitorMethodTask.getMonitoringMethodList().get(j);
					//������̂Ȃ�
					if (method.equals(orgMethod)){
					  continue;
					}
					//�N���X�A�N���X�E���[�h�A���\�b�h��������`�����ɂ���ꍇ
					if (method.getClassName().equals(myMethod.getClassName()) &&
							method.getClassLoaderURL().equals(myMethod.getClassLoaderURL()) &&
							method.getMethodName().equals(myMethod.getMethodName())){
						//�������\�b�h�̃V�O�l�`����������΁A����`�͖����ł��邱�ƁB
						if ("".equals(method.getMethodSignature())){
							return 1;
						}else{
						//�������\�b�h�̃V�O�l�`��������΁A����`�̃V�O�l�`���͊�����`�ƈقȂ�ׂ�
							if (method.getMethodSignature().equals(myMethod.getMethodSignature())){
								return 2;
							}
						}
					}
				}
			}
		}
		return 0;
	}

	/**
	 * �Q�D���\�b�h���Ď�����N���X�Ƃ̏d��������ꍇ�A
	 * �@�@�@�D���\�b�h���Ď�����N���X�̃N���X�E���[�h�������ꍇ�A
	 * �@�@�@�@�u����v�N���X�̃��[�h������΁A�m�f
	 * �@�@�A�D���\�b�h���Ď�����N���X�̃N���X�E���[�h������ꍇ�A
	 * �@�@�@�@�u����v�N���X�̃��[�h��������΁A�m�f
	 *
	 * �R�D�`�F�b�N�̃^�C�~���O�F�ۑ���
	 */
	public static int checkClassOfInstanceDef(ObjectLine objectLine){
		for (int i=0; i<taskList.getTasks().size(); i++){
			ITask task = taskList.getTasks().get(i);
			if (task instanceof MonitorMethodTask){
				MonitorMethodTask monitorMethodTask = (MonitorMethodTask)task;
				for (int j=0; j<monitorMethodTask.getMonitoringMethodList().size(); j++){
					MonitoringMethod method = (MonitoringMethod)monitorMethodTask.getMonitoringMethodList().get(j);
					if (method.getClassName().equals(objectLine.getClassName())){
						if ("".equals(method.getClassLoaderURL()) &&
							!"".equals(objectLine.getClassLoadName())){
							return 1;
						}
						if (!"".equals(method.getClassLoaderURL()) &&
							"".equals(objectLine.getClassLoadName())){
							return 2;
						}
					}
				}
			}
		}
		return 0;
	}

	/**
	 * �Ď��N���X�����ɂ���ꍇ�Atrue
	 * �Ȃ��ꍇ�Afalse
	 * @param monitoringException  �ҏW���̏��
	 * @param orgMonitoringException�@���̏��
	 */
	public static boolean hasSameMonitoringClassName(MonitoringException monitoringException,
													  MonitoringException orgMonitoringException){
		for (int i=0; i<taskList.getTasks().size(); i++){
			ITask task = taskList.getTasks().get(i);
			if (task instanceof ExceptionRegisterTask){
				ExceptionRegisterTask emTask = (ExceptionRegisterTask)task;
				for (int j=0; j<emTask.getMonitoringExceptionList().size(); j++){
					MonitoringException monitor = (MonitoringException)emTask.getMonitoringExceptionList().get(j);
					if (monitor.equals(orgMonitoringException)){
						continue;
					}
					String className = monitor.getTargetClassName();
					if (className.equals(monitoringException.getTargetClassName())){
						return true;
					}
				}
			}

		}
		return false;
	}

	/**
	 * �����N���X�������e���v���[�g���L�����邩���`�F�b�N����B
	 * @param template�@�ҏW���̃e���v���[�g
	 * @param orgTemplate�@�Y���ҏW�̌��e���v���[�g
	 * @return
	 */
	public static boolean hasSameClassForTemplateDef(Template template, Template orgTemplate){
		Vector<Template> templates = templateList.getTemplates();
		for (int i=0; i<templates.size(); i++){
			Template item = (Template)templates.get(i);
			if (item.equals(orgTemplate)){
				continue;
			}
			if (item.getClassName().equals(template.getClassName())){
				return false;
			}
		}
		return true;
	}

	/**
	 * �`�F�b�N�F�����A��{���Ƀ��[����񂪓o�^����Ă��Ȃ��ꍇ�A�^�X�N�̃��[�����M�ƓY�t��I�����Ă͂����Ȃ��B
	 * �`�F�b�N�̃^�C�~���O�F�u���[�����M�v��I��������
	 *
	 */
	public static boolean checkMail(){
		if (ConfigModel.baseInfo.isHasMailInfo()){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * �V�K�ҏW�����`�F�b�N����B
	 * @return
	 */
	public static boolean isNewConfig(){
		if (filePath == null){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * �X�e�[�^�X�\���ȂǂɎg�p����B
	 * @return
	 */
	public static String getFilePath(){
		return filePath;
	}

	public static boolean isChanged(){
		return change;
	}

	public static void setChanged(){
		if (!fileOpening) {
			change = true;
		}
	}

	public static void noChanged(){
		change = false;
	}

	public static boolean hasDocument(){
		if (document == null){
			return false;
		}else{
			return true;
		}
	}

	public static void setFileOpening(boolean fileOpening) {
		ConfigModel.fileOpening = fileOpening;
	}
}