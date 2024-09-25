/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Identificação do Emitente do CT-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Emit implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * CNPJ do emitente
     */
    private String _CNPJ;

    /**
     * Inscrição Estadual do Emitente
     */
    private String _IE;

    /**
     * Razão social ou Nome do emitente
     */
    private String _xNome;

    /**
     * Nome fantasia
     */
    private String _xFant;

    /**
     * Endereço do emitente
     */
    private EnderEmit _enderEmit;

    /**
     * Código do Regime Tributário
     */
    private String _CRT;


      //----------------/
     //- Constructors -/
    //----------------/

    public Emit() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'CNPJ'. The field 'CNPJ' has the
     * following description: CNPJ do emitente
     *
     * @return the value of field 'CNPJ'.
     */
    public String getCNPJ(
    ) {
        return this._CNPJ;
    }

    /**
     * Returns the value of field 'enderEmit'. The field
     * 'enderEmit' has the following description: Endereço do
     * emitente
     *
     * @return the value of field 'EnderEmit'.
     */
    public EnderEmit getEnderEmit(
    ) {
        return this._enderEmit;
    }

    /**
     * Returns the value of field 'IE'. The field 'IE' has the
     * following description: Inscrição Estadual do Emitente
     *
     * @return the value of field 'IE'.
     */
    public String getIE(
    ) {
        return this._IE;
    }

    /**
     * Returns the value of field 'xFant'. The field 'xFant' has
     * the following description: Nome fantasia
     *
     * @return the value of field 'XFant'.
     */
    public String getXFant(
    ) {
        return this._xFant;
    }

    /**
     * Returns the value of field 'xNome'. The field 'xNome' has
     * the following description: Razão social ou Nome do emitente
     *
     * @return the value of field 'XNome'.
     */
    public String getXNome(
    ) {
        return this._xNome;
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
     * Sets the value of field 'CNPJ'. The field 'CNPJ' has the
     * following description: CNPJ do emitente
     *
     * @param CNPJ the value of field 'CNPJ'.
     */
    public void setCNPJ(
            final String CNPJ) {
        this._CNPJ = CNPJ;
    }

    /**
     * Sets the value of field 'enderEmit'. The field 'enderEmit'
     * has the following description: Endereço do emitente
     *
     * @param enderEmit the value of field 'enderEmit'.
     */
    public void setEnderEmit(
            final EnderEmit enderEmit) {
        this._enderEmit = enderEmit;
    }

    /**
     * Sets the value of field 'IE'. The field 'IE' has the
     * following description: Inscrição Estadual do Emitente
     *
     * @param IE the value of field 'IE'.
     */
    public void setIE(
            final String IE) {
        this._IE = IE;
    }

    /**
     * Sets the value of field 'xFant'. The field 'xFant' has the
     * following description: Nome fantasia
     *
     * @param xFant the value of field 'xFant'.
     */
    public void setXFant(
            final String xFant) {
        this._xFant = xFant;
    }

    /**
     * Sets the value of field 'xNome'. The field 'xNome' has the
     * following description: Razão social ou Nome do emitente
     *
     * @param xNome the value of field 'xNome'.
     */
    public void setXNome(
            final String xNome) {
        this._xNome = xNome;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v400.Emit
     */
    public static Emit unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (Emit) org.exolab.castor.xml.Unmarshaller.unmarshal(Emit.class, reader);
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

    /**
     * Sets the value of field '_CRT'. The field '_CRT' has the
     * following description: Código do regime tributário
     *
     * @param _CRT the value of field '_CRT'.
     */
    public void setCRT(String _CRT) {
        this._CRT = _CRT;
    }

    /**
     * Returns the value of field '_CRT'. The field '_CRT' has
     * the following description: Código do regime tributário
     *
     * @return the value of field '_CRT'.
     */
    public String getCRT() {
        return _CRT;
    }

}
