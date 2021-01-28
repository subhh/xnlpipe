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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import digital.dehmel.code.xnlpipe.smx.visitor.Visitor;

import net.jcip.annotations.ThreadSafe;

/**
 * Base class.
 */
@ThreadSafe
public class Node
{
    private final List<Node> children = Collections.synchronizedList(new ArrayList<Node>());
    private final int start;
    private int end;

    public Node (final int start)
    {
        this.start = start;
    }

    public final synchronized void setEnd (final int end)
    {
        this.end = end;
    }

    public final synchronized int getEnd ()
    {
        return end;
    }

    public final int getStart ()
    {
        return start;
    }

    public final boolean hasChildren ()
    {
        if (children.isEmpty()) {
            return false;
        }
        return true;
    }

    public final List<Node> getChildren ()
    {
        return Collections.unmodifiableList(children);
    }

    public final void add (final Node node, final int position)
    {
        children.add(position, node);
    }

    public final void append (final Node node)
    {
        children.add(node);
    }

    public final void prepend (final Node node)
    {
        children.add(0, node);
    }

    public final void wrap (final Node node, final int first, final int last)
    {
        synchronized (children) {
            List<Node> wrapped = children.subList(first, 1 + last);
            for (Node wrap : wrapped) {
                node.append(wrap);
            }
            children.removeAll(wrapped);
            children.add(first, node);
        }
    }

    /**
     * Subclasses override this method to call type specific visitor
     * function.
     */
    public void accept (final Visitor visitor, final Content content)
    {
        visitor.visit(this, content);
    }
}
