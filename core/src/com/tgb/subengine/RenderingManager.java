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

package com.tgb.subengine;

import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

import mmroutils.LogManagerVoid;

import com.tgb.subengine.gfxentities.Drawable2DEntity;
import com.tgb.subengine.particlessystem.Particles;
import com.tgb.subengine.particlessystem.ParticleManager;


/**
 * The rendering manager keep the list of drawable entities and render them as needed
 * @author Alain Becam
 *
 */
public class RenderingManager
{
	
	private static RenderingManager ourInstance = null;
	private long idCurrentEntity=0;
	private long idCurrentParticles=0;
	public boolean in3D = false;
	
	transient LogManagerVoid myLog;
	
	ArrayList<HashMap<Long,Drawable2DEntity>> entitiesToDrawLayers;
	ArrayList<HashMap<Long,Particles>> ourParticles;
	private double middlePos;
	
	private RenderingManager()
	{
		myLog = new LogManagerVoid();
		myLog.initLog("SubEngine");
		idCurrentEntity=0;
		idCurrentParticles=0;
		
		entitiesToDrawLayers = new ArrayList<HashMap<Long,Drawable2DEntity>>(40);
		ourParticles = new ArrayList<HashMap<Long,Particles>>(40);
		
		for (int iLayer=0;iLayer < 40 ; iLayer++)
		{
			HashMap<Long,Drawable2DEntity> newLayer = new HashMap<Long,Drawable2DEntity>();
			entitiesToDrawLayers.add(newLayer);
			HashMap<Long,Particles> newLayerOfParticles= new HashMap<Long,Particles>();
			ourParticles.add(newLayerOfParticles);
		}
	}
	
	public synchronized static RenderingManager getInstance()
	{
		if (ourInstance == null)
		{
			ourInstance = new RenderingManager();
		}
		return (ourInstance);
	}
	
	/**
	 * Remove the instance, so it can be garbage-collected
	 */
	public synchronized static void removeMe()
	{
		ourInstance.closeLog();
		ourInstance = null;
	}
	
	public void closeLog()
	{
		myLog.closeLog();
	}
	
	public synchronized long addDrawableEntity(Drawable2DEntity newEntity)
	{
		//System.out.println("Add an entity on layer 20 (default)");
		newEntity.setIdEntity(idCurrentEntity);
		//System.out.println("Add an entity on layer 20 - "+entitiesToDrawLayers.get(20).size());
		// By default, in the middle!
		entitiesToDrawLayers.get(20).put(idCurrentEntity,newEntity);
		
		return (idCurrentEntity++);
	}
	
	public synchronized long addDrawableEntity(Drawable2DEntity newEntity, int layer)
	{
		//System.out.println("Add an entity on layer "+layer);
		newEntity.setIdEntity(idCurrentEntity);
		//System.out.println("Add an entity on layer "+layer+" - "+entitiesToDrawLayers.get(layer).size());
		entitiesToDrawLayers.get(layer).put(idCurrentEntity,newEntity);
		
		return (idCurrentEntity++);
	}

	/**
	 * Find the layer of the entity and then change it.
	 * Shouldn't be use (slow), so please keep the layer of your entity
	 * @param idEntity the id of the entity, returned at the addition
	 * @param newLayer the new layer where to push the entity
	 */
	public synchronized void changeLayerOf(long idEntity,int newLayer)
	{
		Drawable2DEntity theEntity;
	
		// First find the entity
		for (int iLayer=0;iLayer<40;iLayer++)
		{
			if (entitiesToDrawLayers.get(iLayer).containsKey(idEntity))
			{
				theEntity=entitiesToDrawLayers.get(iLayer).get(idEntity);
				entitiesToDrawLayers.get(iLayer).remove(idEntity);
				entitiesToDrawLayers.get(newLayer).put(idEntity, theEntity);
				break;
			}
		}
	}
	/**
	 * Change the layer of the entity, knowing the original layer
	 * @param idEntity the id of the entity, returned at the addition
	 * @param layer the current layer of the entity
	 * @param newLayer the new layer where to push the entity
	 */
	public synchronized void changeLayerOf(long idEntity,int layer,int newLayer)
	{
		// If the entity exists, remove it then add it back in the right layer
		if (entitiesToDrawLayers.get(layer).containsKey(idEntity))
		{
			Drawable2DEntity theEntity=entitiesToDrawLayers.get(layer).get(idEntity);
			entitiesToDrawLayers.get(layer).remove(idEntity);
			entitiesToDrawLayers.get(newLayer).put(idEntity, theEntity);
		}
		// Otherwise do nothing
	}
	
