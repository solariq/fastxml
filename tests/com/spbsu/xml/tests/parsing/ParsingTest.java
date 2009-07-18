package com.spbsu.xml.tests.parsing;

import com.spbsu.xml.*;
import com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl;
import junit.framework.TestCase;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.StringReader;

/**
 * Created by IntelliJ IDEA.
 * User: Igor Kuralenok
 * Date: 10.05.2006
 * Time: 20:52:49
 * To change this template use File | Settings | File Templates.
 */
public class ParsingTest extends TestCase {
  public void testEmptyFile() {
    final XmlFile xmlFile = XmlFactory.parseTextFile("");
    assertNotNull(xmlFile);
    assertNull(xmlFile.getXmlDefinition());
    assertNull(xmlFile.getRootTag());
  }

  public void testXmlDeclaration() {
    final XmlFile xmlFile = XmlFactory.parseTextFile("<?xml version=\"1.0\"?>");
    assertNotNull(xmlFile);
    final XmlInstruction xmlDefinition = xmlFile.getXmlDefinition();
    assertNotNull(xmlDefinition);
    assertTrue("xml".equals(xmlDefinition.getName().toString()));
    assertTrue("1.0".equals(xmlDefinition.getAttribute("version").toString()));
    assertNull(xmlFile.getRootTag());
  }

  public void testEmptyXmlTag1() {
    final XmlFile xmlFile = XmlFactory.parseTextFile("<xml version=\"1.0\"/>");
    assertNotNull(xmlFile);
    final XmlInstruction xmlDefinition = xmlFile.getXmlDefinition();
    assertNull(xmlDefinition);
    final XmlTag tag = xmlFile.getRootTag();
    assertNotNull(tag);
    assertTrue("xml".equals(tag.getName().toString()));
    assertTrue("1.0".equals(tag.getAttribute("version").toString()));
  }

  public void testEmptyXmlTag2() {
    final XmlFile xmlFile = XmlFactory.parseTextFile("<xml version=\"1.0\"></xml>");
    assertNotNull(xmlFile);
    final XmlInstruction xmlDefinition = xmlFile.getXmlDefinition();
    assertNull(xmlDefinition);
    final XmlTag tag = xmlFile.getRootTag();
    assertNotNull(tag);
    assertTrue("xml".equals(tag.getName().toString()));
    assertTrue("1.0".equals(tag.getAttribute("version").toString()));
  }

  public void testCompositeFile() {
    final XmlFile xmlFile = XmlFactory.parseTextFile("<?xml version=\"1.0\"?><xml version=\"1.0\"/>");
    assertNotNull(xmlFile);
    final XmlInstruction xmlDefinition = xmlFile.getXmlDefinition();
    assertNotNull(xmlDefinition);
    assertTrue("xml".equals(xmlDefinition.getName().toString()));
    assertTrue("1.0".equals(xmlDefinition.getAttribute("version").toString()));
    final XmlTag tag = xmlFile.getRootTag();
    assertNotNull(tag);
    assertTrue("xml".equals(tag.getName().toString()));
    assertTrue("1.0".equals(tag.getAttribute("version").toString()));
  }

  public void testCompositeTag1() throws Exception {
    final XmlFile xmlFile = XmlFactory.parseTextFile("<xml version=\"1.0\"><a/></xml>");
    assertNotNull(xmlFile);
    final XmlTag tag = xmlFile.getRootTag();
    assertNotNull(tag);
    assertTrue("xml".equals(tag.getName().toString()));
    assertTrue("1.0".equals(tag.getAttribute("version").toString()));
    assertNotNull(tag.getFirstChild());
    assertNull(tag.getFirstChild().next());
  }

  public void testCompositeTag2() throws Exception {
    final XmlFile xmlFile = XmlFactory.parseTextFile("<xml version=\"1.0\"><a/> </xml>");
    assertNotNull(xmlFile);
    final XmlTag tag = xmlFile.getRootTag();
    assertNotNull(tag);
    assertTrue("xml".equals(tag.getName().toString()));
    assertTrue("1.0".equals(tag.getAttribute("version").toString()));
    assertNotNull(tag.getFirstChild());
    assertNotNull(tag.getFirstChild().next());
  }

