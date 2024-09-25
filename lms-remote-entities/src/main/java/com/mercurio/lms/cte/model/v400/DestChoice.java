/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Class DestChoice.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class DestChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Número do CNPJ
     */
    private String _CNPJ;

    /**
     * Número do CPF
     */
    private String _CPF;


      //----------------/
     //- Constructors -/
    //----------------/

    public DestChoice() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'CNPJ'. The field 'CNPJ' has the
     * following description: Número do CNPJ
     *
     * @return the value of field 'CNPJ'.
     */
    public String getCNPJ(
    ) {
        return this._CNPJ;
    }

    /**
     * Returns the value of field 'CPF'. The field 'CPF' has the
     * following description: Número do CPF
     *
     * @return the value of field 'CPF'.
     */
    public String getCPF(
    ) {
        return this._CPF;
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
     * following description: Número do CNPJ
     *
     * @param CNPJ the value of field 'CNPJ'.
     */
    public void setCNPJ(
            final String CNPJ) {
        this._CNPJ = CNPJ;
    }

    /**
     * Sets the value of field 'CPF'. The field 'CPF' has the
     * following description: Número do CPF
     *
     * @param CPF the value of field 'CPF'.
     */
    public void setCPF(
            final String CPF) {
        this._CPF = CPF;
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
     * com.mercurio.lms.cte.model.v400.DestChoice
     */
    public static DestChoice unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (DestChoice) org.exolab.castor.xml.Unmarshaller.unmarshal(DestChoice.class, reader);
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
