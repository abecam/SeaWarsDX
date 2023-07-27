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

package com.tgb.subgame;

import java.util.ArrayList;
import java.util.List;

import com.tgb.subgame.unitspc.Saveable;

/**
 * Main class to keep all information common to the game, like score, nb of units...
 * @author Alain Becam
 *
 */
public class ScoreKeeper extends Saveable {
	long Score=0;
	long costOur=0;
	long costAllies=0;
	long costEnemies=0;
	
	long budgetOur     = 2000000000L;
	long budgetAllies  = 1500000000L;
	long budgetEnemies =3000000000L;
	
	long humansOur=0;
	long humansAllies=0;
	long humansEnemies=0;
	
	long miaOur=0;
	long miaAllies=0;
	long miaEnemies=0;
	
	public int nbCarrierLeftOur=0;
	public int nbCarrierTotalOur=0;
	public int nbBoatLeftOur=0;
	public int nbBoatTotalOur=0;
	public int nbSubLeftOur=0;
	public int nbSubTotalOur=0;
	public int nbBasesLeftOur=0;
	public int nbBasesTotalOur=0;
	
	public int nbCarrierLeftAllies=0;
	public int nbCarrierTotalAllies=0;
	public int nbBoatLeftAllies=0;
	public int nbBoatTotalAllies=0;
	public int nbSubLeftAllies=0;
	public int nbSubTotalAllies=0;
	public int nbBasesLeftAllies=0;
	public int nbBasesTotalAllies=0;
	
	public int nbCarrierLeftEnemies=0;
	public int nbCarrierTotalEnemies=0;
	public int nbBoatLeftEnemies=0;
	public int nbBoatTotalEnemies=0;
	public int nbSubLeftEnemies=0;
	public int nbSubTotalEnemies=0;
	public int nbBasesLeftEnemies=0;
	public int nbBasesTotalEnemies=0;
	
	public int nbAmmo=0;
	public int nbAmmoAllies=0;
	public int nbAmmoEne=0;
	public int nbMissiles=0;
	public int nbMissilesAllies=0;
	public int nbMissilesEne=0;
	public int nbTorpedoes=0;
	public int nbTorpedoesAllies=0;
	public int nbTorpedoesEne=0;
	
	static private ScoreKeeper instance=null;
	
	public boolean hasWon=false;
	
	private ScoreKeeper()
	{
		Score=0;
		costOur=0;
		costAllies=0;
		costEnemies=0;
		
		budgetOur     = 2000000000L;
		budgetAllies  = 1500000000L;
		budgetEnemies =3000000000L;
		
		humansOur=0;
		humansAllies=0;
		humansEnemies=0;
		
		miaOur=0;
		miaAllies=0;
		miaEnemies=0;
		
		nbCarrierLeftOur=0;
		nbCarrierTotalOur=0;
		nbBoatLeftOur=0;
		nbBoatTotalOur=0;
		nbSubLeftOur=0;
		nbSubTotalOur=0;
		nbBasesLeftOur=0;
		nbBasesTotalOur=0;
		
		nbCarrierLeftAllies=0;
		nbCarrierTotalAllies=0;
		nbBoatLeftAllies=0;
		nbBoatTotalAllies=0;
		nbSubLeftAllies=0;
		nbSubTotalAllies=0;
		nbBasesLeftAllies=0;
		nbBasesTotalAllies=0;
		
		nbCarrierLeftEnemies=0;
		nbCarrierTotalEnemies=0;
		nbBoatLeftEnemies=0;
		nbBoatTotalEnemies=0;
		nbSubLeftEnemies=0;
		nbSubTotalEnemies=0;
		nbBasesLeftEnemies=0;
		nbBasesTotalEnemies=0;
		
		nbAmmo=0;
		nbAmmoAllies=0;
		nbAmmoEne=0;
		nbMissiles=0;
		nbMissilesAllies=0;
		nbMissilesEne=0;
		nbTorpedoes=0;
		nbTorpedoesAllies=0;
		nbTorpedoesEne=0;
		
		hasWon=false;
	}

