package com.codingame.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.endscreen.EndScreenModule;
import com.codingame.gameengine.module.entities.Circle;
import com.codingame.gameengine.module.entities.Curve;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Rectangle;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;
import com.codingame.gameengine.module.toggle.ToggleModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;

public class Referee extends AbstractReferee {
    public static final int SEA_WIDTH = 1600;
    public static final int SEA_HEIGHT = 900;
    public static final int VIEW_OFFSET_X = 160;
    public static final int VIEW_OFFSET_Y = 140;

    public static final int MAX_TURNS = 120;
    public static final int MAELSTROM_X = 800;
    public static final int MAELSTROM_Y = 450;
    public static final int MAELSTROM_RADIUS = 380;
    public static final int ABYSS_RADIUS = 65;

    public static final int PORT_RADIUS = 130;
    public static final int PICKUP_RADIUS = 85;
    public static final int FIRE_RANGE = 450;
    public static final int BLAST_RADIUS = 115;
    public static final double BLAST_IMPULSE = 95.0;
    public static final int FIRE_COOLDOWN = 2;
    public static final double WATER_DRAG = 0.78;

    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;
    @Inject private TooltipModule tooltipModule;
    @Inject private EndScreenModule endScreenModule;
    @Inject private ToggleModule toggleModule;

    private Random rng;
    private int leagueLevel;
    private final List<Ship> allShips = new ArrayList<>();
    private final List<Chest> chests = new ArrayList<>();
    private Sprite maelstromSprite;
    private Text turnText;

    private int toViewX(double x) {
        return VIEW_OFFSET_X + (int) Math.round(x);
    }

    private int toViewY(double y) {
        return VIEW_OFFSET_Y + (int) Math.round(y);
    }

    @Override
    public void init() {
        rng = gameManager.getRandom();
        leagueLevel = Math.max(1, gameManager.getLeagueLevel());

        gameManager.setMaxTurns(MAX_TURNS);
        gameManager.setFirstTurnMaxTime(1000);
        gameManager.setTurnMaxTime(50);
        gameManager.setFrameDuration(550);

        Player p0 = gameManager.getPlayer(0);
        Player p1 = gameManager.getPlayer(1);
        p0.setPortPosition(140, 450);
        p1.setPortPosition(SEA_WIDTH - 140, 450);

        spawnShips(p0, p1);
        initChests();
        initGraphics();

        // Send initialization inputs (3=Wood 3, 2=Wood 2, 1=Wood 1; myPort first, oppPort second)
        int woodDivision = Math.max(1, 4 - leagueLevel);
        for (Player player : gameManager.getActivePlayers()) {
            Player opp = gameManager.getPlayer(1 - player.getIndex());
            player.sendInputLine(String.format("%d %d %d", SEA_WIDTH, SEA_HEIGHT, woodDivision));
            player.sendInputLine(String.format("%d %d %d %d %d %d",
                player.getPortX(), player.getPortY(), opp.getPortX(), opp.getPortY(),
                MAELSTROM_X, MAELSTROM_Y));
        }
    }

    private void spawnShips(Player p0, Player p1) {
        int shipsPerPlayer = (leagueLevel == 1) ? 1 : 2;
        int[][] offsets = {
            {60, -90},
            {60, 90}
        };
        if (shipsPerPlayer == 1) {
            offsets[0][1] = 0;
        }
        int idCounter = 0;
        for (int i = 0; i < shipsPerPlayer; i++) {
            Ship s0 = new Ship(idCounter++, p0, p0.getPortX() + offsets[i][0], p0.getPortY() + offsets[i][1]);
            p0.addShip(s0);
            allShips.add(s0);
        }
        for (int i = 0; i < shipsPerPlayer; i++) {
            Ship s1 = new Ship(idCounter++, p1, p1.getPortX() - offsets[i][0], p1.getPortY() - offsets[i][1]);
            p1.addShip(s1);
            allShips.add(s1);
        }
    }

