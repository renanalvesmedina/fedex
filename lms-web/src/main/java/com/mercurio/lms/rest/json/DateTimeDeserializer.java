
package com.mercurio.lms.rest.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

public class DateTimeDeserializer extends JsonDeserializer<DateTime> {
	@Override
	public DateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
		String dataHoraISO8601 = jp.getText();
		return ISODateTimeFormat.dateTimeParser().withOffsetParsed().parseDateTime(dataHoraISO8601);
	}
}
