/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v200;

/**
 * ICMS Outros
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class ICMS90 implements java.io.Serializable {


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
    private java.lang.String _pRedBC;

    /**
     * Valor da BC do ICMS
     */
    private java.lang.String _vBC;

    /**
     * Alíquota do ICMS
     */
    private java.lang.String _pICMS;

    /**
     * Valor do ICMS
     */
    private java.lang.String _vICMS;

    /**
     * Valor do Crédito Outorgado/Presumido
     */
    private java.lang.String _vCred;


      //----------------/
     //- Constructors -/
    //----------------/

    public ICMS90() {
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
     * Returns the value of field 'pICMS'. The field 'pICMS' has
     * the following description: Alíquota do ICMS
     * 
     * @return the value of field 'PICMS'.
     */
    public java.lang.String getPICMS(
    ) {
        return this._pICMS;
    }

    /**
     * Returns the value of field 'pRedBC'. The field 'pRedBC' has
     * the following description: Percentual de redução da BC
     * 
     * @return the value of field 'PRedBC'.
     */
    public java.lang.String getPRedBC(
    ) {
        return this._pRedBC;
    }

    /**
     * Returns the value of field 'vBC'. The field 'vBC' has the
     * following description: Valor da BC do ICMS
     * 
     * @return the value of field 'VBC'.
     */
    public java.lang.String getVBC(
    ) {
        return this._vBC;
    }

    /**
     * Returns the value of field 'vCred'. The field 'vCred' has
     * the following description: Valor do Crédito
     * Outorgado/Presumido
     * 
     * @return the value of field 'VCred'.
     */
    public java.lang.String getVCred(
    ) {
        return this._vCred;
    }

    /**
     * Returns the value of field 'vICMS'. The field 'vICMS' has
     * the following description: Valor do ICMS
     * 
     * @return the value of field 'VICMS'.
     */
    public java.lang.String getVICMS(
    ) {
        return this._vICMS;
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
     * Sets the value of field 'pICMS'. The field 'pICMS' has the
     * following description: Alíquota do ICMS
     * 
     * @param pICMS the value of field 'pICMS'.
     */
    public void setPICMS(
            final java.lang.String pICMS) {
        this._pICMS = pICMS;
    }

    /**
     * Sets the value of field 'pRedBC'. The field 'pRedBC' has the
     * following description: Percentual de redução da BC
     * 
     * @param pRedBC the value of field 'pRedBC'.
     */
    public void setPRedBC(
            final java.lang.String pRedBC) {
        this._pRedBC = pRedBC;
    }

    /**
     * Sets the value of field 'vBC'. The field 'vBC' has the
     * following description: Valor da BC do ICMS
     * 
     * @param vBC the value of field 'vBC'.
     */
    public void setVBC(
            final java.lang.String vBC) {
        this._vBC = vBC;
    }

    /**
     * Sets the value of field 'vCred'. The field 'vCred' has the
     * following description: Valor do Crédito Outorgado/Presumido
     * 
     * @param vCred the value of field 'vCred'.
     */
    public void setVCred(
            final java.lang.String vCred) {
        this._vCred = vCred;
    }

    /**
     * Sets the value of field 'vICMS'. The field 'vICMS' has the
     * following description: Valor do ICMS
     * 
     * @param vICMS the value of field 'vICMS'.
     */
    public void setVICMS(
            final java.lang.String vICMS) {
        this._vICMS = vICMS;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v200.ICMS9
     */
    public static com.mercurio.lms.cte.model.v200.ICMS90 unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v200.ICMS90) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v200.ICMS90.class, reader);
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
