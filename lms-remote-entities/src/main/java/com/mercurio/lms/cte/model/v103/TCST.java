/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Tipo Dados do CST (Código de Situação Tributária)
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TCST implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Internal choice value storage
     */
    private java.lang.Object _choiceValue;

    /**
     * Prestação sujeito à tributação normal do ICMS
     */
    private com.mercurio.lms.cte.model.v103.CST00 _CST00;

    /**
     * Prestação sujeito à tributação com redução de BC do
     * ICMS
     */
    private com.mercurio.lms.cte.model.v103.CST20 _CST20;

    /**
     * ICMS Isento, não Tributado ou diferido
     */
    private com.mercurio.lms.cte.model.v103.CST45 _CST45;

    /**
     * Responsabilidade do recolhimento do ICMS atribuído ao
     * tomador ou 3º por ST
     */
    private com.mercurio.lms.cte.model.v103.CST80 _CST80;

    /**
     * ICMS devido à Outra UF
     */
    private com.mercurio.lms.cte.model.v103.CST81 _CST81;

    /**
     * ICMS Outros
     */
    private com.mercurio.lms.cte.model.v103.CST90 _CST90;


      //----------------/
     //- Constructors -/
    //----------------/

    public TCST() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'CST00'. The field 'CST00' has
     * the following description: Prestação sujeito à
     * tributação normal do ICMS
     * 
     * @return the value of field 'CST00'.
     */
    public com.mercurio.lms.cte.model.v103.CST00 getCST00(
    ) {
        return this._CST00;
    }

    /**
     * Returns the value of field 'CST20'. The field 'CST20' has
     * the following description: Prestação sujeito à
     * tributação com redução de BC do ICMS
     * 
     * @return the value of field 'CST20'.
     */
    public com.mercurio.lms.cte.model.v103.CST20 getCST20(
    ) {
        return this._CST20;
    }

    /**
     * Returns the value of field 'CST45'. The field 'CST45' has
     * the following description: ICMS Isento, não Tributado ou
     * diferido
     * 
     * @return the value of field 'CST45'.
     */
    public com.mercurio.lms.cte.model.v103.CST45 getCST45(
    ) {
        return this._CST45;
    }

    /**
     * Returns the value of field 'CST80'. The field 'CST80' has
     * the following description: Responsabilidade do recolhimento
     * do ICMS atribuído ao tomador ou 3º por ST
     * 
     * @return the value of field 'CST80'.
     */
    public com.mercurio.lms.cte.model.v103.CST80 getCST80(
    ) {
        return this._CST80;
    }

    /**
     * Returns the value of field 'CST81'. The field 'CST81' has
     * the following description: ICMS devido à Outra UF
     * 
     * @return the value of field 'CST81'.
     */
    public com.mercurio.lms.cte.model.v103.CST81 getCST81(
    ) {
        return this._CST81;
    }

    /**
     * Returns the value of field 'CST90'. The field 'CST90' has
     * the following description: ICMS Outros
     * 
     * @return the value of field 'CST90'.
     */
    public com.mercurio.lms.cte.model.v103.CST90 getCST90(
    ) {
        return this._CST90;
    }

    /**
     * Returns the value of field 'choiceValue'. The field
     * 'choiceValue' has the following description: Internal choice
     * value storage
     * 
     * @return the value of field 'ChoiceValue'.
     */
    public java.lang.Object getChoiceValue(
    ) {
        return this._choiceValue;
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
     * Sets the value of field 'CST00'. The field 'CST00' has the
     * following description: Prestação sujeito à tributação
     * normal do ICMS
     * 
     * @param CST00 the value of field 'CST00'.
     */
    public void setCST00(
            final com.mercurio.lms.cte.model.v103.CST00 CST00) {
        this._CST00 = CST00;
        this._choiceValue = CST00;
    }

    /**
     * Sets the value of field 'CST20'. The field 'CST20' has the
     * following description: Prestação sujeito à tributação
     * com redução de BC do ICMS
     * 
     * @param CST20 the value of field 'CST20'.
     */
    public void setCST20(
            final com.mercurio.lms.cte.model.v103.CST20 CST20) {
        this._CST20 = CST20;
        this._choiceValue = CST20;
    }

    /**
     * Sets the value of field 'CST45'. The field 'CST45' has the
     * following description: ICMS Isento, não Tributado ou
     * diferido
     * 
     * @param CST45 the value of field 'CST45'.
     */
    public void setCST45(
            final com.mercurio.lms.cte.model.v103.CST45 CST45) {
        this._CST45 = CST45;
        this._choiceValue = CST45;
    }

    /**
     * Sets the value of field 'CST80'. The field 'CST80' has the
     * following description: Responsabilidade do recolhimento do
     * ICMS atribuído ao tomador ou 3º por ST
     * 
     * @param CST80 the value of field 'CST80'.
     */
    public void setCST80(
            final com.mercurio.lms.cte.model.v103.CST80 CST80) {
        this._CST80 = CST80;
        this._choiceValue = CST80;
    }

    /**
     * Sets the value of field 'CST81'. The field 'CST81' has the
     * following description: ICMS devido à Outra UF
     * 
     * @param CST81 the value of field 'CST81'.
     */
    public void setCST81(
            final com.mercurio.lms.cte.model.v103.CST81 CST81) {
        this._CST81 = CST81;
        this._choiceValue = CST81;
    }

    /**
     * Sets the value of field 'CST90'. The field 'CST90' has the
     * following description: ICMS Outros
     * 
     * @param CST90 the value of field 'CST90'.
     */
    public void setCST90(
            final com.mercurio.lms.cte.model.v103.CST90 CST90) {
        this._CST90 = CST90;
        this._choiceValue = CST90;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v103.TCST
     */
    public static com.mercurio.lms.cte.model.v103.TCST unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.TCST) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.TCST.class, reader);
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