	public static ScoreKeeper getInstance()
	{
		if (instance== null)
			instance = new ScoreKeeper();
		
		return instance;
	}
	
	public void reset()
	{
		Score=0;
		costOur=0;
		costAllies=0;
		costEnemies=0;
		
		budgetOur     = 2000000000L;
		budgetAllies  = 1500000000L;
		budgetEnemies =3000000000L;
		
		humansOur=0;
		humansAllies=0;
		humansEnemies=0;
		
		miaOur=0;
		miaAllies=0;
		miaEnemies=0;
		
		nbCarrierLeftOur=0;
		nbCarrierTotalOur=0;
		nbBoatLeftOur=0;
		nbBoatTotalOur=0;
		nbSubLeftOur=0;
		nbSubTotalOur=0;
		nbBasesLeftOur=0;
		nbBasesTotalOur=0;
		
		nbCarrierLeftAllies=0;
		nbCarrierTotalAllies=0;
		nbBoatLeftAllies=0;
		nbBoatTotalAllies=0;
		nbSubLeftAllies=0;
		nbSubTotalAllies=0;
		nbBasesLeftAllies=0;
		nbBasesTotalAllies=0;
		
		nbCarrierLeftEnemies=0;
		nbCarrierTotalEnemies=0;
		nbBoatLeftEnemies=0;
		nbBoatTotalEnemies=0;
		nbSubLeftEnemies=0;
		nbSubTotalEnemies=0;
		nbBasesLeftEnemies=0;
		nbBasesTotalEnemies=0;
		
		nbAmmo=0;
		nbAmmoAllies=0;
		nbAmmoEne=0;
		nbMissiles=0;
		nbMissilesAllies=0;
		nbMissilesEne=0;
		nbTorpedoes=0;
		nbTorpedoesAllies=0;
		nbTorpedoesEne=0;
	}
	public void addCostOur(long costToAdd)
	{
		if (LevelKeeper.getInstance().nextLevelWanted == -1)
		{
			costOur+=costToAdd;
		}
	}
	
	public void addCostAllies(long costToAdd)
	{
		if (LevelKeeper.getInstance().nextLevelWanted == -1)
		{
			costAllies+=costToAdd;
		}
	}
	
	public void addCostEnemies(long costToAdd)
	{
		if (LevelKeeper.getInstance().nextLevelWanted == -1)
		{
			costEnemies+=costToAdd;
		}
	}
	
	public void buySomethingAllies(long amount)
	{
		budgetAllies-=costAllies;
	}
	
	public void buySomethingOur(long amount)
	{
		budgetOur-=costOur;
	}
	
	public void buySomethingEnemies(long amount)
	{
		budgetEnemies-=costEnemies;
	}
	
	public void addPoint(int points)
	{
		Score+=points;
	}
	
	public void addComplementOur(long nb)
	{
		humansOur+=nb;
	}
	
	public void addComplementAllies(long nb)
	{
		humansAllies+=nb;
	}
	
	public void addMIAEnemies(long nb)
	{
		//miaEnemies+=nb;
		if (LevelKeeper.getInstance().nextLevelWanted == -1)
		{
			miaEnemies+=nb;
		}
	}
	
	public void addMIAOur(long nb)
	{
		if (LevelKeeper.getInstance().nextLevelWanted == -1)
		{
			miaOur+=nb;
		}
	}
	
	public void addMIAAllies(long nb)
	{
		if (LevelKeeper.getInstance().nextLevelWanted == -1)
		{
			miaAllies+=nb;
		}
	}
	
	public void addComplementEnemies(long nb)
	{
		humansEnemies+=nb;
	}

	public void calculateScore()
	{
		Score=((long )((nbAmmoEne-nbAmmo) + (nbMissilesEne-nbMissiles) + (nbTorpedoesEne- nbTorpedoes)))  + (miaEnemies-miaOur) + ((costEnemies - costOur)/1000000);
	}
	
	public long getScore() {
		return Score;
	}

	public void setScore(long score) {
		Score = score;
	}

