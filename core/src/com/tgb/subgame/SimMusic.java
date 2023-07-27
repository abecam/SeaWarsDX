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

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * Singleton class to play the music. Use a slightly enhanced version of JavaZoom
 * mp3 player (added simply a threaded player).<br>
 * Event based, use a callback to play the next file. If a problem occurs, we play
 * the next file. The pause is not available in JavaZoom, so stopping and playing
 * back play from the start.
 *  
 * @author Alain Becam
 *
 */
public class SimMusic implements Music.OnCompletionListener
{	
	String files[]={
			"Atlantean_Twilight.mp3",
			"Dark_Star.mp3",
			"Majestic_Hills.mp3",
			"Supernatural.mp3",
			"Achilles.mp3",
			"Exciting_Trailer.mp3",
			"Ghost_Processional.mp3",			
			"Chase.mp3",
			"Dangerous.mp3",			
			"Faceoff.mp3",
			"Feral_Chase.mp3",
			"Crisis.mp3",
			"Road_to_Hell.mp3",
			"A_Mission.mp3",		
			"At_Launch.mp3",
			"Impending_Boom.mp3",
			"Interloper.mp3",
			"Love_Song.mp3",
			"Noble_Race.mp3",
			"Serpentine_Trek.mp3",
			"Tectonic.mp3",
			"The_Reveal.mp3",
			"We_Got_Trouble.mp3",
			"Weight_of_Responsibility.mp3"};
	
	static String lastFile;
	static String currentMusic;
	static boolean isStopped=false;
	static boolean isPlaying=false;
	static boolean mute=false;
	
	static private Music music;
	
	static private SimMusic instance = null;
	
	static final int introMusic=0;
	static final int extroGoodMusic=1;
	static final int extroBadMusic=2;
	
	static boolean specialPlay=false; // If a special song is requested...
	static int specialSong=introMusic;
	
	static int currentSong=3;
	
	private SimMusic()
	{
		;
	}
	
	/**
	 * Be careful, it is not currently synchronised (as we know that in our current project it is called a first time very early).
	 * @return
	 */
	public static SimMusic getInstance()
	{
		if (instance== null)
			instance = new SimMusic();
		
		return instance;
	}
	
	/**
	 */
	protected InputStream getURLInputStream(String name)
		throws Exception
	{

		URL url = new URL(name);
		InputStream fin = url.openStream();
		BufferedInputStream bin = new BufferedInputStream(fin);
		return bin;
	}
	
	public void playFile()
	{
		//java.util.Random myRandom= new java.util.Random();
		
		int nextFileIndex=0;
		
		if (!specialPlay)
		{
			nextFileIndex=currentSong++; // myRandom.nextInt(files.length);
		}
		
		if (nextFileIndex >= files.length)
		{
			nextFileIndex = 3;
			currentSong=3;
		}
		
		isStopped=false;
		isPlaying = false;
		if (specialPlay)
		{
			lastFile="Music\\"+files[specialSong];
			specialPlay=false;
		}
		else
		{
			lastFile="Music\\"+files[nextFileIndex];
		}
		
		//System.out.println("Play file from "+lastFile+", init done ");
		
		try
		{
			music = Gdx.audio.newMusic(Gdx.files.internal(lastFile));
			
			music.setLooping(true);
	        music.setVolume(0.5f);
	        music.play();
	        
	        isPlaying = true;
			System.out.println("Playing");
			
//			if (mute)
//				player.stop();
			currentMusic=lastFile.substring(lastFile.lastIndexOf('/')+1).replace('_', ' ');
			System.out.println("Playing "+currentMusic);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	
	public void playLastFile()
	{
		isStopped=false;
		
		try
		{
			System.out.println("Re-Play file from "+lastFile);
			music = Gdx.audio.newMusic(Gdx.files.internal(lastFile));
			currentMusic=lastFile.substring(lastFile.lastIndexOf('/')+1).replace('_', ' ');
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void playSplashMusic()
	{
		specialPlay = true;
		specialSong = SimMusic.introMusic;
		
		if (music != null)
			music.stop();
		else
		{
			playFile();
		}
	}
	
	public void playGoodEndMusic()
	{
		specialPlay = true;
		specialSong = SimMusic.extroGoodMusic;
		
		if (music != null)
			music.stop();
		else
		{
			playFile();
		}
	}
	
	public void playBadEndMusic()
	{
		specialPlay = true;
		specialSong = SimMusic.extroBadMusic;
		
		if (music != null)
			music.stop();
		else
		{
			playFile();
		}
	}
	
	public void stop()
	{
		isStopped=true;
		
		if (music != null)
			music.stop();
	}
	
	public void next()
	{
		if (!isStopped)
		{
			if (music != null)
				music.stop();
		}
		else
		{
			if (music != null)
			{
				if (!music.isPlaying())
					playFile();
				else
				{
					System.err.println("Previous thread still running");
				}
			}
			else
			{
				playFile();
			}
		}
	}
	
	public void pause()
	{
		if (music != null)
			music.pause();
	}
	
	public void mute()
	{
		//javazoom.jl.player.advanced.jlap.mute();
		if (mute)
			mute=false;
		else
			mute=true;
		
		music.setVolume(mute?0.0f:0.5f);
	}
	
	public void setVolume(float volume)
	{
		;
	}

	public String getLastFile()
	{
		return lastFile;
	}

	public void setLastFile(String lastFile)
	{
		this.lastFile = lastFile;
	}

	public String getCurrentMusic()
	{
		return currentMusic;
	}

	public void setCurrentMusic(String currentMusic)
	{
		this.currentMusic = currentMusic;
	}

	public boolean isStopped()
	{
		return isStopped;
	}

	public void setStopped(boolean isStopped)
	{
		this.isStopped = isStopped;
	}

	public boolean isMute()
	{
		return mute;
	}

	public void setMute(boolean mute)
	{
		this.mute = mute;
	}

	@Override
	public void onCompletion(Music music) {
		// Next file so...
				if (!isStopped)
				{
					//playFile();
					if (!music.isPlaying())
					{
						if (!music.isPlaying())
							playFile();
						else
						{
							System.err.println("Previous stream still running");
						}
					}
					else
					{
						System.err.println("Playback finished without any existing stream ???");
						playFile();
					}
				}
	}
}
