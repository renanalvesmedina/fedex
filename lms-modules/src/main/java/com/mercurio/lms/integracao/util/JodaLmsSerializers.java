package com.mercurio.lms.integracao.util;

import java.io.IOException;

import org.apache.commons.lang.time.DateFormatUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class JodaLmsSerializers {
	private final static DateTimeFormatter ISO_DATE_FORMAT = DateTimeFormat
			.forPattern(DateFormatUtils.ISO_DATE_FORMAT.getPattern());

	private final static DateTimeFormatter ISO_DATETIME_TIME_ZONE_FORMAT = DateTimeFormat
			.forPattern(DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern());

	
	public static DateTimeSerializer createDateTimeSerializer(){
		return new DateTimeSerializer();
	}
	
	public static YearMonthDaySerializer createYearMonthDaySerializer(){
		return new YearMonthDaySerializer();
	}

	public final static class DateTimeSerializer extends JsonSerializer<DateTime> {
		@Override
		public void serialize(DateTime value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			
			if( value != null ){
                org.joda.time.DateTimeZone zone = value.getZone() == null 
                        ? org.joda.time.DateTimeZone.forID("America/Sao_Paulo") //DEFAULT_TMZ
                        : value.getZone();
                if(zone.isLocalDateTimeGap(value.toLocalDateTime())){
                    value = value.plusHours(1);
                }
				jgen.writeString(value.toString(ISO_DATETIME_TIME_ZONE_FORMAT));
			}
		}
	
	}

	public final static class YearMonthDaySerializer extends JsonSerializer<YearMonthDay> {

		@Override
		public void serialize(YearMonthDay value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			if( value != null ){
                DateTime dateTime = com.mercurio.lms.util.JTDateTimeUtils.yearMonthDayToDateTime(value);
				jgen.writeString(dateTime.toString(ISO_DATE_FORMAT));
			}
		}
	
	}
}
