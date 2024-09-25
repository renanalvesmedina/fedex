/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Dados complementares do CT-e para fins operacionais ou
 * comerciais
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Compl implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Característica adicional do transporte
     */
    private String _xCaracAd;

    /**
     * Característica adicional do serviço
     */
    private String _xCaracSer;

    /**
     * Funcionário emissor do CTe
     */
    private String _xEmi;

    /**
     * Previsão do fluxo da carga
     */
    private Fluxo _fluxo;

    /**
     * Informações ref. a previsão de entrega
     */
    private Entrega _entrega;

    /**
     * Município de origem para efeito de cálculo do frete
     */
    private String _origCalc;

    /**
     * Município de destino para efeito de cálculo do frete
     */
    private String _destCalc;

    /**
     * Observações Gerais
     */
    private String _xObs;

    /**
     * Campo de uso livre do contribuinte
     */
    private java.util.Vector<ObsCont> _obsContList;

    /**
     * Campo de uso livre do contribuinte
     */
    private java.util.Vector<ObsFisco> _obsFiscoList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Compl() {
        super();
        this._obsContList = new java.util.Vector<ObsCont>();
        this._obsFiscoList = new java.util.Vector<ObsFisco>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     *
     *
     * @param vObsCont
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addObsCont(
            final ObsCont vObsCont)
    throws IndexOutOfBoundsException {
        // check for the maximum size
        if (this._obsContList.size() >= 10) {
            throw new IndexOutOfBoundsException("addObsCont has a maximum of 10");
        }

        this._obsContList.addElement(vObsCont);
    }

    /**
     *
     *
     * @param index
     * @param vObsCont
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addObsCont(
            final int index,
            final ObsCont vObsCont)
    throws IndexOutOfBoundsException {
        // check for the maximum size
        if (this._obsContList.size() >= 10) {
            throw new IndexOutOfBoundsException("addObsCont has a maximum of 10");
        }

        this._obsContList.add(index, vObsCont);
    }

    /**
     *
     *
     * @param vObsFisco
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addObsFisco(
            final ObsFisco vObsFisco)
    throws IndexOutOfBoundsException {
        // check for the maximum size
        if (this._obsFiscoList.size() >= 10) {
            throw new IndexOutOfBoundsException("addObsFisco has a maximum of 10");
        }

        this._obsFiscoList.addElement(vObsFisco);
    }

    /**
     *
     *
     * @param index
     * @param vObsFisco
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addObsFisco(
            final int index,
            final ObsFisco vObsFisco)
    throws IndexOutOfBoundsException {
        // check for the maximum size
        if (this._obsFiscoList.size() >= 10) {
            throw new IndexOutOfBoundsException("addObsFisco has a maximum of 10");
        }

        this._obsFiscoList.add(index, vObsFisco);
    }

    /**
     * Method enumerateObsCont.
     *
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v400.ObsCont elements
     */
    public java.util.Enumeration<? extends ObsCont> enumerateObsCont(
    ) {
        return this._obsContList.elements();
    }

    /**
     * Method enumerateObsFisco.
     *
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v400.ObsFisco elements
     */
    public java.util.Enumeration<? extends ObsFisco> enumerateObsFisco(
    ) {
        return this._obsFiscoList.elements();
    }

    /**
     * Returns the value of field 'destCalc'. The field 'destCalc'
     * has the following description: Município de destino para
     * efeito de cálculo do frete
     *
     * @return the value of field 'DestCalc'.
     */
    public String getDestCalc(
    ) {
        return this._destCalc;
    }

    /**
     * Returns the value of field 'entrega'. The field 'entrega'
     * has the following description: Informações ref. a
     * previsão de entrega
     *
     * @return the value of field 'Entrega'.
     */
    public Entrega getEntrega(
    ) {
        return this._entrega;
    }

    /**
     * Returns the value of field 'fluxo'. The field 'fluxo' has
     * the following description: Previsão do fluxo da carga
     *
     * @return the value of field 'Fluxo'.
     */
    public Fluxo getFluxo(
    ) {
        return this._fluxo;
    }

    /**
     * Method getObsCont.
     *
     * @param index
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v400.ObsCont at the given index
     */
    public ObsCont getObsCont(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._obsContList.size()) {
            throw new IndexOutOfBoundsException("getObsCont: Index value '" + index + "' not in range [0.." + (this._obsContList.size() - 1) + "]");
        }

        return (ObsCont) _obsContList.get(index);
    }

    /**
     * Method getObsCont.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     *
     * @return this collection as an Array
     */
    public ObsCont[] getObsCont(
    ) {
        ObsCont[] array = new ObsCont[0];
        return (ObsCont[]) this._obsContList.toArray(array);
    }

    /**
     * Method getObsContCount.
     *
     * @return the size of this collection
     */
    public int getObsContCount(
    ) {
        return this._obsContList.size();
    }

    /**
     * Method getObsFisco.
     *
     * @param index
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v400.ObsFisco at the given index
     */
    public ObsFisco getObsFisco(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._obsFiscoList.size()) {
            throw new IndexOutOfBoundsException("getObsFisco: Index value '" + index + "' not in range [0.." + (this._obsFiscoList.size() - 1) + "]");
        }

        return (ObsFisco) _obsFiscoList.get(index);
    }

    /**
     * Method getObsFisco.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     *
     * @return this collection as an Array
     */
    public ObsFisco[] getObsFisco(
    ) {
        ObsFisco[] array = new ObsFisco[0];
        return (ObsFisco[]) this._obsFiscoList.toArray(array);
    }

    /**
     * Method getObsFiscoCount.
     *
     * @return the size of this collection
     */
    public int getObsFiscoCount(
    ) {
        return this._obsFiscoList.size();
    }

    /**
     * Returns the value of field 'origCalc'. The field 'origCalc'
     * has the following description: Município de origem para
     * efeito de cálculo do frete
     *
     * @return the value of field 'OrigCalc'.
     */
    public String getOrigCalc(
    ) {
        return this._origCalc;
    }

    /**
     * Returns the value of field 'xCaracAd'. The field 'xCaracAd'
     * has the following description: Característica adicional do
     * transporte
     *
     * @return the value of field 'XCaracAd'.
     */
    public String getXCaracAd(
    ) {
        return this._xCaracAd;
    }

    /**
     * Returns the value of field 'xCaracSer'. The field
     * 'xCaracSer' has the following description: Característica
     * adicional do serviço
     *
     * @return the value of field 'XCaracSer'.
     */
    public String getXCaracSer(
    ) {
        return this._xCaracSer;
    }

    /**
     * Returns the value of field 'xEmi'. The field 'xEmi' has the
     * following description: Funcionário emissor do CTe
     *
     * @return the value of field 'XEmi'.
     */
    public String getXEmi(
    ) {
        return this._xEmi;
    }

    /**
     * Returns the value of field 'xObs'. The field 'xObs' has the
     * following description: Observações Gerais
     *
     * @return the value of field 'XObs'.
     */
    public String getXObs(
    ) {
        return this._xObs;
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
    public void removeAllObsCont(
    ) {
        this._obsContList.clear();
    }

    /**
     */
    public void removeAllObsFisco(
    ) {
        this._obsFiscoList.clear();
    }

    /**
     * Method removeObsCont.
     *
     * @param vObsCont
     * @return true if the object was removed from the collection.
     */
    public boolean removeObsCont(
            final ObsCont vObsCont) {
        boolean removed = _obsContList.remove(vObsCont);
        return removed;
    }

    /**
     * Method removeObsContAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public ObsCont removeObsContAt(
            final int index) {
        Object obj = this._obsContList.remove(index);
        return (ObsCont) obj;
    }

    /**
     * Method removeObsFisco.
     *
     * @param vObsFisco
     * @return true if the object was removed from the collection.
     */
    public boolean removeObsFisco(
            final ObsFisco vObsFisco) {
        boolean removed = _obsFiscoList.remove(vObsFisco);
        return removed;
    }

    /**
     * Method removeObsFiscoAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public ObsFisco removeObsFiscoAt(
            final int index) {
        Object obj = this._obsFiscoList.remove(index);
        return (ObsFisco) obj;
    }

    /**
     * Sets the value of field 'destCalc'. The field 'destCalc' has
     * the following description: Município de destino para efeito
     * de cálculo do frete
     *
     * @param destCalc the value of field 'destCalc'.
     */
    public void setDestCalc(
            final String destCalc) {
        this._destCalc = destCalc;
    }

    /**
     * Sets the value of field 'entrega'. The field 'entrega' has
     * the following description: Informações ref. a previsão de
     * entrega
     *
     * @param entrega the value of field 'entrega'.
     */
    public void setEntrega(
            final Entrega entrega) {
        this._entrega = entrega;
    }

    /**
     * Sets the value of field 'fluxo'. The field 'fluxo' has the
     * following description: Previsão do fluxo da carga
     *
     * @param fluxo the value of field 'fluxo'.
     */
    public void setFluxo(
            final Fluxo fluxo) {
        this._fluxo = fluxo;
    }

    /**
     *
     *
     * @param index
     * @param vObsCont
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setObsCont(
            final int index,
            final ObsCont vObsCont)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._obsContList.size()) {
            throw new IndexOutOfBoundsException("setObsCont: Index value '" + index + "' not in range [0.." + (this._obsContList.size() - 1) + "]");
        }

        this._obsContList.set(index, vObsCont);
    }

    /**
     *
     *
     * @param vObsContArray
     */
    public void setObsCont(
            final ObsCont[] vObsContArray) {
        //-- copy array
        _obsContList.clear();

        for (int i = 0; i < vObsContArray.length; i++) {
                this._obsContList.add(vObsContArray[i]);
        }
    }

    /**
     *
     *
     * @param index
     * @param vObsFisco
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setObsFisco(
            final int index,
            final ObsFisco vObsFisco)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._obsFiscoList.size()) {
            throw new IndexOutOfBoundsException("setObsFisco: Index value '" + index + "' not in range [0.." + (this._obsFiscoList.size() - 1) + "]");
        }

        this._obsFiscoList.set(index, vObsFisco);
    }

    /**
     *
     *
     * @param vObsFiscoArray
     */
    public void setObsFisco(
            final ObsFisco[] vObsFiscoArray) {
        //-- copy array
        _obsFiscoList.clear();

        for (int i = 0; i < vObsFiscoArray.length; i++) {
                this._obsFiscoList.add(vObsFiscoArray[i]);
        }
    }

    /**
     * Sets the value of field 'origCalc'. The field 'origCalc' has
     * the following description: Município de origem para efeito
     * de cálculo do frete
     *
     * @param origCalc the value of field 'origCalc'.
     */
    public void setOrigCalc(
            final String origCalc) {
        this._origCalc = origCalc;
    }

    /**
     * Sets the value of field 'xCaracAd'. The field 'xCaracAd' has
     * the following description: Característica adicional do
     * transporte
     *
     * @param xCaracAd the value of field 'xCaracAd'.
     */
    public void setXCaracAd(
            final String xCaracAd) {
        this._xCaracAd = xCaracAd;
    }

    /**
     * Sets the value of field 'xCaracSer'. The field 'xCaracSer'
     * has the following description: Característica adicional do
     * serviço
     *
     * @param xCaracSer the value of field 'xCaracSer'.
     */
    public void setXCaracSer(
            final String xCaracSer) {
        this._xCaracSer = xCaracSer;
    }

    /**
     * Sets the value of field 'xEmi'. The field 'xEmi' has the
     * following description: Funcionário emissor do CTe
     *
     * @param xEmi the value of field 'xEmi'.
     */
    public void setXEmi(
            final String xEmi) {
        this._xEmi = xEmi;
    }

    /**
     * Sets the value of field 'xObs'. The field 'xObs' has the
     * following description: Observações Gerais
     *
     * @param xObs the value of field 'xObs'.
     */
    public void setXObs(
            final String xObs) {
        this._xObs = xObs;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v400.Compl
     */
    public static Compl unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (Compl) org.exolab.castor.xml.Unmarshaller.unmarshal(Compl.class, reader);
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
