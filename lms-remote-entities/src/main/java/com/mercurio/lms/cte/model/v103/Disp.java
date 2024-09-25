/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Informações dos dispositivos do Vale PedágioEsse grupo não
 * é preenchido se: 1) existe o Regime Especial de Vale-Pedágio,
 * ou; 2) no trajeto não houver pedágio, ou; 3) for carga
 * fracionada
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Disp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Tipo do dispositivo
     * 0 - Cartão magnético;
     * 1- TAG;
     * 2 - Ticket em papel.
     */
    private com.mercurio.lms.cte.model.v103.types.TpDispType _tpDisp;

    /**
     * Empresa fornecedora do Vale Pedágio
     */
    private java.lang.String _xEmp;

    /**
     * Data de vigência do Contrato
     */
    private java.lang.String _dVig;

    /**
     * Número do dispositivo (cartão ou TAG).
     */
    private java.lang.String _nDisp;

    /**
     * Número de ordem do comprovante de compra do Vale Pedágio
     * fornecido para cada veículo ou combinação veicular, por
     * viagem.
     */
    private java.lang.String _nCompC;


      //----------------/
     //- Constructors -/
    //----------------/

    public Disp() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dVig'. The field 'dVig' has the
     * following description: Data de vigência do Contrato
     * 
     * @return the value of field 'DVig'.
     */
    public java.lang.String getDVig(
    ) {
        return this._dVig;
    }

    /**
     * Returns the value of field 'nCompC'. The field 'nCompC' has
     * the following description: Número de ordem do comprovante
     * de compra do Vale Pedágio fornecido para cada veículo ou
     * combinação veicular, por viagem.
     * 
     * @return the value of field 'NCompC'.
     */
    public java.lang.String getNCompC(
    ) {
        return this._nCompC;
    }

    /**
     * Returns the value of field 'nDisp'. The field 'nDisp' has
     * the following description: Número do dispositivo (cartão
     * ou TAG).
     * 
     * @return the value of field 'NDisp'.
     */
    public java.lang.String getNDisp(
    ) {
        return this._nDisp;
    }

    /**
     * Returns the value of field 'tpDisp'. The field 'tpDisp' has
     * the following description: Tipo do dispositivo
     * 0 - Cartão magnético;
     * 1- TAG;
     * 2 - Ticket em papel.
     * 
     * 
     * @return the value of field 'TpDisp'.
     */
    public com.mercurio.lms.cte.model.v103.types.TpDispType getTpDisp(
    ) {
        return this._tpDisp;
    }

    /**
     * Returns the value of field 'xEmp'. The field 'xEmp' has the
     * following description: Empresa fornecedora do Vale Pedágio
     * 
     * @return the value of field 'XEmp'.
     */
    public java.lang.String getXEmp(
    ) {
        return this._xEmp;
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
     * Sets the value of field 'dVig'. The field 'dVig' has the
     * following description: Data de vigência do Contrato
     * 
     * @param dVig the value of field 'dVig'.
     */
    public void setDVig(
            final java.lang.String dVig) {
        this._dVig = dVig;
    }

    /**
     * Sets the value of field 'nCompC'. The field 'nCompC' has the
     * following description: Número de ordem do comprovante de
     * compra do Vale Pedágio fornecido para cada veículo ou
     * combinação veicular, por viagem.
     * 
     * @param nCompC the value of field 'nCompC'.
     */
    public void setNCompC(
            final java.lang.String nCompC) {
        this._nCompC = nCompC;
    }

    /**
     * Sets the value of field 'nDisp'. The field 'nDisp' has the
     * following description: Número do dispositivo (cartão ou
     * TAG).
     * 
     * @param nDisp the value of field 'nDisp'.
     */
    public void setNDisp(
            final java.lang.String nDisp) {
        this._nDisp = nDisp;
    }

    /**
     * Sets the value of field 'tpDisp'. The field 'tpDisp' has the
     * following description: Tipo do dispositivo
     * 0 - Cartão magnético;
     * 1- TAG;
     * 2 - Ticket em papel.
     * 
     * 
     * @param tpDisp the value of field 'tpDisp'.
     */
    public void setTpDisp(
            final com.mercurio.lms.cte.model.v103.types.TpDispType tpDisp) {
        this._tpDisp = tpDisp;
    }

    /**
     * Sets the value of field 'xEmp'. The field 'xEmp' has the
     * following description: Empresa fornecedora do Vale Pedágio
     * 
     * @param xEmp the value of field 'xEmp'.
     */
    public void setXEmp(
            final java.lang.String xEmp) {
        this._xEmp = xEmp;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v103.Disp
     */
    public static com.mercurio.lms.cte.model.v103.Disp unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.Disp) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.Disp.class, reader);
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
