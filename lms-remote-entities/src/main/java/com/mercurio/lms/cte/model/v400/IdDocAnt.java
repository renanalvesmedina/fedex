/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

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
    private Object _choiceValue;

    /**
     * Documentos de transporte anterior em papel
     */
    private java.util.Vector<IdDocAntPap> _idDocAntPapList;

    /**
     * Documentos de transporte anterior eletrônicos
     */
    private java.util.Vector<IdDocAntEle> _idDocAntEleList;


      //----------------/
     //- Constructors -/
    //----------------/

    public IdDocAnt() {
        super();
        this._idDocAntPapList = new java.util.Vector<IdDocAntPap>();
        this._idDocAntEleList = new java.util.Vector<IdDocAntEle>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     *
     *
     * @param vIdDocAntEle
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addIdDocAntEle(
            final IdDocAntEle vIdDocAntEle)
    throws IndexOutOfBoundsException {
        this._idDocAntEleList.addElement(vIdDocAntEle);
    }

    /**
     *
     *
     * @param index
     * @param vIdDocAntEle
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addIdDocAntEle(
            final int index,
            final IdDocAntEle vIdDocAntEle)
    throws IndexOutOfBoundsException {
        this._idDocAntEleList.add(index, vIdDocAntEle);
    }

    /**
     *
     *
     * @param vIdDocAntPap
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addIdDocAntPap(
            final IdDocAntPap vIdDocAntPap)
    throws IndexOutOfBoundsException {
        this._idDocAntPapList.addElement(vIdDocAntPap);
    }

    /**
     *
     *
     * @param index
     * @param vIdDocAntPap
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addIdDocAntPap(
            final int index,
            final IdDocAntPap vIdDocAntPap)
    throws IndexOutOfBoundsException {
        this._idDocAntPapList.add(index, vIdDocAntPap);
    }

    /**
     * Method enumerateIdDocAntEle.
     *
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v400.IdDocAntEle elements
     */
    public java.util.Enumeration<? extends IdDocAntEle> enumerateIdDocAntEle(
    ) {
        return this._idDocAntEleList.elements();
    }

    /**
     * Method enumerateIdDocAntPap.
     *
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v400.IdDocAntPap elements
     */
    public java.util.Enumeration<? extends IdDocAntPap> enumerateIdDocAntPap(
    ) {
        return this._idDocAntPapList.elements();
    }

    /**
     * Returns the value of field 'choiceValue'. The field
     * 'choiceValue' has the following description: Internal choice
     * value storage
     *
     * @return the value of field 'ChoiceValue'.
     */
    public Object getChoiceValue(
    ) {
        return this._choiceValue;
    }

    /**
     * Method getIdDocAntEle.
     *
     * @param index
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v400.IdDocAntEle at the given inde
     */
    public IdDocAntEle getIdDocAntEle(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._idDocAntEleList.size()) {
            throw new IndexOutOfBoundsException("getIdDocAntEle: Index value '" + index + "' not in range [0.." + (this._idDocAntEleList.size() - 1) + "]");
        }

        return (IdDocAntEle) _idDocAntEleList.get(index);
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
    public IdDocAntEle[] getIdDocAntEle(
    ) {
        IdDocAntEle[] array = new IdDocAntEle[0];
        return (IdDocAntEle[]) this._idDocAntEleList.toArray(array);
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
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v400.IdDocAntPap at the given inde
     */
    public IdDocAntPap getIdDocAntPap(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._idDocAntPapList.size()) {
            throw new IndexOutOfBoundsException("getIdDocAntPap: Index value '" + index + "' not in range [0.." + (this._idDocAntPapList.size() - 1) + "]");
        }

        return (IdDocAntPap) _idDocAntPapList.get(index);
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
    public IdDocAntPap[] getIdDocAntPap(
    ) {
        IdDocAntPap[] array = new IdDocAntPap[0];
        return (IdDocAntPap[]) this._idDocAntPapList.toArray(array);
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
            final IdDocAntEle vIdDocAntEle) {
        boolean removed = _idDocAntEleList.remove(vIdDocAntEle);
        return removed;
    }

    /**
     * Method removeIdDocAntEleAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public IdDocAntEle removeIdDocAntEleAt(
            final int index) {
        Object obj = this._idDocAntEleList.remove(index);
        return (IdDocAntEle) obj;
    }

    /**
     * Method removeIdDocAntPap.
     *
     * @param vIdDocAntPap
     * @return true if the object was removed from the collection.
     */
    public boolean removeIdDocAntPap(
            final IdDocAntPap vIdDocAntPap) {
        boolean removed = _idDocAntPapList.remove(vIdDocAntPap);
        return removed;
    }

    /**
     * Method removeIdDocAntPapAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public IdDocAntPap removeIdDocAntPapAt(
            final int index) {
        Object obj = this._idDocAntPapList.remove(index);
        return (IdDocAntPap) obj;
    }

    /**
     *
     *
     * @param index
     * @param vIdDocAntEle
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setIdDocAntEle(
            final int index,
            final IdDocAntEle vIdDocAntEle)
    throws IndexOutOfBoundsException {
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
            final IdDocAntEle[] vIdDocAntEleArray) {
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
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setIdDocAntPap(
            final int index,
            final IdDocAntPap vIdDocAntPap)
    throws IndexOutOfBoundsException {
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
            final IdDocAntPap[] vIdDocAntPapArray) {
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
     * @return the unmarshaled
     * com.mercurio.lms.cte.model.v400.IdDocAnt
     */
    public static IdDocAnt unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (IdDocAnt) org.exolab.castor.xml.Unmarshaller.unmarshal(IdDocAnt.class, reader);
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
