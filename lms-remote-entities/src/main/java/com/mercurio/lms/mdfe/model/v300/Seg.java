package com.mercurio.lms.mdfe.model.v300;

/**
 * Informações de Seguro da Carga
 */
@SuppressWarnings("serial")
public class Seg implements java.io.Serializable {

    /**
     * Informações do responsável pelo seguro da carga
     */
    private com.mercurio.lms.mdfe.model.v300.InfResp infResp;

    /**
     * Informações da seguradora
     */
    private com.mercurio.lms.mdfe.model.v300.InfSeg infSeg;
    
    /**
     * Número da Apólice
     */
    private java.lang.String nApol;
    
    /**
     * Número da Averbação
     */
    private java.lang.String nAver;
    
    public Seg() {
        super();
    }

	public com.mercurio.lms.mdfe.model.v300.InfResp getInfResp() {
		return infResp;
	}

	public void setInfResp(com.mercurio.lms.mdfe.model.v300.InfResp infResp) {
		this.infResp = infResp;
	}

	public com.mercurio.lms.mdfe.model.v300.InfSeg getInfSeg() {
		return infSeg;
	}

	public void setInfSeg(com.mercurio.lms.mdfe.model.v300.InfSeg infSeg) {
		this.infSeg = infSeg;
	}
	
	public java.lang.String getnApol() {
		return nApol;
	}

	public void setnApol(java.lang.String nApol) {
		this.nApol = nApol;
	}
	
	
	public java.lang.String getnAver() {
		return nAver;
	}

	public void setnAver(java.lang.String nAver) {
		this.nAver = nAver;
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
     * com.mercurio.lms.mdfe.model.v300.Seg
     */
    public static com.mercurio.lms.mdfe.model.v300.Seg unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.Seg) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.Seg.class, reader);
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
