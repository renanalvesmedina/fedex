/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v200;

/**
 * Tributação pelo ICMS60 - ICMS cobrado por substituição
 * tributária.Responsabilidade do recolhimento do ICMS atribuído
 * ao tomador ou 3º por ST
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class ICMS60 implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Classificação Tributária do Serviço
     */
    private com.mercurio.lms.cte.model.v200.types.CSTType _CST;

    /**
     * Valor da BC do ICMS ST retido
     */
    private java.lang.String _vBCSTRet;

    /**
     * Valor do ICMS ST retido
     */
    private java.lang.String _vICMSSTRet;

    /**
     * Alíquota do ICMS
     */
    private java.lang.String _pICMSSTRet;

    /**
     * Valor do Crédito outorgado/Presumido
     */
    private java.lang.String _vCred;


      //----------------/
     //- Constructors -/
    //----------------/

    public ICMS60() {
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
     * Returns the value of field 'pICMSSTRet'. The field
     * 'pICMSSTRet' has the following description: Alíquota do
     * ICMS
     * 
     * @return the value of field 'PICMSSTRet'.
     */
    public java.lang.String getPICMSSTRet(
    ) {
        return this._pICMSSTRet;
    }

    /**
     * Returns the value of field 'vBCSTRet'. The field 'vBCSTRet'
     * has the following description: Valor da BC do ICMS ST retido
     * 
     * @return the value of field 'VBCSTRet'.
     */
    public java.lang.String getVBCSTRet(
    ) {
        return this._vBCSTRet;
    }

    /**
     * Returns the value of field 'vCred'. The field 'vCred' has
     * the following description: Valor do Crédito
     * outorgado/Presumido
     * 
     * @return the value of field 'VCred'.
     */
    public java.lang.String getVCred(
    ) {
        return this._vCred;
    }

    /**
     * Returns the value of field 'vICMSSTRet'. The field
     * 'vICMSSTRet' has the following description: Valor do ICMS ST
     * retido
     * 
     * @return the value of field 'VICMSSTRet'.
     */
    public java.lang.String getVICMSSTRet(
    ) {
        return this._vICMSSTRet;
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
     * Sets the value of field 'pICMSSTRet'. The field 'pICMSSTRet'
     * has the following description: Alíquota do ICMS
     * 
     * @param pICMSSTRet the value of field 'pICMSSTRet'.
     */
    public void setPICMSSTRet(
            final java.lang.String pICMSSTRet) {
        this._pICMSSTRet = pICMSSTRet;
    }

    /**
     * Sets the value of field 'vBCSTRet'. The field 'vBCSTRet' has
     * the following description: Valor da BC do ICMS ST retido
     * 
     * @param vBCSTRet the value of field 'vBCSTRet'.
     */
    public void setVBCSTRet(
            final java.lang.String vBCSTRet) {
        this._vBCSTRet = vBCSTRet;
    }

    /**
     * Sets the value of field 'vCred'. The field 'vCred' has the
     * following description: Valor do Crédito outorgado/Presumido
     * 
     * @param vCred the value of field 'vCred'.
     */
    public void setVCred(
            final java.lang.String vCred) {
        this._vCred = vCred;
    }

    /**
     * Sets the value of field 'vICMSSTRet'. The field 'vICMSSTRet'
     * has the following description: Valor do ICMS ST retido
     * 
     * @param vICMSSTRet the value of field 'vICMSSTRet'.
     */
    public void setVICMSSTRet(
            final java.lang.String vICMSSTRet) {
        this._vICMSSTRet = vICMSSTRet;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v200.ICMS6
     */
    public static com.mercurio.lms.cte.model.v200.ICMS60 unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v200.ICMS60) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v200.ICMS60.class, reader);
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
