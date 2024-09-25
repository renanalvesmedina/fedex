/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Class VPresCompSequence.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class VPresCompSequence implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Componentes do valor da prestação
     */
    private java.util.List<com.mercurio.lms.cte.model.v104.CompComp> _compCompList;


      //----------------/
     //- Constructors -/
    //----------------/

    public VPresCompSequence() {
        super();
        this._compCompList = new java.util.ArrayList<com.mercurio.lms.cte.model.v104.CompComp>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vCompComp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCompComp(
            final com.mercurio.lms.cte.model.v104.CompComp vCompComp)
    throws java.lang.IndexOutOfBoundsException {
        this._compCompList.add(vCompComp);
    }

    /**
     * 
     * 
     * @param index
     * @param vCompComp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCompComp(
            final int index,
            final com.mercurio.lms.cte.model.v104.CompComp vCompComp)
    throws java.lang.IndexOutOfBoundsException {
        this._compCompList.add(index, vCompComp);
    }

    /**
     * Method enumerateCompComp.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v104.CompComp> enumerateCompComp(
    ) {
        return java.util.Collections.enumeration(this._compCompList);
    }

    /**
     * Method getCompComp.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.mercurio.lms.cte.model.CompComp
     * at the given index
     */
    public com.mercurio.lms.cte.model.v104.CompComp getCompComp(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._compCompList.size()) {
            throw new IndexOutOfBoundsException("getCompComp: Index value '" + index + "' not in range [0.." + (this._compCompList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v104.CompComp) _compCompList.get(index);
    }

    /**
     * Method getCompComp.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v104.CompComp[] getCompComp(
    ) {
        com.mercurio.lms.cte.model.v104.CompComp[] array = new com.mercurio.lms.cte.model.v104.CompComp[0];
        return (com.mercurio.lms.cte.model.v104.CompComp[]) this._compCompList.toArray(array);
    }

    /**
     * Method getCompCompCount.
     * 
     * @return the size of this collection
     */
    public int getCompCompCount(
    ) {
        return this._compCompList.size();
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
     * Method iterateCompComp.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v104.CompComp> iterateCompComp(
    ) {
        return this._compCompList.iterator();
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
    public void removeAllCompComp(
    ) {
        this._compCompList.clear();
    }

    /**
     * Method removeCompComp.
     * 
     * @param vCompComp
     * @return true if the object was removed from the collection.
     */
    public boolean removeCompComp(
            final com.mercurio.lms.cte.model.v104.CompComp vCompComp) {
        boolean removed = _compCompList.remove(vCompComp);
        return removed;
    }

    /**
     * Method removeCompCompAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v104.CompComp removeCompCompAt(
            final int index) {
        java.lang.Object obj = this._compCompList.remove(index);
        return (com.mercurio.lms.cte.model.v104.CompComp) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vCompComp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setCompComp(
            final int index,
            final com.mercurio.lms.cte.model.v104.CompComp vCompComp)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._compCompList.size()) {
            throw new IndexOutOfBoundsException("setCompComp: Index value '" + index + "' not in range [0.." + (this._compCompList.size() - 1) + "]");
        }

        this._compCompList.set(index, vCompComp);
    }

    /**
     * 
     * 
     * @param vCompCompArray
     */
    public void setCompComp(
            final com.mercurio.lms.cte.model.v104.CompComp[] vCompCompArray) {
        //-- copy array
        _compCompList.clear();

        for (int i = 0; i < vCompCompArray.length; i++) {
                this._compCompList.add(vCompCompArray[i]);
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
     * com.mercurio.lms.cte.model.VPresCompSequence
     */
    public static com.mercurio.lms.cte.model.v104.VPresCompSequence unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.VPresCompSequence) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.VPresCompSequence.class, reader);
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
