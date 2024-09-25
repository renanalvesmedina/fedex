/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Entrega no intervalo de horário definido
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class NoInter implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Tipo de hora
     */
    private com.mercurio.lms.cte.model.v104.types.TpHorType _tpHor;

    /**
     * Hora inicial 
     */
    private java.lang.String _hIni;

    /**
     * Hora final 
     */
    private java.lang.String _hFim;


      //----------------/
     //- Constructors -/
    //----------------/

    public NoInter() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'hFim'. The field 'hFim' has the
     * following description: Hora final 
     * 
     * @return the value of field 'HFim'.
     */
    public java.lang.String getHFim(
    ) {
        return this._hFim;
    }

    /**
     * Returns the value of field 'hIni'. The field 'hIni' has the
     * following description: Hora inicial 
     * 
     * @return the value of field 'HIni'.
     */
    public java.lang.String getHIni(
    ) {
        return this._hIni;
    }

    /**
     * Returns the value of field 'tpHor'. The field 'tpHor' has
     * the following description: Tipo de hora
     * 
     * @return the value of field 'TpHor'.
     */
    public com.mercurio.lms.cte.model.v104.types.TpHorType getTpHor(
    ) {
        return this._tpHor;
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
     * Sets the value of field 'hFim'. The field 'hFim' has the
     * following description: Hora final 
     * 
     * @param hFim the value of field 'hFim'.
     */
    public void setHFim(
            final java.lang.String hFim) {
        this._hFim = hFim;
    }

    /**
     * Sets the value of field 'hIni'. The field 'hIni' has the
     * following description: Hora inicial 
     * 
     * @param hIni the value of field 'hIni'.
     */
    public void setHIni(
            final java.lang.String hIni) {
        this._hIni = hIni;
    }

    /**
     * Sets the value of field 'tpHor'. The field 'tpHor' has the
     * following description: Tipo de hora
     * 
     * @param tpHor the value of field 'tpHor'.
     */
    public void setTpHor(
            final com.mercurio.lms.cte.model.v104.types.TpHorType tpHor) {
        this._tpHor = tpHor;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.NoInter
     */
    public static com.mercurio.lms.cte.model.v104.NoInter unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.NoInter) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.NoInter.class, reader);
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
