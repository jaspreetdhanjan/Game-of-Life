package com.jaspreetdhanjan.gol;

import java.util.Random;

public class Game {
	private final int w, h;

	private boolean[][] populated, copy;
	private boolean generated = false;
	private Random random = new Random();
	private int reloadTime = 5;

	public Game(int w, int h) {
		this.w = w;
		this.h = h;

		populated = new boolean[w][h];
		copy = new boolean[w][h];
		if (!generated) {
			generateMap();
			return;
		}
	}

	private void generateMap() {
		generated = true;
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (random.nextInt(10) == 0) {
					setPopulated(x, y, true);
				}
			}
		}
	}

	private void setPopulated(int x, int y, boolean b) {
		if (x < 0 || y < 0 || x >= w || y >= h) return;
		populated[x][y] = b;
	}

	private boolean getPopulated(int x, int y, boolean fromCopy) {
		if (x < 0 || y < 0 || x >= w || y >= h) return false;
		if (fromCopy) return copy[x][y];
		return populated[x][y];
	}

	public void tick() {
		if (reloadTime > 0) {
			reloadTime--;
			return;
		}

		createCopy();

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				boolean self = getPopulated(x, y, true);

				boolean n = getPopulated(x, y + 1, true);
				boolean ne = getPopulated(x + 1, y + 1, true);
				boolean e = getPopulated(x + 1, y, true);
				boolean se = getPopulated(x + 1, y - 1, true);
				boolean s = getPopulated(x, y - 1, true);
				boolean sw = getPopulated(x - 1, y - 1, true);
				boolean w = getPopulated(x - 1, y, true);
				boolean nw = getPopulated(x - 1, y + 1, true);

				int pp = 0;

				if (n) pp++;
				if (ne) pp++;
				if (e) pp++;
				if (se) pp++;
				if (s) pp++;
				if (sw) pp++;
				if (w) pp++;
				if (nw) pp++;

				boolean shallSurvive = false;
				if (self && (pp == 3 || pp == 2)) {
					shallSurvive = true;
				}
				if (!self && pp == 3) {
					shallSurvive = true;
				}

				setPopulated(x, y, shallSurvive);
			}
		}
	}

	private void createCopy() {
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				copy[x][y] = populated[x][y];
			}
		}
	}

	public void draw(int[] pixels, int drawColour) {
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				pixels[x + y * w] = getPopulated(x, y, false) ? drawColour : 0;
			}
		}
	}
}