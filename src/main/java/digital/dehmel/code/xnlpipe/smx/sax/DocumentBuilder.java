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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import digital.dehmel.code.xnlpipe.smx.Document;

import net.jcip.annotations.ThreadSafe;

/**
 * Build document from SAX events.
 */
@ThreadSafe
public final class DocumentBuilder
{
    private final SAXParserFactory factory;

    public DocumentBuilder ()
    {
        factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
    }

    public Document parse (final InputStream source)
    {
        try {
            SAXParser parser = factory.newSAXParser();
            EventHandler events = new EventHandler();
            parser.getXMLReader().setProperty("http://xml.org/sax/properties/lexical-handler", events);
            parser.getXMLReader().setContentHandler(events);
            parser.getXMLReader().parse(new InputSource(source));
            return events.getDocument();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Unable to create SAX parser", e);
        } catch (SAXException e) {
            throw new RuntimeException("Invalid source document", e);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read source document", e);
        }
    }
}
