package com.mercurio.lms.config;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.rest.InjectInJerseyResolver;

@ApplicationPath("/services/*")
public class LmsJerseyApplication extends ResourceConfig  {
	
	public LmsJerseyApplication() {
		super(MultiPartFeature.class/*, LoggingFilter.class*/);
		register(new AbstractBinder() {
			
			@Override
			protected void configure() {
				bind(InjectInJerseyResolver.class)
					.to(new TypeLiteral<InjectionResolver<InjectInJersey>>(){})
					.in(Singleton.class);
			}
		});
	}

}
