package com.luk.presentspresents;



import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

//TODO:let's documentate unknown variables and misc methods 

//TODO:upgrade 'blendziors' threads. this may be 
//pokemon ghosts: hunter, gastly or gengar, also flying pokemons appreciated such as pidgeot,  
//which could catch santas' presents, and birds when they grab 5 presents or more GameOver is Visible)
//add golbat feature: create animation when he touches Santa(animation with tongue)
//(after he hides on left, we set him up normal flying animation)

//set good 0.2 s delay between addding another present, to prevent gift-fall on Blendzior
//TODO: make controllers more friendly(make step smaller and thread invoker faster), 
//TODO: OPTIONAL - set thread for dynamic background, or just add to the activity some buildings, Moon, 
//and move them forward, make illusion of moving
//TODO: OPTIONAL - change santa bitmap to less pixellated

/**
 * RULES OF THE GAME
 * @author lukasz
 * if blendzior licks santa, He gots heartAttack and loses one heart(he has 4 only)
 * pidgeots always fly under santamobile, he want to catch falling presents. Avoid this behaviour, 
 * otherwise you lose
 * Blendziors may be killed by simply throwing a present on them, 
 * Pidgeots and Blendziors both may be killed by candies which Santa may throw 
 */
//TODO: design levels: for example:
//level 1: throw 5 presents:
//level 2: throw 10 presents and kill 5 blendziors
//lavel 3: Pidgeots attacks you, but you must throw 15 presents! 
//TODO:fix multiple invoked threads on the same present, clear screen from 
//unused presents



public class MainActivity extends Activity {
	boolean unRestoredGifts = false; //gdy jeszcze nie wszystkie prezenty zostały utworzone
	int CURRENT_LEVEL = 1;
	boolean levelUp = false,zajebanyPrezent = false;
	boolean SoundIsOn = false; 
	public boolean isRunning = false;
	public int POINTS=0;
	int Santa_ID = 0,Blendzior_ID, Komin_ID,Txt_ID, Hearts_ID, Pidgeot_ID;
	int BlendziorStep = 10, Santa_,ds=50,dsa=60;
	int buttonsize = 100, tilesize = 30,forHeartsWidth=100, forHeartsHeight=30;
	int giftsize = 35, MIN_BLENDZIOR_DIST=35;
	int step = 10;
	int elements_on_scene = 50;
	int index=0; //index for imageViews on the scene
	int MAX_PRESENTS = 10;
	//TODO:
	//TODO:
	//TODO:
	//TODO:
	//TODO:
	ImageView ref = null;
	
	/**
	 * Its tricky: Santa should have 4 lives, but program needs to have 0, 2, 4, 6 lives
	 * It depends on MoveBlendzior thread, so when he touches Santa, he actually do this twice
	 */
	int NumberOfLives = 0;
	boolean IsmoveH = false,IsmoveV = false, IsUp=false, IsLeft = true,
			deadBlendzior = false, deathSanta = false;
	static int indexforgiftsThreads = 0;
	static int indexforgifts=0;
	Random generator = new Random();
	GiftDown thread=null;
	AnimationDrawable PidgeotAnimation = new AnimationDrawable();//animation reserved for Pidgeys
	AnimationDrawable staticAnimation = new AnimationDrawable(); //animation for Santa
	AnimationDrawable BlendziorAnimation = new AnimationDrawable(); //animation for flying Golbat-"Blendzior"
	ImageView[] arrayofImages = new ImageView[elements_on_scene];//ImageViews on scene
	ImageView KorwinGameOver;//ImageView for KorwinGameOver
	ImageView[] ImageViewGifts = new ImageView[MAX_PRESENTS];//ImageViews for Gifts
	TextView txt_instance; //Textview for counting presents
    Bitmap bitmap_down, bitmap_up, bitmap_right, bitmap_left, //arrows - controllers
    		bitmap_komin,  bitmap_blanktile, 
	 fly001,fly002,fly003,fly004, flydead, //blendzior bitmaps
	 pidg01,pidg02,pidg03,pidg04,pidg05,//pidgotto bitmaps
	bitmap_heart1,bitmap_heart0, bitmap_heart2,bitmap_heart3,bitmap_heart4,bitmap_gift;//, ??needed?
	Drawable deadBlendziorDrawable;
	static Bitmap[] staticGiftbitmap = new Bitmap[12];//jest 12 różnych prezentów
    RelativeLayout lay;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setupButtons();
  	  lay = new RelativeLayout(this);
      lay.setBackgroundResource(R.drawable.sky_winter);
      setContentView(lay);
      
