package com.bbs.njtech.common.utils;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ThreadPoolUtils {
	
	private static ScheduledThreadPoolExecutor blockChainAddrPool = new ScheduledThreadPoolExecutor(3);

	public static ScheduledThreadPoolExecutor getBlockChainAddrPool() {
		return blockChainAddrPool;
	}

}
