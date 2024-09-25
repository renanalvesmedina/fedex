/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v200;

/**
 * Dados da fatura
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Fat implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Número da fatura
     */
    private java.lang.String _nFat;

    /**
     * Valor original da fatura
     */
    private java.lang.String _vOrig;

    /**
     * Valor do desconto da fatura
     */
    private java.lang.String _vDesc;

    /**
     * Valor líquido da fatura
     */
    private java.lang.String _vLiq;


      //----------------/
     //- Constructors -/
    //----------------/

    public Fat() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'nFat'. The field 'nFat' has the
     * following description: Número da fatura
     * 
     * @return the value of field 'NFat'.
     */
    public java.lang.String getNFat(
    ) {
        return this._nFat;
    }

    /**
     * Returns the value of field 'vDesc'. The field 'vDesc' has
     * the following description: Valor do desconto da fatura
     * 
     * @return the value of field 'VDesc'.
     */
    public java.lang.String getVDesc(
    ) {
        return this._vDesc;
    }

    /**
     * Returns the value of field 'vLiq'. The field 'vLiq' has the
     * following description: Valor líquido da fatura
     * 
     * @return the value of field 'VLiq'.
     */
    public java.lang.String getVLiq(
    ) {
        return this._vLiq;
    }

    /**
     * Returns the value of field 'vOrig'. The field 'vOrig' has
     * the following description: Valor original da fatura
     * 
     * @return the value of field 'VOrig'.
     */
    public java.lang.String getVOrig(
    ) {
        return this._vOrig;
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
     * Sets the value of field 'nFat'. The field 'nFat' has the
     * following description: Número da fatura
     * 
     * @param nFat the value of field 'nFat'.
     */
    public void setNFat(
            final java.lang.String nFat) {
        this._nFat = nFat;
    }

    /**
     * Sets the value of field 'vDesc'. The field 'vDesc' has the
     * following description: Valor do desconto da fatura
     * 
     * @param vDesc the value of field 'vDesc'.
     */
    public void setVDesc(
            final java.lang.String vDesc) {
        this._vDesc = vDesc;
    }

    /**
     * Sets the value of field 'vLiq'. The field 'vLiq' has the
     * following description: Valor líquido da fatura
     * 
     * @param vLiq the value of field 'vLiq'.
     */
    public void setVLiq(
            final java.lang.String vLiq) {
        this._vLiq = vLiq;
    }

    /**
     * Sets the value of field 'vOrig'. The field 'vOrig' has the
     * following description: Valor original da fatura
     * 
     * @param vOrig the value of field 'vOrig'.
     */
    public void setVOrig(
            final java.lang.String vOrig) {
        this._vOrig = vOrig;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v200.Fat
     */
    public static com.mercurio.lms.cte.model.v200.Fat unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v200.Fat) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v200.Fat.class, reader);
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
