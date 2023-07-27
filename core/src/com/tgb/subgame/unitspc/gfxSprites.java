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

import pulpcore.image.CoreImage;

/**
 * Static facility class to create the sprite gfx. Avoid the overload and some
 * serious slowdown during load! Might not be the smartest way though.
 * @author Alain Becam
 *
 */
public class gfxSprites
{
	public static CoreImage imageBoat;
	public static CoreImage imageBoatCarrierEne;
	public static CoreImage imageBoatCruiser;
	public static CoreImage imageBoatCruiserEne;
	public static CoreImage imageBoatBattleship;
	public static CoreImage imageBoatDestroyer;
	public static CoreImage imageBoatFrigate;
	public static CoreImage imageBoatCorvette;
	public static CoreImage imageBoatDestroyerEne;
	public static CoreImage imageBoatFrigateEne;
	public static CoreImage imageBoatCorvetteEne;
	public static CoreImage imageBoatMed;
	public static CoreImage imageBoatSmall;
	public static CoreImage imageBoatSmallEne;
	public static CoreImage imageBoatSmallAllies;
	public static CoreImage imageSmoke;
	public static CoreImage imageSub3D;
	public static CoreImage imageSub3DEne;
	public static CoreImage imageSub;
	public static CoreImage imageFlash;
	public static CoreImage imageFlash2;
	public static CoreImage imageSBoat;
	public static CoreImage imageDarkSmoke;
	public static CoreImage selImage;
	public static CoreImage imageAirplane;
	public static CoreImage imageAwaks;
	public static CoreImage imageAirplaneDark;
	public static CoreImage imageAwaksDark;
	public static CoreImage imageAirplaneLight;
	public static CoreImage imageAwaksLight;
	public static CoreImage imageBase1;
	public static CoreImage imageBase1Dest;
	public static CoreImage imageBase2;
	public static CoreImage imageBase2Dest;
	
	public static CoreImage imageBaseSmall1;
	public static CoreImage imageBaseSmall1Dest;
	public static CoreImage imageBaseSmall2;
	public static CoreImage imageBaseSmall2Dest;
	
	public static CoreImage imageBaseMain1;
	public static CoreImage imageBaseMain1Dest;
	public static CoreImage imageBaseMain2;
	public static CoreImage imageBaseMain2Dest;
	
	// SM gfxs
	public static CoreImage imageBaseSmallOur;
	public static CoreImage imageBaseSmallEne;
	public static CoreImage imageBaseSmallAllies;
	public static CoreImage imageBaseSmallDest;
	
	public static CoreImage imageCarrierSmallOur;
	public static CoreImage imageCarrierSmallEne;
	public static CoreImage imageCarrierSmallAllies;
	
	public static CoreImage imageExcl;
	public static CoreImage imageInt;
	public static CoreImage imageConfront;
	
	public static CoreImage imageWP;
	
	public static CoreImage imageArrow;
	
	// Civilian ships
	public static CoreImage imageCargo;
	public static CoreImage imageCargoEne;
	public static CoreImage imageTanker;
	public static CoreImage imageFishingBoat;
	public static CoreImage imageFishingBoatEne;
	public static CoreImage imagePleasance;
	public static CoreImage imagePleasanceEne;
	public static CoreImage imageCruise;
	public static CoreImage imageCruiseEne;
	
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
		imageBoat = null;
		imageBoatCarrierEne =  null;
		imageBoatCruiser = null;
		imageBoatCruiserEne = null;
		imageBoatBattleship = null;
		imageBoatDestroyer = null;
		imageBoatFrigate = null;
		imageBoatCorvette = null;
		imageBoatDestroyerEne = null;
		imageBoatFrigateEne = null;
		imageBoatCorvetteEne = null;
		imageBoatMed = null;
		imageBoatSmall = null;
		imageBoatSmallEne = null;
		imageBoatSmallAllies = null;
		imageSmoke = null;
		imageSub3D = null;
		imageSub3DEne = null;
		imageSub = null;
		imageFlash = null;
		imageFlash2 = null;
		imageSBoat = null;
		imageDarkSmoke = null;
		selImage = null;
		imageAirplane = null;
		imageAwaks = null;
		imageAirplaneDark = null;
		imageAwaksDark = null;
		imageAirplaneLight = null;
		imageAwaksLight = null;
		imageBase1 = null;
		imageBase1Dest = null;
		imageBase2 = null;
		imageBase2Dest = null;
		
