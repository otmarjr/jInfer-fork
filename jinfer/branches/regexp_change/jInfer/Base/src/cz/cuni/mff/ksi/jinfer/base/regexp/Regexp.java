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
 *
 * @author vektor
 */
public class Regexp<T> {

  /** If this is a token, its token. */
  private T content;
  /** If this is not a token, list of children. */
  private List<Regexp<T>> children;
  private RegexpType type;
  private RegexpInterval interval;
  private boolean mutable;

  /**
   * Creates immutable regexp. TODO anti Comment more!
   * @param content
   * @param children
   * @param type
   * @param interval
   */
  public Regexp(final T content,
          final List<Regexp<T>> children, final RegexpType type, RegexpInterval interval) {
    this(content, children, type, interval, false);
  }

  private Regexp(final T content, final List<Regexp<T>> children,
          final RegexpType type, RegexpInterval interval, boolean mutable) {
    if (!mutable) {
      if (type == null) {
        throw new IllegalArgumentException("Type of regexp has to be non-null.");
      }
      if (type.equals(RegexpType.LAMBDA)) {
        if (content != null) {
          throw new IllegalArgumentException("When regexp is LAMBDA, content has to be null.");
        }
        if (children != null) {
          throw new IllegalArgumentException("When regexp is LAMBDA, children has to be null.");
        }
        if (interval != null) {
          throw new IllegalArgumentException("When regexp is LAMBDA, interval has to be null.");
        }
      } else {
        if (type.equals(RegexpType.TOKEN)) {
          if (content == null) {
            throw new IllegalArgumentException("When regexp is TOKEN, content has to be non-null.");
          }
          if (children != null) {
            throw new IllegalArgumentException("When regexp is TOKEN, children has to be null.");
          }
        } else {
          if (content != null) {
            throw new IllegalArgumentException("When regexp is " + type.toString() + ", content has to be null");
          }
          if (children == null) {
            throw new IllegalArgumentException("When regexp is " + type.toString() + ", children has to be non-null.");
          }
        }
        if (interval == null) {
          throw new IllegalArgumentException("When regexp is " + type.toString() + ", interval has to be non-null.");
        }
      }
    }
    this.content = content;
    this.children = children;
    this.type = type;
    this.interval= interval;
    this.mutable= mutable;
  }

  public static <T> Regexp<T> getMutable() {
    return new Regexp<T>(null, new ArrayList<Regexp<T>>(), null, null, true);
  }

  public static <T> Regexp<T> getLambda() {
    return new Regexp<T>(null, null, RegexpType.LAMBDA, null);
  }

  public static <T> Regexp<T> getToken(final T content, RegexpInterval interval) {
    return new Regexp<T>(content, null, RegexpType.TOKEN, interval);
  }

  public static <T> Regexp<T> getToken(final T content) {
    return Regexp.<T>getToken(content, RegexpInterval.getOnce());
  }

  public static <T> Regexp<T> getConcatenation(final List<Regexp<T>> children, RegexpInterval interval) {
    if (children == null) {
      throw new IllegalArgumentException("Children of concatenation cannot be "
              + "null. Why would you like to create such concatenation?");
    } else if (children.isEmpty()) {
      throw new IllegalArgumentException("Children of concatenation shouldn't "
              + "be set to empty list this way. If you need to add children"
              + " later, call getConcatenationMutable() to obtain such regexp. This "
              + "method is for proper use.");
    }
    return new Regexp<T>(null, children, RegexpType.CONCATENATION, interval);
  }

  public static <T> Regexp<T> getConcatenation(final List<Regexp<T>> children) {
    return getConcatenation(children, RegexpInterval.getOnce());
  }

  public static <T> Regexp<T> getConcatenationMutable() {
    return new Regexp<T>(null,  new ArrayList<Regexp<T>>(0), RegexpType.CONCATENATION, null, true);
  }

  public static <T> Regexp<T> getAlternation(final List<Regexp<T>> children, final RegexpInterval interval) {
    if (children == null) {
      throw new IllegalArgumentException("Children of alternation cannot be "
              + "null. Why would you like to create such alternation?");
    } else if (children.isEmpty()) {
      throw new IllegalArgumentException("Children of alternation can't be"
              + " empty list.");
    }
    return new Regexp<T>(null, children, RegexpType.ALTERNATION, interval);
  }

  public static <T> Regexp<T> getAlternation(final List<Regexp<T>> children) {
    return getAlternation(children, RegexpInterval.getOnce());
  }

