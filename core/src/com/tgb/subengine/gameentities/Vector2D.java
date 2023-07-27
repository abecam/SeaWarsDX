/*
 * Created on 30 jul 2008
 *
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

/*
 * Copyright (c)2008 Alain Becam
 */

package com.tgb.subengine.gameentities;

public class Vector2D
{
	double x,y;
	double norm;
	
	public Vector2D()
	{
		x=0;
		y=0;
		norm=0;
	}
	
	public Vector2D(double xInit, double yInit)
	{
		x=xInit;
		y=yInit;
	}
	
	public double getX()
	{
		return x;
	}
	public void setX(double x)
	{
		this.x = x;
	}
	public double getY()
	{
		return y;
	}
	public void setY(double y)
	{
		this.y = y;
	}
	public double getNorm()
	{
		norm = Math.sqrt(x*x+ y*y);
		
		return norm;
	}
	
	public void setXY(double x,double y)
	{
		this.x = x;
		this.y = y;	
	}
	
	public void addXY(double x,double y)
	{
		this.x += x;
		this.y += y;
	}
	
	public String toString()
	{
		return x+" : "+y;
	}
	
	public double dotProduct(Vector2D otherVector)
	{
		return this.x * otherVector.x + this.y * otherVector.y;
	}
}
