import java.util.concurrent.ThreadLocalRandom;

public class Tablero {
    private int FILAS;
    private int COLUMNAS;
    private char[][] tablero;
    private char JUGADOR_O;
    private char JUGADOR_X;
    private char ESPACIO_VACIO;

    public Tablero(int filas, int columnas, char jugadorO, char jugadorX, char espacioVacio) {
        FILAS = filas;
        COLUMNAS = columnas;
        JUGADOR_O = jugadorO;
        JUGADOR_X = jugadorX;
        ESPACIO_VACIO = espacioVacio;
        tablero = new char[FILAS][COLUMNAS];
        limpiarTablero();
    }

    public void limpiarTablero() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                tablero[i][j] = ESPACIO_VACIO;
            }
        }
    }

    public void imprimirTablero() {
        System.out.print("\n");
        System.out.print("| ");
        for (int i = 0; i < COLUMNAS; i++) {
            System.out.printf("|%d", i + 1);
        }
        System.out.print("|\n");
        for (int i = 0; i < FILAS; i++) {
            System.out.printf("|%d", i + 1);
            for (int j = 0; j < COLUMNAS; j++) {
                System.out.printf("|%c", tablero[i][j]);
            }
            System.out.print("|\n");
        }
    }

    public boolean coordenadasVacias(int y, int x) {
        return tablero[y][x] == ESPACIO_VACIO;
    }

    public void colocarPieza(int y, int x, char pieza) {
        if (y < 0 || y >= FILAS) {
            System.out.print("Fila incorrecta");
            return;
        }
        if (x < 0 || x >= COLUMNAS) {
            System.out.print("Columna incorrecta");
            return;
        }
        if (pieza != JUGADOR_O && pieza != JUGADOR_X) {
            System.out.printf("La pieza debe ser %c o %c", JUGADOR_O, JUGADOR_X);
            return;
        }
        if (!coordenadasVacias(y, x)) {
            System.out.print("Coordenadas ya ocupadas");
            return;
        }
        tablero[y][x] = pieza;
    }

    public boolean comprobarSiGana(char jugador) {
        for (int i = 0; i < FILAS; i++) {
            if (tablero[i][0] == jugador && tablero[i][1] == jugador && tablero[i][2] == jugador) {
                return true; // Filas
            }
        }
        for (int i = 0; i < COLUMNAS; i++) {
            if (tablero[0][i] == jugador && tablero[1][i] == jugador && tablero[2][i] == jugador) {
                return true; // Columnas
            }
        }
        if (tablero[0][0] == jugador && tablero[1][1] == jugador && tablero[2][2] == jugador) {
            return true; // Diagonal principal
        }
        if (tablero[0][2] == jugador && tablero[1][1] == jugador && tablero[2][0] == jugador) {
            return true; // Diagonal secundaria
        }
        return false;
    }

    public boolean empate() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (tablero[i][j] == ESPACIO_VACIO) {
                    return false;
                }
            }
        }
        return true;
    }

    public int obtenerMovimientoAleatorio() {
        return ThreadLocalRandom.current().nextInt(1, 10);
    }
}
