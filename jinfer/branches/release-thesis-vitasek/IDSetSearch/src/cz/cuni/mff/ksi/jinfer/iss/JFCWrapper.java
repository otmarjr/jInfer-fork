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
package cz.cuni.mff.ksi.jinfer.iss;

import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeTreeNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 * Library class encapsulating the JFreeChart logic.
 *
 * @author vektor
 */
public final class JFCWrapper {

  private JFCWrapper() {
  }

  /**
   * Creates a {@link JPanel} containing a pie chart describing the content of
   * the provided {@link Attribute}s.
   *
   * @param nodes List of attribute nodes to display.
   * @return Panel containing the requested graph.
   */
  public static JPanel createGraphPanel(final List<AttributeTreeNode> nodes) {
    final List<String> content = new ArrayList<String>();
    for (final AttributeTreeNode atn : nodes) {
      content.addAll(atn.getContent());
    }
    return createGraphPanel(getChartTitle(nodes), content);
  }

  private static String getChartTitle(final List<AttributeTreeNode> nodes) {
    if (nodes.size() == 1) {
      return nodes.get(0).getElementName() + "@" + nodes.get(0).getAttributeName();
    }
    return "Multiple attributes";
  }

  /**
   * Creates a {@link JPanel} containing a pie chart with provided title,
   * visualizing the domain of the provided {@link Attribute#content}.
   *
   * @param title Graph title.
   * @param content Content of the {@link Attribute} to be visualized. Each
   * value of this content is represented with weight according to the number of
   * times it is found there.
   * @return JPanel containing a constructed JFreeChart pie chart.
   */
  private static JPanel createGraphPanel(final String title, final List<String> content) {
    final ChartPanel ret = new ChartPanel(createChart(title, createDataset(content)));
    ret.setPreferredSize(new Dimension(320, 240));
    return ret;
  }

  private static PieDataset createDataset(final List<String> content) {
    final DefaultPieDataset dataset = new DefaultPieDataset();

    Collections.sort(content);

    String last = null;
    int count = 1;

    for (final String s : content) {
      if (!s.equals(last)) {
        // output the last group
        if (last != null) {
          dataset.setValue(last, count);
        }
        // start a new group
        last = s;
        count = 1;
      }
      count++;
    }

    if (last != null) {
      dataset.setValue(last, count);
    }

    return dataset;
  }

  private static JFreeChart createChart(final String title, final PieDataset dataset) {
    final JFreeChart chart = ChartFactory.createPieChart(title, dataset, false, true, false);

    final PiePlot plot = (PiePlot) chart.getPlot();
    plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
    plot.setNoDataMessage("No data available");
    plot.setCircular(true);
    plot.setLabelGap(0.02);
    return chart;
  }

}
