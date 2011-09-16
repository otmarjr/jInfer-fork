/*
 * Copyright (C) 2011 vektor
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.iss.experiments.data;

import cz.cuni.mff.ksi.jinfer.iss.utils.Constants;
import static cz.cuni.mff.ksi.jinfer.iss.experiments.data.FileCharacteristics.*;

/**
 * Enum listing all the official test data files.
 *
 * @author vektor
 */
public enum OfficialTestData implements TestData {

  OVA1           ("OVA1.xml",     REALISTIC, 29, 43,   0.45588235294117635),
  OVA2           ("OVA2.xml",     REALISTIC, 23, 36,   0.1634615384615385),
  OVA3           ("OVA3.xml",     REALISTIC, 31, 47,   0.25537156151635415),
  XMAc           ("XMA-c.xml",    REALISTIC, 1, 0,     0.7546666666666666),
  XMAp           ("XMA-p.xml",    REALISTIC, 1, 0,     0.2019306150568969),
  XMD            ("XMD.xml",      REALISTIC, 17, 15,   0.09786094165493507),

  MSH            ("MSH.xml",      CONVERTED, 1, 0,     0.5416472778036296),
  NTH            ("NTH.xml",      CONVERTED, 5, 7,     0.057918595422124436),

  GRAPH_100_100  ("100-100.xml",  ARTIFICIAL, 99, 95,  0.836666666666667),
  GRAPH_100_200  ("100-200.xml",  ARTIFICIAL, 96, 174, 0.7260000000000004),
  GRAPH_100_1000 ("100-1000.xml", ARTIFICIAL, 93, 754, 0.380952380952381);

  private final InputFile file;
  // Graph representation
  private final int vertices;
  private final int edges;
  // Known optimum for alpha = beta = 1
  private final Double optimum;

  private OfficialTestData(final String fileName,
          final FileCharacteristics characteristics,
          final int vertices, final int edges, final Double optimum) {
    this.file = new InputFile(Constants.TEST_DATA_ROOT + "/" + characteristics.getFolder() + "/" + fileName,
            characteristics);
    this.vertices = vertices;
    this.edges = edges;
    this.optimum = optimum;
  }

  public InputFile getFile() {
    return file;
  }

  public int getVertices() {
    return vertices;
  }

  public int getEdges() {
    return edges;
  }

  public Double getKnownOptimum() {
    return optimum;
  }

}
