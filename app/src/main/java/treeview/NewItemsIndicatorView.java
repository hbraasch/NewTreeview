package treeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.treeapps.newtreeview.R;

import java.util.ArrayList;

import utils.TreeUtils;

/**
 * Used as an treeview overlay to indicate where changes occurred
 */
public class NewItemsIndicatorView extends View {

	Context context;
	Rect indicatorRectBorder;
	Paint paintIndicator;
	Paint paintMixedIndicator;
	int intIndicatorWidthInPx;
	BitmapShader bmsMixedIndicator;
	Matrix matrix;

	public static enum EnumIndicatorItemType {
		NEW, PARENT_OF_NEW, NEW_AND_PARENT_OF_NEW
	}

	private class IndicatorItem {
		public int intStartPosPx;
		public int intStopPosPx;
		public EnumIndicatorItemType enumIndicatorItemType;
	}
	private ArrayList<IndicatorItem> indicatorItems = new ArrayList<>();
	private ArrayList<Treeview.ListViewListItem> listItems;

	public NewItemsIndicatorView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public NewItemsIndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public NewItemsIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		init();
	}
	
	private void init() {
		intIndicatorWidthInPx = TreeUtils.dpToPx(context, context.getResources().getInteger(R.integer.newitems_indicator_view_width_dp));
		paintIndicator = new Paint();
		paintIndicator.setDither(true);
		paintIndicator.setAntiAlias(true);
		paintMixedIndicator = createCheckerBoard(intIndicatorWidthInPx);

	}
	
	private Paint createCheckerBoard(int pixelSize)
	{
	    Bitmap bitmap = Bitmap.createBitmap(pixelSize, pixelSize, Bitmap.Config.ARGB_8888);

	    Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
	    fill.setStyle(Paint.Style.FILL);
	    fill.setColor(Color.rgb(0xFF, 0xCC, 0x00));

	    Canvas canvas = new Canvas(bitmap);
	    Rect rect = new Rect(0, 0, pixelSize, pixelSize/2);
	    canvas.drawRect(rect, fill);
	    rect.offset(0, pixelSize/2);
	    fill.setColor(Color.RED);
	    canvas.drawRect(rect, fill);

	    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    bmsMixedIndicator = new BitmapShader(bitmap, BitmapShader.TileMode.REPEAT, BitmapShader.TileMode.REPEAT);
	    matrix = new Matrix();
	    matrix.setRotate(0);
	    bmsMixedIndicator.setLocalMatrix(matrix);
	    paint.setShader(bmsMixedIndicator);
	   
	    return paint;
	}
	
	
	public void updateListItems(ArrayList<Treeview.ListViewListItem> listItems) {
		this.listItems = listItems;
		this.invalidate();
	}

	private void updateIndicators(ArrayList<Treeview.ListViewListItem> listItems, int intNoteLengthInPx) {
		
		indicatorItems.clear();
		if (listItems == null) return;
		if (listItems.size() == 0) return;
		// First pass, find how many displayed items are new (or lowest leaves pointing to undisplayed item)
		// Also, mark their position in the list, and mark their type
		int intItemLengthPx = intNoteLengthInPx/listItems.size();
		int intItemNum = 0;
		for (Treeview.ListViewListItem objListItem: listItems) {
			IndicatorItem indicatorItem;
			switch (objListItem.getEnumNewItemType()) {
			case OLD:
				break;
			case ROOT_PARENT_OF_NEW:
			case PARENT_OF_NEW:
				// Only display if its collapsed and there is a a new child underneath
				if (objListItem.getTreeviewNode().getExpansionState() == Treeview.EnumTreenodeExpansionState.COLLAPSED) {
					indicatorItem = new IndicatorItem();
					indicatorItem.enumIndicatorItemType = EnumIndicatorItemType.PARENT_OF_NEW;
					indicatorItem.intStartPosPx = intItemNum * intItemLengthPx;
					indicatorItem.intStopPosPx = indicatorItem.intStartPosPx + intItemLengthPx;
					indicatorItems.add(indicatorItem);
				} else {
					// Do not display
				}
				break;
			case NEW_AND_ROOT_PARENT_OF_NEW:
			case NEW_AND_PARENT_OF_NEW:
				indicatorItem = new IndicatorItem();
				indicatorItem.enumIndicatorItemType = EnumIndicatorItemType.NEW_AND_PARENT_OF_NEW;
				indicatorItem.intStartPosPx = intItemNum * intItemLengthPx;
				indicatorItem.intStopPosPx = indicatorItem.intStartPosPx + intItemLengthPx;
				indicatorItems.add(indicatorItem);
				break;				
			case NEW:
				indicatorItem = new IndicatorItem();
				indicatorItem.enumIndicatorItemType = EnumIndicatorItemType.NEW;
				indicatorItem.intStartPosPx = intItemNum * intItemLengthPx;
				indicatorItem.intStopPosPx = indicatorItem.intStartPosPx + intItemLengthPx;
				indicatorItems.add(indicatorItem);
				break;
			}	
			intItemNum += 1;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		Rect rect = new Rect();
		getDrawingRect(rect);
		int intLineLeftPos = rect.right - intIndicatorWidthInPx;

		updateIndicators(listItems, rect.bottom);
		for (IndicatorItem objIndicatorItem: indicatorItems) {
			switch (objIndicatorItem.enumIndicatorItemType) {
			case NEW:
				indicatorRectBorder = new Rect(intLineLeftPos,objIndicatorItem.intStartPosPx,
						intLineLeftPos + intIndicatorWidthInPx, objIndicatorItem.intStopPosPx);
				paintIndicator.setColor(Color.RED);
				paintIndicator.setStyle(Paint.Style.FILL);
				canvas.drawRect(indicatorRectBorder, paintIndicator);
				break;
			case PARENT_OF_NEW:				
				indicatorRectBorder = new Rect(intLineLeftPos,objIndicatorItem.intStartPosPx,
						intLineLeftPos + intIndicatorWidthInPx, objIndicatorItem.intStopPosPx);
				paintIndicator.setColor(Color.rgb(0xFF, 0xCC, 0x00)); // Yellow
				paintIndicator.setStyle(Paint.Style.FILL);
				canvas.drawRect(indicatorRectBorder, paintIndicator);
				break;
			case NEW_AND_PARENT_OF_NEW:
				indicatorRectBorder = new Rect(intLineLeftPos,objIndicatorItem.intStartPosPx,
						intLineLeftPos + intIndicatorWidthInPx, objIndicatorItem.intStopPosPx);
				canvas.drawRect(indicatorRectBorder, paintMixedIndicator);
				break;
			}
		}
	}
}
