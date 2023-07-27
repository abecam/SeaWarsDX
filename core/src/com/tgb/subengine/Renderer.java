/*
 * This software is distributed under the MIT License
 *
 * Copyright (c) 2008-2020 Alain Becam
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

package com.tgb.subengine;

import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.tgb.subengine.gfxentities.IListenerGfxChanges;

/**
 * Class to define a Graphics2D renderer, to inherit to create new renderer
 * elements to do the rendering
 * @author Alain Becam
 *
 */
public class Renderer
{
	private static Renderer instance = null;
	Graphics2D ourGraphics;
	// The list of GfxChangesListeners, drawable entities (mostly) that want some updates.
	ArrayList<IListenerGfxChanges> GfxChangesListeners;
	
	private Renderer()
	{
		GfxChangesListeners = new ArrayList<IListenerGfxChanges>();
	}
	
	public synchronized static Renderer getInstance()
	{
		if (instance == null)
		{
			instance = new Renderer();
		}
		return instance;
	}
	
	public void registerListener(IListenerGfxChanges oneListener)
	{
		GfxChangesListeners.add(oneListener);
	}
	
	public void removeListener(IListenerGfxChanges oneListener)
	{
		GfxChangesListeners.remove(oneListener);
	}
	
	// Propagate a change of size.
	public void changeSize(int width, int height)
	{
		for (int iListener=0;iListener<GfxChangesListeners.size();iListener++)
		{
			GfxChangesListeners.get(iListener).changeSize(width, height);
		}
	}
	
	public void setGraphics(Graphics2D newGraphics)
	{
		ourGraphics=newGraphics;
	}
	
	public Graphics2D getGraphics()
	{
		ourGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		return ourGraphics;
	}
}
