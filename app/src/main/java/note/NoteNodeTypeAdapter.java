package note;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import treeview.Node;
import treeview.TreeviewNode;

/**
 * Created by HeinrichWork on 25/04/2015.
 */
public class NoteNodeTypeAdapter<T extends Node> implements JsonSerializer<T>, JsonDeserializer<T> {

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        JsonElement jsonElement = new Gson().toJsonTree(src);
        result.add("properties",  jsonElement); // new JsonPrimitive(strSerial));
        return result;
    }


    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");

        try {
            String strPackage = "note.";
            Class myClass = Class.forName(strPackage + type);
            T myObject = (T) new Gson().fromJson(element.toString(),myClass);
            return myObject ;
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + type, cnfe);
        }
    }
}
