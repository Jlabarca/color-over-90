/*
 * -----------------------------------------------------------------------
 * Copyright 2012 - Alistair Rutherford - www.netthreads.co.uk
 * -----------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.jlabarca.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents scrolling background.
 * 
 */
public class BackgroundSprite extends Sprite
{

	int velx=0,vely=0,tipo=0;

	public BackgroundSprite(Texture texture, float width, float height, int tipo)
	{
		  super(texture, (int)width, (int)height);
		  this.tipo = tipo;
		  switch (tipo) {
		  case 0:  
		  		   break;
	      case 1:  texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	               break;
	      case 2:  texture.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
	               break;
	      case 3: texture.setWrap(TextureWrap.Repeat, TextureWrap.ClampToEdge);
	      			break;
	      case 4: texture.setWrap(TextureWrap.MirroredRepeat, TextureWrap.ClampToEdge);
	               break;
		  }
		
	}
	public BackgroundSprite(Texture texture, float width, float height, int tipo,int velx,int vely)
	{
		  super(texture, (int)width, (int)height);
		  this.velx=velx;
		  this.vely=vely;
		  this.tipo = tipo;
		  setSize(width,height);
		  
		  switch (tipo) {
		  case 0:  
		  		   break;
	      case 1:  texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	               break;
	      case 2:  texture.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
	               break;
	      case 3: texture.setWrap(TextureWrap.Repeat, TextureWrap.ClampToEdge);
	      			break;
	      case 4: texture.setWrap(TextureWrap.MirroredRepeat, TextureWrap.ClampToEdge);
	               break;
		  }		
		
	}

	/**
	 * Scroll the background across the frame duration.
	 * 
	 */
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch);
		
	}




}
