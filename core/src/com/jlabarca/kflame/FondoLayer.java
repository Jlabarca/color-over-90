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

package com.jlabarca.kflame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.jlabarca.definition.AppActorTextures;
import com.jlabarca.director.Director;
import com.jlabarca.director.Layer;
import com.jlabarca.texture.BackgroundSprite;
import com.jlabarca.texture.TextureCache;
import com.jlabarca.texture.TextureDefinition;

/**
 * Scene layer.
 * 
 */
public class FondoLayer extends Layer
{

	private Texture background;
	private BackgroundSprite backGroundSprite,backGroundSprite1;
	private int tipo;
	/**
	 * Create layer.
	 * 
	 * @param stage
	 */
	public FondoLayer(float width, float height ,int tipo)
	{
		this.setWidth(width);
		this.setHeight(height);
		this.tipo = tipo;
		loadTextures();
		//buildElements();
	}

	/**
	 * Load view textures.
	 * 
	 */
	private void loadTextures()
	{
		
		
		
		String back1,back2;
		back1 = AppActorTextures.TEXTURE_BACKGROUND;
		//back2 = AppActorTextures.TEXTURE_GLASS;
		
		TextureCache textureCache = TextureCache.instance();
		TextureDefinition definition = textureCache.getDefinition(back1);
		background = textureCache.getTexture(definition).getTexture();
		backGroundSprite = new BackgroundSprite(background, this.getWidth(), this.getHeight(),tipo,0,0);

//		definition = textureCache.getDefinition(AppActorTextures.TEXTURE_BACKGROUND2);
//		background = textureCache.getTexture(definition).getTexture();
//		backGroundSprite2 = new BackgroundSprite(background, this.getWidth(), this.getHeight(),tipo,1600,800);
//
//		definition = textureCache.getDefinition(AppActorTextures.TEXTURE_BACKGROUND3);
//		background = textureCache.getTexture(definition).getTexture();
//		backGroundSprite3 = new BackgroundSprite(background, this.getWidth(), this.getHeight(),tipo,800,400);

		
	}
	
	public void changeTextures(String s1,String s2,int tipo)
	{
		this.tipo = tipo;
		TextureCache textureCache = TextureCache.instance();
		TextureDefinition definition = textureCache.getDefinition(s1);
		background = textureCache.getTexture(definition).getTexture();
		backGroundSprite = new BackgroundSprite(background, this.getWidth(), this.getHeight(),tipo,650,1500);
		
		definition = textureCache.getDefinition(s2);
		background = textureCache.getTexture(definition).getTexture();
		backGroundSprite1 = new BackgroundSprite(background, this.getWidth(), this.getHeight(),tipo,200,1300);

	}


	public void updateSize(int x,int y){
		backGroundSprite.setSize(x,y);
		backGroundSprite1.setSize(x,y);
//		backGroundSprite2.setSize(x,y);
//		backGroundSprite3.setSize(x,y);
	}
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		batch.setShader(null);
		backGroundSprite.draw(batch, parentAlpha);
		//backGroundSprite.setColor(((GameScene)(Director.instance().getScene())).fpsLayer.goalColor);

		//backGroundSprite1.draw(batch, parentAlpha);
//		backGroundSprite2.draw(batch, parentAlpha);
//		backGroundSprite3.draw(batch, parentAlpha);

	}





}
