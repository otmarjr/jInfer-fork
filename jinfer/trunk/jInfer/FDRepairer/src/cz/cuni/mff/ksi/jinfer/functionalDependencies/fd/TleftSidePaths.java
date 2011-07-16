package cz.cuni.mff.ksi.jinfer.functionalDependencies.fd;

import cz.cuni.mff.ksi.jinfer.functionalDependencies.Path;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for TleftSidePaths complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TleftSidePaths">
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
@XmlType(name = "TleftSidePaths", propOrder = {
  "path"
})
public class TleftSidePaths implements SidePaths {

  @XmlElement(required = true)
  protected List<String> path;

  /**
   * Gets the value of the path property.
   * 
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the path property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getPath().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link String }
   * 
   * 
   */
  public List<String> getPath() {
    if (path == null) {
      path = new ArrayList<String>();
    }
    return this.path;
  }

  public List<Path> getPaths() {
    List<String> paths = getPath();
    List<Path> result = new ArrayList<Path>();
    for (String pathStr : paths) {
      result.add(new Path(pathStr));
    }

    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof TleftSidePaths)) {
      return false;
    }
    
    TleftSidePaths leftSide = (TleftSidePaths) obj;
    
    return this.getPath().equals(leftSide.getPath());
  }
  
  
}
