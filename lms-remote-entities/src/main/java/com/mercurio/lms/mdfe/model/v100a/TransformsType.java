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
public class TransformsType implements java.io.Serializable {

    private java.util.Vector<com.mercurio.lms.mdfe.model.v100a.Transform> transformList;

    public TransformsType() {
        super();
        this.transformList = new java.util.Vector<com.mercurio.lms.mdfe.model.v100a.Transform>();
    }

    /**
     * 
     * 
     * @param vTransform
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addTransform(final com.mercurio.lms.mdfe.model.v100a.Transform vTransform) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.transformList.size() >= 2) {
            throw new IndexOutOfBoundsException("addTransform has a maximum of 2");
        }

        this.transformList.addElement(vTransform);
    }

    /**
     * 
     * 
     * @param index
     * @param vTransform
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addTransform(final int index,final com.mercurio.lms.mdfe.model.v100a.Transform vTransform) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.transformList.size() >= 2) {
            throw new IndexOutOfBoundsException("addTransform has a maximum of 2");
        }

        this.transformList.add(index, vTransform);
    }

    /**
     * Method enumerateTransform.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.mdfe.model.v100a.Transform elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v100a.Transform> enumerateTransform() {
        return this.transformList.elements();
    }

    /**
     * Method getTransform.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.mdfe.model.v100a.Transform at the given inde
     */
    public com.mercurio.lms.mdfe.model.v100a.Transform getTransform(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.transformList.size()) {
            throw new IndexOutOfBoundsException("getTransform: Index value '" + index + "' not in range [0.." + (this.transformList.size() - 1) + "]");
        }

        return transformList.get(index);
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
    public com.mercurio.lms.mdfe.model.v100a.Transform[] getTransform() {
        com.mercurio.lms.mdfe.model.v100a.Transform[] array = new com.mercurio.lms.mdfe.model.v100a.Transform[0];
        return this.transformList.toArray(array);
    }

    /**
     * Method getTransformCount.
     * 
     * @return the size of this collection
     */
    public int getTransformCount() {
        return this.transformList.size();
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
     */
    public void removeAllTransform() {
        this.transformList.clear();
    }

    /**
     * Method removeTransform.
     * 
     * @param vTransform
     * @return true if the object was removed from the collection.
     */
    public boolean removeTransform(final com.mercurio.lms.mdfe.model.v100a.Transform vTransform) {
        boolean removed = transformList.remove(vTransform);
        return removed;
    }

    /**
     * Method removeTransformAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v100a.Transform removeTransformAt(final int index) {
        java.lang.Object obj = this.transformList.remove(index);
        return (com.mercurio.lms.mdfe.model.v100a.Transform) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vTransform
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setTransform(final int index,final com.mercurio.lms.mdfe.model.v100a.Transform vTransform) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.transformList.size()) {
            throw new IndexOutOfBoundsException("setTransform: Index value '" + index + "' not in range [0.." + (this.transformList.size() - 1) + "]");
        }

        this.transformList.set(index, vTransform);
    }

    /**
     * 
     * 
     * @param vTransformArray
     */
    public void setTransform(final com.mercurio.lms.mdfe.model.v100a.Transform[] vTransformArray) {
        //-- copy array
        transformList.clear();

        for (int i = 0; i < vTransformArray.length; i++) {
                this.transformList.add(vTransformArray[i]);
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
     * com.mercurio.lms.mdfe.model.v100a.TransformsType
     */
    public static com.mercurio.lms.mdfe.model.v100a.TransformsType unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100a.TransformsType) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100a.TransformsType.class, reader);
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
