/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v300;

/**
 * Dados do Recibo do Lote
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfRec implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Número do Recibo
     */
    private java.lang.String _nRec;

    /**
     * Data e hora do recebimento, no formato AAAA-MM-DDTHH:MM:SS
     */
    private java.util.Date _dhRecbto;

    /**
     * Tempo médio de resposta do serviço (em segundos) dos
     * últimos 5 minutos
     */
    private long _tMed;

    /**
     * keeps track of state for field: _tMed
     */
    private boolean _has_tMed;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfRec() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteTMed(
    ) {
        this._has_tMed= false;
    }

    /**
     * Returns the value of field 'dhRecbto'. The field 'dhRecbto'
     * has the following description: Data e hora do recebimento,
     * no formato AAAA-MM-DDTHH:MM:SS
     * 
     * @return the value of field 'DhRecbto'.
     */
    public java.util.Date getDhRecbto(
    ) {
        return this._dhRecbto;
    }

    /**
     * Returns the value of field 'nRec'. The field 'nRec' has the
     * following description: Número do Recibo
     * 
     * @return the value of field 'NRec'.
     */
    public java.lang.String getNRec(
    ) {
        return this._nRec;
    }

    /**
     * Returns the value of field 'tMed'. The field 'tMed' has the
     * following description: Tempo médio de resposta do serviço
     * (em segundos) dos últimos 5 minutos
     * 
     * @return the value of field 'TMed'.
     */
    public long getTMed(
    ) {
        return this._tMed;
    }

    /**
     * Method hasTMed.
     * 
     * @return true if at least one TMed has been added
     */
    public boolean hasTMed(
    ) {
        return this._has_tMed;
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
     * Sets the value of field 'dhRecbto'. The field 'dhRecbto' has
     * the following description: Data e hora do recebimento, no
     * formato AAAA-MM-DDTHH:MM:SS
     * 
     * @param dhRecbto the value of field 'dhRecbto'.
     */
    public void setDhRecbto(
            final java.util.Date dhRecbto) {
        this._dhRecbto = dhRecbto;
    }

    /**
     * Sets the value of field 'nRec'. The field 'nRec' has the
     * following description: Número do Recibo
     * 
     * @param nRec the value of field 'nRec'.
     */
    public void setNRec(
            final java.lang.String nRec) {
        this._nRec = nRec;
    }

    /**
     * Sets the value of field 'tMed'. The field 'tMed' has the
     * following description: Tempo médio de resposta do serviço
     * (em segundos) dos últimos 5 minutos
     * 
     * @param tMed the value of field 'tMed'.
     */
    public void setTMed(
            final long tMed) {
        this._tMed = tMed;
        this._has_tMed = true;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v300.InfRe
     */
    public static com.mercurio.lms.cte.model.v300.InfRec unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v300.InfRec) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v300.InfRec.class, reader);
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
