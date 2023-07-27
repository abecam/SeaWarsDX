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

import java.util.ArrayList;

import com.tgb.subengine.gameentities.*;
import com.tgb.subgame.unitspc.Boat;
import com.tgb.subgame.unitspc.Submarine;
import com.tgb.subgame.unitspc.Airplane;
import com.tgb.subgame.unitspc.Base;
import com.tgb.subgame.unitspc.sensors.*;
import com.tgb.subgame.*;
import com.tgb.subgame.unitspc.FUnit;
import pulpcore.image.CoreImage;

/**
 * A simple, hard-coded, level.
 * @author Alain Becam
 *
 */
public class FirstLevelPC implements ILevelPC
{
	LevelMap theMap;
	
	public FirstLevelPC(LevelMap theMap)
	{
		this.theMap=theMap;
	}
	
	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getAlliesSubs()
	 */
	public void addAlliesSubs()
	{
		
		CoreImage subImage = Submarine.getImageForMe();
		
		// TODO Auto-generated method stub
		Submarine georgian = new Submarine(theMap);
		
		Sonar ourFrontSonar;
		
		georgian.getProgrammedWPs().addWP(new Waypoint(100,360,0));
		georgian.getProgrammedWPs().addWP(new Waypoint(650,350,0));
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
		georgian.setPosX(50);
		georgian.setPosY(50);
		georgian.setType(Submarine.NUKE);
		georgian.setTypeFaction(FUnit.SUB_ALLIED);
		georgian.createGfx(subImage, 50, 50, 0, 0, 20);
		georgian.setDepth(-50);
		georgian.setWantedDepth(-50);
		
		ourFrontSonar= new Sonar(theMap,georgian);
		ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		ourFrontSonar.setPosAttach(20, 0, 0);
		georgian.getSensors().add(ourFrontSonar);
		georgian.addAttachedObject(ourFrontSonar);
		georgian.getSensors().add(theMap.getOurKD());
		
		
		Submarine atlanta = new Submarine(theMap);
		
		atlanta.getProgrammedWPs().addWP(new Waypoint(680,300,0));
		atlanta.getProgrammedWPs().addWP(new Waypoint(150,300,0));
		atlanta.setLoop(true);
		
		atlanta.setFireAtWill(true);
		atlanta.setComplement(100);
		atlanta.setCost(200000000);
		atlanta.setCurrentSpeed(20);
		atlanta.setEnergy(1000);
		atlanta.setMaxSpeed(35);
		atlanta.setOrientation(0);
		atlanta.setName("Atlanta");
		atlanta.setResistance(10);
		atlanta.setVisibilityLevel(0);
		atlanta.setNoiseSignature(2);
		atlanta.setVisibilitySignature(0);
		atlanta.setPosX(50);
		atlanta.setPosY(150);
		atlanta.setType(Submarine.NUKE);
		atlanta.setTypeFaction(FUnit.SUB_ALLIED);
		atlanta.createGfx(subImage, 50, 150, 0, 0, 20);
		atlanta.setDepth(-50);
		atlanta.setWantedDepth(-50);
		
		Sonar atlantaFrontSonar;
		
		atlantaFrontSonar= new Sonar(theMap,atlanta);
		atlantaFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		atlantaFrontSonar.setPosAttach(20, 0, 0);
		atlanta.getSensors().add(atlantaFrontSonar);
		atlanta.addAttachedObject(atlantaFrontSonar);
		
		
		theMap.addSub(georgian);
		theMap.addSub(atlanta);
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getAlliesrBoats()
	 */
	public void addAlliesBoats()
	{
		CoreImage boatImage = Boat.getImageForMe();
		
		// TODO Auto-generated method stub
		Boat caspian = new Boat(theMap);
		
		caspian.getProgrammedWPs().addWP(new Waypoint(100,260,0));
		caspian.getProgrammedWPs().addWP(new Waypoint(680,250,0));
		caspian.setLoop(true);
		
		caspian.setComplement(4000);
		caspian.setCost(200000000);
		caspian.setCurrentSpeed(20);
		caspian.setFireAtWill(true);
		caspian.setEnergy(1000);
		caspian.setMaxSpeed(40);
		caspian.setOrientation(0);
		caspian.setName("Triomphant");
		caspian.setResistance(10);
		caspian.setVisibilityLevel(0);
		caspian.setNoiseSignature(2);
		caspian.setVisibilitySignature(0);
		caspian.setPosX(600);
		caspian.setPosY(250);
		caspian.setTypeFaction(FUnit.BOAT_ALLIED);
		caspian.setType(Boat.CARRIER);
		caspian.createGfx(boatImage, 600, 250, 0, 0, 20);
		
		Sonar caspianFrontSonar;
		
		caspianFrontSonar= new Sonar(theMap,caspian);
		caspianFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		caspianFrontSonar.setPosAttach(20, 0, 0);
		caspian.getSensors().add(caspianFrontSonar);
		caspian.addAttachedObject(caspianFrontSonar);
		
		Radar ourRadar;
		ourRadar= new Radar(theMap,caspian);
		ourRadar.createGfx(0, 0, 0, 0, 1.5, 1);
		ourRadar.setPosAttach(2, -7, 0);
		ourRadar.setPower(15);
		ourRadar.setSpeedRot(50);
		ourRadar.activate();
		
		caspian.getSensors().add(ourRadar);
		caspian.addAttachedObject(ourRadar);
		
		
		theMap.addBoat(caspian);
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getEnemiesBoats()
	 */
	public void addEnemiesBoats()
	{
		CoreImage boatImage = Boat.getImageForMe();
		
		// TODO Auto-generated method stub
		Boat caspian = new Boat(theMap);
		
		caspian.getProgrammedWPs().addWP(new Waypoint(100,700,0));
		caspian.getProgrammedWPs().addWP(new Waypoint(600,650,0));
		caspian.setLoop(true);
		
		caspian.setFireAtWill(true);
		caspian.setComplement(5000);
		caspian.setCost(200000000);
		caspian.setCurrentSpeed(20);
		caspian.setEnergy(1000);
		caspian.setMaxSpeed(35);
		caspian.setOrientation(0);
		caspian.setName("Caspian");
		caspian.setResistance(70);
		caspian.setVisibilityLevel(0);
		caspian.setNoiseSignature(2);
		caspian.setVisibilitySignature(0);
		caspian.setPosX(200);
		caspian.setPosY(700);
		caspian.setType(Boat.CARRIER);
		caspian.setTypeFaction(FUnit.BOAT_ENEMY);
		caspian.createGfx(boatImage, 200, 500, 0, 0, 20);
		
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
		ourRadar.setPower(12);
		ourRadar.activate();
		
		caspian.getSensors().add(ourRadar);
		caspian.addAttachedObject(ourRadar);
		
		KnownDatas ourKD;
		
		ourKD = new KnownDatas(theMap);
		caspian.getSensors().add(ourKD);
		
		
		caspian.hideMe();
		
		Boat lisian = new Boat(theMap);
		
		lisian.getProgrammedWPs().addWP(new Waypoint(50,600,0));
		lisian.getProgrammedWPs().addWP(new Waypoint(650,650,0));
		lisian.setLoop(true);
		
		lisian.setFireAtWill(true);
		lisian.setComplement(3000);
		lisian.setCost(200000000);
		lisian.setCurrentSpeed(20);
		lisian.setEnergy(1000);
		lisian.setMaxSpeed(35);
		lisian.setOrientation(0);
		lisian.setName("Lisian");
		lisian.setResistance(70);
		lisian.setVisibilityLevel(0);
		lisian.setNoiseSignature(2);
		lisian.setVisibilitySignature(0);
		lisian.setPosX(50);
		lisian.setPosY(500);
		lisian.setType(Boat.CARRIER);
		lisian.setTypeFaction(FUnit.BOAT_ENEMY);
		lisian.createGfx(boatImage, 50, 200, 0, 0, 20);
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
		
		KnownDatas ourKD2;
		
		ourKD2 = new KnownDatas(theMap);
		lisian.getSensors().add(ourKD2);
		
		lisian.hideMe();
		
		
		Boat lisian2 = new Boat(theMap);
		
		lisian2.getProgrammedWPs().addWP(new Waypoint(50,400,0));
		lisian2.getProgrammedWPs().addWP(new Waypoint(650,250,0));
		lisian2.setLoop(true);
		
		lisian2.setFireAtWill(true);
		lisian2.setComplement(3000);
		lisian2.setCost(200000000);
		lisian2.setCurrentSpeed(20);
		lisian2.setEnergy(1000);
		lisian2.setMaxSpeed(35);
		lisian2.setOrientation(0);
		lisian2.setName("lisian2");
		lisian2.setResistance(100);
		lisian2.setVisibilityLevel(0);
		lisian2.setNoiseSignature(2);
		lisian2.setVisibilitySignature(0);
		lisian2.setPosX(800);
		lisian2.setPosY(800);
		lisian2.setType(Boat.CRUISER);
		lisian2.setTypeFaction(FUnit.BOAT_ENEMY);
		lisian2.createGfx(boatImage, 50, 200, 0, 0, 20);
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
		lisian2Radar.setPower(12);
		lisian2Radar.setDebugView(false);
		lisian2Radar.activate();
		
		lisian2.getSensors().add(lisian2Radar);
		lisian2.addAttachedObject(lisian2Radar);
		
		
		lisian2.hideMe();
		
		
		theMap.addBoat(caspian);
		theMap.addBoat(lisian);
		theMap.addBoat(lisian2);
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getEnemiesSubs()
	 */
	public void addEnemiesSubs()
	{	
		CoreImage subImage = Submarine.getImageForMe();
		
		
		// TODO Auto-generated method stub
		Submarine caspian = new Submarine(theMap);
		
		caspian.getProgrammedWPs().addWP(new Waypoint(100,600,0));
		caspian.getProgrammedWPs().addWP(new Waypoint(150,250,0));
		caspian.getProgrammedWPs().addWP(new Waypoint(200,300,0));
		caspian.getProgrammedWPs().addWP(new Waypoint(250,250,0));
		caspian.getProgrammedWPs().addWP(new Waypoint(300,300,0));
		caspian.getProgrammedWPs().addWP(new Waypoint(350,250,0));
		caspian.getProgrammedWPs().addWP(new Waypoint(400,300,0));
		caspian.getProgrammedWPs().addWP(new Waypoint(450,250,0));
		caspian.getProgrammedWPs().addWP(new Waypoint(100,200,0));
		caspian.getProgrammedWPs().addWP(new Waypoint(200,300,0));
		caspian.getProgrammedWPs().addWP(new Waypoint(70,150,0));
		caspian.getProgrammedWPs().addWP(new Waypoint(200,300,0));
		caspian.getProgrammedWPs().addWP(new Waypoint(600,400,0));
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
		caspian.setPosX(100);
		caspian.setPosY(600);
		
		caspian.setType(Submarine.NUKE);
		caspian.setTypeFaction(FUnit.SUB_ENEMY);
		caspian.createGfx(subImage, 100, 600, 0, 0, 20);
		caspian.setDepth(-100);
		caspian.setWantedDepth(-100);

		Sonar ourFrontSonar;
		ourFrontSonar= new Sonar(theMap,caspian);
		ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		ourFrontSonar.setPosAttach(20, 0, 0);
		
		caspian.getSensors().add(ourFrontSonar);
		caspian.addAttachedObject(ourFrontSonar);
		caspian.hideMe();
		
		Submarine lisian = new Submarine(theMap);
		
		Sonar lisianFrontSonar;
		
		lisian.setFireAtWill(true);
		lisian.getProgrammedWPs().addWP(new Waypoint(100,500,0));
		lisian.getProgrammedWPs().addWP(new Waypoint(650,350,0));
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
		lisian.setPosX(50);
		lisian.setPosY(500);
		
		lisian.setType(Submarine.NUKE);
		lisian.setTypeFaction(FUnit.SUB_ENEMY);
		lisian.createGfx(subImage, 50, 500, 0, 0, 20);
		lisian.setDepth(-50);
		lisian.setWantedDepth(-50);
		
		lisianFrontSonar= new Sonar(theMap,lisian);
		lisianFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		lisianFrontSonar.setPosAttach(20, 0, 0);
		lisian.getSensors().add(lisianFrontSonar);
		lisian.addAttachedObject(lisianFrontSonar);
		
		lisian.hideMe();
		
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
		CoreImage boatImage = Boat.getImageForMe();
		
		// TODO Auto-generated method stub
		Boat caspian = new Boat(theMap);
		caspian.getProgrammedWPs().addWP(new Waypoint(100,50,0));
		caspian.getProgrammedWPs().addWP(new Waypoint(50,80,0));
		caspian.getProgrammedWPs().addWP(new Waypoint(100,200,0));
		caspian.getProgrammedWPs().addWP(new Waypoint(400,150,0));
		caspian.getProgrammedWPs().addWP(new Waypoint(10,300,0));
		caspian.setLoop(true);
		caspian.setFireAtWill(true);
		
		caspian.setComplement(5000);
		caspian.setCost(800000000);
		caspian.setCurrentSpeed(20);
		caspian.setEnergy(1000);
		caspian.setMaxSpeed(35);
		caspian.setOrientation(0);
		caspian.setName("MSS Plourin");
		caspian.setResistance(100);
		caspian.setVisibilityLevel(0);
		caspian.setNoiseSignature(2);
		caspian.setVisibilitySignature(0);
		caspian.setPosX(300);
		caspian.setPosY(50);
		caspian.setTypeFaction(FUnit.BOAT_OUR);
		caspian.setType(Boat.CRUISER);
		caspian.createGfx(boatImage, 300, 50, 0, 0, 20);
		
		
		Sonar ourFrontSonar;
		ourFrontSonar= new Sonar(theMap,caspian);
		ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		ourFrontSonar.setPosAttach(40, 0, 0);
		ourFrontSonar.activate();
		
		Radar ourRadar;
		ourRadar= new Radar(theMap,caspian);
		ourRadar.createGfx(0, 0, 0, 0, 1, 1);
		ourRadar.setPosAttach(2, -7, 0);
		ourRadar.setPower(10);
		ourRadar.setSpeedRot(40);
		ourRadar.activate();
		ourRadar.setDebugView(false);
		
		caspian.getSensors().add(ourFrontSonar);
		caspian.addAttachedObject(ourFrontSonar);
		caspian.getSensors().add(ourRadar);
		caspian.addAttachedObject(ourRadar);
		
		KnownDatas ourKD;
		
		ourKD = new KnownDatas(theMap);
		caspian.getSensors().add(ourKD);
		
		System.out.println("Caspian boat created");
		
		Boat lisian = new Boat(theMap);
		
		lisian.getProgrammedWPs().addWP(new Waypoint(600,200,0));
		lisian.getProgrammedWPs().addWP(new Waypoint(150,300,0));
		lisian.setLoop(true);
		lisian.setFireAtWill(true);
		
		lisian.setComplement(5000);
		lisian.setCost(800000000);
		lisian.setCurrentSpeed(30);
		lisian.setEnergy(1000);
		lisian.setMaxSpeed(35);
		lisian.setOrientation(0.3);
		lisian.setName("MSS Genelard");
		lisian.setResistance(100);
		lisian.setVisibilityLevel(0);
		lisian.setNoiseSignature(2);
		lisian.setVisibilitySignature(0);
		lisian.setPosX(50);
		lisian.setPosY(300);
		lisian.setType(Boat.CRUISER);
		lisian.setTypeFaction(FUnit.BOAT_OUR);
		lisian.createGfx(boatImage, 50, 300, 0, 0, 20);
		
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
		lisianRadar.setPosAttach(2, -7, 0);
		lisianRadar.setPower(10);
		lisianRadar.setSpeedRot(50);
		lisianRadar.activate();
		lisianRadar.setDebugView(false);
		lisian.getSensors().add(lisianRadar);
		lisian.addAttachedObject(lisianRadar);
		
		
		theMap.addBoat(caspian);
		theMap.addBoat(lisian);
		
//		// One airplane, for test
//		Airplane caspianAir = new Airplane(theMap);
//		caspianAir.getProgrammedWPs().addWP(new Waypoint(100,50,0));
//		caspianAir.getProgrammedWPs().addWP(new Waypoint(600,600,0));
//		caspianAir.getProgrammedWPs().addWP(new Waypoint(100,200,0));
//		caspianAir.getProgrammedWPs().addWP(new Waypoint(600,150,0));
//		caspianAir.getProgrammedWPs().addWP(new Waypoint(100,600,0));
//		caspianAir.setLoop(true);
//		caspianAir.setFireAtWill(true);
//		
//		caspianAir.setComplement(2);
//		caspianAir.setCost(100000000);
//		caspianAir.setCurrentSpeed(1500);
//		caspianAir.setEnergy(1000);
//		caspianAir.setMaxSpeed(1000);
//		caspianAir.setOrientation(0);
//		caspianAir.setName("MSS Plourin");
//		caspianAir.setResistance(1);
//		caspianAir.setVisibilityLevel(0);
//		caspianAir.setNoiseSignature(2);
//		caspianAir.setVisibilitySignature(0);
//		caspianAir.setPosX(300);
//		caspianAir.setPosY(50);
//		caspianAir.setTypeFaction(FUnit.AIRPLANE_OUR);
//		caspianAir.setType(Airplane.FIGHTER);
//		caspianAir.createGfx(300, 50, 0, 0, 20);
//		
//		Radar ourRadar2;
//		ourRadar2= new Radar(theMap,caspianAir);
//		ourRadar2.createGfx(0, 0, 0, 0, 1, 1);
//		ourRadar2.setPosAttach(0, 0, 0);
//		ourRadar2.setPower(10);
//		ourRadar2.setSpeedRot(40);
//		ourRadar2.activate();
//		ourRadar2.setDebugView(false);
//		
//		caspianAir.getSensors().add(ourRadar2);
//		caspianAir.addAttachedObject(ourRadar2);
//		
//		theMap.addAirplane(caspianAir);
//		System.out.println("caspianAir airplane created");
		
		// One base, for test
		Base mainBase = new Base(theMap);
		mainBase.setFireAtWill(true);
		
		mainBase.setComplement(2);
		mainBase.setCost(100000000);
		mainBase.setEnergy(1000);
		mainBase.setOrientation(0);
		mainBase.setName("Main base");
		mainBase.setResistance(4);
		mainBase.setPosX(600);
		mainBase.setPosY(50);
		mainBase.setTypeFaction(FUnit.BASE_OUR);
		mainBase.setType(Base.SMALLBASEONE);
		mainBase.createGfx(600, 50, 0, 0, 20);
		
		Radar ourRadar3;
		ourRadar3= new Radar(theMap,mainBase);
		ourRadar3.createGfx(0, 0, 0, 0, 1, 1);
		ourRadar3.setPosAttach(-28, -16, 0);
		ourRadar3.setPower(50);
		ourRadar3.setSpeedRot(20);
		ourRadar3.activate();
		ourRadar3.setDebugView(false);
		
		mainBase.getSensors().add(ourRadar3);
		mainBase.addAttachedObject(ourRadar3);
		
		theMap.addBase(mainBase);
		System.out.println("Main base created");
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getOurSubs()
	 */
	public void addOurSubs()
	{
		CoreImage subImage = Submarine.getImageForMe();
		
		
		Submarine caspian = new Submarine(theMap);
		
		caspian.setAutonomous(true);
		caspian.setFireAtWill(true);
		caspian.setComplement(100);
		caspian.setCost(200000000);
		caspian.setCurrentSpeed(20);
		caspian.setEnergy(1000);
		caspian.setMaxSpeed(35);
		caspian.setOrientation(0.10);
		caspian.setName("MSS Plougasnou");
		caspian.setResistance(10);
		caspian.setVisibilityLevel(0);
		caspian.setNoiseSignature(2);
		caspian.setVisibilitySignature(0);
		caspian.setPosX(500);
		caspian.setPosY(50);
		caspian.setType(Submarine.NUKE);
		caspian.setTypeFaction(FUnit.SUB_OUR);
		caspian.createGfx(subImage, 500, 50, 0, 0, 20);
		caspian.setDepth(-50);
		caspian.setWantedDepth(-50);
		
		Sonar ourFrontSonar;
		ourFrontSonar= new Sonar(theMap,caspian);
		ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		ourFrontSonar.setPosAttach(20, 0, 0);
		ourFrontSonar.activate();
		
		caspian.getSensors().add(ourFrontSonar);
		caspian.addAttachedObject(ourFrontSonar);
		
		Submarine lisian = new Submarine(theMap);
		lisian.getProgrammedWPs().addWP(new Waypoint(150,300,0));
		lisian.getProgrammedWPs().addWP(new Waypoint(650,350,0));
		lisian.setLoop(true);
		lisian.setFireAtWill(true);
		lisian.setComplement(100);
		lisian.setCost(200000000);
		lisian.setCurrentSpeed(30);
		lisian.setEnergy(1000);
		lisian.setMaxSpeed(35);
		lisian.setOrientation(-0.5);
		lisian.setName("MSS Paray");
		lisian.setResistance(10);
		lisian.setVisibilityLevel(0);
		lisian.setNoiseSignature(2);
		lisian.setVisibilitySignature(0);
		lisian.setPosX(800);
		lisian.setPosY(500);
		lisian.setDepth(-80);
		lisian.setWantedDepth(-80);
		lisian.setTypeFaction(FUnit.SUB_OUR);
		lisian.setType(Submarine.NUKE);
		lisian.createGfx(subImage, 800, 500, 0, 0, 20);
		
		Sonar lisianFrontSonar;
		lisianFrontSonar= new Sonar(theMap,lisian);
		lisianFrontSonar.createGfx(0, 0, 0, 0, 1, 10);
		lisianFrontSonar.setPosAttach(40, 0, 0);
		lisianFrontSonar.setPower(2);
		lisianFrontSonar.activate();
		
		lisian.getSensors().add(lisianFrontSonar);
		lisian.addAttachedObject(lisianFrontSonar);
		
		theMap.addSub(caspian);
		theMap.addSub(lisian);
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getGlobalSensors()
	 */
	public void addGlobalSensor()
	{	
		Satellite myNewSatellite= new Satellite(theMap);
		myNewSatellite.createGfx(100, 100, 0, Math.PI/2 + 0.1, 20, 10);
		
		theMap.addGlobalSensor(myNewSatellite);
		// TODO Auto-generated method stub
	}
	

	public void addOthers()
	{
		// TODO Auto-generated method stub
		
	}

}
