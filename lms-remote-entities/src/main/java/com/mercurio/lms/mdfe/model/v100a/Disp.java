/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100a;

/**
 * Informações dos dispositivos do Vale Pedágio
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Disp implements java.io.Serializable {

    /**
     * CNPJ da empresa fornecedora do Vale-Pedágio
     */
    private java.lang.String CNPJForn;

    /**
     * CNPJ do responsável pelo pagamento do Vale-Pedágio
     */
    private java.lang.String CNPJPg;

    /**
     * Número do comprovante de compra
     */
    private java.lang.String nCompra;

    public Disp() {
        super();
    }

    /**
     * Returns the value of field 'CNPJForn'. The field 'CNPJForn'
     * has the following description: CNPJ da empresa fornecedora
     * do Vale-Pedágio
     * 
     * @return the value of field 'CNPJForn'.
     */
    public java.lang.String getCNPJForn() {
        return this.CNPJForn;
    }

    /**
     * Returns the value of field 'CNPJPg'. The field 'CNPJPg' has
     * the following description: CNPJ do responsável pelo
     * pagamento do Vale-Pedágio
     * 
     * @return the value of field 'CNPJPg'.
     */
    public java.lang.String getCNPJPg() {
        return this.CNPJPg;
    }

    /**
     * Returns the value of field 'nCompra'. The field 'nCompra'
     * has the following description: Número do comprovante de
     * compra
     * 
     * @return the value of field 'NCompra'.
     */
    public java.lang.String getNCompra() {
        return this.nCompra;
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
     * Sets the value of field 'CNPJForn'. The field 'CNPJForn' has
     * the following description: CNPJ da empresa fornecedora do
     * Vale-Pedágio
     * 
     * @param CNPJForn the value of field 'CNPJForn'.
     */
    public void setCNPJForn(final java.lang.String CNPJForn) {
        this.CNPJForn = CNPJForn;
    }

    /**
     * Sets the value of field 'CNPJPg'. The field 'CNPJPg' has the
     * following description: CNPJ do responsável pelo pagamento
     * do Vale-Pedágio
     * 
     * @param CNPJPg the value of field 'CNPJPg'.
     */
    public void setCNPJPg(final java.lang.String CNPJPg) {
        this.CNPJPg = CNPJPg;
    }

    /**
     * Sets the value of field 'nCompra'. The field 'nCompra' has
     * the following description: Número do comprovante de compra
     * 
     * @param nCompra the value of field 'nCompra'.
     */
    public void setNCompra(final java.lang.String nCompra) {
        this.nCompra = nCompra;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.mdfe.model.v100a.Dis
     */
    public static com.mercurio.lms.mdfe.model.v100a.Disp unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100a.Disp) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100a.Disp.class, reader);
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
