attribute vec4 a_position;
attribute vec2 a_texCoord0;
attribute vec3 a_normal;
uniform mat4 u_projTrans;
uniform vec3 u_modelPos;

varying vec2 v_texCoords;
varying vec3 v_normal;

void main() {
    v_texCoords = a_texCoord0;
    v_normal = a_normal;

    gl_Position = u_projTrans * (a_position + vec4(u_modelPos, 0.0));
}