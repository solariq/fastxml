package com.spbsu.xml.impl;

import com.spbsu.xml.*;
import com.spbsu.xml.impl.lexer.XmlFlexLexer;
import com.spbsu.xml.impl.lexer.XmlLexer;
import com.spbsu.xml.impl.lexer.XmlTokenType;
import org.jetbrains.annotations.Nullable;

import java.util.Vector;

public class XmlTagImpl extends XmlTagChildBase implements XmlTag {
  private static int[] INSIDE_START_TAG_MASK = XmlTokenType.createMask(new int[]{
          XmlTokenType.NAME,
          XmlTokenType.TAG_END,
          XmlTokenType.EMPTY_TAG_END,
          XmlTokenType.PI_END
  });

  private static final int[] BRACES_FILTER = XmlTokenType.createMask(
          XmlTokenType.TAG_START,
          XmlTokenType.END_TAG_START,
          XmlTokenType.EMPTY_TAG_END
  );

  protected CharSequence name = null;
  protected XmlAttributeImpl[] attributes = null;
  private int firstChildOffset = -1;
  private int endTagStartOffset = -1;
  private int lastKnownChildEnd = -1;

  public XmlTagImpl(XmlElement parent, int startOffset) {
    super(parent, startOffset);
  }

  public CharSequence getName() {
    parseTopLevelElements();
    return name;
  }

  public CharSequence getNamespace() {
    throw new RuntimeException("Not supported yet");
  }

  public CharSequence getNamespaceByPrefix(CharSequence prefix) {
    throw new RuntimeException("Not supported yet");
  }

  public CharSequence getAttribute(CharSequence name) {
    parseTopLevelElements();
    for (int i = 0; i < attributes.length; i++) {
      final XmlAttributeImpl attribute = attributes[i];
      if(attribute.getName().equals(name)) return attribute.getValue();
    }
    return null;
  }

  public String getAttributeValue(CharSequence name) {
    return getAttribute(name).toString();
  }

  public XmlTag setAttribute(CharSequence name, CharSequence value) {
    parseTopLevelElements();
    for (int i = 0; i < attributes.length; i++) {
      final XmlAttributeImpl attribute = attributes[i];
      if(attribute.getName().equals(name)) {
        if(value != null) {
          value = XmlFactory.escape(value);
          int startOffset = attribute.getValueStart();
          int endOffset = attribute.getEndOffset() - 1;
          getDocument().replace(startOffset, endOffset, value);
        }
        else
          getDocument().delete(attribute.getStartOffset() - 1, attribute.getEndOffset());
        return this;
      }
    }
    String text = " " + name + "=\"" + XmlFactory.escape(value) + "\"";
    getDocument().insert(firstChildOffset >= 0 ? firstChildOffset - 1 : getEndOffset() - 2, text);
    dropParsedFlag();
    return this;
  }

  public XmlTagChild getFirstChild() {
    parseTopLevelElements();
    if(firstChildOffset < 0) return null;
    final XmlLexer lexer = getDocument().getLexer();
    lexer.reset(firstChildOffset, XmlFlexLexer.YYINITIAL);
    return createChild(this, lexer);
  }

  public XmlTag getChild(CharSequence name) {
    XmlTagChild child = getFirstChild();
    while(child != null) {
      if(child instanceof XmlTag)
        if(((XmlTag) child).getName().equals(name))
          return (XmlTag) child;
      child = child.next();
    }
    return null;
  }

  public XmlTag addContent(CharSequence childText) {
    parseTopLevelElements();
    if(firstChildOffset < 0){
      getDocument().replace(getEndOffset() - 2, getEndOffset(), ">" + childText + "</" + getName() + ">");
      dropParsedFlag();
    }
    else{
      final int childOffset = getEndTagStartOffset();
      getDocument().insert(childOffset, childText);
      final int oldFirstChildOffset = firstChildOffset;
      dropParsedFlag();
      lastKnownChildEnd = childOffset;
      firstChildOffset = oldFirstChildOffset;
    }
    return this;
  }

  public XmlTag addContent(XmlTagChild childText) {
    return addContent(childText.getText());
  }

  public XmlTag deleteChild(XmlTagChild child) {
    final XmlTagChildBase xmlTagChildBase = ((XmlTagChildBase) child);
    getDocument().delete(xmlTagChildBase.getStartOffset(), xmlTagChildBase.getEndOffset());
    return this;
  }

  public void processTagsWithName(XmlVisitor visitor, @Nullable CharSequence name, CharSequence namespace) {
    XmlTagChild current = getFirstChild();
    while(current != null){
      if(current instanceof XmlTag && (name == null || ((XmlTag) current).getName().equals(name))) visitor.visitTag((XmlTag)current);
      current = current.next();
    }
  }

  public void processTexts(XmlVisitor visitor) {
    XmlTagChild current = getFirstChild();
    while(current != null){
      if(current instanceof XmlText) visitor.visitText((XmlText) current);
      current = current.next();
    }
  }

  public void processAttributes(XmlVisitor visitor) {
    parseTopLevelElements();
    for (int i = 0; i < attributes.length; i++) {
      XmlAttributeImpl attribute = attributes[i];
      visitor.visitAttribute(attribute.getName(), attribute.getValue(), this);
    }
  }

