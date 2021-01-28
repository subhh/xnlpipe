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

package digital.dehmel.code.xnlpipe.smx.visitor;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import digital.dehmel.code.xnlpipe.smx.Content;
import digital.dehmel.code.xnlpipe.smx.Comment;
import digital.dehmel.code.xnlpipe.smx.Document;
import digital.dehmel.code.xnlpipe.smx.Element;
import digital.dehmel.code.xnlpipe.smx.Node;
import digital.dehmel.code.xnlpipe.smx.ProcessingInstruction;

import net.jcip.annotations.NotThreadSafe;

/**
 * Serialize document.
 */
@NotThreadSafe
public final class StreamWriter implements Visitor
{

    private static final String ERROR = "Error writing result document";
    private final XMLStreamWriter writer;

    public StreamWriter (final XMLStreamWriter writer)
    {
        this.writer = writer;
    }

    @Override
    public void visit (final Document document)
    {
        try {
            writer.writeStartDocument();
            document.getMarkup().accept(this, document.getContent());
            writer.writeEndDocument();
        } catch (XMLStreamException e) {
            throw new RuntimeException(ERROR, e);
        }
    }

    @Override
    public void visit (final Element element, final Content content)
    {
        try {
            if (element.getStart() > content.getCursor()) {
                writeCharacters(content.getBuffer(), content.getCursor(), element.getStart());
                content.setCursor(element.getStart());
            }

            writer.writeStartElement(element.getPrefix(), element.getLocalName(), element.getNamespaceUri());

            Map<String, String> namespaces = element.getNamespaces();
            synchronized (namespaces) {
                for (Entry<String, String> entry : namespaces.entrySet()) {
                    writer.writeNamespace(entry.getKey(), entry.getValue());
                }
            }

            Map<String, String> attributes = element.getAttributes();
            synchronized (attributes) {
                for (Entry<String, String> entry : attributes.entrySet()) {
                    writer.writeAttribute(entry.getKey(), entry.getValue());
                }
            }

            List<Node> children = element.getChildren();
            synchronized (children) {
                for (Node node : children) {
                    node.accept(this, content);
                }
            }

            if (element.getEnd() > content.getCursor()) {
                writeCharacters(content.getBuffer(), content.getCursor(), element.getEnd());
                content.setCursor(element.getEnd());
            }
            writer.writeEndElement();

        } catch (XMLStreamException e) {
            throw new RuntimeException(ERROR, e);
        }
    }

    @Override
    public void visit (final Comment comment, final Content content)
    {
        try {
            writer.writeComment(comment.getData());
        } catch (XMLStreamException e) {
            throw new RuntimeException(ERROR, e);
        }
    }

    @Override
    public void visit (final ProcessingInstruction processingInstruction, final Content content)
    {
        try {
            writer.writeProcessingInstruction(processingInstruction.getTarget(), processingInstruction.getData());
        } catch (XMLStreamException e) {
            throw new RuntimeException(ERROR, e);
        }
    }

    @Override
    public void visit (final Node node, final Content content)
    {
        List<Node> children = node.getChildren();
        synchronized (children) {
            for (Node child : children) {
                child.accept(this, content);
            }
        }
    }

    private void writeCharacters (final StringBuffer buffer, final int start, final int end) throws XMLStreamException
    {
        char[] text = new char[end - start];
        buffer.getChars(start, end, text, 0);
        writer.writeCharacters(text, 0, end - start);
    }
}
