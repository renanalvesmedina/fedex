/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v200;

/**
 * ICMS devido à UF de origem da prestação, quando diferente da
 * UF do emitente
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class ICMSOutraUF implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Classificação Tributária do Serviço
     */
    private com.mercurio.lms.cte.model.v200.types.CSTType _CST;

    /**
     * Percentual de redução da BC
     */
    private java.lang.String _pRedBCOutraUF;

    /**
     * Valor da BC do ICMS
     */
    private java.lang.String _vBCOutraUF;

    /**
     * Alíquota do ICMS
     */
    private java.lang.String _pICMSOutraUF;

    /**
     * Valor do ICMS devido outra UF
     */
    private java.lang.String _vICMSOutraUF;


      //----------------/
     //- Constructors -/
    //----------------/

    public ICMSOutraUF() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'CST'. The field 'CST' has the
     * following description: Classificação Tributária do
     * Serviço
     * 
     * @return the value of field 'CST'.
     */
    public com.mercurio.lms.cte.model.v200.types.CSTType getCST(
    ) {
        return this._CST;
    }

    /**
     * Returns the value of field 'pICMSOutraUF'. The field
     * 'pICMSOutraUF' has the following description: Alíquota do
     * ICMS
     * 
     * @return the value of field 'PICMSOutraUF'.
     */
    public java.lang.String getPICMSOutraUF(
    ) {
        return this._pICMSOutraUF;
    }

    /**
     * Returns the value of field 'pRedBCOutraUF'. The field
     * 'pRedBCOutraUF' has the following description: Percentual de
     * redução da BC
     * 
     * @return the value of field 'PRedBCOutraUF'.
     */
    public java.lang.String getPRedBCOutraUF(
    ) {
        return this._pRedBCOutraUF;
    }

    /**
     * Returns the value of field 'vBCOutraUF'. The field
     * 'vBCOutraUF' has the following description: Valor da BC do
     * ICMS
     * 
     * @return the value of field 'VBCOutraUF'.
     */
    public java.lang.String getVBCOutraUF(
    ) {
        return this._vBCOutraUF;
    }

    /**
     * Returns the value of field 'vICMSOutraUF'. The field
     * 'vICMSOutraUF' has the following description: Valor do ICMS
     * devido outra UF
     * 
     * @return the value of field 'VICMSOutraUF'.
     */
    public java.lang.String getVICMSOutraUF(
    ) {
        return this._vICMSOutraUF;
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
     * Sets the value of field 'CST'. The field 'CST' has the
     * following description: Classificação Tributária do
     * Serviço
     * 
     * @param CST the value of field 'CST'.
     */
    public void setCST(
            final com.mercurio.lms.cte.model.v200.types.CSTType CST) {
        this._CST = CST;
    }

    /**
     * Sets the value of field 'pICMSOutraUF'. The field
     * 'pICMSOutraUF' has the following description: Alíquota do
     * ICMS
     * 
     * @param pICMSOutraUF the value of field 'pICMSOutraUF'.
     */
    public void setPICMSOutraUF(
            final java.lang.String pICMSOutraUF) {
        this._pICMSOutraUF = pICMSOutraUF;
    }

    /**
     * Sets the value of field 'pRedBCOutraUF'. The field
     * 'pRedBCOutraUF' has the following description: Percentual de
     * redução da BC
     * 
     * @param pRedBCOutraUF the value of field 'pRedBCOutraUF'.
     */
    public void setPRedBCOutraUF(
            final java.lang.String pRedBCOutraUF) {
        this._pRedBCOutraUF = pRedBCOutraUF;
    }

    /**
     * Sets the value of field 'vBCOutraUF'. The field 'vBCOutraUF'
     * has the following description: Valor da BC do ICMS
     * 
     * @param vBCOutraUF the value of field 'vBCOutraUF'.
     */
    public void setVBCOutraUF(
            final java.lang.String vBCOutraUF) {
        this._vBCOutraUF = vBCOutraUF;
    }

    /**
     * Sets the value of field 'vICMSOutraUF'. The field
     * 'vICMSOutraUF' has the following description: Valor do ICMS
     * devido outra UF
     * 
     * @param vICMSOutraUF the value of field 'vICMSOutraUF'.
     */
    public void setVICMSOutraUF(
            final java.lang.String vICMSOutraUF) {
        this._vICMSOutraUF = vICMSOutraUF;
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
     * com.mercurio.lms.cte.model.v200.ICMSOutraUF
     */
    public static com.mercurio.lms.cte.model.v200.ICMSOutraUF unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v200.ICMSOutraUF) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v200.ICMSOutraUF.class, reader);
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
