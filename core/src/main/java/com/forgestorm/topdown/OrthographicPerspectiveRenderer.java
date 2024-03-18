package com.forgestorm.topdown;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * This class handles the camera and rendering
 */
public class OrthographicPerspectiveRenderer {

    public OrthographicCamera camera;
    private Vector3 focusPoint = new Vector3();

    int width;
    int height;

    public OrthographicPerspectiveRenderer(int width, int height) {
        camera = new OrthographicCamera();
        this.width = width;
        this.height = height;
        reCalcCamera(true);
    }

    public void translate(Vector3 translation) {
        if (translation.z > 0) {
            camera.position.z += distort(translation.z);
        }
        if (translation.z < 0) {
            camera.position.z -= distort(translation.z * -1);
        }
        if (translation.y > 0) {
            camera.position.y -= distort(translation.y);
        }
        if (translation.y < 0) {
            camera.position.y += distort(translation.y * -1);
        }
        if (translation.x < 0) {
            camera.position.x += translation.x * -1;
        }
        if (translation.x > 0) {
            camera.position.x -= translation.x;
        }
    }

    public void update() {
        camera.update();
    }

    private void reCalcCamera(boolean updateFocus) {
        camera.setToOrtho(false, width, height);
        camera.rotate(new Quaternion().setEulerAngles(0,-45,0));
        camera.far = 300f;
        camera.update();
        Vector3[] points = camera.frustum.planePoints;
        float m = (points[4].y - points[0].y) / (points[4].z - points[0].z);
        float cameraYOffset = points[0].z * m - points[0].y;
        camera.position.set(focusPoint.x,focusPoint.y + cameraYOffset, focusPoint.z);
        camera.update();

        if (updateFocus) {
            focusPoint = new Vector3((camera.viewportWidth / 2), (camera.viewportHeight / 2), 0);
            reCalcCamera(false);
        }
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        reCalcCamera(false);
    }

    public static float distort(float val) {
        return val + (val * ((float) Math.sqrt(2) - 1f));
    }

}
