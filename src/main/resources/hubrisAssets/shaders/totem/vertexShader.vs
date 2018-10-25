uniform mat4 u_projTrans;

attribute vec4 a_position;
attribute vec2 a_texCoord0;
attribute vec4 a_color;

varying vec4 v_color;
varying vec2 v_texCoord;

uniform vec2 u_viewportInverse;

uniform float timer;

void main() {
    gl_Position = u_projTrans * a_position;
    //gl_Position.x += cos(timer) / 160;
    //gl_Position.y += sin(timer) / 80;
    v_texCoord = a_texCoord0;
    v_color = a_color;
}