package com.mercurio.lms.services;

import java.util.Date;

import javax.inject.Singleton;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.deser.CustomDeserializerFactory;
import org.codehaus.jackson.map.deser.StdDeserializerProvider;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.rest.json.DateDeserializer;
import com.mercurio.lms.rest.json.DateTimeDeserializer;
import com.mercurio.lms.rest.json.YearMonthDayDeserializer;
import com.mercurio.lms.rest.json.YearMonthDaySerializer;

@Provider
@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class ContextWSResolver extends JacksonJaxbJsonProvider {

	public ContextWSResolver() throws Exception {
		super();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
		
		CustomDeserializerFactory cdsf = new CustomDeserializerFactory();
		cdsf.addSpecificMapping(DateTime.class, new DateTimeDeserializer());
		cdsf.addSpecificMapping(Date.class, new DateDeserializer());		
		cdsf.addSpecificMapping(YearMonthDay.class, new YearMonthDayDeserializer());
		mapper.setDeserializerProvider(new StdDeserializerProvider(cdsf));
		
		CustomSerializerFactory sf = new CustomSerializerFactory();
		sf.addSpecificMapping(YearMonthDay.class, new YearMonthDaySerializer());
		mapper.setSerializerFactory(sf);

		setMapper(mapper);
	}
}
