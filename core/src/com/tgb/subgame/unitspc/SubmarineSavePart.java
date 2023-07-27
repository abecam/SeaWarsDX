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
import java.util.List;

public class SubmarineSavePart extends Saveable
{
	public double maxSpeed=35; // In knots
	public double standardSpeed=20;
	public double silenceSpeed=15;
	
	public int noiseSignature; // How silent this unit can be
	public int visibilitySignature;
	public int noiseLevel; // 0-> None, 100-> enough :)
	public int visibilityLevel; // 100-> Surface (perfectly visible if no fog/clouds), 0-> Very deep (not visible at all)
	
	// If/When there will be a better damage model, we will have to save the following too.
//		ArrayList<Sensor> sensors;
		// The following are not used yet
//		ArrayList weapons;
//		ArrayList countermeasures;
	
	public int typeEnemy; // Mask result to find the enemy. 1 for allies (1 && allies = 1), 0 otherwise (1 && enemy = 0)
	
	public int radarMaxDepth=-10;
	public int missilesMaxDepth=-20;
	
	public int resistance=2;
	
	public int tonnage;
	
	public long cost;
	
	// The different parts, ok or damaged.
	public double hullState=100;
	public double dataLinkState=100;
	public double navState=100;
	
	public boolean damaged=false;	
	
	public boolean unmanned=false;
	
	public int nbTorpedoes;
	public int nbTubes; // Will change the time to fire new torpedoes
	public int nbMissiles;
	public int typeLauncher;
	
	public double timeBetweenLaunchesM;
	public int nbInSalveMaxM=0;
	
	public double timeBetweenLaunchesT;
	public int nbInSalveMaxT=0;
	public double orientation;
	
	public SubmarineSavePart(double maxSpeed, double standardSpeed, double silenceSpeed, int noiseSignature, int visibilitySignature, int noiseLevel, int visibilityLevel, int typeEnemy, int radarMaxDepth, int missilesMaxDepth, int resistance, int tonnage, long cost, double hullState, double dataLinkState, double navState, boolean damaged, boolean unmanned, int nbTorpedoes, int nbTubes, int nbMissiles, int typeLauncher, double timeBetweenLaunchesM, int nbInSalveMaxM, double timeBetweenLaunchesT, int nbInSalveMaxT, double orientation)
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
		this.radarMaxDepth = radarMaxDepth;
		this.missilesMaxDepth = missilesMaxDepth;
		this.resistance = resistance;
		this.tonnage = tonnage;
		this.cost = cost;
		this.hullState = hullState;
		this.dataLinkState = dataLinkState;
		this.navState = navState;
		this.damaged = damaged;
		this.unmanned = unmanned;
		this.nbTorpedoes = nbTorpedoes;
		this.nbTubes = nbTubes;
		this.nbMissiles = nbMissiles;
		this.typeLauncher = typeLauncher;
		this.timeBetweenLaunchesM = timeBetweenLaunchesM;
		this.nbInSalveMaxM = nbInSalveMaxM;
		this.timeBetweenLaunchesT = timeBetweenLaunchesT;
		this.nbInSalveMaxT = nbInSalveMaxT;
		this.orientation = orientation;
	}

	@Override
	public String giveMyId()
	{
		// TODO Auto-generated method stub
		return "SubmarineSavePart";
	}

//	@Override
//	public String save()
//	{
//		StringBuffer toSave = new StringBuffer();
//
//		toSave.append("maxSpeed=");
//		toSave.append(maxSpeed+"\n");
//		
//		toSave.append("standardSpeed=");
//		toSave.append(standardSpeed+"\n");
//		
//		toSave.append("silenceSpeed=");
//		toSave.append(silenceSpeed+"\n");
//		
//		toSave.append("noiseSignature=");
//		toSave.append(noiseSignature+"\n");
//		
//		toSave.append("visibilitySignature=");
//		toSave.append(visibilitySignature+"\n");
//		
//		toSave.append("noiseLevel=");
//		toSave.append(noiseLevel+"\n");
//		
//		toSave.append("visibilityLevel=");
//		toSave.append(visibilityLevel+"\n");
//		
//		// If/When there will be a better damage model, we will have to save the following too.
////		ArrayList<Sensor> sensors;
////		ArrayList weapons;
////		ArrayList countermeasures;
//		
//		toSave.append("typeEnemy=");
//		toSave.append(typeEnemy+"\n");
//		
//		toSave.append("resistance=");
//		toSave.append(resistance+"\n");
//		
//		toSave.append("cost=");
//		toSave.append(cost+"\n");
//		
//		toSave.append("tonnage=");
//		toSave.append(tonnage+"\n");
//		
//		toSave.append("hullState=");
//		toSave.append(hullState+"\n");
//		
//		toSave.append("dataLinkState=");
//		toSave.append(dataLinkState+"\n");
//		
//		toSave.append("navState=");
//		toSave.append(navState+"\n");
//		
//		toSave.append("damaged=");
//		toSave.append(damaged+"\n");
//		
//		toSave.append("unmanned=");
//		toSave.append(unmanned+"\n");
//		
//		toSave.append("nbTorpedoes=");
//		toSave.append(nbTorpedoes+"\n");
//		
//		toSave.append("nbTubes=");
//		toSave.append(nbTubes+"\n");
//		
//		toSave.append("nbMissiles=");
//		toSave.append(nbMissiles+"\n");
//		
//		toSave.append("typeLauncher=");
//		toSave.append(typeLauncher+"\n");
//		
//		toSave.append("timeBetweenLaunchesM=");
//		toSave.append(timeBetweenLaunchesM+"\n");
//		
//		toSave.append("nbInSalveMaxM=");
//		toSave.append(nbInSalveMaxM+"\n");
//		
//		toSave.append("timeBetweenLaunchesT=");
//		toSave.append(timeBetweenLaunchesT+"\n");
//		
//		toSave.append("nbInSalveMaxT=");
//		toSave.append(nbInSalveMaxT+"\n");
//		
//		return toSave.toString();
//	}

	@Override
	public String save()
	{
		StringBuffer toSave = new StringBuffer();

		Field[] listOfAllFields = this.getClass().getFields();
		
		for (Field oneField : listOfAllFields)
		{
			System.out.println("Available field: "+oneField.getName());
			
			toSave.append(oneField.getName()+"=");
			try
			{
				toSave.append(oneField.get(this)+"\n");
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
