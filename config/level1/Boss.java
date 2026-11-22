import java.util.*;

class Player {
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
            for (int i = 0; i < shipCount; i++) {
                int id = in.nextInt(), owner = in.nextInt(), x = in.nextInt(), y = in.nextInt();
                int vx = in.nextInt(), vy = in.nextInt(), gold = in.nextInt(), cd = in.nextInt();
                if (owner == 0) myShips.add(new int[]{id, x, y, vx, vy, gold, cd});
            }
            int chestCount = in.nextInt();
            List<int[]> chests = new ArrayList<>();
            for (int i = 0; i < chestCount; i++) {
                chests.add(new int[]{in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt()});
            }

            for (int[] s : myShips) {
                if (s[5] >= 5 || chests.isEmpty()) {
                    System.out.println("SAIL " + myPortX + " " + myPortY + " 85");
                } else {
                    int[] best = chests.get(0);
                    double bestD = 1e9;
                    for (int[] c : chests) {
                        double d = Math.hypot(c[1] - s[1], c[2] - s[2]);
                        if (d < bestD) {
                            bestD = d;
                            best = c;
                        }
                    }
                    System.out.println("SAIL " + best[1] + " " + best[2] + " 80");
                }
            }
        }
    }
}
