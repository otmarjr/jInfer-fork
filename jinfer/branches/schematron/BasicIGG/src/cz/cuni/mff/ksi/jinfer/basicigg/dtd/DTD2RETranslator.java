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
package cz.cuni.mff.ksi.jinfer.basicigg.dtd;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.basicigg.expansion.ExpansionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlmiddleware.schemas.dtds.ElementType;
import org.xmlmiddleware.schemas.dtds.Group;
import org.xmlmiddleware.schemas.dtds.Particle;
import org.xmlmiddleware.schemas.dtds.Reference;

/**
 * Helper class for translation from the DTD parser library data structures
 * to jInfer's regexp data structures.
 *
 * @author vektor
 */
public final class DTD2RETranslator {

  private DTD2RETranslator() {
  }

  /**
   * Translates the specified particle into jInfer-style regexp.
   *
   * @param p Particle to be translated.
   * @param simpleData Flag specifying, whether the resulting regexp should
   * contain a simple data token - something that cannot be determined from
   * the particle alone.
   * @return Regular expression representing the specified particle. If the
   * particle is <code>null</code>, the result will be either lambda (if
   * simpleData is <code>false</code>) or a concatenation with a single
   * simple data token (otherwise).
   */
  public static Regexp<AbstractStructuralNode> particle2Regexp(final Particle p,
          final boolean simpleData) {
    if (p == null) {
      final Regexp<AbstractStructuralNode> ret = Regexp.getMutable();
      ret.setInterval(RegexpInterval.getOnce());
      ret.setType(RegexpType.CONCATENATION);
      if (simpleData) {
        final SimpleData sd = SimpleData.getMutable();
        sd.setImmutable();
        ret.addChild(Regexp.<AbstractStructuralNode>getToken(sd));
      }
      ret.setImmutable();
      return ret;
    }
    final Regexp<AbstractStructuralNode> ret = Regexp.getMutable();

    ret.setInterval(getIntervalForParticle(p));
    ret.setType(getRegexpTypeForParticle(p));

    if (p instanceof Reference) {
      final Reference elementRef = (Reference) p;
      ret.setContent(elementType2Element(elementRef.elementType));
    } else if (p instanceof Group) {
      final Group g = (Group) p;
      ret.getChildren().addAll(members2Children(g.members));
    } else {
      throw new IllegalArgumentException("Unknown particle type");
    }

    if (simpleData) {
      final SimpleData sd = SimpleData.getMutable();
      sd.setImmutable();
      ret.addChild(Regexp.<AbstractStructuralNode>getToken(sd));
    }

    ret.setImmutable();

    return ret;
  }

  private static Element elementType2Element(final ElementType et) {
    final Map<String, Object> metadata = new HashMap<String, Object>();
    metadata.putAll(IGGUtils.ATTR_FROM_SCHEMA);
    metadata.putAll(IGGUtils.METADATA_SENTINEL);
    final Element ret = new Element(Collections.<String>emptyList(),
            et.name.getLocalName(),
            metadata,
            ExpansionHelper.<AbstractStructuralNode>getEmptyConcat(),
            Collections.<Attribute>emptyList());
    return ret;
  }

  private static List<Regexp<AbstractStructuralNode>> members2Children(final List<?> members) {
    final List<Regexp<AbstractStructuralNode>> ret =
            new ArrayList<Regexp<AbstractStructuralNode>>(members.size());

    for (final Object o : members) {
      if (!(o instanceof Particle)) {
        throw new IllegalArgumentException("Non-particle member.");
      }
      final Particle p = (Particle) o;
      ret.add(particle2Regexp(p, false));
    }

    return ret;
  }

  private static RegexpInterval getIntervalForParticle(final Particle p) {
    if (p.isRepeatable) {
      if (p.isRequired) {
        return RegexpInterval.getKleeneCross();
      } else {
        return RegexpInterval.getKleeneStar();
      }
    } else {
      if (p.isRequired) {
        return RegexpInterval.getOnce();
      } else {
        return RegexpInterval.getOptional();
      }
    }
  }

  private static RegexpType getRegexpTypeForParticle(final Particle p) {
    switch (p.type) {
      case Particle.TYPE_UNKNOWN:
        throw new IllegalArgumentException("Particle type unknown cannot be processed");
      case Particle.TYPE_ELEMENTTYPEREF:
        return RegexpType.TOKEN;
      case Particle.TYPE_CHOICE:
        return RegexpType.ALTERNATION;
      case Particle.TYPE_SEQUENCE:
        return RegexpType.CONCATENATION;
      default:
        throw new IllegalArgumentException("Unkown particle type: " + p.type);
    }
  }
}
