/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 * Informações do Destinatário da NF
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Dest implements java.io.Serializable {

    private com.mercurio.lms.mdfe.model.v300.DestChoice destChoice;

    /**
     * Razão Social ou Nome 
     */
    private java.lang.String xNome;

    /**
     * UF do Destinatário
     */
    private com.mercurio.lms.mdfe.model.v300.types.TUf UF;

    public Dest() {
        super();
    }

    /**
     * Returns the value of field 'destChoice'.
     * 
     * @return the value of field 'DestChoice'.
     */
    public com.mercurio.lms.mdfe.model.v300.DestChoice getDestChoice() {
        return this.destChoice;
    }

    /**
     * Returns the value of field 'UF'. The field 'UF' has the
     * following description: UF do Destinatário
     * 
     * @return the value of field 'UF'.
     */
    public com.mercurio.lms.mdfe.model.v300.types.TUf getUF() {
        return this.UF;
    }

    /**
     * Returns the value of field 'xNome'. The field 'xNome' has
     * the following description: Razão Social ou Nome 
     * 
     * @return the value of field 'XNome'.
     */
    public java.lang.String getXNome() {
        return this.xNome;
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
     * Sets the value of field 'destChoice'.
     * 
     * @param destChoice the value of field 'destChoice'.
     */
    public void setDestChoice(final com.mercurio.lms.mdfe.model.v300.DestChoice destChoice) {
        this.destChoice = destChoice;
    }

    /**
     * Sets the value of field 'UF'. The field 'UF' has the
     * following description: UF do Destinatário
     * 
     * @param UF the value of field 'UF'.
     */
    public void setUF(final com.mercurio.lms.mdfe.model.v300.types.TUf UF) {
        this.UF = UF;
    }

    /**
     * Sets the value of field 'xNome'. The field 'xNome' has the
     * following description: Razão Social ou Nome 
     * 
     * @param xNome the value of field 'xNome'.
     */
    public void setXNome(final java.lang.String xNome) {
        this.xNome = xNome;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.mdfe.model.v300.Des
     */
    public static com.mercurio.lms.mdfe.model.v300.Dest unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.Dest) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.Dest.class, reader);
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
