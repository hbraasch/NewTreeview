package utils;

import android.content.Context;
import android.util.DisplayMetrics;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.GsonBuilder;
import note.NoteNodeBase;
import note.NoteNodeTypeAdapter;

/**
 * Created by HeinrichWork on 23/04/2015.
 */
public class TreeUtils {

    public static double pxToSp(Context context, double px) {
        double scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    }

    public static float spToPx(Context context, float sp) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return sp* scaledDensity;
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static <T> void reorderArrayList(ArrayList<T> list, T node, int intNewPosition) {
        // Clamping
        intNewPosition = Math.max(0, intNewPosition);
        intNewPosition = Math.min(intNewPosition, list.size() - 1);

        // Swap
        int intCurrentPosition = list.indexOf(node);
        Collections.swap(list, intCurrentPosition, intNewPosition);

    }
}
