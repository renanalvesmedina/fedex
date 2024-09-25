/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Class IdeChoice.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class IdeChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Indicador do "papel" do tomador do servi�o no CT-e
     */
    private com.mercurio.lms.cte.model.v104.Toma03 _toma03;

    /**
     * Indicador do "papel" do tomador do servi�o no CT-e
     */
    private com.mercurio.lms.cte.model.v104.Toma4 _toma4;


      //----------------/
     //- Constructors -/
    //----------------/

    public IdeChoice() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'toma03'. The field 'toma03' has
     * the following description: Indicador do "papel" do tomador
     * do serviço no CT-e
     * 
     * @return the value of field 'Toma03'.
     */
    public com.mercurio.lms.cte.model.v104.Toma03 getToma03(
    ) {
        return this._toma03;
    }

    /**
     * Returns the value of field 'toma4'. The field 'toma4' has
     * the following description: Indicador do "papel" do tomador
     * do serviço no CT-e
     * 
     * @return the value of field 'Toma4'.
     */
    public com.mercurio.lms.cte.model.v104.Toma4 getToma4(
    ) {
        return this._toma4;
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
     * Sets the value of field 'toma03'. The field 'toma03' has the
     * following description: Indicador do "papel" do tomador do
     * serviço no CT-e
     * 
     * @param toma03 the value of field 'toma03'.
     */
    public void setToma03(
            final com.mercurio.lms.cte.model.v104.Toma03 toma03) {
        this._toma03 = toma03;
    }

    /**
     * Sets the value of field 'toma4'. The field 'toma4' has the
     * following description: Indicador do "papel" do tomador do
     * serviço no CT-e
     * 
     * @param toma4 the value of field 'toma4'.
     */
    public void setToma4(
            final com.mercurio.lms.cte.model.v104.Toma4 toma4) {
        this._toma4 = toma4;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.IdeChoice
     */
    public static com.mercurio.lms.cte.model.v104.IdeChoice unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.IdeChoice) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.IdeChoice.class, reader);
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
