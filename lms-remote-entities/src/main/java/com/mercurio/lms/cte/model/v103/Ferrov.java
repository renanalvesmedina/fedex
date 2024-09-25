/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Informações do modal Ferroviário
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Ferrov implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Tipo de Tráfego
     * 0-Próprio, 1-Mútuo, 2- rodoferroviário ou 3-rodoviário.
     */
    private com.mercurio.lms.cte.model.v103.types.TpTrafType _tpTraf;

    /**
     * Fluxo Ferroviário
     */
    private java.lang.String _fluxo;

    /**
     * Identificação do trem.
     */
    private java.lang.String _idTrem;

    /**
     * Valor do Frete
     */
    private java.lang.String _vFrete;

    /**
     * Informações da Ferrovia Substituída
     */
    private com.mercurio.lms.cte.model.v103.FerroSub _ferroSub;

    /**
     * Informações da DCL
     */
    private java.util.List<com.mercurio.lms.cte.model.v103.DCL> _DCLList;

    /**
     * informações de detalhes dos Vagões
     */
    private java.util.List<com.mercurio.lms.cte.model.v103.DetVag> _detVagList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Ferrov() {
        super();
        this._DCLList = new java.util.ArrayList<com.mercurio.lms.cte.model.v103.DCL>();
        this._detVagList = new java.util.ArrayList<com.mercurio.lms.cte.model.v103.DetVag>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vDCL
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDCL(
            final com.mercurio.lms.cte.model.v103.DCL vDCL)
    throws java.lang.IndexOutOfBoundsException {
        this._DCLList.add(vDCL);
    }

    /**
     * 
     * 
     * @param index
     * @param vDCL
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDCL(
            final int index,
            final com.mercurio.lms.cte.model.v103.DCL vDCL)
    throws java.lang.IndexOutOfBoundsException {
        this._DCLList.add(index, vDCL);
    }

    /**
     * 
     * 
     * @param vDetVag
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDetVag(
            final com.mercurio.lms.cte.model.v103.DetVag vDetVag)
    throws java.lang.IndexOutOfBoundsException {
        this._detVagList.add(vDetVag);
    }

    /**
     * 
     * 
     * @param index
     * @param vDetVag
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDetVag(
            final int index,
            final com.mercurio.lms.cte.model.v103.DetVag vDetVag)
    throws java.lang.IndexOutOfBoundsException {
        this._detVagList.add(index, vDetVag);
    }

    /**
     * Method enumerateDCL.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v103.DCL> enumerateDCL(
    ) {
        return java.util.Collections.enumeration(this._DCLList);
    }

    /**
     * Method enumerateDetVag.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v103.DetVag> enumerateDetVag(
    ) {
        return java.util.Collections.enumeration(this._detVagList);
    }

    /**
     * Method getDCL.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.mercurio.lms.cte.model.v103.DCL
     * at the given index
     */
    public com.mercurio.lms.cte.model.v103.DCL getDCL(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._DCLList.size()) {
            throw new IndexOutOfBoundsException("getDCL: Index value '" + index + "' not in range [0.." + (this._DCLList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v103.DCL) _DCLList.get(index);
    }

    /**
     * Method getDCL.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v103.DCL[] getDCL(
    ) {
        com.mercurio.lms.cte.model.v103.DCL[] array = new com.mercurio.lms.cte.model.v103.DCL[0];
        return (com.mercurio.lms.cte.model.v103.DCL[]) this._DCLList.toArray(array);
    }

    /**
     * Method getDCLCount.
     * 
     * @return the size of this collection
     */
    public int getDCLCount(
    ) {
        return this._DCLList.size();
    }

    /**
     * Method getDetVag.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v103.DetVag at the given index
     */
    public com.mercurio.lms.cte.model.v103.DetVag getDetVag(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._detVagList.size()) {
            throw new IndexOutOfBoundsException("getDetVag: Index value '" + index + "' not in range [0.." + (this._detVagList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v103.DetVag) _detVagList.get(index);
    }

    /**
     * Method getDetVag.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v103.DetVag[] getDetVag(
    ) {
        com.mercurio.lms.cte.model.v103.DetVag[] array = new com.mercurio.lms.cte.model.v103.DetVag[0];
        return (com.mercurio.lms.cte.model.v103.DetVag[]) this._detVagList.toArray(array);
    }

    /**
     * Method getDetVagCount.
     * 
     * @return the size of this collection
     */
    public int getDetVagCount(
    ) {
        return this._detVagList.size();
    }

    /**
     * Returns the value of field 'ferroSub'. The field 'ferroSub'
     * has the following description: Informações da Ferrovia
     * Substituída
     * 
     * @return the value of field 'FerroSub'.
     */
    public com.mercurio.lms.cte.model.v103.FerroSub getFerroSub(
    ) {
        return this._ferroSub;
    }

    /**
     * Returns the value of field 'fluxo'. The field 'fluxo' has
     * the following description: Fluxo Ferroviário
     * 
     * @return the value of field 'Fluxo'.
     */
    public java.lang.String getFluxo(
    ) {
        return this._fluxo;
    }

    /**
     * Returns the value of field 'idTrem'. The field 'idTrem' has
     * the following description: Identificação do trem.
     * 
     * @return the value of field 'IdTrem'.
     */
    public java.lang.String getIdTrem(
    ) {
        return this._idTrem;
    }

    /**
     * Returns the value of field 'tpTraf'. The field 'tpTraf' has
     * the following description: Tipo de Tráfego
     * 0-Próprio, 1-Mútuo, 2- rodoferroviário ou 3-rodoviário.
     * 
     * @return the value of field 'TpTraf'.
     */
    public com.mercurio.lms.cte.model.v103.types.TpTrafType getTpTraf(
    ) {
        return this._tpTraf;
    }

    /**
     * Returns the value of field 'vFrete'. The field 'vFrete' has
     * the following description: Valor do Frete
     * 
     * @return the value of field 'VFrete'.
     */
    public java.lang.String getVFrete(
    ) {
        return this._vFrete;
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
     * Method iterateDCL.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v103.DCL> iterateDCL(
    ) {
        return this._DCLList.iterator();
    }

    /**
     * Method iterateDetVag.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v103.DetVag> iterateDetVag(
    ) {
        return this._detVagList.iterator();
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
    public void removeAllDCL(
    ) {
        this._DCLList.clear();
    }

    /**
     */
    public void removeAllDetVag(
    ) {
        this._detVagList.clear();
    }

    /**
     * Method removeDCL.
     * 
     * @param vDCL
     * @return true if the object was removed from the collection.
     */
    public boolean removeDCL(
            final com.mercurio.lms.cte.model.v103.DCL vDCL) {
        boolean removed = _DCLList.remove(vDCL);
        return removed;
    }

    /**
     * Method removeDCLAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v103.DCL removeDCLAt(
            final int index) {
        java.lang.Object obj = this._DCLList.remove(index);
        return (com.mercurio.lms.cte.model.v103.DCL) obj;
    }

    /**
     * Method removeDetVag.
     * 
     * @param vDetVag
     * @return true if the object was removed from the collection.
     */
    public boolean removeDetVag(
            final com.mercurio.lms.cte.model.v103.DetVag vDetVag) {
        boolean removed = _detVagList.remove(vDetVag);
        return removed;
    }

    /**
     * Method removeDetVagAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v103.DetVag removeDetVagAt(
            final int index) {
        java.lang.Object obj = this._detVagList.remove(index);
        return (com.mercurio.lms.cte.model.v103.DetVag) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vDCL
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setDCL(
            final int index,
            final com.mercurio.lms.cte.model.v103.DCL vDCL)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._DCLList.size()) {
            throw new IndexOutOfBoundsException("setDCL: Index value '" + index + "' not in range [0.." + (this._DCLList.size() - 1) + "]");
        }

        this._DCLList.set(index, vDCL);
    }

    /**
     * 
     * 
     * @param vDCLArray
     */
    public void setDCL(
            final com.mercurio.lms.cte.model.v103.DCL[] vDCLArray) {
        //-- copy array
        _DCLList.clear();

        for (int i = 0; i < vDCLArray.length; i++) {
                this._DCLList.add(vDCLArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vDetVag
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setDetVag(
            final int index,
            final com.mercurio.lms.cte.model.v103.DetVag vDetVag)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._detVagList.size()) {
            throw new IndexOutOfBoundsException("setDetVag: Index value '" + index + "' not in range [0.." + (this._detVagList.size() - 1) + "]");
        }

        this._detVagList.set(index, vDetVag);
    }

    /**
     * 
     * 
     * @param vDetVagArray
     */
    public void setDetVag(
            final com.mercurio.lms.cte.model.v103.DetVag[] vDetVagArray) {
        //-- copy array
        _detVagList.clear();

        for (int i = 0; i < vDetVagArray.length; i++) {
                this._detVagList.add(vDetVagArray[i]);
        }
    }

    /**
     * Sets the value of field 'ferroSub'. The field 'ferroSub' has
     * the following description: Informações da Ferrovia
     * Substituída
     * 
     * @param ferroSub the value of field 'ferroSub'.
     */
    public void setFerroSub(
            final com.mercurio.lms.cte.model.v103.FerroSub ferroSub) {
        this._ferroSub = ferroSub;
    }

    /**
     * Sets the value of field 'fluxo'. The field 'fluxo' has the
     * following description: Fluxo Ferroviário
     * 
     * @param fluxo the value of field 'fluxo'.
     */
    public void setFluxo(
            final java.lang.String fluxo) {
        this._fluxo = fluxo;
    }

    /**
     * Sets the value of field 'idTrem'. The field 'idTrem' has the
     * following description: Identificação do trem.
     * 
     * @param idTrem the value of field 'idTrem'.
     */
    public void setIdTrem(
            final java.lang.String idTrem) {
        this._idTrem = idTrem;
    }

    /**
     * Sets the value of field 'tpTraf'. The field 'tpTraf' has the
     * following description: Tipo de Tráfego
     * 0-Próprio, 1-Mútuo, 2- rodoferroviário ou 3-rodoviário.
     * 
     * @param tpTraf the value of field 'tpTraf'.
     */
    public void setTpTraf(
            final com.mercurio.lms.cte.model.v103.types.TpTrafType tpTraf) {
        this._tpTraf = tpTraf;
    }

    /**
     * Sets the value of field 'vFrete'. The field 'vFrete' has the
     * following description: Valor do Frete
     * 
     * @param vFrete the value of field 'vFrete'.
     */
    public void setVFrete(
            final java.lang.String vFrete) {
        this._vFrete = vFrete;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v103.Ferro
     */
    public static com.mercurio.lms.cte.model.v103.Ferrov unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.Ferrov) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.Ferrov.class, reader);
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
