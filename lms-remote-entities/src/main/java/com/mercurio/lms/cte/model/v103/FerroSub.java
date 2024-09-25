/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Informações da Ferrovia Substituída
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class FerroSub implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Número do CNPJ
     */
    private java.lang.String _CNPJ;

    /**
     * Código interno da Ferrovia Substituta
     */
    private java.lang.String _cInt;

    /**
     * Inscrição Estadual
     */
    private java.lang.String _IE;

    /**
     * Razão Social ou Nome
     */
    private java.lang.String _xNome;

    /**
     * Dados do endereço da ferrovia substituída
     */
    private com.mercurio.lms.cte.model.v103.EnderFerro _enderFerro;


      //----------------/
     //- Constructors -/
    //----------------/

    public FerroSub() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'cInt'. The field 'cInt' has the
     * following description: Código interno da Ferrovia
     * Substituta
     * 
     * @return the value of field 'CInt'.
     */
    public java.lang.String getCInt(
    ) {
        return this._cInt;
    }

    /**
     * Returns the value of field 'CNPJ'. The field 'CNPJ' has the
     * following description: Número do CNPJ
     * 
     * @return the value of field 'CNPJ'.
     */
    public java.lang.String getCNPJ(
    ) {
        return this._CNPJ;
    }

    /**
     * Returns the value of field 'enderFerro'. The field
     * 'enderFerro' has the following description: Dados do
     * endereço da ferrovia substituída
     * 
     * @return the value of field 'EnderFerro'.
     */
    public com.mercurio.lms.cte.model.v103.EnderFerro getEnderFerro(
    ) {
        return this._enderFerro;
    }

    /**
     * Returns the value of field 'IE'. The field 'IE' has the
     * following description: Inscrição Estadual
     * 
     * @return the value of field 'IE'.
     */
    public java.lang.String getIE(
    ) {
        return this._IE;
    }

    /**
     * Returns the value of field 'xNome'. The field 'xNome' has
     * the following description: Razão Social ou Nome
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
     * Sets the value of field 'cInt'. The field 'cInt' has the
     * following description: Código interno da Ferrovia
     * Substituta
     * 
     * @param cInt the value of field 'cInt'.
     */
    public void setCInt(
            final java.lang.String cInt) {
        this._cInt = cInt;
    }

    /**
     * Sets the value of field 'CNPJ'. The field 'CNPJ' has the
     * following description: Número do CNPJ
     * 
     * @param CNPJ the value of field 'CNPJ'.
     */
    public void setCNPJ(
            final java.lang.String CNPJ) {
        this._CNPJ = CNPJ;
    }

    /**
     * Sets the value of field 'enderFerro'. The field 'enderFerro'
     * has the following description: Dados do endereço da
     * ferrovia substituída
     * 
     * @param enderFerro the value of field 'enderFerro'.
     */
    public void setEnderFerro(
            final com.mercurio.lms.cte.model.v103.EnderFerro enderFerro) {
        this._enderFerro = enderFerro;
    }

    /**
     * Sets the value of field 'IE'. The field 'IE' has the
     * following description: Inscrição Estadual
     * 
     * @param IE the value of field 'IE'.
     */
    public void setIE(
            final java.lang.String IE) {
        this._IE = IE;
    }

    /**
     * Sets the value of field 'xNome'. The field 'xNome' has the
     * following description: Razão Social ou Nome
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
     * @return the unmarshaled
     * com.mercurio.lms.cte.model.v103.FerroSub
     */
    public static com.mercurio.lms.cte.model.v103.FerroSub unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.FerroSub) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.FerroSub.class, reader);
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
