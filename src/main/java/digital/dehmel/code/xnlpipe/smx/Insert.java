/*
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

package digital.dehmel.code.xnlpipe.smx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.jcip.annotations.ThreadSafe;

/**
 * Insert markup annotation.
 */
@ThreadSafe
public final class Insert
{
    private static final Logger log = LoggerFactory.getLogger(Insert.class);

    private final Document document;
    private final String localName;
    private final Map<String, String> attributes;
    private final int start;
    private final int end;

    public Insert (final Document document, final String localName, final Map<String, String> attributes, final int start, final int end)
    {
        this.localName = localName;
        this.document = document;
        this.start = start;
        this.end = end;
        if (attributes == null) {
            this.attributes = Collections.emptyMap();
        } else {
            this.attributes = Collections.synchronizedMap(new HashMap<String, String>());
            this.attributes.putAll(attributes);
        }
    }

    public void execute ()
    {
        Node containingStart = findElementContainingStart(document.getMarkup());
        Node containingEnd = findElementContainingEnd(document.getMarkup());
        Node containing = balance(containingStart, containingEnd);

        Element element = new Element(start, "", localName, "", attributes, null);
        element.setEnd(end);

        if (containing.hasChildren()) {
            List<Node> children = containing.getChildren();
            int left = -1;
            int wrapLeft = -1;
            int wrapRight = -1;
            synchronized (children) {
                for (int i = 0; i < children.size(); i++) {
                    Node child = children.get(i);
                    if (child.getEnd() <= element.getStart()) {
                        left = i;
                    }
                    if (child.getStart() >= start && child.getEnd() <= end) {
                        if (wrapLeft == -1) {
                            wrapLeft = i;
                        }
                        if (wrapRight < i) {
                            wrapRight = i;
                        }
                    }
                }
            }
            if (wrapLeft == -1) {
                if (left == -1) {
                    containing.prepend(element);
                } else {
                    containing.add(element, 1 + left);
                }
            } else {
                containing.wrap(element, wrapLeft, wrapRight);
            }
        } else {
            containing.append(element);
        }
    }

    private Node balance (final Node containingStart, final Node containingEnd)
    {
        if (containingStart.equals(containingEnd)) {
            return containingStart;
        }
        String message = String.format("Unable to balance insert (%s, %d, %d) to (%s, %d, %d)",
                                       containingStart, containingStart.getStart(), containingStart.getEnd(),
                                       containingEnd, containingEnd.getStart(), containingEnd.getEnd());
        throw new RuntimeException(message);
    }

    private Element findElementContainingStart (final Node node)
    {
        List<Node> children = node.getChildren();
        synchronized (children) {
            for (Node child : children) {
                if (child instanceof Element && child.getStart() <= start && child.getEnd() > start) {
                    return findElementContainingStart(child);
                }
            }
        }
        if (node instanceof Element) {
            return (Element)node;
        }
        return null;
    }

    private Element findElementContainingEnd (final Node node)
    {
        List<Node> children = node.getChildren();
        synchronized (children) {
            for (Node child : children) {
                if (child instanceof Element && child.getStart() <= end && child.getEnd() >= end) {
                    return findElementContainingEnd(child);
                }
            }
        }
        if (node instanceof Element) {
            return (Element)node;
        }
        return null;
    }
}
