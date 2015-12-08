package com.spbsu.xml;

import com.spbsu.commons.seq.CharSeqBuilderReader;

import java.io.IOException;
import java.io.Reader;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedTransferQueue;

/**
 * User: solar
 * Date: 04.12.15
 * Time: 16:09
 */
public class XmlReader {
  private final CharSeqBuilderReader seq;
  private Queue<XmlElement> parsed = new LinkedTransferQueue<>();
  private Stack<XmlElement> deep = new Stack<>();

  public XmlReader(Reader delegate) {
    seq = new CharSeqBuilderReader(delegate);
  }

  public boolean ready() throws IOException {
    return !parsed.isEmpty() || drainDelegate();
  }

  private boolean drainDelegate() throws IOException {
    return false;
  }

  public XmlElement next() throws IOException {
    if (parsed.isEmpty())
      drainDelegate();
    return parsed.poll();
  }

  public void deepen(XmlTag tag) {
    deep.push(tag);
  }
}
