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

package com.jlabarca.definition;

import java.util.LinkedList;
import java.util.List;

import com.jlabarca.texture.TextureDefinition;

/**
 * You can populate this if you are not using a pre-defined packed texture
 * created using the TexturePacker class.
 * 
 */
@SuppressWarnings("serial")
public class AppActorTextures
{
	public static final String TEXTURE_PATH = "gfx";
	public static final String TEXTURE_PARTICLE = "water2.png";
	public static final String TEXTURE_GLASS = "glasswb.png";
	public static final String TEXTURE_BACKGROUND = "coolWhite.jpg";
	public static final String TEXTURE_SPLASH = "splash.png";
	public static final String TEXTURE_LOGO = "ColorOver.png";
	public static final String TEXTURE_LOGO90 = "90.png";


	public static final List<TextureDefinition> TEXTURES = new LinkedList<TextureDefinition>()
	{
		{
			add(new TextureDefinition(TEXTURE_PARTICLE, TEXTURE_PATH + "/" + TEXTURE_PARTICLE));
			add(new TextureDefinition(TEXTURE_GLASS, TEXTURE_PATH + "/" + TEXTURE_GLASS));
			add(new TextureDefinition(TEXTURE_BACKGROUND, TEXTURE_PATH + "/" + TEXTURE_BACKGROUND));
			add(new TextureDefinition(TEXTURE_SPLASH, TEXTURE_PATH + "/" + TEXTURE_SPLASH));
			add(new TextureDefinition(TEXTURE_LOGO, TEXTURE_PATH + "/" + TEXTURE_LOGO));
			add(new TextureDefinition(TEXTURE_LOGO90, TEXTURE_PATH + "/" + TEXTURE_LOGO90));

		}
	};
	
}
