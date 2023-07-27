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

package com.tgb.subgame.levels;

import com.tgb.subengine.gameentities.*;
import com.tgb.subgame.unitspc.Boat;
import com.tgb.subgame.unitspc.FreeBoat;
import com.tgb.subgame.unitspc.Submarine;
import com.tgb.subgame.unitspc.FreeSub;
import com.tgb.subgame.unitspc.sensors.*;
import com.tgb.subgame.*;
import com.tgb.subgame.unitspc.FUnit;

/**
 * The "splash" level, specifically done to have a group of allies stuck between
 * bad guys.
 * The units here are "free" units, able to go outside the screen.
 * @author Alain Becam
 *
 */
public class SplashLevel implements ILevelPC
{
	LevelMap theMap;
	
	public SplashLevel(LevelMap theMap)
	{
		this.theMap=theMap;
	}
	
	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getAlliesSubs()
	 */
	public void addAlliesSubs()
	{		
		// TODO Auto-generated method stub
		FreeSub georgian = new FreeSub(theMap);
		
		Sonar ourFrontSonar;
		
		georgian.getProgrammedWPs().addWP(new Waypoint(400,0,0));
		georgian.setLoop(true);
		
		
		georgian.setFireAtWill(true);
		georgian.setComplement(100);
		georgian.setCost(200000000);
		georgian.setCurrentSpeed(20);
		georgian.setEnergy(1000);
		georgian.setMaxSpeed(35);
		georgian.setOrientation(0);
		georgian.setName("Georgian");
		georgian.setResistance(10);
		georgian.setVisibilityLevel(0);
		georgian.setNoiseSignature(2);
		georgian.setVisibilitySignature(0);
		georgian.setPosX(370);
		georgian.setPosY(800);
		georgian.setType(Submarine.NUKE);
		georgian.setTypeFaction(FUnit.SUB_ALLIED);
		georgian.createGfx(370, 800, 0, 0, 20);
		georgian.setDepth(-50);
		georgian.setWantedDepth(-50);
		
		ourFrontSonar= new Sonar(theMap,georgian);
		ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		ourFrontSonar.setPosAttach(20, 0, 0);
		georgian.getSensors().add(ourFrontSonar);
		georgian.addAttachedObject(ourFrontSonar);
		georgian.getSensors().add(theMap.getOurKD());
		
		georgian.setView(false);
		
		theMap.addSub(georgian);
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getAlliesrBoats()
	 */
	public void addAlliesBoats()
	{
		// TODO Auto-generated method stub
		FreeBoat caspian = new FreeBoat(theMap);
		
		caspian.getProgrammedWPs().addWP(new Waypoint(420,0,0));
		caspian.setLoop(true);
		
		caspian.setComplement(4000);
		caspian.setCost(200000000);
		caspian.setCurrentSpeed(20);
		caspian.setFireAtWill(true);
		caspian.setEnergy(1000);
		caspian.setMaxSpeed(40);
		caspian.setOrientation(0);
		caspian.setName("Triomphant");
		caspian.setResistance(25);
		caspian.setVisibilityLevel(0);
		caspian.setNoiseSignature(2);
		caspian.setVisibilitySignature(0);
		caspian.setPosX(500);
		caspian.setPosY(750);
		caspian.setTypeFaction(FUnit.BOAT_ALLIED);
		caspian.setType(Boat.CARRIER);
		caspian.createGfx(600, 250, 0, 0, 20);
		
		Sonar caspianFrontSonar;
		
		caspianFrontSonar= new Sonar(theMap,caspian);
		caspianFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		caspianFrontSonar.setPosAttach(20, 0, 0);
		caspian.getSensors().add(caspianFrontSonar);
		caspian.addAttachedObject(caspianFrontSonar);
		
		Radar ourRadar;
		ourRadar= new Radar(theMap,caspian);
		ourRadar.createGfx(0, 0, 0, 0, 1.5, 1);
		if (caspian.getType() == Boat.CARRIER)
		{
			ourRadar.setPosAttach(2, -7, 0);
		}
		else
		{
			ourRadar.setPosAttach(8, 0, 0);
		}
		ourRadar.setPower(10);
		ourRadar.setSpeedRot(50);
		ourRadar.activate();
		ourRadar.setDebugView(false);
		
		caspian.getSensors().add(ourRadar);
		caspian.addAttachedObject(ourRadar);
		caspian.setView(false);
		
		
		theMap.addBoat(caspian);
		
		addAlliesBoats2();
	}
	
	public void addAlliesBoats2()
	{
		// TODO Auto-generated method stub
		FreeBoat caspian = new FreeBoat(theMap);
		
		caspian.getProgrammedWPs().addWP(new Waypoint(500,0,0));
		caspian.setLoop(true);
		
		caspian.setComplement(4000);
		caspian.setCost(200000000);
		caspian.setCurrentSpeed(20);
		caspian.setFireAtWill(true);
		caspian.setEnergy(1000);
		caspian.setMaxSpeed(40);
		caspian.setOrientation(0);
		caspian.setName("Triomphant");
		caspian.setResistance(25);
		caspian.setVisibilityLevel(0);
		caspian.setNoiseSignature(2);
		caspian.setVisibilitySignature(0);
		caspian.setPosX(600);
		caspian.setPosY(750);
		caspian.setTypeFaction(FUnit.BOAT_ALLIED);
		caspian.setType(Boat.CARRIER);
		caspian.createGfx(600, 250, 0, 0, 20);
		
		Sonar caspianFrontSonar;
		
		caspianFrontSonar= new Sonar(theMap,caspian);
		caspianFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		caspianFrontSonar.setPosAttach(20, 0, 0);
		caspian.getSensors().add(caspianFrontSonar);
		caspian.addAttachedObject(caspianFrontSonar);
		
		Radar ourRadar;
		ourRadar= new Radar(theMap,caspian);
		ourRadar.createGfx(0, 0, 0, 0, 1.5, 1);
		if (caspian.getType() == Boat.CARRIER)
		{
			ourRadar.setPosAttach(2, -7, 0);
		}
		else
		{
			ourRadar.setPosAttach(8, 0, 0);
		}
		ourRadar.setPower(10);
		ourRadar.setSpeedRot(50);
		ourRadar.activate();
		ourRadar.setDebugView(false);
		
		caspian.getSensors().add(ourRadar);
		caspian.addAttachedObject(ourRadar);
		caspian.setView(false);
		
		
		theMap.addBoat(caspian);
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getEnemiesBoats()
	 */
	public void addEnemiesBoats()
	{	
		// TODO Auto-generated method stub
		FreeBoat caspian = new FreeBoat(theMap);
		
		caspian.getProgrammedWPs().addWP(new Waypoint(400,480,0));
		caspian.setLoop(true);
		
		caspian.setFireAtWill(true);
		caspian.setComplement(5000);
		caspian.setCost(200000000);
		caspian.setCurrentSpeed(20);
		caspian.setEnergy(1000);
		caspian.setMaxSpeed(35);
		caspian.setOrientation(0);
		caspian.setName("Caspian");
		caspian.setResistance(25);
		caspian.setVisibilityLevel(0);
		caspian.setNoiseSignature(2);
		caspian.setVisibilitySignature(0);
		caspian.setPosX(-20);
		caspian.setPosY(270);
		caspian.setType(Boat.CRUISER);
		caspian.setTypeFaction(FUnit.BOAT_ENEMY);
		caspian.setTimeBetweenLaunchesM(0.8); // 0.4 by default
		caspian.createGfx(20, 270, 0, 0, 20);
		
		Sonar caspianFrontSonar;
		
		caspianFrontSonar= new Sonar(theMap,caspian);
		caspianFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		caspianFrontSonar.setPosAttach(20, 0, 0);
		caspian.getSensors().add(caspianFrontSonar);
		caspian.addAttachedObject(caspianFrontSonar);
		
		Radar ourRadar;
		ourRadar= new Radar(theMap,caspian);
		ourRadar.createGfx(0, 0, 0, 0, 1, 1);
		ourRadar.setPosAttach(2, -7, 0);
		ourRadar.setPower(20);
		ourRadar.activate();
		ourRadar.setDebugView(false);
		
		caspian.getSensors().add(ourRadar);
		caspian.addAttachedObject(ourRadar);		
		caspian.setView(false);
		//caspian.hideMe();
		
		FreeBoat lisian = new FreeBoat(theMap);
		
		lisian.getProgrammedWPs().addWP(new Waypoint(400,500,0));
		lisian.setLoop(true);
		
		lisian.setFireAtWill(true);
		lisian.setComplement(3000);
		lisian.setCost(200000000);
		lisian.setCurrentSpeed(20);
		lisian.setEnergy(1000);
		lisian.setMaxSpeed(35);
		lisian.setOrientation(0);
		lisian.setName("Lisian");
		lisian.setResistance(25);
		lisian.setVisibilityLevel(0);
		lisian.setNoiseSignature(2);
		lisian.setVisibilitySignature(0);
		lisian.setPosX(-50);
		lisian.setPosY(300);
		lisian.setType(Boat.CRUISER);
		lisian.setTypeFaction(FUnit.BOAT_ENEMY);
		lisian.createGfx(50, 300, 0, 0, 20);
		lisian.setTimeBetweenLaunchesM(0.8); // 0.4 by default
		Sonar lisianFrontSonar;
		
		lisianFrontSonar= new Sonar(theMap,lisian);
		lisianFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		lisianFrontSonar.setPosAttach(20, 0, 0);
		lisian.getSensors().add(lisianFrontSonar);
		lisian.addAttachedObject(lisianFrontSonar);
		
		Radar lisianRadar;
		lisianRadar= new Radar(theMap,lisian);
		lisianRadar.createGfx(0, 0, 0, 0, 1, 1);
		lisianRadar.setPosAttach(2, -7, 0);
		lisianRadar.setPower(15);
		lisianRadar.activate();
		
		lisian.getSensors().add(lisianRadar);
		lisian.addAttachedObject(lisianRadar);
		
		//lisian.hideMe();
		lisian.setView(false);
		
		FreeBoat lisian2 = new FreeBoat(theMap);
		
		lisian2.getProgrammedWPs().addWP(new Waypoint(450,450,0));
		lisian2.setLoop(true);
		
		lisian2.setFireAtWill(true);
		lisian2.setComplement(3000);
		lisian2.setCost(200000000);
		lisian2.setCurrentSpeed(20);
		lisian2.setEnergy(1000);
		lisian2.setMaxSpeed(35);
		lisian2.setOrientation(0);
		lisian2.setName("lisian2");
		lisian2.setResistance(25);
		lisian2.setVisibilityLevel(0);
		lisian2.setNoiseSignature(2);
		lisian2.setVisibilitySignature(0);
		lisian2.setPosX(900);
		lisian2.setPosY(300);
		lisian2.setType(Boat.CRUISER);
		lisian2.setTypeFaction(FUnit.BOAT_ENEMY);
		lisian2.createGfx(900, 350, 0, 0, 20);
		lisian2.setTimeBetweenLaunchesM(0.8); // 0.4 by default
		Sonar lisian2FrontSonar;
		
		lisian2FrontSonar= new Sonar(theMap,lisian2);
		lisian2FrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		lisian2FrontSonar.setPosAttach(20, 0, 0);
		lisian2.getSensors().add(lisian2FrontSonar);
		lisian2.addAttachedObject(lisian2FrontSonar);
		
		Radar lisian2Radar;
		lisian2Radar= new Radar(theMap,lisian2);
		lisian2Radar.createGfx(0, 0, 0, 0, 1, 1);
		lisian2Radar.setPosAttach(2, -7, 0);
		lisian2Radar.setPower(6);
		lisian2Radar.setDebugView(false);
		lisian2Radar.activate();
		
		lisian2.getSensors().add(lisian2Radar);
		lisian2.addAttachedObject(lisian2Radar);
		
		
		//lisian2.hideMe();
		lisian2.setView(false);
		lisian2.setView(false);
		
		theMap.addBoat(caspian);
		theMap.addBoat(lisian);
		theMap.addBoat(lisian2);
		
		//createMegaCruiserEnemies();
		
		addEnemiesBoats2();
	}

	private void createMegaCruiserEnemies()
	{
		FreeBoat oneCruiser = new FreeBoat(theMap);
		
		oneCruiser.getProgrammedWPs().addWP(new Waypoint(500,350,0));
		oneCruiser.setLoop(true);
		
		oneCruiser.setType(Boat.CRUISER);
		oneCruiser.setTypeFaction(FUnit.BOAT_ENEMY);
		oneCruiser.setComplement(5000);
		oneCruiser.setComplementNorm(5000);
		GameKeeper.getInstance().addComplementEnemies(5000);
		oneCruiser.setTonnage(700000);
		oneCruiser.setCost(4000000000L);

		//GameKeeper.getInstance().addCostEnemies(400000000L);
		oneCruiser.setMaxSpeed(50);
		oneCruiser.setStandardSpeed(40);
		oneCruiser.setName("MMS Huristrous");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(20000);
		oneCruiser.setNbTorpedoes(400);
		oneCruiser.setResistance(120);
		// And add the absolute coordinates.
		oneCruiser.setPosX(-140);
		oneCruiser.setPosY(100);
		oneCruiser.setNbInSalveMaxM(6);
		
		oneCruiser.createGfx(0, 0, 0, 1, 1);
		
		Sonar lisian2FrontSonar;
		
		lisian2FrontSonar= new Sonar(theMap,oneCruiser);
		lisian2FrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		lisian2FrontSonar.setPosAttach(20, 0, 0);
		oneCruiser.getSensors().add(lisian2FrontSonar);
		oneCruiser.addAttachedObject(lisian2FrontSonar);
		
		Radar lisian2Radar;
		lisian2Radar= new Radar(theMap,oneCruiser);
		lisian2Radar.createGfx(0, 0, 0, 0, 1, 1);
		lisian2Radar.setPosAttach(2, -7, 0);
		lisian2Radar.setPower(30);
		lisian2Radar.setDebugView(false);
		lisian2Radar.activate();
		
		oneCruiser.getSensors().add(lisian2Radar);
		oneCruiser.addAttachedObject(lisian2Radar);
		//oneCruiser.hideMe();
		oneCruiser.setView(false);
		
		
		theMap.addBoat(oneCruiser);
	}
	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getEnemiesSubs()
	 */
	public void addEnemiesSubs()
	{	
		// TODO Auto-generated method stub
		FreeSub caspian = new FreeSub(theMap);
		
		caspian.getProgrammedWPs().addWP(new Waypoint(400,650,0));
		caspian.setLoop(true);
		
		caspian.setFireAtWill(true);
		caspian.setComplement(100);
		caspian.setCost(200000000);
		caspian.setCurrentSpeed(45);
		caspian.setEnergy(1000);
		caspian.setMaxSpeed(45);
		caspian.setOrientation(0);
		caspian.setName("Caspian");
		caspian.setResistance(10);
		caspian.setVisibilityLevel(0);
		caspian.setNoiseSignature(2);
		caspian.setVisibilitySignature(0);
		caspian.setPosX(800);
		caspian.setPosY(600);
		
		caspian.setType(Submarine.NUKE);
		caspian.setTypeFaction(FUnit.SUB_ENEMY);
		caspian.createGfx( 100, 600, 0, 0, 20);
		caspian.setDepth(-100);
		caspian.setWantedDepth(-100);

		Sonar ourFrontSonar;
		ourFrontSonar= new Sonar(theMap,caspian);
		ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		ourFrontSonar.setPosAttach(20, 0, 0);
		
		caspian.getSensors().add(ourFrontSonar);
		caspian.addAttachedObject(ourFrontSonar);
		//caspian.hideMe();
		caspian.setView(false);
		
		FreeSub lisian = new FreeSub(theMap);
		
		Sonar lisianFrontSonar;
		
		lisian.setFireAtWill(true);
		lisian.getProgrammedWPs().addWP(new Waypoint(500,400,0));
		lisian.setLoop(true);
		lisian.setFireAtWill(true);
		lisian.setComplement(100);
		lisian.setCost(200000000);
		lisian.setCurrentSpeed(20);
		lisian.setEnergy(1000);
		lisian.setMaxSpeed(35);
		lisian.setOrientation(0);
		lisian.setName("Lisian");
		lisian.setResistance(10);
		lisian.setVisibilityLevel(0);
		lisian.setNoiseSignature(2);
		lisian.setVisibilitySignature(0);
		lisian.setPosX(700);
		lisian.setPosY(200);
		
		lisian.setType(Submarine.NUKE);
		lisian.setTypeFaction(FUnit.SUB_ENEMY);
		lisian.createGfx( 50, 500, 0, 0, 20);
		lisian.setDepth(-50);
		lisian.setWantedDepth(-50);
		
		lisianFrontSonar= new Sonar(theMap,lisian);
		lisianFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		lisianFrontSonar.setPosAttach(20, 0, 0);
		lisian.getSensors().add(lisianFrontSonar);
		lisian.addAttachedObject(lisianFrontSonar);
		
		//lisian.hideMe();
		lisian.setView(false);
		
		theMap.addSub(caspian);
		theMap.addSub(lisian);
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getNeutralBoats()
	 */
	public void addNeutralBoats()
	{
		;
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getNeutralSubs()
	 */
	public void addNeutralSubs()
	{
		;
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getOurBoats()
	 */
	public void addOurBoats()
	{	
		// TODO Auto-generated method stub
		FreeBoat caspian = new FreeBoat(theMap);
		caspian.getProgrammedWPs().addWP(new Waypoint(500,0,0));
		caspian.setLoop(true);
		caspian.setFireAtWill(true);
		
		caspian.setComplement(5000);
		caspian.setCost(800000000);
		caspian.setCurrentSpeed(20);
		caspian.setEnergy(1000);
		caspian.setMaxSpeed(35);
		caspian.setOrientation(0);
		caspian.setName("MSS Plourin");
		caspian.setResistance(25);
		caspian.setVisibilityLevel(0);
		caspian.setNoiseSignature(2);
		caspian.setVisibilitySignature(0);
		caspian.setPosX(400);
		caspian.setPosY(800);
		caspian.setTypeFaction(FUnit.BOAT_OUR);
		caspian.setType(Boat.CRUISER);
		caspian.createGfx(300, 50, 0, 0, 20);
		caspian.setTimeBetweenLaunchesM(0.8); // 0.4 by default
		
		
		Sonar ourFrontSonar;
		ourFrontSonar= new Sonar(theMap,caspian);
		ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		ourFrontSonar.setPosAttach(40, 0, 0);
		ourFrontSonar.activate();
		
		Radar ourRadar;
		ourRadar= new Radar(theMap,caspian);
		ourRadar.createGfx(0, 0, 0, 0, 1, 1);
		
		if (caspian.getType() == Boat.CARRIER)
		{
			ourRadar.setPosAttach(2, -7, 0);
		}
		else
		{
			ourRadar.setPosAttach(8, 0, 0);
		}
		
		ourRadar.setPower(10);
		ourRadar.setSpeedRot(40);
		ourRadar.activate();
		ourRadar.setDebugView(false);
		
		caspian.getSensors().add(ourFrontSonar);
		caspian.addAttachedObject(ourFrontSonar);
		caspian.getSensors().add(ourRadar);
		caspian.addAttachedObject(ourRadar);
		
		caspian.setView(false);
		
		System.out.println("Caspian boat created");
		
		FreeBoat lisian = new FreeBoat(theMap);
		
		lisian.getProgrammedWPs().addWP(new Waypoint(550,0,0));
		lisian.setLoop(true);
		lisian.setFireAtWill(true);
		
		lisian.setComplement(5000);
		lisian.setCost(800000000);
		lisian.setCurrentSpeed(30);
		lisian.setEnergy(1000);
		lisian.setMaxSpeed(35);
		lisian.setOrientation(0.3);
		lisian.setName("MSS Genelard");
		lisian.setResistance(25);
		lisian.setVisibilityLevel(0);
		lisian.setNoiseSignature(2);
		lisian.setVisibilitySignature(0);
		lisian.setPosX(550);
		lisian.setPosY(750);
		lisian.setType(Boat.CRUISER);
		lisian.setTypeFaction(FUnit.BOAT_OUR);
		lisian.createGfx(50, 300, 0, 0, 20);
		lisian.setTimeBetweenLaunchesM(0.8); // 0.4 by default
		
		Sonar lisianFrontSonar;
		lisianFrontSonar= new Sonar(theMap,lisian);
		lisianFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		lisianFrontSonar.setPosAttach(40, 0, 0);
		lisianFrontSonar.desactivate();
		
		lisian.getSensors().add(lisianFrontSonar);
		lisian.addAttachedObject(lisianFrontSonar);
		
		Radar lisianRadar;
		lisianRadar= new Radar(theMap,lisian);
		lisianRadar.createGfx(0, 0, 0, 0, 1, 1);
		ourRadar.createGfx(0, 0, 0, 0, 1, 1);
		
		if (lisian.getType() == Boat.CARRIER)
		{
			ourRadar.setPosAttach(2, -7, 0);
		}
		else
		{
			ourRadar.setPosAttach(8, 0, 0);
		}
		
		lisianRadar.setPower(10);
		lisianRadar.setSpeedRot(50);
		lisianRadar.activate();
		lisianRadar.setDebugView(false);
		lisian.getSensors().add(lisianRadar);
		lisian.addAttachedObject(lisianRadar);
		
		lisian.setView(false);
		
		theMap.addBoat(caspian);
		theMap.addBoat(lisian);
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getOurSubs()
	 */
	public void addOurSubs()
	{		
		;
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getGlobalSensors()
	 */
	public void addGlobalSensor()
	{	
		;
	}

	public void addOthers()
	{
		;
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getEnemiesBoats()
	 */
	public void addEnemiesBoats2()
	{	
		// TODO Auto-generated method stub
		FreeBoat caspian = new FreeBoat(theMap);
		
		caspian.getProgrammedWPs().addWP(new Waypoint(400,500,0));
		caspian.setLoop(true);
		
		caspian.setFireAtWill(true);
		caspian.setComplement(5000);
		caspian.setCost(200000000);
		caspian.setCurrentSpeed(20);
		caspian.setEnergy(1000);
		caspian.setMaxSpeed(35);
		caspian.setOrientation(0);
		caspian.setName("Caspian");
		caspian.setResistance(25);
		caspian.setVisibilityLevel(0);
		caspian.setNoiseSignature(2);
		caspian.setVisibilitySignature(0);
		caspian.setPosX(-20);
		caspian.setPosY(350);
		caspian.setType(Boat.CRUISER);
		caspian.setTypeFaction(FUnit.BOAT_ENEMY);
		caspian.createGfx(20, 270, 0, 0, 20);
		caspian.setTimeBetweenLaunchesM(0.8); // 0.4 by default
		
		Sonar caspianFrontSonar;
		
		caspianFrontSonar= new Sonar(theMap,caspian);
		caspianFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		caspianFrontSonar.setPosAttach(20, 0, 0);
		caspian.getSensors().add(caspianFrontSonar);
		caspian.addAttachedObject(caspianFrontSonar);
		
		Radar ourRadar;
		ourRadar= new Radar(theMap,caspian);
		ourRadar.createGfx(0, 0, 0, 0, 1, 1);
		ourRadar.setPosAttach(2, -7, 0);
		ourRadar.setPower(20);
		ourRadar.activate();
		ourRadar.setDebugView(false);
		
		caspian.getSensors().add(ourRadar);
		caspian.addAttachedObject(ourRadar);		
		
		//caspian.hideMe();
		caspian.setView(false);
		
		FreeBoat lisian = new FreeBoat(theMap);
		
		lisian.getProgrammedWPs().addWP(new Waypoint(400,540,0));
		lisian.setLoop(true);
		
		lisian.setFireAtWill(true);
		lisian.setComplement(3000);
		lisian.setCost(200000000);
		lisian.setCurrentSpeed(20);
		lisian.setEnergy(1000);
		lisian.setMaxSpeed(35);
		lisian.setOrientation(0);
		lisian.setName("Lisian");
		lisian.setResistance(25);
		lisian.setVisibilityLevel(0);
		lisian.setNoiseSignature(2);
		lisian.setVisibilitySignature(0);
		lisian.setPosX(-50);
		lisian.setPosY(400);
		lisian.setType(Boat.CRUISER);
		lisian.setTypeFaction(FUnit.BOAT_ENEMY);
		lisian.createGfx(50, 300, 0, 0, 20);
		lisian.setNbInSalveMaxM(10);
		lisian.setTimeBetweenLaunchesM(0.8); // 0.4 by default
		Sonar lisianFrontSonar;
		
		lisianFrontSonar= new Sonar(theMap,lisian);
		lisianFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		lisianFrontSonar.setPosAttach(20, 0, 0);
		lisian.getSensors().add(lisianFrontSonar);
		lisian.addAttachedObject(lisianFrontSonar);
		
		Radar lisianRadar;
		lisianRadar= new Radar(theMap,lisian);
		lisianRadar.createGfx(0, 0, 0, 0, 1, 1);
		lisianRadar.setPosAttach(2, -7, 0);
		lisianRadar.setPower(15);
		lisianRadar.activate();
		
		lisian.getSensors().add(lisianRadar);
		lisian.addAttachedObject(lisianRadar);
		
		//lisian.hideMe();
		lisian.setView(false);
		
		FreeBoat lisian2 = new FreeBoat(theMap);
		
		lisian2.getProgrammedWPs().addWP(new Waypoint(450,550,0));
		lisian2.setLoop(true);
		
		lisian2.setFireAtWill(true);
		lisian2.setComplement(3000);
		lisian2.setCost(200000000);
		lisian2.setCurrentSpeed(20);
		lisian2.setEnergy(1000);
		lisian2.setMaxSpeed(35);
		lisian2.setOrientation(0);
		lisian2.setName("lisian2");
		lisian2.setResistance(25);
		lisian2.setVisibilityLevel(0);
		lisian2.setNoiseSignature(2);
		lisian2.setVisibilitySignature(0);
		lisian2.setPosX(900);
		lisian2.setPosY(400);
		lisian2.setType(Boat.CRUISER);
		lisian2.setTypeFaction(FUnit.BOAT_ENEMY);
		lisian2.createGfx(900, 350, 0, 0, 20);
		lisian2.setNbInSalveMaxM(10);
		lisian2.setTimeBetweenLaunchesM(0.8); // 0.4 by default
		Sonar lisian2FrontSonar;
		
		lisian2FrontSonar= new Sonar(theMap,lisian2);
		lisian2FrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		lisian2FrontSonar.setPosAttach(20, 0, 0);
		lisian2.getSensors().add(lisian2FrontSonar);
		lisian2.addAttachedObject(lisian2FrontSonar);
		
		Radar lisian2Radar;
		lisian2Radar= new Radar(theMap,lisian2);
		lisian2Radar.createGfx(0, 0, 0, 0, 1, 1);
		lisian2Radar.setPosAttach(2, -7, 0);
		lisian2Radar.setPower(6);
		lisian2Radar.setDebugView(false);
		lisian2Radar.activate();
		
		lisian2.getSensors().add(lisian2Radar);
		lisian2.addAttachedObject(lisian2Radar);
		
		
		//lisian2.hideMe();
		lisian2.setView(false);
		
		theMap.addBoat(caspian);
		theMap.addBoat(lisian);
		theMap.addBoat(lisian2);
		
		createMegaCruiserEnemies();
	}
}
