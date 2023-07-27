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

import com.tgb.subengine.RendererPulpeC;

import pulpcore.image.CoreImage;
import pulpcore.image.CoreGraphics;


/**
 * A flat sprite, that can be translated  and resized, but not rotated.
 * Variation to animate it (trial for the water in SeaWars)
 * @author Alain Becam
 *
 */
public class FlatSpritePCSpec extends Drawable2DEntity
{
	int width,height;
	/* (non-Javadoc)
	 * @see subengine.gfxentities.Drawable2DEntity#getCenterX()
	 */
	@Override
	public double getCenterX()
	{
		// TODO Auto-generated method stub
		return this.getX();//((this.getX()+this.getSize()*ourImage.getWidth()/2));
	}
	/* (non-Javadoc)
	 * @see subengine.gfxentities.Drawable2DEntity#getCenterY()
	 */
	@Override
	public double getCenterY()
	{
		// TODO Auto-generated method stub
		return this.getY();//((this.getY()+this.getSize()*ourImage.getHeight()/2));
	}
	CoreImage ourImage ;
	CoreImage imageToDraw ;
	CoreGraphics graphicsToUse;
	RendererPulpeC ourRenderer;
	
	public FlatSpritePCSpec(CoreImage imageToUse)
	{
		super(0,0,1);
		super.setAlpha(255);
		ourRenderer = RendererPulpeC.getInstance();
		
		ourImage = imageToUse;
		imageToDraw = ourImage;
	}
	
	public CoreImage getImageToDraw() {
		return imageToDraw;
	}
	public void setImageToDraw(CoreImage imageToDraw) {
		this.imageToDraw = imageToDraw;
	}
	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
		super.setAlpha(alpha);
		
		imageToDraw = ourImage.fade(alpha);
	}
	
	int xDec=0;
	int yDec=0;
	int phase=0;
	
	/* (non-Javadoc)
	 * @see subengine.gfxentities.Drawable2DEntity#drawMe(java.awt.Graphics2D)
	 */
	@Override
	public boolean drawMe()
	{
//		if (phase == 0)
//		{
//			xDec+=1;
//			if (xDec > 4)
//			{
//				phase=1;
//			}
//		}
//		else
//		{
//			xDec-=1;
//			if (xDec < 0)
//			{
//				phase=0;
//			}
//		}
		graphicsToUse = ourRenderer.getGraphics();
		
		graphicsToUse.drawScaledImageSpec(imageToDraw, xDec+(int )(this.getX()-this.getSize()*ourImage.getWidth()/2), yDec+(int )(this.getY()-this.getSize()*ourImage.getHeight()/2),  xDec+(int )(ourImage.getWidth()*this.getSize()), yDec+(int )(ourImage.getHeight()*this.getSize()));
		
		updateAttachedObjects();
		//System.out.println("Painted one sprite - PC");
		return true;
	}
	@Override
	public void setAttachableColor(int newColor)
	{
		// TODO Auto-generated method stub
		
	}

}
