/*
 * This software is distributed under the MIT License
 *
 * Copyright (c) 2008-2020 Alain Becam
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:

 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
*/
package com.tgb.subgame.unitspc;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import pulpcore.image.CoreFont;
import pulpcore.image.CoreImage;

import com.tgb.subengine.RenderingManager;
import com.tgb.subengine.gameentities.Waypoint;
import com.tgb.subengine.gfxentities.CirclePC;
import com.tgb.subengine.gfxentities.IAttachable;
import com.tgb.subengine.gfxentities.SimpleLinePC;
import com.tgb.subengine.gfxentities.SpritePC;
import com.tgb.subengine.gfxentities.Text;
import com.tgb.subengine.particlessystem.Emitter;
import com.tgb.subengine.particlessystem.ParticlePC;
import com.tgb.subengine.particlessystem.ParticleSpritePC;
import com.tgb.subengine.particlessystem.Particles;
import com.tgb.subgame.GameKeeper;
import com.tgb.subgame.GfxRanks;
import com.tgb.subgame.GfxBuildings;
import com.tgb.subgame.LevelMap;
import com.tgb.subgame.RandomContainer;
import com.tgb.subgame.unitspc.sensors.DeportedRadar;
import com.tgb.subgame.unitspc.sensors.Sensor;
import com.tgb.subgame.unitspc.sensors.SimpleAround;
/**
 * Super-class for the trees,
 * Might burn, be blown, ...
 * @author Alain Becam
 *
 */
public class Trees extends ProgrammableUnit 
{
	public static final int TREE1=-1;
	public static final int TREE2=-2;
	public static final int TREE3=-3;
	public static final int TREE4=-4;
	public static final int TREE5=-5;
	public static final int TREE6=-6;
	public static final int TREE7=-7;
	
	boolean nameNotCreated=true;
	
	double orientation;
	
	double xShift,yShift;
	
	int resistance=4;
	
	int cost;
	
	// The different parts, ok or damaged.
	double baseState=100;
	
	boolean damaged=false;	
	
	double iDead; // to die
	
	transient SpritePC treeBody;
	transient Emitter bubblesEmitter; 

	
	transient SpritePC boatSquare;
	
	transient Particles smokeBlack;
	transient Emitter badSmokeEmitter; 
	
	transient Particles sparks;
	transient Emitter sparksEmitter; 
	
	transient Text nameGfx;
	
	transient long idBody; // Ids to remove the gfx elements.
	transient long idBubbles;
	transient long idBadSmoke;
	transient long idExplosion;
	transient long idBoatSquare;
	transient long idSparks;
	transient long idName;
	
///// To check, might need to be created elsewhere than the constructor (createGfx)
	transient ArrayList<IAttachable> attachedObjects; // Attached object, will be moved and rotate with the entity
	transient AffineTransform transformToApply; // Common affine transform
	
	Point2D.Double onePoint2D ;
	Point2D.Double onePoint2Dtmp ;
	
	int explodeTime=0;
	SpritePC explosion;
	
	
	/**
	 * @param theMap
	 */
	public Trees(LevelMap theMap)
	{
		super(theMap);

		attachedObjects = new ArrayList<IAttachable>();
		transformToApply = new AffineTransform();
		
		onePoint2D = new Point2D.Double();
		onePoint2Dtmp = new Point2D.Double();
		
		SndContainer.callMeFirst();
	}
	
	/**
	 * @param theMap
	 */
	public Trees()
	{
		super();
		
		attachedObjects = new ArrayList<IAttachable>();
		transformToApply = new AffineTransform();
		
		onePoint2D = new Point2D.Double();
		onePoint2Dtmp = new Point2D.Double();
		
		SndContainer.callMeFirst();
	}
	
