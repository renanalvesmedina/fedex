/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Tipo Pedido de Cancelamento de CT-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TCancCTe implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _versao.
     */
    private String _versao;

    /**
     * Dados do Pedido de Cancelamentode Conhecimento de Transporte
     * Eletrônico
     */
    private InfCanc _infCanc;

    /**
     * Field _signature.
     */
    private Signature _signature;


      //----------------/
     //- Constructors -/
    //----------------/

    public TCancCTe() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'infCanc'. The field 'infCanc'
     * has the following description: Dados do Pedido de
     * Cancelamentode Conhecimento de Transporte Eletrônico
     *
     * @return the value of field 'InfCanc'.
     */
    public InfCanc getInfCanc(
    ) {
        return this._infCanc;
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
     * Sets the value of field 'infCanc'. The field 'infCanc' has
     * the following description: Dados do Pedido de Cancelamentode
     * Conhecimento de Transporte Eletrônico
     *
     * @param infCanc the value of field 'infCanc'.
     */
    public void setInfCanc(
            final InfCanc infCanc) {
        this._infCanc = infCanc;
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
     * com.mercurio.lms.cte.model.v400.TCancCTe
     */
    public static TCancCTe unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (TCancCTe) org.exolab.castor.xml.Unmarshaller.unmarshal(TCancCTe.class, reader);
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
