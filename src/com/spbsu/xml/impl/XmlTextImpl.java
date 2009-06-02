package com.spbsu.xml.impl;

import com.spbsu.xml.XmlText;
import com.spbsu.xml.XmlVisitor;
import com.spbsu.xml.impl.lexer.XmlFlexLexer;
import com.spbsu.xml.impl.lexer.XmlLexer;
import com.spbsu.xml.impl.lexer.XmlTokenType;

/**
 * Created by IntelliJ IDEA.
 * User: Igor Kuralenok
 * Date: 11.05.2006
 * Time: 17:00:44
 * To change this template use File | Settings | File Templates.
 */
public class XmlTextImpl extends XmlTagChildBase implements XmlText {
  private static final int[] CONTENTS_FILTER = XmlTokenType.createMask(XmlTokenType.WS, XmlTokenType.DATA);
  private CharSequence value;
  public XmlTextImpl(XmlTagImpl parent, int startOffset) {
    super(parent, startOffset);
  }

  public boolean accept(XmlVisitor visitor) {
    return visitor.visitText(this);
  }

  public CharSequence getValue() {
    parseTopLevelElements();
    return value;
  }

  protected void parseTopLevelElementsInner() {
    final XmlLexer lexer = getDocument().getLexer();
    lexer.reset(getStartOffset(), XmlFlexLexer.YYINITIAL);
    StringBuffer buffer = null;
    int currentType = lexer.getTokenType();
    while(currentType >= 0) {
      if(CONTENTS_FILTER[currentType] != 0){
        if(buffer != null) buffer.append(lexer.getTokenText().toString());
      }
      else if(currentType == XmlTokenType.ENTITY_REF || currentType == XmlTokenType.CHAR_ENTITY) {
        if(buffer == null)
          buffer = new StringBuffer(getDocument().subSequence(getStartOffset(), lexer.getTokenStart()).toString());
        buffer.append(XmlTokenType.decodeEntity(lexer.getTokenText()));
      }
      else if(currentType != XmlTokenType.CDATA_START &&
              currentType != XmlTokenType.CDATA_END &&
              currentType != XmlTokenType.BAD_CHAR
          )
        break;
      currentType = lexer.advance();
    }
    endOffset = lexer.getTokenStart();
    if(buffer != null) value = buffer;
    else value = getDocument().subSequence(getStartOffset(), getEndOffset());
    if(value.length() == 0)
      assert false;
  }
}
