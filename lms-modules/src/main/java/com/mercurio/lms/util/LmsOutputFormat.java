package com.mercurio.lms.util;

public class LmsOutputFormat implements org.exolab.castor.xml.OutputFormat {
	private org.apache.xml.serialize.OutputFormat _outputFormat;
    
    public LmsOutputFormat() {
        _outputFormat = new org.apache.xml.serialize.OutputFormat();
    }
    
    public void setMethod(String method) {
        _outputFormat.setMethod(method);
    }

    public void setIndenting(boolean indent) {
        _outputFormat.setIndenting(indent);
    }

    public void setPreserveSpace(boolean preserveSpace) {
        _outputFormat.setPreserveSpace(preserveSpace);
    }

    public Object getFormat() {
        return _outputFormat;
    }
    
    public void setDoctype (String type1, String type2) {
        _outputFormat.setDoctype(type1, type2);
    }

    public void setOmitXMLDeclaration(boolean omitXMLDeclaration) {
        _outputFormat.setOmitXMLDeclaration(omitXMLDeclaration);
    }

    public void setOmitDocumentType(boolean omitDocumentType) {
        _outputFormat.setOmitDocumentType(omitDocumentType);
    }

    public void setEncoding(String encoding) {
        _outputFormat.setEncoding(encoding);
    }

	public void setVersion(String version) {
		_outputFormat.setVersion(version);
	}
}
