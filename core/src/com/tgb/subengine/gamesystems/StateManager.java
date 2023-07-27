/*
 * This software is distributed under the MIT License
 *
 * Copyright (c) 2008-2009 Alain Becam
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

package com.tgb.subengine.gamesystems;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import com.tgb.subengine.uiwrapper.Button;
import pulpcore.Stage;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.Button;
import pulpcore.sprite.Sprite;
/**
 * Manage the different game states.
 * @author Alain Becam
 * TODO Instantiate the part when needed, instead of using their instances
 */
public class StateManager
{
	private static StateManager instance = null;
	/**
	 * The list of game parts: tuples of StateName, GamePart
	 */
	private HashMap<String,IGamePart> theGameParts;
	String currentState;
	IGamePart currentPart;
	Object objectToPass; // One object to pass to the next part
	// Be very careful with the casting...
	int valueToPass; // an int to pass
	String textToPass; // a string to pass
	// Use these values only for small needs! (was created for knowing where to start the
	// first part of the tutorial: at the start if coming from the menu, at the end if
	// coming from the second tutorial
	// Something bigger, like the level's data, ask for another class (a 'keeper')
	
	Button exitButton;
	Button cancelButton;
	Scene2D ourScene;
	
	private StateManager()
	{
		theGameParts = new HashMap<String,IGamePart>();
		
		exitButton = Button.createLabeledToggleButton("Quit", 500, 340);
		exitButton.setAnchor(Sprite.CENTER);
		
		cancelButton = Button.createLabeledToggleButton("Cancel", 500, 380);
		cancelButton.setAnchor(Sprite.CENTER);
		
		ourScene=(Scene2D )Stage.getScene();
	}
	
	public synchronized static StateManager getInstance()
	{
		if (instance == null)
		{
			instance = new StateManager();
		}
		return instance;
	}
	
	public synchronized static void unload()
	{	
		if (instance!= null)
		{
			instance.removeMe();
			instance = null;
		}	
	}
	
	/**
	 * Remove the instance, so it can be garbage-collected
	 */
	public synchronized void removeMe()
	{
		theGameParts.clear();		
		currentPart.quit();
		instance = null;
	}
	
	/**
	 * Add a game part, ie. a managing portion of the game. We have to propagate events to them
	 * @param newPart
	 */
	public void addPart(IGamePart newPart)
	{
		theGameParts.put(newPart.getStateName(), newPart);
	}
	
	/**
	 * Push the current part to sleep and start a new one (set-up)
	 * @param newPart
	 */
	public void startPart(String newPart)
	{
		currentState = newPart;
		currentPart = theGameParts.get(newPart);
		
		currentPart.start();
	}
	
	/**
	 * Do a loop in the current part, if a String is returned we change the current part.
	 * @return String the name of the next part to run, or null if we keep the game !
	 */
	public String doLoop()
	{
		String result = currentPart.doLoop();
		if (Input.isDown(Input.KEY_ESCAPE))
		{
			// Must provide a choice first!
			ourScene.add(exitButton);
			ourScene.add(cancelButton);
			
			//System.exit(0);
		}
		if (exitButton.isClicked())
		{
			System.exit(0);
		}
		
		if (cancelButton.isClicked())
		{
			ourScene.remove(exitButton);
			ourScene.remove(cancelButton);	
			
			exitButton = Button.createLabeledToggleButton("Quit", 500, 340);
			exitButton.setAnchor(Sprite.CENTER);
			
			cancelButton = Button.createLabeledToggleButton("Cancel", 500, 380);
			cancelButton.setAnchor(Sprite.CENTER);
		}
		if (result != null)
		{
			currentPart.quit();
			startPart(result);
		}
		return result;
	}
	
	/**
	 * Listeners propagation. If a string is returned, we change the current part.
	 */
	public String mouseClicked(MouseEvent e)
	{
		String result = currentPart.mouseClicked(e);
		if (result != null)
		{
			startPart(result);
		}
		return result;
	}

	public String mouseEntered(MouseEvent e)
	{
		String result = currentPart.mouseEntered(e);
		if (result != null)
		{
			startPart(result);
		}
		return result;
	}

	public String mouseExited(MouseEvent e)
	{
		String result = currentPart.mouseExited(e);
		if (result != null)
		{
			startPart(result);
		}
		return result;
	}

	public String mousePressed(MouseEvent e)
	{
		String result = currentPart.mousePressed(e);
		if (result != null)
		{
			startPart(result);
		}
		return result;
	}

	public String mouseReleased(MouseEvent e)
	{
		String result = currentPart.mouseReleased(e);
		if (result != null)
		{
			startPart(result);
		}
		return result;
	}

    public String keyPressed(KeyEvent e)
    {
    	String result = currentPart.keyPressed(e);
		if (result != null)
		{
			startPart(result);
		}
		return result;
    }

    public String keyReleased(KeyEvent e)
    {
    	String result = currentPart.keyReleased(e);
		if (result != null)
		{
			startPart(result);
		}
		return result;
    }

    public String keyTyped(KeyEvent e)
    {
    	String result = currentPart.keyTyped(e);
		if (result != null)
		{
			startPart(result);
		}
		return result;
    }

	public Object getObjectToPass()
	{
		return objectToPass;
	}

	public void setObjectToPass(Object objectToPass)
	{
		this.objectToPass = objectToPass;
	}

	public int getValueToPass()
	{
		return valueToPass;
	}

	public void setValueToPass(int valueToPass)
	{
		this.valueToPass = valueToPass;
	}

	public String getTextToPass()
	{
		return textToPass;
	}

	public void setTextToPass(String textToPass)
	{
		this.textToPass = textToPass;
	}
}
