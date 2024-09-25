/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Proprietários do Veículo.
 *  Só preenchido quando o veículo não pertencer à empresa
 * emitente do CT-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Prop implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _propChoice.
     */
    private com.mercurio.lms.cte.model.v104.PropChoice _propChoice;

    /**
     * Registro Nacional dos Transportadores Rodoviários de Carga
     */
    private java.lang.String _RNTRC;

    /**
     * Razão Social ou Nome do proprietário
     */
    private java.lang.String _xNome;

    /**
     * Field _propSequence.
     */
    private com.mercurio.lms.cte.model.v104.PropSequence _propSequence;

    /**
     * Tipo Proprietário
     */
    private com.mercurio.lms.cte.model.v104.types.TpPropType _tpProp;


      //----------------/
     //- Constructors -/
    //----------------/

    public Prop() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'propChoice'.
     * 
     * @return the value of field 'PropChoice'.
     */
    public com.mercurio.lms.cte.model.v104.PropChoice getPropChoice(
    ) {
        return this._propChoice;
    }

    /**
     * Returns the value of field 'propSequence'.
     * 
     * @return the value of field 'PropSequence'.
     */
    public com.mercurio.lms.cte.model.v104.PropSequence getPropSequence(
    ) {
        return this._propSequence;
    }

    /**
     * Returns the value of field 'RNTRC'. The field 'RNTRC' has
     * the following description: Registro Nacional dos
     * Transportadores Rodoviários de Carga
     * 
     * @return the value of field 'RNTRC'.
     */
    public java.lang.String getRNTRC(
    ) {
        return this._RNTRC;
    }

    /**
     * Returns the value of field 'tpProp'. The field 'tpProp' has
     * the following description: Tipo Proprietário
     * 
     * @return the value of field 'TpProp'.
     */
    public com.mercurio.lms.cte.model.v104.types.TpPropType getTpProp(
    ) {
        return this._tpProp;
    }

    /**
     * Returns the value of field 'xNome'. The field 'xNome' has
     * the following description: Razão Social ou Nome do
     * proprietário
     * 
     * @return the value of field 'XNome'.
     */
    public java.lang.String getXNome(
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
     * Sets the value of field 'propChoice'.
     * 
     * @param propChoice the value of field 'propChoice'.
     */
    public void setPropChoice(
            final com.mercurio.lms.cte.model.v104.PropChoice propChoice) {
        this._propChoice = propChoice;
    }

    /**
     * Sets the value of field 'propSequence'.
     * 
     * @param propSequence the value of field 'propSequence'.
     */
    public void setPropSequence(
            final com.mercurio.lms.cte.model.v104.PropSequence propSequence) {
        this._propSequence = propSequence;
    }

    /**
     * Sets the value of field 'RNTRC'. The field 'RNTRC' has the
     * following description: Registro Nacional dos Transportadores
     * Rodoviários de Carga
     * 
     * @param RNTRC the value of field 'RNTRC'.
     */
    public void setRNTRC(
            final java.lang.String RNTRC) {
        this._RNTRC = RNTRC;
    }

    /**
     * Sets the value of field 'tpProp'. The field 'tpProp' has the
     * following description: Tipo Proprietário
     * 
     * @param tpProp the value of field 'tpProp'.
     */
    public void setTpProp(
            final com.mercurio.lms.cte.model.v104.types.TpPropType tpProp) {
        this._tpProp = tpProp;
    }

    /**
     * Sets the value of field 'xNome'. The field 'xNome' has the
     * following description: Razão Social ou Nome do
     * proprietário
     * 
     * @param xNome the value of field 'xNome'.
     */
    public void setXNome(
            final java.lang.String xNome) {
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
     * @return the unmarshaled com.mercurio.lms.cte.model.Prop
     */
    public static com.mercurio.lms.cte.model.v104.Prop unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.Prop) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.Prop.class, reader);
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
