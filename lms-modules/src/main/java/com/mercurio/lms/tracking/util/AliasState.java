package com.mercurio.lms.tracking.util;

import com.mercurio.lms.tracking.State;
import com.mercurio.lms.tracking.States;
import com.thoughtworks.xstream.XStream;

public class AliasState {
	
	public static XStream createAlias(){
		
		XStream xstream = new XStream();
        xstream.alias("states", States.class);
        xstream.alias("state", State.class);
      
        xstream.addImplicitCollection(States.class, "states");
        
        return xstream;
	}
	
}
