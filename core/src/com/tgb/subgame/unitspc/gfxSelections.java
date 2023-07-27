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

import com.tgb.subengine.gfxentities.*;
import com.tgb.subengine.*;
import pulpcore.image.CoreImage;

/**
 * Static facility class to create the selection gfx. Avoid the overload!
 * @author Alain Becam
 *
 */
public class gfxSelections
{
	public static CoreImage unknownSelImage;
	public static CoreImage alliesSelImage;
	public static CoreImage allies50SelImage;
	public static CoreImage allies25SelImage;
	public static CoreImage ourSelImage;
	public static CoreImage our50SelImage;
	public static CoreImage our25SelImage;
	public static CoreImage enemiesSelImage;
	public static CoreImage enemies50SelImage;
	public static CoreImage neutralSelImage; // Not used YET !!!
	public static CoreImage neutral50SelImage; // Not used YET !!!
	
	public static boolean created=false;
	
	public static void callMeFirst()
	{
		if (!created)
			createGfx();
		created = true;
	}
	public static void createGfx()
	{	
		unknownSelImage = CoreImage.load("SelUnknown.png");
		alliesSelImage = CoreImage.load("SelNor.png");
		allies50SelImage = CoreImage.load("SelVis50.png");
		allies25SelImage = CoreImage.load("SelVis25.png");
		ourSelImage = CoreImage.load("SelNor.png");
		our50SelImage = CoreImage.load("SelVis50.png");
		our25SelImage = CoreImage.load("SelVis25.png");
		enemiesSelImage = CoreImage.load("SelEne.png");
		enemies50SelImage = CoreImage.load("SelEne50.png");	
	}
	
	public static SpritePC getOurSprites(int whichOne)
	{
		SpritePC spriteToSend;
		
		switch (whichOne)
		{
			case  FUnit.SEL_UNKNOWN:spriteToSend=new SpritePC(unknownSelImage);
			break;
			case  FUnit.SEL_ALLIED_50:spriteToSend=new SpritePC(allies50SelImage);
			break;
			case  FUnit.SEL_ALLIED_25:spriteToSend=new SpritePC(allies25SelImage);
			break;
			case  FUnit.SEL_ALLIED:spriteToSend=new SpritePC(alliesSelImage);
			break;
			case  FUnit.SEL_OUR_50:spriteToSend=new SpritePC(our50SelImage);
			break;
			case  FUnit.SEL_OUR_25:spriteToSend=new SpritePC(our25SelImage);
			break;
			case  FUnit.SEL_OUR:spriteToSend=new SpritePC(ourSelImage);
			break;
			case  FUnit.SEL_ENEMY_50:spriteToSend=new SpritePC(enemies50SelImage);
			break;
			case  FUnit.SEL_ENEMY:spriteToSend=new SpritePC(enemiesSelImage);
			break;
			case  FUnit.SEL_NEUTRAL_50:spriteToSend=new SpritePC(unknownSelImage);
			break;
			case  FUnit.SEL_NEUTRAL:spriteToSend=new SpritePC(unknownSelImage);
			break;
			default:
				spriteToSend=new SpritePC(unknownSelImage);
		}
		return spriteToSend;
	}
}
