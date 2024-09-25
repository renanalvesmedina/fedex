/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100;

/**
 * Class InfEvento.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfEvento implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Identificador da TAG a ser assinada, a regra de formação
     * do Id é:
     * “ID�? + tpEvento + chave do MDF-e + nSeqEvento
     */
    private java.lang.String _id;

    /**
     * Código do órgão de recepção do Evento. Utilizar a
     * Tabela do IBGE extendida, utilizar 90 para identificar SUFRAM
     */
    private com.mercurio.lms.mdfe.model.v100.types.TCOrgaoIBGE _cOrgao;

    /**
     * Identificação do Ambiente:
     * 1 - Produção
     * 2 - Homologação
     */
    private com.mercurio.lms.mdfe.model.v100.types.TAmb _tpAmb;

    /**
     * CNPJ do emissor do evento
     */
    private java.lang.String _CNPJ;

    /**
     * Chave de Acesso do MDF-e vinculado ao evento
     */
    private java.lang.String _chMDFe;

    /**
     * Data e Hora do Evento, formato UTC (AAAA-MM-DDThh:mm:ssTZD,
     * onde TZD = +hh:mm ou -hh:mm)
     */
    private java.lang.String _dhEvento;

    /**
     * Tipo do Evento:
     * 110111 - Cancelamento
     * 110112 - Encerramento
     * 310620 - Registro de Passagem
     */
    private java.lang.String _tpEvento;

    /**
     * Seqüencial do evento para o mesmo tipo de evento. Para
     * maioria dos eventos será 1, nos casos em que possa existir
     * mais de um evento o autor do evento deve numerar de forma
     * seqüencial.
     */
    private java.lang.String _nSeqEvento;

    /**
     * Detalhamento do evento específico
     */
    private com.mercurio.lms.mdfe.model.v100.DetEvento _detEvento;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfEvento() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'CNPJ'. The field 'CNPJ' has the
     * following description: CNPJ do emissor do evento
     * 
     * @return the value of field 'CNPJ'.
     */
    public java.lang.String getCNPJ(
    ) {
        return this._CNPJ;
    }

    /**
     * Returns the value of field 'cOrgao'. The field 'cOrgao' has
     * the following description: Código do órgão de recepção
     * do Evento. Utilizar a Tabela do IBGE extendida, utilizar 90
     * para identificar SUFRAMA
     * 
     * @return the value of field 'COrgao'.
     */
    public com.mercurio.lms.mdfe.model.v100.types.TCOrgaoIBGE getCOrgao(
    ) {
        return this._cOrgao;
    }

    /**
     * Returns the value of field 'chMDFe'. The field 'chMDFe' has
     * the following description: Chave de Acesso do MDF-e
     * vinculado ao evento
     * 
     * @return the value of field 'ChMDFe'.
     */
    public java.lang.String getChMDFe(
    ) {
        return this._chMDFe;
    }

    /**
     * Returns the value of field 'detEvento'. The field
     * 'detEvento' has the following description: Detalhamento do
     * evento específico
     * 
     * @return the value of field 'DetEvento'.
     */
    public com.mercurio.lms.mdfe.model.v100.DetEvento getDetEvento(
    ) {
        return this._detEvento;
    }

    /**
     * Returns the value of field 'dhEvento'. The field 'dhEvento'
     * has the following description: Data e Hora do Evento,
     * formato UTC (AAAA-MM-DDThh:mm:ssTZD, onde TZD = +hh:mm ou
     * -hh:mm)
     * 
     * @return the value of field 'DhEvento'.
     */
    public java.lang.String getDhEvento(
    ) {
        return this._dhEvento;
    }

    /**
     * Returns the value of field 'id'. The field 'id' has the
     * following description: Identificador da TAG a ser assinada,
     * a regra de formação do Id é:
     * “ID�? + tpEvento + chave do MDF-e + nSeqEvento
     * 
     * @return the value of field 'Id'.
     */
    public java.lang.String getId(
    ) {
        return this._id;
    }

    /**
     * Returns the value of field 'nSeqEvento'. The field
     * 'nSeqEvento' has the following description: Seqüencial do
     * evento para o mesmo tipo de evento. Para maioria dos eventos
     * será 1, nos casos em que possa existir mais de um evento o
     * autor do evento deve numerar de forma seqüencial.
     * 
     * @return the value of field 'NSeqEvento'.
     */
    public java.lang.String getNSeqEvento(
    ) {
        return this._nSeqEvento;
    }

    /**
     * Returns the value of field 'tpAmb'. The field 'tpAmb' has
     * the following description: Identificação do Ambiente:
     * 1 - Produção
     * 2 - Homologação
     * 
     * @return the value of field 'TpAmb'.
     */
    public com.mercurio.lms.mdfe.model.v100.types.TAmb getTpAmb(
    ) {
        return this._tpAmb;
    }

    /**
     * Returns the value of field 'tpEvento'. The field 'tpEvento'
     * has the following description: Tipo do Evento:
     * 110111 - Cancelamento
     * 110112 - Encerramento
     * 310620 - Registro de Passagem
     * 
     * 
     * @return the value of field 'TpEvento'.
     */
    public java.lang.String getTpEvento(
    ) {
        return this._tpEvento;
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
     * Sets the value of field 'CNPJ'. The field 'CNPJ' has the
     * following description: CNPJ do emissor do evento
     * 
     * @param CNPJ the value of field 'CNPJ'.
     */
    public void setCNPJ(
            final java.lang.String CNPJ) {
        this._CNPJ = CNPJ;
    }

    /**
     * Sets the value of field 'cOrgao'. The field 'cOrgao' has the
     * following description: Código do órgão de recepção do
     * Evento. Utilizar a Tabela do IBGE extendida, utilizar 90
     * para identificar SUFRAMA
     * 
     * @param cOrgao the value of field 'cOrgao'.
     */
    public void setCOrgao(
            final com.mercurio.lms.mdfe.model.v100.types.TCOrgaoIBGE cOrgao) {
        this._cOrgao = cOrgao;
    }

    /**
     * Sets the value of field 'chMDFe'. The field 'chMDFe' has the
     * following description: Chave de Acesso do MDF-e vinculado ao
     * evento
     * 
     * @param chMDFe the value of field 'chMDFe'.
     */
    public void setChMDFe(
            final java.lang.String chMDFe) {
        this._chMDFe = chMDFe;
    }

    /**
     * Sets the value of field 'detEvento'. The field 'detEvento'
     * has the following description: Detalhamento do evento
     * específico
     * 
     * @param detEvento the value of field 'detEvento'.
     */
    public void setDetEvento(
            final com.mercurio.lms.mdfe.model.v100.DetEvento detEvento) {
        this._detEvento = detEvento;
    }

    /**
     * Sets the value of field 'dhEvento'. The field 'dhEvento' has
     * the following description: Data e Hora do Evento, formato
     * UTC (AAAA-MM-DDThh:mm:ssTZD, onde TZD = +hh:mm ou -hh:mm)
     * 
     * @param dhEvento the value of field 'dhEvento'.
     */
    public void setDhEvento(
            final java.lang.String dhEvento) {
        this._dhEvento = dhEvento;
    }

    /**
     * Sets the value of field 'id'. The field 'id' has the
     * following description: Identificador da TAG a ser assinada,
     * a regra de formação do Id é:
     * “ID�? + tpEvento + chave do MDF-e + nSeqEvento
     * 
     * @param id the value of field 'id'.
     */
    public void setId(
            final java.lang.String id) {
        this._id = id;
    }

    /**
     * Sets the value of field 'nSeqEvento'. The field 'nSeqEvento'
     * has the following description: Seqüencial do evento para o
     * mesmo tipo de evento. Para maioria dos eventos será 1, nos
     * casos em que possa existir mais de um evento o autor do
     * evento deve numerar de forma seqüencial.
     * 
     * @param nSeqEvento the value of field 'nSeqEvento'.
     */
    public void setNSeqEvento(
            final java.lang.String nSeqEvento) {
        this._nSeqEvento = nSeqEvento;
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
            final com.mercurio.lms.mdfe.model.v100.types.TAmb tpAmb) {
        this._tpAmb = tpAmb;
    }

    /**
     * Sets the value of field 'tpEvento'. The field 'tpEvento' has
     * the following description: Tipo do Evento:
     * 110111 - Cancelamento
     * 110112 - Encerramento
     * 310620 - Registro de Passagem
     * 
     * 
     * @param tpEvento the value of field 'tpEvento'.
     */
    public void setTpEvento(
            final java.lang.String tpEvento) {
        this._tpEvento = tpEvento;
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
     * com.mercurio.lms.mdfe.model.v100.InfEvento
     */
    public static com.mercurio.lms.mdfe.model.v100.InfEvento unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100.InfEvento) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100.InfEvento.class, reader);
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
