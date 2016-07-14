package com.jaspreetdhanjan.gol;

import java.awt.*;
import java.awt.image.*;

import javax.swing.JFrame;

public class Application extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	private static final String TITLE = "Conway's Game of Life";
	private static final int SCALE = 4;
	private static final int WIDTH = 200;
	private static final int HEIGHT = 120;
	private static final int SCALED_WIDTH = WIDTH * SCALE;
	private static final int SCALED_HEIGHT = HEIGHT * SCALE;

	private boolean stop = false;
	private int sleepTimeMillis = 500;

	private BufferedImage drawImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) drawImg.getRaster().getDataBuffer()).getData();
	private Game game = new Game(WIDTH, HEIGHT);

	public void run() {
		init();

		long lastTime = System.currentTimeMillis();
		while (!stop) {
			tick();
			render();

			long nowTime = System.currentTimeMillis();
			long diff = nowTime - lastTime;
			lastTime = nowTime;
			long delay = diff - sleepTimeMillis;
			System.out.println("Running at: " + sleepTimeMillis + "ms per tick, with " + delay + "ms delay");

			try {
				Thread.sleep(sleepTimeMillis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void tick() {
		game.tick();
	}

	private void init() {
		Dimension d = new Dimension(SCALED_WIDTH, SCALED_HEIGHT + 22);

		JFrame frame = new JFrame(TITLE);
		frame.setMinimumSize(d);
		frame.setMaximumSize(d);
		frame.setPreferredSize(d);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}

		game.draw(pixels, 0xffffff);

		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, SCALED_WIDTH, SCALED_HEIGHT);

		int x = 8;
		int y = 8;
		int w = SCALED_WIDTH - 16;
		int h = SCALED_HEIGHT - 16;

		g.drawImage(drawImg, x, y, w, h, null);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		new Thread(new Application(), "Game").start();
	}
}