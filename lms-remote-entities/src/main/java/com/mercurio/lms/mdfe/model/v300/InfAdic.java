/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 * Informações Adicionais
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfAdic implements java.io.Serializable {

    /**
     * Informações adicionais de interesse do Fisco
     */
    private java.lang.String infAdFisco;

    /**
     * Informações complementares de interesse do Contribuinte
     */
    private java.lang.String infCpl;

    public InfAdic() {
        super();
    }

    /**
     * Returns the value of field 'infAdFisco'. The field
     * 'infAdFisco' has the following description: Informações
     * adicionais de interesse do Fisco
     * 
     * @return the value of field 'InfAdFisco'.
     */
    public java.lang.String getInfAdFisco() {
        return this.infAdFisco;
    }

    /**
     * Returns the value of field 'infCpl'. The field 'infCpl' has
     * the following description: Informações complementares de
     * interesse do Contribuinte
     * 
     * @return the value of field 'InfCpl'.
     */
    public java.lang.String getInfCpl() {
        return this.infCpl;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid() {
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
    public void marshal(final java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
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
    public void marshal(final org.xml.sax.ContentHandler handler) throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'infAdFisco'. The field 'infAdFisco'
     * has the following description: Informações adicionais de
     * interesse do Fisco
     * 
     * @param infAdFisco the value of field 'infAdFisco'.
     */
    public void setInfAdFisco(final java.lang.String infAdFisco) {
        this.infAdFisco = infAdFisco;
    }

    /**
     * Sets the value of field 'infCpl'. The field 'infCpl' has the
     * following description: Informações complementares de
     * interesse do Contribuinte
     * 
     * @param infCpl the value of field 'infCpl'.
     */
    public void setInfCpl(final java.lang.String infCpl) {
        this.infCpl = infCpl;
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
     * com.mercurio.lms.mdfe.model.v300.InfAdic
     */
    public static com.mercurio.lms.mdfe.model.v300.InfAdic unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.InfAdic) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.InfAdic.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
