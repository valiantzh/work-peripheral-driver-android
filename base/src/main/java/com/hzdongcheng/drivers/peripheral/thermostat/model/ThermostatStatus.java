package com.hzdongcheng.drivers.peripheral.thermostat.model;


public class ThermostatStatus
{
	
 	public static class TempControlStatus{
		
		public TempControlStatus()
		{
			setCuvinTemp(0);
			setDefrostingTemp(0);
			setCodeOpenTemp(0);
			setCodeCloseTemp(0);
			setHeatOpenTemp(0);
			setHeatCloseTemp(0);
		}
		/**
		 * 产品型号
		 */
		private int ProductType;       
		/**
		 * 库温探头传感器温度
		 */
		private int CuvinTemp;      
		/**
		 * 化霜探头传感器温度
		 */
		private int DefrostingTemp;  
		/**
		 * 制冷开机温度
		 */
		private int codeOpenTemp; 
		/**
		 * 制冷停机温度
		 */
		private int codeCloseTemp;
		/**
		 * 制热开机温度
		 */
		private int heatOpenTemp;
		/**
		 * 制热停机温度
		 */
		private int heatCloseTemp;
		/**
		 * 温控器警报警报
		 */
		private ThermostatWarning thermostatWarning;
		/**
		 * 温控器设备状态
		 */
		private ThermostatDeviceStatus thermostatDeviceStatus;
		/**
		 * @return the productType
		 */
		public int getProductType() {
			return ProductType;
		}
		/**
		 * @param productType the productType to set
		 */
		public void setProductType(int productType) {
			ProductType = productType;
		}
		/**
		 * @return the cuvinTemp
		 */
		public int getCuvinTemp() {
			return CuvinTemp;
		}
		/**
		 * @param cuvinTemp the cuvinTemp to set
		 */
		public void setCuvinTemp(int cuvinTemp) {
			CuvinTemp = cuvinTemp;
		}
		/**
		 * @return the defrostingTemp
		 */
		public int getDefrostingTemp() {
			return DefrostingTemp;
		}
		/**
		 * @param defrostingTemp the defrostingTemp to set
		 */
		public void setDefrostingTemp(int defrostingTemp) {
			DefrostingTemp = defrostingTemp;
		}
		/**
		 * @return the codeOpenTemp
		 */
		public int getCodeOpenTemp() {
			return codeOpenTemp;
		}
		/**
		 * @param codeOpenTemp the codeOpenTemp to set
		 */
		public void setCodeOpenTemp(int codeOpenTemp) {
			this.codeOpenTemp = codeOpenTemp;
		}
		/**
		 * @return the codeCloseTemp
		 */
		public int getCodeCloseTemp() {
			return codeCloseTemp;
		}
		/**
		 * @param codeCloseTemp the codeCloseTemp to set
		 */
		public void setCodeCloseTemp(int codeCloseTemp) {
			this.codeCloseTemp = codeCloseTemp;
		}
		/**
		 * @return the heatOpenTemp
		 */
		public int getHeatOpenTemp() {
			return heatOpenTemp;
		}
		/**
		 * @param heatOpenTemp the heatOpenTemp to set
		 */
		public void setHeatOpenTemp(int heatOpenTemp) {
			this.heatOpenTemp = heatOpenTemp;
		}
		/**
		 * @return the heatCloseTemp
		 */
		public int getHeatCloseTemp() {
			return heatCloseTemp;
		}
		/**
		 * @param heatCloseTemp the heatCloseTemp to set
		 */
		public void setHeatCloseTemp(int heatCloseTemp) {
			this.heatCloseTemp = heatCloseTemp;
		}
		
