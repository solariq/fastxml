package com.spbsu.xml.tests;

import com.spbsu.xml.XmlFile;
import com.spbsu.xml.XmlFactory;
import com.spbsu.xml.RecursiveXmlVisitor;
import com.spbsu.xml.XmlTag;
import junit.framework.TestCase;

/**
 * User: vasiliy
 * Date: Jul 30, 2009
 */
public class XmlMalformedAttrsTest extends TestCase {
  public void testInfiniteLoops() throws Exception {
    String input =
            "<rss>" +
            "<channel>" +
            "<a target=_blank>" +
            "</channel>" +
            "</rss>";

    System.out.println("input length = " + input.length());
    System.out.println("Substring from 47: '" + input.substring(47) + "'");
    XmlFile file = XmlFactory.parseTextFile(input);
    file.accept(new RecursiveXmlVisitor() {
      public boolean visitTag(final XmlTag tag) {
        if (!tag.getName().equals("channel")) {
          super.visitTag(tag);
        }
        return true;
      }
    });
  }
}
