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

import java.util.LinkedList;


/**
 * Journey for an entity, collection of waypoints.
 * @author Alain Becam
 *
 */
public class Journey
{
	LinkedList<Waypoint> waypoints;
	
	int nbWPs=0;
	int idWP=0;
	boolean loop=false;
	boolean edited=false; 
	
	public Journey()
	{
		waypoints = new LinkedList<Waypoint>();
	}
	
	public boolean isEmpty()
	{
		if (waypoints.size() == 0)
		{
			return true;
		}
		else
			return false;
	}
	
	public boolean hasOneOrMoreElement()
	{
		if (waypoints.size() >= 1)
		{
			return true;
		}
		else
			return false;
	}
	
	public void addWP(Waypoint newWP)
	{
		newWP.setNbWP(idWP++);
		nbWPs++;
		waypoints.add(newWP);
	}
	
	/**
	 * Remove the given WP, if it exists
	 * @param newWP
	 * @return true if removed, false otherwise
	 */
	public boolean removeWP(Waypoint newWP)
	{
		if (waypoints.remove(newWP))
		{
			nbWPs--;
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Remove the WP at index, if possible
	 * @param index
	 * @return true if removed, false otherwise (invalid index)
	 */
	public boolean removeWP(int index)
	{
		if (index < waypoints.size())
		{
			waypoints.remove(index);
			nbWPs--;
			return true;
		}
		else
			return false;
	}
	
	public Waypoint getWP(int index)
	{
		if ((waypoints.size() > index) && (index >= 0))
			return waypoints.get(index);
		else
			return null;
	}
	
	/**
	 * Return the first WP
	 * @return the first WP.
	 */
	public Waypoint getFirst()
	{
		if (waypoints.size() > 0)
			return waypoints.getFirst();
		else
			return null;
	}
	
	/**
	 * Return the last WP
	 * @return the last WP.
	 */
	public Waypoint getLast()
	{
		if (waypoints.size() > 0)
			return waypoints.getLast();
		else
			return null;
	}
	
	/**
	 * Remove and return the first element
	 * @return the first element or null
	 */
	public Waypoint pull()
	{
		if (waypoints.size() > 0)
		{
			return waypoints.remove();
		}
		else
			return null;
	}
	
	public void clear()
	{
		waypoints.clear();
	}
	
	public int size()
	{
		return waypoints.size();
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}
	
	public boolean isEdited()
	{
		return edited;
	}
	
	public void edit()
	{
		edited=true;
	}
	
	public void unedit()
	{
		edited=false;
	}
}
