package com.forgestorm.topdown.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class CursorCatchedController extends InputAdapter {

    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE && Gdx.input.isCursorCatched()) {
            // Disable cursor catching if the user hits the Escape key.
            Gdx.input.setCursorCatched(false);
            return true;
        }
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (pointer == Input.Buttons.LEFT && !Gdx.input.isCursorCatched()) {
            // Enable cursor catching if the user left-clicks the screen.
            Gdx.input.setCursorCatched(true);
            return true;
        }
        return false;
    }
}
