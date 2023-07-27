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

import java.awt.Graphics2D;
import java.awt.Image;


/**
 * A sprite, that can be translated, rotated and resized.
 * @author Alain Becam
 *
 */
public class Sprite extends Drawable2DEntity
{
	/* (non-Javadoc)
	 * @see subengine.gfxentities.Drawable2DEntity#getCenterX()
	 */
	@Override
	public double getCenterX()
	{
		// TODO Auto-generated method stub
		return ((this.getX()+ourImage.getWidth(null)/2)*this.getSize());
	}
	/* (non-Javadoc)
	 * @see subengine.gfxentities.Drawable2DEntity#getCenterY()
	 */
	@Override
	public double getCenterY()
	{
		// TODO Auto-generated method stub
		return ((this.getY()+ourImage.getHeight(null)/2)*this.getSize());
	}
	Image ourImage ;
	Graphics2D graphicsToUse;
	
	public Sprite(Image imageToUse)
	{
		super(0,0,1);
		ourImage = imageToUse;
	}
	/* (non-Javadoc)
	 * @see subengine.gfxentities.Drawable2DEntity#drawMe(java.awt.Graphics2D)
	 */
	@Override
	public boolean drawMe()
	{
		graphicsToUse = ourRenderer.getGraphics();
		if ((ourImage != null) && (ourImage.getHeight(null) > 0)) 
		{
			transformToApply.setToIdentity();
			
			transformToApply.translate(this.getX(), this.getY());			
			transformToApply.scale(this.getSize(), this.getSize());
			transformToApply.rotate(this.getRotation()+this.getOrientationAttach(), ourImage.getWidth(null)/2, ourImage.getHeight(null)/2);
			
			graphicsToUse.drawImage(ourImage,transformToApply,null);
			updateAttachedObjects();
		}
		System.out.println("Painted one sprite");
		return true;
	}
	@Override
	public void setAttachableColor(int newColor)
	{
		// TODO Auto-generated method stub
		
	}

}
