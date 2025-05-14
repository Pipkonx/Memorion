package com.example.Controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.Main;
import com.example.modelo.Cartas;
import com.example.modelo.Imagen;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

public class Controlador {

    // --- Elementos para el juego ---
    @FXML
    private GridPane tablero;

    @FXML
    private Button jugarFacilButton;

    @FXML
    private Button jugarMedioButton;

    @FXML
    private Button jugarDificilButton;

    @FXML
    private Button salirButton;

    private Button primeraCarta = null;
    private Button segundaCarta = null;
    private boolean bloqueado = false;

    private Cartas primeraSeleccion = null;

    // Guarda los valores de las cartas para comparar
    private String[][] valoresCartas;

    private final List<Image> imagenes = new ArrayList<>();

    // --- Inicialización del juego ---
    public void initialize(Cartas cartas) {
        // Solo inicializa el tablero si existe en el FXML actual
        if (tablero != null) {
            cargarImagenes();

            int size = Main.getDificultad(); // 3, 6 o 12
            int totalCartas = size * size;
            int numPares = totalCartas / 2;

            // Crear la lista de pares de imágenes
            List<Image> pares = new ArrayList<>();
            for (int i = 0; i < numPares; i++) {
                Image img = imagenes.get(i % imagenes.size());
                pares.add(img);
                pares.add(img);
            }
            // Si el número de cartas es impar, añade una carta extra
            if (totalCartas % 2 != 0) {
                Image img = imagenes.get(numPares % imagenes.size());
                pares.add(img);
            }
            Collections.shuffle(pares);

            Image trasera = new Image(getClass().getResource("/images/back.png").toExternalForm());

            int index = 0;
            tablero.getChildren().clear();
            for (int fila = 0; fila < size; fila++) {
                for (int col = 0; col < size; col++) {
                    if (index >= pares.size())
                        break;
                    Image img = pares.get(index++);
                    Cartas carta = new Cartas(String.valueOf(img.hashCode()), img, trasera);
                    carta.setOnMouseClicked(e -> manejarSeleccion(carta));
                    tablero.add(carta, col, fila);
                }
            }
        }
    }

    private void cargarImagenes() {
        imagenes.clear(); // Limpia la lista antes de cargar
        String base = "src//main//resources//imagenesCartas";
        imagenes.add(new Imagen("camavinga.jpg").getImage());
        imagenes.add(new Imagen("elbicho.jpg").getImage());
        imagenes.add(new Imagen("lamineyamal.jpg").getImage());
        imagenes.add(new Imagen("mbappe.jpg").getImage());
        imagenes.add(new Imagen("messi.jpg").getImage());
        imagenes.add(new Imagen("rodrigodePaul.jpg").getImage());
        imagenes.add(new Imagen("vinicius.jpg").getImage());
    }

    private void manejarSeleccion(Cartas seleccionada) {
        if (bloqueado || seleccionada.esDescubierta())
            return;

        seleccionada.mostrar();

        if (primeraSeleccion == null) {
            primeraSeleccion = seleccionada;
        } else {
            bloqueado = true;
            if (seleccionada.getImagenFrontal().equals(primeraSeleccion.getImagenFrontal())) {
                seleccionada.marcarEncontrada();
                primeraSeleccion.marcarEncontrada();
                primeraSeleccion = null;
                bloqueado = false;
            } else {
                Timer timer = new Timer();
                Cartas anterior = primeraSeleccion;
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            seleccionada.ocultar();
                            anterior.ocultar();
                            primeraSeleccion = null;
                            bloqueado = false;
                        });
                    }
                }, 1000);
            }
        }
    }

    public void iniciarJuego(GridPane tablero) {
        int filas = Main.getFilas();
        int columnas = Main.getColumnas();
        tablero.getChildren().clear();

        // Generar pares de valores para las cartas
        List<String> valores = new ArrayList<>();
        int numCartas = filas * columnas;
        for (int i = 1; i <= numCartas / 2; i++) {
            valores.add(String.valueOf(i));
            valores.add(String.valueOf(i));
        }
        Collections.shuffle(valores);

        valoresCartas = new String[filas][columnas];
        int idx = 0;

        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                String valor = valores.get(idx++);
                valoresCartas[fila][col] = valor;

                Button carta = new Button("?");
                carta.setPrefSize(80, 80);

                int f = fila, c = col;
                carta.setOnAction(e -> manejarSeleccion(carta, f, c));

                tablero.add(carta, col, fila);
            }
        }
    }

    private void manejarSeleccion(Button carta, int fila, int col) {
        // Cambiar esto:
        if (bloqueado || carta.getText() != "?") return;
        // Por esto:
        if (bloqueado || !carta.getText().equals("?")) return;
        
        carta.setText(valoresCartas[fila][col]);

        if (primeraCarta == null) {
            primeraCarta = carta;
        } else if (segundaCarta == null && carta != primeraCarta) {
            segundaCarta = carta;
            bloqueado = true;

            // Comprobar si coinciden
            if (primeraCarta.getText().equals(segundaCarta.getText())) {
                // Coinciden, se quedan boca arriba
                primeraCarta = null;
                segundaCarta = null;
                bloqueado = false;
            } else {
                // No coinciden, voltearlas después de un tiempo
                new Thread(() -> {
                    try { Thread.sleep(800); } catch (InterruptedException ignored) {}
                    javafx.application.Platform.runLater(() -> {
                        primeraCarta.setText("?");
                        segundaCarta.setText("?");
                        primeraCarta = null;
                        segundaCarta = null;
                        bloqueado = false;
                    });
                }).start();
            }
        }
    }

    // --- Métodos del menú (antes en MenuController) ---
    public void jugarFacil(ActionEvent event) {
        jugarFacil();
    }

    public void jugarMedio(ActionEvent event) {
        jugarMedio();
    }

    public void jugarDificil(ActionEvent event) {
        jugarDificil();
    }

    public void salirDelJuego(ActionEvent event) {
        salirDelJuego();
    }

    public void jugarFacil() {
        Main.setFilas(2);      // 2 filas
        Main.setColumnas(3);   // 3 columnas
        Main.setView(new com.example.vistas.JuegoPanel());
    }

    public void jugarMedio() {
        abrirJuegoConDificultad(6);
    }

    public void jugarDificil() {
        abrirJuegoConDificultad(12);
    }

    public void salirDelJuego() {
        System.exit(0);
    }

    private void abrirJuegoConDificultad(int size) {
        try {
            Main.setDificultad(size);
            Main.setView(new com.example.vistas.JuegoPanel());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void iniciarJuego(int size) {
        try {
            Main.setDificultad(size);
            // Si usas FXML:
            // Main.setRoot("gameView.fxml");
            // Si usas paneles Java puros, cambia el panel principal aquí:
            // Main.setPanel(new JuegoPanel(size)); // Ejemplo, ajusta según tu estructura
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setRoot(String fxml) throws IOException {
        // ...
    }
}