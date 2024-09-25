/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v200;

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
    private java.lang.String _chave;

    /**
     * PIN SUFRAMA
     */
    private java.lang.String _PIN;

    /**
     * Data prevista de entrega
     */
    private java.lang.String _dPrev;

    /**
     * Field _infNFeChoice.
     */
    private com.mercurio.lms.cte.model.v200.InfNFeChoice _infNFeChoice;


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
    public java.lang.String getChave(
    ) {
        return this._chave;
    }

    /**
     * Returns the value of field 'dPrev'. The field 'dPrev' has
     * the following description: Data prevista de entrega
     * 
     * @return the value of field 'DPrev'.
     */
    public java.lang.String getDPrev(
    ) {
        return this._dPrev;
    }

    /**
     * Returns the value of field 'infNFeChoice'.
     * 
     * @return the value of field 'InfNFeChoice'.
     */
    public com.mercurio.lms.cte.model.v200.InfNFeChoice getInfNFeChoice(
    ) {
        return this._infNFeChoice;
    }

    /**
     * Returns the value of field 'PIN'. The field 'PIN' has the
     * following description: PIN SUFRAMA
     * 
     * @return the value of field 'PIN'.
     */
    public java.lang.String getPIN(
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
            final java.lang.String chave) {
        this._chave = chave;
    }

    /**
     * Sets the value of field 'dPrev'. The field 'dPrev' has the
     * following description: Data prevista de entrega
     * 
     * @param dPrev the value of field 'dPrev'.
     */
    public void setDPrev(
            final java.lang.String dPrev) {
        this._dPrev = dPrev;
    }

    /**
     * Sets the value of field 'infNFeChoice'.
     * 
     * @param infNFeChoice the value of field 'infNFeChoice'.
     */
    public void setInfNFeChoice(
            final com.mercurio.lms.cte.model.v200.InfNFeChoice infNFeChoice) {
        this._infNFeChoice = infNFeChoice;
    }

    /**
     * Sets the value of field 'PIN'. The field 'PIN' has the
     * following description: PIN SUFRAMA
     * 
     * @param PIN the value of field 'PIN'.
     */
    public void setPIN(
            final java.lang.String PIN) {
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
     * @return the unmarshaled com.mercurio.lms.cte.model.v200.InfNF
     */
    public static com.mercurio.lms.cte.model.v200.InfNFe unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v200.InfNFe) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v200.InfNFe.class, reader);
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
