/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Tipo Dados Unidade de Carga
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TUnidCarga implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Tipo da Unidade de Carga
     */
    private com.mercurio.lms.cte.model.v400.types.TtipoUnidCarga _tpUnidCarga;

    /**
     * Identificação da Unidade de Carga
     */
    private String _idUnidCarga;

    /**
     * Lacres das Unidades de Carga
     */
    private java.util.Vector<LacUnidCarga> _lacUnidCargaList;

    /**
     * Quantidade rateada (Peso,Volume)
     */
    private String _qtdRat;


      //----------------/
     //- Constructors -/
    //----------------/

    public TUnidCarga() {
        super();
        this._lacUnidCargaList = new java.util.Vector<LacUnidCarga>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     *
     *
     * @param vLacUnidCarga
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacUnidCarga(
            final LacUnidCarga vLacUnidCarga)
    throws IndexOutOfBoundsException {
        this._lacUnidCargaList.addElement(vLacUnidCarga);
    }

    /**
     *
     *
     * @param index
     * @param vLacUnidCarga
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacUnidCarga(
            final int index,
            final LacUnidCarga vLacUnidCarga)
    throws IndexOutOfBoundsException {
        this._lacUnidCargaList.add(index, vLacUnidCarga);
    }

    /**
     * Method enumerateLacUnidCarga.
     *
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v400.LacUnidCarga elements
     */
    public java.util.Enumeration<? extends LacUnidCarga> enumerateLacUnidCarga(
    ) {
        return this._lacUnidCargaList.elements();
    }

    /**
     * Returns the value of field 'idUnidCarga'. The field
     * 'idUnidCarga' has the following description: Identificação
     * da Unidade de Carga
     *
     * @return the value of field 'IdUnidCarga'.
     */
    public String getIdUnidCarga(
    ) {
        return this._idUnidCarga;
    }

    /**
     * Method getLacUnidCarga.
     *
     * @param index
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v400.LacUnidCarga at the given
     * index
     */
    public LacUnidCarga getLacUnidCarga(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._lacUnidCargaList.size()) {
            throw new IndexOutOfBoundsException("getLacUnidCarga: Index value '" + index + "' not in range [0.." + (this._lacUnidCargaList.size() - 1) + "]");
        }

        return (LacUnidCarga) _lacUnidCargaList.get(index);
    }

    /**
     * Method getLacUnidCarga.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call.
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     *
     * @return this collection as an Array
     */
    public LacUnidCarga[] getLacUnidCarga(
    ) {
        LacUnidCarga[] array = new LacUnidCarga[0];
        return (LacUnidCarga[]) this._lacUnidCargaList.toArray(array);
    }

    /**
     * Method getLacUnidCargaCount.
     *
     * @return the size of this collection
     */
    public int getLacUnidCargaCount(
    ) {
        return this._lacUnidCargaList.size();
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
     * Returns the value of field 'tpUnidCarga'. The field
     * 'tpUnidCarga' has the following description: Tipo da Unidade
     * de Carga
     *
     * @return the value of field 'TpUnidCarga'.
     */
    public com.mercurio.lms.cte.model.v400.types.TtipoUnidCarga getTpUnidCarga(
    ) {
        return this._tpUnidCarga;
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
    public void removeAllLacUnidCarga(
    ) {
        this._lacUnidCargaList.clear();
    }

    /**
     * Method removeLacUnidCarga.
     *
     * @param vLacUnidCarga
     * @return true if the object was removed from the collection.
     */
    public boolean removeLacUnidCarga(
            final LacUnidCarga vLacUnidCarga) {
        boolean removed = _lacUnidCargaList.remove(vLacUnidCarga);
        return removed;
    }

    /**
     * Method removeLacUnidCargaAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public LacUnidCarga removeLacUnidCargaAt(
            final int index) {
        Object obj = this._lacUnidCargaList.remove(index);
        return (LacUnidCarga) obj;
    }

    /**
     * Sets the value of field 'idUnidCarga'. The field
     * 'idUnidCarga' has the following description: Identificação
     * da Unidade de Carga
     *
     * @param idUnidCarga the value of field 'idUnidCarga'.
     */
    public void setIdUnidCarga(
            final String idUnidCarga) {
        this._idUnidCarga = idUnidCarga;
    }

    /**
     *
     *
     * @param index
     * @param vLacUnidCarga
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setLacUnidCarga(
            final int index,
            final LacUnidCarga vLacUnidCarga)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._lacUnidCargaList.size()) {
            throw new IndexOutOfBoundsException("setLacUnidCarga: Index value '" + index + "' not in range [0.." + (this._lacUnidCargaList.size() - 1) + "]");
        }

        this._lacUnidCargaList.set(index, vLacUnidCarga);
    }

    /**
     *
     *
     * @param vLacUnidCargaArray
     */
    public void setLacUnidCarga(
            final LacUnidCarga[] vLacUnidCargaArray) {
        //-- copy array
        _lacUnidCargaList.clear();

        for (int i = 0; i < vLacUnidCargaArray.length; i++) {
                this._lacUnidCargaList.add(vLacUnidCargaArray[i]);
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
     * Sets the value of field 'tpUnidCarga'. The field
     * 'tpUnidCarga' has the following description: Tipo da Unidade
     * de Carga
     * 
     * @param tpUnidCarga the value of field 'tpUnidCarga'.
     */
    public void setTpUnidCarga(
            final com.mercurio.lms.cte.model.v400.types.TtipoUnidCarga tpUnidCarga) {
        this._tpUnidCarga = tpUnidCarga;
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
     * com.mercurio.lms.cte.model.v400.TUnidCarga
     */
    public static TUnidCarga unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (TUnidCarga) org.exolab.castor.xml.Unmarshaller.unmarshal(TUnidCarga.class, reader);
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
