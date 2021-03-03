package de.it86.pixelmatrixui.controller;

import de.it86.pixelmatrixui.FrameItem;
import de.it86.pixelmatrixui.Helper;
import de.it86.pixelmatrixui.common.ControllerAndView;
import de.it86.pixelmatrixui.data.FrameData;
import de.it86.pixelmatrixui.fxml.FXMLHelper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.util.Callback;

import java.io.IOException;

public class FrameViewController {

    public static ControllerAndView<FrameViewController, TitledPane> create() throws IOException {
        FXMLLoader loader = new FXMLLoader();

        TitledPane view = loader.load(FXMLHelper.frameViewStream());
        FrameViewController controller = loader.getController();

        return new ControllerAndView<>(controller, view);
    }

    private final ObjectProperty<ObservableList<FrameItem>> frames;
    private final ObjectProperty<FrameItem> selectedFrame;
    @FXML
    private ListView<FrameItem> list;

    public FrameViewController() {
        this.frames = new SimpleObjectProperty<>(this, "frames", Helper.initialFrameItemList(32, 64));
        this.selectedFrame = new SimpleObjectProperty<>(this, "selectedFrame");
    }

    public ObjectProperty<ObservableList<FrameItem>> framesProperty() {
        return frames;
    }

    public ObservableList<FrameItem> getFrames() {
        return frames.get();
    }

    public ObjectProperty<FrameItem> selectedFrameProperty() {
        return selectedFrame;
    }

    public FrameItem getSelectedFrame() {
        return selectedFrame.get();
    }

    @FXML
    private void initialize() {
        list.getSelectionModel().selectFirst();
        list.setCellFactory(new Callback<>() {
                                @Override
                                public ListCell<FrameItem> call(ListView<FrameItem> list) {
                                    return new ListCell<>() {
                                        @Override
                                        protected void updateItem(FrameItem item, boolean empty) {
                                            super.updateItem(item, empty);

                                            if (empty || item == null) {
                                                setText(null);
                                                setGraphic(null);
                                            } else {
                                                setText("Frame " + (item.getIndex() + 1));
                                            }
                                        }
                                    };
                                }
                            }
        );

        selectedFrame.bind(list.getSelectionModel().selectedItemProperty());
    }

    @FXML
    private void addNewFrame() {
        FrameData data = FrameData.createEmpty(32, 64);

        FrameItem item = new FrameItem();
        item.setIndex(frames.get().size());
        item.setData(data);

        frames.get().add(item);
    }

    @FXML
    private void copyFrame() {
        FrameItem template = selectedFrame.get();
        if (template == null) {
            return;
        }

        FrameItem item = new FrameItem();
        item.setIndex(frames.get().size());
        item.setData(new FrameData(template.getData()));

        frames.get().add(item);
    }

    @FXML
    private void removeFrame() {
        if (list.getSelectionModel().isEmpty()) {
            return;
        }

        frames.get().remove(list.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void moveUp() {
        if (list.getSelectionModel().isEmpty()) {
            return;
        }

        int index = list.getSelectionModel().getSelectedIndex();
        if (index == 0) {
            return;
        }

        FrameItem item = frames.get().remove(index - 1);
        frames.get().add(index, item);
    }

    @FXML
    private void moveDown() {
        if (list.getSelectionModel().isEmpty()) {
            return;
        }

        int index = list.getSelectionModel().getSelectedIndex();
        if (index == frames.get().size() - 1) {
            return;
        }

        FrameItem item = frames.get().remove(index + 1);
        frames.get().add(index, item);
    }

}
