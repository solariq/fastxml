package com.spbsu.xml.tests;

import com.spbsu.xml.*;
import com.spbsu.xml.impl.lexer.XmlFlexLexer;
import com.spbsu.xml.impl.lexer.XmlLexer;
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

  private static final String XML_W_NUMERIC_ENTITY =
    "<tag>" +
      "&#2012;&#2012;" +
    "</tag>";

  public static final int[] tokenTypes = {0, 19, 3, 11, 11, 2, 19, 3};

  public void testDecodeEntity() throws Exception {
    final XmlFile xmlFile = XmlFactory.parseTextFile(XML);
    final String  fastXmlResult = xmlFile.getRootTag().getChildText().toString();

    final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new StringBufferInputStream(XML));
    final String domResult = doc.getDocumentElement().getTextContent();

    assertEquals(domResult, fastXmlResult);
  }

  public void testNumericEntity() throws Exception {
    final XmlTag rootTag = XmlFactory.parseTextFile(XML_W_NUMERIC_ENTITY).getRootTag();
    final XmlTagChild firstChild = rootTag.getFirstChild();
    assertTrue(firstChild instanceof XmlText);
    final CharSequence value = ((XmlText) firstChild).getValue();
    assertEquals("ߜߜ", value.toString());
    assertEquals("ߜߜ", rootTag.getChildText().toString());

    final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new StringBufferInputStream(XML_W_NUMERIC_ENTITY));
    final String domResult = doc.getDocumentElement().getTextContent();
    assertEquals(domResult, value.toString());
  }

  public void testNumericEntityLexer() throws Exception {
    final XmlLexer lexer = new XmlLexer(new XmlDocument(XML_W_NUMERIC_ENTITY));
    lexer.reset(0, 0);

    for (final int tokenType : tokenTypes) {
      lexer.advance();
      assertEquals(tokenType, lexer.getTokenType());
    }
  }

  public void testNumericEntityFlexLexer() throws Exception {
    final XmlFlexLexer lexer = new XmlFlexLexer();
    lexer.reset(XML_W_NUMERIC_ENTITY, 0, 0);

    for (final int tokenType : tokenTypes) {
      assertEquals(tokenType, lexer.advance());
    }
  }
}
