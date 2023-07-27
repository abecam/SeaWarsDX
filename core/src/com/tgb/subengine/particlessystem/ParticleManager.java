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

/**
 * Manage the particles system, mostly limit the number of particles.
 * Ideally should become smart (by evaluating how many particles can be managed)
 * @author Alain Becam
 *
 */
public class ParticleManager {
	
	private static final int NB_MAX_PARTICLES = 500000;

	int nbMaxOfParticles = 500000;
	
	int currentNbOfParticles=0;
	
	//int nbCallPlusMinus = 0;
	
	private static ParticleManager instance=null;
	
	private ParticleManager()
	{
		nbMaxOfParticles = NB_MAX_PARTICLES;
		currentNbOfParticles=0;
	}
	
	static public ParticleManager getInstance()
	{
		if (instance == null)
		{
			instance = new ParticleManager();
		}
		return instance;
	}
	
	public void setMaxNbOfParticles(int newMax)
	{
		nbMaxOfParticles = newMax;
	}
	
	/**
	 * Reset the count
	 */
	public synchronized void reset()
	{
		nbMaxOfParticles = NB_MAX_PARTICLES;
		currentNbOfParticles=0;
	}
	/**
	 * Remove the instance, so it can be garbage-collected
	 */
	public synchronized static void removeMe()
	{
		instance = null;
	}

	/**
	 * Check how many particles can be added, and send the number that can be added.
	 * If it is 0, you cannot do it !
	 * @param nbOfParticles
	 * @return int the actual number allowed to be added
	 */
	public synchronized int requestToAddParticles(int nbOfParticles)
	{
		int addedNbOfParticles=currentNbOfParticles+nbOfParticles;
		
		if (addedNbOfParticles > nbMaxOfParticles)
		{
			return (nbMaxOfParticles-currentNbOfParticles);
		}
		else
		{
			return nbOfParticles;
		}
	}
	
	public synchronized int addParticles(int nbOfParticles)
	{
		//nbCallPlusMinus++;
		//System.out.println(nbCallPlusMinus+ " Add "+ nbOfParticles + " particles - "+currentNbOfParticles+" / "+nbMaxOfParticles);
		int addedNbOfParticles=currentNbOfParticles+nbOfParticles;
		
		if (addedNbOfParticles > nbMaxOfParticles)
		{
			currentNbOfParticles = nbMaxOfParticles;
			return -1;
		}
		else
		{
			currentNbOfParticles+=nbOfParticles;
			return 0;
		}
	}
	
	public synchronized int removeParticles(int nbOfParticles)
	{
		//nbCallPlusMinus--;
		int addedNbOfParticles=currentNbOfParticles-nbOfParticles;
		//System.out.println(nbCallPlusMinus+ " Remove "+ nbOfParticles + " particles - "+currentNbOfParticles+" / "+nbMaxOfParticles);
		if (addedNbOfParticles < 0)
		{
			currentNbOfParticles = 0;
			return -1;
		}
		else
		{
			currentNbOfParticles-=nbOfParticles;
			return 0;
		}
	}
}
