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

package com.tgb.subgame.unitspc.sensors;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.tgb.subgame.LevelMap;
import com.tgb.subgame.unitspc.Airplane;
import com.tgb.subgame.unitspc.Base;
import com.tgb.subgame.unitspc.Boat;
import com.tgb.subgame.unitspc.FUnit;
import com.tgb.subgame.unitspc.FUnitWatcher;
import com.tgb.subgame.unitspc.ProgrammableUnit;
import com.tgb.subgame.unitspc.Submarine;
import com.tgb.subgame.unitspc.Missile;
import com.tgb.subgame.unitspc.Torpedo;
import com.tgb.subengine.gfxentities.IAttachable;

/**
 * Super-class for a sensor.
 * <br>Two cases:
 * <br>On an unit:<br>
 *    - Typically a sensor is attached to the unit, and the graphics for the sensor attached to this last.<br>
 *    - The sensor might move by its own (rotate for a radar) and it will determine how the detection is done.<br>
 * <br>On the land: the sensor might not be attached.
 * @author Alain Becam
 *
 */
public abstract class Sensor implements IAttachable, ISignalCreator
{
	int myType;
	
	public static final int RADAR=0;
	public static final int SONAR=1;
	public static final int DEPORTEDRADAR=3;

	public static final int PERISCOPE = 4;
	
	LevelMap theMap; // The tactical map, so we know who is where.
	ArrayList<ArrayList<FUnit>> foundUnits; // Nb of last Detection as a key.
	ArrayList<FUnit> currentFUnits;
	ArrayList<FUnit> tmpFUnits;
	int currentTurn=0; // Detection turn.
	int allowedDepth=1; // How many turns do we keep

	double x,y,z; // Our position
	double orientation=0; // Our orientation, for sensors such as sonars or radars
	double angle=Math.PI/2; // Angle of detection
	double power=1; // Power of this sensor.
	
	double orientationAttach=0; // Orientation *relative* to the object we are attached to
	double xAttach,yAttach,zAttach=0; 
	
	ArrayList<IAttachable> attachedObjects; // Attached object, will be moved and rotated with the entity
	AffineTransform transformToApply; // Common affine transform
	
	Point2D.Double onePoint2D ;
	Point2D.Double onePoint2Dtmp ;
	
	Signal ourSignal;
	boolean existingSignal=false; // Is a signal currently emitted?
	
	ProgrammableUnit receiver=null;
	
	boolean dead=false;
	boolean active=true;
	
	int idOwner; // Who own it.
	
	/* 
	 * From the tactical map
	 */
	ArrayList<Submarine> ourSubs;
	ArrayList<Boat> ourBoats;
	ArrayList<Submarine> alliesSubs;
	ArrayList<Boat> alliesBoats;
	ArrayList<Submarine> enemiesSubs;
	ArrayList<Boat> enemiesBoats;
	ArrayList<Submarine> neutralSubs;
	ArrayList<Boat> neutralBoats;
	ArrayList<Missile> missiles;
	ArrayList<Torpedo> torpedoes;
	
	ArrayList<Airplane> ourAirplanes;
	ArrayList<Airplane> alliesAirplanes;
	ArrayList<Airplane> enemiesAirplanes;
	ArrayList<Airplane> neutralAirplanes;
	
	ArrayList<Base> ourBases;
	ArrayList<Base> alliesBases;
	ArrayList<Base> enemiesBases;
	ArrayList<Base> neutralBases;
	
	ArrayList<Signal> signals;
	
	boolean hiddenFUnits=false; // Do we show our units.
	
	public int typeSensor=-1;
	
	
	public boolean isHiddenFUnits() {
		return hiddenFUnits;
	}

	public void setHiddenFUnits(boolean hiddenFUnits) {
		this.hiddenFUnits = hiddenFUnits;
	}
	
	public abstract void activate();

	
	public abstract void desactivate();

	
	public boolean isActive()
	{
		return active;
	}
	
	public Sensor(LevelMap theMap)
	{
		this.theMap=theMap;
		foundUnits = new ArrayList<ArrayList<FUnit>>();
		ourSignal = new Signal();
		
		attachedObjects = new ArrayList<IAttachable>();
		transformToApply = new AffineTransform();
		
		onePoint2D = new Point2D.Double();
		onePoint2Dtmp = new Point2D.Double();
	}
	
	public void setPos(double x,double y, double z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}

