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
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.Point;


/**
 * @author Alain Becam
 *
 */
public class LineRadar extends Drawable2DEntity
{
	Point originLine;
	Point endLine;
	Point endLines[];
	Point originLineTmp;
	Point endLineTmp;
	
	Graphics2D graphicsToUse;
	private Color darkGray = Color.DARK_GRAY;
	
	public LineRadar(int lengthTrail)
	{
		super(0,0,1);
		originLine = new Point(0,0);
		endLine = new Point(100,0);
		endLines = new Point[lengthTrail];
		originLineTmp=new Point();
		endLineTmp=new Point();
		
		double distBetweenLines=-1;
		double intervalle=1;
		
		for (int iLine=0;iLine < lengthTrail; iLine++)
		{
			endLines[iLine] = new Point((int )(100*Math.cos(distBetweenLines/40)),(int )(100*Math.sin(distBetweenLines/40)));
			distBetweenLines-=intervalle;
			intervalle+=0.1;
		}
	}
	/* (non-Javadoc)
	 * @see subengine.gfxentities.Drawable2DEntity#drawMe(java.awt.Graphics2D)
	 */
	@Override
	public boolean drawMe()
	{
		graphicsToUse = ourRenderer.getGraphics();
		
		graphicsToUse.setColor(darkGray);
		
		transformToApply.setToIdentity();
		transformToApply.translate(this.getX(), this.getY());
		transformToApply.scale(this.getSize(), this.getSize());
		transformToApply.rotate(this.getRotation()+this.getOrientationAttach(), 0, 0);
		transformToApply.transform(originLine, originLineTmp);
		transformToApply.transform(endLine, endLineTmp);
		graphicsToUse.drawLine(originLineTmp.x, originLineTmp.y, endLineTmp.x, endLineTmp.y);
		for (int iLine=0;iLine < endLines.length; iLine++)
		{
			transformToApply.transform(endLines[iLine], endLineTmp);
			graphicsToUse.drawLine(originLineTmp.x, originLineTmp.y, endLineTmp.x, endLineTmp.y);
		}
		updateAttachedObjects();
		System.out.println("Painted one radar line - non PC");
		return true;
	}
	@Override
	public void setAttachableColor(int newColor)
	{
		;
	}

}