      arrayofImages[index] = new ImageView(this);
	  setupSantaAnimation();
	  //creating blendziors
      ++index;
      setupBlendziorAnimation();
      ++index;
      setupPidgeotAnimation();
      ++index;
      Komin_ID = index;
	  setupPixmap(bitmap_komin, 70, 310);
	  ++index;
      //setup arrows; arrow up
      setupPixmap(bitmap_up, 650, 75);
      ++index;
      setupPixmap(bitmap_down, 650, 275);
      //more gift arrow
      ++index;
      setupPixmap(bitmap_gift, 0, 0);
      //arrow right
      ++index;
	  setupPixmap(bitmap_right, 700, 175);
      //arrow left
      ++index;
	  setupPixmap(bitmap_left, 600, 175);
      KominesAndBlendziors.start(this);
      Pidgotto.start(this);
      SantaBias.start(this);
      OnBlendziorDeath.start(this);
      setupPointsCounter();
      setContentView(R.layout.activity_main);
      }
	public void setupPointsCounter(){
		txt_instance = new TextView(this);
	      txt_instance.setId(++index);
	      Txt_ID = index;
	      txt_instance.setX(200);txt_instance.setY(0);
	      txt_instance.setText("0");
	      txt_instance.setBackground(new BitmapDrawable(getResources(),bitmap_blanktile));
	      txt_instance.setMaxHeight(tilesize);
	      txt_instance.setMaxWidth(tilesize);
	      ++index;Hearts_ID=index;
	      setupPixmap(bitmap_heart4, 250, 0);
	      lay.addView(txt_instance);
	}
	
	@SuppressWarnings("deprecation")
	public void setupPidgeotAnimation(){
		Pidgeot_ID = index;  
		arrayofImages[Pidgeot_ID] = new ImageView(this);
	  	  arrayofImages[Pidgeot_ID].setId(Pidgeot_ID);
	  	  //TODO:add static animations for Pidgeots
	        //Skaluję blendziora bo za duży jest :/
	  	  Drawable p1 = new BitmapDrawable(getResources(),pidg01);
	  	  Drawable p2 = new BitmapDrawable(getResources(),pidg02);
	  	  Drawable p3 = new BitmapDrawable(getResources(),pidg03);
	  	  Drawable p4 = new BitmapDrawable(getResources(),pidg04);
	  	  Drawable p5 = new BitmapDrawable(getResources(),pidg05);
	  	  
	  	 // deadPidgoDrawable = new BitmapDrawable(getResources(),flydead);
	  	  
	  	  PidgeotAnimation.addFrame(p1,100);
	  	  PidgeotAnimation.addFrame(p2,100);
	  	  PidgeotAnimation.addFrame(p3,100);
	  	  PidgeotAnimation.addFrame(p4,100);
	  	  PidgeotAnimation.addFrame(p5,100);
	  	  
	  	  PidgeotAnimation.setOneShot(false);
	  	  arrayofImages[Pidgeot_ID].setBackgroundDrawable(PidgeotAnimation);
	  	  arrayofImages[Pidgeot_ID].setX(780);
	        arrayofImages[Pidgeot_ID].setY(222);
	        arrayofImages[Pidgeot_ID].setVisibility(ImageView.INVISIBLE);
	        arrayofImages[Pidgeot_ID].setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
	                LayoutParams.WRAP_CONTENT));
	        lay.addView(arrayofImages[Pidgeot_ID]);

	}
	@SuppressWarnings("deprecation")
    public void setupBlendziorAnimation(){
      arrayofImages[index] = new ImageView(this);
  	  arrayofImages[index].setId(index);
  	  Blendzior_ID = index;
        //Skaluję blendziora bo za duży jest :/
  	  Drawable fl01 = new BitmapDrawable(getResources(),fly001);
  	  Drawable fl02 = new BitmapDrawable(getResources(),fly002);
  	  Drawable fl03 = new BitmapDrawable(getResources(),fly003);
  	  Drawable fl04 = new BitmapDrawable(getResources(),fly004);
  	  deadBlendziorDrawable = new BitmapDrawable(getResources(),flydead);
  	  
  	  BlendziorAnimation.addFrame(fl01,100);
  	  BlendziorAnimation.addFrame(fl02,100);
  	  BlendziorAnimation.addFrame(fl03,100);
  	  BlendziorAnimation.addFrame(fl04,100);
  	  BlendziorAnimation.setOneShot(false);
  	  arrayofImages[index].setBackgroundDrawable(BlendziorAnimation);
  	  arrayofImages[index].setX(780);
        arrayofImages[index].setY(200);
        arrayofImages[index].setVisibility(ImageView.INVISIBLE);
        arrayofImages[index].setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        lay.addView(arrayofImages[index]);
    }
    public void setupPixmap(Bitmap bmp, int dx, int dy){
  	  arrayofImages[index] = new ImageView(this);
  	  arrayofImages[index].setId(index);
  	  arrayofImages[index].setImageBitmap(bmp);  
  	  arrayofImages[index].setX(dx);
  	  arrayofImages[index].setY(dy);  
  	  arrayofImages[index].setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        lay.addView(arrayofImages[index]);

    }

	@SuppressWarnings("deprecation")
	public void setupSantaAnimation(){
		  arrayofImages[index].setId(index);
		  Santa_ID = index;
		  staticAnimation.addFrame(getResources().getDrawable(R.drawable.santa_ride01),150);
		  staticAnimation.addFrame(getResources().getDrawable(R.drawable.santa_ride02),150);
		  staticAnimation.addFrame(getResources().getDrawable(R.drawable.santa_ride03),150);
		  staticAnimation.addFrame(getResources().getDrawable(R.drawable.santa_ride04),150);
		  staticAnimation.addFrame(getResources().getDrawable(R.drawable.santa_ride05),150);
		  staticAnimation.setOneShot(false);
		  arrayofImages[index].setBackgroundDrawable(staticAnimation);
		  arrayofImages[index].setX(50);
		  arrayofImages[index].setY(50);
		  arrayofImages[index].setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
	            LayoutParams.WRAP_CONTENT));
		  lay.addView(arrayofImages[index]);
	  }
	public void setupButtons(){
		fly001 = BitmapFactory.decodeResource(getResources(), R.drawable.fly01);
		fly002 = BitmapFactory.decodeResource(getResources(), R.drawable.fly02);
		fly003 = BitmapFactory.decodeResource(getResources(), R.drawable.fly03);
		fly004 = BitmapFactory.decodeResource(getResources(), R.drawable.fly04);
		
		fly004 = Bitmap.createScaledBitmap(fly004, 
				fly001.getWidth()/2, fly001.getHeight()/2, true);
		fly003 = Bitmap.createScaledBitmap(fly003, 
				fly001.getWidth()/2, fly001.getHeight()/2, true);
		fly002 = Bitmap.createScaledBitmap(fly002, 
				fly001.getWidth()/2, fly001.getHeight()/2, true);
		flydead = BitmapFactory.decodeResource(getResources(), R.drawable.fly05_death);
		flydead = Bitmap.createScaledBitmap(flydead, 
				fly001.getWidth()/2, fly001.getHeight()/2, true);
		fly001 = Bitmap.createScaledBitmap(fly001, 
				fly001.getWidth()/2, fly001.getHeight()/2, true);
		pidg01 = BitmapFactory.decodeResource(getResources(), R.drawable.pidg_01);
		pidg02 = BitmapFactory.decodeResource(getResources(), R.drawable.pidg_02);
		pidg03 = BitmapFactory.decodeResource(getResources(), R.drawable.pidg_03);
		pidg04 = BitmapFactory.decodeResource(getResources(), R.drawable.pidg_04);
		pidg05 = BitmapFactory.decodeResource(getResources(), R.drawable.pidg_05);
		//TODO: Later : create death for Pidgotto
		//pidg05 = BitmapFactory.decodeResource(getResources(), R.drawable.pidg_05);
		pidg01 = Bitmap.createScaledBitmap(pidg01, 
				pidg01.getWidth()/2, pidg01.getHeight()/2, true);	
		pidg02 = Bitmap.createScaledBitmap(pidg02, 
				pidg02.getWidth()/2, pidg02.getHeight()/2, true);	
		pidg03 = Bitmap.createScaledBitmap(pidg03, 
				pidg03.getWidth()/2, pidg03.getHeight()/2, true);	
		pidg04 = Bitmap.createScaledBitmap(pidg04, 
				pidg04.getWidth()/2, pidg04.getHeight()/2, true);	
		pidg05 = Bitmap.createScaledBitmap(pidg05, 
				pidg05.getWidth()/2, pidg05.getHeight()/2, true);	
		
		//bi
		bitmap_down = BitmapFactory.decodeResource(getResources(), R.drawable.downbutton);
		bitmap_down = Bitmap.createScaledBitmap(bitmap_down, buttonsize,buttonsize,true);
		bitmap_left = BitmapFactory.decodeResource(getResources(), R.drawable.leftbutton);
		bitmap_left = Bitmap.createScaledBitmap(bitmap_left, buttonsize,buttonsize,true);
		bitmap_up = BitmapFactory.decodeResource(getResources(), R.drawable.upbutton);
		bitmap_up = Bitmap.createScaledBitmap(bitmap_up, buttonsize,buttonsize,true);
		bitmap_right = BitmapFactory.decodeResource(getResources(), R.drawable.rightbutton);
		bitmap_right = Bitmap.createScaledBitmap(bitmap_right, buttonsize,buttonsize,true);
		bitmap_komin = BitmapFactory.decodeResource(getResources(), R.drawable.komin);
		bitmap_komin = Bitmap.createScaledBitmap(bitmap_komin,90,90,true);
		bitmap_gift = BitmapFactory.decodeResource(getResources(), R.drawable.fallgift);
		bitmap_gift = Bitmap.createScaledBitmap(bitmap_gift,buttonsize,buttonsize,true);
		bitmap_blanktile= BitmapFactory.decodeResource(getResources(), R.drawable.blank);
		bitmap_blanktile = Bitmap.createScaledBitmap(bitmap_blanktile,tilesize,tilesize,true);

		bitmap_heart1= BitmapFactory.decodeResource(getResources(), R.drawable.heart1);
		bitmap_heart1 = Bitmap.createScaledBitmap(bitmap_heart1,forHeartsWidth,forHeartsHeight,true);
		bitmap_heart2= BitmapFactory.decodeResource(getResources(), R.drawable.heart2);
		bitmap_heart2 = Bitmap.createScaledBitmap(bitmap_heart2,forHeartsWidth,forHeartsHeight,true);
		bitmap_heart3= BitmapFactory.decodeResource(getResources(), R.drawable.heart3);
		bitmap_heart3 = Bitmap.createScaledBitmap(bitmap_heart3,forHeartsWidth,forHeartsHeight,true);
		bitmap_heart4= BitmapFactory.decodeResource(getResources(), R.drawable.heart4);
		bitmap_heart4 = Bitmap.createScaledBitmap(bitmap_heart4,forHeartsWidth,forHeartsHeight,true);
		bitmap_heart0= BitmapFactory.decodeResource(getResources(), R.drawable.heart0);
		bitmap_heart0= Bitmap.createScaledBitmap(bitmap_heart0,forHeartsWidth,forHeartsHeight,true);
		
		
		
		//bitmapy dla prezentów
		staticGiftbitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.gift01);
		staticGiftbitmap[1] = BitmapFactory.decodeResource(getResources(), R.drawable.gift02);
		staticGiftbitmap[2] = BitmapFactory.decodeResource(getResources(), R.drawable.gift03);
		staticGiftbitmap[3] = BitmapFactory.decodeResource(getResources(), R.drawable.gift04);
		staticGiftbitmap[4] = BitmapFactory.decodeResource(getResources(), R.drawable.gift05);
		staticGiftbitmap[5] = BitmapFactory.decodeResource(getResources(), R.drawable.gift06);
		staticGiftbitmap[6] = BitmapFactory.decodeResource(getResources(), R.drawable.gift07);
		staticGiftbitmap[7] = BitmapFactory.decodeResource(getResources(), R.drawable.gift08);
		staticGiftbitmap[8] = BitmapFactory.decodeResource(getResources(), R.drawable.gift09);
		staticGiftbitmap[9] = BitmapFactory.decodeResource(getResources(), R.drawable.gift10);
		staticGiftbitmap[10] = BitmapFactory.decodeResource(getResources(), R.drawable.gift11);
		staticGiftbitmap[11] = BitmapFactory.decodeResource(getResources(), R.drawable.gift12);
		
		//scale to giftsize
		for(int i = 0; i < 12; i++)
			staticGiftbitmap[i] = Bitmap.createScaledBitmap(staticGiftbitmap[i], giftsize,giftsize,true);
		
		//load KorwinOverGame bitmap
		KorwinGameOver = new ImageView(this);
		
		  
	}
	public void moveChimney(){
	if(isRunning){
		int dx = (int)arrayofImages[Komin_ID].getX() - step;
		if(dx < 0)
			arrayofImages[Komin_ID].setX(750);
		else
			arrayofImages[Komin_ID].setX(dx);
		}
	}
	public int centerX(ImageView view){
		return (int)(view.getX()+view.getWidth()/2.0);
	}
	public int centerY(ImageView view){
		return (int)(view.getY()+view.getHeight()/2.0);
	}
 
    public void moveThemAll(){
    	if(isRunning){
    	//starts Santa animation
    	staticAnimation.start();
    	ImageView santa = arrayofImages[0];
    	ImageView blend = arrayofImages[Blendzior_ID];
    	int centerSantaX = 53*santa.getWidth()/200; //its Santa size of his center
    	int centerSantaY = 23*santa.getHeight()/60;
    	int dist = 30;
    	boolean plusY = generator.nextBoolean();
    	if(plusY)
    		santa.setX(santa.getX()+1);
    	else
    		santa.setX(santa.getX()-1);
    	
    	if(Adiff(santa.getX()+centerSantaX,centerX(blend)) < dist && 
    	   Adiff(santa.getY()+centerSantaY,centerY(blend)) < 3*dist/2){
    		PerformDelay blendziorPunch = new PerformDelay();
    		blendziorPunch.start(this, 1, 5000);
    	}
    	
    	if(santa.getY() > 0 && santa.getY() < 200
    			&& santa.getX() > -20 && santa.getX() < 700){
	    	if(IsmoveV){
	    		if(IsUp)
	    			santa.setY(santa.getY()-12);
	    		else 
	    			santa.setY(santa.getY()+12);
	    	}
	    	if(IsmoveH){
	    		if(IsLeft)
	    			santa.setX(santa.getX()-12);
	    		else if(!IsLeft)
	    			santa.setX(santa.getX()+12);
	    		
	    		}
    	}
      }
    }
    //when Level is new and We need more Gifts
    public void RestoreGifts(){
    	
    }
    public double Adiff(double x,double y){
    	return (x>y) ? x-y : y-x;
    }
    public double euclides(double y, double x){
    	return (double)Math.sqrt(x*x+y*y);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
    	if(isRunning){
    	ImageView santa = (ImageView)findViewById(0);	
        switch (event.getActionMasked()) {
        case MotionEvent.ACTION_UP:{
        	IsmoveV=IsmoveH=false;
        	break;
        }
        case MotionEvent.ACTION_DOWN:{
        	if(event.getX()<100){
        		if(indexforgifts<40)
        			montujGifta();
        		else{
        			unRestoredGifts=true;
        			Context context = getApplicationContext();
        			CharSequence text = "Pusty worek prezentów!";
        			int duration = Toast.LENGTH_SHORT;
        			Toast.makeText(context, text, duration).show();
        			
        		}
//    			if(unRestoredGifts)
//        			for(int i = 0; i < indexforgifts; i++)
//        				ImageViewGifts[i].setVisibility(ImageView.VISIBLE);
        		
        	}
        	if(event.getX()>650 && event.getX()<750 &&event.getY()>275+buttonsize){
        		//move down
        		santa.setY(santa.getY()+step);
        		IsmoveV=true;
       			IsmoveH=false;
       			IsUp=false;
       		}
        	//move up
        	else if(event.getX()>650 && event.getX()<750 &&	event.getY()<175+buttonsize){
        		santa.setY(santa.getY()-step);
        		IsmoveV=true;
        		IsmoveH=false;
        		IsUp=true;
        		
        	}//right move
        	else if(event.getX()>700){
    			santa.setX(santa.getX()+step);
    			IsmoveH=true;
    			IsmoveV=false;
        		IsLeft=false;
        	}
        	//left move
        	else if(event.getX()>600 && event.getX()<700){
        		santa.setX(santa.getX()-step);
        		IsmoveH=true;
        		IsmoveV=false;
        		IsLeft=true;
        	}
        }
        }
    	}
    	return true;
    }
    public void montujGifta(){
      ImageView santa = (ImageView)findViewById(Santa_ID );
	  if(unRestoredGifts || indexforgifts==MAX_PRESENTS-1){
		  indexforgifts=0;
		  levelUp = false;
      }
      ++indexforgifts;
      ++indexforgiftsThreads;
	  
	  ImageViewGifts[indexforgifts] = null;
	  
	  ImageViewGifts[indexforgifts] = new ImageView(this);
	  
	  ImageViewGifts[indexforgifts].setId(index+indexforgifts+1);
  	  ImageViewGifts[indexforgifts].setImageBitmap(staticGiftbitmap[indexforgifts%(staticGiftbitmap.length)]);
	  ImageViewGifts[indexforgifts].setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
	  
  	  ImageViewGifts[indexforgifts].setX(santa.getX()+40);
  	  ImageViewGifts[indexforgifts].setY(santa.getY()+30);      
  	
      ImageViewGifts[indexforgifts].setVisibility(ImageView.VISIBLE);
      lay.addView(ImageViewGifts[indexforgifts]);
      if(indexforgiftsThreads <= MAX_PRESENTS-1){
    	  thread = new GiftDown();
      	  thread.start(this, indexforgiftsThreads); 
      }
      
      Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		CharSequence text = "presents: "+(indexforgifts+1);
		Toast.makeText(context, text, duration).show();
  	}
    
    public void CollectPoints(){
    	++POINTS;	
		txt_instance.setText(""+POINTS/7);
		if((int)(POINTS/7) > 4)
			WinLevel(POINTS/7);
    }
    public void WinLevel(int pts){
    	CharSequence text="";
    	if(pts==5){
    		CURRENT_LEVEL = 2;
    		text = "LVL 2";
    	}
    	else if(pts==10){
    		CURRENT_LEVEL = 3;
    		text = "LVL 3";
    	}
    	else if(pts==15){
    		CURRENT_LEVEL = 4;
    		text = "LVL 4";
    		unRestoredGifts = true;
    	}
    	if(pts%5==0){
    		Context context = getApplicationContext();
    		int duration = Toast.LENGTH_SHORT;
    		Toast.makeText(context, text, duration).show();
    	}
    }
    public void PresentFall(int id){
    	if(isRunning){
    		if(!zajebanyPrezent)
    		if(ImageViewGifts[id].getY()<400){
    			ImageViewGifts[id].setY(
    			ImageViewGifts[id].getY() + 10);  
    		}    
    		//gdy prezent spada, może trafić do komina, ustawiamy znikanie
    		if(	euclides(
    			Adiff(ImageViewGifts[id].getX(), 
    					arrayofImages[Komin_ID].getX()+(float)arrayofImages[Komin_ID].getWidth()/2.0),
    			Adiff(ImageViewGifts[id].getY(), 
    					arrayofImages[Komin_ID].getY()) ) < 50 ){
    			ImageViewGifts[id].setVisibility(ImageView.INVISIBLE);

    			PerformDelay a = new PerformDelay();
        		a.start(this, 0, 1000);
    		}
    		//gdy prezent spada, może trafić na głowę Blendziora, ustawiamy znikanie
    		if(	euclides(
        			Adiff(ImageViewGifts[id].getX(), 
        					arrayofImages[Blendzior_ID].getX()+(float)arrayofImages[Blendzior_ID].getWidth()/2.0),
        			Adiff(ImageViewGifts[id].getY(), 
        					arrayofImages[Blendzior_ID].getY()) ) < MIN_BLENDZIOR_DIST ){
    				deadBlendzior = true;
    				ImageViewGifts[id].setVisibility(ImageView.INVISIBLE);
        			ImageViewGifts[id].setY(ImageViewGifts[Komin_ID].getY()-1000);
        			ImageViewGifts[id].setX(ImageViewGifts[Komin_ID].getX()-1000);
            		//nie dopuszczamy do sytuacji żeby niewidzialny prezent trafił do komina
        	}
    		if(ImageViewGifts[id].getY()>400){
    			ImageViewGifts[id].setVisibility(ImageView.VISIBLE);
    			ImageViewGifts[id].setY(ImageViewGifts[Komin_ID].getY()-1000);
    			ImageViewGifts[id].setX(ImageViewGifts[Komin_ID].getX()-1000);
    		}
    	}
    }
    @SuppressWarnings("deprecation")
	public void OnOpponentDeath(){
    	if(isRunning){
    	if(deadBlendzior){
    		arrayofImages[Blendzior_ID].setY(arrayofImages[Blendzior_ID].getY() + 3);
    		BlendziorAnimation.stop();
    		arrayofImages[Blendzior_ID].setBackgroundDrawable(deadBlendziorDrawable);
    		if(arrayofImages[Blendzior_ID].getX()<-60){
    			deadBlendzior=false;
    			arrayofImages[Blendzior_ID].setBackgroundDrawable(BlendziorAnimation);
    			BlendziorAnimation.start();
    		
    		}
    	}
    	if(deathSanta){
    		arrayofImages[0].setY(arrayofImages[0].getY() + 4);
    		arrayofImages[0].setX(arrayofImages[0].getX() - 2);
    		staticAnimation.stop();
    	}
      }
    }
    public void moveBlendziors(){
        if(isRunning){
        	if(POINTS/6 > 10){
        		
         		arrayofImages[Blendzior_ID].setVisibility(ImageView.VISIBLE);
        		int dx = (int)arrayofImages[Blendzior_ID].getX();
        		int dy= ds;
        		BlendziorAnimation.start();
        	    if(!deadBlendzior)
        	    	arrayofImages[Blendzior_ID].setY(dy);
        		if(dx<-60){
        			ds = 80 + generator.nextInt(190);
        			dx = 787;
        			deadBlendzior = false;
        			arrayofImages[Blendzior_ID].setX(dx);
        			arrayofImages[Blendzior_ID].setY(ds);
        		}
        		arrayofImages[Blendzior_ID].setX(dx-BlendziorStep);
        		//śmierć Blendziora
        		if(ImageViewGifts[indexforgifts]!= null)
        		for(int i = 0; i < MAX_PRESENTS ;i++)//prezentow jest max 50
        			if(ImageViewGifts[i]!= null)
            	if(		Adiff(ImageViewGifts[i].getX() , arrayofImages[Blendzior_ID].getX()
            			+(float)arrayofImages[Blendzior_ID].getWidth()/6.0)<MIN_BLENDZIOR_DIST &&
            			arrayofImages[Blendzior_ID].getY() > ImageViewGifts[i].getY()  &&
            			Adiff(arrayofImages[Blendzior_ID].getY() , ImageViewGifts[i].getY())< 34){
            		
            		deadBlendzior = true;

            	}
        	  }
        	}
        }
    public void movePidgotto(){
    if(isRunning){
    	if(POINTS/6 > 10){
    		
     		arrayofImages[Pidgeot_ID].setVisibility(ImageView.VISIBLE);
    		int dx = (int)arrayofImages[Pidgeot_ID].getX();
    		int dy= dsa;
    		PidgeotAnimation.start();
    	    if(!deadBlendzior)
    	    	arrayofImages[Pidgeot_ID].setY(dy);
    		if(dx<-60){
    			dsa = 75 + generator.nextInt(170);
    			dx = 987;
    			deadBlendzior = false;
    			arrayofImages[Pidgeot_ID].setX(dx);
    			arrayofImages[Pidgeot_ID].setY(dsa);
    			zajebanyPrezent = false;
    		
    		}
    		arrayofImages[Pidgeot_ID].setX(dx-BlendziorStep);
    		//śmierć Blendziora
    		if(ImageViewGifts[indexforgifts]!= null)
    		for(int i = 0; i < MAX_PRESENTS ;i++)//prezentow jest max 50
    			if(ImageViewGifts[i]!= null)
        	if(		Adiff(ImageViewGifts[i].getX() , arrayofImages[Pidgeot_ID].getX()
        			+(float)arrayofImages[Pidgeot_ID].getWidth()/6.0)<MIN_BLENDZIOR_DIST &&
        			arrayofImages[Pidgeot_ID].getY() > ImageViewGifts[i].getY()  &&
        			Adiff(arrayofImages[Pidgeot_ID].getY() , ImageViewGifts[i].getY())< 34){
        		//	if(ImageViewGifts[indexforgifts].getY() < arrayofImages[Blendzior_ID].getY()){
        		//TODO: 
        		//TODO:	
        		//TODO:test catch the present 
//        			deadBlendzior = true;
        		ref = ImageViewGifts[i];
        		ref.setX(arrayofImages[Pidgeot_ID].getX());
        		ref.setY(arrayofImages[Pidgeot_ID].getY());
        		zajebanyPrezent = true;
        	}
    		if(ref != null && zajebanyPrezent){
    			ref.setX(arrayofImages[Pidgeot_ID].getX());
        		ref.setY(arrayofImages[Pidgeot_ID].getY());
        		
    		}
    		}
    	}
    }
    /**
     * Ustawia liczbe żyć Mikołaja
     */
    public void BlendziorHit() {
    	if(isRunning){
    	NumberOfLives++;
    	if(NumberOfLives==2){
	    	arrayofImages[Hearts_ID].setImageBitmap(bitmap_heart3);			  
		  }
		  else if(NumberOfLives==4){
	    	arrayofImages[Hearts_ID].setImageBitmap(bitmap_heart2);
		  }
		  else if(NumberOfLives==6){
    	arrayofImages[Hearts_ID].setImageBitmap(bitmap_heart1);
		  }
		  else if(NumberOfLives==8){
			  arrayofImages[Hearts_ID].setImageBitmap(bitmap_heart0);
			  deathSanta = true;
			  KorwinGameOver.setImageResource(R.drawable.over);
			  KorwinGameOver.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
			            LayoutParams.WRAP_CONTENT));
			  KorwinGameOver.setVisibility(ImageView.VISIBLE);
			  lay.addView(KorwinGameOver);
			}
    	}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void StopGame(){}
    public void StartGame(){
    	  deathSanta=false;
    	  NumberOfLives=0;
    	  arrayofImages[Hearts_ID].setImageBitmap(bitmap_heart4);
		  arrayofImages[0].setX(50);
		  arrayofImages[0].setY(50);
		  staticAnimation.start();
    	KorwinGameOver.setVisibility(ImageView.GONE);
    }
    
    public void ExitGame(){
    	finishAffinity();
    	Toast.makeText(getApplicationContext(), "Bye!", Toast.LENGTH_SHORT).show();
    	System.exit(0);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_pause) {
        	isRunning = (isRunning) ? false : true;
        	Context context = getApplicationContext();
    		int duration = Toast.LENGTH_SHORT;
    		CharSequence text = "Gra wznowiona.";
    		if(!isRunning)
    			text = "Pauza";
    		Toast.makeText(context, text, duration).show();
        	return true;
        }
        else if (id == R.id.action_start) {
        	StartGame();
        	isRunning = true;
        	Context context = getApplicationContext();
    		CharSequence text = "Misja 1: zbierz 10 prezentów.";
    		int duration = Toast.LENGTH_LONG;
    		Toast.makeText(context, text, duration).show();
			
        	return true;
        }
        
        else if (id == R.id.action_sound_on_off) {
        	SoundIsOn = (SoundIsOn) ? false : true;
        	return true;
        }
        else if (id == R.id.action_exit) {
        	StopGame();
        	ExitGame();
        	return true;
        }
        else if (id == R.id.action_main_menu) {
        	StopGame();
        	setContentView(R.layout.activity_main);
        	return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    public void START(View v){
    	setContentView(lay);
    }
    public void OPTIONS(View v){}
    public void EXIT(View v){
    	ExitGame();
    }


}