	/**
	 * Find the layer of the entity and then remove it.
	 * Shouldn't be use (slow), so please keep the layer of your entity
	 * @param idEntity the id of the entity, return at the addition
	 */
	public synchronized void removeEntity(long idEntity)
	{
		// First find the entity
		for (int iLayer=0;iLayer<40;iLayer++)
		{
			if (entitiesToDrawLayers.get(iLayer).containsKey(idEntity))
			{
				entitiesToDrawLayers.get(iLayer).remove(idEntity);
				break;
			}
		}
	}
	
	/**
	 * Remove the entity from its layer
	 * @param idEntity the id of the entity, returned at the addition
	 * @param layer the current layer of the entity
	 */
	public synchronized void removeEntity(long idEntity,int layer)
	{
		if (entitiesToDrawLayers.get(layer).containsKey(idEntity))
		{
			entitiesToDrawLayers.get(layer).remove(idEntity);
		}
	}
	
	/**
	 * Remove all entities.
	 */
	public void washAll()
	{
		Particles currentParticles;
		Drawable2DEntity currentEntity;
		
		for (int iLayer=0;iLayer<40;iLayer++)
		{
			HashMap<Long,Drawable2DEntity> currentLayer=entitiesToDrawLayers.get(iLayer);
			if (!currentLayer.isEmpty())
			{
				Collection<Drawable2DEntity> entitiesInLayer=currentLayer.values();
				Iterator<Drawable2DEntity> iEntities = entitiesInLayer.iterator();
				
				while (iEntities.hasNext())
				{
					currentEntity=(Drawable2DEntity )(iEntities.next());
					currentEntity.removeMe();
				}
			}
			entitiesToDrawLayers.get(iLayer).clear();
		}
		entitiesToDrawLayers.clear();
		for (int iLayer=0;iLayer<40;iLayer++)
		{
			HashMap<Long,Particles> currentLayerParticles=ourParticles.get(iLayer);
			if (!currentLayerParticles.isEmpty())
			{
				Collection<Particles> particlesInLayer=currentLayerParticles.values();
				
				Iterator<Particles> iParticles = particlesInLayer.iterator();
				
				while (iParticles.hasNext())
				{
					//System.out.println("Paint one set of particles");
					currentParticles=iParticles.next();
					currentParticles.removeMe();
				}
			}
			ourParticles.get(iLayer).clear();
		}
		ParticleManager.getInstance().reset();
		ourParticles.clear();
	}
	
	/**
	 * Remove all entities.
	 */
	public void washAllAndPrepare()
	{
		Particles currentParticles;
		Drawable2DEntity currentEntity;
		
		for (int iLayer=0;iLayer<40;iLayer++)
		{
			HashMap<Long,Drawable2DEntity> currentLayer=entitiesToDrawLayers.get(iLayer);
			if (!currentLayer.isEmpty())
			{
				Collection<Drawable2DEntity> entitiesInLayer=currentLayer.values();
				Iterator<Drawable2DEntity> iEntities = entitiesInLayer.iterator();
				
				while (iEntities.hasNext())
				{
					currentEntity=(Drawable2DEntity )(iEntities.next());
					currentEntity.removeMe();
				}
			}
			entitiesToDrawLayers.get(iLayer).clear();
		}
		entitiesToDrawLayers.clear();
		for (int iLayer=0;iLayer<40;iLayer++)
		{
			HashMap<Long,Particles> currentLayerParticles=ourParticles.get(iLayer);
			if (!currentLayerParticles.isEmpty())
			{
				Collection<Particles> particlesInLayer=currentLayerParticles.values();
				
				Iterator<Particles> iParticles = particlesInLayer.iterator();
				
				while (iParticles.hasNext())
				{
					//System.out.println("Paint one set of particles");
					currentParticles=(iParticles.next());
					currentParticles.removeMe();
				}
			}
			ourParticles.get(iLayer).clear();
		}
		ParticleManager.getInstance().reset();
		ourParticles.clear();
		for (int iLayer=0;iLayer < 40 ; iLayer++)
		{
			HashMap<Long,Drawable2DEntity> newLayer = new HashMap<Long,Drawable2DEntity>();
			entitiesToDrawLayers.add(newLayer);
			HashMap<Long,Particles> newLayerOfParticles= new HashMap<Long,Particles>();
			ourParticles.add(newLayerOfParticles);
		}
	}
	
