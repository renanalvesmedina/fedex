/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100;

/**
 * Informações dos Municípios de descarregamento
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfMunDescarga implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Código do Município de Descarregamento
     */
    private java.lang.String _cMunDescarga;

    /**
     * Nome do Município de Descarregamento
     */
    private java.lang.Object _xMunDescarga;

    /**
     * Conhecimentos de Tranporte - usar este grupo quando for
     * prestador de serviço de transporte
     */
    private java.util.List<com.mercurio.lms.mdfe.model.v100.InfCTe> _infCTeList;

    /**
     * Conhecimentos Tranporte (papel)
     */
    private java.util.List<com.mercurio.lms.mdfe.model.v100.InfCT> _infCTList;

    /**
     * Nota Fiscal Eletronica
     */
    private java.util.List<com.mercurio.lms.mdfe.model.v100.InfNFe> _infNFeList;

    /**
     * Nota Fiscal Papel (mod 1 e 1A)
     */
    private java.util.List<com.mercurio.lms.mdfe.model.v100.InfNF> _infNFList;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfMunDescarga() {
        super();
        this._infCTeList = new java.util.ArrayList<com.mercurio.lms.mdfe.model.v100.InfCTe>();
        this._infCTList = new java.util.ArrayList<com.mercurio.lms.mdfe.model.v100.InfCT>();
        this._infNFeList = new java.util.ArrayList<com.mercurio.lms.mdfe.model.v100.InfNFe>();
        this._infNFList = new java.util.ArrayList<com.mercurio.lms.mdfe.model.v100.InfNF>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vInfCT
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfCT(
            final com.mercurio.lms.mdfe.model.v100.InfCT vInfCT)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._infCTList.size() >= 2000) {
            throw new IndexOutOfBoundsException("addInfCT has a maximum of 2000");
        }

        this._infCTList.add(vInfCT);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfCT
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfCT(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.InfCT vInfCT)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._infCTList.size() >= 2000) {
            throw new IndexOutOfBoundsException("addInfCT has a maximum of 2000");
        }

        this._infCTList.add(index, vInfCT);
    }

    /**
     * 
     * 
     * @param vInfCTe
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfCTe(
            final com.mercurio.lms.mdfe.model.v100.InfCTe vInfCTe)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._infCTeList.size() >= 2000) {
            throw new IndexOutOfBoundsException("addInfCTe has a maximum of 2000");
        }

        this._infCTeList.add(vInfCTe);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfCTe
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfCTe(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.InfCTe vInfCTe)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._infCTeList.size() >= 2000) {
            throw new IndexOutOfBoundsException("addInfCTe has a maximum of 2000");
        }

        this._infCTeList.add(index, vInfCTe);
    }

    /**
     * 
     * 
     * @param vInfNF
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfNF(
            final com.mercurio.lms.mdfe.model.v100.InfNF vInfNF)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._infNFList.size() >= 2000) {
            throw new IndexOutOfBoundsException("addInfNF has a maximum of 2000");
        }

        this._infNFList.add(vInfNF);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfNF
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfNF(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.InfNF vInfNF)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._infNFList.size() >= 2000) {
            throw new IndexOutOfBoundsException("addInfNF has a maximum of 2000");
        }

        this._infNFList.add(index, vInfNF);
    }

    /**
     * 
     * 
     * @param vInfNFe
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfNFe(
            final com.mercurio.lms.mdfe.model.v100.InfNFe vInfNFe)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._infNFeList.size() >= 2000) {
            throw new IndexOutOfBoundsException("addInfNFe has a maximum of 2000");
        }

        this._infNFeList.add(vInfNFe);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfNFe
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfNFe(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.InfNFe vInfNFe)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._infNFeList.size() >= 2000) {
            throw new IndexOutOfBoundsException("addInfNFe has a maximum of 2000");
        }

        this._infNFeList.add(index, vInfNFe);
    }

    /**
     * Method enumerateInfCT.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v100.InfCT> enumerateInfCT(
    ) {
        return java.util.Collections.enumeration(this._infCTList);
    }

    /**
     * Method enumerateInfCTe.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v100.InfCTe> enumerateInfCTe(
    ) {
        return java.util.Collections.enumeration(this._infCTeList);
    }

    /**
     * Method enumerateInfNF.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v100.InfNF> enumerateInfNF(
    ) {
        return java.util.Collections.enumeration(this._infNFList);
    }

    /**
     * Method enumerateInfNFe.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v100.InfNFe> enumerateInfNFe(
    ) {
        return java.util.Collections.enumeration(this._infNFeList);
    }

    /**
     * Returns the value of field 'cMunDescarga'. The field
     * 'cMunDescarga' has the following description: Código do
     * Município de Descarregamento
     * 
     * @return the value of field 'CMunDescarga'.
     */
    public java.lang.String getCMunDescarga(
    ) {
        return this._cMunDescarga;
    }

    /**
     * Method getInfCT.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.InfCT at the given index
     */
    public com.mercurio.lms.mdfe.model.v100.InfCT getInfCT(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infCTList.size()) {
            throw new IndexOutOfBoundsException("getInfCT: Index value '" + index + "' not in range [0.." + (this._infCTList.size() - 1) + "]");
        }

        return (com.mercurio.lms.mdfe.model.v100.InfCT) _infCTList.get(index);
    }

    /**
     * Method getInfCT.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v100.InfCT[] getInfCT(
    ) {
        com.mercurio.lms.mdfe.model.v100.InfCT[] array = new com.mercurio.lms.mdfe.model.v100.InfCT[0];
        return (com.mercurio.lms.mdfe.model.v100.InfCT[]) this._infCTList.toArray(array);
    }

    /**
     * Method getInfCTCount.
     * 
     * @return the size of this collection
     */
    public int getInfCTCount(
    ) {
        return this._infCTList.size();
    }

    /**
     * Method getInfCTe.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.InfCTe at the given index
     */
    public com.mercurio.lms.mdfe.model.v100.InfCTe getInfCTe(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infCTeList.size()) {
            throw new IndexOutOfBoundsException("getInfCTe: Index value '" + index + "' not in range [0.." + (this._infCTeList.size() - 1) + "]");
        }

        return (com.mercurio.lms.mdfe.model.v100.InfCTe) _infCTeList.get(index);
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
    public com.mercurio.lms.mdfe.model.v100.InfCTe[] getInfCTe(
    ) {
        com.mercurio.lms.mdfe.model.v100.InfCTe[] array = new com.mercurio.lms.mdfe.model.v100.InfCTe[0];
        return (com.mercurio.lms.mdfe.model.v100.InfCTe[]) this._infCTeList.toArray(array);
    }

    /**
     * Method getInfCTeCount.
     * 
     * @return the size of this collection
     */
    public int getInfCTeCount(
    ) {
        return this._infCTeList.size();
    }

    /**
     * Method getInfNF.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.InfNF at the given index
     */
    public com.mercurio.lms.mdfe.model.v100.InfNF getInfNF(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infNFList.size()) {
            throw new IndexOutOfBoundsException("getInfNF: Index value '" + index + "' not in range [0.." + (this._infNFList.size() - 1) + "]");
        }

        return (com.mercurio.lms.mdfe.model.v100.InfNF) _infNFList.get(index);
    }

    /**
     * Method getInfNF.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v100.InfNF[] getInfNF(
    ) {
        com.mercurio.lms.mdfe.model.v100.InfNF[] array = new com.mercurio.lms.mdfe.model.v100.InfNF[0];
        return (com.mercurio.lms.mdfe.model.v100.InfNF[]) this._infNFList.toArray(array);
    }

    /**
     * Method getInfNFCount.
     * 
     * @return the size of this collection
     */
    public int getInfNFCount(
    ) {
        return this._infNFList.size();
    }

    /**
     * Method getInfNFe.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.InfNFe at the given index
     */
    public com.mercurio.lms.mdfe.model.v100.InfNFe getInfNFe(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infNFeList.size()) {
            throw new IndexOutOfBoundsException("getInfNFe: Index value '" + index + "' not in range [0.." + (this._infNFeList.size() - 1) + "]");
        }

        return (com.mercurio.lms.mdfe.model.v100.InfNFe) _infNFeList.get(index);
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
    public com.mercurio.lms.mdfe.model.v100.InfNFe[] getInfNFe(
    ) {
        com.mercurio.lms.mdfe.model.v100.InfNFe[] array = new com.mercurio.lms.mdfe.model.v100.InfNFe[0];
        return (com.mercurio.lms.mdfe.model.v100.InfNFe[]) this._infNFeList.toArray(array);
    }

    /**
     * Method getInfNFeCount.
     * 
     * @return the size of this collection
     */
    public int getInfNFeCount(
    ) {
        return this._infNFeList.size();
    }

    /**
     * Returns the value of field 'xMunDescarga'. The field
     * 'xMunDescarga' has the following description: Nome do
     * Município de Descarregamento
     * 
     * @return the value of field 'XMunDescarga'.
     */
    public java.lang.Object getXMunDescarga(
    ) {
        return this._xMunDescarga;
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
     * Method iterateInfCT.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.mdfe.model.v100.InfCT> iterateInfCT(
    ) {
        return this._infCTList.iterator();
    }

    /**
     * Method iterateInfCTe.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.mdfe.model.v100.InfCTe> iterateInfCTe(
    ) {
        return this._infCTeList.iterator();
    }

    /**
     * Method iterateInfNF.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.mdfe.model.v100.InfNF> iterateInfNF(
    ) {
        return this._infNFList.iterator();
    }

    /**
     * Method iterateInfNFe.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.mdfe.model.v100.InfNFe> iterateInfNFe(
    ) {
        return this._infNFeList.iterator();
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
    public void removeAllInfCT(
    ) {
        this._infCTList.clear();
    }

    /**
     */
    public void removeAllInfCTe(
    ) {
        this._infCTeList.clear();
    }

    /**
     */
    public void removeAllInfNF(
    ) {
        this._infNFList.clear();
    }

    /**
     */
    public void removeAllInfNFe(
    ) {
        this._infNFeList.clear();
    }

    /**
     * Method removeInfCT.
     * 
     * @param vInfCT
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfCT(
            final com.mercurio.lms.mdfe.model.v100.InfCT vInfCT) {
        boolean removed = _infCTList.remove(vInfCT);
        return removed;
    }

    /**
     * Method removeInfCTAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v100.InfCT removeInfCTAt(
            final int index) {
        java.lang.Object obj = this._infCTList.remove(index);
        return (com.mercurio.lms.mdfe.model.v100.InfCT) obj;
    }

    /**
     * Method removeInfCTe.
     * 
     * @param vInfCTe
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfCTe(
            final com.mercurio.lms.mdfe.model.v100.InfCTe vInfCTe) {
        boolean removed = _infCTeList.remove(vInfCTe);
        return removed;
    }

    /**
     * Method removeInfCTeAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v100.InfCTe removeInfCTeAt(
            final int index) {
        java.lang.Object obj = this._infCTeList.remove(index);
        return (com.mercurio.lms.mdfe.model.v100.InfCTe) obj;
    }

    /**
     * Method removeInfNF.
     * 
     * @param vInfNF
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfNF(
            final com.mercurio.lms.mdfe.model.v100.InfNF vInfNF) {
        boolean removed = _infNFList.remove(vInfNF);
        return removed;
    }

    /**
     * Method removeInfNFAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v100.InfNF removeInfNFAt(
            final int index) {
        java.lang.Object obj = this._infNFList.remove(index);
        return (com.mercurio.lms.mdfe.model.v100.InfNF) obj;
    }

    /**
     * Method removeInfNFe.
     * 
     * @param vInfNFe
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfNFe(
            final com.mercurio.lms.mdfe.model.v100.InfNFe vInfNFe) {
        boolean removed = _infNFeList.remove(vInfNFe);
        return removed;
    }

    /**
     * Method removeInfNFeAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v100.InfNFe removeInfNFeAt(
            final int index) {
        java.lang.Object obj = this._infNFeList.remove(index);
        return (com.mercurio.lms.mdfe.model.v100.InfNFe) obj;
    }

    /**
     * Sets the value of field 'cMunDescarga'. The field
     * 'cMunDescarga' has the following description: Código do
     * Município de Descarregamento
     * 
     * @param cMunDescarga the value of field 'cMunDescarga'.
     */
    public void setCMunDescarga(
            final java.lang.String cMunDescarga) {
        this._cMunDescarga = cMunDescarga;
    }

    /**
     * 
     * 
     * @param index
     * @param vInfCT
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfCT(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.InfCT vInfCT)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infCTList.size()) {
            throw new IndexOutOfBoundsException("setInfCT: Index value '" + index + "' not in range [0.." + (this._infCTList.size() - 1) + "]");
        }

        this._infCTList.set(index, vInfCT);
    }

    /**
     * 
     * 
     * @param vInfCTArray
     */
    public void setInfCT(
            final com.mercurio.lms.mdfe.model.v100.InfCT[] vInfCTArray) {
        //-- copy array
        _infCTList.clear();

        for (int i = 0; i < vInfCTArray.length; i++) {
                this._infCTList.add(vInfCTArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vInfCTe
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfCTe(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.InfCTe vInfCTe)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infCTeList.size()) {
            throw new IndexOutOfBoundsException("setInfCTe: Index value '" + index + "' not in range [0.." + (this._infCTeList.size() - 1) + "]");
        }

        this._infCTeList.set(index, vInfCTe);
    }

    /**
     * 
     * 
     * @param vInfCTeArray
     */
    public void setInfCTe(
            final com.mercurio.lms.mdfe.model.v100.InfCTe[] vInfCTeArray) {
        //-- copy array
        _infCTeList.clear();

        for (int i = 0; i < vInfCTeArray.length; i++) {
                this._infCTeList.add(vInfCTeArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vInfNF
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfNF(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.InfNF vInfNF)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infNFList.size()) {
            throw new IndexOutOfBoundsException("setInfNF: Index value '" + index + "' not in range [0.." + (this._infNFList.size() - 1) + "]");
        }

        this._infNFList.set(index, vInfNF);
    }

    /**
     * 
     * 
     * @param vInfNFArray
     */
    public void setInfNF(
            final com.mercurio.lms.mdfe.model.v100.InfNF[] vInfNFArray) {
        //-- copy array
        _infNFList.clear();

        for (int i = 0; i < vInfNFArray.length; i++) {
                this._infNFList.add(vInfNFArray[i]);
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
    public void setInfNFe(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.InfNFe vInfNFe)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infNFeList.size()) {
            throw new IndexOutOfBoundsException("setInfNFe: Index value '" + index + "' not in range [0.." + (this._infNFeList.size() - 1) + "]");
        }

        this._infNFeList.set(index, vInfNFe);
    }

    /**
     * 
     * 
     * @param vInfNFeArray
     */
    public void setInfNFe(
            final com.mercurio.lms.mdfe.model.v100.InfNFe[] vInfNFeArray) {
        //-- copy array
        _infNFeList.clear();

        for (int i = 0; i < vInfNFeArray.length; i++) {
                this._infNFeList.add(vInfNFeArray[i]);
        }
    }

    /**
     * Sets the value of field 'xMunDescarga'. The field
     * 'xMunDescarga' has the following description: Nome do
     * Município de Descarregamento
     * 
     * @param xMunDescarga the value of field 'xMunDescarga'.
     */
    public void setXMunDescarga(
            final java.lang.Object xMunDescarga) {
        this._xMunDescarga = xMunDescarga;
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
     * org.exolab.castor.builder.binding.InfMunDescarga
     */
    public static com.mercurio.lms.mdfe.model.v100.InfMunDescarga unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100.InfMunDescarga) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100.InfMunDescarga.class, reader);
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
