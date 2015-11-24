package com.spbsu.xml.tests;

import com.spbsu.xml.RecursiveXmlVisitor;
import com.spbsu.xml.XmlFactory;
import com.spbsu.xml.XmlFile;
import com.spbsu.xml.XmlTag;
import junit.framework.TestCase;
import org.junit.Assert;

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
    XmlFile file = XmlFactory.parseText(input);
    try {
      file.accept(new RecursiveXmlVisitor() {
        public boolean visitTag(final XmlTag tag) {
          if (!tag.getName().equals("channel")) {
            super.visitTag(tag);
          }
          return true;
        }
      });
      Assert.assertTrue(false); // must throw exception
    }
    catch (RuntimeException re) {
      Assert.assertEquals("Invalid xml: parsing exception at offset: 43" +
                              "\nend tag name (rss) does not match start name (channel).", re.getMessage());
    }
  }
}
