/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

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
    private com.mercurio.lms.cte.model.v400.types.TTransformURI _algorithm;

    /**
     * Field _items.
     */
    private java.util.Vector<TransformTypeItem> _items;


      //----------------/
     //- Constructors -/
    //----------------/

    public TransformType() {
        super();
        this._items = new java.util.Vector<TransformTypeItem>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vTransformTypeItem
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addTransformTypeItem(
            final TransformTypeItem vTransformTypeItem)
    throws IndexOutOfBoundsException {
        this._items.addElement(vTransformTypeItem);
    }

    /**
     *
     *
     * @param index
     * @param vTransformTypeItem
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addTransformTypeItem(
            final int index,
            final TransformTypeItem vTransformTypeItem)
    throws IndexOutOfBoundsException {
        this._items.add(index, vTransformTypeItem);
    }

    /**
     * Method enumerateTransformTypeItem.
     *
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v400.TransformTypeItem elements
     */
    public java.util.Enumeration<? extends TransformTypeItem> enumerateTransformTypeItem(
    ) {
        return this._items.elements();
    }

    /**
     * Returns the value of field 'algorithm'.
     *
     * @return the value of field 'Algorithm'.
     */
    public com.mercurio.lms.cte.model.v400.types.TTransformURI getAlgorithm(
    ) {
        return this._algorithm;
    }

    /**
     * Method getTransformTypeItem.
     *
     * @param index
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v400.TransformTypeItem at the
     * given index
     */
    public TransformTypeItem getTransformTypeItem(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._items.size()) {
            throw new IndexOutOfBoundsException("getTransformTypeItem: Index value '" + index + "' not in range [0.." + (this._items.size() - 1) + "]");
        }

        return (TransformTypeItem) _items.get(index);
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
    public TransformTypeItem[] getTransformTypeItem(
    ) {
        TransformTypeItem[] array = new TransformTypeItem[0];
        return (TransformTypeItem[]) this._items.toArray(array);
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
            final TransformTypeItem vTransformTypeItem) {
        boolean removed = _items.remove(vTransformTypeItem);
        return removed;
    }

    /**
     * Method removeTransformTypeItemAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public TransformTypeItem removeTransformTypeItemAt(
            final int index) {
        Object obj = this._items.remove(index);
        return (TransformTypeItem) obj;
    }

    /**
     * Sets the value of field 'algorithm'.
     *
     * @param algorithm the value of field 'algorithm'.
     */
    public void setAlgorithm(
            final com.mercurio.lms.cte.model.v400.types.TTransformURI algorithm) {
        this._algorithm = algorithm;
    }

    /**
     *
     *
     * @param index
     * @param vTransformTypeItem
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setTransformTypeItem(
            final int index,
            final TransformTypeItem vTransformTypeItem)
    throws IndexOutOfBoundsException {
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
            final TransformTypeItem[] vTransformTypeItemArray) {
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
     * com.mercurio.lms.cte.model.v400.TransformType
     */
    public static TransformType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (TransformType) org.exolab.castor.xml.Unmarshaller.unmarshal(TransformType.class, reader);
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
