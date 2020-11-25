package br.pucpr.game;

import org.joml.Random;
import org.joml.Vector3f;

import java.util.Vector;

public class Ball {

    Vector3f position = new Vector3f(0f, 0f, 0f);
    Vector3f force = new Vector3f(1f, 3f, 0f);
    float maxX, maxY;

    public Ball(float maxXY) {
        this.maxX = maxXY;
        this.maxY = maxXY;
    }

    public void addPos(float secs) {
        if (position.y >= 2.3f || position.y <= -2.3f)
            invertForceDirection(false);

        position.add(new Vector3f(force.x * secs, force.y * secs, force.z * secs));
    }

    public Vector3f getPos() {
        return position;
    }


    public void resetPos() {
        position = new Vector3f(0f, 0f, 0f);
        SetForceDirection();
    }

    public void SetForceDirection() {
        Random rand = new Random();
        float x = rand.nextFloat() * 3f;

        if (x < 1f)
            x = 1f;

        int aux = rand.nextInt(4);
        if (aux == 0)
            force = new Vector3f(x, 3f, 0f);
        else if (aux == 1)
            force = new Vector3f(-x, 3f, 0f);
        else if (aux == 2)
            force = new Vector3f(x, -3f, 0f);
        else
            force = new Vector3f(-x, -3f, 0f);
    }

    public void invertForceDirection(boolean isXDirection) {
        if (isXDirection)
            force.x *= -1f;
        else
            force.y *= -1f;
    }

    public void checkPlayerCollision(Vector3f playerPos, float maxX, float maxY) {
        if (position.x - (this.maxX / 2) < playerPos.x + (maxX / 2)
                && position.x + (this.maxX / 2) > playerPos.x - (maxX / 2)
                && position.y + (this.maxY / 2) > playerPos.y - (maxY / 2)
                && position.y - (this.maxY / 2) < playerPos.y + (maxY / 2))
            invertForceDirection(true);
    }
}
