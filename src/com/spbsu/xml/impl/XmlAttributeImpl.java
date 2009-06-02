package com.spbsu.xml.impl;

import com.spbsu.xml.XmlElement;
import com.spbsu.xml.XmlTag;
import com.spbsu.xml.XmlVisitor;
import com.spbsu.xml.impl.lexer.XmlFlexLexer;
import com.spbsu.xml.impl.lexer.XmlLexer;
import com.spbsu.xml.impl.lexer.XmlTokenType;
import com.spbsu.util.CharSequenceBase;
import com.spbsu.util.CompositeCharSequence;

/**
 * Created by IntelliJ IDEA.
 * User: Igor Kuralenok
 * Date: 10.05.2006
 * Time: 22:55:00
 * To change this template use File | Settings | File Templates.
 */
public class XmlAttributeImpl extends XmlElementBase {
  public static final XmlAttributeImpl[] EMPTY_ARRAY = new XmlAttributeImpl[0];

  private CharSequence name = null;
  private CharSequence value = null;
  private int valueStart = -1;

  public XmlAttributeImpl(XmlElement parent, int text) {
    super(parent, text);
  }

  public CharSequence getName(){
    if(name != null) return name;
    parseTopLevelElements();
    return name;
  }

  public CharSequence getValue(){
    if(value != null) return value;
    parseTopLevelElements();
    return value;
  }

  protected void parseTopLevelElementsInner() {
    XmlLexer lexer = getDocument().getLexer();
    lexer.reset(getStartOffset(), XmlFlexLexer.ATTR_LIST);
    assertTrue(lexer, XmlTokenType.NAME);
    name = lexer.getTokenText();
    lexer.advance();
    assertTrue(lexer,  XmlTokenType.EQ);
    lexer.advance();
    assertTrue(lexer, XmlTokenType.ATTR_VAL_START);
    valueStart = lexer.getTokenEnd();
    int tokenType;
    while((tokenType = lexer.advance()) != XmlTokenType.ATTR_VAL_END){
      if(tokenType == XmlTokenType.ATTR_VAL){
        if(value == null) value = lexer.getTokenText();
        else value = value.toString() + lexer.getTokenText();
      }
      else if(tokenType == XmlTokenType.CHAR_ENTITY){
        final CharSequence decodedEntity = XmlTokenType.decodeEntity(lexer.getTokenText());
        value = value != null ? new CompositeCharSequence(new CharSequence[]{value, decodedEntity}) : decodedEntity;
      }
      else {
        assertTrue(lexer, XmlTokenType.BAD_CHAR);
        break;
      }
    }
    if(value == null) value = CharSequenceBase.EMPTY;
    assertTrue(lexer, XmlTokenType.ATTR_VAL_END);
    endOffset = lexer.getTokenEnd();
  }

  public boolean accept(XmlVisitor visitor) {
    return visitor.visitAttribute(getName(), getValue(), (XmlTag)getParent());
  }

  public int getValueStart() {
    parseTopLevelElements();
    return valueStart;
  }
}
