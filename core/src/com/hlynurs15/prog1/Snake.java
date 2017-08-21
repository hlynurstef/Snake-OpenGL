package com.hlynurs15.prog1;

import java.util.HashMap;
import java.util.Map;

public class Snake {
	// This map holds the directions for the snake.
	private static final Map<Tuple<Integer, Integer>, String> directions = createDirections();
	
	private float x;	// Current x location of the head of the snake.
	private float y;	// Current y location of the head of the snake.
	
	private Tuple<Integer, Integer> direction = new Tuple<Integer, Integer>(0,0);		// Holds the current direction Tuple for the snake.
	
	/* CONSTRUCTOR */
	
	public Snake(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/* PUBLIC FUNCTIONS */
	
	public void setDirection(String direction) {
		direction = directions.get(direction);
	}
	
	/* PRIVATE FUNCTIONS */
	
	private static Map<Tuple<Integer, Integer>, String> createDirections() {
		Map<Tuple<Integer, Integer>, String> dir = new HashMap<Tuple<Integer, Integer>, String>();
		dir.put(new Tuple<Integer, Integer>( 0, 1), "up");
		dir.put(new Tuple<Integer, Integer>( 0,-1), "down");
		dir.put(new Tuple<Integer, Integer>(-1, 0), "left");
		dir.put(new Tuple<Integer, Integer>( 1, 0), "right");
		return dir;
	}
}
