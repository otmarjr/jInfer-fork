/*
 *  Copyright (C) 2010 vektor
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.base.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import org.apache.log4j.Logger;

/**
 * Utilities for file handling.
 *
 * @author vektor
 */
public final class FileUtils {

  private FileUtils() {
  }

  private static final Logger LOG = Logger.getLogger(FileUtils.class);

  public static final String JINFER_DIR = System.getProperty("user.home") + "/.jinfer";

  /**
   * Returns the extension of the provided file.
   *
   * Extension is defined as the part behind the last position of dot (".") in the file name.
   */
  public static String getExtension(final String fileName) {
    return fileName.substring(fileName.lastIndexOf('.') + 1);
  }

  /**
   * Checks whether the provided path is a path to a valid executable binary
   * that can be run and produces expected result.
   *
   * @param pathToBinary Full path to the binary to be checked, including its
   * name.
   * @param cmdLineOpts Options to be passed to the binary (such as <code>-v</code>).
   * @param expectedFirstLine Expected beginning of the first line this binary
   * should produce. This is checked for <code>stdout<code> as well as
   * <code>stderr</code>, it is enough to find it in one of these places.
   * @param isEmptyValid Whether to accept an empty path to binary as valid.
   *
   * @return <code>True</code> if the specified path is either empty and that
   * is OK, or if the path points to an executable file that, after being
   * executed with provided options, produces the expected output.
   * <code>False<code> otherwise.
   */
  public static boolean isBinaryValid(
          final String pathToBinary,
          final String cmdLineOpts,
          final String expectedFirstLine,
          final boolean isEmptyValid) {
    if (isEmptyValid && "".equals(pathToBinary)) {
      return true;
    }

    final File binaryFile = new File(pathToBinary);
    if (!binaryFile.isFile() || !binaryFile.canExecute()) {
      LOG.warn("The path is not a file or it's not executable: " + pathToBinary);
      return false;
    }

    final ProcessBuilder pb = new ProcessBuilder(pathToBinary, cmdLineOpts);
    try {
      final Process process = pb.start();
      final BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      final BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
      final String stdOut = inputReader.readLine();
      final String errOut = errorReader.readLine();
      if ((stdOut != null && stdOut.startsWith(expectedFirstLine))
              || (errOut != null && errOut.startsWith(expectedFirstLine))) {
        inputReader.close();
        errorReader.close();
        return true;
      }
      LOG.warn("Actual output does not match expected for the file " + pathToBinary);
      return false;
    } catch (final IOException ex) {
      LOG.error("An exception occured while verifying the file " + pathToBinary, ex);
      return false;
    }
  }

  /**
   * Returns the first line of the stdout of the specified program run.
   *
   * @param pathToBinary Full path to the binary to be run, including its name.
   * @param cmdLineOpts Command line options to be passed to the binary.
   * @return First line of the standard output of this program.
   */
  public static String getBinaryResult(
          final String pathToBinary,
          final String cmdLineOpts) {
    final File binaryFile = new File(pathToBinary);
    if (!binaryFile.isFile() || !binaryFile.canExecute()) {
      LOG.warn("The path is not a file or it's not executable: " + pathToBinary);
      return null;
    }
    final ProcessBuilder pb = new ProcessBuilder(pathToBinary, cmdLineOpts);
    try {
      final Process process = pb.start();
      final BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      return inputReader.readLine();
    } catch (final IOException ex) {
      LOG.error("An exception occured while running the file " + pathToBinary, ex);
      return null;
    }
  }

  /**
   * Writes the specified string to the specified file. It tries to create all
   * the necessary folders on the way to the file.
   *
   * @param s String to be written.
   * @param f File to be written to.
   */
  public static void writeString(final String s, final File f) {
    assureParentsExist(f);
    PrintWriter pw = null;
    try {
      pw = new PrintWriter(f);
      pw.write(s);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    } finally {
      pw.close();
    }
  }

  /**
   * Appends the specified string to the specified file. It tries to create all
   * the necessary folders on the way to the file.
   *
   * @param s String to be appended.
   * @param f File to be written to.
   */
  public static void appendString(final String s, final File f) {
    assureParentsExist(f);
    try {
      final BufferedWriter out = new BufferedWriter(new FileWriter(f, true));
      out.write(s);
      out.close();
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void assureParentsExist(final File f) {
    final File parentDir = f.getParentFile();
    if (!parentDir.exists() || !parentDir.isDirectory()) {
      parentDir.mkdirs();
    }
  }


  /**
   * Reads the specified {@link Reader} and writes its content to the specified
   * {@link StringBuilder}.
   *
   * @param r Reader to read from.
   * @param sb String buffer to write to.
   * 
   * @throws InterruptedException If the thread is interrupted.
   * @throws IOException If there is a problem with reading.
   */
  public static void readerToBuilder(final Reader r, final StringBuilder sb)
          throws InterruptedException, IOException {
    final BufferedReader reader = new BufferedReader(r);

    String line = reader.readLine();
    while (line != null) {
      sb.append(line).append('\n');
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      line = reader.readLine();
    }
  }

  /**
   * Loads the specified resource file and returns it as String.
   *
   * @param resource Path to the Java resource.
   * @return String content of the resource file.
   *
   * @throws InterruptedException If the thread is interrupted.
   * @throws IOException If there is a problem with reading.
   */
  public static String loadTemplate(final String resource)
          throws IOException, InterruptedException {
    final InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
    final StringBuilder ret = new StringBuilder();
    readerToBuilder(new InputStreamReader(is), ret);
    return ret.toString();
  }
}
