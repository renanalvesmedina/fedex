/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v200;

/**
 * Informações relativas aos Impostos
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Imp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Informações relativas ao ICMS
     */
    private com.mercurio.lms.cte.model.v200.ICMS _ICMS;

    /**
     * Valor Total dos Tributos
     */
    private java.lang.String _vTotTrib;

    /**
     * Informações adicionais de interesse do Fisco
     */
    private java.lang.String _infAdFisco;

    /**
     * Informações do ICMS de partilha com a UF de término do
     * serviço de transporte na operação interestadual
     */
    private com.mercurio.lms.cte.model.v200.ICMSUFFim _ICMSUFFim;


      //----------------/
     //- Constructors -/
    //----------------/

    public Imp() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'ICMS'. The field 'ICMS' has the
     * following description: Informações relativas ao ICMS
     * 
     * @return the value of field 'ICMS'.
     */
    public com.mercurio.lms.cte.model.v200.ICMS getICMS(
    ) {
        return this._ICMS;
    }

    /**
     * Returns the value of field 'ICMSUFFim'. The field
     * 'ICMSUFFim' has the following description: Informações do
     * ICMS de partilha com a UF de término do serviço de
     * transporte na operação interestadual
     * 
     * @return the value of field 'ICMSUFFim'.
     */
    public com.mercurio.lms.cte.model.v200.ICMSUFFim getICMSUFFim(
    ) {
        return this._ICMSUFFim;
    }

    /**
     * Returns the value of field 'infAdFisco'. The field
     * 'infAdFisco' has the following description: Informações
     * adicionais de interesse do Fisco
     * 
     * @return the value of field 'InfAdFisco'.
     */
    public java.lang.String getInfAdFisco(
    ) {
        return this._infAdFisco;
    }

    /**
     * Returns the value of field 'vTotTrib'. The field 'vTotTrib'
     * has the following description: Valor Total dos Tributos
     * 
     * @return the value of field 'VTotTrib'.
     */
    public java.lang.String getVTotTrib(
    ) {
        return this._vTotTrib;
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
     * Sets the value of field 'ICMS'. The field 'ICMS' has the
     * following description: Informações relativas ao ICMS
     * 
     * @param ICMS the value of field 'ICMS'.
     */
    public void setICMS(
            final com.mercurio.lms.cte.model.v200.ICMS ICMS) {
        this._ICMS = ICMS;
    }

    /**
     * Sets the value of field 'ICMSUFFim'. The field 'ICMSUFFim'
     * has the following description: Informações do ICMS de
     * partilha com a UF de término do serviço de transporte na
     * operação interestadual
     * 
     * @param ICMSUFFim the value of field 'ICMSUFFim'.
     */
    public void setICMSUFFim(
            final com.mercurio.lms.cte.model.v200.ICMSUFFim ICMSUFFim) {
        this._ICMSUFFim = ICMSUFFim;
    }

    /**
     * Sets the value of field 'infAdFisco'. The field 'infAdFisco'
     * has the following description: Informações adicionais de
     * interesse do Fisco
     * 
     * @param infAdFisco the value of field 'infAdFisco'.
     */
    public void setInfAdFisco(
            final java.lang.String infAdFisco) {
        this._infAdFisco = infAdFisco;
    }

    /**
     * Sets the value of field 'vTotTrib'. The field 'vTotTrib' has
     * the following description: Valor Total dos Tributos
     * 
     * @param vTotTrib the value of field 'vTotTrib'.
     */
    public void setVTotTrib(
            final java.lang.String vTotTrib) {
        this._vTotTrib = vTotTrib;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v200.Imp
     */
    public static com.mercurio.lms.cte.model.v200.Imp unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v200.Imp) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v200.Imp.class, reader);
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
