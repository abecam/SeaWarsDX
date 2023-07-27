/*
 * This software is distributed under the MIT License
 *
 * Copyright (c) 2008-2009 Alain Becam
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

package com.tgb.subengine.gfxentities;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.tgb.subengine.Renderer;

/**
 * Superclass for the drawable 2D entity. Must be inherited (the drawMe method is virtual! )
 * Then I recommend that the game entities are not the sub-class, but have a drawable facet,
 * So create and update one or several drawable entities.
 * A particule is not necessary a DrawableEntity (for performance purpose)
 * @author Alain Becam
 *
 */
public abstract class Drawable2DEntity implements IAttachable
{
	private double x,y,z; // x,y the position on the plan, z the altitude
	public double xBackup,yBackup,zBackup; // backup position to restore it if needed
	public boolean is3D=true;
	private double rotation; // Is also yaw, but is called rotation as the pitch,roll and yaw will be only useful for 3D-like motion
	private double pitch; // Nose up and down
	private double roll; // Wings up and down
	private double size; // Global size. The detail is up to the subclass, as we don't know the shape here
	private double sizeRelative;
	
	private long deepOnScreen; // basically z, but for the on screen rendering. By default the round of z
	
	private int alpha; // transparency of the element
	
	boolean valide=true; // will it be drawn or not
	
	private long idEntity; // Given by the RenderingManager
	
	Renderer ourRenderer;
	
	ArrayList<IAttachable> attachedObjects; // Attached object, will be moved and rotate with the entity
	AffineTransform transformToApply; // Common affine transform
	
	Point2D.Double onePoint2D ;
	Point2D.Double onePoint2Dtmp ;
	
	double orientationAttach=0; // Orientation *relative* to the object we are attached to
	double xAttach,yAttach,zAttach=0;
	protected int ourColor; 
	/**
	 * @return the idEntity
	 */
	public long getIdEntity()
	{
		return idEntity;
	}

	/**
	 * @param idEntity the idEntity to set
	 */
	public void setIdEntity(long idEntity)
	{
		this.idEntity = idEntity;
	}

	/**
	 * draw the entity. You must implement this method!
	 * @return true if everything was alright
	 */
	public abstract boolean drawMe();
	
	/**
	 * Rotate the entity
	 * @param rot in radians
	 */
	public void rotate(double rot)
	{
		rotation+=rot;
		// Stay between 0 and 2*PI
		while (rotation > 2*Math.PI)
		{
			rotation-=2*Math.PI;
		}
		while (rotation < 0)
		{
			rotation+=2*Math.PI;
		}
	}
	
	/**
	 * Pitch the entity
	 * @param rot in radians
	 */
	public void pitch(double rot)
	{
		pitch+=rot;
		// Stay between 0 and 2*PI
		while (pitch > 2*Math.PI)
		{
			pitch-=2*Math.PI;
		}
		while (pitch < 0)
		{
			pitch+=2*Math.PI;
		}
	}
	
	/**
	 * Roll the entity
	 * @param rot in radians
	 */
	public void roll(double rot)
	{
		roll+=rot;
		// Stay between 0 and 2*PI
		while (roll > 2*Math.PI)
		{
			roll-=2*Math.PI;
		}
		while (roll < 0)
		{
			roll+=2*Math.PI;
		}
	}
	
	/**
	 * Move the entity, relative to its current position
	 * @param dx
	 * @param dy
	 */
	public void moveRelative(double dx,double dy)
	{
		x+=dx;
		y+=dy;
	}
	
	/**
	 * Move the entity, relative to its current position
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void moveRelative(double dx,double dy,double dz)
	{
		x+=dx;
		y+=dy;
		z+=dz;
		deepOnScreen=Math.round(z);
	}
	
	/**
	 * Move the entity to the desired position
	 * @param xDest
	 * @param yDest
	 */
	public void moveTo(double xDest,double yDest)
	{
		x=xDest;
		y=yDest;
	}
	
	/**
	 * Move the entity to the desired position
	 * @param xDest
	 * @param yDest
	 * @param zDest
	 */
	public void moveTo(double xDest,double yDest,double zDest)
	{
		x=xDest;
		y=yDest;
		z=zDest;
		deepOnScreen=Math.round(z);
	}
	
	public void resize(double percent)
	{
		size=sizeRelative*percent/100;
	}

