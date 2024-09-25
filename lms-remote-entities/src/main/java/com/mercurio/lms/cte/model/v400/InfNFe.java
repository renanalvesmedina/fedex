/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Informações das NF-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfNFe implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Chave de acesso da NF-e
     */
    private String _chave;

    /**
     * PIN SUFRAMA
     */
    private String _PIN;

    /**
     * Data prevista de entrega
     */
    private String _dPrev;

    /**
     * Field _infNFeChoice.
     */
    private InfNFeChoice _infNFeChoice;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfNFe() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'chave'. The field 'chave' has
     * the following description: Chave de acesso da NF-e
     *
     * @return the value of field 'Chave'.
     */
    public String getChave(
    ) {
        return this._chave;
    }

    /**
     * Returns the value of field 'dPrev'. The field 'dPrev' has
     * the following description: Data prevista de entrega
     *
     * @return the value of field 'DPrev'.
     */
    public String getDPrev(
    ) {
        return this._dPrev;
    }

    /**
     * Returns the value of field 'infNFeChoice'.
     *
     * @return the value of field 'InfNFeChoice'.
     */
    public InfNFeChoice getInfNFeChoice(
    ) {
        return this._infNFeChoice;
    }

    /**
     * Returns the value of field 'PIN'. The field 'PIN' has the
     * following description: PIN SUFRAMA
     *
     * @return the value of field 'PIN'.
     */
    public String getPIN(
    ) {
        return this._PIN;
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
     * Sets the value of field 'chave'. The field 'chave' has the
     * following description: Chave de acesso da NF-e
     *
     * @param chave the value of field 'chave'.
     */
    public void setChave(
            final String chave) {
        this._chave = chave;
    }

    /**
     * Sets the value of field 'dPrev'. The field 'dPrev' has the
     * following description: Data prevista de entrega
     *
     * @param dPrev the value of field 'dPrev'.
     */
    public void setDPrev(
            final String dPrev) {
        this._dPrev = dPrev;
    }

    /**
     * Sets the value of field 'infNFeChoice'.
     *
     * @param infNFeChoice the value of field 'infNFeChoice'.
     */
    public void setInfNFeChoice(
            final InfNFeChoice infNFeChoice) {
        this._infNFeChoice = infNFeChoice;
    }

    /**
     * Sets the value of field 'PIN'. The field 'PIN' has the
     * following description: PIN SUFRAMA
     *
     * @param PIN the value of field 'PIN'.
     */
    public void setPIN(
            final String PIN) {
        this._PIN = PIN;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v400.InfNF
     */
    public static InfNFe unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (InfNFe) org.exolab.castor.xml.Unmarshaller.unmarshal(InfNFe.class, reader);
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
