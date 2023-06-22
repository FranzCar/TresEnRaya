public class Jugador {
    private String nombre;
    private char simbolo;

    public Jugador(String nombre, char simbolo){
        this.nombre = nombre;
        this.simbolo = simbolo;
    }

    public String getNombre(){
        return this.nombre;
    }

    public char getSimbolo(){
        return this.simbolo;
    }
}
