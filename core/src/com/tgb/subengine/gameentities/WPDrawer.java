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

package com.tgb.subengine.gameentities;

import pulpcore.image.CoreImage;

import com.tgb.subengine.*;
import com.tgb.subengine.gfxentities.*;
import com.tgb.subengine.gameentities.*;
import com.tgb.subgame.unitspc.gfxSelections;
import com.tgb.subgame.unitspc.gfxSprites;

/**
 * Utility class to draw a set of Waypoints, recovered via TacticalMap in the subGame
 * @author Alain Becam
 *
 */
public class WPDrawer {
	long idDrawedWP[];
	long idEdges[];
	SpritePC DrawableWP[];
	SimpleLinePC drawableEdge[];
	
	public WPDrawer()
	{
		idDrawedWP = new long[300];
		idEdges = new long[301];
		DrawableWP = new SpritePC[300];
		drawableEdge = new SimpleLinePC[301];
		gfxSelections.callMeFirst();
		gfxSprites.callMeFirst();
		CoreImage imageFirstWP = gfxSelections.ourSelImage;
		CoreImage imageWP = gfxSprites.getImageWP();
		
		// First one.
		DrawableWP[0]=new SpritePC(imageFirstWP);
		DrawableWP[0].setSize(0.75);
		DrawableWP[0].invalidate();
		idDrawedWP[0] = RenderingManager.getInstance().addDrawableEntity(DrawableWP[0],39);
		drawableEdge[0]=new SimpleLinePC();
		drawableEdge[0].invalidate();
		idEdges[0] = RenderingManager.getInstance().addDrawableEntity(drawableEdge[0],39);
		
		for (int iWP=1;iWP < 300;iWP++)
		{
			DrawableWP[iWP]=new SpritePC(imageWP);
			DrawableWP[iWP].invalidate();
			idDrawedWP[iWP] = RenderingManager.getInstance().addDrawableEntity(DrawableWP[iWP],39);
			drawableEdge[iWP]=new SimpleLinePC();
			drawableEdge[iWP].invalidate();
			idEdges[iWP] = RenderingManager.getInstance().addDrawableEntity(drawableEdge[iWP],39);
		}
		drawableEdge[300]=new SimpleLinePC();
		drawableEdge[300].invalidate();
		idEdges[300] = RenderingManager.getInstance().addDrawableEntity(drawableEdge[300],39);
	}
	
	public void setUpAndDrawWPs(Journey wps)
	{
		hideMe();
		//System.out.println("Draw "+wps.size()+ " WPs");
		Waypoint tmpWP;

		if (wps != null)
		{
			if (wps.size() > 0)
			{
				for (int iWP=0;iWP < wps.size();iWP++)
				{
					tmpWP=wps.getWP(iWP);
					DrawableWP[iWP].setPos(tmpWP.getXWP(), tmpWP.getYWP(), 0);
					DrawableWP[iWP].validate();

					if (iWP>0)
					{
						drawableEdge[iWP].validate();
						drawableEdge[iWP-1].setPosEnd(tmpWP.getXWP(), tmpWP.getYWP());
						drawableEdge[iWP].setPos(tmpWP.getXWP(), tmpWP.getYWP(),0);
					}
					else
					{
						drawableEdge[iWP].setPos(tmpWP.getXWP(), tmpWP.getYWP(),0);
						drawableEdge[iWP].setPosEnd(tmpWP.getXWP(), tmpWP.getYWP());
						drawableEdge[iWP].validate();
					}
				}
				if (wps.isLoop())
				{
					drawableEdge[wps.size()-1].setPos(wps.getWP(wps.size()-1).getXWP(), wps.getWP(wps.size()-1).getYWP(), 0);
					drawableEdge[wps.size()-1].setPosEnd(wps.getWP(0).getXWP(), wps.getWP(0).getYWP());
					drawableEdge[wps.size()-1].validate();
				}
				else
				{
					// Do nothing
					drawableEdge[wps.size()-1].invalidate();
				}

				if (wps.size() < 300)
				{
					DrawableWP[wps.size()].invalidate();
				}
				for (int iWP=wps.size()+1;iWP < 300;iWP++)
				{
					DrawableWP[iWP].invalidate();
					drawableEdge[iWP].invalidate();
				}
			}
		}
	}
	
	public void hideMe()
	{
		for (int iWP=0;iWP < 300;iWP++)
		{
			DrawableWP[iWP].invalidate();
			drawableEdge[iWP].invalidate();	
		}
		drawableEdge[300].invalidate();
	}
	
	public void removeMe()
	{
		for (int iWP=0;iWP < 300;iWP++)
		{
			RenderingManager.getInstance().removeEntity(idDrawedWP[iWP], 39);
			RenderingManager.getInstance().removeEntity(idEdges[iWP], 39);
		}
		RenderingManager.getInstance().removeEntity(idEdges[300], 39);
	}
}
