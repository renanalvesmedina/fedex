/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 * Informações dos Municípios de Carregamento
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfMunCarrega implements java.io.Serializable {

    /**
     * Código do Município de Carregamento
     */
    private java.lang.String cMunCarrega;

    /**
     * Nome do Município de Carregamento
     */
    private java.lang.String xMunCarrega;

    public InfMunCarrega() {
        super();
    }

    /**
     * Returns the value of field 'cMunCarrega'. The field
     * 'cMunCarrega' has the following description: Código do
     * Município de Carregamento
     * 
     * @return the value of field 'CMunCarrega'.
     */
    public java.lang.String getCMunCarrega() {
        return this.cMunCarrega;
    }

    /**
     * Returns the value of field 'xMunCarrega'. The field
     * 'xMunCarrega' has the following description: Nome do
     * Município de Carregamento
     * 
     * @return the value of field 'XMunCarrega'.
     */
    public java.lang.String getXMunCarrega() {
        return this.xMunCarrega;
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
     * Sets the value of field 'cMunCarrega'. The field
     * 'cMunCarrega' has the following description: Código do
     * Município de Carregamento
     * 
     * @param cMunCarrega the value of field 'cMunCarrega'.
     */
    public void setCMunCarrega(final java.lang.String cMunCarrega) {
        this.cMunCarrega = cMunCarrega;
    }

    /**
     * Sets the value of field 'xMunCarrega'. The field
     * 'xMunCarrega' has the following description: Nome do
     * Município de Carregamento
     * 
     * @param xMunCarrega the value of field 'xMunCarrega'.
     */
    public void setXMunCarrega(final java.lang.String xMunCarrega) {
        this.xMunCarrega = xMunCarrega;
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
     * com.mercurio.lms.mdfe.model.v300.InfMunCarrega
     */
    public static com.mercurio.lms.mdfe.model.v300.InfMunCarrega unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.InfMunCarrega) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.InfMunCarrega.class, reader);
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
