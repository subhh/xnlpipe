XML NLP Pipeline
=

The XML NLP Pipeline is a Java command line application that integrates the Stanford CoreNLP pipeline (Manning et
al. 2014) in an XML-based processing pipeline. It uses a simplified version of the Separated Markup API for XML (SMAX)
by Nico Verwer (Verwer 2020) to patch the annotated tokens back to the XML document, preserving all previous
annotations.

Usage
-

The commandline application takes two parameters: The path to a properties file with the Stanford CoreNLP configuration
and the data directory.

```
java -jar /path/to/xnlpipe.jar -p </path/to/properties> -d </path/to/data/directory>
```

The application recursively walks the data directory, runs the Stanford CoreNLP pipeline for every file named
`transcript.xml` and stores the result in a file `content.xml` in the same directory.

License
-

Copyright (c) 2021 by Staats- und Universitätsbibliothek Hamburg and released under the terms of the GNU General Public License v3.

Authors
-

David Maus &lt;david.maus@sub.uni-hamburg.de&gt;

Bibliography
-

Imsieke, Gerrit. 2018. “Tokenized-to-Tree: An XProc/XSLT Library For Patching Back Tokenization/Analysis Results Into
Marked-up Text.” In XML Prague 2018 Conference Proceedings, 229–45. Prague, Czech Republic.

Manning, Christopher D., Mihai Surdeanu, John Bauer, Jenny Finkel, Steven J. Bethard, and David McClosky. 2014. “The
Stanford CoreNLP Natural Language Processing Toolkit.” In Proceedings of the 52nd Annual Meeting of the Association for
Computational Linguistics: System Demonstrations, 55–60. http://www.aclweb.org/anthology/P/P14/P14-5010.

Verwer, Nico. “Plain Text Processingin Structured Documents.” In Proceedings of Declarative Amsterdam 2020. CWI,
Amsterdam: John Benjamins, 2020. https://doi.org/10.1075/da.2020.verwer.plain-text-processing.

