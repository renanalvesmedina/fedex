/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100;

/**
 * Proprietários do Reboque. Só preenchido quando o reboque não
 * pertencer à empresa emitente do MDF-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Prop implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Registro Nacional dos Transportadores Rodoviários de Carga
     */
    private java.lang.String _RNTRC;


      //----------------/
     //- Constructors -/
    //----------------/

    public Prop() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'RNTRC'. The field 'RNTRC' has
     * the following description: Registro Nacional dos
     * Transportadores Rodoviários de Carga
     * 
     * @return the value of field 'RNTRC'.
     */
    public java.lang.String getRNTRC(
    ) {
        return this._RNTRC;
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
     * Sets the value of field 'RNTRC'. The field 'RNTRC' has the
     * following description: Registro Nacional dos Transportadores
     * Rodoviários de Carga
     * 
     * @param RNTRC the value of field 'RNTRC'.
     */
    public void setRNTRC(
            final java.lang.String RNTRC) {
        this._RNTRC = RNTRC;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled org.exolab.castor.builder.binding.Pro
     */
    public static com.mercurio.lms.mdfe.model.v100.Prop unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100.Prop) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100.Prop.class, reader);
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
