#version 330

//Matriz de transformação World
uniform mat4 uWorld;

//Matrizes de transformação da camera
uniform mat4 uView;         //Posicionamento
uniform mat4 uProjection;   //Abertura
uniform vec3 uCameraPos;    //Posicao da camera

//Atributos do vertice
in vec3 aPosition;
in vec3 aNormal;   //Vetor unitário que faz angulo de 90 graus com a face sendo iluminada

out vec3 vNormal;
out vec3 vViewPath;

void main() {
    //Posicao do vértice no mundo
	vec4 worldPos = uWorld * vec4(aPosition, 1.0);

	//Posição com camera e projeção
	gl_Position = uProjection * uView * worldPos;

    //A normal deve estar em coordenadas do mundo
    vNormal = (uWorld * vec4(aNormal, 0.0)).xyz;

    //Vetor de trajetória do vértice até a camera (V)
    vViewPath = uCameraPos - worldPos.xyz;
}