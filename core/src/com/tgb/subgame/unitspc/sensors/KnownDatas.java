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

package com.tgb.subgame.unitspc.sensors;

import com.tgb.subgame.unitspc.Base;
import com.tgb.subgame.unitspc.FUnit;
import com.tgb.subgame.unitspc.Submarine;

import com.tgb.subengine.RenderingManager;
import com.tgb.subengine.particlessystem.*;
import com.tgb.subgame.LevelMap;

/**
 * Known data sensor. "Detect" static data, like the bases.
 * 
 * @author Alain Becam
 *
 */
public class KnownDatas extends Sensor
{
	double ourTime=3; // time since last update
	double tmpDetect;
	Base tmpBase;

	@Override
	public void removeMe() {
		// TODO Auto-generated method stub
		; // Do nothing, as we are shared !!!
		// The tactical map will remove us using the next method.
	}
	
	public void removeMeFromMap()
	{
		 dead=true;
		 removeFUnits();
	}
	/**
	 * @param theMap
	 */
	public KnownDatas(LevelMap theMap)
	{
		super(theMap);
		this.setDepth(1);
		ourTime=3;
		
		setUpDetectionTurn();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.units.sensors.Sensor#createGfx(double, double, double, double, double, double)
	 */
	@Override
	public void createGfx(double xInit, double yInit, double zInit, double direction, double puissance, double speed)
	{
		; // Nothing
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.units.sensors.Sensor#doUpdate(double)
	 */
	@Override
	public void doUpdate(double time)
	{
		ourTime+=time;
		
		if (ourTime > 0.1)
		{
			this.setUpDetectionTurn();
			
			ourTime=0;

			updateAttachedObjects();
			
			enemiesBases=theMap.getEnemiesBases();
			alliesBases=theMap.getAlliesBases();
			neutralBases=theMap.getNeutralBases();
			ourBases=theMap.getOurBases();

			if (ourBases != null)
			{
				for (int iBoat = 0; iBoat < ourBases.size() ; iBoat++)
				{
					//System.out.println("Found one of our bases");
					tmpBase=ourBases.get(iBoat);
					if (!tmpBase.isDead())
					{
						FUnit oneFoundBase = new FUnit(hiddenFUnits);

						oneFoundBase.setX(tmpBase.getPosX());
						oneFoundBase.setY(tmpBase.getPosY());
						oneFoundBase.setAngle(tmpBase.getOrientation());
						oneFoundBase.setType(FUnit.BASE_OUR);
						oneFoundBase.setLevelDetection(5);				
						oneFoundBase.updateGfx();
						oneFoundBase.setIdPU(tmpBase.getIdMap());

						this.addFUnit(oneFoundBase);
					}
				}
			}
			if (alliesBases != null)
			{
				for (int iBoat = 0; iBoat < alliesBases.size() ; iBoat++)
				{
					tmpBase=alliesBases.get(iBoat);
					if (!tmpBase.isDead())
					{
						FUnit oneFoundBase = new FUnit(hiddenFUnits);

						oneFoundBase.setX(tmpBase.getPosX());
						oneFoundBase.setY(tmpBase.getPosY());
						oneFoundBase.setAngle(tmpBase.getOrientation());
						oneFoundBase.setType(FUnit.BASE_ALLIED);
						oneFoundBase.setLevelDetection(5);				
						oneFoundBase.updateGfx();
						oneFoundBase.setIdPU(tmpBase.getIdMap());

						this.addFUnit(oneFoundBase);
					}
				}
			}
			if (enemiesBases != null)
			{
				for (int iBoat = 0; iBoat < enemiesBases.size() ; iBoat++)
				{
					tmpBase=enemiesBases.get(iBoat);
					if (!tmpBase.isDead())
					{
						FUnit oneFoundBase = new FUnit(hiddenFUnits);

						oneFoundBase.setX(tmpBase.getPosX());
						oneFoundBase.setY(tmpBase.getPosY());
						oneFoundBase.setAngle(tmpBase.getOrientation());
						oneFoundBase.setType(FUnit.BASE_ENEMY);
						oneFoundBase.setLevelDetection(5);				
						oneFoundBase.updateGfx();
						oneFoundBase.setIdPU(tmpBase.getIdMap());

						this.addFUnit(oneFoundBase);
					}
				}
			}
		}
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Cannot be activated/desactivated
	 */
	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void desactivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAttachableColor(int newColor)
	{
		// TODO Auto-generated method stub
		
	}
}
