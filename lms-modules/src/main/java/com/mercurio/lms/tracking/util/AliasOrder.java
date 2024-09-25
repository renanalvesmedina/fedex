package com.mercurio.lms.tracking.util;

import com.mercurio.lms.tracking.Order;
import com.mercurio.lms.tracking.State;
import com.mercurio.lms.tracking.States;
import com.thoughtworks.xstream.XStream;

public class AliasOrder {

	public static XStream createAlias() {
		XStream xstream = new XStream();
		xstream.alias("order", Order.class);
		return xstream;
	}

}
