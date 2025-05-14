package com.example.vistas;
import com.example.Controlador.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;



public class JuegoPanel extends BorderPane {
     private final Controlador controlador;
    private final GridPane tablero;

    public JuegoPanel() {
        this.controlador = new Controlador();
        this.tablero = new GridPane();

        tablero.setHgap(10);
        tablero.setVgap(10);
        tablero.setAlignment(Pos.CENTER);

        controlador.iniciarJuego(tablero);

        Button volverMenu = new Button("Volver al Menú");
        volverMenu.setOnAction(e -> {
            com.example.Main.setView(new MainPanel());
        });

        HBox topBar = new HBox(volverMenu);
        topBar.setAlignment(Pos.CENTER);
        topBar.setSpacing(10);

        setTop(topBar);
        setCenter(tablero);
    }
}