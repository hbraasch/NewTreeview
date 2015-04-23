package treeview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by HeinrichWork on 23/04/2015.
 */
public class IndentableTextView extends TextView {

    private Context context;

    public IndentableTextView(Context context) {
        super(context);
        this.context = context;
    }

    public IndentableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public IndentableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }

}
