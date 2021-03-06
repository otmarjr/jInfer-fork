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
package cz.cuni.mff.ksi.jinfer.base.regexp;

import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a regular expression of arbitrary type.
 *
 * <ul>
 * <li>Definition 1:<br/>
 * REGEXP := TOKEN | CONCATENATION OF REGEXPS | ALTERNATION OF REGEXPS | KLEENE STAR OF A REGEXP
 * </li>
 * <li>
 * Definition 2:<br/>
 * The language of regular expressions is the smallest language containing
 * lambda, all letters of Σ, that is closed on concatenation, alternation and
 * Kleene star operator.
 * </li>
 * </ul>
 * 
 * @author vektor
 */
public class Regexp<T> {

  /** If this is a token, its token. */
  private T content;
  /** If this is not a token, list of children. */
  private final List<Regexp<T>> children;
  private final RegexpType type;

  public Regexp(final T content,
          final List<Regexp<T>> children, final RegexpType type) {
    this.content = content;
    this.children = children;
    this.type = type;
  }

  public static <T> Regexp<T> getToken(final T content) {
    return new Regexp<T>(content, Collections.<Regexp<T>>emptyList(), RegexpType.TOKEN);
  }

  public static <T> Regexp<T> getConcatenation(final List<Regexp<T>> children) {
    return new Regexp<T>(null, children, RegexpType.CONCATENATION);
  }

  public static <T> Regexp<T> getConcatenation() {
    return getConcatenation(new ArrayList<Regexp<T>>(0));
  }

  public static <T> Regexp<T> getAlternation(final List<Regexp<T>> children) {
    return new Regexp<T>(null, children, RegexpType.ALTERNATION);
  }

  public static <T> Regexp<T> getKleene(final List<Regexp<T>> children) {
    return new Regexp<T>(null, children, RegexpType.KLEENE);
  }

  public void setContent(final T content) {
    this.content = content;
  }

  public T getContent() {
    return content;
  }

  public List<Regexp<T>> getChildren() {
    return children;
  }

  /**
   * Shorthand for getChildren().get().
   * 
   * @param i Which child to fetch.
   * @return I-th child.
   */
  public Regexp<T> getChild(final int i) {
    return children.get(i);
  }

  /**
   * Shorthand for getChildren().add().
   *
   * @param child Which child to add.
   */
  public void addChild(final Regexp<T> child) {
    children.add(child);
  }

  /**
   * Returns all tokens contained in this regular expression, inorder from
   * the left to the right.
   * 
   * @return All tokens of this regexp.
   */
  @SuppressWarnings("unchecked")
  public List<T> getTokens() {
    switch (type) {
      case TOKEN:
        return Arrays.asList(content);
      case KLEENE:
        return getChild(0).getTokens();
      case CONCATENATION:
      case ALTERNATION:
        final List<T> ret = new ArrayList<T>();
        for (final Regexp<T> child : children) {
          ret.addAll(child.getTokens());
        }
        return ret;
      default:
        throw new IllegalArgumentException("Unknown enum member.");
    }
  }

  public RegexpType getType() {
    return type;
  }

  public boolean isToken() {
    return RegexpType.TOKEN.equals(type);
  }

  public boolean isConcatenation() {
    return RegexpType.CONCATENATION.equals(type);
  }

  public boolean isAlternation() {
    return RegexpType.ALTERNATION.equals(type);
  }

  public boolean isKleene() {
    return RegexpType.KLEENE.equals(type);
  }

  /**
   * Returns true, if this is a token and its content is null;
   * or if the children are empty.
   *
   * Regexp should be considered to represent λ (lambda) iff it is empty.
   * 
   * @return
   */
  public boolean isEmpty() {
    if (isToken()) {
      return content == null;
    }
    return BaseUtils.isEmpty(children);
  }

  /**
   * <p>Converts the i-th position of this concatenation to alternation and
   * inserts the rest as the one and only of its children.</p>
   *
   * <p>Example: consider concatenation :<br/>
   * <code>(A B C D)</code><br/>
   * after applying <code>branch(2)</code> it will
   * look like this:<br/>
   * <code>(A B (C D | ))</code>
   * </p>
   *
   * <p>If i-th position is already an alternation, nothing happens.</p>
   *
   * <p>Note that elements of the concatenation are indexed from 0.</p>
   *
   * @param position Where to branch.
   */
  public void branch(final int position) {
    if (!isConcatenation()) {
      throw new IllegalArgumentException();
    }
    if (position >= children.size()) {
      throw new IllegalArgumentException("Requested to branch at position " + position + ", but only " + children.size() + " long");
    }
    if (getChild(position).isAlternation()) {
      return;
    }

    final List<Regexp<T>> newChildren = new ArrayList<Regexp<T>>(position + 1);

    for (int i = 0; i < position; i++) {
      newChildren.add(getChild(i));
    }

    final List<Regexp<T>> altChildren = new ArrayList<Regexp<T>>();

    for (int i = position; i < children.size(); i++) {
      altChildren.add(getChild(i));
    }

    final Regexp<T> concat = new Regexp<T>(null, altChildren, RegexpType.CONCATENATION);

    final List<Regexp<T>> c = new ArrayList<Regexp<T>>();
    c.add(concat);

    newChildren.add(new Regexp<T>(null, c, RegexpType.ALTERNATION));

    children.clear();
    children.addAll(newChildren);
  }

  /**
   * Returns the suffix of this concatenation, from parameter <code>from</code> to the end.
   * 
   * @param from
   * @return
   */
  public Regexp<T> getEnd(final int from) {
    if (!isConcatenation()) {
      throw new IllegalArgumentException();
    }
    if (from >= children.size()) {
      throw new IllegalArgumentException();
    }
    if (from == 0) {
      return this;
    }

    final List<Regexp<T>> newChildren = new ArrayList<Regexp<T>>();

    for (int i = from; i < children.size(); i++) {
      newChildren.add(getChild(i));
    }

    return new Regexp<T>(null, newChildren, RegexpType.CONCATENATION);
  }

  @Override
  public String toString() {
    switch (type) {
      case TOKEN:
        return content.toString();
      case KLEENE:
        return "(" + getChild(0).toString() + ")*";
      case CONCATENATION:
        final StringBuilder retConc = new StringBuilder();
        retConc.append('(');
        for (final Regexp<T> child : children) {
          retConc.append(child.toString()).append(",");
        }
        retConc.append(')');
        return retConc.toString();
      case ALTERNATION:
        final StringBuilder retAlt = new StringBuilder();
        retAlt.append('(');
        for (final Regexp<T> child : children) {
          retAlt.append(child.toString()).append("|");
        }
        retAlt.append(')');
        return retAlt.toString();
      default:
        throw new IllegalArgumentException("Unknown enum member.");
    }
  }
}