	public void removeTurn(int turnToRemove)
	{
		// First remove the Gfx units!
		tmpFUnits = foundUnits.get(turnToRemove);
		for (int iFUnit = 0; iFUnit < tmpFUnits.size() ; iFUnit++)
		{
			// Update !!!
			FUnitWatcher.getInstance().removeFUnits(1);
			tmpFUnits.get(iFUnit).removeMe();	
		}
		// Then remove them from memory
		foundUnits.remove(turnToRemove);
		currentTurn--;
	}
	
	public void setUpDetectionTurn()
	{
		if (foundUnits.size() > allowedDepth)
		{
			removeTurn(0);	
		}
		
		currentFUnits = new ArrayList<FUnit>();
		foundUnits.add(currentFUnits);
		
		currentTurn++;
	}
	
	public void addFUnit(FUnit oneUnit)
	{	
		// 50 contacts per sensors max.
		if (true)//currentFUnits.size() < 50)
		{
			//FUnitWatcher.getInstance().addFUnits(1);
			currentFUnits.add(oneUnit);
		}
		else
		{
			oneUnit.removeMe();
		}
	}
	
	public void setDepth(int depth)
	{
		allowedDepth=depth;
	}
	
	public abstract void removeMe();
	
	public void removeFUnits()
	{
		if (foundUnits != null)
		{
			for (int iFUnits = 0; iFUnits < foundUnits.size() ; iFUnits++)
			{
				// Update !!!
				tmpFUnits = foundUnits.get(iFUnits);
				for (int iFUnit = 0; iFUnit < tmpFUnits.size() ; iFUnit++)
				{
					// Update !!!
					FUnitWatcher.getInstance().removeFUnits(1);
					tmpFUnits.get(iFUnit).removeMe();
				}
				tmpFUnits.clear();
			}
			foundUnits.clear();
		}
	}
	
	/**
	 * Keep only the closer contacts.
	 * Does not work yet !!! (copy of above)
	 * @param posX
	 * @return 0
	 */
	public int keepClosers(double posX, double posY)
	{
		if (foundUnits != null)
		{
			for (int iFUnits = 0; iFUnits < foundUnits.size() ; iFUnits++)
			{
				// Update !!!
				tmpFUnits = foundUnits.get(iFUnits);
				for (int iFUnit = 0; iFUnit < tmpFUnits.size() ; iFUnit++)
				{
					// Update !!!
					FUnitWatcher.getInstance().removeFUnits(1);
					tmpFUnits.get(iFUnit).removeMe();
				}
				tmpFUnits.clear();
			}
			foundUnits.clear();
		}
		return 0;
	}
	
	public void updateFUnitGfx()
	{
		if (foundUnits != null)
		{
			for (int iFUnits = 0; iFUnits < foundUnits.size() ; iFUnits++)
			{
				// Update !!!
				tmpFUnits = foundUnits.get(iFUnits);
				for (int iFUnit = 0; iFUnit < tmpFUnits.size() ; iFUnit++)
				{
					// Update !!!
					tmpFUnits.get(iFUnit).updateGfx();		
				}
			}
		}
	}
	// The update allow to move the sensor and detect the units. Also give our position away...
	public abstract void doUpdate(double time);
	
	public abstract void hide();
	
	public abstract void show();
	/**
	 * Create the graphics entity
	 */
	public abstract void createGfx(double xInit, double yInit, double zInit, double direction, double puissance, double speed);
	
	/* (non-Javadoc)
	 * @see com.tgb.subengine.gfxentities.IAttachable#setOrientationAttach(double)
	 */
	public void setOrientationAttach(double orientation)
	{
		// TODO Auto-generated method stub
		this.orientationAttach = orientation;
	}
	/* (non-Javadoc)
	 * @see com.tgb.subengine.gfxentities.IAttachable#setPosAttach(double, double, double)
	 */
	public void setPosAttach(double x, double y, double z)
	{
		// TODO Auto-generated method stub
		xAttach=x;
		yAttach=y;
		zAttach=z;
	}
	/**
	 * @return the orientationAttach
	 */
	public double getOrientationAttach()
	{
		return orientationAttach;
	}
	/* (non-Javadoc)
	 * @see com.tgb.subengine.gfxentities.IAttachable#getXAttach()
	 */
	public double getXAttach()
	{
		// TODO Auto-generated method stub
		return xAttach;
	}
	/* (non-Javadoc)
	 * @see com.tgb.subengine.gfxentities.IAttachable#getYAttach()
	 */
	public double getYAttach()
	{
		// TODO Auto-generated method stub
		return yAttach;
	}
	/* (non-Javadoc)
	 * @see com.tgb.subengine.gfxentities.IAttachable#getZAttach()
	 */
	public double getZAttach()
	{
		// TODO Auto-generated method stub
		return zAttach;
	}
	/**
	 * @param attach the xAttach to set
	 */
	public void setXAttach(double attach)
	{
		xAttach = attach;
	}
	/**
	 * @param attach the yAttach to set
	 */
	public void setYAttach(double attach)
	{
		yAttach = attach;
	}
	/**
	 * @param attach the zAttach to set
	 */
	public void setZAttach(double attach)
	{
		zAttach = attach;
	}
	
