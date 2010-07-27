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
package cz.cuni.mff.ksi.jinfer.validator;

public class Remark {

  private String module;
  private String file;
  private Integer line;
  private Severity severity;

  public Remark(final String module,
          final String file, final Integer line, final Severity severity,
          final String text) {
    this.module = module;
    this.file = file;
    this.line = line;
    this.severity = severity;
    this.text = text;
  }
  private String text;

  public static Remark getError(final String s) {
    return new Remark(null, null, null, Severity.ERROR, s);
  }

  public static Remark getWarning(final String s) {
    return new Remark(null, null, null, Severity.WARNING, s);
  }

  public String getModule() {
    return module;
  }

  public void setModule(final String module) {
    this.module = module;
  }

  public String getFile() {
    return file;
  }

  public void setFile(final String file) {
    this.file = file;
  }

  public Integer getLine() {
    return line;
  }

  public void setLine(final Integer line) {
    this.line = line;
  }

  public Severity getSeverity() {
    return severity;
  }

  public void setSeverity(final Severity severity) {
    this.severity = severity;
  }

  public String getText() {
    return text;
  }

  public void setText(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    final StringBuilder ret = new StringBuilder();
    switch (severity) {
      case WARNING:
        ret.append('W');
        break;
      case ERROR:
        ret.append('E');
        break;
      default:
        throw new IllegalArgumentException();
    }
    if (module != null) {
      ret.append('[').append(module).append(']');
    }
    if (file != null) {
      ret.append(' ').append(file);
    }
    if (line != null) {
      ret.append(':').append(line.toString());
    }
    if (text != null) {
      ret.append(' ').append(text);
    }
    return ret.toString();
  }
}