	@Override
	public void setTypeFaction(int typeFaction)
	{
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * @param depth the depth to set
	 */
	public void setDepth(double depth)
	{
		this.depth = depth;
		int visib=(int )(255+depth);
		if (visib < 0)
			visib = 0;
		if (visib > 255)
			visib = 255;
		if (treeBody != null)
			treeBody.setAlpha(visib);
	}
	
	/**
	 * @return the cost
	 */
	public int getCost()
	{
		return cost;
	}
	/**
	 * @param cost the cost to set
	 */
	public void setCost(int cost)
	{
		this.cost = cost;
	}

	/**
	 * @return the orientation
	 */
	public double getOrientation()
	{
		return orientation;
	}
	/**
	 * @param orientation the orientation to set
	 */
	public void setOrientation(double orientation)
	{
		this.orientation = orientation;
	}
	
	/**
	 * @return the resistance
	 */
	public int getResistance()
	{
		return resistance;
	}
	/**
	 * @param resistance the resistance to set
	 */
	public void setResistance(int resistance)
	{
		this.resistance = resistance;
	}
	
	/**
	 * Return the default first image for the base.
	 * @return the correct image
	 */
	public static CoreImage getImage1ForMe()
	{
		return gfxSprites.getImageBase1();
	}
	
	/**
	 * Return the default first image for the base.
	 * @return the correct image
	 */
	public static CoreImage getImage2ForMe()
	{
		return gfxSprites.getImageBase1();
	}
	
	public void initFields()
	{
		/*
		sensors = new ArrayList<Sensor>();

		attachedObjects = new ArrayList<IAttachable>();
		transformToApply = new AffineTransform();
		
		onePoint2D = new Point2D.Double();
		onePoint2Dtmp = new Point2D.Double();
		*/
	}
	
	public void createName()
	{
		if (nameNotCreated)
		{
			switch (this.type)
			{
				case Trees.TREE1:
					this.name = "Oak";
					break;
				case Trees.TREE2:
					this.name = "Platanus";
					break;
				case Trees.TREE3:
					this.name = "Hugins 3";
					break;
				case Trees.TREE4:
					this.name = "Hugins 3";
					break;
				case Trees.TREE5:
					this.name = "Hugins 3";
					break;
				case Trees.TREE6:
					this.name = "Hugins 3";
					break;
				case Trees.TREE7:
					this.name = "Hugins 3";
					break;
			}
			
			nameNotCreated=false;
		}
	}
	/**
	 * Create the graphics entity
	 */
	public void createGfx(double x, double y, double z, double direction, double speed)
	{
		CoreImage boatImage;
		GfxBuildings.callMeFirst();
		boatImage=GfxBuildings.getImageArbre();
		
		switch (this.type)
		{
			case Trees.TREE1:
				boatImage=GfxBuildings.getImageArbre();
				break;
			case Trees.TREE2:
				boatImage=GfxBuildings.getImageArbre2();
				break;
			case Trees.TREE3:
				boatImage=GfxBuildings.getImageArbre3();
				break;
			case Trees.TREE4:
				boatImage=GfxBuildings.getImageArbre4();
				break;
			case Trees.TREE5:
				boatImage=GfxBuildings.getImageArbre5();
				break;
			case Trees.TREE6:
				boatImage=GfxBuildings.getImageArbre6();
				break;
			case Trees.TREE7:
				boatImage=GfxBuildings.getImageArbre7();
				break;
		}
			
		
		createGfx(boatImage, x, y, z, direction, speed);
	}
	
	/**
	 * Create the graphics entity
	 * If you have more than one torpedo (should be :) ), it is better to use this
	 * method.
	 */
	public void createGfx(CoreImage boatImage, double x, double y, double z, double direction, double speed)
	{	
		initFields();
		
			treeBody = new SpritePC(boatImage);

			if (this.isDead())
			{
				// Destructed tree
				//treeBody.setImageToDraw(gfxSprites.getImageBase2Dest());
			}
			treeBody.setRotation(direction);
			xShift=1;
			yShift=1;
			treeBody.setPos(x, y, z);
			treeBody.setSize(Math.random()*0.4+0.2);
			
			idBody = RenderingManager.getInstance().addDrawableEntity(treeBody,20); // Surface

			boatSquare = new SpritePC(gfxSprites.getSelImage());
			boatSquare.setSize(1.2);
			boatSquare.invalidate();
			treeBody.addAttachedObject(boatSquare);
			idBoatSquare = RenderingManager.getInstance().addDrawableEntity(boatSquare,39);

			nameGfx = new Text();
			nameGfx.setTextToShow(this.getName()+" "+this.getLevel());
			
			CoreFont font = CoreFont.getSystemFont();
			
			int originalWidth = font.getStringWidth(nameGfx.getTextToShow());
			int originalHeight = font.getHeight();

			nameGfx.setSizeRect(originalWidth,originalHeight);
			nameGfx.setPosAttach(0, -20, 0);
			nameGfx.invalidate();
			treeBody.addAttachedObject(nameGfx);
			idName = RenderingManager.getInstance().addDrawableEntity(nameGfx,39);
			
			explosion = new SpritePC(gfxSprites.getImageFlash2());	
			explosion.invalidate();
			explosion.setSize(1);
			explosion.setPosAttach(Math.random()*10-5, Math.random()*8-4, 0);
			treeBody.addAttachedObject(explosion);
			idExplosion = RenderingManager.getInstance().addDrawableEntity(explosion,21);

			ParticleSpritePC blackSmoke= new ParticleSpritePC(gfxSprites.getImageDarkSmoke());

			blackSmoke.setEnergy(10);
			blackSmoke.setSize(0.3);
			blackSmoke.setTimeLeft(5000);
			blackSmoke.setSizeIncDec(1.002);

			blackSmoke.setAlive(true);


			badSmokeEmitter = new Emitter(x, y, Math.PI, 0.4, Emitter.POINT, 4, 600, 0, 0.3, 5000, blackSmoke);
			badSmokeEmitter.setPosAttach(Math.random()*8-4, 0, 0);
			treeBody.addAttachedObject(badSmokeEmitter);
			smokeBlack=new Particles(10,badSmokeEmitter,blackSmoke);
			if ((!damaged) || (dead))
				smokeBlack.invalidate();
//			bubblesEmitter = new Emitter(x, y, 1, 10, 10, 0, Math.PI/4, Emitter.POINT_DIRECTIONNAL, 20, 50, 0, 0.1, 5000);
//			bubbles=new Particles(500,bubblesEmitter,ParticlePC);
//			bubblesEmitter.setPosAttach(40, 1, 0);
//			boatBody.addAttachedObject(bubblesEmitter);

			idBadSmoke =  RenderingManager.getInstance().addParticles(smokeBlack, 20);
//			idBubbles = RenderingManager.getInstance().addParticles(bubbles, 19);

			//ParticlePC spark= new ParticlePC();
			ParticleSpritePC spark= new ParticleSpritePC(gfxSprites.getImageFlash2());

			spark.setEnergy(10);
			spark.setSize(0.3);
			spark.setTimeLeft(5000);
			spark.setSizeIncDec(1.002);
			//spark.setAcc(1.2, 1.2, 1.2);
			//spark.setMyColor(0xffffffFF);
			//spark.setColorAIncDec(-1);

			spark.setAlive(true);

			sparksEmitter = new Emitter(x, y, Math.PI, 0.4, Emitter.POINT, 500, 20, 0, 20, 500, spark);
			sparksEmitter.setPosAttach(Math.random()*8-4, Math.random()*8-4, 0);
			treeBody.addAttachedObject(sparksEmitter);
			sparks=new Particles(2000,sparksEmitter,spark);
			if ((!damaged) || (dead))
				sparks.invalidate();
	}
	
	/**
	 * Update the torpedo
	 * NB: We do not simulate the torpedo here (yet)
	 */
	public void updateMe(double x, double y, double z, double direction, double speed)
	{
		treeBody.setRotation(direction);
		treeBody.setPos(x, y, z);
	}
	
	public void hideMe()
	{
		treeBody.invalidate();
		smokeBlack.invalidate();
	}
	
	public void showMe()
	{
		treeBody.validate();
		if (damaged)
	    	smokeBlack.validate();
	}
	
	/**
	 * Remove the gfx elements
	 *
	 */
	public void removeMe()
	{
		
			RenderingManager.getInstance().removeEntity(idBody, 20);
			RenderingManager.getInstance().removeParticles(idBadSmoke, 20);
			RenderingManager.getInstance().removeEntity(idExplosion, 21);
			RenderingManager.getInstance().removeEntity(idBoatSquare, 39);
			RenderingManager.getInstance().removeParticles(idSparks, 20);
			RenderingManager.getInstance().removeEntity(idName, 39);

			smokeBlack = null;
			
			bubblesEmitter = null; 
			badSmokeEmitter = null;
			sparks = null;
			sparksEmitter = null;
			
			treeBody = null;
			
			boatSquare = null;
			nameGfx = null;
			
			explosion = null;
			
			
			washAllAttachedObjects();
	}
	
	public void removeMeWOBody()
	{
		
			RenderingManager.getInstance().removeParticles(idBadSmoke, 20);
			RenderingManager.getInstance().removeEntity(idExplosion, 21);

			smokeBlack = null;
			bubblesEmitter = null; 
			badSmokeEmitter = null;
			
			explosion = null;

			washAllAttachedObjects();
	}
	
	/* (non-Javadoc)
	 * @see com.tgb.subgame.units.ProgrammableUnit#doUpdate()
	 */
	@Override
	public void doUpdate(double time)
	{
			if (!dead)
			{

				updateAttachedObjects();
			}
			else if (explodeTime < 10)
			{
				explodeTime++;
				explosion.setSize(explosion.getSize()+0.2);
				System.out.println("Explode !!!!");
				//sparks.validate();
				//SndContainer.playExplosionSound();
			}
			else if (iDead > -1)
			{
				explosion.invalidate();
				this.setDepth(iDead--);
				smokeBlack.invalidate();
				
				// Destroyed tree ?
				//treeBody.setImageToDraw(gfxSprites.getImageBase2Dest());
			}
			else
			{
				if (explodeTime == 10)
				{
					//sparks.invalidate();
					removeMeWOBody();
				}
				else
					explodeTime++;
			}
	}


	public int addAttachedObject(IAttachable newObject)
	{
		newObject.setOrientationAttach(this.orientation);

		transformToApply.setToIdentity();
		transformToApply.translate(this.getPosX(), this.getPosY());
		transformToApply.rotate(this.getOrientation(), 0, 0);
		//transformToApply.scale(this.getSize(), this.getSize());
		onePoint2D.setLocation(newObject.getXAttach(), newObject.getYAttach());
		transformToApply.transform(onePoint2D, onePoint2Dtmp);
		newObject.setAbsPos(onePoint2Dtmp.x, onePoint2Dtmp.y,0);
		
		attachedObjects.add(newObject);
		return (attachedObjects.size() - 1);
	}
	
	public void updateAttachedObject(IAttachable objectToUpdate)
	{
		objectToUpdate.setOrientationAttach(this.orientation);

		transformToApply.setToIdentity();
		transformToApply.translate(this.getPosX(), this.getPosY());
		transformToApply.rotate(this.getOrientation(), 0, 0);
		//transformToApply.scale(this.getSize(), this.getSize());
		onePoint2D.setLocation(objectToUpdate.getXAttach(), objectToUpdate.getYAttach());
		transformToApply.transform(onePoint2D, onePoint2Dtmp);
		objectToUpdate.setAbsPos(onePoint2Dtmp.x, onePoint2Dtmp.y,0);
	}
	
	public void updateAttachedObjects()
	{
		for (int iObjects=0;iObjects < attachedObjects.size() ; iObjects++)
		{
			updateAttachedObject(attachedObjects.get(iObjects));
		}
	}

	public void washAllAttachedObjects()
	{
		attachedObjects.clear();
	}

	public void damageBoat(int amount)
	{
		
			if (!dead)
			{
				double dist=Math.random();
//				this.baseState-=RandomContainer.getInstance().myRandom.nextInt(amount)/this.resistance;
//				this.navState-=RandomContainer.getInstance().myRandom.nextInt(amount)/this.resistance;
//				this.dataLinkState-=RandomContainer.getInstance().myRandom.nextInt(amount)/this.resistance;
//
//				this.complement-=5*RandomContainer.getInstance().myRandom.nextInt(amount)/this.resistance;
//				this.energy-=RandomContainer.getInstance().myRandom.nextInt(amount)/this.resistance;

				this.baseState-=dist*amount/this.resistance;
				
				this.complement-=5*Math.random()*amount/this.resistance;
				this.energy-=5*Math.random()*amount/this.resistance;

				
				if ((this.baseState <= 0) && damaged)
				{
					baseState=0;
					dead = true;

					explosion.validate();
					iDead=0;

					theMap.baseDead(this.typeFaction);
					treeBody.invalidate();
					//idSparks =  RenderingManager.getInstance().addParticles(sparks, 20);
				}
				if ((this.baseState < 50) && (!damaged))
				{
					damaged = true;

					smokeBlack.validate();
					sparks.validate();
					
					this.showMe();
				}
			}
	}

	public boolean toRemove()
	{
		if (dead && (explodeTime == 10))
			return true;
		return false;
	}
	@Override
	public void setView(boolean view) {
		// TODO Auto-generated method stub
		super.setView(view);
		
		if (view)
		{
			if (boatSquare != null)
			{
				boatSquare.validate();
			}
		}
		else
		{
			if (boatSquare != null)
			{
				boatSquare.invalidate();
			}
		}
	}

	public boolean isDead() {
		return dead;
	}


	public double getBaseState() {
		return baseState;
	}

	public void setBaseState(int hullState) {
		this.baseState = hullState;
	}


	@Override
	public void setIdBoat(int idBoat) {
		// TODO Auto-generated method stub
		super.setIdBoat(idBoat);
	}
	
	
	/*
	 * Methods for the strategic map
	 */
	
	/**
	 * Can be repaired by a base or a special unit (or anything else)
	 * @param amount
	 */
	public void getRepaired(int amount)
	{
		super.getRepaired(0);
		
		baseState+=amount;
		if (damaged && baseState > 50)
		{
			damaged = false;
		}
		if (baseState> 100)
		{
			baseState=100;
		}
		
		energy+=amount;
		if (energy > energyMax)
		{
			energy=energyMax;
		}
	}
	@Override
	public void hideLabel()
	{
		if (nameGfx != null)
		{
			nameGfx.invalidate();
		}
	}

	@Override
	public void showLabel()
	{
		if (nameGfx != null)
		{
			if (treeBody.isValide())
				nameGfx.validate();
		}
	}

	@Override
	public void doUpdateSM(double time)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void levelUp()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String saveIfNeeded()
	{
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public List<String> loadIfSupported(List<String> myPart)
	{
		// TODO Auto-generated method stub
		return myPart;
	}

	@Override
	public String giveMyId()
	{
		// TODO Auto-generated method stub
		return "";
	}
	
	@Override
	public String getTypeName()
	{
		return this.name;
	}
}