  public XmlTagChild getLastChild() {
    XmlTagChild child = getFirstChild();
    if(child == null) return null;
    XmlTagChild next = child.next();
    while(next != null){
      child = next;
      next = next.next();
    }
    return child;
  }

  public CharSequence getChildText() {
    final StringBuilder buffer = new StringBuilder();
    XmlTagChild child = getFirstChild();
    while(child != null){
      if(child instanceof XmlText){
        final XmlText text = (XmlText) child;
        buffer.append(text.getValue());
      }
      child = child.next();
    }
    return buffer;
  }

  public String getTextTrim() {
    return getChildText().toString().trim();
  }

  public boolean accept(XmlVisitor visitor) {
    return visitor.visitTag(this);
  }

  public XmlTagChild next() {
    parseTopLevelElements();
    if(firstChildOffset >= 0 && endTagStartOffset < 0) adjustNotEmptyTagText();
    return super.next();
  }

  public int getEndOffset() {
    parseTopLevelElements();
    if(endOffset <= 0) adjustNotEmptyTagText();
    return endOffset;
  }

  protected void parseTopLevelElementsInner() {
    final XmlLexer lexer = getParent().getDocument().getLexer();
    lexer.reset(getStartOffset(), XmlFlexLexer.YYINITIAL);
    assertTrue(lexer, XmlTokenType.TAG_START);

    int tokenType = lexer.getTokenType();
    while(tokenType >= 0 && tokenType != XmlTokenType.NAME) tokenType = lexer.advance();
    assertTrue(lexer, XmlTokenType.NAME);
    name = lexer.getTokenText();

    lexer.advance();
    attributes = parseAttributes(lexer, this);
    tokenType = lexer.getTokenType();
    if (tokenType != XmlTokenType.EMPTY_TAG_END) {
      assertTrue(lexer, XmlTokenType.TAG_END);
      firstChildOffset = lexer.getTokenEnd();
      endOffset = 0;
    }
    else endOffset = lexer.getTokenEnd();
  }

  public static XmlAttributeImpl[] parseAttributes(XmlLexer lexer, XmlElement tag) {
    Vector<XmlAttributeImpl> attributesV = null;
    int tokenType;
    do{
      tokenType = XmlTokenType.skipTokensByMask(lexer, INSIDE_START_TAG_MASK);
      if(tokenType == XmlTokenType.NAME) {
        if(attributesV == null) attributesV = new Vector<XmlAttributeImpl>();
        final XmlAttributeImpl attribute = new XmlAttributeImpl(tag, lexer.getTokenStart());
        attribute.parseTopLevelElements();
        attributesV.addElement(attribute);
      }
      else break;
      tokenType = lexer.advance();
    }
    while(tokenType >= 0);
    if(attributesV == null) return XmlAttributeImpl.EMPTY_ARRAY;
    return attributesV.toArray(new XmlAttributeImpl[attributesV.size()]);
  }

  private int getEndTagStartOffset() {
    if(endTagStartOffset <= 0) adjustNotEmptyTagText();
    return endTagStartOffset;
  }

  private void adjustNotEmptyTagText(){
    parseTopLevelElements();
    if(firstChildOffset < 0 || endTagStartOffset >= 0) return;
    int balance = 1;
    XmlLexer lexer = getDocument().getLexer();
    final int start = Math.max(firstChildOffset, lastKnownChildEnd);
    lexer.reset(start, XmlFlexLexer.YYINITIAL);
    while (balance > 0 && lexer.getTokenType() >= 0) {
      final int tokenType = XmlTokenType.skipTokensByMask(lexer, BRACES_FILTER);
      if (tokenType == XmlTokenType.TAG_START) balance++;
      else balance--;
      if (balance > 0) lexer.advance();
    }
    endTagStartOffset = lexer.getTokenStart();
    lexer.advance();
    assertTrue(lexer, XmlTokenType.NAME);
    if (!lexer.getTokenText().equals(name))
      throw new RuntimeException("Invalid xml: parsing exception at offset: " + lexer.getTokenStart() +
                                 "\nend tag name (" + lexer.getTokenText()
                                 + ") does not match start name ("+name+").");
    lexer.advance();
    assertTrue(lexer, XmlTokenType.TAG_END);
    endOffset = lexer.getTokenEnd();
  }

  protected static XmlTagChild createChild(XmlTagImpl parent, XmlLexer lexer) {
    int tokenType = lexer.getTokenType();
    while(tokenType == XmlTokenType.COMMENT) tokenType = lexer.advance();

    XmlTagChild firstChild = null;
    if(tokenType == XmlTokenType.TAG_START){
      firstChild = new XmlTagImpl(parent, lexer.getTokenStart());
    }
    else if(tokenType == XmlTokenType.PI_START) {
      firstChild = new XmlInstructionImpl(parent, lexer.getTokenStart());
    }
    else if(tokenType != XmlTokenType.END_TAG_START) {
      firstChild = new XmlTextImpl(parent, lexer.getTokenStart());
    }
    return firstChild;
  }

  protected void dropParsedFlag() {
    final XmlElementBase parent = (XmlElementBase)getParent();
    parent.dropParsedFlag();
    super.dropParsedFlag();
    firstChildOffset = -1;
    endTagStartOffset = -1;
    lastKnownChildEnd = -1;
  }

  public void setLastKnownChildStart(int nextStart) {
    lastKnownChildEnd = nextStart;
  }
}
