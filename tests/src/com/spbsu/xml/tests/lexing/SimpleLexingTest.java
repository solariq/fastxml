package com.spbsu.xml.tests.lexing;

import com.spbsu.xml.XmlDocument;
import com.spbsu.xml.impl.lexer.XmlFlexLexer;
import com.spbsu.xml.impl.lexer.XmlLexer;
import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: Igor Kuralenok
 * Date: 10.05.2006
 * Time: 19:15:56
 * To change this template use File | Settings | File Templates.
 */
public class SimpleLexingTest extends TestCase {
  public void testEmpty() throws Exception{
    final XmlLexer lexer = new XmlLexer(new XmlDocument(""));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    assertEquals(-1, lexer.advance());
    assertEquals(-1, lexer.advance());
  }

  public void testText() throws Exception{
    XmlLexer lexer = new XmlLexer(new XmlDocument("cooltext"));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    assertEquals("DATA", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
  }

  public void testWSInText() throws Exception{
    XmlLexer lexer = new XmlLexer(new XmlDocument("cool text"));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    assertEquals("DATA", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("WS", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("DATA", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
  }

  public void testQuoteInText() throws Exception{
    XmlLexer lexer = new XmlLexer(new XmlDocument("cool\"text"));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    assertEquals("DATA", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
  }

  public void testEmptyTag() throws Exception{
    XmlLexer lexer = new XmlLexer(new XmlDocument("<a/>"));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    assertEquals("TAG_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EMPTY_TAG_END", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
  }

  public void testTagNameWithDash() throws Exception {
    XmlLexer lexer = new XmlLexer(new XmlDocument("<a-a/>"));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    assertEquals("TAG_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EMPTY_TAG_END", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
  }

  public void testTagNameWithDots() throws Exception{
    XmlLexer lexer = new XmlLexer(new XmlDocument("<a:a/>"));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    assertEquals("TAG_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EMPTY_TAG_END", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
  }

  public void testTagNameWithDot() throws Exception{
    final XmlLexer lexer = new XmlLexer(new XmlDocument("<a.a/>"));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    assertEquals("TAG_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EMPTY_TAG_END", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
  }

  public void testTagNameWithSlash() throws Exception{
    XmlLexer lexer = new XmlLexer(new XmlDocument("<a/s/>"));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    assertEquals("TAG_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("DATA", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
  }

  public void testAttributeName() throws Exception{
    XmlLexer lexer = new XmlLexer(new XmlDocument("<a s=\" \"/>"));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    assertEquals("TAG_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("WS", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EQ", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("ATTR_VAL_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("ATTR_VAL", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("ATTR_VAL_END", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EMPTY_TAG_END", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
  }

  public void testAttributeNameWithDots() throws Exception{
    XmlLexer lexer = new XmlLexer(new XmlDocument("<a s:s=\" \"/>"));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    assertEquals("TAG_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("WS", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EQ", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("ATTR_VAL_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("ATTR_VAL", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("ATTR_VAL_END", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EMPTY_TAG_END", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
  }

  public void testCharEntity1() throws Exception{
    final XmlLexer lexer=new XmlLexer(new XmlDocument(" &#333; "));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    assertEquals("WS", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("CHAR_ENTITY", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("WS", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
  }

  public void testCharEntity2() throws Exception{
    final XmlLexer lexer=new XmlLexer(new XmlDocument(" &#quot; "));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    assertEquals("WS", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("BAD_CHAR", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("DATA", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("WS", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
  }

  public void testCharEntity3() throws Exception{
    final XmlLexer lexer=new XmlLexer(new XmlDocument(" &quot; "));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    assertEquals("WS", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("CHAR_ENTITY", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("WS", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
  }

  public void testCharEntityInAttribute() throws Exception{
    final XmlLexer lexer=new XmlLexer(new XmlDocument("<a s:s=\"&quot;\"/>"));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    assertEquals("TAG_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("WS", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EQ", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("ATTR_VAL_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("CHAR_ENTITY", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("ATTR_VAL_END", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EMPTY_TAG_END", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
  }

  public void testComment1() throws Exception{
    final XmlLexer lexer=new XmlLexer(new XmlDocument("<!-- asd --><a/>"));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    assertEquals("COMMENT", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("TAG_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EMPTY_TAG_END", DebugUtil.getNameForTokenIndex(lexer.advance()));

    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
  }

  public void testComment2() throws Exception {
    final XmlLexer lexer=new XmlLexer(new XmlDocument("<xml><a> <!-- comment --> <b attr=\"\"/></a></xml>"));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    assertEquals("TAG_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("TAG_END", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("TAG_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("TAG_END", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("WS", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("COMMENT", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("WS", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("TAG_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("WS", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EQ", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("ATTR_VAL_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("ATTR_VAL_END", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EMPTY_TAG_END", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("END_TAG_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("TAG_END", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("END_TAG_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("TAG_END", DebugUtil.getNameForTokenIndex(lexer.advance()));
    assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
  }

  public void testPerformance() throws Exception{
    final String base = "<a s:s=\"&quot;\"/>";
    final StringBuffer buf = new StringBuffer();

    for(int i = 0; i < 10000; i++) buf.append(base);
    XmlLexer lexer = new XmlLexer(new XmlDocument(buf));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    final long time = System.currentTimeMillis();
    int index = 0;
    while(lexer.advance() >= 0) index++;
    final long total = System.currentTimeMillis() - time;
    System.out.println("Found " + index + " for " + total + " milliseconds");
    assertTrue(total < 100);
  }
}
