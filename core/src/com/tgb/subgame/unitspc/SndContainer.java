/*
 * Created on 8 nov 2008
 */
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
 * The sound container keep and play the game's sounds.
 * Copyright (c)2008 Alain Becam
 */

package com.tgb.subgame.unitspc;
import pulpcore.sound.Sound;
import pulpcore.sound.Playback;

/*
 * TODO: Make it less stupid :)
 */
public class SndContainer
{
	public static Sound explosionSound;
	public static Sound explosion2Sound;
	public static Sound explosion3Sound;
	public static Sound explosion4Sound;
	public static Sound explosion5Sound;
	public static Sound explosion6Sound;
	public static Sound explosion7Sound;
	public static Sound fireMissSound;
	public static Sound fireTorpSound;
	public static Sound fireMiss2Sound;

	public static Playback explosionPlayBack = null;
	public static Playback explosion2PlayBack = null;
	public static Playback explosion3PlayBack = null;
	public static Playback explosion4PlayBack = null;
	public static Playback explosion5PlayBack = null;
	public static Playback explosion6PlayBack = null;
	public static Playback explosion7PlayBack = null;
	public static Playback fireMissPlayBack = null;
	public static Playback fireTorpPlayBack = null;
	public static Playback fireMiss2PlayBack = null;
	
	public static boolean created=false;
	
	public static boolean speakerOn=true;
	
	public static void callMeFirst()
	{
		if (!created)
			loadSounds();
		
		created=true;
	}
	
	public static void loadSounds()
	{	
		explosionSound = Sound.load("exploVBig.wav");
		explosion2Sound = Sound.load("exploZoli.wav");
		explosion3Sound = Sound.load("exploAir5.wav");
		explosion4Sound = Sound.load("exploSub1.wav");
		explosion5Sound = Sound.load("exploLoud.wav");
		explosion6Sound = Sound.load("exploSub2.wav");
		explosion7Sound = Sound.load("exploBig.wav");
		fireMissSound = Sound.load("SubLaunchP.wav");
		fireTorpSound = Sound.load("SubLaunch3p.wav");//FireTorpe4.wav");
		fireMiss2Sound = Sound.load("FireTorpe4.wav");
	}

	public static Sound getExplosionSound()
	{
		return explosionSound;
	}
	
	public static Sound getExplosion2Sound()
	{
		return explosion2Sound;
	}
	
	public static Sound getExplosion3Sound()
	{
		return explosion3Sound;
	}
	
	public static Sound getExplosion4Sound()
	{
		return explosion4Sound;
	}

	public static Sound getExplosion5Sound()
	{
		return explosion5Sound;
	}

	public static Sound getExplosion6Sound()
	{
		return explosion6Sound;
	}

	public static Sound getExplosion7Sound()
	{
		return explosion7Sound;
	}

	public static Sound getFireMissSound()
	{
		return fireMissSound;
	}

	public static Sound getFireTorpSound()
	{
		return fireTorpSound;
	}
	
	public static Sound getFireMiss2Sound()
	{
		return fireMiss2Sound;
	}

	public static Playback playExplosionSound()
	{
		if (speakerOn)
		{
			if (explosionPlayBack == null)
				explosionPlayBack = explosionSound.play();
			else if (explosionPlayBack.isFinished())
				explosionPlayBack = explosionSound.play();
		}
		return explosionPlayBack;
	}

	public static Playback playExplosion2Sound()
	{
		if (speakerOn)
		{
			if (explosion2PlayBack == null)
				explosion2PlayBack = explosion2Sound.play();
			else if (explosion2PlayBack.isFinished())
				explosion2PlayBack = explosion2Sound.play();
		}
		return explosion2PlayBack;
	}

	public static Playback playExplosion3Sound()
	{
		if (speakerOn)
		{
			if (explosion3PlayBack == null)
				explosion3PlayBack = explosion3Sound.play(new pulpcore.animation.Fixed(0.2));
			else if (explosion3PlayBack.isFinished())
				explosion3PlayBack = explosion3Sound.play(new pulpcore.animation.Fixed(0.2));
		}
		return explosion3PlayBack;
	}

	public static Playback playExplosion4Sound()
	{
		if (speakerOn)
		{
			if (explosion4PlayBack == null)
				explosion4PlayBack = explosion4Sound.play(new pulpcore.animation.Fixed(0.1));
			else if (explosion4PlayBack.isFinished())
				explosion4PlayBack = explosion4Sound.play(new pulpcore.animation.Fixed(0.1));
		}
		return explosion4PlayBack;
	}
	
	public static Playback playExplosion5Sound()
	{
		if (speakerOn)
		{
			if (explosion5PlayBack == null)
				explosion5PlayBack = explosion5Sound.play();
			else if (explosion5PlayBack.isFinished())
				explosion5PlayBack = explosion5Sound.play();
		}
		return explosion5PlayBack;
	}
	
	public static Playback playExplosion6Sound()
	{
		if (speakerOn)
		{
			if (explosion6PlayBack == null)
				explosion6PlayBack = explosion6Sound.play();
			else if (explosion6PlayBack.isFinished())
				explosion6PlayBack = explosion6Sound.play();
		}
		return explosion6PlayBack;
	}
	
	public static Playback playExplosion7Sound()
	{
		if (speakerOn)
		{
			if (explosion7PlayBack == null)
				explosion7PlayBack = explosion7Sound.play();
			else if (explosion7PlayBack.isFinished())
				explosion7PlayBack = explosion7Sound.play();
		}
		return explosion7PlayBack;
	}

	public static Playback playFireMissSound()
	{
		if (speakerOn)
		{
			if (fireMissPlayBack == null)
				fireMissPlayBack = fireMissSound.play();
			else if (fireMissPlayBack.isFinished())
				fireMissPlayBack = fireMissSound.play();
		}
		return fireMissPlayBack;
	}
	
	public static Playback playFireMiss2Sound()
	{
		if (speakerOn)
		{
			if (fireMiss2PlayBack == null)
				fireMiss2PlayBack = fireMiss2Sound.play();
			else if (fireMiss2PlayBack.isFinished())
				fireMiss2PlayBack = fireMiss2Sound.play();
		}
		return fireMiss2PlayBack;
	}

	public static Playback playFireTorpSound()
	{
		if (speakerOn)
		{
			if (fireTorpPlayBack == null)
				fireTorpPlayBack = fireTorpSound.play();
			else if (fireTorpPlayBack.isFinished())
				fireTorpPlayBack = fireTorpSound.play();
		}
		return fireTorpPlayBack;
	}

	public static boolean isSpeakerOn()
	{
		return speakerOn;
	}

	public static void setSpeakerOn(boolean speakerOn)
	{
		SndContainer.speakerOn = speakerOn;
	}
}
