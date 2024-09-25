package com.mercurio.lms.mdfe.model.v300;

/**
 * Informações da seguradora
 */
@SuppressWarnings("serial")
public class InfResp implements java.io.Serializable {

    
    /**
     * Responsável pelo seguro
     */
    private com.mercurio.lms.mdfe.model.v300.types.RespSegType respSeg;
    
    /**
     * Número do CNPJ do responsável pelo seguro
     */
    private java.lang.String CNPJ;

    /**
     * Número do CPF do responsável pelo seguro
     */
    private java.lang.String CPF;
    
    public InfResp() {
        super();
    }

	public com.mercurio.lms.mdfe.model.v300.types.RespSegType getRespSeg() {
		return respSeg;
	}

	public void setRespSeg(com.mercurio.lms.mdfe.model.v300.types.RespSegType respSeg) {
		this.respSeg = respSeg;
	}

	public java.lang.String getCNPJ() {
		return CNPJ;
	}

	public void setCNPJ(java.lang.String cNPJ) {
		this.CNPJ = cNPJ;
	}

	public java.lang.String getCPF() {
		return CPF;
	}

	public void setCPF(java.lang.String cPF) {
		this.CPF = cPF;
	}
	
	/**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid() {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(final java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(final org.xml.sax.ContentHandler handler) throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }
	
	 /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * com.mercurio.lms.mdfe.model.v300.InfResp
     */
    public static com.mercurio.lms.mdfe.model.v300.InfResp unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.InfResp) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.InfResp.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
