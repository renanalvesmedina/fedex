package com.mercurio.lms.tracking.util;

import com.mercurio.lms.tracking.Service;
import com.mercurio.lms.tracking.Services;
import com.thoughtworks.xstream.XStream;

public class AliasService {

	public static XStream createAlias() {

		XStream xstream = new XStream();
		xstream.alias("services", Services.class);
		xstream.alias("service", Service.class);

		xstream.addImplicitCollection(Services.class, "services");

		return xstream;
	}

}
