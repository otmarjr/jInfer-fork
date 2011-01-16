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

import cz.cuni.mff.ksi.jinfer.base.utils.CollectionToString;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a regular expression of arbitrary type.
 *
 * <ul>
 * <li>Definition 1:<br/>
 * REGEXP := TOKEN | CONCATENATION OF REGEXPS | ALTERNATION OF REGEXPS | PERMUTATIONS OF REGEXPS
 * </li>
 * <li>
 * Definition 2:<br/>
 * The language of regular expressions is the smallest language containing
 * lambda, all letters of Σ, that is closed on concatenation, alternation and
 * Kleene star operator.
 * </li>
 * </ul>
 * Permutation is just syntactic sugar. Each regexp has @link{RegexpInterval}
 * associated with it.
 *
 * @param <T>
 * @author vektor
 */
public class Regexp<T> {

  // TODO anti Comment public methods, including constructors!

  /** If regexp is a token, its token. */
  private T content;
  /** If this is not a token, list of children. */
  private List<Regexp<T>> children;
  /** One of RegexpType types */
  private RegexpType type;
  private RegexpInterval interval;
  private boolean mutable;

  // TODO anti Please move away from the top of the file
  private void checkConstraits() {
    if (!mutable) {
      if (type == null) {
        throw new IllegalArgumentException("Type of regexp has to be non-null.");
      }
      if (children == null) {
        throw new IllegalArgumentException("When regexp is " + getType() + ", children has to be non-null.");
      }
      if (type.equals(RegexpType.LAMBDA)) {
        if (content != null) {
          throw new IllegalArgumentException("When regexp is LAMBDA, content has to be null.");
        }
        if (interval != null) {
          throw new IllegalArgumentException("When regexp is LAMBDA, interval has to be null.");
        }
      } else if (type.equals(RegexpType.TOKEN)) {
        if (content == null) {
          throw new IllegalArgumentException("When regexp is TOKEN, content has to be non-null.");
        }
      } else {
        if (content != null) {
          throw new IllegalArgumentException("When regexp is " + type.toString() + ", content has to be null");
        }
        if (interval == null) {
          throw new IllegalArgumentException("When regexp is " + type.toString() + ", interval has to be non-null.");
        }
      }
    }
  }

  /**
   * Creates immutable regexp. This is the default.
   * @param content
   * @param children
   * @param type
   * @param interval
   */
  public Regexp(final T content,
          final List<Regexp<T>> children,
          final RegexpType type,
          final RegexpInterval interval) {
    this(content, children, type, interval, false);
  }

  private Regexp(final T content,
          final List<Regexp<T>> children,
          final RegexpType type,
          final RegexpInterval interval,
          final boolean mutable) {

    this.content = content;
    this.children = children;
    this.type = type;
    this.interval = interval;
    this.mutable = mutable;
    checkConstraits();
  }

  /**
   * Get empty (members not set) regexp which is mutable. This is the only way
   * to create mutable regexp. Proper use is like this:
   *
   *
   * Regexp<T> r = Regexp.<T>getMutable();
   * r.setInterval(...);
   * r.setType(RegexpType.LAMBDA);
   * r.setImmutable();cz.cuni.mff.ksi.jinfer.base
   *
   * or
   *
   * Regexp<T> r = Regexp.<T>getMutable();
   * r.setInterval(...);
   * r.setType(RegexpType.TOKEN);
   * r.setContent(...)
   * r.setImmutable();
   *
   * or
   *
   * Regexp<T> r = Regexp.<T>getMutable();
   * r.setInterval(...);
   * r.setType(RegexpType.CONCATENATION);
   * r.addChild(...);
   * r.addChild(...);
   * r.addChild(...);
   * r.setImmutable();
   *
   * Take care to setup all members correctly and lock regexp.
   *
   * @param <T>
   * @return
   */
  public static <T> Regexp<T> getMutable() {
    return new Regexp<T>(null, new ArrayList<Regexp<T>>(), null, null, true);
  }