	public long getCostOur() {
		return costOur;
	}

	public void setCostOur(long costOur) {
		this.costOur = costOur;
	}

	public long getCostAllies() {
		return costAllies;
	}

	public void setCostAllies(long costAllies) {
		this.costAllies = costAllies;
	}

	public long getCostEnemies() {
		return costEnemies;
	}

	public void setCostEnemies(long costEnemies) {
		this.costEnemies = costEnemies;
	}

	public long getBudgetOur() {
		return budgetOur;
	}

	public void setBudgetOur(long budgetOur) {
		this.budgetOur = budgetOur;
	}

	public long getBudgetAllies() {
		return budgetAllies;
	}

	public void setBudgetAllies(long budgetAllies) {
		this.budgetAllies = budgetAllies;
	}

	public long getBudgetEnemies() {
		return budgetEnemies;
	}

	public void setBudgetEnemies(long budgetEnemies) {
		this.budgetEnemies = budgetEnemies;
	}

	public long getHumansOur() {
		return humansOur;
	}

	public void setHumansOur(long humansOur) {
		this.humansOur = humansOur;
	}

	public long getHumansAllies() {
		return humansAllies;
	}

	public void setHumansAllies(long humansAllies) {
		this.humansAllies = humansAllies;
	}

	public long getHumansEnemies() {
		return humansEnemies;
	}

	public void setHumansEnemies(long humansEnemies) {
		this.humansEnemies = humansEnemies;
	}

	public long getMiaOur()
	{
		return miaOur;
	}

	public void setMiaOur(long miaOur)
	{
		this.miaOur = miaOur;
	}

	public long getMiaAllies()
	{
		return miaAllies;
	}

	public void setMiaAllies(long miaAllies)
	{
		this.miaAllies = miaAllies;
	}

	public long getMiaEnemies()
	{
		return miaEnemies;
	}

	public void setMiaEnemies(long miaEnemies)
	{
		this.miaEnemies = miaEnemies;
	}

	public int getNbAmmo()
	{
		return nbAmmo;
	}

	public void setNbAmmo(int nbAmmo)
	{
		this.nbAmmo = nbAmmo;
	}

	public void addAmmo(int nbAmmo)
	{
		this.nbAmmo += nbAmmo;
		this.costOur+=6;
	}

	
	public int getNbAmmoAllies()
	{
		return nbAmmoAllies;
	}

	public void setNbAmmoAllies(int nbAmmoAllies)
	{
		this.nbAmmoAllies = nbAmmoAllies;
	}
	
	public void addAmmoAllies(int nbAmmoAllies)
	{
		this.nbAmmoAllies += nbAmmoAllies;
		this.costAllies+=6;
	}

	public int getNbAmmoEne()
	{
		return nbAmmoEne;
	}

	public void setNbAmmoEne(int nbAmmoEne)
	{
		this.nbAmmoEne = nbAmmoEne;
	}
	
	public void addAmmoEne(int nbAmmoEne)
	{
		this.nbAmmoEne += nbAmmoEne;
		this.costEnemies+=6;
	}

	public int getNbMissiles()
	{
		return nbMissiles;
	}

	public void setNbMissiles(int nbMissiles)
	{
		this.nbMissiles = nbMissiles;
	}
	
	public void addMissiles(int nbMissiles)
	{
		this.nbMissiles += nbMissiles;
		this.costOur+=120000;
	}

	public int getNbMissilesAllies()
	{
		return nbMissilesAllies;
	}

	public void setNbMissilesAllies(int nbMissilesAllies)
	{
		this.nbMissilesAllies = nbMissilesAllies;
	}

	public void addMissilesAllies(int nbMissilesAllies)
	{
		this.nbMissilesAllies += nbMissilesAllies;
		this.costAllies+=120000;
	}
	
	public int getNbMissilesEne()
	{
		return nbMissilesEne;
	}

	public void setNbMissilesEne(int nbMissilesEne)
	{
		this.nbMissilesEne = nbMissilesEne;
	}
	
