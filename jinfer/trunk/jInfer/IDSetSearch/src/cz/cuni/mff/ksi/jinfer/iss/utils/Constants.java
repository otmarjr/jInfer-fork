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
package cz.cuni.mff.ksi.jinfer.iss.utils;

import cz.cuni.mff.ksi.jinfer.iss.experiments.FileCharacteristics;
import cz.cuni.mff.ksi.jinfer.iss.experiments.InputFile;

/**
 * A few constants used throughout the project.
 *
 * @author vektor
 */
public final class Constants {

  private Constants() {

  }

  public static final int ITERATIONS = 10;

  public static final InputFile GRAPH = new InputFile("C:\\Users\\vitasek\\Documents\\Soukrome\\test-xml\\graph.xml", FileCharacteristics.ARTIFICIAL);

  public static final String TEST_DATA_ROOT = "C:\\Users\\vitasek\\Documents\\Soukrome\\test-data-official";

  public static final String TEST_OUTPUT_ROOT = "C:\\Users\\vitasek\\Documents\\Soukrome\\test-output";

}
