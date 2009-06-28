package com.spbsu.xml.tests;

import com.spbsu.xml.XmlFactory;
import com.spbsu.xml.XmlFile;
import junit.framework.TestCase;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringBufferInputStream;

/**
 * @author vp
 */
public class XmlEntityDecodingTest extends TestCase {
  private static final String XML =
    "<tag>" +
      "&lt;inner&gt;" +
        "&lt;inside-inner/&gt;" +
      "&lt;/inner&gt;" +
    "</tag>";

  public void testDecodeEntity() throws Exception {
    final XmlFile xmlFile = XmlFactory.parseTextFile(XML);
    final String  fastXmlResult = xmlFile.getRootTag().getChildText().toString();

    final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new StringBufferInputStream(XML));
    final String domResult = doc.getDocumentElement().getTextContent();

    assertEquals(domResult, fastXmlResult);
  }
}
