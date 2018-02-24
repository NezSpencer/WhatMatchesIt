package com.nuhiara.nezspencer.ShapeMatchColor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;


public class StartPage extends BaseGameActivity {

    SignInButton signInButton;
    ImageButton play_game;
    ImageButton checkAchievement;
    ImageButton checkLeaderBoard;
    ImageButton rateGame;

    int bestScore;
    int currentScore;
    private int number;

    int numSquare;
    int numCircle;
    int numTriangle;
    int numYellowSqaure;

    SharedPreferences yell,circ,trian,sqar;
    SharedPreferences.Editor yellEd,circEd,trianEd,sqarEd;

    final static String yel="yellow";
    final static String cir="circle";
    final static String tri="triangle";
    final static String sq="yellowSquare";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_layout);



        number=0;


        signInButton=(SignInButton)findViewById(R.id.signinButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginUserInitiatedSignIn();
            }
        });

        play_game=(ImageButton)findViewById(R.id.play_button);
        play_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(StartPage.this, Home.class));

            }
        });

        checkAchievement=(ImageButton)findViewById(R.id.checkAchievement_button);
        checkAchievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(StartPage.this,"Score to send is "+currentScore,Toast.LENGTH_LONG).show();
            }
        });
        checkLeaderBoard=(ImageButton)findViewById(R.id.leaderBoardButton);
        checkLeaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(StartPage.this,"score to send is "+bestScore,Toast.LENGTH_LONG).show();
            }
        });

        rateGame=(ImageButton)findViewById(R.id.rate_button);
        rateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName())));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences highScorePreferences= PreferenceManager.getDefaultSharedPreferences(this);
        bestScore=highScorePreferences.getInt("highscore",0);

        SharedPreferences scorePreferences=PreferenceManager.getDefaultSharedPreferences(this);
        currentScore=scorePreferences.getInt("score",0);

        yell=PreferenceManager.getDefaultSharedPreferences(this);
        circ=PreferenceManager.getDefaultSharedPreferences(this);
        sqar=PreferenceManager.getDefaultSharedPreferences(this);
        trian=PreferenceManager.getDefaultSharedPreferences(this);

        numYellowSqaure=yell.getInt(yel,0);
        numCircle=circ.getInt(cir,0);
        numSquare=sqar.getInt(sq,0);
        numTriangle=trian.getInt(tri,0);

        if (getApiClient().isConnected())
        {

            sendToLeaderboard(bestScore);
            checkUnlockedAchievements(currentScore,numCircle,numSquare,numTriangle,numYellowSqaure);
        }

    }

    public void sendToLeaderboard(int highscore)
    {
        Games.Leaderboards.submitScore(getApiClient(),
                getString(R.string.board_id),
                highscore);
    }

    public void checkUnlockedAchievements(int score,int numCircle,int numSquare,int numTriangle,
                                          int numsOfYellowsqaures)
    {
        boolean isPrime=false;

        if (score % 3==0)
        {

            Games.Achievements.unlock(getApiClient(),
                    getString(R.string.achievement_jump_by_3));
        }

        if (numCircle >=20)
        {

            Games.Achievements.unlock(getApiClient(),
                    getString(R.string.achievement_circulator));
        }
        if (numSquare>0)
        {

            for (int i=numSquare; i>0; i--)
                Games.Achievements.unlock(getApiClient(),
                        getString(R.string.achievement_squared));

        }
        if (numTriangle>=20)
        {

            Games.Achievements.unlock(getApiClient(),
                    getString(R.string.achievement_triangulator));
        }
        if (numsOfYellowsqaures >0)
        {

            for (int i=numsOfYellowsqaures; i>0; i--)
                Games.Achievements.unlock(getApiClient(),
                        getString(R.string.achievement_fair_and_square));

        }

        for (int i=2; i<score; i++)
        {
            if (score % i==0)
                isPrime=false;
            else
                isPrime=true;
        }
        if (isPrime)
        {
            Games.Achievements.unlock(getApiClient(),
                    getString(R.string.achievement_5_prime));
        }

    }

    @Override
    public void onBackPressed() {


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0)
        {

            number++;
            if (number==1)
            {
                Toast.makeText(StartPage.this,"Press return key again to quit",Toast.LENGTH_LONG).show();
            }
            if (number>=2)
                finish();



        }

        return super.onKeyDown(keyCode, event);
    }

}
