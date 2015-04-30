package treeview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.Collections;

import com.treeapps.newtreeview.R;

/**
 * Created by HeinrichWork on 23/04/2015.
 */
public class TreeUtils {


    public static <T> void reorderArrayList(ArrayList<T> list, T node, int intNewPosition) {
        // Clamping
        intNewPosition = Math.max(0, intNewPosition);
        intNewPosition = Math.min(intNewPosition, list.size() - 1);

        // Swap
        int intCurrentPosition = list.indexOf(node);
        Collections.swap(list, intCurrentPosition, intNewPosition);
    }

    public static LayerDrawable generateOverlaidThumbnailDrawable(Context context, Drawable drawableThumbnail, boolean boolIsAnnotated, boolean boolIsWebImage)
    {
        Resources resources = context.getResources();
        LayerDrawable myMediaPreviewLayerDrawable = (LayerDrawable) context.getResources()
                .getDrawable(R.drawable.treenode_media_preview_layers);

        myMediaPreviewLayerDrawable.setDrawableByLayerId(R.id.media_preview_layer_background,drawableThumbnail);
        if (boolIsWebImage) {
            myMediaPreviewLayerDrawable.setDrawableByLayerId(R.id.media_preview_layer_web_image, resources.getDrawable(R.mipmap.treenode_www));
        }
        if (boolIsAnnotated) {
            myMediaPreviewLayerDrawable.setDrawableByLayerId(R.id.media_preview_layer_annotated,
                    resources.getDrawable(R.mipmap.treenode_annotation));
        }

        return myMediaPreviewLayerDrawable;

    }
}
