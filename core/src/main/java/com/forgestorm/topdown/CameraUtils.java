package com.forgestorm.topdown;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

import static com.forgestorm.topdown.GameConstants.Chunk.QUAD_WIDTH;

public final class CameraUtils {
    private static final int RESOLUTION_FACTOR = 100;

    public static OrthographicCamera createOrthographicCamera(int viewportWidth, int viewportHeight) {

        OrthographicCamera camera = new OrthographicCamera(
            getOrthographicViewportWidth(viewportWidth),
            getOrthographicViewportHeight(viewportHeight));
        camera.near = 0.01f;
        camera.far = 100f;
        camera.position.set(8, 0, 17 * QUAD_WIDTH);
//        camera.direction.rotate(Vector3.Y, 45.0F);
        camera.direction.rotate(Vector3.X, 45.0F);
        camera.up.set(Vector3.Y);
        return camera;
    }

    public static float getOrthographicViewportWidth(int viewportWidth) {
        return (float) viewportWidth / RESOLUTION_FACTOR;
    }

    public static float getOrthographicViewportHeight(int viewportHeight) {
        return (float) viewportHeight / RESOLUTION_FACTOR;
    }

    public static PerspectiveCamera createPerspectiveCamera(int viewportWidth, int viewportHeight) {
        PerspectiveCamera camera = new PerspectiveCamera(67f, viewportWidth, viewportHeight);
        camera.near = 0.01f;
        camera.far = 1000f;
        camera.position.set(-5f, 50f, -5f);
        camera.up.set(Vector3.Y);
        camera.lookAt(0, 45, 0);
        return camera;
    }
}
