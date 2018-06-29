package com.game.melodi.Loading;

/**
 * Created by RabitHole on 6/1/2018.
 */

public interface PlayServicesInterface {
    public void onStartMethod();
    public void signIn();
    public void signOut();
    public void rateGame();
    public void unlockAchievement(String str);
    public void submitScore(String LeaderBoard,int highScore);
    public void submitLevel(int highLevel);
    public void showAchievement();
    public void showScore(String LeaderBoard);
    public void showLevel();
    public boolean isSignedIn();

}
