package com.leonmontealegre.pong;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Game extends ApplicationAdapter {

	public static final int WINNING_AMOUNT = 5;

	public static final int TARGET_UPS = 30;
	private static final float ns = 1000000000.0f / TARGET_UPS;
	private long lastTime = System.nanoTime();
	private float delta = 0;

	private SpriteBatch batch;

	private Paddle player1;
	private Paddle player2;
	private Ball ball;

	private Texture background;

	private GameState currentState;

	private UIManager ui;

	private int player1Points = 0, player2Points = 0;

	private int winner = -1;

	@Override
	public void create() {
		Gdx.input.setInputProcessor(Input.instance);

		batch = new SpriteBatch();
		ui = new UIManager(this);

		background = new Texture("background.png");

		float height = Gdx.graphics.getHeight()/5;
		player1 = new Paddle(50, Gdx.graphics.getHeight()/2, height, false) {
			@Override
			public void update() {
				//TODO: Loop through the Vector2 positions in Input.touchPositions
				//if it's on the left side, then set the previous position to the current position
				//and change the current position to the touch position, but adjusting upward
				//by half of the paddle's size
				for (Vector2 vec : Input.touchPositions)
				{

					if(vec.x < Gdx.graphics.getWidth()/2){
						prevPosition.y = position.y;
						position.y = vec.y - this.size.y/2;
						break;
					}
				}

			}
		};
		player2 = new Paddle(Gdx.graphics.getWidth()-50, Gdx.graphics.getHeight()/2, height, true) {
			@Override
			public void update() {
				//TODO: Loop through the Vector2 positions in Input.touchPositions
				//if it's on the right side, then set the previous position to the current position
				//and change the current position to the touch position, but adjusting upward
				//by half of the paddle's size
				for (Vector2 vec : Input.touchPositions)
				{

					if(vec.x > Gdx.graphics.getWidth()/2){
						prevPosition.y = position.y;
						position.y = vec.y - this.size.y/2;
						break;
					}
				}
			}
		};

		reset();
	}

	private void reset() {
		Input.touchPositions.clear();
		currentState = GameState.Start;
		float size = Gdx.graphics.getWidth()/40;
		ball = new Ball(Gdx.graphics.getWidth()/2-size/2, Gdx.graphics.getHeight()/2-size/2, size);
	}

	public void update() {
		ui.update();

		if (currentState == GameState.Playing) {
			//TODO: call update function on player1, player2, and ball
			player1.update();
			player2.update();
			ball.update();

			//TODO: if the ball collidesWith the player, call the onCollide function of the ball
			//on the player
			//Make an if statement for each player
			if(ball.collidesWith(player1))
				ball.onCollide(player1);

			if(ball.collidesWith(player2))
				ball.onCollide(player2);

			//TODO: if the ball isOutOfBounds, check which side it is on (-1 left, 1 right)
			//Update the appropriate player points for who scored
			//update the ui calling the updatePoints method with parameters of true/false for
			//player1/player2 and the amount of points
			//check if the player is over the winning amount and call gameOver with the player
			//number as the argument and return to end the method.
			//at the end of the outer if statement, reset().'
			if(ball.isOutOfBounds()){
				int side = ball.getSide();
				if(side == -1){
					player2Points++;
					ui.updatePoints(false, player2Points);

					if(player2Points >= WINNING_AMOUNT) {
						gameOver(2);
						return;
					}

				}

				else{
					player1Points++;
					ui.updatePoints(true, player1Points);

					if(player1Points <= WINNING_AMOUNT) {
						gameOver(1);
						return;
					}
				}
				reset();
			}

		}
	}

	private void gameOver(int winner) {
		this.winner = winner;
		Gdx.app.exit();
	}

	public int getWinner() {
		return winner;
	}

	@Override
	public void render() {
		//if leon hasn't explained this, ask him to explain this
		long now = System.nanoTime();
		delta += (now - lastTime) / ns;
		while (delta >= 1) {
			this.update();
			delta--;
		}

		//background color
		Gdx.gl.glClearColor(1, 1, 1, 1);
		//do not touch--important but also weird
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		final float backgroundWidth = Gdx.graphics.getHeight() * background.getWidth()/background.getHeight();
		batch.draw(background, Gdx.graphics.getWidth()/2-backgroundWidth/2, 0, backgroundWidth, Gdx.graphics.getHeight());

		player1.render(batch);
		player2.render(batch);
		ball.render(batch);

		ui.renderUI(batch);
		if (currentState == GameState.Start)
			ui.renderStartUI(batch);

		batch.end();

		lastTime = now;
	}

	public void setState(GameState state) {
		currentState = state;
	}

	public GameState getCurrentState() {
		return currentState;
	}

	public enum GameState {
		Start, Playing
	}

}
