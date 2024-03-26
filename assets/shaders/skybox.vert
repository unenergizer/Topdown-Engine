attribute vec4 a_position;
attribute vec2 a_texCoord0;
attribute vec3 a_normal;
attribute int a_data;
uniform mat4 u_projTrans;
uniform vec3 u_modelPos;
uniform vec3 u_lightPos;

varying vec2 v_texCoords;
varying vec3 v_normal;
varying vec3 v_fragPos;
varying vec3 v_lightPos;

varying vec3 v_testBS;

float distort(float val) {
    float sqrtOfTwo = 1.4142135624;
    return val + (val * (sqrtOfTwo - 1.0));
}

void main() {
    v_texCoords = a_texCoord0;
    v_normal = a_normal;

    //Delete here to
    int posX = (a_data >> 10) & 31;
    int posY = (a_data >> 5) & 31;
    int posZ = a_data & 31;
    vec4 temp = vec4(posX, posY, posZ, 0);

    if (temp.x == a_position.x && temp.y == a_position.y && temp.z == a_position.z) {
        v_testBS.x = 1;
    } else {
        v_testBS.x = 0;
    }
    //Here if you are trying to remove the testing code
    //Delete v_testBS and a_data also

    vec4 pos = a_position + vec4(u_modelPos, 0.0);
    pos *= vec4(16.0, distort(16.0), distort(16.0), 1);

    v_fragPos = vec3(a_position + vec4(u_modelPos, 0.0));
    v_lightPos = u_lightPos / 16.0;

    gl_Position = u_projTrans * (pos);
}
