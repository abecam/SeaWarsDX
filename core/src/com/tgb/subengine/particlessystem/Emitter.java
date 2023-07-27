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

package com.tgb.subengine.particlessystem;

import com.tgb.subengine.gfxentities.*;

/**
 * The emitter for a set of particles. Can work with an example of particle, 
 * allowing the parameter to come from the particle.
 * @author Alain Becam
 *
 */
public class Emitter implements IAttachable
{
	public static final int POINT=0;
	public static final int POINT_DIRECTIONNAL=1;
	public static final int LINE=2;
	public static final int RECTANGLE_AREA=3;
	double x,y,z;
	double size;
	int energyInitial;
	double weightInitial;
	double orientation;
	
	double angle; // If directionnal
	
	double x2,y2; // If line or rectangle area
	
	double orientationAttach=0; // Orientation *relative* to the object we are attached to
	double xAttach,yAttach,zAttach=0; 
	
	int type;
	
	int partPerFrame=10;  // particules per frame
	int msBetweenFrame=100; // Wanted frames per second
	int lastTimeEmitter=100000;
	
	int variability=0; // In percentile
	double speed=10;
	long particuleLife=100; // Will be passed to the particle, then decreased until the particule die.
	
	Particle exampleParticle; // If wanted, used to set/get the parameters of the emitted particles
	
	/**
	 * @param x
	 * @param y
	 * @param size
	 * @param energyInitial
	 * @param weightInitial
	 * @param orientation
	 * @param angle
	 * @param type
	 * @param partPerFrame
	 * @param msBetweenFrame
	 * @param variability
	 * @param speed
	 * @param particuleLife
	 */
	public Emitter(double x, double y, double size, int energyInitial, double weightInitial, double orientation, double angle, int type, int partPerFrame, int msBetweenFrame, int variability, double speed, long particuleLife)
	{
		super();
		this.x = x;
		this.y = y;
		this.size = size;
		this.energyInitial = energyInitial;
		this.weightInitial = weightInitial;
		this.orientation = orientation;
		this.angle = angle;
		this.type = type;
		this.partPerFrame = partPerFrame;
		this.msBetweenFrame = msBetweenFrame;
		this.variability = variability;
		this.speed = speed;
		this.particuleLife = particuleLife;
		
		this.exampleParticle=null;
	}
	public Emitter(double x, double y,double orientation, double angle, int type, int partPerFrame, int msBetweenFrame, int variability, double speed, long particuleLife, Particle exampleParticle)
	{
		super();
		this.x = x;
		this.y = y;
		this.orientation = orientation;
		this.angle = angle;
		this.type = type;
		this.partPerFrame = partPerFrame;
		this.msBetweenFrame = msBetweenFrame;
		this.variability = variability;
		this.speed = speed;
		this.particuleLife = particuleLife;
		
		this.exampleParticle=exampleParticle;
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
	 * @return the energyInitial
	 */
	public int getEnergyInitial()
	{
		return energyInitial;
	}
	/**
	 * @param energyInitial the energyInitial to set
	 */
	public void setEnergyInitial(int energyInitial)
	{
		this.energyInitial = energyInitial;
	}
	/**
	 * @return the msBetweenFrame
	 */
	public int getMsBetweenFrame()
	{
		return msBetweenFrame;
	}
	/**
	 * @param msBetweenFrame the msBetweenFrame to set
	 */
	public void setMsBetweenFrame(int msBetweenFrame)
	{
		this.msBetweenFrame = msBetweenFrame;
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
	 * @return the particuleLife
	 */
	public long getParticuleLife()
	{
		return particuleLife;
	}
	/**
	 * @param particuleLife the particuleLife to set
	 */
	public void setParticuleLife(long particuleLife)
	{
		this.particuleLife = particuleLife;
	}
	/**
	 * @return the partPerFrame
	 */
	public int getPartPerFrame()
	{
		return partPerFrame;
	}
	/**
	 * @param partPerFrame the partPerFrame to set
	 */
	public void setPartPerFrame(int partPerFrame)
	{
		this.partPerFrame = partPerFrame;
	}
	/**
	 * @return the size
	 */
	public double getSize()
	{
		return size;
	}
	/**
	 * @param size the size to set
	 */
	public void setSize(double size)
	{
		this.size = size;
	}
	/**
	 * @return the speed
	 */
	public double getSpeed()
	{
		return speed;
	}
	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed)
	{
		this.speed = speed;
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
	 * @return the variability
	 */
	public int getVariability()
	{
		return variability;
	}
	/**
	 * @param variability the variability to set
	 */
	public void setVariability(int variability)
	{
		this.variability = variability;
	}
	/**
	 * @return the weightInitial
	 */
	public double getWeightInitial()
	{
		return weightInitial;
	}
	/**
	 * @param weightInitial the weightInitial to set
	 */
	public void setWeightInitial(double weightInitial)
	{
		this.weightInitial = weightInitial;
	}
	public void setPos(double x,double y)
	{
		this.x = x;
		this.y = y;
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
	 * @return the x2
	 */
	public double getX2()
	{
		return x2;
	}
	/**
	 * @param x2 the x2 to set
	 */
	public void setX2(double x2)
	{
		this.x2 = x2;
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
	 * @return the y2
	 */
	public double getY2()
	{
		return y2;
	}
	/**
	 * @param y2 the y2 to set
	 */
	public void setY2(double y2)
	{
		this.y2 = y2;
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
	 * @return the exampleParticle
	 */
	public Particle getExampleParticle()
	{
		return exampleParticle;
	}
	/**
	 * @param exampleParticle the exampleParticle to set
	 */
	public void setExampleParticle(Particle exampleParticle)
	{
		this.exampleParticle = exampleParticle;
	}
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
		setX(x);
		setY(y);
		setZ(z);
	}
	@Override
	public void setAttachableColor(int newColor)
	{
		// TODO Auto-generated method stub
		
	}
}