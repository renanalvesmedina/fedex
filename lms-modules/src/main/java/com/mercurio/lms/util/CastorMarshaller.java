package com.mercurio.lms.util;

import java.io.IOException;
import java.io.StringWriter;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.ValidationException;

public abstract class CastorMarshaller {

    public static StringBuffer marshall(Object o) throws ResolverException, IOException, MarshalException, ValidationException {
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = CastorSingleton.getInstance().getContext().createMarshaller();
        marshaller.setWriter(stringWriter);
        marshaller.setValidation(true);
        marshaller.setSuppressXSIType(true);
        marshaller.marshal(o);
        
        return stringWriter.getBuffer();
    }
    
}
