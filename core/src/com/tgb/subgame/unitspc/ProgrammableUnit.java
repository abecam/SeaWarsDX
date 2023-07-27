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

import java.util.ArrayList;
import java.util.List;

import com.tgb.subengine.gameentities.Journey;
import com.tgb.subgame.*;

/**
 * Superclass for the unit's AI
 * @author Alain Becam
 *
 */
public abstract class ProgrammableUnit extends Saveable implements java.io.Serializable  
{
	int idBoat=-1;
	int idOwner; // Who own it.
	
	int idMap=-1; // Id for the strategic map
	
	int idGroup=-1; // If I am part of a group (typically a carrier with cover units).
	// Is also the ID of the main unit!!!
	ArrayList<ProgrammableUnit> myGroup; // If leader, the group
	boolean isLeaderOfGroup=false;
	
	String name;
	
	long XP;
	long level;
	long newLevelReachable;
	boolean levelChanged; // Has the level changed?
	
	int energy;
	int energyMax;
	
	int type;
	int typeFaction; // As defined in FUnit
	
	double wantedSpeed; // Speed as asked by the tactical map
	
	transient Journey programmedWPs;
	
	boolean wpsUpdated=false;
	
	int nbFighters; // If it is a carrier or a base.
	int nbFightersMax;
	int nbFightersOnFlightMax;
	int currentNbFightersInFlight;
	int nbAwacs;
	int nbAwacsSession; // nb of awacs for one level, they might be regenerated (base or close to a base)
	int nbTankers;
	
	boolean started= false;
	boolean loop= false; // Will loop through the WPs?
	int indexWP=0; // Which WP are we seeking now (for loop)
	
	transient ArrayList<FUnit> foundUnits;
	
	boolean autonomous=true; // Select where to go by itself!
	
	boolean fireAtWill=false;
	
	boolean ignoreFriends=false;
	
	boolean attackNeutrals=false;
	
	boolean followWP=true;
	
	int priorityAttack=0; // Who to attack first (0 if none).
	
	int targetID;
	
	double targetX,targetY,targetDepth; // In case the target is not known, but was detected.
	
	LevelMap theMap; // The tactical map, so the unit know who is where.
	
	boolean drawWP=false; // Draw the current wps.
	
	boolean view=false; // Is this unit in view?
	
	public abstract void doUpdate(double time);
	
	public static final int FIRE_MISSILE=0;
	public static final int FIRE_TORPEDO=1;
	
	boolean fireAsked=false;
	boolean fireInSalve=false;
	boolean fireWithWP=false;
	boolean fireWithSeek=false;
	boolean fireWithTarget=false;
	int typeFireAsked;
	double xFireAsked,yFireAsked;
	
	transient Journey wpsToFollow;
	
	transient LevelKeeper ourLevelKeeper; // Allow to see the depth (alpha of the current map)
	
	double xMap,yMap; // Position on the global map
	double xTmpMap,yTmpMap; //
	double xMapTarget=0,yMapTarget=0;
	boolean followTargetMap=false;
	double xMapSpeed=0,yMapSpeed=0;
	protected long complement;
	long complementNorm;
	protected boolean dead = false;
	protected double posX;
	protected double posY;
	protected double depth;

	protected double rudderEfficiency;
	double actualSpeed;
	double angleToTurn; // Angle d�sir� pour tourner
	
	// Is this unit limited by the visible level boundaries? (also excluding the right menu)
	boolean boundaryLimited=true;
	// Do this unit send its airplanes (if available)?
	boolean flightsOn=true;
	
	ProgrammableUnitSavePart ourSaveableField;
	
	public String save()
	{
		// First get the fields
		ourSaveableField = new ProgrammableUnitSavePart(idBoat, idOwner, idGroup, myGroup, isLeaderOfGroup, name, 
				XP, level, newLevelReachable, energy, energyMax, type, typeFaction, nbFighters, nbFightersMax, nbFightersOnFlightMax, 
				currentNbFightersInFlight, nbAwacs, nbAwacsSession, nbTankers, xMap, yMap, complement, complementNorm, 
				dead, posX, posY, depth, rudderEfficiency, actualSpeed, angleToTurn, boundaryLimited, flightsOn,
				autonomous, fireAtWill, ignoreFriends);
		StringBuffer toSave = new StringBuffer();
		
		toSave.append(saveIfNeeded());

		toSave.append(ourSaveableField.saveAll());		
				
		return toSave.toString();
	}
	
