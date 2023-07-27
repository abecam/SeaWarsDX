/*
 * Created on 25 oct. 2013
 *
 */

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

package com.tgb.subgame.start;

import javax.swing.JFrame;

import org.pulpcore.platform.desktop.awt.DesktopAwtApplication;

import pulpcore.platform.applet.BufferedImageSurface;


public class StartSeawar extends JFrame
{
	static final int FRAME_WIDTH = 1000;
	static final int FRAME_HEIGHT = 840;
	
	public StartSeawar()
	{	
		super("Sea Wars");
	}
	
	public void initAll()
	{
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setUndecorated(false);

        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        
        DesktopAwtApplication app = new DesktopAwtApplication("Sea Wars", new SeaWars());


        app.init(this);
        app.start();
        app.setVisible(true);	
        this.setVisible(true);
	}
	
	public static void main(String[] args) {
		StartSeawar ourStartDraw = new StartSeawar();
		ourStartDraw.initAll();
	}
}
