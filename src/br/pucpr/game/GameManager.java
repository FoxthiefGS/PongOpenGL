package br.pucpr.game;

public class GameManager {

    Ball ball;
    boolean isPlaying = false, endGame = false;
    int firstPlayerScore, secondPlayerScore;

    public GameManager(Ball ball){
        this.ball = ball;
    }

    public void setGameState(boolean state){
        isPlaying = state;
    }

    public boolean getGameState(){
        return isPlaying;
    }

    public void setEndGame(){
        endGame = true;
    }

    public boolean getEndGame(){
        return endGame;
    }

    public int CheckWinner(){
        if(firstPlayerScore >= 3)
            return 1;
        else if(secondPlayerScore >= 3)
            return 2;
        else
            return 0;
    }

    public void CheckGame(){
        if(ball.getPos().x >= 4f){
            firstPlayerScore++;
            ball.resetPos();
            setGameState(false);
        }
        else if(ball.getPos().x <= -4f){
            secondPlayerScore++;
            ball.resetPos();
            setGameState(false);
        }
    }

    public void ResetGame(){
        setGameState(false);
        endGame = false;
        firstPlayerScore = 0;
        secondPlayerScore = 0;
    }

}
