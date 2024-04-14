attribute vec4 a_position;
attribute vec2 a_texCoord0;
attribute vec3 a_normal;
uniform mat4 u_projTrans;
uniform vec3 u_modelPos;
uniform vec3 u_lightPos;

varying vec2 v_texCoords;
varying vec3 v_normal;
varying vec3 v_fragPos;
varying vec3 v_lightPos;

float distort(float val) {
    float sqrtOfTwo = 1.4142135624;
    return val + (val * (sqrtOfTwo - 1.0));
}

void main() {
    v_texCoords = a_texCoord0;
    v_normal = a_normal;

    vec4 pos = a_position + vec4(u_modelPos, 0.0);
    pos *= vec4(16.0, distort(16.0), distort(16.0), 1);

    v_fragPos = vec3(a_position + vec4(u_modelPos, 0.0));
    v_lightPos = u_lightPos / 16.0;

    gl_Position = u_projTrans * (pos);
}
