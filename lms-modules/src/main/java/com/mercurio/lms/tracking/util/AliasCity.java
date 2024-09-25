package com.mercurio.lms.tracking.util;

import com.mercurio.lms.tracking.Cities;
import com.mercurio.lms.tracking.City;
import com.thoughtworks.xstream.XStream;

public class AliasCity {
	
	public static XStream createAlias(){
		
		XStream xstream = new XStream();
        xstream.alias("cities", Cities.class);
        xstream.alias("city", City.class);
      
        xstream.addImplicitCollection(Cities.class, "cities");
        
        return xstream;
	}
	
}
