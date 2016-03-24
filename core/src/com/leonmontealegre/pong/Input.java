package com.leonmontealegre.pong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Input implements InputProcessor {

    public static final Input instance = new Input();

    //list of current locations of fingers on the screen
    public static List<Vector2> touchPositions = new ArrayList<Vector2>();

    private Input() {}

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //TODO: add a new Vector2 representing where the finger is located
        //The tricky thing is that the y value has to be flipped because the
        //BOTTOM left corner is 0,0 in LibGDX.
        Vector2 vector2 = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
        touchPositions.add(vector2);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (touchPositions.size() > 0) {
            if (pointer < touchPositions.size())
                touchPositions.remove(pointer);
            else
                touchPositions.remove(touchPositions.size() - 1);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (touchPositions.size() > 0) {
            touchPositions.set(pointer % touchPositions.size(), new Vector2(screenX, Gdx.graphics.getHeight() - screenY));
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) { return false;}

    @Override
    public boolean scrolled(int amount) { return false; }

    @Override
    public boolean keyDown(int keycode) { return false; }

    @Override
    public boolean keyUp(int keycode) { return false; }

    @Override
    public boolean keyTyped(char character) { return false; }

}
