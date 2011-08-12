/*
 * Copyright (C) 2011 vektor
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
package cz.cuni.mff.ksi.jinfer.iss.experiments;

import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.GlpkUtils;

/**
 * Provides the info about the system (CPU, RAM, OS, Java, etc) in textual form.
 *
 * Info that is not easily obtained from within Java has to be set in constants,
 * though.
 *
 * @author vektor
 */
public final class SystemInfo {

  private SystemInfo() {
  }

  /** Number of processor cores. */
  private static final int PROCESSOR_CORES = 2;
  /** Clock speed in MHz. */
  private static final int PROCESSOR_CLOCK_SPEED = 2330;
  /** Processor architecture/family. */
  private static final String PROCESSOR_ARCH = "Intel(R) Core(TM)2 Duo CPU     E6550  @ 2.33GHz";
  /** RAM size in MB. */
  private static final int RAM_SIZE = 4096;

  /** Number of processor cores. */
  //private static final int PROCESSOR_CORES = 2;
  /** Clock speed in MHz. */
  //private static final int PROCESSOR_CLOCK_SPEED = 2330;
  /** Processor architecture/family. */
  //private static final String PROCESSOR_ARCH = "Intel(R) Core(TM)2 Duo CPU     E6550  @ 2.33GHz";
  /** RAM size in MB. */
  //private static final int RAM_SIZE = 4096;

  /** OS name. */
  private static final String OS_NAME = System.getProperty("os.name");
  /** OS architecture. */
  private static final String OS_ARCH = System.getProperty("os.arch");
  /** OS version. */
  private static final String OS_VERSION = System.getProperty("os.version");

  /** Java version. */
  private static final String JAVA_VERSION = System.getProperty("java.version");
  /** Java Virtual Machine. */
  private static final String JAVA_VM_NAME = System.getProperty("java.vm.name");

  /**
   * Returns the complete system info in a formatted string.
   *
   * @return String containing formatted info about the CPU, RAM, OS, Java,
   * GLPK etc.
   */
  public static String getInfo() {
    final StringBuilder ret = new StringBuilder();
    ret.append("CPU info")
        .append("\n  ").append(PROCESSOR_ARCH)
        .append("\n  Cores: ").append(PROCESSOR_CORES)
        .append("\n  Clock speed: ").append(PROCESSOR_CLOCK_SPEED).append(" MHz")
        .append("\nMemory info")
        .append("\n  Size: ").append(RAM_SIZE).append(" MB")
        .append("\nOS info")
        .append("\n  Name: ").append(OS_NAME)
        .append("\n  Version: ").append(OS_VERSION)
        .append("\n  Architecture: ").append(OS_ARCH)
        .append("\nJava info")
        .append("\n  Version: ").append(JAVA_VERSION)
        .append("\n  VM: ").append(JAVA_VM_NAME)
        .append("\nGLPK info")
        .append("\n  ").append(GlpkUtils.getVersion());
    if (GlpkUtils.getPath().contains("cygwin")) {
      ret.append(" (probably Cygwin)");
    }
    return ret.toString();
  }

}
