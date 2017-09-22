package com.hankora817.godsaid.graph;

import android.graphics.Color;

public class LineGraph
{
	private String name = null;
	private int color = Color.BLUE;
	private Integer[] coordinateArr = null;
	private int bitmapResource = -1;
	
	
	public LineGraph(String name, int color, Integer[] coordinateArr)
	{
		this.name = name;
		this.color = color;
		this.setCoordinateArr(coordinateArr);
	}
	
	
	public LineGraph(String name, int color, Integer[] coordinateArr, int bitmapResource)
	{
		this.name = name;
		this.color = color;
		this.setCoordinateArr(coordinateArr);
		this.bitmapResource = bitmapResource;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	
	public int getColor()
	{
		return color;
	}
	
	
	public void setColor(int color)
	{
		this.color = color;
	}
	
	
	public Integer[] getCoordinateArr()
	{
		return coordinateArr;
	}
	
	
	public void setCoordinateArr(Integer[] coordinateArr)
	{
		this.coordinateArr = coordinateArr;
	}
	
	
	public int getBitmapResource()
	{
		return bitmapResource;
	}
	
	
	public void setBitmapResource(int bitmapResource)
	{
		this.bitmapResource = bitmapResource;
	}
}
