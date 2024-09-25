/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100;

/**
 * Informações do Emitente da NF
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Emi implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * CNPJ do emitente
     */
    private java.lang.String _CNPJ;

    /**
     * Razão Social ou Nome 
     */
    private java.lang.String _xNome;

    /**
     * UF do Emitente
     */
    private com.mercurio.lms.mdfe.model.v100.types.TUf _UF;


      //----------------/
     //- Constructors -/
    //----------------/

    public Emi() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'CNPJ'. The field 'CNPJ' has the
     * following description: CNPJ do emitente
     * 
     * @return the value of field 'CNPJ'.
     */
    public java.lang.String getCNPJ(
    ) {
        return this._CNPJ;
    }

    /**
     * Returns the value of field 'UF'. The field 'UF' has the
     * following description: UF do Emitente
     * 
     * @return the value of field 'UF'.
     */
    public com.mercurio.lms.mdfe.model.v100.types.TUf getUF(
    ) {
        return this._UF;
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
     * Sets the value of field 'CNPJ'. The field 'CNPJ' has the
     * following description: CNPJ do emitente
     * 
     * @param CNPJ the value of field 'CNPJ'.
     */
    public void setCNPJ(
            final java.lang.String CNPJ) {
        this._CNPJ = CNPJ;
    }

    /**
     * Sets the value of field 'UF'. The field 'UF' has the
     * following description: UF do Emitente
     * 
     * @param UF the value of field 'UF'.
     */
    public void setUF(
            final com.mercurio.lms.mdfe.model.v100.types.TUf UF) {
        this._UF = UF;
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
     * @return the unmarshaled org.exolab.castor.builder.binding.Emi
     */
    public static com.mercurio.lms.mdfe.model.v100.Emi unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100.Emi) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100.Emi.class, reader);
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
