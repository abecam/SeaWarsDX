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

import pulpcore.image.CoreImage;

/**
 * Static facility class to create the building gfx. Avoid the overload!
 * @author Alain Becam
 *
 */
public class GfxBuildings
{
	public static CoreImage imageArbre;
	public static CoreImage imageArbre2;
	public static CoreImage imageArbre3;
	public static CoreImage imageArbre4;
	public static CoreImage imageArbre5;
	public static CoreImage imageArbre6;
	public static CoreImage imageArbre7;
	public static CoreImage imageRoseVent;
	public static CoreImage imageEglise;
	public static CoreImage imageTourPen;
	public static CoreImage imageCastle;
	public static CoreImage imagePacman;
	public static CoreImage imageGhost;
	public static CoreImage imageDrac;
	public static CoreImage imageHouse;
	public static CoreImage imageBc;
	public static CoreImage imageAzaTree;
	public static CoreImage imageAuraB;
	public static CoreImage imageAuraB2;
	public static CoreImage imageToitOrth;
//	public static CoreImage imageSBoat;
//	public static CoreImage imageDarkSmoke;
//	public static CoreImage selImage;
//	public static CoreImage imageAirplane;
//	public static CoreImage imageAwaks;
//	public static CoreImage imageAirplaneDark;
//	public static CoreImage imageAwaksDark;
//	public static CoreImage imageAirplaneLight;
//	public static CoreImage imageAwaksLight;
//	public static CoreImage imageBase1;
//	public static CoreImage imageBase1Dest;
//	public static CoreImage imageBase2;
//	public static CoreImage imageBase2Dest;
	
	// SM gfxs
//	public static CoreImage imageBaseSmallOur;
//	public static CoreImage imageBaseSmallEne;
//	public static CoreImage imageBaseSmallAllies;
//	public static CoreImage imageBaseSmallDest;
//	
//	public static CoreImage imageCarrierSmallOur;
//	public static CoreImage imageCarrierSmallEne;
//	public static CoreImage imageCarrierSmallAllies;
//	
//	public static CoreImage imageExcl;
//	public static CoreImage imageInt;
//	public static CoreImage imageConfront;
//	
//	public static CoreImage imageWP;
	// Does not appear if dead
	
	public static boolean created=false;
	
	public static void callMeFirst()
	{
		if (!created)
			createGfx();
		created = true;
	}
	
	public static void removeMe()
	{
		imageArbre = null;
		imageArbre2 =  null;
		imageArbre3 = null;
		imageArbre4 = null;
		imageArbre5 = null;
		imageArbre6 = null;
		imageArbre7 = null;
		imageRoseVent = null;
		imageEglise = null;
		imageTourPen = null;
		imageCastle = null;
		imagePacman = null;
		imageGhost = null;
		imageDrac = null;
		imageHouse = null;
		imageBc = null;
		imageAzaTree = null;
		imageAuraB = null;
		imageAuraB2 = null;
		imageToitOrth = null;
//		imageSBoat = null;
//		imageDarkSmoke = null;
//		selImage = null;
//		imageAirplane = null;
//		imageAwaks = null;
//		imageAirplaneDark = null;
//		imageAwaksDark = null;
//		imageAirplaneLight = null;
//		imageAwaksLight = null;
//		imageBase1 = null;
//		imageBase1Dest = null;
//		imageBase2 = null;
//		imageBase2Dest = null;

//		imageBaseSmallOur = null;
//		imageBaseSmallEne = null;
//		imageBaseSmallAllies = null;
//		imageBaseSmallDest = null;
//		
//		imageCarrierSmallOur = null;
//		imageCarrierSmallEne = null;
//		imageCarrierSmallAllies = null;
//		
//		imageExcl = null;
//		imageInt = null;
//		imageConfront = null;
//		
//		imageWP = null;
	}
	
