/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 * Proprietários do Veículo.
 * Só preenchido quando o veículo não pertencer à empresa
 * emitente do MDF-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Prop implements java.io.Serializable {

    private com.mercurio.lms.mdfe.model.v300.PropChoice propChoice;

    /**
     * Registro Nacional dos Transportadores Rodoviários de Carga
     */
    private java.lang.String RNTRC;

    /**
     * Razão Social ou Nome do proprietário
     */
    private java.lang.String xNome;

    private com.mercurio.lms.mdfe.model.v300.PropSequence propSequence;

    /**
     * Tipo Proprietário
     */
    private com.mercurio.lms.mdfe.model.v300.types.TpPropType tpProp;

    public Prop() {
        super();
    }

    /**
     * Returns the value of field 'propChoice'.
     * 
     * @return the value of field 'PropChoice'.
     */
    public com.mercurio.lms.mdfe.model.v300.PropChoice getPropChoice() {
        return this.propChoice;
    }

    /**
     * Returns the value of field 'propSequence'.
     * 
     * @return the value of field 'PropSequence'.
     */
    public com.mercurio.lms.mdfe.model.v300.PropSequence getPropSequence() {
        return this.propSequence;
    }

    /**
     * Returns the value of field 'RNTRC'. The field 'RNTRC' has
     * the following description: Registro Nacional dos
     * Transportadores Rodoviários de Carga
     * 
     * @return the value of field 'RNTRC'.
     */
    public java.lang.String getRNTRC() {
        return this.RNTRC;
    }

    /**
     * Returns the value of field 'tpProp'. The field 'tpProp' has
     * the following description: Tipo Proprietário
     * 
     * @return the value of field 'TpProp'.
     */
    public com.mercurio.lms.mdfe.model.v300.types.TpPropType getTpProp() {
        return this.tpProp;
    }

    /**
     * Returns the value of field 'xNome'. The field 'xNome' has
     * the following description: Razão Social ou Nome do
     * proprietário
     * 
     * @return the value of field 'XNome'.
     */
    public java.lang.String getXNome() {
        return this.xNome;
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
     * Sets the value of field 'propChoice'.
     * 
     * @param propChoice the value of field 'propChoice'.
     */
    public void setPropChoice(final com.mercurio.lms.mdfe.model.v300.PropChoice propChoice) {
        this.propChoice = propChoice;
    }

    /**
     * Sets the value of field 'propSequence'.
     * 
     * @param propSequence the value of field 'propSequence'.
     */
    public void setPropSequence(final com.mercurio.lms.mdfe.model.v300.PropSequence propSequence) {
        this.propSequence = propSequence;
    }

    /**
     * Sets the value of field 'RNTRC'. The field 'RNTRC' has the
     * following description: Registro Nacional dos Transportadores
     * Rodoviários de Carga
     * 
     * @param RNTRC the value of field 'RNTRC'.
     */
    public void setRNTRC(final java.lang.String RNTRC) {
        this.RNTRC = RNTRC;
    }

    /**
     * Sets the value of field 'tpProp'. The field 'tpProp' has the
     * following description: Tipo Proprietário
     * 
     * @param tpProp the value of field 'tpProp'.
     */
    public void setTpProp(final com.mercurio.lms.mdfe.model.v300.types.TpPropType tpProp) {
        this.tpProp = tpProp;
    }

    /**
     * Sets the value of field 'xNome'. The field 'xNome' has the
     * following description: Razão Social ou Nome do
     * proprietário
     * 
     * @param xNome the value of field 'xNome'.
     */
    public void setXNome(final java.lang.String xNome) {
        this.xNome = xNome;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.mdfe.model.v300.Pro
     */
    public static com.mercurio.lms.mdfe.model.v300.Prop unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.Prop) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.Prop.class, reader);
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
