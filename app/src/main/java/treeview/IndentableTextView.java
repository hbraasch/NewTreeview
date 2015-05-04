package treeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

import com.treeapps.newtreeview.R;

import shared.Utils;

/**
 * Created by HeinrichWork on 23/04/2015.
 */
public class IndentableTextView extends TextView {

    private static final int TEXT_HORIZONTAL_OFFSET_IN_DP =  5;

    private Context context;

    private Rect drawingRect = new Rect();
    private Rect drawingRectHeightAjusted;
    int colorFrom;
    int colorTo;
    Paint paintTextWhite;
    Paint paintTextBlack;
    Paint paintPlainBlack;
    Paint paintIndentBackground;
    Paint paintSelectOutline;
    Paint paintDragDropGuideLeft;
    Paint paintDragDropGuideRight;
    private LinearGradient myLinearGradient;
    int intColorStart;
    int intColorEnd;

    Treeview.ListViewListItem listViewListItem;
    int intSelectColor;
    float fltLineWidthInDp;
    int intRadiusDefaultInPx;
    float fltRadiusInPx;
    int intThumbnailHeightInPx;
    int intThumbnailWidthInPx;
    int intTabWidthInPx;
    int intIndentLevelAmountMax;
    int inTextHorizontalOffsetInPx = Utils.dpToPx(getContext(), TEXT_HORIZONTAL_OFFSET_IN_DP);

    private void init() {

        fltLineWidthInDp = Utils.dpToPx(getContext(), getResources().getInteger(R.integer.treeview_select_line_width_in_dp));
        intRadiusDefaultInPx = Utils.dpToPx(getContext(), getResources().getInteger(R.integer.treeview_indent_radius_in_dp));

        paintPlainBlack = new Paint();
        paintPlainBlack.setDither(true);
        paintPlainBlack.setAntiAlias(true);
        paintPlainBlack.setColor(Color.BLACK);

        paintIndentBackground = new Paint();

        paintSelectOutline = new Paint();
        paintSelectOutline.setDither(true);
        paintSelectOutline.setAntiAlias(true);
        paintSelectOutline.setColor(getResources().getColor(R.color.treeview_select_outline_color));
        paintSelectOutline.setStyle(Paint.Style.STROKE);
        paintSelectOutline.setStrokeWidth(fltLineWidthInDp * 2);

        paintDragDropGuideLeft = new Paint();
        paintDragDropGuideLeft.setDither(true);
        paintDragDropGuideLeft.setAntiAlias(true);
        paintDragDropGuideLeft.setColor(Color.GRAY);
        paintDragDropGuideLeft.setStyle(Paint.Style.STROKE);
        paintDragDropGuideLeft.setPathEffect(new DashPathEffect(new float[]{2 * fltLineWidthInDp, 2 * fltLineWidthInDp}, 0));
        paintDragDropGuideLeft.setStrokeWidth(fltLineWidthInDp * 1);

        paintDragDropGuideRight = new Paint();
        paintDragDropGuideRight.setDither(true);
        paintDragDropGuideRight.setAntiAlias(true);
        paintDragDropGuideRight.setColor(Color.LTGRAY);
        paintDragDropGuideRight.setStyle(Paint.Style.STROKE);
        paintDragDropGuideRight.setPathEffect(new DashPathEffect(new float[]{2 * fltLineWidthInDp, 2 * fltLineWidthInDp}, 0));
        paintDragDropGuideRight.setStrokeWidth(fltLineWidthInDp * 1);

        setWillNotDraw(false);

    }

