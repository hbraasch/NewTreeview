package shared;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.DisplayMetrics;

import com.google.gson.GsonBuilder;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import note.UtilsNote;

/**
 * Created by HeinrichWork on 29/04/2015.
 */
public class UtilsShared {
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
        gson.registerTypeAdapter(Node.class, new UtilsNote<>());
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
        gson.registerTypeAdapter(Node.class, new UtilsNote<>());
        String strJson = gson.create().toJson(obj);
        return strJson;
    }

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



    public static interface OnGenerateThumbnailImageCompleteListener {
        public void onComplete(boolean boolIsSuccess, String strErrorMessage, Drawable drawable);
    }

    public static void generateThumbnailImage(final Context context, Uri origUri,
                                final OnGenerateThumbnailImageCompleteListener onGenerateThumbnailImageCompleteListener) {

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        createCompressedBitmapsFromImageResource(context, origUri, dm.widthPixels, dm.heightPixels, new OnGenerateImageBitmapsCompleteListener() {
            @Override
            public void onComplete(boolean boolIsSuccess, String strErrorMessage, Bitmap bmThumbnail, Bitmap bmFull) {
                if (bmThumbnail == null) {
                    onGenerateThumbnailImageCompleteListener.onComplete(false, strErrorMessage, null);
                } else {
                    // Save thumbnail for all the image related resource types and save to local repository
                    onGenerateThumbnailImageCompleteListener.onComplete(true, "", new BitmapDrawable(context.getResources(), bmThumbnail));
                }
            }
        });
    }

    public static interface OnGenerateImageBitmapsCompleteListener {
        public void onComplete(boolean boolIsSuccess, String strErrorMessage, Bitmap bmThumbnail, Bitmap bmFull);
    }

    public static void createCompressedBitmapsFromImageResource(Context context,
                                                                Uri uri,
                                                                int maxWidthFull, int maxHeightFull,
                                                                OnGenerateImageBitmapsCompleteListener onGenerateImageBitmapsCompleteListener)  {

        int viewWidth  = maxWidthFull;
        int viewHeight = maxHeightFull;
        Bitmap bmThumbnail = null;
        Bitmap bmFull = null;
        String strUri = uri.toString();

        if(!strUri.startsWith("/", 0) && !strUri.startsWith("file://", 0))
        {
             // Read (might need to be another thread to stop blocking)
            bmFull = UtilsShared.retrieveRemoteImage(context, strUri, viewWidth, viewHeight);
        }
        else
        {
            try {
                bmFull = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        bmThumbnail = ThumbnailUtils.extractThumbnail(bmFull, dpToPx(context, 50), dpToPx(context, 50));

        onGenerateImageBitmapsCompleteListener.onComplete(true,"", bmThumbnail, bmFull);

    }


    public static Bitmap retrieveRemoteImage(Context context, String strUri, int viewWidth, int viewHeight)
    {
        InputStream is = null;
        Bitmap bitmap = null;
        URL myUrl;

        if (strUri.startsWith("http", 0))
        {
            try{
                myUrl = new URL(strUri);
                is = (InputStream)myUrl.getContent();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }  else if (strUri.startsWith("content:", 0) || strUri.startsWith("file:", 0))
        {
            Uri uriIn = Uri.parse(strUri);
            // From http://stackoverflow.com/questions/11764392/getting-an-image-from-gallery-on-from-the-picasa-google-synced-folders-doesn
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                ParcelFileDescriptor parcelFileDescriptor;
                try {
                    parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uriIn, "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    is = new FileInputStream(fileDescriptor);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                try {
                    is = (FileInputStream)context.getContentResolver().openInputStream(uriIn);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        if(is != null)
        {
            BitmapFactory.Options options = null;

            // If a valid maximum size is given then we calculate the sampleSize of the bitmap
            // in such a way that the image fits entirely in that size.
            if(viewWidth > 0 && viewHeight > 0)
            {
                options = new BitmapFactory.Options();
                // First run: Get size of image without loading to memory
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(is, null, options);

                // Compute sampleSize of resulting bitmap
                options.inSampleSize = UtilsShared.calculateInSampleSize(options, viewWidth, viewHeight);

                // Reset file pointer and read image for real
                options.inJustDecodeBounds = false;

                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Re-open the input stream in order to reset to start of the stream.
                // It would also be possible to use an InputStream instead a FileInputStream and then use
                // is.getChannel().position(0) to get back to the start, but that does not support Picasa
                try {
                    Uri uriIn = Uri.parse(strUri);
                    is = (FileInputStream)context.getContentResolver().openInputStream(uriIn);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if(is != null)
            {
                bitmap = (options == null)?BitmapFactory.decodeStream(is):BitmapFactory.decodeStream(is, null, options);
            }
        }
        return bitmap;
    }

    public static String getLocalPathFromUri(Context context, Uri uri) {
        String path = "";
        String[] filePathColumn;
        Cursor cursor;
        int columnIndex;

        String temp = uri.toString();
        if (temp.startsWith("/")) {
            return temp;
        }

        filePathColumn = new String[2];
        filePathColumn[0] = MediaStore.Images.Media.DATA;
        filePathColumn[1] = MediaStore.Images.Media.DISPLAY_NAME;

        // Retrieve the file path from the database
        try {
            cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        } catch (Exception e) {
            return path;
        }
        if (cursor != null) {
            cursor.moveToFirst();
            columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            path = cursor.getString(columnIndex);
            columnIndex = cursor.getColumnIndex(filePathColumn[1]);
            cursor.close();
        }

        return path;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((height / inSampleSize) > reqHeight && (width / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static <T extends Node> void addNodeBeforeOrAfter(ArrayList<T> rootChildNodes, T noteNodeSource, T noteNodeTarget, boolean boolIsBefore) {
        // Remove from where its at this stage
        T objSourceParentTreeNode = (T) noteNodeSource.getParent();
        if (objSourceParentTreeNode == null) {
            rootChildNodes.remove(noteNodeSource);
        } else {
            objSourceParentTreeNode.getChildNodes().remove(noteNodeSource);
        }
        // Add as item before target
        T objTargetParentTreeNode = (T) noteNodeTarget.getParent();
        ArrayList<T> targetParentChildren;
        if (objTargetParentTreeNode == null) {
            targetParentChildren = rootChildNodes;
        } else {
            targetParentChildren = objTargetParentTreeNode.getChildNodes();
        }
        int intTargetOrder = targetParentChildren.indexOf(noteNodeTarget);
        int intSourceOrder  = (boolIsBefore)?intTargetOrder:intTargetOrder + 1;
        if (objTargetParentTreeNode ==  null) {
            rootChildNodes.add(intSourceOrder, noteNodeSource);
            noteNodeSource.setParent(null);
            noteNodeSource.setDirty(true);
        } else {
            objTargetParentTreeNode.addChildNode(noteNodeSource, intSourceOrder);
        }
    }

    public static <T extends Node> void addNodeBelow(ArrayList<T> rootChildNodes, T noteNodeSource, T noteNodeTarget) {
        // Remove from where its at this stage
        T objSourceParentTreeNode = (T) noteNodeSource.getParent();
        if (objSourceParentTreeNode == null) {
            rootChildNodes.remove(noteNodeSource);
        } else {
            objSourceParentTreeNode.getChildNodes().remove(noteNodeSource);
        }
        // Add below
        noteNodeTarget.addChildNode(noteNodeSource);

    }
}
