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

import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.TrightSidePaths;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.FD;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.ObjectFactory;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.Tdependencies;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.TleftSidePaths;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sviro
 */
public class FDProcessorTest {

  public FDProcessorTest() {
  }

  @Test(expected = InterruptedException.class)
  public void testProcessNull() throws InterruptedException {
    System.out.println("processNull");
    final List<FD> expResult = new ArrayList<FD>(0);
    final List<FD> result = new FDProcessor().process(null);
    assertEquals(expResult, result);
  }

  @Test(expected=InterruptedException.class)
  public void testProcessEmpty() throws InterruptedException {
    System.out.println("processEmpty");
    final InputStream s = new ByteArrayInputStream("".getBytes());
    final List<FD> result = new FDProcessor().process(s);
  }

  @Test(expected=InterruptedException.class)
  public void testProcessEmpty2() throws InterruptedException {
    System.out.println("processEmpty2");
    final InputStream s = new ByteArrayInputStream("<!-- Inferred on Sat Jul 24 17:53:09 CEST 2010 by Trivial IG Generator, Modular Simplifier, Trivial DTD exporter -->\n\n\n".getBytes());
    final List<FD> result = new FDProcessor().process(s);
  }
  
  private static final String FD1 = 
          "<dependencies>" +
            "<dependency>" +
		"<leftSidePaths>" +
			"<path>path1</path>" +
			"<path>path2</path>" +
			"<path>path3</path>" +
		"</leftSidePaths>" +
		"<rightSidePaths>" +
			"<path>path4</path>" +
		"</rightSidePaths>" +
	"</dependency>" +
"</dependencies>";
            
  
  @Test
  public void testProcess() throws InterruptedException {
    System.out.println("process");
    final InputStream s = new ByteArrayInputStream(FD1.getBytes());
    List<FD> result = new FDProcessor().process(s);
    List<FD> expected = getFD1expected();
    assertEquals(expected.size(), result.size());
    assertEquals(expected, result);
  }

  private List<FD> getFD1expected() {
    ObjectFactory objectFactory = new ObjectFactory();
    Tdependencies dependencies = objectFactory.createTdependencies();
    List<FD> fds = dependencies.getDependency();
    FD fd = objectFactory.createFD();
    fds.add(fd);
    TleftSidePaths leftSide = objectFactory.createTleftSidePaths();
    fd.setLeftSidePaths(leftSide);
    List<String> leftPaths = leftSide.getPath();
    leftPaths.add("path1");
    leftPaths.add("path2");
    leftPaths.add("path3");
    
    
    TrightSidePaths rightSide = objectFactory.createTrightSidePaths();
    fd.setRightSidePaths(rightSide);
    rightSide.setPath("path4");
    
    return fds;
  }
}
