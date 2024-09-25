/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Entrega com data definida
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class ComData implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Tipo de data/período programado para entrega:
     *  1-Na data;
     *  2-Até a data;
     *  3-A partir da data
     */
    private com.mercurio.lms.cte.model.v103.types.TpPerType _tpPer;

    /**
     * Data programada 
     */
    private java.lang.String _dProg;


      //----------------/
     //- Constructors -/
    //----------------/

    public ComData() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dProg'. The field 'dProg' has
     * the following description: Data programada 
     * 
     * @return the value of field 'DProg'.
     */
    public java.lang.String getDProg(
    ) {
        return this._dProg;
    }

    /**
     * Returns the value of field 'tpPer'. The field 'tpPer' has
     * the following description: Tipo de data/período programado
     * para entrega:
     *  1-Na data;
     *  2-Até a data;
     *  3-A partir da data
     * 
     * @return the value of field 'TpPer'.
     */
    public com.mercurio.lms.cte.model.v103.types.TpPerType getTpPer(
    ) {
        return this._tpPer;
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
     * Sets the value of field 'dProg'. The field 'dProg' has the
     * following description: Data programada 
     * 
     * @param dProg the value of field 'dProg'.
     */
    public void setDProg(
            final java.lang.String dProg) {
        this._dProg = dProg;
    }

    /**
     * Sets the value of field 'tpPer'. The field 'tpPer' has the
     * following description: Tipo de data/período programado para
     * entrega:
     *  1-Na data;
     *  2-Até a data;
     *  3-A partir da data
     * 
     * @param tpPer the value of field 'tpPer'.
     */
    public void setTpPer(
            final com.mercurio.lms.cte.model.v103.types.TpPerType tpPer) {
        this._tpPer = tpPer;
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
     * com.mercurio.lms.cte.model.v103.ComData
     */
    public static com.mercurio.lms.cte.model.v103.ComData unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.ComData) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.ComData.class, reader);
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
