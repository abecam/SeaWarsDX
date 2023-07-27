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
 * A set of particles
 * @author Alain Becam
 *
 */
public class Particles
{
	class Modifier
	{
		
	}
	
	class Reflector
	{
		
	}
	
	Particle[] setOfParticles;
	Emitter ourEmitter;
	int nbParticlesAlive;
	int nbParticlesMax;
	int currentParticlePos=0;
	int nbOfParticlesToCreate; // depend of the number available.3
	long idParticles;
	
	double angleNow;
	double xSpeed;
	double ySpeed;
	
	long lastTime;
	long tmpTime;
	long timeInterval;
	
	boolean valide = true;
	
	public Particles(int nbOfParticlesMax, Emitter theEmitter)
	{
		nbOfParticlesMax = ParticleManager.getInstance().requestToAddParticles(nbOfParticlesMax);
		ParticleManager.getInstance().addParticles(nbOfParticlesMax);
		setOfParticles= new Particle[nbOfParticlesMax];
		nbParticlesMax=nbOfParticlesMax;
		ourEmitter = theEmitter;
		nbParticlesAlive=0;
		for (int iParticules=0;iParticules < nbParticlesMax; iParticules++)
		{
			setOfParticles[iParticules]=new ParticlePC();
		}
		
		lastTime=new java.util.Date().getTime();
	}
	
	public Particles(int nbOfParticlesMax, Emitter theEmitter, Class particleClass)
	{
		nbOfParticlesMax = ParticleManager.getInstance().requestToAddParticles(nbOfParticlesMax);
		ParticleManager.getInstance().addParticles(nbOfParticlesMax);
		setOfParticles= new Particle[nbOfParticlesMax];
		nbParticlesMax=nbOfParticlesMax;
		ourEmitter = theEmitter;
		nbParticlesAlive=0;
		for (int iParticules=0;iParticules < nbParticlesMax; iParticules++)
		{
			try
			{
				setOfParticles[iParticules]=(Particle ) particleClass.newInstance();
			} catch (IllegalAccessException e)
			{
				e.printStackTrace();
			} catch (InstantiationException e)
			{
				e.printStackTrace();
			}	
		}
		
		lastTime=new java.util.Date().getTime();
	}

	public Particles(int nbOfParticlesMax, Emitter theEmitter, Particle instanceParticle)
	{
		nbOfParticlesMax = ParticleManager.getInstance().requestToAddParticles(nbOfParticlesMax);
		ParticleManager.getInstance().addParticles(nbOfParticlesMax);
		
		setOfParticles= new Particle[nbOfParticlesMax];
		nbParticlesMax=nbOfParticlesMax;
		ourEmitter = theEmitter;
		nbParticlesAlive=0;
		for (int iParticules=0;iParticules < nbParticlesMax; iParticules++)
		{
			try
			{
				setOfParticles[iParticules]=(Particle )instanceParticle.clone();
			}
			catch (CloneNotSupportedException e)
			{
				System.out.println("Particle not cloneable.");
			}
		}
		
		lastTime=new java.util.Date().getTime();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}
	
