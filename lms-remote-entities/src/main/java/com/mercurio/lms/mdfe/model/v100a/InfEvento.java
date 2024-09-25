/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100a;

/**
 * 
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfEvento implements java.io.Serializable {

    /**
     * Identificador da TAG a ser assinada, a regra de formação
     * do Id é:
     * “ID�? + tpEvento + chave do MDF-e + nSeqEvento
     */
    private java.lang.String id;

    /**
     * Código do órgão de recepção do Evento. Utilizar a
     * Tabela do IBGE extendida, utilizar 90 para identificar
     * SUFRAMA, 91 para RFB, 92 para BackOffice BRId e 93 para ONE
     */
    private com.mercurio.lms.mdfe.model.v100a.types.TCOrgaoIBGE cOrgao;

    /**
     * Identificação do Ambiente:
     * 1 - Produção
     * 2 - Homologação
     */
    private com.mercurio.lms.mdfe.model.v100a.types.TAmb tpAmb;

    /**
     * CNPJ do emissor do evento
     */
    private java.lang.String CNPJ;

    /**
     * Chave de Acesso do MDF-e vinculado ao evento
     */
    private java.lang.String chMDFe;

    /**
     * Data e Hora do Evento, formato AAAA-MM-DDThh:mm:ss
     */
    private java.lang.String dhEvento;

    /**
     * Tipo do Evento:
     * 110111 - Cancelamento
     * 110112 - Encerramento
     * 110114 - Inclusão de Condutor
     * 310620 - Registro de Passagem
     * 510620 - Registro de Passagem BRId
     */
    private java.lang.String tpEvento;

    /**
     * Seqüencial do evento para o mesmo tipo de evento. Para
     * maioria dos eventos será 1, nos casos em que possa existir
     * mais de um evento o autor do evento deve numerar de forma
     * seqüencial.
     */
    private java.lang.String nSeqEvento;

    /**
     * Detalhamento do evento específico
     */
    private com.mercurio.lms.mdfe.model.v100a.DetEvento detEvento;

    public InfEvento() {
        super();
    }

    /**
     * Returns the value of field 'CNPJ'. The field 'CNPJ' has the
     * following description: CNPJ do emissor do evento
     * 
     * @return the value of field 'CNPJ'.
     */
    public java.lang.String getCNPJ() {
        return this.CNPJ;
    }

    /**
     * Returns the value of field 'cOrgao'. The field 'cOrgao' has
     * the following description: Código do órgão de recepção
     * do Evento. Utilizar a Tabela do IBGE extendida, utilizar 90
     * para identificar SUFRAMA, 91 para RFB, 92 para BackOffice
     * BRId e 93 para ONE
     * 
     * @return the value of field 'COrgao'.
     */
    public com.mercurio.lms.mdfe.model.v100a.types.TCOrgaoIBGE getCOrgao() {
        return this.cOrgao;
    }

    /**
     * Returns the value of field 'chMDFe'. The field 'chMDFe' has
     * the following description: Chave de Acesso do MDF-e
     * vinculado ao evento
     * 
     * @return the value of field 'ChMDFe'.
     */
    public java.lang.String getChMDFe() {
        return this.chMDFe;
    }

    /**
     * Returns the value of field 'detEvento'. The field
     * 'detEvento' has the following description: Detalhamento do
     * evento específico
     * 
     * @return the value of field 'DetEvento'.
     */
    public com.mercurio.lms.mdfe.model.v100a.DetEvento getDetEvento() {
        return this.detEvento;
    }

    /**
     * Returns the value of field 'dhEvento'. The field 'dhEvento'
     * has the following description: Data e Hora do Evento,
     * formato AAAA-MM-DDThh:mm:ss
     * 
     * @return the value of field 'DhEvento'.
     */
    public java.lang.String getDhEvento() {
        return this.dhEvento;
    }

    /**
     * Returns the value of field 'id'. The field 'id' has the
     * following description: Identificador da TAG a ser assinada,
     * a regra de formação do Id é:
     * “ID�? + tpEvento + chave do MDF-e + nSeqEvento
     * 
     * @return the value of field 'Id'.
     */
    public java.lang.String getId() {
        return this.id;
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
    public java.lang.String getNSeqEvento() {
        return this.nSeqEvento;
    }

    /**
     * Returns the value of field 'tpAmb'. The field 'tpAmb' has
     * the following description: Identificação do Ambiente:
     * 1 - Produção
     * 2 - Homologação
     * 
     * @return the value of field 'TpAmb'.
     */
    public com.mercurio.lms.mdfe.model.v100a.types.TAmb getTpAmb() {
        return this.tpAmb;
    }

    /**
     * Returns the value of field 'tpEvento'. The field 'tpEvento'
     * has the following description: Tipo do Evento:
     * 110111 - Cancelamento
     * 110112 - Encerramento
     * 110114 - Inclusão de Condutor
     * 310620 - Registro de Passagem
     * 510620 - Registro de Passagem BRId
     * 
     * @return the value of field 'TpEvento'.
     */
    public java.lang.String getTpEvento() {
        return this.tpEvento;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid() {
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
    public void marshal(final java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
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
    public void marshal(final org.xml.sax.ContentHandler handler) throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'CNPJ'. The field 'CNPJ' has the
     * following description: CNPJ do emissor do evento
     * 
     * @param CNPJ the value of field 'CNPJ'.
     */
    public void setCNPJ(final java.lang.String CNPJ) {
        this.CNPJ = CNPJ;
    }

    /**
     * Sets the value of field 'cOrgao'. The field 'cOrgao' has the
     * following description: Código do órgão de recepção do
     * Evento. Utilizar a Tabela do IBGE extendida, utilizar 90
     * para identificar SUFRAMA, 91 para RFB, 92 para BackOffice
     * BRId e 93 para ONE
     * 
     * @param cOrgao the value of field 'cOrgao'.
     */
    public void setCOrgao(final com.mercurio.lms.mdfe.model.v100a.types.TCOrgaoIBGE cOrgao) {
        this.cOrgao = cOrgao;
    }

    /**
     * Sets the value of field 'chMDFe'. The field 'chMDFe' has the
     * following description: Chave de Acesso do MDF-e vinculado ao
     * evento
     * 
     * @param chMDFe the value of field 'chMDFe'.
     */
    public void setChMDFe(final java.lang.String chMDFe) {
        this.chMDFe = chMDFe;
    }

    /**
     * Sets the value of field 'detEvento'. The field 'detEvento'
     * has the following description: Detalhamento do evento
     * específico
     * 
     * @param detEvento the value of field 'detEvento'.
     */
    public void setDetEvento(final com.mercurio.lms.mdfe.model.v100a.DetEvento detEvento) {
        this.detEvento = detEvento;
    }

    /**
     * Sets the value of field 'dhEvento'. The field 'dhEvento' has
     * the following description: Data e Hora do Evento, formato
     * AAAA-MM-DDThh:mm:ss
     * 
     * @param dhEvento the value of field 'dhEvento'.
     */
    public void setDhEvento(final java.lang.String dhEvento) {
        this.dhEvento = dhEvento;
    }

    /**
     * Sets the value of field 'id'. The field 'id' has the
     * following description: Identificador da TAG a ser assinada,
     * a regra de formação do Id é:
     * “ID�? + tpEvento + chave do MDF-e + nSeqEvento
     * 
     * @param id the value of field 'id'.
     */
    public void setId(final java.lang.String id) {
        this.id = id;
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
    public void setNSeqEvento(final java.lang.String nSeqEvento) {
        this.nSeqEvento = nSeqEvento;
    }

    /**
     * Sets the value of field 'tpAmb'. The field 'tpAmb' has the
     * following description: Identificação do Ambiente:
     * 1 - Produção
     * 2 - Homologação
     * 
     * @param tpAmb the value of field 'tpAmb'.
     */
    public void setTpAmb(final com.mercurio.lms.mdfe.model.v100a.types.TAmb tpAmb) {
        this.tpAmb = tpAmb;
    }

    /**
     * Sets the value of field 'tpEvento'. The field 'tpEvento' has
     * the following description: Tipo do Evento:
     * 110111 - Cancelamento
     * 110112 - Encerramento
     * 110114 - Inclusão de Condutor
     * 310620 - Registro de Passagem
     * 510620 - Registro de Passagem BRId
     * 
     * @param tpEvento the value of field 'tpEvento'.
     */
    public void setTpEvento(final java.lang.String tpEvento) {
        this.tpEvento = tpEvento;
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
     * com.mercurio.lms.mdfe.model.v100a.InfEvento
     */
    public static com.mercurio.lms.mdfe.model.v100a.InfEvento unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100a.InfEvento) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100a.InfEvento.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