		imageBaseSmall1 = null;
		imageBaseSmall1Dest = null;
		imageBaseSmall2 = null;
		imageBaseSmall2Dest = null;
		
		imageBaseMain1 = null;
		imageBaseMain1Dest = null;
		imageBaseMain2 = null;
		imageBaseMain2Dest = null;

		imageBaseSmallOur = null;
		imageBaseSmallEne = null;
		imageBaseSmallAllies = null;
		imageBaseSmallDest = null;
		
		imageCarrierSmallOur = null;
		imageCarrierSmallEne = null;
		imageCarrierSmallAllies = null;
		
		imageExcl = null;
		imageInt = null;
		imageConfront = null;
		
		imageWP = null;
		
		imageArrow = null;
		
		imageCargo = null;
		imageCargoEne = null;
		imageTanker = null;
		imageFishingBoat = null;
		imageFishingBoatEne = null;
		imagePleasance = null;
		imagePleasanceEne = null;
		imageCruise = null;
		imageCruiseEne = null;
	}
	
	public static void createGfx()
	{	
		imageBoat = CoreImage.load("Carrier3.png");
		imageBoatCarrierEne = CoreImage.load("CarrierEne.png");
		imageBoatCruiser = CoreImage.load("BoatCruiser3.png");
		imageBoatCruiserEne = CoreImage.load("BoatCruiserEne.png");
		imageBoatBattleship = CoreImage.load("BoatBig.png");
		imageBoatDestroyer = CoreImage.load("BoatDestroyer.png");
		imageBoatDestroyerEne = CoreImage.load("BoatDestroyerEne.png");
		imageBoatFrigate = CoreImage.load("BoatFrigate3.png");
		imageBoatFrigateEne = CoreImage.load("BoatFrigateEne.png");
		imageBoatCorvette = CoreImage.load("BoatCorvette.png");
		imageBoatCorvetteEne = CoreImage.load("BoatCorvetteEne.png");
		imageBoatMed = CoreImage.load("BoatMed.png");
		imageBoatSmall = CoreImage.load("BoatSmall.png");
		imageBoatSmallEne = CoreImage.load("BoatSmallEne.png");
		imageBoatSmallAllies = CoreImage.load("BoatSmallAllies.png");
		imageSmoke = CoreImage.load("Smoke2.png");
		imageSub3D = CoreImage.load("Sub3D.png");
		imageSub = CoreImage.load("Sub.png");
		imageSub3DEne = CoreImage.load("Sub3DEne2.png");
		imageFlash = CoreImage.load("Flash.png");
		imageFlash2 = CoreImage.load("Flash2.png");
		imageSBoat = CoreImage.load("SmallBoat.png");
		imageDarkSmoke = CoreImage.load("SmokeDarkOpa.png");
		selImage = CoreImage.load("SelNor.png");
		imageAirplane = CoreImage.load("Awac.png");
		imageAwaks = CoreImage.load("AwacR.png");
		imageAirplaneDark = CoreImage.load("AwacDark.png");
		imageAwaksDark = CoreImage.load("AwacRDark.png");
		imageAirplaneLight = CoreImage.load("AwacLight.png");
		imageAwaksLight = CoreImage.load("AwacRLight.png");
		imageBase1 = CoreImage.load("Base1.png");
		imageBase1Dest = CoreImage.load("Base1Dest.png");
		imageBase2 = CoreImage.load("Base2.png");
		imageBase2Dest = CoreImage.load("Base2Dest.png");

		imageBaseSmall1 = CoreImage.load("SmallBase1.png");
		imageBaseSmall1Dest = CoreImage.load("SmallBase1Destroyed.png");
		imageBaseSmall2 = CoreImage.load("SmallBase2.png");
		imageBaseSmall2Dest = CoreImage.load("SmallBase2Destroyed.png");
		
		imageBaseMain1 = CoreImage.load("MainBase1.png");
		imageBaseMain1Dest = CoreImage.load("MainBase1Destroyed.png");
		imageBaseMain2 = CoreImage.load("MainBase1.png");
		imageBaseMain2Dest = CoreImage.load("MainBase1Destroyed.png");
		
		imageBaseSmallOur = CoreImage.load("BaseSmall.png");
		imageBaseSmallEne = CoreImage.load("BaseSmallEne.png");
		imageBaseSmallAllies = CoreImage.load("BaseSmallAllies.png");
		imageBaseSmallDest = CoreImage.load("BaseSmallDest.png");
		
		imageCarrierSmallOur = CoreImage.load("CarrierSmallOur.png");
		imageCarrierSmallEne = CoreImage.load("CarrierSmallEne.png");
		imageCarrierSmallAllies = CoreImage.load("CarrierSmallAllies.png");
		
		imageExcl = CoreImage.load("Excl16.png");
		imageInt = CoreImage.load("Int16.png");
		imageConfront = CoreImage.load("Confro.png");
		
		imageWP = CoreImage.load("WP.png");
		
		imageArrow = CoreImage.load("Fleche.png");
		
		imageCargo = CoreImage.load("BigContainer.png");
		imageCargoEne = CoreImage.load("BigContainer2.png");
		imageTanker = CoreImage.load("GazBig.png");
		imageFishingBoat = CoreImage.load("CivilBoatSmall.png");
		imageFishingBoatEne = CoreImage.load("CivilBoatSmall3.png");
		imagePleasance = CoreImage.load("Aerogli.png");
		imagePleasanceEne = CoreImage.load("voilier.png");
		imageCruise = CoreImage.load("CivilBoatBig.png");
		imageCruiseEne = CoreImage.load("CivilBoatBig2.png");
	}
	
	public static CoreImage getImageBoat() {
		return imageBoat;
	}
	public static CoreImage getImageBoatNorm()
	{
		return imageBoatCruiser;
	}
	public static CoreImage getImageBoatBattleship()
	{
		return imageBoatBattleship;
	}
	public static CoreImage getImageBoatDestroyer()
	{
		return imageBoatDestroyer;
	}
	public static CoreImage getImageBoatFrigate()
	{
		return imageBoatFrigate;
	}
	public static CoreImage getImageBoatCorvette()
	{
		return imageBoatCorvette;
	}
	public static CoreImage getImageBoatMed()
	{
		return imageBoatMed;
	}
	public static CoreImage getImageBoatSmall()
	{
		return imageBoatSmall;
	}
	public static CoreImage getImageBoatSmallEne()
	{
		return imageBoatSmallEne;
	}
	public static CoreImage getImageBoatSmallAllies()
	{
		return imageBoatSmallAllies;
	}
	public static boolean isCreated()
	{
		return created;
	}
	public static CoreImage getImageSmoke() {
		return imageSmoke;
	}
	public static CoreImage getImageSub3D() {
		return imageSub3D;
	}
	public static CoreImage getImageSub() {
		return imageSub;
	}
	public static CoreImage getImageFlash() {
		return imageFlash;
	}
	public static CoreImage getImageFlash2() {
		return imageFlash2;
	}
	public static CoreImage getImageSBoat() {
		return imageSBoat;
	}
	public static CoreImage getImageDarkSmoke() {
		return imageDarkSmoke;
	}
	public static CoreImage getSelImage() {
		return selImage;
	}
	public static CoreImage getImageAirplane() {
		return imageAirplane;
	}
	public static CoreImage getImageAwaks() {
		return imageAwaks;
	}
	public static CoreImage getImageAirplaneDark() {
		return imageAirplaneDark;
	}
	public static CoreImage getImageAwaksDark() {
		return imageAwaksDark;
	}
	public static CoreImage getImageAirplaneLight() {
		return imageAirplaneLight;
	}
	public static CoreImage getImageAwaksLight() {
		return imageAwaksLight;
	}
	public static CoreImage getImageBase1() {
		return imageBase1;
	}
	public static CoreImage getImageBase1Dest() {
		return imageBase1Dest;
	}
	public static CoreImage getImageBase2() {
		return imageBase2;
	}
	public static CoreImage getImageBase2Dest() {
		return imageBase2Dest;
	}
	public static CoreImage getImageBaseSmall1() {
		return imageBaseSmall1;
	}
	public static CoreImage getImageBaseSmall1Dest() {
		return imageBaseSmall1Dest;
	}
	public static CoreImage getImageBaseSmall2() {
		return imageBaseSmall2;
	}
	public static CoreImage getImageBaseSmall2Dest() {
		return imageBaseSmall2Dest;
	}
	
	public static CoreImage getImageBaseMain1() {
		return imageBaseMain1;
	}
	public static CoreImage getImageBase1Main1Dest() {
		return imageBaseMain1Dest;
	}
	public static CoreImage getImageBaseMain2() {
		return imageBaseMain2;
	}
	public static CoreImage getImageBaseMain2Dest() {
		return imageBaseMain2Dest;
	}
	
	public static CoreImage getImageBaseSmallOur() {
		return imageBaseSmallOur;
	}
	public static CoreImage getImageBaseSmallEne() {
		return imageBaseSmallEne;
	}
	public static CoreImage getImageBaseSmallAllies() {
		return imageBaseSmallAllies;
	}
	public static CoreImage getImageBaseSmallDest() {
		return imageBaseSmallDest;
	}
	public static CoreImage getImageCarrierSmallOur() {
		return imageCarrierSmallOur;
	}
	public static CoreImage getImageCarrierSmallEne() {
		return imageCarrierSmallEne;
	}
	public static CoreImage getImageCarrierSmallAllies() {
		return imageCarrierSmallAllies;
	}
	
	public static CoreImage getImageBoatCarrierEne()
	{
		return imageBoatCarrierEne;
	}

	public static CoreImage getImageBoatCruiserEne()
	{
		return imageBoatCruiserEne;
	}

	public static CoreImage getImageSub3DEne()
	{
		return imageSub3DEne;
	}

	public static CoreImage getImageExcl()
	{
		return imageExcl;
	}
	public static CoreImage getImageInt()
	{
		return imageInt;
	}
	public static CoreImage getImageConfront()
	{
		return imageConfront;
	}
	
	public static CoreImage getImageBoatDestroyerEne()
	{
		return imageBoatDestroyerEne;
	}

	public static CoreImage getImageBoatFrigateEne()
	{
		return imageBoatFrigateEne;
	}

	public static CoreImage getImageBoatCorvetteEne()
	{
		return imageBoatCorvetteEne;
	}

	public static CoreImage getImageWP()
	{
		return imageWP;
	}

	public static CoreImage getImageArrow()
	{
		return imageArrow;
	}

	public static CoreImage getImageBoatCargo()
	{
		return imageCargo;
	}
	
	public static CoreImage getImageBoatCargoEne()
	{
		return imageCargoEne;
	}

	public static CoreImage getImageBoatTanker()
	{
		return imageTanker;
	}

	public static CoreImage getImageBoatFishingShip()
	{
		return imageFishingBoat;
	}
	
	public static CoreImage getImageBoatFishingShipEne()
	{
		return imageFishingBoatEne;
	}

	public static CoreImage getImageBoatPleasance()
	{
		return imagePleasance;
	}
	
	public static CoreImage getImageBoatPleasanceEne()
	{
		return imagePleasanceEne;
	}

	public static CoreImage getImageBoatCruise()
	{
		return imageCruise;
	}
	
	public static CoreImage getImageBoatCruiseEne()
	{
		return imageCruiseEne;
	}
}
