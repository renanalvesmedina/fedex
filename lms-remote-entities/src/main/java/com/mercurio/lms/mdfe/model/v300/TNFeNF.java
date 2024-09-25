/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 * Tipo de Dados das Notas Fiscais Papel e Eletrônica
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TNFeNF implements java.io.Serializable {

    /**
     * Internal choice value storage
     */
    private java.lang.Object _choiceValue;

    /**
     * Informações da NF-e
     */
    private com.mercurio.lms.mdfe.model.v300.InfNFe infNFe;

    /**
     * Informações da NF mod 1 e 1A
     */
    private com.mercurio.lms.mdfe.model.v300.InfNF infNF;

    public TNFeNF() {
        super();
    }

    /**
     * Returns the value of field 'choiceValue'. The field
     * 'choiceValue' has the following description: Internal choice
     * value storage
     * 
     * @return the value of field 'ChoiceValue'.
     */
    public java.lang.Object getChoiceValue() {
        return this._choiceValue;
    }

    /**
     * Returns the value of field 'infNF'. The field 'infNF' has
     * the following description: Informações da NF mod 1 e 1A
     * 
     * @return the value of field 'InfNF'.
     */
    public com.mercurio.lms.mdfe.model.v300.InfNF getInfNF() {
        return this.infNF;
    }

    /**
     * Returns the value of field 'infNFe'. The field 'infNFe' has
     * the following description: Informações da NF-e
     * 
     * @return the value of field 'InfNFe'.
     */
    public com.mercurio.lms.mdfe.model.v300.InfNFe getInfNFe() {
        return this.infNFe;
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
     * Sets the value of field 'infNF'. The field 'infNF' has the
     * following description: Informações da NF mod 1 e 1A
     * 
     * @param infNF the value of field 'infNF'.
     */
    public void setInfNF(final com.mercurio.lms.mdfe.model.v300.InfNF infNF) {
        this.infNF = infNF;
        this._choiceValue = infNF;
    }

    /**
     * Sets the value of field 'infNFe'. The field 'infNFe' has the
     * following description: Informações da NF-e
     * 
     * @param infNFe the value of field 'infNFe'.
     */
    public void setInfNFe(final com.mercurio.lms.mdfe.model.v300.InfNFe infNFe) {
        this.infNFe = infNFe;
        this._choiceValue = infNFe;
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
     * com.mercurio.lms.mdfe.model.v300.TNFeNF
     */
    public static com.mercurio.lms.mdfe.model.v300.TNFeNF unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.TNFeNF) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.TNFeNF.class, reader);
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
