/**
 * Package containing entire logic for parsing XSD Schemas with SAX parser.
 * <p>
 * XSD Schemas allow using different namespace prefixes for tag names.
 * This parser does NOT recognize namespaces, all prefixes from tag names are trimmed.
 * For example, there is no difference between <i>xs:choice</i> and <i>xsd:choice</i>;
 * both will match the <code>XSDTag.CHOICE</code> constant.
 * </p>
 * @see XSDTag
 */
package cz.cuni.mff.ksi.jinfer.xsdimportsax;
