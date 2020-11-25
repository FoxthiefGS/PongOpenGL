package br.pucpr.cg;

import br.pucpr.game.Ball;
import br.pucpr.game.GameManager;
import br.pucpr.mage.*;
import br.pucpr.mage.camera.CameraFPS;
import br.pucpr.mage.phong.DirectionalLight;
import br.pucpr.mage.phong.Material;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static br.pucpr.mage.MathUtil.mul;
import static org.joml.Math.toRadians;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class TerrainScene implements Scene {
    private Keyboard keys = Keyboard.getInstance();

    private static final String PATH = "/Users/Meow/Pictures";

    private final float WALK_SPEED = 20f;
    private final float TURN_SPEED = toRadians(120f);

    private Shader shader;
    private Mesh meshFirstPlayer, meshSecondPlayer, meshBall;
    private CameraFPS camera = new CameraFPS();
    private DirectionalLight light;
    private Material materialFirstPlayer, materialSecondPlayer, materialBall;
    private Ball ball;
    private GameManager gameManager;

    float playerMaxX = 0.3f, playerMaxY = 1f, ballXY = 0.1f, speed = 3.5f;
    Vector3f ballPos = new Vector3f(0f, 0f, 0f), firstPlayerPos = new Vector3f(-3f, 0f, 0f), secondPlayerPos = new Vector3f(3f, 0f, 0f);

    @Override
    public void init() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        shader = Shader.loadProgram("phong");
        meshFirstPlayer = MeshFactory.createRect(shader, new Vector3f(0f, 0f, 0f), playerMaxX, playerMaxY);
        meshSecondPlayer = MeshFactory.createRect(shader, new Vector3f(0f, 0f, 0f), playerMaxX, playerMaxY);

        ball = new Ball(ballXY);
        ball.SetForceDirection();
        meshBall = MeshFactory.createRect(shader, ball.getPos(), ballXY, ballXY);

        camera.getPosition().set(0f, 0f, 5f);
        camera.setAngleY(toRadians(0f));

        light = new DirectionalLight()
                .setDirection(0f, 0f, 1f)
                .setAmbient(1f)
                .setColor(1.0f, 0.0f, 1.0f);

        materialFirstPlayer = new Material()
                .setColor(0f, 0f, 1f);

        materialSecondPlayer = new Material()
                .setColor(0f, 0f, 1f);

        materialBall = new Material()
                .setColor(1f);

        gameManager = new GameManager(ball);
    }

    @Override
    public void update(float secs) {
        if (keys.isPressed(GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(glfwGetCurrentContext(), true);
            return;
        }

        if (keys.isDown(GLFW_KEY_B)) {
            gameManager.ResetGame();
            materialFirstPlayer = new Material().setColor(0f, 0f, 1f);
            materialSecondPlayer = new Material().setColor(0f, 0f, 1f);
            firstPlayerPos = new Vector3f(-3f, 0f, 0f);
            secondPlayerPos = new Vector3f(3f, 0f, 0f);
            ball.resetPos();
        }

        if(keys.isDown(GLFW_KEY_ENTER)){
            if(!gameManager.getGameState() && !gameManager.getEndGame())
                gameManager.setGameState(true);
        }

        if(gameManager.getGameState()) {
            if (keys.isDown(GLFW_KEY_UP) && secondPlayerPos.y <= 2f) {
                secondPlayerPos.add(new Vector3f(0f, secs * speed, 0f));
            } else if (keys.isDown(GLFW_KEY_DOWN) && secondPlayerPos.y >= -2f) {
                secondPlayerPos.add(new Vector3f(0f, -secs * speed, 0f));
            }

            if (keys.isDown(GLFW_KEY_W) && firstPlayerPos.y <= 2f) {
                firstPlayerPos.add(new Vector3f(0f, secs * speed, 0f));
            } else if (keys.isDown(GLFW_KEY_S) && firstPlayerPos.y >= -2f) {
                firstPlayerPos.add(new Vector3f(0f, -secs * speed, 0f));
            }

            ball.addPos(secs);
            ballPos = ball.getPos();
            ball.checkPlayerCollision(firstPlayerPos, playerMaxX, playerMaxY);
            ball.checkPlayerCollision(secondPlayerPos, playerMaxX, playerMaxY);

            gameManager.CheckGame();
            if(gameManager.CheckWinner() == 1){
                materialFirstPlayer = new Material().setColor(0f, 1f, 0f);
                materialSecondPlayer = new Material().setColor(1f, 0f, 0f);
                gameManager.setEndGame();
            }
            else if(gameManager.CheckWinner() == 2){
                materialFirstPlayer = new Material().setColor(1f, 0f, 0f);
                materialSecondPlayer = new Material().setColor(0f, 1f, 0f);
                gameManager.setEndGame();
            }
        }

    }

    @Override
    public void draw() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        shader.bind()
            .set(camera)
            .set(light)
        .unbind();

        meshFirstPlayer
            .setUniform("uWorld", new Matrix4f().translate(firstPlayerPos))
            .setItem("material", materialFirstPlayer)
        .draw(shader);

        meshSecondPlayer
             .setUniform("uWorld", new Matrix4f().translate(secondPlayerPos))
             .setItem("material", materialSecondPlayer)
        .draw(shader);

        meshBall
                .setUniform("uWorld", new Matrix4f().translate(new Vector3f(ballPos)))
                .setItem("material", materialBall)
                .draw(shader);
    }

    @Override
    public void deinit() {
    }

    public static void main(String[] args) {
        new Window(new TerrainScene(), "Pong", 1024, 768).show();
    }
}