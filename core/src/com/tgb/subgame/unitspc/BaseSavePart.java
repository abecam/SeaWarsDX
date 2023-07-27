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

public class BaseSavePart extends Saveable
{
	// The different parts, ok or damaged.
	public double baseState=100;
	public double dataLinkState=100;
	public double navState=100;

	public boolean damaged=false;	

	public boolean unmanned=false;

	public double iDead; // to sunk

	public int resistance=4;
	
	public int cost;
	
	public int nbMissiles;
	public double timeBetweenLaunchesM;
	public double timeBeforeAirLaunches;
	
	public BaseSavePart(double baseState, double dataLinkState, double navState, boolean damaged, boolean unmanned, double iDead,
			int resistance, int cost, int nbMissiles, double timeBetweenLaunchesM, double timeBeforeAirLaunches)
	{
		super();
		this.baseState = baseState;
		this.dataLinkState = dataLinkState;
		this.navState = navState;
		this.damaged = damaged;
		this.unmanned = unmanned;
		this.iDead = iDead;
		
		this.resistance = resistance;
		this.cost = cost;
		this.nbMissiles = nbMissiles;
		this.timeBetweenLaunchesM = timeBetweenLaunchesM;
		this.timeBeforeAirLaunches = timeBeforeAirLaunches;
	}

	@Override
	public String giveMyId()
	{
		// TODO Auto-generated method stub
		return "BaseSavePart";
	}

	@Override
//	public String save()
//	{
//		StringBuffer toSave = new StringBuffer();
//		
//		toSave.append("*"+giveMyId()+"\n");
//		
//		toSave.append("baseState=");
//		toSave.append(baseState+"\n");
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
//		toSave.append("iDead=");
//		toSave.append(iDead+"\n");
//		
//		toSave.append(giveMyId()+"/"+"\n");
//		
//		return toSave.toString();
//	}

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
