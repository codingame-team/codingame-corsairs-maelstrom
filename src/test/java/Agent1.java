import java.util.*;

/**
 * Advanced Kinematics & Ballistics Agent for Corsairs of the Maelstrom:
 * - Exact Maelstrom Vector Field model: computes the swirl + pull vector C(x, y)
 * - Inertial PD Controller: aims thrust along (Target - Pos) - (Vel + Current) to cancel lateral drift
 * - Dynamic Return-to-Port threshold based on distance to Port and variable cargo mass
 * - Predictive Cannon Ballistics: leads enemy ships using their velocity + Maelstrom current
 */
public class Agent1 {
    static final int MAELSTROM_RADIUS = 380;

    static double[] getMaelstromCurrent(double x, double y, int mx, int my, int leagueLevel) {
        if (leagueLevel < 2) return new double[]{0, 0};
        double rx = mx - x;
        double ry = my - y;
        double r = Math.hypot(rx, ry);
        if (r <= 1e-3 || r > MAELSTROM_RADIUS) return new double[]{0, 0};
        double urx = rx / r;
        double ury = ry / r;
        double utx = -ury;
        double uty = urx;
        double intensity = 1.0 - (r / MAELSTROM_RADIUS);
        double swirl = 18.0 + 26.0 * intensity;
        double pull = 10.0 + 22.0 * intensity;
        return new double[]{utx * swirl + urx * pull, uty * swirl + ury * pull};
    }

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

            Set<Integer> assignedChests = new HashSet<>();

            for (int[] s : myShips) {
                int sx = s[1], sy = s[2], vx = s[3], vy = s[4], gold = s[5], cd = s[6];
                double[] cur = getMaelstromCurrent(sx, sy, maelstromX, maelstromY, leagueLevel);
                double driftX = sx + vx + cur[0];
                double driftY = sy + vy + cur[1];

                // 1. Choose target destination (Home Port or optimal value/distance Chest)
                int targetX = myPortX;
                int targetY = myPortY;

                double distToPort = Math.hypot(myPortX - sx, myPortY - sy);
                boolean returnToPort = (gold >= 4) || (gold >= 2 && distToPort < 290) || chests.isEmpty();

                if (!returnToPort) {
                    double bestScore = -1e9;
                    int bestIdx = -1;
                    for (int ci = 0; ci < chests.size(); ci++) {
                        if (assignedChests.contains(ci)) continue;
                        int[] c = chests.get(ci);
                        double dToChest = Math.hypot(c[1] - driftX, c[2] - driftY);
                        double dChestToPort = Math.hypot(c[1] - myPortX, c[2] - myPortY);
                        double dChestToEye = Math.hypot(c[1] - maelstromX, c[2] - maelstromY);
                        if (leagueLevel >= 2 && dChestToEye < 100) continue; // too close to abyss
                        double score = c[3] * 180.0 - dToChest - 0.35 * dChestToPort;
                        if (score > bestScore) {
                            bestScore = score;
                            bestIdx = ci;
                            targetX = c[1];
                            targetY = c[2];
                        }
                    }
                    if (bestIdx != -1) assignedChests.add(bestIdx);
                }

                // 2. Compute velocity-compensated steering vector
                double errX = targetX - driftX;
                double errY = targetY - driftY;

                // Safety repulsion if predicted drift enters near Maelstrom Eye (< 120)
                if (leagueLevel >= 2) {
                    double predEyeDist = Math.hypot(driftX - maelstromX, driftY - maelstromY);
                    if (predEyeDist < 135) {
                        double pushOutX = (driftX - maelstromX) / Math.max(1.0, predEyeDist);
                        double pushOutY = (driftY - maelstromY) / Math.max(1.0, predEyeDist);
                        errX = pushOutX * 400.0;
                        errY = pushOutY * 400.0;
                    }
                }

                int aimX = (int) Math.round(sx + errX);
                int aimY = (int) Math.round(sy + errY);
                double massFactor = 1.0 + 0.15 * gold;
                int power = (int) Math.min(100, Math.round(Math.hypot(errX, errY) * massFactor));

                // 3. Predictive Cannon Ballistics (League 3+)
                String fireCmd = "";
                if (leagueLevel >= 3 && cd == 0) {
                    for (int[] e : enemyShips) {
                        double[] eCur = getMaelstromCurrent(e[1], e[2], maelstromX, maelstromY, leagueLevel);
                        int predEx = (int) Math.round(e[1] + e[3] + eCur[0]);
                        int predEy = (int) Math.round(e[2] + e[4] + eCur[1]);
                        double dToPred = Math.hypot(predEx - sx, predEy - sy);
                        // Make sure we don't blast ourselves!
                        double selfNextX = driftX + (errX / Math.max(1.0, Math.hypot(errX, errY))) * (power / massFactor);
                        double selfNextY = driftY + (errY / Math.max(1.0, Math.hypot(errX, errY))) * (power / massFactor);
                        if (dToPred <= 435 && Math.hypot(predEx - selfNextX, predEy - selfNextY) > 130) {
                            if (e[5] > 0 || Math.hypot(predEx - maelstromX, predEy - maelstromY) < 200) {
                                fireCmd = " FIRE " + predEx + " " + predEy + " MSG BOOM!";
                                break;
                            }
                        }
                    }
                }

                System.out.println("SAIL " + aimX + " " + aimY + " " + Math.max(10, power) + fireCmd);
            }
        }
    }
}
