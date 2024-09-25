/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Entrega com hora definida
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class ComHora implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 1--No horário;
     * 2-Até o horário;
     * 3-A partir do horário
     */
    private com.mercurio.lms.cte.model.v103.types.TpHorType _tpHor;

    /**
     * Hora programada 
     */
    private java.lang.String _hProg;


      //----------------/
     //- Constructors -/
    //----------------/

    public ComHora() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'hProg'. The field 'hProg' has
     * the following description: Hora programada 
     * 
     * @return the value of field 'HProg'.
     */
    public java.lang.String getHProg(
    ) {
        return this._hProg;
    }

    /**
     * Returns the value of field 'tpHor'. The field 'tpHor' has
     * the following description: 1--No horário;
     * 2-Até o horário;
     * 3-A partir do horário
     * 
     * @return the value of field 'TpHor'.
     */
    public com.mercurio.lms.cte.model.v103.types.TpHorType getTpHor(
    ) {
        return this._tpHor;
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
     * Sets the value of field 'hProg'. The field 'hProg' has the
     * following description: Hora programada 
     * 
     * @param hProg the value of field 'hProg'.
     */
    public void setHProg(
            final java.lang.String hProg) {
        this._hProg = hProg;
    }

    /**
     * Sets the value of field 'tpHor'. The field 'tpHor' has the
     * following description: 1--No horário;
     * 2-Até o horário;
     * 3-A partir do horário
     * 
     * @param tpHor the value of field 'tpHor'.
     */
    public void setTpHor(
            final com.mercurio.lms.cte.model.v103.types.TpHorType tpHor) {
        this._tpHor = tpHor;
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
     * com.mercurio.lms.cte.model.v103.ComHora
     */
    public static com.mercurio.lms.cte.model.v103.ComHora unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.ComHora) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.ComHora.class, reader);
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