	/* (non-Javadoc)
	 * @see com.tgb.subengine.gfxentities.IAttachable#setAbsPos(double, double, double)
	 */
	public void setAbsPos(double x, double y, double z)
	{
		// TODO Auto-generated method stub
		setPos(x,y,z);
	}

	
	public int addAttachedObject(IAttachable newObject)
	{
		newObject.setOrientationAttach(this.orientation+this.orientationAttach);

		transformToApply.setToIdentity();
		transformToApply.translate(this.getX(), this.getY());
		transformToApply.rotate(this.getOrientation()+this.orientationAttach, 0, 0);
		//transformToApply.scale(this.getSize(), this.getSize());
		onePoint2D.setLocation(newObject.getXAttach(), newObject.getYAttach());
		transformToApply.transform(onePoint2D, onePoint2Dtmp);
		newObject.setAbsPos(onePoint2Dtmp.x, onePoint2Dtmp.y,0);
		
		attachedObjects.add(newObject);
		return (attachedObjects.size() - 1);
	}
	
	public void updateAttachedObject(IAttachable objectToUpdate)
	{
		objectToUpdate.setOrientationAttach(this.orientation+this.orientationAttach);

		transformToApply.setToIdentity();
		transformToApply.translate(this.getX(), this.getY());
		transformToApply.rotate(this.getOrientation()+this.orientationAttach, 0, 0);
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
	 * @return the foundUnits
	 */
	public ArrayList<FUnit> getFoundUnits()
	{
		if (foundUnits.size() > 0)
			return foundUnits.get(foundUnits.size() -1 );
		else
			return null;
	}
	
	/**
	 * @return the foundUnits
	 */
	public ArrayList<FUnit> getFoundUnits(int turn)
	{
		if ((turn > 0) && (turn < foundUnits.size()))
			return foundUnits.get(turn);
		else
			return null;
	}
//
//	/**
//	 * @param foundUnits the foundUnits to set
//	 */
//	public void setFoundUnits(ArrayList<FUnit> foundUnits)
//	{
//		this.foundUnits = foundUnits;
//	}

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
	 * @return the theMap
	 */
	public LevelMap getTheMap()
	{
		return theMap;
	}

	/**
	 * @param theMap the theMap to set
	 */
	public void setTheMap(LevelMap theMap)
	{
		this.theMap = theMap;
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
	 * @return the receiver
	 */
	public ProgrammableUnit getReceiver()
	{
		return receiver;
	}

	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(ProgrammableUnit receiver)
	{
		this.receiver = receiver;
	}

	/**
	 * @return the power
	 */
	public double getPower()
	{
		return power;
	}

	/**
	 * @param power the power to set
	 */
	public void setPower(double power)
	{
		this.power = power;
	}

	public int getAllowedDepth() {
		return allowedDepth;
	}

	public void setAllowedDepth(int allowedDepth) {
		this.allowedDepth = allowedDepth;
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.unitspc.sensors.ISignalCreator#getOurSignal()
	 */
	public Signal getOurSignal() {
		return ourSignal;
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.unitspc.sensors.ISignalCreator#setOurSignal(com.tgb.subgame.units.sensors.Signal)
	 */
	public void setOurSignal(Signal ourSignal) {
		this.ourSignal = ourSignal;
	}

	public boolean isDead() {
		return dead;
	}

	public int getIdOwner() {
		return idOwner;
	}

	public void setIdOwner(int idOwner) {
		this.idOwner = idOwner;
	}

	public int getMyType() {
		return myType;
	}

	public void setMyType(int myType) {
		this.myType = myType;
	}
}
