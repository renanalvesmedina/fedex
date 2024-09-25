/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100a;

/**
 * Informações dos Municípios de descarregamento
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfMunDescarga implements java.io.Serializable {

    /**
     * Código do Município de Descarregamento
     */
    private java.lang.String cMunDescarga;

    /**
     * Nome do Município de Descarregamento
     */
    private java.lang.String xMunDescarga;

    /**
     * Conhecimentos de Tranporte - usar este grupo quando for
     * prestador de serviço de transporte
     */
    private java.util.Vector<com.mercurio.lms.mdfe.model.v100a.InfCTe> infCTeList;

    /**
     * Nota Fiscal Eletronica
     */
    private java.util.Vector<com.mercurio.lms.mdfe.model.v100a.InfNFe> infNFeList;

    /**
     * Manifesto Eletrônico de Documentos Fiscais. Somente para
     * modal Aquaviário (vide regras MOC)
     */
    private java.util.Vector<com.mercurio.lms.mdfe.model.v100a.InfMDFeTransp> infMDFeTranspList;

    public InfMunDescarga() {
        super();
        this.infCTeList = new java.util.Vector<com.mercurio.lms.mdfe.model.v100a.InfCTe>();
        this.infNFeList = new java.util.Vector<com.mercurio.lms.mdfe.model.v100a.InfNFe>();
        this.infMDFeTranspList = new java.util.Vector<com.mercurio.lms.mdfe.model.v100a.InfMDFeTransp>();
    }

    /**
     * 
     * 
     * @param vInfCTe
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfCTe(final com.mercurio.lms.mdfe.model.v100a.InfCTe vInfCTe) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.infCTeList.size() >= 4000) {
            throw new IndexOutOfBoundsException("addInfCTe has a maximum of 4000");
        }

        this.infCTeList.addElement(vInfCTe);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfCTe
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfCTe(final int index,final com.mercurio.lms.mdfe.model.v100a.InfCTe vInfCTe) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.infCTeList.size() >= 4000) {
            throw new IndexOutOfBoundsException("addInfCTe has a maximum of 4000");
        }

        this.infCTeList.add(index, vInfCTe);
    }

    /**
     * 
     * 
     * @param vInfMDFeTransp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfMDFeTransp(final com.mercurio.lms.mdfe.model.v100a.InfMDFeTransp vInfMDFeTransp) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.infMDFeTranspList.size() >= 4000) {
            throw new IndexOutOfBoundsException("addInfMDFeTransp has a maximum of 4000");
        }

        this.infMDFeTranspList.addElement(vInfMDFeTransp);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfMDFeTransp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfMDFeTransp(final int index,final com.mercurio.lms.mdfe.model.v100a.InfMDFeTransp vInfMDFeTransp) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.infMDFeTranspList.size() >= 4000) {
            throw new IndexOutOfBoundsException("addInfMDFeTransp has a maximum of 4000");
        }

        this.infMDFeTranspList.add(index, vInfMDFeTransp);
    }

    /**
     * 
     * 
     * @param vInfNFe
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfNFe(final com.mercurio.lms.mdfe.model.v100a.InfNFe vInfNFe) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.infNFeList.size() >= 4000) {
            throw new IndexOutOfBoundsException("addInfNFe has a maximum of 4000");
        }

        this.infNFeList.addElement(vInfNFe);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfNFe
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfNFe(final int index,final com.mercurio.lms.mdfe.model.v100a.InfNFe vInfNFe) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.infNFeList.size() >= 4000) {
            throw new IndexOutOfBoundsException("addInfNFe has a maximum of 4000");
        }

        this.infNFeList.add(index, vInfNFe);
    }

    /**
     * Method enumerateInfCTe.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.mdfe.model.v100a.InfCTe elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v100a.InfCTe> enumerateInfCTe() {
        return this.infCTeList.elements();
    }

    /**
     * Method enumerateInfMDFeTransp.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.mdfe.model.v100a.InfMDFeTransp elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v100a.InfMDFeTransp> enumerateInfMDFeTransp() {
        return this.infMDFeTranspList.elements();
    }

    /**
     * Method enumerateInfNFe.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.mdfe.model.v100a.InfNFe elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v100a.InfNFe> enumerateInfNFe() {
        return this.infNFeList.elements();
    }

    /**
     * Returns the value of field 'cMunDescarga'. The field
     * 'cMunDescarga' has the following description: Código do
     * Município de Descarregamento
     * 
     * @return the value of field 'CMunDescarga'.
     */
    public java.lang.String getCMunDescarga() {
        return this.cMunDescarga;
    }

    /**
     * Method getInfCTe.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.mdfe.model.v100a.InfCTe at the given index
     */
    public com.mercurio.lms.mdfe.model.v100a.InfCTe getInfCTe(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.infCTeList.size()) {
            throw new IndexOutOfBoundsException("getInfCTe: Index value '" + index + "' not in range [0.." + (this.infCTeList.size() - 1) + "]");
        }

        return infCTeList.get(index);
    }

    /**
     * Method getInfCTe.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v100a.InfCTe[] getInfCTe() {
        com.mercurio.lms.mdfe.model.v100a.InfCTe[] array = new com.mercurio.lms.mdfe.model.v100a.InfCTe[0];
        return this.infCTeList.toArray(array);
    }

    /**
     * Method getInfCTeCount.
     * 
     * @return the size of this collection
     */
    public int getInfCTeCount() {
        return this.infCTeList.size();
    }

    /**
     * Method getInfMDFeTransp.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.mdfe.model.v100a.InfMDFeTransp at the given
     * index
     */
    public com.mercurio.lms.mdfe.model.v100a.InfMDFeTransp getInfMDFeTransp(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.infMDFeTranspList.size()) {
            throw new IndexOutOfBoundsException("getInfMDFeTransp: Index value '" + index + "' not in range [0.." + (this.infMDFeTranspList.size() - 1) + "]");
        }

        return infMDFeTranspList.get(index);
    }

    /**
     * Method getInfMDFeTransp.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v100a.InfMDFeTransp[] getInfMDFeTransp() {
        com.mercurio.lms.mdfe.model.v100a.InfMDFeTransp[] array = new com.mercurio.lms.mdfe.model.v100a.InfMDFeTransp[0];
        return this.infMDFeTranspList.toArray(array);
    }

    /**
     * Method getInfMDFeTranspCount.
     * 
     * @return the size of this collection
     */
    public int getInfMDFeTranspCount() {
        return this.infMDFeTranspList.size();
    }

    /**
     * Method getInfNFe.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.mdfe.model.v100a.InfNFe at the given index
     */
    public com.mercurio.lms.mdfe.model.v100a.InfNFe getInfNFe(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.infNFeList.size()) {
            throw new IndexOutOfBoundsException("getInfNFe: Index value '" + index + "' not in range [0.." + (this.infNFeList.size() - 1) + "]");
        }

        return infNFeList.get(index);
    }

    /**
     * Method getInfNFe.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v100a.InfNFe[] getInfNFe() {
        com.mercurio.lms.mdfe.model.v100a.InfNFe[] array = new com.mercurio.lms.mdfe.model.v100a.InfNFe[0];
        return this.infNFeList.toArray(array);
    }

    /**
     * Method getInfNFeCount.
     * 
     * @return the size of this collection
     */
    public int getInfNFeCount() {
        return this.infNFeList.size();
    }

    /**
     * Returns the value of field 'xMunDescarga'. The field
     * 'xMunDescarga' has the following description: Nome do
     * Município de Descarregamento
     * 
     * @return the value of field 'XMunDescarga'.
     */
    public java.lang.String getXMunDescarga() {
        return this.xMunDescarga;
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
    public void removeAllInfCTe() {
        this.infCTeList.clear();
    }

    /**
     */
    public void removeAllInfMDFeTransp() {
        this.infMDFeTranspList.clear();
    }

    /**
     */
    public void removeAllInfNFe() {
        this.infNFeList.clear();
    }

    /**
     * Method removeInfCTe.
     * 
     * @param vInfCTe
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfCTe(final com.mercurio.lms.mdfe.model.v100a.InfCTe vInfCTe) {
        boolean removed = infCTeList.remove(vInfCTe);
        return removed;
    }

    /**
     * Method removeInfCTeAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v100a.InfCTe removeInfCTeAt(final int index) {
        java.lang.Object obj = this.infCTeList.remove(index);
        return (com.mercurio.lms.mdfe.model.v100a.InfCTe) obj;
    }

    /**
     * Method removeInfMDFeTransp.
     * 
     * @param vInfMDFeTransp
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfMDFeTransp(final com.mercurio.lms.mdfe.model.v100a.InfMDFeTransp vInfMDFeTransp) {
        boolean removed = infMDFeTranspList.remove(vInfMDFeTransp);
        return removed;
    }

    /**
     * Method removeInfMDFeTranspAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v100a.InfMDFeTransp removeInfMDFeTranspAt(final int index) {
        java.lang.Object obj = this.infMDFeTranspList.remove(index);
        return (com.mercurio.lms.mdfe.model.v100a.InfMDFeTransp) obj;
    }

    /**
     * Method removeInfNFe.
     * 
     * @param vInfNFe
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfNFe(final com.mercurio.lms.mdfe.model.v100a.InfNFe vInfNFe) {
        boolean removed = infNFeList.remove(vInfNFe);
        return removed;
    }

    /**
     * Method removeInfNFeAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v100a.InfNFe removeInfNFeAt(final int index) {
        java.lang.Object obj = this.infNFeList.remove(index);
        return (com.mercurio.lms.mdfe.model.v100a.InfNFe) obj;
    }

    /**
     * Sets the value of field 'cMunDescarga'. The field
     * 'cMunDescarga' has the following description: Código do
     * Município de Descarregamento
     * 
     * @param cMunDescarga the value of field 'cMunDescarga'.
     */
    public void setCMunDescarga(final java.lang.String cMunDescarga) {
        this.cMunDescarga = cMunDescarga;
    }

    /**
     * 
     * 
     * @param index
     * @param vInfCTe
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfCTe(final int index,final com.mercurio.lms.mdfe.model.v100a.InfCTe vInfCTe) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.infCTeList.size()) {
            throw new IndexOutOfBoundsException("setInfCTe: Index value '" + index + "' not in range [0.." + (this.infCTeList.size() - 1) + "]");
        }

        this.infCTeList.set(index, vInfCTe);
    }

    /**
     * 
     * 
     * @param vInfCTeArray
     */
    public void setInfCTe(final com.mercurio.lms.mdfe.model.v100a.InfCTe[] vInfCTeArray) {
        //-- copy array
        infCTeList.clear();

        for (int i = 0; i < vInfCTeArray.length; i++) {
                this.infCTeList.add(vInfCTeArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vInfMDFeTransp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfMDFeTransp(final int index,final com.mercurio.lms.mdfe.model.v100a.InfMDFeTransp vInfMDFeTransp) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.infMDFeTranspList.size()) {
            throw new IndexOutOfBoundsException("setInfMDFeTransp: Index value '" + index + "' not in range [0.." + (this.infMDFeTranspList.size() - 1) + "]");
        }

        this.infMDFeTranspList.set(index, vInfMDFeTransp);
    }

    /**
     * 
     * 
     * @param vInfMDFeTranspArray
     */
    public void setInfMDFeTransp(final com.mercurio.lms.mdfe.model.v100a.InfMDFeTransp[] vInfMDFeTranspArray) {
        //-- copy array
        infMDFeTranspList.clear();

        for (int i = 0; i < vInfMDFeTranspArray.length; i++) {
                this.infMDFeTranspList.add(vInfMDFeTranspArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vInfNFe
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfNFe(final int index,final com.mercurio.lms.mdfe.model.v100a.InfNFe vInfNFe) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.infNFeList.size()) {
            throw new IndexOutOfBoundsException("setInfNFe: Index value '" + index + "' not in range [0.." + (this.infNFeList.size() - 1) + "]");
        }

        this.infNFeList.set(index, vInfNFe);
    }

    /**
     * 
     * 
     * @param vInfNFeArray
     */
    public void setInfNFe(final com.mercurio.lms.mdfe.model.v100a.InfNFe[] vInfNFeArray) {
        //-- copy array
        infNFeList.clear();

        for (int i = 0; i < vInfNFeArray.length; i++) {
                this.infNFeList.add(vInfNFeArray[i]);
        }
    }

    /**
     * Sets the value of field 'xMunDescarga'. The field
     * 'xMunDescarga' has the following description: Nome do
     * Município de Descarregamento
     * 
     * @param xMunDescarga the value of field 'xMunDescarga'.
     */
    public void setXMunDescarga(final java.lang.String xMunDescarga) {
        this.xMunDescarga = xMunDescarga;
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
     * com.mercurio.lms.mdfe.model.v100a.InfMunDescarga
     */
    public static com.mercurio.lms.mdfe.model.v100a.InfMunDescarga unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100a.InfMunDescarga) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100a.InfMunDescarga.class, reader);
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