	/*
	 * Particles
	 */
	public synchronized long addParticles(Particles oneSetOfParticles)
	{
		//System.out.println("Add a set of particles on layer 20 (default)");
		oneSetOfParticles.setIdParticles(idCurrentParticles);
		// Add in the middle
		ourParticles.get(20).put(idCurrentParticles,oneSetOfParticles);
		
		return (idCurrentParticles++);
	}
	
	public synchronized long addParticles(Particles oneSetOfParticles, int layer)
	{
		//System.out.println("Add a set of particles on layer "+layer);
		oneSetOfParticles.setIdParticles(idCurrentParticles);
		// Add in the given layer
		ourParticles.get(layer).put(idCurrentParticles,oneSetOfParticles);
		
		return (idCurrentParticles++);
	}
	
	
	/**
	 * Find the layer of the entity and then remove it.
	 * Shouldn't be use (slow), so please keep the layer of your entity
	 * @param idParticle the id of the entity, return at the addition
	 */
	public synchronized void removeParticles(long idParticle)
	{
		// First find the entity
		for (int iLayer=0;iLayer<40;iLayer++)
		{
			if (ourParticles.get(iLayer).containsKey(idParticle))
			{
				ourParticles.get(iLayer).get(idParticle).removeMe();
				ourParticles.get(iLayer).remove(idParticle);
				break;
			}
		}
	}
	
