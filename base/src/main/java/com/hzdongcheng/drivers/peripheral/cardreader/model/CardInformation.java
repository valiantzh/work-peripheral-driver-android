package com.hzdongcheng.drivers.peripheral.cardreader.model;

/**
 * 
 * @ClassName: CardInformation 
 * @Description: TODO(卡片信息封装类) 
 * @author Administrator 
 * @date 2018年1月27 下午5:09:27 
 * @version 1.0
 */
public class CardInformation {
	
	private String cardID  = "";         //卡号
	private String name    = "";         //姓名
	private String address = "";         //住址
	private String nation  = "";         //名族
	private String sex     = "";         //性别
	private String Born    = "";         //出生日期
	private String GrantDept = "";       //发证机关
	private String UserLifeBegin = "";   //办证日期
	private String userLifeEnd   = "";   //截止日期
	
	/**
	 * @return the cardID
	 */
	public String getCardID() {
		return cardID;
	}

	/**
	 * @param cardID the cardID to set
	 */
	public void setCardID(String cardID) {
		this.cardID = cardID;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the nation
	 */
	public String getNation() {
		return nation;
	}

	/**
	 * @param nation the nation to set
	 */
	public void setNation(String nation) {
		this.nation = nation;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * @return the born
	 */
	public String getBorn() {
		return Born;
	}

	/**
	 * @param born the born to set
	 */
	public void setBorn(String born) {
		Born = born;
	}

	/**
	 * @return the grantDept
	 */
	public String getGrantDept() {
		return GrantDept;
	}

	/**
	 * @param grantDept the grantDept to set
	 */
	public void setGrantDept(String grantDept) {
		GrantDept = grantDept;
	}

	/**
	 * @return the userLifeBegin
	 */
	public String getUserLifeBegin() {
		return UserLifeBegin;
	}

	/**
	 * @param userLifeBegin the userLifeBegin to set
	 */
	public void setUserLifeBegin(String userLifeBegin) {
		UserLifeBegin = userLifeBegin;
	}

	/**
	 * @return the userLifeEnd
	 */
	public String getUserLifeEnd() {
		return userLifeEnd;
	}

	/**
	 * @param userLifeEnd the userLifeEnd to set
	 */
	public void setUserLifeEnd(String userLifeEnd) {
		this.userLifeEnd = userLifeEnd;
	}
	
	public String getString(){	
		String result =  cardID + "|" + name+"|"+address+"|"+nation+"|"+sex+"|"+Born+"|"+GrantDept+
				"|"+UserLifeBegin+"|"+userLifeEnd;
		return result;
	}
}