  /**
   * Get immutable lambda regexp.
   * 
   * @param <T>
   * @return
   */
  public static <T> Regexp<T> getLambda() {
    return new Regexp<T>(null, new ArrayList<Regexp<T>>(), RegexpType.LAMBDA, null);
  }

  /**
   * Get immutable token regexp.
   * 
   * @param <T>
   * @param content symbol of regexp
   * @param interval associated interval, e.g. {3,7}
   * @return
   */
  public static <T> Regexp<T> getToken(final T content, final RegexpInterval interval) {
    return new Regexp<T>(content, new ArrayList<Regexp<T>>(), RegexpType.TOKEN, interval);
  }

  /**
   * Get immutable token regexp with interval set to {1, 1}.
   * 
   * @param <T>
   * @param content symbol of regexp
   * @return
   */
  public static <T> Regexp<T> getToken(final T content) {
    return Regexp.<T>getToken(content, RegexpInterval.getOnce());
  }

  /**
   * Get immutable concatenation regexp.
   *
   * @param <T>
   * @param children of this regexp, that are subregexps.
   * @param interval associated interval, e.g. {3,7}
   * @return
   */
  public static <T> Regexp<T> getConcatenation(final List<Regexp<T>> children, final RegexpInterval interval) {
    return new Regexp<T>(null, children, RegexpType.CONCATENATION, interval);
  }

  /**
   * Get immutable concatenation regexp with interval set to {1, 1}.
   * 
   * @param <T>
   * @param children of this regexp, that are subregexps.
   * @return
   */
  public static <T> Regexp<T> getConcatenation(final List<Regexp<T>> children) {
    return getConcatenation(children, RegexpInterval.getOnce());
  }

  /**
   * Get immutable alternation regexp.
   * @param <T>
   * @param children of this regexp, that are subregexps.
   * @param interval associated interval, e.g. {3,7}
   * @return
   */
  public static <T> Regexp<T> getAlternation(final List<Regexp<T>> children, final RegexpInterval interval) {
    return new Regexp<T>(null, children, RegexpType.ALTERNATION, interval);
  }

  /**
   * Get immutable alternation regexp with interval set to {1, 1}.
   * @param <T>
   * @param children of this regexp, that are subregexps.
   * @return
   */
  public static <T> Regexp<T> getAlternation(final List<Regexp<T>> children) {
    return getAlternation(children, RegexpInterval.getOnce());
  }

  /**
   * Get immutable permutation regexp.
   * @param <T>
   * @param children of this regexp, that are subregexps.
   * @param interval associated interval, e.g. {3,7}
   * @return
   */
  public static <T> Regexp<T> getPermutation(final List<Regexp<T>> children, final RegexpInterval interval) {
    return new Regexp<T>(null, children, RegexpType.PERMUTATION, interval);
  }

  /**
   * Get immutable permutation regexp with interval set to {1, 1}.
   * @param <T>
   * @param children of this regexp, that are subregexps.
   * @return
   */
  public static <T> Regexp<T> getPermutation(final List<Regexp<T>> children) {
    return getPermutation(children, RegexpInterval.getOnce());
  }

  /**
   * Set content of the regexp if it is mutable.
   * 
   * Throws exception when regexp is immutable.
   * @param content
   */
  public void setContent(final T content) {
    if (this.mutable) {
      this.content = content;
    } else {
      throw new IllegalStateException("Trying to change content of immutable regexp.");
    }
  }

  /**
   * @return content of token regexp or null if regexp is not token
   */
  public T getContent() {
    return content;
  }

  /**
   * @return null - when regexp children are null (regexp is token), immutable (unmodifiable) list, when regexp is immutable and not token, and mutable list when regexp is not token and is mutable.
   */
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

  /**
   * Set type of regexp when it is mutable, throws exception when regexp is immutable.
   * 
   * @param type desired type
   */
  public void setType(final RegexpType type) {
    if (this.mutable) {
      this.type = type;
    } else {
      throw new IllegalStateException("Trying to change type of immutable regexp.");
    }
  }

