package com.northtech.WeChat.wxmetainfo.bean.wxbutton;

//复合按钮
public class ComplexButton extends BaseButton {
  
	private BaseButton[] sub_button;

	public BaseButton[] getSub_button() {
		return sub_button;
	}

	public void setSub_button(BaseButton[] sub_button) {
		this.sub_button = sub_button;
	}
	
}
