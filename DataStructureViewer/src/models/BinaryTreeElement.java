package models;

import enumerators.HotSpotType;

/**
 * A class which represents a binary tree node
 *
 * @author Iain Workman
 */
public class BinaryTreeElement extends WorkSpaceGraphElement {

    // Constructor
    /**
     * Create a BinaryTreeElement at the provided position, with the provided
     * size
     *
     * @param positionX::double ~ The X position of the BinaryTreeElement
     * @param positionY::double ~ The Y position of the BinaryTreeElement
     * @param zIndex::int ~ The Z index of the BinaryTreeElement
     * @param parent::WorkSpaceGraph ~ The WorkSpaceGraph to which this element
     * belongs
     */
    public BinaryTreeElement(double positionX, double positionY, int zIndex, WorkSpaceGraph parent) {
        super(positionX, positionY, zIndex, 60, 60, 60, 60, parent);

        incomingHotSpot_ = new HotSpot(30, 5, HotSpotType.INCOMING, this);
        outgoingLeft_ = new HotSpot(15, 50, HotSpotType.OUTGOING, this);
        outgoingRight_ = new HotSpot(45, 50, HotSpotType.OUTGOING, this);

        getHotSpots().add(incomingHotSpot_);
        getHotSpots().add(outgoingLeft_);
        getHotSpots().add(outgoingRight_);
        resizeImplementation();
    }

    // Public Methods
    /**
     * The value which is stored within this BinaryTreeElement
     */
    public int getValue() {
        return value_;
    }

    /**
     * Sets the value stored in this BinaryTreeElement
     *
     * @param value::int ~ The new value to be stored in the BinaryTreeElement
     */
    public void setValue(int value) {
        value_ = value;
        getParent().notifySubscribersOfAlter(this);
    }

    // Protected Methods
    
    /**
     * Defines the actions to take when the Element is resized
     */
    @Override
    protected final void resizeImplementation() {
        incomingHotSpot_.setX(getWidth() / 2);
        outgoingLeft_.setX(getWidth() / 3);
        outgoingLeft_.setY(getHeight() - 5);

        outgoingRight_.setX((getWidth() / 3) * 2);
        outgoingRight_.setY(getHeight() - 5);
    }

    // Private Member Variables
    private int value_;
    private final HotSpot incomingHotSpot_;
    private final HotSpot outgoingLeft_;
    private final HotSpot outgoingRight_;

}
