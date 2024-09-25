/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v200;

/**
 * Informações do modal Rodoviário
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Rodo implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Registro Nacional de Transportadores Rodoviários de Carga
     */
    private java.lang.String _RNTRC;

    /**
     * Data prevista para entrega da carga no Recebedor
     */
    private java.lang.String _dPrev;

    /**
     * Indicador de Lotação
     */
    private com.mercurio.lms.cte.model.v200.types.LotaType _lota;

    /**
     * Código Identificador da Operação de Transporte
     */
    private java.lang.String _CIOT;

    /**
     * Ordens de Coleta associados
     */
    private java.util.Vector<com.mercurio.lms.cte.model.v200.Occ> _occList;

    /**
     * Informações de Vale Pedágio
     */
    private java.util.Vector<com.mercurio.lms.cte.model.v200.ValePed> _valePedList;

    /**
     * Dados dos Veículos
     */
    private java.util.Vector<com.mercurio.lms.cte.model.v200.Veic> _veicList;

    /**
     * Lacres
     */
    private java.util.Vector<com.mercurio.lms.cte.model.v200.LacRodo> _lacRodoList;

    /**
     * Informações do(s) Motorista(s)
     */
    private java.util.Vector<com.mercurio.lms.cte.model.v200.Moto> _motoList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Rodo() {
        super();
        this._occList = new java.util.Vector<com.mercurio.lms.cte.model.v200.Occ>();
        this._valePedList = new java.util.Vector<com.mercurio.lms.cte.model.v200.ValePed>();
        this._veicList = new java.util.Vector<com.mercurio.lms.cte.model.v200.Veic>();
        this._lacRodoList = new java.util.Vector<com.mercurio.lms.cte.model.v200.LacRodo>();
        this._motoList = new java.util.Vector<com.mercurio.lms.cte.model.v200.Moto>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vLacRodo
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacRodo(
            final com.mercurio.lms.cte.model.v200.LacRodo vLacRodo)
    throws java.lang.IndexOutOfBoundsException {
        this._lacRodoList.addElement(vLacRodo);
    }

    /**
     * 
     * 
     * @param index
     * @param vLacRodo
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacRodo(
            final int index,
            final com.mercurio.lms.cte.model.v200.LacRodo vLacRodo)
    throws java.lang.IndexOutOfBoundsException {
        this._lacRodoList.add(index, vLacRodo);
    }

    /**
     * 
     * 
     * @param vMoto
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addMoto(
            final com.mercurio.lms.cte.model.v200.Moto vMoto)
    throws java.lang.IndexOutOfBoundsException {
        this._motoList.addElement(vMoto);
    }

    /**
     * 
     * 
     * @param index
     * @param vMoto
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addMoto(
            final int index,
            final com.mercurio.lms.cte.model.v200.Moto vMoto)
    throws java.lang.IndexOutOfBoundsException {
        this._motoList.add(index, vMoto);
    }

    /**
     * 
     * 
     * @param vOcc
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addOcc(
            final com.mercurio.lms.cte.model.v200.Occ vOcc)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._occList.size() >= 10) {
            throw new IndexOutOfBoundsException("addOcc has a maximum of 10");
        }

        this._occList.addElement(vOcc);
    }

    /**
     * 
     * 
     * @param index
     * @param vOcc
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addOcc(
            final int index,
            final com.mercurio.lms.cte.model.v200.Occ vOcc)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._occList.size() >= 10) {
            throw new IndexOutOfBoundsException("addOcc has a maximum of 10");
        }

        this._occList.add(index, vOcc);
    }

    /**
     * 
     * 
     * @param vValePed
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addValePed(
            final com.mercurio.lms.cte.model.v200.ValePed vValePed)
    throws java.lang.IndexOutOfBoundsException {
        this._valePedList.addElement(vValePed);
    }

    /**
     * 
     * 
     * @param index
     * @param vValePed
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addValePed(
            final int index,
            final com.mercurio.lms.cte.model.v200.ValePed vValePed)
    throws java.lang.IndexOutOfBoundsException {
        this._valePedList.add(index, vValePed);
    }

    /**
     * 
     * 
     * @param vVeic
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addVeic(
            final com.mercurio.lms.cte.model.v200.Veic vVeic)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._veicList.size() >= 4) {
            throw new IndexOutOfBoundsException("addVeic has a maximum of 4");
        }

        this._veicList.addElement(vVeic);
    }

    /**
     * 
     * 
     * @param index
     * @param vVeic
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addVeic(
            final int index,
            final com.mercurio.lms.cte.model.v200.Veic vVeic)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._veicList.size() >= 4) {
            throw new IndexOutOfBoundsException("addVeic has a maximum of 4");
        }

        this._veicList.add(index, vVeic);
    }

    /**
     * Method enumerateLacRodo.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v200.LacRodo elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v200.LacRodo> enumerateLacRodo(
    ) {
        return this._lacRodoList.elements();
    }

    /**
     * Method enumerateMoto.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v200.Moto elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v200.Moto> enumerateMoto(
    ) {
        return this._motoList.elements();
    }

    /**
     * Method enumerateOcc.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v200.Occ elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v200.Occ> enumerateOcc(
    ) {
        return this._occList.elements();
    }

    /**
     * Method enumerateValePed.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v200.ValePed elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v200.ValePed> enumerateValePed(
    ) {
        return this._valePedList.elements();
    }

    /**
     * Method enumerateVeic.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v200.Veic elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v200.Veic> enumerateVeic(
    ) {
        return this._veicList.elements();
    }

    /**
     * Returns the value of field 'CIOT'. The field 'CIOT' has the
     * following description: Código Identificador da Operação
     * de Transporte
     * 
     * @return the value of field 'CIOT'.
     */
    public java.lang.String getCIOT(
    ) {
        return this._CIOT;
    }

    /**
     * Returns the value of field 'dPrev'. The field 'dPrev' has
     * the following description: Data prevista para entrega da
     * carga no Recebedor
     * 
     * @return the value of field 'DPrev'.
     */
    public java.lang.String getDPrev(
    ) {
        return this._dPrev;
    }

    /**
     * Method getLacRodo.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v200.LacRodo at the given index
     */
    public com.mercurio.lms.cte.model.v200.LacRodo getLacRodo(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._lacRodoList.size()) {
            throw new IndexOutOfBoundsException("getLacRodo: Index value '" + index + "' not in range [0.." + (this._lacRodoList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v200.LacRodo) _lacRodoList.get(index);
    }

    /**
     * Method getLacRodo.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v200.LacRodo[] getLacRodo(
    ) {
        com.mercurio.lms.cte.model.v200.LacRodo[] array = new com.mercurio.lms.cte.model.v200.LacRodo[0];
        return (com.mercurio.lms.cte.model.v200.LacRodo[]) this._lacRodoList.toArray(array);
    }

    /**
     * Method getLacRodoCount.
     * 
     * @return the size of this collection
     */
    public int getLacRodoCount(
    ) {
        return this._lacRodoList.size();
    }

    /**
     * Returns the value of field 'lota'. The field 'lota' has the
     * following description: Indicador de Lotação
     * 
     * 
     * @return the value of field 'Lota'.
     */
    public com.mercurio.lms.cte.model.v200.types.LotaType getLota(
    ) {
        return this._lota;
    }

    /**
     * Method getMoto.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v200.Moto at the given index
     */
    public com.mercurio.lms.cte.model.v200.Moto getMoto(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._motoList.size()) {
            throw new IndexOutOfBoundsException("getMoto: Index value '" + index + "' not in range [0.." + (this._motoList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v200.Moto) _motoList.get(index);
    }

    /**
     * Method getMoto.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v200.Moto[] getMoto(
    ) {
        com.mercurio.lms.cte.model.v200.Moto[] array = new com.mercurio.lms.cte.model.v200.Moto[0];
        return (com.mercurio.lms.cte.model.v200.Moto[]) this._motoList.toArray(array);
    }

    /**
     * Method getMotoCount.
     * 
     * @return the size of this collection
     */
    public int getMotoCount(
    ) {
        return this._motoList.size();
    }

    /**
     * Method getOcc.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.mercurio.lms.cte.model.v200.Occ
     * at the given index
     */
    public com.mercurio.lms.cte.model.v200.Occ getOcc(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._occList.size()) {
            throw new IndexOutOfBoundsException("getOcc: Index value '" + index + "' not in range [0.." + (this._occList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v200.Occ) _occList.get(index);
    }

    /**
     * Method getOcc.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v200.Occ[] getOcc(
    ) {
        com.mercurio.lms.cte.model.v200.Occ[] array = new com.mercurio.lms.cte.model.v200.Occ[0];
        return (com.mercurio.lms.cte.model.v200.Occ[]) this._occList.toArray(array);
    }

    /**
     * Method getOccCount.
     * 
     * @return the size of this collection
     */
    public int getOccCount(
    ) {
        return this._occList.size();
    }

    /**
     * Returns the value of field 'RNTRC'. The field 'RNTRC' has
     * the following description: Registro Nacional de
     * Transportadores Rodoviários de Carga
     * 
     * @return the value of field 'RNTRC'.
     */
    public java.lang.String getRNTRC(
    ) {
        return this._RNTRC;
    }

    /**
     * Method getValePed.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v200.ValePed at the given index
     */
    public com.mercurio.lms.cte.model.v200.ValePed getValePed(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._valePedList.size()) {
            throw new IndexOutOfBoundsException("getValePed: Index value '" + index + "' not in range [0.." + (this._valePedList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v200.ValePed) _valePedList.get(index);
    }

    /**
     * Method getValePed.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v200.ValePed[] getValePed(
    ) {
        com.mercurio.lms.cte.model.v200.ValePed[] array = new com.mercurio.lms.cte.model.v200.ValePed[0];
        return (com.mercurio.lms.cte.model.v200.ValePed[]) this._valePedList.toArray(array);
    }

    /**
     * Method getValePedCount.
     * 
     * @return the size of this collection
     */
    public int getValePedCount(
    ) {
        return this._valePedList.size();
    }

    /**
     * Method getVeic.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v200.Veic at the given index
     */
    public com.mercurio.lms.cte.model.v200.Veic getVeic(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._veicList.size()) {
            throw new IndexOutOfBoundsException("getVeic: Index value '" + index + "' not in range [0.." + (this._veicList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v200.Veic) _veicList.get(index);
    }

    /**
     * Method getVeic.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v200.Veic[] getVeic(
    ) {
        com.mercurio.lms.cte.model.v200.Veic[] array = new com.mercurio.lms.cte.model.v200.Veic[0];
        return (com.mercurio.lms.cte.model.v200.Veic[]) this._veicList.toArray(array);
    }

    /**
     * Method getVeicCount.
     * 
     * @return the size of this collection
     */
    public int getVeicCount(
    ) {
        return this._veicList.size();
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
    public void removeAllLacRodo(
    ) {
        this._lacRodoList.clear();
    }

    /**
     */
    public void removeAllMoto(
    ) {
        this._motoList.clear();
    }

    /**
     */
    public void removeAllOcc(
    ) {
        this._occList.clear();
    }

    /**
     */
    public void removeAllValePed(
    ) {
        this._valePedList.clear();
    }

    /**
     */
    public void removeAllVeic(
    ) {
        this._veicList.clear();
    }

    /**
     * Method removeLacRodo.
     * 
     * @param vLacRodo
     * @return true if the object was removed from the collection.
     */
    public boolean removeLacRodo(
            final com.mercurio.lms.cte.model.v200.LacRodo vLacRodo) {
        boolean removed = _lacRodoList.remove(vLacRodo);
        return removed;
    }

    /**
     * Method removeLacRodoAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v200.LacRodo removeLacRodoAt(
            final int index) {
        java.lang.Object obj = this._lacRodoList.remove(index);
        return (com.mercurio.lms.cte.model.v200.LacRodo) obj;
    }

    /**
     * Method removeMoto.
     * 
     * @param vMoto
     * @return true if the object was removed from the collection.
     */
    public boolean removeMoto(
            final com.mercurio.lms.cte.model.v200.Moto vMoto) {
        boolean removed = _motoList.remove(vMoto);
        return removed;
    }

    /**
     * Method removeMotoAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v200.Moto removeMotoAt(
            final int index) {
        java.lang.Object obj = this._motoList.remove(index);
        return (com.mercurio.lms.cte.model.v200.Moto) obj;
    }

    /**
     * Method removeOcc.
     * 
     * @param vOcc
     * @return true if the object was removed from the collection.
     */
    public boolean removeOcc(
            final com.mercurio.lms.cte.model.v200.Occ vOcc) {
        boolean removed = _occList.remove(vOcc);
        return removed;
    }

    /**
     * Method removeOccAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v200.Occ removeOccAt(
            final int index) {
        java.lang.Object obj = this._occList.remove(index);
        return (com.mercurio.lms.cte.model.v200.Occ) obj;
    }

    /**
     * Method removeValePed.
     * 
     * @param vValePed
     * @return true if the object was removed from the collection.
     */
    public boolean removeValePed(
            final com.mercurio.lms.cte.model.v200.ValePed vValePed) {
        boolean removed = _valePedList.remove(vValePed);
        return removed;
    }

    /**
     * Method removeValePedAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v200.ValePed removeValePedAt(
            final int index) {
        java.lang.Object obj = this._valePedList.remove(index);
        return (com.mercurio.lms.cte.model.v200.ValePed) obj;
    }

    /**
     * Method removeVeic.
     * 
     * @param vVeic
     * @return true if the object was removed from the collection.
     */
    public boolean removeVeic(
            final com.mercurio.lms.cte.model.v200.Veic vVeic) {
        boolean removed = _veicList.remove(vVeic);
        return removed;
    }

    /**
     * Method removeVeicAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v200.Veic removeVeicAt(
            final int index) {
        java.lang.Object obj = this._veicList.remove(index);
        return (com.mercurio.lms.cte.model.v200.Veic) obj;
    }

    /**
     * Sets the value of field 'CIOT'. The field 'CIOT' has the
     * following description: Código Identificador da Operação
     * de Transporte
     * 
     * @param CIOT the value of field 'CIOT'.
     */
    public void setCIOT(
            final java.lang.String CIOT) {
        this._CIOT = CIOT;
    }

    /**
     * Sets the value of field 'dPrev'. The field 'dPrev' has the
     * following description: Data prevista para entrega da carga
     * no Recebedor
     * 
     * @param dPrev the value of field 'dPrev'.
     */
    public void setDPrev(
            final java.lang.String dPrev) {
        this._dPrev = dPrev;
    }

    /**
     * 
     * 
     * @param index
     * @param vLacRodo
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setLacRodo(
            final int index,
            final com.mercurio.lms.cte.model.v200.LacRodo vLacRodo)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._lacRodoList.size()) {
            throw new IndexOutOfBoundsException("setLacRodo: Index value '" + index + "' not in range [0.." + (this._lacRodoList.size() - 1) + "]");
        }

        this._lacRodoList.set(index, vLacRodo);
    }

    /**
     * 
     * 
     * @param vLacRodoArray
     */
    public void setLacRodo(
            final com.mercurio.lms.cte.model.v200.LacRodo[] vLacRodoArray) {
        //-- copy array
        _lacRodoList.clear();

        for (int i = 0; i < vLacRodoArray.length; i++) {
                this._lacRodoList.add(vLacRodoArray[i]);
        }
    }

    /**
     * Sets the value of field 'lota'. The field 'lota' has the
     * following description: Indicador de Lotação
     * 
     * 
     * @param lota the value of field 'lota'.
     */
    public void setLota(
            final com.mercurio.lms.cte.model.v200.types.LotaType lota) {
        this._lota = lota;
    }

    /**
     * 
     * 
     * @param index
     * @param vMoto
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setMoto(
            final int index,
            final com.mercurio.lms.cte.model.v200.Moto vMoto)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._motoList.size()) {
            throw new IndexOutOfBoundsException("setMoto: Index value '" + index + "' not in range [0.." + (this._motoList.size() - 1) + "]");
        }

        this._motoList.set(index, vMoto);
    }

    /**
     * 
     * 
     * @param vMotoArray
     */
    public void setMoto(
            final com.mercurio.lms.cte.model.v200.Moto[] vMotoArray) {
        //-- copy array
        _motoList.clear();

        for (int i = 0; i < vMotoArray.length; i++) {
                this._motoList.add(vMotoArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vOcc
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setOcc(
            final int index,
            final com.mercurio.lms.cte.model.v200.Occ vOcc)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._occList.size()) {
            throw new IndexOutOfBoundsException("setOcc: Index value '" + index + "' not in range [0.." + (this._occList.size() - 1) + "]");
        }

        this._occList.set(index, vOcc);
    }

    /**
     * 
     * 
     * @param vOccArray
     */
    public void setOcc(
            final com.mercurio.lms.cte.model.v200.Occ[] vOccArray) {
        //-- copy array
        _occList.clear();

        for (int i = 0; i < vOccArray.length; i++) {
                this._occList.add(vOccArray[i]);
        }
    }

    /**
     * Sets the value of field 'RNTRC'. The field 'RNTRC' has the
     * following description: Registro Nacional de Transportadores
     * Rodoviários de Carga
     * 
     * @param RNTRC the value of field 'RNTRC'.
     */
    public void setRNTRC(
            final java.lang.String RNTRC) {
        this._RNTRC = RNTRC;
    }

    /**
     * 
     * 
     * @param index
     * @param vValePed
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setValePed(
            final int index,
            final com.mercurio.lms.cte.model.v200.ValePed vValePed)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._valePedList.size()) {
            throw new IndexOutOfBoundsException("setValePed: Index value '" + index + "' not in range [0.." + (this._valePedList.size() - 1) + "]");
        }

        this._valePedList.set(index, vValePed);
    }

    /**
     * 
     * 
     * @param vValePedArray
     */
    public void setValePed(
            final com.mercurio.lms.cte.model.v200.ValePed[] vValePedArray) {
        //-- copy array
        _valePedList.clear();

        for (int i = 0; i < vValePedArray.length; i++) {
                this._valePedList.add(vValePedArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vVeic
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setVeic(
            final int index,
            final com.mercurio.lms.cte.model.v200.Veic vVeic)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._veicList.size()) {
            throw new IndexOutOfBoundsException("setVeic: Index value '" + index + "' not in range [0.." + (this._veicList.size() - 1) + "]");
        }

        this._veicList.set(index, vVeic);
    }

    /**
     * 
     * 
     * @param vVeicArray
     */
    public void setVeic(
            final com.mercurio.lms.cte.model.v200.Veic[] vVeicArray) {
        //-- copy array
        _veicList.clear();

        for (int i = 0; i < vVeicArray.length; i++) {
                this._veicList.add(vVeicArray[i]);
        }
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v200.Rodo
     */
    public static com.mercurio.lms.cte.model.v200.Rodo unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v200.Rodo) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v200.Rodo.class, reader);
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
