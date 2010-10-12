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

import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.IGGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.IGGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.FileUtils;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openide.util.Lookup;
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
  public String getName() {
    return "Basic IG Generator";
  }

  @Override
  public String getModuleDescription() {
    return getName();
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.emptyList();
  }

  @Override
  public void start(final Input input, final IGGeneratorCallback callback)
          throws InterruptedException {
    final List<Element> ret = new ArrayList<Element>();

    // find processor mappings for all folders
    final Map<FolderType, Map<String, Processor>> registeredProcessors = getRegisteredProcessors();

    ret.addAll(getRulesFromInput(input.getDocuments(), registeredProcessors.get(FolderType.XML)));
    ret.addAll(getRulesFromInput(input.getSchemas(), registeredProcessors.get(FolderType.SCHEMA)));
    ret.addAll(getRulesFromInput(input.getQueries(), registeredProcessors.get(FolderType.QUERY)));

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
  private static List<Element> getRulesFromInput(final Collection<File> files,
          final Map<String, Processor> mappings) throws InterruptedException {
    if (BaseUtils.isEmpty(files)) {
      return new ArrayList<Element>(0);
    }

    final List<Element> ret = new ArrayList<Element>();

    for (final File f : files) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      try {
        final String ext = FileUtils.getExtension(f.getAbsolutePath());
        if (mappings.containsKey(ext)) {
          ret.addAll(mappings.get(ext).process(new FileInputStream(f)));
        } else if (mappings.containsKey("*")) {
          ret.addAll(mappings.get("*").process(new FileInputStream(f)));
        } else {
          LOG.error("File extension does not have a corresponding mapping: " + f.getAbsolutePath());
        }
      } catch (final FileNotFoundException e) {
        throw new RuntimeException("File not found: " + f.getAbsolutePath(), e);
      }
    }

    return ret;
  }

  /**
   * Returns the map (folder - (extension - processor) ) of all processors
   * installed in this NetBeans.
   */
  private Map<FolderType, Map<String, Processor>> getRegisteredProcessors() {
    final Map<FolderType, Map<String, Processor>> ret =
            new HashMap<FolderType, Map<String, Processor>>();

    for (final FolderType ft : FolderType.values()) {
      ret.put(ft, new HashMap<String, Processor>());
    }

    for (final Processor p : Lookup.getDefault().lookupAll(Processor.class)) {
      if (p.processUndefined()) {
        ret.get(p.getFolder()).put("*", p);
      }
      ret.get(p.getFolder()).put(p.getExtension(), p);
    }

    return ret;
  }
}
