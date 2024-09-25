/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v200;

/**
 * Identificação do CT-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Ide implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Código da UF do emitente do CT-e.
     */
    private com.mercurio.lms.cte.model.v200.types.TCodUfIBGE _cUF;

    /**
     * Código numérico que compõe a Chave de Acesso. 
     */
    private java.lang.String _cCT;

    /**
     * Código Fiscal de Operações e Prestações
     */
    private java.lang.String _CFOP;

    /**
     * Natureza da Operação
     */
    private java.lang.String _natOp;

    /**
     * Forma de pagamento do serviço TAG OBSOLETA - Será retirada
     * em versão futura
     */
    private com.mercurio.lms.cte.model.v200.types.ForPagType _forPag;

    /**
     * Modelo do documento fiscal
     */
    private com.mercurio.lms.cte.model.v200.types.TModCT _mod;

    /**
     * Série do CT-e
     */
    private java.lang.String _serie;

    /**
     * Número do CT-e
     */
    private java.lang.String _nCT;

    /**
     * Data e hora de emissão do CT-e 
     */
    private java.lang.String _dhEmi;

    /**
     * Formato de impressão do DACTE
     */
    private com.mercurio.lms.cte.model.v200.types.TpImpType _tpImp;

    /**
     * Forma de emissão do CT-e
     */
    private com.mercurio.lms.cte.model.v200.types.TpEmisType _tpEmis;

    /**
     * Digito Verificador da chave de acesso do CT-e
     */
    private java.lang.String _cDV;

    /**
     * Tipo do Ambiente
     */
    private com.mercurio.lms.cte.model.v200.types.TAmb _tpAmb;

    /**
     * Tipo do CT-e
     */
    private com.mercurio.lms.cte.model.v200.types.TFinCTe _tpCTe;

    /**
     * Identificador do processo de emissão do CT-e
     */
    private com.mercurio.lms.cte.model.v200.types.TProcEmi _procEmi;

    /**
     * Versão do processo de emissão
     */
    private java.lang.String _verProc;

    /**
     * Chave de acesso do CT-e referenciado
     */
    private java.lang.String _refCTE;

    /**
     * Código do Município de envio do CT-e (de onde o documento
     * foi transmitido)
     */
    private java.lang.String _cMunEnv;

    /**
     * Nome do Município de envio do CT-e (de onde o documento foi
     * transmitido)
     */
    private java.lang.String _xMunEnv;

    /**
     * Sigla da UF de envio do CT-e (de onde o documento foi
     * transmitido)
     */
    private com.mercurio.lms.cte.model.v200.types.TUf _UFEnv;

    /**
     * Modal
     */
    private com.mercurio.lms.cte.model.v200.types.TModTransp _modal;

    /**
     * Tipo do Serviço
     */
    private com.mercurio.lms.cte.model.v200.types.TpServType _tpServ;

    /**
     * Código do Município de início da prestação
     */
    private java.lang.String _cMunIni;

    /**
     * Nome do Município do início da prestação
     */
    private java.lang.String _xMunIni;

    /**
     * UF do início da prestação
     */
    private com.mercurio.lms.cte.model.v200.types.TUf _UFIni;

    /**
     * Código do Município de término da prestação
     */
    private java.lang.String _cMunFim;

    /**
     * Nome do Município do término da prestação
     */
    private java.lang.String _xMunFim;

    /**
     * UF do término da prestação
     */
    private com.mercurio.lms.cte.model.v200.types.TUf _UFFim;

    /**
     * Indicador se o Recebedor retira no Aeroporto, Filial, Porto
     * ou Estação de Destino?
     */
    private com.mercurio.lms.cte.model.v200.types.RetiraType _retira;

    /**
     * Detalhes do retira
     */
    private java.lang.String _xDetRetira;

    /**
     * Field _ideChoice.
     */
    private com.mercurio.lms.cte.model.v200.IdeChoice _ideChoice;

    /**
     * Informar apenas
     * para tpEmis diferente de 1
     */
    private com.mercurio.lms.cte.model.v200.IdeSequence _ideSequence;


      //----------------/
     //- Constructors -/
    //----------------/

    public Ide() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'cCT'. The field 'cCT' has the
     * following description: Código numérico que compõe a Chave
     * de Acesso. 
     * 
     * @return the value of field 'CCT'.
     */
    public java.lang.String getCCT(
    ) {
        return this._cCT;
    }

    /**
     * Returns the value of field 'cDV'. The field 'cDV' has the
     * following description: Digito Verificador da chave de acesso
     * do CT-e
     * 
     * @return the value of field 'CDV'.
     */
    public java.lang.String getCDV(
    ) {
        return this._cDV;
    }

    /**
     * Returns the value of field 'CFOP'. The field 'CFOP' has the
     * following description: Código Fiscal de Operações e
     * Prestações
     * 
     * @return the value of field 'CFOP'.
     */
    public java.lang.String getCFOP(
    ) {
        return this._CFOP;
    }

    /**
     * Returns the value of field 'cMunEnv'. The field 'cMunEnv'
     * has the following description: Código do Município de
     * envio do CT-e (de onde o documento foi transmitido)
     * 
     * @return the value of field 'CMunEnv'.
     */
    public java.lang.String getCMunEnv(
    ) {
        return this._cMunEnv;
    }

    /**
     * Returns the value of field 'cMunFim'. The field 'cMunFim'
     * has the following description: Código do Município de
     * término da prestação
     * 
     * @return the value of field 'CMunFim'.
     */
    public java.lang.String getCMunFim(
    ) {
        return this._cMunFim;
    }

    /**
     * Returns the value of field 'cMunIni'. The field 'cMunIni'
     * has the following description: Código do Município de
     * início da prestação
     * 
     * @return the value of field 'CMunIni'.
     */
    public java.lang.String getCMunIni(
    ) {
        return this._cMunIni;
    }

    /**
     * Returns the value of field 'cUF'. The field 'cUF' has the
     * following description: Código da UF do emitente do CT-e.
     * 
     * @return the value of field 'CUF'.
     */
    public com.mercurio.lms.cte.model.v200.types.TCodUfIBGE getCUF(
    ) {
        return this._cUF;
    }

    /**
     * Returns the value of field 'dhEmi'. The field 'dhEmi' has
     * the following description: Data e hora de emissão do CT-e 
     * 
     * @return the value of field 'DhEmi'.
     */
    public java.lang.String getDhEmi(
    ) {
        return this._dhEmi;
    }

    /**
     * Returns the value of field 'forPag'. The field 'forPag' has
     * the following description: Forma de pagamento do serviço
     * TAG OBSOLETA - Será retirada em versão futura
     * 
     * @return the value of field 'ForPag'.
     */
    public com.mercurio.lms.cte.model.v200.types.ForPagType getForPag(
    ) {
        return this._forPag;
    }

    /**
     * Returns the value of field 'ideChoice'.
     * 
     * @return the value of field 'IdeChoice'.
     */
    public com.mercurio.lms.cte.model.v200.IdeChoice getIdeChoice(
    ) {
        return this._ideChoice;
    }

    /**
     * Returns the value of field 'ideSequence'. The field
     * 'ideSequence' has the following description: Informar apenas
     * para tpEmis diferente de 1
     * 
     * @return the value of field 'IdeSequence'.
     */
    public com.mercurio.lms.cte.model.v200.IdeSequence getIdeSequence(
    ) {
        return this._ideSequence;
    }

    /**
     * Returns the value of field 'mod'. The field 'mod' has the
     * following description: Modelo do documento fiscal
     * 
     * @return the value of field 'Mod'.
     */
    public com.mercurio.lms.cte.model.v200.types.TModCT getMod(
    ) {
        return this._mod;
    }

    /**
     * Returns the value of field 'modal'. The field 'modal' has
     * the following description: Modal
     * 
     * @return the value of field 'Modal'.
     */
    public com.mercurio.lms.cte.model.v200.types.TModTransp getModal(
    ) {
        return this._modal;
    }

    /**
     * Returns the value of field 'nCT'. The field 'nCT' has the
     * following description: Número do CT-e
     * 
     * @return the value of field 'NCT'.
     */
    public java.lang.String getNCT(
    ) {
        return this._nCT;
    }

    /**
     * Returns the value of field 'natOp'. The field 'natOp' has
     * the following description: Natureza da Operação
     * 
     * @return the value of field 'NatOp'.
     */
    public java.lang.String getNatOp(
    ) {
        return this._natOp;
    }

    /**
     * Returns the value of field 'procEmi'. The field 'procEmi'
     * has the following description: Identificador do processo de
     * emissão do CT-e
     * 
     * @return the value of field 'ProcEmi'.
     */
    public com.mercurio.lms.cte.model.v200.types.TProcEmi getProcEmi(
    ) {
        return this._procEmi;
    }

    /**
     * Returns the value of field 'refCTE'. The field 'refCTE' has
     * the following description: Chave de acesso do CT-e
     * referenciado
     * 
     * @return the value of field 'RefCTE'.
     */
    public java.lang.String getRefCTE(
    ) {
        return this._refCTE;
    }

    /**
     * Returns the value of field 'retira'. The field 'retira' has
     * the following description: Indicador se o Recebedor retira
     * no Aeroporto, Filial, Porto ou Estação de Destino?
     * 
     * @return the value of field 'Retira'.
     */
    public com.mercurio.lms.cte.model.v200.types.RetiraType getRetira(
    ) {
        return this._retira;
    }

    /**
     * Returns the value of field 'serie'. The field 'serie' has
     * the following description: Série do CT-e
     * 
     * @return the value of field 'Serie'.
     */
    public java.lang.String getSerie(
    ) {
        return this._serie;
    }

    /**
     * Returns the value of field 'tpAmb'. The field 'tpAmb' has
     * the following description: Tipo do Ambiente
     * 
     * @return the value of field 'TpAmb'.
     */
    public com.mercurio.lms.cte.model.v200.types.TAmb getTpAmb(
    ) {
        return this._tpAmb;
    }

    /**
     * Returns the value of field 'tpCTe'. The field 'tpCTe' has
     * the following description: Tipo do CT-e
     * 
     * @return the value of field 'TpCTe'.
     */
    public com.mercurio.lms.cte.model.v200.types.TFinCTe getTpCTe(
    ) {
        return this._tpCTe;
    }

    /**
     * Returns the value of field 'tpEmis'. The field 'tpEmis' has
     * the following description: Forma de emissão do CT-e
     * 
     * @return the value of field 'TpEmis'.
     */
    public com.mercurio.lms.cte.model.v200.types.TpEmisType getTpEmis(
    ) {
        return this._tpEmis;
    }

    /**
     * Returns the value of field 'tpImp'. The field 'tpImp' has
     * the following description: Formato de impressão do DACTE
     * 
     * @return the value of field 'TpImp'.
     */
    public com.mercurio.lms.cte.model.v200.types.TpImpType getTpImp(
    ) {
        return this._tpImp;
    }

    /**
     * Returns the value of field 'tpServ'. The field 'tpServ' has
     * the following description: Tipo do Serviço
     * 
     * @return the value of field 'TpServ'.
     */
    public com.mercurio.lms.cte.model.v200.types.TpServType getTpServ(
    ) {
        return this._tpServ;
    }

    /**
     * Returns the value of field 'UFEnv'. The field 'UFEnv' has
     * the following description: Sigla da UF de envio do CT-e (de
     * onde o documento foi transmitido)
     * 
     * @return the value of field 'UFEnv'.
     */
    public com.mercurio.lms.cte.model.v200.types.TUf getUFEnv(
    ) {
        return this._UFEnv;
    }

    /**
     * Returns the value of field 'UFFim'. The field 'UFFim' has
     * the following description: UF do término da prestação
     * 
     * @return the value of field 'UFFim'.
     */
    public com.mercurio.lms.cte.model.v200.types.TUf getUFFim(
    ) {
        return this._UFFim;
    }

    /**
     * Returns the value of field 'UFIni'. The field 'UFIni' has
     * the following description: UF do início da prestação
     * 
     * @return the value of field 'UFIni'.
     */
    public com.mercurio.lms.cte.model.v200.types.TUf getUFIni(
    ) {
        return this._UFIni;
    }

    /**
     * Returns the value of field 'verProc'. The field 'verProc'
     * has the following description: Versão do processo de
     * emissão
     * 
     * @return the value of field 'VerProc'.
     */
    public java.lang.String getVerProc(
    ) {
        return this._verProc;
    }

    /**
     * Returns the value of field 'xDetRetira'. The field
     * 'xDetRetira' has the following description: Detalhes do
     * retira
     * 
     * @return the value of field 'XDetRetira'.
     */
    public java.lang.String getXDetRetira(
    ) {
        return this._xDetRetira;
    }

    /**
     * Returns the value of field 'xMunEnv'. The field 'xMunEnv'
     * has the following description: Nome do Município de envio
     * do CT-e (de onde o documento foi transmitido)
     * 
     * @return the value of field 'XMunEnv'.
     */
    public java.lang.String getXMunEnv(
    ) {
        return this._xMunEnv;
    }

    /**
     * Returns the value of field 'xMunFim'. The field 'xMunFim'
     * has the following description: Nome do Município do
     * término da prestação
     * 
     * @return the value of field 'XMunFim'.
     */
    public java.lang.String getXMunFim(
    ) {
        return this._xMunFim;
    }

    /**
     * Returns the value of field 'xMunIni'. The field 'xMunIni'
     * has the following description: Nome do Município do início
     * da prestação
     * 
     * @return the value of field 'XMunIni'.
     */
    public java.lang.String getXMunIni(
    ) {
        return this._xMunIni;
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
     * Sets the value of field 'cCT'. The field 'cCT' has the
     * following description: Código numérico que compõe a Chave
     * de Acesso. 
     * 
     * @param cCT the value of field 'cCT'.
     */
    public void setCCT(
            final java.lang.String cCT) {
        this._cCT = cCT;
    }

    /**
     * Sets the value of field 'cDV'. The field 'cDV' has the
     * following description: Digito Verificador da chave de acesso
     * do CT-e
     * 
     * @param cDV the value of field 'cDV'.
     */
    public void setCDV(
            final java.lang.String cDV) {
        this._cDV = cDV;
    }

    /**
     * Sets the value of field 'CFOP'. The field 'CFOP' has the
     * following description: Código Fiscal de Operações e
     * Prestações
     * 
     * @param CFOP the value of field 'CFOP'.
     */
    public void setCFOP(
            final java.lang.String CFOP) {
        this._CFOP = CFOP;
    }

    /**
     * Sets the value of field 'cMunEnv'. The field 'cMunEnv' has
     * the following description: Código do Município de envio do
     * CT-e (de onde o documento foi transmitido)
     * 
     * @param cMunEnv the value of field 'cMunEnv'.
     */
    public void setCMunEnv(
            final java.lang.String cMunEnv) {
        this._cMunEnv = cMunEnv;
    }

    /**
     * Sets the value of field 'cMunFim'. The field 'cMunFim' has
     * the following description: Código do Município de término
     * da prestação
     * 
     * @param cMunFim the value of field 'cMunFim'.
     */
    public void setCMunFim(
            final java.lang.String cMunFim) {
        this._cMunFim = cMunFim;
    }

    /**
     * Sets the value of field 'cMunIni'. The field 'cMunIni' has
     * the following description: Código do Município de início
     * da prestação
     * 
     * @param cMunIni the value of field 'cMunIni'.
     */
    public void setCMunIni(
            final java.lang.String cMunIni) {
        this._cMunIni = cMunIni;
    }

    /**
     * Sets the value of field 'cUF'. The field 'cUF' has the
     * following description: Código da UF do emitente do CT-e.
     * 
     * @param cUF the value of field 'cUF'.
     */
    public void setCUF(
            final com.mercurio.lms.cte.model.v200.types.TCodUfIBGE cUF) {
        this._cUF = cUF;
    }

    /**
     * Sets the value of field 'dhEmi'. The field 'dhEmi' has the
     * following description: Data e hora de emissão do CT-e 
     * 
     * @param dhEmi the value of field 'dhEmi'.
     */
    public void setDhEmi(
            final java.lang.String dhEmi) {
        this._dhEmi = dhEmi;
    }

    /**
     * Sets the value of field 'forPag'. The field 'forPag' has the
     * following description: Forma de pagamento do serviço TAG
     * OBSOLETA - Será retirada em versão futura
     * 
     * @param forPag the value of field 'forPag'.
     */
    public void setForPag(
            final com.mercurio.lms.cte.model.v200.types.ForPagType forPag) {
        this._forPag = forPag;
    }

    /**
     * Sets the value of field 'ideChoice'.
     * 
     * @param ideChoice the value of field 'ideChoice'.
     */
    public void setIdeChoice(
            final com.mercurio.lms.cte.model.v200.IdeChoice ideChoice) {
        this._ideChoice = ideChoice;
    }

    /**
     * Sets the value of field 'ideSequence'. The field
     * 'ideSequence' has the following description: Informar apenas
     * para tpEmis diferente de 1
     * 
     * @param ideSequence the value of field 'ideSequence'.
     */
    public void setIdeSequence(
            final com.mercurio.lms.cte.model.v200.IdeSequence ideSequence) {
        this._ideSequence = ideSequence;
    }

    /**
     * Sets the value of field 'mod'. The field 'mod' has the
     * following description: Modelo do documento fiscal
     * 
     * @param mod the value of field 'mod'.
     */
    public void setMod(
            final com.mercurio.lms.cte.model.v200.types.TModCT mod) {
        this._mod = mod;
    }

    /**
     * Sets the value of field 'modal'. The field 'modal' has the
     * following description: Modal
     * 
     * @param modal the value of field 'modal'.
     */
    public void setModal(
            final com.mercurio.lms.cte.model.v200.types.TModTransp modal) {
        this._modal = modal;
    }

    /**
     * Sets the value of field 'nCT'. The field 'nCT' has the
     * following description: Número do CT-e
     * 
     * @param nCT the value of field 'nCT'.
     */
    public void setNCT(
            final java.lang.String nCT) {
        this._nCT = nCT;
    }

    /**
     * Sets the value of field 'natOp'. The field 'natOp' has the
     * following description: Natureza da Operação
     * 
     * @param natOp the value of field 'natOp'.
     */
    public void setNatOp(
            final java.lang.String natOp) {
        this._natOp = natOp;
    }

    /**
     * Sets the value of field 'procEmi'. The field 'procEmi' has
     * the following description: Identificador do processo de
     * emissão do CT-e
     * 
     * @param procEmi the value of field 'procEmi'.
     */
    public void setProcEmi(
            final com.mercurio.lms.cte.model.v200.types.TProcEmi procEmi) {
        this._procEmi = procEmi;
    }

    /**
     * Sets the value of field 'refCTE'. The field 'refCTE' has the
     * following description: Chave de acesso do CT-e referenciado
     * 
     * @param refCTE the value of field 'refCTE'.
     */
    public void setRefCTE(
            final java.lang.String refCTE) {
        this._refCTE = refCTE;
    }

    /**
     * Sets the value of field 'retira'. The field 'retira' has the
     * following description: Indicador se o Recebedor retira no
     * Aeroporto, Filial, Porto ou Estação de Destino?
     * 
     * @param retira the value of field 'retira'.
     */
    public void setRetira(
            final com.mercurio.lms.cte.model.v200.types.RetiraType retira) {
        this._retira = retira;
    }

    /**
     * Sets the value of field 'serie'. The field 'serie' has the
     * following description: Série do CT-e
     * 
     * @param serie the value of field 'serie'.
     */
    public void setSerie(
            final java.lang.String serie) {
        this._serie = serie;
    }

    /**
     * Sets the value of field 'tpAmb'. The field 'tpAmb' has the
     * following description: Tipo do Ambiente
     * 
     * @param tpAmb the value of field 'tpAmb'.
     */
    public void setTpAmb(
            final com.mercurio.lms.cte.model.v200.types.TAmb tpAmb) {
        this._tpAmb = tpAmb;
    }

    /**
     * Sets the value of field 'tpCTe'. The field 'tpCTe' has the
     * following description: Tipo do CT-e
     * 
     * @param tpCTe the value of field 'tpCTe'.
     */
    public void setTpCTe(
            final com.mercurio.lms.cte.model.v200.types.TFinCTe tpCTe) {
        this._tpCTe = tpCTe;
    }

    /**
     * Sets the value of field 'tpEmis'. The field 'tpEmis' has the
     * following description: Forma de emissão do CT-e
     * 
     * @param tpEmis the value of field 'tpEmis'.
     */
    public void setTpEmis(
            final com.mercurio.lms.cte.model.v200.types.TpEmisType tpEmis) {
        this._tpEmis = tpEmis;
    }

    /**
     * Sets the value of field 'tpImp'. The field 'tpImp' has the
     * following description: Formato de impressão do DACTE
     * 
     * @param tpImp the value of field 'tpImp'.
     */
    public void setTpImp(
            final com.mercurio.lms.cte.model.v200.types.TpImpType tpImp) {
        this._tpImp = tpImp;
    }

    /**
     * Sets the value of field 'tpServ'. The field 'tpServ' has the
     * following description: Tipo do Serviço
     * 
     * @param tpServ the value of field 'tpServ'.
     */
    public void setTpServ(
            final com.mercurio.lms.cte.model.v200.types.TpServType tpServ) {
        this._tpServ = tpServ;
    }

    /**
     * Sets the value of field 'UFEnv'. The field 'UFEnv' has the
     * following description: Sigla da UF de envio do CT-e (de onde
     * o documento foi transmitido)
     * 
     * @param UFEnv the value of field 'UFEnv'.
     */
    public void setUFEnv(
            final com.mercurio.lms.cte.model.v200.types.TUf UFEnv) {
        this._UFEnv = UFEnv;
    }

    /**
     * Sets the value of field 'UFFim'. The field 'UFFim' has the
     * following description: UF do término da prestação
     * 
     * @param UFFim the value of field 'UFFim'.
     */
    public void setUFFim(
            final com.mercurio.lms.cte.model.v200.types.TUf UFFim) {
        this._UFFim = UFFim;
    }

    /**
     * Sets the value of field 'UFIni'. The field 'UFIni' has the
     * following description: UF do início da prestação
     * 
     * @param UFIni the value of field 'UFIni'.
     */
    public void setUFIni(
            final com.mercurio.lms.cte.model.v200.types.TUf UFIni) {
        this._UFIni = UFIni;
    }

    /**
     * Sets the value of field 'verProc'. The field 'verProc' has
     * the following description: Versão do processo de emissão
     * 
     * @param verProc the value of field 'verProc'.
     */
    public void setVerProc(
            final java.lang.String verProc) {
        this._verProc = verProc;
    }

    /**
     * Sets the value of field 'xDetRetira'. The field 'xDetRetira'
     * has the following description: Detalhes do retira
     * 
     * @param xDetRetira the value of field 'xDetRetira'.
     */
    public void setXDetRetira(
            final java.lang.String xDetRetira) {
        this._xDetRetira = xDetRetira;
    }

    /**
     * Sets the value of field 'xMunEnv'. The field 'xMunEnv' has
     * the following description: Nome do Município de envio do
     * CT-e (de onde o documento foi transmitido)
     * 
     * @param xMunEnv the value of field 'xMunEnv'.
     */
    public void setXMunEnv(
            final java.lang.String xMunEnv) {
        this._xMunEnv = xMunEnv;
    }

    /**
     * Sets the value of field 'xMunFim'. The field 'xMunFim' has
     * the following description: Nome do Município do término da
     * prestação
     * 
     * @param xMunFim the value of field 'xMunFim'.
     */
    public void setXMunFim(
            final java.lang.String xMunFim) {
        this._xMunFim = xMunFim;
    }

    /**
     * Sets the value of field 'xMunIni'. The field 'xMunIni' has
     * the following description: Nome do Município do início da
     * prestação
     * 
     * @param xMunIni the value of field 'xMunIni'.
     */
    public void setXMunIni(
            final java.lang.String xMunIni) {
        this._xMunIni = xMunIni;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v200.Ide
     */
    public static com.mercurio.lms.cte.model.v200.Ide unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v200.Ide) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v200.Ide.class, reader);
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
