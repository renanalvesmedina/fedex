/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 * Tipo Pedido de Concessão de Autorização de MDF-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TEnviMDFe implements java.io.Serializable {

    private java.lang.String versao;

    private java.lang.String idLote;

    private com.mercurio.lms.mdfe.model.v300.MDFe MDFe;

    public TEnviMDFe() {
        super();
    }

    /**
     * Returns the value of field 'idLote'.
     * 
     * @return the value of field 'IdLote'.
     */
    public java.lang.String getIdLote() {
        return this.idLote;
    }

    /**
     * Returns the value of field 'MDFe'.
     * 
     * @return the value of field 'MDFe'.
     */
    public com.mercurio.lms.mdfe.model.v300.MDFe getMDFe() {
        return this.MDFe;
    }

    /**
     * Returns the value of field 'versao'.
     * 
     * @return the value of field 'Versao'.
     */
    public java.lang.String getVersao() {
        return this.versao;
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
     * Sets the value of field 'idLote'.
     * 
     * @param idLote the value of field 'idLote'.
     */
    public void setIdLote(final java.lang.String idLote) {
        this.idLote = idLote;
    }

    /**
     * Sets the value of field 'MDFe'.
     * 
     * @param MDFe the value of field 'MDFe'.
     */
    public void setMDFe(final com.mercurio.lms.mdfe.model.v300.MDFe MDFe) {
        this.MDFe = MDFe;
    }

    /**
     * Sets the value of field 'versao'.
     * 
     * @param versao the value of field 'versao'.
     */
    public void setVersao(final java.lang.String versao) {
        this.versao = versao;
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
     * com.mercurio.lms.mdfe.model.v300.TEnviMDFe
     */
    public static com.mercurio.lms.mdfe.model.v300.TEnviMDFe unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.TEnviMDFe) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.TEnviMDFe.class, reader);
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
