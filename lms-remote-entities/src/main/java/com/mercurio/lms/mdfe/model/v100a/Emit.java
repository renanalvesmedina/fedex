/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100a;

/**
 * Identificação do Emitente do Manifesto
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Emit implements java.io.Serializable {

    /**
     * CNPJ do emitente
     */
    private java.lang.String CNPJ;

    /**
     * Inscrição Estadual do emitemte
     */
    private java.lang.String IE;

    /**
     * Razão social ou Nome do emitente
     */
    private java.lang.String xNome;

    /**
     * Nome fantasia do emitente
     */
    private java.lang.String xFant;

    /**
     * Endereço do emitente
     */
    private com.mercurio.lms.mdfe.model.v100a.EnderEmit enderEmit;

    public Emit() {
        super();
    }

    /**
     * Returns the value of field 'CNPJ'. The field 'CNPJ' has the
     * following description: CNPJ do emitente
     * 
     * @return the value of field 'CNPJ'.
     */
    public java.lang.String getCNPJ() {
        return this.CNPJ;
    }

    /**
     * Returns the value of field 'enderEmit'. The field
     * 'enderEmit' has the following description: Endereço do
     * emitente
     * 
     * @return the value of field 'EnderEmit'.
     */
    public com.mercurio.lms.mdfe.model.v100a.EnderEmit getEnderEmit() {
        return this.enderEmit;
    }

    /**
     * Returns the value of field 'IE'. The field 'IE' has the
     * following description: Inscrição Estadual do emitemte
     * 
     * @return the value of field 'IE'.
     */
    public java.lang.String getIE() {
        return this.IE;
    }

    /**
     * Returns the value of field 'xFant'. The field 'xFant' has
     * the following description: Nome fantasia do emitente
     * 
     * @return the value of field 'XFant'.
     */
    public java.lang.String getXFant() {
        return this.xFant;
    }

    /**
     * Returns the value of field 'xNome'. The field 'xNome' has
     * the following description: Razão social ou Nome do emitente
     * 
     * @return the value of field 'XNome'.
     */
    public java.lang.String getXNome() {
        return this.xNome;
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
     * Sets the value of field 'CNPJ'. The field 'CNPJ' has the
     * following description: CNPJ do emitente
     * 
     * @param CNPJ the value of field 'CNPJ'.
     */
    public void setCNPJ(final java.lang.String CNPJ) {
        this.CNPJ = CNPJ;
    }

    /**
     * Sets the value of field 'enderEmit'. The field 'enderEmit'
     * has the following description: Endereço do emitente
     * 
     * @param enderEmit the value of field 'enderEmit'.
     */
    public void setEnderEmit(final com.mercurio.lms.mdfe.model.v100a.EnderEmit enderEmit) {
        this.enderEmit = enderEmit;
    }

    /**
     * Sets the value of field 'IE'. The field 'IE' has the
     * following description: Inscrição Estadual do emitemte
     * 
     * @param IE the value of field 'IE'.
     */
    public void setIE(final java.lang.String IE) {
        this.IE = IE;
    }

    /**
     * Sets the value of field 'xFant'. The field 'xFant' has the
     * following description: Nome fantasia do emitente
     * 
     * @param xFant the value of field 'xFant'.
     */
    public void setXFant(final java.lang.String xFant) {
        this.xFant = xFant;
    }

    /**
     * Sets the value of field 'xNome'. The field 'xNome' has the
     * following description: Razão social ou Nome do emitente
     * 
     * @param xNome the value of field 'xNome'.
     */
    public void setXNome(final java.lang.String xNome) {
        this.xNome = xNome;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.mdfe.model.v100a.Emi
     */
    public static com.mercurio.lms.mdfe.model.v100a.Emit unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100a.Emit) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100a.Emit.class, reader);
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
