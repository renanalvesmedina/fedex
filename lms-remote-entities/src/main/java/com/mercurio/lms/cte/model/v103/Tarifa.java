/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Informações de tarifa
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Tarifa implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _trecho.
     */
    private java.lang.String _trecho;

    /**
     * Field _CL.
     */
    private java.lang.String _CL;

    /**
     * Código da Tarifa
     */
    private java.lang.String _cTar;

    /**
     * Valor da Tarifa
     */
    private java.lang.String _vTar;


      //----------------/
     //- Constructors -/
    //----------------/

    public Tarifa() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'CL'.
     * 
     * @return the value of field 'CL'.
     */
    public java.lang.String getCL(
    ) {
        return this._CL;
    }

    /**
     * Returns the value of field 'cTar'. The field 'cTar' has the
     * following description: Código da Tarifa
     * 
     * @return the value of field 'CTar'.
     */
    public java.lang.String getCTar(
    ) {
        return this._cTar;
    }

    /**
     * Returns the value of field 'trecho'.
     * 
     * @return the value of field 'Trecho'.
     */
    public java.lang.String getTrecho(
    ) {
        return this._trecho;
    }

    /**
     * Returns the value of field 'vTar'. The field 'vTar' has the
     * following description: Valor da Tarifa
     * 
     * @return the value of field 'VTar'.
     */
    public java.lang.String getVTar(
    ) {
        return this._vTar;
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
     * Sets the value of field 'CL'.
     * 
     * @param CL the value of field 'CL'.
     */
    public void setCL(
            final java.lang.String CL) {
        this._CL = CL;
    }

    /**
     * Sets the value of field 'cTar'. The field 'cTar' has the
     * following description: Código da Tarifa
     * 
     * @param cTar the value of field 'cTar'.
     */
    public void setCTar(
            final java.lang.String cTar) {
        this._cTar = cTar;
    }

    /**
     * Sets the value of field 'trecho'.
     * 
     * @param trecho the value of field 'trecho'.
     */
    public void setTrecho(
            final java.lang.String trecho) {
        this._trecho = trecho;
    }

    /**
     * Sets the value of field 'vTar'. The field 'vTar' has the
     * following description: Valor da Tarifa
     * 
     * @param vTar the value of field 'vTar'.
     */
    public void setVTar(
            final java.lang.String vTar) {
        this._vTar = vTar;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v103.Tarif
     */
    public static com.mercurio.lms.cte.model.v103.Tarifa unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.Tarifa) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.Tarifa.class, reader);
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
