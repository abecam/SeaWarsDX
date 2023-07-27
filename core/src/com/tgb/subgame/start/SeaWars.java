package com.tgb.subgame.start;

import java.io.File;

// From PulpCore BubbleMark implementation. See http://www.bubblemark.com/

import com.tgb.subengine.RenderingManager;
import com.tgb.subengine.gamesystems.StateManager;
import com.tgb.subengine.UtilsPC;
import com.tgb.subgame.MapKeeper2;
import com.tgb.subgame.TacticalMapPC;
import com.tgb.subgame.TacticalMapPCSubs;
import com.tgb.subgame.ScriptedMap;
import com.tgb.subgame.DrawMap;
import com.tgb.subgame.LevelKeeper;
import com.tgb.subgame.StrategicMap;
import com.tgb.subgame.PrepareRandomMap;
import com.tgb.subgame.WonMap;
import com.tgb.subgame.TutorialMap1;
import com.tgb.subgame.TutorialMap2;
import com.tgb.subgame.LevelScoreMap;
import com.tgb.subgame.SimMusic;

import pulpcore.animation.Bool;
import pulpcore.animation.Fixed;
import pulpcore.image.CoreGraphics;
import pulpcore.image.CoreImage;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.Button;
import pulpcore.sprite.Label;
import pulpcore.sprite.Group;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.Slider;
import pulpcore.sprite.Sprite;
import pulpcore.Input;
import pulpcore.Stage;
import pulpcore.Assets;
import pulpcore.image.BlendMode;

public class SeaWars extends Scene2D {
    
    Bool pixelSnapping = new Bool(false);
    Fixed frameRate = new Fixed();
    Slider mySlider;
    Button speakerButton;
    Button noteButton;
    
    RenderingManager myRenderingManager;
    com.tgb.subengine.RendererPulpeC myRenderer;
    
    transient TacticalMapPC myMap;
    transient TacticalMapPCSubs mySubMap;
    transient DrawMap myDrawMap;
    transient StrategicMap myStrategicMap;
    transient ScriptedMap myScriptedMap;
    transient PrepareRandomMap myPrepareRandomMap;
    transient WonMap myWonMap;
    transient TutorialMap2 myTutorialMapPC;
    transient TutorialMap1 myTutorialMap1;
    transient LevelScoreMap myLevelScoreMap;
    
	StateManager theStateManager;
	
	SimMusic mySimMusic; // Player of streamed MP3
	
	boolean notInitialised=true;
	
	FilledSprite backgrd ;
	Label loadingLabel ;
	Group loadingFillingText ;
	double lFTwidth,lFTheight;
	static final String lineOfShakespeareText = "\"You do look, my son, in a moved sort,\nAs if you were dismay'd: be cheerful, sir.\nOur revels now are ended. These our actors,\nAs I foretold you, were all spirits, and\nAre melted into air, into thin air:\nAnd, like the baseless fabric of this vision,\nThe cloud-capp'd towers, the gorgeous palaces,\nThe solemn temples, the great globe itself,\nYea, all which it inherit, shall dissolve,\nAnd, like this insubstantial pageant faded,\nLeave not a rack behind. We are such stuff\nAs dreams are made on; and our little life\nIs rounded with a sleep. Sir, I am vex'd;\nBear with my weakness; my old brain is troubled:\nBe not disturb'd with my infirmity:\nIf you be pleased, retire into my cell,\nAnd there repose: a turn or two I'll walk,\nTo still my beating mind.\"\n\n    William Shakespeare, The Tempest,\n    From The Project Gutenberg (www.gutenberg.org)";
	
	int timePassed=0;
	
	boolean hidden=false;
//	CoreImage myImage;
//	CoreImage levelImage;
	
//	FlatSpritePC background;
	
