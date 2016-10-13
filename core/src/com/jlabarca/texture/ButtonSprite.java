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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Static sprite is texture encapsulated by Actor. It cannot be scaled or rotated.
 * 
 */
public class ButtonSprite extends Actor
{
	private TextureRegion textureRegion;
	private float   yOffset = Gdx.graphics.getHeight();
	private Circle cuerpo= new Circle();
	public ButtonSprite(TextureRegion textureRegion)
	{
		this.textureRegion = textureRegion;
		// Set the sprite width and height.
		this.setWidth(textureRegion.getRegionWidth());
		this.setHeight(textureRegion.getRegionHeight());
        this.setBounds(0, 0, getWidth(), getHeight());
        this.cuerpo.setRadius(getWidth()/2);

	}
	  public void touch(float x, float y)
	   {
		  if(cuerpo.contains(x, yOffset-y))
			  make();
	   }
	public void make() {
		// TODO Auto-generated method stub
		
	}

	@Override
	   public Actor hit(float x, float y, boolean touchable)
	   {
	      return super.hit(x, y, touchable);
	   }
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		batch.draw(textureRegion, this.getX(), this.getY());
	}
	@Override
	public void setX(float x){
		super.setX(x);
		cuerpo.x = getWidth()/2+this.getX();
	}
	@Override
	public void setY(float y){
		super.setY(y);
		cuerpo.y = getHeight()/2+this.getY();
	}



}
