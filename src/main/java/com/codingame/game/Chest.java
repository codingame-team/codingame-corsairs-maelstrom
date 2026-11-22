package com.codingame.game;

import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;

public class Chest {
    private final int id;
    private int x;
    private int y;
    private int gold;
    private boolean active = true;

    private Group group;
    private Sprite sprite;
    private Text goldText;

    public Chest(int id, int x, int y, int gold) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.gold = gold;
    }

    public void respawn(int newX, int newY, int newGold) {
        this.x = newX;
        this.y = newY;
        this.gold = newGold;
        this.active = true;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public Text getGoldText() {
        return goldText;
    }

    public void setGoldText(Text goldText) {
        this.goldText = goldText;
    }
}