    private void initChests() {
        chests.clear();
        int cid = 0;
        // High-value Royal Chests near the Maelstrom rim
        chests.add(new Chest(cid++, MAELSTROM_X, MAELSTROM_Y - 190, 5));
        chests.add(new Chest(cid++, MAELSTROM_X, MAELSTROM_Y + 190, 5));

        // 3 symmetric pairs of chests across the sea
        for (int i = 0; i < 3; i++) {
            int cx = 360 + rng.nextInt(320);
            int cy = 140 + i * 280 + rng.nextInt(90);
            int gold = (cx > 560) ? 3 : 2;
            chests.add(new Chest(cid++, cx, cy, gold));
            chests.add(new Chest(cid++, SEA_WIDTH - cx, SEA_HEIGHT - cy, gold));
        }
    }

    private void initGraphics() {
        // Sea background
        graphicEntityModule.createSprite()
            .setImage("background.jpg")
            .setBaseWidth(1920)
            .setBaseHeight(1080)
            .setZIndex(-20);

        // Sea sector border
        graphicEntityModule.createRectangle()
            .setX(VIEW_OFFSET_X)
            .setY(VIEW_OFFSET_Y)
            .setWidth(SEA_WIDTH)
            .setHeight(SEA_HEIGHT)
            .setFillColor(0x061b33)
            .setAlpha(0.35)
            .setLineWidth(4)
            .setLineColor(0x2d6a9f)
            .setZIndex(-15);

        // Maelstrom Vortex Visuals (League 2+)
        int mvx = toViewX(MAELSTROM_X);
        int mvy = toViewY(MAELSTROM_Y);

        if (leagueLevel >= 2) {
            Circle outerVortex = graphicEntityModule.createCircle()
                .setX(mvx)
                .setY(mvy)
                .setRadius(MAELSTROM_RADIUS)
                .setFillColor(0x0a2540)
                .setFillAlpha(0.28)
                .setLineWidth(3)
                .setLineColor(0x58a6ff)
                .setLineAlpha(0.45)
                .setZIndex(-10);
            toggleModule.displayOnToggleState(outerVortex, "debugVectors", true);

            maelstromSprite = graphicEntityModule.createSprite()
                .setImage("maelstrom.png")
                .setX(mvx)
                .setY(mvy)
                .setAnchor(0.5)
                .setBaseWidth(MAELSTROM_RADIUS * 2 - 40)
                .setBaseHeight(MAELSTROM_RADIUS * 2 - 40)
                .setAlpha(0.38)
                .setZIndex(-8);

            Circle abyssEye = graphicEntityModule.createCircle()
                .setX(mvx)
                .setY(mvy)
                .setRadius(ABYSS_RADIUS)
                .setFillColor(0x02060d)
                .setFillAlpha(0.88)
                .setLineWidth(3)
                .setLineColor(0xff4d6d)
                .setZIndex(-6);
            tooltipModule.setTooltipText(abyssEye,
                "Eye of the Maelstrom (800, 450)\nRadius: 65 (Lethal Whirlpool!)\nCurrent Field Radius: 380");
        }

        // Title & Turn Counter
        graphicEntityModule.createText("CORSAIRS OF THE MAELSTROM")
            .setX(960)
            .setY(40)
            .setAnchor(0.5)
            .setFontFamily("Lato")
            .setFontSize(42)
            .setFillColor(0xffffff)
            .setStrokeThickness(4)
            .setStrokeColor(0x000000)
            .setZIndex(30);

        turnText = graphicEntityModule.createText("TURN 0 / " + MAX_TURNS)
            .setX(960)
            .setY(92)
            .setAnchor(0.5)
            .setFontFamily("Lato")
            .setFontSize(36)
            .setFillColor(0xffffff)
            .setStrokeThickness(4)
            .setStrokeColor(0x000000)
            .setZIndex(30);

        // Player HUDs & Ports
        for (Player p : gameManager.getPlayers()) {
            int idx = p.getIndex();
            int hudX = idx == 0 ? 330 : 1590;

            graphicEntityModule.createRectangle()
                .setX(hudX - 245)
                .setY(16)
                .setWidth(490)
                .setHeight(104)
                .setFillColor(0x102138)
                .setAlpha(0.88)
                .setLineWidth(3)
                .setLineColor(p.getColorToken())
                .setZIndex(25);

            graphicEntityModule.createSprite()
                .setImage(p.getAvatarToken())
                .setX(hudX - 190)
                .setY(68)
                .setAnchor(0.5)
                .setBaseWidth(76)
                .setBaseHeight(76)
                .setZIndex(26);

            graphicEntityModule.createText(p.getNicknameToken())
                .setX(hudX - 130)
                .setY(46)
                .setAnchorX(0)
                .setAnchorY(0.5)
                .setFontFamily("Lato")
                .setFontSize(36)
                .setFillColor(p.getColorToken())
                .setZIndex(26);

            Text scoreTxt = graphicEntityModule.createText("GOLD: 0")
                .setX(hudX - 130)
                .setY(90)
                .setAnchorX(0)
                .setAnchorY(0.5)
                .setFontFamily("Lato")
                .setFontSize(38)
                .setFillColor(0xffd54f)
                .setZIndex(26);
            p.setScoreText(scoreTxt);

            Text cargoTxt = graphicEntityModule.createText("CARGO: 0")
                .setX(hudX + 135)
                .setY(90)
                .setAnchor(0.5)
                .setFontFamily("Lato")
                .setFontSize(36)
                .setFillColor(0xb3e5fc)
                .setZIndex(26);
            p.setCargoText(cargoTxt);

            // Home Port
            Circle portZone = graphicEntityModule.createCircle()
                .setX(toViewX(p.getPortX()))
                .setY(toViewY(p.getPortY()))
                .setRadius(PORT_RADIUS)
                .setFillColor(p.getColorToken())
                .setFillAlpha(0.14)
                .setLineWidth(3)
                .setLineColor(p.getColorToken())
                .setLineAlpha(0.55)
                .setZIndex(-4);
            toggleModule.displayOnToggleState(portZone, "debugVectors", true);

            String portImg = idx == 0 ? "port_p0.png" : "port_p1.png";
            Sprite portSprite = graphicEntityModule.createSprite()
                .setImage(portImg)
                .setX(toViewX(p.getPortX()))
                .setY(toViewY(p.getPortY()))
                .setAnchor(0.5)
                .setBaseWidth(82)
                .setBaseHeight(82)
                .setZIndex(4);

            tooltipModule.setTooltipText(portSprite,
                String.format("Home Port (Player %d)\nPos: (%d, %d)\nDelivery Radius: %d",
                    idx, p.getPortX(), p.getPortY(), PORT_RADIUS));
        }

        // Chests
        for (Chest c : chests) {
            Sprite cs = graphicEntityModule.createSprite()
                .setImage("chest.png")
                .setAnchor(0.5)
                .setBaseWidth(44)
                .setBaseHeight(44);

            Text gt = graphicEntityModule.createText(c.getGold() + "g")
                .setY(-34)
                .setAnchor(0.5)
                .setFontFamily("Lato")
                .setFontSize(36)
                .setFillColor(0xffd54f)
                .setStrokeThickness(3)
                .setStrokeColor(0x000000);

            Group cg = graphicEntityModule.createGroup(cs, gt)
                .setX(toViewX(c.getX()))
                .setY(toViewY(c.getY()))
                .setZIndex(5);

            c.setSprite(cs);
            c.setGoldText(gt);
            c.setGroup(cg);
            updateChestTooltip(c);
        }

        // Ships & Cannon FX
        for (Ship s : allShips) {
            String shipImg = s.getOwner().getIndex() == 0 ? "ship_p0.png" : "ship_p1.png";
            Sprite ss = graphicEntityModule.createSprite()
                .setImage(shipImg)
                .setAnchor(0.5)
                .setBaseWidth(72)
                .setBaseHeight(48);
            if (s.getOwner().getIndex() == 1) {
                ss.setRotation(Math.PI);
            }

            Text lbl = graphicEntityModule.createText("0g")
                .setY(-38)
                .setAnchor(0.5)
                .setFontFamily("Lato")
                .setFontSize(36)
                .setFillColor(0xffffff)
                .setStrokeThickness(3)
                .setStrokeColor(0x000000);
            toggleModule.displayOnToggleState(lbl, "debugVectors", true);

            Group sg = graphicEntityModule.createGroup(ss, lbl)
                .setX(toViewX(s.getX()))
                .setY(toViewY(s.getY()))
                .setZIndex(10);

            Sprite cb = graphicEntityModule.createSprite()
                .setImage("cannonball.png")
                .setAnchor(0.5)
                .setBaseWidth(26)
                .setBaseHeight(26)
                .setVisible(false)
                .setZIndex(15);

            Sprite exp = graphicEntityModule.createSprite()
                .setImage("explosion.png")
                .setAnchor(0.5)
                .setBaseWidth(88)
                .setBaseHeight(88)
                .setVisible(false)
                .setZIndex(16);

            s.setSprite(ss);
            s.setLabel(lbl);
            s.setGroup(sg);
            s.setCannonballSprite(cb);
            s.setExplosionSprite(exp);
            updateShipTooltip(s);
        }
    }

