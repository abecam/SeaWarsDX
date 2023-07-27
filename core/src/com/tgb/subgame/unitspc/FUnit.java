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

import com.tgb.subengine.RenderingManager;
import com.tgb.subengine.gfxentities.*;

import java.awt.Color;

/**
 * A found unit, which has been detected and might be identified
 * @author Alain Becam
 *
 */
public class FUnit
{
	public static final int UNKNOWN=0;
	public static final int BIOLOGICAL=1;
	
	public static final int SUB_UNKNOWN=256; // binary 1.0000.0000
	
	public static final int ALLIED=1;
	
	public static final int SUB_ALLIED=5;
	public static final int SUB_OUR=7;
	public static final int SUB_ENEMY=8;
	
	public static final int BOAT_UNKNOWN=276;
	
	public static final int BOAT_ALLIED=21;
	public static final int BOAT_OUR=23;
	public static final int BOAT_ENEMY=24;
	
	public static final int AIRPLANE_ALLIED=31;
	public static final int AIRPLANE_OUR=33;
	public static final int AIRPLANE_ENEMY=34;
	
	public static final int BASE_ALLIED=81;
	public static final int BASE_OUR=83;
	public static final int BASE_ENEMY=84;
	
	public static final int UNKNOWN_MASK=0x100;
	public static final int UNKNOWN_SEA=0x100 | 50;
	public static final int UNKNOWN_AIR=0x100 | 60;
	
	public static final int PROJ_MASK= 0x200;
	public static final int TORPEDO= 0x201;
	public static final int MISSILE= 0x202;
	
	int type = UNKNOWN;
	
	int typeFaction = UNKNOWN;
	
	int levelDetection=0; // 0: just found, 5: object known, 10: object in view
	
	ProgrammableUnit boatFound=null;
	Submarine subFound=null;
	
	int idPU; // Id of the found programmable unit
	
	double x,y,z; // Position of the detected unit. Only if the unit is not visible
	double angle;
	double depth;
	double speedContact=0;
	boolean speedKnown=false; // Some device cannot find the speed (like sonme satellites)
	boolean speedAccurate=false;
	double timeContact; // When this contact has been found
	
	transient SpritePC ourSprite;
	long idGfxUnit; // Important, as our statut change, we need to keep the id for our gfx, so we can change it
	long idSpeedLine;
	
	int oldGfx=SEL_VOID;
	int newGfx=SEL_UNKNOWN;
	
	SimpleLineFromCenterPC theDirAndSpeedLine;
	
	boolean hidden=false; // Is this unit visible or not ?
	
	public static final int SEL_VOID=-1;
	public static final int SEL_UNKNOWN=0;
	public static final int SEL_ALLIED_50=1;
	public static final int SEL_ALLIED_25=2;
	public static final int SEL_ALLIED=3;
	public static final int SEL_OUR_50=4;
	public static final int SEL_OUR_25=5;
	public static final int SEL_OUR=6;
	public static final int SEL_ENEMY_50=7;
	public static final int SEL_ENEMY=8;
	public static final int SEL_NEUTRAL_50=9;
	public static final int SEL_NEUTRAL=10;
	
	public FUnit()
	{
		gfxSelections.callMeFirst();
		ourSprite=gfxSelections.getOurSprites(SEL_UNKNOWN);
		theDirAndSpeedLine = new SimpleLineFromCenterPC();
		idGfxUnit = RenderingManager.getInstance().addDrawableEntity(ourSprite,38);
		idSpeedLine = RenderingManager.getInstance().addDrawableEntity(theDirAndSpeedLine,39);
	}
	
	public FUnit(boolean hidden)
	{
		this.hidden=hidden;
		if (!hidden)
		{
			gfxSelections.callMeFirst();
			ourSprite=gfxSelections.getOurSprites(SEL_UNKNOWN);
			theDirAndSpeedLine = new SimpleLineFromCenterPC();
			idGfxUnit = RenderingManager.getInstance().addDrawableEntity(ourSprite,38);
			idSpeedLine = RenderingManager.getInstance().addDrawableEntity(theDirAndSpeedLine,39);
		}
	}
	
	
	public void removeMe()
	{
		RenderingManager.getInstance().removeEntity(idGfxUnit, 38);
		RenderingManager.getInstance().removeEntity(idSpeedLine, 39);
	}
	
	public void hideMe()
	{
		ourSprite.invalidate();
		theDirAndSpeedLine.invalidate();
	}
	
	public void showMe()
	{
		ourSprite.validate();
		theDirAndSpeedLine.validate();
	}
	
