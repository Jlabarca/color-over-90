
package com.jlabarca.kflame;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.jlabarca.definition.AppActorTextures;
import com.jlabarca.texture.TextureCache;
import com.jlabarca.texture.TextureDefinition;

import finnstr.libgdx.liquidfun.ParticleSystem;

/** Renders all particles from a given {@link ParticleSystem}
 * @author FinnStr */
public class ParticleDebugRenderer2 {

	protected ShaderProgram shader;
	protected Mesh mesh;
	private Texture texture;
	Vector2 scale;
	Color color;
	public ParticleDebugRenderer2(Color color, int maxParticleNumber) {
		ShaderProgram.pedantic = false;
		shader = createShader(color);
		//shader = new ShaderProgram(Gdx.files.internal("shaders/defaultim.vert").readString(), 
		//		Gdx.files.internal("shaders/defaultim.frag").readString());

		setMaxParticleNumber(maxParticleNumber);
		TextureCache textureCache = TextureCache.instance();
		TextureDefinition definition = textureCache.getDefinition(AppActorTextures.TEXTURE_PARTICLE);
		texture = textureCache.getTexture(definition).getTexture();
		scale = new Vector2(GameLayer.scale.x*2,GameLayer.scale.y*2);
		this.color = color;
	}


	public void setMaxParticleNumber(int pCount) {
		if(mesh != null) mesh.dispose();
		mesh = new Mesh(false, pCount, pCount, new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE), 
				 new VertexAttribute(Usage.ColorUnpacked, 4, ShaderProgram.COLOR_ATTRIBUTE));	}
	
	public int getMaxParticleNumber() {
		return mesh.getMaxVertices();
	}
	
	public void render (ParticleSystem pSystem, float pRadiusScale, Matrix4 pProjMatrix) {
		texture.bind();
		shader.begin();
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA); 
		Gdx.gl20.glEnable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
		Gdx.gl20.glEnable(0x8861); //GL11.GL_POINT_SPRITE_OES
		
		shader.setUniformf("particlesize",pSystem.getParticleRadius());
		shader.setUniformf("scale", 3f*pRadiusScale);
		shader.setUniformMatrix("u_projTrans", pProjMatrix);
		shader.setUniformi("u_texture", 0);
	

		mesh.setVertices(pSystem.getParticlePositionAndColorBufferArray(true));
		mesh.render(shader, GL20.GL_POINTS, 0, pSystem.getParticleCount());
		shader.end();
		Gdx.gl20.glDisable(0x8861);
		
	}
	
	public void dispose() {
		shader.dispose();
		mesh.dispose();
	}
	
	static final public ShaderProgram createShader(Color pColor) {
	
		String prefix = "";
		if(Gdx.app.getType() == ApplicationType.Desktop)
			prefix +="#version 120\n";
		else
			prefix +="#version 100\n";
		
		final String vertexShader = 
				"attribute vec4 a_position;\n" //
				+ "\n" //
				+ "uniform float particlesize;\n" //
				+ "uniform float scale;\n"
				+ "uniform mat4 u_projTrans;\n" //
				+ "attribute vec4 a_color;\n" //
				+ "varying vec4 v_color;" 
				+ "\n" //
				+ "void main()\n" //
				+ "{\n" //
				+ "   gl_Position =  u_projTrans * vec4(a_position.xy, 0.0, 1.0);\n" //
				+ "   gl_PointSize = scale * particlesize;\n" //
				+ "   v_color = a_color;" //
				+ "   v_color = floor(v_color/0.3)*0.1; \n"
				+ "}\n";
		final String fragmentShader = "#ifdef GL_ES\n" //
		      + "#define LOWP lowp\n" //
		      + "precision mediump float;\n" //
		      + "#else\n" //
		      + "#define LOWP \n" //
		      + "#endif\n" //
		      + "varying vec4 v_color;\n"
			  + "uniform sampler2D u_texture;\n" 
			  + "void main()\n"//
				+ "{\n" //
				+ " 	gl_FragColor = v_color*texture2D(u_texture, gl_PointCoord);\n" //
				+ "}";
		
				
		
		
		ShaderProgram shader = new ShaderProgram(prefix + vertexShader,
				prefix + fragmentShader);
		if (shader.isCompiled() == false) {
			Gdx.app.log("ERROR", shader.getLog());
		}


		return shader;
	}
}