    private void updateChestTooltip(Chest c) {
        tooltipModule.setTooltipText(c.getGroup(),
            String.format("Treasure Chest %d\nPos: (%d, %d)\nValue: %d Gold",
                c.getId(), c.getX(), c.getY(), c.getGold()));
    }

    private void updateShipTooltip(Ship s) {
        tooltipModule.setTooltipText(s.getGroup(),
            String.format("Corsair %d (Player %d)\nPos: (%d, %d) | Vel: (%d, %d)\nCargo: %d Gold | Cooldown: %d",
                s.getId(), s.getOwner().getIndex(),
                s.getIntX(), s.getIntY(), s.getIntVx(), s.getIntVy(),
                s.getGold(), s.getCooldown()));
    }

    @Override
    public void gameTurn(int turn) {
        turnText.setText("TURN " + turn + " / " + MAX_TURNS);
        if (maelstromSprite != null) {
            maelstromSprite.setRotation(maelstromSprite.getRotation() + 0.35);
        }

        for (Ship s : allShips) {
            s.setCooldown(s.getCooldown() - 1);
            s.resetTurnCommand();
            s.getCannonballSprite().setVisible(false);
            s.getExplosionSprite().setVisible(false);
        }

        // 1. Send inputs to each active player (Owner is ALWAYS 0 for self, 1 for opponent)
        for (Player player : gameManager.getActivePlayers()) {
            sendTurnInput(player, turn);
            player.execute();
        }

        // 2. Read and validate player outputs
        for (Player player : gameManager.getActivePlayers()) {
            try {
                List<String> outputs = player.getOutputs();
                parsePlayerOutputs(player, outputs);
            } catch (TimeoutException e) {
                disqualifyPlayer(player, "timed out!");
            }
        }

        if (gameManager.getActivePlayers().size() < 2) {
            gameManager.endGame();
            return;
        }

        // 3. Apply Thrust / Anchor, Maelstrom Vector Field, and Kinematics
        resolvePhysicsAndCurrents();

        // 4. Resolve Cannonball Impacts (League 3+)
        if (leagueLevel >= 3) {
            resolveCannonFire();
        }

        // 5. Check Maelstrom Whirlpool Eye, Chest Pickups, Port Deliveries & Respawns
        resolveMaelstromHazardsAndLoot();

        // 6. Refresh HUD & Visuals
        refreshGraphics();
        checkEarlyEnd(turn);
    }

