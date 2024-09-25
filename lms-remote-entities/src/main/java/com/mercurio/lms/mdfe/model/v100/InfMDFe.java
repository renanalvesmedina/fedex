/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100;

/**
 * Informações do MDF-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfMDFe implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Versão do leiaute
     */
    private java.lang.String _versao;

    /**
     * Identificador da tag a ser assinada
     */
    private java.lang.String _id;

    /**
     * Identificação do MDF-e
     */
    private com.mercurio.lms.mdfe.model.v100.Ide _ide;

    /**
     * Identificação do Emitente do Manifesto
     */
    private com.mercurio.lms.mdfe.model.v100.Emit _emit;

    /**
     * Informações do modal
     */
    private com.mercurio.lms.mdfe.model.v100.InfModal _infModal;

    /**
     * Informações dos Documentos fiscais vinculados ao manifesto
     */
    private com.mercurio.lms.mdfe.model.v100.InfDoc _infDoc;

    /**
     * Totalizadores da carga transportada e seus documentos fiscais
     */
    private com.mercurio.lms.mdfe.model.v100.Tot _tot;

    /**
     * Lacres do MDF-e
     */
    private java.util.List<com.mercurio.lms.mdfe.model.v100.Lacres> _lacresList;

    /**
     * Informações Adicionais
     */
    private com.mercurio.lms.mdfe.model.v100.InfAdic _infAdic;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfMDFe() {
        super();
        this._lacresList = new java.util.ArrayList<com.mercurio.lms.mdfe.model.v100.Lacres>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vLacres
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacres(
            final com.mercurio.lms.mdfe.model.v100.Lacres vLacres)
    throws java.lang.IndexOutOfBoundsException {
        this._lacresList.add(vLacres);
    }

    /**
     * 
     * 
     * @param index
     * @param vLacres
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacres(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.Lacres vLacres)
    throws java.lang.IndexOutOfBoundsException {
        this._lacresList.add(index, vLacres);
    }

    /**
     * Method enumerateLacres.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v100.Lacres> enumerateLacres(
    ) {
        return java.util.Collections.enumeration(this._lacresList);
    }

    /**
     * Returns the value of field 'emit'. The field 'emit' has the
     * following description: Identificação do Emitente do
     * Manifesto
     * 
     * @return the value of field 'Emit'.
     */
    public com.mercurio.lms.mdfe.model.v100.Emit getEmit(
    ) {
        return this._emit;
    }

    /**
     * Returns the value of field 'id'. The field 'id' has the
     * following description: Identificador da tag a ser assinada
     * 
     * @return the value of field 'Id'.
     */
    public java.lang.String getId(
    ) {
        return this._id;
    }

    /**
     * Returns the value of field 'ide'. The field 'ide' has the
     * following description: Identificação do MDF-e
     * 
     * @return the value of field 'Ide'.
     */
    public com.mercurio.lms.mdfe.model.v100.Ide getIde(
    ) {
        return this._ide;
    }

    /**
     * Returns the value of field 'infAdic'. The field 'infAdic'
     * has the following description: Informações Adicionais
     * 
     * @return the value of field 'InfAdic'.
     */
    public com.mercurio.lms.mdfe.model.v100.InfAdic getInfAdic(
    ) {
        return this._infAdic;
    }

    /**
     * Returns the value of field 'infDoc'. The field 'infDoc' has
     * the following description: Informações dos Documentos
     * fiscais vinculados ao manifesto
     * 
     * @return the value of field 'InfDoc'.
     */
    public com.mercurio.lms.mdfe.model.v100.InfDoc getInfDoc(
    ) {
        return this._infDoc;
    }

    /**
     * Returns the value of field 'infModal'. The field 'infModal'
     * has the following description: Informações do modal
     * 
     * @return the value of field 'InfModal'.
     */
    public com.mercurio.lms.mdfe.model.v100.InfModal getInfModal(
    ) {
        return this._infModal;
    }

    /**
     * Method getLacres.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.Lacres at the given index
     */
    public com.mercurio.lms.mdfe.model.v100.Lacres getLacres(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._lacresList.size()) {
            throw new IndexOutOfBoundsException("getLacres: Index value '" + index + "' not in range [0.." + (this._lacresList.size() - 1) + "]");
        }

        return (com.mercurio.lms.mdfe.model.v100.Lacres) _lacresList.get(index);
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
    public com.mercurio.lms.mdfe.model.v100.Lacres[] getLacres(
    ) {
        com.mercurio.lms.mdfe.model.v100.Lacres[] array = new com.mercurio.lms.mdfe.model.v100.Lacres[0];
        return (com.mercurio.lms.mdfe.model.v100.Lacres[]) this._lacresList.toArray(array);
    }

    /**
     * Method getLacresCount.
     * 
     * @return the size of this collection
     */
    public int getLacresCount(
    ) {
        return this._lacresList.size();
    }

    /**
     * Returns the value of field 'tot'. The field 'tot' has the
     * following description: Totalizadores da carga transportada e
     * seus documentos fiscais
     * 
     * @return the value of field 'Tot'.
     */
    public com.mercurio.lms.mdfe.model.v100.Tot getTot(
    ) {
        return this._tot;
    }

    /**
     * Returns the value of field 'versao'. The field 'versao' has
     * the following description: Versão do leiaute
     * 
     * @return the value of field 'Versao'.
     */
    public java.lang.String getVersao(
    ) {
        return this._versao;
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
     * Method iterateLacres.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.mdfe.model.v100.Lacres> iterateLacres(
    ) {
        return this._lacresList.iterator();
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
    public void removeAllLacres(
    ) {
        this._lacresList.clear();
    }

    /**
     * Method removeLacres.
     * 
     * @param vLacres
     * @return true if the object was removed from the collection.
     */
    public boolean removeLacres(
            final com.mercurio.lms.mdfe.model.v100.Lacres vLacres) {
        boolean removed = _lacresList.remove(vLacres);
        return removed;
    }

    /**
     * Method removeLacresAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v100.Lacres removeLacresAt(
            final int index) {
        java.lang.Object obj = this._lacresList.remove(index);
        return (com.mercurio.lms.mdfe.model.v100.Lacres) obj;
    }

    /**
     * Sets the value of field 'emit'. The field 'emit' has the
     * following description: Identificação do Emitente do
     * Manifesto
     * 
     * @param emit the value of field 'emit'.
     */
    public void setEmit(
            final com.mercurio.lms.mdfe.model.v100.Emit emit) {
        this._emit = emit;
    }

    /**
     * Sets the value of field 'id'. The field 'id' has the
     * following description: Identificador da tag a ser assinada
     * 
     * @param id the value of field 'id'.
     */
    public void setId(
            final java.lang.String id) {
        this._id = id;
    }

    /**
     * Sets the value of field 'ide'. The field 'ide' has the
     * following description: Identificação do MDF-e
     * 
     * @param ide the value of field 'ide'.
     */
    public void setIde(
            final com.mercurio.lms.mdfe.model.v100.Ide ide) {
        this._ide = ide;
    }

    /**
     * Sets the value of field 'infAdic'. The field 'infAdic' has
     * the following description: Informações Adicionais
     * 
     * @param infAdic the value of field 'infAdic'.
     */
    public void setInfAdic(
            final com.mercurio.lms.mdfe.model.v100.InfAdic infAdic) {
        this._infAdic = infAdic;
    }

    /**
     * Sets the value of field 'infDoc'. The field 'infDoc' has the
     * following description: Informações dos Documentos fiscais
     * vinculados ao manifesto
     * 
     * @param infDoc the value of field 'infDoc'.
     */
    public void setInfDoc(
            final com.mercurio.lms.mdfe.model.v100.InfDoc infDoc) {
        this._infDoc = infDoc;
    }

    /**
     * Sets the value of field 'infModal'. The field 'infModal' has
     * the following description: Informações do modal
     * 
     * @param infModal the value of field 'infModal'.
     */
    public void setInfModal(
            final com.mercurio.lms.mdfe.model.v100.InfModal infModal) {
        this._infModal = infModal;
    }

    /**
     * 
     * 
     * @param index
     * @param vLacres
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setLacres(
            final int index,
            final com.mercurio.lms.mdfe.model.v100.Lacres vLacres)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._lacresList.size()) {
            throw new IndexOutOfBoundsException("setLacres: Index value '" + index + "' not in range [0.." + (this._lacresList.size() - 1) + "]");
        }

        this._lacresList.set(index, vLacres);
    }

    /**
     * 
     * 
     * @param vLacresArray
     */
    public void setLacres(
            final com.mercurio.lms.mdfe.model.v100.Lacres[] vLacresArray) {
        //-- copy array
        _lacresList.clear();

        for (int i = 0; i < vLacresArray.length; i++) {
                this._lacresList.add(vLacresArray[i]);
        }
    }

    /**
     * Sets the value of field 'tot'. The field 'tot' has the
     * following description: Totalizadores da carga transportada e
     * seus documentos fiscais
     * 
     * @param tot the value of field 'tot'.
     */
    public void setTot(
            final com.mercurio.lms.mdfe.model.v100.Tot tot) {
        this._tot = tot;
    }

    /**
     * Sets the value of field 'versao'. The field 'versao' has the
     * following description: Versão do leiaute
     * 
     * @param versao the value of field 'versao'.
     */
    public void setVersao(
            final java.lang.String versao) {
        this._versao = versao;
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
     * org.exolab.castor.builder.binding.InfMDFe
     */
    public static com.mercurio.lms.mdfe.model.v100.InfMDFe unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100.InfMDFe) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100.InfMDFe.class, reader);
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
