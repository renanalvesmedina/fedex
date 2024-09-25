/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

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
    private String _chassi;

    /**
     * Cor do veículo
     */
    private String _cCor;

    /**
     * Descrição da cor
     */
    private String _xCor;

    /**
     * Código Marca Modelo
     */
    private String _cMod;

    /**
     * Valor Unitário do Veículo
     */
    private String _vUnit;

    /**
     * Frete Unitário
     */
    private String _vFrete;


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
    public String getCCor(
    ) {
        return this._cCor;
    }

    /**
     * Returns the value of field 'cMod'. The field 'cMod' has the
     * following description: Código Marca Modelo
     *
     * @return the value of field 'CMod'.
     */
    public String getCMod(
    ) {
        return this._cMod;
    }

    /**
     * Returns the value of field 'chassi'. The field 'chassi' has
     * the following description: Chassi do veículo
     *
     * @return the value of field 'Chassi'.
     */
    public String getChassi(
    ) {
        return this._chassi;
    }

    /**
     * Returns the value of field 'vFrete'. The field 'vFrete' has
     * the following description: Frete Unitário
     *
     * @return the value of field 'VFrete'.
     */
    public String getVFrete(
    ) {
        return this._vFrete;
    }

    /**
     * Returns the value of field 'vUnit'. The field 'vUnit' has
     * the following description: Valor Unitário do Veículo
     *
     * @return the value of field 'VUnit'.
     */
    public String getVUnit(
    ) {
        return this._vUnit;
    }

    /**
     * Returns the value of field 'xCor'. The field 'xCor' has the
     * following description: Descrição da cor
     *
     * @return the value of field 'XCor'.
     */
    public String getXCor(
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
            final String cCor) {
        this._cCor = cCor;
    }

    /**
     * Sets the value of field 'cMod'. The field 'cMod' has the
     * following description: Código Marca Modelo
     *
     * @param cMod the value of field 'cMod'.
     */
    public void setCMod(
            final String cMod) {
        this._cMod = cMod;
    }

    /**
     * Sets the value of field 'chassi'. The field 'chassi' has the
     * following description: Chassi do veículo
     *
     * @param chassi the value of field 'chassi'.
     */
    public void setChassi(
            final String chassi) {
        this._chassi = chassi;
    }

    /**
     * Sets the value of field 'vFrete'. The field 'vFrete' has the
     * following description: Frete Unitário
     *
     * @param vFrete the value of field 'vFrete'.
     */
    public void setVFrete(
            final String vFrete) {
        this._vFrete = vFrete;
    }

    /**
     * Sets the value of field 'vUnit'. The field 'vUnit' has the
     * following description: Valor Unitário do Veículo
     *
     * @param vUnit the value of field 'vUnit'.
     */
    public void setVUnit(
            final String vUnit) {
        this._vUnit = vUnit;
    }

    /**
     * Sets the value of field 'xCor'. The field 'xCor' has the
     * following description: Descrição da cor
     *
     * @param xCor the value of field 'xCor'.
     */
    public void setXCor(
            final String xCor) {
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
     * com.mercurio.lms.cte.model.v400.VeicNovos
     */
    public static VeicNovos unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (VeicNovos) org.exolab.castor.xml.Unmarshaller.unmarshal(VeicNovos.class, reader);
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