		/**
		 * @return the thermostatWarning
		 */
		public ThermostatWarning getThermostatWarning() {
			return thermostatWarning;
		}
		/**
		 * @param thermostatWarning the thermostatWarning to set
		 */
		public void setThermostatWarning(ThermostatWarning thermostatWarning) {
			this.thermostatWarning = thermostatWarning;
		}
		/**
		 * @return the thermostatDeviceStatus
		 */
		public ThermostatDeviceStatus getThermostatDeviceStatus() {
			return thermostatDeviceStatus;
		}
		/**
		 * @param thermostatDeviceStatus the thermostatDeviceStatus to set
		 */
		public void setThermostatDeviceStatus(ThermostatDeviceStatus thermostatDeviceStatus) {
			this.thermostatDeviceStatus = thermostatDeviceStatus;
		}
	}

	/**
	 * 
	 * @author 温控器警报状态 0：无报警；1：报警
	 *
	 */
	public static class ThermostatWarning{
		
		
		/**
		 * 门打开报警
		 */
		private int doorWarn=0;
		/**
		 * 压力开关报警
		 */
		private int stressWarn=0;
		/**
		 * 紧急外部报警
		 */
		private int instancyWarn=0;
		/**
		 * 普通外部报警
		 */
		private int generalWarn=0;
		/**
		 * 超出试用时间报警
		 */
		private int timeoutWarn=0;
		/**
		 * 库温超下限报警
		 */
		private int CuvinMinWarn=0;
		/**
		 *库温超上限报警
		 */
		private int CuvinMaxWarn=0;
		/**
		 * 化霜探头故障报警
		 */
		private int DefrostingSondeWarn=0;
		/**
		 * 库温探头故障报警
		 */
		private int CuvinSondeWarn=0;
		/**
		 * @return the doorWarn
		 */
		public int getDoorWarn() {
			return doorWarn;
		}
		/**
		 * @param doorWarn the doorWarn to set
		 */
		public void setDoorWarn(int doorWarn) {
			this.doorWarn = doorWarn;
		}
		/**
		 * @return the stressWarn
		 */
		public int getStressWarn() {
			return stressWarn;
		}
		/**
		 * @param stressWarn the stressWarn to set
		 */
		public void setStressWarn(int stressWarn) {
			this.stressWarn = stressWarn;
		}
		/**
		 * @return the instancyWarn
		 */
		public int getInstancyWarn() {
			return instancyWarn;
		}
		/**
		 * @param instancyWarn the instancyWarn to set
		 */
		public void setInstancyWarn(int instancyWarn) {
			this.instancyWarn = instancyWarn;
		}
		/**
		 * @return the generalWarn
		 */
		public int getGeneralWarn() {
			return generalWarn;
		}
		/**
		 * @param generalWarn the generalWarn to set
		 */
		public void setGeneralWarn(int generalWarn) {
			this.generalWarn = generalWarn;
		}
		/**
		 * @return the timeoutWarn
		 */
		public int getTimeoutWarn() {
			return timeoutWarn;
		}
		/**
		 * @param timeoutWarn the timeoutWarn to set
		 */
		public void setTimeoutWarn(int timeoutWarn) {
			this.timeoutWarn = timeoutWarn;
		}
		/**
		 * @return the cuvinMinWarn
		 */
		public int getCuvinMinWarn() {
			return CuvinMinWarn;
		}
		/**
		 * @param cuvinMinWarn the cuvinMinWarn to set
		 */
		public void setCuvinMinWarn(int cuvinMinWarn) {
			CuvinMinWarn = cuvinMinWarn;
		}
		/**
		 * @return the cuvinMaxWarn
		 */
		public int getCuvinMaxWarn() {
			return CuvinMaxWarn;
		}
		/**
		 * @param cuvinMaxWarn the cuvinMaxWarn to set
		 */
		public void setCuvinMaxWarn(int cuvinMaxWarn) {
			CuvinMaxWarn = cuvinMaxWarn;
		}
		/**
		 * @return the defrostingSondeWarn
		 */
		public int getDefrostingSondeWarn() {
			return DefrostingSondeWarn;
		}
		/**
		 * @param defrostingSondeWarn the defrostingSondeWarn to set
		 */
		public void setDefrostingSondeWarn(int defrostingSondeWarn) {
			DefrostingSondeWarn = defrostingSondeWarn;
		}
		/**
		 * @return the cuvinSondeWarn
		 */
		public int getCuvinSondeWarn() {
			return CuvinSondeWarn;
		}
		/**
		 * @param cuvinSondeWarn the cuvinSondeWarn to set
		 */
		public void setCuvinSondeWarn(int cuvinSondeWarn) {
			CuvinSondeWarn = cuvinSondeWarn;
		}
	}

