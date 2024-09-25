package com.mercurio.lms.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.exolab.castor.xml.Serializer;
import  org.xml.sax.DocumentHandler;

public class LmsXMLSerializer implements Serializer {	  
    private org.apache.xml.serialize.Serializer _serializer;

    public LmsXMLSerializer() {
        _serializer = new XMLSerializer();
    }
    
    public void setOutputCharStream(Writer out) {
        _serializer.setOutputCharStream(out);
    }

    public DocumentHandler asDocumentHandler() throws IOException {
        return _serializer.asDocumentHandler();
    }

    public void setOutputFormat(org.exolab.castor.xml.OutputFormat format) {
        _serializer.setOutputFormat((OutputFormat) format.getFormat());
    }

    public void setOutputByteStream(OutputStream output) {
        _serializer.setOutputByteStream(output);
    }

}
