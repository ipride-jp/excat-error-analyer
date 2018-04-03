package jp.co.ipride.excat.analyzer.viewer.sourceviewer;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Visitor�̋��ʋ@�\���܂Ƃ߂�N���X
 * @author iPride_Demo
 *
 */
public class BaseVisitor extends ASTVisitor{


	/**
	 * �p�b�P�[�W��
	 */
	protected String packageName = null;
	
	
	/**
	 * ���\�b�h��錾����N���X�����擾
	 * @param classType �N���X��錾����m�[�g
	 * @param seperator �C���i�[�N���X�ƃA�E�g�N���X���q������
	 * @return�@�A�E�g�N���X�����܂ރN���X���i�p�b�P�[�W�����܂܂Ȃ��j
	 */
	protected String getDeclareClassName(ASTNode classType,String seperator){
		
		String classNameToRoot = null;
		while(classType != null){
			if(classType instanceof TypeDeclaration){
				TypeDeclaration classDeclaration = (TypeDeclaration)classType;
				//�N���X��
				String curClassName = null;
				SimpleName sn = classDeclaration.getName();
				curClassName = sn.getIdentifier();	
				if(classNameToRoot == null){
					classNameToRoot = curClassName;
				}else{
					classNameToRoot = curClassName + seperator + classNameToRoot;
				}
				
				classType = classType.getParent();
			}else{
				break;
			}
		}
		
			
		return classNameToRoot;
	}
	
	/**
	 * visit package declaration
	 */
	public boolean visit(PackageDeclaration node){
		Name name = node.getName();
		packageName = name.getFullyQualifiedName();
		return super.visit(node);
	}
	
	public String getPackageName() {
		return packageName;
	}
	
}