	public static void createGfx()
	{	
		imageArbre = CoreImage.load("Arbre.png");
		imageArbre2 = CoreImage.load("Arbre2.png");
		imageArbre3 = CoreImage.load("Arbre3.png");
		imageArbre4 = CoreImage.load("Arbre4.png");
		imageArbre5 = CoreImage.load("Arbre5.png");
		imageArbre6 = CoreImage.load("Arbre6.png");
		imageEglise = CoreImage.load("EgliseBois.png");
		imageArbre7 = CoreImage.load("Arbre7.png");
		imageTourPen = CoreImage.load("TourPene.png");
		imageRoseVent = CoreImage.load("RoseVent22.png");
		imageCastle = CoreImage.load("chateau.png");
		imagePacman = CoreImage.load("Pacman.png");
		imageGhost = CoreImage.load("Ghost.png");
		imageDrac = CoreImage.load("Drac.png");
		imageHouse = CoreImage.load("maison.png");
		imageBc = CoreImage.load("Bc.png");
		imageAuraB = CoreImage.load("AuraB.png");
		imageAzaTree = CoreImage.load("AzaTree.png");
		imageAuraB2 = CoreImage.load("AuraBall2.png");
		imageToitOrth = CoreImage.load("ToitOrth.png");
//		imageSBoat = CoreImage.load("SmallBoat.png");
//		imageDarkSmoke = CoreImage.load("SmokeDarkOpa.png");
//		selImage = CoreImage.load("SelNor.png");
//		imageAirplane = CoreImage.load("Awac.png");
//		imageAwaks = CoreImage.load("AwacR.png");
//		imageAirplaneDark = CoreImage.load("AwacDark.png");
//		imageAwaksDark = CoreImage.load("AwacRDark.png");
//		imageAirplaneLight = CoreImage.load("AwacLight.png");
//		imageAwaksLight = CoreImage.load("AwacRLight.png");
//		imageBase1 = CoreImage.load("Base1.png");
//		imageBase1Dest = CoreImage.load("Base1Dest.png");
//		imageBase2 = CoreImage.load("Base2.png");
//		imageBase2Dest = CoreImage.load("Base2Dest.png");
//
//		imageBaseSmallOur = CoreImage.load("BaseSmall.png");
//		imageBaseSmallEne = CoreImage.load("BaseSmallEne.png");
//		imageBaseSmallAllies = CoreImage.load("BaseSmallAllies.png");
//		imageBaseSmallDest = CoreImage.load("BaseSmallDest.png");
//		
//		imageCarrierSmallOur = CoreImage.load("CarrierSmallOur.png");
//		imageCarrierSmallEne = CoreImage.load("CarrierSmallEne.png");
//		imageCarrierSmallAllies = CoreImage.load("CarrierSmallAllies.png");
//		
//		imageExcl = CoreImage.load("Excl16.png");
//		imageInt = CoreImage.load("Int16.png");
//		imageConfront = CoreImage.load("Confro.png");
//		
//		imageWP = CoreImage.load("WP.png");
	}

	public static CoreImage getImageArbre()
	{
		return imageArbre;
	}

	public static CoreImage getImageArbre2()
	{
		return imageArbre2;
	}

	public static CoreImage getImageArbre3()
	{
		return imageArbre3;
	}

	public static CoreImage getImageArbre4()
	{
		return imageArbre4;
	}

	public static CoreImage getImageArbre5()
	{
		return imageArbre5;
	}

	public static CoreImage getImageArbre6()
	{
		return imageArbre6;
	}

	public static CoreImage getImageArbre7()
	{
		return imageArbre7;
	}

	public static CoreImage getImageRoseVent()
	{
		return imageRoseVent;
	}

	public static CoreImage getImageEglise()
	{
		return imageEglise;
	}

	public static CoreImage getImageTourPen()
	{
		return imageTourPen;
	}

	public static CoreImage getImageCastle()
	{
		return imageCastle;
	}

	public static CoreImage getImagePacman()
	{
		return imagePacman;
	}

	public static CoreImage getImageGhost()
	{
		return imageGhost;
	}

	public static CoreImage getImageDrac()
	{
		return imageDrac;
	}

	public static CoreImage getImageHouse()
	{
		return imageHouse;
	}

	public static CoreImage getImageBc()
	{
		return imageBc;
	}

	public static CoreImage getImageAzaTree()
	{
		return imageAzaTree;
	}

	public static CoreImage getImageAuraB()
	{
		return imageAuraB;
	}

	public static CoreImage getImageAuraB2()
	{
		return imageAuraB2;
	}

	public static CoreImage getImageToitOrth()
	{
		return imageToitOrth;
	}

	public static boolean isCreated()
	{
		return created;
	}
}