  public static <T> Regexp<T> getPermutation(final List<Regexp<T>> children, RegexpInterval interval) {
    if (children == null) {
      throw new IllegalArgumentException("Children of permutation cannot be "
              + "null. Why would you like to create such permutation?");
    } else if (children.isEmpty()) {
      throw new IllegalArgumentException("Children of permutation can't be "
              + "empty list.");
    }
    return new Regexp<T>(null, children, RegexpType.PERMUTATION, interval);
  }

  public static <T> Regexp<T> getPermutation(final List<Regexp<T>> children) {
    return getPermutation(children, RegexpInterval.getOnce());
  }

  public void setContent(final T content) {
    if (this.mutable) {
      this.content = content;
    } else {
      throw new IllegalStateException("Trying to change content of immutable regexp.");
    }
  }

  public T getContent() {
    return content;
  }

  public List<Regexp<T>> getChildren() {
    if (children == null) {
      return null;
    }
    if (mutable) {
      return children;
    } else {
      return Collections.unmodifiableList(children);
    }
  }

  public void setType(RegexpType type) {
    if (this.mutable) {
      this.type = type;
    } else {
      throw new IllegalStateException("Trying to change type of immutable regexp.");
    }
  }

  public RegexpType getType() {
    return type;
  }

  public void setInterval(RegexpInterval interval) {
    if (this.mutable) {
      this.interval = interval;
    } else {
      throw new IllegalStateException("Trying to set interval of immutable regexp.");
    }
  }

  public RegexpInterval getInterval() {
    return interval;
  }

  public void setImmutable() {
    if (this.mutable) {
      this.mutable = false;
    } else {
      throw new IllegalStateException("Trying to set inmutable regexp, that is once inmutable.");
    }
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
    if (mutable) {
      children.add(child);
    } else {
      throw new IllegalStateException("Trying to add child to immutable regexp.");
    }
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
      case CONCATENATION:
      case ALTERNATION:
      case PERMUTATION:
        final List<T> ret = new ArrayList<T>();
        for (final Regexp<T> child : children) {
          ret.addAll(child.getTokens());
        }
        return ret;
      case LAMBDA:
        return Collections.emptyList();
      default:
        throw new IllegalArgumentException("Unknown enum member.");
    }
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

  public boolean isPermutation() {
    return RegexpType.PERMUTATION.equals(type);
  }

  public boolean isLambda() {
    return RegexpType.LAMBDA.equals(type);
  }

  /**
   * Returns true, if this is a token and its content is null;
   * or if the children are empty.
   *
   * Regexp should be considered to represent λ (lambda) iff it is empty.
   * 
   * @return
   */
/*  public boolean isEmpty() {
    if (isToken()) {
      return content == null;
    }
    return BaseUtils.isEmpty(children);
  }
*/
  
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

    final Regexp<T> concat = Regexp.<T>getMutable();
    concat.setType(RegexpType.CONCATENATION);
    concat.getChildren().addAll(altChildren);
    concat.setInterval(RegexpInterval.getOnce());

    final List<Regexp<T>> c = new ArrayList<Regexp<T>>();
    c.add(concat);

    newChildren.add(Regexp.<T>getAlternation(c));

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

    // TODO vektor check here
    // TODO anti This needs to be mutable
//    return new Regexp<T>(null, newChildren, RegexpType.CONCATENATION);
    Regexp<T> ret= Regexp.<T>getMutable();
    ret.getChildren().addAll(children);
    ret.setType(RegexpType.CONCATENATION);
    ret.setInterval(RegexpInterval.getOnce());
    ret.setImmutable();
    return ret;
  }

  private String comboToString(final String delimiter) {
    final StringBuilder ret = new StringBuilder();
    ret.append('(');
    for (final Regexp<T> child : children) {
      ret.append(child.toString()).append(delimiter);
    }
    ret.append(')');
    return ret.toString();
  }

  @Override
  public String toString() {
    switch (type) {
      case TOKEN:
          return content.toString() + interval.toString();
      case CONCATENATION:
        return comboToString(",") + interval.toString();
      case ALTERNATION:
        return comboToString("|") + interval.toString();
      case PERMUTATION:
        return comboToString("&") + interval.toString();
      case LAMBDA:
        return "λ";
      default:
        throw new IllegalArgumentException("Unknown enum member.");
    }
  }
}
