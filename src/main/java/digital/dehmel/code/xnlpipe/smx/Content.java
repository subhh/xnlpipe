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

import net.jcip.annotations.ThreadSafe;

/**
 * Document content.
 */
@ThreadSafe
public final class Content
{
    private final StringBuffer buffer;
    private int cursor;

    public Content (final StringBuffer buffer)
    {
        this.buffer = buffer;
    }

    public StringBuffer getBuffer ()
    {
        return buffer;
    }

    public synchronized int getCursor ()
    {
        return cursor;
    }

    public synchronized void setCursor (final int cursor)
    {
        this.cursor = cursor;
    }
}
