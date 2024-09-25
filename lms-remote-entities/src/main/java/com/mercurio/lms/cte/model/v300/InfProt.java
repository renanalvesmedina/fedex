/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v300;

/**
 * Dados do protocolo de status
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfProt implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _id.
     */
    private java.lang.String _id;

    /**
     * Identificação do Ambiente:
     * 1 - Produção
     * 2 - Homologação
     */
    private com.mercurio.lms.cte.model.v300.types.TAmb _tpAmb;

    /**
     * Versão do Aplicativo que processou a NF-e
     */
    private java.lang.String _verAplic;

    /**
     * Chaves de acesso da CT-e, compostas por: UF do emitente,
     * AAMM da emissão da NFe, CNPJ do emitente, modelo, subsérie
     * e número da CT-e e código numérico+DV.
     */
    private java.lang.String _chCTe;

    /**
     * Data e hora de processamento, no formato
     * AAAA-MM-DDTHH:MM:SS. Deve ser preenchida com data e hora da
     * gravação no Banco em caso de Confirmação. Em caso de
     * Rejeição, com data e hora do recebimento do Lote de CT-e
     * enviado.
     */
    private java.util.Date _dhRecbto;

    /**
     * Número do Protocolo de Status do CT-e. 1 posição tipo de
     * autorizador (1 – Secretaria de Fazenda Estadual, 3 - SEFAZ
     * Virtual RS, 5 - SEFAZ Virtual SP ); 2 posições ano; 10
     * seqüencial no ano.
     */
    private java.lang.String _nProt;

    /**
     * Digest Value da CT-e processado. Utilizado para conferir a
     * integridade do CT-e original.
     */
    private byte[] _digVal;

    /**
     * Código do status do CT-e.
     */
    private java.lang.String _cStat;

    /**
     * Descrição literal do status do CT-e.
     */
    private java.lang.String _xMotivo;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfProt() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'cStat'. The field 'cStat' has
     * the following description: Código do status do CT-e.
     * 
     * @return the value of field 'CStat'.
     */
    public java.lang.String getCStat(
    ) {
        return this._cStat;
    }

    /**
     * Returns the value of field 'chCTe'. The field 'chCTe' has
     * the following description: Chaves de acesso da CT-e,
     * compostas por: UF do emitente, AAMM da emissão da NFe, CNPJ
     * do emitente, modelo, subsérie e número da CT-e e código
     * numérico+DV.
     * 
     * @return the value of field 'ChCTe'.
     */
    public java.lang.String getChCTe(
    ) {
        return this._chCTe;
    }

    /**
     * Returns the value of field 'dhRecbto'. The field 'dhRecbto'
     * has the following description: Data e hora de processamento,
     * no formato AAAA-MM-DDTHH:MM:SS. Deve ser preenchida com data
     * e hora da gravação no Banco em caso de Confirmação. Em
     * caso de Rejeição, com data e hora do recebimento do Lote
     * de CT-e enviado.
     * 
     * @return the value of field 'DhRecbto'.
     */
    public java.util.Date getDhRecbto(
    ) {
        return this._dhRecbto;
    }

    /**
     * Returns the value of field 'digVal'. The field 'digVal' has
     * the following description: Digest Value da CT-e processado.
     * Utilizado para conferir a integridade do CT-e original.
     * 
     * @return the value of field 'DigVal'.
     */
    public byte[] getDigVal(
    ) {
        return this._digVal;
    }

    /**
     * Returns the value of field 'id'.
     * 
     * @return the value of field 'Id'.
     */
    public java.lang.String getId(
    ) {
        return this._id;
    }

    /**
     * Returns the value of field 'nProt'. The field 'nProt' has
     * the following description: Número do Protocolo de Status do
     * CT-e. 1 posição tipo de autorizador (1 – Secretaria de
     * Fazenda Estadual, 3 - SEFAZ Virtual RS, 5 - SEFAZ Virtual SP
     * ); 2 posições ano; 10 seqüencial no ano.
     * 
     * @return the value of field 'NProt'.
     */
    public java.lang.String getNProt(
    ) {
        return this._nProt;
    }

    /**
     * Returns the value of field 'tpAmb'. The field 'tpAmb' has
     * the following description: Identificação do Ambiente:
     * 1 - Produção
     * 2 - Homologação
     * 
     * @return the value of field 'TpAmb'.
     */
    public com.mercurio.lms.cte.model.v300.types.TAmb getTpAmb(
    ) {
        return this._tpAmb;
    }

    /**
     * Returns the value of field 'verAplic'. The field 'verAplic'
     * has the following description: Versão do Aplicativo que
     * processou a NF-e
     * 
     * @return the value of field 'VerAplic'.
     */
    public java.lang.String getVerAplic(
    ) {
        return this._verAplic;
    }

    /**
     * Returns the value of field 'xMotivo'. The field 'xMotivo'
     * has the following description: Descrição literal do status
     * do CT-e.
     * 
     * @return the value of field 'XMotivo'.
     */
    public java.lang.String getXMotivo(
    ) {
        return this._xMotivo;
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
     * Sets the value of field 'cStat'. The field 'cStat' has the
     * following description: Código do status do CT-e.
     * 
     * @param cStat the value of field 'cStat'.
     */
    public void setCStat(
            final java.lang.String cStat) {
        this._cStat = cStat;
    }

    /**
     * Sets the value of field 'chCTe'. The field 'chCTe' has the
     * following description: Chaves de acesso da CT-e, compostas
     * por: UF do emitente, AAMM da emissão da NFe, CNPJ do
     * emitente, modelo, subsérie e número da CT-e e código
     * numérico+DV.
     * 
     * @param chCTe the value of field 'chCTe'.
     */
    public void setChCTe(
            final java.lang.String chCTe) {
        this._chCTe = chCTe;
    }

    /**
     * Sets the value of field 'dhRecbto'. The field 'dhRecbto' has
     * the following description: Data e hora de processamento, no
     * formato AAAA-MM-DDTHH:MM:SS. Deve ser preenchida com data e
     * hora da gravação no Banco em caso de Confirmação. Em
     * caso de Rejeição, com data e hora do recebimento do Lote
     * de CT-e enviado.
     * 
     * @param dhRecbto the value of field 'dhRecbto'.
     */
    public void setDhRecbto(
            final java.util.Date dhRecbto) {
        this._dhRecbto = dhRecbto;
    }

    /**
     * Sets the value of field 'digVal'. The field 'digVal' has the
     * following description: Digest Value da CT-e processado.
     * Utilizado para conferir a integridade do CT-e original.
     * 
     * @param digVal the value of field 'digVal'.
     */
    public void setDigVal(
            final byte[] digVal) {
        this._digVal = digVal;
    }

    /**
     * Sets the value of field 'id'.
     * 
     * @param id the value of field 'id'.
     */
    public void setId(
            final java.lang.String id) {
        this._id = id;
    }

    /**
     * Sets the value of field 'nProt'. The field 'nProt' has the
     * following description: Número do Protocolo de Status do
     * CT-e. 1 posição tipo de autorizador (1 – Secretaria de
     * Fazenda Estadual, 3 - SEFAZ Virtual RS, 5 - SEFAZ Virtual SP
     * ); 2 posições ano; 10 seqüencial no ano.
     * 
     * @param nProt the value of field 'nProt'.
     */
    public void setNProt(
            final java.lang.String nProt) {
        this._nProt = nProt;
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
            final com.mercurio.lms.cte.model.v300.types.TAmb tpAmb) {
        this._tpAmb = tpAmb;
    }

    /**
     * Sets the value of field 'verAplic'. The field 'verAplic' has
     * the following description: Versão do Aplicativo que
     * processou a NF-e
     * 
     * @param verAplic the value of field 'verAplic'.
     */
    public void setVerAplic(
            final java.lang.String verAplic) {
        this._verAplic = verAplic;
    }

    /**
     * Sets the value of field 'xMotivo'. The field 'xMotivo' has
     * the following description: Descrição literal do status do
     * CT-e.
     * 
     * @param xMotivo the value of field 'xMotivo'.
     */
    public void setXMotivo(
            final java.lang.String xMotivo) {
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
     * com.mercurio.lms.cte.model.v300.InfProt
     */
    public static com.mercurio.lms.cte.model.v300.InfProt unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v300.InfProt) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v300.InfProt.class, reader);
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
