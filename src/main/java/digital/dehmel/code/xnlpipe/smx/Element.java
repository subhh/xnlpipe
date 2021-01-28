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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import digital.dehmel.code.xnlpipe.smx.visitor.Visitor;
import net.jcip.annotations.ThreadSafe;

/**
 * An element node.
 */
@ThreadSafe
public final class Element extends Node
{
    private final Map<String, String> namespaces;
    private final Map<String, String> attributes;
    private final String namespaceUri;
    private final String localName;
    private final String prefix;

    public Element (final int start, final String namespaceUri, final String localName, final String prefix, Map<String, String> attributes, Map<String, String> namespaces)
    {
        super(start);
        this.namespaceUri = namespaceUri;
        this.localName = localName;
        this.prefix = prefix;
        if (namespaces == null) {
            this.namespaces = Collections.emptyMap();
        } else {
            // todo: Check if we need to synchronize on the attributes argument here.
            this.namespaces = Collections.synchronizedMap(new HashMap<String, String>(namespaces.size()));
            this.namespaces.putAll(namespaces);
        }
        if (attributes == null) {
            this.attributes = Collections.emptyMap();
        } else {
            // todo: Check if we need to synchronize on the attributes
            // argument here.
            this.attributes = Collections.synchronizedMap(new HashMap<String, String>(attributes.size()));
            this.attributes.putAll(attributes);
        }
    }

    public Map<String, String> getAttributes ()
    {
        return Collections.unmodifiableMap(attributes);
    }

    public Map<String, String> getNamespaces ()
    {
        return Collections.unmodifiableMap(namespaces);
    }

    public String getNamespaceUri ()
    {
        return namespaceUri;
    }

    public String getLocalName ()
    {
        return localName;
    }

    public String getPrefix ()
    {
        return prefix;
    }

    public boolean isEmpty ()
    {
        if (!hasChildren() && namespaces.isEmpty() && attributes.isEmpty() && getStart() == getEnd()) {
            return true;
        }
        return false;
    }

    @Override
    public void accept (final Visitor visitor, final Content content)
    {
        visitor.visit(this, content);
    }
}
