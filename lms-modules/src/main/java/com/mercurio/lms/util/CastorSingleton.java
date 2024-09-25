package com.mercurio.lms.util;

import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.UnmarshalHandler;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.XMLContext;
import org.exolab.castor.xml.parsing.NamespaceHandling;

public class CastorSingleton {
	
	private static CastorSingleton instance;
	
	private final XMLContext context;
	
	private CastorSingleton() throws ResolverException {
		context = new XMLContext();
		context.addPackage("com.mercurio.lms.cte.model.v103");
		context.addPackage("com.mercurio.lms.cte.model.v103.types");
		context.addPackage("com.mercurio.lms.cte.model.v104");
		context.addPackage("com.mercurio.lms.cte.model.v104.types");
		context.addPackage("com.mercurio.lms.cte.model.v200");
		context.addPackage("com.mercurio.lms.cte.model.v200.types");
	}
	
	public static CastorSingleton getInstance() throws ResolverException {
		if (instance == null) {
			instance = new CastorSingleton();
		}
		return instance;
	}

	public XMLContext getContext() {
		return context;
	}
	
	public Unmarshaller createUnmarshaller(){
		Unmarshaller unmarshaller = new Unmarshaller(context.getInternalContext()){
	
			public UnmarshalHandler createHandler() {
				UnmarshalHandler uh = super.createHandler();
				NamespaceHandling nh = uh.getNamespaceHandling();
				//bug no Castor: http://jira.codehaus.org/browse/CASTOR-3093
				//deverá ser corrigida na versão 1.3.3
				nh.addDefaultNamespace("http://www.portalfiscal.inf.br/cte");
				nh.createNamespace();
				nh.addDefaultNamespace("http://www.portalfiscal.inf.br/cte");
				nh.createNamespace();
				nh.addDefaultNamespace("http://www.portalfiscal.inf.br/cte");
				nh.createNamespace();
				nh.addDefaultNamespace("http://www.portalfiscal.inf.br/cte");
				nh.createNamespace();
				nh.addDefaultNamespace("http://www.portalfiscal.inf.br/cte");
				nh.createNamespace();
				nh.addDefaultNamespace("http://www.portalfiscal.inf.br/cte");
				nh.createNamespace();
				nh.addDefaultNamespace("http://www.portalfiscal.inf.br/cte");
				nh.createNamespace();
				nh.addDefaultNamespace("http://www.portalfiscal.inf.br/cte");
				nh.createNamespace();
				nh.addDefaultNamespace("http://www.portalfiscal.inf.br/cte");
				nh.createNamespace();
				nh.addDefaultNamespace("http://www.portalfiscal.inf.br/cte");
				nh.createNamespace();
				nh.addDefaultNamespace("http://www.portalfiscal.inf.br/cte");
				nh.createNamespace();
				nh.addDefaultNamespace("http://www.portalfiscal.inf.br/cte");
				nh.createNamespace();
				nh.addDefaultNamespace("http://www.portalfiscal.inf.br/cte");
				nh.createNamespace();
	
				
				
				return uh;
}
		};
		return unmarshaller;
	}
	
	
	
}
