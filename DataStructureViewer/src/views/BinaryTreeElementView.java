package views;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import models.BinaryTreeElement;

/**
 * A class for the visual representation of a BinaryTreeElement
 * @author Iain Workman
 */
public class BinaryTreeElementView extends WorkSpaceViewElement {

    // Constructor
    /**
     * Creates an instance of a BinaryTreeElementView which visualizes the provided
     * BinaryTreeElement
     * @param element 
     */
    public BinaryTreeElementView(BinaryTreeElement element) {
        super(element);

        backgroundRectangle_ = new Rectangle(0, 0, getWidth() - 1, getHeight() - 1);
        backgroundRectangle_.setFill(Color.web("#009688"));
        backgroundRectangle_.setStroke(Color.WHITE);
        backgroundRectangle_.setStrokeWidth(2);
        
        valueLabel_ = new Label("0");
        valueLabel_.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        valueLabel_.setTextFill(Color.WHITE);

        getComponentsPane().getChildren().addAll(backgroundRectangle_, valueLabel_);

        setLabelEditable(valueLabel_, "");
        valueLabel_.setWrapText(true);
        // Incase we get resized we really should listen for this an update the
        // canvas appropriately.
        widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            update();
        });

        heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            update();
        });

        setupTransformers();
        update();

    }

    // Public Methods
    /**
     * Updates the BinaryTreeElementView when the underlying BinaryTreeElement
     * has been changed.
     */
    @Override
    public void update() {
        this.setSize(getElement().getWidth(), getElement().getHeight());
        backgroundRectangle_.setWidth(getWidth() - 1);
        backgroundRectangle_.setHeight(getHeight() - 1);

        valueLabel_.relocate(0, 0);
        //valueLabel_.relocate((getWidth() - valueLabel_.getWidth()) / 2,
        //(getHeight() - valueLabel_.getHeight()) / 2);

        valueLabel_.setMaxWidth(this.getWidth());
        valueLabel_.setMaxHeight(this.getHeight());

        Text measureText = new Text(valueLabel_.getText());
        double layoutX = (this.getWidth() - measureText.getLayoutBounds().getWidth()) / 2;

        double amountOfRoomHeight = this.getHeight() - valueLabel_.getHeight();

        double layoutY = amountOfRoomHeight / 2;
        if (layoutX < 0) {
            layoutX = 0;
        } else if (layoutY < 0) {
            layoutY = 0;
        }

        if (layoutY + valueLabel_.getHeight() > this.getHeight()) {
            layoutY = this.getHeight() - valueLabel_.getMaxHeight();
        }
        valueLabel_.relocate(layoutX, layoutY);
        
        updateHotSpots();

    }

    // Private Member Variables
    private final Rectangle backgroundRectangle_;
    private final Label valueLabel_;

}
