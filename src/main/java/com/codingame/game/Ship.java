package com.codingame.game;

import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;

public class Ship {
    private final int id;
    private final Player owner;
    private double x;
    private double y;
    private double vx;
    private double vy;
    private int gold;
    private int cooldown;

    // Turn commands
    private boolean anchorRequested;
    private boolean sailRequested;
    private int sailTargetX;
    private int sailTargetY;
    private int sailPower;
    private boolean fireRequested;
    private int fireTargetX;
    private int fireTargetY;
    private String message = "";

    // Graphics
    private Group group;
    private Sprite sprite;
    private Text label;
    private Sprite cannonballSprite;
    private Sprite explosionSprite;

    public Ship(int id, Player owner, double x, double y) {
        this.id = id;
        this.owner = owner;
        this.x = x;
        this.y = y;
    }

    public void resetTurnCommand() {
        this.anchorRequested = false;
        this.sailRequested = false;
        this.sailTargetX = (int) Math.round(x);
        this.sailTargetY = (int) Math.round(y);
        this.sailPower = 0;
        this.fireRequested = false;
        this.fireTargetX = 0;
        this.fireTargetY = 0;
        this.message = "";
    }

    public int getId() {
        return id;
    }

    public Player getOwner() {
        return owner;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getIntX() {
        return (int) Math.round(x);
    }

    public int getIntY() {
        return (int) Math.round(y);
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public int getIntVx() {
        return (int) Math.round(vx);
    }

    public int getIntVy() {
        return (int) Math.round(vy);
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = Math.max(0, gold);
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = Math.max(0, cooldown);
    }

    public boolean isAnchorRequested() {
        return anchorRequested;
    }

    public void setAnchorRequested(boolean anchorRequested) {
        this.anchorRequested = anchorRequested;
    }

    public boolean isSailRequested() {
        return sailRequested;
    }

    public int getSailTargetX() {
        return sailTargetX;
    }

    public int getSailTargetY() {
        return sailTargetY;
    }

    public int getSailPower() {
        return sailPower;
    }

    public void setSailCommand(int tx, int ty, int power) {
        this.sailRequested = true;
        this.sailTargetX = tx;
        this.sailTargetY = ty;
        this.sailPower = Math.max(0, Math.min(100, power));
    }

    public boolean isFireRequested() {
        return fireRequested;
    }

    public int getFireTargetX() {
        return fireTargetX;
    }

    public int getFireTargetY() {
        return fireTargetY;
    }

    public void setFireCommand(int tx, int ty) {
        this.fireRequested = true;
        this.fireTargetX = tx;
        this.fireTargetY = ty;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (message != null && message.length() > 18) {
            this.message = message.substring(0, 18);
        } else {
            this.message = message == null ? "" : message;
        }
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Text getLabel() {
        return label;
    }

    public void setLabel(Text label) {
        this.label = label;
    }

    public Sprite getCannonballSprite() {
        return cannonballSprite;
    }

    public void setCannonballSprite(Sprite cannonballSprite) {
        this.cannonballSprite = cannonballSprite;
    }

    public Sprite getExplosionSprite() {
        return explosionSprite;
    }

    public void setExplosionSprite(Sprite explosionSprite) {
        this.explosionSprite = explosionSprite;
    }
}
