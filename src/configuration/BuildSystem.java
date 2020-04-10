package configuration;

/**
 * Defining Build System with public name and buildFiles fields so the objects can be
 * serialized. The end-user has the option to define custom build systems.
 *
 * @author Ali Bozna
 */
public class BuildSystem {
    public String name;
    public String[] buildFiles;

    public BuildSystem(String name, String[] buildFiles) {
        this.buildFiles = buildFiles;
        this.name = name;
    }

    public BuildSystem() {
    }
}
