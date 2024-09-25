/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Dados dos VeículosUm CT-e poderá ter vários veículos
 * associados, ex.: cavalo + reboque.
 *  Só preenchido em CT-e rodoviário de lotação.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Veic implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Código interno do veículo 
     */
    private java.lang.String _cInt;

    /**
     * RENAVAM do veículo 
     */
    private java.lang.String _RENAVAM;

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
     * Tipo de Propriedade de veículo
     */
    private com.mercurio.lms.cte.model.v104.types.TpPropType _tpProp;

    /**
     * Tipo de veículo
     */
    private com.mercurio.lms.cte.model.v104.types.TpVeicType _tpVeic;

    /**
     * Tipo de Rodado
     */
    private com.mercurio.lms.cte.model.v104.types.TpRodType _tpRod;

    /**
     * Tipo de Carroceria
     */
    private com.mercurio.lms.cte.model.v104.types.TpCarType _tpCar;

    /**
     * UF em que veículo está licenciado
     */
    private com.mercurio.lms.cte.model.v104.types.TUf _UF;

    /**
     * Proprietários do Veículo.
     *  Só preenchido quando o veículo não pertencer à empresa
     * emitente do CT-e
     */
    private com.mercurio.lms.cte.model.v104.Prop _prop;


      //----------------/
     //- Constructors -/
    //----------------/

    public Veic() {
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
     * following description: Proprietários do Veículo.
     *  Só preenchido quando o veículo não pertencer à empresa
     * emitente do CT-e
     * 
     * @return the value of field 'Prop'.
     */
    public com.mercurio.lms.cte.model.v104.Prop getProp(
    ) {
        return this._prop;
    }

    /**
     * Returns the value of field 'RENAVAM'. The field 'RENAVAM'
     * has the following description: RENAVAM do veículo 
     * 
     * @return the value of field 'RENAVAM'.
     */
    public java.lang.String getRENAVAM(
    ) {
        return this._RENAVAM;
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
     * Returns the value of field 'tpCar'. The field 'tpCar' has
     * the following description: Tipo de Carroceria
     * 
     * @return the value of field 'TpCar'.
     */
    public com.mercurio.lms.cte.model.v104.types.TpCarType getTpCar(
    ) {
        return this._tpCar;
    }

    /**
     * Returns the value of field 'tpProp'. The field 'tpProp' has
     * the following description: Tipo de Propriedade de veículo
     * 
     * @return the value of field 'TpProp'.
     */
    public com.mercurio.lms.cte.model.v104.types.TpPropType getTpProp(
    ) {
        return this._tpProp;
    }

    /**
     * Returns the value of field 'tpRod'. The field 'tpRod' has
     * the following description: Tipo de Rodado
     * 
     * @return the value of field 'TpRod'.
     */
    public com.mercurio.lms.cte.model.v104.types.TpRodType getTpRod(
    ) {
        return this._tpRod;
    }

    /**
     * Returns the value of field 'tpVeic'. The field 'tpVeic' has
     * the following description: Tipo de veículo
     * 
     * @return the value of field 'TpVeic'.
     */
    public com.mercurio.lms.cte.model.v104.types.TpVeicType getTpVeic(
    ) {
        return this._tpVeic;
    }

    /**
     * Returns the value of field 'UF'. The field 'UF' has the
     * following description: UF em que veículo está licenciado
     * 
     * @return the value of field 'UF'.
     */
    public com.mercurio.lms.cte.model.v104.types.TUf getUF(
    ) {
        return this._UF;
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
     * following description: Proprietários do Veículo.
     *  Só preenchido quando o veículo não pertencer à empresa
     * emitente do CT-e
     * 
     * @param prop the value of field 'prop'.
     */
    public void setProp(
            final com.mercurio.lms.cte.model.v104.Prop prop) {
        this._prop = prop;
    }

    /**
     * Sets the value of field 'RENAVAM'. The field 'RENAVAM' has
     * the following description: RENAVAM do veículo 
     * 
     * @param RENAVAM the value of field 'RENAVAM'.
     */
    public void setRENAVAM(
            final java.lang.String RENAVAM) {
        this._RENAVAM = RENAVAM;
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
     * Sets the value of field 'tpCar'. The field 'tpCar' has the
     * following description: Tipo de Carroceria
     * 
     * @param tpCar the value of field 'tpCar'.
     */
    public void setTpCar(
            final com.mercurio.lms.cte.model.v104.types.TpCarType tpCar) {
        this._tpCar = tpCar;
    }

    /**
     * Sets the value of field 'tpProp'. The field 'tpProp' has the
     * following description: Tipo de Propriedade de veículo
     * 
     * @param tpProp the value of field 'tpProp'.
     */
    public void setTpProp(
            final com.mercurio.lms.cte.model.v104.types.TpPropType tpProp) {
        this._tpProp = tpProp;
    }

    /**
     * Sets the value of field 'tpRod'. The field 'tpRod' has the
     * following description: Tipo de Rodado
     * 
     * @param tpRod the value of field 'tpRod'.
     */
    public void setTpRod(
            final com.mercurio.lms.cte.model.v104.types.TpRodType tpRod) {
        this._tpRod = tpRod;
    }

    /**
     * Sets the value of field 'tpVeic'. The field 'tpVeic' has the
     * following description: Tipo de veículo
     * 
     * @param tpVeic the value of field 'tpVeic'.
     */
    public void setTpVeic(
            final com.mercurio.lms.cte.model.v104.types.TpVeicType tpVeic) {
        this._tpVeic = tpVeic;
    }

    /**
     * Sets the value of field 'UF'. The field 'UF' has the
     * following description: UF em que veículo está licenciado
     * 
     * @param UF the value of field 'UF'.
     */
    public void setUF(
            final com.mercurio.lms.cte.model.v104.types.TUf UF) {
        this._UF = UF;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.Veic
     */
    public static com.mercurio.lms.cte.model.v104.Veic unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.Veic) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.Veic.class, reader);
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
