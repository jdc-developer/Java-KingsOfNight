package jdc.kings;

import java.awt.Canvas;

public class KingsOfNightApplication extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private boolean running = false;

	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running) {
           long now = System.nanoTime();
           delta += (now - lastTime) / ns;
           lastTime = now;
           while(delta >=1) {
               tick();
               delta--;
           }
           if(running)
               render();
           frames++;
            
           if(System.currentTimeMillis() - timer > 1000) {
               timer += 1000;
               System.out.println("FPS: "+ frames);
               frames = 0;
           }
        }
        stop();
	}

	private void stop() {
		// TODO Auto-generated method stub
		
	}

	private void render() {
		// TODO Auto-generated method stub
		
	}

	private void tick() {
		// TODO Auto-generated method stub
		
	}

}
