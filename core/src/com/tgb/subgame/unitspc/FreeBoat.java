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
 * Boat which is able to go everywhere (not limited to game boundaries)
 * Copyright (c)2008 Alain Becam
 */

package com.tgb.subgame.unitspc;

import com.tgb.subengine.gameentities.Waypoint;
import com.tgb.subgame.LevelMap;
import com.tgb.subgame.unitspc.sensors.DeportedRadar;
import com.tgb.subgame.unitspc.sensors.SimpleAround;

public class FreeBoat extends Boat
{

	public FreeBoat()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public FreeBoat(LevelMap theMap)
	{
		super(theMap);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doUpdate(double time)
	{
		if (!dead)
		{
			//System.out.println("Energy left "+ energyLeft);
			// Launch a airplane ???
			if (loadingA)
			{
				timeBeforeAirLaunches-=time;
				
				if (timeBeforeAirLaunches < 0)
				{
					loadingA=false;
				}
			}
			if (this.type == Boat.CARRIER)
			{
				if ((nbAwacs >= 0) && !loadingA)
				{
					Airplane caspianAir = new Airplane(theMap);
					caspianAir.getProgrammedWPs().addWP(new Waypoint(this.getPosX()+Math.random()*200-100,this.getPosY()+Math.random()*200-100,0));
					caspianAir.getProgrammedWPs().addWP(new Waypoint(this.getPosX()+Math.random()*200-100,this.getPosY()+Math.random()*200-100,0));
					caspianAir.getProgrammedWPs().addWP(new Waypoint(this.getPosX()+Math.random()*200-100,this.getPosY()+Math.random()*200-100,0));
					caspianAir.getProgrammedWPs().addWP(new Waypoint(this.getPosX()+Math.random()*200-100,this.getPosY()+Math.random()*200-100,0));
					caspianAir.setLoop(true);
					caspianAir.setFireAtWill(true);
					caspianAir.setIgnoreFriends(false);
					
					caspianAir.setComplement(20);
					caspianAir.setComplementNorm(20);
					caspianAir.setCost(300000000);
					caspianAir.setOrientation(this.orientation);
					caspianAir.setCurrentSpeed(600);
					caspianAir.setEnergy(1000);
					caspianAir.setMaxSpeed(800);
					caspianAir.setName("Hawk 16");
					caspianAir.setResistance(5);
					caspianAir.setVisibilityLevel(0);
					caspianAir.setNoiseSignature(2);
					caspianAir.setVisibilitySignature(0);
					caspianAir.setPosX(this.getPosX());
					caspianAir.setPosY(this.getPosY());
					caspianAir.setOwner(this);
					
					switch (this.typeFaction)
					{
						case FUnit.BOAT_ALLIED:
							caspianAir.setTypeFaction(FUnit.AIRPLANE_ALLIED);
							break;
						case FUnit.BOAT_OUR:
							caspianAir.setTypeFaction(FUnit.AIRPLANE_OUR);
							break;
						case FUnit.BOAT_ENEMY:
							caspianAir.setTypeFaction(FUnit.AIRPLANE_ENEMY);
						break;
					}
					
					caspianAir.setType(Airplane.RADARPLANE);
					caspianAir.createGfx(300, 50, 0, 0, 20);
					
					DeportedRadar ourRadar2;
					ourRadar2= new DeportedRadar(theMap,this);
					ourRadar2.createGfx(0, 0, 0, 0, 1, 1);
					ourRadar2.setPosAttach(0, 0, 0);
					ourRadar2.setPower(20);
					ourRadar2.setSpeedRot(30);
					ourRadar2.activate();
					ourRadar2.setDebugView(false);
					
					this.getSensors().add(ourRadar2);
					caspianAir.getDeportedSensors().add(ourRadar2);
					caspianAir.addAttachedObject(ourRadar2);
					caspianAir.setView(false);
					
					if (this.typeFaction == FUnit.BOAT_ENEMY)
						caspianAir.hideMe();
					
					theMap.addAirplane(caspianAir);

					nbAwacs--;
					loadingA=true;
					this.timeBeforeAirLaunches=timeBetweenAirLaunches;
				}
				if ((nbFighters >= 0) && (this.currentNbFightersInFlight < this.nbFightersOnFlightMax) && !loadingA)
				{
					Airplane caspianAir = new Airplane(theMap);
					caspianAir.getProgrammedWPs().addWP(new Waypoint(Math.random()*500+100,Math.random()*600+100,0));
					caspianAir.getProgrammedWPs().addWP(new Waypoint(Math.random()*500+100,Math.random()*600+100,0));
					caspianAir.getProgrammedWPs().addWP(new Waypoint(Math.random()*500+100,Math.random()*600+100,0));
					caspianAir.getProgrammedWPs().addWP(new Waypoint(Math.random()*500+100,Math.random()*600+100,0));
					caspianAir.setLoop(true);
					caspianAir.setFireAtWill(true);
					caspianAir.setIgnoreFriends(false);
					
					caspianAir.setComplement(2);
					caspianAir.setComplementNorm(2);
					caspianAir.setCost(100000000);
					caspianAir.setOrientation(this.orientation);
					caspianAir.setCurrentSpeed(1500);
					caspianAir.setEnergy(1000);
					caspianAir.setMaxSpeed(1000);
					caspianAir.setName("F23 Fighter");
					caspianAir.setResistance(1);
					caspianAir.setVisibilityLevel(0);
					caspianAir.setNoiseSignature(2);
					caspianAir.setVisibilitySignature(0);
					caspianAir.setPosX(this.getPosX());
					caspianAir.setPosY(this.getPosY());
					caspianAir.setOwner(this);
					
					caspianAir.setType(Airplane.FIGHTER);
					switch (this.typeFaction)
					{
						case FUnit.BOAT_ALLIED:
							caspianAir.setTypeFaction(FUnit.AIRPLANE_ALLIED);
							break;
						case FUnit.BOAT_OUR:
							caspianAir.setTypeFaction(FUnit.AIRPLANE_OUR);
							break;
						case FUnit.BOAT_ENEMY:
							caspianAir.setTypeFaction(FUnit.AIRPLANE_ENEMY);
						break;
					}
					
					caspianAir.createGfx(300, 50, 0, 0, 20);
					
					SimpleAround ourRadar2;
					ourRadar2= new SimpleAround(theMap,caspianAir);
					ourRadar2.createGfx(0, 0, 0, 0, 1, 1);
					ourRadar2.setPosAttach(0, 0, 0);
					ourRadar2.setPower(10);
					ourRadar2.activate();
					ourRadar2.setDebugView(false);
					
					caspianAir.getSensors().add(ourRadar2);
					caspianAir.addAttachedObject(ourRadar2);
					caspianAir.setView(false);
					caspianAir.getSensors().add(theMap.getOurKD());
					
					if (this.typeFaction == FUnit.BOAT_ENEMY)
						caspianAir.hideMe();
					
					theMap.addAirplane(caspianAir);

					nbFighters--;
					loadingA=true;
					this.timeBeforeAirLaunches=timeBetweenAirLaunches;
				}
			}

			updateAttachedObjects();
			// TODO Auto-generated method stub
			
			this.orientation+=angleToTurn*time*rudderEfficiency;
			
			this.posX = this.posX + time*this.currentSpeed*Math.cos(this.orientation);
			this.posY = this.posY + time*this.currentSpeed*Math.sin(this.orientation);
			
			depthWater = ourLevelKeeper.getAlpha((int )this.posX, (int )this.posY)-255;
			
			//System.out.println("Depth "+depthWater);
			
			if (( this.currentSpeed > 3) && (depthWater > -10))
			{
				// Not good :)
				//this.turn(0.2);
				this.wantedSpeed =0;
			}
			else if (( this.currentSpeed < -3) && (depthWater > -10))
			{
				// Not good :)
				//this.turn(0.2);
				this.wantedSpeed = 0;
			}
			
			if (!unmanned)
			{
				//	If no waypoints, we turn!
				if (this.programmedWPs.isEmpty() || (!followWP))
				{
					if (this.currentSpeed < this.wantedSpeed)
						this.currentSpeed += 2;
					else if (this.currentSpeed > this.wantedSpeed)
							this.currentSpeed -= 2;
					//this.orientation += 0.04*time;
				}
				else
				{
					// Got to the next WP
					seekWP();
				}
			}
			else
			{
				if (this.currentSpeed < this.wantedSpeed)
					this.currentSpeed += 2;
				else if (this.currentSpeed > this.wantedSpeed)
						this.currentSpeed -= 2;
			}

			this.updateMe(posX, posY, 0, this.orientation, this.currentSpeed);

			for (int iSensors=0;iSensors < sensors.size() ; iSensors++)
			{
				sensors.get(iSensors).doUpdate(time);

				foundUnits = sensors.get(iSensors).getFoundUnits();
				
				attackIfNeeded(time);
			}
			// Global sensors
			// KnownDatas only now (because the satellites infos might be too old...)
			foundUnits = this.theMap.getOurKD().getFoundUnits();
			
			attackIfNeeded(time);	
		}
		else if (explodeTime < 10)
		{
				explodeTime++;
				explosion.setSize(explosion.getSize()+0.2);
				//System.out.println("Explode !!!!");
				SndContainer.playExplosion2Sound();
		}
		else if (iDead > -1)
		{
			explosion.invalidate();
			this.setDepth(iDead--);
			smokeBlack.invalidate();
			if (this.type != Boat.CARRIER)
			{
				smoke.invalidate();
			}
			removeMeWOBody();
		}
		else if (iDead > depthWater)
		{
			this.setDepth(iDead--);
		}
	}

}
