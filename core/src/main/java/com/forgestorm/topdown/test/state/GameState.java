package com.forgestorm.topdown.test.state;

import com.badlogic.gdx.graphics.Camera;

public interface GameState {
    void init();

    void update();

    void render();

    default void resize(int width, int height) {}

    void dispose();
}
