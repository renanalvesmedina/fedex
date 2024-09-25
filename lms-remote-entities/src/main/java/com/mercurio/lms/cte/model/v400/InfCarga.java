/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Informações da Carga do CT-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfCarga implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Valor total da carga
     */
    private String _vCarga;

    /**
     * Produto predominante
     */
    private String _proPred;

    /**
     * Outras características da carga
     */
    private String _xOutCat;

    /**
     * Informações de quantidades da Carga do CT-e
     */
    private java.util.Vector<InfQ> _infQList;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfCarga() {
        super();
        this._infQList = new java.util.Vector<InfQ>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     *
     *
     * @param vInfQ
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfQ(
            final InfQ vInfQ)
    throws IndexOutOfBoundsException {
        this._infQList.addElement(vInfQ);
    }

    /**
     *
     *
     * @param index
     * @param vInfQ
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfQ(
            final int index,
            final InfQ vInfQ)
    throws IndexOutOfBoundsException {
        this._infQList.add(index, vInfQ);
    }

    /**
     * Method enumerateInfQ.
     *
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v400.InfQ elements
     */
    public java.util.Enumeration<? extends InfQ> enumerateInfQ(
    ) {
        return this._infQList.elements();
    }

    /**
     * Method getInfQ.
     *
     * @param index
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v400.InfQ at the given index
     */
    public InfQ getInfQ(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infQList.size()) {
            throw new IndexOutOfBoundsException("getInfQ: Index value '" + index + "' not in range [0.." + (this._infQList.size() - 1) + "]");
        }

        return (InfQ) _infQList.get(index);
    }

    /**
     * Method getInfQ.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     *
     * @return this collection as an Array
     */
    public InfQ[] getInfQ(
    ) {
        InfQ[] array = new InfQ[0];
        return (InfQ[]) this._infQList.toArray(array);
    }

    /**
     * Method getInfQCount.
     *
     * @return the size of this collection
     */
    public int getInfQCount(
    ) {
        return this._infQList.size();
    }

    /**
     * Returns the value of field 'proPred'. The field 'proPred'
     * has the following description: Produto predominante
     *
     * @return the value of field 'ProPred'.
     */
    public String getProPred(
    ) {
        return this._proPred;
    }

    /**
     * Returns the value of field 'vCarga'. The field 'vCarga' has
     * the following description: Valor total da carga
     *
     * @return the value of field 'VCarga'.
     */
    public String getVCarga(
    ) {
        return this._vCarga;
    }

    /**
     * Returns the value of field 'xOutCat'. The field 'xOutCat'
     * has the following description: Outras características da
     * carga
     *
     * @return the value of field 'XOutCat'.
     */
    public String getXOutCat(
    ) {
        return this._xOutCat;
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
    public void removeAllInfQ(
    ) {
        this._infQList.clear();
    }

    /**
     * Method removeInfQ.
     *
     * @param vInfQ
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfQ(
            final InfQ vInfQ) {
        boolean removed = _infQList.remove(vInfQ);
        return removed;
    }

    /**
     * Method removeInfQAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public InfQ removeInfQAt(
            final int index) {
        Object obj = this._infQList.remove(index);
        return (InfQ) obj;
    }

    /**
     *
     *
     * @param index
     * @param vInfQ
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfQ(
            final int index,
            final InfQ vInfQ)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infQList.size()) {
            throw new IndexOutOfBoundsException("setInfQ: Index value '" + index + "' not in range [0.." + (this._infQList.size() - 1) + "]");
        }

        this._infQList.set(index, vInfQ);
    }

    /**
     *
     *
     * @param vInfQArray
     */
    public void setInfQ(
            final InfQ[] vInfQArray) {
        //-- copy array
        _infQList.clear();

        for (int i = 0; i < vInfQArray.length; i++) {
                this._infQList.add(vInfQArray[i]);
        }
    }

    /**
     * Sets the value of field 'proPred'. The field 'proPred' has
     * the following description: Produto predominante
     *
     * @param proPred the value of field 'proPred'.
     */
    public void setProPred(
            final String proPred) {
        this._proPred = proPred;
    }

    /**
     * Sets the value of field 'vCarga'. The field 'vCarga' has the
     * following description: Valor total da carga
     *
     * @param vCarga the value of field 'vCarga'.
     */
    public void setVCarga(
            final String vCarga) {
        this._vCarga = vCarga;
    }

    /**
     * Sets the value of field 'xOutCat'. The field 'xOutCat' has
     * the following description: Outras características da carga
     *
     * @param xOutCat the value of field 'xOutCat'.
     */
    public void setXOutCat(
            final String xOutCat) {
        this._xOutCat = xOutCat;
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
     * com.mercurio.lms.cte.model.v400.InfCarga
     */
    public static InfCarga unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (InfCarga) org.exolab.castor.xml.Unmarshaller.unmarshal(InfCarga.class, reader);
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
