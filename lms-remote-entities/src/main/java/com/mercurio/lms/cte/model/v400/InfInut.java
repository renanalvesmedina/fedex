/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Dados do Retorno do Pedido de Inutilização de Numeração do
 * Conhecimento de Transporte eletrônico
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfInut implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _id.
     */
    private String _id;

    /**
     * Identificação do Ambiente:
     * 1 - Produção
     * 2 - Homologação
     */
    private com.mercurio.lms.cte.model.v400.types.TAmb _tpAmb;

    /**
     * Versão do Aplicativo que processou a CT-e
     */
    private String _verAplic;

    /**
     * Código do status da mensagem enviada.
     */
    private String _cStat;

    /**
     * Descrição literal do status do serviço solicitado.
     */
    private String _xMotivo;

    /**
     * Código da UF solicitada
     */
    private com.mercurio.lms.cte.model.v400.types.TCodUfIBGE _cUF;

    /**
     * Ano de inutilização da numeração
     */
    private short _ano;

    /**
     * keeps track of state for field: _ano
     */
    private boolean _has_ano;

    /**
     * CNPJ do emitente
     */
    private String _CNPJ;

    /**
     * Modelo da CT-e (57)
     */
    private com.mercurio.lms.cte.model.v400.types.TModCT _mod;

    /**
     * Série da CT-e
     */
    private String _serie;

    /**
     * Número da CT-e inicial
     */
    private String _nCTIni;

    /**
     * Número da CT-e final
     */
    private String _nCTFin;

    /**
     * Data e hora de recebimento, no formato AAAA-MM-DDTHH:MM:SS.
     * Deve ser preenchida com data e hora da gravação no Banco
     * em caso de Confirmação. Em caso de Rejeição, com data e
     * hora do recebimento do Pedido de Inutilização.
     */
    private java.util.Date _dhRecbto;

    /**
     * Número do Protocolo de Status do CT-e. 1 posição (1 –
     * Secretaria de Fazenda Estadual , 3 - SEFAZ Virtual RS, 5 -
     * SEFAZ Virtual SP); 2 - código da UF - 2 posições ano; 10
     * seqüencial no ano.
     */
    private String _nProt;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfInut() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteAno(
    ) {
        this._has_ano= false;
    }

    /**
     * Returns the value of field 'ano'. The field 'ano' has the
     * following description: Ano de inutilização da numeração
     *
     * @return the value of field 'Ano'.
     */
    public short getAno(
    ) {
        return this._ano;
    }

    /**
     * Returns the value of field 'CNPJ'. The field 'CNPJ' has the
     * following description: CNPJ do emitente
     *
     * @return the value of field 'CNPJ'.
     */
    public String getCNPJ(
    ) {
        return this._CNPJ;
    }

    /**
     * Returns the value of field 'cStat'. The field 'cStat' has
     * the following description: Código do status da mensagem
     * enviada.
     *
     * @return the value of field 'CStat'.
     */
    public String getCStat(
    ) {
        return this._cStat;
    }

    /**
     * Returns the value of field 'cUF'. The field 'cUF' has the
     * following description: Código da UF solicitada
     *
     * @return the value of field 'CUF'.
     */
    public com.mercurio.lms.cte.model.v400.types.TCodUfIBGE getCUF(
    ) {
        return this._cUF;
    }

    /**
     * Returns the value of field 'dhRecbto'. The field 'dhRecbto'
     * has the following description: Data e hora de recebimento,
     * no formato AAAA-MM-DDTHH:MM:SS. Deve ser preenchida com data
     * e hora da gravação no Banco em caso de Confirmação. Em
     * caso de Rejeição, com data e hora do recebimento do Pedido
     * de Inutilização.
     *
     * @return the value of field 'DhRecbto'.
     */
    public java.util.Date getDhRecbto(
    ) {
        return this._dhRecbto;
    }

    /**
     * Returns the value of field 'id'.
     *
     * @return the value of field 'Id'.
     */
    public String getId(
    ) {
        return this._id;
    }

    /**
     * Returns the value of field 'mod'. The field 'mod' has the
     * following description: Modelo da CT-e (57)
     *
     * @return the value of field 'Mod'.
     */
    public com.mercurio.lms.cte.model.v400.types.TModCT getMod(
    ) {
        return this._mod;
    }

    /**
     * Returns the value of field 'nCTFin'. The field 'nCTFin' has
     * the following description: Número da CT-e final
     *
     * @return the value of field 'NCTFin'.
     */
    public String getNCTFin(
    ) {
        return this._nCTFin;
    }

    /**
     * Returns the value of field 'nCTIni'. The field 'nCTIni' has
     * the following description: Número da CT-e inicial
     *
     * @return the value of field 'NCTIni'.
     */
    public String getNCTIni(
    ) {
        return this._nCTIni;
    }

    /**
     * Returns the value of field 'nProt'. The field 'nProt' has
     * the following description: Número do Protocolo de Status do
     * CT-e. 1 posição (1 – Secretaria de Fazenda Estadual , 3
     * - SEFAZ Virtual RS, 5 - SEFAZ Virtual SP); 2 - código da UF
     * - 2 posições ano; 10 seqüencial no ano.
     *
     * @return the value of field 'NProt'.
     */
    public String getNProt(
    ) {
        return this._nProt;
    }

    /**
     * Returns the value of field 'serie'. The field 'serie' has
     * the following description: Série da CT-e
     *
     * @return the value of field 'Serie'.
     */
    public String getSerie(
    ) {
        return this._serie;
    }

    /**
     * Returns the value of field 'tpAmb'. The field 'tpAmb' has
     * the following description: Identificação do Ambiente:
     * 1 - Produção
     * 2 - Homologação
     *
     * @return the value of field 'TpAmb'.
     */
    public com.mercurio.lms.cte.model.v400.types.TAmb getTpAmb(
    ) {
        return this._tpAmb;
    }

    /**
     * Returns the value of field 'verAplic'. The field 'verAplic'
     * has the following description: Versão do Aplicativo que
     * processou a CT-e
     *
     * @return the value of field 'VerAplic'.
     */
    public String getVerAplic(
    ) {
        return this._verAplic;
    }

    /**
     * Returns the value of field 'xMotivo'. The field 'xMotivo'
     * has the following description: Descrição literal do status
     * do serviço solicitado.
     *
     * @return the value of field 'XMotivo'.
     */
    public String getXMotivo(
    ) {
        return this._xMotivo;
    }

    /**
     * Method hasAno.
     *
     * @return true if at least one Ano has been added
     */
    public boolean hasAno(
    ) {
        return this._has_ano;
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
     * Sets the value of field 'ano'. The field 'ano' has the
     * following description: Ano de inutilização da numeração
     *
     * @param ano the value of field 'ano'.
     */
    public void setAno(
            final short ano) {
        this._ano = ano;
        this._has_ano = true;
    }

    /**
     * Sets the value of field 'CNPJ'. The field 'CNPJ' has the
     * following description: CNPJ do emitente
     *
     * @param CNPJ the value of field 'CNPJ'.
     */
    public void setCNPJ(
            final String CNPJ) {
        this._CNPJ = CNPJ;
    }

    /**
     * Sets the value of field 'cStat'. The field 'cStat' has the
     * following description: Código do status da mensagem
     * enviada.
     *
     * @param cStat the value of field 'cStat'.
     */
    public void setCStat(
            final String cStat) {
        this._cStat = cStat;
    }

    /**
     * Sets the value of field 'cUF'. The field 'cUF' has the
     * following description: Código da UF solicitada
     *
     * @param cUF the value of field 'cUF'.
     */
    public void setCUF(
            final com.mercurio.lms.cte.model.v400.types.TCodUfIBGE cUF) {
        this._cUF = cUF;
    }

    /**
     * Sets the value of field 'dhRecbto'. The field 'dhRecbto' has
     * the following description: Data e hora de recebimento, no
     * formato AAAA-MM-DDTHH:MM:SS. Deve ser preenchida com data e
     * hora da gravação no Banco em caso de Confirmação. Em
     * caso de Rejeição, com data e hora do recebimento do Pedido
     * de Inutilização.
     *
     * @param dhRecbto the value of field 'dhRecbto'.
     */
    public void setDhRecbto(
            final java.util.Date dhRecbto) {
        this._dhRecbto = dhRecbto;
    }

    /**
     * Sets the value of field 'id'.
     *
     * @param id the value of field 'id'.
     */
    public void setId(
            final String id) {
        this._id = id;
    }

    /**
     * Sets the value of field 'mod'. The field 'mod' has the
     * following description: Modelo da CT-e (57)
     *
     * @param mod the value of field 'mod'.
     */
    public void setMod(
            final com.mercurio.lms.cte.model.v400.types.TModCT mod) {
        this._mod = mod;
    }

    /**
     * Sets the value of field 'nCTFin'. The field 'nCTFin' has the
     * following description: Número da CT-e final
     *
     * @param nCTFin the value of field 'nCTFin'.
     */
    public void setNCTFin(
            final String nCTFin) {
        this._nCTFin = nCTFin;
    }

    /**
     * Sets the value of field 'nCTIni'. The field 'nCTIni' has the
     * following description: Número da CT-e inicial
     *
     * @param nCTIni the value of field 'nCTIni'.
     */
    public void setNCTIni(
            final String nCTIni) {
        this._nCTIni = nCTIni;
    }

    /**
     * Sets the value of field 'nProt'. The field 'nProt' has the
     * following description: Número do Protocolo de Status do
     * CT-e. 1 posição (1 – Secretaria de Fazenda Estadual , 3
     * - SEFAZ Virtual RS, 5 - SEFAZ Virtual SP); 2 - código da UF
     * - 2 posições ano; 10 seqüencial no ano.
     *
     * @param nProt the value of field 'nProt'.
     */
    public void setNProt(
            final String nProt) {
        this._nProt = nProt;
    }

    /**
     * Sets the value of field 'serie'. The field 'serie' has the
     * following description: Série da CT-e
     *
     * @param serie the value of field 'serie'.
     */
    public void setSerie(
            final String serie) {
        this._serie = serie;
    }

    /**
     * Sets the value of field 'tpAmb'. The field 'tpAmb' has the
     * following description: Identificação do Ambiente:
     * 1 - Produção
     * 2 - Homologação
     *
     * @param tpAmb the value of field 'tpAmb'.
     */
    public void setTpAmb(
            final com.mercurio.lms.cte.model.v400.types.TAmb tpAmb) {
        this._tpAmb = tpAmb;
    }

    /**
     * Sets the value of field 'verAplic'. The field 'verAplic' has
     * the following description: Versão do Aplicativo que
     * processou a CT-e
     *
     * @param verAplic the value of field 'verAplic'.
     */
    public void setVerAplic(
            final String verAplic) {
        this._verAplic = verAplic;
    }

    /**
     * Sets the value of field 'xMotivo'. The field 'xMotivo' has
     * the following description: Descrição literal do status do
     * serviço solicitado.
     *
     * @param xMotivo the value of field 'xMotivo'.
     */
    public void setXMotivo(
            final String xMotivo) {
        this._xMotivo = xMotivo;
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
     * com.mercurio.lms.cte.model.v400.InfInut
     */
    public static InfInut unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (InfInut) org.exolab.castor.xml.Unmarshaller.unmarshal(InfInut.class, reader);
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
