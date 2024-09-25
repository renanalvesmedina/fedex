/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v200;

/**
 * Class SignedInfoType.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class SignedInfoType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _id.
     */
    private java.lang.String _id;

    /**
     * Field _canonicalizationMethod.
     */
    private com.mercurio.lms.cte.model.v200.CanonicalizationMethod _canonicalizationMethod;

    /**
     * Field _signatureMethod.
     */
    private com.mercurio.lms.cte.model.v200.SignatureMethod _signatureMethod;

    /**
     * Field _reference.
     */
    private com.mercurio.lms.cte.model.v200.Reference _reference;


      //----------------/
     //- Constructors -/
    //----------------/

    public SignedInfoType() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'canonicalizationMethod'.
     * 
     * @return the value of field 'CanonicalizationMethod'.
     */
    public com.mercurio.lms.cte.model.v200.CanonicalizationMethod getCanonicalizationMethod(
    ) {
        return this._canonicalizationMethod;
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
     * Returns the value of field 'reference'.
     * 
     * @return the value of field 'Reference'.
     */
    public com.mercurio.lms.cte.model.v200.Reference getReference(
    ) {
        return this._reference;
    }

    /**
     * Returns the value of field 'signatureMethod'.
     * 
     * @return the value of field 'SignatureMethod'.
     */
    public com.mercurio.lms.cte.model.v200.SignatureMethod getSignatureMethod(
    ) {
        return this._signatureMethod;
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
     * Sets the value of field 'canonicalizationMethod'.
     * 
     * @param canonicalizationMethod the value of field
     * 'canonicalizationMethod'.
     */
    public void setCanonicalizationMethod(
            final com.mercurio.lms.cte.model.v200.CanonicalizationMethod canonicalizationMethod) {
        this._canonicalizationMethod = canonicalizationMethod;
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
     * Sets the value of field 'reference'.
     * 
     * @param reference the value of field 'reference'.
     */
    public void setReference(
            final com.mercurio.lms.cte.model.v200.Reference reference) {
        this._reference = reference;
    }

    /**
     * Sets the value of field 'signatureMethod'.
     * 
     * @param signatureMethod the value of field 'signatureMethod'.
     */
    public void setSignatureMethod(
            final com.mercurio.lms.cte.model.v200.SignatureMethod signatureMethod) {
        this._signatureMethod = signatureMethod;
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
     * com.mercurio.lms.cte.model.v200.SignedInfoType
     */
    public static com.mercurio.lms.cte.model.v200.SignedInfoType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v200.SignedInfoType) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v200.SignedInfoType.class, reader);
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
