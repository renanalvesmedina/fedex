package com.mercurio.lms.util;

import org.exolab.castor.xml.OutputFormat;
import org.exolab.castor.xml.Serializer;
import org.exolab.castor.xml.XMLSerializerFactory;

public class LmsXMLSerializerFactory implements XMLSerializerFactory {

	public Serializer getSerializer() {
		return new LmsXMLSerializer();
	}


	public OutputFormat getOutputFormat() {
		return new LmsOutputFormat();
	}

}
