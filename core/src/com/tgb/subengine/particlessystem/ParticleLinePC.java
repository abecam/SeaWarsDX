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

import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import pulpcore.image.CoreGraphics;

import com.tgb.subengine.RendererPulpeC;

/**
 * A particle composed of a line.
 * @author Alain Becam
 *
 */
public class ParticleLinePC extends ParticlePC implements Cloneable
{
	Point originLine;
	Point endLine;
	Point originLineTmp;
	Point endLineTmp;
	AffineTransform transformToApply; // Common affine transform
	
	RendererPulpeC ourRenderer;
	
	public ParticleLinePC()
	{
		super();
		ourRenderer = RendererPulpeC.getInstance();
		
		originLine = new Point(-1000,0);
		endLine = new Point(1000,0);
		originLineTmp=new Point();
		endLineTmp=new Point();
		
		transformToApply = new AffineTransform();
	}
	
	public boolean drawMe(long time)
	{
		graphicsToUse= ourRenderer.getGraphics();
			
		graphicsToUse.setColor(0xFF222222);
		//System.out.println("Orientation "+this.getOrientation());
		transformToApply.setToIdentity();
		transformToApply.translate(this.getX(), this.getY());
		transformToApply.scale(this.getSize(), this.getSize());
		transformToApply.rotate(this.getOrientation()+Math.PI/2, 0, 0);		
		transformToApply.transform(originLine, originLineTmp);
		transformToApply.transform(endLine, endLineTmp);
		graphicsToUse.drawLine(originLineTmp.x, originLineTmp.y, endLineTmp.x, endLineTmp.y);
		//System.out.println("Painted one particle Line - PC");
		update(time);
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		ParticleLinePC newCopy=(ParticleLinePC )super.clone();
		
		
		// TODO Auto-generated method stub
		return newCopy;
	}
}