	public abstract String saveIfNeeded();
	
	public abstract List<String> loadIfSupported(List<String> myPart);
	
	/**
	 * The parent loader will use the Id to extract the good part
	 * @param myPart
	 */
	public List<String> load(List<String> myPart)
	{
		List<String> remainingList = loadIfSupported(myPart);
		
		ourSaveableField = new ProgrammableUnitSavePart(idBoat, idOwner, idGroup, myGroup, isLeaderOfGroup, name, 
				XP, level, newLevelReachable, energy, energyMax, type, typeFaction, nbFighters, nbFightersMax, nbFightersOnFlightMax, 
				currentNbFightersInFlight, nbAwacs, nbAwacsSession, nbTankers, xMap, yMap, complement, complementNorm, 
				dead, posX, posY, depth, rudderEfficiency, actualSpeed, angleToTurn, boundaryLimited, flightsOn,
				autonomous, fireAtWill, ignoreFriends);
		
		remainingList = ourSaveableField.load(remainingList);
		
		idBoat = ourSaveableField.idBoat;
		idOwner = ourSaveableField.idOwner;
		idGroup = ourSaveableField.idGroup; 
		myGroup = ourSaveableField.myGroup; 
		isLeaderOfGroup = ourSaveableField.isLeaderOfGroup;
		name = ourSaveableField.name;
		XP = ourSaveableField.XP;
		level = ourSaveableField.level;
		newLevelReachable = ourSaveableField.newLevelReachable;
		energy = ourSaveableField.energy;
		energyMax = ourSaveableField.energyMax;
		type = ourSaveableField.type;
		typeFaction = ourSaveableField.typeFaction;
		nbFighters = ourSaveableField.nbFighters;
		nbFightersMax = ourSaveableField.nbFightersMax;
		nbFightersOnFlightMax = ourSaveableField.nbFightersOnFlightMax;
		currentNbFightersInFlight = ourSaveableField.currentNbFightersInFlight;
		nbAwacs = ourSaveableField.nbAwacs;
		nbAwacsSession = ourSaveableField.nbAwacsSession;
		nbTankers = ourSaveableField.nbTankers;
		xMap = ourSaveableField.xMap;
		yMap = ourSaveableField.yMap;
		complement = ourSaveableField.complement;
		complementNorm = ourSaveableField.complementNorm;
		dead = ourSaveableField.dead;
		posX = ourSaveableField.posX;
		posY = ourSaveableField.posY;
		depth = ourSaveableField.depth;
		rudderEfficiency = ourSaveableField.rudderEfficiency;
		actualSpeed = ourSaveableField.actualSpeed;
		angleToTurn = ourSaveableField.angleToTurn;
		boundaryLimited = ourSaveableField.boundaryLimited;
		flightsOn = ourSaveableField.flightsOn;
		autonomous = ourSaveableField.autonomous;
		fireAtWill = ourSaveableField.fireAtWill;
		ignoreFriends = ourSaveableField.ignoreFriends;
		
		return remainingList;
	}
	
	/**
	 * loadFromOneLine is used only in the saveable fields
	 */
	@Override
	public boolean loadFromOneLine(String oneLine)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public ProgrammableUnit(LevelMap theMap)
	{
		isLeaderOfGroup=false;
		programmedWPs = new Journey();
		this.theMap=theMap;
		foundUnits = new ArrayList<FUnit>();
		
		xMapTarget=0;
		yMapTarget=0;
		followTargetMap=false;
		xMapSpeed=0;
		yMapSpeed=0;
		
		ourLevelKeeper=LevelKeeper.getInstance();
		
		level=0;
		XP=0;
		
		rudderEfficiency = 80;
		angleToTurn=0;
	}
	
	public ProgrammableUnit()
	{
		isLeaderOfGroup=false;
		programmedWPs = new Journey();
		foundUnits = new ArrayList<FUnit>();
		
		xMapTarget=0;
		yMapTarget=0;
		followTargetMap=false;
		xMapSpeed=0;
		yMapSpeed=0;
		
		rudderEfficiency = 80;
		angleToTurn=0;
		
		ourLevelKeeper=LevelKeeper.getInstance();
	}

	
	public LevelMap getTheMap() {
		return theMap;
	}

