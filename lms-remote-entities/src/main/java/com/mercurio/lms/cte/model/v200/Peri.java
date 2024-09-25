/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v200;

/**
 * Preenchido quando for transporte de produtos classificados pela
 * ONU como perigosos.Não deve ser preenchido para modal
 * dutoviário.
 *  Observação para o modal aéreo:
 *  - O preenchimento desses campos não desobriga a empresa aérea
 * de emitir os demais documentos que constam na legislação
 * vigente.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Peri implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Número ONU/UN
     */
    private java.lang.String _nONU;

    /**
     * Nome apropriado para embarque do produto
     */
    private java.lang.String _xNomeAE;

    /**
     * Classe ou subclasse/divisão, e risco subsidiário/risco
     * secundário
     */
    private java.lang.String _xClaRisco;

    /**
     * Grupo de Embalagem
     */
    private java.lang.String _grEmb;

    /**
     * Quantidade total por produto
     */
    private java.lang.String _qTotProd;

    /**
     * Quantidade e Tipo de volumes
     */
    private java.lang.String _qVolTipo;

    /**
     * Ponto de Fulgor
     */
    private java.lang.String _pontoFulgor;


      //----------------/
     //- Constructors -/
    //----------------/

    public Peri() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'grEmb'. The field 'grEmb' has
     * the following description: Grupo de Embalagem
     * 
     * @return the value of field 'GrEmb'.
     */
    public java.lang.String getGrEmb(
    ) {
        return this._grEmb;
    }

    /**
     * Returns the value of field 'nONU'. The field 'nONU' has the
     * following description: Número ONU/UN
     * 
     * @return the value of field 'NONU'.
     */
    public java.lang.String getNONU(
    ) {
        return this._nONU;
    }

    /**
     * Returns the value of field 'pontoFulgor'. The field
     * 'pontoFulgor' has the following description: Ponto de Fulgor
     * 
     * @return the value of field 'PontoFulgor'.
     */
    public java.lang.String getPontoFulgor(
    ) {
        return this._pontoFulgor;
    }

    /**
     * Returns the value of field 'qTotProd'. The field 'qTotProd'
     * has the following description: Quantidade total por produto
     * 
     * @return the value of field 'QTotProd'.
     */
    public java.lang.String getQTotProd(
    ) {
        return this._qTotProd;
    }

    /**
     * Returns the value of field 'qVolTipo'. The field 'qVolTipo'
     * has the following description: Quantidade e Tipo de volumes
     * 
     * @return the value of field 'QVolTipo'.
     */
    public java.lang.String getQVolTipo(
    ) {
        return this._qVolTipo;
    }

    /**
     * Returns the value of field 'xClaRisco'. The field
     * 'xClaRisco' has the following description: Classe ou
     * subclasse/divisão, e risco subsidiário/risco secundário
     * 
     * @return the value of field 'XClaRisco'.
     */
    public java.lang.String getXClaRisco(
    ) {
        return this._xClaRisco;
    }

    /**
     * Returns the value of field 'xNomeAE'. The field 'xNomeAE'
     * has the following description: Nome apropriado para embarque
     * do produto
     * 
     * @return the value of field 'XNomeAE'.
     */
    public java.lang.String getXNomeAE(
    ) {
        return this._xNomeAE;
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
     * Sets the value of field 'grEmb'. The field 'grEmb' has the
     * following description: Grupo de Embalagem
     * 
     * @param grEmb the value of field 'grEmb'.
     */
    public void setGrEmb(
            final java.lang.String grEmb) {
        this._grEmb = grEmb;
    }

    /**
     * Sets the value of field 'nONU'. The field 'nONU' has the
     * following description: Número ONU/UN
     * 
     * @param nONU the value of field 'nONU'.
     */
    public void setNONU(
            final java.lang.String nONU) {
        this._nONU = nONU;
    }

    /**
     * Sets the value of field 'pontoFulgor'. The field
     * 'pontoFulgor' has the following description: Ponto de Fulgor
     * 
     * @param pontoFulgor the value of field 'pontoFulgor'.
     */
    public void setPontoFulgor(
            final java.lang.String pontoFulgor) {
        this._pontoFulgor = pontoFulgor;
    }

    /**
     * Sets the value of field 'qTotProd'. The field 'qTotProd' has
     * the following description: Quantidade total por produto
     * 
     * @param qTotProd the value of field 'qTotProd'.
     */
    public void setQTotProd(
            final java.lang.String qTotProd) {
        this._qTotProd = qTotProd;
    }

    /**
     * Sets the value of field 'qVolTipo'. The field 'qVolTipo' has
     * the following description: Quantidade e Tipo de volumes
     * 
     * @param qVolTipo the value of field 'qVolTipo'.
     */
    public void setQVolTipo(
            final java.lang.String qVolTipo) {
        this._qVolTipo = qVolTipo;
    }

    /**
     * Sets the value of field 'xClaRisco'. The field 'xClaRisco'
     * has the following description: Classe ou subclasse/divisão,
     * e risco subsidiário/risco secundário
     * 
     * @param xClaRisco the value of field 'xClaRisco'.
     */
    public void setXClaRisco(
            final java.lang.String xClaRisco) {
        this._xClaRisco = xClaRisco;
    }

    /**
     * Sets the value of field 'xNomeAE'. The field 'xNomeAE' has
     * the following description: Nome apropriado para embarque do
     * produto
     * 
     * @param xNomeAE the value of field 'xNomeAE'.
     */
    public void setXNomeAE(
            final java.lang.String xNomeAE) {
        this._xNomeAE = xNomeAE;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v200.Peri
     */
    public static com.mercurio.lms.cte.model.v200.Peri unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v200.Peri) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v200.Peri.class, reader);
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
