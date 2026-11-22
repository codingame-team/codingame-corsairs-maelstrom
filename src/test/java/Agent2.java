import java.util.*;

public class Agent2 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt();
        int height = in.nextInt();
        int leagueLevel = in.nextInt();
        int myPortX = in.nextInt();
        int myPortY = in.nextInt();
        int oppPortX = in.nextInt();
        int oppPortY = in.nextInt();
        int maelstromX = in.nextInt();
        int maelstromY = in.nextInt();

        while (true) {
            int shipCount = in.nextInt();
            List<int[]> myShips = new ArrayList<>();
            List<int[]> enemyShips = new ArrayList<>();
            for (int i = 0; i < shipCount; i++) {
                int id = in.nextInt(), owner = in.nextInt(), x = in.nextInt(), y = in.nextInt();
                int vx = in.nextInt(), vy = in.nextInt(), gold = in.nextInt(), cd = in.nextInt();
                if (owner == 0) myShips.add(new int[]{id, x, y, vx, vy, gold, cd});
                else enemyShips.add(new int[]{id, x, y, vx, vy, gold, cd});
            }
            int chestCount = in.nextInt();
            List<int[]> chests = new ArrayList<>();
            for (int i = 0; i < chestCount; i++) {
                chests.add(new int[]{in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt()});
            }

            Set<Integer> claimed = new HashSet<>();
            for (int[] s : myShips) {
                int sx = s[1], sy = s[2], gold = s[5], cd = s[6];
                double distToMaelstrom = Math.hypot(sx - maelstromX, sy - maelstromY);
                if (leagueLevel >= 2 && distToMaelstrom < 130) {
                    int escapeX = sx + (sx - maelstromX) * 2;
                    int escapeY = sy + (sy - maelstromY) * 2;
                    System.out.println("SAIL " + escapeX + " " + escapeY + " 100");
                    continue;
                }
                if (gold >= 5 || chests.isEmpty()) {
                    System.out.println("SAIL " + myPortX + " " + myPortY + " 90");
                    continue;
                }

                int[] best = chests.get(0);
                double bestD = 1e9;
                int chosen = -1;
                for (int ci = 0; ci < chests.size(); ci++) {
                    int[] c = chests.get(ci);
                    double d = Math.hypot(c[1] - sx, c[2] - sy) + (claimed.contains(ci) ? 450 : 0);
                    if (d < bestD) {
                        bestD = d;
                        best = c;
                        chosen = ci;
                    }
                }
                if (chosen != -1) claimed.add(chosen);
                System.out.println("SAIL " + best[1] + " " + best[2] + " 85");
            }
        }
    }
}
