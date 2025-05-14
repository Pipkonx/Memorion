package com.example.vistas;

import com.example.Controlador.Controlador;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;


public class MainPanel extends VBox {
 private final Controlador controlador;

    public MainPanel() {
        this.controlador = new Controlador();

        setSpacing(20);
        setAlignment(Pos.CENTER);

        Button btnFacil = new Button("Jugar Fácil");
        btnFacil.setOnAction(e -> controlador.jugarFacil());

        Button btnMedio = new Button("Jugar Medio");
        btnMedio.setOnAction(e -> controlador.jugarMedio());

        Button btnDificil = new Button("Jugar Difícil");
        btnDificil.setOnAction(e -> controlador.jugarDificil());

        Button btnSalir = new Button("Salir");
        btnSalir.setOnAction(e -> controlador.salirDelJuego());

        getChildren().addAll(btnFacil, btnMedio, btnDificil, btnSalir);
    }
}