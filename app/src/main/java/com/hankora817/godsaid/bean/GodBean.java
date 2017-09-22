package com.hankora817.godsaid.bean;

import java.io.Serializable;

import org.json.JSONObject;

public class GodBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int number;
	private String said;
	private String which;
	
	
	public String getSaid()
	{
		return said;
	}
	
	
	public void setSaid(String said)
	{
		this.said = said;
	}
	
	
	public String getWhich()
	{
		return which;
	}
	
	
	public void setWhich(String which)
	{
		this.which = which;
	}
	
	
	public int getNumber()
	{
		return number;
	}
	
	
	public void setNumber(int number)
	{
		this.number = number;
	}
	
	
	public GodBean getJSONObject()
	{
		return this;
	}
	
	
	public void setJSONObject(String json)
	{
		try
		{
			JSONObject obj = new JSONObject(json);
			if (obj.has("description"))
			{
				this.setSaid(obj.getJSONObject("description").getString("said"));
				this.setWhich(obj.getJSONObject("description").getString("which"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}