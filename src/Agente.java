import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Agente extends Agent {
    private char simbolo;
    private Tablero tablero;

    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            this.simbolo = ((String) args[0]).charAt(0);
            this.tablero = new Tablero();

            addBehaviour(new ComportamientoJugador());
        }
    }

    private class ComportamientoJugador extends CyclicBehaviour {
        public void action() {
            // Recibir mensajes de otros agentes
            MessageTemplate plantilla = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage msg = myAgent.receive(plantilla);
            if (msg != null) {
                String contenido = msg.getContent();
                char simbolo = contenido.charAt(0);
                int fila = Character.getNumericValue(contenido.charAt(1));
                int columna = Character.getNumericValue(contenido.charAt(2));

                // Actualizar el tablero con el movimiento del otro jugador
                tablero.hacerMovimiento(fila, columna, simbolo);

                // Realizar un movimiento (este es solo un ejemplo y deberías reemplazarlo con tu propia lógica)
                int miFila = 0;
                int miColumna = 0;
                tablero.hacerMovimiento(miFila, miColumna, Agente.this.simbolo);

                // Enviar un mensaje al otro agente con nuestro movimiento
                ACLMessage respuesta = msg.createReply();
                respuesta.setPerformative(ACLMessage.INFORM);
                respuesta.setContent(Agente.this.simbolo + Integer.toString(miFila) + Integer.toString(miColumna));
                myAgent.send(respuesta);
            } else {
                block();
            }
        }
    }
}