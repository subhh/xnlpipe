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

import digital.dehmel.code.xnlpipe.smx.Content;
import digital.dehmel.code.xnlpipe.smx.Comment;
import digital.dehmel.code.xnlpipe.smx.Document;
import digital.dehmel.code.xnlpipe.smx.Element;
import digital.dehmel.code.xnlpipe.smx.Node;
import digital.dehmel.code.xnlpipe.smx.ProcessingInstruction;

/**
 * Interface of a tree visitor.
 */
public interface Visitor
{
    default void visit (final Document document)
    {
    }

    default void visit (final Element element, final Content content)
    {
    }

    default void visit (final Comment comment, final Content content)
    {
    }

    default void visit (final ProcessingInstruction processingInstruction, final Content content)
    {
    }

    default void visit (final Node node, final Content content)
    {
    }
}
