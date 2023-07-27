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

import com.tgb.subengine.Renderer;

import pulpcore.image.Colors;

/**
 * Super class for the particle, pure-java.
 * @author Alain Becam
 *
 */
public abstract class Particle implements Cloneable
{
	
	double x,y,z;
	double xOld,yOld,zOld;
	double xSpeed,ySpeed,zSpeed=0;
	double xAcc,yAcc,zAcc=0;
	
	double size;
	int energy;
	long timeLeft; // If particuleLife in the Emitter is set, receive the number, in ms.
	double weight;
	double xGrav,yGrav,zGrav=0;
	
	double speedVar=0; // Speed variability, 0 to ...
	double weightVar=0;
	double sizeVar=0;
	double energyVar=0;
	
	double speedIncDec=1; // Speed increase/decrease 0 to ...
	double weightIncDec=1;
	double sizeIncDec=1;
	double energyIncDec=1;
	
	int colorRVar=0; // Color variability, 0 to ...
	int colorGVar=0;
	int colorBVar=0;
	int colorAVar=0;
	
	int colorRIncDec=0; // Color increase/decrease 0 to ...
	int colorGIncDec=0;
	int colorBIncDec=0;
	int colorAIncDec=0;
	
	long speedVarColor=1000; // Speed to change the color.
	long timeForColor=0;
	
	double orientation=0;
	
	boolean alive=true;
	
	int myColor; // ARGB (PulpeCore format)
	
	Renderer ourRenderer;

	public Particle()
	{
		ourRenderer = Renderer.getInstance();
		myColor=Colors.RED;
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
	 * @return the myColor
	 */
	public int getMyColor()
	{
		return myColor;
	}

	/**
	 * @param myColor the myColor to set
	 */
	public void setMyColor(int myColor)
	{
		this.myColor = myColor;
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

	public abstract void copyFromExample(Particle example);

	
	public boolean drawMe(long time)
	{
		update(time);
		System.out.println("Painted one particle");
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
	public int getColorRVar() {
		return colorRVar;
	}
	public void setColorRVar(int colorRVar) {
		this.colorRVar = colorRVar;
	}
	public int getColorGVar() {
		return colorGVar;
	}
	public void setColorGVar(int colorGVar) {
		this.colorGVar = colorGVar;
	}
	public int getColorBVar() {
		return colorBVar;
	}
	public void setColorBVar(int colorBVar) {
		this.colorBVar = colorBVar;
	}
	public int getColorAVar() {
		return colorAVar;
	}
	public void setColorAVar(int colorAVar) {
		this.colorAVar = colorAVar;
	}
	public int getColorRIncDec() {
		return colorRIncDec;
	}
	public void setColorRIncDec(int colorRIncDec) {
		this.colorRIncDec = colorRIncDec;
	}
	public int getColorGIncDec() {
		return colorGIncDec;
	}
	public void setColorGIncDec(int colorGIncDec) {
		this.colorGIncDec = colorGIncDec;
	}
	public int getColorBIncDec() {
		return colorBIncDec;
	}
	public void setColorBIncDec(int colorBIncDec) {
		this.colorBIncDec = colorBIncDec;
	}
	public int getColorAIncDec() {
		return colorAIncDec;
	}
	public void setColorAIncDec(int colorAIncDec) {
		this.colorAIncDec = colorAIncDec;
	}
	public long getSpeedVarColor() {
		return speedVarColor;
	}
	public void setSpeedVarColor(long speedVarColor) {
		this.speedVarColor = speedVarColor;
	}
	public long getTimeForColor() {
		return timeForColor;
	}
	public void setTimeForColor(long timeForColor) {
		this.timeForColor = timeForColor;
	}
}