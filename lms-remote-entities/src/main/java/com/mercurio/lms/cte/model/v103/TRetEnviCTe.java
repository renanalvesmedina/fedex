/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Tipo Retorno do Pedido de Concessão de Autorização da CT-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TRetEnviCTe implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _versao.
     */
    private java.lang.String _versao;

    /**
     * Identificação do Ambiente:
     * 1 - Produção
     * 2 - Homologação
     */
    private com.mercurio.lms.cte.model.v103.types.TAmb _tpAmb;

    /**
     * Identificação da UF
     */
    private com.mercurio.lms.cte.model.v103.types.TCodUfIBGE _cUF;

    /**
     * Versão do Aplicativo que recebeu o Lote.
     */
    private java.lang.String _verAplic;

    /**
     * Código do status da mensagem enviada.
     */
    private java.lang.String _cStat;

    /**
     * Descrição literal do status do serviço solicitado.
     */
    private java.lang.String _xMotivo;

    /**
     * Dados do Recibo do Lote
     */
    private com.mercurio.lms.cte.model.v103.InfRec _infRec;


      //----------------/
     //- Constructors -/
    //----------------/

    public TRetEnviCTe() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'cStat'. The field 'cStat' has
     * the following description: Código do status da mensagem
     * enviada.
     * 
     * @return the value of field 'CStat'.
     */
    public java.lang.String getCStat(
    ) {
        return this._cStat;
    }

    /**
     * Returns the value of field 'cUF'. The field 'cUF' has the
     * following description: Identificação da UF
     * 
     * @return the value of field 'CUF'.
     */
    public com.mercurio.lms.cte.model.v103.types.TCodUfIBGE getCUF(
    ) {
        return this._cUF;
    }

    /**
     * Returns the value of field 'infRec'. The field 'infRec' has
     * the following description: Dados do Recibo do Lote
     * 
     * @return the value of field 'InfRec'.
     */
    public com.mercurio.lms.cte.model.v103.InfRec getInfRec(
    ) {
        return this._infRec;
    }

    /**
     * Returns the value of field 'tpAmb'. The field 'tpAmb' has
     * the following description: Identificação do Ambiente:
     * 1 - Produção
     * 2 - Homologação
     * 
     * @return the value of field 'TpAmb'.
     */
    public com.mercurio.lms.cte.model.v103.types.TAmb getTpAmb(
    ) {
        return this._tpAmb;
    }

    /**
     * Returns the value of field 'verAplic'. The field 'verAplic'
     * has the following description: Versão do Aplicativo que
     * recebeu o Lote.
     * 
     * @return the value of field 'VerAplic'.
     */
    public java.lang.String getVerAplic(
    ) {
        return this._verAplic;
    }

    /**
     * Returns the value of field 'versao'.
     * 
     * @return the value of field 'Versao'.
     */
    public java.lang.String getVersao(
    ) {
        return this._versao;
    }

    /**
     * Returns the value of field 'xMotivo'. The field 'xMotivo'
     * has the following description: Descrição literal do status
     * do serviço solicitado.
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
     * following description: Código do status da mensagem
     * enviada.
     * 
     * @param cStat the value of field 'cStat'.
     */
    public void setCStat(
            final java.lang.String cStat) {
        this._cStat = cStat;
    }

    /**
     * Sets the value of field 'cUF'. The field 'cUF' has the
     * following description: Identificação da UF
     * 
     * @param cUF the value of field 'cUF'.
     */
    public void setCUF(
            final com.mercurio.lms.cte.model.v103.types.TCodUfIBGE cUF) {
        this._cUF = cUF;
    }

    /**
     * Sets the value of field 'infRec'. The field 'infRec' has the
     * following description: Dados do Recibo do Lote
     * 
     * @param infRec the value of field 'infRec'.
     */
    public void setInfRec(
            final com.mercurio.lms.cte.model.v103.InfRec infRec) {
        this._infRec = infRec;
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
            final com.mercurio.lms.cte.model.v103.types.TAmb tpAmb) {
        this._tpAmb = tpAmb;
    }

    /**
     * Sets the value of field 'verAplic'. The field 'verAplic' has
     * the following description: Versão do Aplicativo que recebeu
     * o Lote.
     * 
     * @param verAplic the value of field 'verAplic'.
     */
    public void setVerAplic(
            final java.lang.String verAplic) {
        this._verAplic = verAplic;
    }

    /**
     * Sets the value of field 'versao'.
     * 
     * @param versao the value of field 'versao'.
     */
    public void setVersao(
            final java.lang.String versao) {
        this._versao = versao;
    }

    /**
     * Sets the value of field 'xMotivo'. The field 'xMotivo' has
     * the following description: Descrição literal do status do
     * serviço solicitado.
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
     * com.mercurio.lms.cte.model.v103.TRetEnviCTe
     */
    public static com.mercurio.lms.cte.model.v103.TRetEnviCTe unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.TRetEnviCTe) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.TRetEnviCTe.class, reader);
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
