package com.jlabarca.director;


import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 * Represents a visual layer which can receive input.
 * 
 * Layers are a group of actors or other groups.
 * 
 */
public class Layer extends Group implements InputProcessor, Node
{
	@Override
	public void enter()
	{
		SnapshotArray<Actor> list = this.getChildren();

		int size = list.size;
		for (int i = 0; i < size; i++)
		{
			Actor actor = list.get(i);

			if (actor instanceof Layer)
			{
				Layer layer = (Layer) actor;

				layer.enter();
			}
		}
	}

	@Override
	public void exit()
	{
		SnapshotArray<Actor> list = this.getChildren();

		int size = list.size;
		for (int i = 0; i < size; i++)
		{
			Actor actor = list.get(i);

			if (actor instanceof Layer)
			{
				Layer layer = (Layer) actor;

				layer.exit();
			}
		}
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button)
	{
		
		boolean handled = false;

		SnapshotArray<Actor> list = this.getChildren();

		int size = list.size;
		int index = 0;
		while (index < size && !handled)
		{
			Actor actor = list.get(index);

			if (actor instanceof Layer)
			{
				Layer layer = (Layer) actor;

				handled = layer.touchDown(x, y, pointer, button);
			}

			index++;
		}

		return handled;
		
		
		
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button)
	{
		boolean handled = false;
		SnapshotArray<Actor> list = this.getChildren();

		int size = list.size;
		int index = 0;
		while (index < size && !handled)
		{
			Actor actor = list.get(index);

			if (actor instanceof Layer)
			{
				Layer layer = (Layer) actor;

				handled = layer.touchUp(x, y, pointer, button);
			}

			index++;
		}

		return handled;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer)
	{
		
		boolean handled = false;

		SnapshotArray<Actor> list = this.getChildren();

		int size = list.size;
		int index = 0;
		while (index < size && !handled)
		{
			Actor actor = list.get(index);

			if (actor instanceof Layer)
			{
				Layer layer = (Layer) actor;

				handled = layer.touchDragged(x, y, pointer);
			}

			index++;
		}

		return handled;
	}

	@Override
	public boolean mouseMoved(int x, int y)
	{
		boolean handled = false;

		SnapshotArray<Actor> list = this.getChildren();

		int size = list.size;;
		int index = 0;
		while (index < size && !handled)
		{
			Actor actor = list.get(index);

			if (actor instanceof Layer)
			{
				Layer layer = (Layer) actor;

				handled = layer.mouseMoved(x, y);
			}

			index++;
		}

		return handled;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}


}
