package com.example.connect3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class StartingClass extends AppCompatActivity {
    boolean againstComputer=true;
    boolean firstTurnComputer=false;
    boolean difficultyImp=true;
    static  String MSG_AGAINST="StartingClass_againstComputer";
    static  String  MSG_FIRSTTURN="StartingClass_firstTurnComputer";
    static String MSG_DIFFICULTY="StartingClass_DIFFICULTYIMP";


    public void againstSelect(View view){
        RadioButton selectedButton=(RadioButton)view;
        View layout=findViewById(R.id.AgainstComputerLayout);
        if(selectedButton.getId()==R.id.RadioButtonComputer){
            //match will be against computer
            againstComputer=true;
            layout.setVisibility(View.VISIBLE);
        }
        else{
            // match against another person
            layout.setVisibility(View.INVISIBLE);
            againstComputer=false;
        }
    }

    public void startGame(View view){
        /*
        starts the activity where game will start with settings as chosen by user
         */
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra(MSG_AGAINST,againstComputer);
        intent.putExtra(MSG_FIRSTTURN,firstTurnComputer);
        intent.putExtra(MSG_DIFFICULTY,difficultyImp);
        System.out.println("SC against comp : "+againstComputer);
        System.out.println("SC firstTurnComp : "+firstTurnComputer);
        System.out.println("SC diffcultyImp : "+difficultyImp);
        startActivity(intent);
    }

    public void firstTurnSelect(View view){
        // sets whether first turn would be of computer or not
        RadioButton selecteddButton=(RadioButton)view;
        if(selecteddButton.getId()==R.id.RadioButtonComputerFirst){
            firstTurnComputer=true;
        }
        else firstTurnComputer=false;
    }

    public void difficultySelect(View view){
        //sets difficulty level
        RadioButton selecteddButton=(RadioButton)view;
        if(selecteddButton.getId()==R.id.RadioButtonNormal){
            difficultyImp=false;
        }
        else difficultyImp=true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_class);
    }
}
