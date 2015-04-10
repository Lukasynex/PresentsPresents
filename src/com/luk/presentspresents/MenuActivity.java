//package com.luk.presentspresents;
//
//
//
//import java.util.Random;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.AnimationDrawable;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.RelativeLayout.LayoutParams;
//import android.widget.TextView;
//
////TODO:remove redundant code, add 'kominy' and 'blendziors' threads. this may be 
////pokemon-ghost: hunter, gastly or gengar 
//public class MenuActivity extends Activity {
//	public int POINTS=0,RealPoints;
//	int Santa_ID = 0,Blendzior_ID, Komin_ID,Txt_ID, Hearts_ID;
//	int BlendziorStep = 10, Santa_,ds=50;
//	int buttonsize = 100, tilesize = 30,forHeartsWidth=100, forHeartsHeight=30;
//	int giftsize = 35, MIN_BLENDZIOR_DIST=35;
//	int step = 10,zrzucone=0;
//	int elements = 50, index=0;
//	int NumberOfLives = 0;
//	double time = 0.72;
//	boolean IsmoveH = false,IsmoveV = false, IsUp=false, IsLeft = true, deadBlendzior = false,
//			BlendziorHitIsRunning=false,santadeath = false;
//			;
//	
//	static int indexforgifts=0;
//	Random generator = new Random();
//	GiftDown thread=null;
//	  
//	AnimationDrawable staticAnimation = new AnimationDrawable();
//	AnimationDrawable BlendziorAnimation = new AnimationDrawable();
//	ImageView[] arrayofImages = new ImageView[elements];
//	ImageView[] ImageViewGifts = new ImageView[50];
//	TextView txt_instance;
//    Bitmap bitmap_down, bitmap_up, bitmap_right, bitmap_left, bitmap_komin, bitmap_gift, bitmap_blanktile;
//	Bitmap fly001,fly002,fly003,fly004, flydead, bitmap_heart1,bitmap_heart0, bitmap_heart2,bitmap_heart3,bitmap_heart4;
//	Drawable deadBlendziorDrawable;
//	static Bitmap[] staticGiftbitmap = new Bitmap[12];//jest 12 różnych prezentów
//    RelativeLayout lay;
//	@SuppressWarnings("deprecation")
//	@Override
//    protected void onCreate(Bundle savedInstanceState) {
//      super.onCreate(savedInstanceState);
//      setupButtons();
//  	  lay = new RelativeLayout(this);
//      lay.setBackgroundResource(R.drawable.sky_winter);
//      setContentView(lay);
//      //index=0;
//      arrayofImages[index] = new ImageView(this);
//	  setupSantaAnimation();
//	  
//     
//      //creating blendziors
//      ++index;
//      arrayofImages[index] = new ImageView(this);
//	  arrayofImages[index].setId(index);
//	  Blendzior_ID = index;
//      //Skaluję blendziora bo za duży jest :/
//	  Drawable fl01 = new BitmapDrawable(getResources(),fly001);
//	  Drawable fl02 = new BitmapDrawable(getResources(),fly002);
//	  Drawable fl03 = new BitmapDrawable(getResources(),fly003);
//	  Drawable fl04 = new BitmapDrawable(getResources(),fly004);
//	  deadBlendziorDrawable = new BitmapDrawable(getResources(),flydead);
//	  
//	  BlendziorAnimation.addFrame(fl01,100);
//	  BlendziorAnimation.addFrame(fl02,100);
//	  BlendziorAnimation.addFrame(fl03,100);
//	  BlendziorAnimation.addFrame(fl04,100);
//	  BlendziorAnimation.setOneShot(false);
//	  arrayofImages[index].setBackgroundDrawable(BlendziorAnimation);
//	  arrayofImages[index].setX(780);
//      arrayofImages[index].setY(200);
//      arrayofImages[index].setVisibility(ImageView.INVISIBLE);
//      arrayofImages[index].setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
//              LayoutParams.WRAP_CONTENT));
//      lay.addView(arrayofImages[index]);
//      ++index;
//      Komin_ID = index;
//	  setupPixmap(bitmap_komin, 70, 310);
//	  ++index;
//      //setup arrows; arrow up
//      setupPixmap(bitmap_up, 650, 75);
//      ++index;
//      setupPixmap(bitmap_down, 650, 275);
//      //more gift arrow
//      ++index;
//      setupPixmap(bitmap_gift, 0, 0);
//
//      //arrow right
//      ++index;
//	  setupPixmap(bitmap_right, 700, 175);
//      //arrow left
//      ++index;
//	  setupPixmap(bitmap_left, 600, 175);
//      
//      //runningChimney = new KominesAndBlendziors();
//      KominesAndBlendziors.start(this);
//      SantaBias.start(this);
//      OnBlendziorDeath.start(this);
//      txt_instance = new TextView(this);
//      txt_instance.setId(++index);
//      Txt_ID = index;
//      txt_instance.setX(200);txt_instance.setY(0);
//      txt_instance.setText("0");
//      txt_instance.setBackground(new BitmapDrawable(getResources(),bitmap_blanktile));
//      txt_instance.setMaxHeight(tilesize);
//      txt_instance.setMaxWidth(tilesize);
//      ++index;Hearts_ID=index;
//      setupPixmap(bitmap_heart4, 250, 0);
//      lay.addView(txt_instance);
//      setContentView(lay);
//      }
//    public void setupPixmap(Bitmap bmp, int dx, int dy){
//  	  arrayofImages[index] = new ImageView(this);
//  	  arrayofImages[index].setId(index);
//  	  arrayofImages[index].setImageBitmap(bmp);  
//  	  arrayofImages[index].setX(dx);
//  	  arrayofImages[index].setY(dy);  
//  	  arrayofImages[index].setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT));
//        lay.addView(arrayofImages[index]);
//
//    }
//
//	@SuppressWarnings("deprecation")
//	public void setupSantaAnimation(){
//		  arrayofImages[index].setId(index);
//		  Santa_ID = index;
//		  staticAnimation.addFrame(getResources().getDrawable(R.drawable.santa_ride01),150);
//		  staticAnimation.addFrame(getResources().getDrawable(R.drawable.santa_ride02),150);
//		  staticAnimation.addFrame(getResources().getDrawable(R.drawable.santa_ride03),150);
//		  staticAnimation.addFrame(getResources().getDrawable(R.drawable.santa_ride04),150);
//		  staticAnimation.addFrame(getResources().getDrawable(R.drawable.santa_ride05),150);
//		  staticAnimation.setOneShot(false);
//		  arrayofImages[index].setBackgroundDrawable(staticAnimation);
//		  arrayofImages[index].setX(50);
//		  arrayofImages[index].setY(50);
//		  arrayofImages[index].setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
//	            LayoutParams.WRAP_CONTENT));
//		  lay.addView(arrayofImages[index]);
//	  }
//	public void setupButtons(){
//		fly001 = BitmapFactory.decodeResource(getResources(), R.drawable.fly01);
//		fly002 = BitmapFactory.decodeResource(getResources(), R.drawable.fly02);
//		fly003 = BitmapFactory.decodeResource(getResources(), R.drawable.fly03);
//		fly004 = BitmapFactory.decodeResource(getResources(), R.drawable.fly04);
//		
//		fly004 = Bitmap.createScaledBitmap(fly004, 
//				fly001.getWidth()/2, fly001.getHeight()/2, true);
//		fly003 = Bitmap.createScaledBitmap(fly003, 
//				fly001.getWidth()/2, fly001.getHeight()/2, true);
//		fly002 = Bitmap.createScaledBitmap(fly002, 
//				fly001.getWidth()/2, fly001.getHeight()/2, true);
//		flydead = BitmapFactory.decodeResource(getResources(), R.drawable.fly05_death);
//		flydead = Bitmap.createScaledBitmap(flydead, 
//				fly001.getWidth()/2, fly001.getHeight()/2, true);
//		fly001 = Bitmap.createScaledBitmap(fly001, 
//				fly001.getWidth()/2, fly001.getHeight()/2, true);
//		
//		//bi
//		bitmap_down = BitmapFactory.decodeResource(getResources(), R.drawable.downbutton);
//		bitmap_down = Bitmap.createScaledBitmap(bitmap_down, buttonsize,buttonsize,true);
//		bitmap_left = BitmapFactory.decodeResource(getResources(), R.drawable.leftbutton);
//		bitmap_left = Bitmap.createScaledBitmap(bitmap_left, buttonsize,buttonsize,true);
//		bitmap_up = BitmapFactory.decodeResource(getResources(), R.drawable.upbutton);
//		bitmap_up = Bitmap.createScaledBitmap(bitmap_up, buttonsize,buttonsize,true);
//		bitmap_right = BitmapFactory.decodeResource(getResources(), R.drawable.rightbutton);
//		bitmap_right = Bitmap.createScaledBitmap(bitmap_right, buttonsize,buttonsize,true);
//		bitmap_komin = BitmapFactory.decodeResource(getResources(), R.drawable.komin);
//		bitmap_komin = Bitmap.createScaledBitmap(bitmap_komin,90,90,true);
//		bitmap_gift = BitmapFactory.decodeResource(getResources(), R.drawable.fallgift);
//		bitmap_gift = Bitmap.createScaledBitmap(bitmap_gift,buttonsize,buttonsize,true);
//		bitmap_blanktile= BitmapFactory.decodeResource(getResources(), R.drawable.blank);
//		bitmap_blanktile = Bitmap.createScaledBitmap(bitmap_blanktile,tilesize,tilesize,true);
//
//		bitmap_heart1= BitmapFactory.decodeResource(getResources(), R.drawable.heart1);
//		bitmap_heart1 = Bitmap.createScaledBitmap(bitmap_heart1,forHeartsWidth,forHeartsHeight,true);
//		bitmap_heart2= BitmapFactory.decodeResource(getResources(), R.drawable.heart2);
//		bitmap_heart2 = Bitmap.createScaledBitmap(bitmap_heart2,forHeartsWidth,forHeartsHeight,true);
//		bitmap_heart3= BitmapFactory.decodeResource(getResources(), R.drawable.heart3);
//		bitmap_heart3 = Bitmap.createScaledBitmap(bitmap_heart3,forHeartsWidth,forHeartsHeight,true);
//		bitmap_heart4= BitmapFactory.decodeResource(getResources(), R.drawable.heart4);
//		bitmap_heart4 = Bitmap.createScaledBitmap(bitmap_heart4,forHeartsWidth,forHeartsHeight,true);
//		bitmap_heart0= BitmapFactory.decodeResource(getResources(), R.drawable.heart0);
//		bitmap_heart0= Bitmap.createScaledBitmap(bitmap_heart0,forHeartsWidth,forHeartsHeight,true);
//		
//		
//		
//		//bitmapy dla prezentów
//		staticGiftbitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.gift01);
//		staticGiftbitmap[1] = BitmapFactory.decodeResource(getResources(), R.drawable.gift02);
//		staticGiftbitmap[2] = BitmapFactory.decodeResource(getResources(), R.drawable.gift03);
//		staticGiftbitmap[3] = BitmapFactory.decodeResource(getResources(), R.drawable.gift04);
//		staticGiftbitmap[4] = BitmapFactory.decodeResource(getResources(), R.drawable.gift05);
//		staticGiftbitmap[5] = BitmapFactory.decodeResource(getResources(), R.drawable.gift06);
//		staticGiftbitmap[6] = BitmapFactory.decodeResource(getResources(), R.drawable.gift07);
//		staticGiftbitmap[7] = BitmapFactory.decodeResource(getResources(), R.drawable.gift08);
//		staticGiftbitmap[8] = BitmapFactory.decodeResource(getResources(), R.drawable.gift09);
//		staticGiftbitmap[9] = BitmapFactory.decodeResource(getResources(), R.drawable.gift10);
//		staticGiftbitmap[10] = BitmapFactory.decodeResource(getResources(), R.drawable.gift11);
//		staticGiftbitmap[11] = BitmapFactory.decodeResource(getResources(), R.drawable.gift12);
//		
//		//scale to giftsize
//		for(int i = 0; i < 12; i++)
//			staticGiftbitmap[i] = Bitmap.createScaledBitmap(staticGiftbitmap[i], giftsize,giftsize,true);
//
//	}
//	public void moveChimney(){
//		int dx = (int)arrayofImages[Komin_ID].getX() - step;
//		if(dx < 0)
//			arrayofImages[Komin_ID].setX(750);
//		else
//			arrayofImages[Komin_ID].setX(dx);
//	}
//	public int centerX(ImageView view){
//		return (int)(view.getX()+view.getWidth()/2.0);
//	}
//	public int centerY(ImageView view){
//		return (int)(view.getY()+view.getHeight()/2.0);
//	}
//	//TODO: 
//    public void moveThemAll(){
//    	//starts Santa animation
//    	staticAnimation.start();
//    	ImageView santa = arrayofImages[0];
//    	ImageView blend = arrayofImages[Blendzior_ID];
//    	
//    	boolean plusY = generator.nextBoolean();
//    	if(plusY)
//    		santa.setX(santa.getX()+1);
//    	else
//    		santa.setX(santa.getX()-1);
//    	
//    	if(Adiff(centerX(santa),centerX(blend)) < 30 && 
//    	   Adiff(centerY(santa),centerY(blend)) < (int)(santa.getHeight()/2.0)){
//    		PerformDelay blendziorPunch = new PerformDelay();
//    		blendziorPunch.start(this, 1, 5000);
//    	}
//    	
//    	if(arrayofImages[0].getY() > 0 && arrayofImages[0].getY() < 200
//    			&& arrayofImages[0].getX() > -20 && arrayofImages[0].getX() < 700){
//	    	if(IsmoveV){
//	    		if(IsUp)
//	    			arrayofImages[0].setY(arrayofImages[0].getY()-12);
//	    		else 
//	    			arrayofImages[0].setY(arrayofImages[0].getY()+12);
//	    	}
//	    	if(IsmoveH){
//	    		if(IsLeft)
//	    			arrayofImages[0].setX(arrayofImages[0].getX()-12);
//	    		else if(!IsLeft)
//	    			arrayofImages[0].setX(arrayofImages[0].getX()+12);
//	    		
//	    		}
//    	}
//    }
//    public double Adiff(double x,double y){
//    	return (x>y) ? x-y : y-x;
//    }
//    public double euclides(double y, double x){
//    	return (double)Math.sqrt(x*x+y*y);
//    }
//    @Override
//    public boolean onTouchEvent(MotionEvent event){
//    	ImageView santa = (ImageView)findViewById(0);
////TODO: poprawić położenie przycisków		
//        switch (event.getActionMasked()) {
//        case MotionEvent.ACTION_UP:{
//        	IsmoveV=IsmoveH=false;
//        	break;
//        }
//        case MotionEvent.ACTION_DOWN:{
//        	if(event.getX()<100){
//        		if(zrzucone<50)
//        		montujGifta();
//        		
//        	}
//        	if(event.getX()>650 && event.getX()<750 &&event.getY()>275+buttonsize){
//        		//move down
//        		santa.setY(santa.getY()+step);
//        		IsmoveV=true;
//       			IsmoveH=false;
//       			IsUp=false;
//       		}
//        	//move up
//        	else if(event.getX()>650 && event.getX()<750 &&	event.getY()<175+buttonsize){
//        		santa.setY(santa.getY()-step);
//        		IsmoveV=true;
//        		IsmoveH=false;
//        		IsUp=true;
//        		
//        	}//right move
//        	else if(event.getX()>700){
//    			santa.setX(santa.getX()+step);
//    			IsmoveH=true;
//    			IsmoveV=false;
//        		IsLeft=false;
//        	}
//        	//left move
//        	else if(event.getX()>600 && event.getX()<700){
//        		santa.setX(santa.getX()-step);
//        		IsmoveH=true;
//        		IsmoveV=false;
//        		IsLeft=true;
//        	}
//        }
//        }
//        return true;
//    }
//    public void montujGifta(){
//    	++zrzucone;
//      ImageView santa = (ImageView)findViewById(Santa_ID );
//	  ++indexforgifts;
//	  ImageViewGifts[indexforgifts] = new ImageView(this);
//  	  ImageViewGifts[indexforgifts].setId(index+indexforgifts+1);
//  	  ImageViewGifts[indexforgifts].setImageBitmap(staticGiftbitmap[indexforgifts%(staticGiftbitmap.length)]);
//  	  ImageViewGifts[indexforgifts].setX(santa.getX()+40);
//  	  ImageViewGifts[indexforgifts].setY(santa.getY()+30);      
//      ImageViewGifts[indexforgifts].setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT));
//      ImageViewGifts[indexforgifts].setVisibility(ImageView.VISIBLE);
//      lay.addView(ImageViewGifts[indexforgifts]);
//      ImageViewGifts[indexforgifts].setX(santa.getX()+40);
//      ImageViewGifts[indexforgifts].setY(santa.getY()+30);      
//      thread = new GiftDown();
//      thread.start(this, indexforgifts); 
//      
//  	}
//    public void CollectPoints(){
//    	++POINTS;	
//		txt_instance.setText(""+POINTS/7);
//    }
//    public void PresentFall(int id){
//    		ImageViewGifts[id].setY(
//    		ImageViewGifts[id].getY() + 10);      
//    		//gdy prezent spada, może trafić do komina, ustawiamy znikanie
//    		if(	euclides(
//    			Adiff(ImageViewGifts[id].getX(), 
//    					arrayofImages[Komin_ID].getX()+(float)arrayofImages[Komin_ID].getWidth()/2.0),
//    			Adiff(ImageViewGifts[id].getY(), 
//    					arrayofImages[Komin_ID].getY()) ) < 50 ){
//    			ImageViewGifts[id].setVisibility(ImageView.GONE);
//    			//TODO:op!
//
//    			PerformDelay a = new PerformDelay();
//        		a.start(this, 0, 1000);
//    		}
//    		//gdy prezent spada, może trafić na głowę Blendziora, ustawiamy znikanie
//    		if(	euclides(
//        			Adiff(ImageViewGifts[id].getX(), 
//        					arrayofImages[Blendzior_ID].getX()+(float)arrayofImages[Blendzior_ID].getWidth()/2.0),
//        			Adiff(ImageViewGifts[id].getY(), 
//        					arrayofImages[Blendzior_ID].getY()) ) < MIN_BLENDZIOR_DIST ){
//        			ImageViewGifts[id].setVisibility(ImageView.GONE);
//        			//nie dopuszczamy do sytuacji żeby niewidzialny prezent trafił do komina
//        			ImageViewGifts[id].setY(ImageViewGifts[Komin_ID].getY()-1000);
//    		}
//    }
//    @SuppressWarnings("deprecation")
//	public void OnOpponentDeath(){
//    	if(deadBlendzior){
//    		arrayofImages[Blendzior_ID].setY(arrayofImages[Blendzior_ID].getY() + 3);
//    		BlendziorAnimation.stop();
//    		arrayofImages[Blendzior_ID].setBackgroundDrawable(deadBlendziorDrawable);
//    		if(arrayofImages[Blendzior_ID].getX()<-40){
//    			deadBlendzior=false;
//    			arrayofImages[Blendzior_ID].setBackgroundDrawable(BlendziorAnimation);
//    			BlendziorAnimation.start();
//    			
//    		}
//    	}
//    	if(santadeath){
//    		arrayofImages[0].setY(arrayofImages[0].getY() + 4);
//    		arrayofImages[0].setX(arrayofImages[0].getX() - 2);
//    		staticAnimation.stop();
//    	}
//    }
//    public void moveBlendziors(){
//    	
//    	if(POINTS > 10){
//    		time += 0.05f;
//    		
//    		arrayofImages[Blendzior_ID].setVisibility(ImageView.VISIBLE);
//    		int dx = (int)arrayofImages[Blendzior_ID].getX();
//    		int dy= 200 + ds;
//    		BlendziorAnimation.start();
//    	    if(!deadBlendzior)
//    	    	arrayofImages[Blendzior_ID].setY(dy);
//    		if(dx<-40){
//    			ds = (int)(100*Math.sin(time));
//    			dx = 777;
//    			deadBlendzior = false;
//    		}
//    		arrayofImages[Blendzior_ID].setX(dx-BlendziorStep);
//    		//śmierć Blendziora
//    		if(ImageViewGifts[indexforgifts]!= null)	
//    		for(int i = indexforgifts; i < 50 ;i++)//prezentow jest max 50
//    			if(ImageViewGifts[i]!= null)
//        	if(		Adiff(ImageViewGifts[i].getX() , arrayofImages[Blendzior_ID].getX()
//        			+(float)arrayofImages[Blendzior_ID].getWidth()/6.0)<MIN_BLENDZIOR_DIST &&
//        			arrayofImages[Blendzior_ID].getY() > ImageViewGifts[i].getY()  &&
//        			Adiff(arrayofImages[Blendzior_ID].getY() , ImageViewGifts[i].getY())< 34){
//        		//	if(ImageViewGifts[indexforgifts].getY() < arrayofImages[Blendzior_ID].getY()){
//        			deadBlendzior = true;
//        		}
//    		
//    		//kontakt Blendziora z Mikołajem
////    		int SantaPosX =(int) ImageViewGifts[Santa_ID].getX();
////    		int BlendziorPosX =(int) arrayofImages[Blendzior_ID].getX();
////    		int SantaPosY =(int)ImageViewGifts[Santa_ID].getY();
////    		int BlendziorPosY =(int)(arrayofImages[Blendzior_ID].getY()+(float)arrayofImages[Blendzior_ID].getHeight()/2.0);
//////    		
////    		if(	euclides(Adiff(SantaPosX,BlendziorPosX),Adiff(SantaPosY,BlendziorPosY)) < 1 ){
////    			PerformDelay lostheart = new PerformDelay();
////    			lostheart.start(this, 1, 500);
////    		}
//    	}
//    }
//    /**
//     * Ustawia liczbe żyć Mikołaja
//     */
//    @SuppressWarnings("deprecation")
//	public void BlendziorHit() {
//    	NumberOfLives++;
//
//    	if(NumberOfLives==2){
//	    	arrayofImages[Hearts_ID].setImageBitmap(bitmap_heart3);			  
//		  }
//		  else if(NumberOfLives==4){
//	    	arrayofImages[Hearts_ID].setImageBitmap(bitmap_heart2);
//			  
//		  }
//		  else if(NumberOfLives==6){
//    	arrayofImages[Hearts_ID].setImageBitmap(bitmap_heart1);
//		  }
//		  else if(NumberOfLives==8){
//			  arrayofImages[Hearts_ID].setImageBitmap(bitmap_heart0);
//			  ImageView a = new ImageView(this);
//			  santadeath = true;
//			  a.setImageResource(R.drawable.over);
//   			  lay.addView(a);
//		  }
//    }
//    
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//    
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//	
//}
//
