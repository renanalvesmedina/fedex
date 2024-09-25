package com.mercurio.lms.tracking.util;

import com.mercurio.lms.tracking.Depot;
import com.mercurio.lms.tracking.Depots;
import com.thoughtworks.xstream.XStream;

public class AliasDepot {
	
	public static XStream createAlias(){
		
		XStream xstream = new XStream();
        xstream.alias("depots", Depots.class);
        xstream.alias("depot", Depot.class);
      
        xstream.addImplicitCollection(Depots.class, "depots");
        
        return xstream;
	}
	
}