	public void addMissilesEne(int nbMissilesEne)
	{
		this.nbMissilesEne += nbMissilesEne;
		this.costEnemies+=120000;
	}

	public int getNbTorpedoes()
	{
		return nbTorpedoes;
	}

	public void setNbTorpedoes(int nbTorpedoes)
	{
		this.nbTorpedoes = nbTorpedoes;
	}
	
	public void addTorpedoes(int nbTorpedoes)
	{
		this.nbTorpedoes += nbTorpedoes;
		this.costOur+=150000;
	}

	public int getNbTorpedoesAllies()
	{
		return nbTorpedoesAllies;
	}

	public void setNbTorpedoesAllies(int nbTorpedoesAllies)
	{
		this.nbTorpedoesAllies = nbTorpedoesAllies;
	}
	
	public void addTorpedoesAllies(int nbTorpedoesAllies)
	{
		this.nbTorpedoesAllies += nbTorpedoesAllies;
		this.costAllies+=150000;
	}

	public int getNbTorpedoesEne()
	{
		return nbTorpedoesEne;
	}

	public void setNbTorpedoesEne(int nbTorpedoesEne)
	{
		this.nbTorpedoesEne = nbTorpedoesEne;
	}
	
	public void addTorpedoesEne(int nbTorpedoesEne)
	{
		this.nbTorpedoesEne += nbTorpedoesEne;
		this.costEnemies+=150000;
	}

	public boolean isHasWon()
	{
		return hasWon;
	}

	public void setHasWon(boolean hasWon)
	{
		this.hasWon = hasWon;
	}

	@Override
	public String giveMyId()
	{
		// TODO Auto-generated method stub
		return "ScoreKeeper";
	}

