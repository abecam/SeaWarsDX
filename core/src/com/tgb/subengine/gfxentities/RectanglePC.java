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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import com.tgb.subengine.RendererPulpeC;

import pulpcore.image.CoreGraphics;


/**
 * A simple rectangle, not filled.
 * @author Alain Becam
 *
 */
public class RectanglePC extends Drawable2DEntity
{
	CoreGraphics graphicsToUse;
	RendererPulpeC ourRenderer;
	
	int ourColor;
	
	double xLeft,yDown;
	
	public RectanglePC()
	{
		super(0,0,1);
		ourRenderer = RendererPulpeC.getInstance();
		
		xLeft=1;
		yDown=1;
		ourColor = 0xff000000;
	}

	public void setSizeRect(double x,double y)
	{
		xLeft=x;
		yDown=y;
	}
	
	public int getOurColor()
	{
		return ourColor;
	}

	public void setOurColor(int ourColor)
	{
		this.ourColor = ourColor;
	}
	
	/* (non-Javadoc)
	 * @see subengine.Drawable2DEntity#drawMe(java.awt.Graphics2D)
	 */
	@Override
	public boolean drawMe()
	{
		graphicsToUse = ourRenderer.getGraphics();
		graphicsToUse.setColor(ourColor);
		// TODO Auto-generated method stub

		graphicsToUse.drawLine(this.getX(), this.getY(), this.getX()+this.getSize()*xLeft,  this.getY());
		graphicsToUse.drawLine(this.getX(), this.getY(), this.getX(), this.getY()+this.getSize()*yDown);
		graphicsToUse.drawLine(this.getX(), this.getY()+this.getSize()*yDown,this.getX()+this.getSize()* xLeft, this.getY()+this.getSize()*yDown);
		graphicsToUse.drawLine(this.getX()+this.getSize()*xLeft, this.getY(),this.getX()+this.getSize()* xLeft,  this.getY()+this.getSize()*yDown);
		updateAttachedObjects();
		return false;
	}

	@Override
	public void setAttachableColor(int newColor)
	{
		ourColor = newColor;
	}
}
