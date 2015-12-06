attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord;

uniform mat4 u_worldTrans;
uniform mat4 u_projViewTrans;

uniform float u_time;
uniform vec4 u_ambientColor;

varying vec2 v_texCoord;

varying vec3 v_pos;

void main() {
    v_texCoord = a_texCoord;
    v_pos = u_projViewTrans * u_worldTrans * vec4(a_position, 1.0);

    if(a_texCoord.y < 1) {
        a_position.x += cos(u_time+a_position.x)/4.0+sin(u_time+a_position.x)/4.0;
        a_position.z += cos(u_time+a_position.z)/4.0+sin(u_time+a_position.z)/4.0;
    }

    gl_Position = u_projViewTrans * u_worldTrans * vec4(a_position, 1.0);
}