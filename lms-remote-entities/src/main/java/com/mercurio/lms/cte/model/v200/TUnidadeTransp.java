/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v200;

/**
 * Tipo Dados Unidade de Transporte
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TUnidadeTransp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Tipo da Unidade de Transporte
     */
    private com.mercurio.lms.cte.model.v200.types.TtipoUnidTransp _tpUnidTransp;

    /**
     * Identificação da Unidade de Transporte
     */
    private java.lang.String _idUnidTransp;

    /**
     * Lacres das Unidades de Transporte
     */
    private java.util.Vector<com.mercurio.lms.cte.model.v200.LacUnidTransp> _lacUnidTranspList;

    /**
     * Informações das Unidades de Carga (Containeres/ULD/Outros)
     */
    private java.util.Vector<com.mercurio.lms.cte.model.v200.InfUnidCarga> _infUnidCargaList;

    /**
     * Quantidade rateada (Peso,Volume)
     */
    private java.lang.String _qtdRat;


      //----------------/
     //- Constructors -/
    //----------------/

    public TUnidadeTransp() {
        super();
        this._lacUnidTranspList = new java.util.Vector<com.mercurio.lms.cte.model.v200.LacUnidTransp>();
        this._infUnidCargaList = new java.util.Vector<com.mercurio.lms.cte.model.v200.InfUnidCarga>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vInfUnidCarga
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfUnidCarga(
            final com.mercurio.lms.cte.model.v200.InfUnidCarga vInfUnidCarga)
    throws java.lang.IndexOutOfBoundsException {
        this._infUnidCargaList.addElement(vInfUnidCarga);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfUnidCarga
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfUnidCarga(
            final int index,
            final com.mercurio.lms.cte.model.v200.InfUnidCarga vInfUnidCarga)
    throws java.lang.IndexOutOfBoundsException {
        this._infUnidCargaList.add(index, vInfUnidCarga);
    }

    /**
     * 
     * 
     * @param vLacUnidTransp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacUnidTransp(
            final com.mercurio.lms.cte.model.v200.LacUnidTransp vLacUnidTransp)
    throws java.lang.IndexOutOfBoundsException {
        this._lacUnidTranspList.addElement(vLacUnidTransp);
    }

    /**
     * 
     * 
     * @param index
     * @param vLacUnidTransp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacUnidTransp(
            final int index,
            final com.mercurio.lms.cte.model.v200.LacUnidTransp vLacUnidTransp)
    throws java.lang.IndexOutOfBoundsException {
        this._lacUnidTranspList.add(index, vLacUnidTransp);
    }

    /**
     * Method enumerateInfUnidCarga.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v200.InfUnidCarga elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v200.InfUnidCarga> enumerateInfUnidCarga(
    ) {
        return this._infUnidCargaList.elements();
    }

    /**
     * Method enumerateLacUnidTransp.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v200.LacUnidTransp elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v200.LacUnidTransp> enumerateLacUnidTransp(
    ) {
        return this._lacUnidTranspList.elements();
    }

    /**
     * Returns the value of field 'idUnidTransp'. The field
     * 'idUnidTransp' has the following description:
     * Identificação da Unidade de Transporte
     * 
     * @return the value of field 'IdUnidTransp'.
     */
    public java.lang.String getIdUnidTransp(
    ) {
        return this._idUnidTransp;
    }

    /**
     * Method getInfUnidCarga.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v200.InfUnidCarga at the given
     * index
     */
    public com.mercurio.lms.cte.model.v200.InfUnidCarga getInfUnidCarga(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infUnidCargaList.size()) {
            throw new IndexOutOfBoundsException("getInfUnidCarga: Index value '" + index + "' not in range [0.." + (this._infUnidCargaList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v200.InfUnidCarga) _infUnidCargaList.get(index);
    }

    /**
     * Method getInfUnidCarga.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v200.InfUnidCarga[] getInfUnidCarga(
    ) {
        com.mercurio.lms.cte.model.v200.InfUnidCarga[] array = new com.mercurio.lms.cte.model.v200.InfUnidCarga[0];
        return (com.mercurio.lms.cte.model.v200.InfUnidCarga[]) this._infUnidCargaList.toArray(array);
    }

    /**
     * Method getInfUnidCargaCount.
     * 
     * @return the size of this collection
     */
    public int getInfUnidCargaCount(
    ) {
        return this._infUnidCargaList.size();
    }

    /**
     * Method getLacUnidTransp.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v200.LacUnidTransp at the given
     * index
     */
    public com.mercurio.lms.cte.model.v200.LacUnidTransp getLacUnidTransp(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._lacUnidTranspList.size()) {
            throw new IndexOutOfBoundsException("getLacUnidTransp: Index value '" + index + "' not in range [0.." + (this._lacUnidTranspList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v200.LacUnidTransp) _lacUnidTranspList.get(index);
    }

    /**
     * Method getLacUnidTransp.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v200.LacUnidTransp[] getLacUnidTransp(
    ) {
        com.mercurio.lms.cte.model.v200.LacUnidTransp[] array = new com.mercurio.lms.cte.model.v200.LacUnidTransp[0];
        return (com.mercurio.lms.cte.model.v200.LacUnidTransp[]) this._lacUnidTranspList.toArray(array);
    }

    /**
     * Method getLacUnidTranspCount.
     * 
     * @return the size of this collection
     */
    public int getLacUnidTranspCount(
    ) {
        return this._lacUnidTranspList.size();
    }

    /**
     * Returns the value of field 'qtdRat'. The field 'qtdRat' has
     * the following description: Quantidade rateada (Peso,Volume)
     * 
     * @return the value of field 'QtdRat'.
     */
    public java.lang.String getQtdRat(
    ) {
        return this._qtdRat;
    }

    /**
     * Returns the value of field 'tpUnidTransp'. The field
     * 'tpUnidTransp' has the following description: Tipo da
     * Unidade de Transporte
     * 
     * @return the value of field 'TpUnidTransp'.
     */
    public com.mercurio.lms.cte.model.v200.types.TtipoUnidTransp getTpUnidTransp(
    ) {
        return this._tpUnidTransp;
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
    public void removeAllInfUnidCarga(
    ) {
        this._infUnidCargaList.clear();
    }

    /**
     */
    public void removeAllLacUnidTransp(
    ) {
        this._lacUnidTranspList.clear();
    }

    /**
     * Method removeInfUnidCarga.
     * 
     * @param vInfUnidCarga
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfUnidCarga(
            final com.mercurio.lms.cte.model.v200.InfUnidCarga vInfUnidCarga) {
        boolean removed = _infUnidCargaList.remove(vInfUnidCarga);
        return removed;
    }

    /**
     * Method removeInfUnidCargaAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v200.InfUnidCarga removeInfUnidCargaAt(
            final int index) {
        java.lang.Object obj = this._infUnidCargaList.remove(index);
        return (com.mercurio.lms.cte.model.v200.InfUnidCarga) obj;
    }

    /**
     * Method removeLacUnidTransp.
     * 
     * @param vLacUnidTransp
     * @return true if the object was removed from the collection.
     */
    public boolean removeLacUnidTransp(
            final com.mercurio.lms.cte.model.v200.LacUnidTransp vLacUnidTransp) {
        boolean removed = _lacUnidTranspList.remove(vLacUnidTransp);
        return removed;
    }

    /**
     * Method removeLacUnidTranspAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v200.LacUnidTransp removeLacUnidTranspAt(
            final int index) {
        java.lang.Object obj = this._lacUnidTranspList.remove(index);
        return (com.mercurio.lms.cte.model.v200.LacUnidTransp) obj;
    }

    /**
     * Sets the value of field 'idUnidTransp'. The field
     * 'idUnidTransp' has the following description:
     * Identificação da Unidade de Transporte
     * 
     * @param idUnidTransp the value of field 'idUnidTransp'.
     */
    public void setIdUnidTransp(
            final java.lang.String idUnidTransp) {
        this._idUnidTransp = idUnidTransp;
    }

    /**
     * 
     * 
     * @param index
     * @param vInfUnidCarga
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfUnidCarga(
            final int index,
            final com.mercurio.lms.cte.model.v200.InfUnidCarga vInfUnidCarga)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infUnidCargaList.size()) {
            throw new IndexOutOfBoundsException("setInfUnidCarga: Index value '" + index + "' not in range [0.." + (this._infUnidCargaList.size() - 1) + "]");
        }

        this._infUnidCargaList.set(index, vInfUnidCarga);
    }

    /**
     * 
     * 
     * @param vInfUnidCargaArray
     */
    public void setInfUnidCarga(
            final com.mercurio.lms.cte.model.v200.InfUnidCarga[] vInfUnidCargaArray) {
        //-- copy array
        _infUnidCargaList.clear();

        for (int i = 0; i < vInfUnidCargaArray.length; i++) {
                this._infUnidCargaList.add(vInfUnidCargaArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vLacUnidTransp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setLacUnidTransp(
            final int index,
            final com.mercurio.lms.cte.model.v200.LacUnidTransp vLacUnidTransp)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._lacUnidTranspList.size()) {
            throw new IndexOutOfBoundsException("setLacUnidTransp: Index value '" + index + "' not in range [0.." + (this._lacUnidTranspList.size() - 1) + "]");
        }

        this._lacUnidTranspList.set(index, vLacUnidTransp);
    }

    /**
     * 
     * 
     * @param vLacUnidTranspArray
     */
    public void setLacUnidTransp(
            final com.mercurio.lms.cte.model.v200.LacUnidTransp[] vLacUnidTranspArray) {
        //-- copy array
        _lacUnidTranspList.clear();

        for (int i = 0; i < vLacUnidTranspArray.length; i++) {
                this._lacUnidTranspList.add(vLacUnidTranspArray[i]);
        }
    }

    /**
     * Sets the value of field 'qtdRat'. The field 'qtdRat' has the
     * following description: Quantidade rateada (Peso,Volume)
     * 
     * @param qtdRat the value of field 'qtdRat'.
     */
    public void setQtdRat(
            final java.lang.String qtdRat) {
        this._qtdRat = qtdRat;
    }

    /**
     * Sets the value of field 'tpUnidTransp'. The field
     * 'tpUnidTransp' has the following description: Tipo da
     * Unidade de Transporte
     * 
     * @param tpUnidTransp the value of field 'tpUnidTransp'.
     */
    public void setTpUnidTransp(
            final com.mercurio.lms.cte.model.v200.types.TtipoUnidTransp tpUnidTransp) {
        this._tpUnidTransp = tpUnidTransp;
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
     * com.mercurio.lms.cte.model.v200.TUnidadeTransp
     */
    public static com.mercurio.lms.cte.model.v200.TUnidadeTransp unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v200.TUnidadeTransp) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v200.TUnidadeTransp.class, reader);
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