	/**
	 * Remove the entity from its layer
	 * @param idParticle the id of the entity, returned at the addition
	 * @param layer the current layer of the entity
	 */
	public synchronized void removeParticles(long idParticle,int layer)
	{
		if (ourParticles.get(layer).containsKey(idParticle))
		{
			ourParticles.get(layer).get(idParticle).removeMe();
			ourParticles.get(layer).remove(idParticle);
		}
	}
	
	
	/**
	 * Paint the different elements, in the right order of layers, using the given Graphics
	 *
	 */
	public void paint()
	{		
		Drawable2DEntity currentEntity;
		Particles currentParticles;
		
		if (!in3D)
		{
			// In order, paint all the entities!
			for (int iLayer=0;iLayer<40;iLayer++)
			{
				HashMap<Long,Drawable2DEntity> currentLayer=entitiesToDrawLayers.get(iLayer);
				if (!currentLayer.isEmpty())
				{
					//myLog.add2Log(currentLayer.size() + " entities to draw in layer "+iLayer);
					Collection entitiesInLayer=currentLayer.values();
					Iterator iEntities = entitiesInLayer.iterator();

					while (iEntities.hasNext())
					{
						currentEntity=(Drawable2DEntity )(iEntities.next());
						if (currentEntity.isValide())
							currentEntity.drawMe();
					}
				}
				// Here should be painted the FX and particles for each level!
				// The particles
				HashMap<Long,Particles> currentLayerParticles=ourParticles.get(iLayer);
				if (!currentLayerParticles.isEmpty())
				{
					//myLog.add2Log(currentLayerParticles.size() + " particles system to draw in layer "+iLayer);
					Collection particlesInLayer=currentLayerParticles.values();

					Iterator iParticles = particlesInLayer.iterator();

					while (iParticles.hasNext())
					{
						//System.out.println("Paint one set of particles");
						currentParticles=(Particles )(iParticles.next());
						if (currentParticles.isValide())
							currentParticles.drawUs();
					}
				}

			}	
		}
		else
		{
			// cheat the system to render in 3D
			// first right, shift then render
			{
				// In order, paint all the entities!
				for (int iLayer=0;iLayer<40;iLayer++)
				{
					HashMap<Long,Drawable2DEntity> currentLayer=entitiesToDrawLayers.get(iLayer);
					if (!currentLayer.isEmpty())
					{
						//myLog.add2Log(currentLayer.size() + " entities to draw in layer "+iLayer);
						Collection entitiesInLayer=currentLayer.values();
						Iterator iEntities = entitiesInLayer.iterator();

						while (iEntities.hasNext())
						{
							currentEntity=(Drawable2DEntity )(iEntities.next());
							if (currentEntity.is3D)
							{
								currentEntity.xBackup = currentEntity.getX();
								currentEntity.setAbsPos(currentEntity.getX()/2+currentEntity.getZ()/10, currentEntity.getY(), currentEntity.getZ());
								if (currentEntity.isValide())
									currentEntity.drawMe();
								currentEntity.setX(currentEntity.xBackup);
							}
							else
							{
								if (currentEntity.isValide())
									currentEntity.drawMe();
							}
						}
					}
					// Here should be painted the FX and particles for each level!
					// The particles
					HashMap<Long,Particles> currentLayerParticles=ourParticles.get(iLayer);
					if (!currentLayerParticles.isEmpty())
					{
						//myLog.add2Log(currentLayerParticles.size() + " particles system to draw in layer "+iLayer);
						Collection particlesInLayer=currentLayerParticles.values();

						Iterator iParticles = particlesInLayer.iterator();

						while (iParticles.hasNext())
						{
							//System.out.println("Paint one set of particles");
							currentParticles=(Particles )(iParticles.next());
							if (currentParticles.isValide())
								currentParticles.drawUs();
						}
					}

				}	
			}
			// then left, restore and shift then render
			
			{
				// In order, paint all the entities!
				for (int iLayer=0;iLayer<40;iLayer++)
				{
					HashMap<Long,Drawable2DEntity> currentLayer=entitiesToDrawLayers.get(iLayer);
					if (!currentLayer.isEmpty())
					{
						//myLog.add2Log(currentLayer.size() + " entities to draw in layer "+iLayer);
						Collection entitiesInLayer=currentLayer.values();
						Iterator iEntities = entitiesInLayer.iterator();

						while (iEntities.hasNext())
						{
							currentEntity=(Drawable2DEntity )(iEntities.next());
							if (currentEntity.is3D)
							{
								currentEntity.xBackup = currentEntity.getX();
								currentEntity.setAbsPos(middlePos+currentEntity.getX()/2-currentEntity.getZ()/10, currentEntity.getY(), currentEntity.getZ());
								if (currentEntity.isValide())
									currentEntity.drawMe();
								currentEntity.setX(currentEntity.xBackup);
							}
						}
					}
					// Here should be painted the FX and particles for each level!
					// The particles
					HashMap<Long,Particles> currentLayerParticles=ourParticles.get(iLayer);
					if (!currentLayerParticles.isEmpty())
					{
						//myLog.add2Log(currentLayerParticles.size() + " particles system to draw in layer "+iLayer);
						Collection particlesInLayer=currentLayerParticles.values();

						Iterator iParticles = particlesInLayer.iterator();

						while (iParticles.hasNext())
						{
							//System.out.println("Paint one set of particles");
							currentParticles=(Particles )(iParticles.next());
							if (currentParticles.isValide())
								currentParticles.drawUs();
						}
					}

				}	
			}
		}
	}

	/**
	 * @return the middlePos
	 */
	public double getMiddlePos()
	{
		return middlePos;
	}

	/**
	 * @param middlePos the middlePos to set
	 */
	public void setMiddlePos(double middlePos)
	{
		this.middlePos = middlePos;
	}
}
