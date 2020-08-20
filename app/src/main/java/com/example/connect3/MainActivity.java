package com.example.connect3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    boolean gameOn = true;// stores whether the game is still on or not
    boolean againstComputer = true;// stores whether the game is against computer or user
    boolean firstTurnComputer=false;// stores whether first turn would be of computer or not
    boolean difficultyImp=true;
    //0 = red , 1 = yellow , -1 =empty
    int forComp=1,forUser=0;
    int playerTurn = 0, turnCount = 0;
    int countPossibleMoves=0;
    int[] scoreForPosition=new int[9];
    int[] countPositionPossibilities=new int[9];
    int[] countPositionWins=new int[9];
    int[] countPositionDefeats=new int[9];
    int possiblePosition=-1;
    int[][] winSequence = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {6, 4, 2}
    };
    int[] items = {-1, -1, -1, -1, -1, -1, -1, -1, -1};// stores current state of board

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=getIntent();
        if(intent!=null){
            againstComputer=intent.getBooleanExtra(StartingClass.MSG_AGAINST,true);
            firstTurnComputer=intent.getBooleanExtra(StartingClass.MSG_FIRSTTURN,false);
            difficultyImp=intent.getBooleanExtra(StartingClass.MSG_DIFFICULTY,true);

            System.out.println("MA against comp : "+againstComputer);
            System.out.println("MA firstTurnComp : "+firstTurnComputer);
            System.out.println("MA diffcultyImp : "+difficultyImp);
         }
        if(againstComputer && firstTurnComputer){
            playerTurn=1;
            placeItem();
        }
    }

    public void itemClicked(View view) {

        ImageView item = (ImageView) view;
        // figures out which button is pressed
        int itemIndex = Integer.valueOf(item.getTag().toString()) - 1;
        //checks i move is valid and game is still on
        if (items[itemIndex] == -1 && turnCount < 9 && gameOn) {
            turnCount++;// increases number of valid moves played
            if (playerTurn == 0) {// if current turn is of player 0
                item.setImageResource(R.drawable.red);//visually show player's move
                playerTurn = 1;//sets turn to be of player 1
                items[itemIndex] = 0;// stores player's move
            } else {// if current turn is of player 1
                item.setImageResource(R.drawable.yellow);//visually show player's move
                playerTurn = 0;//sets turn to be of player 0
                items[itemIndex] = 1;// stores player's move
            }
            item.setTranslationY(-1500);
            item.animate().translationYBy(1500).setDuration(300);
            checkIfPlayerWon();// check if any player won the match
            if (turnCount >= 9 && gameOn) {
                // match is a tie
                endGame(-1);// end game as a tie
            }
            else if(againstComputer)placeItem();// if game is against computer, make computer's move
        }
    }


    private void checkIfPlayerWon() {
        //checks if anyone won the match and ends game accordingly

        //iterate through possible win positions
        for (int[] sequence : winSequence) {
            //checks if any player has won the match
            if (items[sequence[0]] == items[sequence[1]] && items[sequence[2]] == items[sequence[1]]
                    && items[sequence[0]] != -1) {
                if (items[sequence[0]] == 1) {
                    endGame(1);// end game as player 1 won
                } else {
                    endGame(0);// end game as player 0 won
                }
            }
        }
    }

    private int checkForWin(int[] p_items) {
        /*
        for the given state of board p_items return if 0,1 if player 0,1 won else return -1
         */
        for (int[] sequence : winSequence) {
            if (p_items[sequence[0]] == p_items[sequence[1]] && p_items[sequence[2]] ==
                    p_items[sequence[1]] && p_items[sequence[0]] != -1) {
                return p_items[sequence[0]];
            }
                                        }
        return -1;
    }
    private void endGame(int whoWon) {
        //checks if game is still on
        if(gameOn) {
            gameOn = false;// denotes that game has ended
            TextView winnerTextView = (TextView) findViewById(R.id.WinnerTextView);
            Button restartButton = (Button) findViewById(R.id.RestartButton);
            Button homeButton = (Button) findViewById(R.id.HomeButton);
            String winStatusText ;// stores the message to be displayed
            // computes winStatusText based on the result and settings of game
            switch (whoWon) {
                case 0:
                    if(againstComputer)winStatusText = "You Won";
                    else winStatusText = "Red Won";
                    break;
                case 1:
                    if(againstComputer)winStatusText = "You lost";
                    else winStatusText = "Yellow Won";
                    break;
                default:
                    winStatusText = "!!Match draw!!";
            }
            winnerTextView.setText(winStatusText);//display the message
            restartButton.setVisibility(View.VISIBLE);//show restart button
            homeButton.setVisibility(View.VISIBLE);// show homePage button
            winnerTextView.setVisibility(View.VISIBLE);// makes message text visible
        }
    }


    public void reset(View view) {
        /*
        resets the game's state and variables
         */
        gameOn = true;
        Arrays.fill(items, -1);// empties the board
        turnCount = 0;
        playerTurn = 0;
        TextView winnerTextView = (TextView) findViewById(R.id.WinnerTextView);
        Button restartButton = (Button) findViewById(R.id.RestartButton);
        Button homeButton = (Button) findViewById(R.id.HomeButton);
        winnerTextView.setVisibility(View.INVISIBLE);// hides final message
        restartButton.setVisibility(View.INVISIBLE);// hides restart button
        homeButton.setVisibility(View.INVISIBLE);// hides homePage button

        GridLayout gridLayout = (GridLayout) findViewById(R.id.GridLayout);
        //empties the board visually
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView imageView = (ImageView) gridLayout.getChildAt(i);
            imageView.setImageDrawable(null);
        }
        if(againstComputer&&firstTurnComputer){
            playerTurn=1;
            placeItem();
        }
    }

    public void goHome(View view){
        // go to homePage
        finish();
    }

    private int checkForPlaceItem() {
        /*
         if making a particular move wins the match or prevents from being defeated
         this returns the position where that move is to be made
         if no such move is possible in current turn return -1
         */
        int index = -1;
        for (int[] sequence : winSequence) {
            int first = items[sequence[0]], second = items[sequence[1]], third = items[sequence[2]];
            if ((first == second || third == second || first == third) &&
                    (first == -1 || second == -1 || third == -1) &&
                    (!((first == -1 && second == -1) || (second == -1 && third == -1) || (first == -1 && third == -1)))
            ) {
                if (first == second)
                    index = sequence[2];
                else if (second == third) index = sequence[0];
                else index = sequence[1];
            }
        }
        return index;
    }

    private int checkForPlaceItem(int[] p_items,int whosTurn) {
    	/*
         if making a particular move wins the match (according to the state of p_items) 
         or prevents from being defeated
         this returns the position where that move is to be made
         if no such move is possible in current turn return -1
         */
        
        int index = -1;
        int winIndex=-1,loseIndex=-1;
        for (int[] sequence : winSequence) {
            int first = p_items[sequence[0]], second = p_items[sequence[1]], third = p_items[sequence[2]];
            if ((first == second || third == second || first == third) &&
                    (first == -1 || second == -1 || third == -1) &&
                    (!((first == -1 && second == -1) || (second == -1 && third == -1) || (first == -1 && third == -1)))
            ) {
                if (first == second){
                    if(first==whosTurn)
                    winIndex = sequence[2];
                    else loseIndex=sequence[2];
                }
                else if (second == third){
                    if(second==whosTurn)
                        winIndex = sequence[0];
                    else loseIndex=sequence[0];
                }
                else {
                    if(first==whosTurn)
                        winIndex = sequence[1];
                    else loseIndex=sequence[1];
                }

            }
        }
        if(winIndex!=-1)index=winIndex;
        else if(loseIndex!=-1)index=loseIndex;
        return index;
    }
    private void placeItem(){
    	//this function makes the computer's move if playing against it
        if(turnCount<9 && gameOn){// checks if game is still on
            int index = checkForPlaceItem();
            if (index == -1) {
                if(difficultyImp)
                    index=checkMove();//find's the best possible move
                else{ 
                    // if difficulty is not set to impossible either randomly
                    // makes a move or makes the best posibble move
                    Random random=new Random();
                    int randomNumner=random.nextInt(100)+1;
                    if(randomNumner<=50)index=checkMove();

                    else{
                        List<Integer> possibleIndex=new ArrayList<>();
                        for(int i=0;i<9;i++)
                            if(items[i]==-1)possibleIndex.add(i);
                        int randomIndex=random.nextInt(possibleIndex.size());
                        index=possibleIndex.get(randomIndex);
                        }
                    }
            }
            // visually make the move
            ImageView item ;
            switch (index) {
                case 0:
                    item = (ImageView) findViewById(R.id.imageView1);
                    break;
                case 1:
                    item = (ImageView) findViewById(R.id.imageView2);
                    break;
                case 2:
                    item = (ImageView) findViewById(R.id.imageView3);
                    break;
                case 3:
                    item = (ImageView) findViewById(R.id.imageView4);
                    break;
                case 4:
                    item = (ImageView) findViewById(R.id.imageView5);
                    break;
                case 5:
                    item = (ImageView) findViewById(R.id.imageView6);
                    break;
                case 6:
                    item = (ImageView) findViewById(R.id.imageView7);
                    break;
                case 7:
                    item = (ImageView) findViewById(R.id.imageView8);
                    break;
                default:
                    item = (ImageView) findViewById(R.id.imageView9);
            }
            if(playerTurn==1) {
                item.setImageResource(R.drawable.yellow);
                playerTurn = 0;
                items[index] = 1;
            }
            else{

                item.setImageResource(R.drawable.red);
                playerTurn = 1;
                items[index] = 0;
            }
            item.setTranslationY(-1500);
            item.animate().translationYBy(1500).setDuration(300);
            checkIfPlayerWon();
            turnCount++;
            if(turnCount>=9&&gameOn)endGame(-1);
        }
        else if(gameOn)endGame(-1);
    }

    private int checkMove(){
    	// returns the best possible move that can be made in current turn
        int index;
        countPossibleMoves=0;
        Arrays.fill(scoreForPosition,0);
        Arrays.fill(countPositionPossibilities,0);
        Arrays.fill(countPositionDefeats,0);
        Arrays.fill(countPositionWins,0);
        List<Integer> possibleMoves=new ArrayList<>();// stores the possible moves that can be made
        for(int i=0;i<9;i++)
        {
            if(items[i]==-1)possibleMoves.add(i);
            else scoreForPosition[i]=Integer.MIN_VALUE;
        }
        int cur_turn=forComp;
        int[] possibleItems = items.clone();
        //figure out what move to make to win or prevent defeat in current move  
        index = checkForPlaceItem(possibleItems,cur_turn);
        if(index!=-1){// such move is there for current board state
            possibleItems[index] = cur_turn;// makes that move
            int status = checkForWin(possibleItems);// checks if anyone won given possibleItems as board state
            if(status==forComp){// computer won as per possibleItems state
                scoreForPosition[possiblePosition] += 10;
                countPositionPossibilities[possiblePosition]++;
                countPositionWins[possiblePosition]++;
            }
            else if(status==forUser){// computer lost as per possibleItems state
                scoreForPosition[possiblePosition]-=10;
                countPositionPossibilities[possiblePosition]++;
                countPositionDefeats[possiblePosition]++;
            }
            else{// no one won in current move as per possibleItems state
                scoreForPosition[possiblePosition]+=2;
                if(possibleMoves.size()==1){
                    countPositionPossibilities[possiblePosition]++;
                }
            }
            return index;
        }
        //no such move exists
        if(possibleMoves.size() == 1){// only one move is possible, make that move
            possibleItems[possibleMoves.get(0)] = cur_turn;
            return possibleMoves.get(0);
        }
        
        
        // figures out what move to make by considering all possible moves
        int nextTurn = forUser;
        for(int i=0;i<possibleMoves.size();i++){// try each move possible in current turn one by one
            possibleItems=items.clone();
            possibleItems[possibleMoves.get(i)]=forComp;
            possiblePosition=possibleMoves.get(i);
            checkAllMoves(possibleItems.clone(),nextTurn);// check moves that can be made after making
            // currently selected move
        }
        double maxPoints;
        double[] finalScore=new double[9];
        // give score to all the possible moves
        for(int i=0;i<9;i++){
            finalScore[i]=(scoreForPosition[i]/(double)countPositionPossibilities[i])*10;
        }
	//figure out the maximum score and how many positions got it
        int maxCount=1,maxIndex=0;
        maxPoints=finalScore[0];
        for(int i=1;i<9;i++)
        {
            if(finalScore[i]>maxPoints){
                maxPoints=finalScore[i];
                maxCount=1;
                maxIndex=i;
                                            }
            else if(finalScore[i]==maxPoints)maxCount++;
        }
        if(maxCount==1)return maxIndex;// one position got max score, return it
        
        // more than one position have max score, return any one of it randomly 
        int[] p_positions=new int[maxCount];
        for(int i=0,c=0;i<9&&c<maxCount;i++)
            if(finalScore[i]==maxPoints)p_positions[c++]=i;
        Random random=new Random();
        int randomInt;
        if(maxCount>2)randomInt=random.nextInt(maxCount);
        else {
            randomInt=random.nextInt(maxCount+1000);
            randomInt=(randomInt<(maxCount+1000)/2)?0:1;
        }
        index=p_positions[randomInt];
        return index;
    }

    private void checkAllMoves(int[] p_items,int cur_turn){
    	/*
    		scores all possible positions for current turn by analyzing
    		all the moves that can be made after making current move	
    	*/
        List<Integer> possibleMoves=new ArrayList<>();// stores all posiible moves
        int[] possibleItems = p_items.clone();
        for (int i = 0; i < 9; i++)
            if (p_items[i] == -1) possibleMoves.add(i);
        if(possibleMoves.size() == 0)return;
        int p_index=checkForPlaceItem(p_items,cur_turn);//move that wins match or prevents defeat
        int nextTurn;
        //switch current and next turn i.e. mark move as made 
        if(cur_turn == forUser)nextTurn = forComp;
        else nextTurn = forUser;
        if(p_index!=-1){
            possibleItems[p_index] = cur_turn;// make move
            
            // score the current move according to given imaginary move
            int status=checkForWin(possibleItems);
            if(status==forComp){
                scoreForPosition[possiblePosition]+=10;
                countPositionPossibilities[possiblePosition]++;
                countPositionWins[possiblePosition]++;
            }
            else if(status==forUser){
                scoreForPosition[possiblePosition]-=10;
                countPositionPossibilities[possiblePosition]++;
                countPositionDefeats[possiblePosition]++;
            }
            else{
                scoreForPosition[possiblePosition]+=2;
                if(possibleMoves.size()==1){
                    countPositionPossibilities[possiblePosition]++;
                }
                else   checkAllMoves(possibleItems.clone(),nextTurn);
            }
        }
        else {
        //no single move will end the game, so check all the possible moves 
        //and moves that can be after it
            for(int i=0;i<possibleMoves.size();i++){
                possibleItems=p_items.clone();
                possibleItems[possibleMoves.get(i)]=cur_turn;
                checkAllMoves(possibleItems.clone(),nextTurn);
            }
        }
    }
}
