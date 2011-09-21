/**
 * Package containing entire logic for parsing XSD Schemas with Document Object Model parser.
 * <p>
 * Note that there are several types of entities called "element" within this package.
 * To avoid ambiguity, we shall use notation
 * <code>DOM.Element</code> for instances of {@link org.w3c.dom.Element }
 * and notation
 * <code>Element</code> for instances of {@link cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element}.
 * To add to this complication, XSD Schemas are XML documents which consist of elements and attributes.
 * We shall refer to these XML elements as "tags" and XML attributes as "tag attributes".
 * <em>All names of tags and tag attributes will be written in italics.</em>
 * Moreover, all tags and tag attributes that are known to the parser are specified in
 * {@link XSDTag } and {@link XSDAttribute } classes, respectively.
 * For example, the <i>attribute</i> tag can have a tag attribute <i>use</i>
 * - the former will match {@link XSDTag#ATTRIBUTE } and the latter will match {@link XSDAttribute#USE }.
 * Lastly, tag attributes should not be confused with instances of {@link Attribute },
 * which is a class for storing information about the tag attributes inside jInfer.
 * </p>
 * <p>
 * XSD Schemas allow using different namespace prefixes for tag names.
 * This parser does NOT recognize namespaces, all prefixes from tag names are trimmed.
 * For example, there is no difference between <i>xs:choice</i> and <i>xsd:choice</i>;
 * both will match the <code>XSDTag.CHOICE</code> constant.
 * </p>
 * @see XSDTag
 * @see XSDAttribute
 * @see XSDBuiltInDataTypes
 */
package cz.cuni.mff.ksi.jinfer.xsdimportdom;