  public void testCompositeTag3() throws Exception {
    final XmlFile xmlFile = XmlFactory.parseTextFile("<xml version=\"1.0\"><a/> <b attr=\"\"/></xml>");
    assertNotNull(xmlFile);
    final XmlTag tag = xmlFile.getRootTag();
    assertNotNull(tag);
    assertTrue("xml".equals(tag.getName().toString()));
    assertTrue("1.0".equals(tag.getAttribute("version").toString()));
    assertNotNull(tag.getFirstChild());
    assertNotNull(tag.getFirstChild().next());
    final XmlTag tagB = (XmlTag)tag.getFirstChild().next().next();
    assertNotNull(tagB);
    assertEquals("b", tagB.getName().toString());
    assertEquals("", tagB.getAttribute("attr").toString());
  }

  public void testSubTag1() throws Exception {
    final XmlFile xmlFile = XmlFactory.parseTextFile("<xml version=\"1.0\"><a><b attr=\"\"/></a></xml>");
    assertNotNull(xmlFile);
    final XmlTag tag = xmlFile.getRootTag();
    assertNotNull(tag);
    assertTrue("xml".equals(tag.getName().toString()));
    assertTrue("1.0".equals(tag.getAttribute("version").toString()));
    assertNotNull(tag.getFirstChild());
    final XmlTag tagB = (XmlTag)((XmlTag) tag.getFirstChild()).getFirstChild();
    assertNotNull(tagB);
    assertEquals("b", tagB.getName().toString());
    assertEquals("", tagB.getAttribute("attr").toString());
  }

  public void testComment1() throws Exception {
    final XmlFile xmlFile = XmlFactory.parseTextFile("<xml version=\"1.0\"><a> <!-- ываыва --> <b attr=\"\"/></a></xml>");
    assertNotNull(xmlFile);
    final XmlTag tag = xmlFile.getRootTag();
    assertNotNull(tag);
    assertTrue("xml".equals(tag.getName().toString()));
    assertTrue("1.0".equals(tag.getAttribute("version").toString()));
    assertNotNull(tag.getFirstChild());
    final XmlTag tagB = (XmlTag)((XmlTag) tag.getFirstChild()).getFirstChild().next().next();
    assertNotNull(tagB);
    assertEquals("b", tagB.getName().toString());
    assertEquals("", tagB.getAttribute("attr").toString());
  }

  public void testVisitorPerformance() throws Exception{
    final String baseStart = "<a s:s=\"&quot;\">";
    final String baseEnd = "</a>";
    final StringBuffer buf = new StringBuffer();
    int tagsCount = 1;
    buf.append("<a xmlns:s=\"xxx\">");
    for(int j = 0; j < 50; j++){
      for(int i = 0; i < 20; i++){
        buf.append(baseStart);
        tagsCount++;
      }
      for(int i = 0; i < 20; i++) buf.append(baseEnd);
    }
    buf.append("</a>");
    final String bufStr = buf.toString();

    // DOM
    {
//      long startTime = System.currentTimeMillis();
//      final DocumentBuilderFactory domFactory = new DocumentBuilderFactoryImpl();
//      domFactory.setNamespaceAware(true);
//      final DocumentBuilder domParser = domFactory.newDocumentBuilder();
//      domParser.parse(new InputSource(new StringReader(bufStr)));
//      System.out.println("DOM parsed " + tagsCount + " tags for " + (System.currentTimeMillis() - startTime) + "ms");
    }

    // SAX
    {
      long startTime = System.currentTimeMillis();
      final SAXParserFactory saxFactory = new SAXParserFactoryImpl();
      saxFactory.setNamespaceAware(true);
      final SAXParser saxParser = saxFactory.newSAXParser();
      saxParser.parse(new InputSource(new StringReader(bufStr)), new DefaultHandler(){
        int tags;
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
          tags++;
          super.startElement(uri, localName, qName, attributes);
        }
      });
      System.out.println("SAX visited " + tagsCount + " tags for " + (System.currentTimeMillis() - startTime) + "ms");
    }

    {
      long startTime = System.currentTimeMillis();
      final XmlFile xmlFile = XmlFactory.parseTextFile(buf);
      PerformanceMeterVisitor visitor = new PerformanceMeterVisitor();
      xmlFile.accept(visitor);
      System.out.println("" + visitor.tags + " visited for " + (System.currentTimeMillis() - startTime) + "ms");
    }

  }

  private static class PerformanceMeterVisitor extends RecursiveXmlVisitor {
    int tags;

    public boolean visitTag(XmlTag xmlTag) {
      tags++;
      return super.visitTag(xmlTag);
    }
  }
}