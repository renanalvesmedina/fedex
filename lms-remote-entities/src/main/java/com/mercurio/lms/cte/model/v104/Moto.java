/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Informações do(s) Motorista(s)Só preenchido em CT-e
 * rodoviário de lotação
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Moto implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Nome do Motorista
     */
    private java.lang.String _xNome;

    /**
     * CPF do Motorista
     */
    private java.lang.String _CPF;


      //----------------/
     //- Constructors -/
    //----------------/

    public Moto() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'CPF'. The field 'CPF' has the
     * following description: CPF do Motorista
     * 
     * @return the value of field 'CPF'.
     */
    public java.lang.String getCPF(
    ) {
        return this._CPF;
    }

    /**
     * Returns the value of field 'xNome'. The field 'xNome' has
     * the following description: Nome do Motorista
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
     * Sets the value of field 'CPF'. The field 'CPF' has the
     * following description: CPF do Motorista
     * 
     * @param CPF the value of field 'CPF'.
     */
    public void setCPF(
            final java.lang.String CPF) {
        this._CPF = CPF;
    }

    /**
     * Sets the value of field 'xNome'. The field 'xNome' has the
     * following description: Nome do Motorista
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
     * @return the unmarshaled com.mercurio.lms.cte.model.Moto
     */
    public static com.mercurio.lms.cte.model.v104.Moto unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.Moto) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.Moto.class, reader);
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
