package treeview;

import java.lang.reflect.Type;

import com.google.gson.GsonBuilder;
import note.NoteNode;
import note.NoteNodeTypeAdapter;

/**
 * Created by HeinrichWork on 23/04/2015.
 */
public class TreeviewUtils {
    /**
     * Json deserializer - REMEMBER to add type adapters for new abstract classed and their derivatives
     * e.g. gson.registerTypeAdapter(Payload.class, new PayloadTypeAdapter<>());
     * @param strSerialize
     * @param objType
     * @param <T>
     * @return
     */
    // Note: Here is example of getting type of ArrayList:
    // java.lang.reflect.Type collectionType = new TypeToken<ArrayList<clsListViewState>>(){}.getType();
    public static <T> T deserializeFromString(String strSerialize, Type objType) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(NoteNode.class, new NoteNodeTypeAdapter<>());
        return gson.create().fromJson(strSerialize, objType);
    }

    /**
     * Json serializer - REMEMBER to add type adapters for new abstract classed and their derivatives
     * e.g. gson.registerTypeAdapter(Payload.class, new PayloadTypeAdapter<>());
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String serializeToString(T obj) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(NoteNode.class, new NoteNodeTypeAdapter<>());
        String strJson = gson.create().toJson(obj);
        return strJson;
    }
}
