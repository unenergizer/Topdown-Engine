varying vec2 v_texCoords;
varying vec3 v_normal;
uniform sampler2D u_texture;
varying vec3 v_fragPos;
varying vec3 v_lightPos;

varying vec3 v_testBS;

void main() {
    vec4 tgt = texture2D(u_texture, v_texCoords);

    vec4 ambiantLight = vec4(0.2, 0.2, 0.2, 1.0);
    vec4 lightColor = vec4(0.5, 0.5, 0.5, 1.0);

    vec3 norm = normalize(v_normal);
    vec3 lightDir = normalize(v_lightPos - v_fragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec4 diffuse = diff * lightColor;

    vec4 result = (ambiantLight + diffuse) * tgt;

    //Delete here to
    if (v_testBS.x == 1)
        result = lightColor;
    //Here if you are trying to remove the testing code
    //You should also remove the v_testBS

    gl_FragColor = result;
}
