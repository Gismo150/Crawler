package Models;

import main.Config;
import org.eclipse.egit.github.core.Repository;

import java.util.Arrays;
import java.util.List;

/**
 * Basic build systems to filter for.
 *
 * CMAKE:
 * CMakeLists.txt file contains a set of directives and instructions describing the project's source files and targets.
 * Hence, only this file is required to classify a repository that uses the CMake-Toolchain.
 *
 * AUTOTOOLS:
 * The autoconf program produces a configure script from either configure.in or configure.ac (see note below).
 * The automake program produces a Makefile.in from a Makefile.am.
 * The configure script is run to produce one or more Makefile files from Makefile.in files.
 * The make program uses the Makefile to compile the program.
 *
 * TLDR:
 * This boolean expression is used to classify a repository that uses Autotools:
 * (configure.ac || configure.in) && Makefile.am
 *
 * Note: The configure.in name used to be standard. However, the GNU documentation now recommends configure.ac as it is
 * more obvious which program should be used when processing it. The files perform the same purpose and have the same format.
 * The only difference is the name.
 *
 * MAKE:
 * The make utility requires a file, Makefile (or makefile), which defines set of tasks to be executed
 *
 * UNKNOWN:
 * If you are not interested in filtering repositories by its build systems, then simply leave the buildSystem property empty
 * within the config.properties file. The Crawler will then simply filter and match for all repos that are written in one specific
 * programming language.
 *
 * @author Daniel Braun
 */

public enum BuildSystem {
    CMAKE("CMAKE", new String[] {"CMakeLists.txt"}),
    AUTOTOOLS("AUTOTOOLS", new String[]{"configure.ac", "configure.in","Makefile.am"}),
    MAKE("MAKE", new String []{"Makefile"}),
    CUSTOM("CUSTOM", new String[] {""}),
    UNKNOWN("UNKNOWN", new String []{});
    private String name;
    private String[] buildFiles;
    private List<String> filePaths;

    BuildSystem(String name, String[] buildFiles) {
        this.name = name;
        this.buildFiles = buildFiles;
    }

    @Override
    public String toString() {
        return this.name;
    }


    public static BuildSystem getBuildType(String name) {
        switch (name) {
            case "CMAKE":
                return BuildSystem.CMAKE;
            case "AUTOTOOLS":
                return BuildSystem.AUTOTOOLS;
            case "MAKE":
                return BuildSystem.MAKE;
            case "CUSTOM":
                return BuildSystem.CUSTOM;
            default:
                return BuildSystem.UNKNOWN;
        }
    }

    public List<String> getFilePaths() {
        return filePaths;
    }

    public void setFilePaths(List<String> filePaths) {
        this.filePaths = filePaths;
    }

    public String[] getBuildFiles() {return buildFiles;}
}

