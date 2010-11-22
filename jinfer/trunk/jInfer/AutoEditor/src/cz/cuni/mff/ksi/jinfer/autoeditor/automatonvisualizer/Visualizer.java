/*
 *  Copyright (C) 2010 rio
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

package cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer;

import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author rio
 */
public class Visualizer<T> extends VisualizationViewer<State<T>, Step<T>> {

  public Visualizer(final Layout<State<T>, Step<T>> layout) {
    super(layout);
  }

  // TODO rio verify format is supported
  public void saveImage(final File file, final String formatName) throws IOException {
    boolean isDoubleBuffered = isDoubleBuffered();
    setDoubleBuffered(false);

    final Dimension size = getSize();
    BufferedImage bi = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_BGR);
    Graphics2D graphics = bi.createGraphics();
    paintComponent(graphics);

    setDoubleBuffered(isDoubleBuffered);

    ImageIO.write(bi, formatName, file);
  }

  public void saveJPEG(final File file) throws IOException {
    saveImage(file, "jpg");
  }

  public void savePNG(final File file) throws IOException {
    saveImage(file, "png");
  }

}
