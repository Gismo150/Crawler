package utils;

import Models.RMetaData;
import com.google.gson.*;

import java.io.*;

/**
 * Simple Json file reader.
 *
 * @author Daniel Braun
 */
public class JsonReader {

    private final String repositoriesJsonPathAndName;
    // static variable single_instance of type Singleton
    private static JsonReader single_instance = null;
    private Gson gson;
    private boolean isNewFile;
    private JsonArray repositoriesJsonArray;

    private JsonReader()  {
        gson = new Gson();
        repositoriesJsonPathAndName = FileHelper.getRepositoriesJsonFilePath();

        isNewFile = FileHelper.fileExistsOrCreate(repositoriesJsonPathAndName);
        repositoriesJsonArray = readJsonArrayFromFile(repositoriesJsonPathAndName);
    }

    // static method to create instance of Singleton class
    public static JsonReader getInstance()
    {
        if (single_instance == null)
            single_instance = new JsonReader();

        return single_instance;
    }

    public JsonArray readJsonArrayFromFile(String pathToJsonFile) {
        //JSON parser object to parse read file
        if (!isNewFile) {
            JsonParser jsonParser = new JsonParser();

            try (FileReader reader = new FileReader(pathToJsonFile)) {
                //Read JSON file
                Object obj = jsonParser.parse(reader);
                reader.close();

                return (JsonArray) obj;

            } catch (FileNotFoundException e) {
                System.err.println("Json file not found at:" + pathToJsonFile + ".\n Shutting down.");
                System.err.println(e.getMessage());
                System.exit(1);
            } catch (IOException e) {
                System.err.println("IOException.\n Shutting down.");
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
        System.err.println("Json array was not parsed and returned correctly!");
        return new JsonArray();
    }

    public RMetaData deserializeRepositoryFromJsonArray(int arrayIndex){
        if(repositoriesJsonArray.size() > arrayIndex) {
            JsonElement jsonElement = repositoriesJsonArray.get(arrayIndex);
            if(!jsonElement.isJsonNull())
                return gson.fromJson(jsonElement.toString(), RMetaData.class);
        }
        System.err.println("Array index out of bound of JsonArray.");
        return null;
    }

    public String getRepositoryStringFromJsonArray(int arrayIndex) {
        reloadJsonArrayFromFile();
        if(repositoriesJsonArray.size() > arrayIndex) {
            JsonElement jsonElement = repositoriesJsonArray.get(arrayIndex);
            if(!jsonElement.isJsonNull())
                return jsonElement.toString();
        }
        System.err.println("Array index out of bound of JsonArray.");
        return null;
    }

    public JsonObject getRepositoryJsonObjectFromJsonArray(int arrayIndex) {
        reloadJsonArrayFromFile();
        if(repositoriesJsonArray.size() > arrayIndex) {
            JsonObject jsonObject = repositoriesJsonArray.get(arrayIndex).getAsJsonObject();
            if(!jsonObject.isJsonNull())
                return jsonObject;
        }
        System.err.println("Array index out of bound of JsonArray.");
        return null;
    }

    public JsonArray getJsonArray(){
        return repositoriesJsonArray;
    }

    public void checkArgInRange(int arrayIndex) {
        if(arrayIndex > repositoriesJsonArray.size()) {
            System.err.println("ERROR: Index out of bounds.\nThe repositories.json maximum array size is "
                    + repositoriesJsonArray.size()+ ".\nThe provided number '"
                    + arrayIndex +"' is out of bounds.\nAborting.");
            System.exit(1);
        }
    }

    private void reloadJsonArrayFromFile() {
        repositoriesJsonArray = readJsonArrayFromFile(repositoriesJsonPathAndName);
    }
}
