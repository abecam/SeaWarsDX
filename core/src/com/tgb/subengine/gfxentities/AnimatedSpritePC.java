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
import pulpcore.image.AnimatedImage;
import pulpcore.image.CoreGraphics;
import pulpcore.math.*;


/**
 * A sprite, that can be translated, rotated and resized.
 * @author Alain Becam
 *
 */
public class AnimatedSpritePC extends Drawable2DEntity
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
	AnimatedImage ourImage ;
	AnimatedImage imageToDraw ;
	CoreGraphics graphicsToUse;
	RendererPulpeC ourRenderer;
	int iFrame=0;
	boolean automaticPlay=true;
	boolean reverse=false;
	
	int step=1;
	
	public AnimatedSpritePC(CoreImage imageToUse,int nbSubImagesX,int nbSubImagesY)
	{
		super(0,0,1);
		super.setAlpha(255);
		
		iFrame = 0;
		automaticPlay=true;
		 
		ourRenderer = RendererPulpeC.getInstance();
		
		ourImage = new AnimatedImage(imageToUse, nbSubImagesX, nbSubImagesY);
		imageToDraw = ourImage;
	}
	
	public CoreImage getImageToDraw() {
		return imageToDraw;
	}
	
	public void setImageToDraw(CoreImage imageToUse,int nbSubImagesX,int nbSubImagesY) 
	{
		ourImage = new AnimatedImage(imageToUse, nbSubImagesX, nbSubImagesY);
		imageToDraw = ourImage;
		imageToDraw = ourImage;
	}
	
	/**
	 * @return the automaticPlay
	 */
	public boolean isAutomaticPlay()
	{
		return automaticPlay;
	}
	/**
	 * @param automaticPlay the automaticPlay to set
	 */
	public void setAutomaticPlay(boolean automaticPlay)
	{
		this.automaticPlay = automaticPlay;
	}
	
	/**
	 * @return the reverse
	 */
	public boolean isReverse()
	{
		return reverse;
	}
	/**
	 * @param reverse the reverse to set
	 */
	public void setReverse(boolean reverse)
	{
		this.reverse = reverse;
	}
	
	
	/**
	 * @return the step
	 */
	public int getStep()
	{
		return step;
	}
	/**
	 * @param step the step to set
	 */
	public void setStep(int step)
	{
		this.step = step;
	}
	
	public void setImageToDraw(AnimatedImage imageToUse) 
	{
		ourImage = imageToUse;
	}
	
	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
		super.setAlpha(alpha);
		
		//imageToDraw = ourImage.fade(alpha);
	}
	/* (non-Javadoc)
	 * @see subengine.gfxentities.Drawable2DEntity#drawMe(java.awt.Graphics2D)
	 */
	@Override
	public boolean drawMe()
	{
		graphicsToUse = ourRenderer.getGraphics();
		
		graphicsToUse.drawRotatedImage(imageToDraw.getImage(iFrame), (int )(this.getX()-this.getSize()*ourImage.getWidth()/2), (int )(this.getY()-this.getSize()*ourImage.getHeight()/2), (int )(ourImage.getWidth()*this.getSize()), (int )(ourImage.getHeight()*this.getSize()), CoreMath.toFixed(this.getRotation()+this.getOrientationAttach()));
		
		updateAttachedObjects();
		
		if (automaticPlay)
		{
			if (reverse)
			{
				iFrame-=step;
				if (iFrame < 0)
				{
					iFrame=imageToDraw.getNumFrames()-1;
				}
			}
			else
			{
				iFrame+=step;
				if (iFrame >= imageToDraw.getNumFrames())
				{
					iFrame=0;
				}
			}	
		}
		//System.out.println("Painted one sprite - PC");
		return true;
	}
	@Override
	public void setAttachableColor(int ourColor)
	{
		// TODO Auto-generated method stub
		
	}

}