    private void sendTurnInput(Player player, int turn) {
        int myIdx = player.getIndex();
        player.sendInputLine(String.valueOf(turn));
        List<Ship> orderedShips = new ArrayList<>();
        for (Ship s : allShips) {
            if (s.getOwner().getIndex() == myIdx) orderedShips.add(s);
        }
        for (Ship s : allShips) {
            if (s.getOwner().getIndex() != myIdx) orderedShips.add(s);
        }

        player.sendInputLine(String.valueOf(orderedShips.size()));
        for (Ship s : orderedShips) {
            int relOwner = (s.getOwner().getIndex() == myIdx) ? 0 : 1;
            player.sendInputLine(String.format("%d %d %d %d %d %d %d %d",
                s.getId(), relOwner, s.getIntX(), s.getIntY(),
                s.getIntVx(), s.getIntVy(), s.getGold(), s.getCooldown()));
        }

        List<Chest> activeChests = new ArrayList<>();
        for (Chest c : chests) {
            if (c.isActive()) activeChests.add(c);
        }
        player.sendInputLine(String.valueOf(activeChests.size()));
        for (Chest c : activeChests) {
            player.sendInputLine(String.format("%d %d %d %d",
                c.getId(), c.getX(), c.getY(), c.getGold()));
        }
    }

