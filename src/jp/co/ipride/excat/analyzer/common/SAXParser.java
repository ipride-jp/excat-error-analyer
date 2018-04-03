package jp.co.ipride.excat.analyzer.common;

import java.io.StringReader;

import jp.co.ipride.excat.analyzer.viewer.threadmap.ThreadMapItemTypeA;
import jp.co.ipride.excat.common.utility.HelperFunc;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * this is sax parser for getting information of dump files.
 * @author tu-ipride
 * @version 3.0
 * @since 2009/10/4
 */
public class SAXParser {

	public static ThreadMapItemTypeA getThreadMapItemTypeA(String path){
		ThreadMapHandler handler = new ThreadMapHandler();
		parse(path, handler);
		ThreadMapItemTypeA item = handler.getThreadMapItem();
		item.setFilePath(path);
		return item;
	}

	/**
	 *
	 * @param file
	 */
	public static void parse(String path, DefaultHandler handler){
        try {
    		String result = DumpFile.getDumpFileContent(path);
            // SAX�p�[�T�̐���
            XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
            // �t�B�[�`���[�̐ݒ�
            parser.setFeature("http://xml.org/sax/features/validation",false);
            parser.setFeature("http://apache.org/xml/features/validation/schema",false);
            parser.setFeature("http://xml.org/sax/features/namespaces",false);
            // �n���h���̓o�^
            parser.setContentHandler(handler);
            parser.setDTDHandler(handler);
            parser.setErrorHandler(handler);
            // ���
            parser.parse(new InputSource(new StringReader(result)));
        } catch (Exception e) {
			HelperFunc.getLogger().error("SAXParser", e);
        }
	}
}
