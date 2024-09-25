package com.mercurio.lms.util;

import java.text.MessageFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StatisticLogger {

	private static final Logger LOGGER = LogManager.getLogger(StatisticLogger.class);
	
	public static void statistcVM(String msg){
		String MSG = "Memoria ==> Livre: {0} de: {1} total usada: {2}";
		Runtime.getRuntime().gc();
		Long memFree = Runtime.getRuntime().freeMemory();
		Long memTotal = Runtime.getRuntime().totalMemory();
		LOGGER.warn(msg);
		LOGGER.warn(MessageFormat.format(MSG, memFree, memTotal, memTotal - memFree));
	}
}