	public void setPos(double x,double y,double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	/**
	 * @return the x
	 */
	public double getX()
	{
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(double x)
	{
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public double getY()
	{
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(double y)
	{
		this.y = y;
	}
	/**
	 * @return the z
	 */
	public double getZ()
	{
		return z;
	}
	/**
	 * @param z the z to set
	 */
	public void setZ(double z)
	{
		this.z = z;
	}
	/**
	 * @return the boatFound
	 */
	public ProgrammableUnit getBoatFound()
	{
		return boatFound;
	}
	/**
	 * @param boatFound the boatFound to set
	 */
	public void setBoatFound(Boat boatFound)
	{
		this.boatFound = boatFound;
		this.depth=boatFound.getDepth();
	}
	/**
	 * @return the levelDetection
	 */
	public int getLevelDetection()
	{
		return levelDetection;
	}
	/**
	 * @param levelDetection the levelDetection to set
	 */
	public void setLevelDetection(int levelDetection)
	{
		this.levelDetection = levelDetection;

		if (!hidden)
		{
			switch (levelDetection)
			{		
			case 0: 
			case 1: 
			case 2: 
				newGfx = SEL_UNKNOWN;
				break;

			case 3:
				switch (type)
				{
				case SUB_ALLIED:
				case BOAT_ALLIED:
				case BASE_ALLIED:
				case AIRPLANE_ALLIED:
					newGfx= SEL_ALLIED_25;
					break;
				case SUB_OUR:
				case BOAT_OUR:
				case AIRPLANE_OUR:
					newGfx= SEL_OUR_25;
					break;
				case SUB_ENEMY:
				case BOAT_ENEMY:
				case AIRPLANE_ENEMY:
				case MISSILE:
				case TORPEDO:
					newGfx= SEL_ENEMY_50;
					break;
				}
				break;

			case 4:
				switch (type)
				{
				case SUB_ALLIED:
				case BOAT_ALLIED:
				case BASE_ALLIED:
				case AIRPLANE_ALLIED:
					newGfx= SEL_ALLIED_50;
					break;
				case SUB_OUR:
				case BOAT_OUR:
				case BASE_OUR:
				case AIRPLANE_OUR:
					newGfx= SEL_OUR_50;
					break;
				case SUB_ENEMY:
				case BOAT_ENEMY:
				case BASE_ENEMY:
				case AIRPLANE_ENEMY:
				case MISSILE:
				case TORPEDO:
					newGfx= SEL_ENEMY_50;
					break;
				}
				break;

			default:
				switch (type)
				{
				case SUB_ALLIED:
				case BOAT_ALLIED:
				case BASE_ALLIED:
				case AIRPLANE_ALLIED:
					newGfx= SEL_ALLIED;
					break;
				case SUB_OUR:
				case BOAT_OUR:
				case BASE_OUR:
				case AIRPLANE_OUR:
					newGfx= SEL_OUR;
					break;
				case SUB_ENEMY:
				case BOAT_ENEMY:
				case BASE_ENEMY:
				case AIRPLANE_ENEMY:
				case MISSILE:
				case TORPEDO:
					newGfx= SEL_ENEMY;
					break;
				}
			break;
			}
			updateGfx();
		}
	}
	/**
	 * @return the subFound
	 */
	public Submarine getSubFound()
	{
		return subFound;
	}
	/**
	 * @param subFound the subFound to set
	 */
	public void setSubFound(Submarine subFound)
	{
		this.subFound = subFound;
		this.depth=subFound.getDepth();
	}
	/**
	 * @return the timeContact
	 */
	public double getTimeContact()
	{
		return timeContact;
	}
	/**
	 * @param timeContact the timeContact to set
	 */
	public void setTimeContact(double timeContact)
	{
		this.timeContact = timeContact;
	}
	/**
	 * @return the type
	 */
	public int getType()
	{
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type)
	{
		this.type = type;
	}
	
	/**
	 * @return the angle
	 */
	public double getAngle()
	{
		return angle;
	}

	/**
	 * @param angle the angle to set
	 */
	public void setAngle(double angle)
	{
		this.angle = angle;
	}

	/**
	 * @return the speedAccurate
	 */
	public boolean isSpeedAccurate()
	{
		return speedAccurate;
	}

	/**
	 * @param speedAccurate the speedAccurate to set
	 */
	public void setSpeedAccurate(boolean speedAccurate)
	{
		this.speedAccurate = speedAccurate;
	}

	/**
	 * @return the speedContact
	 */
	public double getSpeedContact()
	{
		return speedContact;
	}

	/**
	 * @param speedContact the speedContact to set
	 */
	public void setSpeedContact(double speedContact)
	{
		this.speedContact = speedContact;
	}

	/**
	 * @return the speedKnown
	 */
	public boolean isSpeedKnown()
	{
		return speedKnown;
	}

	/**
	 * @param speedKnown the speedKnown to set
	 */
	public void setSpeedKnown(boolean speedKnown)
	{
		this.speedKnown = speedKnown;
	}
	
	public double getDepth() {
		return depth;
	}

	public void setDepth(double depth) {
		this.depth = depth;
	}
	
	public int getTypeFaction() {
		return typeFaction;
	}

	/**
	 * Simplified faction: enemy or ally
	 * @param typeOwner
	 */
	public void setTypeFaction(int typeOwner) {
		if ((typeOwner & 1) == 1)
		{
			this.typeFaction=1;
		}
		else
		{
			this.typeFaction=0;
		}
	}

	public int getIdPU()
	{
		return idPU;
	}

	public void setIdPU(int idPU)
	{
		this.idPU = idPU;
	}

	public void updateGfx()
	{
		if (!hidden)
		{
			ourSprite.setPos(x, y, z);
			theDirAndSpeedLine.setPos(x, y, 0);

			if (oldGfx != newGfx)
			{
				// Something to change!!!
				// First remove the existing one.
				RenderingManager.getInstance().removeEntity(idGfxUnit,38);
				oldGfx = newGfx;
				// Then add the new one
				ourSprite = gfxSelections.getOurSprites(newGfx);

				idGfxUnit = RenderingManager.getInstance().addDrawableEntity(ourSprite,38);

				theDirAndSpeedLine.setRotation(this.angle);
				if (!speedKnown)
					theDirAndSpeedLine.setSize(10);
				else
					theDirAndSpeedLine.setSize(speedContact);
			}
		}
	}
}
