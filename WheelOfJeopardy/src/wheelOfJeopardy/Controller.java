package wheelOfJeopardy;

import java.util.Arrays;
import java.util.Random;

public class Controller {
	private QuestionBoard QuestionBoard;
	private Player[] Players;
	private int CurrentPlayerNumber;
	private ScoreBoard ScoreBoard;
	private TimeKeeper TimeKeeper;
	private Wheel Wheel;
	private int RoundNumber;
	private String DatabaseName;
        private Question LastQuestion;
	//need to add View as an attribute
	
	public Controller(String theFirstPlayerName, String theSecondPlayerName, String theThirdPlayerName,String theDatabaseName){
		this.Players[0] = new Player(theFirstPlayerName);
		this.Players[1] = new Player(theSecondPlayerName);
		this.Players[2] = new Player(theThirdPlayerName);
	
		this.ScoreBoard = new ScoreBoard(this.Players[0],this.Players[1],this.Players[2]);
		
		this.DatabaseName = theDatabaseName;

	}
	
	public void startGame(){
		this.RoundNumber = 1;
		
		this.createQuestionBoard();
		
		this.Wheel = new Wheel(this.QuestionBoard.getAllCategories());
		
		//Pick current player randomly
		Random randomGenerator = new Random();
		this.CurrentPlayerNumber = randomGenerator.nextInt(3);
	}
	public void stopGame(){
		
	}
	public Question getLastQuestion(){
            return this.LastQuestion;
        }
        
	public ScoreBoard getScoreBoard(){
		return this.ScoreBoard;
	}
        
        public QuestionBoard getQuestionBoard(){
            return this.QuestionBoard;
        }
	
	public WheelSector spin(){
            return this.Wheel.spin();
            
	}

        public void bankruptPlayer(Player thePlayer){
            this.ScoreBoard.bankruptPlayer(this.getCurrentPlayer());
        }
	public boolean canRoundContinue(){
		int currentRoundCount = this.ScoreBoard.getRoundCount();
		if (currentRoundCount < 50){
			return true;
		}
		return false;
	}
	
	public void loseATurn(){
		this.setCurrentPlayer();
	}
	public Question getQuestionForCategory(String theCategory){
		Question theQuestion = this.QuestionBoard.getQuestionForCategory(theCategory);
                this.LastQuestion = theQuestion;
                return theQuestion;
	}
        
        public Player getCurrentPlayer(){
		return this.Players[this.CurrentPlayerNumber];
	}
	
	private void setCurrentPlayer(){
		if (this.CurrentPlayerNumber < this.Players.length-1){
			this.CurrentPlayerNumber = this.CurrentPlayerNumber + 1;
		}
		else{
			this.CurrentPlayerNumber = 0;
		}
	}
	
	public void useTokenForCurrentPlayer(){
		this.ScoreBoard.useTokenForPlayer(this.getCurrentPlayer());
	}
	public void createQuestionBoard(){
		String theActualDatabaseNames[] = DatabaseManager.getDatabaseNames();
		if (Arrays.asList(theActualDatabaseNames).contains(this.DatabaseName)){
			this.QuestionBoard = DatabaseManager.createQuestionBoardForRound(this.DatabaseName,this.RoundNumber);
		}
	}
        
        public Player getOpponent(){
            //Pick current player randomly
            Random randomGenerator = new Random();
            int theOpponentNumber = -1;
            while(theOpponentNumber != this.CurrentPlayerNumber){
                theOpponentNumber = randomGenerator.nextInt(3);
            }
            return this.Players[theOpponentNumber];
        }
        public void addTokenForCurrentPlayer(){
            this.ScoreBoard.incrementTokensForPlayer(this.getCurrentPlayer());
        }
        
        public void addLastQuestionPointsForCurrentPlayer(){
            this.ScoreBoard.addPointsForPlayer(this.getCurrentPlayer(), this.LastQuestion.getPointValue());
        }
        public void subtractLastQuestionPointsForCurrentPlayer(){
            this.ScoreBoard.subtractPointsForPlayer(this.getCurrentPlayer(),this.LastQuestion.getPointValue());
        }
        public void overrideAnswerValidationForCurrentPlayer(boolean theOverride){
            if (theOverride){
                this.addLastQuestionPointsForCurrentPlayer();
            }
            else{
                this.subtractLastQuestionPointsForCurrentPlayer();
            }
        }

}
