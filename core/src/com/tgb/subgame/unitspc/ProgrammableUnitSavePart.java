/*
 * Created on 21 oct. 2019
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

package com.tgb.subgame.unitspc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ProgrammableUnitSavePart extends Saveable
{
	/**
	 * All fields public to allow loading/saving using reflection, only primitive or simple type!
	 */
	public int idBoat=-1;
	public int idOwner; // Who own it.
	
	
	/**
	 * Groups are *not* supported by the reflection based loading!!!
	 */
	public int idGroup=-1; // If I am part of a group (typically a carrier with cover units).
	// Is also the ID of the main unit!!!
	ArrayList<ProgrammableUnit> myGroup; // If leader, the group
	public boolean isLeaderOfGroup=false;
	
	public String name;
	
	public long XP;
	public long level;
	public long newLevelReachable;
	
	public int energy;
	public int energyMax;
	
	public int type;
	public int typeFaction; // As defined in FUnit
	
	public int nbFighters; // If it is a carrier or a base.
	public int nbFightersMax;
	public int nbFightersOnFlightMax;
	public int currentNbFightersInFlight;
	public int nbAwacs;
	public int nbAwacsSession; // nb of awacs for one level, they might be regenerated (base or close to a base)
	public int nbTankers;
	
	
	public double xMap,yMap; // Position on the global map
	
	public long complement;
	public long complementNorm;
	public  boolean dead = false;
	public  double posX;
	public  double posY;
	public  double depth;

	public  double rudderEfficiency;
	public double actualSpeed;
	public double angleToTurn; // Angle d�sir� pour tourner
	
	// Is this unit limited by the visible level boundaries? (also excluding the right menu)
	public boolean boundaryLimited=true;
	// Do this unit send its airplanes (if available)?
	public boolean flightsOn=true;
	
	public boolean autonomous;
	public boolean fireAtWill;
	public boolean ignoreFriends;
	
	public ProgrammableUnitSavePart(int idBoat, int idOwner, int idGroup, ArrayList<ProgrammableUnit> myGroup, boolean isLeaderOfGroup, String name, long xP, long level, long newLevelReachable, int energy, int energyMax, int type, int typeFaction, int nbFighters, int nbFightersMax, int nbFightersOnFlightMax, int currentNbFightersInFlight, int nbAwacs, int nbAwacsSession, int nbTankers, double xMap, double yMap, long complement, long complementNorm, boolean dead, double posX, double posY, double depth, double rudderEfficiency, double actualSpeed, double angleToTurn, boolean boundaryLimited, boolean flightsOn,boolean autonomous, boolean fireAtWill, boolean ignoreFriends)
	{
		this.idBoat = idBoat;
		this.idOwner = idOwner;
		this.idGroup = idGroup;
		this.myGroup = myGroup;
		this.isLeaderOfGroup = isLeaderOfGroup;
		this.name = name;
		XP = xP;
		this.level = level;
		this.newLevelReachable = newLevelReachable;
		this.energy = energy;
		this.energyMax = energyMax;
		this.type = type;
		this.typeFaction = typeFaction;
		this.nbFighters = nbFighters;
		this.nbFightersMax = nbFightersMax;
		this.nbFightersOnFlightMax = nbFightersOnFlightMax;
		this.currentNbFightersInFlight = currentNbFightersInFlight;
		this.nbAwacs = nbAwacs;
		this.nbAwacsSession = nbAwacsSession;
		this.nbTankers = nbTankers;
		this.xMap = xMap;
		this.yMap = yMap;
		this.complement = complement;
		this.complementNorm = complementNorm;
		this.dead = dead;
		this.posX = posX;
		this.posY = posY;
		this.depth = depth;
		this.rudderEfficiency = rudderEfficiency;
		this.actualSpeed = actualSpeed;
		this.angleToTurn = angleToTurn;
		this.boundaryLimited = boundaryLimited;
		this.flightsOn = flightsOn;
		this.autonomous = autonomous;
		this.fireAtWill = fireAtWill;
		this.ignoreFriends = ignoreFriends;
	}
	
	@Override
	public String giveMyId()
	{
		// TODO Auto-generated method stub
		return "ProgrammableUnitFields";
	}

	@Override
	public String save()
	{
		StringBuffer toSave = new StringBuffer();
		
		toSave.append("*"+giveMyId()+"\n");
		
		toSave.append("idBoat=");
		toSave.append(idBoat+"\n");
		
		toSave.append("idOwner=");
		toSave.append(idOwner+"\n");
		
		toSave.append("idGroup=");
		toSave.append(idGroup+"\n");
		if (myGroup != null)
		{
			toSave.append("*group\n");
		
			for (ProgrammableUnit onProgrammableUnit: myGroup)
			{
				toSave.append(onProgrammableUnit.saveAll()+"\n");
			}
		}
		toSave.append("isLeaderOfGroup=");
		toSave.append(isLeaderOfGroup+"\n");

		toSave.append("name=");
		toSave.append(name+"\n");
	
		toSave.append("XP=");
		toSave.append(XP+"\n");
		
		toSave.append("level=");
		toSave.append(level+"\n");
		
		toSave.append("newLevelReachable=");
		toSave.append(newLevelReachable+"\n");
		
		toSave.append("energy=");
		toSave.append(energy+"\n");

		toSave.append("energyMax=");
		toSave.append(energyMax+"\n");

		toSave.append("type=");
		toSave.append(type+"\n");
		
		toSave.append("typeFaction=");
		toSave.append(typeFaction+"\n");
		
		toSave.append("nbFighters=");
		toSave.append(nbFighters+"\n");
			
		toSave.append("nbFightersMax=");
		toSave.append(nbFightersMax+"\n");
		
		toSave.append("nbFightersOnFlightMax=");
		toSave.append(nbFightersOnFlightMax+"\n");
		
		toSave.append("nbAwacs=");
		toSave.append(nbAwacs+"\n");
		
		toSave.append("nbAwacsSession=");
		toSave.append(nbAwacsSession+"\n");
				
		toSave.append("nbTankers=");
		toSave.append(nbTankers+"\n");
		
		toSave.append("xMap=");
		toSave.append(xMap+"\n");
		
		toSave.append("yMap=");
		toSave.append(yMap+"\n");
		
		toSave.append("complement=");
		toSave.append(complement+"\n");
		
		toSave.append("complementNorm=");
		toSave.append(complementNorm+"\n");
		
		toSave.append("dead=");
		toSave.append(dead+"\n");
		
		toSave.append("rudderEfficiency=");
		toSave.append(rudderEfficiency+"\n");
		
		toSave.append("actualSpeed=");
		toSave.append(actualSpeed+"\n");
		
		toSave.append("angleToTurn=");
		toSave.append(angleToTurn+"\n");
		
		toSave.append("boundaryLimited=");
		toSave.append(boundaryLimited+"\n");
		
		toSave.append("flightsOn=");
		toSave.append(flightsOn+"\n");
		
		toSave.append("autonomous=");
		toSave.append(autonomous+"\n");
		
		toSave.append("fireAtWill=");
		toSave.append(fireAtWill+"\n");
		
		toSave.append("ignoreFriends=");
		toSave.append(ignoreFriends+"\n");
		
		return toSave.toString();
	}

	@Override
	public boolean loadFromOneLine(String oneLine)
	{
		if (oneLine.contains("="))
		{
			String nameOfField = oneLine.substring(0, oneLine.indexOf("="));

			try
			{
				String content=oneLine.substring(oneLine.indexOf("=")+1);

				Field foundField = this.getClass().getField(nameOfField);

				if (foundField.getType() == String.class)
				{
					try
					{
						System.out.println(nameOfField+ " set to " + content +" as "+ foundField.getType());

						foundField.set(this, content);
						
						return true;
					} catch (IllegalArgumentException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (foundField.getType() == int.class)
				{
					int value = new Integer(content);

					try
					{
						System.out.println(nameOfField+ " set to " + value +" as "+ foundField.getType());

						foundField.set(this, value);
						
						return true;
					} catch (IllegalArgumentException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (foundField.getType() == long.class)
				{
					long value = new Long(content);

					try
					{
						System.out.println(nameOfField+ " set to " + value +" as "+ foundField.getType());

						foundField.set(this, value);
						
						return true;
					} catch (IllegalArgumentException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (foundField.getType() == double.class)
				{
					Double value = new Double(content);

					try
					{
						System.out.println(nameOfField+ " set to " + value +" as "+ foundField.getType());

						foundField.set(this, value);
						
						return true;
					} catch (IllegalArgumentException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (foundField.getType() == boolean.class)
				{
					boolean value = new Boolean(content);

					try
					{
						System.out.println(nameOfField+ " set to " + value +" as "+ foundField.getType());

						foundField.set(this, value);
						
						return true;
					} catch (IllegalArgumentException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (NoSuchFieldException e)
			{
				System.out.println("This field was not found in "+giveMyId()+" : "+nameOfField);
			} catch (SecurityException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return false;
	}

}
