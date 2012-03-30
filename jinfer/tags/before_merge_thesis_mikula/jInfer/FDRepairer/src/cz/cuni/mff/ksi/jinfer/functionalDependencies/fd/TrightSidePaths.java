package cz.cuni.mff.ksi.jinfer.functionalDependencies.fd;

import cz.cuni.mff.ksi.jinfer.functionalDependencies.Path;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for TrightSidePaths complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TrightSidePaths">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="path" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrightSidePaths", propOrder = {
  "path"
})
public class TrightSidePaths implements SidePaths {

  @XmlElement(required = true)
  protected String path;

  /**
   * Gets the value of the path property.
   * 
   * @return
   *     possible object is
   *     {@link String }
   *     
   */
  public String getPath() {
    return path;
  }

  public Path getPathObj() {
    return new Path(path);
  }

  /**
   * Sets the value of the path property.
   * 
   * @param value
   *     allowed object is
   *     {@link String }
   *     
   */
  public void setPath(String value) {
    this.path = value;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof TrightSidePaths)) {
      return false;
    }
    
    TrightSidePaths rightSide = (TrightSidePaths) obj;
    
    return this.getPath().equals(rightSide.getPath());
  }
  
  
}
