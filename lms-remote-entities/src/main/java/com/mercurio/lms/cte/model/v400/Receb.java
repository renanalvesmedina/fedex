/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Informações do Recebedor da Carga
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Receb implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _recebChoice.
     */
    private RecebChoice _recebChoice;

    /**
     * Inscrição Estadual
     */
    private String _IE;

    /**
     * Razão Social ou Nome
     */
    private String _xNome;

    /**
     * Telefone
     */
    private String _fone;

    /**
     * Dados do endereço
     */
    private EnderReceb _enderReceb;

    /**
     * Endereço de email
     */
    private String _email;


      //----------------/
     //- Constructors -/
    //----------------/

    public Receb() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'email'. The field 'email' has
     * the following description: Endereço de email
     *
     * @return the value of field 'Email'.
     */
    public String getEmail(
    ) {
        return this._email;
    }

    /**
     * Returns the value of field 'enderReceb'. The field
     * 'enderReceb' has the following description: Dados do
     * endereço
     *
     * @return the value of field 'EnderReceb'.
     */
    public EnderReceb getEnderReceb(
    ) {
        return this._enderReceb;
    }

    /**
     * Returns the value of field 'fone'. The field 'fone' has the
     * following description: Telefone
     *
     * @return the value of field 'Fone'.
     */
    public String getFone(
    ) {
        return this._fone;
    }

    /**
     * Returns the value of field 'IE'. The field 'IE' has the
     * following description: Inscrição Estadual
     *
     * @return the value of field 'IE'.
     */
    public String getIE(
    ) {
        return this._IE;
    }

    /**
     * Returns the value of field 'recebChoice'.
     *
     * @return the value of field 'RecebChoice'.
     */
    public RecebChoice getRecebChoice(
    ) {
        return this._recebChoice;
    }

    /**
     * Returns the value of field 'xNome'. The field 'xNome' has
     * the following description: Razão Social ou Nome
     *
     * @return the value of field 'XNome'.
     */
    public String getXNome(
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
     * Sets the value of field 'email'. The field 'email' has the
     * following description: Endereço de email
     *
     * @param email the value of field 'email'.
     */
    public void setEmail(
            final String email) {
        this._email = email;
    }

    /**
     * Sets the value of field 'enderReceb'. The field 'enderReceb'
     * has the following description: Dados do endereço
     *
     * @param enderReceb the value of field 'enderReceb'.
     */
    public void setEnderReceb(
            final EnderReceb enderReceb) {
        this._enderReceb = enderReceb;
    }

    /**
     * Sets the value of field 'fone'. The field 'fone' has the
     * following description: Telefone
     *
     * @param fone the value of field 'fone'.
     */
    public void setFone(
            final String fone) {
        this._fone = fone;
    }

    /**
     * Sets the value of field 'IE'. The field 'IE' has the
     * following description: Inscrição Estadual
     *
     * @param IE the value of field 'IE'.
     */
    public void setIE(
            final String IE) {
        this._IE = IE;
    }

    /**
     * Sets the value of field 'recebChoice'.
     *
     * @param recebChoice the value of field 'recebChoice'.
     */
    public void setRecebChoice(
            final RecebChoice recebChoice) {
        this._recebChoice = recebChoice;
    }

    /**
     * Sets the value of field 'xNome'. The field 'xNome' has the
     * following description: Razão Social ou Nome
     *
     * @param xNome the value of field 'xNome'.
     */
    public void setXNome(
            final String xNome) {
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
     * @return the unmarshaled com.mercurio.lms.cte.model.v400.Receb
     */
    public static Receb unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (Receb) org.exolab.castor.xml.Unmarshaller.unmarshal(Receb.class, reader);
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
