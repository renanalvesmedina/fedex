/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100a;

/**
 * Schema XML de validação do evento do encerramento 
 * 110112
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class EvEncMDFe implements java.io.Serializable {

    /**
     * Descrição do Evento - “Encerramento�?
     */
    private com.mercurio.lms.mdfe.model.v100a.types.DescEventoType descEvento;

    /**
     * Número do Protocolo de Status do MDF-e. 
     * 1 posição tipo de autorizador (9 - SEFAZ Nacional ); 
     * 2 posições ano;
     * 10 seqüencial no ano.
     */
    private java.lang.String nProt;

    /**
     * Data que o Manifesto foi encerrado
     */
    private java.lang.String dtEnc;

    /**
     * UF de encerramento do Manifesto
     */
    private com.mercurio.lms.mdfe.model.v100a.types.TCodUfIBGE cUF;

    /**
     * Código do Município de Encerramento do manifesto
     */
    private java.lang.String cMun;

    public EvEncMDFe() {
        super();
    }

    /**
     * Returns the value of field 'cMun'. The field 'cMun' has the
     * following description: Código do Município de Encerramento
     * do manifesto
     * 
     * @return the value of field 'CMun'.
     */
    public java.lang.String getCMun() {
        return this.cMun;
    }

    /**
     * Returns the value of field 'cUF'. The field 'cUF' has the
     * following description: UF de encerramento do Manifesto
     * 
     * @return the value of field 'CUF'.
     */
    public com.mercurio.lms.mdfe.model.v100a.types.TCodUfIBGE getCUF() {
        return this.cUF;
    }

    /**
     * Returns the value of field 'descEvento'. The field
     * 'descEvento' has the following description: Descrição do
     * Evento - “Encerramento�?
     * 
     * @return the value of field 'DescEvento'.
     */
    public com.mercurio.lms.mdfe.model.v100a.types.DescEventoType getDescEvento() {
        return this.descEvento;
    }

    /**
     * Returns the value of field 'dtEnc'. The field 'dtEnc' has
     * the following description: Data que o Manifesto foi
     * encerrado
     * 
     * @return the value of field 'DtEnc'.
     */
    public java.lang.String getDtEnc() {
        return this.dtEnc;
    }

    /**
     * Returns the value of field 'nProt'. The field 'nProt' has
     * the following description: Número do Protocolo de Status do
     * MDF-e. 
     * 1 posição tipo de autorizador (9 - SEFAZ Nacional ); 
     * 2 posições ano;
     * 10 seqüencial no ano.
     * 
     * @return the value of field 'NProt'.
     */
    public java.lang.String getNProt() {
        return this.nProt;
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
     * Sets the value of field 'cMun'. The field 'cMun' has the
     * following description: Código do Município de Encerramento
     * do manifesto
     * 
     * @param cMun the value of field 'cMun'.
     */
    public void setCMun(final java.lang.String cMun) {
        this.cMun = cMun;
    }

    /**
     * Sets the value of field 'cUF'. The field 'cUF' has the
     * following description: UF de encerramento do Manifesto
     * 
     * @param cUF the value of field 'cUF'.
     */
    public void setCUF(final com.mercurio.lms.mdfe.model.v100a.types.TCodUfIBGE cUF) {
        this.cUF = cUF;
    }

    /**
     * Sets the value of field 'descEvento'. The field 'descEvento'
     * has the following description: Descrição do Evento -
     * “Encerramento�?
     * 
     * @param descEvento the value of field 'descEvento'.
     */
    public void setDescEvento(final com.mercurio.lms.mdfe.model.v100a.types.DescEventoType descEvento) {
        this.descEvento = descEvento;
    }

    /**
     * Sets the value of field 'dtEnc'. The field 'dtEnc' has the
     * following description: Data que o Manifesto foi encerrado
     * 
     * @param dtEnc the value of field 'dtEnc'.
     */
    public void setDtEnc(final java.lang.String dtEnc) {
        this.dtEnc = dtEnc;
    }

    /**
     * Sets the value of field 'nProt'. The field 'nProt' has the
     * following description: Número do Protocolo de Status do
     * MDF-e. 
     * 1 posição tipo de autorizador (9 - SEFAZ Nacional ); 
     * 2 posições ano;
     * 10 seqüencial no ano.
     * 
     * @param nProt the value of field 'nProt'.
     */
    public void setNProt(final java.lang.String nProt) {
        this.nProt = nProt;
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
     * com.mercurio.lms.mdfe.model.v100a.EvEncMDFe
     */
    public static com.mercurio.lms.mdfe.model.v100a.EvEncMDFe unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100a.EvEncMDFe) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100a.EvEncMDFe.class, reader);
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
