package utils;

import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

import note.NoteNodeBase;
import note.NoteNodeTypeAdapter;

/**
 * Created by HeinrichWork on 29/04/2015.
 */
public class Utils {
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
        gson.registerTypeAdapter(NoteNodeBase.class, new NoteNodeTypeAdapter<>());
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
        gson.registerTypeAdapter(NoteNodeBase.class, new NoteNodeTypeAdapter<>());
        String strJson = gson.create().toJson(obj);
        return strJson;
    }
}
