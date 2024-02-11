package com.forgestorm.topdown.state.tk;

import com.badlogic.gdx.utils.ScreenUtils;
import com.forgestorm.topdown.state.GameState;

public class TkGameState implements GameState {

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f,0.15f,0.2f,1f, true);
    }

    @Override
    public void dispose() {

    }

}
