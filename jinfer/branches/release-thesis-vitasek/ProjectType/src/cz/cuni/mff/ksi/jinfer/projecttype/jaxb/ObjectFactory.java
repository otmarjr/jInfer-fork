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

package cz.cuni.mff.ksi.jinfer.projecttype.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 *
 * @author sviro
 * 
 */
@SuppressWarnings("PMD")
@XmlRegistry
public class ObjectFactory {

    private final static QName _Jinferinput_QNAME = new QName("", "jinferinput");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Txml }
     * 
     */
    public Txml createTxml() {
        return new Txml();
    }

    /**
     * Create an instance of {@link Tqueries }
     * 
     */
    public Tqueries createTqueries() {
        return new Tqueries();
    }

    /**
     * Create an instance of {@link Tschemas }
     * 
     */
    public Tschemas createTschemas() {
        return new Tschemas();
    }
    
    /**
     * Create an instance of {@link Tfds }
     * 
     */
    public Tfds createTfds() {
        return new Tfds();
    }

    /**
     * Create an instance of {@link Tfile }
     * 
     */
    public Tfile createTfile() {
        return new Tfile();
    }

    /**
     * Create an instance of {@link Tjinfer }
     * 
     */
    public Tjinfer createTjinfer() {
        return new Tjinfer();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tjinfer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "jinferinput")
    public JAXBElement<Tjinfer> createJinferinput(final Tjinfer value) {
        return new JAXBElement<Tjinfer>(_Jinferinput_QNAME, Tjinfer.class, null, value);
    }

}
