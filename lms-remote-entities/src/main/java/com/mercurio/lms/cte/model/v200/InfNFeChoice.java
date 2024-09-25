/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v200;

/**
 * Class InfNFeChoice.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfNFeChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Informações das Unidades de Transporte
     * (Carreta/Reboque/Vagão)
     */
    private java.util.Vector<com.mercurio.lms.cte.model.v200.InfUnidTransp> _infUnidTranspList;

    /**
     * Informações das Unidades de Carga (Containeres/ULD/Outros)
     */
    private java.util.Vector<com.mercurio.lms.cte.model.v200.InfUnidCarga> _infUnidCargaList;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfNFeChoice() {
        super();
        this._infUnidTranspList = new java.util.Vector<com.mercurio.lms.cte.model.v200.InfUnidTransp>();
        this._infUnidCargaList = new java.util.Vector<com.mercurio.lms.cte.model.v200.InfUnidCarga>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vInfUnidCarga
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfUnidCarga(
            final com.mercurio.lms.cte.model.v200.InfUnidCarga vInfUnidCarga)
    throws java.lang.IndexOutOfBoundsException {
        this._infUnidCargaList.addElement(vInfUnidCarga);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfUnidCarga
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfUnidCarga(
            final int index,
            final com.mercurio.lms.cte.model.v200.InfUnidCarga vInfUnidCarga)
    throws java.lang.IndexOutOfBoundsException {
        this._infUnidCargaList.add(index, vInfUnidCarga);
    }

    /**
     * 
     * 
     * @param vInfUnidTransp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfUnidTransp(
            final com.mercurio.lms.cte.model.v200.InfUnidTransp vInfUnidTransp)
    throws java.lang.IndexOutOfBoundsException {
        this._infUnidTranspList.addElement(vInfUnidTransp);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfUnidTransp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfUnidTransp(
            final int index,
            final com.mercurio.lms.cte.model.v200.InfUnidTransp vInfUnidTransp)
    throws java.lang.IndexOutOfBoundsException {
        this._infUnidTranspList.add(index, vInfUnidTransp);
    }

    /**
     * Method enumerateInfUnidCarga.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v200.InfUnidCarga elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v200.InfUnidCarga> enumerateInfUnidCarga(
    ) {
        return this._infUnidCargaList.elements();
    }

    /**
     * Method enumerateInfUnidTransp.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v200.InfUnidTransp elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v200.InfUnidTransp> enumerateInfUnidTransp(
    ) {
        return this._infUnidTranspList.elements();
    }

    /**
     * Method getInfUnidCarga.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v200.InfUnidCarga at the given
     * index
     */
    public com.mercurio.lms.cte.model.v200.InfUnidCarga getInfUnidCarga(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infUnidCargaList.size()) {
            throw new IndexOutOfBoundsException("getInfUnidCarga: Index value '" + index + "' not in range [0.." + (this._infUnidCargaList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v200.InfUnidCarga) _infUnidCargaList.get(index);
    }

    /**
     * Method getInfUnidCarga.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v200.InfUnidCarga[] getInfUnidCarga(
    ) {
        com.mercurio.lms.cte.model.v200.InfUnidCarga[] array = new com.mercurio.lms.cte.model.v200.InfUnidCarga[0];
        return (com.mercurio.lms.cte.model.v200.InfUnidCarga[]) this._infUnidCargaList.toArray(array);
    }

    /**
     * Method getInfUnidCargaCount.
     * 
     * @return the size of this collection
     */
    public int getInfUnidCargaCount(
    ) {
        return this._infUnidCargaList.size();
    }

    /**
     * Method getInfUnidTransp.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v200.InfUnidTransp at the given
     * index
     */
    public com.mercurio.lms.cte.model.v200.InfUnidTransp getInfUnidTransp(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infUnidTranspList.size()) {
            throw new IndexOutOfBoundsException("getInfUnidTransp: Index value '" + index + "' not in range [0.." + (this._infUnidTranspList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v200.InfUnidTransp) _infUnidTranspList.get(index);
    }

    /**
     * Method getInfUnidTransp.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v200.InfUnidTransp[] getInfUnidTransp(
    ) {
        com.mercurio.lms.cte.model.v200.InfUnidTransp[] array = new com.mercurio.lms.cte.model.v200.InfUnidTransp[0];
        return (com.mercurio.lms.cte.model.v200.InfUnidTransp[]) this._infUnidTranspList.toArray(array);
    }

    /**
     * Method getInfUnidTranspCount.
     * 
     * @return the size of this collection
     */
    public int getInfUnidTranspCount(
    ) {
        return this._infUnidTranspList.size();
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
    public void removeAllInfUnidCarga(
    ) {
        this._infUnidCargaList.clear();
    }

    /**
     */
    public void removeAllInfUnidTransp(
    ) {
        this._infUnidTranspList.clear();
    }

    /**
     * Method removeInfUnidCarga.
     * 
     * @param vInfUnidCarga
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfUnidCarga(
            final com.mercurio.lms.cte.model.v200.InfUnidCarga vInfUnidCarga) {
        boolean removed = _infUnidCargaList.remove(vInfUnidCarga);
        return removed;
    }

    /**
     * Method removeInfUnidCargaAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v200.InfUnidCarga removeInfUnidCargaAt(
            final int index) {
        java.lang.Object obj = this._infUnidCargaList.remove(index);
        return (com.mercurio.lms.cte.model.v200.InfUnidCarga) obj;
    }

    /**
     * Method removeInfUnidTransp.
     * 
     * @param vInfUnidTransp
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfUnidTransp(
            final com.mercurio.lms.cte.model.v200.InfUnidTransp vInfUnidTransp) {
        boolean removed = _infUnidTranspList.remove(vInfUnidTransp);
        return removed;
    }

    /**
     * Method removeInfUnidTranspAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v200.InfUnidTransp removeInfUnidTranspAt(
            final int index) {
        java.lang.Object obj = this._infUnidTranspList.remove(index);
        return (com.mercurio.lms.cte.model.v200.InfUnidTransp) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vInfUnidCarga
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfUnidCarga(
            final int index,
            final com.mercurio.lms.cte.model.v200.InfUnidCarga vInfUnidCarga)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infUnidCargaList.size()) {
            throw new IndexOutOfBoundsException("setInfUnidCarga: Index value '" + index + "' not in range [0.." + (this._infUnidCargaList.size() - 1) + "]");
        }

        this._infUnidCargaList.set(index, vInfUnidCarga);
    }

    /**
     * 
     * 
     * @param vInfUnidCargaArray
     */
    public void setInfUnidCarga(
            final com.mercurio.lms.cte.model.v200.InfUnidCarga[] vInfUnidCargaArray) {
        //-- copy array
        _infUnidCargaList.clear();

        for (int i = 0; i < vInfUnidCargaArray.length; i++) {
                this._infUnidCargaList.add(vInfUnidCargaArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vInfUnidTransp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfUnidTransp(
            final int index,
            final com.mercurio.lms.cte.model.v200.InfUnidTransp vInfUnidTransp)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infUnidTranspList.size()) {
            throw new IndexOutOfBoundsException("setInfUnidTransp: Index value '" + index + "' not in range [0.." + (this._infUnidTranspList.size() - 1) + "]");
        }

        this._infUnidTranspList.set(index, vInfUnidTransp);
    }

    /**
     * 
     * 
     * @param vInfUnidTranspArray
     */
    public void setInfUnidTransp(
            final com.mercurio.lms.cte.model.v200.InfUnidTransp[] vInfUnidTranspArray) {
        //-- copy array
        _infUnidTranspList.clear();

        for (int i = 0; i < vInfUnidTranspArray.length; i++) {
                this._infUnidTranspList.add(vInfUnidTranspArray[i]);
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
     * com.mercurio.lms.cte.model.v200.InfNFeChoice
     */
    public static com.mercurio.lms.cte.model.v200.InfNFeChoice unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v200.InfNFeChoice) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v200.InfNFeChoice.class, reader);
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
