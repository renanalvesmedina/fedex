/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Informações das NFEste grupo deve ser informado quando o
 * documento originário for NF 
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfNF implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Número do Romaneio da NF
     */
    private String _nRoma;

    /**
     * Número do Pedido da NF
     */
    private String _nPed;

    /**
     * Modelo da Nota Fiscal
     */
    private com.mercurio.lms.cte.model.v400.types.TModNF _mod;

    /**
     * Série
     */
    private String _serie;

    /**
     * Número
     */
    private String _nDoc;

    /**
     * Data de Emissão
     */
    private String _dEmi;

    /**
     * Valor da Base de Cálculo do ICMS
     */
    private String _vBC;

    /**
     * Valor Total do ICMS
     */
    private String _vICMS;

    /**
     * Valor da Base de Cálculo do ICMS ST
     */
    private String _vBCST;

    /**
     * Valor Total do ICMS ST
     */
    private String _vST;

    /**
     * Valor Total dos Produtos
     */
    private String _vProd;

    /**
     * Valor Total da NF
     */
    private String _vNF;

    /**
     * CFOP Predominante
     */
    private String _nCFOP;

    /**
     * Peso total em Kg
     */
    private String _nPeso;

    /**
     * PIN SUFRAMA
     */
    private String _PIN;

    /**
     * Data prevista de entrega
     */
    private String _dPrev;

    /**
     * Field _infNFChoice.
     */
    private InfNFChoice _infNFChoice;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfNF() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dEmi'. The field 'dEmi' has the
     * following description: Data de Emissão
     *
     * @return the value of field 'DEmi'.
     */
    public String getDEmi(
    ) {
        return this._dEmi;
    }

    /**
     * Returns the value of field 'dPrev'. The field 'dPrev' has
     * the following description: Data prevista de entrega
     *
     * @return the value of field 'DPrev'.
     */
    public String getDPrev(
    ) {
        return this._dPrev;
    }

    /**
     * Returns the value of field 'infNFChoice'.
     *
     * @return the value of field 'InfNFChoice'.
     */
    public InfNFChoice getInfNFChoice(
    ) {
        return this._infNFChoice;
    }

    /**
     * Returns the value of field 'mod'. The field 'mod' has the
     * following description: Modelo da Nota Fiscal
     *
     * @return the value of field 'Mod'.
     */
    public com.mercurio.lms.cte.model.v400.types.TModNF getMod(
    ) {
        return this._mod;
    }

    /**
     * Returns the value of field 'nCFOP'. The field 'nCFOP' has
     * the following description: CFOP Predominante
     *
     * @return the value of field 'NCFOP'.
     */
    public String getNCFOP(
    ) {
        return this._nCFOP;
    }

    /**
     * Returns the value of field 'nDoc'. The field 'nDoc' has the
     * following description: Número
     *
     * @return the value of field 'NDoc'.
     */
    public String getNDoc(
    ) {
        return this._nDoc;
    }

    /**
     * Returns the value of field 'nPed'. The field 'nPed' has the
     * following description: Número do Pedido da NF
     *
     * @return the value of field 'NPed'.
     */
    public String getNPed(
    ) {
        return this._nPed;
    }

    /**
     * Returns the value of field 'nPeso'. The field 'nPeso' has
     * the following description: Peso total em Kg
     *
     * @return the value of field 'NPeso'.
     */
    public String getNPeso(
    ) {
        return this._nPeso;
    }

    /**
     * Returns the value of field 'nRoma'. The field 'nRoma' has
     * the following description: Número do Romaneio da NF
     *
     * @return the value of field 'NRoma'.
     */
    public String getNRoma(
    ) {
        return this._nRoma;
    }

    /**
     * Returns the value of field 'PIN'. The field 'PIN' has the
     * following description: PIN SUFRAMA
     *
     * @return the value of field 'PIN'.
     */
    public String getPIN(
    ) {
        return this._PIN;
    }

    /**
     * Returns the value of field 'serie'. The field 'serie' has
     * the following description: Série
     *
     * @return the value of field 'Serie'.
     */
    public String getSerie(
    ) {
        return this._serie;
    }

    /**
     * Returns the value of field 'vBC'. The field 'vBC' has the
     * following description: Valor da Base de Cálculo do ICMS
     *
     * @return the value of field 'VBC'.
     */
    public String getVBC(
    ) {
        return this._vBC;
    }

    /**
     * Returns the value of field 'vBCST'. The field 'vBCST' has
     * the following description: Valor da Base de Cálculo do ICMS
     * ST
     *
     * @return the value of field 'VBCST'.
     */
    public String getVBCST(
    ) {
        return this._vBCST;
    }

    /**
     * Returns the value of field 'vICMS'. The field 'vICMS' has
     * the following description: Valor Total do ICMS
     *
     * @return the value of field 'VICMS'.
     */
    public String getVICMS(
    ) {
        return this._vICMS;
    }

    /**
     * Returns the value of field 'vNF'. The field 'vNF' has the
     * following description: Valor Total da NF
     *
     * @return the value of field 'VNF'.
     */
    public String getVNF(
    ) {
        return this._vNF;
    }

    /**
     * Returns the value of field 'vProd'. The field 'vProd' has
     * the following description: Valor Total dos Produtos
     *
     * @return the value of field 'VProd'.
     */
    public String getVProd(
    ) {
        return this._vProd;
    }

    /**
     * Returns the value of field 'vST'. The field 'vST' has the
     * following description: Valor Total do ICMS ST
     *
     * @return the value of field 'VST'.
     */
    public String getVST(
    ) {
        return this._vST;
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
     * Sets the value of field 'dEmi'. The field 'dEmi' has the
     * following description: Data de Emissão
     *
     * @param dEmi the value of field 'dEmi'.
     */
    public void setDEmi(
            final String dEmi) {
        this._dEmi = dEmi;
    }

    /**
     * Sets the value of field 'dPrev'. The field 'dPrev' has the
     * following description: Data prevista de entrega
     *
     * @param dPrev the value of field 'dPrev'.
     */
    public void setDPrev(
            final String dPrev) {
        this._dPrev = dPrev;
    }

    /**
     * Sets the value of field 'infNFChoice'.
     *
     * @param infNFChoice the value of field 'infNFChoice'.
     */
    public void setInfNFChoice(
            final InfNFChoice infNFChoice) {
        this._infNFChoice = infNFChoice;
    }

    /**
     * Sets the value of field 'mod'. The field 'mod' has the
     * following description: Modelo da Nota Fiscal
     *
     * @param mod the value of field 'mod'.
     */
    public void setMod(
            final com.mercurio.lms.cte.model.v400.types.TModNF mod) {
        this._mod = mod;
    }

    /**
     * Sets the value of field 'nCFOP'. The field 'nCFOP' has the
     * following description: CFOP Predominante
     *
     * @param nCFOP the value of field 'nCFOP'.
     */
    public void setNCFOP(
            final String nCFOP) {
        this._nCFOP = nCFOP;
    }

    /**
     * Sets the value of field 'nDoc'. The field 'nDoc' has the
     * following description: Número
     *
     * @param nDoc the value of field 'nDoc'.
     */
    public void setNDoc(
            final String nDoc) {
        this._nDoc = nDoc;
    }

    /**
     * Sets the value of field 'nPed'. The field 'nPed' has the
     * following description: Número do Pedido da NF
     *
     * @param nPed the value of field 'nPed'.
     */
    public void setNPed(
            final String nPed) {
        this._nPed = nPed;
    }

    /**
     * Sets the value of field 'nPeso'. The field 'nPeso' has the
     * following description: Peso total em Kg
     *
     * @param nPeso the value of field 'nPeso'.
     */
    public void setNPeso(
            final String nPeso) {
        this._nPeso = nPeso;
    }

    /**
     * Sets the value of field 'nRoma'. The field 'nRoma' has the
     * following description: Número do Romaneio da NF
     *
     * @param nRoma the value of field 'nRoma'.
     */
    public void setNRoma(
            final String nRoma) {
        this._nRoma = nRoma;
    }

    /**
     * Sets the value of field 'PIN'. The field 'PIN' has the
     * following description: PIN SUFRAMA
     *
     * @param PIN the value of field 'PIN'.
     */
    public void setPIN(
            final String PIN) {
        this._PIN = PIN;
    }

    /**
     * Sets the value of field 'serie'. The field 'serie' has the
     * following description: Série
     *
     * @param serie the value of field 'serie'.
     */
    public void setSerie(
            final String serie) {
        this._serie = serie;
    }

    /**
     * Sets the value of field 'vBC'. The field 'vBC' has the
     * following description: Valor da Base de Cálculo do ICMS
     *
     * @param vBC the value of field 'vBC'.
     */
    public void setVBC(
            final String vBC) {
        this._vBC = vBC;
    }

    /**
     * Sets the value of field 'vBCST'. The field 'vBCST' has the
     * following description: Valor da Base de Cálculo do ICMS ST
     *
     * @param vBCST the value of field 'vBCST'.
     */
    public void setVBCST(
            final String vBCST) {
        this._vBCST = vBCST;
    }

    /**
     * Sets the value of field 'vICMS'. The field 'vICMS' has the
     * following description: Valor Total do ICMS
     *
     * @param vICMS the value of field 'vICMS'.
     */
    public void setVICMS(
            final String vICMS) {
        this._vICMS = vICMS;
    }

    /**
     * Sets the value of field 'vNF'. The field 'vNF' has the
     * following description: Valor Total da NF
     *
     * @param vNF the value of field 'vNF'.
     */
    public void setVNF(
            final String vNF) {
        this._vNF = vNF;
    }

    /**
     * Sets the value of field 'vProd'. The field 'vProd' has the
     * following description: Valor Total dos Produtos
     *
     * @param vProd the value of field 'vProd'.
     */
    public void setVProd(
            final String vProd) {
        this._vProd = vProd;
    }

    /**
     * Sets the value of field 'vST'. The field 'vST' has the
     * following description: Valor Total do ICMS ST
     *
     * @param vST the value of field 'vST'.
     */
    public void setVST(
            final String vST) {
        this._vST = vST;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v400.InfNF
     */
    public static InfNF unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (InfNF) org.exolab.castor.xml.Unmarshaller.unmarshal(InfNF.class, reader);
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
