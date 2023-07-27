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

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import com.tgb.subengine.gameentities.Vector2D;

import java.awt.Toolkit;

import pulpcore.image.CoreImage;
import pulpcore.image.CoreFont;
import pulpcore.util.ByteArray;
import pulpcore.CoreSystem;
import java.io.IOException;
import pulpcore.Build;

/**
 * Utils methods and facilities
 * @author Alain Becam
 *
 */
public class UtilsPC
{

	public static CoreImage loadImage(String name)
	{
		System.out.println("Loading image "+name);
		return load( name,null);
	}
	
   
	public static InputStream getURLInputStream(URL url)
	{
		try
		{
			InputStream fin = url.openStream();
			BufferedInputStream bin = new BufferedInputStream(fin);
			return bin;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
    
    static CoreImage load(String name, CoreFont font) 
    {
    	System.out.println("Loading image "+name);
    	URL imageURL = getResource(name);
        // Attempt to load raw bytes from the asset collection
        ByteArray in = new ByteArray();
        InputStream inStream = getURLInputStream(imageURL);
        try
        {
        	in.write(inStream);
        }
        catch (IOException e)
        {
        	e.printStackTrace();
        	System.out.println("Problem loading image "+name);
        }
        CoreImage image = null;
        // Try the internal image loader
//        if (name.toLowerCase().endsWith(".png")) {
//            try {
//                PNGReader pngReader = new PNGReader();
//                image = pngReader.read(in, font);
//            }
//            catch (IOException ex) {
//                if (Build.DEBUG) CoreSystem.print("Error loading image: " + name, ex);
//            }
//        }
        
        // Try again with the system image loader
        if (image == null) {
            in.reset();
            image = CoreSystem.getThisAppContext().loadImage(in);
            if (image == null) {
                if (Build.DEBUG) CoreSystem.print("Could not load image: " + name);
                return null;
            }
        }
        
        return image;
    }
	

	 /**
     * Return the URL of the filename under the resources directory
     */
    public static URL getResource(String filename)
    {
        // URL url = Resources.class.getResource(filename);
        try
        {
        	if (!filename.startsWith("file:"))
                filename = "file:"+filename;
//            if (!filename.startsWith("file:"))
//                filename = "file:///G:/Dev/SubAndNames/WorkspaceSubAndNames/subenginepc/" + filename;
        	/*if (!filename.startsWith("file:"))
             	filename = "file:///C:/Program/EclipseWTP/eclipse/workspace/" + filename;*/
//            if (!filename.startsWith("file:"))
//            {
//            	filename = "http://www.theGiantBall.com/" + filename;
//                //filename = "http://213.251.185.171/" + filename;
//            }
        	filename = filename.replace("file:", "file:./Assets/");
        	
            URI uri = new URI(filename);
            URL url = uri.toURL();
            return url;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * function to give the angle for the angle of one vector. Can be then compared to the direction
     */ 
    public static double posToAngle(double x, double y, double xT, double yT)
    {
    	double xE, yE, norm;
        double alpha;

        xE = xT - x;
        yE = yT - y;
        norm = Math.sqrt(xE * xE + yE * yE);

        alpha = Math.acos(xE / norm);
        if (yE < 0)
        {
            // Then we are on the other side
            alpha = 2*Math.PI- alpha;
        }
        return alpha;
    }
    /**
     * Check if angleToSub is "inside" dir, return the angle separating them.
     * Not sure if it is all correct (taken from a old, hacky code!)
     * @param dir
     * @param angleToSub
     * @return double the differences between the angles
     */
    public static double compareTwoAngles(double dir, double angleToSub)
    {
    	double dirC = Math.PI-dir;
    	
    	if (dirC < 0)
    		dirC+=2*Math.PI;
    	if (dirC > 2*Math.PI)
    		dirC-=2*Math.PI;
    	double dif;
    	
    	dif = Math.abs(dirC-angleToSub);
    	
    	
    	if (dif > Math.PI)
    	{
    		if (dirC > Math.PI)
    			dif=(angleToSub+2*Math.PI)-dirC; 
    		else
    			dif=(dirC+2*Math.PI)-angleToSub; 
    	}
    	
    	return dif;
    }   
    
    public static double angleBetween2VectorsFromOrigin(Vector2D a, Vector2D b)
    {
    	return (Math.atan2( a.getX()*b.getY() - a.getY()*b.getX(), a.getX()*b.getX() + a.getY()*b.getY() ));
    }
    
    public static double distance (final double x, final double y, final double x2, final double y2)
    {
    	double xT = x2 - x;
    	double yT = y2 - y;
    	return Math.sqrt(Math.pow(xT, 2) + Math.pow(yT,2));
    }
    
    public static double distanceAcc (final double x, final double y, final double x2, final double y2)
    {
    	double xT = x2 - x;
    	double yT = y2 - y;
    	return Math.sqrt(xT * xT + yT * yT);
    }
    
    public static double pow(final double a, final double b) {
        final int x = (int) (Double.doubleToLongBits(a) >> 32);
        final int y = (int) (b * (x - 1072632447) + 1072632447);
        return Double.longBitsToDouble(((long) y) << 32);
    }
    
    public static double calculateAreaTriangle( double xT1, double yT1,double xT2, double yT2,double xT3, double yT3)
    {
    	xT2-=xT1;
    	yT2-=yT1;
    	xT3-=xT1;
    	yT3-=yT1;
    	xT1=0;
    	yT1=0;
    	return (0.5 * Math.abs(xT2*yT3-yT2*xT3));
    }
    public static boolean isInTriangle(double x,double y, double xT1, double yT1,double xT2, double yT2,double xT3, double yT3)
    {
    	// Calculate the three sub-triangle and check the surface.
    	double surfaceTotal;
    	
    	surfaceTotal=calculateAreaTriangle(x,y,xT1,yT1,xT2,yT2);
    	surfaceTotal+=calculateAreaTriangle(x,y,xT2,yT2,xT3,yT3);
    	surfaceTotal+=calculateAreaTriangle(x,y,xT3,yT3,xT1,yT1);
    	
    	if (surfaceTotal > calculateAreaTriangle(xT1,yT1,xT2,yT2,xT3,yT3))
    		return false;
    	else
    		return true;
    }
}
