/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100a;

/**
 * Autorizados para download do XML do DF-eInformar CNPJ ou CPF.
 * Preencher os zeros não significativos.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class AutXML implements java.io.Serializable {

    /**
     * Internal choice value storage
     */
    private java.lang.Object _choiceValue;

    /**
     * CNPJ do autorizado
     */
    private java.lang.String CNPJ;

    /**
     * CPF do autorizado
     */
    private java.lang.String CPF;

    public AutXML() {
        super();
    }

    /**
     * Returns the value of field 'CNPJ'. The field 'CNPJ' has the
     * following description: CNPJ do autorizado
     * 
     * @return the value of field 'CNPJ'.
     */
    public java.lang.String getCNPJ() {
        return this.CNPJ;
    }

    /**
     * Returns the value of field 'CPF'. The field 'CPF' has the
     * following description: CPF do autorizado
     * 
     * @return the value of field 'CPF'.
     */
    public java.lang.String getCPF() {
        return this.CPF;
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
     * following description: CNPJ do autorizado
     * 
     * @param CNPJ the value of field 'CNPJ'.
     */
    public void setCNPJ(final java.lang.String CNPJ) {
        this.CNPJ = CNPJ;
        this._choiceValue = CNPJ;
    }

    /**
     * Sets the value of field 'CPF'. The field 'CPF' has the
     * following description: CPF do autorizado
     * 
     * @param CPF the value of field 'CPF'.
     */
    public void setCPF(final java.lang.String CPF) {
        this.CPF = CPF;
        this._choiceValue = CPF;
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
     * com.mercurio.lms.mdfe.model.v100a.AutXML
     */
    public static com.mercurio.lms.mdfe.model.v100a.AutXML unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100a.AutXML) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100a.AutXML.class, reader);
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
