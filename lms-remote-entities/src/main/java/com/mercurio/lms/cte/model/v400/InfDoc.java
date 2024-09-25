/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Informações dos documentos transportados pelo CT-e
 * Opcional para Redespacho Intermediario e Serviço vinculado a
 * multimodal.Poderá não ser informado para os CT-e de redespacho
 * intermediário. Nos demais casos deverá sempre ser informado.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfDoc implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Internal choice value storage
     */
    private Object _choiceValue;

    /**
     * Informações das NF
     */
    private java.util.Vector<InfNF> _infNFList;

    /**
     * Informações das NF-e
     */
    private java.util.Vector<InfNFe> _infNFeList;

    /**
     * Informações dos demais documentos
     */
    private java.util.Vector<InfOutros> _infOutrosList;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfDoc() {
        super();
        this._infNFList = new java.util.Vector<InfNF>();
        this._infNFeList = new java.util.Vector<InfNFe>();
        this._infOutrosList = new java.util.Vector<InfOutros>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     *
     *
     * @param vInfNF
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfNF(
            final InfNF vInfNF)
    throws IndexOutOfBoundsException {
        this._infNFList.addElement(vInfNF);
    }

    /**
     *
     *
     * @param index
     * @param vInfNF
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfNF(
            final int index,
            final InfNF vInfNF)
    throws IndexOutOfBoundsException {
        this._infNFList.add(index, vInfNF);
    }

    /**
     *
     *
     * @param vInfNFe
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfNFe(
            final InfNFe vInfNFe)
    throws IndexOutOfBoundsException {
        this._infNFeList.addElement(vInfNFe);
    }

    /**
     *
     *
     * @param index
     * @param vInfNFe
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfNFe(
            final int index,
            final InfNFe vInfNFe)
    throws IndexOutOfBoundsException {
        this._infNFeList.add(index, vInfNFe);
    }

    /**
     *
     *
     * @param vInfOutros
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfOutros(
            final InfOutros vInfOutros)
    throws IndexOutOfBoundsException {
        this._infOutrosList.addElement(vInfOutros);
    }

    /**
     *
     *
     * @param index
     * @param vInfOutros
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfOutros(
            final int index,
            final InfOutros vInfOutros)
    throws IndexOutOfBoundsException {
        this._infOutrosList.add(index, vInfOutros);
    }

    /**
     * Method enumerateInfNF.
     *
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v400.InfNF elements
     */
    public java.util.Enumeration<? extends InfNF> enumerateInfNF(
    ) {
        return this._infNFList.elements();
    }

    /**
     * Method enumerateInfNFe.
     *
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v400.InfNFe elements
     */
    public java.util.Enumeration<? extends InfNFe> enumerateInfNFe(
    ) {
        return this._infNFeList.elements();
    }

    /**
     * Method enumerateInfOutros.
     *
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v400.InfOutros elements
     */
    public java.util.Enumeration<? extends InfOutros> enumerateInfOutros(
    ) {
        return this._infOutrosList.elements();
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
     * Method getInfNF.
     *
     * @param index
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v400.InfNF at the given index
     */
    public InfNF getInfNF(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infNFList.size()) {
            throw new IndexOutOfBoundsException("getInfNF: Index value '" + index + "' not in range [0.." + (this._infNFList.size() - 1) + "]");
        }

        return (InfNF) _infNFList.get(index);
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
    public InfNF[] getInfNF(
    ) {
        InfNF[] array = new InfNF[0];
        return (InfNF[]) this._infNFList.toArray(array);
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
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v400.InfNFe at the given index
     */
    public InfNFe getInfNFe(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infNFeList.size()) {
            throw new IndexOutOfBoundsException("getInfNFe: Index value '" + index + "' not in range [0.." + (this._infNFeList.size() - 1) + "]");
        }

        return (InfNFe) _infNFeList.get(index);
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
    public InfNFe[] getInfNFe(
    ) {
        InfNFe[] array = new InfNFe[0];
        return (InfNFe[]) this._infNFeList.toArray(array);
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
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v400.InfOutros at the given index
     */
    public InfOutros getInfOutros(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infOutrosList.size()) {
            throw new IndexOutOfBoundsException("getInfOutros: Index value '" + index + "' not in range [0.." + (this._infOutrosList.size() - 1) + "]");
        }

        return (InfOutros) _infOutrosList.get(index);
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
    public InfOutros[] getInfOutros(
    ) {
        InfOutros[] array = new InfOutros[0];
        return (InfOutros[]) this._infOutrosList.toArray(array);
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
            final InfNF vInfNF) {
        boolean removed = _infNFList.remove(vInfNF);
        return removed;
    }

    /**
     * Method removeInfNFAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public InfNF removeInfNFAt(
            final int index) {
        Object obj = this._infNFList.remove(index);
        return (InfNF) obj;
    }

    /**
     * Method removeInfNFe.
     *
     * @param vInfNFe
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfNFe(
            final InfNFe vInfNFe) {
        boolean removed = _infNFeList.remove(vInfNFe);
        return removed;
    }

    /**
     * Method removeInfNFeAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public InfNFe removeInfNFeAt(
            final int index) {
        Object obj = this._infNFeList.remove(index);
        return (InfNFe) obj;
    }

    /**
     * Method removeInfOutros.
     *
     * @param vInfOutros
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfOutros(
            final InfOutros vInfOutros) {
        boolean removed = _infOutrosList.remove(vInfOutros);
        return removed;
    }

    /**
     * Method removeInfOutrosAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public InfOutros removeInfOutrosAt(
            final int index) {
        Object obj = this._infOutrosList.remove(index);
        return (InfOutros) obj;
    }

    /**
     *
     *
     * @param index
     * @param vInfNF
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfNF(
            final int index,
            final InfNF vInfNF)
    throws IndexOutOfBoundsException {
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
            final InfNF[] vInfNFArray) {
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
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfNFe(
            final int index,
            final InfNFe vInfNFe)
    throws IndexOutOfBoundsException {
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
            final InfNFe[] vInfNFeArray) {
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
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfOutros(
            final int index,
            final InfOutros vInfOutros)
    throws IndexOutOfBoundsException {
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
            final InfOutros[] vInfOutrosArray) {
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
     * @return the unmarshaled com.mercurio.lms.cte.model.v400.InfDo
     */
    public static InfDoc unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (InfDoc) org.exolab.castor.xml.Unmarshaller.unmarshal(InfDoc.class, reader);
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
