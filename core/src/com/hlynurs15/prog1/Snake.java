package com.hlynurs15.prog1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A snake representing a Snake controlled by the player.
 * @author Hlynur Stefánsson
 */
public class Snake {
	/**
	 * A HashMap holding the directions for the snake.
	 * Key: String ("up", "down", "left" or "right")
	 * Value: Tuple<Integer, Integer>
	 */
	private static final Map<String, Tuple<Integer, Integer>> velocities = createDirections();
	/**
	 * The current velocity of the snake, starts at (0,0) so the snake
	 * does not start moving until the user gives an input.
	 */
	private Tuple<Integer, Integer> velocity = new Tuple<Integer, Integer>(0,0);
	
	private float startX;
	private float startY;
	
	/**
	 * The head of the snake.
	 */
	private Block head;
	/**
	 * The tail of the snake.
	 */
	private List<Block> tail = new ArrayList<Block>();
	
	private float speed;
	
	private float currentSpeed;
	
	private float moveTimer;
	
	private boolean ate = false;
	
	private Color colour;
	
	/**
	 * Constructor. Sets the starting location of the head of the snake.
	 * @param x The x window coordinate.
	 * @param y The y window coordinate.
	 */
	public Snake(float x, float y, float speed, Color colour) {
		this.startX = x;
		this.startY = y;
		this.speed = speed;
		this.currentSpeed = speed;
		this.moveTimer = speed;
		this.colour = colour;
		this.head = new Block(x+Settings.BLOCK_SIZE/2, y+Settings.BLOCK_SIZE/2, colour);
	}
	
	public void reset() {
		this.currentSpeed = this.speed;
		this.moveTimer = speed;
		this.velocity.setX(0);
		this.velocity.setY(0);
		this.tail.clear();
		this.ate = false;
		this.head.setLocation(startX+Settings.BLOCK_SIZE/2, startY+Settings.BLOCK_SIZE/2);
	}
	
	public float[] getBounds() {
		return this.head.getBounds();
	}
	
	public void update(float deltaTime, boolean ate) {
		moveTimer -= deltaTime;
		if (ate) {
			this.ate = ate;
		}
		
		if (moveTimer <= 0) {
			moveTimer = currentSpeed;
			float dx = this.velocity.getX() * Settings.BLOCK_SIZE;
			float dy = this.velocity.getY() * Settings.BLOCK_SIZE;
			
			float oldX = this.head.getX();
			float oldY = this.head.getY();
			
			this.head.translate(dx, dy);
			
			if (this.ate) {
				tail.add(new Block(oldX, oldY, colour));
				this.ate = false;
				if (currentSpeed > 0.02) {
					currentSpeed -= 0.005;
				}
			}
			else {
				if (!tail.isEmpty()) {
					Block first = tail.remove(0);
					first.setLocation(oldX, oldY);
					tail.add(first);
				}
			}
		}
	}
	
	public float getX() { return this.head.getX(); }
	public float getY() { return this.head.getY(); }
	
	public void draw() {
		this.head.draw();
		
		for (Block block : tail) {
			block.draw();
		}
	}
	
	/**
	 * Sets the direction of the snake to up, down, left or right.
	 * @param direction A string: "up", "down", "left" or "right".
	 */
	public void setDirection(String direction) {
		Tuple<Integer, Integer> tempVelocity = new Tuple<Integer, Integer>(velocities.get(direction));
		if (coastIsClear(tempVelocity)) {
			this.velocity = tempVelocity;
		}
	}
	
	public boolean intersectsTail() {
		if (!tail.isEmpty()) {
			for (Block block : tail) {
				if (head.intersects(block)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean headIntersectsBlock(Block block) {
		return head.intersects(block);
	}
	
	public boolean intersectsBlock(Block block) {
		if (head.intersects(block)) {
			return true;
		}
		for (Block b: tail) {
			if (b.intersects(block)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean coastIsClear(Tuple<Integer, Integer> direction) {
		if (!tail.isEmpty()) {
			float newX = head.getX() + Settings.BLOCK_SIZE * direction.getX();
			float newY = head.getY() + Settings.BLOCK_SIZE * direction.getY();
			
			Block lastBlock = tail.get(tail.size() - 1);
			
			if (lastBlock.getX() == newX && lastBlock.getY() == newY) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Creates the directions that the snake can move in.
	 * @return Map with direction as key and Tuple of direction as values.
	 */
	private static Map<String, Tuple<Integer, Integer>> createDirections() {
		Map<String, Tuple<Integer, Integer>> dir = new HashMap<String, Tuple<Integer, Integer>>();
		dir.put("up",    new Tuple<Integer, Integer>( 0, 1));
		dir.put("down",  new Tuple<Integer, Integer>( 0,-1));
		dir.put("left",  new Tuple<Integer, Integer>(-1, 0));
		dir.put("right", new Tuple<Integer, Integer>( 1, 0));
		return dir;
	}	
}
