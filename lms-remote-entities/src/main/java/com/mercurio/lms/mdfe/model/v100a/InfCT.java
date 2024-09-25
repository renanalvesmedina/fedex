/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100a;

/**
 * Conhecimentos Tranporte (papel)
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfCT implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Número do CT
     */
    private java.lang.String _nCT;

    /**
     * Série do CT
     */
    private java.lang.String _serie;

    /**
     * Subserie do CT
     */
    private java.lang.String _subser;

    /**
     * Data de Emissão
     */
    private java.lang.String _dEmi;

    /**
     * Valor total da carga
     */
    private java.lang.String _vCarga;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfCT() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dEmi'. The field 'dEmi' has the
     * following description: Data de Emissão
     * 
     * @return the value of field 'DEmi'.
     */
    public java.lang.String getDEmi(
    ) {
        return this._dEmi;
    }

    /**
     * Returns the value of field 'nCT'. The field 'nCT' has the
     * following description: Número do CT
     * 
     * @return the value of field 'NCT'.
     */
    public java.lang.String getNCT(
    ) {
        return this._nCT;
    }

    /**
     * Returns the value of field 'serie'. The field 'serie' has
     * the following description: Série do CT
     * 
     * @return the value of field 'Serie'.
     */
    public java.lang.String getSerie(
    ) {
        return this._serie;
    }

    /**
     * Returns the value of field 'subser'. The field 'subser' has
     * the following description: Subserie do CT
     * 
     * @return the value of field 'Subser'.
     */
    public java.lang.String getSubser(
    ) {
        return this._subser;
    }

    /**
     * Returns the value of field 'vCarga'. The field 'vCarga' has
     * the following description: Valor total da carga
     * 
     * @return the value of field 'VCarga'.
     */
    public java.lang.String getVCarga(
    ) {
        return this._vCarga;
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
     * Sets the value of field 'dEmi'. The field 'dEmi' has the
     * following description: Data de Emissão
     * 
     * @param dEmi the value of field 'dEmi'.
     */
    public void setDEmi(
            final java.lang.String dEmi) {
        this._dEmi = dEmi;
    }

    /**
     * Sets the value of field 'nCT'. The field 'nCT' has the
     * following description: Número do CT
     * 
     * @param nCT the value of field 'nCT'.
     */
    public void setNCT(
            final java.lang.String nCT) {
        this._nCT = nCT;
    }

    /**
     * Sets the value of field 'serie'. The field 'serie' has the
     * following description: Série do CT
     * 
     * @param serie the value of field 'serie'.
     */
    public void setSerie(
            final java.lang.String serie) {
        this._serie = serie;
    }

    /**
     * Sets the value of field 'subser'. The field 'subser' has the
     * following description: Subserie do CT
     * 
     * @param subser the value of field 'subser'.
     */
    public void setSubser(
            final java.lang.String subser) {
        this._subser = subser;
    }

    /**
     * Sets the value of field 'vCarga'. The field 'vCarga' has the
     * following description: Valor total da carga
     * 
     * @param vCarga the value of field 'vCarga'.
     */
    public void setVCarga(
            final java.lang.String vCarga) {
        this._vCarga = vCarga;
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
     * org.exolab.castor.builder.binding.InfCT
     */
    public static com.mercurio.lms.mdfe.model.v100a.InfCT unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100a.InfCT) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100a.InfCT.class, reader);
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
