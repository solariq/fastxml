package com.spbsu.xml.tests;

import junit.framework.TestCase;
import com.spbsu.xml.*;

/**
 * @author vp
 */
public class XmlCDATATest extends TestCase {
  private static final String CDATA_START = "<![CDATA[";
  private static final String CDATA_END = "]]>";
  private static final String CONTENT = "content";
  private static final String TAG = "child";
  private static final String TEST_XML =
    "<test>" +
      "<" + TAG + ">" + CDATA_START + CONTENT +  CDATA_END + "</" + TAG + ">" +
    "</test>";

  public void testCDATA() throws Exception {
    final XmlFile xmlFile = XmlFactory.parseTextFile(TEST_XML);
    final XmlTag rootTag = xmlFile.getRootTag();
    final XmlTag tag = rootTag.getChild(TAG);
    final XmlTagChild child = tag.getFirstChild();
    assertTrue(child instanceof XmlText);
    assertEquals(((XmlText) child).getValue(), CONTENT);
  }
}
