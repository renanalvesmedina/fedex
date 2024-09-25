/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100a;

/**
 * Identificação do MDF-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Ide implements java.io.Serializable {

    /**
     * Código da UF do emitente do MDF-e
     */
    private com.mercurio.lms.mdfe.model.v100a.types.TCodUfIBGE cUF;

    /**
     * Tipo do Ambiente
     */
    private com.mercurio.lms.mdfe.model.v100a.types.TAmb tpAmb;

    /**
     * Tipo do Emitente
     */
    private com.mercurio.lms.mdfe.model.v100a.types.TEmit tpEmit;

    /**
     * Modelo do Manifesto Eletrônico
     */
    private com.mercurio.lms.mdfe.model.v100a.types.TModMD mod;

    /**
     * Série do Manifesto
     */
    private java.lang.String serie;

    /**
     * Número do Manifesto
     */
    private java.lang.String nMDF;

    /**
     * Código numérico que compõe a Chave de Acesso. 
     */
    private java.lang.String cMDF;

    /**
     * Digito verificador da chave de acesso do Manifesto
     */
    private java.lang.String cDV;

    /**
     * Modalidade de transporte
     */
    private com.mercurio.lms.mdfe.model.v100a.types.TModalMD modal;

    /**
     * Data e hora de emissão do Manifesto
     */
    private java.lang.String dhEmi;

    /**
     * Forma de emissão do Manifesto (Normal ou Contingência)
     */
    private com.mercurio.lms.mdfe.model.v100a.types.TpEmisType tpEmis;

    /**
     * Identificação do processo de emissão do Manifesto
     */
    private java.lang.String procEmi;

    /**
     * Versão do processo de emissão
     */
    private java.lang.String verProc;

    /**
     * Sigla da UF do Carregamento
     */
    private com.mercurio.lms.mdfe.model.v100a.types.TUf UFIni;

    /**
     * Sigla da UF do Descarregamento
     */
    private com.mercurio.lms.mdfe.model.v100a.types.TUf UFFim;

    /**
     * Informações dos Municípios de Carregamento
     */
    private java.util.Vector<com.mercurio.lms.mdfe.model.v100a.InfMunCarrega> infMunCarregaList;

    /**
     * Informações do Percurso do MDF-e
     */
    private java.util.Vector<com.mercurio.lms.mdfe.model.v100a.InfPercurso> infPercursoList;

    /**
     * Data e hora previstos de inicio da viagem
     */
    private java.lang.String dhIniViagem;

    public Ide() {
        super();
        this.infMunCarregaList = new java.util.Vector<com.mercurio.lms.mdfe.model.v100a.InfMunCarrega>();
        this.infPercursoList = new java.util.Vector<com.mercurio.lms.mdfe.model.v100a.InfPercurso>();
    }

    /**
     * 
     * 
     * @param vInfMunCarrega
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfMunCarrega(final com.mercurio.lms.mdfe.model.v100a.InfMunCarrega vInfMunCarrega) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.infMunCarregaList.size() >= 50) {
            throw new IndexOutOfBoundsException("addInfMunCarrega has a maximum of 50");
        }

        this.infMunCarregaList.addElement(vInfMunCarrega);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfMunCarrega
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfMunCarrega(final int index,final com.mercurio.lms.mdfe.model.v100a.InfMunCarrega vInfMunCarrega) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.infMunCarregaList.size() >= 50) {
            throw new IndexOutOfBoundsException("addInfMunCarrega has a maximum of 50");
        }

        this.infMunCarregaList.add(index, vInfMunCarrega);
    }

    /**
     * 
     * 
     * @param vInfPercurso
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfPercurso(final com.mercurio.lms.mdfe.model.v100a.InfPercurso vInfPercurso) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.infPercursoList.size() >= 25) {
            throw new IndexOutOfBoundsException("addInfPercurso has a maximum of 25");
        }

        this.infPercursoList.addElement(vInfPercurso);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfPercurso
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfPercurso(final int index,final com.mercurio.lms.mdfe.model.v100a.InfPercurso vInfPercurso) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.infPercursoList.size() >= 25) {
            throw new IndexOutOfBoundsException("addInfPercurso has a maximum of 25");
        }

        this.infPercursoList.add(index, vInfPercurso);
    }

    /**
     * Method enumerateInfMunCarrega.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.mdfe.model.v100a.InfMunCarrega elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v100a.InfMunCarrega> enumerateInfMunCarrega() {
        return this.infMunCarregaList.elements();
    }

    /**
     * Method enumerateInfPercurso.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.mdfe.model.v100a.InfPercurso elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v100a.InfPercurso> enumerateInfPercurso() {
        return this.infPercursoList.elements();
    }

    /**
     * Returns the value of field 'cDV'. The field 'cDV' has the
     * following description: Digito verificador da chave de acesso
     * do Manifesto
     * 
     * @return the value of field 'CDV'.
     */
    public java.lang.String getCDV() {
        return this.cDV;
    }

    /**
     * Returns the value of field 'cMDF'. The field 'cMDF' has the
     * following description: Código numérico que compõe a Chave
     * de Acesso. 
     * 
     * @return the value of field 'CMDF'.
     */
    public java.lang.String getCMDF() {
        return this.cMDF;
    }

    /**
     * Returns the value of field 'cUF'. The field 'cUF' has the
     * following description: Código da UF do emitente do MDF-e
     * 
     * @return the value of field 'CUF'.
     */
    public com.mercurio.lms.mdfe.model.v100a.types.TCodUfIBGE getCUF() {
        return this.cUF;
    }

    /**
     * Returns the value of field 'dhEmi'. The field 'dhEmi' has
     * the following description: Data e hora de emissão do
     * Manifesto
     * 
     * @return the value of field 'DhEmi'.
     */
    public java.lang.String getDhEmi() {
        return this.dhEmi;
    }

    /**
     * Returns the value of field 'dhIniViagem'. The field
     * 'dhIniViagem' has the following description: Data e hora
     * previstos de inicio da viagem
     * 
     * @return the value of field 'DhIniViagem'.
     */
    public java.lang.String getDhIniViagem() {
        return this.dhIniViagem;
    }

    /**
     * Method getInfMunCarrega.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.mdfe.model.v100a.InfMunCarrega at the given
     * index
     */
    public com.mercurio.lms.mdfe.model.v100a.InfMunCarrega getInfMunCarrega(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.infMunCarregaList.size()) {
            throw new IndexOutOfBoundsException("getInfMunCarrega: Index value '" + index + "' not in range [0.." + (this.infMunCarregaList.size() - 1) + "]");
        }

        return infMunCarregaList.get(index);
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
    public com.mercurio.lms.mdfe.model.v100a.InfMunCarrega[] getInfMunCarrega() {
        com.mercurio.lms.mdfe.model.v100a.InfMunCarrega[] array = new com.mercurio.lms.mdfe.model.v100a.InfMunCarrega[0];
        return this.infMunCarregaList.toArray(array);
    }

    /**
     * Method getInfMunCarregaCount.
     * 
     * @return the size of this collection
     */
    public int getInfMunCarregaCount() {
        return this.infMunCarregaList.size();
    }

    /**
     * Method getInfPercurso.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.mdfe.model.v100a.InfPercurso at the given
     * index
     */
    public com.mercurio.lms.mdfe.model.v100a.InfPercurso getInfPercurso(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.infPercursoList.size()) {
            throw new IndexOutOfBoundsException("getInfPercurso: Index value '" + index + "' not in range [0.." + (this.infPercursoList.size() - 1) + "]");
        }

        return infPercursoList.get(index);
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
    public com.mercurio.lms.mdfe.model.v100a.InfPercurso[] getInfPercurso() {
        com.mercurio.lms.mdfe.model.v100a.InfPercurso[] array = new com.mercurio.lms.mdfe.model.v100a.InfPercurso[0];
        return this.infPercursoList.toArray(array);
    }

    /**
     * Method getInfPercursoCount.
     * 
     * @return the size of this collection
     */
    public int getInfPercursoCount() {
        return this.infPercursoList.size();
    }

    /**
     * Returns the value of field 'mod'. The field 'mod' has the
     * following description: Modelo do Manifesto Eletrônico
     * 
     * @return the value of field 'Mod'.
     */
    public com.mercurio.lms.mdfe.model.v100a.types.TModMD getMod() {
        return this.mod;
    }

    /**
     * Returns the value of field 'modal'. The field 'modal' has
     * the following description: Modalidade de transporte
     * 
     * @return the value of field 'Modal'.
     */
    public com.mercurio.lms.mdfe.model.v100a.types.TModalMD getModal() {
        return this.modal;
    }

    /**
     * Returns the value of field 'nMDF'. The field 'nMDF' has the
     * following description: Número do Manifesto
     * 
     * @return the value of field 'NMDF'.
     */
    public java.lang.String getNMDF() {
        return this.nMDF;
    }

    /**
     * Returns the value of field 'procEmi'. The field 'procEmi'
     * has the following description: Identificação do processo
     * de emissão do Manifesto
     * 
     * @return the value of field 'ProcEmi'.
     */
    public java.lang.String getProcEmi() {
        return this.procEmi;
    }

    /**
     * Returns the value of field 'serie'. The field 'serie' has
     * the following description: Série do Manifesto
     * 
     * @return the value of field 'Serie'.
     */
    public java.lang.String getSerie() {
        return this.serie;
    }

    /**
     * Returns the value of field 'tpAmb'. The field 'tpAmb' has
     * the following description: Tipo do Ambiente
     * 
     * @return the value of field 'TpAmb'.
     */
    public com.mercurio.lms.mdfe.model.v100a.types.TAmb getTpAmb() {
        return this.tpAmb;
    }

    /**
     * Returns the value of field 'tpEmis'. The field 'tpEmis' has
     * the following description: Forma de emissão do Manifesto
     * (Normal ou Contingência)
     * 
     * @return the value of field 'TpEmis'.
     */
    public com.mercurio.lms.mdfe.model.v100a.types.TpEmisType getTpEmis() {
        return this.tpEmis;
    }

    /**
     * Returns the value of field 'tpEmit'. The field 'tpEmit' has
     * the following description: Tipo do Emitente
     * 
     * 
     * @return the value of field 'TpEmit'.
     */
    public com.mercurio.lms.mdfe.model.v100a.types.TEmit getTpEmit() {
        return this.tpEmit;
    }

    /**
     * Returns the value of field 'UFFim'. The field 'UFFim' has
     * the following description: Sigla da UF do Descarregamento
     * 
     * @return the value of field 'UFFim'.
     */
    public com.mercurio.lms.mdfe.model.v100a.types.TUf getUFFim() {
        return this.UFFim;
    }

    /**
     * Returns the value of field 'UFIni'. The field 'UFIni' has
     * the following description: Sigla da UF do Carregamento
     * 
     * @return the value of field 'UFIni'.
     */
    public com.mercurio.lms.mdfe.model.v100a.types.TUf getUFIni() {
        return this.UFIni;
    }

    /**
     * Returns the value of field 'verProc'. The field 'verProc'
     * has the following description: Versão do processo de
     * emissão
     * 
     * @return the value of field 'VerProc'.
     */
    public java.lang.String getVerProc() {
        return this.verProc;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid() {
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
    public void marshal(final java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
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
    public void marshal(final org.xml.sax.ContentHandler handler) throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }

    /**
     */
    public void removeAllInfMunCarrega() {
        this.infMunCarregaList.clear();
    }

    /**
     */
    public void removeAllInfPercurso() {
        this.infPercursoList.clear();
    }

    /**
     * Method removeInfMunCarrega.
     * 
     * @param vInfMunCarrega
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfMunCarrega(final com.mercurio.lms.mdfe.model.v100a.InfMunCarrega vInfMunCarrega) {
        boolean removed = infMunCarregaList.remove(vInfMunCarrega);
        return removed;
    }

    /**
     * Method removeInfMunCarregaAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v100a.InfMunCarrega removeInfMunCarregaAt(final int index) {
        java.lang.Object obj = this.infMunCarregaList.remove(index);
        return (com.mercurio.lms.mdfe.model.v100a.InfMunCarrega) obj;
    }

    /**
     * Method removeInfPercurso.
     * 
     * @param vInfPercurso
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfPercurso(final com.mercurio.lms.mdfe.model.v100a.InfPercurso vInfPercurso) {
        boolean removed = infPercursoList.remove(vInfPercurso);
        return removed;
    }

    /**
     * Method removeInfPercursoAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v100a.InfPercurso removeInfPercursoAt(final int index) {
        java.lang.Object obj = this.infPercursoList.remove(index);
        return (com.mercurio.lms.mdfe.model.v100a.InfPercurso) obj;
    }

    /**
     * Sets the value of field 'cDV'. The field 'cDV' has the
     * following description: Digito verificador da chave de acesso
     * do Manifesto
     * 
     * @param cDV the value of field 'cDV'.
     */
    public void setCDV(final java.lang.String cDV) {
        this.cDV = cDV;
    }

    /**
     * Sets the value of field 'cMDF'. The field 'cMDF' has the
     * following description: Código numérico que compõe a Chave
     * de Acesso. 
     * 
     * @param cMDF the value of field 'cMDF'.
     */
    public void setCMDF(final java.lang.String cMDF) {
        this.cMDF = cMDF;
    }

    /**
     * Sets the value of field 'cUF'. The field 'cUF' has the
     * following description: Código da UF do emitente do MDF-e
     * 
     * @param cUF the value of field 'cUF'.
     */
    public void setCUF(final com.mercurio.lms.mdfe.model.v100a.types.TCodUfIBGE cUF) {
        this.cUF = cUF;
    }

    /**
     * Sets the value of field 'dhEmi'. The field 'dhEmi' has the
     * following description: Data e hora de emissão do Manifesto
     * 
     * @param dhEmi the value of field 'dhEmi'.
     */
    public void setDhEmi(final java.lang.String dhEmi) {
        this.dhEmi = dhEmi;
    }

    /**
     * Sets the value of field 'dhIniViagem'. The field
     * 'dhIniViagem' has the following description: Data e hora
     * previstos de inicio da viagem
     * 
     * @param dhIniViagem the value of field 'dhIniViagem'.
     */
    public void setDhIniViagem(final java.lang.String dhIniViagem) {
        this.dhIniViagem = dhIniViagem;
    }

    /**
     * 
     * 
     * @param index
     * @param vInfMunCarrega
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfMunCarrega(final int index,final com.mercurio.lms.mdfe.model.v100a.InfMunCarrega vInfMunCarrega) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.infMunCarregaList.size()) {
            throw new IndexOutOfBoundsException("setInfMunCarrega: Index value '" + index + "' not in range [0.." + (this.infMunCarregaList.size() - 1) + "]");
        }

        this.infMunCarregaList.set(index, vInfMunCarrega);
    }

    /**
     * 
     * 
     * @param vInfMunCarregaArray
     */
    public void setInfMunCarrega(final com.mercurio.lms.mdfe.model.v100a.InfMunCarrega[] vInfMunCarregaArray) {
        //-- copy array
        infMunCarregaList.clear();

        for (int i = 0; i < vInfMunCarregaArray.length; i++) {
                this.infMunCarregaList.add(vInfMunCarregaArray[i]);
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
    public void setInfPercurso(final int index,final com.mercurio.lms.mdfe.model.v100a.InfPercurso vInfPercurso) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.infPercursoList.size()) {
            throw new IndexOutOfBoundsException("setInfPercurso: Index value '" + index + "' not in range [0.." + (this.infPercursoList.size() - 1) + "]");
        }

        this.infPercursoList.set(index, vInfPercurso);
    }

    /**
     * 
     * 
     * @param vInfPercursoArray
     */
    public void setInfPercurso(final com.mercurio.lms.mdfe.model.v100a.InfPercurso[] vInfPercursoArray) {
        //-- copy array
        infPercursoList.clear();

        for (int i = 0; i < vInfPercursoArray.length; i++) {
                this.infPercursoList.add(vInfPercursoArray[i]);
        }
    }

    /**
     * Sets the value of field 'mod'. The field 'mod' has the
     * following description: Modelo do Manifesto Eletrônico
     * 
     * @param mod the value of field 'mod'.
     */
    public void setMod(final com.mercurio.lms.mdfe.model.v100a.types.TModMD mod) {
        this.mod = mod;
    }

    /**
     * Sets the value of field 'modal'. The field 'modal' has the
     * following description: Modalidade de transporte
     * 
     * @param modal the value of field 'modal'.
     */
    public void setModal(final com.mercurio.lms.mdfe.model.v100a.types.TModalMD modal) {
        this.modal = modal;
    }

    /**
     * Sets the value of field 'nMDF'. The field 'nMDF' has the
     * following description: Número do Manifesto
     * 
     * @param nMDF the value of field 'nMDF'.
     */
    public void setNMDF(final java.lang.String nMDF) {
        this.nMDF = nMDF;
    }

    /**
     * Sets the value of field 'procEmi'. The field 'procEmi' has
     * the following description: Identificação do processo de
     * emissão do Manifesto
     * 
     * @param procEmi the value of field 'procEmi'.
     */
    public void setProcEmi(final java.lang.String procEmi) {
        this.procEmi = procEmi;
    }

    /**
     * Sets the value of field 'serie'. The field 'serie' has the
     * following description: Série do Manifesto
     * 
     * @param serie the value of field 'serie'.
     */
    public void setSerie(final java.lang.String serie) {
        this.serie = serie;
    }

    /**
     * Sets the value of field 'tpAmb'. The field 'tpAmb' has the
     * following description: Tipo do Ambiente
     * 
     * @param tpAmb the value of field 'tpAmb'.
     */
    public void setTpAmb(final com.mercurio.lms.mdfe.model.v100a.types.TAmb tpAmb) {
        this.tpAmb = tpAmb;
    }

    /**
     * Sets the value of field 'tpEmis'. The field 'tpEmis' has the
     * following description: Forma de emissão do Manifesto
     * (Normal ou Contingência)
     * 
     * @param tpEmis the value of field 'tpEmis'.
     */
    public void setTpEmis(final com.mercurio.lms.mdfe.model.v100a.types.TpEmisType tpEmis) {
        this.tpEmis = tpEmis;
    }

    /**
     * Sets the value of field 'tpEmit'. The field 'tpEmit' has the
     * following description: Tipo do Emitente
     * 
     * 
     * @param tpEmit the value of field 'tpEmit'.
     */
    public void setTpEmit(final com.mercurio.lms.mdfe.model.v100a.types.TEmit tpEmit) {
        this.tpEmit = tpEmit;
    }

    /**
     * Sets the value of field 'UFFim'. The field 'UFFim' has the
     * following description: Sigla da UF do Descarregamento
     * 
     * @param UFFim the value of field 'UFFim'.
     */
    public void setUFFim(final com.mercurio.lms.mdfe.model.v100a.types.TUf UFFim) {
        this.UFFim = UFFim;
    }

    /**
     * Sets the value of field 'UFIni'. The field 'UFIni' has the
     * following description: Sigla da UF do Carregamento
     * 
     * @param UFIni the value of field 'UFIni'.
     */
    public void setUFIni(final com.mercurio.lms.mdfe.model.v100a.types.TUf UFIni) {
        this.UFIni = UFIni;
    }

    /**
     * Sets the value of field 'verProc'. The field 'verProc' has
     * the following description: Versão do processo de emissão
     * 
     * @param verProc the value of field 'verProc'.
     */
    public void setVerProc(final java.lang.String verProc) {
        this.verProc = verProc;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.mdfe.model.v100a.Ide
     */
    public static com.mercurio.lms.mdfe.model.v100a.Ide unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100a.Ide) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100a.Ide.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
