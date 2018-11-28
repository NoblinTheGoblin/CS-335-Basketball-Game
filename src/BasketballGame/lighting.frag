uniform sampler2D texture;
varying vec4 diffuse;

void main(void) {
	vec4 texColor = texture2D(texture, gl_TexCoord[0].st);
	gl_FragColor = (gl_LightSource[0].ambient + diffuse) * texColor;
}