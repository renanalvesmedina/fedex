/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v200;

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
    private com.mercurio.lms.cte.model.v200.types.TtipoUnidCarga _tpUnidCarga;

    /**
     * Identificação da Unidade de Carga
     */
    private java.lang.String _idUnidCarga;

    /**
     * Lacres das Unidades de Carga
     */
    private java.util.Vector<com.mercurio.lms.cte.model.v200.LacUnidCarga> _lacUnidCargaList;

    /**
     * Quantidade rateada (Peso,Volume)
     */
    private java.lang.String _qtdRat;


      //----------------/
     //- Constructors -/
    //----------------/

    public TUnidCarga() {
        super();
        this._lacUnidCargaList = new java.util.Vector<com.mercurio.lms.cte.model.v200.LacUnidCarga>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vLacUnidCarga
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacUnidCarga(
            final com.mercurio.lms.cte.model.v200.LacUnidCarga vLacUnidCarga)
    throws java.lang.IndexOutOfBoundsException {
        this._lacUnidCargaList.addElement(vLacUnidCarga);
    }

    /**
     * 
     * 
     * @param index
     * @param vLacUnidCarga
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacUnidCarga(
            final int index,
            final com.mercurio.lms.cte.model.v200.LacUnidCarga vLacUnidCarga)
    throws java.lang.IndexOutOfBoundsException {
        this._lacUnidCargaList.add(index, vLacUnidCarga);
    }

    /**
     * Method enumerateLacUnidCarga.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v200.LacUnidCarga elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v200.LacUnidCarga> enumerateLacUnidCarga(
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
    public java.lang.String getIdUnidCarga(
    ) {
        return this._idUnidCarga;
    }

    /**
     * Method getLacUnidCarga.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v200.LacUnidCarga at the given
     * index
     */
    public com.mercurio.lms.cte.model.v200.LacUnidCarga getLacUnidCarga(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._lacUnidCargaList.size()) {
            throw new IndexOutOfBoundsException("getLacUnidCarga: Index value '" + index + "' not in range [0.." + (this._lacUnidCargaList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v200.LacUnidCarga) _lacUnidCargaList.get(index);
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
    public com.mercurio.lms.cte.model.v200.LacUnidCarga[] getLacUnidCarga(
    ) {
        com.mercurio.lms.cte.model.v200.LacUnidCarga[] array = new com.mercurio.lms.cte.model.v200.LacUnidCarga[0];
        return (com.mercurio.lms.cte.model.v200.LacUnidCarga[]) this._lacUnidCargaList.toArray(array);
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
    public java.lang.String getQtdRat(
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
    public com.mercurio.lms.cte.model.v200.types.TtipoUnidCarga getTpUnidCarga(
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
            final com.mercurio.lms.cte.model.v200.LacUnidCarga vLacUnidCarga) {
        boolean removed = _lacUnidCargaList.remove(vLacUnidCarga);
        return removed;
    }

    /**
     * Method removeLacUnidCargaAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v200.LacUnidCarga removeLacUnidCargaAt(
            final int index) {
        java.lang.Object obj = this._lacUnidCargaList.remove(index);
        return (com.mercurio.lms.cte.model.v200.LacUnidCarga) obj;
    }

    /**
     * Sets the value of field 'idUnidCarga'. The field
     * 'idUnidCarga' has the following description: Identificação
     * da Unidade de Carga
     * 
     * @param idUnidCarga the value of field 'idUnidCarga'.
     */
    public void setIdUnidCarga(
            final java.lang.String idUnidCarga) {
        this._idUnidCarga = idUnidCarga;
    }

    /**
     * 
     * 
     * @param index
     * @param vLacUnidCarga
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setLacUnidCarga(
            final int index,
            final com.mercurio.lms.cte.model.v200.LacUnidCarga vLacUnidCarga)
    throws java.lang.IndexOutOfBoundsException {
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
            final com.mercurio.lms.cte.model.v200.LacUnidCarga[] vLacUnidCargaArray) {
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
            final java.lang.String qtdRat) {
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
            final com.mercurio.lms.cte.model.v200.types.TtipoUnidCarga tpUnidCarga) {
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
     * com.mercurio.lms.cte.model.v200.TUnidCarga
     */
    public static com.mercurio.lms.cte.model.v200.TUnidCarga unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v200.TUnidCarga) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v200.TUnidCarga.class, reader);
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
