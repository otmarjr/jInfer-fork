package cz.cuni.mff.ksi.jinfer.functionalDependencies.fd;

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

    private final static QName _Dependencies_QNAME = new QName("", "dependencies");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Tdependencies }
     * 
     */
    public Tdependencies createTdependencies() {
        return new Tdependencies();
    }

    /**
     * Create an instance of {@link TleftSidePaths }
     * 
     */
    public TleftSidePaths createTleftSidePaths() {
        return new TleftSidePaths();
    }

    /**
     * Create an instance of {@link TrightSidePaths }
     * 
     */
    public TrightSidePaths createTrightSidePaths() {
        return new TrightSidePaths();
    }

    /**
     * Create an instance of {@link Tdependency }
     * 
     */
    public FD createFD() {
        return new FD();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tdependencies }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "dependencies")
    public JAXBElement<Tdependencies> createDependencies(Tdependencies value) {
        return new JAXBElement<Tdependencies>(_Dependencies_QNAME, Tdependencies.class, null, value);
    }

}