    public IndentableTextView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public IndentableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public IndentableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void setProperties(Treeview.ListViewListItem listViewListItem,
                              int intMediaPreviewImageHeightInPx, int intMediaPreviewImageWidthInPx,
                              int intIndentLevelAmountMax,
                              int intSelectColor,
                              int intTextSizeInSp,
                              int intIndentRadiusInDp) {

        this.listViewListItem = listViewListItem;

        this.intThumbnailHeightInPx = intMediaPreviewImageHeightInPx;
        this.intThumbnailWidthInPx = intMediaPreviewImageWidthInPx;

        this.intIndentLevelAmountMax = intIndentLevelAmountMax;

        this.intSelectColor = intSelectColor;

        paintPlainBlack.setTextSize(Utils.spToPx(getContext(), intTextSizeInSp));

        fltRadiusInPx = (float) Utils.dpToPx(getContext(), intIndentRadiusInDp);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.getDrawingRect(drawingRect);

        this.intTabWidthInPx = drawingRect.width()/intIndentLevelAmountMax;


        // Modify height due to thumbnail size
        drawingRectHeightAjusted = new Rect(drawingRect.left, drawingRect.top, drawingRect.right, Math.max(
                drawingRect.bottom, drawingRect.top + intThumbnailHeightInPx));

        drawingRectHeightAjusted.right = getMeasuredWidth();

        // Calculate where the tab split must occur
        float fltRowWidth = drawingRectHeightAjusted.right - drawingRectHeightAjusted.left;
        float fltTabWidth;
        if (intTabWidthInPx == 0) {
            fltTabWidth = fltRowWidth / intIndentLevelAmountMax;
        } else {
            fltTabWidth = intTabWidthInPx;
        }
        float fltIndentWidth = listViewListItem.getLevel() * fltTabWidth;
        float fltIndentRemainWidth = fltRowWidth - fltIndentWidth;

        RectF indentRect = new RectF();
        indentRect.left = drawingRectHeightAjusted.left + fltIndentWidth;
        indentRect.top = drawingRectHeightAjusted.top;
        indentRect.right = indentRect.left + fltIndentRemainWidth;
        indentRect.bottom = drawingRectHeightAjusted.bottom;

        int intTextYpos = (int) (((drawingRectHeightAjusted.bottom - drawingRectHeightAjusted.top) / 2) - ((paintPlainBlack
                .descent() + paintPlainBlack.ascent()) / 2));

        if (listViewListItem.getTreeviewNode().isSelected()) {
            intColorStart = this.intSelectColor;
            setBackgroundColor(getResources().getColor(R.color.treeview_select_background_fill_color));
        } else {
            intColorStart = getResources().getColor(R.color.treeview_unselect_color);
            setBackgroundColor(getResources().getColor(R.color.treeview_unselect_background_fill_color));
        }
        myLinearGradient = new LinearGradient(indentRect.left, indentRect.top, indentRect.right,
                indentRect.bottom, intColorStart, intColorEnd, Shader.TileMode.CLAMP);
        paintIndentBackground.setShader(myLinearGradient);

        DrawCustomRect(canvas, indentRect, paintIndentBackground);
        canvas.drawText(FormatString(listViewListItem.getTreeviewNode().getDescription(), paintPlainBlack), inTextHorizontalOffsetInPx, intTextYpos, paintPlainBlack);

        super.onDraw(canvas);

    }

