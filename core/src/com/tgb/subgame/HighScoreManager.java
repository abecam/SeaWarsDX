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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Simple class to upload and download the player's scores using simple web-services
 * The other part is written in PHP and use MySQL.
 * 
 * NB: about the licence. The methods come from the pulpcore web-forum, with
 * slight modifications for my use. Therefore, the above licence covers the content
 * aspect (things like "NbMissilesEne" or "http://www.thegiantball.com/~GBT/php/addLevelScore.php?"
 * Feel free to use the methods without the licence, if only you change these values.
 * @author Alain Becam
 *
 */
public class HighScoreManager {
    private static int numberOfScores = 500;
    private static String [] outText = new String[numberOfScores];

    public static void appendLevelScore(String name, String dateOfScore,long Score,int NbAmmo,int NbAmmoEne,int NbMissiles,int NbMissilesEne,int NbTorpedoes,int NbTorpedoesEne,long MIA,long MIAEne,long Cost,long CostEne) 
    {

    	try {
    		System.out.println(dateOfScore);
    		// Construct data
    		String data = URLEncoder.encode("Name", "UTF-8") + "=" +
    		URLEncoder.encode(name, "UTF-8");
    		data += "&" + URLEncoder.encode("DateOfScore", "UTF-8") + "=" +
    		URLEncoder.encode(dateOfScore , "UTF-8");
    		data += "&" + URLEncoder.encode("Score", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(Score) , "UTF-8");
    		data += "&" + URLEncoder.encode("NbAmmo", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(NbAmmo) , "UTF-8");
    		data += "&" + URLEncoder.encode("NbAmmoEne", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(NbAmmoEne) , "UTF-8");
    		data += "&" + URLEncoder.encode("NbMissiles", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(NbMissiles) , "UTF-8");
    		data += "&" + URLEncoder.encode("NbMissilesEne", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(NbMissilesEne) , "UTF-8");
    		data += "&" + URLEncoder.encode("NbTorpedoes", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(NbTorpedoes) , "UTF-8");
    		data += "&" + URLEncoder.encode("NbTorpedoesEne", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(NbTorpedoesEne) , "UTF-8");
    		data += "&" + URLEncoder.encode("MIA", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(MIA) , "UTF-8");
    		data += "&" + URLEncoder.encode("MIAEne", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(MIAEne) , "UTF-8");
    		data += "&" + URLEncoder.encode("Cost", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(Cost) , "UTF-8");
    		data += "&" + URLEncoder.encode("CostEne", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(CostEne) , "UTF-8");

    		// Set Connection
    		URL Url = new URL("http://www.thegiantball.com/~GBT/php/addLevelScore.php?" + data);
    		//System.out.println(Url.toExternalForm());
    		//System.out.println("http://www.thegiantball.com/~GBT/php/addLevelScore.php?" + data);
    		URLConnection conn = Url.openConnection();

    		// Update
    		Object result = conn.getContent();
    		
    		System.out.println(result);
    	}
    	catch (Exception e) 
    	{
    		e.printStackTrace();
    	}
    }
    
    public static void appendConquestScore(String name, String dateOfScore,int Score,int NbAmmo,int NbAmmoEne,int NbMissiles,int NbMissilesEne,int NbTorpedoes,int NbTorpedoesEne,long MIA,long MIAEne,long Cost,long CostEne,int NbBasesLeft,int NbBasesLeftEne,int NbCarrierLeft,int NbCarrierLeftEne,int NbOtherBoatsLeft,int NbOtherBoatsLeftEne,int NbSubsLeft,int NbSubsLeftEne) 
    {

    	try {
    		System.out.println(dateOfScore);
    		// Construct data
    		String data = URLEncoder.encode("Name", "UTF-8") + "=" +
    		URLEncoder.encode(name, "UTF-8");
    		data += "&" + URLEncoder.encode("DateOfScore", "UTF-8") + "=" +
    		URLEncoder.encode(dateOfScore , "UTF-8");
    		data += "&" + URLEncoder.encode("Score", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(Score) , "UTF-8");
    		data += "&" + URLEncoder.encode("NbAmmo", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(NbAmmo) , "UTF-8");
    		data += "&" + URLEncoder.encode("NbAmmoEne", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(NbAmmoEne) , "UTF-8");
    		data += "&" + URLEncoder.encode("NbMissiles", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(NbMissiles) , "UTF-8");
    		data += "&" + URLEncoder.encode("NbMissilesEne", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(NbMissilesEne) , "UTF-8");
    		data += "&" + URLEncoder.encode("NbTorpedoes", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(NbTorpedoes) , "UTF-8");
    		data += "&" + URLEncoder.encode("NbTorpedoesEne", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(NbTorpedoesEne) , "UTF-8");
    		data += "&" + URLEncoder.encode("MIA", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(MIA) , "UTF-8");
    		data += "&" + URLEncoder.encode("MIAEne", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(MIAEne) , "UTF-8");
    		data += "&" + URLEncoder.encode("Cost", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(Cost) , "UTF-8");
    		data += "&" + URLEncoder.encode("CostEne", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(CostEne) , "UTF-8");
    		data += "&" + URLEncoder.encode("NbBasesLeft", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(NbBasesLeft) , "UTF-8");
    		data += "&" + URLEncoder.encode("NbBasesLeftEne", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(NbBasesLeftEne) , "UTF-8");
    		data += "&" + URLEncoder.encode("NbCarrierLeft", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(NbCarrierLeft) , "UTF-8");
    		data += "&" + URLEncoder.encode("NbCarrierLeftEne", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(NbCarrierLeftEne) , "UTF-8");
    		data += "&" + URLEncoder.encode("NbOtherBoatsLeft", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(NbOtherBoatsLeft) , "UTF-8");
    		data += "&" + URLEncoder.encode("NbOtherBoatsLeftEne", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(NbOtherBoatsLeftEne) , "UTF-8");
    		data += "&" + URLEncoder.encode("NbSubsLeft", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(NbSubsLeft) , "UTF-8");
    		data += "&" + URLEncoder.encode("NbSubsLeftEne", "UTF-8") + "=" +
    		URLEncoder.encode(String.valueOf(NbSubsLeftEne) , "UTF-8");
    		
    		// Set Connection
    		URL Url = new URL("http://www.thegiantball.com/~GBT/php/addConquestScore.php?" + data);
    		URLConnection conn = Url.openConnection();
    		//System.out.println(Url.toExternalForm());
    		//System.out.println("http://www.thegiantball.com/~GBT/php/addConquestScore.php?" + data);
    		// Update
    		Object result = conn.getContent();
    		System.out.println(result);
    	} 
    	catch (Exception e) 
    	{
    		e.printStackTrace();
    	}
    }

    public static String GetOnlineCurrentHighScore() {
    	String rData = "";

    	try {

    		// Set Connection
    		URL Url = new URL("http://www.thegiantball.com/~GBT/php/getScore.php");
    		URLConnection conn = Url.openConnection();

    		// Get the response
    		BufferedReader rd = new BufferedReader(new
    				InputStreamReader( conn.getInputStream()));
    		String line ="";
    		int i = 0;
    		while ((line = rd.readLine()) != null&&i<numberOfScores-1)
    		{
    			// Process line...
    			outText[i] = line;
    			i++;
    		}
    		rd.close();
    	} catch (Exception e) {
    		rData = "Score server is having a problem";
    	}
    	return outText[0] + rData;
    }

    public static String[] GetOnlineHighScores() {
    	try {

    		// Set Connection
    		URL Url = new URL("http://www.thegiantball.com/~GBT/php/getScore.php");
    		URLConnection conn = Url.openConnection();

    		// Get the response
    		BufferedReader rd = new BufferedReader(new
    				InputStreamReader( conn.getInputStream()));
    		String line ="";
    		int i = 0;
    		while ((line = rd.readLine()) != null&&i<numberOfScores-1)
    		{
    			// Process line...
    			outText[i] = line;
    			i++;
    		}
    		rd.close();
    	} catch (Exception e) {
    	}
    	return outText;
    }

} 