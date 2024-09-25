/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100a;

/**
 * 
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class SignedInfoType implements java.io.Serializable {

    private java.lang.String id;

    private com.mercurio.lms.mdfe.model.v100a.CanonicalizationMethod canonicalizationMethod;

    private com.mercurio.lms.mdfe.model.v100a.SignatureMethod signatureMethod;

    private com.mercurio.lms.mdfe.model.v100a.Reference reference;

    public SignedInfoType() {
        super();
    }

    /**
     * Returns the value of field 'canonicalizationMethod'.
     * 
     * @return the value of field 'CanonicalizationMethod'.
     */
    public com.mercurio.lms.mdfe.model.v100a.CanonicalizationMethod getCanonicalizationMethod() {
        return this.canonicalizationMethod;
    }

    /**
     * Returns the value of field 'id'.
     * 
     * @return the value of field 'Id'.
     */
    public java.lang.String getId() {
        return this.id;
    }

    /**
     * Returns the value of field 'reference'.
     * 
     * @return the value of field 'Reference'.
     */
    public com.mercurio.lms.mdfe.model.v100a.Reference getReference() {
        return this.reference;
    }

    /**
     * Returns the value of field 'signatureMethod'.
     * 
     * @return the value of field 'SignatureMethod'.
     */
    public com.mercurio.lms.mdfe.model.v100a.SignatureMethod getSignatureMethod() {
        return this.signatureMethod;
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
     * Sets the value of field 'canonicalizationMethod'.
     * 
     * @param canonicalizationMethod the value of field
     * 'canonicalizationMethod'.
     */
    public void setCanonicalizationMethod(final com.mercurio.lms.mdfe.model.v100a.CanonicalizationMethod canonicalizationMethod) {
        this.canonicalizationMethod = canonicalizationMethod;
    }

    /**
     * Sets the value of field 'id'.
     * 
     * @param id the value of field 'id'.
     */
    public void setId(final java.lang.String id) {
        this.id = id;
    }

    /**
     * Sets the value of field 'reference'.
     * 
     * @param reference the value of field 'reference'.
     */
    public void setReference(final com.mercurio.lms.mdfe.model.v100a.Reference reference) {
        this.reference = reference;
    }

    /**
     * Sets the value of field 'signatureMethod'.
     * 
     * @param signatureMethod the value of field 'signatureMethod'.
     */
    public void setSignatureMethod(final com.mercurio.lms.mdfe.model.v100a.SignatureMethod signatureMethod) {
        this.signatureMethod = signatureMethod;
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
     * com.mercurio.lms.mdfe.model.v100a.SignedInfoType
     */
    public static com.mercurio.lms.mdfe.model.v100a.SignedInfoType unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100a.SignedInfoType) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100a.SignedInfoType.class, reader);
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
