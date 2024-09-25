/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 * Dados do Recibo do Arquivo
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfRec implements java.io.Serializable {

    /**
     * Número do Recibo
     */
    private java.lang.String nRec;

    /**
     * Data e hora do recebimento, no formato AAAA-MM-DDTHH:MM:SS
     */
    private java.util.Date dhRecbto;

    /**
     * Tempo médio de resposta do serviço (em segundos) dos
     * últimos 5 minutos
     */
    private long tMed;

    /**
     * Keeps track of whether primitive field tMed has been set
     * already.
     */
    private boolean hastMed;

    public InfRec() {
        super();
    }

    /**
     */
    public void deleteTMed() {
        this.hastMed= false;
    }

    /**
     * Returns the value of field 'dhRecbto'. The field 'dhRecbto'
     * has the following description: Data e hora do recebimento,
     * no formato AAAA-MM-DDTHH:MM:SS
     * 
     * @return the value of field 'DhRecbto'.
     */
    public java.util.Date getDhRecbto() {
        return this.dhRecbto;
    }

    /**
     * Returns the value of field 'nRec'. The field 'nRec' has the
     * following description: Número do Recibo
     * 
     * @return the value of field 'NRec'.
     */
    public java.lang.String getNRec() {
        return this.nRec;
    }

    /**
     * Returns the value of field 'tMed'. The field 'tMed' has the
     * following description: Tempo médio de resposta do serviço
     * (em segundos) dos últimos 5 minutos
     * 
     * @return the value of field 'TMed'.
     */
    public long getTMed() {
        return this.tMed;
    }

    /**
     * Method hasTMed.
     * 
     * @return true if at least one TMed has been added
     */
    public boolean hasTMed() {
        return this.hastMed;
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
     * Sets the value of field 'dhRecbto'. The field 'dhRecbto' has
     * the following description: Data e hora do recebimento, no
     * formato AAAA-MM-DDTHH:MM:SS
     * 
     * @param dhRecbto the value of field 'dhRecbto'.
     */
    public void setDhRecbto(final java.util.Date dhRecbto) {
        this.dhRecbto = dhRecbto;
    }

    /**
     * Sets the value of field 'nRec'. The field 'nRec' has the
     * following description: Número do Recibo
     * 
     * @param nRec the value of field 'nRec'.
     */
    public void setNRec(final java.lang.String nRec) {
        this.nRec = nRec;
    }

    /**
     * Sets the value of field 'tMed'. The field 'tMed' has the
     * following description: Tempo médio de resposta do serviço
     * (em segundos) dos últimos 5 minutos
     * 
     * @param tMed the value of field 'tMed'.
     */
    public void setTMed(final long tMed) {
        this.tMed = tMed;
        this.hastMed = true;
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
     * com.mercurio.lms.mdfe.model.v300.InfRec
     */
    public static com.mercurio.lms.mdfe.model.v300.InfRec unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.InfRec) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.InfRec.class, reader);
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
