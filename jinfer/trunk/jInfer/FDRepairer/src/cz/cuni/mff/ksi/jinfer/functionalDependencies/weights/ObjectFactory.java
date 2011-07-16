package cz.cuni.mff.ksi.jinfer.functionalDependencies.weights;

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
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Weights_QNAME = new QName("", "weights");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Tweights }
     * 
     */
    public Tweights createTweights() {
        return new Tweights();
    }

    /**
     * Create an instance of {@link Tweight }
     * 
     */
    public Tweight createTweight() {
        return new Tweight();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tweights }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "weights")
    public JAXBElement<Tweights> createWeights(Tweights value) {
        return new JAXBElement<Tweights>(_Weights_QNAME, Tweights.class, null, value);
    }

}
