/*
 * Created on 21 oct. 2019
 *
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

package com.tgb.subgame.unitspc;

import java.util.List;

/**
 * Class that can save its fields
 * -> Return a string with its list of fields, eventually a sub-ISaveable field
 * @author Alain
 *
 */
public abstract class Saveable
{
	public abstract String giveMyId();
	
	public String saveAll()
	{
		StringBuffer toSave = new StringBuffer();
		
		toSave.append("*"+giveMyId()+"\n");
		
		toSave.append(save());
		
		toSave.append(giveMyId()+"/"+"\n");
		
		return toSave.toString();
	}
	
	public abstract String save();
	
	/**
	 * The parent loader will use the Id to extract the good part.
	 * Each part return the remaining of the file.
	 * @param myPart
	 */
	public abstract boolean  loadFromOneLine(String oneLine);
	
	public List<String> load(List<String> myPart)
	{
		if (!myPart.isEmpty())
		{
			System.out.println(giveMyId()+": Loading from  "+myPart.get(0));
		}
		
		List<String> whatIsLeft = myPart;
		
		int posInList = 0;
		
		boolean endReached = true;
		
		for (String oneLine: myPart)
		{
			if (oneLine.startsWith("#"))
			{
				// Do nothing, a comment
			}
			else
			{
				if (oneLine.contains(giveMyId()+"/"))
				{
					whatIsLeft = myPart.subList(posInList+1, myPart.size());
					
					System.out.println(giveMyId()+": First line of rest will be "+whatIsLeft.get(0));
					
					endReached = false;
					
					break;
				}
				if ( !loadFromOneLine(oneLine) )
				{
					System.out.println("Warning, the line "+oneLine+" was not recognised.");
				}
			}
			
			posInList++;
		}
		
		if (endReached)
		{
			System.out.println("Warning, we reached the end of the file without a proper termination.");
		}
		return whatIsLeft;
	}
}
