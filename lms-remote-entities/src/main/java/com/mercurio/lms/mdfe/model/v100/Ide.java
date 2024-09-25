/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100;

/**
 * Identificação do MDF-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Ide implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Código da UF do emitente do MDF-e
     */
    private com.mercurio.lms.mdfe.model.v100.types.TCodUfIBGE _cUF;

    /**
     * Tipo do Ambiente
     */
    private com.mercurio.lms.mdfe.model.v100.types.TAmb _tpAmb;

    /**
     * Tipo do Emitente
     */
    private com.mercurio.lms.mdfe.model.v100.types.TEmit _tpEmit;

    /**
     * Modelo do Manifesto Eletrônico
     */
    private com.mercurio.lms.mdfe.model.v100.types.TModMD _mod;

    /**
     * Série do Manifesto
     */
    private java.lang.String _serie;

    /**
     * Número do Manifesto
     */
    private java.lang.String _nMDF;

    /**
     * Código numérico que compõe a Chave de Acesso. 
     */
    private java.lang.String _cMDF;

    /**
     * Digito verificador da chave de acesso do Manifesto
     */
    private java.lang.String _cDV;

    /**
     * Modalidade de transporte
     */
    private com.mercurio.lms.mdfe.model.v100.types.TModalMD _modal;

    /**
     * Data e hora de emissão do Manifesto
     */
    private java.lang.String _dhEmi;

    /**
     * Forma de emissão do Manifesto (Normal ou Contingência)
     */
    private com.mercurio.lms.mdfe.model.v100.types.TpEmisType _tpEmis;

    /**
     * Identificação do processo de emissão do Manifesto
     */
    private java.lang.String _procEmi;

    /**
     * Versão do processo de emissão
     */
    private java.lang.String _verProc;

    /**
     * Sigla da UF do Carregamento
     */
    private com.mercurio.lms.mdfe.model.v100.types.TUf _UFIni;

    /**
     * Sigla da UF do Descarregamento
     */
    private com.mercurio.lms.mdfe.model.v100.types.TUf _UFFim;

    /**
     * Informações dos Municípios de Carregamento
     */
    private java.util.List<com.mercurio.lms.mdfe.model.v100.InfMunCarrega> _infMunCarregaList;

    /**
     * Informações do Percurso do MDF-e
     */
    private java.util.List<com.mercurio.lms.mdfe.model.v100.InfPercurso> _infPercursoList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Ide() {
        super();
        this._infMunCarregaList = new java.util.ArrayList<com.mercurio.lms.mdfe.model.v100.InfMunCarrega>();
        this._infPercursoList = new java.util.ArrayList<com.mercurio.lms.mdfe.model.v100.InfPercurso>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vInfMunCarrega
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfMunCarrega(
            final com.mercurio.lms.mdfe.model.v100.InfMunCarrega vInfMunCarrega)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._infMunCarregaList.size() >= 50) {
            throw new IndexOutOfBoundsException("addInfMunCarrega has a maximum of 50");
        }

        this._infMunCarregaList.add(vInfMunCarrega);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfMunCarrega
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfMunCarrega(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.InfMunCarrega vInfMunCarrega)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._infMunCarregaList.size() >= 50) {
            throw new IndexOutOfBoundsException("addInfMunCarrega has a maximum of 50");
        }

        this._infMunCarregaList.add(index, vInfMunCarrega);
    }

    /**
     * 
     * 
     * @param vInfPercurso
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfPercurso(
            final com.mercurio.lms.mdfe.model.v100.InfPercurso vInfPercurso)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._infPercursoList.size() >= 25) {
            throw new IndexOutOfBoundsException("addInfPercurso has a maximum of 25");
        }

        this._infPercursoList.add(vInfPercurso);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfPercurso
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfPercurso(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.InfPercurso vInfPercurso)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._infPercursoList.size() >= 25) {
            throw new IndexOutOfBoundsException("addInfPercurso has a maximum of 25");
        }

        this._infPercursoList.add(index, vInfPercurso);
    }

    /**
     * Method enumerateInfMunCarrega.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v100.InfMunCarrega> enumerateInfMunCarrega(
    ) {
        return java.util.Collections.enumeration(this._infMunCarregaList);
    }

    /**
     * Method enumerateInfPercurso.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v100.InfPercurso> enumerateInfPercurso(
    ) {
        return java.util.Collections.enumeration(this._infPercursoList);
    }

    /**
     * Returns the value of field 'cDV'. The field 'cDV' has the
     * following description: Digito verificador da chave de acesso
     * do Manifesto
     * 
     * @return the value of field 'CDV'.
     */
    public java.lang.String getCDV(
    ) {
        return this._cDV;
    }

    /**
     * Returns the value of field 'cMDF'. The field 'cMDF' has the
     * following description: Código numérico que compõe a Chave
     * de Acesso. 
     * 
     * @return the value of field 'CMDF'.
     */
    public java.lang.String getCMDF(
    ) {
        return this._cMDF;
    }

    /**
     * Returns the value of field 'cUF'. The field 'cUF' has the
     * following description: Código da UF do emitente do MDF-e
     * 
     * @return the value of field 'CUF'.
     */
    public com.mercurio.lms.mdfe.model.v100.types.TCodUfIBGE getCUF(
    ) {
        return this._cUF;
    }

    /**
     * Returns the value of field 'dhEmi'. The field 'dhEmi' has
     * the following description: Data e hora de emissão do
     * Manifesto
     * 
     * @return the value of field 'DhEmi'.
     */
    public java.lang.String getDhEmi(
    ) {
        return this._dhEmi;
    }

    /**
     * Method getInfMunCarrega.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.InfMunCarrega at the given
     * index
     */
    public com.mercurio.lms.mdfe.model.v100.InfMunCarrega getInfMunCarrega(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infMunCarregaList.size()) {
            throw new IndexOutOfBoundsException("getInfMunCarrega: Index value '" + index + "' not in range [0.." + (this._infMunCarregaList.size() - 1) + "]");
        }

        return (com.mercurio.lms.mdfe.model.v100.InfMunCarrega) _infMunCarregaList.get(index);
    }

    /**
     * Method getInfMunCarrega.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v100.InfMunCarrega[] getInfMunCarrega(
    ) {
        com.mercurio.lms.mdfe.model.v100.InfMunCarrega[] array = new com.mercurio.lms.mdfe.model.v100.InfMunCarrega[0];
        return (com.mercurio.lms.mdfe.model.v100.InfMunCarrega[]) this._infMunCarregaList.toArray(array);
    }

    /**
     * Method getInfMunCarregaCount.
     * 
     * @return the size of this collection
     */
    public int getInfMunCarregaCount(
    ) {
        return this._infMunCarregaList.size();
    }

    /**
     * Method getInfPercurso.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.InfPercurso at the given
     * index
     */
    public com.mercurio.lms.mdfe.model.v100.InfPercurso getInfPercurso(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infPercursoList.size()) {
            throw new IndexOutOfBoundsException("getInfPercurso: Index value '" + index + "' not in range [0.." + (this._infPercursoList.size() - 1) + "]");
        }

        return (com.mercurio.lms.mdfe.model.v100.InfPercurso) _infPercursoList.get(index);
    }

    /**
     * Method getInfPercurso.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v100.InfPercurso[] getInfPercurso(
    ) {
        com.mercurio.lms.mdfe.model.v100.InfPercurso[] array = new com.mercurio.lms.mdfe.model.v100.InfPercurso[0];
        return (com.mercurio.lms.mdfe.model.v100.InfPercurso[]) this._infPercursoList.toArray(array);
    }

    /**
     * Method getInfPercursoCount.
     * 
     * @return the size of this collection
     */
    public int getInfPercursoCount(
    ) {
        return this._infPercursoList.size();
    }

    /**
     * Returns the value of field 'mod'. The field 'mod' has the
     * following description: Modelo do Manifesto Eletrônico
     * 
     * @return the value of field 'Mod'.
     */
    public com.mercurio.lms.mdfe.model.v100.types.TModMD getMod(
    ) {
        return this._mod;
    }

    /**
     * Returns the value of field 'modal'. The field 'modal' has
     * the following description: Modalidade de transporte
     * 
     * @return the value of field 'Modal'.
     */
    public com.mercurio.lms.mdfe.model.v100.types.TModalMD getModal(
    ) {
        return this._modal;
    }

    /**
     * Returns the value of field 'nMDF'. The field 'nMDF' has the
     * following description: Número do Manifesto
     * 
     * @return the value of field 'NMDF'.
     */
    public java.lang.String getNMDF(
    ) {
        return this._nMDF;
    }

    /**
     * Returns the value of field 'procEmi'. The field 'procEmi'
     * has the following description: Identificação do processo
     * de emissão do Manifesto
     * 
     * @return the value of field 'ProcEmi'.
     */
    public java.lang.String getProcEmi(
    ) {
        return this._procEmi;
    }

    /**
     * Returns the value of field 'serie'. The field 'serie' has
     * the following description: Série do Manifesto
     * 
     * @return the value of field 'Serie'.
     */
    public java.lang.String getSerie(
    ) {
        return this._serie;
    }

    /**
     * Returns the value of field 'tpAmb'. The field 'tpAmb' has
     * the following description: Tipo do Ambiente
     * 
     * @return the value of field 'TpAmb'.
     */
    public com.mercurio.lms.mdfe.model.v100.types.TAmb getTpAmb(
    ) {
        return this._tpAmb;
    }

    /**
     * Returns the value of field 'tpEmis'. The field 'tpEmis' has
     * the following description: Forma de emissão do Manifesto
     * (Normal ou Contingência)
     * 
     * @return the value of field 'TpEmis'.
     */
    public com.mercurio.lms.mdfe.model.v100.types.TpEmisType getTpEmis(
    ) {
        return this._tpEmis;
    }

    /**
     * Returns the value of field 'tpEmit'. The field 'tpEmit' has
     * the following description: Tipo do Emitente
     * 
     * 
     * @return the value of field 'TpEmit'.
     */
    public com.mercurio.lms.mdfe.model.v100.types.TEmit getTpEmit(
    ) {
        return this._tpEmit;
    }

    /**
     * Returns the value of field 'UFFim'. The field 'UFFim' has
     * the following description: Sigla da UF do Descarregamento
     * 
     * @return the value of field 'UFFim'.
     */
    public com.mercurio.lms.mdfe.model.v100.types.TUf getUFFim(
    ) {
        return this._UFFim;
    }

    /**
     * Returns the value of field 'UFIni'. The field 'UFIni' has
     * the following description: Sigla da UF do Carregamento
     * 
     * @return the value of field 'UFIni'.
     */
    public com.mercurio.lms.mdfe.model.v100.types.TUf getUFIni(
    ) {
        return this._UFIni;
    }

    /**
     * Returns the value of field 'verProc'. The field 'verProc'
     * has the following description: Versão do processo de
     * emissão
     * 
     * @return the value of field 'VerProc'.
     */
    public java.lang.String getVerProc(
    ) {
        return this._verProc;
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
     * Method iterateInfMunCarrega.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.mdfe.model.v100.InfMunCarrega> iterateInfMunCarrega(
    ) {
        return this._infMunCarregaList.iterator();
    }

    /**
     * Method iterateInfPercurso.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.mdfe.model.v100.InfPercurso> iterateInfPercurso(
    ) {
        return this._infPercursoList.iterator();
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
    public void removeAllInfMunCarrega(
    ) {
        this._infMunCarregaList.clear();
    }

    /**
     */
    public void removeAllInfPercurso(
    ) {
        this._infPercursoList.clear();
    }

    /**
     * Method removeInfMunCarrega.
     * 
     * @param vInfMunCarrega
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfMunCarrega(
            final com.mercurio.lms.mdfe.model.v100.InfMunCarrega vInfMunCarrega) {
        boolean removed = _infMunCarregaList.remove(vInfMunCarrega);
        return removed;
    }

    /**
     * Method removeInfMunCarregaAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v100.InfMunCarrega removeInfMunCarregaAt(
            final int index) {
        java.lang.Object obj = this._infMunCarregaList.remove(index);
        return (com.mercurio.lms.mdfe.model.v100.InfMunCarrega) obj;
    }

    /**
     * Method removeInfPercurso.
     * 
     * @param vInfPercurso
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfPercurso(
            final com.mercurio.lms.mdfe.model.v100.InfPercurso vInfPercurso) {
        boolean removed = _infPercursoList.remove(vInfPercurso);
        return removed;
    }

    /**
     * Method removeInfPercursoAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v100.InfPercurso removeInfPercursoAt(
            final int index) {
        java.lang.Object obj = this._infPercursoList.remove(index);
        return (com.mercurio.lms.mdfe.model.v100.InfPercurso) obj;
    }

    /**
     * Sets the value of field 'cDV'. The field 'cDV' has the
     * following description: Digito verificador da chave de acesso
     * do Manifesto
     * 
     * @param cDV the value of field 'cDV'.
     */
    public void setCDV(
            final java.lang.String cDV) {
        this._cDV = cDV;
    }

    /**
     * Sets the value of field 'cMDF'. The field 'cMDF' has the
     * following description: Código numérico que compõe a Chave
     * de Acesso. 
     * 
     * @param cMDF the value of field 'cMDF'.
     */
    public void setCMDF(
            final java.lang.String cMDF) {
        this._cMDF = cMDF;
    }

    /**
     * Sets the value of field 'cUF'. The field 'cUF' has the
     * following description: Código da UF do emitente do MDF-e
     * 
     * @param cUF the value of field 'cUF'.
     */
    public void setCUF(
            final com.mercurio.lms.mdfe.model.v100.types.TCodUfIBGE cUF) {
        this._cUF = cUF;
    }

    /**
     * Sets the value of field 'dhEmi'. The field 'dhEmi' has the
     * following description: Data e hora de emissão do Manifesto
     * 
     * @param dhEmi the value of field 'dhEmi'.
     */
    public void setDhEmi(
            final java.lang.String dhEmi) {
        this._dhEmi = dhEmi;
    }

    /**
     * 
     * 
     * @param index
     * @param vInfMunCarrega
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfMunCarrega(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.InfMunCarrega vInfMunCarrega)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infMunCarregaList.size()) {
            throw new IndexOutOfBoundsException("setInfMunCarrega: Index value '" + index + "' not in range [0.." + (this._infMunCarregaList.size() - 1) + "]");
        }

        this._infMunCarregaList.set(index, vInfMunCarrega);
    }

    /**
     * 
     * 
     * @param vInfMunCarregaArray
     */
    public void setInfMunCarrega(
            final com.mercurio.lms.mdfe.model.v100.InfMunCarrega[] vInfMunCarregaArray) {
        //-- copy array
        _infMunCarregaList.clear();

        for (int i = 0; i < vInfMunCarregaArray.length; i++) {
                this._infMunCarregaList.add(vInfMunCarregaArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vInfPercurso
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfPercurso(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.InfPercurso vInfPercurso)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._infPercursoList.size()) {
            throw new IndexOutOfBoundsException("setInfPercurso: Index value '" + index + "' not in range [0.." + (this._infPercursoList.size() - 1) + "]");
        }

        this._infPercursoList.set(index, vInfPercurso);
    }

    /**
     * 
     * 
     * @param vInfPercursoArray
     */
    public void setInfPercurso(
            final com.mercurio.lms.mdfe.model.v100.InfPercurso[] vInfPercursoArray) {
        //-- copy array
        _infPercursoList.clear();

        for (int i = 0; i < vInfPercursoArray.length; i++) {
                this._infPercursoList.add(vInfPercursoArray[i]);
        }
    }

    /**
     * Sets the value of field 'mod'. The field 'mod' has the
     * following description: Modelo do Manifesto Eletrônico
     * 
     * @param mod the value of field 'mod'.
     */
    public void setMod(
            final com.mercurio.lms.mdfe.model.v100.types.TModMD mod) {
        this._mod = mod;
    }

    /**
     * Sets the value of field 'modal'. The field 'modal' has the
     * following description: Modalidade de transporte
     * 
     * @param modal the value of field 'modal'.
     */
    public void setModal(
            final com.mercurio.lms.mdfe.model.v100.types.TModalMD modal) {
        this._modal = modal;
    }

    /**
     * Sets the value of field 'nMDF'. The field 'nMDF' has the
     * following description: Número do Manifesto
     * 
     * @param nMDF the value of field 'nMDF'.
     */
    public void setNMDF(
            final java.lang.String nMDF) {
        this._nMDF = nMDF;
    }

    /**
     * Sets the value of field 'procEmi'. The field 'procEmi' has
     * the following description: Identificação do processo de
     * emissão do Manifesto
     * 
     * @param procEmi the value of field 'procEmi'.
     */
    public void setProcEmi(
            final java.lang.String procEmi) {
        this._procEmi = procEmi;
    }

    /**
     * Sets the value of field 'serie'. The field 'serie' has the
     * following description: Série do Manifesto
     * 
     * @param serie the value of field 'serie'.
     */
    public void setSerie(
            final java.lang.String serie) {
        this._serie = serie;
    }

    /**
     * Sets the value of field 'tpAmb'. The field 'tpAmb' has the
     * following description: Tipo do Ambiente
     * 
     * @param tpAmb the value of field 'tpAmb'.
     */
    public void setTpAmb(
            final com.mercurio.lms.mdfe.model.v100.types.TAmb tpAmb) {
        this._tpAmb = tpAmb;
    }

    /**
     * Sets the value of field 'tpEmis'. The field 'tpEmis' has the
     * following description: Forma de emissão do Manifesto
     * (Normal ou Contingência)
     * 
     * @param tpEmis the value of field 'tpEmis'.
     */
    public void setTpEmis(
            final com.mercurio.lms.mdfe.model.v100.types.TpEmisType tpEmis) {
        this._tpEmis = tpEmis;
    }

    /**
     * Sets the value of field 'tpEmit'. The field 'tpEmit' has the
     * following description: Tipo do Emitente
     * 
     * 
     * @param tpEmit the value of field 'tpEmit'.
     */
    public void setTpEmit(
            final com.mercurio.lms.mdfe.model.v100.types.TEmit tpEmit) {
        this._tpEmit = tpEmit;
    }

    /**
     * Sets the value of field 'UFFim'. The field 'UFFim' has the
     * following description: Sigla da UF do Descarregamento
     * 
     * @param UFFim the value of field 'UFFim'.
     */
    public void setUFFim(
            final com.mercurio.lms.mdfe.model.v100.types.TUf UFFim) {
        this._UFFim = UFFim;
    }

    /**
     * Sets the value of field 'UFIni'. The field 'UFIni' has the
     * following description: Sigla da UF do Carregamento
     * 
     * @param UFIni the value of field 'UFIni'.
     */
    public void setUFIni(
            final com.mercurio.lms.mdfe.model.v100.types.TUf UFIni) {
        this._UFIni = UFIni;
    }

    /**
     * Sets the value of field 'verProc'. The field 'verProc' has
     * the following description: Versão do processo de emissão
     * 
     * @param verProc the value of field 'verProc'.
     */
    public void setVerProc(
            final java.lang.String verProc) {
        this._verProc = verProc;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled org.exolab.castor.builder.binding.Ide
     */
    public static com.mercurio.lms.mdfe.model.v100.Ide unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100.Ide) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100.Ide.class, reader);
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
