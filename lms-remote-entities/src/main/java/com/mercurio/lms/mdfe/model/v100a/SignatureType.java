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
public class SignatureType implements java.io.Serializable {

    private java.lang.String id;

    private com.mercurio.lms.mdfe.model.v100a.SignedInfo signedInfo;

    private com.mercurio.lms.mdfe.model.v100a.SignatureValue signatureValue;

    private com.mercurio.lms.mdfe.model.v100a.KeyInfo keyInfo;

    public SignatureType() {
        super();
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
     * Returns the value of field 'keyInfo'.
     * 
     * @return the value of field 'KeyInfo'.
     */
    public com.mercurio.lms.mdfe.model.v100a.KeyInfo getKeyInfo() {
        return this.keyInfo;
    }

    /**
     * Returns the value of field 'signatureValue'.
     * 
     * @return the value of field 'SignatureValue'.
     */
    public com.mercurio.lms.mdfe.model.v100a.SignatureValue getSignatureValue() {
        return this.signatureValue;
    }

    /**
     * Returns the value of field 'signedInfo'.
     * 
     * @return the value of field 'SignedInfo'.
     */
    public com.mercurio.lms.mdfe.model.v100a.SignedInfo getSignedInfo() {
        return this.signedInfo;
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
     * Sets the value of field 'id'.
     * 
     * @param id the value of field 'id'.
     */
    public void setId(final java.lang.String id) {
        this.id = id;
    }

    /**
     * Sets the value of field 'keyInfo'.
     * 
     * @param keyInfo the value of field 'keyInfo'.
     */
    public void setKeyInfo(final com.mercurio.lms.mdfe.model.v100a.KeyInfo keyInfo) {
        this.keyInfo = keyInfo;
    }

    /**
     * Sets the value of field 'signatureValue'.
     * 
     * @param signatureValue the value of field 'signatureValue'.
     */
    public void setSignatureValue(final com.mercurio.lms.mdfe.model.v100a.SignatureValue signatureValue) {
        this.signatureValue = signatureValue;
    }

    /**
     * Sets the value of field 'signedInfo'.
     * 
     * @param signedInfo the value of field 'signedInfo'.
     */
    public void setSignedInfo(final com.mercurio.lms.mdfe.model.v100a.SignedInfo signedInfo) {
        this.signedInfo = signedInfo;
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
     * com.mercurio.lms.mdfe.model.v100a.SignatureType
     */
    public static com.mercurio.lms.mdfe.model.v100a.SignatureType unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100a.SignatureType) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100a.SignatureType.class, reader);
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
