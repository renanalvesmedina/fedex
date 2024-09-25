/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Dados da cobrança do CT-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Cobr implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Dados da fatura
     */
    private com.mercurio.lms.cte.model.v104.Fat _fat;

    /**
     * Dados das duplicatas
     */
    private java.util.List<com.mercurio.lms.cte.model.v104.Dup> _dupList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Cobr() {
        super();
        this._dupList = new java.util.ArrayList<com.mercurio.lms.cte.model.v104.Dup>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vDup
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDup(
            final com.mercurio.lms.cte.model.v104.Dup vDup)
    throws java.lang.IndexOutOfBoundsException {
        this._dupList.add(vDup);
    }

    /**
     * 
     * 
     * @param index
     * @param vDup
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDup(
            final int index,
            final com.mercurio.lms.cte.model.v104.Dup vDup)
    throws java.lang.IndexOutOfBoundsException {
        this._dupList.add(index, vDup);
    }

    /**
     * Method enumerateDup.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v104.Dup> enumerateDup(
    ) {
        return java.util.Collections.enumeration(this._dupList);
    }

    /**
     * Method getDup.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.mercurio.lms.cte.model.Dup at
     * the given index
     */
    public com.mercurio.lms.cte.model.v104.Dup getDup(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._dupList.size()) {
            throw new IndexOutOfBoundsException("getDup: Index value '" + index + "' not in range [0.." + (this._dupList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v104.Dup) _dupList.get(index);
    }

    /**
     * Method getDup.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v104.Dup[] getDup(
    ) {
        com.mercurio.lms.cte.model.v104.Dup[] array = new com.mercurio.lms.cte.model.v104.Dup[0];
        return (com.mercurio.lms.cte.model.v104.Dup[]) this._dupList.toArray(array);
    }

    /**
     * Method getDupCount.
     * 
     * @return the size of this collection
     */
    public int getDupCount(
    ) {
        return this._dupList.size();
    }

    /**
     * Returns the value of field 'fat'. The field 'fat' has the
     * following description: Dados da fatura
     * 
     * @return the value of field 'Fat'.
     */
    public com.mercurio.lms.cte.model.v104.Fat getFat(
    ) {
        return this._fat;
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
     * Method iterateDup.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v104.Dup> iterateDup(
    ) {
        return this._dupList.iterator();
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
    public void removeAllDup(
    ) {
        this._dupList.clear();
    }

    /**
     * Method removeDup.
     * 
     * @param vDup
     * @return true if the object was removed from the collection.
     */
    public boolean removeDup(
            final com.mercurio.lms.cte.model.v104.Dup vDup) {
        boolean removed = _dupList.remove(vDup);
        return removed;
    }

    /**
     * Method removeDupAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v104.Dup removeDupAt(
            final int index) {
        java.lang.Object obj = this._dupList.remove(index);
        return (com.mercurio.lms.cte.model.v104.Dup) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vDup
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setDup(
            final int index,
            final com.mercurio.lms.cte.model.v104.Dup vDup)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._dupList.size()) {
            throw new IndexOutOfBoundsException("setDup: Index value '" + index + "' not in range [0.." + (this._dupList.size() - 1) + "]");
        }

        this._dupList.set(index, vDup);
    }

    /**
     * 
     * 
     * @param vDupArray
     */
    public void setDup(
            final com.mercurio.lms.cte.model.v104.Dup[] vDupArray) {
        //-- copy array
        _dupList.clear();

        for (int i = 0; i < vDupArray.length; i++) {
                this._dupList.add(vDupArray[i]);
        }
    }

    /**
     * Sets the value of field 'fat'. The field 'fat' has the
     * following description: Dados da fatura
     * 
     * @param fat the value of field 'fat'.
     */
    public void setFat(
            final com.mercurio.lms.cte.model.v104.Fat fat) {
        this._fat = fat;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.Cobr
     */
    public static com.mercurio.lms.cte.model.v104.Cobr unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.Cobr) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.Cobr.class, reader);
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
