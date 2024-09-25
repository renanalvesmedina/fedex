package com.mercurio.lms.rest.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.JTDateTimeUtils;

public class YearMonthDaySerializer extends JsonSerializer<YearMonthDay> {
	
	public static String formatter(YearMonthDay value) {
		if (value == null) {
			return "";
		} 
		
		DateTime dt = JTDateTimeUtils.yearMonthDayToDateTime(value);
		return dt.toString();
	}
	
	@Override
	public void serialize(YearMonthDay value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeString(YearMonthDaySerializer.formatter(value));
	}
}
