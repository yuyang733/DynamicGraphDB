package net.CVNIS.DynamicGraphDB;

public interface TimeProperty {
	
	public static final long INVALID_TIME = -1L;			//表示无效时间
	public static final long NOW = Long.MAX_VALUE;			//表示当前时间
	
	/**
	 * 获取创建时间戳
	 * @return
	 */
	long getCreateTimestamp();
	
	/**
	 * 获取最后一次操作的时间戳
	 * @return
	 */
	long getTimestamp();
	
}
