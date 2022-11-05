/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2016 Marco Hutter - http://www.javagl.de
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.jgltf.model.animation;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Simple utility class to run an {@link AnimationManager} in an own thread
 */
public final class AnimationRunner
{
    public static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5, Thread::new);
    static {
        for(int i =0; i<5; i++) executorService.execute(()->{});
    }
    /**
     * The {@link AnimationManager}
     */
    private final AnimationManager animationManager;
    
    /**
     * Whether this runner is currently running
     */
    private volatile boolean running = false;

    private volatile boolean blocking = false;

    private long previousNs = System.nanoTime();
    /**
     * The step size, in milliseconds
     */
    private final long stepSizeMs = 6;
    
    /**
     * Create a new runner for the given {@link AnimationManager}
     * 
     * @param animationManager The {@link AnimationManager}
     */
    public AnimationRunner(AnimationManager animationManager)
    {
        Objects.requireNonNull(animationManager, 
            "The animationManager may not be null");
        this.animationManager = animationManager;
    }

    public AnimationRunner(){
        animationManager = null;
    }
    
    /**
     * Start this runner. If the runner is already {@link #isRunning()},
     * then this has no effect.
     */
    public synchronized void start()
    {
        if (isRunning())
        {
            return;
        }
        executorService.execute(new AnimationRunnable(null));
    }

    public synchronized void start(Runnable callback)
    {
        if (isRunning())
        {
            return;
        }
        executorService.execute(new AnimationRunnable(callback));
    }
    
    /**
     * Stop this runner. If the runner is not {@link #isRunning()},
     * then this has no effect.
     */
    public synchronized void stop()
    {
        running = false;
    }

    /**
     * Returns whether this runner is currently running
     * 
     * @return Whether this runner is currently running 
     */
    public boolean isRunning()
    {
        return running;
    }
    
    /**
     * Will be called in an own thread to perform time steps in the
     * {@link AnimationManager}
     */
    private void runAnimations()
    {
        while (blocking){ }
        running = true;
        previousNs = System.nanoTime();
        while (isRunning())
        {
            blocking = true;
            long currentNs = System.nanoTime();
            long deltaNs = currentNs - previousNs;
            animationManager.performStep(deltaNs);
            previousNs = currentNs;
            if(animationManager.tipStop()) {
                stop();
                animationManager.reset();
                break;
            }
            try
            {
                Thread.sleep(stepSizeMs);
            } 
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                blocking = false;
                return;
            }
        }
        blocking = false;
    }

    public AnimationManager getAnimationManager() {
        return animationManager;
    }

    public class AnimationRunnable implements Runnable{
        private final Runnable callback;

        public AnimationRunnable(Runnable callback){
            this.callback = callback;
        }

        @Override
        public void run() {
            runAnimations();
            if(callback != null) callback.run();
        }
    }
}