    private void DrawCustomRect(Canvas objCanvas, RectF objRect, Paint objPaint) {

        final float fltDiamInPx = fltRadiusInPx * 2;
        Path path = new Path();
        if (listViewListItem.getEnumAboveRelation() == Treeview.EnumTreenodeListItemLevelRelation.SAME) {
            if (listViewListItem.getEnumBelowRelation() == Treeview.EnumTreenodeListItemLevelRelation.SAME) {
                // Top has 90 degree corner, bottom has 90 degree corner
                objCanvas.drawRect(objRect, objPaint);
            } else if (listViewListItem.getEnumBelowRelation() == Treeview.EnumTreenodeListItemLevelRelation.HIGHER) {
                // Top has 90 degree corner, bottom has soft corner
                path.moveTo(objRect.right, objRect.top);
                path.lineTo(objRect.right, objRect.bottom);
                path.lineTo(objRect.left + fltRadiusInPx, objRect.bottom);
                path.addArc(new RectF(objRect.left, objRect.bottom - fltDiamInPx, objRect.left + fltDiamInPx, objRect.bottom),
                        90, 90);
                path.lineTo(objRect.left, objRect.top);
                path.lineTo(objRect.right, objRect.top);
                objCanvas.drawPath(path, objPaint);
            } else if (listViewListItem.getEnumBelowRelation() == Treeview.EnumTreenodeListItemLevelRelation.LOWER) {
                // Top has 90 degree corner, bottom has soft corner
                path.moveTo(objRect.right, objRect.top);
                path.lineTo(objRect.right, objRect.bottom);
                path.lineTo(objRect.left + fltRadiusInPx, objRect.bottom);
                path.addArc(new RectF(objRect.left, objRect.bottom - fltDiamInPx, objRect.left + fltDiamInPx, objRect.bottom),
                        90, 90);
                path.lineTo(objRect.left, objRect.top);
                path.lineTo(objRect.right, objRect.top);
                objCanvas.drawPath(path, objPaint);
            }
        } else if (listViewListItem.getEnumAboveRelation() == Treeview.EnumTreenodeListItemLevelRelation.HIGHER) {
            if (listViewListItem.getEnumBelowRelation() == Treeview.EnumTreenodeListItemLevelRelation.SAME) {
                // Top has soft corner, bottom has 90 degree corner
                path.moveTo(objRect.right, objRect.top);
                path.lineTo(objRect.right, objRect.bottom);
                path.lineTo(objRect.left, objRect.bottom);
                path.lineTo(objRect.left, objRect.top + fltRadiusInPx);
                path.addArc(new RectF(objRect.left, objRect.top, objRect.left + fltDiamInPx, objRect.top + fltDiamInPx), 180,
                        90);
                path.lineTo(objRect.right, objRect.top);
                objCanvas.drawPath(path, objPaint);
            } else if (listViewListItem.getEnumBelowRelation() == Treeview.EnumTreenodeListItemLevelRelation.HIGHER) {
                // Top has soft degree corner, bottom has soft corner
                path.moveTo(objRect.right, objRect.top);
                path.lineTo(objRect.right, objRect.bottom);
                path.lineTo(objRect.left + fltRadiusInPx, objRect.bottom);
                path.addArc(new RectF(objRect.left, objRect.bottom - fltDiamInPx, objRect.left + fltDiamInPx, objRect.bottom),
                        90, 90);

                path.lineTo(objRect.left, objRect.top + fltRadiusInPx);
                path.lineTo(objRect.right, objRect.top);
                path.addArc(new RectF(objRect.left, objRect.top, objRect.left + fltDiamInPx, objRect.top + fltDiamInPx), 180,
                        90);
                path.lineTo(objRect.right, objRect.top);
                objCanvas.drawPath(path, objPaint);

            } else if (listViewListItem.getEnumBelowRelation() == Treeview.EnumTreenodeListItemLevelRelation.LOWER) {
                // Top has soft degree corner, bottom has spiked corner
                path.moveTo(objRect.right, objRect.top);
                path.lineTo(objRect.right, objRect.bottom);
                path.lineTo(objRect.left - fltRadiusInPx, objRect.bottom);
                path.addArc(new RectF(objRect.left - fltDiamInPx, objRect.bottom - fltDiamInPx, objRect.left, objRect.bottom),
                        90, -90);
                path.lineTo(objRect.left, objRect.top + fltRadiusInPx);
                path.lineTo(objRect.right, objRect.top);
                path.addArc(new RectF(objRect.left, objRect.top, objRect.left + fltDiamInPx, objRect.top + fltDiamInPx), 180,
                        90);
                path.lineTo(objRect.right, objRect.top);
                objCanvas.drawPath(path, objPaint);
            }
        } else if (listViewListItem.getEnumAboveRelation() == Treeview.EnumTreenodeListItemLevelRelation.LOWER) {
            if (listViewListItem.getEnumBelowRelation() == Treeview.EnumTreenodeListItemLevelRelation.SAME) {
                // Top has spiked corner, bottom has 90 degree corner
                path.moveTo(objRect.right, objRect.top);
                path.lineTo(objRect.right, objRect.bottom);
                path.lineTo(objRect.left, objRect.bottom);
                path.lineTo(objRect.left, objRect.top + fltRadiusInPx);
                path.addArc(new RectF(objRect.left - fltDiamInPx, objRect.top, objRect.left, objRect.top + fltDiamInPx), 0, -90);
                path.lineTo(objRect.right, objRect.top);
                objCanvas.drawPath(path, objPaint);
            } else if (listViewListItem.getEnumBelowRelation() == Treeview.EnumTreenodeListItemLevelRelation.HIGHER) {
                // Top has spiked corner, bottom has soft corner
                path.moveTo(objRect.right, objRect.top);
                path.lineTo(objRect.right, objRect.bottom);
                path.lineTo(objRect.left + fltRadiusInPx, objRect.bottom);
                path.addArc(new RectF(objRect.left, objRect.bottom - fltDiamInPx, objRect.left + fltDiamInPx, objRect.bottom),
                        90, 90);
                path.lineTo(objRect.left, objRect.top + fltRadiusInPx);
                path.lineTo(objRect.right, objRect.top);
                path.addArc(new RectF(objRect.left - fltDiamInPx, objRect.top, objRect.left, objRect.top + fltDiamInPx), 0, -90);
                path.lineTo(objRect.right, objRect.top);
                objCanvas.drawPath(path, objPaint);
            } else if (listViewListItem.getEnumBelowRelation() == Treeview.EnumTreenodeListItemLevelRelation.LOWER) {
                // Top has spiked corner, bottom has soft corner
                path.moveTo(objRect.right, objRect.top);
                path.lineTo(objRect.right, objRect.bottom);
                path.lineTo(objRect.left + fltRadiusInPx, objRect.bottom);
                path.addArc(new RectF(objRect.left, objRect.bottom - fltDiamInPx, objRect.left + fltDiamInPx, objRect.bottom),
                        90, 90);
                path.lineTo(objRect.left, objRect.top + fltRadiusInPx);
                path.lineTo(objRect.right, objRect.top);
                path.addArc(new RectF(objRect.left - fltDiamInPx, objRect.top, objRect.left, objRect.top + fltDiamInPx), 0, -90);
                path.lineTo(objRect.right, objRect.top);
                objCanvas.drawPath(path, objPaint);
            }
        }

    }

    private String FormatString(String strValue, Paint objPaint) {
        // Truncate if string getting to long. Cannot get wrapping to work yet.
        // Temporary workaround

        float fltTextSize = objPaint.measureText(strValue);
        float[] fltMeasuredWidth = { 0f };
        float fltMaxWidth;

        fltMaxWidth = drawingRectHeightAjusted.right - drawingRectHeightAjusted.left - intThumbnailWidthInPx;
        int intBreakChar = objPaint.breakText(strValue, true, fltMaxWidth, fltMeasuredWidth);
        if (intBreakChar != 0) {
            if (fltMaxWidth <= fltTextSize) {
                return strValue.substring(0, intBreakChar - 3) + "...";
            }
        }
        return strValue;
    }

}
