package com.jlabarca.kflame;

public interface ActionResolver {
	public boolean isOnline();
	public boolean getSignedInGPGS();
	public void loginGPGS();
	public void submitScoreGPGS(float score);
	public void unlockAchievementGPGS(String achievementId);
	public void getLeaderboardGPGS();
	public void getAchievementsGPGS();
	public void logOut();
	public float getMyRank();
	public float getMyHighScore();
	public void getLeaderboardIntent();
	public void increaseScore(int amount);
	public void submitLevelGPGS(float score);
	
	
}