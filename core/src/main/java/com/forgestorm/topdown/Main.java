package com.forgestorm.topdown;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.forgestorm.topdown.demoUtils.RenderUtils;
import com.forgestorm.topdown.state.GameState;
import com.forgestorm.topdown.state.tk.TkGameState;
import com.forgestorm.topdown.state.voxel.VoxelGameState;
import com.forgestorm.topdown.state.voxel.VoxelGameState2;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    GameState currentState;
    private int width, height;

    public static RenderUtils renderUtils;

    @Override
    public void create() {
        //Load Render Utils
        renderUtils = new RenderUtils(() -> {
            //Generate the world after asset loading is complete
            currentState = new VoxelGameState2();
            currentState.init();
            currentState.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        });
    }

    @Override
    public void render() {
        if (!renderUtils.doneLoading()) {
            return; //We don't render anything unless our assetManager has finished loading textures
        }

        if (currentState != null && Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            switchGameStates();
        }

        if (currentState != null) {
            currentState.update();
            currentState.render();
        }
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        if (currentState != null)
            currentState.resize(width, height);
    }

    @Override
    public void dispose() {
        if (currentState != null)
            currentState.dispose();

        renderUtils.dispose();
    }

    private void switchGameStates() {
        if (currentState instanceof VoxelGameState2) {
            currentState = new TkGameState();
            currentState.init();
            currentState.resize(width, height);
        } else if (currentState instanceof TkGameState) {
            currentState = new VoxelGameState2();
            currentState.init();
            currentState.resize(width, height);
        }
    }

    public static ShaderProgram loadShader(String vert, String frag) {
        ShaderProgram temp = new ShaderProgram(Gdx.files.internal(vert), Gdx.files.internal(frag));

        if (temp.isCompiled()) {
            System.out.println("Shader Loaded");
            return temp;
        } else {
            System.out.println("Did not swap shader, failed to compile due to \n" + temp.getLog());
            return null;
        }
    }
}
