package com.mercurio.lms.mdfe.model.v300;

/**
 * Dados do CIOT
 */
@SuppressWarnings("serial")
public class InfCIOT implements java.io.Serializable {

    
    /**
     * Código Identificador da Operação de Transporte
     */
    private java.lang.String CIOT;
    
    /**
     * Número do CNPJ responsável pela geração do CIOT
     */
    private java.lang.String CNPJ;
    
    /**
     * Número do CPF responsável pela geração do CIOT
     */
    private java.lang.String CPF;
    
    public InfCIOT() {
        super();
    }

	public java.lang.String getCIOT() {
		return CIOT;
	}

	public void setCIOT(java.lang.String CIOT) {
		this.CIOT = CIOT;
	}

	public java.lang.String getCNPJ() {
		return CNPJ;
	}

	public void setCNPJ(java.lang.String cNPJ) {
		CNPJ = cNPJ;
	}
	
	public java.lang.String getCPF() {
		return CPF;
	}

	public void setCPF(java.lang.String cPF) {
		CPF = cPF;
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
     * com.mercurio.lms.mdfe.model.v300.InfCIOT
     */
    public static com.mercurio.lms.mdfe.model.v300.InfCIOT unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.InfCIOT) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.InfCIOT.class, reader);
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
