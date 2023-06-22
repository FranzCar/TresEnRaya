public class Partida {
    private Jugador jugador1;
    private Jugador jugador2;
    private Tablero tablero;

    public Partida(Jugador jugador1, Jugador jugador2){
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.tablero = new Tablero();
    }

    public void iniciarPartida(){
        // Aqui iria la logica para iniciar la partida, alternando turnos entre los jugadores
        // Usar tablero.hacerMovimiento para hacer un movimiento y tablero.comprobarGanador para comprobar si alguien ha ganado
    }
}
