package cz.cuni.mff.ksi.jinfer.functionalDependencies.fd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Tdependency complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Tdependency">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="leftSidePaths" type="{}TleftSidePaths"/>
 *         &lt;element name="rightSidePaths" type="{}TrightSidePaths"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tdependency", propOrder = {
    "leftSidePaths",
    "rightSidePaths"
})
public class FD {

    @XmlElement(required = true)
    protected TleftSidePaths leftSidePaths;
    @XmlElement(required = true)
    protected TrightSidePaths rightSidePaths;

    /**
     * Gets the value of the leftSidePaths property.
     * 
     * @return
     *     possible object is
     *     {@link TleftSidePaths }
     *     
     */
    public TleftSidePaths getLeftSidePaths() {
        return leftSidePaths;
    }

    /**
     * Sets the value of the leftSidePaths property.
     * 
     * @param value
     *     allowed object is
     *     {@link TleftSidePaths }
     *     
     */
    public void setLeftSidePaths(TleftSidePaths value) {
        this.leftSidePaths = value;
    }

    /**
     * Gets the value of the rightSidePaths property.
     * 
     * @return
     *     possible object is
     *     {@link TrightSidePaths }
     *     
     */
    public TrightSidePaths getRightSidePaths() {
        return rightSidePaths;
    }

    /**
     * Sets the value of the rightSidePaths property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrightSidePaths }
     *     
     */
    public void setRightSidePaths(TrightSidePaths value) {
        this.rightSidePaths = value;
    }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    
    if (!(obj instanceof FD)) {
      return false;
    }
    
    FD fd2 = (FD) obj;
    
    return this.getLeftSidePaths().equals(fd2.getLeftSidePaths()) && this.getRightSidePaths().equals(fd2.getRightSidePaths());
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    
    boolean first = true;
    for (String path : leftSidePaths.getPath()) {
      if (first) {
        first = false;
      } else {
        builder.append(",");
      }
      
      builder.append(path);
    }
    
    builder.append("} -> ").append(rightSidePaths.getPath());
    
    return builder.toString();
  }
    
  

}
