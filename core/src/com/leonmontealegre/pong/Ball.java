package com.leonmontealegre.pong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Ball {

    private static final float START_SPEED = 24f;

    private Texture texture;

    private Vector2 position;
    private float size;

    private Vector2 velocity;

    public Ball(float xPos, float yPos, float size) {
        this.texture = new Texture("ball.png");
        this.position = new Vector2(xPos, yPos);
        //TODO: change these startspeed values. x speed has a random sign of START_SPEED
        //y speed is random value between START_SPEED/2 and START_SPEED with a random sign
        //HINT: Use MathUtils.randomSign() to get a random 1 or -1 for multiplication porpoises
        float startSpeedX = MathUtils.randomSign() * START_SPEED;
        float startSpeedY = MathUtils.randomSign() * MathUtils.random(START_SPEED/2, START_SPEED);
        this.velocity = new Vector2(startSpeedX, startSpeedY);
        this.size = size;
    }

    public void update() {
        //TODO: add the velocity to the position for x and y.  Hint: access x with velocity.x
        this.position.x += velocity.x;
        this.position.y += velocity.y;

        //TODO: implement collision detection in the y direction.  The ball bounces if it
        //hits the top or the bottom.  When it hits it, flip the y velocity and maintain x velocity
        if(this.position.y >= Gdx.graphics.getHeight()){
            velocity.y = -velocity.y;
            position.y = Gdx.graphics.getHeight() - size - 1;
        }

        if(position.y <= 0){
            velocity.y = -velocity.y;
            position.y = 1;
        }

    }

    public void onCollide(Paddle paddle) {
        //TODO: Move the ball out from the paddle so it isn't infinitely colliding
        //Flip the x velocity to make it rebound
        if(velocity.x > 0){
            velocity.x = -velocity.x;
            position.x = paddle.position.x - this.size - 1;
        }

        else if(velocity.x < 0){
            velocity.x = -velocity.x;
            position.x = paddle.position.x + 1;
        }
        //Inherit a little bit of y velocity from the paddle's velocity so that it goes off
        //at a slightly different angle
        if((paddle.getVelocity().y < 0 && this.velocity.y > 0) ||
                (paddle.getVelocity().y > 0 && this.velocity.y < 0)){
            this.velocity.y = -1;
        this.velocity.y += paddle.getVelocity().y;
        }

    }

    public boolean collidesWith(Paddle paddle) {
        //TODO: check to see if the ball is inside the paddle's rectangular bounds
        //The ball is also a rectangle.
        if (this.position.x < paddle.position.x + paddle.size.x
                && this.position.x+size > paddle.position.x
                && this.position.y+size > paddle.position.y
                && this.position.y < paddle.position.y + paddle.size.y){
            return true;
        }

        return false;
    }

    public boolean isOutOfBounds() {
        //checks if the ball is off the screen on the left or right side
        return this.position.x <= 0 || this.position.x+this.size >= Gdx.graphics.getWidth();
    }

    public int getSide() {
        //returns what side the ball is currently on.  -1 is left, 1 is right
        return this.position.x <= Gdx.graphics.getWidth()/2 ? -1 : 1;
    }

    public void render(SpriteBatch batch) {
        //TODO: draw the sprite to batch
        batch.draw(texture, position.x, position.y, size, size);
    }

}
