/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Dados do Pedido de Cancelamentode Conhecimento de Transporte
 * Eletrônico
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfCanc implements java.io.Serializable {


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
     * Serviço Solicitado
     */
    private String _xServ = "CANCELAR";

    /**
     * Chaves de acesso compostas por Código da UF + AAMM da
     * emissão + CNPJ do Emitente + Modelo, Série e Número do
     * CT-e+ Código Numérico + DV.
     */
    private String _chCTe;

    /**
     * Número do Protocolo de Status do CT-e. 1 posição tipo de
     * autorizador (1 – Secretaria de Fazenda Estadual 2 –
     * Receita Federal - SCAN, 3 - SEFAZ Virtual RFB ); 2
     * posições ano; 10 seqüencial no ano.
     */
    private String _nProt;

    /**
     * Justificativa do cancelamento
     */
    private String _xJust;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfCanc() {
        super();
        setXServ("CANCELAR");
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'chCTe'. The field 'chCTe' has
     * the following description: Chaves de acesso compostas por
     * Código da UF + AAMM da emissão + CNPJ do Emitente +
     * Modelo, Série e Número do CT-e+ Código Numérico + DV.
     *
     * @return the value of field 'ChCTe'.
     */
    public String getChCTe(
    ) {
        return this._chCTe;
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
     * Returns the value of field 'nProt'. The field 'nProt' has
     * the following description: Número do Protocolo de Status do
     * CT-e. 1 posição tipo de autorizador (1 – Secretaria de
     * Fazenda Estadual 2 – Receita Federal - SCAN, 3 - SEFAZ
     * Virtual RFB ); 2 posições ano; 10 seqüencial no ano.
     *
     * @return the value of field 'NProt'.
     */
    public String getNProt(
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
    public com.mercurio.lms.cte.model.v400.types.TAmb getTpAmb(
    ) {
        return this._tpAmb;
    }

    /**
     * Returns the value of field 'xJust'. The field 'xJust' has
     * the following description: Justificativa do cancelamento
     *
     * @return the value of field 'XJust'.
     */
    public String getXJust(
    ) {
        return this._xJust;
    }

    /**
     * Returns the value of field 'xServ'. The field 'xServ' has
     * the following description: Serviço Solicitado
     *
     * @return the value of field 'XServ'.
     */
    public String getXServ(
    ) {
        return this._xServ;
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
     * Sets the value of field 'chCTe'. The field 'chCTe' has the
     * following description: Chaves de acesso compostas por
     * Código da UF + AAMM da emissão + CNPJ do Emitente +
     * Modelo, Série e Número do CT-e+ Código Numérico + DV.
     *
     * @param chCTe the value of field 'chCTe'.
     */
    public void setChCTe(
            final String chCTe) {
        this._chCTe = chCTe;
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
     * Sets the value of field 'nProt'. The field 'nProt' has the
     * following description: Número do Protocolo de Status do
     * CT-e. 1 posição tipo de autorizador (1 – Secretaria de
     * Fazenda Estadual 2 – Receita Federal - SCAN, 3 - SEFAZ
     * Virtual RFB ); 2 posições ano; 10 seqüencial no ano.
     *
     * @param nProt the value of field 'nProt'.
     */
    public void setNProt(
            final String nProt) {
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
            final com.mercurio.lms.cte.model.v400.types.TAmb tpAmb) {
        this._tpAmb = tpAmb;
    }

    /**
     * Sets the value of field 'xJust'. The field 'xJust' has the
     * following description: Justificativa do cancelamento
     *
     * @param xJust the value of field 'xJust'.
     */
    public void setXJust(
            final String xJust) {
        this._xJust = xJust;
    }

    /**
     * Sets the value of field 'xServ'. The field 'xServ' has the
     * following description: Serviço Solicitado
     *
     * @param xServ the value of field 'xServ'.
     */
    public void setXServ(
            final String xServ) {
        this._xServ = xServ;
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
     * com.mercurio.lms.cte.model.v400.InfCanc
     */
    public static InfCanc unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (InfCanc) org.exolab.castor.xml.Unmarshaller.unmarshal(InfCanc.class, reader);
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