	/**
	 * 
	 * @author 温控器设备状态 0：关闭；1：开启
	 *
	 */
	public static class ThermostatDeviceStatus{
		
		/**
		 * 制冷或制热状态  0：制冷 1：制热
		 */
		private int tempStatus=0;
		/**
		 * 风机状态
		 */
		private int fanStatus=0;
		/**
		 * 制热开机状态
		 */
		private int heatStatus=0;
		/**
		 * 制冷开机状态
		 */
		private int codeStatus=0;
		/**
		 * 化霜状态
		 */
		private int DefrostingStatus=0;
		/**
		 * @return the tempStatus
		 */
		public int getTempStatus() {
			return tempStatus;
		}
		/**
		 * @param tempStatus the tempStatus to set
		 */
		public void setTempStatus(int tempStatus) {
			this.tempStatus = tempStatus;
		}
		/**
		 * @return the fanStatus
		 */
		public int getFanStatus() {
			return fanStatus;
		}
		/**
		 * @param fanStatus the fanStatus to set
		 */
		public void setFanStatus(int fanStatus) {
			this.fanStatus = fanStatus;
		}
		/**
		 * @return the heatStatus
		 */
		public int getHeatStatus() {
			return heatStatus;
		}
		/**
		 * @param heatStatus the heatStatus to set
		 */
		public void setHeatStatus(int heatStatus) {
			this.heatStatus = heatStatus;
		}
		/**
		 * @return the codeStatus
		 */
		public int getCodeStatus() {
			return codeStatus;
		}
		/**
		 * @param codeStatus the codeStatus to set
		 */
		public void setCodeStatus(int codeStatus) {
			this.codeStatus = codeStatus;
		}
		/**
		 * @return the defrostingStatus
		 */
		public int getDefrostingStatus() {
			return DefrostingStatus;
		}
		/**
		 * @param defrostingStatus the defrostingStatus to set
		 */
		public void setDefrostingStatus(int defrostingStatus) {
			DefrostingStatus = defrostingStatus;
		}
	}

	/**
	 * 
	 * @author 温控器继电器状态 0：关闭；1：开启
	 *
	 */
	public static class RelayStatus{
		/**
		 * 风机继电器
		 */
		private int fanRelayStatus=0;
		/**
		 * 加热丝继电器
		 */
		private int heatRelayStatus=0;
		/**
		 * 压缩机继电器
		 */
		private int codeRelayStatus=0;
		/**
		 * @return the fanRelayStatus
		 */
		public int getFanRelayStatus() {
			return fanRelayStatus;
		}
		/**
		 * @param fanRelayStatus the fanRelayStatus to set
		 */
		public void setFanRelayStatus(int fanRelayStatus) {
			this.fanRelayStatus = fanRelayStatus;
		}
		/**
		 * @return the heatRelayStatus
		 */
		public int getHeatRelayStatus() {
			return heatRelayStatus;
		}
		/**
		 * @param heatRelayStatus the heatRelayStatus to set
		 */
		public void setHeatRelayStatus(int heatRelayStatus) {
			this.heatRelayStatus = heatRelayStatus;
		}
		/**
		 * @return the codeRelayStatus
		 */
		public int getCodeRelayStatus() {
			return codeRelayStatus;
		}
		/**
		 * @param codeRelayStatus the codeRelayStatus to set
		 */
		public void setCodeRelayStatus(int codeRelayStatus) {
			this.codeRelayStatus = codeRelayStatus;
		}
	}

	
	
}
