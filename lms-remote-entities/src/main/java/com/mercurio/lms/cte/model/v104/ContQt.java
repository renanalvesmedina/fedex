/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Informações dos containeres/ULDDispositivo de carga unitizada
 * (Unit Load Device - ULD) significa todo tipo de contêiner de
 * carga, contêiner de avião, palete de aeronave com rede ou
 * palete de aeronave com rede sobre um iglu. 
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class ContQt implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Número do Container/ULD
     */
    private java.lang.String _nCont;

    /**
     * Lacres dos containeres/ULD
     */
    private java.util.List<com.mercurio.lms.cte.model.v104.LacContQt> _lacContQtList;

    /**
     * Data prevista de entrega
     */
    private java.lang.String _dPrev;


      //----------------/
     //- Constructors -/
    //----------------/

    public ContQt() {
        super();
        this._lacContQtList = new java.util.ArrayList<com.mercurio.lms.cte.model.v104.LacContQt>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vLacContQt
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacContQt(
            final com.mercurio.lms.cte.model.v104.LacContQt vLacContQt)
    throws java.lang.IndexOutOfBoundsException {
        this._lacContQtList.add(vLacContQt);
    }

    /**
     * 
     * 
     * @param index
     * @param vLacContQt
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacContQt(
            final int index,
            final com.mercurio.lms.cte.model.v104.LacContQt vLacContQt)
    throws java.lang.IndexOutOfBoundsException {
        this._lacContQtList.add(index, vLacContQt);
    }

    /**
     * Method enumerateLacContQt.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v104.LacContQt> enumerateLacContQt(
    ) {
        return java.util.Collections.enumeration(this._lacContQtList);
    }

    /**
     * Returns the value of field 'dPrev'. The field 'dPrev' has
     * the following description: Data prevista de entrega
     * 
     * @return the value of field 'DPrev'.
     */
    public java.lang.String getDPrev(
    ) {
        return this._dPrev;
    }

    /**
     * Method getLacContQt.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.LacContQt at the given index
     */
    public com.mercurio.lms.cte.model.v104.LacContQt getLacContQt(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._lacContQtList.size()) {
            throw new IndexOutOfBoundsException("getLacContQt: Index value '" + index + "' not in range [0.." + (this._lacContQtList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v104.LacContQt) _lacContQtList.get(index);
    }

    /**
     * Method getLacContQt.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v104.LacContQt[] getLacContQt(
    ) {
        com.mercurio.lms.cte.model.v104.LacContQt[] array = new com.mercurio.lms.cte.model.v104.LacContQt[0];
        return (com.mercurio.lms.cte.model.v104.LacContQt[]) this._lacContQtList.toArray(array);
    }

    /**
     * Method getLacContQtCount.
     * 
     * @return the size of this collection
     */
    public int getLacContQtCount(
    ) {
        return this._lacContQtList.size();
    }

    /**
     * Returns the value of field 'nCont'. The field 'nCont' has
     * the following description: Número do Container/ULD
     * 
     * @return the value of field 'NCont'.
     */
    public java.lang.String getNCont(
    ) {
        return this._nCont;
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
     * Method iterateLacContQt.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v104.LacContQt> iterateLacContQt(
    ) {
        return this._lacContQtList.iterator();
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
    public void removeAllLacContQt(
    ) {
        this._lacContQtList.clear();
    }

    /**
     * Method removeLacContQt.
     * 
     * @param vLacContQt
     * @return true if the object was removed from the collection.
     */
    public boolean removeLacContQt(
            final com.mercurio.lms.cte.model.v104.LacContQt vLacContQt) {
        boolean removed = _lacContQtList.remove(vLacContQt);
        return removed;
    }

    /**
     * Method removeLacContQtAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v104.LacContQt removeLacContQtAt(
            final int index) {
        java.lang.Object obj = this._lacContQtList.remove(index);
        return (com.mercurio.lms.cte.model.v104.LacContQt) obj;
    }

    /**
     * Sets the value of field 'dPrev'. The field 'dPrev' has the
     * following description: Data prevista de entrega
     * 
     * @param dPrev the value of field 'dPrev'.
     */
    public void setDPrev(
            final java.lang.String dPrev) {
        this._dPrev = dPrev;
    }

    /**
     * 
     * 
     * @param index
     * @param vLacContQt
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setLacContQt(
            final int index,
            final com.mercurio.lms.cte.model.v104.LacContQt vLacContQt)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._lacContQtList.size()) {
            throw new IndexOutOfBoundsException("setLacContQt: Index value '" + index + "' not in range [0.." + (this._lacContQtList.size() - 1) + "]");
        }

        this._lacContQtList.set(index, vLacContQt);
    }

    /**
     * 
     * 
     * @param vLacContQtArray
     */
    public void setLacContQt(
            final com.mercurio.lms.cte.model.v104.LacContQt[] vLacContQtArray) {
        //-- copy array
        _lacContQtList.clear();

        for (int i = 0; i < vLacContQtArray.length; i++) {
                this._lacContQtList.add(vLacContQtArray[i]);
        }
    }

    /**
     * Sets the value of field 'nCont'. The field 'nCont' has the
     * following description: Número do Container/ULD
     * 
     * @param nCont the value of field 'nCont'.
     */
    public void setNCont(
            final java.lang.String nCont) {
        this._nCont = nCont;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.ContQt
     */
    public static com.mercurio.lms.cte.model.v104.ContQt unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.ContQt) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.ContQt.class, reader);
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
