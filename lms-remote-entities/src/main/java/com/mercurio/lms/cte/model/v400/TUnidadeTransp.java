/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

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
    private com.mercurio.lms.cte.model.v400.types.TtipoUnidTransp _tpUnidTransp;

    /**
     * Identificação da Unidade de Transporte
     */
    private String _idUnidTransp;

    /**
     * Lacres das Unidades de Transporte
     */
    private java.util.Vector<LacUnidTransp> _lacUnidTranspList;

    /**
     * Informações das Unidades de Carga (Containeres/ULD/Outros)
     */
    private java.util.Vector<InfUnidCarga> _infUnidCargaList;

    /**
     * Quantidade rateada (Peso,Volume)
     */
    private String _qtdRat;


      //----------------/
     //- Constructors -/
    //----------------/

    public TUnidadeTransp() {
        super();
        this._lacUnidTranspList = new java.util.Vector<LacUnidTransp>();
        this._infUnidCargaList = new java.util.Vector<InfUnidCarga>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     *
     *
     * @param vInfUnidCarga
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfUnidCarga(
            final InfUnidCarga vInfUnidCarga)
    throws IndexOutOfBoundsException {
        this._infUnidCargaList.addElement(vInfUnidCarga);
    }

    /**
     *
     *
     * @param index
     * @param vInfUnidCarga
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfUnidCarga(
            final int index,
            final InfUnidCarga vInfUnidCarga)
    throws IndexOutOfBoundsException {
        this._infUnidCargaList.add(index, vInfUnidCarga);
    }

    /**
     *
     *
     * @param vLacUnidTransp
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacUnidTransp(
            final LacUnidTransp vLacUnidTransp)
    throws IndexOutOfBoundsException {
        this._lacUnidTranspList.addElement(vLacUnidTransp);
    }

    /**
     *
     *
     * @param index
     * @param vLacUnidTransp
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacUnidTransp(
            final int index,
            final LacUnidTransp vLacUnidTransp)
    throws IndexOutOfBoundsException {
        this._lacUnidTranspList.add(index, vLacUnidTransp);
    }

    /**
     * Method enumerateInfUnidCarga.
     *
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v400.InfUnidCarga elements
     */
    public java.util.Enumeration<? extends InfUnidCarga> enumerateInfUnidCarga(
    ) {
        return this._infUnidCargaList.elements();
    }

    /**
     * Method enumerateLacUnidTransp.
     *
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v400.LacUnidTransp elements
     */
    public java.util.Enumeration<? extends LacUnidTransp> enumerateLacUnidTransp(
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
    public String getIdUnidTransp(
    ) {
        return this._idUnidTransp;
    }

    /**
     * Method getInfUnidCarga.
     *
     * @param index
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v400.InfUnidCarga at the given
     * index
     */
    public InfUnidCarga getInfUnidCarga(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infUnidCargaList.size()) {
            throw new IndexOutOfBoundsException("getInfUnidCarga: Index value '" + index + "' not in range [0.." + (this._infUnidCargaList.size() - 1) + "]");
        }

        return (InfUnidCarga) _infUnidCargaList.get(index);
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
    public InfUnidCarga[] getInfUnidCarga(
    ) {
        InfUnidCarga[] array = new InfUnidCarga[0];
        return (InfUnidCarga[]) this._infUnidCargaList.toArray(array);
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
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v400.LacUnidTransp at the given
     * index
     */
    public LacUnidTransp getLacUnidTransp(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._lacUnidTranspList.size()) {
            throw new IndexOutOfBoundsException("getLacUnidTransp: Index value '" + index + "' not in range [0.." + (this._lacUnidTranspList.size() - 1) + "]");
        }

        return (LacUnidTransp) _lacUnidTranspList.get(index);
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
    public LacUnidTransp[] getLacUnidTransp(
    ) {
        LacUnidTransp[] array = new LacUnidTransp[0];
        return (LacUnidTransp[]) this._lacUnidTranspList.toArray(array);
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
    public String getQtdRat(
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
    public com.mercurio.lms.cte.model.v400.types.TtipoUnidTransp getTpUnidTransp(
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
            final InfUnidCarga vInfUnidCarga) {
        boolean removed = _infUnidCargaList.remove(vInfUnidCarga);
        return removed;
    }

    /**
     * Method removeInfUnidCargaAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public InfUnidCarga removeInfUnidCargaAt(
            final int index) {
        Object obj = this._infUnidCargaList.remove(index);
        return (InfUnidCarga) obj;
    }

    /**
     * Method removeLacUnidTransp.
     *
     * @param vLacUnidTransp
     * @return true if the object was removed from the collection.
     */
    public boolean removeLacUnidTransp(
            final LacUnidTransp vLacUnidTransp) {
        boolean removed = _lacUnidTranspList.remove(vLacUnidTransp);
        return removed;
    }

    /**
     * Method removeLacUnidTranspAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public LacUnidTransp removeLacUnidTranspAt(
            final int index) {
        Object obj = this._lacUnidTranspList.remove(index);
        return (LacUnidTransp) obj;
    }

    /**
     * Sets the value of field 'idUnidTransp'. The field
     * 'idUnidTransp' has the following description:
     * Identificação da Unidade de Transporte
     *
     * @param idUnidTransp the value of field 'idUnidTransp'.
     */
    public void setIdUnidTransp(
            final String idUnidTransp) {
        this._idUnidTransp = idUnidTransp;
    }

    /**
     *
     *
     * @param index
     * @param vInfUnidCarga
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfUnidCarga(
            final int index,
            final InfUnidCarga vInfUnidCarga)
    throws IndexOutOfBoundsException {
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
            final InfUnidCarga[] vInfUnidCargaArray) {
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
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setLacUnidTransp(
            final int index,
            final LacUnidTransp vLacUnidTransp)
    throws IndexOutOfBoundsException {
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
            final LacUnidTransp[] vLacUnidTranspArray) {
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
            final String qtdRat) {
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
            final com.mercurio.lms.cte.model.v400.types.TtipoUnidTransp tpUnidTransp) {
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
     * com.mercurio.lms.cte.model.v400.TUnidadeTransp
     */
    public static TUnidadeTransp unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (TUnidadeTransp) org.exolab.castor.xml.Unmarshaller.unmarshal(TUnidadeTransp.class, reader);
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
