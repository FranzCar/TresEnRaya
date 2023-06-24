package Main;

import agents.RefereeAgent;
import agents.SmartPlayAgent;
import agents.TournamentAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class Main {

    public static void main(String[] args) {
        try {
            // Inicializar JADE Runtime
            Runtime runtime = Runtime.instance();
            Profile profile = new ProfileImpl();
            AgentContainer container = runtime.createMainContainer(profile);

            // Crear y arrancar los agentes
            AgentController refereeAgentController = container.createNewAgent("referee", RefereeAgent.class.getName(), null);
            refereeAgentController.start();

            AgentController smartPlayAgentController = container.createNewAgent("smartPlayer", SmartPlayAgent.class.getName(), null);
            smartPlayAgentController.start();

            AgentController tournamentAgentController = container.createNewAgent("tournament", TournamentAgent.class.getName(), null);
            tournamentAgentController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
