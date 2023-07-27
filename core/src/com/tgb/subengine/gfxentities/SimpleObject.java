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


/**
 * @author Alain Becam
 *
 */
public class SimpleObject extends Drawable2DEntity
{
	Graphics2D graphicsToUse;
	
	public SimpleObject()
	{
		super(0,0,10);
	}

	/* (non-Javadoc)
	 * @see subengine.Drawable2DEntity#drawMe(java.awt.Graphics2D)
	 */
	@Override
	public boolean drawMe()
	{
		graphicsToUse = ourRenderer.getGraphics();
		// TODO Auto-generated method stub
		graphicsToUse.draw3DRect((int )this.getX(), (int )this.getY(), 10, 10, false);
		updateAttachedObjects();
		return false;
	}

	@Override
	public void setAttachableColor(int newColor)
	{
		// TODO Auto-generated method stub
		
	}
}
