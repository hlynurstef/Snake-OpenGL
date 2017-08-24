package com.hlynurs15.prog1;

import java.util.Random;

public class Obstacle extends Block{
	private float[] directions = {-1, 1};
	private float xVelocity;
	private float yVelocity;
	private float speed;
	private Random random = new Random();

	public Obstacle(float x, float y, Color color) {
		super(x, y, color);
		this.speed = random.nextInt((200-100) + 1) + 100;
		this.xVelocity = directions[random.nextInt(2)];
		this.yVelocity = directions[random.nextInt(2)];
	}
	
	public void update(float deltaTime) {
		float newX = getX() + speed * xVelocity * deltaTime;
		float newY = getY() + speed * yVelocity * deltaTime;
		
		checkWallCollisions(newX, newY);
		
		float dx = speed * xVelocity * deltaTime;
		float dy = speed * yVelocity * deltaTime;
		
		super.translate(dx, dy);
	}
	
	private void checkWallCollisions(float newX, float newY) {
		float[] bounds = super.getBounds();
		if (bounds[0] <= 0 || bounds[2] >= Settings.WINDOW_WIDTH) {
			xVelocity *= -1;
			Sounds.BOUNCE.play(0.5f);
		}
		if (bounds[1] <= 0 || bounds[3] >= Settings.WINDOW_HEIGHT) {
			yVelocity *= -1;
			Sounds.BOUNCE.play(0.5f);
		}
	}
	
}
