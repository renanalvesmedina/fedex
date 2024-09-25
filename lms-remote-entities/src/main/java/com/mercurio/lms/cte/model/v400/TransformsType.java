/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Class TransformsType.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TransformsType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _transformList.
     */
    private java.util.Vector<Transform> _transformList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TransformsType() {
        super();
        this._transformList = new java.util.Vector<Transform>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vTransform
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addTransform(
            final Transform vTransform)
    throws IndexOutOfBoundsException {
        // check for the maximum size
        if (this._transformList.size() >= 2) {
            throw new IndexOutOfBoundsException("addTransform has a maximum of 2");
        }

        this._transformList.addElement(vTransform);
    }

    /**
     *
     *
     * @param index
     * @param vTransform
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addTransform(
            final int index,
            final Transform vTransform)
    throws IndexOutOfBoundsException {
        // check for the maximum size
        if (this._transformList.size() >= 2) {
            throw new IndexOutOfBoundsException("addTransform has a maximum of 2");
        }

        this._transformList.add(index, vTransform);
    }

    /**
     * Method enumerateTransform.
     *
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v400.Transform elements
     */
    public java.util.Enumeration<? extends Transform> enumerateTransform(
    ) {
        return this._transformList.elements();
    }

    /**
     * Method getTransform.
     *
     * @param index
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v400.Transform at the given index
     */
    public Transform getTransform(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._transformList.size()) {
            throw new IndexOutOfBoundsException("getTransform: Index value '" + index + "' not in range [0.." + (this._transformList.size() - 1) + "]");
        }

        return (Transform) _transformList.get(index);
    }

    /**
     * Method getTransform.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     *
     * @return this collection as an Array
     */
    public Transform[] getTransform(
    ) {
        Transform[] array = new Transform[0];
        return (Transform[]) this._transformList.toArray(array);
    }

    /**
     * Method getTransformCount.
     *
     * @return the size of this collection
     */
    public int getTransformCount(
    ) {
        return this._transformList.size();
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
     */
    public void removeAllTransform(
    ) {
        this._transformList.clear();
    }

    /**
     * Method removeTransform.
     *
     * @param vTransform
     * @return true if the object was removed from the collection.
     */
    public boolean removeTransform(
            final Transform vTransform) {
        boolean removed = _transformList.remove(vTransform);
        return removed;
    }

    /**
     * Method removeTransformAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public Transform removeTransformAt(
            final int index) {
        Object obj = this._transformList.remove(index);
        return (Transform) obj;
    }

    /**
     *
     *
     * @param index
     * @param vTransform
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setTransform(
            final int index,
            final Transform vTransform)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._transformList.size()) {
            throw new IndexOutOfBoundsException("setTransform: Index value '" + index + "' not in range [0.." + (this._transformList.size() - 1) + "]");
        }

        this._transformList.set(index, vTransform);
    }

    /**
     * 
     * 
     * @param vTransformArray
     */
    public void setTransform(
            final Transform[] vTransformArray) {
        //-- copy array
        _transformList.clear();

        for (int i = 0; i < vTransformArray.length; i++) {
                this._transformList.add(vTransformArray[i]);
        }
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
     * com.mercurio.lms.cte.model.v400.TransformsType
     */
    public static TransformsType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (TransformsType) org.exolab.castor.xml.Unmarshaller.unmarshal(TransformsType.class, reader);
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
