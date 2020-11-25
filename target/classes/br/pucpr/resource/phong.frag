#version 330

uniform vec3 uLightDir;         //Direcao da luz

//Componentes da luz
uniform vec3 uAmbientLight;     //Cor do componente ambiente
uniform vec3 uDiffuseLight;     //Cor do componente difuso
uniform vec3 uSpecularLight;     //Cor do componente difuso

//Características do material
uniform vec3 uAmbientMaterial;
uniform vec3 uDiffuseMaterial;
uniform vec3 uSpecularMaterial;
uniform float uSpecularPower;

//Normal recebida do vertex shader. Já em coordenadas do mundo
in vec3 vNormal;
in vec3 vViewPath;

out vec4 outColor;

void main() {
    //Calculo do componente ambiente. Como ele não reflete, é aplicado diretamente a cena.
    vec3 ambient = uAmbientLight * uAmbientMaterial;

    //Calculo do componente difuso. A intensidade do brilho será equivalente ao cosseno do angulo entre a normal (N) e o
    //inverso da luz (-L). Como L e N são vetores unitários, o cosseno pode ser obtido diretamente pelo produto escalar.
    vec3 L = normalize(uLightDir);
	vec3 N = normalize(vNormal);
    float diffuseIntensity = max(dot(N, -L), 0.0);
    vec3 diffuse = diffuseIntensity * uDiffuseLight * uDiffuseMaterial;

    //Calculo do componente especular. A intensidade do é equivalente ao cosseno do angulo entre o caminho da visão (V)
    //e o reflexo da luz sobre a superfície (R). O cosseno também poderá ser calculado pelo produto escalar.
    float specularIntensity = 0.0;
    if (uSpecularPower > 0.0) {
        vec3 V = normalize(vViewPath);
        vec3 R = reflect(L, N);
        specularIntensity = pow(max(dot(R, V), 0.0), uSpecularPower);
    }
    vec3 specular = specularIntensity * uSpecularLight * uSpecularMaterial;

    //A cor final da luz é obtida somando os dois componentes. Valores maiores do que 1.0 serão truncados.
    vec3 color = clamp(ambient + diffuse + specular, 0.0, 1.0);
    outColor = vec4(color, 1.0);
}