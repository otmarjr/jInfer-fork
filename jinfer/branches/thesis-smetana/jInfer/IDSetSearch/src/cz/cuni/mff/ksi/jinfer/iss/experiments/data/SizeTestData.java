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
 * A set of artificial data, random graphs. They should have approximately the
 * same density (~1/10 of all possible edges).
 *
 * @author vektor
 */
public enum SizeTestData implements TestData {

  GRAPH_0_0      ("0-0.xml",     0, 0,    0d),
  GRAPH_10_5     ("10-5.xml",    10, 5,   0.8500000000000002),
  GRAPH_20_20    ("20-20.xml",   18, 13,  0.7166666666666669),
  GRAPH_30_45    ("30-45.xml",   29, 43,  0.7083333333333334),
  GRAPH_40_80    ("40-80.xml",   39, 72,  0.6950000000000002),
  GRAPH_50_125   ("50-125.xml",  48, 111, 0.6566666666666666),
  GRAPH_60_180   ("60-180.xml",  58, 157, 0.6214285714285716),
  GRAPH_70_245   ("70-245.xml",  67, 205, 0.5982142857142856),
  GRAPH_80_320   ("80-320.xml",  76, 261, 0.5791666666666667),
  GRAPH_90_405   ("90-405.xml",  86, 352, 0.528888888888889),
  GRAPH_100_500  ("100-500.xml", 91, 388, 0.4981818181818182);

  // anything more takes 1800+ seconds...

  private final InputFile file;
  // Graph representation
  private final int vertices;
  private final int edges;
  // Known optimum for alpha = beta = 1
  private final Double optimum;

  private SizeTestData(final String fileName,
          final int vertices, final int edges, final Double optimum) {
    this.file = new InputFile(Constants.TEST_DATA_ROOT + "/artificial/size/" + fileName, ARTIFICIAL);
    this.vertices = vertices;
    this.edges = edges;
    this.optimum = optimum;
  }

  @Override
  public InputFile getFile() {
    return file;
  }

  @Override
  public int getVertices() {
    return vertices;
  }

  @Override
  public int getEdges() {
    return edges;
  }

  @Override
  public Double getKnownOptimum() {
    return optimum;
  }

}
