/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Class RemChoice2.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class RemChoice2 implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Informações das NF
     */
    private java.util.List<com.mercurio.lms.cte.model.v104.InfNF> _infNFList;

    /**
     * Informações das NF-e
     */
    private java.util.List<com.mercurio.lms.cte.model.v104.InfNFe> _infNFeList;

    /**
     * Informações dos demais documentos
     */
    private java.util.List<com.mercurio.lms.cte.model.v104.InfOutros> _infOutrosList;


      //----------------/
     //- Constructors -/
    //----------------/

    public RemChoice2() {
        super();
        this._infNFList = new java.util.ArrayList<com.mercurio.lms.cte.model.v104.InfNF>();
        this._infNFeList = new java.util.ArrayList<com.mercurio.lms.cte.model.v104.InfNFe>();
        this._infOutrosList = new java.util.ArrayList<com.mercurio.lms.cte.model.v104.InfOutros>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vInfNF
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfNF(
            final com.mercurio.lms.cte.model.v104.InfNF vInfNF)
    throws java.lang.IndexOutOfBoundsException {
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
            final com.mercurio.lms.cte.model.v104.InfNF vInfNF)
    throws java.lang.IndexOutOfBoundsException {
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
            final com.mercurio.lms.cte.model.v104.InfNFe vInfNFe)
    throws java.lang.IndexOutOfBoundsException {
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
            final com.mercurio.lms.cte.model.v104.InfNFe vInfNFe)
    throws java.lang.IndexOutOfBoundsException {
        this._infNFeList.add(index, vInfNFe);
    }

    /**
     * 
     * 
     * @param vInfOutros
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfOutros(
            final com.mercurio.lms.cte.model.v104.InfOutros vInfOutros)
    throws java.lang.IndexOutOfBoundsException {
        this._infOutrosList.add(vInfOutros);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfOutros
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfOutros(
            final int index,
            final com.mercurio.lms.cte.model.v104.InfOutros vInfOutros)
    throws java.lang.IndexOutOfBoundsException {
        this._infOutrosList.add(index, vInfOutros);
    }

    /**
     * Method enumerateInfNF.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v104.InfNF> enumerateInfNF(
    ) {
        return java.util.Collections.enumeration(this._infNFList);
    }

    /**
     * Method enumerateInfNFe.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v104.InfNFe> enumerateInfNFe(
    ) {
        return java.util.Collections.enumeration(this._infNFeList);
    }

    /**
     * Method enumerateInfOutros.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v104.InfOutros> enumerateInfOutros(
    ) {
        return java.util.Collections.enumeration(this._infOutrosList);
    }

    /**
     * Method getInfNF.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.mercurio.lms.cte.model.InfNF at
     * the given index
     */
    public com.mercurio.lms.cte.model.v104.InfNF getInfNF(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infNFList.size()) {
            throw new IndexOutOfBoundsException("getInfNF: Index value '" + index + "' not in range [0.." + (this._infNFList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v104.InfNF) _infNFList.get(index);
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
    public com.mercurio.lms.cte.model.v104.InfNF[] getInfNF(
    ) {
        com.mercurio.lms.cte.model.v104.InfNF[] array = new com.mercurio.lms.cte.model.v104.InfNF[0];
        return (com.mercurio.lms.cte.model.v104.InfNF[]) this._infNFList.toArray(array);
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
     * @return the value of the com.mercurio.lms.cte.model.InfNFe
     * at the given index
     */
    public com.mercurio.lms.cte.model.v104.InfNFe getInfNFe(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infNFeList.size()) {
            throw new IndexOutOfBoundsException("getInfNFe: Index value '" + index + "' not in range [0.." + (this._infNFeList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v104.InfNFe) _infNFeList.get(index);
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
    public com.mercurio.lms.cte.model.v104.InfNFe[] getInfNFe(
    ) {
        com.mercurio.lms.cte.model.v104.InfNFe[] array = new com.mercurio.lms.cte.model.v104.InfNFe[0];
        return (com.mercurio.lms.cte.model.v104.InfNFe[]) this._infNFeList.toArray(array);
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
     * Method getInfOutros.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.InfOutros at the given index
     */
    public com.mercurio.lms.cte.model.v104.InfOutros getInfOutros(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infOutrosList.size()) {
            throw new IndexOutOfBoundsException("getInfOutros: Index value '" + index + "' not in range [0.." + (this._infOutrosList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v104.InfOutros) _infOutrosList.get(index);
    }

    /**
     * Method getInfOutros.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v104.InfOutros[] getInfOutros(
    ) {
        com.mercurio.lms.cte.model.v104.InfOutros[] array = new com.mercurio.lms.cte.model.v104.InfOutros[0];
        return (com.mercurio.lms.cte.model.v104.InfOutros[]) this._infOutrosList.toArray(array);
    }

    /**
     * Method getInfOutrosCount.
     * 
     * @return the size of this collection
     */
    public int getInfOutrosCount(
    ) {
        return this._infOutrosList.size();
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
     * Method iterateInfNF.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v104.InfNF> iterateInfNF(
    ) {
        return this._infNFList.iterator();
    }

    /**
     * Method iterateInfNFe.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v104.InfNFe> iterateInfNFe(
    ) {
        return this._infNFeList.iterator();
    }

    /**
     * Method iterateInfOutros.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v104.InfOutros> iterateInfOutros(
    ) {
        return this._infOutrosList.iterator();
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
     */
    public void removeAllInfOutros(
    ) {
        this._infOutrosList.clear();
    }

    /**
     * Method removeInfNF.
     * 
     * @param vInfNF
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfNF(
            final com.mercurio.lms.cte.model.v104.InfNF vInfNF) {
        boolean removed = _infNFList.remove(vInfNF);
        return removed;
    }

    /**
     * Method removeInfNFAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v104.InfNF removeInfNFAt(
            final int index) {
        java.lang.Object obj = this._infNFList.remove(index);
        return (com.mercurio.lms.cte.model.v104.InfNF) obj;
    }

    /**
     * Method removeInfNFe.
     * 
     * @param vInfNFe
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfNFe(
            final com.mercurio.lms.cte.model.v104.InfNFe vInfNFe) {
        boolean removed = _infNFeList.remove(vInfNFe);
        return removed;
    }

    /**
     * Method removeInfNFeAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v104.InfNFe removeInfNFeAt(
            final int index) {
        java.lang.Object obj = this._infNFeList.remove(index);
        return (com.mercurio.lms.cte.model.v104.InfNFe) obj;
    }

    /**
     * Method removeInfOutros.
     * 
     * @param vInfOutros
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfOutros(
            final com.mercurio.lms.cte.model.v104.InfOutros vInfOutros) {
        boolean removed = _infOutrosList.remove(vInfOutros);
        return removed;
    }

    /**
     * Method removeInfOutrosAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v104.InfOutros removeInfOutrosAt(
            final int index) {
        java.lang.Object obj = this._infOutrosList.remove(index);
        return (com.mercurio.lms.cte.model.v104.InfOutros) obj;
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
            final com.mercurio.lms.cte.model.v104.InfNF vInfNF)
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
            final com.mercurio.lms.cte.model.v104.InfNF[] vInfNFArray) {
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
            final com.mercurio.lms.cte.model.v104.InfNFe vInfNFe)
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
            final com.mercurio.lms.cte.model.v104.InfNFe[] vInfNFeArray) {
        //-- copy array
        _infNFeList.clear();

        for (int i = 0; i < vInfNFeArray.length; i++) {
                this._infNFeList.add(vInfNFeArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vInfOutros
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfOutros(
            final int index,
            final com.mercurio.lms.cte.model.v104.InfOutros vInfOutros)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infOutrosList.size()) {
            throw new IndexOutOfBoundsException("setInfOutros: Index value '" + index + "' not in range [0.." + (this._infOutrosList.size() - 1) + "]");
        }

        this._infOutrosList.set(index, vInfOutros);
    }

    /**
     * 
     * 
     * @param vInfOutrosArray
     */
    public void setInfOutros(
            final com.mercurio.lms.cte.model.v104.InfOutros[] vInfOutrosArray) {
        //-- copy array
        _infOutrosList.clear();

        for (int i = 0; i < vInfOutrosArray.length; i++) {
                this._infOutrosList.add(vInfOutrosArray[i]);
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
     * @return the unmarshaled com.mercurio.lms.cte.model.RemChoice2
     */
    public static com.mercurio.lms.cte.model.v104.RemChoice2 unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.RemChoice2) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.RemChoice2.class, reader);
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