    private void parsePlayerOutputs(Player player, List<String> outputs) {
        List<Ship> myShips = player.getShips();
        for (int i = 0; i < myShips.size() && i < outputs.size(); i++) {
            Ship ship = myShips.get(i);
            String line = outputs.get(i).trim();
            if (line.isEmpty()) {
                disqualifyPlayer(player, "sent an empty command line!");
                return;
            }
            String[] tokens = line.split("\\s+");
            int cursor = 0;

            try {
                String cmd = tokens[cursor++].toUpperCase();
                if ("SAIL".equals(cmd)) {
                    if (cursor + 2 >= tokens.length) {
                        disqualifyPlayer(player, "invalid SAIL syntax (expected SAIL x y power): " + line);
                        return;
                    }
                    int tx = Integer.parseInt(tokens[cursor++]);
                    int ty = Integer.parseInt(tokens[cursor++]);
                    int power = Integer.parseInt(tokens[cursor++]);
                    ship.setSailCommand(tx, ty, power);
                } else if ("ANCHOR".equals(cmd)) {
                    ship.setAnchorRequested(true);
                } else if ("WAIT".equals(cmd)) {
                    // Coast with current velocity
                } else if ("FIRE".equals(cmd)) {
                    if (cursor + 1 >= tokens.length) {
                        disqualifyPlayer(player, "invalid FIRE syntax (expected FIRE x y): " + line);
                        return;
                    }
                    int fx = Integer.parseInt(tokens[cursor++]);
                    int fy = Integer.parseInt(tokens[cursor++]);
                    if (leagueLevel >= 3) {
                        ship.setFireCommand(fx, fy);
                    } else {
                        gameManager.addToGameSummary(player.getNicknameToken()
                            + ": FIRE is not unlocked in this league.");
                    }
                } else {
                    disqualifyPlayer(player, "unrecognized command '" + cmd + "'");
                    return;
                }

                // Optional secondary action: FIRE x y or MSG text
                while (cursor < tokens.length) {
                    String sub = tokens[cursor++].toUpperCase();
                    if ("FIRE".equals(sub)) {
                        if (cursor + 1 >= tokens.length) {
                            disqualifyPlayer(player, "invalid FIRE syntax: " + line);
                            return;
                        }
                        int fx = Integer.parseInt(tokens[cursor++]);
                        int fy = Integer.parseInt(tokens[cursor++]);
                        if (leagueLevel >= 3) {
                            ship.setFireCommand(fx, fy);
                        }
                    } else if ("MSG".equals(sub)) {
                        StringBuilder sb = new StringBuilder();
                        while (cursor < tokens.length) {
                            if (sb.length() > 0) sb.append(' ');
                            sb.append(tokens[cursor++]);
                        }
                        ship.setMessage(sb.toString());
                    } else {
                        disqualifyPlayer(player, "unrecognized token '" + sub + "'");
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                disqualifyPlayer(player, "expected integer parameter in '" + line + "'");
                return;
            }
        }
    }

    private void disqualifyPlayer(Player player, String reason) {
        player.deactivate(player.getNicknameToken() + " " + reason);
        player.setScore(-1);
        gameManager.addTooltip(player, player.getNicknameToken() + " disqualified: " + reason);
        gameManager.addToGameSummary(GameManager.formatErrorMessage(player.getNicknameToken() + " " + reason));
    }

    private void resolvePhysicsAndCurrents() {
        for (Ship s : allShips) {
            double vx = s.getVx();
            double vy = s.getVy();

            // 1. Thrust or Anchor
            if (s.isAnchorRequested()) {
                vx *= 0.25;
                vy *= 0.25;
            } else if (s.isSailRequested() && s.getSailPower() > 0) {
                double dx = s.getSailTargetX() - s.getX();
                double dy = s.getSailTargetY() - s.getY();
                double dist = Math.hypot(dx, dy);
                if (dist > 1e-3) {
                    // Variable mass: heavier cargo reduces acceleration!
                    double massFactor = 1.0 + 0.15 * s.getGold();
                    double accel = s.getSailPower() / massFactor;
                    vx += (dx / dist) * accel;
                    vy += (dy / dist) * accel;
                }
            }

            // 2. Maelstrom Vector Field (League 2+)
            if (leagueLevel >= 2) {
                double rx = MAELSTROM_X - s.getX();
                double ry = MAELSTROM_Y - s.getY();
                double r = Math.hypot(rx, ry);
                if (r > 1e-3 && r <= MAELSTROM_RADIUS) {
                    double urx = rx / r;
                    double ury = ry / r;
                    // Counter-clockwise tangential vector
                    double utx = -ury;
                    double uty = urx;
                    double intensity = 1.0 - (r / MAELSTROM_RADIUS);
                    double swirl = 18.0 + 26.0 * intensity;
                    double pull = 10.0 + 22.0 * intensity;
                    double anchorDamping = s.isAnchorRequested() ? 0.5 : 1.0;

                    vx += (utx * swirl + urx * pull) * anchorDamping;
                    vy += (uty * swirl + ury * pull) * anchorDamping;
                }
            }

            // 3. Move ship and clamp to sea boundaries with bounce damping
            double nx = s.getX() + vx;
            double ny = s.getY() + vy;
            if (nx < 40) {
                nx = 40;
                vx = -vx * 0.4;
            } else if (nx > SEA_WIDTH - 40) {
                nx = SEA_WIDTH - 40;
                vx = -vx * 0.4;
            }
            if (ny < 40) {
                ny = 40;
                vy = -vy * 0.4;
            } else if (ny > SEA_HEIGHT - 40) {
                ny = SEA_HEIGHT - 40;
                vy = -vy * 0.4;
            }

            if (Math.hypot(vx, vy) > 2.0) {
                s.getSprite().setRotation(Math.atan2(vy, vx));
            }

            // 4. Water friction drag
            s.setX(nx);
            s.setY(ny);
            s.setVx(vx * WATER_DRAG);
            s.setVy(vy * WATER_DRAG);

            s.getGroup().setX(toViewX(nx), Curve.LINEAR)
                        .setY(toViewY(ny), Curve.LINEAR);
        }
    }

    private void resolveCannonFire() {
        for (Ship shooter : allShips) {
            if (!shooter.isFireRequested()) continue;
            if (shooter.getCooldown() > 0) {
                gameManager.addToGameSummary(shooter.getOwner().getNicknameToken()
                    + ": Corsair " + shooter.getId() + " cannons reloading (cooldown=" + shooter.getCooldown() + ").");
                continue;
            }

            int fx = shooter.getFireTargetX();
            int fy = shooter.getFireTargetY();
            double dist = Math.hypot(fx - shooter.getX(), fy - shooter.getY());
            if (dist > FIRE_RANGE) {
                double ratio = FIRE_RANGE / dist;
                fx = (int) Math.round(shooter.getX() + (fx - shooter.getX()) * ratio);
                fy = (int) Math.round(shooter.getY() + (fy - shooter.getY()) * ratio);
            }
            shooter.setCooldown(FIRE_COOLDOWN);

            // Animate cannonball & blast
            Sprite cb = shooter.getCannonballSprite();
            cb.setX(toViewX(shooter.getX()))
              .setY(toViewY(shooter.getY()))
              .setVisible(true);
            graphicEntityModule.commitEntityState(0.1, cb);
            cb.setX(toViewX(fx), Curve.LINEAR)
              .setY(toViewY(fy), Curve.LINEAR);
            graphicEntityModule.commitEntityState(0.75, cb);
            cb.setVisible(false);

            Sprite exp = shooter.getExplosionSprite();
            exp.setX(toViewX(fx))
               .setY(toViewY(fy))
               .setVisible(true);

            // Apply knockback impulse and gold drop to ships within BLAST_RADIUS
            for (Ship target : allShips) {
                double dToBlast = Math.hypot(target.getX() - fx, target.getY() - fy);
                if (dToBlast <= BLAST_RADIUS) {
                    double ux = (dToBlast < 1e-3) ? (shooter.getX() < fx ? 1 : -1) : (target.getX() - fx) / dToBlast;
                    double uy = (dToBlast < 1e-3) ? 0 : (target.getY() - fy) / dToBlast;
                    double scale = 1.0 - 0.35 * (dToBlast / BLAST_RADIUS);

                    target.setVx(target.getVx() + ux * BLAST_IMPULSE * scale);
                    target.setVy(target.getVy() + uy * BLAST_IMPULSE * scale);
                    target.setX(Math.max(40, Math.min(SEA_WIDTH - 40, target.getX() + ux * 45 * scale)));
                    target.setY(Math.max(40, Math.min(SEA_HEIGHT - 40, target.getY() + uy * 45 * scale)));
                    target.getGroup().setX(toViewX(target.getX()), Curve.EASE_OUT)
                                     .setY(toViewY(target.getY()), Curve.EASE_OUT);

                    if (target.getOwner() != shooter.getOwner() && target.getGold() > 0) {
                        int stolen = Math.min(2, target.getGold());
                        target.setGold(target.getGold() - stolen);
                        shooter.setGold(shooter.getGold() + stolen);
                        gameManager.addToGameSummary(GameManager.formatSuccessMessage(
                            String.format("%s's Corsair %d blasted Corsair %d and plundered %d Gold!",
                                shooter.getOwner().getNicknameToken(), shooter.getId(), target.getId(), stolen)));
                    }
                }
            }
        }
    }

    private void resolveMaelstromHazardsAndLoot() {
        // 1. Check Maelstrom Eye (League 2+)
        if (leagueLevel >= 2) {
            for (Ship s : allShips) {
                double distToEye = Math.hypot(s.getX() - MAELSTROM_X, s.getY() - MAELSTROM_Y);
                if (distToEye < ABYSS_RADIUS) {
                    Player owner = s.getOwner();
                    s.setGold(0);
                    s.setVx(0);
                    s.setVy(0);
                    s.setX(owner.getPortX());
                    s.setY(owner.getPortY());
                    s.getGroup().setX(toViewX(s.getX()), Curve.EASE_IN)
                                .setY(toViewY(s.getY()), Curve.EASE_IN);
                    gameManager.addTooltip(owner, owner.getNicknameToken() + " swallowed by Maelstrom!");
                    gameManager.addToGameSummary(GameManager.formatErrorMessage(
                        owner.getNicknameToken() + "'s Corsair " + s.getId()
                            + " was swallowed by the Maelstrom and lost all cargo!"));
                }
            }
        }

        // 2. Collect Treasure Chests
        for (Chest c : chests) {
            if (!c.isActive()) continue;
            Ship collector = null;
            double bestDist = PICKUP_RADIUS;
            for (Ship s : allShips) {
                double d = Math.hypot(s.getX() - c.getX(), s.getY() - c.getY());
                if (d <= bestDist) {
                    bestDist = d;
                    collector = s;
                }
            }
            if (collector != null) {
                collector.setGold(collector.getGold() + c.getGold());
                c.setActive(false);
                c.getGroup().setVisible(false);
            }
        }

        // 3. Deliver Gold at Home Port
        for (Ship s : allShips) {
            if (s.getGold() > 0) {
                Player owner = s.getOwner();
                double distToPort = Math.hypot(s.getX() - owner.getPortX(), s.getY() - owner.getPortY());
                if (distToPort <= PORT_RADIUS) {
                    int delivered = s.getGold();
                    owner.setScore(owner.getScore() + delivered);
                    s.setGold(0);
                    gameManager.addToGameSummary(GameManager.formatSuccessMessage(
                        String.format("%s's Corsair %d delivered %d Gold to Port!",
                            owner.getNicknameToken(), s.getId(), delivered)));
                }
            }
        }

        // 4. Respawn symmetric pairs of chests if active count drops below 4
        int activeCount = 0;
        List<Chest> inactive = new ArrayList<>();
        for (Chest c : chests) {
            if (c.isActive()) activeCount++;
            else inactive.add(c);
        }

        if (activeCount < 4 && inactive.size() >= 2) {
            int cx = 360 + rng.nextInt(340);
            int cy = 130 + rng.nextInt(SEA_HEIGHT - 260);
            int gold = (Math.hypot(cx - MAELSTROM_X, cy - MAELSTROM_Y) < 280) ? 4 : 2;

            Chest c1 = inactive.get(0);
            c1.respawn(cx, cy, gold);
            c1.getGoldText().setText(gold + "g");
            c1.getGroup().setX(toViewX(cx)).setY(toViewY(cy)).setVisible(true);

            Chest c2 = inactive.get(1);
            c2.respawn(SEA_WIDTH - cx, SEA_HEIGHT - cy, gold);
            c2.getGoldText().setText(gold + "g");
            c2.getGroup().setX(toViewX(SEA_WIDTH - cx)).setY(toViewY(SEA_HEIGHT - cy)).setVisible(true);
        }
    }

    private void refreshGraphics() {
        for (Player p : gameManager.getPlayers()) {
            p.getScoreText().setText("GOLD: " + p.getScore());
            p.getCargoText().setText("CARGO: " + p.getTotalCarriedGold());
        }
        for (Chest c : chests) {
            if (c.isActive()) updateChestTooltip(c);
        }
        for (Ship s : allShips) {
            s.getLabel().setText(s.getGold() + "g");
            updateShipTooltip(s);
        }
    }

    private void checkEarlyEnd(int turn) {
        Player p0 = gameManager.getPlayer(0);
        Player p1 = gameManager.getPlayer(1);
        // First to 35 delivered gold wins immediately!
        if (p0.getScore() >= 35 || p1.getScore() >= 35) {
            if (p0.getScore() != p1.getScore()) {
                gameManager.addToGameSummary(GameManager.formatSuccessMessage(
                    "Target treasure quota of 35 Gold reached!"));
                gameManager.endGame();
            }
        }
    }

    @Override
    public void onEnd() {
        // Tie-breaker: add carried cargo if scores are tied at turn 120
        Player p0 = gameManager.getPlayer(0);
        Player p1 = gameManager.getPlayer(1);
        if (p0.getScore() == p1.getScore() && p0.getScore() >= 0 && p1.getScore() >= 0) {
            p0.setScore(p0.getScore() * 10 + p0.getTotalCarriedGold());
            p1.setScore(p1.getScore() * 10 + p1.getTotalCarriedGold());
        }

        int[] scores = gameManager.getPlayers().stream().mapToInt(Player::getScore).toArray();
        String[] texts = gameManager.getPlayers().stream()
            .map(p -> p.getScore() + " Gold (+ " + p.getTotalCarriedGold() + " aboard)")
            .toArray(String[]::new);
        endScreenModule.setScores(scores, texts);
    }
}
