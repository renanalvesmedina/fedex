/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100;

/**
 * Dados dos reboques
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class VeicReboque implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Código interno do veículo 
     */
    private java.lang.String _cInt;

    /**
     * Placa do veículo 
     */
    private java.lang.String _placa;

    /**
     * Tara em KG
     */
    private java.lang.String _tara;

    /**
     * Capacidade em KG
     */
    private java.lang.String _capKG;

    /**
     * Capacidade em M3
     */
    private java.lang.String _capM3;

    /**
     * Proprietários do Reboque. Só preenchido quando o reboque
     * não pertencer à empresa emitente do MDF-e
     */
    private com.mercurio.lms.mdfe.model.v100.Prop _prop;


      //----------------/
     //- Constructors -/
    //----------------/

    public VeicReboque() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'cInt'. The field 'cInt' has the
     * following description: Código interno do veículo 
     * 
     * @return the value of field 'CInt'.
     */
    public java.lang.String getCInt(
    ) {
        return this._cInt;
    }

    /**
     * Returns the value of field 'capKG'. The field 'capKG' has
     * the following description: Capacidade em KG
     * 
     * @return the value of field 'CapKG'.
     */
    public java.lang.String getCapKG(
    ) {
        return this._capKG;
    }

    /**
     * Returns the value of field 'capM3'. The field 'capM3' has
     * the following description: Capacidade em M3
     * 
     * @return the value of field 'CapM3'.
     */
    public java.lang.String getCapM3(
    ) {
        return this._capM3;
    }

    /**
     * Returns the value of field 'placa'. The field 'placa' has
     * the following description: Placa do veículo 
     * 
     * @return the value of field 'Placa'.
     */
    public java.lang.String getPlaca(
    ) {
        return this._placa;
    }

    /**
     * Returns the value of field 'prop'. The field 'prop' has the
     * following description: Proprietários do Reboque. Só
     * preenchido quando o reboque não pertencer à empresa
     * emitente do MDF-e
     * 
     * @return the value of field 'Prop'.
     */
    public com.mercurio.lms.mdfe.model.v100.Prop getProp(
    ) {
        return this._prop;
    }

    /**
     * Returns the value of field 'tara'. The field 'tara' has the
     * following description: Tara em KG
     * 
     * @return the value of field 'Tara'.
     */
    public java.lang.String getTara(
    ) {
        return this._tara;
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
     * Sets the value of field 'cInt'. The field 'cInt' has the
     * following description: Código interno do veículo 
     * 
     * @param cInt the value of field 'cInt'.
     */
    public void setCInt(
            final java.lang.String cInt) {
        this._cInt = cInt;
    }

    /**
     * Sets the value of field 'capKG'. The field 'capKG' has the
     * following description: Capacidade em KG
     * 
     * @param capKG the value of field 'capKG'.
     */
    public void setCapKG(
            final java.lang.String capKG) {
        this._capKG = capKG;
    }

    /**
     * Sets the value of field 'capM3'. The field 'capM3' has the
     * following description: Capacidade em M3
     * 
     * @param capM3 the value of field 'capM3'.
     */
    public void setCapM3(
            final java.lang.String capM3) {
        this._capM3 = capM3;
    }

    /**
     * Sets the value of field 'placa'. The field 'placa' has the
     * following description: Placa do veículo 
     * 
     * @param placa the value of field 'placa'.
     */
    public void setPlaca(
            final java.lang.String placa) {
        this._placa = placa;
    }

    /**
     * Sets the value of field 'prop'. The field 'prop' has the
     * following description: Proprietários do Reboque. Só
     * preenchido quando o reboque não pertencer à empresa
     * emitente do MDF-e
     * 
     * @param prop the value of field 'prop'.
     */
    public void setProp(
            final com.mercurio.lms.mdfe.model.v100.Prop prop) {
        this._prop = prop;
    }

    /**
     * Sets the value of field 'tara'. The field 'tara' has the
     * following description: Tara em KG
     * 
     * @param tara the value of field 'tara'.
     */
    public void setTara(
            final java.lang.String tara) {
        this._tara = tara;
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
     * com.mercurio.lms.mdfe.model.v100.VeicReboque
     */
    public static com.mercurio.lms.mdfe.model.v100.VeicReboque unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100.VeicReboque) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100.VeicReboque.class, reader);
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
