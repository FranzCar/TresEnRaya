package juegoTresEnRaya;

import java.io.Serializable;

public class TresEnRaya implements Serializable {

    public Marca[][] tablero;
    public Marca jugadorSiguiente;

    public TresEnRaya(Marca jugadorSiguiente) {
        tablero = new Marca[3][3];
        this.jugadorSiguiente = jugadorSiguiente;
        this.llenarConVacios();
    }

    public TresEnRaya(Marca[][] tablero) {
        this.tablero = copiarTablero(tablero);
    }

    private void llenarConVacios() {
        for (int fila = 0; fila < this.tablero.length; fila++) {
            for (int columna = 0; columna < this.tablero[fila].length; columna++) {
                tablero[fila][columna] = Marca.VACIO;
            }
        }
    }

    public void agregarMarca(Marca marca, int linea, int columna) {
        if (linea > 2 || linea < 0 || columna > 2 || columna < 0)
            return;
        if (this.tablero[linea][columna] == Marca.VACIO) {
            tablero[linea][columna] = marca;
        }
    }

    public Marca ganador() {
        return marcaGanadora(Marca.X) ? Marca.X : marcaGanadora(Marca.O) ? Marca.O : Marca.VACIO;
    }

    public boolean juegoTerminado() {
        return juegoEmpatado() || marcaGanadora(Marca.X) || marcaGanadora(Marca.O);
    }

    public boolean marcaGanadora(Marca marca) {
        return cruzEnLinea(marca, 0) || cruzEnLinea(marca, 1) || cruzEnLinea(marca, 2)
                || cruzEnColumna(marca, 0) || cruzEnColumna(marca, 1) || cruzEnColumna(marca, 2)
                || diagonales(marca);
    }

    public boolean juegoEmpatado() {
        for (Marca[] linea : this.tablero) {
            for (Marca columna : linea) {
                if (columna == Marca.VACIO) {
                    return false;
                }
            }
        }
        return true;
    }

    public int casillasVacias() {
        int count = 0;
        for (Marca[] linea : this.tablero) {
            for (Marca columna : linea) {
                if (columna == Marca.VACIO) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean cruzEnLinea(Marca marca, int linea) {
        return this.tablero[linea][0] == marca && this.tablero[linea][1] == marca && this.tablero[linea][2] == marca;
    }

    private boolean cruzEnColumna(Marca marca, int columna) {
        return this.tablero[0][columna] == marca && this.tablero[1][columna] == marca && this.tablero[2][columna] == marca;
    }

    private boolean diagonales(Marca marca) {
        return (this.tablero[0][0] == marca && this.tablero[1][1] == marca && this.tablero[2][2] == marca)
                || (this.tablero[0][2] == marca && this.tablero[1][1] == marca && this.tablero[2][0] == marca);
    }

    private Marca[][] copiarTablero(Marca[][] tablero) {
        Marca[][] copiaTablero = new Marca[3][3];
        for (int fila = 0; fila < 3; fila++) {
            System.arraycopy(tablero[fila], 0, copiaTablero[fila], 0, 3);
        }
        return copiaTablero;
    }

    public void mostrarTablero() {
        for (Marca[] linea : this.tablero) {
            for (Marca marca : linea) {
                if (marca == Marca.VACIO) {
                    System.out.print("  | ");
                } else {
                    System.out.print(marca + " | ");
                }
            }
            System.out.println("\n");
        }
    }

}