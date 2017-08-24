package com.hlynurs15.prog1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public final class Sounds {
	
	public static final Sound HURT = Gdx.audio.newSound(Gdx.files.internal("sounds/hurt.wav"));
	public static final Sound EAT = Gdx.audio.newSound(Gdx.files.internal("sounds/eat_food.wav"));
	public static final Sound OBSTACLE_ENTER= Gdx.audio.newSound(Gdx.files.internal("sounds/obstacle_enter.wav"));
	public static final Sound BOUNCE = Gdx.audio.newSound(Gdx.files.internal("sounds/bounce.wav"));
	
	/**
	 * Private constructor to prevent anyone from creating an instance of this class.
	 * This is done because the Settings class is to be used as a static class.
	 */
	private Sounds() {}
}
