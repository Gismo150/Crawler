package utils;

import Models.RMetaData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Simple Json file writer.
 *
 * @author Daniel Braun
 */
public class JsonWriter {

    private final String repositoriesJsonPathAndName;

    // static variable single_instance of type Singleton
    private static JsonWriter single_instance = null;
    private Gson gson;
    private boolean isNewFile;


    private JsonWriter()  {
        gson = new GsonBuilder().create();
        repositoriesJsonPathAndName = FileHelper.getRepositoriesJsonFilePath();
        isNewFile = FileHelper.fileExistsOrCreate(repositoriesJsonPathAndName);
    }

    // static method to create instance of Singleton class
    public static JsonWriter getInstance()
    {
        if (single_instance == null)
            single_instance = new JsonWriter();

        return single_instance;
    }

    public void writeRepositoryToJson(RMetaData repoObject){
        if(isNewFile){// Create a new json array and append the current repoObject
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(gson.toJsonTree(repoObject, RMetaData.class));
            writeToFile(jsonArray);
            isNewFile = false;
        } else {// Read the existing json file and append current object
            JsonArray jsonArray = JsonReader.getInstance().getJsonArray();
            jsonArray.add(gson.toJsonTree(repoObject, RMetaData.class));
            writeToFile(jsonArray);
        }
    }

    private void writeToFile(JsonArray jsonArray){
        try (FileWriter file = new FileWriter(repositoriesJsonPathAndName)) {

            file.write(jsonArray.toString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateRepositoryInJsonArray (RMetaData rMetaData, int arrayIndex){
        JsonArray jsonArray = JsonReader.getInstance().getJsonArray();
        jsonArray.set(arrayIndex, gson.toJsonTree(rMetaData, RMetaData.class));
        writeToFile(jsonArray);
    }
}
