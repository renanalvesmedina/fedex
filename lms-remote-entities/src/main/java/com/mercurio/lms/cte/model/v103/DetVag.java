/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * informações de detalhes dos Vagões
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class DetVag implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Número de Identificação do vagão
     */
    private java.lang.String _nVag;

    /**
     * Capacidade em Toneladas
     */
    private java.lang.String _cap;

    /**
     * Tipo de Vagão
     */
    private java.lang.String _tpVag;

    /**
     * Peso Real em Toneladas
     */
    private java.lang.String _pesoR;

    /**
     * Peso Base de Cálculo de Frete em Toneladas
     */
    private java.lang.String _pesoBC;

    /**
     * Lacres dos vagões
     */
    private java.util.List<com.mercurio.lms.cte.model.v103.LacDetVag> _lacDetVagList;

    /**
     * informações dos containeres contidos no vagão com DCL
     */
    private java.util.List<com.mercurio.lms.cte.model.v103.ContVag> _contVagList;


      //----------------/
     //- Constructors -/
    //----------------/

    public DetVag() {
        super();
        this._lacDetVagList = new java.util.ArrayList<com.mercurio.lms.cte.model.v103.LacDetVag>();
        this._contVagList = new java.util.ArrayList<com.mercurio.lms.cte.model.v103.ContVag>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vContVag
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addContVag(
            final com.mercurio.lms.cte.model.v103.ContVag vContVag)
    throws java.lang.IndexOutOfBoundsException {
        this._contVagList.add(vContVag);
    }

    /**
     * 
     * 
     * @param index
     * @param vContVag
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addContVag(
            final int index,
            final com.mercurio.lms.cte.model.v103.ContVag vContVag)
    throws java.lang.IndexOutOfBoundsException {
        this._contVagList.add(index, vContVag);
    }

    /**
     * 
     * 
     * @param vLacDetVag
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacDetVag(
            final com.mercurio.lms.cte.model.v103.LacDetVag vLacDetVag)
    throws java.lang.IndexOutOfBoundsException {
        this._lacDetVagList.add(vLacDetVag);
    }

    /**
     * 
     * 
     * @param index
     * @param vLacDetVag
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacDetVag(
            final int index,
            final com.mercurio.lms.cte.model.v103.LacDetVag vLacDetVag)
    throws java.lang.IndexOutOfBoundsException {
        this._lacDetVagList.add(index, vLacDetVag);
    }

    /**
     * Method enumerateContVag.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v103.ContVag> enumerateContVag(
    ) {
        return java.util.Collections.enumeration(this._contVagList);
    }

    /**
     * Method enumerateLacDetVag.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v103.LacDetVag> enumerateLacDetVag(
    ) {
        return java.util.Collections.enumeration(this._lacDetVagList);
    }

    /**
     * Returns the value of field 'cap'. The field 'cap' has the
     * following description: Capacidade em Toneladas
     * 
     * @return the value of field 'Cap'.
     */
    public java.lang.String getCap(
    ) {
        return this._cap;
    }

    /**
     * Method getContVag.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v103.ContVag at the given index
     */
    public com.mercurio.lms.cte.model.v103.ContVag getContVag(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._contVagList.size()) {
            throw new IndexOutOfBoundsException("getContVag: Index value '" + index + "' not in range [0.." + (this._contVagList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v103.ContVag) _contVagList.get(index);
    }

    /**
     * Method getContVag.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v103.ContVag[] getContVag(
    ) {
        com.mercurio.lms.cte.model.v103.ContVag[] array = new com.mercurio.lms.cte.model.v103.ContVag[0];
        return (com.mercurio.lms.cte.model.v103.ContVag[]) this._contVagList.toArray(array);
    }

    /**
     * Method getContVagCount.
     * 
     * @return the size of this collection
     */
    public int getContVagCount(
    ) {
        return this._contVagList.size();
    }

    /**
     * Method getLacDetVag.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v103.LacDetVag at the given index
     */
    public com.mercurio.lms.cte.model.v103.LacDetVag getLacDetVag(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._lacDetVagList.size()) {
            throw new IndexOutOfBoundsException("getLacDetVag: Index value '" + index + "' not in range [0.." + (this._lacDetVagList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v103.LacDetVag) _lacDetVagList.get(index);
    }

    /**
     * Method getLacDetVag.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v103.LacDetVag[] getLacDetVag(
    ) {
        com.mercurio.lms.cte.model.v103.LacDetVag[] array = new com.mercurio.lms.cte.model.v103.LacDetVag[0];
        return (com.mercurio.lms.cte.model.v103.LacDetVag[]) this._lacDetVagList.toArray(array);
    }

    /**
     * Method getLacDetVagCount.
     * 
     * @return the size of this collection
     */
    public int getLacDetVagCount(
    ) {
        return this._lacDetVagList.size();
    }

    /**
     * Returns the value of field 'nVag'. The field 'nVag' has the
     * following description: Número de Identificação do vagão
     * 
     * @return the value of field 'NVag'.
     */
    public java.lang.String getNVag(
    ) {
        return this._nVag;
    }

    /**
     * Returns the value of field 'pesoBC'. The field 'pesoBC' has
     * the following description: Peso Base de Cálculo de Frete em
     * Toneladas
     * 
     * 
     * @return the value of field 'PesoBC'.
     */
    public java.lang.String getPesoBC(
    ) {
        return this._pesoBC;
    }

    /**
     * Returns the value of field 'pesoR'. The field 'pesoR' has
     * the following description: Peso Real em Toneladas
     * 
     * @return the value of field 'PesoR'.
     */
    public java.lang.String getPesoR(
    ) {
        return this._pesoR;
    }

    /**
     * Returns the value of field 'tpVag'. The field 'tpVag' has
     * the following description: Tipo de Vagão
     * 
     * @return the value of field 'TpVag'.
     */
    public java.lang.String getTpVag(
    ) {
        return this._tpVag;
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
     * Method iterateContVag.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v103.ContVag> iterateContVag(
    ) {
        return this._contVagList.iterator();
    }

    /**
     * Method iterateLacDetVag.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v103.LacDetVag> iterateLacDetVag(
    ) {
        return this._lacDetVagList.iterator();
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
    public void removeAllContVag(
    ) {
        this._contVagList.clear();
    }

    /**
     */
    public void removeAllLacDetVag(
    ) {
        this._lacDetVagList.clear();
    }

    /**
     * Method removeContVag.
     * 
     * @param vContVag
     * @return true if the object was removed from the collection.
     */
    public boolean removeContVag(
            final com.mercurio.lms.cte.model.v103.ContVag vContVag) {
        boolean removed = _contVagList.remove(vContVag);
        return removed;
    }

    /**
     * Method removeContVagAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v103.ContVag removeContVagAt(
            final int index) {
        java.lang.Object obj = this._contVagList.remove(index);
        return (com.mercurio.lms.cte.model.v103.ContVag) obj;
    }

    /**
     * Method removeLacDetVag.
     * 
     * @param vLacDetVag
     * @return true if the object was removed from the collection.
     */
    public boolean removeLacDetVag(
            final com.mercurio.lms.cte.model.v103.LacDetVag vLacDetVag) {
        boolean removed = _lacDetVagList.remove(vLacDetVag);
        return removed;
    }

    /**
     * Method removeLacDetVagAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v103.LacDetVag removeLacDetVagAt(
            final int index) {
        java.lang.Object obj = this._lacDetVagList.remove(index);
        return (com.mercurio.lms.cte.model.v103.LacDetVag) obj;
    }

    /**
     * Sets the value of field 'cap'. The field 'cap' has the
     * following description: Capacidade em Toneladas
     * 
     * @param cap the value of field 'cap'.
     */
    public void setCap(
            final java.lang.String cap) {
        this._cap = cap;
    }

    /**
     * 
     * 
     * @param index
     * @param vContVag
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setContVag(
            final int index,
            final com.mercurio.lms.cte.model.v103.ContVag vContVag)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._contVagList.size()) {
            throw new IndexOutOfBoundsException("setContVag: Index value '" + index + "' not in range [0.." + (this._contVagList.size() - 1) + "]");
        }

        this._contVagList.set(index, vContVag);
    }

    /**
     * 
     * 
     * @param vContVagArray
     */
    public void setContVag(
            final com.mercurio.lms.cte.model.v103.ContVag[] vContVagArray) {
        //-- copy array
        _contVagList.clear();

        for (int i = 0; i < vContVagArray.length; i++) {
                this._contVagList.add(vContVagArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vLacDetVag
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setLacDetVag(
            final int index,
            final com.mercurio.lms.cte.model.v103.LacDetVag vLacDetVag)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._lacDetVagList.size()) {
            throw new IndexOutOfBoundsException("setLacDetVag: Index value '" + index + "' not in range [0.." + (this._lacDetVagList.size() - 1) + "]");
        }

        this._lacDetVagList.set(index, vLacDetVag);
    }

    /**
     * 
     * 
     * @param vLacDetVagArray
     */
    public void setLacDetVag(
            final com.mercurio.lms.cte.model.v103.LacDetVag[] vLacDetVagArray) {
        //-- copy array
        _lacDetVagList.clear();

        for (int i = 0; i < vLacDetVagArray.length; i++) {
                this._lacDetVagList.add(vLacDetVagArray[i]);
        }
    }

    /**
     * Sets the value of field 'nVag'. The field 'nVag' has the
     * following description: Número de Identificação do vagão
     * 
     * @param nVag the value of field 'nVag'.
     */
    public void setNVag(
            final java.lang.String nVag) {
        this._nVag = nVag;
    }

    /**
     * Sets the value of field 'pesoBC'. The field 'pesoBC' has the
     * following description: Peso Base de Cálculo de Frete em
     * Toneladas
     * 
     * 
     * @param pesoBC the value of field 'pesoBC'.
     */
    public void setPesoBC(
            final java.lang.String pesoBC) {
        this._pesoBC = pesoBC;
    }

    /**
     * Sets the value of field 'pesoR'. The field 'pesoR' has the
     * following description: Peso Real em Toneladas
     * 
     * @param pesoR the value of field 'pesoR'.
     */
    public void setPesoR(
            final java.lang.String pesoR) {
        this._pesoR = pesoR;
    }

    /**
     * Sets the value of field 'tpVag'. The field 'tpVag' has the
     * following description: Tipo de Vagão
     * 
     * @param tpVag the value of field 'tpVag'.
     */
    public void setTpVag(
            final java.lang.String tpVag) {
        this._tpVag = tpVag;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v103.DetVa
     */
    public static com.mercurio.lms.cte.model.v103.DetVag unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.DetVag) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.DetVag.class, reader);
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
