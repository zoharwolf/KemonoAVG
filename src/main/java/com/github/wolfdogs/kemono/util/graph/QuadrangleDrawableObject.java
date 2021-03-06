package com.github.wolfdogs.kemono.util.graph;

import static org.lwjgl.opengl.GL11.*;

public class QuadrangleDrawableObject extends DrawableObject implements Colorable
{
	private float width, height;
	private float r, g, b, a;
	
	
	public QuadrangleDrawableObject(float width, float height)
	{
		setWidth(width);
		setHeight(height);
	}
	
	public void setWidth(float width)
	{
		this.width = width;
		setCenterX(width / 2);
	}
	
	public void setHeight(float height)
	{
		this.height = height;
		setCenterY(height / 2);
	}
	
	@Override
	protected void onRender()
	{
		glColor4f(r, g, b, a);
		
		glBegin(GL_QUADS);
		glVertex2f(0.0f, 0.0f);
		glVertex2f(width, 0.0f);
		glVertex2f(width, height);
		glVertex2f(0.0f, height);
		glEnd();
	}
	
	@Override
	public float getR()
	{
		return r;
	}
	
	@Override
	public void setR(float r)
	{
		this.r = r;
	}
	
	@Override
	public float getG()
	{
		return g;
	}
	
	@Override
	public void setG(float g)
	{
		this.g = g;
	}
	
	@Override
	public float getB()
	{
		return b;
	}
	
	@Override
	public void setB(float b)
	{
		this.b = b;
	}
	
	@Override
	public float getA()
	{
		return a;
	}
	
	@Override
	public void setA(float a)
	{
		this.a = a;
	}
	
	@Override
	public void setColor(float r, float g, float b)
	{
		setR(r);
		setG(g);
		setB(b);
	}
	
	@Override
	public void setColor(float r, float g, float b, float a)
	{
		setR(r);
		setG(g);
		setB(b);
		setA(a);
	}
}
