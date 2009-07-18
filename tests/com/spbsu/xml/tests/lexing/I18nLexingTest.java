package com.spbsu.xml.tests.lexing;

import com.spbsu.xml.XmlDocument;
import com.spbsu.xml.impl.lexer.XmlFlexLexer;
import com.spbsu.xml.impl.lexer.XmlLexer;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: Igor Kuralenok
 * Date: 11.05.2006
 * Time: 10:42:57
 * To change this template use File | Settings | File Templates.
 */
public class I18nLexingTest extends   TestCase {
  public void testChars() throws Exception{
    XmlLexer lexer = new XmlLexer(new XmlDocument("???????????"));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    Assert.assertEquals("DATA", DebugUtil.getNameForTokenIndex(lexer.advance()));
    Assert.assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
    Assert.assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
  }

  public void testEmptyTag() throws Exception{
    final XmlLexer lexer = new XmlLexer(new XmlDocument("<Облом/>"));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    Assert.assertEquals("TAG_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    Assert.assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    Assert.assertEquals("EMPTY_TAG_END", DebugUtil.getNameForTokenIndex(lexer.advance()));
    Assert.assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
    Assert.assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
  }

  public void testCharEntityInAttribute() throws Exception{
    final XmlLexer lexer=new XmlLexer(new XmlDocument("<ф ы:в=\"а&quot;\"/>"));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    Assert.assertEquals("TAG_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    Assert.assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    Assert.assertEquals("WS", DebugUtil.getNameForTokenIndex(lexer.advance()));
    Assert.assertEquals("NAME", DebugUtil.getNameForTokenIndex(lexer.advance()));
    Assert.assertEquals("EQ", DebugUtil.getNameForTokenIndex(lexer.advance()));
    Assert.assertEquals("ATTR_VAL_START", DebugUtil.getNameForTokenIndex(lexer.advance()));
    Assert.assertEquals("ATTR_VAL", DebugUtil.getNameForTokenIndex(lexer.advance()));
    Assert.assertEquals("CHAR_ENTITY", DebugUtil.getNameForTokenIndex(lexer.advance()));
    Assert.assertEquals("ATTR_VAL_END", DebugUtil.getNameForTokenIndex(lexer.advance()));
    Assert.assertEquals("EMPTY_TAG_END", DebugUtil.getNameForTokenIndex(lexer.advance()));
    Assert.assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
    Assert.assertEquals("EOF", DebugUtil.getNameForTokenIndex(lexer.advance()));
  }

  public void testPerformance() throws Exception{
    final String base = "<?? ?:?=\"?&quot;\"/>";
    final StringBuffer buf = new StringBuffer();

    for(int i = 0; i < 10000; i++) buf.append(base);
    final XmlLexer lexer = new XmlLexer(new XmlDocument(buf));
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    final long time = System.currentTimeMillis();
    int index = 0;
    while(lexer.advance() >= 0) index++;
    final long total = System.currentTimeMillis() - time;
    System.out.println("Found " + index + " for " + total + " milliseconds");
    assertTrue(total < 100);
  }
}
