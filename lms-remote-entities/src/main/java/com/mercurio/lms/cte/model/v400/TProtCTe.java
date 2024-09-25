/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Tipo Protocolo de status resultado do processamento da CT-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TProtCTe implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _versao.
     */
    private String _versao;

    /**
     * Dados do protocolo de status
     */
    private InfProt _infProt;

    /**
     * Field _signature.
     */
    private Signature _signature;


      //----------------/
     //- Constructors -/
    //----------------/

    public TProtCTe() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'infProt'. The field 'infProt'
     * has the following description: Dados do protocolo de status
     *
     * @return the value of field 'InfProt'.
     */
    public InfProt getInfProt(
    ) {
        return this._infProt;
    }

    /**
     * Returns the value of field 'signature'.
     *
     * @return the value of field 'Signature'.
     */
    public Signature getSignature(
    ) {
        return this._signature;
    }

    /**
     * Returns the value of field 'versao'.
     *
     * @return the value of field 'Versao'.
     */
    public String getVersao(
    ) {
        return this._versao;
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
     * Sets the value of field 'infProt'. The field 'infProt' has
     * the following description: Dados do protocolo de status
     *
     * @param infProt the value of field 'infProt'.
     */
    public void setInfProt(
            final InfProt infProt) {
        this._infProt = infProt;
    }

    /**
     * Sets the value of field 'signature'.
     *
     * @param signature the value of field 'signature'.
     */
    public void setSignature(
            final Signature signature) {
        this._signature = signature;
    }

    /**
     * Sets the value of field 'versao'.
     *
     * @param versao the value of field 'versao'.
     */
    public void setVersao(
            final String versao) {
        this._versao = versao;
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
     * com.mercurio.lms.cte.model.v400.TProtCTe
     */
    public static TProtCTe unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (TProtCTe) org.exolab.castor.xml.Unmarshaller.unmarshal(TProtCTe.class, reader);
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
