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

import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.transformers.NodeColorTransformer;
import cz.cuni.mff.ksi.jinfer.autoeditor.options.ColorUtils;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;

/**
 * Base class for visualization of {@link Automaton} extending JUNG
 * {@link VisualizationViewer} with support of saving as images.
 *
 * This class does not handle layout of vertices. It is provided by {@link Layout},
 * instance of which has to be specified in construction.
 *
 * @author rio
 */
public class Visualizer<T> extends VisualizationViewer<State<T>, Step<T>> {

  private static final long serialVersionUID = 38097345;

  private final Set<String> supportedImageFormatNames;
  
  /**
   * Constructs instance with specified {@link Layout}.
   *
   * @param layout Layout instance.
   */
  public Visualizer(final Layout<State<T>, Step<T>> layout) {
    super(layout);
    supportedImageFormatNames = new HashSet<String>();
    for (final String imageFormatName : ImageIO.getWriterFormatNames()) {
      supportedImageFormatNames.add(imageFormatName);
    }

    setBackground(ColorUtils.getBackgroundColor());
    getRenderContext().setVertexFillPaintTransformer(new NodeColorTransformer<T>(getPickedVertexState()));
  }

  /**
   * Saves this visual representation as image and store it in file.
   *
   * @param file File to store the image.
   * @param formatName Name of format of the image. See {@link #getSupportedImageFormatNames()}.
   * @throws IOException Saving to file failed.
   */
  public void saveImage(final File file, final String formatName) throws IOException {
    if (!supportedImageFormatNames.contains(formatName)) {
      throw new IllegalArgumentException("Image format '" + formatName + "' is not suported");
    }

    final boolean isDoubleBuffered = isDoubleBuffered();
    setDoubleBuffered(false);

    final Dimension size = getSize();
    final BufferedImage bi = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_BGR);
    final Graphics2D graphics = bi.createGraphics();
    paintComponent(graphics);

    setDoubleBuffered(isDoubleBuffered);

    ImageIO.write(bi, formatName, file);
  }

  /**
   * Retrieves set of all image format names supported by {@link #saveImage(java.io.File, java.lang.String)}.
   */
  public Set<String> getSupportedImageFormatNames() {
    return supportedImageFormatNames;
  }
}
