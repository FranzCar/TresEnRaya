package agente;

import jade.core.AID;
import jade.core.Agent;

import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import juegoTresEnRaya.Marca;
import juegoTresEnRaya.Estado;
import juegoTresEnRaya.TresEnRaya;

import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

public class AgenteArbitro extends Agent {

    public TresEnRaya juego;

    protected void setup() {
        System.out.println("Iniciando " + getLocalName() + " para el juego Tres en Raya");
        Marca primerJugador = new Random().nextInt(2) == 0 ? Marca.X : Marca.O;
        this.juego = new TresEnRaya(primerJugador);
        System.out.println("El jugador " + primerJugador + " comienza el juego");
        FSMBehaviour agenteArbitro = new FSMBehaviour();
        agenteArbitro.registerFirstState(new ComprobarEstadoJuego(this.juego), "ComprobarEstadoJuego");
        agenteArbitro.registerState(new EsperarJugadorListo(), "EsperarJugadorListo");
        agenteArbitro.registerState(new SolicitarJugadorJugar(this.juego), "SolicitarJugadorJugar");
        agenteArbitro.registerState(new ActualizarTablero(this.juego), "ActualizarTablero");
        agenteArbitro.registerState(new FinJuego(this.juego), "FinJuego");

        agenteArbitro.registerTransition("ComprobarEstadoJuego", "FinJuego", 0);
        agenteArbitro.registerTransition("ComprobarEstadoJuego", "EsperarJugadorListo", 1);
        agenteArbitro.registerDefaultTransition("EsperarJugadorListo", "SolicitarJugadorJugar");
        agenteArbitro.registerDefaultTransition("SolicitarJugadorJugar", "ActualizarTablero");
        agenteArbitro.registerDefaultTransition("ActualizarTablero", "ComprobarEstadoJuego");

        addBehaviour(agenteArbitro);
    }
}

class ComprobarEstadoJuego extends OneShotBehaviour {

    private final TresEnRaya juego;
    int transicion = 1;

    public ComprobarEstadoJuego(TresEnRaya juego) {
        this.juego = juego;
    }

    private String getNombreJugador() {
        return "jugador" + this.juego.jugadorSiguiente;
    }

    @Override
    public void action() {
        if (this.juego.juegoTerminado()) {
            System.out.println("El juego ha terminado");
            try {
                ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
                mensaje.addReceiver(new AID("jugador" + Marca.X, AID.ISLOCALNAME));
                mensaje.addReceiver(new AID("jugador" + Marca.O, AID.ISLOCALNAME));
                mensaje.setContentObject(Estado.FINALIZADO);
                myAgent.send(mensaje);
                transicion = 0;
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            // Llamar al agente que va a jugar
            String jugadorActual = this.getNombreJugador();
            ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
            mensaje.addReceiver(new AID(jugadorActual, AID.ISLOCALNAME));
            try {
                mensaje.setContentObject(Estado.EN_JUEGO);
                myAgent.send(mensaje);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public int onEnd() {
        return transicion;
    }
}

class EsperarJugadorListo extends OneShotBehaviour {

    @Override
    public void action() {
        ACLMessage mensajeRecibido = myAgent.blockingReceive();
        System.out.println("Recibiendo mensaje de " + mensajeRecibido.getSender().getName() + " diciendo que está listo");
    }
}

class SolicitarJugadorJugar extends OneShotBehaviour {

    public TresEnRaya juego;

    SolicitarJugadorJugar(TresEnRaya juego) {
        this.juego = juego;
    }

    private String getNombreJugador() {
        return "jugador" + this.juego.jugadorSiguiente;
    }

    @Override
    public void action() {
        ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
        mensaje.addReceiver(new AID(this.getNombreJugador(), AID.ISLOCALNAME));
        try {
            mensaje.setContentObject(this.juego);
            System.out.println("Enviando estado del juego a " + this.getNombreJugador());
            myAgent.send(mensaje);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ActualizarTablero extends OneShotBehaviour {

    private final TresEnRaya juego;

    ActualizarTablero(TresEnRaya juego) {
        this.juego = juego;
    }

    @Override
    public void action() {
        ACLMessage mensajeRecibido = myAgent.blockingReceive();
        System.out.println("Recibiendo mensaje de " + mensajeRecibido.getSender().getName() + " diciendo que ha jugado");
        try {
            int[] movimiento = (int[]) mensajeRecibido.getContentObject();
            System.out.println("Jugando en línea " + movimiento[1] + ", columna " + movimiento[2]);
            this.juego.agregarMarca(this.juego.jugadorSiguiente, movimiento[1], movimiento[2]);
            this.juego.jugadorSiguiente = this.juego.jugadorSiguiente == Marca.X ? Marca.O : Marca.X;
        } catch (UnreadableException e) {
            e.printStackTrace();
        }

    }
}

class FinJuego extends OneShotBehaviour {

    private final TresEnRaya juego;

    public FinJuego(TresEnRaya juego) {
        this.juego = juego;
    }

    @Override
    public void action() {
        System.out.println("El ganador es " + juego.ganador());
        this.juego.mostrarTablero();
        myAgent.doDelete();
    }
}