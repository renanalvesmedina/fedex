
package com.mercurio.lms.rest.json;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.joda.time.format.ISODateTimeFormat;

public class YearMonthDayDeserializer extends JsonDeserializer<YearMonthDay> {
	
	public static YearMonthDay parse(String strISO8601Date) {
		if (StringUtils.isEmpty(strISO8601Date)) {
			return null;
		}
		
		DateTime dateTime = ISODateTimeFormat.dateTimeParser().withOffsetParsed().parseDateTime(strISO8601Date);
		return new YearMonthDay(dateTime);
	}
	
	@Override
	public YearMonthDay deserialize(JsonParser jp, DeserializationContext ctxt) throws JsonParseException, IOException {
		return YearMonthDayDeserializer.parse(jp.getText());
	}
}
