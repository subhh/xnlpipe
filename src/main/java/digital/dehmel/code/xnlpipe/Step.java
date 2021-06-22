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

import java.util.HashMap;
import java.util.Map;

import digital.dehmel.code.xnlpipe.smx.Document;
import digital.dehmel.code.xnlpipe.smx.Insert;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import net.jcip.annotations.ThreadSafe;

/**
 * Runs the content of a document through the Stanford NLP and inserts
 * the resulting tokens back into the document.
 */
@ThreadSafe
final class Step
{
    private final StanfordCoreNLP pipeline;

    Step (final StanfordCoreNLP pipeline)
    {
        this.pipeline = pipeline;
    }

    void annotate (final Document document)
    {
        CoreDocument nlpDocument = new CoreDocument(document.getContent().getBuffer().toString());
        pipeline.annotate(nlpDocument);

        for (CoreLabel token : nlpDocument.tokens()) {
            Map<String, String> attributes = new HashMap<String, String>();
            attributes.put("lemma", token.lemma());
            attributes.put("type", token.ner());
            attributes.put("pos", token.tag());

            String name;
            if (".".equals(token.tag())) {
                name = "pc";
            } else {
                name = "w";
            }

            Insert insert = new Insert(document, name, attributes, token.beginPosition(), token.endPosition());
            insert.execute();
        }
    }
}
