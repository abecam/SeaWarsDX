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

import pulpcore.image.CoreImage;

import com.tgb.subengine.gfxentities.SpritePCAdd;

/**
 * Sprite particle with "add" blend mode.
 * @author Alain Becam
 *
 */
public class ParticleSpriteAdd extends ParticlePC implements Cloneable
{
	private SpritePCAdd ourSprite;
	
	public ParticleSpriteAdd(CoreImage imageToUse)
	{
		super();
		ourSprite = new SpritePCAdd(imageToUse);
	}
	
	public ParticleSpriteAdd(SpritePCAdd spriteToUse)
	{
		super();
		ourSprite = spriteToUse;
	}
	
	public boolean drawMe(long time)
	{
		//System.out.println("Will paint one particle sprite - PC");
		graphicsToUse= ourRenderer.getGraphics();
		
		// Simple draw, a point or a circle
		//graphicsToUse.setColor(Color.RED);
		//System.out.println("Draw here : "+(int )(this.x-this.size/2)+" : "+(int )(this.y-this.size/2)+" : "+ (int )this.size+" : "+ (int )this.size+" || "+this.x+" - "+this.y);
		ourSprite.setX(this.getX());
		ourSprite.setY(this.getY());
		ourSprite.setZ(this.getZ());
		ourSprite.setSize(this.getSize());
		ourSprite.setRotation(this.getOrientation());
		ourSprite.drawMe();

		update(time);
		//System.out.println("Painted one particle sprite - PC");
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		ParticleSpriteAdd newCopy=(ParticleSpriteAdd )super.clone();
		
		newCopy.ourSprite = this.ourSprite;
		
		// TODO Auto-generated method stub
		return newCopy;
	}
}
