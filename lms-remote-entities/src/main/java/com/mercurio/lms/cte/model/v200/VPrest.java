/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v200;

/**
 * Valores da Prestação de Serviço
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class VPrest implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Valor Total da Prestação do Serviço
     */
    private java.lang.String _vTPrest;

    /**
     * Valor a Receber
     */
    private java.lang.String _vRec;

    /**
     * Componentes do Valor da Prestação
     */
    private java.util.Vector<com.mercurio.lms.cte.model.v200.Comp> _compList;


      //----------------/
     //- Constructors -/
    //----------------/

    public VPrest() {
        super();
        this._compList = new java.util.Vector<com.mercurio.lms.cte.model.v200.Comp>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vComp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addComp(
            final com.mercurio.lms.cte.model.v200.Comp vComp)
    throws java.lang.IndexOutOfBoundsException {
        this._compList.addElement(vComp);
    }

    /**
     * 
     * 
     * @param index
     * @param vComp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addComp(
            final int index,
            final com.mercurio.lms.cte.model.v200.Comp vComp)
    throws java.lang.IndexOutOfBoundsException {
        this._compList.add(index, vComp);
    }

    /**
     * Method enumerateComp.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v200.Comp elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v200.Comp> enumerateComp(
    ) {
        return this._compList.elements();
    }

    /**
     * Method getComp.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v200.Comp at the given index
     */
    public com.mercurio.lms.cte.model.v200.Comp getComp(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._compList.size()) {
            throw new IndexOutOfBoundsException("getComp: Index value '" + index + "' not in range [0.." + (this._compList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v200.Comp) _compList.get(index);
    }

    /**
     * Method getComp.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v200.Comp[] getComp(
    ) {
        com.mercurio.lms.cte.model.v200.Comp[] array = new com.mercurio.lms.cte.model.v200.Comp[0];
        return (com.mercurio.lms.cte.model.v200.Comp[]) this._compList.toArray(array);
    }

    /**
     * Method getCompCount.
     * 
     * @return the size of this collection
     */
    public int getCompCount(
    ) {
        return this._compList.size();
    }

    /**
     * Returns the value of field 'vRec'. The field 'vRec' has the
     * following description: Valor a Receber
     * 
     * @return the value of field 'VRec'.
     */
    public java.lang.String getVRec(
    ) {
        return this._vRec;
    }

    /**
     * Returns the value of field 'vTPrest'. The field 'vTPrest'
     * has the following description: Valor Total da Prestação do
     * Serviço
     * 
     * @return the value of field 'VTPrest'.
     */
    public java.lang.String getVTPrest(
    ) {
        return this._vTPrest;
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
    public void removeAllComp(
    ) {
        this._compList.clear();
    }

    /**
     * Method removeComp.
     * 
     * @param vComp
     * @return true if the object was removed from the collection.
     */
    public boolean removeComp(
            final com.mercurio.lms.cte.model.v200.Comp vComp) {
        boolean removed = _compList.remove(vComp);
        return removed;
    }

    /**
     * Method removeCompAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v200.Comp removeCompAt(
            final int index) {
        java.lang.Object obj = this._compList.remove(index);
        return (com.mercurio.lms.cte.model.v200.Comp) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vComp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setComp(
            final int index,
            final com.mercurio.lms.cte.model.v200.Comp vComp)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._compList.size()) {
            throw new IndexOutOfBoundsException("setComp: Index value '" + index + "' not in range [0.." + (this._compList.size() - 1) + "]");
        }

        this._compList.set(index, vComp);
    }

    /**
     * 
     * 
     * @param vCompArray
     */
    public void setComp(
            final com.mercurio.lms.cte.model.v200.Comp[] vCompArray) {
        //-- copy array
        _compList.clear();

        for (int i = 0; i < vCompArray.length; i++) {
                this._compList.add(vCompArray[i]);
        }
    }

    /**
     * Sets the value of field 'vRec'. The field 'vRec' has the
     * following description: Valor a Receber
     * 
     * @param vRec the value of field 'vRec'.
     */
    public void setVRec(
            final java.lang.String vRec) {
        this._vRec = vRec;
    }

    /**
     * Sets the value of field 'vTPrest'. The field 'vTPrest' has
     * the following description: Valor Total da Prestação do
     * Serviço
     * 
     * @param vTPrest the value of field 'vTPrest'.
     */
    public void setVTPrest(
            final java.lang.String vTPrest) {
        this._vTPrest = vTPrest;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v200.VPres
     */
    public static com.mercurio.lms.cte.model.v200.VPrest unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v200.VPrest) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v200.VPrest.class, reader);
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
