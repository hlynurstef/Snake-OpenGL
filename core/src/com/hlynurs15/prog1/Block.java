package com.hlynurs15.prog1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Block {
	private float x;
	private float y;
	private Color color;
	
	public Block(float x, float y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void translate(float dx, float dy) {
		this.x += dx;
		this.y += dy;
	}
	
	public void draw() {
		setVertexBufferToLocation();
		
		//COLOR IS SET HERE
		Gdx.gl.glUniform4f(Prog1Game.colorLoc, color.r, color.g, color.b, color.a);
		Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_STRIP, 0, 4);
	}
	
	/**
	 * Calculates and returns the boundaries of this Block.
	 * @return Boundaries of this block as [minx, miny, maxx, maxy].
	 */
	public float[] getBounds() {
		float[] boundaries = new float[4];
		boundaries[0] = x-Settings.BLOCK_SIZE/2;
		boundaries[1] = y-Settings.BLOCK_SIZE/2;
		boundaries[2] = x+Settings.BLOCK_SIZE/2;
		boundaries[3] = y+Settings.BLOCK_SIZE/2;
		return boundaries;
	}
	
	public boolean intersects(Block other) {
		float[] bounds = getBounds();
		float[] otherBounds = other.getBounds();
		
		if (bounds[2] > otherBounds[0] && bounds[0] < otherBounds[2] &&
			bounds[3] > otherBounds[1] && bounds[1] < otherBounds[3]) {
			return true;
		}
		return false;
	}
	
	public float getX() { return this.x; }
	public float getY() { return this.y; }
	
	private float[] createVertices() {
		float[] vertices = {
			x-Settings.BLOCK_SIZE/2, y-Settings.BLOCK_SIZE/2,	// Top left
			x-Settings.BLOCK_SIZE/2, y+Settings.BLOCK_SIZE/2,	// Bottom Left
			x+Settings.BLOCK_SIZE/2, y-Settings.BLOCK_SIZE/2,	// Top right
			x+Settings.BLOCK_SIZE/2, y+Settings.BLOCK_SIZE/2	// Bottom right
		};
		return vertices;
	}
	
	private void setVertexBufferToLocation() {
		Prog1Game.vertexBuffer.clear();
		Prog1Game.vertexBuffer.put(createVertices());
		Prog1Game.vertexBuffer.rewind();
		Gdx.gl.glVertexAttribPointer(Prog1Game.positionLoc, 2, GL20.GL_FLOAT, false, 0, Prog1Game.vertexBuffer);
	}
}
