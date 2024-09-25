/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v300;

/**
 * Class ReferenceType.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class ReferenceType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _id.
     */
    private java.lang.String _id;

    /**
     * Field _URI.
     */
    private java.lang.String _URI;

    /**
     * Field _type.
     */
    private java.lang.String _type;

    /**
     * Field _transforms.
     */
    private com.mercurio.lms.cte.model.v300.Transforms _transforms;

    /**
     * Field _digestMethod.
     */
    private com.mercurio.lms.cte.model.v300.DigestMethod _digestMethod;

    /**
     * Field _digestValue.
     */
    private byte[] _digestValue;


      //----------------/
     //- Constructors -/
    //----------------/

    public ReferenceType() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'digestMethod'.
     * 
     * @return the value of field 'DigestMethod'.
     */
    public com.mercurio.lms.cte.model.v300.DigestMethod getDigestMethod(
    ) {
        return this._digestMethod;
    }

    /**
     * Returns the value of field 'digestValue'.
     * 
     * @return the value of field 'DigestValue'.
     */
    public byte[] getDigestValue(
    ) {
        return this._digestValue;
    }

    /**
     * Returns the value of field 'id'.
     * 
     * @return the value of field 'Id'.
     */
    public java.lang.String getId(
    ) {
        return this._id;
    }

    /**
     * Returns the value of field 'transforms'.
     * 
     * @return the value of field 'Transforms'.
     */
    public com.mercurio.lms.cte.model.v300.Transforms getTransforms(
    ) {
        return this._transforms;
    }

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'Type'.
     */
    public java.lang.String getType(
    ) {
        return this._type;
    }

    /**
     * Returns the value of field 'URI'.
     * 
     * @return the value of field 'URI'.
     */
    public java.lang.String getURI(
    ) {
        return this._URI;
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
     * Sets the value of field 'digestMethod'.
     * 
     * @param digestMethod the value of field 'digestMethod'.
     */
    public void setDigestMethod(
            final com.mercurio.lms.cte.model.v300.DigestMethod digestMethod) {
        this._digestMethod = digestMethod;
    }

    /**
     * Sets the value of field 'digestValue'.
     * 
     * @param digestValue the value of field 'digestValue'.
     */
    public void setDigestValue(
            final byte[] digestValue) {
        this._digestValue = digestValue;
    }

    /**
     * Sets the value of field 'id'.
     * 
     * @param id the value of field 'id'.
     */
    public void setId(
            final java.lang.String id) {
        this._id = id;
    }

    /**
     * Sets the value of field 'transforms'.
     * 
     * @param transforms the value of field 'transforms'.
     */
    public void setTransforms(
            final com.mercurio.lms.cte.model.v300.Transforms transforms) {
        this._transforms = transforms;
    }

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(
            final java.lang.String type) {
        this._type = type;
    }

    /**
     * Sets the value of field 'URI'.
     * 
     * @param URI the value of field 'URI'.
     */
    public void setURI(
            final java.lang.String URI) {
        this._URI = URI;
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
     * com.mercurio.lms.cte.model.v300.ReferenceType
     */
    public static com.mercurio.lms.cte.model.v300.ReferenceType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v300.ReferenceType) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v300.ReferenceType.class, reader);
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
