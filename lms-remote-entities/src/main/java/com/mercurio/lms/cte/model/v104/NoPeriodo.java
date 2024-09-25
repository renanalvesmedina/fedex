/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Entrega no período definido
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class NoPeriodo implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Tipo período
     */
    private com.mercurio.lms.cte.model.v104.types.TpPerType _tpPer;

    /**
     * Data inicial 
     */
    private java.lang.String _dIni;

    /**
     * Data final 
     */
    private java.lang.String _dFim;


      //----------------/
     //- Constructors -/
    //----------------/

    public NoPeriodo() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dFim'. The field 'dFim' has the
     * following description: Data final 
     * 
     * @return the value of field 'DFim'.
     */
    public java.lang.String getDFim(
    ) {
        return this._dFim;
    }

    /**
     * Returns the value of field 'dIni'. The field 'dIni' has the
     * following description: Data inicial 
     * 
     * @return the value of field 'DIni'.
     */
    public java.lang.String getDIni(
    ) {
        return this._dIni;
    }

    /**
     * Returns the value of field 'tpPer'. The field 'tpPer' has
     * the following description: Tipo período
     * 
     * @return the value of field 'TpPer'.
     */
    public com.mercurio.lms.cte.model.v104.types.TpPerType getTpPer(
    ) {
        return this._tpPer;
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
     * Sets the value of field 'dFim'. The field 'dFim' has the
     * following description: Data final 
     * 
     * @param dFim the value of field 'dFim'.
     */
    public void setDFim(
            final java.lang.String dFim) {
        this._dFim = dFim;
    }

    /**
     * Sets the value of field 'dIni'. The field 'dIni' has the
     * following description: Data inicial 
     * 
     * @param dIni the value of field 'dIni'.
     */
    public void setDIni(
            final java.lang.String dIni) {
        this._dIni = dIni;
    }

    /**
     * Sets the value of field 'tpPer'. The field 'tpPer' has the
     * following description: Tipo período
     * 
     * @param tpPer the value of field 'tpPer'.
     */
    public void setTpPer(
            final com.mercurio.lms.cte.model.v104.types.TpPerType tpPer) {
        this._tpPer = tpPer;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.NoPeriodo
     */
    public static com.mercurio.lms.cte.model.v104.NoPeriodo unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.NoPeriodo) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.NoPeriodo.class, reader);
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
