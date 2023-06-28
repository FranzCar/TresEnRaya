package juegoTresEnRaya;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlgoritmoMinMax {

    Marca jugador;
    Marca oponente;
    TresEnRaya juego;

    public AlgoritmoMinMax(Marca jugador, Marca oponente, Marca[][] tablero) {
        this.jugador = jugador;
        this.oponente = oponente;
        this.juego = new TresEnRaya(tablero);
    }

    public int[] minimax(int profundidad, Marca miMarca) {
        List<int[]> siguientesMovimientos = generarMovimientos();
        if (siguientesMovimientos.size() == 9)
            return new int[]{1, new Random().nextInt(3), new Random().nextInt(3)};

        int mejorPuntuacion = (this.jugador == miMarca) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int puntuacionActual;
        int mejorFila = -1;
        int mejorColumna = -1;

        if (this.juego.juegoTerminado() || profundidad == 0) {
            if (this.juego.ganador() == this.jugador)
                mejorPuntuacion = this.juego.casillasVacias() + 1;
            else if (this.juego.ganador() == this.oponente) {
                mejorPuntuacion = -(this.juego.casillasVacias() + 1);
            } else {
                mejorPuntuacion = 0;
            }
        } else {

            for (int[] movimiento : siguientesMovimientos) {
                this.juego.tablero[movimiento[0]][movimiento[1]] = miMarca;
                if (jugador == miMarca) {
                    puntuacionActual = minimax(profundidad - 1, this.jugador)[0];
                    if (puntuacionActual > mejorPuntuacion) {
                        mejorPuntuacion = puntuacionActual;
                        mejorFila = movimiento[0];
                        mejorColumna = movimiento[1];
                    }
                } else {
                    puntuacionActual = minimax(profundidad - 1, this.oponente)[0];
                    if (puntuacionActual < mejorPuntuacion) {
                        mejorPuntuacion = puntuacionActual;
                        mejorFila = movimiento[0];
                        mejorColumna = movimiento[1];
                    }
                }
                this.juego.tablero[movimiento[0]][movimiento[1]] = Marca.VACIO;
            }
        }
        return new int[]{mejorPuntuacion, mejorFila, mejorColumna};
    }

    private List<int[]> generarMovimientos() {
        List<int[]> siguientesMovimientos = new ArrayList<int[]>();

        if (this.juego.marcaGanadora(jugador) || this.juego.marcaGanadora(oponente)) {
            return siguientesMovimientos;
        }

        for (int fila = 0; fila < 3; ++fila) {
            for (int columna = 0; columna < 3; ++columna) {
                if (this.juego.tablero[fila][columna] == Marca.VACIO) {
                    siguientesMovimientos.add(new int[]{fila, columna});
                }
            }
        }
        return siguientesMovimientos;
    }
}