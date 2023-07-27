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
 * Sub which is able to go everywhere (not limited to game boundaries)
 * Copyright (c)2008 Alain Becam
 */

package com.tgb.subgame.unitspc;

import com.tgb.subgame.LevelMap;

public class FreeSub extends Submarine
{
	
	public FreeSub()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public FreeSub(LevelMap theMap)
	{
		super(theMap);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.units.ProgrammableUnit#doUpdate()
	 */
	@Override
	public void doUpdate(double time)
	{	
		if (!dead)
		{
			updateAttachedObjects();

			if ((this.depth - this.wantedDepth) > 2)
			{
				depth-=(0.04+0.04*Math.abs(this.currentSpeed));
				this.setDepth(depth);
			}
			if ((this.depth - this.wantedDepth) < -2)
			{
				depth+=(0.04+0.04*Math.abs(this.currentSpeed));
				this.setDepth(depth);
			}
			
			this.posX = this.posX + time*this.currentSpeed*Math.cos(this.orientation);
			this.posY = this.posY + time*this.currentSpeed*Math.sin(this.orientation);
			
			depthWater = ourLevelKeeper.getAlpha((int )this.posX, (int )this.posY)-255;
			
			if ((this.getDepth() < depthWater) && (depthWater != 255))
				this.setDepth(depthWater);
			//System.out.println("Depth "+depthWater);
			
			if (( this.currentSpeed > 3) && (depthWater > -10))
			{
				// Not good :)
				//this.turn(0.2);
				this.wantedSpeed =0;
			}
			else if (( this.currentSpeed < -3) && (depthWater > -10))
			{
				// Not good :)
				//this.turn(0.2);
				this.wantedSpeed =0;
			}
			
			if (!unmanned)
			{
				// If no waypoints, we turn!
				if (this.programmedWPs.isEmpty() || (!followWP))
				{
					if (this.currentSpeed < this.wantedSpeed)
						this.currentSpeed += 2;
					else if (this.currentSpeed > this.wantedSpeed)
							this.currentSpeed -= 2;
					//this.orientation += 0.08*time;
				}
				else
				{
					// Got to the next WP
					seekWP();
				}
			}
			else
			{
				if (this.currentSpeed < this.wantedSpeed)
					this.currentSpeed += 2;
				else if (this.currentSpeed > this.wantedSpeed)
						this.currentSpeed -= 2;
			}
			this.updateMe(posX, posY, 0, this.orientation, this.currentSpeed);

			for (int iSensors=0;iSensors < sensors.size() ; iSensors++)
			{
				sensors.get(iSensors).doUpdate(time);
				
				foundUnits = sensors.get(iSensors).getFoundUnits();
				
				attackIfNeeded(time);
			}
			foundUnits = this.theMap.getOurKD().getFoundUnits();
			
			attackIfNeeded(time);
			
		}
		else if (iDead > depthWater)
		{
			// Die !!!
			this.setDepth(iDead--);
			SndContainer.playExplosion4Sound();
		}
	}
}
