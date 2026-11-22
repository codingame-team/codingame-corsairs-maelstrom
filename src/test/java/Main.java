import com.codingame.gameengine.runner.MultiplayerGameRunner;
import com.codingame.gameengine.runner.simulate.GameResult;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && "--simulate".equals(args[0])) {
            runBatchSimulation();
            return;
        }

        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
        gameRunner.setLeagueLevel(3);
        gameRunner.setSeed(42L);
        gameRunner.addAgent(Agent1.class, "InertialCorsairAI");
        gameRunner.addAgent(Agent2.class, "PrivateerBoss");

        gameRunner.start(8888);
    }

    private static void runBatchSimulation() {
        for (int league = 1; league <= 3; league++) {
            int p0Wins = 0, p1Wins = 0, draws = 0;
            for (int i = 1; i <= 5; i++) {
                MultiplayerGameRunner runner = new MultiplayerGameRunner();
                runner.setLeagueLevel(league);
                runner.setSeed(1000L * league + i);
                runner.addAgent(Agent1.class, "InertialCorsairAI");
                runner.addAgent(Agent2.class, "PrivateerBoss");

                GameResult result = runner.simulate();
                int s0 = result.scores.get(0);
                int s1 = result.scores.get(1);
                if (s0 > s1) p0Wins++;
                else if (s1 > s0) p1Wins++;
                else draws++;

                System.out.printf("[League %d | Seed %d] InertialCorsairAI=%d vs PrivateerBoss=%d (Frames=%d)%n",
                    league, 1000L * league + i, s0, s1, result.views.size());
            }
            System.out.printf("==> League %d Summary: InertialCorsairAI Wins=%d, PrivateerBoss Wins=%d, Draws=%d%n%n",
                league, p0Wins, p1Wins, draws);
        }
    }
}
