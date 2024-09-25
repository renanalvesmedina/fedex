/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v300;

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
    private java.lang.String _id;

    /**
     * Identificação do Ambiente:
     * 1 - Produção
     * 2 - Homologação
     */
    private com.mercurio.lms.cte.model.v300.types.TAmb _tpAmb;

    /**
     * Serviço Solicitado
     */
    private java.lang.String _xServ = "CANCELAR";

    /**
     * Chaves de acesso compostas por Código da UF + AAMM da
     * emissão + CNPJ do Emitente + Modelo, Série e Número do
     * CT-e+ Código Numérico + DV.
     */
    private java.lang.String _chCTe;

    /**
     * Número do Protocolo de Status do CT-e. 1 posição tipo de
     * autorizador (1 – Secretaria de Fazenda Estadual 2 –
     * Receita Federal - SCAN, 3 - SEFAZ Virtual RFB ); 2
     * posições ano; 10 seqüencial no ano.
     */
    private java.lang.String _nProt;

    /**
     * Justificativa do cancelamento
     */
    private java.lang.String _xJust;


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
    public java.lang.String getChCTe(
    ) {
        return this._chCTe;
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
     * Fazenda Estadual 2 – Receita Federal - SCAN, 3 - SEFAZ
     * Virtual RFB ); 2 posições ano; 10 seqüencial no ano.
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
     * Returns the value of field 'xJust'. The field 'xJust' has
     * the following description: Justificativa do cancelamento
     * 
     * @return the value of field 'XJust'.
     */
    public java.lang.String getXJust(
    ) {
        return this._xJust;
    }

    /**
     * Returns the value of field 'xServ'. The field 'xServ' has
     * the following description: Serviço Solicitado
     * 
     * @return the value of field 'XServ'.
     */
    public java.lang.String getXServ(
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
            final java.lang.String chCTe) {
        this._chCTe = chCTe;
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
     * Fazenda Estadual 2 – Receita Federal - SCAN, 3 - SEFAZ
     * Virtual RFB ); 2 posições ano; 10 seqüencial no ano.
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
     * Sets the value of field 'xJust'. The field 'xJust' has the
     * following description: Justificativa do cancelamento
     * 
     * @param xJust the value of field 'xJust'.
     */
    public void setXJust(
            final java.lang.String xJust) {
        this._xJust = xJust;
    }

    /**
     * Sets the value of field 'xServ'. The field 'xServ' has the
     * following description: Serviço Solicitado
     * 
     * @param xServ the value of field 'xServ'.
     */
    public void setXServ(
            final java.lang.String xServ) {
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
     * com.mercurio.lms.cte.model.v300.InfCanc
     */
    public static com.mercurio.lms.cte.model.v300.InfCanc unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v300.InfCanc) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v300.InfCanc.class, reader);
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
