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
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.jcip.annotations.ThreadSafe;

@ThreadSafe
final class FileVisitor extends SimpleFileVisitor<Path>
{
    private static final String filename = "transcript.xml";

    private final List<Path> files = Collections.synchronizedList(new ArrayList<Path>());

    List<Path> getFiles ()
    {
        return Collections.unmodifiableList(files);
    }

    @Override
    public FileVisitResult visitFile (final Path file, final BasicFileAttributes attrs) throws IOException
    {
        if (file.getFileName().endsWith(filename)) {
            files.add(file);
        }
        return FileVisitResult.CONTINUE;
    }
}
