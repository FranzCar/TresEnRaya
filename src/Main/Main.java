package Main;

import agente.AgenteArbitro;
import agente.AgenteJugadorInteligente;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class Main {

    public static void main(String[] args) {
        Runtime rt = Runtime.instance();

        // Configurar el perfil
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.MAIN_PORT, "1099");
        p.setParameter(Profile.GUI, "true");

        // Crear el contenedor principal
        AgentContainer mainContainer = rt.createMainContainer(p);

        try {
            // Crear el agente árbitro
            AgentController arbitroController = mainContainer.createNewAgent("arbitro", AgenteArbitro.class.getName(), null);
            arbitroController.start();

            // Crear los agentes jugadores
            AgentController jugador1Controller = mainContainer.createNewAgent("jugadorX", AgenteJugadorInteligente.class.getName(), null);
            jugador1Controller.start();

            AgentController jugador2Controller = mainContainer.createNewAgent("jugadorO", AgenteJugadorInteligente.class.getName(), null);
            jugador2Controller.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
