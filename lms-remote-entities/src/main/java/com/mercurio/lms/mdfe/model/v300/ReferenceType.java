/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 * 
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class ReferenceType implements java.io.Serializable {

    private java.lang.String id;

    private java.lang.String URI;

    private java.lang.String type;

    private com.mercurio.lms.mdfe.model.v300.Transforms transforms;

    private com.mercurio.lms.mdfe.model.v300.DigestMethod digestMethod;

    private byte[] digestValue;

    public ReferenceType() {
        super();
    }

    /**
     * Returns the value of field 'digestMethod'.
     * 
     * @return the value of field 'DigestMethod'.
     */
    public com.mercurio.lms.mdfe.model.v300.DigestMethod getDigestMethod() {
        return this.digestMethod;
    }

    /**
     * Returns the value of field 'digestValue'.
     * 
     * @return the value of field 'DigestValue'.
     */
    public byte[] getDigestValue() {
        return this.digestValue;
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
     * Returns the value of field 'transforms'.
     * 
     * @return the value of field 'Transforms'.
     */
    public com.mercurio.lms.mdfe.model.v300.Transforms getTransforms() {
        return this.transforms;
    }

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'Type'.
     */
    public java.lang.String getType() {
        return this.type;
    }

    /**
     * Returns the value of field 'URI'.
     * 
     * @return the value of field 'URI'.
     */
    public java.lang.String getURI() {
        return this.URI;
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
     * Sets the value of field 'digestMethod'.
     * 
     * @param digestMethod the value of field 'digestMethod'.
     */
    public void setDigestMethod(final com.mercurio.lms.mdfe.model.v300.DigestMethod digestMethod) {
        this.digestMethod = digestMethod;
    }

    /**
     * Sets the value of field 'digestValue'.
     * 
     * @param digestValue the value of field 'digestValue'.
     */
    public void setDigestValue(final byte[] digestValue) {
        this.digestValue = digestValue;
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
     * Sets the value of field 'transforms'.
     * 
     * @param transforms the value of field 'transforms'.
     */
    public void setTransforms(final com.mercurio.lms.mdfe.model.v300.Transforms transforms) {
        this.transforms = transforms;
    }

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(final java.lang.String type) {
        this.type = type;
    }

    /**
     * Sets the value of field 'URI'.
     * 
     * @param URI the value of field 'URI'.
     */
    public void setURI(final java.lang.String URI) {
        this.URI = URI;
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
     * com.mercurio.lms.mdfe.model.v300.ReferenceType
     */
    public static com.mercurio.lms.mdfe.model.v300.ReferenceType unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.ReferenceType) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.ReferenceType.class, reader);
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
