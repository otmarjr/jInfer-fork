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

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A few utilities to be used in JUnit tests.
 *
 * @author vektor
 */
public final class TestUtils {

  private TestUtils() {
  }

  /**
   * Empty context, can be used in
   * {@link cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element} construction.
   */
  public static final List<String> EMPTY_CONTEXT = Collections.emptyList();

  /**
   * Empty metadata, can be used in
   * {@link cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element} construction.
   */
  public static final Map<String, Object> EMPTY_METADATA = Collections.emptyMap();

  /**
   * Returns a simple textual representation of specified element, fit for using
   * in unit tests.
   */
  public static String elementToStr(final Element e) {
    final StringBuilder ret = new StringBuilder(e.getName());
    if (!BaseUtils.isEmpty(e.getAttributes())) {
      ret.append(':');
      ret.append(CollectionToString.colToString(e.getAttributes(), ",",
              new CollectionToString.ToString<Attribute>() {
        @Override
        public String toString(final Attribute t) {
          return t.getName();
        }
      }, "", ""));
    }
    ret.append('{').append(regexpToStr(e.getSubnodes())).append('}');
    return ret.toString();
  }

  /**
   * Returns a simple textual representation of specified regexp, fit for using
   * in unit tests.
   */
  @SuppressWarnings("PMD.MissingBreakInSwitch")
  public static String regexpToStr(final Regexp<AbstractStructuralNode> r) {
    switch (r.getType()) {
      case TOKEN:
        return r.getContent().isElement()
                ? elementToStr((Element)r.getContent()) + r.getInterval().toString()
                : "\"" + r.getContent().getName() + "\"";
      case CONCATENATION:
      case ALTERNATION:
      case PERMUTATION:
        return CollectionToString.colToString(r.getChildren(),
                getDelimiter(r.getType()),
                new CollectionToString.ToString<Regexp<AbstractStructuralNode>>() {

                  @Override
                  public String toString(final Regexp<AbstractStructuralNode> t) {
                    return regexpToStr(t);
                  }
                })
                + r.getInterval().toString();
      case LAMBDA:
        return "\u03BB";
      default:
        throw new IllegalArgumentException("Unknown enum member " + r.getType());
    }
  }

  /**
   * Returns an element with provided name and subnodes. The element is
   * otherwise empty: context and metadata are empty, there are no attributes.
   * The element will be created as immutable.
   *
   * @param name Name for this element.
   * @param subnodes Subnodes of this element.
   * @return Constructed element.
   */
  public static Element getElement(final String name,
          final Regexp<AbstractStructuralNode> subnodes) {
    return new Element(
            EMPTY_CONTEXT,
            name,
            EMPTY_METADATA,
            subnodes,
            Collections.<Attribute>emptyList());
  }

  /**
   * Returns an element with provided name. The element is otherwise
   * empty: context and metadata are empty, the subnodes are
   * {@link RegexpType#LAMBDA}, there are no attributes.
   * The element will be created as immutable.
   *
   * @param name Name for this element.
   * @return Constructed element.
   */
  public static Element getElement(final String name) {
    return new Element(
            EMPTY_CONTEXT,
            name,
            EMPTY_METADATA,
            Regexp.<AbstractStructuralNode>getLambda(),
            Collections.<Attribute>emptyList());
  }

  /**
   * Returns an element with provided name, containing an attribute with
   * provided name and content. The element is otherwise empty: context and
   * metadata are empty, the subnodes are {@link RegexpType#LAMBDA}.
   * The element will be created as immutable.
   *
   * @param element Name for this element.
   * @param attribute Name of its only attribute.
   * @param value The only value of the attribute.
   * @return Constructed element.
   */
  public static Element getElementWithAttribute(final String element,
          final String attribute, final String value) {
    return new Element(
            EMPTY_CONTEXT,
            element,
            EMPTY_METADATA,
            Regexp.<AbstractStructuralNode>getLambda(),
            Arrays.asList(new Attribute(
                    EMPTY_CONTEXT,
                    attribute,
                    EMPTY_METADATA,
                    null,
                    Arrays.<String>asList(value)))
            );
  }

  /**
   * Returns a simple data with provided string as its only content. Simple data
   * is otherwise quite empty: context and metadata are empty, content type is
   * "string".
   *
   * @param content The only content string this simple data should have.
   * @return Constructed simple data.
   */
  public static SimpleData getSimpleData(final String content) {
    return new SimpleData(EMPTY_CONTEXT,
            "simple data",
            EMPTY_METADATA,
            "string",
            Arrays.asList(content));
  }

    /**
   * Returns a simple data with provided string as its name. Simple data
   * is otherwise quite empty: context, metadata and content are empty, content type is
   * "string".
   *
   * @param name The name of simple data.
   * @return Constructed simple data.
   */
  public static SimpleData getSimpleDataByName(final String name) {
    return new SimpleData(EMPTY_CONTEXT,
            name,
            EMPTY_METADATA,
            "string",
            EMPTY_CONTEXT);
  }

  /**
   * Returns an attribute with provided name. The attribute is otherwise empty:
   * context, metadata and content are empty, content type is null.
   *
   * @param name Name for this attribute.
   * @return Constructed attribute.
   */
  public static Attribute getAttribute(final String name) {
    return new Attribute(
            EMPTY_CONTEXT,
            name,
            EMPTY_METADATA,
            null,
            EMPTY_CONTEXT);
  }

  /**
   * Encapsulates the specified node into a mutable token regexp with interval
   * "once".
   */
  public static Regexp<AbstractStructuralNode> getToken(
          final AbstractStructuralNode n) {
    final Regexp<AbstractStructuralNode> ret = Regexp.getMutable();
    ret.setType(RegexpType.TOKEN);
    ret.setInterval(RegexpInterval.getOnce());
    ret.setContent(n);
    return ret;
  }

  private static String getDelimiter(final RegexpType t) {
    switch (t) {
      case CONCATENATION:
        return ",";
      case ALTERNATION:
        return "|";
      case PERMUTATION:
        return "&";
      default:
        throw new IllegalStateException("Invalid regexp type at this point: " + t);
    }
  }

}
