package com.example.modelo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Cartas extends ImageView {
    private final Image imagenFrontal;
    private final Image imagenTrasera;
    private final String valor; // Identificador para comparar
    private boolean descubierta = false;
    private boolean encontrada = false;

    public Cartas(String valor, Image frontal, Image trasera) {
        this.valor = valor;
        this.imagenFrontal = frontal;
        this.imagenTrasera = trasera;
        setImage(imagenTrasera);
        setFitWidth(100);
        setFitHeight(100);
    }

    public void voltear() {
        if (encontrada) return;
        descubierta = !descubierta;
        setImage(descubierta ? imagenFrontal : imagenTrasera);
    }

    public void mostrar() {
        descubierta = true;
        setImage(imagenFrontal);
    }

    public void ocultar() {
        descubierta = false;
        setImage(imagenTrasera);
    }

    public boolean esDescubierta() {
        return descubierta;
    }

    public void marcarEncontrada() {
        encontrada = true;
    }

    public boolean esEncontrada() {
        return encontrada;
    }

    public String getValor() {
        return valor;
    }

    public Image getImagenFrontal() {
        return imagenFrontal;
    }
}
        