
package com.mercurio.lms.rest.json;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

public class DateDeserializer extends JsonDeserializer<Date> {
	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws JsonParseException, IOException {
		String dataHoraISO8601 = jp.getText();
		DateTime dt = ISODateTimeFormat.dateTimeParser().withOffsetParsed().parseDateTime(dataHoraISO8601);
		return new Date(dt.getYear() - 1900 , dt.getMonthOfYear() -1, dt.getDayOfMonth());
	}
}
