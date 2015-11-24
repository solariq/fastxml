package com.spbsu.xml;

import com.spbsu.commons.seq.CharSeq;
import com.spbsu.commons.seq.CharSeqArray;
import com.spbsu.commons.seq.CharSeqComposite;
import com.spbsu.xml.impl.lexer.XmlLexer;


public class XmlDocument {
  public static final int MAX_FRAGMENTS_COUNT = 200;

  private XmlLexer lexer = new XmlLexer(this);
  int operationsCount = 0;
  private CharSequence text;
  public static long totalCount = 0;

  public XmlDocument(CharSequence text) {
    this.text = CharSeq.copy(text);
  }

  public XmlLexer getLexer() {
    return lexer;
  }

  public void insert(int offset, CharSequence text) {
    this.text = new CharSeqComposite(this.text.subSequence(0, offset),
                                     text,
                                     this.text.subSequence(offset, length()));
    compact();
  }

  public void replace(int start, int end, CharSequence text) {
    this.text = new CharSeqComposite(this.text.subSequence(0, start),
                                     text,
                                     this.text.subSequence(end, length()));
    compact();
  }

  public void delete(int start, int end) {
    this.text = new CharSeqComposite(text.subSequence(0, start), text.subSequence(end, length()));
    compact();
  }

  private void compact() {
    if(text instanceof CharSeqComposite && ((CharSeqComposite) text).fragmentsCount() > MAX_FRAGMENTS_COUNT){
      final char[] array = ((CharSeqComposite)text).toCharArray();
      text = new CharSeqArray(array, 0, array.length);
      operationsCount = 0;
    }
  }

  public CharSequence subSequence(int start) {
    return text.subSequence(start, length());
  }

  public CharSequence subSequence(int start, int end) {
    return text.subSequence(start, end);
  }

  public int length() {
    return text.length();
  }

  public char charAt(int offset) {
    return text.charAt(offset);
  }

  public String toString() {
    return text.toString();
  }

  public CharSequence getText() {
    return text;
  }
}
