/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Informações de Vale Pedágio
 * 
 * 
 * Dados só serão informados por empresas de transporte
 * rodoviário e quando o CT-e for de carga lotação.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class ValePed implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Número do Certificado do Regime Especial que permite que o
     * Vale Pedágio não seja pago antecipadamente
     */
    private java.lang.String _nroRE;

    /**
     * Valor Total dos Vales Pedágio.
     */
    private java.lang.String _vTValePed;

    /**
     * Responsável pelo pagamento do Vale Pedágio.
     * 0-emitente do CT-e, 1-remetente, 2-expedidor, 3-recebedor,
     * 4-destinatário, 5-Tomador do Serviço
     */
    private com.mercurio.lms.cte.model.v103.types.RespPgType _respPg;

    /**
     * Informações dos dispositivos do Vale Pedágio
     */
    private java.util.List<com.mercurio.lms.cte.model.v103.Disp> _dispList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ValePed() {
        super();
        this._dispList = new java.util.ArrayList<com.mercurio.lms.cte.model.v103.Disp>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vDisp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDisp(
            final com.mercurio.lms.cte.model.v103.Disp vDisp)
    throws java.lang.IndexOutOfBoundsException {
        this._dispList.add(vDisp);
    }

    /**
     * 
     * 
     * @param index
     * @param vDisp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDisp(
            final int index,
            final com.mercurio.lms.cte.model.v103.Disp vDisp)
    throws java.lang.IndexOutOfBoundsException {
        this._dispList.add(index, vDisp);
    }

    /**
     * Method enumerateDisp.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v103.Disp> enumerateDisp(
    ) {
        return java.util.Collections.enumeration(this._dispList);
    }

    /**
     * Method getDisp.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.cte.model.v103.Disp at the given index
     */
    public com.mercurio.lms.cte.model.v103.Disp getDisp(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._dispList.size()) {
            throw new IndexOutOfBoundsException("getDisp: Index value '" + index + "' not in range [0.." + (this._dispList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v103.Disp) _dispList.get(index);
    }

    /**
     * Method getDisp.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v103.Disp[] getDisp(
    ) {
        com.mercurio.lms.cte.model.v103.Disp[] array = new com.mercurio.lms.cte.model.v103.Disp[0];
        return (com.mercurio.lms.cte.model.v103.Disp[]) this._dispList.toArray(array);
    }

    /**
     * Method getDispCount.
     * 
     * @return the size of this collection
     */
    public int getDispCount(
    ) {
        return this._dispList.size();
    }

    /**
     * Returns the value of field 'nroRE'. The field 'nroRE' has
     * the following description: Número do Certificado do Regime
     * Especial que permite que o Vale Pedágio não seja pago
     * antecipadamente
     * 
     * @return the value of field 'NroRE'.
     */
    public java.lang.String getNroRE(
    ) {
        return this._nroRE;
    }

    /**
     * Returns the value of field 'respPg'. The field 'respPg' has
     * the following description: Responsável pelo pagamento do
     * Vale Pedágio.
     * 0-emitente do CT-e, 1-remetente, 2-expedidor, 3-recebedor,
     * 4-destinatário, 5-Tomador do Serviço
     * 
     * @return the value of field 'RespPg'.
     */
    public com.mercurio.lms.cte.model.v103.types.RespPgType getRespPg(
    ) {
        return this._respPg;
    }

    /**
     * Returns the value of field 'vTValePed'. The field
     * 'vTValePed' has the following description: Valor Total dos
     * Vales Pedágio.
     * 
     * @return the value of field 'VTValePed'.
     */
    public java.lang.String getVTValePed(
    ) {
        return this._vTValePed;
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
     * Method iterateDisp.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v103.Disp> iterateDisp(
    ) {
        return this._dispList.iterator();
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
    public void removeAllDisp(
    ) {
        this._dispList.clear();
    }

    /**
     * Method removeDisp.
     * 
     * @param vDisp
     * @return true if the object was removed from the collection.
     */
    public boolean removeDisp(
            final com.mercurio.lms.cte.model.v103.Disp vDisp) {
        boolean removed = _dispList.remove(vDisp);
        return removed;
    }

    /**
     * Method removeDispAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v103.Disp removeDispAt(
            final int index) {
        java.lang.Object obj = this._dispList.remove(index);
        return (com.mercurio.lms.cte.model.v103.Disp) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vDisp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setDisp(
            final int index,
            final com.mercurio.lms.cte.model.v103.Disp vDisp)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._dispList.size()) {
            throw new IndexOutOfBoundsException("setDisp: Index value '" + index + "' not in range [0.." + (this._dispList.size() - 1) + "]");
        }

        this._dispList.set(index, vDisp);
    }

    /**
     * 
     * 
     * @param vDispArray
     */
    public void setDisp(
            final com.mercurio.lms.cte.model.v103.Disp[] vDispArray) {
        //-- copy array
        _dispList.clear();

        for (int i = 0; i < vDispArray.length; i++) {
                this._dispList.add(vDispArray[i]);
        }
    }

    /**
     * Sets the value of field 'nroRE'. The field 'nroRE' has the
     * following description: Número do Certificado do Regime
     * Especial que permite que o Vale Pedágio não seja pago
     * antecipadamente
     * 
     * @param nroRE the value of field 'nroRE'.
     */
    public void setNroRE(
            final java.lang.String nroRE) {
        this._nroRE = nroRE;
    }

    /**
     * Sets the value of field 'respPg'. The field 'respPg' has the
     * following description: Responsável pelo pagamento do Vale
     * Pedágio.
     * 0-emitente do CT-e, 1-remetente, 2-expedidor, 3-recebedor,
     * 4-destinatário, 5-Tomador do Serviço
     * 
     * @param respPg the value of field 'respPg'.
     */
    public void setRespPg(
            final com.mercurio.lms.cte.model.v103.types.RespPgType respPg) {
        this._respPg = respPg;
    }

    /**
     * Sets the value of field 'vTValePed'. The field 'vTValePed'
     * has the following description: Valor Total dos Vales
     * Pedágio.
     * 
     * @param vTValePed the value of field 'vTValePed'.
     */
    public void setVTValePed(
            final java.lang.String vTValePed) {
        this._vTValePed = vTValePed;
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
     * com.mercurio.lms.cte.model.v103.ValePed
     */
    public static com.mercurio.lms.cte.model.v103.ValePed unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.ValePed) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.ValePed.class, reader);
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
