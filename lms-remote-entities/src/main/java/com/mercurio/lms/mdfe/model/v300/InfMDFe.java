/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 * Informações do MDF-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfMDFe implements java.io.Serializable {

    /**
     * Versão do leiaute
     */
    private java.lang.String versao;

    /**
     * Identificador da tag a ser assinada
     */
    private java.lang.String id;

    /**
     * Identificação do MDF-e
     */
    private com.mercurio.lms.mdfe.model.v300.Ide ide;

    /**
     * Identificação do Emitente do Manifesto
     */
    private com.mercurio.lms.mdfe.model.v300.Emit emit;

    /**
     * Informações do modal
     */
    private com.mercurio.lms.mdfe.model.v300.InfModal infModal;

    /**
     * Informações dos Documentos fiscais vinculados ao manifesto
     */
    private com.mercurio.lms.mdfe.model.v300.InfDoc infDoc;
    
    /**
     * Informa��es de Seguro da Carga
     */
    private java.util.Vector<com.mercurio.lms.mdfe.model.v300.Seg> segList;
    
    /**
     * Informa��es do Produto predominante da carga
     */
    private com.mercurio.lms.mdfe.model.v300.ProdPred prodPred;

    /**
     * Totalizadores da carga transportada e seus documentos fiscais
     */
    private com.mercurio.lms.mdfe.model.v300.Tot tot;

    /**
     * Lacres do MDF-e
     */
    private java.util.Vector<com.mercurio.lms.mdfe.model.v300.Lacres> lacresList;

    /**
     * Autorizados para download do XML do DF-e
     */
    private java.util.Vector<com.mercurio.lms.mdfe.model.v300.AutXML> autXMLList;

    /**
     * Informações Adicionais
     */
    private com.mercurio.lms.mdfe.model.v300.InfAdic infAdic;
    
    public InfMDFe() {
        super();
        this.lacresList = new java.util.Vector<com.mercurio.lms.mdfe.model.v300.Lacres>();
        this.autXMLList = new java.util.Vector<com.mercurio.lms.mdfe.model.v300.AutXML>();
        this.segList = new java.util.Vector<com.mercurio.lms.mdfe.model.v300.Seg>();
    }
    
    public void addSeg(final com.mercurio.lms.mdfe.model.v300.Seg seg) {
        this.segList.addElement(seg);
    }
    
    /**
     * 
     * 
     * @param index
     * @param vSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSeg(final int index,final com.mercurio.lms.mdfe.model.v300.Seg vSeg) throws java.lang.IndexOutOfBoundsException {
        this.segList.add(index, vSeg);
    }

    /**
     * 
     * 
     * @param vAutXML
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addAutXML(final com.mercurio.lms.mdfe.model.v300.AutXML vAutXML) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.autXMLList.size() >= 10) {
            throw new IndexOutOfBoundsException("addAutXML has a maximum of 10");
        }

        this.autXMLList.addElement(vAutXML);
    }

    /**
     * 
     * 
     * @param index
     * @param vAutXML
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addAutXML(final int index,final com.mercurio.lms.mdfe.model.v300.AutXML vAutXML) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.autXMLList.size() >= 10) {
            throw new IndexOutOfBoundsException("addAutXML has a maximum of 10");
        }

        this.autXMLList.add(index, vAutXML);
    }

    /**
     * 
     * 
     * @param vLacres
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacres(final com.mercurio.lms.mdfe.model.v300.Lacres vLacres) throws java.lang.IndexOutOfBoundsException {
        this.lacresList.addElement(vLacres);
    }

    /**
     * 
     * 
     * @param index
     * @param vLacres
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacres(final int index,final com.mercurio.lms.mdfe.model.v300.Lacres vLacres) throws java.lang.IndexOutOfBoundsException {
        this.lacresList.add(index, vLacres);
    }

    /**
     * Method enumerateAutXML.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.mdfe.model.v300.AutXML elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v300.AutXML> enumerateAutXML() {
        return this.autXMLList.elements();
    }

    /**
     * Method enumerateLacres.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.mdfe.model.v300.Lacres elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v300.Lacres> enumerateLacres() {
        return this.lacresList.elements();
    }
    
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v300.Seg> enumerateSeg() {
        return this.segList.elements();
    }
    
    public com.mercurio.lms.mdfe.model.v300.Seg getSeg(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.segList.size()) {
            throw new IndexOutOfBoundsException("getSeg: Index value '" + index + "' not in range [0.." + (this.segList.size() - 1) + "]");
        }

        return segList.get(index);
    }

    /**
     * Method getAutXML.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.mdfe.model.v300.AutXML at the given index
     */
    public com.mercurio.lms.mdfe.model.v300.AutXML getAutXML(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.autXMLList.size()) {
            throw new IndexOutOfBoundsException("getAutXML: Index value '" + index + "' not in range [0.." + (this.autXMLList.size() - 1) + "]");
        }

        return autXMLList.get(index);
    }

    /**
     * Method getAutXML.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v300.AutXML[] getAutXML() {
        com.mercurio.lms.mdfe.model.v300.AutXML[] array = new com.mercurio.lms.mdfe.model.v300.AutXML[0];
        return this.autXMLList.toArray(array);
    }

    /**
     * Method getAutXMLCount.
     * 
     * @return the size of this collection
     */
    public int getAutXMLCount() {
        return this.autXMLList.size();
    }

    /**
     * Returns the value of field 'emit'. The field 'emit' has the
     * following description: Identificação do Emitente do
     * Manifesto
     * 
     * @return the value of field 'Emit'.
     */
    public com.mercurio.lms.mdfe.model.v300.Emit getEmit() {
        return this.emit;
    }

    /**
     * Returns the value of field 'id'. The field 'id' has the
     * following description: Identificador da tag a ser assinada
     * 
     * @return the value of field 'Id'.
     */
    public java.lang.String getId() {
        return this.id;
    }

    /**
     * Returns the value of field 'ide'. The field 'ide' has the
     * following description: Identificação do MDF-e
     * 
     * @return the value of field 'Ide'.
     */
    public com.mercurio.lms.mdfe.model.v300.Ide getIde() {
        return this.ide;
    }

    /**
     * Returns the value of field 'infAdic'. The field 'infAdic'
     * has the following description: Informações Adicionais
     * 
     * @return the value of field 'InfAdic'.
     */
    public com.mercurio.lms.mdfe.model.v300.InfAdic getInfAdic() {
        return this.infAdic;
    }
    
    /**
     * Returns the value of field 'infDoc'. The field 'infDoc' has
     * the following description: Informações dos Documentos
     * fiscais vinculados ao manifesto
     * 
     * @return the value of field 'InfDoc'.
     */
    public com.mercurio.lms.mdfe.model.v300.InfDoc getInfDoc() {
        return this.infDoc;
    }
    
    /**
     * Returns the value of field 'Seg'. The field 'Seg' has
     * the following description: Informa��es de Seguro da Carga
     * 
     * @return the value of field 'Seg'.
     */
    public com.mercurio.lms.mdfe.model.v300.Seg[] getSeg() {
    	 com.mercurio.lms.mdfe.model.v300.Seg[] array = new com.mercurio.lms.mdfe.model.v300.Seg[0];
         return this.segList.toArray(array);
    }
    
    public int getSegCount() {
        return this.segList.size();
    }

    /**
     * Returns the value of field 'infModal'. The field 'infModal'
     * has the following description: Informações do modal
     * 
     * @return the value of field 'InfModal'.
     */
    public com.mercurio.lms.mdfe.model.v300.InfModal getInfModal() {
        return this.infModal;
    }

    /**
     * Method getLacres.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.mdfe.model.v300.Lacres at the given index
     */
    public com.mercurio.lms.mdfe.model.v300.Lacres getLacres(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.lacresList.size()) {
            throw new IndexOutOfBoundsException("getLacres: Index value '" + index + "' not in range [0.." + (this.lacresList.size() - 1) + "]");
        }

        return lacresList.get(index);
    }

    /**
     * Method getLacres.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v300.Lacres[] getLacres() {
        com.mercurio.lms.mdfe.model.v300.Lacres[] array = new com.mercurio.lms.mdfe.model.v300.Lacres[0];
        return this.lacresList.toArray(array);
    }

    /**
     * Method getLacresCount.
     * 
     * @return the size of this collection
     */
    public int getLacresCount() {
        return this.lacresList.size();
    }
    
    /**
     * Returns the value of field 'prodPred'. The field 'prodPred'
     * has the following description: Informa��es do Produto predominante da carga
     * 
     * @return the value of field 'ProdPred'.
     */
    public com.mercurio.lms.mdfe.model.v300.ProdPred getProdPred() {
    	return this.prodPred;
    }

    /**
     * Returns the value of field 'tot'. The field 'tot' has the
     * following description: Totalizadores da carga transportada e
     * seus documentos fiscais
     * 
     * @return the value of field 'Tot'.
     */
    public com.mercurio.lms.mdfe.model.v300.Tot getTot() {
        return this.tot;
    }

    /**
     * Returns the value of field 'versao'. The field 'versao' has
     * the following description: Versão do leiaute
     * 
     * @return the value of field 'Versao'.
     */
    public java.lang.String getVersao() {
        return this.versao;
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
    public void removeAllAutXML() {
        this.autXMLList.clear();
    }

    /**
     */
    public void removeAllLacres() {
        this.lacresList.clear();
    }
    
    /**
     */
    public void removeAllSeg() {
        this.segList.clear();
    }
    
    public boolean removeSeg(final com.mercurio.lms.mdfe.model.v300.Seg vSeg) {
        boolean removed = segList.remove(vSeg);
        return removed;
    }

    public com.mercurio.lms.mdfe.model.v300.Seg removeSegAt(final int index) {
        java.lang.Object obj = this.autXMLList.remove(index);
        return (com.mercurio.lms.mdfe.model.v300.Seg) obj;
    }

    /**
     * Method removeAutXML.
     * 
     * @param vAutXML
     * @return true if the object was removed from the collection.
     */
    public boolean removeAutXML(final com.mercurio.lms.mdfe.model.v300.AutXML vAutXML) {
        boolean removed = autXMLList.remove(vAutXML);
        return removed;
    }

    /**
     * Method removeAutXMLAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v300.AutXML removeAutXMLAt(final int index) {
        java.lang.Object obj = this.autXMLList.remove(index);
        return (com.mercurio.lms.mdfe.model.v300.AutXML) obj;
    }

    /**
     * Method removeLacres.
     * 
     * @param vLacres
     * @return true if the object was removed from the collection.
     */
    public boolean removeLacres(final com.mercurio.lms.mdfe.model.v300.Lacres vLacres) {
        boolean removed = lacresList.remove(vLacres);
        return removed;
    }

    /**
     * Method removeLacresAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v300.Lacres removeLacresAt(final int index) {
        java.lang.Object obj = this.lacresList.remove(index);
        return (com.mercurio.lms.mdfe.model.v300.Lacres) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vAutXML
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setAutXML(final int index,final com.mercurio.lms.mdfe.model.v300.AutXML vAutXML) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.autXMLList.size()) {
            throw new IndexOutOfBoundsException("setAutXML: Index value '" + index + "' not in range [0.." + (this.autXMLList.size() - 1) + "]");
        }

        this.autXMLList.set(index, vAutXML);
    }

    /**
     * 
     * 
     * @param vAutXMLArray
     */
    public void setAutXML(final com.mercurio.lms.mdfe.model.v300.AutXML[] vAutXMLArray) {
        //-- copy array
        autXMLList.clear();

        for (int i = 0; i < vAutXMLArray.length; i++) {
                this.autXMLList.add(vAutXMLArray[i]);
        }
    }

    /**
     * Sets the value of field 'emit'. The field 'emit' has the
     * following description: Identificação do Emitente do
     * Manifesto
     * 
     * @param emit the value of field 'emit'.
     */
    public void setEmit(final com.mercurio.lms.mdfe.model.v300.Emit emit) {
        this.emit = emit;
    }

    /**
     * Sets the value of field 'id'. The field 'id' has the
     * following description: Identificador da tag a ser assinada
     * 
     * @param id the value of field 'id'.
     */
    public void setId(final java.lang.String id) {
        this.id = id;
    }

    /**
     * Sets the value of field 'ide'. The field 'ide' has the
     * following description: Identificação do MDF-e
     * 
     * @param ide the value of field 'ide'.
     */
    public void setIde(final com.mercurio.lms.mdfe.model.v300.Ide ide) {
        this.ide = ide;
    }

    /**
     * Sets the value of field 'infAdic'. The field 'infAdic' has
     * the following description: Informações Adicionais
     * 
     * @param infAdic the value of field 'infAdic'.
     */
    public void setInfAdic(final com.mercurio.lms.mdfe.model.v300.InfAdic infAdic) {
        this.infAdic = infAdic;
    }
    
    /**
     * Sets the value of field 'prodPred'. The field 'prodPred' has
     * the following description: Informa��es do Produto predominante da carga
     * 
     * @param prodPred the value of field 'prodPred'.
     */
    public void setProdPred(final com.mercurio.lms.mdfe.model.v300.ProdPred prodPred) {
    	this.prodPred = prodPred;
    }

    /**
     * Sets the value of field 'infDoc'. The field 'infDoc' has the
     * following description: Informações dos Documentos fiscais
     * vinculados ao manifesto
     * 
     * @param infDoc the value of field 'infDoc'.
     */
    public void setInfDoc(final com.mercurio.lms.mdfe.model.v300.InfDoc infDoc) {
        this.infDoc = infDoc;
    }
    
    public void setSeg(final int index,final com.mercurio.lms.mdfe.model.v300.Seg vSeg) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.segList.size()) {
            throw new IndexOutOfBoundsException("setSeg: Index value '" + index + "' not in range [0.." + (this.segList.size() - 1) + "]");
        }

        this.segList.set(index, vSeg);
    }
    
    /**
     * Sets the value of field 'seg'. The field 'seg' has the
     * following description: Informa��es de Seguro da Carga
     * 
     * @param seg the value of field 'seg'.
     */
    public void setSeg(final com.mercurio.lms.mdfe.model.v300.Seg[] vSegArray) {
    	//-- copy array
        segList.clear();

        for (int i = 0; i < vSegArray.length; i++) {
                this.segList.add(vSegArray[i]);
        }
    }

    /**
     * Sets the value of field 'infModal'. The field 'infModal' has
     * the following description: Informações do modal
     * 
     * @param infModal the value of field 'infModal'.
     */
    public void setInfModal(final com.mercurio.lms.mdfe.model.v300.InfModal infModal) {
        this.infModal = infModal;
    }

    /**
     * 
     * 
     * @param index
     * @param vLacres
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setLacres(final int index,final com.mercurio.lms.mdfe.model.v300.Lacres vLacres) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.lacresList.size()) {
            throw new IndexOutOfBoundsException("setLacres: Index value '" + index + "' not in range [0.." + (this.lacresList.size() - 1) + "]");
        }

        this.lacresList.set(index, vLacres);
    }

    /**
     * 
     * 
     * @param vLacresArray
     */
    public void setLacres(final com.mercurio.lms.mdfe.model.v300.Lacres[] vLacresArray) {
        //-- copy array
        lacresList.clear();

        for (int i = 0; i < vLacresArray.length; i++) {
                this.lacresList.add(vLacresArray[i]);
        }
    }

    /**
     * Sets the value of field 'tot'. The field 'tot' has the
     * following description: Totalizadores da carga transportada e
     * seus documentos fiscais
     * 
     * @param tot the value of field 'tot'.
     */
    public void setTot(final com.mercurio.lms.mdfe.model.v300.Tot tot) {
        this.tot = tot;
    }

    /**
     * Sets the value of field 'versao'. The field 'versao' has the
     * following description: Versão do leiaute
     * 
     * @param versao the value of field 'versao'.
     */
    public void setVersao(final java.lang.String versao) {
        this.versao = versao;
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
     * com.mercurio.lms.mdfe.model.v300.InfMDFe
     */
    public static com.mercurio.lms.mdfe.model.v300.InfMDFe unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.InfMDFe) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.InfMDFe.class, reader);
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
