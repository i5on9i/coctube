package com.cocube;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 14
 * Time: 오전 12:53
 * To change this template use File | Settings | File Templates.
 */
public class YouTubeState {



    public static final int YOUTUBE_STATE_SERVICE_AVAILABLE = 1;
    public static final int YOUTUBE_STATE_CANRESOLVE = 2;
    public static final int YOUTUBE_STATE_NOT_INSTALLED = 3;


    private static int current = YOUTUBE_STATE_NOT_INSTALLED;


    private volatile static YouTubeState instance;
    private Object stateLock = new Object();

    /**
     * Returns singleton class instance
     */
    public static YouTubeState getInstance() {
        if (instance == null) {
            synchronized (YouTubeState.class) {
                if (instance == null) {
                    instance = new YouTubeState();
                }
            }
        }
        return instance;
    }

    protected YouTubeState() {
    }

    public synchronized void init() {

    }

    public int getCurrentState(){
        synchronized (stateLock){
            return current;
        }
    }

    public void setCurrentState(int state){
        synchronized (stateLock){
            current = state;
        }
    }

}
