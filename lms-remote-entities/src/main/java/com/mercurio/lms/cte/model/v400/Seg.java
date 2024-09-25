/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Informações de Seguro da Carga
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Seg implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Responsável pelo seguro
     */
    private com.mercurio.lms.cte.model.v400.types.RespSegType _respSeg;

    /**
     * Nome da Seguradora
     */
    private String _xSeg;

    /**
     * Número da Apólice
     */
    private String _nApol;

    /**
     * Número da Averbação
     */
    private String _nAver;

    /**
     * Valor da Carga para efeito de averbação
     */
    private String _vCarga;


      //----------------/
     //- Constructors -/
    //----------------/

    public Seg() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'nApol'. The field 'nApol' has
     * the following description: Número da Apólice
     *
     * @return the value of field 'NApol'.
     */
    public String getNApol(
    ) {
        return this._nApol;
    }

    /**
     * Returns the value of field 'nAver'. The field 'nAver' has
     * the following description: Número da Averbação
     *
     * @return the value of field 'NAver'.
     */
    public String getNAver(
    ) {
        return this._nAver;
    }

    /**
     * Returns the value of field 'respSeg'. The field 'respSeg'
     * has the following description: Responsável pelo seguro
     *
     * @return the value of field 'RespSeg'.
     */
    public com.mercurio.lms.cte.model.v400.types.RespSegType getRespSeg(
    ) {
        return this._respSeg;
    }

    /**
     * Returns the value of field 'vCarga'. The field 'vCarga' has
     * the following description: Valor da Carga para efeito de
     * averbação
     *
     * @return the value of field 'VCarga'.
     */
    public String getVCarga(
    ) {
        return this._vCarga;
    }

    /**
     * Returns the value of field 'xSeg'. The field 'xSeg' has the
     * following description: Nome da Seguradora
     *
     * @return the value of field 'XSeg'.
     */
    public String getXSeg(
    ) {
        return this._xSeg;
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
     * Sets the value of field 'nApol'. The field 'nApol' has the
     * following description: Número da Apólice
     *
     * @param nApol the value of field 'nApol'.
     */
    public void setNApol(
            final String nApol) {
        this._nApol = nApol;
    }

    /**
     * Sets the value of field 'nAver'. The field 'nAver' has the
     * following description: Número da Averbação
     *
     * @param nAver the value of field 'nAver'.
     */
    public void setNAver(
            final String nAver) {
        this._nAver = nAver;
    }

    /**
     * Sets the value of field 'respSeg'. The field 'respSeg' has
     * the following description: Responsável pelo seguro
     *
     * @param respSeg the value of field 'respSeg'.
     */
    public void setRespSeg(
            final com.mercurio.lms.cte.model.v400.types.RespSegType respSeg) {
        this._respSeg = respSeg;
    }

    /**
     * Sets the value of field 'vCarga'. The field 'vCarga' has the
     * following description: Valor da Carga para efeito de
     * averbação
     *
     * @param vCarga the value of field 'vCarga'.
     */
    public void setVCarga(
            final String vCarga) {
        this._vCarga = vCarga;
    }

    /**
     * Sets the value of field 'xSeg'. The field 'xSeg' has the
     * following description: Nome da Seguradora
     *
     * @param xSeg the value of field 'xSeg'.
     */
    public void setXSeg(
            final String xSeg) {
        this._xSeg = xSeg;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v400.Seg
     */
    public static Seg unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (Seg) org.exolab.castor.xml.Unmarshaller.unmarshal(Seg.class, reader);
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
