/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Informações do Destinatário do CT-eSó pode ser omitido em
 * caso de redespacho intermediário
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Dest implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _destChoice.
     */
    private DestChoice _destChoice;

    /**
     * Inscrição Estadual
     */
    private String _IE;

    /**
     * Razão Social ou Nome do destinatário
     */
    private String _xNome;

    /**
     * Telefone
     */
    private String _fone;

    /**
     * Inscrição na SUFRAMA
     */
    private String _ISUF;

    /**
     * Dados do endereço
     */
    private EnderDest _enderDest;

    /**
     * Endereço de email
     */
    private String _email;


      //----------------/
     //- Constructors -/
    //----------------/

    public Dest() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'destChoice'.
     *
     * @return the value of field 'DestChoice'.
     */
    public DestChoice getDestChoice(
    ) {
        return this._destChoice;
    }

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
     * Returns the value of field 'enderDest'. The field
     * 'enderDest' has the following description: Dados do
     * endereço
     *
     * @return the value of field 'EnderDest'.
     */
    public EnderDest getEnderDest(
    ) {
        return this._enderDest;
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
     * Returns the value of field 'ISUF'. The field 'ISUF' has the
     * following description: Inscrição na SUFRAMA
     *
     * @return the value of field 'ISUF'.
     */
    public String getISUF(
    ) {
        return this._ISUF;
    }

    /**
     * Returns the value of field 'xNome'. The field 'xNome' has
     * the following description: Razão Social ou Nome do
     * destinatário
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
     * Sets the value of field 'destChoice'.
     *
     * @param destChoice the value of field 'destChoice'.
     */
    public void setDestChoice(
            final DestChoice destChoice) {
        this._destChoice = destChoice;
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
     * Sets the value of field 'enderDest'. The field 'enderDest'
     * has the following description: Dados do endereço
     *
     * @param enderDest the value of field 'enderDest'.
     */
    public void setEnderDest(
            final EnderDest enderDest) {
        this._enderDest = enderDest;
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
     * Sets the value of field 'ISUF'. The field 'ISUF' has the
     * following description: Inscrição na SUFRAMA
     *
     * @param ISUF the value of field 'ISUF'.
     */
    public void setISUF(
            final String ISUF) {
        this._ISUF = ISUF;
    }

    /**
     * Sets the value of field 'xNome'. The field 'xNome' has the
     * following description: Razão Social ou Nome do
     * destinatário
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
     * @return the unmarshaled com.mercurio.lms.cte.model.v400.Dest
     */
    public static Dest unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (Dest) org.exolab.castor.xml.Unmarshaller.unmarshal(Dest.class, reader);
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
