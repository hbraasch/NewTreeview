package treeview;

import android.graphics.drawable.Drawable;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.treeapps.newtreeview.MainActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by HeinrichWork on 23/04/2015.
 */
public class TreeviewNode extends Node<TreeviewNode> {



    // Node unique key
    private String strTreenodeUuid;

    // Item row data
    private Integer intExpandedIconId;
    private Integer intCollapsedIconId;
    private Integer intEmptyIconId;
    private boolean boolIsNew;
    private  boolean boolIsHidden;
    private boolean boolIsChecked;
    private String strDescription;
    private Drawable drawableMediaPreviewImage;

    // Item folding data
    private Treeview.EnumTreenodeExpansionState enumTreenodeExpansionState;
    private boolean boolIsSelected;

    // Behaviour
    private boolean boolIsDirty;
    private boolean boolIsDeleted;



    // Items parent and children
    private TreeviewNode parent;
    private ArrayList<TreeviewNode> childTreeviewNodes = new ArrayList<TreeviewNode>();

    // Application specific, not to be serialised
    private transient Object tag;

    public TreeviewNode(String strDescription) {
        this.strTreenodeUuid = UUID.randomUUID().toString();

        this.intExpandedIconId = MainActivity.EnumIconImageId.EXPANDED.getValue();
        this.intCollapsedIconId = MainActivity.EnumIconImageId.COLLAPSED.getValue();
        this.intEmptyIconId = MainActivity.EnumIconImageId.EMPTY.getValue();
        this.boolIsNew = false;
        this.boolIsHidden = false;
        this.boolIsChecked = false;
        this.boolIsDirty = false;
        this.boolIsDeleted = false;
        this.strDescription = strDescription;
        this.drawableMediaPreviewImage = null;
        this.enumTreenodeExpansionState = Treeview.EnumTreenodeExpansionState.EMPTY;
    }

    public TreeviewNode(Integer intExpandedIconId, Integer intCollapsedIconId, Integer intEmptyIconId, boolean boolIsNew, boolean boolIsHidden, boolean boolIsChecked, String strDescription, Drawable drawableMediaPreviewImage, boolean boolIsDirty, boolean boolIsDeleted, Treeview.EnumTreenodeExpansionState enumTreenodeExpansionState) {
        this.strTreenodeUuid = UUID.randomUUID().toString();
        this.intExpandedIconId = intExpandedIconId;
        this.intCollapsedIconId = intCollapsedIconId;
        this.intEmptyIconId = intEmptyIconId;
        this.boolIsNew = boolIsNew;
        this.boolIsHidden = boolIsHidden;
        this.boolIsChecked = boolIsChecked;
        this.boolIsDirty = boolIsDirty;
        this.boolIsDeleted = boolIsDeleted;
        this.strDescription = strDescription;
        this.drawableMediaPreviewImage = drawableMediaPreviewImage;
        this.enumTreenodeExpansionState = enumTreenodeExpansionState;
    }

    // Getter Setters

    public String getTreenodeUuid() {
        return strTreenodeUuid;
    }

    public void setTreenodeUuid(String strTreenodeUuid) {
        this.strTreenodeUuid = strTreenodeUuid;
    }

    public Integer getExpandedIconId() {
        return intExpandedIconId;
    }

    public void setExpandedIconId(Integer intExpandedIconId) {
        this.intExpandedIconId = intExpandedIconId;
    }

    public Integer getCollapsedIconId() {
        return intCollapsedIconId;
    }

    public void setCollapsedIconId(Integer intCollapsedIconId) {
        this.intCollapsedIconId = intCollapsedIconId;
    }

    public Integer getEmptyIconId() {
        return intEmptyIconId;
    }

    public void setEmptyIconId(Integer intEmptyIconId) {
        this.intEmptyIconId = intEmptyIconId;
    }

    public boolean isDirty() {
        return boolIsDirty;
    }

