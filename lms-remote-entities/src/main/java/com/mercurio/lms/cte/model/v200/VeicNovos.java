/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v200;

/**
 * informações dos veículos transportados
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class VeicNovos implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Chassi do veículo
     */
    private java.lang.String _chassi;

    /**
     * Cor do veículo
     */
    private java.lang.String _cCor;

    /**
     * Descrição da cor
     */
    private java.lang.String _xCor;

    /**
     * Código Marca Modelo
     */
    private java.lang.String _cMod;

    /**
     * Valor Unitário do Veículo
     */
    private java.lang.String _vUnit;

    /**
     * Frete Unitário
     */
    private java.lang.String _vFrete;


      //----------------/
     //- Constructors -/
    //----------------/

    public VeicNovos() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'cCor'. The field 'cCor' has the
     * following description: Cor do veículo
     * 
     * @return the value of field 'CCor'.
     */
    public java.lang.String getCCor(
    ) {
        return this._cCor;
    }

    /**
     * Returns the value of field 'cMod'. The field 'cMod' has the
     * following description: Código Marca Modelo
     * 
     * @return the value of field 'CMod'.
     */
    public java.lang.String getCMod(
    ) {
        return this._cMod;
    }

    /**
     * Returns the value of field 'chassi'. The field 'chassi' has
     * the following description: Chassi do veículo
     * 
     * @return the value of field 'Chassi'.
     */
    public java.lang.String getChassi(
    ) {
        return this._chassi;
    }

    /**
     * Returns the value of field 'vFrete'. The field 'vFrete' has
     * the following description: Frete Unitário
     * 
     * @return the value of field 'VFrete'.
     */
    public java.lang.String getVFrete(
    ) {
        return this._vFrete;
    }

    /**
     * Returns the value of field 'vUnit'. The field 'vUnit' has
     * the following description: Valor Unitário do Veículo
     * 
     * @return the value of field 'VUnit'.
     */
    public java.lang.String getVUnit(
    ) {
        return this._vUnit;
    }

    /**
     * Returns the value of field 'xCor'. The field 'xCor' has the
     * following description: Descrição da cor
     * 
     * @return the value of field 'XCor'.
     */
    public java.lang.String getXCor(
    ) {
        return this._xCor;
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
     * Sets the value of field 'cCor'. The field 'cCor' has the
     * following description: Cor do veículo
     * 
     * @param cCor the value of field 'cCor'.
     */
    public void setCCor(
            final java.lang.String cCor) {
        this._cCor = cCor;
    }

    /**
     * Sets the value of field 'cMod'. The field 'cMod' has the
     * following description: Código Marca Modelo
     * 
     * @param cMod the value of field 'cMod'.
     */
    public void setCMod(
            final java.lang.String cMod) {
        this._cMod = cMod;
    }

    /**
     * Sets the value of field 'chassi'. The field 'chassi' has the
     * following description: Chassi do veículo
     * 
     * @param chassi the value of field 'chassi'.
     */
    public void setChassi(
            final java.lang.String chassi) {
        this._chassi = chassi;
    }

    /**
     * Sets the value of field 'vFrete'. The field 'vFrete' has the
     * following description: Frete Unitário
     * 
     * @param vFrete the value of field 'vFrete'.
     */
    public void setVFrete(
            final java.lang.String vFrete) {
        this._vFrete = vFrete;
    }

    /**
     * Sets the value of field 'vUnit'. The field 'vUnit' has the
     * following description: Valor Unitário do Veículo
     * 
     * @param vUnit the value of field 'vUnit'.
     */
    public void setVUnit(
            final java.lang.String vUnit) {
        this._vUnit = vUnit;
    }

    /**
     * Sets the value of field 'xCor'. The field 'xCor' has the
     * following description: Descrição da cor
     * 
     * @param xCor the value of field 'xCor'.
     */
    public void setXCor(
            final java.lang.String xCor) {
        this._xCor = xCor;
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
     * com.mercurio.lms.cte.model.v200.VeicNovos
     */
    public static com.mercurio.lms.cte.model.v200.VeicNovos unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v200.VeicNovos) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v200.VeicNovos.class, reader);
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
