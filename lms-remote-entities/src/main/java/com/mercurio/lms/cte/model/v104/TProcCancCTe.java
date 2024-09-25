/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Tipo Pedido de Cancelamento de CT-e processado
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TProcCancCTe implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _versao.
     */
    private java.lang.String _versao;

    /**
     * Field _cancCTe.
     */
    private com.mercurio.lms.cte.model.v104.CancCTe _cancCTe;

    /**
     * Field _retCancCTe.
     */
    private com.mercurio.lms.cte.model.v104.RetCancCTe _retCancCTe;


      //----------------/
     //- Constructors -/
    //----------------/

    public TProcCancCTe() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'cancCTe'.
     * 
     * @return the value of field 'CancCTe'.
     */
    public com.mercurio.lms.cte.model.v104.CancCTe getCancCTe(
    ) {
        return this._cancCTe;
    }

    /**
     * Returns the value of field 'retCancCTe'.
     * 
     * @return the value of field 'RetCancCTe'.
     */
    public com.mercurio.lms.cte.model.v104.RetCancCTe getRetCancCTe(
    ) {
        return this._retCancCTe;
    }

    /**
     * Returns the value of field 'versao'.
     * 
     * @return the value of field 'Versao'.
     */
    public java.lang.String getVersao(
    ) {
        return this._versao;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
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
    public void marshal(
            final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
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
    public void marshal(
            final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'cancCTe'.
     * 
     * @param cancCTe the value of field 'cancCTe'.
     */
    public void setCancCTe(
            final com.mercurio.lms.cte.model.v104.CancCTe cancCTe) {
        this._cancCTe = cancCTe;
    }

    /**
     * Sets the value of field 'retCancCTe'.
     * 
     * @param retCancCTe the value of field 'retCancCTe'.
     */
    public void setRetCancCTe(
            final com.mercurio.lms.cte.model.v104.RetCancCTe retCancCTe) {
        this._retCancCTe = retCancCTe;
    }

    /**
     * Sets the value of field 'versao'.
     * 
     * @param versao the value of field 'versao'.
     */
    public void setVersao(
            final java.lang.String versao) {
        this._versao = versao;
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
     * com.mercurio.lms.cte.model.TProcCancCTe
     */
    public static com.mercurio.lms.cte.model.v104.TProcCancCTe unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.TProcCancCTe) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.TProcCancCTe.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate(
    )
    throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
