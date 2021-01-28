/*
 * This file is part of XML NLP Pipeline.
 *
 * XML NLP Pipeline is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * XML NLP Pipeline is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with XML NLP Pipeline.  If not, see
 * <https://www.gnu.org/licenses/>.
 *
 */

package digital.dehmel.code.xnlpipe;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import digital.dehmel.code.xnlpipe.smx.Document;
import digital.dehmel.code.xnlpipe.smx.sax.DocumentBuilder;
import digital.dehmel.code.xnlpipe.smx.visitor.StreamWriter;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import net.jcip.annotations.ThreadSafe;

/**
 * CLI application.
 */
@ThreadSafe
public abstract class Main
{
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static final void main (final String[] args) throws Exception
    {
        Configuration configuration = new Configuration();
        configuration.parse(args);

        Path directory = configuration.getDataDirectory();
        List<Path> files = getFiles(directory);

        Properties props = configuration.getStanfordConfiguration();
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Step step = new Step(pipeline);
        DocumentBuilder builder = new DocumentBuilder();

        XMLOutputFactory out = XMLOutputFactory.newInstance();

        synchronized (files) {
            for (Path file : files) {
                log.info("Annotating document {}", file);
                Document document = builder.parse(Files.newInputStream(file));
                step.annotate(document);

                XMLStreamWriter writer = out.createXMLStreamWriter(createOutputStream(file));
                StreamWriter visitor = new StreamWriter(writer);
                visitor.visit(document);
            }
        }
    }

    private static final OutputStream createOutputStream (final Path infile) throws IOException
    {
        Path outfile = infile.getParent().resolve("content.xml");
        return Files.newOutputStream(outfile);
    }

    private static final List<Path> getFiles (final Path directory) throws IOException
    {
        FileVisitor visitor = new FileVisitor();
        Files.walkFileTree(directory, visitor);
        return visitor.getFiles();
    }
}
