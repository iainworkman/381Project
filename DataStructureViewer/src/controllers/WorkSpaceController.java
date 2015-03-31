package controllers;

import controllers.SelectionController.SelectionModifier;
import events.PointSelectionEvent;
import events.RectangleSelectionEvent;
import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import models.LinkedListElement;
import models.WorkSpaceGraph;
import models.WorkSpaceGraphElement;
import views.TransformSpot;
import views.WorkSpaceView;

/**
 * A class which handles events generated by input to a WorkSpaceView,
 * interprets them and delivers the interpreted WorkSpaceGraph model.
 *
 * @author Iain Workman
 */
public class WorkSpaceController {

    // Constructor
    /**
     * Creates an instance of a WorkSpaceController which interprets the input
     * from the provided WorkSpaceView, and passes the instructions to the
     * provided WorkSpaceGraph
     *
     * @param model::WorkSpaceGraph ~ The model to send the instructions to
     * @param view::WorkSpaceView ~ The view which will receive the input
     */
    public WorkSpaceController(WorkSpaceGraph model, WorkSpaceView view) {
        model_ = model;
        selectionController_ = new SelectionController();
        viewContextMenu_ = new ContextMenu();
        MenuItem bringToFrontMenuItem = new MenuItem("Bring to Front");
        view_ = view;
        // Bring to front item in context menu being clicked
        bringToFrontMenuItem.setOnAction((ActionEvent event) -> {
            if (this.contextMenuElement_ == null) {
                return;
            }

            model_.bringToFront(contextMenuElement_);
        });

        MenuItem sendToBackMenuItem = new MenuItem("Send to Back");

        // Send to back item in context menu being clicked
        sendToBackMenuItem.setOnAction((ActionEvent event) -> {
            if (this.contextMenuElement_ == null) {
                return;
            }

            model_.sendToBack(contextMenuElement_);
        });

        viewContextMenu_.getItems().addAll(sendToBackMenuItem,
                bringToFrontMenuItem);

        // Mouse being clicked on the view
        view.setOnMousePressed((MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {

                if (!(event.getTarget() instanceof TransformSpot)) {
                    selectionController_.startSelectionAt(event.getX(), event.getY());
                }

                mouseX_ = event.getX();
                mouseY_ = event.getY();
                selectionController_.startSelectionAt(event.getX(), event.getY());

            } else if (event.getButton() == MouseButton.SECONDARY) {
                // Do Nothing
            }

        });

        // Mouse click released
        view.setOnMouseReleased((MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (event.isShiftDown()) {
                    selectionController_.endSelection(SelectionModifier.Append);
                } else {
                    selectionController_.endSelection(SelectionModifier.ClearAndSelect);
                }
            } else if (event.getButton() == MouseButton.SECONDARY) {

                WorkSpaceGraphElement elementUnderMouse
                        = model_.getElementAt(event.getX(), event.getY());

                if (elementUnderMouse == null) {
                    view.clearSelection();
                    viewContextMenu_.hide();
                } else {
                    contextMenuElement_ = elementUnderMouse;
                    viewContextMenu_.show(view, event.getX(), event.getY());
                }
            }
        });

        // Mouse being dragged
        view.setOnMouseDragged((MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (!(event.getTarget() instanceof TransformSpot)) {

                    if (model_.getElementAt(mouseX_, mouseY_) != null) {
                        moveElements(event);
                    } else {
                        selectionController_.setCurrentX(event.getX());
                        selectionController_.setCurrentY(event.getY());
                        if (!selectionController_.isPointSelection()) {

                            // Get a normalized version of the selection rectangle 
                            // stored in the selectionController
                            double x1 = Math.min(selectionController_.getStartX(), selectionController_.getCurrentX());
                            double y1 = Math.min(selectionController_.getStartY(), selectionController_.getCurrentY());

                            double x2 = Math.max(selectionController_.getStartX(), selectionController_.getCurrentX());
                            double y2 = Math.max(selectionController_.getStartY(), selectionController_.getCurrentY());
                            view.updateSelectionRectangle(x1, y1, x2, y2);
                        }
                    }

                }
            }
        });

        selectionController_.setOnPointSelection((PointSelectionEvent event) -> {
            view.selectAt(event.getPointX(), event.getPointY(), event.getSelectionModifier() == SelectionModifier.Append);
        });

        selectionController_.setOnRectangleSelection((RectangleSelectionEvent event) -> {
            view.selectWithRectangle(event.startX(), event.startY(), event.endX(), event.endY(), event.getSelectionModifier() == SelectionModifier.Append);
            view.resetSelectionRectangle();
        });

        // An item being dropped on the view from the toolbar
        view.setOnDragDropped((DragEvent event) -> {
            String dragData = event.getDragboard().getString();

            WorkSpaceGraphElement elementToAdd = null;
            switch (dragData) {
                case "Linked List":
                    elementToAdd = new LinkedListElement(event.getX(), event.getY(), 0, model_);
                    break;
                case "Linked List Node":
                    break;
                case "Array":
                    break;
            }

            if (elementToAdd != null) {
                model_.addElement(elementToAdd);
            }

        });

        // A dragged item entering the view
        view.setOnDragOver((DragEvent event) -> {
            event.acceptTransferModes(TransferMode.COPY);

            event.consume();
        });
    }

    /**
     * Move the elements that are currently selected in the GraphView. Informs
     * the graphview that the element need to move.
     */
    private void moveElements(MouseEvent event) {

        double distanceMovedX = event.getX() - mouseX_;
        double distanceMovedY = event.getY() - mouseY_;
        mouseX_ = event.getX();
        mouseY_ = event.getY();
        view_.moveSelection(distanceMovedX, distanceMovedY);
    }
    // Private Member Variables
    private WorkSpaceView view_;
    private double mouseX_;
    private double mouseY_;
    private final ContextMenu viewContextMenu_;
    private final WorkSpaceGraph model_;
    private final SelectionController selectionController_;
    /// The item which was clicked on with the context menu
    private WorkSpaceGraphElement contextMenuElement_;
}
