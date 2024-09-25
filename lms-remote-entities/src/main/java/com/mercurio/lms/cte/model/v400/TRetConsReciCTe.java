/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

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
    private String _versao;

    /**
     * Identificação do Ambiente:
     * 1 - Produção
     * 2 - Homologação
     */
    private com.mercurio.lms.cte.model.v400.types.TAmb _tpAmb;

    /**
     * Versão do Aplicativo que processou a CT-e
     */
    private String _verAplic;

    /**
     * Número do Recibo Consultado
     */
    private String _nRec;

    /**
     * código do status do retorno da consulta.
     */
    private String _cStat;

    /**
     * Descrição literal do status do do retorno da consulta.
     */
    private String _xMotivo;

    /**
     * Idntificação da UF
     */
    private com.mercurio.lms.cte.model.v400.types.TCodUfIBGE _cUF;

    /**
     * Conjunto de CT-es processados, só existe nos casos em que o
     * lote consultado se encontra processado
     */
    private java.util.Vector<ProtCTe> _protCTeList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TRetConsReciCTe() {
        super();
        this._protCTeList = new java.util.Vector<ProtCTe>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     *
     *
     * @param vProtCTe
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProtCTe(
            final ProtCTe vProtCTe)
    throws IndexOutOfBoundsException {
        // check for the maximum size
        if (this._protCTeList.size() >= 50) {
            throw new IndexOutOfBoundsException("addProtCTe has a maximum of 50");
        }

        this._protCTeList.addElement(vProtCTe);
    }

    /**
     *
     *
     * @param index
     * @param vProtCTe
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProtCTe(
            final int index,
            final ProtCTe vProtCTe)
    throws IndexOutOfBoundsException {
        // check for the maximum size
        if (this._protCTeList.size() >= 50) {
            throw new IndexOutOfBoundsException("addProtCTe has a maximum of 50");
        }

        this._protCTeList.add(index, vProtCTe);
    }

    /**
     * Method enumerateProtCTe.
     *
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v400.ProtCTe elements
     */
    public java.util.Enumeration<? extends ProtCTe> enumerateProtCTe(
    ) {
        return this._protCTeList.elements();
    }

    /**
     * Returns the value of field 'cStat'. The field 'cStat' has
     * the following description: código do status do retorno da
     * consulta.
     *
     * @return the value of field 'CStat'.
     */
    public String getCStat(
    ) {
        return this._cStat;
    }

    /**
     * Returns the value of field 'cUF'. The field 'cUF' has the
     * following description: Idntificação da UF
     *
     * @return the value of field 'CUF'.
     */
    public com.mercurio.lms.cte.model.v400.types.TCodUfIBGE getCUF(
    ) {
        return this._cUF;
    }

    /**
     * Returns the value of field 'nRec'. The field 'nRec' has the
     * following description: Número do Recibo Consultado
     *
     * @return the value of field 'NRec'.
     */
    public String getNRec(
    ) {
        return this._nRec;
    }

    /**
     * Method getProtCTe.
     *
     * @param index
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v400.ProtCTe at the given index
     */
    public ProtCTe getProtCTe(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._protCTeList.size()) {
            throw new IndexOutOfBoundsException("getProtCTe: Index value '" + index + "' not in range [0.." + (this._protCTeList.size() - 1) + "]");
        }

        return (ProtCTe) _protCTeList.get(index);
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
    public ProtCTe[] getProtCTe(
    ) {
        ProtCTe[] array = new ProtCTe[0];
        return (ProtCTe[]) this._protCTeList.toArray(array);
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
    public com.mercurio.lms.cte.model.v400.types.TAmb getTpAmb(
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
    public String getVerAplic(
    ) {
        return this._verAplic;
    }

    /**
     * Returns the value of field 'versao'.
     *
     * @return the value of field 'Versao'.
     */
    public String getVersao(
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
    public String getXMotivo(
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
            final ProtCTe vProtCTe) {
        boolean removed = _protCTeList.remove(vProtCTe);
        return removed;
    }

    /**
     * Method removeProtCTeAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public ProtCTe removeProtCTeAt(
            final int index) {
        Object obj = this._protCTeList.remove(index);
        return (ProtCTe) obj;
    }

    /**
     * Sets the value of field 'cStat'. The field 'cStat' has the
     * following description: código do status do retorno da
     * consulta.
     *
     * @param cStat the value of field 'cStat'.
     */
    public void setCStat(
            final String cStat) {
        this._cStat = cStat;
    }

    /**
     * Sets the value of field 'cUF'. The field 'cUF' has the
     * following description: Idntificação da UF
     *
     * @param cUF the value of field 'cUF'.
     */
    public void setCUF(
            final com.mercurio.lms.cte.model.v400.types.TCodUfIBGE cUF) {
        this._cUF = cUF;
    }

    /**
     * Sets the value of field 'nRec'. The field 'nRec' has the
     * following description: Número do Recibo Consultado
     *
     * @param nRec the value of field 'nRec'.
     */
    public void setNRec(
            final String nRec) {
        this._nRec = nRec;
    }

    /**
     *
     *
     * @param index
     * @param vProtCTe
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setProtCTe(
            final int index,
            final ProtCTe vProtCTe)
    throws IndexOutOfBoundsException {
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
            final ProtCTe[] vProtCTeArray) {
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
            final com.mercurio.lms.cte.model.v400.types.TAmb tpAmb) {
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
            final String verAplic) {
        this._verAplic = verAplic;
    }

    /**
     * Sets the value of field 'versao'.
     *
     * @param versao the value of field 'versao'.
     */
    public void setVersao(
            final String versao) {
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
            final String xMotivo) {
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
     * com.mercurio.lms.cte.model.v400.TRetConsReciCTe
     */
    public static TRetConsReciCTe unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (TRetConsReciCTe) org.exolab.castor.xml.Unmarshaller.unmarshal(TRetConsReciCTe.class, reader);
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
