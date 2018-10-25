uniform mat4 u_projTrans;

attribute vec4 a_position;
attribute vec2 a_texCoord0;
attribute vec4 a_color;

varying vec4 v_color;
varying vec2 v_texCoord;

uniform vec2 u_viewportInverse;

uniform float shadeTimer;

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
    gl_Position = u_projTrans * a_position;
    if (shadeTimer < 1) {
        gl_Position.y += rand(gl_Position.xy) / 50;
    }
    v_texCoord = a_texCoord0;
    v_color = a_color;
}