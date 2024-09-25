/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Informações de identificação dos documentos de Transporte
 * Anterior
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class IdDocAnt implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Internal choice value storage
     */
    private java.lang.Object _choiceValue;

    /**
     * Documentos de transporte anterior em papel
     */
    private java.util.List<com.mercurio.lms.cte.model.v104.IdDocAntPap> _idDocAntPapList;

    /**
     * Documentos de transporte anterior eletrônicos
     */
    private java.util.List<com.mercurio.lms.cte.model.v104.IdDocAntEle> _idDocAntEleList;


      //----------------/
     //- Constructors -/
    //----------------/

    public IdDocAnt() {
        super();
        this._idDocAntPapList = new java.util.ArrayList<com.mercurio.lms.cte.model.v104.IdDocAntPap>();
        this._idDocAntEleList = new java.util.ArrayList<com.mercurio.lms.cte.model.v104.IdDocAntEle>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vIdDocAntEle
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addIdDocAntEle(
            final com.mercurio.lms.cte.model.v104.IdDocAntEle vIdDocAntEle)
    throws java.lang.IndexOutOfBoundsException {
        this._idDocAntEleList.add(vIdDocAntEle);
    }

    /**
     * 
     * 
     * @param index
     * @param vIdDocAntEle
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addIdDocAntEle(
            final int index,
            final com.mercurio.lms.cte.model.v104.IdDocAntEle vIdDocAntEle)
    throws java.lang.IndexOutOfBoundsException {
        this._idDocAntEleList.add(index, vIdDocAntEle);
    }

    /**
     * 
     * 
     * @param vIdDocAntPap
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addIdDocAntPap(
            final com.mercurio.lms.cte.model.v104.IdDocAntPap vIdDocAntPap)
    throws java.lang.IndexOutOfBoundsException {
        this._idDocAntPapList.add(vIdDocAntPap);
    }

    /**
     * 
     * 
     * @param index
     * @param vIdDocAntPap
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addIdDocAntPap(
            final int index,
            final com.mercurio.lms.cte.model.v104.IdDocAntPap vIdDocAntPap)
    throws java.lang.IndexOutOfBoundsException {
        this._idDocAntPapList.add(index, vIdDocAntPap);
    }

    /**
     * Method enumerateIdDocAntEle.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v104.IdDocAntEle> enumerateIdDocAntEle(
    ) {
        return java.util.Collections.enumeration(this._idDocAntEleList);
    }

    /**
     * Method enumerateIdDocAntPap.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v104.IdDocAntPap> enumerateIdDocAntPap(
    ) {
        return java.util.Collections.enumeration(this._idDocAntPapList);
    }

    /**
     * Returns the value of field 'choiceValue'. The field
     * 'choiceValue' has the following description: Internal choice
     * value storage
     * 
     * @return the value of field 'ChoiceValue'.
     */
    public java.lang.Object getChoiceValue(
    ) {
        return this._choiceValue;
    }

    /**
     * Method getIdDocAntEle.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.IdDocAntEle at the given index
     */
    public com.mercurio.lms.cte.model.v104.IdDocAntEle getIdDocAntEle(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._idDocAntEleList.size()) {
            throw new IndexOutOfBoundsException("getIdDocAntEle: Index value '" + index + "' not in range [0.." + (this._idDocAntEleList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v104.IdDocAntEle) _idDocAntEleList.get(index);
    }

    /**
     * Method getIdDocAntEle.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v104.IdDocAntEle[] getIdDocAntEle(
    ) {
        com.mercurio.lms.cte.model.v104.IdDocAntEle[] array = new com.mercurio.lms.cte.model.v104.IdDocAntEle[0];
        return (com.mercurio.lms.cte.model.v104.IdDocAntEle[]) this._idDocAntEleList.toArray(array);
    }

    /**
     * Method getIdDocAntEleCount.
     * 
     * @return the size of this collection
     */
    public int getIdDocAntEleCount(
    ) {
        return this._idDocAntEleList.size();
    }

    /**
     * Method getIdDocAntPap.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.IdDocAntPap at the given index
     */
    public com.mercurio.lms.cte.model.v104.IdDocAntPap getIdDocAntPap(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._idDocAntPapList.size()) {
            throw new IndexOutOfBoundsException("getIdDocAntPap: Index value '" + index + "' not in range [0.." + (this._idDocAntPapList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v104.IdDocAntPap) _idDocAntPapList.get(index);
    }

    /**
     * Method getIdDocAntPap.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v104.IdDocAntPap[] getIdDocAntPap(
    ) {
        com.mercurio.lms.cte.model.v104.IdDocAntPap[] array = new com.mercurio.lms.cte.model.v104.IdDocAntPap[0];
        return (com.mercurio.lms.cte.model.v104.IdDocAntPap[]) this._idDocAntPapList.toArray(array);
    }

    /**
     * Method getIdDocAntPapCount.
     * 
     * @return the size of this collection
     */
    public int getIdDocAntPapCount(
    ) {
        return this._idDocAntPapList.size();
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
     * Method iterateIdDocAntEle.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v104.IdDocAntEle> iterateIdDocAntEle(
    ) {
        return this._idDocAntEleList.iterator();
    }

    /**
     * Method iterateIdDocAntPap.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v104.IdDocAntPap> iterateIdDocAntPap(
    ) {
        return this._idDocAntPapList.iterator();
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
    public void removeAllIdDocAntEle(
    ) {
        this._idDocAntEleList.clear();
    }

    /**
     */
    public void removeAllIdDocAntPap(
    ) {
        this._idDocAntPapList.clear();
    }

    /**
     * Method removeIdDocAntEle.
     * 
     * @param vIdDocAntEle
     * @return true if the object was removed from the collection.
     */
    public boolean removeIdDocAntEle(
            final com.mercurio.lms.cte.model.v104.IdDocAntEle vIdDocAntEle) {
        boolean removed = _idDocAntEleList.remove(vIdDocAntEle);
        return removed;
    }

    /**
     * Method removeIdDocAntEleAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v104.IdDocAntEle removeIdDocAntEleAt(
            final int index) {
        java.lang.Object obj = this._idDocAntEleList.remove(index);
        return (com.mercurio.lms.cte.model.v104.IdDocAntEle) obj;
    }

    /**
     * Method removeIdDocAntPap.
     * 
     * @param vIdDocAntPap
     * @return true if the object was removed from the collection.
     */
    public boolean removeIdDocAntPap(
            final com.mercurio.lms.cte.model.v104.IdDocAntPap vIdDocAntPap) {
        boolean removed = _idDocAntPapList.remove(vIdDocAntPap);
        return removed;
    }

    /**
     * Method removeIdDocAntPapAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v104.IdDocAntPap removeIdDocAntPapAt(
            final int index) {
        java.lang.Object obj = this._idDocAntPapList.remove(index);
        return (com.mercurio.lms.cte.model.v104.IdDocAntPap) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vIdDocAntEle
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setIdDocAntEle(
            final int index,
            final com.mercurio.lms.cte.model.v104.IdDocAntEle vIdDocAntEle)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._idDocAntEleList.size()) {
            throw new IndexOutOfBoundsException("setIdDocAntEle: Index value '" + index + "' not in range [0.." + (this._idDocAntEleList.size() - 1) + "]");
        }

        this._idDocAntEleList.set(index, vIdDocAntEle);
    }

    /**
     * 
     * 
     * @param vIdDocAntEleArray
     */
    public void setIdDocAntEle(
            final com.mercurio.lms.cte.model.v104.IdDocAntEle[] vIdDocAntEleArray) {
        //-- copy array
        _idDocAntEleList.clear();

        for (int i = 0; i < vIdDocAntEleArray.length; i++) {
                this._idDocAntEleList.add(vIdDocAntEleArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vIdDocAntPap
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setIdDocAntPap(
            final int index,
            final com.mercurio.lms.cte.model.v104.IdDocAntPap vIdDocAntPap)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._idDocAntPapList.size()) {
            throw new IndexOutOfBoundsException("setIdDocAntPap: Index value '" + index + "' not in range [0.." + (this._idDocAntPapList.size() - 1) + "]");
        }

        this._idDocAntPapList.set(index, vIdDocAntPap);
    }

    /**
     * 
     * 
     * @param vIdDocAntPapArray
     */
    public void setIdDocAntPap(
            final com.mercurio.lms.cte.model.v104.IdDocAntPap[] vIdDocAntPapArray) {
        //-- copy array
        _idDocAntPapList.clear();

        for (int i = 0; i < vIdDocAntPapArray.length; i++) {
                this._idDocAntPapList.add(vIdDocAntPapArray[i]);
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
     * @return the unmarshaled com.mercurio.lms.cte.model.IdDocAnt
     */
    public static com.mercurio.lms.cte.model.v104.IdDocAnt unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.IdDocAnt) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.IdDocAnt.class, reader);
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
