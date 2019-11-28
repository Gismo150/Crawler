package utils;

import main.Config;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileHelper {

    public static boolean fileExistsOrCreate(String PathAndName) {
        File f = new File(PathAndName);
        try {
            return f.createNewFile(); // The function returns true if the file does not exist and a new file is created.
            // It returns false if the filename already exists.
        } catch (IOException e) {
            System.err.println("File couldn't be created.\nShutting down.");
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return false;
    }

    public static boolean checkFileExists(String path) {
        File tempFile = new File(path);
        return tempFile.exists();
    }

    public static String getRepositoriesJsonFilePath() {
        if (Config.FILEPATH.isEmpty()) {
            return System.getProperty("user.dir") + "/" + Config.JSONFILENAME;
        } else {
            return Config.FILEPATH + "/" + Config.JSONFILENAME;
        }
    }

    public static String getResultsJsonFilePath() {
        if (Config.FILEPATH.isEmpty()) {
            return System.getProperty("user.dir") + "/" + Config.RESULTFILENAME;
        } else {
            return Config.FILEPATH + "/" + Config.RESULTFILENAME;
        }
    }

    public static List<String> getAllFileNamesOfDir(String dir) {
        try (Stream<Path> walk = Files.walk(Paths.get(dir))) {

            List<String> result = walk.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