	public void setTheMap(LevelMap theMap) {
		this.theMap = theMap;
	}

	/**
	 * For unit that can fire, request a fire of the good weapon when possible.
	 * 
	 * @param isSalve
	 * @param followWP
	 * @param type
	 */
	public void requestFire(boolean isSalve, boolean followWP, boolean seek, boolean withTarget, int type, double xToTarget,double yToTarget, Journey setOfWPs)
	{
		fireAsked=true;
		fireInSalve=isSalve;
		fireWithWP=followWP;
		typeFireAsked=type;
		xFireAsked=xToTarget;
		yFireAsked=yToTarget;
		fireWithSeek=seek;
		fireWithTarget=withTarget;
		wpsToFollow=setOfWPs;
	}
	
	/**
	 * End fire...
	 */
	public void endFire()
	{
		fireAsked=false;
	}
	
	/**
	 * @return the energy
	 */
	public int getEnergy()
	{
		return energy;
	}
	/**
	 * @param energy the energy to set
	 */
	public void setEnergy(int energy)
	{
		this.energy = energy;
	}
	public int getEnergyMax()
	{
		return energyMax;
	}

	public void setEnergyMax(int energyMax)
	{
		this.energyMax = energyMax;
		this.energy = energyMax;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	
	/**
	 * @return the attackNeutrals
	 */
	public boolean isAttackNeutrals()
	{
		return attackNeutrals;
	}

	/**
	 * @param attackNeutrals the attackNeutrals to set
	 */
	public void setAttackNeutrals(boolean attackNeutrals)
	{
		this.attackNeutrals = attackNeutrals;
	}

	/**
	 * @return the autonomous
	 */
	public boolean isAutonomous()
	{
		return autonomous;
	}

	/**
	 * @param autonomous the autonomous to set
	 */
	public void setAutonomous(boolean autonomous)
	{
		this.autonomous = autonomous;
	}

	/**
	 * @return the fireAtWill
	 */
	public boolean isFireAtWill()
	{
		return fireAtWill;
	}

	/**
	 * @param fireAtWill the fireAtWill to set
	 */
	public void setFireAtWill(boolean fireAtWill)
	{
		this.fireAtWill = fireAtWill;
	}

	/**
	 * @return the ignoreFriends
	 */
	public boolean isIgnoreFriends()
	{
		return ignoreFriends;
	}

	public boolean isView() {
		return view;
	}

	public void setView(boolean view) {
		this.view = view;
	}

	/**
	 * @param ignoreFriends the ignoreFriends to set
	 */
	public void setIgnoreFriends(boolean ignoreFriends)
	{
		this.ignoreFriends = ignoreFriends;
	}

	/**
	 * @return the priorityAttack
	 */
	public int getPriorityAttack()
	{
		return priorityAttack;
	}

	/**
	 * @param priorityAttack the priorityAttack to set
	 */
	public void setPriorityAttack(int priorityAttack)
	{
		this.priorityAttack = priorityAttack;
	}

	/**
	 * @return the programmedWPs
	 */
	public Journey getProgrammedWPs()
	{
		return programmedWPs;
	}

	/**
	 * @param programmedWPs the programmedWPs to set
	 */
	public void setProgrammedWPs(Journey programmedWPs)
	{
		this.programmedWPs = programmedWPs;
	}

	/**
	 * @return the targetID
	 */
	public int getTargetID()
	{
		return targetID;
	}

	/**
	 * @param targetID the targetID to set
	 */
	public void setTargetID(int targetID)
	{
		this.targetID = targetID;
	}
	
	public void addFoundUnit(FUnit oneUnit)
	{
		foundUnits.add(oneUnit);
	}
	
	public void cleanFoundUnits()
	{
		foundUnits.clear();
	}

	public double getTargetX() {
		return targetX;
	}

	public void setTargetX(double targetX) {
		this.targetX = targetX;
	}

	public double getTargetY() {
		return targetY;
	}

	public void setTargetY(double targetY) {
		this.targetY = targetY;
	}

	public void setTargetPos(double targetX,double targetY,double targetDepth)
	{
		this.targetX = targetX;
		this.targetY = targetY;
		this.targetDepth = targetDepth;
	}
	
	public double getTargetDepth() {
		return targetDepth;
	}

	public void setTargetDepth(double targetDepth) {
		this.targetDepth = targetDepth;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
		this.programmedWPs.setLoop(loop);
	}

	public int getIdBoat() {
		return idBoat;
	}

	public void setIdBoat(int idBoat) {
		this.idBoat = idBoat;
	}
	
	public int getIdOwner() {
		return idOwner;
	}


	public void setIdOwner(int idOwner) {
		this.idOwner = idOwner;
	}
	
	/**
	 * @return the type
	 */
	public int getType()
	{
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type)
	{
		this.type = type;
	}
	public double getRudderEfficiency()
	{
		return rudderEfficiency;
	}

	public void setRudderEfficiency(double rudderEfficiency)
	{
		this.rudderEfficiency = rudderEfficiency;
	}

	public int getTypeFaction() {
		return typeFaction;
	}

	public abstract void setTypeFaction(int typeFaction);
	
	public void drawWP(){
		drawWP=true;
		if (theMap.getMyWPDrawer() != null)
		{
			theMap.getMyWPDrawer().setUpAndDrawWPs(this.programmedWPs);
		}
		else
		{
			System.err.println("PU->drawWP: myWPDrawer is already null, shouldn't be...");
		}
	}
	
	public void undrawWP(){
		if (drawWP)
		{
			if (theMap.getMyWPDrawer() != null)
			{
				theMap.getMyWPDrawer().hideMe();
			}
			else
			{
				System.err.println("PU->undrawWP: myWPDrawer is already null, shouldn't be...");
			}
		}
		drawWP=false;
	}

	public double getWantedSpeed() {
		return wantedSpeed;
	}

	public void setWantedSpeed(double wantedSpeed) {
		this.wantedSpeed = wantedSpeed;
	}

	public boolean isFollowWP() {
		return followWP;
	}

	public void setFollowWP(boolean followWP) {
		if (this.followWP != followWP)
		{
			this.followWP = followWP;
		}
	}
	
	public double getXMap() {
		return xMap;
	}

	public void setXMap(int x) 
	{
		xMap = x;
	}

	public double getYMap() 
	{
		return yMap;
	}

	public void setYMap(int y) 
	{
		yMap = y;
	}
	
	public void setPosMap(double x,double y)
	{
		xMap=x;
		yMap=y;
	}

	public int getIdMap() {
		return idMap;
	}

	public void setIdMap(int idMap) {
		this.idMap = idMap;
	}

	public int getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(int idGroup) {
		this.idGroup = idGroup;
	}
	
	public void addAirplaneInFlight()
	{
		currentNbFightersInFlight++;
	}
	
	public void airplaneDead()
	{
		currentNbFightersInFlight--;
	}
	
	public void resetAirplaneInFlight()
	{
		currentNbFightersInFlight=0;
	}
	
	public int getNbFighters() {
		return nbFighters;
	}

	/**
	 * Initial set-up of nb of fighters, also max so.
	 * @param nbFighters
	 */
	public void setNbFighters(int nbFighters) {
		this.nbFighters = nbFighters;
		this.nbFightersMax = nbFighters;
	}

	public int getNbAwacs() {
		return nbAwacs;
	}

	public void setNbAwacs(int nbAwacs) {
		this.nbAwacs = nbAwacs;
		this.nbAwacsSession=this.nbAwacs;
	}

	public int getNbTankers() {
		return nbTankers;
	}

	public void setNbTankers(int nbTankers) {
		this.nbTankers = nbTankers;
	}

	public int getNbFightersOnFlightMax() {
		return nbFightersOnFlightMax;
	}

	public void setNbFightersOnFlightMax(int nbFightersOnFlightMax) {
		this.nbFightersOnFlightMax = nbFightersOnFlightMax;
	}

	public ArrayList<ProgrammableUnit> getMyGroup() {
		return myGroup;
	}

	public void setMyGroup(ArrayList<ProgrammableUnit> myGroup) {
		this.myGroup = myGroup;
	}

	public boolean isLeaderOfGroup() {
		return isLeaderOfGroup;
	}

	public void setLeaderOfGroup(boolean isLeaderOfGroup) {
		this.isLeaderOfGroup = isLeaderOfGroup;
	}

	public void setPosMapTarget(double targetX, double targetY)
	{
		xMapTarget = targetX;
		yMapTarget = targetY;
	}
	
	public double getXMapTarget()
	{
		return xMapTarget;
	}

	public void setXMapTarget(double targetX)
	{
		xMapTarget = targetX;
	}

	public double getYMapTarget()
	{
		return yMapTarget;
	}

	public void setYMapTarget(double targetY)
	{
		yMapTarget = targetY;
	}

	public double getXMapSpeed()
	{
		return xMapSpeed;
	}

	public void setXMapSpeed(double mapSpeed)
	{
		xMapSpeed = mapSpeed;
	}

	public double getYMapSpeed()
	{
		return yMapSpeed;
	}

	public void setYMapSpeed(double mapSpeed)
	{
		yMapSpeed = mapSpeed;
	}
	
	abstract public void doUpdateSM(double time);

	public boolean isFollowTargetMap()
	{
		return followTargetMap;
	}

	public void setFollowTargetMap(boolean followTargetMap)
	{
		this.followTargetMap = followTargetMap;
	}

	/**
	 * @return the complement
	 */
	public long getComplement()
	{
		return complement;
	}

	/**
	 * @param complement the complement to set
	 */
	public void setComplement(long complement)
	{
		this.complement = complement;
	}

	/**
	 * @return the normal complement
	 */
	public long getComplementNorm()
	{
		return complementNorm;
	}

	/**
	 * @param complement the normal complement to set
	 */
	public void setComplementNorm(long complementNorm)
	{
		this.complementNorm = complementNorm;
	}

	public boolean isDead()
	{
		return dead;
	}

	/**
	 * @return the depth
	 */
	public double getDepth()
	{
		return depth;
	}

	/**
	 * @return the posX
	 */
	public double getPosX()
	{
		return posX;
	}

	/**
	 * @param posX the posX to set
	 */
	public void setPosX(double posX)
	{
		this.posX = posX;
	}

	/**
	 * @return the posY
	 */
	public double getPosY()
	{
		return posY;
	}

	/**
	 * @param posY the posY to set
	 */
	public void setPosY(double posY)
	{
		this.posY = posY;
	}

	public long getXP()
	{
		return XP;
	}

	public long getLevel()
	{
		return level;
	}
	
//	public boolean addXP(long nbXP)
//	{
//		XP+=nbXP;
//		newLevelReachable=(long )Math.floor(10000*Math.log((((double )XP)/300+1)));
//		if (newLevelReachable != level)
//		{
//			//level = newLevelReachable;
//			System.out.println(XP+" - New level for unit "+this.idBoat+" :"+newLevelReachable);
//			this.levelUp();
//			return true;
//		}
//		return false;
//	}
	
	public long addXP(long value)
    {
        this.XP+=value;
        
        long limit = 80+4*((long)(Math.exp(newLevelReachable+2)));

        if (XP > limit)
        {
        	newLevelReachable++;
        	levelUp();
            System.out.println(XP+" - New level for unit "+this.idBoat+" :"+newLevelReachable+" - Level at "+limit);
            return (limit);
        }
        return 0;
    }

	
	// Callback for a level up !
	public abstract void levelUp();
	
	public abstract void showLabel();
	
	public abstract void hideLabel();
	
	protected void wpHasBeenUpdated()
	{
		wpsUpdated = true;
	}
	
	public boolean hasWPBeenUpdated()
	{
		if (wpsUpdated)
		{
			wpsUpdated = false; // Reset
			return true;
		}
		return false;
	}

	public boolean isBoundaryLimited()
	{
		return boundaryLimited;
	}

	public void setBoundaryLimited(boolean boundaryLimited)
	{
		this.boundaryLimited = boundaryLimited;
	}

	public boolean isFlightsOn()
	{
		return flightsOn;
	}

	public void setFlightsOn(boolean flightsOn)
	{
		this.flightsOn = flightsOn;
	}
	
	/**
	 * Can be repaired by a base or a special unit (or anything else)
	 * Here check if the unit can level up
	 * @param amount
	 */
	public void getRepaired(int amount)
	{
		addXP(0);
	}

	public abstract String getTypeName();
}
