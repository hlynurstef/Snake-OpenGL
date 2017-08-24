package com.hlynurs15.prog1;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.utils.BufferUtils;

public class Prog1Game extends ApplicationAdapter {
	
	public static FloatBuffer vertexBuffer;

	private FloatBuffer modelMatrix;
	private FloatBuffer projectionMatrix;

	private int renderingProgramID;
	private int vertexShaderID;
	private int fragmentShaderID;

	public static int positionLoc;

	private int modelMatrixLoc;
	private int projectionMatrixLoc;

	public static int colorLoc;
	private float deltaTime;
	
	private static final float OBSTACLE_TIMER = 2.0f;
	
	private List<Block> foods = new ArrayList<Block>();
	private Snake snake;
	private List<Obstacle> obstacles = new ArrayList<Obstacle>();
	private float addObstacleTimer = OBSTACLE_TIMER;
	
	private Random random = new Random();
	
	@Override
	public void create () {
		
		String vertexShaderString;
		String fragmentShaderString;

		vertexShaderString = Gdx.files.internal("shaders/simple2D.vert").readString();
		fragmentShaderString =  Gdx.files.internal("shaders/simple2D.frag").readString();

		vertexShaderID = Gdx.gl.glCreateShader(GL20.GL_VERTEX_SHADER);
		fragmentShaderID = Gdx.gl.glCreateShader(GL20.GL_FRAGMENT_SHADER);
	
		Gdx.gl.glShaderSource(vertexShaderID, vertexShaderString);
		Gdx.gl.glShaderSource(fragmentShaderID, fragmentShaderString);
	
		Gdx.gl.glCompileShader(vertexShaderID);
		Gdx.gl.glCompileShader(fragmentShaderID);

		renderingProgramID = Gdx.gl.glCreateProgram();
	
		Gdx.gl.glAttachShader(renderingProgramID, vertexShaderID);
		Gdx.gl.glAttachShader(renderingProgramID, fragmentShaderID);
	
		Gdx.gl.glLinkProgram(renderingProgramID);

		positionLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_position");
		Gdx.gl.glEnableVertexAttribArray(positionLoc);

		modelMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_modelMatrix");
		projectionMatrixLoc	= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_projectionMatrix");

		colorLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_color");

		Gdx.gl.glUseProgram(renderingProgramID);

		float[] pm = new float[16];

		pm[0] = 2.0f / Gdx.graphics.getWidth(); pm[4] = 0.0f; pm[8] = 0.0f; pm[12] = -1.0f;
		pm[1] = 0.0f; pm[5] = 2.0f / Gdx.graphics.getHeight(); pm[9] = 0.0f; pm[13] = -1.0f;
		pm[2] = 0.0f; pm[6] = 0.0f; pm[10] = 1.0f; pm[14] = 0.0f;
		pm[3] = 0.0f; pm[7] = 0.0f; pm[11] = 0.0f; pm[15] = 1.0f;

		projectionMatrix = BufferUtils.newFloatBuffer(16);
		projectionMatrix.put(pm);
		projectionMatrix.rewind();
		Gdx.gl.glUniformMatrix4fv(projectionMatrixLoc, 1, false, projectionMatrix);

		float[] mm = new float[16];

		mm[0] = 1.0f; mm[4] = 0.0f; mm[8] = 0.0f; mm[12] = 0.0f;
		mm[1] = 0.0f; mm[5] = 1.0f; mm[9] = 0.0f; mm[13] = 0.0f;
		mm[2] = 0.0f; mm[6] = 0.0f; mm[10] = 1.0f; mm[14] = 0.0f;
		mm[3] = 0.0f; mm[7] = 0.0f; mm[11] = 0.0f; mm[15] = 1.0f;

		modelMatrix = BufferUtils.newFloatBuffer(16);
		modelMatrix.put(mm);
		modelMatrix.rewind();

		Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, modelMatrix);

		vertexBuffer = BufferUtils.newFloatBuffer(8);
		
		Gdx.gl.glVertexAttribPointer(positionLoc, 2, GL20.GL_FLOAT, false, 0, vertexBuffer);
		