	/**
	 * Common constructors' set-up
	 */ 
	public void initDrawable2DEntity(double xInit, double yInit, double sizeInit)
	{
		this.x = xInit;
		this.y = yInit;
		
		this.size = sizeInit;
		this.sizeRelative = sizeInit;
		ourRenderer = Renderer.getInstance();
		attachedObjects = new ArrayList<IAttachable>();
		transformToApply = new AffineTransform();
		
		onePoint2D = new Point2D.Double();
		onePoint2Dtmp = new Point2D.Double();
	}
	/**
	 * @param x
	 * @param y
	 * @param size
	 */
	public Drawable2DEntity(double x, double y, double size)
	{
		super();
		this.z = 0;
		this.rotation = 0;
		this.pitch = 0;
		this.roll = 0;
		initDrawable2DEntity(x,y,size);
	}
	
	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param size
	 */
	public Drawable2DEntity(double x, double y, double z, double size)
	{
		super();
		
		this.z = z;
		initDrawable2DEntity(x,y,size);
	}
	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param rotation
	 * @param pitch
	 * @param roll
	 * @param size
	 */
	public Drawable2DEntity(double x, double y, double z, double rotation, double pitch, double roll, double size)
	{
		super();
		this.z = z;
		this.rotation = rotation;
		this.pitch = pitch;
		this.roll = roll;
		initDrawable2DEntity(x,y,size);
	}

	/**
	 * @return the deepOnScreen
	 */
	public long getDeepOnScreen()
	{
		return deepOnScreen;
	}

	/**
	 * @param deepOnScreen the deepOnScreen to set
	 */
	public void setDeepOnScreen(long deepOnScreen)
	{
		this.deepOnScreen = deepOnScreen;
	}

	/**
	 * @return the pitch
	 */
	public double getPitch()
	{
		return pitch;
	}

	/**
	 * @param pitch the pitch to set
	 */
	public void setPitch(double pitch)
	{
		this.pitch = pitch;
	}

	/**
	 * @return the roll
	 */
	public double getRoll()
	{
		return roll;
	}

	/**
	 * @param roll the roll to set
	 */
	public void setRoll(double roll)
	{
		this.roll = roll;
	}

	/**
	 * @return the rotation
	 */
	public double getRotation()
	{
		return rotation;
	}

	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(double rotation)
	{
		this.rotation = rotation;
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
		this.sizeRelative = size;
	}
	
	/**
	 * @param size the size to grow
	 */
	public void grow(double size)
	{
		this.size += size;
		this.sizeRelative = size;
	}

	/**
	 * @return the x
	 */
	public double getX()
	{
		return x;
	}

	/**
	 * @return the x for the center of the entity
	 */
	public double getCenterX()
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
	 * @return the y for the center of the entity
	 */
	public double getCenterY()
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
	 * @return the z for the center of the entity
	 */
	public double getCenterZ()
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
	 * SetPos for the attached entity. Do not use directly, use setPos instead
	 * @see com.tgb.subengine.gfxentities.IAttachable#setAbsPos(double, double, double)
	 */
	public void setAbsPos(double x, double y, double z)
	{
		// TODO Auto-generated method stub
		setPos( x, y, z);
	}

	/**
	 * Set the position of the entity. 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setPos(double x,double y,double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * @return the alpha
	 */
	public int getAlpha()
	{
		return alpha;
	}

	/**
	 * @param alpha the alpha to set
	 */
	public void setAlpha(int alpha)
	{
		this.alpha = alpha;
	}

	/**
	 * @return the valide
	 */
	public boolean isValide()
	{
		return valide;
	}

	/**
	 * validate the entity
	 */
	public void validate()
	{
		this.valide = true;
	}
	
	/**
	 * invalidate the entity
	 */
	public void invalidate()
	{
		this.valide = false;
	}
	
	public int addAttachedObject(IAttachable newObject)
	{
		newObject.setOrientationAttach(this.rotation+this.orientationAttach);

		transformToApply.setToIdentity();
		transformToApply.translate(this.getCenterX(), this.getCenterY());
		transformToApply.scale(this.getSize(), this.getSize());
		transformToApply.rotate(this.getRotation()+this.orientationAttach, 0, 0);
		onePoint2D.setLocation(newObject.getXAttach(), newObject.getYAttach());
		transformToApply.transform(onePoint2D, onePoint2Dtmp);
		newObject.setAbsPos(onePoint2Dtmp.x, onePoint2Dtmp.y,this.getCenterZ());
		
		attachedObjects.add(newObject);
		return (attachedObjects.size() - 1);
	}
	
	public void updateAttachedObject(IAttachable objectToUpdate)
	{
		objectToUpdate.setOrientationAttach(this.rotation+this.orientationAttach);

		transformToApply.setToIdentity();
		transformToApply.translate(this.getCenterX(), this.getCenterY());
		transformToApply.scale(this.getSize(), this.getSize());
		transformToApply.rotate(this.getRotation()+this.orientationAttach, 0, 0);
		onePoint2D.setLocation(objectToUpdate.getXAttach(), objectToUpdate.getYAttach());
		transformToApply.transform(onePoint2D, onePoint2Dtmp);
		objectToUpdate.setAbsPos(onePoint2Dtmp.x, onePoint2Dtmp.y,this.getCenterZ());
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
	
	/**
	 * Remove the attachments (and others ? )
	 */
	public void removeMe()
	{
		attachedObjects.clear();
	}

	public int getOurColor()
	{
		return ourColor;
	}

	public void setOurColor(int ourColor)
	{
		this.ourColor = ourColor;
	}
}
