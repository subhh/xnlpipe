/*
 *
 * This file is part of XML NLP Pipe.
 *
 * XML NLP Pipe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * XML NLP Pipe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with XML NLP Pipe.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package digital.dehmel.code.xnlpipe.smx.sax;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.NamespaceSupport;

import digital.dehmel.code.xnlpipe.smx.Comment;
import digital.dehmel.code.xnlpipe.smx.Content;
import digital.dehmel.code.xnlpipe.smx.Document;
import digital.dehmel.code.xnlpipe.smx.Element;
import digital.dehmel.code.xnlpipe.smx.Node;
import digital.dehmel.code.xnlpipe.smx.ProcessingInstruction;
import net.jcip.annotations.NotThreadSafe;

/**
 * SAX event handler.
 */
@NotThreadSafe
final class EventHandler extends DefaultHandler2
{
    private static final String COLON = ":";

    private StringBuffer content;
    private Node markup;
    private Stack<Node> ancestors;
    private NamespaceSupport namespaces;
    private boolean needNewContext;

    Document getDocument ()
    {
        return new Document(markup, new Content(content));
    }

    public void startDocument () throws SAXException
    {
        markup = new Node(0);
        content = new StringBuffer();
        ancestors = new Stack<Node>();
        namespaces = new NamespaceSupport();
        needNewContext = false;
    }

    public void endDocument () throws SAXException
    {
        markup.setEnd(content.length());
    }

    public void startPrefixMapping (final String prefix, final String uri) throws SAXException
    {
        if (needNewContext) {
            namespaces.pushContext();
            needNewContext = false;
        }
        namespaces.declarePrefix(prefix, uri);
    }

    public void endPrefixMapping (final String prefix) throws SAXException
    {
        namespaces.popContext();
    }

    public void startElement (final String uri, final String localName, final String qName, final Attributes attrs)
    {
        ancestors.push(markup);
        String prefix;
        if (qName.contains(COLON)) {
            prefix = qName.substring(0, qName.indexOf(COLON));
        } else {
            prefix = "";
        }

        Map<String, String> attributes = null;
        if (attrs.getLength() > 0) {
            attributes = new HashMap<String, String>();
            for (int i = 0; i < attrs.getLength(); i++) {
                attributes.put(attrs.getQName(i), attrs.getValue(i));
            }
        }

        Map<String, String> nsDecls = new HashMap<String, String>();
        for (Enumeration<String> enumeration = namespaces.getDeclaredPrefixes(); enumeration.hasMoreElements();) {
            String pfx = (String)enumeration.nextElement();
            nsDecls.put(pfx, namespaces.getURI(pfx));
        }

        markup = new Element(content.length(), uri, localName, prefix, attributes, nsDecls);
        if (needNewContext) {
            namespaces.pushContext();
        }
        needNewContext = true;
    }

    public void endElement (final String uri, final String localName, final String qName) throws SAXException
    {
        markup.setEnd(content.length());
        ancestors.peek().append(markup);
        markup = ancestors.pop();
    }

    public void characters (final char[] text, final int start, final int length) throws SAXException
    {
        content.append(text, start, length);
    }

    public void comment (final char[] text, final int start, final int length) throws SAXException
    {
        markup.append(new Comment(content.length(), new String(text, start, length)));
    }

    public void processingInstruction (final String target, final String data) throws SAXException
    {
        markup.append(new ProcessingInstruction(0, target, data));
    }
}
