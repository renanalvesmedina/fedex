/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Class TransformType.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TransformType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _algorithm.
     */
    private com.mercurio.lms.cte.model.v104.types.TTransformURI _algorithm;

    /**
     * Field _items.
     */
    private java.util.List<com.mercurio.lms.cte.model.v104.TransformTypeItem> _items;


      //----------------/
     //- Constructors -/
    //----------------/

    public TransformType() {
        super();
        this._items = new java.util.ArrayList<com.mercurio.lms.cte.model.v104.TransformTypeItem>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vTransformTypeItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addTransformTypeItem(
            final com.mercurio.lms.cte.model.v104.TransformTypeItem vTransformTypeItem)
    throws java.lang.IndexOutOfBoundsException {
        this._items.add(vTransformTypeItem);
    }

    /**
     * 
     * 
     * @param index
     * @param vTransformTypeItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addTransformTypeItem(
            final int index,
            final com.mercurio.lms.cte.model.v104.TransformTypeItem vTransformTypeItem)
    throws java.lang.IndexOutOfBoundsException {
        this._items.add(index, vTransformTypeItem);
    }

    /**
     * Method enumerateTransformTypeItem.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v104.TransformTypeItem> enumerateTransformTypeItem(
    ) {
        return java.util.Collections.enumeration(this._items);
    }

    /**
     * Returns the value of field 'algorithm'.
     * 
     * @return the value of field 'Algorithm'.
     */
    public com.mercurio.lms.cte.model.v104.types.TTransformURI getAlgorithm(
    ) {
        return this._algorithm;
    }

    /**
     * Method getTransformTypeItem.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.TransformTypeItem at the given
     * index
     */
    public com.mercurio.lms.cte.model.v104.TransformTypeItem getTransformTypeItem(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._items.size()) {
            throw new IndexOutOfBoundsException("getTransformTypeItem: Index value '" + index + "' not in range [0.." + (this._items.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v104.TransformTypeItem) _items.get(index);
    }

    /**
     * Method getTransformTypeItem.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v104.TransformTypeItem[] getTransformTypeItem(
    ) {
        com.mercurio.lms.cte.model.v104.TransformTypeItem[] array = new com.mercurio.lms.cte.model.v104.TransformTypeItem[0];
        return (com.mercurio.lms.cte.model.v104.TransformTypeItem[]) this._items.toArray(array);
    }

    /**
     * Method getTransformTypeItemCount.
     * 
     * @return the size of this collection
     */
    public int getTransformTypeItemCount(
    ) {
        return this._items.size();
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
     * Method iterateTransformTypeItem.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v104.TransformTypeItem> iterateTransformTypeItem(
    ) {
        return this._items.iterator();
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
    public void removeAllTransformTypeItem(
    ) {
        this._items.clear();
    }

    /**
     * Method removeTransformTypeItem.
     * 
     * @param vTransformTypeItem
     * @return true if the object was removed from the collection.
     */
    public boolean removeTransformTypeItem(
            final com.mercurio.lms.cte.model.v104.TransformTypeItem vTransformTypeItem) {
        boolean removed = _items.remove(vTransformTypeItem);
        return removed;
    }

    /**
     * Method removeTransformTypeItemAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v104.TransformTypeItem removeTransformTypeItemAt(
            final int index) {
        java.lang.Object obj = this._items.remove(index);
        return (com.mercurio.lms.cte.model.v104.TransformTypeItem) obj;
    }

    /**
     * Sets the value of field 'algorithm'.
     * 
     * @param algorithm the value of field 'algorithm'.
     */
    public void setAlgorithm(
            final com.mercurio.lms.cte.model.v104.types.TTransformURI algorithm) {
        this._algorithm = algorithm;
    }

    /**
     * 
     * 
     * @param index
     * @param vTransformTypeItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setTransformTypeItem(
            final int index,
            final com.mercurio.lms.cte.model.v104.TransformTypeItem vTransformTypeItem)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._items.size()) {
            throw new IndexOutOfBoundsException("setTransformTypeItem: Index value '" + index + "' not in range [0.." + (this._items.size() - 1) + "]");
        }

        this._items.set(index, vTransformTypeItem);
    }

    /**
     * 
     * 
     * @param vTransformTypeItemArray
     */
    public void setTransformTypeItem(
            final com.mercurio.lms.cte.model.v104.TransformTypeItem[] vTransformTypeItemArray) {
        //-- copy array
        _items.clear();

        for (int i = 0; i < vTransformTypeItemArray.length; i++) {
                this._items.add(vTransformTypeItemArray[i]);
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
     * com.mercurio.lms.cte.model.TransformType
     */
    public static com.mercurio.lms.cte.model.v104.TransformType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.TransformType) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.TransformType.class, reader);
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
