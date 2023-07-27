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

package com.tgb.subengine.gameentities;

/**
 * Game Entity
 * @author Alain Becam
 *
 */
public interface IEntity
{
	/**
	 * Create the entity, i.e. create it for the game world.
	 * @param name
	 * @return boolean true if it worked
	 */
	public boolean createEntity(String name);
	
	/**
	 * Awake an entity, typically when it comes back from a pause or when it comes
	 * in a tactical map (from the global map)
	 * @return boolean true if it worked
	 */
	public boolean awakeEntity();
	
	/**
	 * Pause the entity.
	 * @return boolean true if it worked
	 */
	public boolean pauseEntity();
	
	/**
	 * Destroy the entity, when the unit is
	 * destoyed in the game.
	 * @return boolean true if it worked
	 */
	public boolean destroyEntity();
}
