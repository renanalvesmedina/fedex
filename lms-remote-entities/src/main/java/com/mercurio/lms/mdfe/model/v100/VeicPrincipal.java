/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100;

/**
 * Dados do Veículo com a Tração
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class VeicPrincipal implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Código interno do veículo 
     */
    private java.lang.String _cInt;

    /**
     * Placa do veículo 
     */
    private java.lang.String _placa;

    /**
     * Tara em KG
     */
    private java.lang.String _tara;

    /**
     * Capacidade em KG
     */
    private java.lang.String _capKG;

    /**
     * Capacidade em M3
     */
    private java.lang.String _capM3;

    /**
     * Proprietários do Veículo. Só preenchido quando o veículo
     * não pertencer à empresa emitente do MDF-e
     */
    private com.mercurio.lms.mdfe.model.v100.Prop _prop;

    /**
     * Informações do(s) Condutor(s) do veículo
     */
    private java.util.List<com.mercurio.lms.mdfe.model.v100.Condutor> _condutorList;


      //----------------/
     //- Constructors -/
    //----------------/

    public VeicPrincipal() {
        super();
        this._condutorList = new java.util.ArrayList<com.mercurio.lms.mdfe.model.v100.Condutor>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vCondutor
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCondutor(
            final com.mercurio.lms.mdfe.model.v100.Condutor vCondutor)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._condutorList.size() >= 10) {
            throw new IndexOutOfBoundsException("addCondutor has a maximum of 10");
        }

        this._condutorList.add(vCondutor);
    }

    /**
     * 
     * 
     * @param index
     * @param vCondutor
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCondutor(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.Condutor vCondutor)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._condutorList.size() >= 10) {
            throw new IndexOutOfBoundsException("addCondutor has a maximum of 10");
        }

        this._condutorList.add(index, vCondutor);
    }

    /**
     * Method enumerateCondutor.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v100.Condutor> enumerateCondutor(
    ) {
        return java.util.Collections.enumeration(this._condutorList);
    }

    /**
     * Returns the value of field 'cInt'. The field 'cInt' has the
     * following description: Código interno do veículo 
     * 
     * @return the value of field 'CInt'.
     */
    public java.lang.String getCInt(
    ) {
        return this._cInt;
    }

    /**
     * Returns the value of field 'capKG'. The field 'capKG' has
     * the following description: Capacidade em KG
     * 
     * @return the value of field 'CapKG'.
     */
    public java.lang.String getCapKG(
    ) {
        return this._capKG;
    }

    /**
     * Returns the value of field 'capM3'. The field 'capM3' has
     * the following description: Capacidade em M3
     * 
     * @return the value of field 'CapM3'.
     */
    public java.lang.String getCapM3(
    ) {
        return this._capM3;
    }

    /**
     * Method getCondutor.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.mdfe.model.v100.Condutor at the given index
     */
    public com.mercurio.lms.mdfe.model.v100.Condutor getCondutor(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._condutorList.size()) {
            throw new IndexOutOfBoundsException("getCondutor: Index value '" + index + "' not in range [0.." + (this._condutorList.size() - 1) + "]");
        }

        return (com.mercurio.lms.mdfe.model.v100.Condutor) _condutorList.get(index);
    }

    /**
     * Method getCondutor.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v100.Condutor[] getCondutor(
    ) {
        com.mercurio.lms.mdfe.model.v100.Condutor[] array = new com.mercurio.lms.mdfe.model.v100.Condutor[0];
        return (com.mercurio.lms.mdfe.model.v100.Condutor[]) this._condutorList.toArray(array);
    }

    /**
     * Method getCondutorCount.
     * 
     * @return the size of this collection
     */
    public int getCondutorCount(
    ) {
        return this._condutorList.size();
    }

    /**
     * Returns the value of field 'placa'. The field 'placa' has
     * the following description: Placa do veículo 
     * 
     * @return the value of field 'Placa'.
     */
    public java.lang.String getPlaca(
    ) {
        return this._placa;
    }

    /**
     * Returns the value of field 'prop'. The field 'prop' has the
     * following description: Proprietários do Veículo. Só
     * preenchido quando o veículo não pertencer à empresa
     * emitente do MDF-e
     * 
     * @return the value of field 'Prop'.
     */
    public com.mercurio.lms.mdfe.model.v100.Prop getProp(
    ) {
        return this._prop;
    }

    /**
     * Returns the value of field 'tara'. The field 'tara' has the
     * following description: Tara em KG
     * 
     * @return the value of field 'Tara'.
     */
    public java.lang.String getTara(
    ) {
        return this._tara;
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
     * Method iterateCondutor.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.mdfe.model.v100.Condutor> iterateCondutor(
    ) {
        return this._condutorList.iterator();
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
    public void removeAllCondutor(
    ) {
        this._condutorList.clear();
    }

    /**
     * Method removeCondutor.
     * 
     * @param vCondutor
     * @return true if the object was removed from the collection.
     */
    public boolean removeCondutor(
            final com.mercurio.lms.mdfe.model.v100.Condutor vCondutor) {
        boolean removed = _condutorList.remove(vCondutor);
        return removed;
    }

    /**
     * Method removeCondutorAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v100.Condutor removeCondutorAt(
            final int index) {
        java.lang.Object obj = this._condutorList.remove(index);
        return (com.mercurio.lms.mdfe.model.v100.Condutor) obj;
    }

    /**
     * Sets the value of field 'cInt'. The field 'cInt' has the
     * following description: Código interno do veículo 
     * 
     * @param cInt the value of field 'cInt'.
     */
    public void setCInt(
            final java.lang.String cInt) {
        this._cInt = cInt;
    }

    /**
     * Sets the value of field 'capKG'. The field 'capKG' has the
     * following description: Capacidade em KG
     * 
     * @param capKG the value of field 'capKG'.
     */
    public void setCapKG(
            final java.lang.String capKG) {
        this._capKG = capKG;
    }

    /**
     * Sets the value of field 'capM3'. The field 'capM3' has the
     * following description: Capacidade em M3
     * 
     * @param capM3 the value of field 'capM3'.
     */
    public void setCapM3(
            final java.lang.String capM3) {
        this._capM3 = capM3;
    }

    /**
     * 
     * 
     * @param index
     * @param vCondutor
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setCondutor(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.Condutor vCondutor)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._condutorList.size()) {
            throw new IndexOutOfBoundsException("setCondutor: Index value '" + index + "' not in range [0.." + (this._condutorList.size() - 1) + "]");
        }

        this._condutorList.set(index, vCondutor);
    }

    /**
     * 
     * 
     * @param vCondutorArray
     */
    public void setCondutor(
            final com.mercurio.lms.mdfe.model.v100.Condutor[] vCondutorArray) {
        //-- copy array
        _condutorList.clear();

        for (int i = 0; i < vCondutorArray.length; i++) {
                this._condutorList.add(vCondutorArray[i]);
        }
    }

    /**
     * Sets the value of field 'placa'. The field 'placa' has the
     * following description: Placa do veículo 
     * 
     * @param placa the value of field 'placa'.
     */
    public void setPlaca(
            final java.lang.String placa) {
        this._placa = placa;
    }

    /**
     * Sets the value of field 'prop'. The field 'prop' has the
     * following description: Proprietários do Veículo. Só
     * preenchido quando o veículo não pertencer à empresa
     * emitente do MDF-e
     * 
     * @param prop the value of field 'prop'.
     */
    public void setProp(
            final com.mercurio.lms.mdfe.model.v100.Prop prop) {
        this._prop = prop;
    }

    /**
     * Sets the value of field 'tara'. The field 'tara' has the
     * following description: Tara em KG
     * 
     * @param tara the value of field 'tara'.
     */
    public void setTara(
            final java.lang.String tara) {
        this._tara = tara;
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
     * com.mercurio.lms.mdfe.model.v100.VeicPrincipal
     */
    public static com.mercurio.lms.mdfe.model.v100.VeicPrincipal unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100.VeicPrincipal) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100.VeicPrincipal.class, reader);
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
