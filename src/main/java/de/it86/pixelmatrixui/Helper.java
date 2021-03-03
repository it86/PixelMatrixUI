package de.it86.pixelmatrixui;

import de.it86.pixelmatrixui.data.FrameData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class Helper {

    private Helper() {
        /* utility class */
    }

    public static ObservableList<FrameItem> initialFrameItemList(int rows, int columns) {
        FrameData data = FrameData.createEmpty(rows, columns);

        FrameItem item = new FrameItem();
        item.setIndex(0);
        item.setData(data);

        return FXCollections.observableArrayList(item);
    }

}