    @Override
    public void load() {
    	
    	this.setDirtyRectanglesEnabled(false);
    	this.setMaxElapsedTime(5000);

    	//Assets.addCatalog("SeaWars.zip", UtilsPC.getURLInputStream(UtilsPC.getResource("SeaWars.zip")));
    	Assets.addCatalogFromFileSystem("SeaWars.zip", new File("Assets/Sub/"));
    	
    	backgrd = new FilledSprite(20, 20, 960, 760, 0x55ffffff); 
    	add(backgrd);
    	loadingLabel = new Label("Loading, please wait", 400, 350);
    	loadingLabel.setAnchor(Sprite.WEST);
    	add(loadingLabel);
    	loadingFillingText = Label.createMultilineLabel(lineOfShakespeareText, 500, 560);
    	loadingFillingText.setAnchor(Sprite.CENTER);
    	lFTwidth=loadingFillingText.width.get();
    	lFTheight=loadingFillingText.height.get();
    	loadingFillingText.setSize(0.01*lFTwidth, 0.01*lFTheight);
    	add(loadingFillingText);
    	
    	myRenderer = com.tgb.subengine.RendererPulpeC.getInstance();
		myRenderingManager=RenderingManager.getInstance();
        
		
//    	Assets.addCatalog("Sub.zip", UtilsPC.getURLInputStream(UtilsPC.getResource("~GBT/images/Sub.zip")));
//	
//        //add(new ImageSprite(backImage2,0,0,Stage.getWidth(),Stage.getHeight()));
//        
////        mySlider = new Slider("particles.png", "HourHand.png", 0, 80);
////
////        add(mySlider);
//    	remove(loadingLabel);
//    	remove(backgrd);
//    	
//        // Add fps display
//        Label frameRateLabel = new Label("%.1f fps", 5, 5);
//        frameRateLabel.setFormatArg(frameRate);
//        add(frameRateLabel);     
//        
//        CoreImage imagesSpeaker[] = new CoreImage[]{ CoreImage.load("Speaker.png"),
//        		CoreImage.load("SpeakerPreOn.png"),
//        		CoreImage.load("SpeakerPre2On.png"),
//        		CoreImage.load("SpeakerOff.png"),
//        		CoreImage.load("SpeakerPre2.png"),
//        		CoreImage.load("SpeakerPre.png")
//        };
//        speakerButton = new Button(imagesSpeaker, 930, 750,true);
//
//        add(speakerButton);
//        
//        CoreImage imagesNote[] = new CoreImage[]{ CoreImage.load("Note.png"),
//        		CoreImage.load("NoteSel2.png"),
//        		CoreImage.load("NoteSel3.png"),
//        		CoreImage.load("NoteOff.png"),
//        		CoreImage.load("NoteOffSel2.png"),
//        		CoreImage.load("NoteOffSel.png")
//        };
//        noteButton = new Button(imagesNote, 970, 750,true);
//
//        add(noteButton);
//
//        try
//    	{
//    		myRenderer = com.tgb.subengine.gfxentities.RendererPulpeC.getInstance();
//    		myRenderingManager=RenderingManager.getInstance();
//    		
//    		myMap=new com.tgb.subgame.TacticalMapPC();
//    		myDrawMap=new com.tgb.subgame.DrawMap();
//    		myStrategicMap=new com.tgb.subgame.StrategicMap();
//    		myScriptedMap=new com.tgb.subgame.ScriptedMap();
//    		myPrepareRandomMap=new com.tgb.subgame.PrepareRandomMap();
//    		myTutorialMapPC = new com.tgb.subgame.TutorialMap2();
//    		myTutorialMap1 = new com.tgb.subgame.TutorialMap1();
//    		myWonMap=new WonMap();
//    		myLevelScoreMap = new com.tgb.subgame.LevelScoreMap();
//
//    		theStateManager=StateManager.getInstance();
//
//    		theStateManager.addPart(myMap);
//    		theStateManager.addPart(myDrawMap);
//    		theStateManager.addPart(myStrategicMap);
//    		theStateManager.addPart(myScriptedMap);
//    		theStateManager.addPart(myPrepareRandomMap);
//    		theStateManager.addPart(myWonMap);
//    		theStateManager.addPart(myTutorialMapPC);
//    		theStateManager.addPart(myTutorialMap1);
//    		theStateManager.addPart(myLevelScoreMap);
//    		
//    		mySimMusic = SimMusic.getInstance();
//    		
//    		theStateManager.startPart(myScriptedMap.getStateName());
//    		
//    		//mySimMusic.playFile();
//    		//theStateManager.startPart(myStrategicMap.getStateName());
//
//    	}
//    	catch (Exception e)
//    	{
//    		e.printStackTrace();
//    	}
    }

