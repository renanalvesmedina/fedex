/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Informações de quantidades da Carga do CT-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfQ implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Código da Unidade de Medida 
     */
    private com.mercurio.lms.cte.model.v103.types.CUnidType _cUnid;

    /**
     * Tipo da Medida
     */
    private java.lang.String _tpMed;

    /**
     * Quantidade
     */
    private java.lang.String _qCarga;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfQ() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'cUnid'. The field 'cUnid' has
     * the following description: Código da Unidade de Medida 
     * 
     * @return the value of field 'CUnid'.
     */
    public com.mercurio.lms.cte.model.v103.types.CUnidType getCUnid(
    ) {
        return this._cUnid;
    }

    /**
     * Returns the value of field 'qCarga'. The field 'qCarga' has
     * the following description: Quantidade
     * 
     * @return the value of field 'QCarga'.
     */
    public java.lang.String getQCarga(
    ) {
        return this._qCarga;
    }

    /**
     * Returns the value of field 'tpMed'. The field 'tpMed' has
     * the following description: Tipo da Medida
     * 
     * @return the value of field 'TpMed'.
     */
    public java.lang.String getTpMed(
    ) {
        return this._tpMed;
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
     * Sets the value of field 'cUnid'. The field 'cUnid' has the
     * following description: Código da Unidade de Medida 
     * 
     * @param cUnid the value of field 'cUnid'.
     */
    public void setCUnid(
            final com.mercurio.lms.cte.model.v103.types.CUnidType cUnid) {
        this._cUnid = cUnid;
    }

    /**
     * Sets the value of field 'qCarga'. The field 'qCarga' has the
     * following description: Quantidade
     * 
     * @param qCarga the value of field 'qCarga'.
     */
    public void setQCarga(
            final java.lang.String qCarga) {
        this._qCarga = qCarga;
    }

    /**
     * Sets the value of field 'tpMed'. The field 'tpMed' has the
     * following description: Tipo da Medida
     * 
     * @param tpMed the value of field 'tpMed'.
     */
    public void setTpMed(
            final java.lang.String tpMed) {
        this._tpMed = tpMed;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v103.InfQ
     */
    public static com.mercurio.lms.cte.model.v103.InfQ unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.InfQ) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.InfQ.class, reader);
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
