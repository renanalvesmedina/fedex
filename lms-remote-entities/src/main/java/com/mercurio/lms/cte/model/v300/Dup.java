/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v300;

/**
 * Dados das duplicatas
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Dup implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Número da duplicata
     */
    private java.lang.String _nDup;

    /**
     * Data de vencimento da duplicata (AAAA-MM-DD)
     */
    private java.lang.String _dVenc;

    /**
     * Valor da duplicata
     */
    private java.lang.String _vDup;


      //----------------/
     //- Constructors -/
    //----------------/

    public Dup() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dVenc'. The field 'dVenc' has
     * the following description: Data de vencimento da duplicata
     * (AAAA-MM-DD)
     * 
     * @return the value of field 'DVenc'.
     */
    public java.lang.String getDVenc(
    ) {
        return this._dVenc;
    }

    /**
     * Returns the value of field 'nDup'. The field 'nDup' has the
     * following description: Número da duplicata
     * 
     * @return the value of field 'NDup'.
     */
    public java.lang.String getNDup(
    ) {
        return this._nDup;
    }

    /**
     * Returns the value of field 'vDup'. The field 'vDup' has the
     * following description: Valor da duplicata
     * 
     * @return the value of field 'VDup'.
     */
    public java.lang.String getVDup(
    ) {
        return this._vDup;
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
     * Sets the value of field 'dVenc'. The field 'dVenc' has the
     * following description: Data de vencimento da duplicata
     * (AAAA-MM-DD)
     * 
     * @param dVenc the value of field 'dVenc'.
     */
    public void setDVenc(
            final java.lang.String dVenc) {
        this._dVenc = dVenc;
    }

    /**
     * Sets the value of field 'nDup'. The field 'nDup' has the
     * following description: Número da duplicata
     * 
     * @param nDup the value of field 'nDup'.
     */
    public void setNDup(
            final java.lang.String nDup) {
        this._nDup = nDup;
    }

    /**
     * Sets the value of field 'vDup'. The field 'vDup' has the
     * following description: Valor da duplicata
     * 
     * @param vDup the value of field 'vDup'.
     */
    public void setVDup(
            final java.lang.String vDup) {
        this._vDup = vDup;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v300.Dup
     */
    public static com.mercurio.lms.cte.model.v300.Dup unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v300.Dup) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v300.Dup.class, reader);
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
