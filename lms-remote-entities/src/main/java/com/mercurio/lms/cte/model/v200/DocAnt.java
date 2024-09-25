/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v200;

/**
 * Documentos de Transporte Anterior
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class DocAnt implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Emissor do documento anterior
     */
    private java.util.Vector<com.mercurio.lms.cte.model.v200.EmiDocAnt> _emiDocAntList;


      //----------------/
     //- Constructors -/
    //----------------/

    public DocAnt() {
        super();
        this._emiDocAntList = new java.util.Vector<com.mercurio.lms.cte.model.v200.EmiDocAnt>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vEmiDocAnt
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addEmiDocAnt(
            final com.mercurio.lms.cte.model.v200.EmiDocAnt vEmiDocAnt)
    throws java.lang.IndexOutOfBoundsException {
        this._emiDocAntList.addElement(vEmiDocAnt);
    }

    /**
     * 
     * 
     * @param index
     * @param vEmiDocAnt
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addEmiDocAnt(
            final int index,
            final com.mercurio.lms.cte.model.v200.EmiDocAnt vEmiDocAnt)
    throws java.lang.IndexOutOfBoundsException {
        this._emiDocAntList.add(index, vEmiDocAnt);
    }

    /**
     * Method enumerateEmiDocAnt.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v200.EmiDocAnt elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v200.EmiDocAnt> enumerateEmiDocAnt(
    ) {
        return this._emiDocAntList.elements();
    }

    /**
     * Method getEmiDocAnt.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v200.EmiDocAnt at the given index
     */
    public com.mercurio.lms.cte.model.v200.EmiDocAnt getEmiDocAnt(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._emiDocAntList.size()) {
            throw new IndexOutOfBoundsException("getEmiDocAnt: Index value '" + index + "' not in range [0.." + (this._emiDocAntList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v200.EmiDocAnt) _emiDocAntList.get(index);
    }

    /**
     * Method getEmiDocAnt.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v200.EmiDocAnt[] getEmiDocAnt(
    ) {
        com.mercurio.lms.cte.model.v200.EmiDocAnt[] array = new com.mercurio.lms.cte.model.v200.EmiDocAnt[0];
        return (com.mercurio.lms.cte.model.v200.EmiDocAnt[]) this._emiDocAntList.toArray(array);
    }

    /**
     * Method getEmiDocAntCount.
     * 
     * @return the size of this collection
     */
    public int getEmiDocAntCount(
    ) {
        return this._emiDocAntList.size();
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
    public void removeAllEmiDocAnt(
    ) {
        this._emiDocAntList.clear();
    }

    /**
     * Method removeEmiDocAnt.
     * 
     * @param vEmiDocAnt
     * @return true if the object was removed from the collection.
     */
    public boolean removeEmiDocAnt(
            final com.mercurio.lms.cte.model.v200.EmiDocAnt vEmiDocAnt) {
        boolean removed = _emiDocAntList.remove(vEmiDocAnt);
        return removed;
    }

    /**
     * Method removeEmiDocAntAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v200.EmiDocAnt removeEmiDocAntAt(
            final int index) {
        java.lang.Object obj = this._emiDocAntList.remove(index);
        return (com.mercurio.lms.cte.model.v200.EmiDocAnt) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vEmiDocAnt
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setEmiDocAnt(
            final int index,
            final com.mercurio.lms.cte.model.v200.EmiDocAnt vEmiDocAnt)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._emiDocAntList.size()) {
            throw new IndexOutOfBoundsException("setEmiDocAnt: Index value '" + index + "' not in range [0.." + (this._emiDocAntList.size() - 1) + "]");
        }

        this._emiDocAntList.set(index, vEmiDocAnt);
    }

    /**
     * 
     * 
     * @param vEmiDocAntArray
     */
    public void setEmiDocAnt(
            final com.mercurio.lms.cte.model.v200.EmiDocAnt[] vEmiDocAntArray) {
        //-- copy array
        _emiDocAntList.clear();

        for (int i = 0; i < vEmiDocAntArray.length; i++) {
                this._emiDocAntList.add(vEmiDocAntArray[i]);
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
     * @return the unmarshaled com.mercurio.lms.cte.model.v200.DocAn
     */
    public static com.mercurio.lms.cte.model.v200.DocAnt unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v200.DocAnt) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v200.DocAnt.class, reader);
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
