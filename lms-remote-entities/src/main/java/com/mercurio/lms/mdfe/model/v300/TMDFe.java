/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 * Tipo Manifesto de Documentos Fiscais Eletrônicos
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TMDFe implements java.io.Serializable {

    /**
     * Informações do MDF-e
     */
    private com.mercurio.lms.mdfe.model.v300.InfMDFe infMDFe;
    
    private com.mercurio.lms.mdfe.model.v300.InfMDFeSupl infMDFeSupl;

    private Signature signature;

    public TMDFe() {
        super();
    }

    /**
     * Returns the value of field 'infMDFe'. The field 'infMDFe'
     * has the following description: Informações do MDF-e
     * 
     * @return the value of field 'InfMDFe'.
     */
    public com.mercurio.lms.mdfe.model.v300.InfMDFe getInfMDFe() {
        return this.infMDFe;
    }
    
    public com.mercurio.lms.mdfe.model.v300.InfMDFeSupl getInfMDFeSupl() {
        return this.infMDFeSupl;
    }

    /**
     * Returns the value of field 'signature'.
     * 
     * @return the value of field 'Signature'.
     */
    public Signature getSignature() {
        return this.signature;
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
     * Sets the value of field 'infMDFe'. The field 'infMDFe' has
     * the following description: Informações do MDF-e
     * 
     * @param infMDFe the value of field 'infMDFe'.
     */
    public void setInfMDFe(final com.mercurio.lms.mdfe.model.v300.InfMDFe infMDFe) {
        this.infMDFe = infMDFe;
    }
    
    public void setInfMDFeSupl(final com.mercurio.lms.mdfe.model.v300.InfMDFeSupl infMDFeSupl) {
        this.infMDFeSupl = infMDFeSupl;
    }

    /**
     * Sets the value of field 'signature'.
     * 
     * @param signature the value of field 'signature'.
     */
    public void setSignature(final Signature signature) {
        this.signature = signature;
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
     * com.mercurio.lms.mdfe.model.v300.TMDFe
     */
    public static com.mercurio.lms.mdfe.model.v300.TMDFe unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.TMDFe) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.TMDFe.class, reader);
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