  /**
   *
   * @return type of regexp
   */
  public RegexpType getType() {
    return type;
  }

  /**
   * Set interval of regexp when it is mutable, throws exception when regexp is immutable.
   * 
   * @param interval desired interval
   */
  public void setInterval(final RegexpInterval interval) {
    if (this.mutable) {
      this.interval = interval;
    } else {
      throw new IllegalStateException("Trying to set interval of immutable regexp.");
    }
  }

  /**
   *
   * @return interval associated with regexp.
   */
  public RegexpInterval getInterval() {
    return interval;
  }

  /**
   * Lock the regexp to be immutable from now.
   * Throws exception if you are programming badly and tries to lock it twice.
   */
  public void setImmutable() {
    if (this.mutable) {
      this.mutable = false;
    } else {
      throw new IllegalStateException("Trying to set inmutable regexp, that is once inmutable.");
    }
    checkConstraits();
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
   * Works only when regexp is mutable, throws exception otherwise.
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

  /**
   *
   * @return true iff type of this regexp equals RegexpType.TOKEN
   */
  public boolean isToken() {
    return RegexpType.TOKEN.equals(type);
  }

  /**
   *
   * @return true iff type of this regexp equals RegexpType.CONCATENATION
   */
  public boolean isConcatenation() {
    return RegexpType.CONCATENATION.equals(type);
  }

  /**
   *
   * @return true iff type of this regexp equals RegexpType.ALTERNATION
   */
  public boolean isAlternation() {
    return RegexpType.ALTERNATION.equals(type);
  }

  /**
   *
   * @return true iff type of this regexp equals RegexpType.PERMUTATION
   */
  public boolean isPermutation() {
    return RegexpType.PERMUTATION.equals(type);
  }

  /**
   *
   * @return true iff type of this regexp equals RegexpType.LAMBDA
   */
  public boolean isLambda() {
    return RegexpType.LAMBDA.equals(type);
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

    final Regexp<T> concat = Regexp.<T>getMutable();
    concat.setType(RegexpType.CONCATENATION);
    concat.getChildren().addAll(altChildren);
    concat.setInterval(RegexpInterval.getOnce());

    final List<Regexp<T>> c = new ArrayList<Regexp<T>>();
    c.add(concat);

    final Regexp<T> emptyConcat = Regexp.getMutable();
    emptyConcat.setType(RegexpType.ALTERNATION);
    emptyConcat.getChildren().addAll(c);
    emptyConcat.setInterval(RegexpInterval.getOnce());
    newChildren.add(emptyConcat);

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

    final Regexp<T> ret = Regexp.<T>getMutable();
    ret.getChildren().addAll(newChildren);
    ret.setType(RegexpType.CONCATENATION);
    ret.setInterval(RegexpInterval.getOnce());
    return ret;
  }

  @Override
  public String toString() {
    if (type != null) {
      switch (type) {
        case TOKEN:
          return content.toString() + interval.toString();
        case CONCATENATION:
        case ALTERNATION:
        case PERMUTATION:
          return CollectionToString.colToString(children, getDelimiter(type),
                  new CollectionToString.ToString<Regexp<T>>() {

                    @Override
                    public String toString(final Regexp<T> t) {
                      return t.toString();
                    }
                  })
                  + interval.toString();
        case LAMBDA:
          return "\u03BB";
        default:
          throw new IllegalArgumentException("Unknown enum member " + type);
      }
    } else {
      return "RegexpType=null";
    }
  }

  private static String getDelimiter(final RegexpType t) {
    switch (t) {
      case CONCATENATION:
        return "\n,";
      case ALTERNATION:
        return "\n|";
      case PERMUTATION:
        return "\n&";
      default:
        throw new IllegalStateException("Invalid regexp type at this point: " + t);
    }
  }
}
// this code is pure evil
// see RegexpAutomatonStateRemoval.java for pure evil