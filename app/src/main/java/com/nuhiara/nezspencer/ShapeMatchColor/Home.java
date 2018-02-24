package com.nuhiara.nezspencer.ShapeMatchColor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class Home extends Activity implements View.OnClickListener {

    int shapeList[]={R.drawable.red_circle,R.drawable.blue_circle,R.drawable.yellow_circle,R.drawable.green_circle,
            R.drawable.red_triangle,R.drawable.blue_triangle,R.drawable.yellow_triangle,R.drawable.green_triangle,
            R.drawable.red_square,R.drawable.blue_square,R.drawable.yellow_square,R.drawable.green_square};


    int iconList[]={R.drawable.red_circl,R.drawable.blue_circl,R.drawable.yellow_circl,R.drawable.green_circl,
            R.drawable.red_triangl,R.drawable.blue_triangl,R.drawable.yellow_triangl,R.drawable.green_triangl,
            R.drawable.red_squar,R.drawable.blue_squar,R.drawable.yellow_squar,R.drawable.green_squar};


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

    int []arrayIcon=new int[5];


    TextView high_board;
    int numberShown;
    int totalScore;
    int highscore=0;

    //to store the highscores
    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    SharedPreferences currentScorePref;
    int progressBarDecrement; //used to update progressBar values

    ImageView bigImage;
    ImageView icon;
    ProgressBar progressBar;

    Button yes_button;
    Button no_button;

    Vibrator vibrator;
    boolean isLevel1;
    boolean isLevel2;
    boolean isLevel3;

    TextView score_text;
    int a;
    int b;
    private boolean isCorrect;
    private int firstTime;
    private boolean paused;
    private int BackPress;

    worker worker;
    private boolean closing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        numSquare=0;
        numCircle=0;
        numTriangle=0;
        numYellowSqaure=0;


        BackPress=0;

        yell=PreferenceManager.getDefaultSharedPreferences(this);
        circ=PreferenceManager.getDefaultSharedPreferences(this);
        sqar=PreferenceManager.getDefaultSharedPreferences(this);
        trian=PreferenceManager.getDefaultSharedPreferences(this);

        yellEd=yell.edit();
        circEd=circ.edit();
        trianEd=trian.edit();
        sqarEd=sqar.edit();

        yellEd.putInt(yel,0);
        circEd.putInt(cir,0);
        trianEd.putInt(tri,0);
        sqarEd.putInt(sq,0);

        yellEd.apply();
        circEd.apply();
        trianEd.apply();
        sqarEd.apply();

        prefs=PreferenceManager.getDefaultSharedPreferences(Home.this);
        highscore=prefs.getInt("highscore",0);

        currentScorePref= PreferenceManager.getDefaultSharedPreferences(Home.this);


        high_board=(TextView)findViewById(R.id.high_label);
        high_board.setText("BEST\n"+highscore);
        numberShown =0;
        totalScore=0;

        firstTime=0;
        vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        isLevel1=true;
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        progressBarDecrement=100; // progressBar value set to max;

        //paused=false;

        //progressBar.setProgress(progressBarDecrement);


        bigImage=(ImageView)findViewById(R.id.big_image);
        icon=(ImageView)findViewById(R.id.icon);
        yes_button=(Button)findViewById(R.id.yes_button);
        no_button=(Button)findViewById(R.id.no_button);

        yes_button.setOnClickListener(this);
        no_button.setOnClickListener(this);

        score_text=(TextView)findViewById(R.id.score_label);

        final Dialog d=new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.setContentView(R.layout.drew);
        d.show();
        d.setCancelable(false);
        d.setCanceledOnTouchOutside(true);

        game();

    }

    public int[] miniShapeArray()
    {
        int [] tempShape=new int[5];
        int one=(int)(Math.random()*12);
        int two=(int)(Math.random()*12);
        while (one==two)
            two=(int)(Math.random()*12);

        int three=(int)(Math.random()*12);
        while (one==three || two==three)
            three=(int)(Math.random()*12);

        int four=(int)(Math.random()*12);
        while (one==four || two==four || three==four)
            four=(int)(Math.random()*12);


        int five=(int)(Math.random()*12);
        while (one==five || two==five || three==five || four==five)
            five=(int)(Math.random()*12);

        tempShape[0]=one;
        tempShape[1]=two;
        tempShape[2]=three;
        tempShape[3]=four;
        tempShape[4]=five;

        return tempShape;
    }


    public int randomizeIcon()
    {
        a=(int)(Math.random()*5);

        if(isLevel1) {

                return iconList[arrayIcon[a]];

        }

        if (isLevel2)
        {
            //if (numberShown <=50) {
              //  return iconList[a];
            //} else if (numberShown <=70) {
              //  return iconTriangle[a];
            //} else {
              //  return iconSquare[a];
            //}
            return iconList[arrayIcon[a]];
        }

        if (isLevel3)
        {
            //if (numberShown%5 ==0) {
              //  return iconList[a];
            //} else if (numberShown%3 ==0) {
              //  return iconTriangle[a];
            //} else {
              //  return iconSquare[a];
           // }
            return iconList[arrayIcon[a]];
        }

        return 0;
    }

    public int randomizeShape()
    {
        b=(int)(Math.random() *5);

        if(isLevel1) {
          //  if (numberShown > 10 && numberShown <= 20) {
            //    return shapeList[b];
            //} else if (numberShown > 20 && numberShown <= 30) {
              //  return triangleList[b];
            //} else {
              //  return squareList[b];
            //}
            return shapeList[arrayIcon[b]];
        }

        if (isLevel2)
        {
            //if (numberShown <=50) {
              //  return shapeList[b];
            //} else if (numberShown <=70) {
              //  return triangleList[b];
            //} else {
              //  return squareList[b];
            //}
            return shapeList[arrayIcon[b]];
        }

        if (isLevel3)
        {
            //f (numberShown%5 ==0) {
            //    return shapeList[b];
            //} else if (numberShown%3 ==0) {
              //  return triangleList[b];
            //} else {
              //  return squareList[b];
            //}
            return shapeList[arrayIcon[b]];
        }

        return 0;
    }



    public void game()
    {

        changeImage();

         worker=new worker();
        worker.execute();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       // int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
          //  return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    public void changeImage()
    {
        numberShown++;

        if (numberShown==1)
        {
            arrayIcon=miniShapeArray();
        }

        if (numberShown==31)
            arrayIcon=miniShapeArray();

        if (numberShown>90 && numberShown%20==0)
            arrayIcon=miniShapeArray();

        icon.setImageResource(randomizeIcon());
        bigImage.setImageResource(randomizeShape());


        if (numberShown >30 && numberShown <=90)
        {
            isLevel1=false;
            isLevel2=true;
        }

        if (numberShown >90)
        {
            isLevel2=false;
            isLevel3=true;
        }

    }

    public boolean isSimilar()
    {
        if (a==b)
            return true;

        return false;
    }

    public void achievementVariables()
    {

            if (shapeList[a]==R.drawable.blue_circle || shapeList[a]==R.drawable.red_circle ||shapeList[a]==R.drawable.yellow_circle)
            {
                numCircle++;
            }
            if (shapeList[a]==R.drawable.red_square||shapeList[a]==R.drawable.blue_square||shapeList[a]==R.drawable.yellow_square)
            {
                numSquare++;
            }
            if (shapeList[a]==R.drawable.blue_triangle||shapeList[a]==R.drawable.red_triangle||shapeList[a]==R.drawable.yellow_triangle)
            {
                numTriangle++;
            }
            if (shapeList[a]==R.drawable.yellow_square)
                numYellowSqaure++;

    }



    @Override
    public void onClick(View view) {

        firstTime++;
        if (view.getId()==R.id.yes_button && isSimilar())
        {
            isCorrect=true;
            paused=false;
            totalScore++;
            achievementVariables();

        }

        else if (view.getId()==R.id.no_button && !isSimilar())
        {
            totalScore++;
            isCorrect=true;
            paused=false;
        }
        else
        {
            isCorrect=false;
            firstTime=0;

        }

    }

    @Override
    public void onBackPressed() {

    }

    class worker extends AsyncTask<Void,Integer,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {

            while(!isCorrect && firstTime==0)
            {

                //Toast.makeText(Home.this,"one",Toast.LENGTH_LONG).show();
               //Log.e("one", " isCorrect is " + isCorrect);
                publishProgress(progressBarDecrement);
            }
            //Log.e("two"," isCorrect is "+isCorrect);

            while (progressBarDecrement >=0)
            {
               //Log.e("inside while"," isCorrect is "+isCorrect);
                while (paused)
                {
                   // Log.e("inside timer while loop", " paused is " + paused);
                    //Toast.makeText(Home.this,"paused",Toast.LENGTH_LONG).show();
                }
                publishProgress(progressBarDecrement);
                try {
                    Thread.sleep(8);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isCorrect)
                {
                    progressBarDecrement=100;




                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            changeImage();
                            score_text.setText("Score\n"+totalScore);
                            isCorrect=false;
                        }
                    });
                }
                progressBarDecrement--;

                if (!isCorrect && firstTime==0)
                {
                    break;
                }

            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);



                vibrator.vibrate(500);


                edit=currentScorePref.edit();
                edit.putInt("score",totalScore);
                edit.apply();

            yellEd=yell.edit();
            circEd=circ.edit();
            trianEd=trian.edit();
            sqarEd=sqar.edit();

            yellEd.putInt(yel,numYellowSqaure);
            circEd.putInt(cir,numCircle);
            trianEd.putInt(tri,numTriangle);
            sqarEd.putInt(sq,numSquare);

            yellEd.apply();
            circEd.apply();
            trianEd.apply();
            sqarEd.apply();


            if (totalScore >highscore)
                {

                    edit=prefs.edit();
                    edit.putInt("highscore",totalScore);
                    edit.apply();


                    final AlertDialog.Builder highBye=new AlertDialog.Builder(Home.this);
                    View vc=getLayoutInflater().inflate(R.layout.game_over_high,null);

                    highBye.setView(vc);


                    TextView hg=(TextView)vc.findViewById(R.id.hiscore);
                    hg.setText(""+totalScore);

                    highBye.setPositiveButton("Retry",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent=getIntent();
                            dialogInterface.dismiss();
                            dialogInterface.cancel();
                            finish();
                            startActivity(intent);

                        }
                    });

                    highBye.setNegativeButton("Home",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                            dialogInterface.cancel();
                            finish();
                        }
                    });

                    AlertDialog byebye=highBye.create();
                    byebye.setOwnerActivity(Home.this);
                    byebye.setCancelable(false);

                    byebye.setCanceledOnTouchOutside(false);
                    byebye.show();

                }
                else {
                    final AlertDialog.Builder bye=new AlertDialog.Builder(Home.this);
                    View xit=getLayoutInflater().inflate(R.layout.game_over,null);
                    bye.setView(xit);

                    TextView v1=(TextView)xit.findViewById(R.id.score_text);
                    v1.setText("You Got: "+totalScore);
                    TextView v2=(TextView)xit.findViewById(R.id.highscore_text);
                    v2.setText("Highscore :"+highscore);

                    bye.setPositiveButton("Retry",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent=getIntent();
                            dialogInterface.dismiss();
                            dialogInterface.cancel();
                            finish();
                            startActivity(intent);

                        }
                    });

                    bye.setNegativeButton("Home",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                            dialogInterface.cancel();
                            finish();
                        }
                    });

//                    AlertDialog byebye=bye.create();
//                    byebye.setOwnerActivity(Home.this);

                    bye.setCancelable(false);

                    bye.show();


                }

            }



        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        paused=false;
        changeImage();
        progressBarDecrement=100;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.e("destroy","destroying Home");
    }
}
