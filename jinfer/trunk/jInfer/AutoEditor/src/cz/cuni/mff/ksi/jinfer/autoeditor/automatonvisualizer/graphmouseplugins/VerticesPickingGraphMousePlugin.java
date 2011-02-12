/*
 *  Copyright (C) 2010 sviro
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

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationServer.Paintable;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractGraphMousePlugin;
import edu.uci.ics.jung.visualization.picking.PickedState;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import javax.swing.JComponent;

/**
 * Graph mouse plugin providing picking of vertices.
 *
 * @author sviro
 */
public class VerticesPickingGraphMousePlugin<V, E> extends AbstractGraphMousePlugin
        implements MouseListener, MouseMotionListener {

  /**
   * the picked Vertex, if any
   */
  protected V vertex;
  /**
   * the picked Edge, if any
   */
  protected E edge;
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
  /**
   * used to draw a rectangle to contain picked vertices
   */
  protected Rectangle2D rect = new Rectangle2D.Float();
  /**
   * the Paintable for the lens picking rectangle
   */
  protected Paintable lensPaintable;
  /**
   * color for the picking rectangle
   */
  protected Color lensColor = Color.cyan;
  private boolean firstPick = true;

  /**
   * Create an instance with default settings.
   */
  public VerticesPickingGraphMousePlugin() {
    super(InputEvent.BUTTON1_MASK);
    this.lensPaintable = new LensPaintable();
    this.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
  }

  /**
   * @return Returns the lensColor.
   */
  public Color getLensColor() {
    return lensColor;
  }

  /**
   * @param lensColor The lensColor to set.
   */
  public void setLensColor(final Color lensColor) {
    this.lensColor = lensColor;
  }

  /**
   * a Paintable to draw the rectangle used to pick multiple
   * Vertices
   * @author Tom Nelson, sviro
   *
   */
  class LensPaintable implements Paintable {

    @Override
    public void paint(final Graphics g) {
      final Color oldColor = g.getColor();
      g.setColor(lensColor);
      ((Graphics2D) g).draw(rect);
      g.setColor(oldColor);
    }

    @Override
    public boolean useTransform() {
      return false;
    }
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
    final VisualizationViewer<V, E> vv = (VisualizationViewer<V, E>) e.getSource();
    final GraphElementAccessor<V, E> pickSupport = vv.getPickSupport();
    final PickedState<V> pickedVertexState = vv.getPickedVertexState();

    if (pickSupport == null || pickedVertexState == null) {
      return;
    }

    if (e.getModifiers() == modifiers) {
      if (!firstPick) {
        vv.addPostRenderPaintable(lensPaintable);
      }
      rect.setFrameFromDiagonal(down, down);
      // p is the screen point for the mouse event
      final Point2D ip = e.getPoint();

      final Layout<V, E> layout = vv.getGraphLayout();
      vertex = pickSupport.getVertex(layout, ip.getX(), ip.getY());

      if (vertex == null) {
        vv.addPostRenderPaintable(lensPaintable);
        pickedVertexState.clear();
        firstPick = true;
        return;
      }

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
      } else {
        final boolean wasThere = pickedVertexState.pick(vertex, !pickedVertexState.isPicked(
                vertex));
        if (wasThere) {
          vertex = null;
          return;
        } else {

          // layout.getLocation applies the layout transformer so
          // q is transformed by the layout transformer only
          final Point2D q = layout.transform(vertex);
          // translate mouse point to graph coord system
          final Point2D gp = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(
                  Layer.LAYOUT, ip);

          offsetx = (float) (gp.getX() - q.getX());
          offsety = (float) (gp.getY() - q.getY());
        }
      }
    }

    e.consume();
  }

  /**
   * If the mouse is dragging a rectangle, pick the
   * Vertices contained in that rectangle
   *
   * clean up settings from mousePressed
   */
  @SuppressWarnings("unchecked")
  @Override
  public void mouseReleased(final MouseEvent e) {
    final VisualizationViewer<V, E> vv = (VisualizationViewer<V, E>) e.getSource();
    if (e.getModifiers() == modifiers && down != null) {
      final Point2D out = e.getPoint();

      if (vertex == null && !heyThatsTooClose(down, out, 5)) {
        pickContainedVertices(vv, down, out, false);
      }
    }
    down = null;
    vertex = null;
    edge = null;
    rect.setFrame(0, 0, 0, 0);
    vv.removePostRenderPaintable(lensPaintable);
    vv.repaint();
  }

  /**
   * If the mouse is over a picked vertex, drag all picked
   * vertices with the mouse.
   * If the mouse is not over a Vertex, draw the rectangle
   * to select multiple Vertices
   *
   */
  @SuppressWarnings("unchecked")
  @Override
  public void mouseDragged(final MouseEvent e) {
    if (!locked) {
      final VisualizationViewer<V, E> vv = (VisualizationViewer<V, E>) e.getSource();
      if (vertex != null) {
        final Point p = e.getPoint();

        final PickedState<V> ps = vv.getPickedVertexState();

        for (V v : ps.getPicked()) {
          final Point2D graphPoint = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(
                p);
          final Point2D graphDown = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(
                down);
          final Layout<V, E> layout = vv.getGraphLayout();
          final double dx = graphPoint.getX() - graphDown.getX();
          final double dy = graphPoint.getY() - graphDown.getY();

          final Point2D vp = layout.transform(v);
          vp.setLocation(vp.getX() + dx, vp.getY() + dy);
          layout.setLocation(v, vp);
        }
        down = p;

      } else {
        if (e.getModifiers() == modifiers) {
          final Point2D out = e.getPoint();
          rect.setFrameFromDiagonal(down, out);
        }
      }
      if (vertex != null) {
        e.consume();
      }
      vv.repaint();
    }
  }

  /**
   * rejects picking if the rectangle is too small, like
   * if the user meant to select one vertex but moved the
   * mouse slightly
   * @param p
   * @param q
   * @param min
   * @return
   */
  private boolean heyThatsTooClose(final Point2D p, final Point2D q, final double min) {
    return Math.abs(p.getX() - q.getX()) < min
            && Math.abs(p.getY() - q.getY()) < min;
  }

  /**
   * pick the vertices inside the rectangle created from points
   * 'down' and 'out'
   *
   */
  protected void pickContainedVertices(final VisualizationViewer<V, E> vv, final Point2D down,
          final Point2D out,
          final boolean clear) {

    final PickedState<V> pickedVertexState = vv.getPickedVertexState();

    final Rectangle2D pickRectangle = new Rectangle2D.Double();
    pickRectangle.setFrameFromDiagonal(down, out);

    if (pickedVertexState != null) {
      if (clear) {
        pickedVertexState.clear();
      }
      final GraphElementAccessor<V, E> pickSupport = vv.getPickSupport();

      final Layout<V, E> layout = vv.getGraphLayout();
      final Collection<V> picked = pickSupport.getVertices(layout, pickRectangle);
      for (V v : picked) {
        pickedVertexState.pick(v, true);
      }
    }
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

  /**
   * @return Returns the locked.
   */
  public boolean isLocked() {
    return locked;
  }

  /**
   * @param locked The locked to set.
   */
  public void setLocked(final boolean locked) {
    this.locked = locked;
  }
}
