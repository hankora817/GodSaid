package com.hankora817.godsaid.bean;

import org.json.JSONObject;

public class QuizBean
{
	private int number;
	private String said;
	private String which;
	private String hint_1;
	private String hint_2;
	private String hint_3;
	private int correct;
	
	
	public Integer getNumber()
	{
		return number;
	}
	
	
	public void setNumber(int number)
	{
		this.number = number;
	}
	
	
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
	
	
	public String getHint_1()
	{
		return hint_1;
	}
	
	
	public void setHint_1(String hint_1)
	{
		this.hint_1 = hint_1;
	}
	
	
	public String getHint_2()
	{
		return hint_2;
	}
	
	
	public void setHint_2(String hint_2)
	{
		this.hint_2 = hint_2;
	}
	
	
	public String getHint_3()
	{
		return hint_3;
	}
	
	
	public void setHint_3(String hint_3)
	{
		this.hint_3 = hint_3;
	}
	
	
	public int getCorrect()
	{
		return correct;
	}
	
	
	public void setCorrect(int correct)
	{
		this.correct = correct;
	}
	
	
	public void setJSONObject(String json)
	{
		try
		{
			JSONObject obj = new JSONObject(json);
			if (obj.has("description"))
			{
				JSONObject object = obj.getJSONObject("description");
				this.setNumber(object.getInt("number"));
				this.setSaid(object.getString("said"));
				this.setWhich(object.getString("which"));
				this.setHint_1(object.getString("hint_1"));
				this.setHint_2(object.getString("hint_2"));
				this.setHint_3(object.getString("hint_3"));
				this.setCorrect(object.getInt("correct"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