    Thread worker; 
    int posInText=0;
    double sizeFT=0.01;
    String endString="...";
	@Override
	public synchronized void update(int elapsedTime) {   
		if (Input.isDown(Input.KEY_9))
		{
			hidden=!hidden;
		}
		if (notInitialised)
		{
			//if (timePassed > 15)
			{
				loadingFillingText.setSize(sizeFT*lFTwidth, sizeFT*lFTheight);
				
				if (sizeFT < 1)
				{
					sizeFT+=0.0003;
				}
				else
				{
					sizeFT=1;
				}
//				loadingFillingText = Label.createMultilineLabel(lineOfShakespeareText.substring(0,posInText), 500, 450);
//				if (posInText < lineOfShakespeareText.length())
//				{
//					posInText++;
//				}
			}
			if (timePassed > 15)
			{
				loadingLabel.setText( "Loading, please wait"+endString.substring(0, posInText) );
				posInText++;
				if (posInText > endString.length())
				{
					posInText=0;
				}
				timePassed=0;
			}
			else
			{
				timePassed++;
			}
		}
		if (notInitialised && worker == null)
		{
			worker = new Thread() {
                @Override
                public void run() {
   
        	        // Add fps display
        	        Label frameRateLabel = new Label("%.1f fps", 5, 5);
        	        frameRateLabel.setFormatArg(frameRate);
        	        add(frameRateLabel);     
        	        
        	        CoreImage imagesSpeaker[] = new CoreImage[]{ CoreImage.load("Speaker.png"),
        	        		CoreImage.load("SpeakerPreOn.png"),
        	        		CoreImage.load("SpeakerPre2On.png"),
        	        		CoreImage.load("SpeakerOff.png"),
        	        		CoreImage.load("SpeakerPre2.png"),
        	        		CoreImage.load("SpeakerPre.png")
        	        };
        	        speakerButton = new Button(imagesSpeaker, 930, 750,true);

        	        add(speakerButton);
        	        
        	        CoreImage imagesNote[] = new CoreImage[]{ CoreImage.load("Note.png"),
        	        		CoreImage.load("NoteSel2.png"),
        	        		CoreImage.load("NoteSel3.png"),
        	        		CoreImage.load("NoteOff.png"),
        	        		CoreImage.load("NoteOffSel2.png"),
        	        		CoreImage.load("NoteOffSel.png")
        	        };
        	        noteButton = new Button(imagesNote, 970, 750,true);

        	        add(noteButton);

        	        try
        	    	{
        	    		
        	    		myMap=new TacticalMapPC();
        	    		mySubMap=new TacticalMapPCSubs();
        	    		myDrawMap=new DrawMap();
        	    		myStrategicMap=new StrategicMap();
        	    		myScriptedMap=new ScriptedMap();
        	    		myPrepareRandomMap=new PrepareRandomMap();
        	    		myTutorialMapPC = new TutorialMap2();
        	    		myTutorialMap1 = new TutorialMap1();
        	    		myWonMap=new WonMap();
        	    		myLevelScoreMap = new LevelScoreMap();

        	    		theStateManager=StateManager.getInstance();

        	    		theStateManager.addPart(myMap);
        	    		theStateManager.addPart(mySubMap);
        	    		theStateManager.addPart(myDrawMap);
        	    		theStateManager.addPart(myStrategicMap);
        	    		theStateManager.addPart(myScriptedMap);
        	    		theStateManager.addPart(myPrepareRandomMap);
        	    		theStateManager.addPart(myWonMap);
        	    		theStateManager.addPart(myTutorialMapPC);
        	    		theStateManager.addPart(myTutorialMap1);
        	    		theStateManager.addPart(myLevelScoreMap);
        	    		
        	    		mySimMusic = SimMusic.getInstance();
        	    		
        	    		remove(loadingLabel);
            	    	remove(loadingFillingText);
            	    	remove(backgrd);
        	    		
        	    		theStateManager.startPart(myScriptedMap.getStateName());
        	    		
        	    		//mySimMusic.playFile();
        	    		//theStateManager.startPart(myStrategicMap.getStateName());
        	    		notInitialised = false;

        	    	}
        	    	catch (Exception e)
        	    	{
        	    		e.printStackTrace();
        	    	}
                }
            };
            worker.start(); 
	
		}	
		
		// Game loop
		if (!notInitialised)
		{
			// Sound on/off
			if (speakerButton.isClicked())
			{
				com.tgb.subgame.unitspc.SndContainer.setSpeakerOn(!speakerButton.isSelected());
			}
			if (noteButton.isClicked())
			{
				if (noteButton.isSelected())
				{
					mySimMusic.stop();
				}
				else
				{
					mySimMusic.playLastFile();
				}
			}
			
			try
			{

				theStateManager.doLoop();
//				if (LevelKeeper.getInstance().loadSprite())
//				{
//				// Add a new foreground !
//				System.out.println("Add a new level");
//				levelImage=LevelKeeper.getInstance().getCurrentLevel();
//				add(new ImageSprite(levelImage,0,0,Stage.getWidth(),Stage.getHeight()));
//				}
				myRenderingManager=RenderingManager.getInstance();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			// Update fps display
			double fps = Stage.getActualFrameRate();
			if (fps >= 0) {
				frameRate.set(fps);
			}
		}
	}
    
    @Override
	public void drawScene(CoreGraphics arg0) {
    	
    	myRenderer.setGraphics(arg0);
    	arg0.setBlendMode(BlendMode.SrcOver());
    	
    	myRenderingManager.setMiddlePos(TacticalMapPC.START_MENU/2);
		//
		// TODO Auto-generated method stub
		
		//super.drawScene(arg0);
    	if (hidden)
		{
    		super.drawScene(arg0);
    		
    		if (!notInitialised)
			{
				myRenderingManager.paint();
			}
		}
		else
		{
			if (!notInitialised)
			{
				myRenderingManager.paint();
			}

			super.drawScene(arg0);
		}
	}

	public void setCapFrameRate(final boolean capFrameRate) {
        invokeLater(new Runnable() {
            public void run() {
                Stage.setFrameRate(capFrameRate ? Stage.DEFAULT_FPS : Stage.MAX_FPS);
            }
        });
    }
    
    public void setPixelSnapping(final boolean pixelSnapping) {
        invokeLater(new Runnable() {
            public void run() {
                SeaWars.this.pixelSnapping.set(pixelSnapping);
            }
        });
    }

	@Override
	public synchronized void unload() {
		mySimMusic.stop();
		MapKeeper2.getInstance().cleanAll();
		MapKeeper2.unload();
		StateManager.unload();
		LevelKeeper.unload();
		System.out.println("Sub-Engine removed");
		// TODO Auto-generated method stub
		super.unload();
		
	}
}
