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
package cz.cuni.mff.ksi.jinfer.basicigg;

import cz.cuni.mff.ksi.jinfer.basicigg.xpath.XPathProcessor;
import cz.cuni.mff.ksi.jinfer.basicigg.dtd.DTDProcessor;
import cz.cuni.mff.ksi.jinfer.base.interfaces.IGGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.IGGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.FileUtils;
import cz.cuni.mff.ksi.jinfer.basicigg.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.basicigg.xml.XMLProcessor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * A trivial implementation of IGGenerator module. Works only with XML documents.
 * 
 * @author vektor
 */
@ServiceProvider(service = IGGenerator.class)
public class IGGeneratorImpl implements IGGenerator {

  private static final Logger LOG = Logger.getLogger(IGGeneratorImpl.class);

  @Override
  public String getModuleName() {
    return "Trivial IG Generator";
  }

  @Override
  public void start(final Input input, final IGGeneratorCallback callback)
          throws InterruptedException {
    final List<AbstractNode> ret = new ArrayList<AbstractNode>();

    final Map<String, Processor> xmlProcessor = new HashMap<String, Processor>();
    xmlProcessor.put("*", new XMLProcessor());
    final Map<String, Processor> dtdProcessor = new HashMap<String, Processor>();
    dtdProcessor.put("dtd", new DTDProcessor());
    final Map<String, Processor> xpathProcessor = new HashMap<String, Processor>();
    xpathProcessor.put("*", new XPathProcessor());

    ret.addAll(getRulesFromInput(input.getDocuments(), xmlProcessor));
    ret.addAll(getRulesFromInput(input.getSchemas(), dtdProcessor));
    ret.addAll(getRulesFromInput(input.getQueries(), xpathProcessor));

    callback.finished(ret);
  }

  /**
   * Retrieves IG rules from provided input files.
   *
   * @param files A collection of input files from which rules will be retrieved.
   * @param mappings {@link Map} of pairs (extension - processor). Extension is
   * a lowercase file extension (e.g. "dtd"),
   * processor is the {@link Processor} responsible
   * for this file type. If <code>mappings</code> contain the key "*", will the
   * associated processor handle all file types.
   *
   * @return List of IG rules. Empty, if there are no input files or an error occurs.
   */
  private static List<AbstractNode> getRulesFromInput(final Collection<File> files,
          final Map<String, Processor> mappings) throws InterruptedException {
    if (BaseUtils.isEmpty(files)) {
      return new ArrayList<AbstractNode>(0);
    }

    final List<AbstractNode> ret = new ArrayList<AbstractNode>();

    for (final File f : files) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      try {
        if (mappings.containsKey("*")) {
          ret.addAll(mappings.get("*").process(new FileInputStream(f)));
        } else {
          final String ext = FileUtils.getExtension(f.getAbsolutePath());
          if (mappings.containsKey(ext)) {
            ret.addAll(mappings.get(ext).process(new FileInputStream(f)));
          } else {
            LOG.error("File extension does not have a corresponding mapping: " + f.getAbsolutePath());
          }
        }
      } catch (final FileNotFoundException e) {
        LOG.error("File not found: " + f.getAbsolutePath(), e);
      }
    }

    return ret;
  }
}
