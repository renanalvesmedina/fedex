/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v300;

/**
 * Informações do CT-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfCte implements java.io.Serializable {


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
     * Identificação do CT-e
     */
    private com.mercurio.lms.cte.model.v300.Ide _ide;

    /**
     * Dados complementares do CT-e para fins operacionais ou
     * comerciais
     */
    private com.mercurio.lms.cte.model.v300.Compl _compl;

    /**
     * Identificação do Emitente do CT-e
     */
    private com.mercurio.lms.cte.model.v300.Emit _emit;

    /**
     * Informações do Remetente das mercadorias transportadas
     * pelo CT-e
     */
    private com.mercurio.lms.cte.model.v300.Rem _rem;

    /**
     * Informações do Expedidor da Carga
     */
    private com.mercurio.lms.cte.model.v300.Exped _exped;

    /**
     * Informações do Recebedor da Carga
     */
    private com.mercurio.lms.cte.model.v300.Receb _receb;

    /**
     * Informações do Destinatário do CT-e
     */
    private com.mercurio.lms.cte.model.v300.Dest _dest;

    /**
     * Valores da Prestação de Serviço
     */
    private com.mercurio.lms.cte.model.v300.VPrest _vPrest;

    /**
     * Informações relativas aos Impostos
     */
    private com.mercurio.lms.cte.model.v300.Imp _imp;

    /**
     * Field _infCteChoice.
     */
    private com.mercurio.lms.cte.model.v300.InfCteChoice _infCteChoice;

    /**
     * Autorizados para download do XML do DF-e
     */
    private java.util.Vector<com.mercurio.lms.cte.model.v300.AutXML> _autXMLList;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfCte() {
        super();
        this._autXMLList = new java.util.Vector<com.mercurio.lms.cte.model.v300.AutXML>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vAutXML
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addAutXML(
            final com.mercurio.lms.cte.model.v300.AutXML vAutXML)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._autXMLList.size() >= 10) {
            throw new IndexOutOfBoundsException("addAutXML has a maximum of 10");
        }

        this._autXMLList.addElement(vAutXML);
    }

    /**
     * 
     * 
     * @param index
     * @param vAutXML
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addAutXML(
            final int index,
            final com.mercurio.lms.cte.model.v300.AutXML vAutXML)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._autXMLList.size() >= 10) {
            throw new IndexOutOfBoundsException("addAutXML has a maximum of 10");
        }

        this._autXMLList.add(index, vAutXML);
    }

    /**
     * Method enumerateAutXML.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v300.AutXML elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v300.AutXML> enumerateAutXML(
    ) {
        return this._autXMLList.elements();
    }

    /**
     * Method getAutXML.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v300.AutXML at the given index
     */
    public com.mercurio.lms.cte.model.v300.AutXML getAutXML(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._autXMLList.size()) {
            throw new IndexOutOfBoundsException("getAutXML: Index value '" + index + "' not in range [0.." + (this._autXMLList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v300.AutXML) _autXMLList.get(index);
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
    public com.mercurio.lms.cte.model.v300.AutXML[] getAutXML(
    ) {
        com.mercurio.lms.cte.model.v300.AutXML[] array = new com.mercurio.lms.cte.model.v300.AutXML[0];
        return (com.mercurio.lms.cte.model.v300.AutXML[]) this._autXMLList.toArray(array);
    }

    /**
     * Method getAutXMLCount.
     * 
     * @return the size of this collection
     */
    public int getAutXMLCount(
    ) {
        return this._autXMLList.size();
    }

    /**
     * Returns the value of field 'compl'. The field 'compl' has
     * the following description: Dados complementares do CT-e para
     * fins operacionais ou comerciais
     * 
     * @return the value of field 'Compl'.
     */
    public com.mercurio.lms.cte.model.v300.Compl getCompl(
    ) {
        return this._compl;
    }

    /**
     * Returns the value of field 'dest'. The field 'dest' has the
     * following description: Informações do Destinatário do
     * CT-e
     * 
     * @return the value of field 'Dest'.
     */
    public com.mercurio.lms.cte.model.v300.Dest getDest(
    ) {
        return this._dest;
    }

    /**
     * Returns the value of field 'emit'. The field 'emit' has the
     * following description: Identificação do Emitente do CT-e
     * 
     * @return the value of field 'Emit'.
     */
    public com.mercurio.lms.cte.model.v300.Emit getEmit(
    ) {
        return this._emit;
    }

    /**
     * Returns the value of field 'exped'. The field 'exped' has
     * the following description: Informações do Expedidor da
     * Carga
     * 
     * @return the value of field 'Exped'.
     */
    public com.mercurio.lms.cte.model.v300.Exped getExped(
    ) {
        return this._exped;
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
     * following description: Identificação do CT-e
     * 
     * @return the value of field 'Ide'.
     */
    public com.mercurio.lms.cte.model.v300.Ide getIde(
    ) {
        return this._ide;
    }

    /**
     * Returns the value of field 'imp'. The field 'imp' has the
     * following description: Informações relativas aos Impostos
     * 
     * @return the value of field 'Imp'.
     */
    public com.mercurio.lms.cte.model.v300.Imp getImp(
    ) {
        return this._imp;
    }

    /**
     * Returns the value of field 'infCteChoice'.
     * 
     * @return the value of field 'InfCteChoice'.
     */
    public com.mercurio.lms.cte.model.v300.InfCteChoice getInfCteChoice(
    ) {
        return this._infCteChoice;
    }

    /**
     * Returns the value of field 'receb'. The field 'receb' has
     * the following description: Informações do Recebedor da
     * Carga
     * 
     * @return the value of field 'Receb'.
     */
    public com.mercurio.lms.cte.model.v300.Receb getReceb(
    ) {
        return this._receb;
    }

    /**
     * Returns the value of field 'rem'. The field 'rem' has the
     * following description: Informações do Remetente das
     * mercadorias transportadas pelo CT-e
     * 
     * @return the value of field 'Rem'.
     */
    public com.mercurio.lms.cte.model.v300.Rem getRem(
    ) {
        return this._rem;
    }

    /**
     * Returns the value of field 'vPrest'. The field 'vPrest' has
     * the following description: Valores da Prestação de
     * Serviço
     * 
     * @return the value of field 'VPrest'.
     */
    public com.mercurio.lms.cte.model.v300.VPrest getVPrest(
    ) {
        return this._vPrest;
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
    public void removeAllAutXML(
    ) {
        this._autXMLList.clear();
    }

    /**
     * Method removeAutXML.
     * 
     * @param vAutXML
     * @return true if the object was removed from the collection.
     */
    public boolean removeAutXML(
            final com.mercurio.lms.cte.model.v300.AutXML vAutXML) {
        boolean removed = _autXMLList.remove(vAutXML);
        return removed;
    }

    /**
     * Method removeAutXMLAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v300.AutXML removeAutXMLAt(
            final int index) {
        java.lang.Object obj = this._autXMLList.remove(index);
        return (com.mercurio.lms.cte.model.v300.AutXML) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vAutXML
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setAutXML(
            final int index,
            final com.mercurio.lms.cte.model.v300.AutXML vAutXML)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._autXMLList.size()) {
            throw new IndexOutOfBoundsException("setAutXML: Index value '" + index + "' not in range [0.." + (this._autXMLList.size() - 1) + "]");
        }

        this._autXMLList.set(index, vAutXML);
    }

    /**
     * 
     * 
     * @param vAutXMLArray
     */
    public void setAutXML(
            final com.mercurio.lms.cte.model.v300.AutXML[] vAutXMLArray) {
        //-- copy array
        _autXMLList.clear();

        for (int i = 0; i < vAutXMLArray.length; i++) {
                this._autXMLList.add(vAutXMLArray[i]);
        }
    }

    /**
     * Sets the value of field 'compl'. The field 'compl' has the
     * following description: Dados complementares do CT-e para
     * fins operacionais ou comerciais
     * 
     * @param compl the value of field 'compl'.
     */
    public void setCompl(
            final com.mercurio.lms.cte.model.v300.Compl compl) {
        this._compl = compl;
    }

    /**
     * Sets the value of field 'dest'. The field 'dest' has the
     * following description: Informações do Destinatário do
     * CT-e
     * 
     * @param dest the value of field 'dest'.
     */
    public void setDest(
            final com.mercurio.lms.cte.model.v300.Dest dest) {
        this._dest = dest;
    }

    /**
     * Sets the value of field 'emit'. The field 'emit' has the
     * following description: Identificação do Emitente do CT-e
     * 
     * @param emit the value of field 'emit'.
     */
    public void setEmit(
            final com.mercurio.lms.cte.model.v300.Emit emit) {
        this._emit = emit;
    }

    /**
     * Sets the value of field 'exped'. The field 'exped' has the
     * following description: Informações do Expedidor da Carga
     * 
     * @param exped the value of field 'exped'.
     */
    public void setExped(
            final com.mercurio.lms.cte.model.v300.Exped exped) {
        this._exped = exped;
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
     * following description: Identificação do CT-e
     * 
     * @param ide the value of field 'ide'.
     */
    public void setIde(
            final com.mercurio.lms.cte.model.v300.Ide ide) {
        this._ide = ide;
    }

    /**
     * Sets the value of field 'imp'. The field 'imp' has the
     * following description: Informações relativas aos Impostos
     * 
     * @param imp the value of field 'imp'.
     */
    public void setImp(
            final com.mercurio.lms.cte.model.v300.Imp imp) {
        this._imp = imp;
    }

    /**
     * Sets the value of field 'infCteChoice'.
     * 
     * @param infCteChoice the value of field 'infCteChoice'.
     */
    public void setInfCteChoice(
            final com.mercurio.lms.cte.model.v300.InfCteChoice infCteChoice) {
        this._infCteChoice = infCteChoice;
    }

    /**
     * Sets the value of field 'receb'. The field 'receb' has the
     * following description: Informações do Recebedor da Carga
     * 
     * @param receb the value of field 'receb'.
     */
    public void setReceb(
            final com.mercurio.lms.cte.model.v300.Receb receb) {
        this._receb = receb;
    }

    /**
     * Sets the value of field 'rem'. The field 'rem' has the
     * following description: Informações do Remetente das
     * mercadorias transportadas pelo CT-e
     * 
     * @param rem the value of field 'rem'.
     */
    public void setRem(
            final com.mercurio.lms.cte.model.v300.Rem rem) {
        this._rem = rem;
    }

    /**
     * Sets the value of field 'vPrest'. The field 'vPrest' has the
     * following description: Valores da Prestação de Serviço
     * 
     * @param vPrest the value of field 'vPrest'.
     */
    public void setVPrest(
            final com.mercurio.lms.cte.model.v300.VPrest vPrest) {
        this._vPrest = vPrest;
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
     * @return the unmarshaled com.mercurio.lms.cte.model.v300.InfCt
     */
    public static com.mercurio.lms.cte.model.v300.InfCte unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v300.InfCte) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v300.InfCte.class, reader);
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