	@Override
	public String save()
	{
		StringBuffer toSave = new StringBuffer();

		toSave.append("Score=");
		toSave.append(Score+"\n");
		
		toSave.append("costOur=");
		toSave.append(costOur+"\n");
		 
		toSave.append("costAllies=");
		toSave.append(costAllies+"\n");
		 
		toSave.append("costEnemies=");
		toSave.append(costEnemies+"\n");
		
		 
		toSave.append("budgetOur=");
		toSave.append(budgetOur+"\n");
			 
		toSave.append("budgetAllies=");
		toSave.append(budgetAllies+"\n");
			 
		toSave.append("budgetEnemies=");
		toSave.append(budgetEnemies+"\n");		
		 
		toSave.append("humansOur=");
		toSave.append(humansOur+"\n");
				
		toSave.append("humansAllies=");
		toSave.append(humansAllies+"\n");
				 
		toSave.append("humansEnemies=");
		toSave.append(humansEnemies+"\n");	
		 
		toSave.append("miaOur=");
		toSave.append(miaOur+"\n");
				 
		toSave.append("miaAllies=");
		toSave.append(miaAllies+"\n");
		 
		toSave.append("miaEnemies=");
		toSave.append(miaEnemies+"\n");
		
		 
		toSave.append("nbCarrierLeftOur=");
		toSave.append(nbCarrierLeftOur+"\n");
		 
		toSave.append("nbCarrierTotalOur=");
		toSave.append(nbCarrierTotalOur+"\n");
		 
		toSave.append("nbBoatLeftOur=");
		toSave.append(nbBoatLeftOur+"\n");
		 
		toSave.append("nbBoatTotalOur=");
		toSave.append(nbBoatTotalOur+"\n");
		 
		toSave.append("nbSubLeftOur=");
		toSave.append(nbSubLeftOur+"\n");
		 
		toSave.append("nbSubTotalOur=");
		toSave.append(nbSubTotalOur+"\n");
		 
		toSave.append("nbBasesLeftOur=");
		toSave.append(nbBasesLeftOur+"\n");
		 
		toSave.append("nbBasesTotalOur=");
		toSave.append(nbBasesTotalOur+"\n");
		
		 
		toSave.append("nbCarrierLeftAllies=");
		toSave.append(nbCarrierLeftAllies+"\n");
		 
		toSave.append("nbCarrierTotalAllies=");
		toSave.append(nbCarrierTotalAllies+"\n");
		 
		toSave.append("nbBoatLeftAllies=");
		toSave.append(nbBoatLeftAllies+"\n");
		 
		toSave.append("nbBoatTotalAllies=");
		toSave.append(nbBoatTotalAllies+"\n");
		 
		toSave.append("nbSubLeftAllies=");
		toSave.append(nbSubLeftAllies+"\n");
		 
		toSave.append("nbSubTotalAllies=");
		toSave.append(nbSubTotalAllies+"\n");
		 
		toSave.append("nbBasesLeftAllies=");
		toSave.append(nbBasesLeftAllies+"\n");
		 
		toSave.append("nbBasesTotalAllies=");
		toSave.append(nbBasesTotalAllies+"\n");
		
		 
		toSave.append("nbCarrierLeftEnemies=");
		toSave.append(nbCarrierLeftEnemies+"\n");
		 
		toSave.append("nbCarrierTotalEnemies=");
		toSave.append(nbCarrierTotalEnemies+"\n");
		 
		toSave.append("nbBoatLeftEnemies=");
		toSave.append(nbBoatLeftEnemies+"\n");
		 
		toSave.append("nbBoatTotalEnemies=");
		toSave.append(nbBoatTotalEnemies+"\n");
		 
		toSave.append("nbSubLeftEnemies=");
		toSave.append(nbSubLeftEnemies+"\n");
		 
		toSave.append("nbSubTotalEnemies=");
		toSave.append(nbSubTotalEnemies+"\n");
		 
		toSave.append("nbBasesLeftEnemies=");
		toSave.append(nbBasesLeftEnemies+"\n");
		 
		toSave.append("nbBasesTotalEnemies=");
		toSave.append(nbBasesTotalEnemies+"\n");
		
		 
		toSave.append("nbAmmo=");
		toSave.append(nbAmmo+"\n");
		 
		toSave.append("nbAmmoAllies=");
		toSave.append(nbAmmoAllies+"\n");
		 
		toSave.append("nbAmmoEne=");
		toSave.append(nbAmmoEne+"\n");
		 
		toSave.append("nbMissiles=");
		toSave.append(nbMissiles+"\n");
		 
		toSave.append("nbMissilesAllies=");
		toSave.append(nbMissilesAllies+"\n");
		 
		toSave.append("nbMissilesEne=");
		toSave.append(nbMissilesEne+"\n");
		 
		toSave.append("nbTorpedoes=");
		toSave.append(nbTorpedoes+"\n");
		 
		toSave.append("nbTorpedoesAllies=");
		toSave.append(nbTorpedoesAllies+"\n");
		 
		toSave.append("nbTorpedoesEne=");
		toSave.append(nbTorpedoesEne+"\n");

		return toSave.toString();
	}

