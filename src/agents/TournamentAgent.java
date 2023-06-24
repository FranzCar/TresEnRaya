package agents;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import tictactoeGame.Mark;
import tictactoeGame.State;
import tictactoeGame.TicTacToe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TournamentAgent extends Agent {

    private final int MAX_PLAYERS = 4;

    private List<String> players;
    private List<String> winners;

    protected void setup() {
        System.out.println("Starting " + getLocalName() + " for Tic Tac Toe Tournament");

        players = new ArrayList<>();
        winners = new ArrayList<>();

        // Registrar comportamiento secuencial
        SequentialBehaviour tournamentBehaviour = new SequentialBehaviour();
        tournamentBehaviour.addSubBehaviour(new RegisterPlayersBehaviour());
        tournamentBehaviour.addSubBehaviour(new PlayMatchesBehaviour());
        tournamentBehaviour.addSubBehaviour(new FinalMatchBehaviour());
        tournamentBehaviour.addSubBehaviour(new DeclareWinnerBehaviour());

        addBehaviour(tournamentBehaviour);
    }

    private class RegisterPlayersBehaviour extends OneShotBehaviour {

        @Override
        public void action() {
            // Registrar agentes jugadores
            for (int i = 1; i <= MAX_PLAYERS; i++) {
                String playerName = "player" + i;
                players.add(playerName);

                // Enviar mensaje de registro a los agentes jugadores
                ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                message.addReceiver(getAID(playerName));
                message.setContent("REGISTER");
                send(message);
            }
        }
    }

    private class PlayMatchesBehaviour extends OneShotBehaviour {

        @Override
        public void action() {
            while (players.size() > 1) {
                String player1 = players.remove(0);
                String player2 = players.remove(0);

                System.out.println("Match between " + player1 + " and " + player2);

                // Enviar mensaje de inicio de partida a los agentes jugadores
                ACLMessage message1 = new ACLMessage(ACLMessage.INFORM);
                message1.addReceiver(getAID(player1));
                message1.setContent("START");
                send(message1);

                ACLMessage message2 = new ACLMessage(ACLMessage.INFORM);
                message2.addReceiver(getAID(player2));
                message2.setContent("START");
                send(message2);

                // Esperar mensajes de finalización de partida
                ACLMessage result1 = blockingReceive();
                ACLMessage result2 = blockingReceive();

                try {
                    // Obtener el estado del juego
                    State state1 = (State) result1.getContentObject();
                    State state2 = (State) result2.getContentObject();

                    if (state1 == State.FINISHED && state2 == State.FINISHED) {
                        winners.add(player1);
                        winners.add(player2);
                    } else if (state1 == State.FINISHED) {
                        winners.add(player1);
                        players.add(player2);
                    } else if (state2 == State.FINISHED) {
                        winners.add(player2);
                        players.add(player1);
                    }
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class FinalMatchBehaviour extends OneShotBehaviour {

        @Override
        public void action() {
            if (players.size() == 1) {
                String finalist = players.get(0);

                System.out.println("Final match between " + finalist + " and " + winners.get(0));

                // Enviar mensaje de inicio de partida a los agentes jugadores
                ACLMessage message1 = new ACLMessage(ACLMessage.INFORM);
                message1.addReceiver(getAID(finalist));
                message1.setContent("START");
                send(message1);

                ACLMessage message2 = new ACLMessage(ACLMessage.INFORM);
                message2.addReceiver(getAID(winners.get(0)));
                message2.setContent("START");
                send(message2);

                // Esperar mensajes de finalización de partida
                ACLMessage result1 = blockingReceive();
                ACLMessage result2 = blockingReceive();

                try {
                    // Obtener el estado del juego
                    State state1 = (State) result1.getContentObject();
                    State state2 = (State) result2.getContentObject();

                    if (state1 == State.FINISHED && state2 == State.FINISHED) {
                        winners.add(finalist);
                    } else if (state1 == State.FINISHED) {
                        winners.add(finalist);
                    } else if (state2 == State.FINISHED) {
                        winners.add(winners.get(0));
                    }
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class DeclareWinnerBehaviour extends OneShotBehaviour {

        @Override
        public void action() {
            if (winners.size() == 1) {
                String winner = winners.get(0);
                System.out.println("The winner is: " + winner);

                // Enviar mensaje de finalización del torneo al agente árbitro
                ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                message.addReceiver(getAID("referee"));
                message.setContent("TOURNAMENT_FINISHED");
                send(message);
            }
        }
    }
}
