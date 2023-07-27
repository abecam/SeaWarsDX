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

package com.tgb.subengine.particlessystem;

import pulpcore.image.CoreGraphics;
import pulpcore.image.Colors;

import com.tgb.subengine.RendererPulpeC;

/**
 * A simple PulpCore particle
 * 
 * @author Alain Becam
 *
 */

public class ParticlePC extends Particle implements Cloneable
{
	CoreGraphics graphicsToUse;
	
	RendererPulpeC ourRenderer;

	public ParticlePC()
	{
		ourRenderer = RendererPulpeC.getInstance();
		myColor = 0xFFFF0000;
	}
	/**
	 * @return the alive
	 */
	public boolean isAlive()
	{
		return alive;
	}

	/**
	 * @param alive the alive to set
	 */
	public void setAlive(boolean alive)
	{
		this.alive = alive;
	}

	/**
	 * @return the energy
	 */
	public int getEnergy()
	{
		return energy;
	}

	/**
	 * @param energy the energy to set
	 */
	public void setEnergy(int energy)
	{
		this.energy = energy;
	}

	public void setGravity(double xGravity,double yGravity,double zGravity)
	{
		this.xGrav = xGravity;
		this.yGrav = yGravity;
		this.zGrav = zGravity;
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
	 * @return the timeLeft
	 */
	public long getTimeLeft()
	{
		return timeLeft;
	}

	public long removeTime(long timeToRemove)
	{
		timeLeft-=timeToRemove;
		return timeLeft;
	}
	/**
	 * @param timeLeft the timeLeft to set
	 */
	public void setTimeLeft(long timeLeft)
	{
		this.timeLeft = timeLeft;
	}

	/**
	 * @return the weight
	 */
	public double getWeight()
	{
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(double weight)
	{
		this.weight = weight;
	}

	public void setPos(double x,double y,double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setSpeed(double x,double y,double z)
	{
		this.xSpeed = x;
		this.ySpeed = y;
		this.zSpeed = z;
	}
	
	public void setAcc(double x,double y,double z)
	{
		this.xAcc = x;
		this.yAcc = y;
		this.zAcc = z;
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
	 * @return the energyIncDec
	 */
	public double getEnergyIncDec()
	{
		return energyIncDec;
	}
	/**
	 * @param energyIncDec the energyIncDec to set
	 */
	public void setEnergyIncDec(double energyIncDec)
	{
		this.energyIncDec = energyIncDec;
	}
	/**
	 * @return the energyVar
	 */
	public double getEnergyVar()
	{
		return energyVar;
	}
	/**
	 * @param energyVar the energyVar to set
	 */
	public void setEnergyVar(double energyVar)
	{
		this.energyVar = energyVar;
	}
	/**
	 * @return the sizeIncDec
	 */
	public double getSizeIncDec()
	{
		return sizeIncDec;
	}
	/**
	 * @param sizeIncDec the sizeIncDec to set
	 */
	public void setSizeIncDec(double sizeIncDec)
	{
		this.sizeIncDec = sizeIncDec;
	}
	/**
	 * @return the sizeVar
	 */
	public double getSizeVar()
	{
		return sizeVar;
	}
	/**
	 * @param sizeVar the sizeVar to set
	 */
	public void setSizeVar(double sizeVar)
	{
		this.sizeVar = sizeVar;
	}
	/**
	 * @return the speedIncDec
	 */
	public double getSpeedIncDec()
	{
		return speedIncDec;
	}
	/**
	 * @param speedIncDec the speedIncDec to set
	 */
	public void setSpeedIncDec(double speedIncDec)
	{
		this.speedIncDec = speedIncDec;
	}
	/**
	 * @return the speedVar
	 */
	public double getSpeedVar()
	{
		return speedVar;
	}
	/**
	 * @param speedVar the speedVar to set
	 */
	public void setSpeedVar(double speedVar)
	{
		this.speedVar = speedVar;
	}
	/**
	 * @return the weightIncDec
	 */
	public double getWeightIncDec()
	{
		return weightIncDec;
	}
	/**
	 * @param weightIncDec the weightIncDec to set
	 */
	public void setWeightIncDec(double weightIncDec)
	{
		this.weightIncDec = weightIncDec;
	}
	/**
	 * @return the weightVar
	 */
	public double getWeightVar()
	{
		return weightVar;
	}
	/**
	 * @param weightVar the weightVar to set
	 */
	public void setWeightVar(double weightVar)
	{
		this.weightVar = weightVar;
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
	 * @return the xAcc
	 */
	public double getXAcc()
	{
		return xAcc;
	}
	/**
	 * @param acc the xAcc to set
	 */
	public void setXAcc(double acc)
	{
		xAcc = acc;
	}
	/**
	 * @return the xGrav
	 */
	public double getXGrav()
	{
		return xGrav;
	}
	/**
	 * @param grav the xGrav to set
	 */
	public void setXGrav(double grav)
	{
		xGrav = grav;
	}
	/**
	 * @return the xSpeed
	 */
	public double getXSpeed()
	{
		return xSpeed;
	}
	/**
	 * @param speed the xSpeed to set
	 */
	public void setXSpeed(double speed)
	{
		xSpeed = speed;
	}
	/**
	 * @return the yAcc
	 */
	public double getYAcc()
	{
		return yAcc;
	}
	/**
	 * @param acc the yAcc to set
	 */
	public void setYAcc(double acc)
	{
		yAcc = acc;
	}
	/**
	 * @return the yGrav
	 */
	public double getYGrav()
	{
		return yGrav;
	}
	/**
	 * @param grav the yGrav to set
	 */
	public void setYGrav(double grav)
	{
		yGrav = grav;
	}
	/**
	 * @return the ySpeed
	 */
	public double getYSpeed()
	{
		return ySpeed;
	}
	/**
	 * @param speed the ySpeed to set
	 */
	public void setYSpeed(double speed)
	{
		ySpeed = speed;
	}
	/**
	 * @return the zAcc
	 */
	public double getZAcc()
	{
		return zAcc;
	}
	/**
	 * @param acc the zAcc to set
	 */
	public void setZAcc(double acc)
	{
		zAcc = acc;
	}
	/**
	 * @return the zGrav
	 */
	public double getZGrav()
	{
		return zGrav;
	}
	/**
	 * @param grav the zGrav to set
	 */
	public void setZGrav(double grav)
	{
		zGrav = grav;
	}
	/**
	 * @return the zSpeed
	 */
	public double getZSpeed()
	{
		return zSpeed;
	}
	/**
	 * @param speed the zSpeed to set
	 */
	public void setZSpeed(double speed)
	{
		zSpeed = speed;
	}
	/**
	 * @return the xOld
	 */
	public double getXOld()
	{
		return xOld;
	}
	/**
	 * @return the yOld
	 */
	public double getYOld()
	{
		return yOld;
	}
	/**
	 * @return the zOld
	 */
	public double getZOld()
	{
		return zOld;
	}
	/**
	 * Move the particle, relative to its current position
	 * @param dx
	 * @param dy
	 */
	public void moveRelative(double dx,double dy)
	{
		x+=dx;
		y+=dy;
	}
	
	/**
	 * Move the particle, relative to its current position
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void moveRelative(double dx,double dy,double dz)
	{
		x+=dx;
		y+=dy;
		z+=dz;
	}
	
	public void setAll(double xSpeed, double ySpeed, double zSpeed, double xAcc, double yAcc, double zAcc, double size, int energy, long timeLeft, double weight, double xGrav, double yGrav, double zGrav, double speedVar, double weightVar, double sizeVar, double energyVar, double speedIncDec, double weightIncDec, double sizeIncDec, double energyIncDec, double orientation, int myColor, boolean alive,int colorRVar, int colorGVar, int colorBVar, int colorAVar, int colorRIncDec, int colorGIncDec, int colorBIncDec, int colorAIncDec,long speedVarColor)
	{
		this.xSpeed=xSpeed;
		this.ySpeed=ySpeed;
		this.zSpeed=zSpeed;
		this.xAcc=xAcc;
		this.yAcc=yAcc;
		this.zAcc=zAcc;
		
		this.size=size;
		this.energy=energy;
		this.timeLeft=timeLeft; // If particuleLife in the Emitter is set, receive the number, in ms.
		this.weight=weight;
		this.xGrav=xGrav;
		this.yGrav=yGrav;
		this.zGrav=zGrav;
		
		this.speedVar=speedVar; // Speed variability, between 0 and 1
		this.weightVar=weightVar;
		this.sizeVar=sizeVar;
		this.energyVar=energyVar;
		
		this.speedIncDec=speedIncDec; // Speed increase/decrease
		this.weightIncDec=weightIncDec;
		this.sizeIncDec=sizeIncDec;
		this.energyIncDec=energyIncDec;
		
		this.orientation=orientation;
		
		// myColor can legally be null (for a bitmap for instance)
//		if (myColor == 0)
//		{
//			this.myColor=0xFF888888;
//		}
		this.myColor=myColor;
		
		this.alive=alive;
		
		this.colorRVar=colorRVar; // Color variability, 0 to ...
		this.colorGVar=colorGVar;
		this.colorBVar=colorBVar;
		this.colorAVar=colorAVar;
		
		this.colorRIncDec=colorRIncDec; // Color increase/decrease 0 to ...
		this.colorGIncDec=colorGIncDec;
		this.colorBIncDec=colorBIncDec;
		this.colorAIncDec=colorAIncDec;
		
		this.speedVarColor=speedVarColor;
	}

	public void copyFromExample(Particle example)
	{
		this.setAll(example.xSpeed, example.ySpeed, example.zSpeed, 
					example.xAcc, example.yAcc, example.zAcc, 
					example.size, example.energy, example.timeLeft, 
					example.weight, example.xGrav, example.yGrav, example.zGrav, 
					example.speedVar, example.weightVar, example.sizeVar, 
					example.energyVar, example.speedIncDec, example.weightIncDec, 
					example.sizeIncDec, example.energyIncDec, example.orientation, 
					example.myColor,example.alive,
					example.getColorRVar(),example.getColorGVar(),example.getColorBVar(),example.getColorAVar(),
					example.getColorRIncDec(),example.getColorGIncDec(),example.getColorBIncDec(),example.getColorAIncDec(),example.getSpeedVarColor());
	}
	
	public boolean drawMe(long time)
	{
		graphicsToUse= ourRenderer.getGraphics();
			
		// Simple draw, a point or a circle
		graphicsToUse.setColor(this.myColor);
		//System.out.println("Draw here : "+(int )(this.x-this.size/2)+" : "+(int )(this.y-this.size/2)+" : "+ (int )this.size+" : "+ (int )this.size+" || "+this.x+" - "+this.y);
		graphicsToUse.drawRect((int )(this.x-this.size/2), (int )(this.y-this.size/2), (int )this.size, (int )this.size);
		
		update(time);
		//System.out.println("Painted one particle - PC");
		return true;
	}
	
	public void update(long time)
	{
		this.x=this.x+this.xSpeed*((double )time);
		this.y=this.y+this.ySpeed*((double )time);
		this.size*=this.sizeIncDec;

		//System.out.println("Speed : "+xSpeed+ " - " + ySpeed);
		this.xSpeed=this.xSpeed+this.xAcc*((double )time)+this.xGrav*((double )time);
		this.ySpeed=this.ySpeed+this.yAcc*((double )time)+this.yGrav*((double )time);
		
		updateColor(time);
	}
	
	public void updateColor(long time)
	{
		this.timeForColor+=time;
		
		if (this.timeForColor > this.speedVarColor)
		{
			this.timeForColor=0;
			
			if ((colorAIncDec > 0) && (colorAIncDec < 0))
			{
				int alpha=Colors.getAlpha(this.myColor);
				alpha+=colorAIncDec;

				if (alpha < 0)
				{
					alpha=0;
				}
				else if (alpha > 255)
				{
					alpha = 255;
				}

				this.myColor = (0x00FFFFFF & this.myColor) + (alpha << 24);
			}
				
			if ((colorRIncDec > 0) && (colorRIncDec < 0))
			{
				int red=Colors.getRed(this.myColor);
				red+=colorRIncDec;

				if (red < 0)
				{
					red=0;
				}
				else if (red > 255)
				{
					red = 255;
				}

				this.myColor = (0xFF00FFFF & this.myColor) + (red << 16);
			}
			
			if ((colorGIncDec > 0) && (colorGIncDec < 0))
			{
				int green=Colors.getGreen(this.myColor);
				green+=colorGIncDec;

				if (green < 0)
				{
					green=0;
				}
				else if (green > 255)
				{
					green = 255;
				}

				this.myColor = (0xFFFF00FF & this.myColor) + (green << 24);
			}
			
			if ((colorBIncDec > 0) && (colorBIncDec < 0))
			{
				int blue=Colors.getBlue(this.myColor);
				blue+=colorBIncDec;

				if (blue < 0)
				{
					blue=0;
				}
				else if (blue > 255)
				{
					blue = 255;
				}

				this.myColor = (0xFFFFFF00 & this.myColor) + (blue << 24);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		// TODO Auto-generated method stub
		return super.clone();
	}
}