	@Override
	public boolean loadFromOneLine(String oneLine)
	{
		if (oneLine.contains("Score"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("Score is "+content);
			Score = new Long(content);

			return true;
		}

		if (oneLine.contains("costOur"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("costOur is "+content);
			costOur = new Long(content);

			return true;
		}

		if (oneLine.contains("costAllies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("costAllies is "+content);
			costAllies = new Long(content);

			return true;
		}


		if (oneLine.contains("costEnemies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("costEnemies is "+content);
			costEnemies = new Long(content);

			return true;
		}

		if (oneLine.contains("budgetOur"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("budgetOur is "+content);
			budgetOur = new Long(content);

			return true;
		}

		if (oneLine.contains("budgetAllies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("budgetAllies is "+content);
			budgetAllies = new Long(content);

			return true;
		}

		if (oneLine.contains("budgetEnemies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("budgetEnemies is "+content);
			budgetEnemies = new Long(content);

			return true;
		}

		if (oneLine.contains("humansOur"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("humansOur is "+content);
			humansOur = new Long(content);

			return true;
		}

		if (oneLine.contains("humansAllies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("humansAllies is "+content);
			humansAllies = new Long(content);

			return true;
		}

		if (oneLine.contains("humansEnemies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("humansEnemies is "+content);
			humansEnemies = new Long(content);

			return true;
		}

		if (oneLine.contains("miaOur"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("miaOur is "+content);
			miaOur = new Long(content);

			return true;
		}

		if (oneLine.contains("miaAllies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("miaAllies is "+content);
			miaAllies = new Long(content);

			return true;
		}

		if (oneLine.contains("miaEnemies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("miaEnemies is "+content);
			miaEnemies = new Long(content);

			return true;
		}

		if (oneLine.contains("nbCarrierLeftOur"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbCarrierLeftOur is "+content);
			nbCarrierLeftOur = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbCarrierTotalOur"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbCarrierTotalOur is "+content);
			nbCarrierTotalOur = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbBoatLeftOur"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbBoatLeftOur is "+content);
			nbBoatLeftOur = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbBoatTotalOur"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbBoatTotalOur is "+content);
			nbBoatTotalOur = new Integer(content);

			return true;
		}
		if (oneLine.contains("nbSubLeftOur"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbSubLeftOur is "+content);
			nbSubLeftOur = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbSubTotalOur"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbSubTotalOur is "+content);
			nbSubTotalOur = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbBasesLeftOur"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbBasesLeftOur is "+content);
			nbBasesLeftOur = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbBasesTotalOur"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbBasesTotalOur is "+content);
			nbBasesTotalOur = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbCarrierLeftAllies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbCarrierLeftAllies is "+content);
			nbCarrierLeftAllies = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbCarrierTotalAllies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbCarrierTotalAllies is "+content);
			nbCarrierTotalAllies = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbBoatLeftAllies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbBoatLeftAllies is "+content);
			nbBoatLeftAllies = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbBoatTotalAllies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbBoatTotalAllies is "+content);
			nbBoatTotalAllies = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbSubLeftAllies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbSubLeftAllies is "+content);
			nbSubLeftAllies = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbSubTotalAllies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbSubTotalAllies is "+content);
			nbSubTotalAllies = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbBasesLeftAllies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbBasesLeftAllies is "+content);
			nbBasesLeftAllies = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbBasesTotalAllies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbBasesTotalAllies is "+content);
			nbBasesTotalAllies = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbCarrierLeftEnemies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbCarrierLeftEnemies is "+content);
			nbCarrierLeftEnemies = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbCarrierTotalEnemies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbCarrierTotalEnemies is "+content);
			nbCarrierTotalEnemies = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbBoatLeftEnemies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbBoatLeftEnemies is "+content);
			nbBoatLeftEnemies = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbBoatTotalEnemies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbBoatTotalEnemies is "+content);
			nbBoatTotalEnemies = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbSubLeftEnemies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbSubLeftEnemies is "+content);
			nbSubLeftEnemies = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbSubTotalEnemies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbSubTotalEnemies is "+content);
			nbSubTotalEnemies = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbBasesLeftEnemies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbBasesLeftEnemies is "+content);
			nbBasesLeftEnemies = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbBasesTotalEnemies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbBasesTotalEnemies is "+content);
			nbBasesTotalEnemies = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbAmmo"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbAmmo is "+content);
			nbAmmo = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbAmmoAllies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbAmmoAllies is "+content);
			nbAmmoAllies = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbAmmoEne"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbAmmoEne is "+content);
			nbAmmoEne = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbMissiles"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbMissiles is "+content);
			nbMissiles = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbMissilesAllies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbMissilesAllies is "+content);
			nbMissilesAllies = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbMissilesEne"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbMissilesEne is "+content);
			nbMissilesEne = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbTorpedoes"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbTorpedoes is "+content);
			nbTorpedoes = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbTorpedoesAllies"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbTorpedoesAllies is "+content);
			nbTorpedoesAllies = new Integer(content);

			return true;
		}

		if (oneLine.contains("nbTorpedoesEne"))
		{
			String content=oneLine.substring(oneLine.indexOf("=")+1);
			System.out.println("nbTorpedoesEne is "+content);
			nbTorpedoesEne = new Integer(content);

			return true;
		}

		return false;
	}
}
