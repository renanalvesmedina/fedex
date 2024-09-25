/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Indicador do "papel" do tomador do serviço no CT-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Toma4 implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Tomador do Serviço
     */
    private com.mercurio.lms.cte.model.v104.types.TomaType _toma;

    /**
     * Field _toma4Choice.
     */
    private com.mercurio.lms.cte.model.v104.Toma4Choice _toma4Choice;

    /**
     * Inscrição Estadual
     */
    private java.lang.String _IE;

    /**
     * Field _toma4Sequence.
     */
    private com.mercurio.lms.cte.model.v104.Toma4Sequence _toma4Sequence;


      //----------------/
     //- Constructors -/
    //----------------/

    public Toma4() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'IE'. The field 'IE' has the
     * following description: Inscrição Estadual
     * 
     * @return the value of field 'IE'.
     */
    public java.lang.String getIE(
    ) {
        return this._IE;
    }

    /**
     * Returns the value of field 'toma'. The field 'toma' has the
     * following description: Tomador do Serviço
     * 
     * @return the value of field 'Toma'.
     */
    public com.mercurio.lms.cte.model.v104.types.TomaType getToma(
    ) {
        return this._toma;
    }

    /**
     * Returns the value of field 'toma4Choice'.
     * 
     * @return the value of field 'Toma4Choice'.
     */
    public com.mercurio.lms.cte.model.v104.Toma4Choice getToma4Choice(
    ) {
        return this._toma4Choice;
    }

    /**
     * Returns the value of field 'toma4Sequence'.
     * 
     * @return the value of field 'Toma4Sequence'.
     */
    public com.mercurio.lms.cte.model.v104.Toma4Sequence getToma4Sequence(
    ) {
        return this._toma4Sequence;
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
     * Sets the value of field 'IE'. The field 'IE' has the
     * following description: Inscrição Estadual
     * 
     * @param IE the value of field 'IE'.
     */
    public void setIE(
            final java.lang.String IE) {
        this._IE = IE;
    }

    /**
     * Sets the value of field 'toma'. The field 'toma' has the
     * following description: Tomador do Serviço
     * 
     * @param toma the value of field 'toma'.
     */
    public void setToma(
            final com.mercurio.lms.cte.model.v104.types.TomaType toma) {
        this._toma = toma;
    }

    /**
     * Sets the value of field 'toma4Choice'.
     * 
     * @param toma4Choice the value of field 'toma4Choice'.
     */
    public void setToma4Choice(
            final com.mercurio.lms.cte.model.v104.Toma4Choice toma4Choice) {
        this._toma4Choice = toma4Choice;
    }

    /**
     * Sets the value of field 'toma4Sequence'.
     * 
     * @param toma4Sequence the value of field 'toma4Sequence'.
     */
    public void setToma4Sequence(
            final com.mercurio.lms.cte.model.v104.Toma4Sequence toma4Sequence) {
        this._toma4Sequence = toma4Sequence;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.Toma4
     */
    public static com.mercurio.lms.cte.model.v104.Toma4 unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.Toma4) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.Toma4.class, reader);
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
