package agente;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import juegoTresEnRaya.Marca;
import juegoTresEnRaya.AlgoritmoMinMax;
import juegoTresEnRaya.Estado;
import juegoTresEnRaya.TresEnRaya;

import java.io.IOException;

public class AgenteJugadorInteligente extends Agent {

    Marca marca = Marca.X;
    Marca oponente = Marca.O;

    protected void setup() {
        System.out.println("Iniciando " + getLocalName() + " para el juego Tres en Raya");
        FSMBehaviour agenteInteligente = new FSMBehaviour();
        agenteInteligente.registerFirstState(new EsperarArbitro(), "EsperarJugar");
        agenteInteligente.registerState(new ListoParaJugar(), "ListoParaJugar");
        agenteInteligente.registerState(new Jugar(marca, oponente), "Jugar");
        agenteInteligente.registerState(new Fin(), "JuegoTerminado");

        agenteInteligente.registerDefaultTransition("Jugar", "EsperarJugar");
        agenteInteligente.registerTransition("EsperarJugar", "JuegoTerminado", 1);
        agenteInteligente.registerTransition("EsperarJugar", "ListoParaJugar", 0);
        agenteInteligente.registerDefaultTransition("ListoParaJugar", "Jugar");

        addBehaviour(agenteInteligente);
    }

}

class EsperarArbitro extends OneShotBehaviour {

    int transicion = 0;

    @Override
    public void action() {
        ACLMessage mensajeRecibido = myAgent.blockingReceive();
        try {
            if ((Estado) mensajeRecibido.getContentObject() == Estado.FINALIZADO) {
                transicion = 1;
            } else if ((Estado) mensajeRecibido.getContentObject() == Estado.EN_JUEGO) {
                transicion = 0;
                System.out.println("Es mi turno " + myAgent.getName() + ", recibiendo mensaje de " + mensajeRecibido.getSender().getName());
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onEnd() {
        return transicion;
    }
}

class ListoParaJugar extends OneShotBehaviour {

    @Override
    public void action() {
        ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
        mensaje.addReceiver(new AID("arbitro", AID.ISLOCALNAME));
        mensaje.setContent("LISTO");
        myAgent.send(mensaje);
    }
}

class Jugar extends OneShotBehaviour {

    private final Marca marca;
    private final Marca oponente;


    public Jugar(Marca marca, Marca oponente) {
        this.oponente = oponente;
        this.marca = marca;
    }

    @Override
    public void action() {
        ACLMessage mensajeRecibido = myAgent.blockingReceive();
        try {
            TresEnRaya t = (TresEnRaya) mensajeRecibido.getContentObject();
            AlgoritmoMinMax m = new AlgoritmoMinMax(marca, oponente, t.tablero);
            int[] estadoMinMax = m.minimax(3, marca);
            System.out.println(t.jugadorSiguiente + " juega: " + estadoMinMax[1] + " , " + estadoMinMax[2]);
            ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
            mensaje.setContentObject(estadoMinMax);
            mensaje.addReceiver(mensajeRecibido.getSender());
            myAgent.send(mensaje);
        } catch (UnreadableException | IOException e) {
            System.out.println("Debes enviar un objeto TresEnRaya");
        }

    }
}

class Fin extends OneShotBehaviour {

    @Override
    public void action() {
        myAgent.doDelete();
    }
}