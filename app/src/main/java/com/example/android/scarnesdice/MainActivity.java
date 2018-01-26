package com.example.android.scarnesdice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    TextView scoreText;
    ImageView image1,image2;

    Button roll;
    Button hold;
    Button reset;
    TextView win;

    boolean won=false;
    boolean doub=false;

    boolean turn=true;
    int dice1,dice2,userScore,cpuScore,turnScore;

    int[] dicimage={R.drawable.dice1,R.drawable.dice2,R.drawable.dice3,R.drawable.dice4,R.drawable.dice5,R.drawable.dice6};
    Random rn=new Random();

    //chance is true for user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        turnScore=0;
        dice1=dice2=1;
        userScore=0;
        turn=true;
        scoreText=(TextView) findViewById(R.id.scoreText);
        win=(TextView) findViewById(R.id.win);
        image1=(ImageView)findViewById(R.id.image1);
        image2=(ImageView)findViewById(R.id.image2);
        roll=(Button) findViewById(R.id.button_roll);
        hold=(Button) findViewById(R.id.button_hold);
        reset=(Button) findViewById(R.id.button_reset);
    }

    public void hold(View v)
    {
        if(turn) {
            userScore += turnScore;
            turn = false;
            turnScore=0;
            updateUI();
            computerTurn();
        }
        else {
            cpuScore += turnScore;
            turnScore=0;
            turn = true;
            updateUI();
        }

    }

    public void roll(View v) {
        //generate random numbers
        dice1= rn.nextInt(6)+1;
        dice2= rn.nextInt(6)+1;
        doub=false;
        if(dice1==dice2 && dice1!=1) //if value on both the dice is same then user cant hold
        {
            doub=true;
            turnScore += dice1+dice2;
            updateUI();
            if(turn)
                hold.setEnabled(false);


        }
        else if(dice1!=1 && dice2!=1) {
            turnScore += dice1+dice2;
            if (turn) {
                if(turnScore+userScore>=50) {
                    userScore+=turnScore;
                    won=true;
                    win.setEnabled(turn);
                    win.setText("Winner: USER \n Reset to continue");
                    Toast t = Toast.makeText(getApplicationContext(), "User Won!", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
            else
            {
                if(turnScore+cpuScore>=50) {
                    cpuScore+=turnScore;
                    won=true;
                    win.setEnabled(true);
                    win.setText("Computer Won!! \n Reset to play another game");
                    Toast t = Toast.makeText(getApplicationContext(), "Comp Won!", Toast.LENGTH_LONG);
                    t.show();
                }
            }
            updateUI();
        }
        else if(dice1==1 && dice2==1)
        {
            if(turn)
            {
                userScore=0;
                turnScore=0;
                turn=!turn;
                updateUI();
                computerTurn();
            }
            else
            {
                cpuScore=0;
                turnScore=0;
                turn=!turn;
                updateUI();
            }

        }
        else {
            turnScore=0;
          /*  turn=!turn;
            updateUI();
            if(!turn)
                computerTurn();
            //hold(null);
            */
            hold(null);
        }

       // updateUI();

    }

    public void computerTurn()
    {
        int rollr=rn.nextInt(2);
        if(!turn)
        {

            if(rollr==0&&cpuScore<50)
            {
                roll(null);
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        computerTurn();
                    }
                },1000);
            }
            else if(!doub)
                hold(null);
        }

    }

    public void reset(View view) {
        turnScore=0;
        userScore=0;
        cpuScore=0;
        dice1=1;
        dice2=1;
        won=false;
        turn=true;
        updateUI();

    }

    public void updateUI()
    {
        scoreText.setText(" Your Score= "+ userScore + "\n Computer Score=" + cpuScore+"\n Turn Score=" + turnScore+"\n Turn: "+turn);
        image1.setImageResource(dicimage[dice1-1]);
        image2.setImageResource(dicimage[dice2-1]);
        //Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.animation);
        //image.startAnimation(hyperspaceJumpAnimation);
        if(won || !turn)
        {
            roll.setEnabled(false);
            hold.setEnabled(false);
        }
        else
        {
            roll.setEnabled(true);
            hold.setEnabled(true);
        }

    }
}
