package com.spbsu.xml.tests.parsing;

import com.spbsu.xml.RecursiveXmlVisitor;
import com.spbsu.xml.XmlFactory;
import com.spbsu.xml.XmlFile;
import com.spbsu.xml.XmlTag;
import junit.framework.TestCase;

public class WriteTest extends TestCase {
  public void testSetAttribute() throws Exception {
    final XmlFile xmlFile = XmlFactory.parseTextFile("<a/>");
    xmlFile.getRootTag().setAttribute("x", "x");
    assertEquals("<a x=\"x\"/>", xmlFile.getDocument().toString());
  }

  public void testRemoveAttribute() throws Exception {
    final XmlFile xmlFile = XmlFactory.parseTextFile("<a x=\"ss\"/>");
    xmlFile.getRootTag().setAttribute("x", null);
    assertEquals("<a/>", xmlFile.getDocument().toString());
  }

  public void testReplaceAttribute() throws Exception {
    final XmlFile xmlFile = XmlFactory.parseTextFile("<a x=\"ss\"/>");
    xmlFile.getRootTag().setAttribute("x", "x");
    assertEquals("<a x=\"x\"/>", xmlFile.getDocument().toString());
  }

  public void testAddChildToEmpty() throws Exception {
    final XmlFile xmlFile = XmlFactory.parseTextFile("<a/>");
    xmlFile.getRootTag().addContent("<a/>");
    assertEquals("<a><a/></a>", xmlFile.getRootTag().getDocument().toString());
  }

  public void testAddChild() throws Exception {
    final XmlFile xmlFile = XmlFactory.parseTextFile("<a><b/></a>");
    xmlFile.getRootTag().addContent("<a/>");
    assertEquals("<a><b/><a/></a>", xmlFile.getRootTag().getDocument().toString());
  }

  public void testDeleteChild() throws Exception {
    final XmlFile xmlFile = XmlFactory.parseTextFile("<a><b/></a>");
    xmlFile.getRootTag().deleteChild(xmlFile.getRootTag().getFirstChild());
    assertEquals("<a></a>", xmlFile.getRootTag().getDocument().toString());
  }

  public void testVisitorPerformance() throws Exception{
    final String base = "<a s:s=\"&quot;\"/>";
    final StringBuffer buf = new StringBuffer();
    buf.append("<a>");
    for(int i = 0; i < 10000; i++) buf.append(base);
    buf.append("</a>");
    long startTime = System.currentTimeMillis();
    final XmlFile xmlFile = XmlFactory.parseTextFile(buf);
    PerformanceMeterVisitor visitor = new PerformanceMeterVisitor();
    xmlFile.accept(visitor);
    System.out.println("" + visitor.tags + " edited for " + (System.currentTimeMillis() - startTime) + "ms");
  }

  private static class PerformanceMeterVisitor extends RecursiveXmlVisitor {
    int tags;

    public boolean visitTag(XmlTag xmlTag) {
      tags++;
      xmlTag.setAttribute("x", "y");
      xmlTag.addContent("xxx");
      return super.visitTag(xmlTag);
    }
  }
}
