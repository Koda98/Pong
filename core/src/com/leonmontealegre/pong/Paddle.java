package com.leonmontealegre.pong;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Paddle {

    private Texture texture;

    protected Vector2 position;
    protected Vector2 prevPosition;
    protected Vector2 size;

    private boolean flip;

    public Paddle(float xPos, float yPos, float height, boolean flip) {
        //TODO: add a new texture using the paddle.png file

        //scales the image and sets the position to the middle of the paddle
        size = new Vector2(height * texture.getWidth()/texture.getHeight(), height);
        position = new Vector2(xPos-size.x/2, yPos-size.y/2);

        //stores the current location as the previousPosition to help calculate velocity
        prevPosition = new Vector2(position);
        this.flip = flip;
    }

    public abstract void update();

    public Vector2 getVelocity() {
        //TODO: return a new Vector2 with average velocity for x and y.
        return new Vector2((position.x - prevPosition.x) / Game.TARGET_UPS,
                            (position.y - prevPosition.y) / Game.TARGET_UPS);
    }

    public void render(SpriteBatch batch) {
        //draws it on the screen with the flip for the paddle mirroring
        batch.draw(texture, position.x, position.y, size.x, size.y, 0, 0, texture.getWidth(), texture.getHeight(), flip, false);
    }

}
