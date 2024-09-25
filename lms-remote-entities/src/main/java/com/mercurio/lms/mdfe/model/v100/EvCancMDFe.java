/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100;

/**
 * Schema XML de validação do evento do cancelamento 
 * 110111
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class EvCancMDFe implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Descrição do Evento - “Cancelamento�?
     */
    private com.mercurio.lms.mdfe.model.v100.types.DescEventoType _descEvento;

    /**
     * Número do Protocolo de Status do MDF-e. 1 posição tipo de
     * autorizador (1 – Secretaria de Fazenda Estadual; 7 - SEFAZ
     * Nacional ); 2 posições ano; 10 seqüencial no ano.
     */
    private java.lang.String _nProt;

    /**
     * Justificativa do Cancelamento
     */
    private java.lang.String _xJust;


      //----------------/
     //- Constructors -/
    //----------------/

    public EvCancMDFe() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'descEvento'. The field
     * 'descEvento' has the following description: Descrição do
     * Evento - “Cancelamento�?
     * 
     * @return the value of field 'DescEvento'.
     */
    public com.mercurio.lms.mdfe.model.v100.types.DescEventoType getDescEvento(
    ) {
        return this._descEvento;
    }

    /**
     * Returns the value of field 'nProt'. The field 'nProt' has
     * the following description: Número do Protocolo de Status do
     * MDF-e. 1 posição tipo de autorizador (1 – Secretaria de
     * Fazenda Estadual; 7 - SEFAZ Nacional ); 2 posições ano; 10
     * seqüencial no ano.
     * 
     * @return the value of field 'NProt'.
     */
    public java.lang.String getNProt(
    ) {
        return this._nProt;
    }

    /**
     * Returns the value of field 'xJust'. The field 'xJust' has
     * the following description: Justificativa do Cancelamento
     * 
     * @return the value of field 'XJust'.
     */
    public java.lang.String getXJust(
    ) {
        return this._xJust;
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
     * Sets the value of field 'descEvento'. The field 'descEvento'
     * has the following description: Descrição do Evento -
     * “Cancelamento�?
     * 
     * @param descEvento the value of field 'descEvento'.
     */
    public void setDescEvento(
            final com.mercurio.lms.mdfe.model.v100.types.DescEventoType descEvento) {
        this._descEvento = descEvento;
    }

    /**
     * Sets the value of field 'nProt'. The field 'nProt' has the
     * following description: Número do Protocolo de Status do
     * MDF-e. 1 posição tipo de autorizador (1 – Secretaria de
     * Fazenda Estadual; 7 - SEFAZ Nacional ); 2 posições ano; 10
     * seqüencial no ano.
     * 
     * @param nProt the value of field 'nProt'.
     */
    public void setNProt(
            final java.lang.String nProt) {
        this._nProt = nProt;
    }

    /**
     * Sets the value of field 'xJust'. The field 'xJust' has the
     * following description: Justificativa do Cancelamento
     * 
     * @param xJust the value of field 'xJust'.
     */
    public void setXJust(
            final java.lang.String xJust) {
        this._xJust = xJust;
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
     * com.mercurio.lms.mdfe.model.v100.EvCancMDFe
     */
    public static com.mercurio.lms.mdfe.model.v100.EvCancMDFe unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100.EvCancMDFe) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100.EvCancMDFe.class, reader);
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
