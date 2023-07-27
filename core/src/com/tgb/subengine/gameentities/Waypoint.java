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

/**
 * @author Alain Becam
 *
 */
public class Waypoint
{
	double xWP;
	double yWP;
	double depthWP;
	
	int nbWP; // number in the journey
	int idWP; // if needed
	
	int mark; // Mark to use at our convenience
	
	public Waypoint(double xWP,double yWP,double depthWP)
	{
		this.xWP = xWP;
		this.yWP = yWP;
		this.depthWP = depthWP;
	}
	
	/**
	 * @return the depthWP
	 */
	public double getDepthWP()
	{
		return depthWP;
	}
	/**
	 * @param depthWP the depthWP to set
	 */
	public void setDepthWP(double depthWP)
	{
		this.depthWP = depthWP;
	}
	/**
	 * @return the idWP
	 */
	public int getIdWP()
	{
		return idWP;
	}
	/**
	 * @param idWP the idWP to set
	 */
	public void setIdWP(int idWP)
	{
		this.idWP = idWP;
	}
	/**
	 * @return the nbWP
	 */
	public int getNbWP()
	{
		return nbWP;
	}
	/**
	 * @param nbWP the nbWP to set
	 */
	public void setNbWP(int nbWP)
	{
		this.nbWP = nbWP;
	}
	/**
	 * @return the xWP
	 */
	public double getXWP()
	{
		return xWP;
	}
	/**
	 * @param xwp the xWP to set
	 */
	public void setXWP(double xwp)
	{
		xWP = xwp;
	}
	/**
	 * @return the yWP
	 */
	public double getYWP()
	{
		return yWP;
	}
	/**
	 * @param ywp the yWP to set
	 */
	public void setYWP(double ywp)
	{
		yWP = ywp;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}
}
