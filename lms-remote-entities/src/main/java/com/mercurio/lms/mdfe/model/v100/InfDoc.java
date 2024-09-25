/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100;

/**
 * Informações dos Documentos fiscais vinculados ao manifesto
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfDoc implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Informações dos Municípios de descarregamento
     */
    private java.util.List<com.mercurio.lms.mdfe.model.v100.InfMunDescarga> _infMunDescargaList;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfDoc() {
        super();
        this._infMunDescargaList = new java.util.ArrayList<com.mercurio.lms.mdfe.model.v100.InfMunDescarga>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vInfMunDescarga
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfMunDescarga(
            final com.mercurio.lms.mdfe.model.v100.InfMunDescarga vInfMunDescarga)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._infMunDescargaList.size() >= 100) {
            throw new IndexOutOfBoundsException("addInfMunDescarga has a maximum of 100");
        }

        this._infMunDescargaList.add(vInfMunDescarga);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfMunDescarga
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfMunDescarga(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.InfMunDescarga vInfMunDescarga)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._infMunDescargaList.size() >= 100) {
            throw new IndexOutOfBoundsException("addInfMunDescarga has a maximum of 100");
        }

        this._infMunDescargaList.add(index, vInfMunDescarga);
    }

    /**
     * Method enumerateInfMunDescarga.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v100.InfMunDescarga> enumerateInfMunDescarga(
    ) {
        return java.util.Collections.enumeration(this._infMunDescargaList);
    }

    /**
     * Method getInfMunDescarga.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.InfMunDescarga at the
     * given index
     */
    public com.mercurio.lms.mdfe.model.v100.InfMunDescarga getInfMunDescarga(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infMunDescargaList.size()) {
            throw new IndexOutOfBoundsException("getInfMunDescarga: Index value '" + index + "' not in range [0.." + (this._infMunDescargaList.size() - 1) + "]");
        }

        return (com.mercurio.lms.mdfe.model.v100.InfMunDescarga) _infMunDescargaList.get(index);
    }

    /**
     * Method getInfMunDescarga.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v100.InfMunDescarga[] getInfMunDescarga(
    ) {
        com.mercurio.lms.mdfe.model.v100.InfMunDescarga[] array = new com.mercurio.lms.mdfe.model.v100.InfMunDescarga[0];
        return (com.mercurio.lms.mdfe.model.v100.InfMunDescarga[]) this._infMunDescargaList.toArray(array);
    }

    /**
     * Method getInfMunDescargaCount.
     * 
     * @return the size of this collection
     */
    public int getInfMunDescargaCount(
    ) {
        return this._infMunDescargaList.size();
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
     * Method iterateInfMunDescarga.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.mdfe.model.v100.InfMunDescarga> iterateInfMunDescarga(
    ) {
        return this._infMunDescargaList.iterator();
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
    public void removeAllInfMunDescarga(
    ) {
        this._infMunDescargaList.clear();
    }

    /**
     * Method removeInfMunDescarga.
     * 
     * @param vInfMunDescarga
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfMunDescarga(
            final com.mercurio.lms.mdfe.model.v100.InfMunDescarga vInfMunDescarga) {
        boolean removed = _infMunDescargaList.remove(vInfMunDescarga);
        return removed;
    }

    /**
     * Method removeInfMunDescargaAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v100.InfMunDescarga removeInfMunDescargaAt(
            final int index) {
        java.lang.Object obj = this._infMunDescargaList.remove(index);
        return (com.mercurio.lms.mdfe.model.v100.InfMunDescarga) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vInfMunDescarga
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfMunDescarga(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.InfMunDescarga vInfMunDescarga)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infMunDescargaList.size()) {
            throw new IndexOutOfBoundsException("setInfMunDescarga: Index value '" + index + "' not in range [0.." + (this._infMunDescargaList.size() - 1) + "]");
        }

        this._infMunDescargaList.set(index, vInfMunDescarga);
    }

    /**
     * 
     * 
     * @param vInfMunDescargaArray
     */
    public void setInfMunDescarga(
            final com.mercurio.lms.mdfe.model.v100.InfMunDescarga[] vInfMunDescargaArray) {
        //-- copy array
        _infMunDescargaList.clear();

        for (int i = 0; i < vInfMunDescargaArray.length; i++) {
                this._infMunDescargaList.add(vInfMunDescargaArray[i]);
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
     * org.exolab.castor.builder.binding.InfDoc
     */
    public static com.mercurio.lms.mdfe.model.v100.InfDoc unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100.InfDoc) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100.InfDoc.class, reader);
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
