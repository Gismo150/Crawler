package configuration;

import java.util.List;

/**
 * Defining Build System with public name and buildFiles fields so the objects can be
 * serialized. The end-user has the option to define custom build systems.
 *
 * @author Ali Bozna
 */
public class BuildSystem {
    public static BuildSystem UNKOWN = new BuildSystem("UNKOWN", new String[]{});

    public String name;
    public String[] buildFiles;

    private List<String> filePaths;

    public BuildSystem(String name, String[] buildFiles) {
        this.buildFiles = buildFiles;
        this.name = name;
    }

    public BuildSystem() {
    }

    public List<String> getFilePaths() {
        return this.filePaths;
    }

    public void setFilePaths(List<String> filePaths) {
        this.filePaths = filePaths;
    }
}