		snake = new Snake(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0.1f, Settings.WHITE);
		foods.add(new Block(getRandomX(), getRandomY(), Settings.GREEN));
		obstacles.add(new Obstacle(getRandomX(), getRandomY(), Settings.RED));
	}

	private void update() {
		deltaTime = Gdx.graphics.getDeltaTime();
		
		// Add another obstacle every OBSTACLE_TIMER interval
		addObstacleTimer -= deltaTime;
		if (addObstacleTimer < 0) {
			Sounds.OBSTACLE_ENTER.play();
			obstacles.add(new Obstacle(getRandomX(), getRandomY(), Settings.RED));
			addObstacleTimer = OBSTACLE_TIMER;
		}
		
		// Check if snake ate food
		checkIfSnakeDied();
		boolean ate = checkIfSnakeAte();
		handleInput();
		for (Obstacle obstacle : obstacles) {
			obstacle.update(deltaTime);
		}
		snake.update(deltaTime, ate);
	}
	
	private void clearScreen() {
		Gdx.gl.glClearColor(Settings.BLACK.r, Settings.BLACK.g, Settings.BLACK.b, Settings.BLACK.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	private void display() {
		for (Obstacle obstacle : obstacles) {
			obstacle.draw();
		}
		snake.draw();
		for (Block food: foods) {
			food.draw();
		}
	}
	
	private void handleInput() {
		if(Gdx.input.justTouched())
		{
			//do mouse/touch input stuff
			float mouseX = Gdx.input.getX();
			float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
			foods.add(new Block(mouseX, mouseY, Settings.GREEN));
		}
		
		//do all updates to the game
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			snake.setDirection("left");
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			snake.setDirection("right");
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			snake.setDirection("up");
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			snake.setDirection("down");
		}
	}

	@Override
	public void render () {
		update();
		clearScreen();
		display();
	}
	
	private float getRandomX() {
		float num = random.nextInt((int) Settings.WINDOW_WIDTH);
		num -= (num+Settings.BLOCK_SIZE/2) % Settings.BLOCK_SIZE;
		num = (num < 0) ? Settings.WINDOW_WIDTH/2 : num;
		num = (num > Settings.WINDOW_WIDTH) ? Settings.WINDOW_WIDTH/2 : num;
		return num;
	}
	
	private float getRandomY() {
		float num = random.nextInt((int) Settings.WINDOW_HEIGHT);
		num -= (num+Settings.BLOCK_SIZE/2) % Settings.BLOCK_SIZE;
		num = (num < 0) ? Settings.WINDOW_HEIGHT/2 : num;
		num = (num > Settings.WINDOW_HEIGHT) ? Settings.WINDOW_HEIGHT/2 : num;
		return num;
	}
	
	private boolean checkIfSnakeAte() {
		for (Block food: foods) {
			if (snake.headIntersectsBlock(food)) {
				Sounds.EAT.play();
				if (foods.size() > 1) {
					foods.remove(food);
				}
				else {
					food.setLocation(getRandomX(), getRandomY());					
				}
				if (obstacles.size() > 0) {
					obstacles.remove(0);
				}
				return true;
			}
		}
		return false;
	}
	
	private void checkIfSnakeDied() {
		float[] snakeBounds = snake.getBounds();
		
		// Check if snake hits boundaries of the game window or hits own tail
		if (snakeBounds[0] < 0 || snakeBounds[2] > Settings.WINDOW_WIDTH ||
			snakeBounds[1] < 0 || snakeBounds[3] > Settings.WINDOW_HEIGHT ||
			snake.intersectsTail()) {
			Sounds.HURT.play();
			snake.reset();
			resetFood();
			resetObstacles();
			addObstacleTimer = OBSTACLE_TIMER;
		}
		for (Obstacle obstacle : obstacles) {
			if (snake.intersectsBlock(obstacle)) {
				Sounds.HURT.play();
				snake.reset();
				resetFood();
				resetObstacles();
				addObstacleTimer = OBSTACLE_TIMER;
				return;
			}
		}
	}
	
	private void resetFood() {
		foods.clear();
		foods.add(new Block(getRandomX(), getRandomY(), Settings.GREEN));
	}
	
	private void resetObstacles() {
		obstacles.clear();
		obstacles.add(new Obstacle(getRandomX(), getRandomY(), Settings.RED));
	}
}