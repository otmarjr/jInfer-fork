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
package cz.cuni.mff.ksi.jinfer.functionalDependencies;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * 
 * @author sviro
 */
@ServiceProvider(service = Processor.class)
public class FDProcessor implements Processor<Element> {

    @Override
    public FolderType getFolder() {
        return FolderType.FD;
    }

    @Override
    public String getExtension() {
        return "fd";
    }

    @Override
    public boolean processUndefined() {
        return false;
    }

    @Override
    public List<Element> process(InputStream s) throws InterruptedException {
        return new ArrayList<Element>();
    }
    
    @Override
    public Class<?> getResultType() {
        return Element.class;
    }
    
}