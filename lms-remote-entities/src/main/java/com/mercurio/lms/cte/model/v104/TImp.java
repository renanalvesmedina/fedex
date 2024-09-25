/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Tipo Dados do Imposto
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TImp implements java.io.Serializable {


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
    private com.mercurio.lms.cte.model.v104.ICMS00 _ICMS00;

    /**
     * Prestação sujeito à tributação com redução de BC do
     * ICMS
     */
    private com.mercurio.lms.cte.model.v104.ICMS20 _ICMS20;

    /**
     * ICMS Isento, não Tributado ou diferido
     */
    private com.mercurio.lms.cte.model.v104.ICMS45 _ICMS45;

    /**
     * Tributação pelo ICMS60 - ICMS cobrado por substituição
     * tributária.Responsabilidade do recolhimento do ICMS
     * atribuído ao tomador ou 3º por ST
     */
    private com.mercurio.lms.cte.model.v104.ICMS60 _ICMS60;

    /**
     * ICMS Outros
     */
    private com.mercurio.lms.cte.model.v104.ICMS90 _ICMS90;

    /**
     * ICMS devido à UF de origem da prestação, quando diferente
     * da UF do emitente
     */
    private com.mercurio.lms.cte.model.v104.ICMSOutraUF _ICMSOutraUF;

    /**
     * Simples Nacional
     */
    private com.mercurio.lms.cte.model.v104.ICMSSN _ICMSSN;


      //----------------/
     //- Constructors -/
    //----------------/

    public TImp() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

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
     * Returns the value of field 'ICMS00'. The field 'ICMS00' has
     * the following description: Prestação sujeito à
     * tributação normal do ICMS
     * 
     * @return the value of field 'ICMS00'.
     */
    public com.mercurio.lms.cte.model.v104.ICMS00 getICMS00(
    ) {
        return this._ICMS00;
    }

    /**
     * Returns the value of field 'ICMS20'. The field 'ICMS20' has
     * the following description: Prestação sujeito à
     * tributação com redução de BC do ICMS
     * 
     * @return the value of field 'ICMS20'.
     */
    public com.mercurio.lms.cte.model.v104.ICMS20 getICMS20(
    ) {
        return this._ICMS20;
    }

    /**
     * Returns the value of field 'ICMS45'. The field 'ICMS45' has
     * the following description: ICMS Isento, não Tributado ou
     * diferido
     * 
     * @return the value of field 'ICMS45'.
     */
    public com.mercurio.lms.cte.model.v104.ICMS45 getICMS45(
    ) {
        return this._ICMS45;
    }

    /**
     * Returns the value of field 'ICMS60'. The field 'ICMS60' has
     * the following description: Tributação pelo ICMS60 - ICMS
     * cobrado por substituição tributária.Responsabilidade do
     * recolhimento do ICMS atribuído ao tomador ou 3º por ST
     * 
     * @return the value of field 'ICMS60'.
     */
    public com.mercurio.lms.cte.model.v104.ICMS60 getICMS60(
    ) {
        return this._ICMS60;
    }

    /**
     * Returns the value of field 'ICMS90'. The field 'ICMS90' has
     * the following description: ICMS Outros
     * 
     * @return the value of field 'ICMS90'.
     */
    public com.mercurio.lms.cte.model.v104.ICMS90 getICMS90(
    ) {
        return this._ICMS90;
    }

    /**
     * Returns the value of field 'ICMSOutraUF'. The field
     * 'ICMSOutraUF' has the following description: ICMS devido à
     * UF de origem da prestação, quando diferente da UF do
     * emitente
     * 
     * @return the value of field 'ICMSOutraUF'.
     */
    public com.mercurio.lms.cte.model.v104.ICMSOutraUF getICMSOutraUF(
    ) {
        return this._ICMSOutraUF;
    }

    /**
     * Returns the value of field 'ICMSSN'. The field 'ICMSSN' has
     * the following description: Simples Nacional
     * 
     * @return the value of field 'ICMSSN'.
     */
    public com.mercurio.lms.cte.model.v104.ICMSSN getICMSSN(
    ) {
        return this._ICMSSN;
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
     * Sets the value of field 'ICMS00'. The field 'ICMS00' has the
     * following description: Prestação sujeito à tributação
     * normal do ICMS
     * 
     * @param ICMS00 the value of field 'ICMS00'.
     */
    public void setICMS00(
            final com.mercurio.lms.cte.model.v104.ICMS00 ICMS00) {
        this._ICMS00 = ICMS00;
        this._choiceValue = ICMS00;
    }

    /**
     * Sets the value of field 'ICMS20'. The field 'ICMS20' has the
     * following description: Prestação sujeito à tributação
     * com redução de BC do ICMS
     * 
     * @param ICMS20 the value of field 'ICMS20'.
     */
    public void setICMS20(
            final com.mercurio.lms.cte.model.v104.ICMS20 ICMS20) {
        this._ICMS20 = ICMS20;
        this._choiceValue = ICMS20;
    }

    /**
     * Sets the value of field 'ICMS45'. The field 'ICMS45' has the
     * following description: ICMS Isento, não Tributado ou
     * diferido
     * 
     * @param ICMS45 the value of field 'ICMS45'.
     */
    public void setICMS45(
            final com.mercurio.lms.cte.model.v104.ICMS45 ICMS45) {
        this._ICMS45 = ICMS45;
        this._choiceValue = ICMS45;
    }

    /**
     * Sets the value of field 'ICMS60'. The field 'ICMS60' has the
     * following description: Tributação pelo ICMS60 - ICMS
     * cobrado por substituição tributária.Responsabilidade do
     * recolhimento do ICMS atribuído ao tomador ou 3º por ST
     * 
     * @param ICMS60 the value of field 'ICMS60'.
     */
    public void setICMS60(
            final com.mercurio.lms.cte.model.v104.ICMS60 ICMS60) {
        this._ICMS60 = ICMS60;
        this._choiceValue = ICMS60;
    }

    /**
     * Sets the value of field 'ICMS90'. The field 'ICMS90' has the
     * following description: ICMS Outros
     * 
     * @param ICMS90 the value of field 'ICMS90'.
     */
    public void setICMS90(
            final com.mercurio.lms.cte.model.v104.ICMS90 ICMS90) {
        this._ICMS90 = ICMS90;
        this._choiceValue = ICMS90;
    }

    /**
     * Sets the value of field 'ICMSOutraUF'. The field
     * 'ICMSOutraUF' has the following description: ICMS devido à
     * UF de origem da prestação, quando diferente da UF do
     * emitente
     * 
     * @param ICMSOutraUF the value of field 'ICMSOutraUF'.
     */
    public void setICMSOutraUF(
            final com.mercurio.lms.cte.model.v104.ICMSOutraUF ICMSOutraUF) {
        this._ICMSOutraUF = ICMSOutraUF;
        this._choiceValue = ICMSOutraUF;
    }

    /**
     * Sets the value of field 'ICMSSN'. The field 'ICMSSN' has the
     * following description: Simples Nacional
     * 
     * @param ICMSSN the value of field 'ICMSSN'.
     */
    public void setICMSSN(
            final com.mercurio.lms.cte.model.v104.ICMSSN ICMSSN) {
        this._ICMSSN = ICMSSN;
        this._choiceValue = ICMSSN;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.TImp
     */
    public static com.mercurio.lms.cte.model.v104.TImp unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.TImp) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.TImp.class, reader);
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
