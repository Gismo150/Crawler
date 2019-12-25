package Models;

import org.eclipse.egit.github.core.Repository;

import java.util.List;

/**
 * Build systems to search for or recognised by the system.
 *
 * @author Daniel Braun
 */

public enum BuildSystem {
    CMAKE("CMAKE"),
    UNKNOWN("UNKNOWN");
    private String name;
    private List<String> filePaths;

    BuildSystem(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }


    public static BuildSystem getBuildType(String name) {
        switch (name) {
            case "CMAKE":
                return BuildSystem.CMAKE;
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
}

