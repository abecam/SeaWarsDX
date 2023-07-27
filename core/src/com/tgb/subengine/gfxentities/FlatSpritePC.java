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
 * @author Alain Becam
 *
 */
public class FlatSpritePC extends Drawable2DEntity
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
	
	public FlatSpritePC(CoreImage imageToUse)
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
	/* (non-Javadoc)
	 * @see subengine.gfxentities.Drawable2DEntity#drawMe(java.awt.Graphics2D)
	 */
	@Override
	public boolean drawMe()
	{
		graphicsToUse = ourRenderer.getGraphics();
		
		graphicsToUse.drawScaledImage(imageToDraw, (int )(this.getX()-this.getSize()*ourImage.getWidth()/2), (int )(this.getY()-this.getSize()*ourImage.getHeight()/2), (int )(ourImage.getWidth()*this.getSize()), (int )(ourImage.getHeight()*this.getSize()));
		
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
