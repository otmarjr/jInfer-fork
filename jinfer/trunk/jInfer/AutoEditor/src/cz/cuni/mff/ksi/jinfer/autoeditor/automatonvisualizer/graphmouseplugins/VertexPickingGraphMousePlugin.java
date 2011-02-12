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
package cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.graphmouseplugins;

import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.Visualizer;
import cz.cuni.mff.ksi.jinfer.autoeditor.gui.component.AbstractComponent;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractGraphMousePlugin;
import edu.uci.ics.jung.visualization.picking.PickedState;
import java.awt.Cursor;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import javax.swing.JComponent;

/**
 * Graph mouse plugin providing picking of one vertex and notifying
 * {@link AbstractComponent} to end user interaction.
 *
 * Modified {@link VerticesPickingGraphMousePlugin} originally by sviro.
 *
 * @author rio
 */
public class VertexPickingGraphMousePlugin<V> extends AbstractGraphMousePlugin
        implements MouseListener, MouseMotionListener {

  /**
   * component that has to be notified that a vertex was picked by calling
   * its GUIDone method.
   */
  protected AbstractComponent<V> component;
  /**
   * the picked Vertex, if any
   */
  protected State<V> vertex;
  /**
   * the picked Edge, if any
   */
  protected Step<V> edge;
  /**
   * the x distance from the picked vertex center to the mouse point
   */
  protected double offsetx;
  /**
   * the y distance from the picked vertex center to the mouse point
   */
  protected double offsety;
  /**
   * controls whether the Vertices may be moved with the mouse
   */
  protected boolean locked;
  private boolean firstPick = true;

  /**
   * Create an instance with default settings.
   */
  public VertexPickingGraphMousePlugin(final AbstractComponent<V> component) {
    super(InputEvent.BUTTON1_MASK);
    this.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    this.component = component;
  }

  /**
   * For primary modifiers (default, MouseButton1):
   * pick a single Vertex or Edge that
   * is under the mouse pointer. If no Vertex or edge is under
   * the pointer, unselect all picked Vertices and edges, and
   * set up to draw a rectangle for multiple selection
   * of contained Vertices.
   * For additional selection (default Shift+MouseButton1):
   * Add to the selection, a single Vertex or Edge that is
   * under the mouse pointer. If a previously picked Vertex
   * or Edge is under the pointer, it is un-picked.
   * If no vertex or Edge is under the pointer, set up
   * to draw a multiple selection rectangle (as above)
   * but do not unpick previously picked elements.
   *
   * @param e the event
   */
  @SuppressWarnings("unchecked")
  @Override
  public void mousePressed(final MouseEvent e) {
    down = e.getPoint();
    final Visualizer<V> vv = (Visualizer<V>) e.getSource();
    final GraphElementAccessor<State<V>, Step<V>> pickSupport = vv.getPickSupport();
    final PickedState<State<V>> pickedVertexState = vv.getPickedVertexState();

    if (pickSupport == null || pickedVertexState == null) {
      return;
    }

    if (e.getModifiers() == modifiers) {
      // p is the screen point for the mouse event
      final Point2D ip = e.getPoint();

      final Layout<State<V>, Step<V>> layout = vv.getGraphLayout();
      vertex = pickSupport.getVertex(layout, ip.getX(), ip.getY());
      if (vertex != null) {
        if (firstPick) {
          firstPick = false;

          if (!pickedVertexState.isPicked(vertex)) {
            pickedVertexState.clear();
            pickedVertexState.pick(vertex, true);
          }
          // layout.getLocation applies the layout transformer so
          // q is transformed by the layout transformer only
          final Point2D q = layout.transform(vertex);
          // transform the mouse point to graph coordinate system
          final Point2D gp = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(
                  Layer.LAYOUT, ip);

          offsetx = (float) (gp.getX() - q.getX());
          offsety = (float) (gp.getY() - q.getY());
          component.guiDone();
        } else {
          final boolean wasThere = pickedVertexState.pick(vertex, !pickedVertexState.isPicked(
                  vertex));
          if (wasThere) {
            vertex = null;
          } else {

            // layout.getLocation applies the layout transformer so
            // q is transformed by the layout transformer only
            final Point2D q = layout.transform(vertex);
            // translate mouse point to graph coord system
            final Point2D gp = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(
                    Layer.LAYOUT, ip);

            offsetx = (float) (gp.getX() - q.getX());
            offsety = (float) (gp.getY() - q.getY());
            component.guiDone();
          }
        }
      } else {
        pickedVertexState.clear();
        firstPick = true;
      }

    }

    if (vertex != null) {
      e.consume();
    }
  }

  /**
   * clean up settings from mousePressed
   */
  @SuppressWarnings("unchecked")
  @Override
  public void mouseReleased(final MouseEvent e) {
    final VisualizationViewer<State<V>, Step<V>> vv = (VisualizationViewer<State<V>, Step<V>>) e.getSource();
    down = null;
    vertex = null;
    edge = null;
    vv.repaint();
  }

  @SuppressWarnings("unchecked")
  @Override
  public void mouseDragged(final MouseEvent e) {
    //
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    //
  }

  @Override
  public void mouseEntered(final MouseEvent e) {
    final JComponent c = (JComponent) e.getSource();
    c.setCursor(cursor);
  }

  @Override
  public void mouseExited(final MouseEvent e) {
    final JComponent c = (JComponent) e.getSource();
    c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
  }

  @Override
  public void mouseMoved(final MouseEvent e) {
    //
  }
}
