package com.mercurio.lms.integracao.util;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.Module;
import org.codehaus.jackson.map.module.SimpleModule;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class JodaISOModule extends SimpleModule {
	
	public static Module newInstance(){
		return new JodaISOModule();
	}
	
	private JodaISOModule() {
		super("Joda-ISO-Module", Version.unknownVersion());
		addSerializer(DateTime.class,JodaLmsSerializers.createDateTimeSerializer());
		addSerializer(YearMonthDay.class,JodaLmsSerializers.createYearMonthDaySerializer());
	}

}
