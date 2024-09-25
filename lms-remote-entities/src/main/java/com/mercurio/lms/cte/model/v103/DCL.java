/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Informações da DCL
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class DCL implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * série da DCL
     */
    private java.lang.String _serie;

    /**
     * número da DCL
     */
    private java.lang.String _nDCL;

    /**
     * Data de emissão
     */
    private java.lang.String _dEmi;

    /**
     * Quantidade de Vagões
     */
    private java.lang.String _qVag;

    /**
     * peso para cálculo em Toneladas (somatório dos pesos dos
     * vagões)
     */
    private java.lang.String _pCalc;

    /**
     * Valor da Tarifa
     */
    private java.lang.String _vTar;

    /**
     * Valor do Frete
     */
    private java.lang.String _vFrete;

    /**
     * Valor dos Serviços Acessórios
     */
    private java.lang.String _vSAcess;

    /**
     * Valor Total do Serviço (Valor do Frete + valor dos serviços
     */
    private java.lang.String _vTServ;

    /**
     * Identificação do trem.
     */
    private java.lang.String _idTrem;

    /**
     * informações de detalhes dos Vagões
     */
    private java.util.List<com.mercurio.lms.cte.model.v103.DetVagDCL> _detVagDCLList;


      //----------------/
     //- Constructors -/
    //----------------/

    public DCL() {
        super();
        this._detVagDCLList = new java.util.ArrayList<com.mercurio.lms.cte.model.v103.DetVagDCL>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vDetVagDCL
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDetVagDCL(
            final com.mercurio.lms.cte.model.v103.DetVagDCL vDetVagDCL)
    throws java.lang.IndexOutOfBoundsException {
        this._detVagDCLList.add(vDetVagDCL);
    }

    /**
     * 
     * 
     * @param index
     * @param vDetVagDCL
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDetVagDCL(
            final int index,
            final com.mercurio.lms.cte.model.v103.DetVagDCL vDetVagDCL)
    throws java.lang.IndexOutOfBoundsException {
        this._detVagDCLList.add(index, vDetVagDCL);
    }

    /**
     * Method enumerateDetVagDCL.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v103.DetVagDCL> enumerateDetVagDCL(
    ) {
        return java.util.Collections.enumeration(this._detVagDCLList);
    }

    /**
     * Returns the value of field 'dEmi'. The field 'dEmi' has the
     * following description: Data de emissão
     * 
     * @return the value of field 'DEmi'.
     */
    public java.lang.String getDEmi(
    ) {
        return this._dEmi;
    }

    /**
     * Method getDetVagDCL.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v103.DetVagDCL at the given index
     */
    public com.mercurio.lms.cte.model.v103.DetVagDCL getDetVagDCL(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._detVagDCLList.size()) {
            throw new IndexOutOfBoundsException("getDetVagDCL: Index value '" + index + "' not in range [0.." + (this._detVagDCLList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v103.DetVagDCL) _detVagDCLList.get(index);
    }

    /**
     * Method getDetVagDCL.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v103.DetVagDCL[] getDetVagDCL(
    ) {
        com.mercurio.lms.cte.model.v103.DetVagDCL[] array = new com.mercurio.lms.cte.model.v103.DetVagDCL[0];
        return (com.mercurio.lms.cte.model.v103.DetVagDCL[]) this._detVagDCLList.toArray(array);
    }

    /**
     * Method getDetVagDCLCount.
     * 
     * @return the size of this collection
     */
    public int getDetVagDCLCount(
    ) {
        return this._detVagDCLList.size();
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
     * Returns the value of field 'nDCL'. The field 'nDCL' has the
     * following description: número da DCL
     * 
     * @return the value of field 'NDCL'.
     */
    public java.lang.String getNDCL(
    ) {
        return this._nDCL;
    }

    /**
     * Returns the value of field 'pCalc'. The field 'pCalc' has
     * the following description: peso para cálculo em Toneladas
     * (somatório dos pesos dos vagões)
     * 
     * @return the value of field 'PCalc'.
     */
    public java.lang.String getPCalc(
    ) {
        return this._pCalc;
    }

    /**
     * Returns the value of field 'qVag'. The field 'qVag' has the
     * following description: Quantidade de Vagões
     * 
     * @return the value of field 'QVag'.
     */
    public java.lang.String getQVag(
    ) {
        return this._qVag;
    }

    /**
     * Returns the value of field 'serie'. The field 'serie' has
     * the following description: série da DCL
     * 
     * @return the value of field 'Serie'.
     */
    public java.lang.String getSerie(
    ) {
        return this._serie;
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
     * Returns the value of field 'vSAcess'. The field 'vSAcess'
     * has the following description: Valor dos Serviços
     * Acessórios
     * 
     * @return the value of field 'VSAcess'.
     */
    public java.lang.String getVSAcess(
    ) {
        return this._vSAcess;
    }

    /**
     * Returns the value of field 'vTServ'. The field 'vTServ' has
     * the following description: Valor Total do Serviço (Valor do
     * Frete + valor dos serviços)
     * 
     * @return the value of field 'VTServ'.
     */
    public java.lang.String getVTServ(
    ) {
        return this._vTServ;
    }

    /**
     * Returns the value of field 'vTar'. The field 'vTar' has the
     * following description: Valor da Tarifa
     * 
     * @return the value of field 'VTar'.
     */
    public java.lang.String getVTar(
    ) {
        return this._vTar;
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
     * Method iterateDetVagDCL.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v103.DetVagDCL> iterateDetVagDCL(
    ) {
        return this._detVagDCLList.iterator();
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
    public void removeAllDetVagDCL(
    ) {
        this._detVagDCLList.clear();
    }

    /**
     * Method removeDetVagDCL.
     * 
     * @param vDetVagDCL
     * @return true if the object was removed from the collection.
     */
    public boolean removeDetVagDCL(
            final com.mercurio.lms.cte.model.v103.DetVagDCL vDetVagDCL) {
        boolean removed = _detVagDCLList.remove(vDetVagDCL);
        return removed;
    }

    /**
     * Method removeDetVagDCLAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v103.DetVagDCL removeDetVagDCLAt(
            final int index) {
        java.lang.Object obj = this._detVagDCLList.remove(index);
        return (com.mercurio.lms.cte.model.v103.DetVagDCL) obj;
    }

    /**
     * Sets the value of field 'dEmi'. The field 'dEmi' has the
     * following description: Data de emissão
     * 
     * @param dEmi the value of field 'dEmi'.
     */
    public void setDEmi(
            final java.lang.String dEmi) {
        this._dEmi = dEmi;
    }

    /**
     * 
     * 
     * @param index
     * @param vDetVagDCL
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setDetVagDCL(
            final int index,
            final com.mercurio.lms.cte.model.v103.DetVagDCL vDetVagDCL)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._detVagDCLList.size()) {
            throw new IndexOutOfBoundsException("setDetVagDCL: Index value '" + index + "' not in range [0.." + (this._detVagDCLList.size() - 1) + "]");
        }

        this._detVagDCLList.set(index, vDetVagDCL);
    }

    /**
     * 
     * 
     * @param vDetVagDCLArray
     */
    public void setDetVagDCL(
            final com.mercurio.lms.cte.model.v103.DetVagDCL[] vDetVagDCLArray) {
        //-- copy array
        _detVagDCLList.clear();

        for (int i = 0; i < vDetVagDCLArray.length; i++) {
                this._detVagDCLList.add(vDetVagDCLArray[i]);
        }
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
     * Sets the value of field 'nDCL'. The field 'nDCL' has the
     * following description: número da DCL
     * 
     * @param nDCL the value of field 'nDCL'.
     */
    public void setNDCL(
            final java.lang.String nDCL) {
        this._nDCL = nDCL;
    }

    /**
     * Sets the value of field 'pCalc'. The field 'pCalc' has the
     * following description: peso para cálculo em Toneladas
     * (somatório dos pesos dos vagões)
     * 
     * @param pCalc the value of field 'pCalc'.
     */
    public void setPCalc(
            final java.lang.String pCalc) {
        this._pCalc = pCalc;
    }

    /**
     * Sets the value of field 'qVag'. The field 'qVag' has the
     * following description: Quantidade de Vagões
     * 
     * @param qVag the value of field 'qVag'.
     */
    public void setQVag(
            final java.lang.String qVag) {
        this._qVag = qVag;
    }

    /**
     * Sets the value of field 'serie'. The field 'serie' has the
     * following description: série da DCL
     * 
     * @param serie the value of field 'serie'.
     */
    public void setSerie(
            final java.lang.String serie) {
        this._serie = serie;
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
     * Sets the value of field 'vSAcess'. The field 'vSAcess' has
     * the following description: Valor dos Serviços Acessórios
     * 
     * @param vSAcess the value of field 'vSAcess'.
     */
    public void setVSAcess(
            final java.lang.String vSAcess) {
        this._vSAcess = vSAcess;
    }

    /**
     * Sets the value of field 'vTServ'. The field 'vTServ' has the
     * following description: Valor Total do Serviço (Valor do
     * Frete + valor dos serviços)
     * 
     * @param vTServ the value of field 'vTServ'.
     */
    public void setVTServ(
            final java.lang.String vTServ) {
        this._vTServ = vTServ;
    }

    /**
     * Sets the value of field 'vTar'. The field 'vTar' has the
     * following description: Valor da Tarifa
     * 
     * @param vTar the value of field 'vTar'.
     */
    public void setVTar(
            final java.lang.String vTar) {
        this._vTar = vTar;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v103.DCL
     */
    public static com.mercurio.lms.cte.model.v103.DCL unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.DCL) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.DCL.class, reader);
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
