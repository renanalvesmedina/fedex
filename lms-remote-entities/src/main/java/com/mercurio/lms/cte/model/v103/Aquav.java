/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Informações do modal Aquaviário
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Aquav implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Valor da Prestação Base de Cálculo do AFRMM
     */
    private java.lang.String _vPrest;

    /**
     * AFRMM (Adicional de Frete para Renovação da Marinha
     * Mercante)
     */
    private java.lang.String _vAFRMM;

    /**
     * Número do Booking (reserva)
     */
    private java.lang.String _nBooking;

    /**
     * Número de Controle
     */
    private java.lang.String _nCtrl;

    /**
     * Identificação do Navio 
     */
    private java.lang.String _xNavio;

    /**
     * Número da Viagem
     */
    private java.lang.String _nViag;

    /**
     * Direção:N-Norte, L-Leste, S-Sul, O-Oeste 
     */
    private com.mercurio.lms.cte.model.v103.types.DirecType _direc;

    /**
     * Porto de Embarque
     */
    private java.lang.String _prtEmb;

    /**
     * Porto de Transbordo
     */
    private java.lang.String _prtTrans;

    /**
     * Porto de Destino
     */
    private java.lang.String _prtDest;

    /**
     * Tipo de Navegação:
     * 0 - Interior
     * 1 - Cabotagem
     */
    private com.mercurio.lms.cte.model.v103.types.TpNavType _tpNav;

    /**
     * Irin do navio sempre deverá ser informado
     */
    private java.lang.String _irin;

    /**
     * grupo de informações dos lacres dos cointainers da qtde da
     * carga 
     */
    private java.util.List<com.mercurio.lms.cte.model.v103.Lacre> _lacreList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Aquav() {
        super();
        this._lacreList = new java.util.ArrayList<com.mercurio.lms.cte.model.v103.Lacre>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vLacre
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacre(
            final com.mercurio.lms.cte.model.v103.Lacre vLacre)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._lacreList.size() >= 3) {
            throw new IndexOutOfBoundsException("addLacre has a maximum of 3");
        }

        this._lacreList.add(vLacre);
    }

    /**
     * 
     * 
     * @param index
     * @param vLacre
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacre(
            final int index,
            final com.mercurio.lms.cte.model.v103.Lacre vLacre)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._lacreList.size() >= 3) {
            throw new IndexOutOfBoundsException("addLacre has a maximum of 3");
        }

        this._lacreList.add(index, vLacre);
    }

    /**
     * Method enumerateLacre.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v103.Lacre> enumerateLacre(
    ) {
        return java.util.Collections.enumeration(this._lacreList);
    }

    /**
     * Returns the value of field 'direc'. The field 'direc' has
     * the following description: Direção:N-Norte, L-Leste,
     * S-Sul, O-Oeste 
     * 
     * @return the value of field 'Direc'.
     */
    public com.mercurio.lms.cte.model.v103.types.DirecType getDirec(
    ) {
        return this._direc;
    }

    /**
     * Returns the value of field 'irin'. The field 'irin' has the
     * following description: Irin do navio sempre deverá ser
     * informado
     * 
     * @return the value of field 'Irin'.
     */
    public java.lang.String getIrin(
    ) {
        return this._irin;
    }

    /**
     * Method getLacre.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v103.Lacre at the given index
     */
    public com.mercurio.lms.cte.model.v103.Lacre getLacre(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._lacreList.size()) {
            throw new IndexOutOfBoundsException("getLacre: Index value '" + index + "' not in range [0.." + (this._lacreList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v103.Lacre) _lacreList.get(index);
    }

    /**
     * Method getLacre.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v103.Lacre[] getLacre(
    ) {
        com.mercurio.lms.cte.model.v103.Lacre[] array = new com.mercurio.lms.cte.model.v103.Lacre[0];
        return (com.mercurio.lms.cte.model.v103.Lacre[]) this._lacreList.toArray(array);
    }

    /**
     * Method getLacreCount.
     * 
     * @return the size of this collection
     */
    public int getLacreCount(
    ) {
        return this._lacreList.size();
    }

    /**
     * Returns the value of field 'nBooking'. The field 'nBooking'
     * has the following description: Número do Booking (reserva)
     * 
     * @return the value of field 'NBooking'.
     */
    public java.lang.String getNBooking(
    ) {
        return this._nBooking;
    }

    /**
     * Returns the value of field 'nCtrl'. The field 'nCtrl' has
     * the following description: Número de Controle
     * 
     * @return the value of field 'NCtrl'.
     */
    public java.lang.String getNCtrl(
    ) {
        return this._nCtrl;
    }

    /**
     * Returns the value of field 'nViag'. The field 'nViag' has
     * the following description: Número da Viagem
     * 
     * @return the value of field 'NViag'.
     */
    public java.lang.String getNViag(
    ) {
        return this._nViag;
    }

    /**
     * Returns the value of field 'prtDest'. The field 'prtDest'
     * has the following description: Porto de Destino
     * 
     * @return the value of field 'PrtDest'.
     */
    public java.lang.String getPrtDest(
    ) {
        return this._prtDest;
    }

    /**
     * Returns the value of field 'prtEmb'. The field 'prtEmb' has
     * the following description: Porto de Embarque
     * 
     * @return the value of field 'PrtEmb'.
     */
    public java.lang.String getPrtEmb(
    ) {
        return this._prtEmb;
    }

    /**
     * Returns the value of field 'prtTrans'. The field 'prtTrans'
     * has the following description: Porto de Transbordo
     * 
     * @return the value of field 'PrtTrans'.
     */
    public java.lang.String getPrtTrans(
    ) {
        return this._prtTrans;
    }

    /**
     * Returns the value of field 'tpNav'. The field 'tpNav' has
     * the following description: Tipo de Navegação:
     * 0 - Interior
     * 1 - Cabotagem
     * 
     * @return the value of field 'TpNav'.
     */
    public com.mercurio.lms.cte.model.v103.types.TpNavType getTpNav(
    ) {
        return this._tpNav;
    }

    /**
     * Returns the value of field 'vAFRMM'. The field 'vAFRMM' has
     * the following description: AFRMM (Adicional de Frete para
     * Renovação da Marinha Mercante)
     * 
     * @return the value of field 'VAFRMM'.
     */
    public java.lang.String getVAFRMM(
    ) {
        return this._vAFRMM;
    }

    /**
     * Returns the value of field 'vPrest'. The field 'vPrest' has
     * the following description: Valor da Prestação Base de
     * Cálculo do AFRMM
     * 
     * @return the value of field 'VPrest'.
     */
    public java.lang.String getVPrest(
    ) {
        return this._vPrest;
    }

    /**
     * Returns the value of field 'xNavio'. The field 'xNavio' has
     * the following description: Identificação do Navio 
     * 
     * @return the value of field 'XNavio'.
     */
    public java.lang.String getXNavio(
    ) {
        return this._xNavio;
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
     * Method iterateLacre.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v103.Lacre> iterateLacre(
    ) {
        return this._lacreList.iterator();
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
    public void removeAllLacre(
    ) {
        this._lacreList.clear();
    }

    /**
     * Method removeLacre.
     * 
     * @param vLacre
     * @return true if the object was removed from the collection.
     */
    public boolean removeLacre(
            final com.mercurio.lms.cte.model.v103.Lacre vLacre) {
        boolean removed = _lacreList.remove(vLacre);
        return removed;
    }

    /**
     * Method removeLacreAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v103.Lacre removeLacreAt(
            final int index) {
        java.lang.Object obj = this._lacreList.remove(index);
        return (com.mercurio.lms.cte.model.v103.Lacre) obj;
    }

    /**
     * Sets the value of field 'direc'. The field 'direc' has the
     * following description: Direção:N-Norte, L-Leste, S-Sul,
     * O-Oeste 
     * 
     * @param direc the value of field 'direc'.
     */
    public void setDirec(
            final com.mercurio.lms.cte.model.v103.types.DirecType direc) {
        this._direc = direc;
    }

    /**
     * Sets the value of field 'irin'. The field 'irin' has the
     * following description: Irin do navio sempre deverá ser
     * informado
     * 
     * @param irin the value of field 'irin'.
     */
    public void setIrin(
            final java.lang.String irin) {
        this._irin = irin;
    }

    /**
     * 
     * 
     * @param index
     * @param vLacre
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setLacre(
            final int index,
            final com.mercurio.lms.cte.model.v103.Lacre vLacre)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._lacreList.size()) {
            throw new IndexOutOfBoundsException("setLacre: Index value '" + index + "' not in range [0.." + (this._lacreList.size() - 1) + "]");
        }

        this._lacreList.set(index, vLacre);
    }

    /**
     * 
     * 
     * @param vLacreArray
     */
    public void setLacre(
            final com.mercurio.lms.cte.model.v103.Lacre[] vLacreArray) {
        //-- copy array
        _lacreList.clear();

        for (int i = 0; i < vLacreArray.length; i++) {
                this._lacreList.add(vLacreArray[i]);
        }
    }

    /**
     * Sets the value of field 'nBooking'. The field 'nBooking' has
     * the following description: Número do Booking (reserva)
     * 
     * @param nBooking the value of field 'nBooking'.
     */
    public void setNBooking(
            final java.lang.String nBooking) {
        this._nBooking = nBooking;
    }

    /**
     * Sets the value of field 'nCtrl'. The field 'nCtrl' has the
     * following description: Número de Controle
     * 
     * @param nCtrl the value of field 'nCtrl'.
     */
    public void setNCtrl(
            final java.lang.String nCtrl) {
        this._nCtrl = nCtrl;
    }

    /**
     * Sets the value of field 'nViag'. The field 'nViag' has the
     * following description: Número da Viagem
     * 
     * @param nViag the value of field 'nViag'.
     */
    public void setNViag(
            final java.lang.String nViag) {
        this._nViag = nViag;
    }

    /**
     * Sets the value of field 'prtDest'. The field 'prtDest' has
     * the following description: Porto de Destino
     * 
     * @param prtDest the value of field 'prtDest'.
     */
    public void setPrtDest(
            final java.lang.String prtDest) {
        this._prtDest = prtDest;
    }

    /**
     * Sets the value of field 'prtEmb'. The field 'prtEmb' has the
     * following description: Porto de Embarque
     * 
     * @param prtEmb the value of field 'prtEmb'.
     */
    public void setPrtEmb(
            final java.lang.String prtEmb) {
        this._prtEmb = prtEmb;
    }

    /**
     * Sets the value of field 'prtTrans'. The field 'prtTrans' has
     * the following description: Porto de Transbordo
     * 
     * @param prtTrans the value of field 'prtTrans'.
     */
    public void setPrtTrans(
            final java.lang.String prtTrans) {
        this._prtTrans = prtTrans;
    }

    /**
     * Sets the value of field 'tpNav'. The field 'tpNav' has the
     * following description: Tipo de Navegação:
     * 0 - Interior
     * 1 - Cabotagem
     * 
     * @param tpNav the value of field 'tpNav'.
     */
    public void setTpNav(
            final com.mercurio.lms.cte.model.v103.types.TpNavType tpNav) {
        this._tpNav = tpNav;
    }

    /**
     * Sets the value of field 'vAFRMM'. The field 'vAFRMM' has the
     * following description: AFRMM (Adicional de Frete para
     * Renovação da Marinha Mercante)
     * 
     * @param vAFRMM the value of field 'vAFRMM'.
     */
    public void setVAFRMM(
            final java.lang.String vAFRMM) {
        this._vAFRMM = vAFRMM;
    }

    /**
     * Sets the value of field 'vPrest'. The field 'vPrest' has the
     * following description: Valor da Prestação Base de Cálculo
     * do AFRMM
     * 
     * @param vPrest the value of field 'vPrest'.
     */
    public void setVPrest(
            final java.lang.String vPrest) {
        this._vPrest = vPrest;
    }

    /**
     * Sets the value of field 'xNavio'. The field 'xNavio' has the
     * following description: Identificação do Navio 
     * 
     * @param xNavio the value of field 'xNavio'.
     */
    public void setXNavio(
            final java.lang.String xNavio) {
        this._xNavio = xNavio;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v103.Aquav
     */
    public static com.mercurio.lms.cte.model.v103.Aquav unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.Aquav) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.Aquav.class, reader);
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
