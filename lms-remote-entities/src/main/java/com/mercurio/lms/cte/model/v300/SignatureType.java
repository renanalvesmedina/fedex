/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v300;

/**
 * Class SignatureType.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class SignatureType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _id.
     */
    private java.lang.String _id;

    /**
     * Field _signedInfo.
     */
    private com.mercurio.lms.cte.model.v300.SignedInfo _signedInfo;

    /**
     * Field _signatureValue.
     */
    private com.mercurio.lms.cte.model.v300.SignatureValue _signatureValue;

    /**
     * Field _keyInfo.
     */
    private com.mercurio.lms.cte.model.v300.KeyInfo _keyInfo;


      //----------------/
     //- Constructors -/
    //----------------/

    public SignatureType() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

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
     * Returns the value of field 'keyInfo'.
     * 
     * @return the value of field 'KeyInfo'.
     */
    public com.mercurio.lms.cte.model.v300.KeyInfo getKeyInfo(
    ) {
        return this._keyInfo;
    }

    /**
     * Returns the value of field 'signatureValue'.
     * 
     * @return the value of field 'SignatureValue'.
     */
    public com.mercurio.lms.cte.model.v300.SignatureValue getSignatureValue(
    ) {
        return this._signatureValue;
    }

    /**
     * Returns the value of field 'signedInfo'.
     * 
     * @return the value of field 'SignedInfo'.
     */
    public com.mercurio.lms.cte.model.v300.SignedInfo getSignedInfo(
    ) {
        return this._signedInfo;
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
     * Sets the value of field 'id'.
     * 
     * @param id the value of field 'id'.
     */
    public void setId(
            final java.lang.String id) {
        this._id = id;
    }

    /**
     * Sets the value of field 'keyInfo'.
     * 
     * @param keyInfo the value of field 'keyInfo'.
     */
    public void setKeyInfo(
            final com.mercurio.lms.cte.model.v300.KeyInfo keyInfo) {
        this._keyInfo = keyInfo;
    }

    /**
     * Sets the value of field 'signatureValue'.
     * 
     * @param signatureValue the value of field 'signatureValue'.
     */
    public void setSignatureValue(
            final com.mercurio.lms.cte.model.v300.SignatureValue signatureValue) {
        this._signatureValue = signatureValue;
    }

    /**
     * Sets the value of field 'signedInfo'.
     * 
     * @param signedInfo the value of field 'signedInfo'.
     */
    public void setSignedInfo(
            final com.mercurio.lms.cte.model.v300.SignedInfo signedInfo) {
        this._signedInfo = signedInfo;
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
     * com.mercurio.lms.cte.model.v300.SignatureType
     */
    public static com.mercurio.lms.cte.model.v300.SignatureType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v300.SignatureType) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v300.SignatureType.class, reader);
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
