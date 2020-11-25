package br.pucpr.mage.phong;

import br.pucpr.mage.ShaderItem;
import org.joml.Vector3f;

import br.pucpr.mage.Shader;
import org.lwjgl.system.CallbackI;

import static br.pucpr.mage.MathUtil.asString;

public class Material implements ShaderItem {
    private Vector3f ambient;
    private Vector3f diffuse;
    private Vector3f specular;
    private float power;
    
    public Material(Vector3f ambient, Vector3f diffuse, Vector3f specular, float power) {
        super();
        this.ambient = new Vector3f(ambient);
        this.diffuse = new Vector3f(diffuse);
        this.specular = new Vector3f(specular);
        this.power = power;
    }

    public Material(Vector3f ambient, Vector3f diffuse) {
        this(ambient, diffuse, new Vector3f(1.0f, 1.0f, 1.0f), 0.0f);
    }

    public Material(Vector3f color) {
        this(color, color);
    }
    
    public Material() {
        this(new Vector3f(1.0f, 1.0f, 1.0f));
    }
    
    public Vector3f getAmbient() {
        return ambient;
    }
    public Vector3f getDiffuse() {
        return diffuse;
    }
    public Vector3f getSpecular() {
        return specular;
    }
    public float getPower() {
        return power;
    }

    public Material setColor(float r, float g, float b) {
        return setAmbient(r, g, b).setDiffuse(r, g, b);
    }

    public Material setColor(float i) {
        return setColor(i, i, i);
    }

    public Material setAmbient(float r, float g, float b) {
        getAmbient().set(r, g, b);
        return this;
    }

    public Material setAmbient(float i) {
        return setAmbient(i, i, i);
    }

    public Material setDiffuse(float r, float g, float b) {
        getDiffuse().set(r, g, b);
        return this;
    }

    public Material setDiffuse(float i) {
        return setDiffuse(i, i, i);
    }

    public Material setSpecular(float r, float g, float b) {
        getSpecular().set(r, g, b);
        return this;
    }

    public Material setSpecular(float i) {
        return setSpecular(i, i, i);
    }

    public Material setPower(float power) {
        this.power = power;
        return this;
    }    

    @Override
    public void apply(Shader shader) {
        shader.setUniform("uAmbientMaterial", ambient)
            .setUniform("uDiffuseMaterial", diffuse)
            .setUniform("uSpecularMaterial", specular)
            .setUniform("uSpecularPower", power);
    }

    @Override
    public String toString() {
        return "Material{" +
                "ambient=" + asString(ambient) +
                ", diffuse=" + asString(diffuse) +
                ", specular=" + asString(specular) +
                ", power=" + String.format("%.2f", power) +
                '}';
    }
}
