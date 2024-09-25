/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 * Dados dos reboques
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class VeicReboque implements java.io.Serializable {

    /**
     * Código interno do veículo 
     */
    private java.lang.String cInt;

    /**
     * Placa do veículo 
     */
    private java.lang.String placa;

    /**
     * RENAVAM do veículo 
     */
    private java.lang.String RENAVAM;

    /**
     * Tara em KG
     */
    private java.lang.String tara;

    /**
     * Capacidade em KG
     */
    private java.lang.String capKG;

    /**
     * Capacidade em M3
     */
    private java.lang.String capM3;

    /**
     * Proprietários do Veículo.
     * Só preenchido quando o veículo não pertencer à empresa
     * emitente do MDF-e
     */
    private com.mercurio.lms.mdfe.model.v300.Prop prop;

    /**
     * Tipo de Carroceria
     */
    private com.mercurio.lms.mdfe.model.v300.types.TpCarType tpCar;

    /**
     * UF em que veículo está licenciado
     */
    private com.mercurio.lms.mdfe.model.v300.types.TUf UF;

    public VeicReboque() {
        super();
    }

    /**
     * Returns the value of field 'cInt'. The field 'cInt' has the
     * following description: Código interno do veículo 
     * 
     * @return the value of field 'CInt'.
     */
    public java.lang.String getCInt() {
        return this.cInt;
    }

    /**
     * Returns the value of field 'capKG'. The field 'capKG' has
     * the following description: Capacidade em KG
     * 
     * @return the value of field 'CapKG'.
     */
    public java.lang.String getCapKG() {
        return this.capKG;
    }

    /**
     * Returns the value of field 'capM3'. The field 'capM3' has
     * the following description: Capacidade em M3
     * 
     * @return the value of field 'CapM3'.
     */
    public java.lang.String getCapM3() {
        return this.capM3;
    }

    /**
     * Returns the value of field 'placa'. The field 'placa' has
     * the following description: Placa do veículo 
     * 
     * @return the value of field 'Placa'.
     */
    public java.lang.String getPlaca() {
        return this.placa;
    }

    /**
     * Returns the value of field 'prop'. The field 'prop' has the
     * following description: Proprietários do Veículo.
     * Só preenchido quando o veículo não pertencer à empresa
     * emitente do MDF-e
     * 
     * @return the value of field 'Prop'.
     */
    public com.mercurio.lms.mdfe.model.v300.Prop getProp() {
        return this.prop;
    }

    /**
     * Returns the value of field 'RENAVAM'. The field 'RENAVAM'
     * has the following description: RENAVAM do veículo 
     * 
     * @return the value of field 'RENAVAM'.
     */
    public java.lang.String getRENAVAM() {
        return this.RENAVAM;
    }

    /**
     * Returns the value of field 'tara'. The field 'tara' has the
     * following description: Tara em KG
     * 
     * @return the value of field 'Tara'.
     */
    public java.lang.String getTara() {
        return this.tara;
    }

    /**
     * Returns the value of field 'tpCar'. The field 'tpCar' has
     * the following description: Tipo de Carroceria
     * 
     * @return the value of field 'TpCar'.
     */
    public com.mercurio.lms.mdfe.model.v300.types.TpCarType getTpCar() {
        return this.tpCar;
    }

    /**
     * Returns the value of field 'UF'. The field 'UF' has the
     * following description: UF em que veículo está licenciado
     * 
     * @return the value of field 'UF'.
     */
    public com.mercurio.lms.mdfe.model.v300.types.TUf getUF() {
        return this.UF;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid() {
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
    public void marshal(final java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
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
    public void marshal(final org.xml.sax.ContentHandler handler) throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'cInt'. The field 'cInt' has the
     * following description: Código interno do veículo 
     * 
     * @param cInt the value of field 'cInt'.
     */
    public void setCInt(final java.lang.String cInt) {
        this.cInt = cInt;
    }

    /**
     * Sets the value of field 'capKG'. The field 'capKG' has the
     * following description: Capacidade em KG
     * 
     * @param capKG the value of field 'capKG'.
     */
    public void setCapKG(final java.lang.String capKG) {
        this.capKG = capKG;
    }

    /**
     * Sets the value of field 'capM3'. The field 'capM3' has the
     * following description: Capacidade em M3
     * 
     * @param capM3 the value of field 'capM3'.
     */
    public void setCapM3(final java.lang.String capM3) {
        this.capM3 = capM3;
    }

    /**
     * Sets the value of field 'placa'. The field 'placa' has the
     * following description: Placa do veículo 
     * 
     * @param placa the value of field 'placa'.
     */
    public void setPlaca(final java.lang.String placa) {
        this.placa = placa;
    }

    /**
     * Sets the value of field 'prop'. The field 'prop' has the
     * following description: Proprietários do Veículo.
     * Só preenchido quando o veículo não pertencer à empresa
     * emitente do MDF-e
     * 
     * @param prop the value of field 'prop'.
     */
    public void setProp(final com.mercurio.lms.mdfe.model.v300.Prop prop) {
        this.prop = prop;
    }

    /**
     * Sets the value of field 'RENAVAM'. The field 'RENAVAM' has
     * the following description: RENAVAM do veículo 
     * 
     * @param RENAVAM the value of field 'RENAVAM'.
     */
    public void setRENAVAM(final java.lang.String RENAVAM) {
        this.RENAVAM = RENAVAM;
    }

    /**
     * Sets the value of field 'tara'. The field 'tara' has the
     * following description: Tara em KG
     * 
     * @param tara the value of field 'tara'.
     */
    public void setTara(final java.lang.String tara) {
        this.tara = tara;
    }

    /**
     * Sets the value of field 'tpCar'. The field 'tpCar' has the
     * following description: Tipo de Carroceria
     * 
     * @param tpCar the value of field 'tpCar'.
     */
    public void setTpCar(final com.mercurio.lms.mdfe.model.v300.types.TpCarType tpCar) {
        this.tpCar = tpCar;
    }

    /**
     * Sets the value of field 'UF'. The field 'UF' has the
     * following description: UF em que veículo está licenciado
     * 
     * @param UF the value of field 'UF'.
     */
    public void setUF(final com.mercurio.lms.mdfe.model.v300.types.TUf UF) {
        this.UF = UF;
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
     * com.mercurio.lms.mdfe.model.v300.VeicReboque
     */
    public static com.mercurio.lms.mdfe.model.v300.VeicReboque unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.VeicReboque) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.VeicReboque.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
