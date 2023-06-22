public class Tablero {
    private char[][] tablero;

    public Tablero(){
        tablero = new char[3][3];
        for(int i=0; i<3; i++)
            for(int j=0; j<3; j++)
                tablero[i][j] = '-';
    }

    public void hacerMovimiento(int fila, int columna, char simbolo){
        if(tablero[fila][columna] == '-')
            tablero[fila][columna] = simbolo;
    }

    public boolean comprobarGanador(char simbolo){
        // Aqui iria la logica para comprobar si el simbolo ha formado una linea en el tablero
        // Devuelve true si el simbolo ha ganado, false de lo contrario
        return true;
    }
}
