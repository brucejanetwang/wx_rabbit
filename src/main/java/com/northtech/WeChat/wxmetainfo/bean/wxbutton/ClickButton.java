package com.northtech.WeChat.wxmetainfo.bean.wxbutton;

//click按钮
public class ClickButton extends BaseButton{
	 public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	private String type;
	 private String key;
}
