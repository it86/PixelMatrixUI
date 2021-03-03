package de.it86.pixelmatrixui;

import de.it86.pixelmatrixui.data.FrameData;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class FrameItem {

    private final IntegerProperty index;
    private final ObjectProperty<FrameData> data;

    public FrameItem() {
        this.index = new SimpleIntegerProperty(this, "index");
        this.data = new SimpleObjectProperty<>(this, "data");
    }

    public int getIndex() {
        return index.get();
    }

    public void setIndex(int index) {
        this.index.set(index);
    }

    public FrameData getData() {
        return data.get();
    }

    public void setData(FrameData data) {
        this.data.set(data);
    }

}
