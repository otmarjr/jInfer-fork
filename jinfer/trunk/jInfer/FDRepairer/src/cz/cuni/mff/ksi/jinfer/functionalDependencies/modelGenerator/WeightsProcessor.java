/*
 * Copyright (C) 2011 sviro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.functionalDependencies.modelGenerator;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.weights.Tweight;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.weights.Tweights;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;
import org.xml.sax.SAXException;

/**
 * Implementation of {@link cz.cuni.mff.ksi.jinfer.base.interfaces.Processor}
 * providing logic for weights retrieval from weights files.
 * @author sviro
 */
@ServiceProvider(service = Processor.class)
public class WeightsProcessor implements Processor<Tweight> {

  private static final Logger LOG = Logger.getLogger(WeightsProcessor.class);

  private static final String SCHEMA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
"<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">" +
"<xs:complexType name=\"Tweight\">" +
  "<xs:sequence>" +
    "<xs:element name=\"path\" type=\"xs:string\"/>" +
    "<xs:element name=\"value\" type=\"xs:decimal\"/>" +
  "</xs:sequence>" +
"</xs:complexType>" +
"<xs:complexType name=\"Tweights\">" +
  "<xs:sequence>" +
    "<xs:element name=\"weight\" type=\"Tweight\" minOccurs=\"1\" maxOccurs=\"unbounded\"/>" +
  "</xs:sequence>" +
"</xs:complexType>" +
"<xs:element name=\"weights\" type=\"Tweights\"/>" +
"</xs:schema>";
  
  @Override
  public FolderType getFolder() {
    return FolderType.FD;
  }

  @Override
  public String getExtension() {
    return "wt";
  }

  @Override
  public boolean processUndefined() {
    return false;
  }

  @Override
  public List<Tweight> process(final InputStream inputStream) throws InterruptedException {
    final ClassLoader orig = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(Tweights.class.getClassLoader()); //NOPMD

    try {
      final JAXBContext context = JAXBContext.newInstance(Tweights.class.getPackage().getName());
      final Unmarshaller unmarshaller = context.createUnmarshaller();
      
      final SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
      final Schema schema = sf.newSchema(new StreamSource(new ByteArrayInputStream(SCHEMA.getBytes())));
      
      unmarshaller.setSchema(schema);
      @SuppressWarnings("unchecked")
      final JAXBElement<Tweights> weightsElement = (JAXBElement<Tweights>) unmarshaller.unmarshal(
              inputStream);

      final Tweights weights = weightsElement.getValue();
      return weights.getWeight();
    } catch (SAXException ex) {
      LOG.error(ex);
      throw new InterruptedException();
    } catch (UnmarshalException ex) {
      LOG.error("File with weights is broken");
      throw new InterruptedException();
    } catch (JAXBException ex) {
      LOG.error(ex);
      throw new InterruptedException();
    } catch (IllegalArgumentException ex) {
      LOG.error("Input stream must not be null.");
      throw new InterruptedException();
    }finally {
      Thread.currentThread().setContextClassLoader(orig);
      try {
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (IOException ex) {
        //
      }
    }
  }

  @Override
  public Class<?> getResultType() {
    return Tweight.class;
  }
}
