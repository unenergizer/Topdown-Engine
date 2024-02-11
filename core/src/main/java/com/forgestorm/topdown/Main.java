package com.forgestorm.topdown;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.forgestorm.topdown.camera.TkPerspectiveCameraController;
import com.forgestorm.topdown.state.GameState;
import com.forgestorm.topdown.state.tk.TkGameState;
import com.forgestorm.topdown.state.voxel.VoxelGameState;
import com.forgestorm.topdown.world.Chunk;
import com.forgestorm.topdown.world.ChunkManager;
import com.forgestorm.topdown.world.ChunkMeshGenerator;

import static com.badlogic.gdx.graphics.GL20.GL_DEPTH_TEST;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {

    GameState currentState;

    @Override
    public void create() {
        currentState = new VoxelGameState();
        currentState.init();
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            switchGameStates();
        }

        currentState.update();
        currentState.render();
    }

    @Override
    public void dispose() {
        currentState.dispose();
    }

    private void switchGameStates() {
        if (currentState instanceof VoxelGameState) {
            currentState = new TkGameState();
            currentState.init();
        } else if (currentState instanceof TkGameState) {
            currentState = new VoxelGameState();
            currentState.init();
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
