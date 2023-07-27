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


/**
 * A game part correspond to a state: the main menu, the pause, the strategic map...
 * @author Alain Becam
 *
 */
public interface IGamePart
{
	/**
	 * These methods corresponds to the Java listener interface but are managed by the StateManager, not by java!
	 * @return null if we continue, or the state of the other part to start.
	 */ 
	public String mouseClicked(MouseEvent e);

	public String mouseEntered(MouseEvent e);

	public String mouseExited(MouseEvent e);

	public String mousePressed(MouseEvent e);

	public String mouseReleased(MouseEvent e);

    public String keyPressed(KeyEvent e);

    public String keyReleased(KeyEvent e);

    public String keyTyped(KeyEvent e);
    
    /**
     * Get the mouse absolute position, from java.awt.MouseInfo, for threaded games mostly.
     * The main loop must ask for it!
     * @param x
     * @param y
     */
    public String mousePosition(int x,int y);
    
    /**
     * Return an unique state name for ourself !
     * @return String the name of the state
     */
    public String getStateName();
    
    /**
     * Do a loop, and return null if we continue, or the state of the other part to start.
     * @return null if we continue to manage the game, or the state of the other part
     */
    public String doLoop();
    
    /**
     * Slart, if asked by the StateManager. 
     */
    public void start();
    
    /**
     * Sleep, if asked by the StateManager. You must realize that this method should
     * generally be called by ourself :)
     */
    public void sleep();
    
    /**
     * quit, if asked. Should be called at the end only or if the game start to be rather
     * big. This method is useful mostly if you must close or quit something, like a file
     * or a thread. Without such things, it shouldn't be needed.
     */
    public void quit();
    
}
