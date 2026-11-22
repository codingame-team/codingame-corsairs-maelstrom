package com.codingame.game;

import java.util.ArrayList;
import java.util.List;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;
import com.codingame.gameengine.module.entities.Text;

public class Player extends AbstractMultiplayerPlayer {
    private int portX;
    private int portY;
    private final List<Ship> ships = new ArrayList<>();
    private Text scoreText;
    private Text cargoText;

    @Override
    public int getExpectedOutputLines() {
        return Math.max(1, ships.size());
    }

    public int getPortX() {
        return portX;
    }

    public int getPortY() {
        return portY;
    }

    public void setPortPosition(int x, int y) {
        this.portX = x;
        this.portY = y;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void addShip(Ship s) {
        ships.add(s);
    }

    public int getTotalCarriedGold() {
        int sum = 0;
        for (Ship s : ships) {
            sum += s.getGold();
        }
        return sum;
    }

    public Text getScoreText() {
        return scoreText;
    }

    public void setScoreText(Text scoreText) {
        this.scoreText = scoreText;
    }

    public Text getCargoText() {
        return cargoText;
    }

    public void setCargoText(Text cargoText) {
        this.cargoText = cargoText;
    }
}
