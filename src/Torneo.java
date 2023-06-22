import java.util.List;

public class Torneo {
    private List<Jugador> jugadores;
    private List<Partida> partidas;

    public Torneo(List<Jugador> jugadores){
        this.jugadores = jugadores;
        // Aquí podrías generar las partidas iniciales.
    }

    public void iniciarRonda(){
        // Aquí iría la lógica para jugar todas las partidas en la ronda actual
        // y avanzar a los ganadores a la siguiente ronda.
    }

    public Jugador obtenerCampeon(){
        // Aquí iría la lógica para determinar el campeón del torneo.
        // Devuelve null si el torneo aún no ha terminado.
        return jugadores.get(0);
    }
}
