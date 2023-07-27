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

import java.util.List;

public class BoatSavePart extends Saveable
{
	double maxSpeed; // In knots
	double standardSpeed;
	double silenceSpeed;
	
	int noiseSignature; // How silent this unit can be
	int visibilitySignature;
	int noiseLevel; // 0-> None, 100-> enough :)
	int visibilityLevel; // 100-> Surface (perfectly visible if no fog/clouds), 0-> Very deep (not visible at all)
	
	// If/When there will be a better damage model, we will have to save the following too.
//	ArrayList<Sensor> sensors;
	// The following are not used yet
//	ArrayList weapons;
//	ArrayList countermeasures;
	
	int typeEnemy; // Mask result to find the enemy. 1 for allies (1 && allies = 1), 0 otherwise (1 && enemy = 0)
	
	int resistance=4;
	
	long cost;
	
	int tonnage;
	
	// The different parts, ok or damaged.
	double hullState=100;
	double dataLinkState=100;
	double navState=100;
	
	boolean damaged=false;	
	
	boolean unmanned=false;
	
	int nbTorpedoes;
	int nbTorpedoesMax;
	int nbTubes; // Will change the time to fire new torpedoes
	int nbMissiles;
	int nbMissilesMax;
	int typeLauncher;
	
	double timeBeforeAirLaunches; // Counter between two airplanes launches
	double timeBetweenAirLaunches; // Time between two airplanes launches
	
	int nbAmmo;
	double rangeInterception;
	int explodeTimeMax;
	public double orientation;
	
	public BoatSavePart(double maxSpeed, double standardSpeed, double silenceSpeed, int noiseSignature, int visibilitySignature, int noiseLevel, int visibilityLevel, int typeEnemy, int resistance, long cost, int tonnage, double hullState, double dataLinkState, double navState, boolean damaged, boolean unmanned, int nbTorpedoes, int nbTorpedoesMax, int nbTubes, int nbMissiles, int nbMissilesMax, int typeLauncher, double timeBeforeAirLaunches, double timeBetweenAirLaunches, int nbAmmo, double rangeInterception, int explodeTimeMax, double orientation)
	{
		super();
		this.maxSpeed = maxSpeed;
		this.standardSpeed = standardSpeed;
		this.silenceSpeed = silenceSpeed;
		this.noiseSignature = noiseSignature;
		this.visibilitySignature = visibilitySignature;
		this.noiseLevel = noiseLevel;
		this.visibilityLevel = visibilityLevel;
		this.typeEnemy = typeEnemy;
		this.resistance = resistance;
		this.cost = cost;
		this.tonnage = tonnage;
		this.hullState = hullState;
		this.dataLinkState = dataLinkState;
		this.navState = navState;
		this.damaged = damaged;
		this.unmanned = unmanned;
		this.nbTorpedoes = nbTorpedoes;
		this.nbTorpedoesMax = nbTorpedoesMax;
		this.nbTubes = nbTubes;
		this.nbMissiles = nbMissiles;
		this.nbMissilesMax = nbMissilesMax;
		this.typeLauncher = typeLauncher;
		this.timeBeforeAirLaunches = timeBeforeAirLaunches;
		this.timeBetweenAirLaunches = timeBetweenAirLaunches;
		this.nbAmmo = nbAmmo;
		this.rangeInterception = rangeInterception;
		this.explodeTimeMax = explodeTimeMax;
		this.orientation = orientation;
	}

	@Override
	public String giveMyId()
	{
		// TODO Auto-generated method stub
		return "BoatSavePart";
	}

	@Override
	public String save()
	{
		StringBuffer toSave = new StringBuffer();

		toSave.append("maxSpeed=");
		toSave.append(maxSpeed+"\n");
		
		toSave.append("standardSpeed=");
		toSave.append(standardSpeed+"\n");
		
		toSave.append("silenceSpeed=");
		toSave.append(silenceSpeed+"\n");
		
		toSave.append("noiseSignature=");
		toSave.append(noiseSignature+"\n");
		
		toSave.append("visibilitySignature=");
		toSave.append(visibilitySignature+"\n");
		
		toSave.append("noiseLevel=");
		toSave.append(noiseLevel+"\n");
		
		toSave.append("visibilityLevel=");
		toSave.append(visibilityLevel+"\n");
		
		// If/When there will be a better damage model, we will have to save the following too.
//		ArrayList<Sensor> sensors;
//		ArrayList weapons;
//		ArrayList countermeasures;
		
		toSave.append("typeEnemy=");
		toSave.append(typeEnemy+"\n");
		
		toSave.append("resistance=");
		toSave.append(resistance+"\n");
		
		toSave.append("cost=");
		toSave.append(cost+"\n");
		
		toSave.append("tonnage=");
		toSave.append(tonnage+"\n");
		
		toSave.append("hullState=");
		toSave.append(hullState+"\n");
		
		toSave.append("dataLinkState=");
		toSave.append(dataLinkState+"\n");
		
		toSave.append("navState=");
		toSave.append(navState+"\n");
		
		toSave.append("damaged=");
		toSave.append(damaged+"\n");
		
		toSave.append("unmanned=");
		toSave.append(unmanned+"\n");
		
		toSave.append("nbTorpedoes=");
		toSave.append(nbTorpedoes+"\n");
		
		toSave.append("nbTorpedoesMax=");
		toSave.append(nbTorpedoesMax+"\n");
		
		toSave.append("nbTubes=");
		toSave.append(nbTubes+"\n");
		
		toSave.append("nbMissiles=");
		toSave.append(nbMissiles+"\n");
		
		toSave.append("nbMissilesMax=");
		toSave.append(nbMissilesMax+"\n");
		
		toSave.append("typeLauncher=");
		toSave.append(typeLauncher+"\n");
		
		toSave.append("timeBeforeAirLaunches=");
		toSave.append(timeBeforeAirLaunches+"\n");
		
		toSave.append("timeBetweenAirLaunches=");
		toSave.append(timeBetweenAirLaunches+"\n");
		
		toSave.append("nbAmmo=");
		toSave.append(nbAmmo+"\n");
		
		toSave.append("rangeInterception=");
		toSave.append(rangeInterception+"\n");
		
		toSave.append("explodeTimeMax=");
		toSave.append(explodeTimeMax+"\n");

		toSave.append("orientation=");
		toSave.append(orientation+"\n");
		
		return toSave.toString();
	}

