package shared;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

import treeview.Treeview;

/**
 * Created by HeinrichWork on 26/04/2015.
 */
public abstract class Node<T extends NodeBase> implements NodeBase<T> {

    public static enum EnumDragDropTypes {
        BELOW, BEFORE, AFTER
    }

    // Item row data
    private Integer intIconId;
    private boolean boolIsNew;
    private  boolean boolIsHidden;
    private boolean boolIsChecked;
    private String strDescription;
    private Drawable mediaPreviewImage;

    // Item folding data
    private Treeview.EnumTreenodeExpansionState enumTreenodeExpansionState;
    private boolean boolIsSelected;

    // Behaviour
    private boolean boolIsDirty;
    private boolean boolIsDeleted;


    // Items parent and childNoteNodes
    private T parent;
    private ArrayList<T> childNodes = new ArrayList<T>();

    // Getter Setters


    public Integer getIconId() {
        return intIconId;
    }

    public void setIconId(Integer intIconId) {
        this.intIconId = intIconId;
    }

    public ArrayList<T> getChildNodes() {
        return childNodes;
    }

    public T getParent() {
        return parent;
    }

    public void setParent(T parentNode) {
        parent = parentNode;
    }

    public void setChildNodes(ArrayList<T> childNodes) {
        this.childNodes = childNodes;
    }

    public String getDescription() {
        return strDescription;
    }

    public void setDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public void setSelected(boolean boolIsSelected) {
        this.boolIsSelected = boolIsSelected;
    }

    public boolean isSelected() {
        return boolIsSelected;
    }

    public boolean isChecked() {
        return boolIsChecked;
    }

    public void setChecked(boolean boolIsChecked) {
        this.boolIsChecked = boolIsChecked;
    }

    public boolean isHidden() {
        return boolIsHidden;
    }

    public void setHidden(boolean boolIsHidden) {
        this.boolIsHidden = boolIsHidden;
    }

    public boolean isNew() {
        return boolIsNew;
    }

    public void setNew(boolean boolIsNew) {
        this.boolIsNew = boolIsNew;
    }

    public void setDeleted(boolean boolIsDeleted) {
        this.boolIsDeleted = boolIsDeleted;
    }

    public boolean isDeleted() {
        return boolIsDeleted;
    }

    public boolean isDirty() {
        return boolIsDirty;
    }

    public void setDirty(boolean boolIsDirty) {
        this.boolIsDirty = boolIsDirty;
    }

    public Drawable getMediaPreviewImage() {
        return mediaPreviewImage;
    }

    public void setMediaPreviewImage(Drawable mediaPreviewImage) {
        this.mediaPreviewImage = mediaPreviewImage;
    }

    public Treeview.EnumTreenodeExpansionState getExpansionState() {
        return enumTreenodeExpansionState;
    }

    public void setExpansionState(Treeview.EnumTreenodeExpansionState enumTreenodeExpansionState) {
        this.enumTreenodeExpansionState = enumTreenodeExpansionState;
    }

    public void addChildNode(T childNode) {
        childNodes.add(childNode);
        childNode.setParent(this);
        childNode.setNew(true);
    }

    public void addChildNode(T childNode, int index) {
        childNodes.add(index, childNode);
        childNode.setParent(this);
        childNode.setNew(true);
    }


}
