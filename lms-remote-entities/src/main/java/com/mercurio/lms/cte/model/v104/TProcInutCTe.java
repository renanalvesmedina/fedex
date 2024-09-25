/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Tipo Pedido de inutilzação de númeração de CT-e processado
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TProcInutCTe implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _versao.
     */
    private java.lang.String _versao;

    /**
     * Field _inutCTe.
     */
    private com.mercurio.lms.cte.model.v104.InutCTe _inutCTe;

    /**
     * Field _retInutCTe.
     */
    private com.mercurio.lms.cte.model.v104.RetInutCTe _retInutCTe;


      //----------------/
     //- Constructors -/
    //----------------/

    public TProcInutCTe() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'inutCTe'.
     * 
     * @return the value of field 'InutCTe'.
     */
    public com.mercurio.lms.cte.model.v104.InutCTe getInutCTe(
    ) {
        return this._inutCTe;
    }

    /**
     * Returns the value of field 'retInutCTe'.
     * 
     * @return the value of field 'RetInutCTe'.
     */
    public com.mercurio.lms.cte.model.v104.RetInutCTe getRetInutCTe(
    ) {
        return this._retInutCTe;
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
     * Sets the value of field 'inutCTe'.
     * 
     * @param inutCTe the value of field 'inutCTe'.
     */
    public void setInutCTe(
            final com.mercurio.lms.cte.model.v104.InutCTe inutCTe) {
        this._inutCTe = inutCTe;
    }

    /**
     * Sets the value of field 'retInutCTe'.
     * 
     * @param retInutCTe the value of field 'retInutCTe'.
     */
    public void setRetInutCTe(
            final com.mercurio.lms.cte.model.v104.RetInutCTe retInutCTe) {
        this._retInutCTe = retInutCTe;
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
     * com.mercurio.lms.cte.model.TProcInutCTe
     */
    public static com.mercurio.lms.cte.model.v104.TProcInutCTe unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.TProcInutCTe) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.TProcInutCTe.class, reader);
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
