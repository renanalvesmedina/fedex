/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Informações do modal Aéreo
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Aereo implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Número da Minuta
     */
    private java.lang.String _nMinu;

    /**
     * Número da Operacional do Conhecimento Aéreo
     */
    private java.lang.String _nOCA;

    /**
     * Data prevista da entrega
     */
    private java.lang.String _dPrev;

    /**
     * Loja Agente Emissor
     */
    private java.lang.String _xLAgEmi;

    /**
     * código IATA
     */
    private java.lang.String _cIATA;

    /**
     * Informações de tarifa
     */
    private com.mercurio.lms.cte.model.v103.Tarifa _tarifa;


      //----------------/
     //- Constructors -/
    //----------------/

    public Aereo() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'cIATA'. The field 'cIATA' has
     * the following description: código IATA
     * 
     * @return the value of field 'CIATA'.
     */
    public java.lang.String getCIATA(
    ) {
        return this._cIATA;
    }

    /**
     * Returns the value of field 'dPrev'. The field 'dPrev' has
     * the following description: Data prevista da entrega
     * 
     * @return the value of field 'DPrev'.
     */
    public java.lang.String getDPrev(
    ) {
        return this._dPrev;
    }

    /**
     * Returns the value of field 'nMinu'. The field 'nMinu' has
     * the following description: Número da Minuta
     * 
     * @return the value of field 'NMinu'.
     */
    public java.lang.String getNMinu(
    ) {
        return this._nMinu;
    }

    /**
     * Returns the value of field 'nOCA'. The field 'nOCA' has the
     * following description: Número da Operacional do
     * Conhecimento Aéreo
     * 
     * @return the value of field 'NOCA'.
     */
    public java.lang.String getNOCA(
    ) {
        return this._nOCA;
    }

    /**
     * Returns the value of field 'tarifa'. The field 'tarifa' has
     * the following description: Informações de tarifa
     * 
     * @return the value of field 'Tarifa'.
     */
    public com.mercurio.lms.cte.model.v103.Tarifa getTarifa(
    ) {
        return this._tarifa;
    }

    /**
     * Returns the value of field 'xLAgEmi'. The field 'xLAgEmi'
     * has the following description: Loja Agente Emissor
     * 
     * @return the value of field 'XLAgEmi'.
     */
    public java.lang.String getXLAgEmi(
    ) {
        return this._xLAgEmi;
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
     * Sets the value of field 'cIATA'. The field 'cIATA' has the
     * following description: código IATA
     * 
     * @param cIATA the value of field 'cIATA'.
     */
    public void setCIATA(
            final java.lang.String cIATA) {
        this._cIATA = cIATA;
    }

    /**
     * Sets the value of field 'dPrev'. The field 'dPrev' has the
     * following description: Data prevista da entrega
     * 
     * @param dPrev the value of field 'dPrev'.
     */
    public void setDPrev(
            final java.lang.String dPrev) {
        this._dPrev = dPrev;
    }

    /**
     * Sets the value of field 'nMinu'. The field 'nMinu' has the
     * following description: Número da Minuta
     * 
     * @param nMinu the value of field 'nMinu'.
     */
    public void setNMinu(
            final java.lang.String nMinu) {
        this._nMinu = nMinu;
    }

    /**
     * Sets the value of field 'nOCA'. The field 'nOCA' has the
     * following description: Número da Operacional do
     * Conhecimento Aéreo
     * 
     * @param nOCA the value of field 'nOCA'.
     */
    public void setNOCA(
            final java.lang.String nOCA) {
        this._nOCA = nOCA;
    }

    /**
     * Sets the value of field 'tarifa'. The field 'tarifa' has the
     * following description: Informações de tarifa
     * 
     * @param tarifa the value of field 'tarifa'.
     */
    public void setTarifa(
            final com.mercurio.lms.cte.model.v103.Tarifa tarifa) {
        this._tarifa = tarifa;
    }

    /**
     * Sets the value of field 'xLAgEmi'. The field 'xLAgEmi' has
     * the following description: Loja Agente Emissor
     * 
     * @param xLAgEmi the value of field 'xLAgEmi'.
     */
    public void setXLAgEmi(
            final java.lang.String xLAgEmi) {
        this._xLAgEmi = xLAgEmi;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v103.Aereo
     */
    public static com.mercurio.lms.cte.model.v103.Aereo unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.Aereo) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.Aereo.class, reader);
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
