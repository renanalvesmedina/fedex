/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Tipo Retorno do Pedido de Consulta do Recibo do Lote de CT-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TRetConsReciCTe implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _versao.
     */
    private java.lang.String _versao;

    /**
     * Identificação do Ambiente:
     * 1 - Produção
     * 2 - Homologação
     */
    private com.mercurio.lms.cte.model.v104.types.TAmb _tpAmb;

    /**
     * Versão do Aplicativo que processou a CT-e
     */
    private java.lang.String _verAplic;

    /**
     * Número do Recibo Consultado
     */
    private java.lang.String _nRec;

    /**
     * código do status do retorno da consulta.
     */
    private java.lang.String _cStat;

    /**
     * Descrição literal do status do do retorno da consulta.
     */
    private java.lang.String _xMotivo;

    /**
     * Idntificação da UF
     */
    private com.mercurio.lms.cte.model.v104.types.TCodUfIBGE _cUF;

    /**
     * Conjunto de CT-es processados, só existe nos casos em que o
     * lote consultado se encontra processado
     */
    private java.util.List<com.mercurio.lms.cte.model.v104.ProtCTe> _protCTeList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TRetConsReciCTe() {
        super();
        this._protCTeList = new java.util.ArrayList<com.mercurio.lms.cte.model.v104.ProtCTe>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vProtCTe
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProtCTe(
            final com.mercurio.lms.cte.model.v104.ProtCTe vProtCTe)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._protCTeList.size() >= 50) {
            throw new IndexOutOfBoundsException("addProtCTe has a maximum of 50");
        }

        this._protCTeList.add(vProtCTe);
    }

    /**
     * 
     * 
     * @param index
     * @param vProtCTe
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProtCTe(
            final int index,
            final com.mercurio.lms.cte.model.v104.ProtCTe vProtCTe)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._protCTeList.size() >= 50) {
            throw new IndexOutOfBoundsException("addProtCTe has a maximum of 50");
        }

        this._protCTeList.add(index, vProtCTe);
    }

    /**
     * Method enumerateProtCTe.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v104.ProtCTe> enumerateProtCTe(
    ) {
        return java.util.Collections.enumeration(this._protCTeList);
    }

    /**
     * Returns the value of field 'cStat'. The field 'cStat' has
     * the following description: código do status do retorno da
     * consulta.
     * 
     * @return the value of field 'CStat'.
     */
    public java.lang.String getCStat(
    ) {
        return this._cStat;
    }

    /**
     * Returns the value of field 'cUF'. The field 'cUF' has the
     * following description: Idntificação da UF
     * 
     * @return the value of field 'CUF'.
     */
    public com.mercurio.lms.cte.model.v104.types.TCodUfIBGE getCUF(
    ) {
        return this._cUF;
    }

    /**
     * Returns the value of field 'nRec'. The field 'nRec' has the
     * following description: Número do Recibo Consultado
     * 
     * @return the value of field 'NRec'.
     */
    public java.lang.String getNRec(
    ) {
        return this._nRec;
    }

    /**
     * Method getProtCTe.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.mercurio.lms.cte.model.ProtCTe
     * at the given index
     */
    public com.mercurio.lms.cte.model.v104.ProtCTe getProtCTe(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._protCTeList.size()) {
            throw new IndexOutOfBoundsException("getProtCTe: Index value '" + index + "' not in range [0.." + (this._protCTeList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v104.ProtCTe) _protCTeList.get(index);
    }

    /**
     * Method getProtCTe.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v104.ProtCTe[] getProtCTe(
    ) {
        com.mercurio.lms.cte.model.v104.ProtCTe[] array = new com.mercurio.lms.cte.model.v104.ProtCTe[0];
        return (com.mercurio.lms.cte.model.v104.ProtCTe[]) this._protCTeList.toArray(array);
    }

    /**
     * Method getProtCTeCount.
     * 
     * @return the size of this collection
     */
    public int getProtCTeCount(
    ) {
        return this._protCTeList.size();
    }

    /**
     * Returns the value of field 'tpAmb'. The field 'tpAmb' has
     * the following description: Identificação do Ambiente:
     * 1 - Produção
     * 2 - Homologação
     * 
     * @return the value of field 'TpAmb'.
     */
    public com.mercurio.lms.cte.model.v104.types.TAmb getTpAmb(
    ) {
        return this._tpAmb;
    }

    /**
     * Returns the value of field 'verAplic'. The field 'verAplic'
     * has the following description: Versão do Aplicativo que
     * processou a CT-e
     * 
     * @return the value of field 'VerAplic'.
     */
    public java.lang.String getVerAplic(
    ) {
        return this._verAplic;
    }

    /**
     * Returns the value of field 'versao'.
     * 
     * @return the value of field 'Versao'.
     */
    public java.lang.String getVersao(
    ) {
        return this._versao;
    }

    /**
     * Returns the value of field 'xMotivo'. The field 'xMotivo'
     * has the following description: Descrição literal do status
     * do do retorno da consulta.
     * 
     * @return the value of field 'XMotivo'.
     */
    public java.lang.String getXMotivo(
    ) {
        return this._xMotivo;
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
     * Method iterateProtCTe.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v104.ProtCTe> iterateProtCTe(
    ) {
        return this._protCTeList.iterator();
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
    public void removeAllProtCTe(
    ) {
        this._protCTeList.clear();
    }

    /**
     * Method removeProtCTe.
     * 
     * @param vProtCTe
     * @return true if the object was removed from the collection.
     */
    public boolean removeProtCTe(
            final com.mercurio.lms.cte.model.v104.ProtCTe vProtCTe) {
        boolean removed = _protCTeList.remove(vProtCTe);
        return removed;
    }

    /**
     * Method removeProtCTeAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v104.ProtCTe removeProtCTeAt(
            final int index) {
        java.lang.Object obj = this._protCTeList.remove(index);
        return (com.mercurio.lms.cte.model.v104.ProtCTe) obj;
    }

    /**
     * Sets the value of field 'cStat'. The field 'cStat' has the
     * following description: código do status do retorno da
     * consulta.
     * 
     * @param cStat the value of field 'cStat'.
     */
    public void setCStat(
            final java.lang.String cStat) {
        this._cStat = cStat;
    }

    /**
     * Sets the value of field 'cUF'. The field 'cUF' has the
     * following description: Idntificação da UF
     * 
     * @param cUF the value of field 'cUF'.
     */
    public void setCUF(
            final com.mercurio.lms.cte.model.v104.types.TCodUfIBGE cUF) {
        this._cUF = cUF;
    }

    /**
     * Sets the value of field 'nRec'. The field 'nRec' has the
     * following description: Número do Recibo Consultado
     * 
     * @param nRec the value of field 'nRec'.
     */
    public void setNRec(
            final java.lang.String nRec) {
        this._nRec = nRec;
    }

    /**
     * 
     * 
     * @param index
     * @param vProtCTe
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setProtCTe(
            final int index,
            final com.mercurio.lms.cte.model.v104.ProtCTe vProtCTe)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._protCTeList.size()) {
            throw new IndexOutOfBoundsException("setProtCTe: Index value '" + index + "' not in range [0.." + (this._protCTeList.size() - 1) + "]");
        }

        this._protCTeList.set(index, vProtCTe);
    }

    /**
     * 
     * 
     * @param vProtCTeArray
     */
    public void setProtCTe(
            final com.mercurio.lms.cte.model.v104.ProtCTe[] vProtCTeArray) {
        //-- copy array
        _protCTeList.clear();

        for (int i = 0; i < vProtCTeArray.length; i++) {
                this._protCTeList.add(vProtCTeArray[i]);
        }
    }

    /**
     * Sets the value of field 'tpAmb'. The field 'tpAmb' has the
     * following description: Identificação do Ambiente:
     * 1 - Produção
     * 2 - Homologação
     * 
     * @param tpAmb the value of field 'tpAmb'.
     */
    public void setTpAmb(
            final com.mercurio.lms.cte.model.v104.types.TAmb tpAmb) {
        this._tpAmb = tpAmb;
    }

    /**
     * Sets the value of field 'verAplic'. The field 'verAplic' has
     * the following description: Versão do Aplicativo que
     * processou a CT-e
     * 
     * @param verAplic the value of field 'verAplic'.
     */
    public void setVerAplic(
            final java.lang.String verAplic) {
        this._verAplic = verAplic;
    }

    /**
     * Sets the value of field 'versao'.
     * 
     * @param versao the value of field 'versao'.
     */
    public void setVersao(
            final java.lang.String versao) {
        this._versao = versao;
    }

    /**
     * Sets the value of field 'xMotivo'. The field 'xMotivo' has
     * the following description: Descrição literal do status do
     * do retorno da consulta.
     * 
     * @param xMotivo the value of field 'xMotivo'.
     */
    public void setXMotivo(
            final java.lang.String xMotivo) {
        this._xMotivo = xMotivo;
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
     * com.mercurio.lms.cte.model.TRetConsReciCTe
     */
    public static com.mercurio.lms.cte.model.v104.TRetConsReciCTe unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.TRetConsReciCTe) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.TRetConsReciCTe.class, reader);
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
