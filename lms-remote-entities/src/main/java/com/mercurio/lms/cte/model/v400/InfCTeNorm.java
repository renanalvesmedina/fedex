/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Grupo de informações do CT-e Normal e Substituto
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfCTeNorm implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Informações da Carga do CT-e
     */
    private InfCarga _infCarga;

    /**
     * Informações dos documentos transportados pelo CT-e
     * Opcional para Redespacho Intermediario e Serviço vinculado
     * a multimodal.
     */
    private InfDoc _infDoc;

    /**
     * Documentos de Transporte Anterior
     */
    private DocAnt _docAnt;

    /**
     * Informações de Seguro da Carga
     */
    private java.util.Vector<Seg> _segList;

    /**
     * Informações do modal
     */
    private InfModal _infModal;

    /**
     * Preenchido quando for transporte de produtos classificados
     * pela ONU como perigosos.
     */
    private java.util.Vector<Peri> _periList;

    /**
     * informações dos veículos transportados
     */
    private java.util.Vector<VeicNovos> _veicNovosList;

    /**
     * Dados da cobrança do CT-e
     */
    private Cobr _cobr;

    /**
     * Informações do CT-e de substituição 
     */
    private InfCteSub _infCteSub;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfCTeNorm() {
        super();
        this._segList = new java.util.Vector<Seg>();
        this._periList = new java.util.Vector<Peri>();
        this._veicNovosList = new java.util.Vector<VeicNovos>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vPeri
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addPeri(
            final Peri vPeri)
    throws IndexOutOfBoundsException {
        this._periList.addElement(vPeri);
    }

    /**
     *
     *
     * @param index
     * @param vPeri
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addPeri(
            final int index,
            final Peri vPeri)
    throws IndexOutOfBoundsException {
        this._periList.add(index, vPeri);
    }

    /**
     *
     *
     * @param vSeg
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSeg(
            final Seg vSeg)
    throws IndexOutOfBoundsException {
        this._segList.addElement(vSeg);
    }

    /**
     *
     *
     * @param index
     * @param vSeg
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSeg(
            final int index,
            final Seg vSeg)
    throws IndexOutOfBoundsException {
        this._segList.add(index, vSeg);
    }

    /**
     *
     *
     * @param vVeicNovos
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addVeicNovos(
            final VeicNovos vVeicNovos)
    throws IndexOutOfBoundsException {
        this._veicNovosList.addElement(vVeicNovos);
    }

    /**
     *
     *
     * @param index
     * @param vVeicNovos
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addVeicNovos(
            final int index,
            final VeicNovos vVeicNovos)
    throws IndexOutOfBoundsException {
        this._veicNovosList.add(index, vVeicNovos);
    }

    /**
     * Method enumeratePeri.
     *
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v400.Peri elements
     */
    public java.util.Enumeration<? extends Peri> enumeratePeri(
    ) {
        return this._periList.elements();
    }

    /**
     * Method enumerateSeg.
     *
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v400.Seg elements
     */
    public java.util.Enumeration<? extends Seg> enumerateSeg(
    ) {
        return this._segList.elements();
    }

    /**
     * Method enumerateVeicNovos.
     *
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v400.VeicNovos elements
     */
    public java.util.Enumeration<? extends VeicNovos> enumerateVeicNovos(
    ) {
        return this._veicNovosList.elements();
    }

    /**
     * Returns the value of field 'cobr'. The field 'cobr' has the
     * following description: Dados da cobrança do CT-e
     *
     * @return the value of field 'Cobr'.
     */
    public Cobr getCobr(
    ) {
        return this._cobr;
    }

    /**
     * Returns the value of field 'docAnt'. The field 'docAnt' has
     * the following description: Documentos de Transporte Anterior
     *
     * @return the value of field 'DocAnt'.
     */
    public DocAnt getDocAnt(
    ) {
        return this._docAnt;
    }

    /**
     * Returns the value of field 'infCarga'. The field 'infCarga'
     * has the following description: Informações da Carga do
     * CT-e
     *
     * @return the value of field 'InfCarga'.
     */
    public InfCarga getInfCarga(
    ) {
        return this._infCarga;
    }

    /**
     * Returns the value of field 'infCteSub'. The field
     * 'infCteSub' has the following description: Informações do
     * CT-e de substituição
     *
     * @return the value of field 'InfCteSub'.
     */
    public InfCteSub getInfCteSub(
    ) {
        return this._infCteSub;
    }

    /**
     * Returns the value of field 'infDoc'. The field 'infDoc' has
     * the following description: Informações dos documentos
     * transportados pelo CT-e
     * Opcional para Redespacho Intermediario e Serviço vinculado
     * a multimodal.
     *
     * @return the value of field 'InfDoc'.
     */
    public InfDoc getInfDoc(
    ) {
        return this._infDoc;
    }

    /**
     * Returns the value of field 'infModal'. The field 'infModal'
     * has the following description: Informações do modal
     *
     * @return the value of field 'InfModal'.
     */
    public InfModal getInfModal(
    ) {
        return this._infModal;
    }

    /**
     * Method getPeri.
     *
     * @param index
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v400.Peri at the given index
     */
    public Peri getPeri(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._periList.size()) {
            throw new IndexOutOfBoundsException("getPeri: Index value '" + index + "' not in range [0.." + (this._periList.size() - 1) + "]");
        }

        return (Peri) _periList.get(index);
    }

    /**
     * Method getPeri.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     *
     * @return this collection as an Array
     */
    public Peri[] getPeri(
    ) {
        Peri[] array = new Peri[0];
        return (Peri[]) this._periList.toArray(array);
    }

    /**
     * Method getPeriCount.
     *
     * @return the size of this collection
     */
    public int getPeriCount(
    ) {
        return this._periList.size();
    }

    /**
     * Method getSeg.
     *
     * @param index
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.mercurio.lms.cte.model.v400.Seg
     * at the given index
     */
    public Seg getSeg(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._segList.size()) {
            throw new IndexOutOfBoundsException("getSeg: Index value '" + index + "' not in range [0.." + (this._segList.size() - 1) + "]");
        }

        return (Seg) _segList.get(index);
    }

    /**
     * Method getSeg.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     *
     * @return this collection as an Array
     */
    public Seg[] getSeg(
    ) {
        Seg[] array = new Seg[0];
        return (Seg[]) this._segList.toArray(array);
    }

    /**
     * Method getSegCount.
     *
     * @return the size of this collection
     */
    public int getSegCount(
    ) {
        return this._segList.size();
    }

    /**
     * Method getVeicNovos.
     *
     * @param index
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v400.VeicNovos at the given index
     */
    public VeicNovos getVeicNovos(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._veicNovosList.size()) {
            throw new IndexOutOfBoundsException("getVeicNovos: Index value '" + index + "' not in range [0.." + (this._veicNovosList.size() - 1) + "]");
        }

        return (VeicNovos) _veicNovosList.get(index);
    }

    /**
     * Method getVeicNovos.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     *
     * @return this collection as an Array
     */
    public VeicNovos[] getVeicNovos(
    ) {
        VeicNovos[] array = new VeicNovos[0];
        return (VeicNovos[]) this._veicNovosList.toArray(array);
    }

    /**
     * Method getVeicNovosCount.
     *
     * @return the size of this collection
     */
    public int getVeicNovosCount(
    ) {
        return this._veicNovosList.size();
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
    public void removeAllPeri(
    ) {
        this._periList.clear();
    }

    /**
     */
    public void removeAllSeg(
    ) {
        this._segList.clear();
    }

    /**
     */
    public void removeAllVeicNovos(
    ) {
        this._veicNovosList.clear();
    }

    /**
     * Method removePeri.
     *
     * @param vPeri
     * @return true if the object was removed from the collection.
     */
    public boolean removePeri(
            final Peri vPeri) {
        boolean removed = _periList.remove(vPeri);
        return removed;
    }

    /**
     * Method removePeriAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public Peri removePeriAt(
            final int index) {
        Object obj = this._periList.remove(index);
        return (Peri) obj;
    }

    /**
     * Method removeSeg.
     *
     * @param vSeg
     * @return true if the object was removed from the collection.
     */
    public boolean removeSeg(
            final Seg vSeg) {
        boolean removed = _segList.remove(vSeg);
        return removed;
    }

    /**
     * Method removeSegAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public Seg removeSegAt(
            final int index) {
        Object obj = this._segList.remove(index);
        return (Seg) obj;
    }

    /**
     * Method removeVeicNovos.
     *
     * @param vVeicNovos
     * @return true if the object was removed from the collection.
     */
    public boolean removeVeicNovos(
            final VeicNovos vVeicNovos) {
        boolean removed = _veicNovosList.remove(vVeicNovos);
        return removed;
    }

    /**
     * Method removeVeicNovosAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public VeicNovos removeVeicNovosAt(
            final int index) {
        Object obj = this._veicNovosList.remove(index);
        return (VeicNovos) obj;
    }

    /**
     * Sets the value of field 'cobr'. The field 'cobr' has the
     * following description: Dados da cobrança do CT-e
     *
     * @param cobr the value of field 'cobr'.
     */
    public void setCobr(
            final Cobr cobr) {
        this._cobr = cobr;
    }

    /**
     * Sets the value of field 'docAnt'. The field 'docAnt' has the
     * following description: Documentos de Transporte Anterior
     *
     * @param docAnt the value of field 'docAnt'.
     */
    public void setDocAnt(
            final DocAnt docAnt) {
        this._docAnt = docAnt;
    }

    /**
     * Sets the value of field 'infCarga'. The field 'infCarga' has
     * the following description: Informações da Carga do CT-e
     *
     * @param infCarga the value of field 'infCarga'.
     */
    public void setInfCarga(
            final InfCarga infCarga) {
        this._infCarga = infCarga;
    }

    /**
     * Sets the value of field 'infCteSub'. The field 'infCteSub'
     * has the following description: Informações do CT-e de
     * substituição
     *
     * @param infCteSub the value of field 'infCteSub'.
     */
    public void setInfCteSub(
            final InfCteSub infCteSub) {
        this._infCteSub = infCteSub;
    }

    /**
     * Sets the value of field 'infDoc'. The field 'infDoc' has the
     * following description: Informações dos documentos
     * transportados pelo CT-e
     * Opcional para Redespacho Intermediario e Serviço vinculado
     * a multimodal.
     *
     * @param infDoc the value of field 'infDoc'.
     */
    public void setInfDoc(
            final InfDoc infDoc) {
        this._infDoc = infDoc;
    }

    /**
     * Sets the value of field 'infModal'. The field 'infModal' has
     * the following description: Informações do modal
     *
     * @param infModal the value of field 'infModal'.
     */
    public void setInfModal(
            final InfModal infModal) {
        this._infModal = infModal;
    }

    /**
     *
     *
     * @param index
     * @param vPeri
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setPeri(
            final int index,
            final Peri vPeri)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._periList.size()) {
            throw new IndexOutOfBoundsException("setPeri: Index value '" + index + "' not in range [0.." + (this._periList.size() - 1) + "]");
        }

        this._periList.set(index, vPeri);
    }

    /**
     *
     *
     * @param vPeriArray
     */
    public void setPeri(
            final Peri[] vPeriArray) {
        //-- copy array
        _periList.clear();

        for (int i = 0; i < vPeriArray.length; i++) {
                this._periList.add(vPeriArray[i]);
        }
    }

    /**
     *
     *
     * @param index
     * @param vSeg
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setSeg(
            final int index,
            final Seg vSeg)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._segList.size()) {
            throw new IndexOutOfBoundsException("setSeg: Index value '" + index + "' not in range [0.." + (this._segList.size() - 1) + "]");
        }

        this._segList.set(index, vSeg);
    }

    /**
     *
     *
     * @param vSegArray
     */
    public void setSeg(
            final Seg[] vSegArray) {
        //-- copy array
        _segList.clear();

        for (int i = 0; i < vSegArray.length; i++) {
                this._segList.add(vSegArray[i]);
        }
    }

    /**
     *
     *
     * @param index
     * @param vVeicNovos
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setVeicNovos(
            final int index,
            final VeicNovos vVeicNovos)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._veicNovosList.size()) {
            throw new IndexOutOfBoundsException("setVeicNovos: Index value '" + index + "' not in range [0.." + (this._veicNovosList.size() - 1) + "]");
        }

        this._veicNovosList.set(index, vVeicNovos);
    }

    /**
     * 
     * 
     * @param vVeicNovosArray
     */
    public void setVeicNovos(
            final VeicNovos[] vVeicNovosArray) {
        //-- copy array
        _veicNovosList.clear();

        for (int i = 0; i < vVeicNovosArray.length; i++) {
                this._veicNovosList.add(vVeicNovosArray[i]);
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
     * @return the unmarshaled
     * com.mercurio.lms.cte.model.v400.InfCTeNorm
     */
    public static InfCTeNorm unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (InfCTeNorm) org.exolab.castor.xml.Unmarshaller.unmarshal(InfCTeNorm.class, reader);
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