    public void setDirty(boolean boolIsDirty) {
        this.boolIsDirty = boolIsDirty;
    }

    public boolean isDeleted() {
        return boolIsDeleted;
    }

    public void setIsDeleted(boolean boolIsDeleted) {
        this.boolIsDeleted = boolIsDeleted;
    }

    public boolean isNew() {
        return boolIsNew;
    }

    public void setIsNew(boolean boolIsNew) {
        this.boolIsNew = boolIsNew;
    }

    public boolean isHidden() {
        return boolIsHidden;
    }

    public void setIsHidden(boolean boolIsHidden) {
        this.boolIsHidden = boolIsHidden;
    }

    public boolean isChecked() {
        return boolIsChecked;
    }

    public void setChecked(boolean boolIsChecked) {
        this.boolIsChecked = boolIsChecked;
    }

    public String getDescription() {
        return strDescription;
    }

    public void setDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public Drawable getDrawableMediaPreviewImage() {
        return drawableMediaPreviewImage;
    }

    public void setDrawableMediaPreviewImage(Drawable drawableMediaPreviewImage) {
        this.drawableMediaPreviewImage = drawableMediaPreviewImage;
    }

    public Treeview.EnumTreenodeExpansionState getExpansionState() {
        return enumTreenodeExpansionState;
    }

    public void setExpansionState(Treeview.EnumTreenodeExpansionState enumTreenodeExpansionState) {
        this.enumTreenodeExpansionState = enumTreenodeExpansionState;
    }

    public ArrayList<TreeviewNode> getChildNodes() {
        return childTreeviewNodes;
    }

    public void setChildTreeviewNodes(ArrayList<TreeviewNode> childTreeviewNodes) {
        this.childTreeviewNodes = childTreeviewNodes;
    }

    public TreeviewNode getParent() {
        return parent;
    }

    public void setParent(TreeviewNode parent) {
        this.parent = parent;
    }

    public boolean getSelected() {
        return boolIsSelected;
    }

    public void setSelected(boolean boolIsSelected) {
        this.boolIsSelected = boolIsSelected;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    // Methods
    public void addNode(TreeviewNode treeviewNode) {
        treeviewNode.parent = this;
        getChildNodes().add(treeviewNode);
    }

    public void toggleExpansionState() {
        Treeview.EnumTreenodeExpansionState enumTreenodeExpansionState = getExpansionState();
        switch (enumTreenodeExpansionState) {
            case COLLAPSED:
                setExpansionState(Treeview.EnumTreenodeExpansionState.EXPANDED);
                break;
            case EXPANDED:
                setExpansionState(Treeview.EnumTreenodeExpansionState.COLLAPSED);
                break;
            case EMPTY:
                break;
        }
    }

    private boolean boolItteratorHelper;
    public boolean hasHiddenChildItems() {
        boolItteratorHelper = false;
        TreeIterator<TreeviewNode> treeIterator = new TreeIterator<>(this.getChildNodes());
        treeIterator.execute(new TreeIterator.OnTouchAllNodesListener<TreeviewNode>() {
            @Override
            public boolean onNode(ArrayList<TreeviewNode> parentArrayList, TreeviewNode treeviewNode, int intLevel) {
                if (treeviewNode.isHidden()) {
                    boolItteratorHelper = true;
                    return false;
                }
                return true;
            }
        });
        return boolItteratorHelper;
    }

    public boolean hasNewChildItems() {
        boolItteratorHelper = false;
        TreeIterator<TreeviewNode> treeIterator = new TreeIterator<>(this.getChildNodes());
        treeIterator.execute(new TreeIterator.OnTouchAllNodesListener<TreeviewNode>() {
            @Override
            public boolean onNode(ArrayList<TreeviewNode> parentArrayList, TreeviewNode treeviewNode, int intLevel) {
                if (treeviewNode.isNew()) {
                    boolItteratorHelper = true;
                    return false;
                }
                return true;
            }
        });
        return boolItteratorHelper;
    }


}


