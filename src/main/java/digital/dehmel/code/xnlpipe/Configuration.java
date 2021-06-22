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

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.HelpFormatter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import net.jcip.annotations.ThreadSafe;

/**
 * CLI configuration.
 */
@ThreadSafe
final class Configuration
{
    private static final String OPTION_PROPERTIES_SHORT = "p";
    private static final String OPTION_DIRECTORY_SHORT = "d";

    private final DefaultParser parser = new DefaultParser();
    private final Options options = new Options();

    private CommandLine arguments;

    Configuration ()
    {
        options.addOption(OPTION_DIRECTORY_SHORT, "directory", true, "Path to data directory (default: .)");
        options.addRequiredOption(OPTION_PROPERTIES_SHORT, "properites", true, "Path to Stanford NLP configuration file");
    }

    void parse (final String[] args)
    {
        try {
            arguments = parser.parse(options, args);
            if (arguments.getArgList().size() > 0) {
                throw new ParseException("Excess arguments on command line");
            }
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            printHelp();
            throw new RuntimeException(e);
        }
    }

    Path getDataDirectory ()
    {
        if (arguments == null) {
            throw new IllegalStateException();
        }
        if (arguments.hasOption(OPTION_DIRECTORY_SHORT)) {
            return Paths.get(arguments.getOptionValue(OPTION_DIRECTORY_SHORT));
        }
        return Paths.get(".");
    }

    Properties getStanfordConfiguration () throws IOException
    {
        if (arguments == null) {
            throw new IllegalStateException();
        }
        FileInputStream ins = new FileInputStream(arguments.getOptionValue(OPTION_PROPERTIES_SHORT));
        Properties props = new Properties();
        props.load(ins);
        return props;
    }

    private void printHelp ()
    {
        System.out.println("jxnlpipe " + getVersion());
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("digital.dehmel.code.xnlpipe.Main", options, true);
    }

    private String getVersion ()
    {
        return getClass().getPackage().getImplementationVersion();
    }
}
