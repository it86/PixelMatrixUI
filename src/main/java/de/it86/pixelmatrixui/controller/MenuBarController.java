package de.it86.pixelmatrixui.controller;

import de.it86.pixelmatrixui.common.ControllerAndView;
import de.it86.pixelmatrixui.common.Procedure;
import de.it86.pixelmatrixui.fxml.FXMLHelper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuBar;

import java.io.IOException;

public final class MenuBarController {

    public static ControllerAndView<MenuBarController, MenuBar> create() throws IOException {
        FXMLLoader loader = new FXMLLoader();

        MenuBar view = loader.load(FXMLHelper.menuBarStream());
        MenuBarController controller = loader.getController();

        return new ControllerAndView<>(controller, view);
    }

    private final ObjectProperty<Procedure> loadProcedure;
    private final ObjectProperty<Procedure> saveProcedure;
    private final ObjectProperty<Procedure> exportProcedure;
    private final ObjectProperty<Procedure> moveProcedure;
    private final ObjectProperty<Procedure> importBackground;
    private final ObjectProperty<Procedure> clearBackground;

    public MenuBarController() {
        this.loadProcedure = new SimpleObjectProperty<>(this, "loadProcedure");
        this.saveProcedure = new SimpleObjectProperty<>(this, "saveProcedure");
        this.exportProcedure = new SimpleObjectProperty<>(this, "exportProcedure");
        this.moveProcedure = new SimpleObjectProperty<>(this, "moveProcedure");
        this.importBackground = new SimpleObjectProperty<>(this, "importBackground");
        this.clearBackground = new SimpleObjectProperty<>(this, "clearBackground");
    }

    //**************************************************************************
    // Getter & Setter
    //**************************************************************************

    public Procedure getLoadProcedure() {
        return loadProcedure.get();
    }

    public void setLoadProcedure(Procedure loadProcedure) {
        this.loadProcedure.set(loadProcedure);
    }

    public ObjectProperty<Procedure> loadProcedureProperty() {
        return loadProcedure;
    }

    public Procedure getSaveProcedure() {
        return saveProcedure.get();
    }

    public void setSaveProcedure(Procedure saveProcedure) {
        this.saveProcedure.set(saveProcedure);
    }

    public ObjectProperty<Procedure> saveProcedureProperty() {
        return saveProcedure;
    }

    public Procedure getExportProcedure() {
        return exportProcedure.get();
    }

    public void setExportProcedure(Procedure exportProcedure) {
        this.exportProcedure.set(exportProcedure);
    }

    public ObjectProperty<Procedure> exportProcedureProperty() {
        return exportProcedure;
    }

    public Procedure getMoveProcedure() {
        return moveProcedure.get();
    }

    public void setMoveProcedure(Procedure moveProcedure) {
        this.moveProcedure.set(moveProcedure);
    }

    public ObjectProperty<Procedure> moveProcedureProperty() {
        return moveProcedure;
    }

    public Procedure getImportBackground() {
        return importBackground.get();
    }

    public void setImportBackground(Procedure importBackground) {
        this.importBackground.set(importBackground);
    }

    public ObjectProperty<Procedure> importBackgroundProperty() {
        return importBackground;
    }

    public Procedure getClearBackground() {
        return clearBackground.get();
    }

    public void setClearBackground(Procedure clearBackground) {
        this.clearBackground.set(clearBackground);
    }

    public ObjectProperty<Procedure> clearBackgroundProperty() {
        return clearBackground;
    }

    //**************************************************************************
    // Event Handler
    //**************************************************************************

    @FXML
    private void exit() {
        System.exit(0);
    }

    @FXML
    private void load() {
        if (loadProcedure.get() == null) {
            return;
        }

        loadProcedure.get().execute();
    }

    @FXML
    private void save() {
        if (saveProcedure.get() == null) {
            return;
        }

        saveProcedure.get().execute();
    }

    @FXML
    private void export() {
        if (exportProcedure.get() == null) {
            return;
        }

        exportProcedure.get().execute();
    }

    @FXML
    private void move() {
        if (moveProcedure.get() == null) {
            return;
        }

        moveProcedure.get().execute();
    }

    @FXML
    private void importBackground() {
        if (importBackground.get() == null) {
            return;
        }

        importBackground.get().execute();
    }

    @FXML
    private void clearBackground() {
        if (clearBackground.get() == null) {
            return;
        }

        clearBackground.get().execute();
    }

}
