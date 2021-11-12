package com.main.blackjack;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private GameView2 gameView;
    private boolean running;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GameView2 gameView){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    public void setRunning(boolean isRunning){
        this.running = isRunning;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running){
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                        long now = System.nanoTime();
                        delta += (now - lastTime) / ns;
                        lastTime = now;
                        while (delta >= 1) {
                            this.gameView.update();
                            delta--;
                        }
                        if (running == true) {
                            this.gameView.draw(canvas);
                            frames++;
                        }
                        if (System.currentTimeMillis() - timer > 1000) {
                            timer += 1000;
                            //Log.i("FPS Counter", "FPS: " + frames);
                            frames = 0;
                        }
                }
                } catch (Exception e){
                    e.printStackTrace();
                }finally {
                if (canvas!=null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