	/** 
	 * We could use reflection to do that automatically
	 * 
	 */
	@Override
	public boolean loadFromOneLine(String oneLine)
	{
		if (oneLine.contains("maxSpeed"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("maxSpeed is "+content);
			maxSpeed = new Double(content);

			return true;
		}

		if (oneLine.contains("standardSpeed"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("standardSpeed is "+content);
			standardSpeed = new Double(content);

			return true;
		}
		
		if (oneLine.contains("silenceSpeed"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("silenceSpeed is "+content);
			silenceSpeed = new Double(content);

			return true;
		}

		
		if (oneLine.contains("noiseSignature"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("noiseSignature is "+content);
			noiseSignature = new Integer(content);

			return true;
		}

		if (oneLine.contains("visibilitySignature"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("visibilitySignature is "+content);
			visibilitySignature = new Integer(content);

			return true;
		}

		if (oneLine.contains("noiseLevel"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("noiseLevel is "+content);
			noiseLevel = new Integer(content);

			return true;
		}
		
		if (oneLine.contains("visibilityLevel"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("visibilityLevel is "+content);
			visibilityLevel = new Integer(content);

			return true;
		}

		if (oneLine.contains("typeEnemy"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("typeEnemy is "+content);
			typeEnemy = new Integer(content);

			return true;
		}
		
		if (oneLine.contains("resistance"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("resistance is "+content);
			resistance = new Integer(content);

			return true;
		}
		
		if (oneLine.contains("cost"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("cost is "+content);
			cost = new Long(content);

			return true;
		}
		
		if (oneLine.contains("tonnage"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("tonnage is "+content);
			tonnage = new Integer(content);

			return true;
		}
		
		if (oneLine.contains("hullState"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("hullState is "+content);
			hullState = new Double(content);

			return true;
		}
		
		if (oneLine.contains("dataLinkState"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("dataLinkState is "+content);
			dataLinkState = new Double(content);

			return true;
		}
		
		if (oneLine.contains("navState"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("navState is "+content);
			navState = new Double(content);

			return true;
		}
		
		if (oneLine.contains("damaged"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("damaged is "+content);
			damaged = new Boolean(content);

			return true;
		}
		
		if (oneLine.contains("unmanned"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("unmanned is "+content);
			unmanned = new Boolean(content);

			return true;
		}
		
		if (oneLine.contains("nbTorpedoes"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbTorpedoes is "+content);
			nbTorpedoes = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbTorpedoesMax"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbTorpedoesMax is "+content);
			nbTorpedoesMax = new Integer(content);

			return true;
		}
		
		if (oneLine.contains("nbTubes"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbTubes is "+content);
			nbTubes = new Integer(content);

			return true;
		}
		
		if (oneLine.contains("nbMissiles"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbMissiles is "+content);
			nbMissiles = new Integer(content);

			return true;
		}
		
		if (oneLine.contains("nbMissilesMax"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbMissilesMax is "+content);
			nbMissilesMax = new Integer(content);

			return true;
		}
		
		if (oneLine.contains("typeLauncher"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("typeLauncher is "+content);
			typeLauncher = new Integer(content);

			return true;
		}
		
		if (oneLine.contains("timeBeforeAirLaunches"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("timeBeforeAirLaunches is "+content);
			timeBeforeAirLaunches = new Double(content);

			return true;
		}

		if (oneLine.contains("timeBetweenAirLaunches"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("timeBetweenAirLaunches is "+content);
			timeBetweenAirLaunches = new Double(content);

			return true;
		}

		if (oneLine.contains("nbAmmo"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbAmmo is "+content);
			nbAmmo = new Integer(content);

			return true;
		}
		
		if (oneLine.contains("rangeInterception"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("rangeInterception is "+content);
			rangeInterception = new Double(content);

			return true;
		}
		
		if (oneLine.contains("explodeTimeMax"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("explodeTimeMax is "+content);
			explodeTimeMax = new Integer(content);

			return true;
		}
		
		if (oneLine.contains("orientation"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("orientation is "+content);
			orientation = new Double(content);

			return true;
		}

		return false;
	}
}
