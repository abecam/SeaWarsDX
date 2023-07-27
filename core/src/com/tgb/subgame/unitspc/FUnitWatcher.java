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

/**
 * The FUnitWatcher allows to keep the number of Found Units under control.
 * Unfortunately it breaks the game mechanics :), so it is not used yet.
 * Copyright (c)2008 Alain Becam
 */

package com.tgb.subgame.unitspc;

public class FUnitWatcher
{

	int nbMaxOfFUnits = 10000;
	
	int currentNbOfFUnits=0;
	
	int nbCallPlusMinus = 0;
	
	private static FUnitWatcher instance=null;
	
	private FUnitWatcher()
	{
		nbMaxOfFUnits = 10000;
		currentNbOfFUnits=0;
	}
	
	static public FUnitWatcher getInstance()
	{
		if (instance == null)
		{
			instance = new FUnitWatcher();
		}
		return instance;
	}
	
	/**
	 * Reset the count
	 */
	public void reset()
	{
		nbMaxOfFUnits = 10000;
		currentNbOfFUnits=0;
	}
	/**
	 * Remove the instance, so it can be garbage-collected
	 */
	public synchronized static void removeMe()
	{
		instance = null;
	}

	/**
	 * Check how many FUnits can be added, and send the number that can be added.
	 * If it is 0, you cannot do it !
	 * @param nbOfFUnits
	 * @return int the actual number allowed to be added
	 */
	public synchronized int requestToAddFUnits(int nbOfFUnits)
	{
		int addedNbOfFUnits=currentNbOfFUnits+nbOfFUnits;
		
		if (addedNbOfFUnits > nbMaxOfFUnits)
		{
			return (nbMaxOfFUnits-currentNbOfFUnits);
		}
		else
		{
			return nbOfFUnits;
		}
	}
	
	public synchronized int addFUnits(int nbOfFUnits)
	{
		nbCallPlusMinus++;
		//System.out.println(nbCallPlusMinus+ " Add "+ nbOfFUnits + " FUnits - "+currentNbOfFUnits+" / "+nbMaxOfFUnits);
		int addedNbOfFUnits=currentNbOfFUnits+nbOfFUnits;
		
//		if (addedNbOfFUnits > nbMaxOfFUnits)
//		{
//			currentNbOfFUnits = nbMaxOfFUnits;
//			return -1;
//		}
//		else
//		{
//			currentNbOfFUnits+=nbOfFUnits;
//			return 0;
//		}

		currentNbOfFUnits+=nbOfFUnits;
		
		if (currentNbOfFUnits > nbMaxOfFUnits)
		{
			nbMaxOfFUnits=currentNbOfFUnits;
			//System.out.println(nbCallPlusMinus+ " Add "+ nbOfFUnits + " FUnits - "+currentNbOfFUnits+" / "+nbMaxOfFUnits);
		}
		return 0;
	}
	
	public synchronized int removeFUnits(int nbOfFUnits)
	{
		nbCallPlusMinus--;
		int addedNbOfFUnits=currentNbOfFUnits-nbOfFUnits;
		//System.out.println(nbCallPlusMinus+ " Remove "+ nbOfFUnits + " FUnits - "+currentNbOfFUnits+" / "+nbMaxOfFUnits);
//		if (addedNbOfFUnits < 0)
//		{
//			currentNbOfFUnits = 0;
//			return -1;
//		}
//		else
//		{
//			currentNbOfFUnits-=nbOfFUnits;
//			return 0;
//		}
		currentNbOfFUnits-=nbOfFUnits;
		if (currentNbOfFUnits < 200)
		{
			//System.out.println(nbCallPlusMinus+ " Remove "+ nbOfFUnits + " FUnits - "+currentNbOfFUnits+" / "+nbMaxOfFUnits);
		}
		return 0;
	}
}
