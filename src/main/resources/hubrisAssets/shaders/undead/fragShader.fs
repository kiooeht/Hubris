//SpriteBatch will use texture unit 0
uniform sampler2D u_texture;
uniform float shadeTimer;

//"in" varyings from our vertex shader
varying vec4 v_color;
varying vec2 v_texCoord;

void main() {
    //sample the texture
    vec4 texColor = texture2D(u_texture, v_texCoord);
    texColor.a = min(texColor.a, v_color.a);
    
    float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));
    vec4 grayColor = vec4(gray, gray, gray, texColor.a);

    //final color
    gl_FragColor = mix(texColor, grayColor * vec4(vec3(0.47, 0.756, 0.24) * 1.15, texColor.a), shadeTimer);
}