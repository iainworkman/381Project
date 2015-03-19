package views;

import Enumerators.Enumerators;
import Enumerators.Enumerators.TransformerLocation;
import Enumerators.Enumerators.TransformerType;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import models.LinkedListElement;


/**
 * A class for the visual representation of a LinkedListElement
 * 
 * NOTE: Currently this does almost nothing to actually visualize the data, I 
 * just needed it on there to get the drag and drop working. There's still
 * loads to be done in here ~ Iain
 * @author Iain Workman
 */
public class LinkedListViewElement extends WorkSpaceViewElement {
    
    // Constructor
    /**
     * Creates an instance of a LinkedListViewElement
     * @param element::LinkedListElement ~ The LinkedListElement this view is
     * to represent.
     */
    public LinkedListViewElement (LinkedListElement element) {
        super(element);
      //  setMinSize(element.getWidth(), element.getHeight());
        this.setStyle("-fx-background-color: Red");
        
        // Initialize UI
        backgroundRectangle_ = new Rectangle(0, 0, getWidth() -1 , getHeight() -1);
        backgroundRectangle_.setFill(Color.WHITE);
        backgroundRectangle_.setStroke(Color.BLACK);
        
        headLabel_ = new Label("Head");
        headLabel_.relocate(5,5);
        
        tailLabel_ = new Label("Tail");
        tailLabel_.relocate(5, 25);
        
        countLabel_ = new Label("Count");
        countLabel_.relocate(5, 45);
        getChildren().addAll(backgroundRectangle_,
                headLabel_,
                tailLabel_,
                countLabel_);
        
        // Incase we get resized we really should listen for this an update the
        // canvas appropriately.
        widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            onResize(); 
        });

        heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            onResize();
        });
        
       this.setSnapToPixel(false);
        this.setupTransformers();
        this.onResize();
        this.update();
    }
    
    // Public Methods

    @Override
    public void update() {
        // For some reason setting the canvas to the same as the element expands
        // the size of the element, which then causes this to fire and we end
        // up with an ever growing element. As such these are set to 1 less 
        // than the width and height of the elements.
    

       
        
    }
    
    @Override
    public void setIsSelected(boolean selectionState) {
        super.setIsSelected(selectionState);
        if(selectionState)
            backgroundRectangle_.setStroke(Color.RED);
        else
            backgroundRectangle_.setStroke(Color.BLACK);
    }
    
    
    
        /**This takes care of all of the tasks that need to be considered when this element is resized.
     * 
     */
    public void onResize()
    {
        LinkedListElement listElement = (LinkedListElement) this.getElement();
        this.setSize(listElement.getWidth(), listElement.getHeight());
        backgroundRectangle_.setWidth(this.getWidth() - 1);
        backgroundRectangle_.setHeight(this.getHeight() - 1);
    }
    

   //  Private Member Variables
    private final Rectangle backgroundRectangle_;
    private final Label headLabel_;
    private final Label tailLabel_;
    private final Label countLabel_;





    

}
