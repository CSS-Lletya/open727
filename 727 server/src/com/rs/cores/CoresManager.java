package com.rs.cores;

import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class CoresManager {

	/**
	 * Initializes all important cores management systems.
	 */
	public static void init() {
		byte availableProcessors = (byte) Runtime.getRuntime().availableProcessors();
		serverWorkersCount = availableProcessors;
		serverWorkerChannelExecutor = serverWorkersCount > 1 ? Executors.newFixedThreadPool(serverWorkersCount, new DecoderThreadFactory()) : Executors.newSingleThreadExecutor(new DecoderThreadFactory());
		serverBossChannelExecutor = Executors.newSingleThreadExecutor(new DecoderThreadFactory());
		fastExecutor = new Timer("Fast Executor");
		slowExecutor = Executors.newScheduledThreadPool(2, new SlowThreadFactory());
	}

	/**
	 * Shuts down all core management systems
	 */
	public static void shutdown() {
		serverWorkerChannelExecutor.shutdown();
		serverBossChannelExecutor.shutdown();
		fastExecutor.cancel();
		slowExecutor.shutdown();
		shutdown = true;
	}
	
	public static volatile boolean shutdown;
	public static ExecutorService serverWorkerChannelExecutor;
	public static ExecutorService serverBossChannelExecutor;
	public static Timer fastExecutor;
	public static ScheduledExecutorService slowExecutor;
	public static byte serverWorkersCount;
}