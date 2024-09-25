/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v300;

/**
 * Informações do ICMS de partilha com a UF de término do
 * serviço de transporte na operação interestadualGrupo a ser
 * informado nas prestações interestaduais para consumidor final,
 * não contribuinte do ICMS
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class ICMSUFFim implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Valor da BC do ICMS na UF de término da prestação do
     * serviço de transporte
     */
    private java.lang.String _vBCUFFim;

    /**
     * Percentual do ICMS relativo ao Fundo de Combate à pobreza
     * (FCP) na UF de término da prestação do serviço de
     * transporte
     */
    private java.lang.String _pFCPUFFim;

    /**
     * Alíquota interna da UF de término da prestação do
     * serviço de transporte
     */
    private java.lang.String _pICMSUFFim;

    /**
     * Alíquota interestadual das UF envolvidas
     */
    private java.lang.String _pICMSInter;

    /**
     * Percentual provisório de partilha entre os estados
     */
    private java.lang.String _pICMSInterPart;

    /**
     * Valor do ICMS relativo ao Fundo de Combate á Pobreza (FCP)
     * da UF de término da prestação
     */
    private java.lang.String _vFCPUFFim;

    /**
     * Valor do ICMS de partilha para a UF de término da
     * prestação do serviço de transporte
     */
    private java.lang.String _vICMSUFFim;

    /**
     * Valor do ICMS de partilha para a UF de início da
     * prestação do serviço de transporte
     */
    private java.lang.String _vICMSUFIni;


      //----------------/
     //- Constructors -/
    //----------------/

    public ICMSUFFim() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'pFCPUFFim'. The field
     * 'pFCPUFFim' has the following description: Percentual do
     * ICMS relativo ao Fundo de Combate à pobreza (FCP) na UF de
     * término da prestação do serviço de transporte
     * 
     * @return the value of field 'PFCPUFFim'.
     */
    public java.lang.String getPFCPUFFim(
    ) {
        return this._pFCPUFFim;
    }

    /**
     * Returns the value of field 'pICMSInter'. The field
     * 'pICMSInter' has the following description: Alíquota
     * interestadual das UF envolvidas
     * 
     * @return the value of field 'PICMSInter'.
     */
    public java.lang.String getPICMSInter(
    ) {
        return this._pICMSInter;
    }

    /**
     * Returns the value of field 'pICMSInterPart'. The field
     * 'pICMSInterPart' has the following description: Percentual
     * provisório de partilha entre os estados
     * 
     * @return the value of field 'PICMSInterPart'.
     */
    public java.lang.String getPICMSInterPart(
    ) {
        return this._pICMSInterPart;
    }

    /**
     * Returns the value of field 'pICMSUFFim'. The field
     * 'pICMSUFFim' has the following description: Alíquota
     * interna da UF de término da prestação do serviço de
     * transporte
     * 
     * @return the value of field 'PICMSUFFim'.
     */
    public java.lang.String getPICMSUFFim(
    ) {
        return this._pICMSUFFim;
    }

    /**
     * Returns the value of field 'vBCUFFim'. The field 'vBCUFFim'
     * has the following description: Valor da BC do ICMS na UF de
     * término da prestação do serviço de transporte
     * 
     * @return the value of field 'VBCUFFim'.
     */
    public java.lang.String getVBCUFFim(
    ) {
        return this._vBCUFFim;
    }

    /**
     * Returns the value of field 'vFCPUFFim'. The field
     * 'vFCPUFFim' has the following description: Valor do ICMS
     * relativo ao Fundo de Combate á Pobreza (FCP) da UF de
     * término da prestação
     * 
     * @return the value of field 'VFCPUFFim'.
     */
    public java.lang.String getVFCPUFFim(
    ) {
        return this._vFCPUFFim;
    }

    /**
     * Returns the value of field 'vICMSUFFim'. The field
     * 'vICMSUFFim' has the following description: Valor do ICMS de
     * partilha para a UF de término da prestação do serviço de
     * transporte
     * 
     * @return the value of field 'VICMSUFFim'.
     */
    public java.lang.String getVICMSUFFim(
    ) {
        return this._vICMSUFFim;
    }

    /**
     * Returns the value of field 'vICMSUFIni'. The field
     * 'vICMSUFIni' has the following description: Valor do ICMS de
     * partilha para a UF de início da prestação do serviço de
     * transporte
     * 
     * @return the value of field 'VICMSUFIni'.
     */
    public java.lang.String getVICMSUFIni(
    ) {
        return this._vICMSUFIni;
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
     * Sets the value of field 'pFCPUFFim'. The field 'pFCPUFFim'
     * has the following description: Percentual do ICMS relativo
     * ao Fundo de Combate à pobreza (FCP) na UF de término da
     * prestação do serviço de transporte
     * 
     * @param pFCPUFFim the value of field 'pFCPUFFim'.
     */
    public void setPFCPUFFim(
            final java.lang.String pFCPUFFim) {
        this._pFCPUFFim = pFCPUFFim;
    }

    /**
     * Sets the value of field 'pICMSInter'. The field 'pICMSInter'
     * has the following description: Alíquota interestadual das
     * UF envolvidas
     * 
     * @param pICMSInter the value of field 'pICMSInter'.
     */
    public void setPICMSInter(
            final java.lang.String pICMSInter) {
        this._pICMSInter = pICMSInter;
    }

    /**
     * Sets the value of field 'pICMSInterPart'. The field
     * 'pICMSInterPart' has the following description: Percentual
     * provisório de partilha entre os estados
     * 
     * @param pICMSInterPart the value of field 'pICMSInterPart'.
     */
    public void setPICMSInterPart(
            final java.lang.String pICMSInterPart) {
        this._pICMSInterPart = pICMSInterPart;
    }

    /**
     * Sets the value of field 'pICMSUFFim'. The field 'pICMSUFFim'
     * has the following description: Alíquota interna da UF de
     * término da prestação do serviço de transporte
     * 
     * @param pICMSUFFim the value of field 'pICMSUFFim'.
     */
    public void setPICMSUFFim(
            final java.lang.String pICMSUFFim) {
        this._pICMSUFFim = pICMSUFFim;
    }

    /**
     * Sets the value of field 'vBCUFFim'. The field 'vBCUFFim' has
     * the following description: Valor da BC do ICMS na UF de
     * término da prestação do serviço de transporte
     * 
     * @param vBCUFFim the value of field 'vBCUFFim'.
     */
    public void setVBCUFFim(
            final java.lang.String vBCUFFim) {
        this._vBCUFFim = vBCUFFim;
    }

    /**
     * Sets the value of field 'vFCPUFFim'. The field 'vFCPUFFim'
     * has the following description: Valor do ICMS relativo ao
     * Fundo de Combate á Pobreza (FCP) da UF de término da
     * prestação
     * 
     * @param vFCPUFFim the value of field 'vFCPUFFim'.
     */
    public void setVFCPUFFim(
            final java.lang.String vFCPUFFim) {
        this._vFCPUFFim = vFCPUFFim;
    }

    /**
     * Sets the value of field 'vICMSUFFim'. The field 'vICMSUFFim'
     * has the following description: Valor do ICMS de partilha
     * para a UF de término da prestação do serviço de
     * transporte
     * 
     * @param vICMSUFFim the value of field 'vICMSUFFim'.
     */
    public void setVICMSUFFim(
            final java.lang.String vICMSUFFim) {
        this._vICMSUFFim = vICMSUFFim;
    }

    /**
     * Sets the value of field 'vICMSUFIni'. The field 'vICMSUFIni'
     * has the following description: Valor do ICMS de partilha
     * para a UF de início da prestação do serviço de
     * transporte
     * 
     * @param vICMSUFIni the value of field 'vICMSUFIni'.
     */
    public void setVICMSUFIni(
            final java.lang.String vICMSUFIni) {
        this._vICMSUFIni = vICMSUFIni;
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
     * com.mercurio.lms.cte.model.v300.ICMSUFFim
     */
    public static com.mercurio.lms.cte.model.v300.ICMSUFFim unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v300.ICMSUFFim) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v300.ICMSUFFim.class, reader);
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
