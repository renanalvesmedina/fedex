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
public class DetVagDCL implements java.io.Serializable {


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
     * Lacres dos vagões do DCL
     */
    private java.util.List<com.mercurio.lms.cte.model.v103.LacDetVagDCL> _lacDetVagDCLList;

    /**
     * informações dos containeres contidos no vagão com DCL
     */
    private java.util.List<com.mercurio.lms.cte.model.v103.ContDCL> _contDCLList;


      //----------------/
     //- Constructors -/
    //----------------/

    public DetVagDCL() {
        super();
        this._lacDetVagDCLList = new java.util.ArrayList<com.mercurio.lms.cte.model.v103.LacDetVagDCL>();
        this._contDCLList = new java.util.ArrayList<com.mercurio.lms.cte.model.v103.ContDCL>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vContDCL
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addContDCL(
            final com.mercurio.lms.cte.model.v103.ContDCL vContDCL)
    throws java.lang.IndexOutOfBoundsException {
        this._contDCLList.add(vContDCL);
    }

    /**
     * 
     * 
     * @param index
     * @param vContDCL
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addContDCL(
            final int index,
            final com.mercurio.lms.cte.model.v103.ContDCL vContDCL)
    throws java.lang.IndexOutOfBoundsException {
        this._contDCLList.add(index, vContDCL);
    }

    /**
     * 
     * 
     * @param vLacDetVagDCL
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacDetVagDCL(
            final com.mercurio.lms.cte.model.v103.LacDetVagDCL vLacDetVagDCL)
    throws java.lang.IndexOutOfBoundsException {
        this._lacDetVagDCLList.add(vLacDetVagDCL);
    }

    /**
     * 
     * 
     * @param index
     * @param vLacDetVagDCL
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacDetVagDCL(
            final int index,
            final com.mercurio.lms.cte.model.v103.LacDetVagDCL vLacDetVagDCL)
    throws java.lang.IndexOutOfBoundsException {
        this._lacDetVagDCLList.add(index, vLacDetVagDCL);
    }

    /**
     * Method enumerateContDCL.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v103.ContDCL> enumerateContDCL(
    ) {
        return java.util.Collections.enumeration(this._contDCLList);
    }

    /**
     * Method enumerateLacDetVagDCL.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v103.LacDetVagDCL> enumerateLacDetVagDCL(
    ) {
        return java.util.Collections.enumeration(this._lacDetVagDCLList);
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
     * Method getContDCL.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v103.ContDCL at the given index
     */
    public com.mercurio.lms.cte.model.v103.ContDCL getContDCL(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._contDCLList.size()) {
            throw new IndexOutOfBoundsException("getContDCL: Index value '" + index + "' not in range [0.." + (this._contDCLList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v103.ContDCL) _contDCLList.get(index);
    }

    /**
     * Method getContDCL.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v103.ContDCL[] getContDCL(
    ) {
        com.mercurio.lms.cte.model.v103.ContDCL[] array = new com.mercurio.lms.cte.model.v103.ContDCL[0];
        return (com.mercurio.lms.cte.model.v103.ContDCL[]) this._contDCLList.toArray(array);
    }

    /**
     * Method getContDCLCount.
     * 
     * @return the size of this collection
     */
    public int getContDCLCount(
    ) {
        return this._contDCLList.size();
    }

    /**
     * Method getLacDetVagDCL.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v103.LacDetVagDCL at the given
     * index
     */
    public com.mercurio.lms.cte.model.v103.LacDetVagDCL getLacDetVagDCL(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._lacDetVagDCLList.size()) {
            throw new IndexOutOfBoundsException("getLacDetVagDCL: Index value '" + index + "' not in range [0.." + (this._lacDetVagDCLList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v103.LacDetVagDCL) _lacDetVagDCLList.get(index);
    }

    /**
     * Method getLacDetVagDCL.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v103.LacDetVagDCL[] getLacDetVagDCL(
    ) {
        com.mercurio.lms.cte.model.v103.LacDetVagDCL[] array = new com.mercurio.lms.cte.model.v103.LacDetVagDCL[0];
        return (com.mercurio.lms.cte.model.v103.LacDetVagDCL[]) this._lacDetVagDCLList.toArray(array);
    }

    /**
     * Method getLacDetVagDCLCount.
     * 
     * @return the size of this collection
     */
    public int getLacDetVagDCLCount(
    ) {
        return this._lacDetVagDCLList.size();
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
     * Method iterateContDCL.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v103.ContDCL> iterateContDCL(
    ) {
        return this._contDCLList.iterator();
    }

    /**
     * Method iterateLacDetVagDCL.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v103.LacDetVagDCL> iterateLacDetVagDCL(
    ) {
        return this._lacDetVagDCLList.iterator();
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
    public void removeAllContDCL(
    ) {
        this._contDCLList.clear();
    }

    /**
     */
    public void removeAllLacDetVagDCL(
    ) {
        this._lacDetVagDCLList.clear();
    }

    /**
     * Method removeContDCL.
     * 
     * @param vContDCL
     * @return true if the object was removed from the collection.
     */
    public boolean removeContDCL(
            final com.mercurio.lms.cte.model.v103.ContDCL vContDCL) {
        boolean removed = _contDCLList.remove(vContDCL);
        return removed;
    }

    /**
     * Method removeContDCLAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v103.ContDCL removeContDCLAt(
            final int index) {
        java.lang.Object obj = this._contDCLList.remove(index);
        return (com.mercurio.lms.cte.model.v103.ContDCL) obj;
    }

    /**
     * Method removeLacDetVagDCL.
     * 
     * @param vLacDetVagDCL
     * @return true if the object was removed from the collection.
     */
    public boolean removeLacDetVagDCL(
            final com.mercurio.lms.cte.model.v103.LacDetVagDCL vLacDetVagDCL) {
        boolean removed = _lacDetVagDCLList.remove(vLacDetVagDCL);
        return removed;
    }

    /**
     * Method removeLacDetVagDCLAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v103.LacDetVagDCL removeLacDetVagDCLAt(
            final int index) {
        java.lang.Object obj = this._lacDetVagDCLList.remove(index);
        return (com.mercurio.lms.cte.model.v103.LacDetVagDCL) obj;
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
     * @param vContDCL
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setContDCL(
            final int index,
            final com.mercurio.lms.cte.model.v103.ContDCL vContDCL)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._contDCLList.size()) {
            throw new IndexOutOfBoundsException("setContDCL: Index value '" + index + "' not in range [0.." + (this._contDCLList.size() - 1) + "]");
        }

        this._contDCLList.set(index, vContDCL);
    }

    /**
     * 
     * 
     * @param vContDCLArray
     */
    public void setContDCL(
            final com.mercurio.lms.cte.model.v103.ContDCL[] vContDCLArray) {
        //-- copy array
        _contDCLList.clear();

        for (int i = 0; i < vContDCLArray.length; i++) {
                this._contDCLList.add(vContDCLArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vLacDetVagDCL
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setLacDetVagDCL(
            final int index,
            final com.mercurio.lms.cte.model.v103.LacDetVagDCL vLacDetVagDCL)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._lacDetVagDCLList.size()) {
            throw new IndexOutOfBoundsException("setLacDetVagDCL: Index value '" + index + "' not in range [0.." + (this._lacDetVagDCLList.size() - 1) + "]");
        }

        this._lacDetVagDCLList.set(index, vLacDetVagDCL);
    }

    /**
     * 
     * 
     * @param vLacDetVagDCLArray
     */
    public void setLacDetVagDCL(
            final com.mercurio.lms.cte.model.v103.LacDetVagDCL[] vLacDetVagDCLArray) {
        //-- copy array
        _lacDetVagDCLList.clear();

        for (int i = 0; i < vLacDetVagDCLArray.length; i++) {
                this._lacDetVagDCLList.add(vLacDetVagDCLArray[i]);
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
     * @return the unmarshaled
     * com.mercurio.lms.cte.model.v103.DetVagDCL
     */
    public static com.mercurio.lms.cte.model.v103.DetVagDCL unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.DetVagDCL) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.DetVagDCL.class, reader);
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
