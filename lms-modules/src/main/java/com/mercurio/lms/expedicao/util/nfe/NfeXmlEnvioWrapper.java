package com.mercurio.lms.expedicao.util.nfe;

import com.mercurio.lms.expedicao.util.AliasLayoutNfse;
import com.mercurio.lms.layoutNfse.model.rps.Rps;
import com.thoughtworks.xstream.XStream;

public class NfeXmlEnvioWrapper {
	
	private final Rps rps;
	
	public NfeXmlEnvioWrapper(Rps rps) {
		super();
		this.rps = rps;
	}


	public String generate() {

	    XStream xstream = new XStream();
		xstream = AliasLayoutNfse.createAliasEnvio(xstream);
		

		return xstream.toXML(rps);
		
	}
	
}
