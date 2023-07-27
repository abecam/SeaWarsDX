/*
 * Created on 17 sep 2008
 */

/**
 * An event on the map: an attack, a detection,...
 * @author Alain Becam 
 */

package com.tgb.subgame;

import com.tgb.subengine.RenderingManager;
import com.tgb.subengine.gfxentities.SpritePC;
import com.tgb.subgame.unitspc.gfxSprites;

public class MapEvents
{
	final static int ATTACK=0;
	final static int CONVOYS=1;
	final static int DETECTION=2;
	final static int BASE_ATTACK=3;
	final static int SURPRISE=4;
	final static int DEFENSE=5;
	final static int BASE_DEFENSE=6;
	
	
	double posX,posY;
	
	int type;
	double duration;
	double durationLeft;
	
	SpritePC eventBody;
	long idBody;
	
	public MapEvents(double newPosX,double newPosY,int newType,double newDuration)
	{
		this.posX=newPosX;
		this.posY=newPosY;
		this.type=newType;
		this.duration=newDuration;
		durationLeft=this.duration;
	}


	public double getPosX()
	{
		return posX;
	}


	public void setPosX(double posX)
	{
		this.posX = posX;
	}


	public double getPosY()
	{
		return posY;
	}


	public void setPosY(double posY)
	{
		this.posY = posY;
	}


	public int getType()
	{
		return type;
	}


	public void setType(int type)
	{
		this.type = type;
	}


	public double getDuration()
	{
		return duration;
	}


	public void setDuration(double duration)
	{
		this.duration = duration;
	}


	public double getDurationLeft()
	{
		return durationLeft;
	}


	public void setDurationLeft(double durationLeft)
	{
		this.durationLeft = durationLeft;
	}
	
	public void startEvent()
	{
		durationLeft = duration;
	}
	
	public boolean updateEvent(double timePassed)
	{
		durationLeft-=timePassed;
		//System.out.println("Duration left "+durationLeft+" - "+timePassed);
		if (durationLeft <= 0)
		{
			removeMeSM();
			return true;
		}
		else
			return false;
	}
	
	public void consumeEvent()
	{
		durationLeft=-1; // Event done. Nothing at the moment, might change... :)
	}
	
	/**
	 * Create the graphics entity
	 * method.
	 */
	public void createGfxSM(double x, double y, double z, double direction, double speed)
	{	
		switch (this.type)
		{
			case ATTACK:
				eventBody = new SpritePC(gfxSprites.getImageConfront());
				break;
			case CONVOYS:
				eventBody = new SpritePC(gfxSprites.getImageExcl());
				break;
			case DEFENSE:
				eventBody = new SpritePC(gfxSprites.getImageExcl());
				break;
			case DETECTION:
				eventBody = new SpritePC(gfxSprites.getImageInt());
				break;
			case BASE_ATTACK:
				eventBody = new SpritePC(gfxSprites.getImageConfront());
				break;
			case BASE_DEFENSE:
				eventBody = new SpritePC(gfxSprites.getImageExcl());
				break;
			case SURPRISE:
				eventBody = new SpritePC(gfxSprites.getImageInt());
				break;
			default:
				eventBody = new SpritePC(gfxSprites.getImageInt());
		}

		eventBody.setRotation(direction);

		eventBody.setPos(x, y, z);
		idBody = RenderingManager.getInstance().addDrawableEntity(eventBody,30); // Surface
	}
	
	/**
	 * Remove the gfx elements
	 *
	 */
	public void removeMeSM()
	{
		RenderingManager.getInstance().removeEntity(idBody, 30);
	}
}
