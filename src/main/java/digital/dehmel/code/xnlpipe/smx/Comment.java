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

import digital.dehmel.code.xnlpipe.smx.visitor.Visitor;

import net.jcip.annotations.ThreadSafe;

/**
 * A comment node.
 */
@ThreadSafe
public final class Comment extends Node
{
    private final String data;

    public Comment (final int start, final String data)
    {
        super(start);
        this.data = data;
    }

    public String getData ()
    {
        return data;
    }

    @Override
    public void accept (final Visitor visitor, final Content content)
    {
        visitor.visit(this, content);
    }
}
