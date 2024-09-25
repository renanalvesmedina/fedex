/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 * Informações dos Documentos fiscais vinculados ao manifesto
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfDoc implements java.io.Serializable {

    /**
     * Informações dos Municípios de descarregamento
     */
    private java.util.Vector<com.mercurio.lms.mdfe.model.v300.InfMunDescarga> infMunDescargaList;

    public InfDoc() {
        super();
        this.infMunDescargaList = new java.util.Vector<com.mercurio.lms.mdfe.model.v300.InfMunDescarga>();
    }

    /**
     * 
     * 
     * @param vInfMunDescarga
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfMunDescarga(final com.mercurio.lms.mdfe.model.v300.InfMunDescarga vInfMunDescarga) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.infMunDescargaList.size() >= 100) {
            throw new IndexOutOfBoundsException("addInfMunDescarga has a maximum of 100");
        }

        this.infMunDescargaList.addElement(vInfMunDescarga);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfMunDescarga
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfMunDescarga(final int index,final com.mercurio.lms.mdfe.model.v300.InfMunDescarga vInfMunDescarga) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.infMunDescargaList.size() >= 100) {
            throw new IndexOutOfBoundsException("addInfMunDescarga has a maximum of 100");
        }

        this.infMunDescargaList.add(index, vInfMunDescarga);
    }

    /**
     * Method enumerateInfMunDescarga.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.mdfe.model.v300.InfMunDescarga elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v300.InfMunDescarga> enumerateInfMunDescarga() {
        return this.infMunDescargaList.elements();
    }

    /**
     * Method getInfMunDescarga.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.mdfe.model.v300.InfMunDescarga at the
     * given index
     */
    public com.mercurio.lms.mdfe.model.v300.InfMunDescarga getInfMunDescarga(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.infMunDescargaList.size()) {
            throw new IndexOutOfBoundsException("getInfMunDescarga: Index value '" + index + "' not in range [0.." + (this.infMunDescargaList.size() - 1) + "]");
        }

        return infMunDescargaList.get(index);
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
    public com.mercurio.lms.mdfe.model.v300.InfMunDescarga[] getInfMunDescarga() {
        com.mercurio.lms.mdfe.model.v300.InfMunDescarga[] array = new com.mercurio.lms.mdfe.model.v300.InfMunDescarga[0];
        return this.infMunDescargaList.toArray(array);
    }

    /**
     * Method getInfMunDescargaCount.
     * 
     * @return the size of this collection
     */
    public int getInfMunDescargaCount() {
        return this.infMunDescargaList.size();
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid() {
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
    public void marshal(final java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
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
    public void marshal(final org.xml.sax.ContentHandler handler) throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }

    /**
     */
    public void removeAllInfMunDescarga() {
        this.infMunDescargaList.clear();
    }

    /**
     * Method removeInfMunDescarga.
     * 
     * @param vInfMunDescarga
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfMunDescarga(final com.mercurio.lms.mdfe.model.v300.InfMunDescarga vInfMunDescarga) {
        boolean removed = infMunDescargaList.remove(vInfMunDescarga);
        return removed;
    }

    /**
     * Method removeInfMunDescargaAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v300.InfMunDescarga removeInfMunDescargaAt(final int index) {
        java.lang.Object obj = this.infMunDescargaList.remove(index);
        return (com.mercurio.lms.mdfe.model.v300.InfMunDescarga) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vInfMunDescarga
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfMunDescarga(final int index,final com.mercurio.lms.mdfe.model.v300.InfMunDescarga vInfMunDescarga) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.infMunDescargaList.size()) {
            throw new IndexOutOfBoundsException("setInfMunDescarga: Index value '" + index + "' not in range [0.." + (this.infMunDescargaList.size() - 1) + "]");
        }

        this.infMunDescargaList.set(index, vInfMunDescarga);
    }

    /**
     * 
     * 
     * @param vInfMunDescargaArray
     */
    public void setInfMunDescarga(final com.mercurio.lms.mdfe.model.v300.InfMunDescarga[] vInfMunDescargaArray) {
        //-- copy array
        infMunDescargaList.clear();

        for (int i = 0; i < vInfMunDescargaArray.length; i++) {
                this.infMunDescargaList.add(vInfMunDescargaArray[i]);
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
     * com.mercurio.lms.mdfe.model.v300.InfDoc
     */
    public static com.mercurio.lms.mdfe.model.v300.InfDoc unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.InfDoc) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.InfDoc.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