	public void drawUs()
	{
		tmpTime=new java.util.Date().getTime();
		timeInterval=tmpTime-lastTime;
		lastTime = tmpTime;
		//System.out.println("Time: "+timeInterval+" nbParticlesAlive "+ nbParticlesAlive+" nbParticlesMax "+ nbParticlesMax);
		// If we are below the max number of particles allowed, emitte some if needed
		if (nbParticlesAlive < nbParticlesMax)
		{
			if (ourEmitter.lastTimeEmitter > ourEmitter.msBetweenFrame)
			{
				if ((nbParticlesMax - nbParticlesAlive) > ourEmitter.partPerFrame)
				{
					nbOfParticlesToCreate = ourEmitter.partPerFrame;
				}
				else
				{
					nbOfParticlesToCreate = nbParticlesMax - nbParticlesAlive;
				}
				//System.out.println(" Will create " + nbOfParticlesToCreate +" particles");
				for (int iParticles=0;iParticles < nbOfParticlesToCreate; iParticles++)
				{
					// Set-up the particle.
					if (ourEmitter.getExampleParticle() == null)
					{
						setOfParticles[currentParticlePos].setPos(ourEmitter.x, ourEmitter.y, ourEmitter.z);
						setOfParticles[currentParticlePos].setEnergy(ourEmitter.energyInitial);
						setOfParticles[currentParticlePos].setWeight(ourEmitter.weightInitial);
						setOfParticles[currentParticlePos].setAlive(true);
						setOfParticles[currentParticlePos].setTimeLeft(ourEmitter.particuleLife);
						setOfParticles[currentParticlePos].setEnergy(ourEmitter.energyInitial);
						setOfParticles[currentParticlePos].setSize(ourEmitter.size);
						//System.out.println(" Time left "+setOfParticles[currentParticlePos].getTimeLeft());
					}
					else
					{
						setOfParticles[currentParticlePos].copyFromExample(ourEmitter.getExampleParticle());
						setOfParticles[currentParticlePos].setPos(ourEmitter.x, ourEmitter.y, ourEmitter.z);
						//System.out.println(" Copy - Time left "+setOfParticles[currentParticlePos].getTimeLeft()+" - "+ourEmitter.getExampleParticle().getTimeLeft());
					}
					
					//System.out.println(" Particle " + iParticules +" created");
					
					switch (ourEmitter.type)
					{
						case Emitter.POINT:
							// Random direction from the origin
							angleNow=2*Math.PI*Math.random();
							xSpeed=0.01*ourEmitter.speed*Math.cos(angleNow);
							ySpeed=0.01*ourEmitter.speed*Math.sin(angleNow);
							setOfParticles[currentParticlePos].setSpeed(xSpeed, ySpeed, 0);
							setOfParticles[currentParticlePos].setOrientation(angleNow);
							break;
						case Emitter.POINT_DIRECTIONNAL:
							// Random direction from the origin in a cone
							angleNow=ourEmitter.orientation+ourEmitter.orientationAttach+(0.5-Math.random())*ourEmitter.angle;
							xSpeed=0.01*ourEmitter.speed*Math.cos(angleNow);
							ySpeed=0.01*ourEmitter.speed*Math.sin(angleNow);
							setOfParticles[currentParticlePos].setSpeed(xSpeed, ySpeed, 0);
							setOfParticles[currentParticlePos].setOrientation(angleNow);
							break;
						case Emitter.LINE:
							// Random creation from the line (for now)
							break;
						case Emitter.RECTANGLE_AREA:
							// Random creation from a area
							break;
					}
					nbParticlesAlive++;
					currentParticlePos++;
					if (currentParticlePos >= nbParticlesMax)
					{
						currentParticlePos = 0;
					}
				}
				ourEmitter.lastTimeEmitter=0;
			}
			else
			{
				ourEmitter.lastTimeEmitter+=timeInterval;
			}
		}
		// Update the particles
		for (int iParticules=0;iParticules < nbParticlesMax; iParticules++)
		{
			//System.out.println(" Particle " + iParticules +" looked at");
			if (setOfParticles[iParticules].isAlive())
			{
				//System.out.println(" Particle " + iParticules +" managed");
				// Update and draw
				//System.out.println(timeInterval+" ms passed");
				if (setOfParticles[iParticules].removeTime(timeInterval) < 0)
				{
					// Particle is dead
					setOfParticles[iParticules].setAlive(false);
					nbParticlesAlive--;
					//System.out.println(" Particle " + iParticules +" killed");
				}
				else
				{
					//System.out.println(" Particle " + iParticules +" drawn");
					setOfParticles[iParticules].drawMe(timeInterval);
				}
			}
		}
	}

	/**
	 * @return the idParticles
	 */
	public long getIdParticles()
	{
		return idParticles;
	}

	/**
	 * @param idParticles the idParticles to set
	 */
	public void setIdParticles(long idParticles)
	{
		this.idParticles = idParticles;
	}
	
	/**
	 * @return the valide
	 */
	public boolean isValide()
	{
		return valide;
	}

	/**
	 * validate the particles
	 */
	public void validate()
	{
		this.valide = true;
	}
	
	/**
	 * invalidate the particles
	 */
	public void invalidate()
	{
		this.valide = false;
	}
	
	/**
	 * Remove me from the global set of particles
	 */
	public void removeMe()
	{
		ParticleManager.getInstance().removeParticles(nbParticlesMax);
	}
}
