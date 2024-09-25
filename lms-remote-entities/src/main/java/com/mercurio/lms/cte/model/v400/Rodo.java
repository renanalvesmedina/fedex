/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Informações do modal Rodoviário
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Rodo implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Registro Nacional de Transportadores Rodoviários de Carga
     */
    private String _RNTRC;

    /**
     * Ordens de Coleta associados
     */
    private java.util.Vector<Occ> _occList;

      //----------------/
     //- Constructors -/
    //----------------/

    public Rodo() {
        super();
        this._occList = new java.util.Vector<Occ>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     *
     *
     * @param vOcc
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addOcc(
            final Occ vOcc)
    throws IndexOutOfBoundsException {
        // check for the maximum size
        if (this._occList.size() >= 10) {
            throw new IndexOutOfBoundsException("addOcc has a maximum of 10");
        }

        this._occList.addElement(vOcc);
    }

    /**
     *
     *
     * @param index
     * @param vOcc
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addOcc(
            final int index,
            final Occ vOcc)
    throws IndexOutOfBoundsException {
        // check for the maximum size
        if (this._occList.size() >= 10) {
            throw new IndexOutOfBoundsException("addOcc has a maximum of 10");
        }

        this._occList.add(index, vOcc);
    }

    /**
     * Method enumerateOcc.
     *
     * @return an Enumeration over all
     * com.mercurio.lms.cte.model.v400.Occ elements
     */
    public java.util.Enumeration<? extends Occ> enumerateOcc(
    ) {
        return this._occList.elements();
    }

    /**
     * Method getOcc.
     *
     * @param index
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.mercurio.lms.cte.model.v400.Occ
     * at the given index
     */
    public Occ getOcc(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._occList.size()) {
            throw new IndexOutOfBoundsException("getOcc: Index value '" + index + "' not in range [0.." + (this._occList.size() - 1) + "]");
        }

        return (Occ) _occList.get(index);
    }

    /**
     * Method getOcc.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     *
     * @return this collection as an Array
     */
    public Occ[] getOcc(
    ) {
        Occ[] array = new Occ[0];
        return (Occ[]) this._occList.toArray(array);
    }

    /**
     * Method getOccCount.
     *
     * @return the size of this collection
     */
    public int getOccCount(
    ) {
        return this._occList.size();
    }

    /**
     * Returns the value of field 'RNTRC'. The field 'RNTRC' has
     * the following description: Registro Nacional de
     * Transportadores Rodoviários de Carga
     *
     * @return the value of field 'RNTRC'.
     */
    public String getRNTRC(
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
     */
    public void removeAllOcc(
    ) {
        this._occList.clear();
    }

    /**
     * Method removeOcc.
     *
     * @param vOcc
     * @return true if the object was removed from the collection.
     */
    public boolean removeOcc(
            final Occ vOcc) {
        boolean removed = _occList.remove(vOcc);
        return removed;
    }

    /**
     * Method removeOccAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public Occ removeOccAt(
            final int index) {
        Object obj = this._occList.remove(index);
        return (Occ) obj;
    }

    /**
     *
     *
     * @param index
     * @param vOcc
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setOcc(
            final int index,
            final Occ vOcc)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._occList.size()) {
            throw new IndexOutOfBoundsException("setOcc: Index value '" + index + "' not in range [0.." + (this._occList.size() - 1) + "]");
        }

        this._occList.set(index, vOcc);
    }

    /**
     *
     *
     * @param vOccArray
     */
    public void setOcc(
            final Occ[] vOccArray) {
        //-- copy array
        _occList.clear();

        for (int i = 0; i < vOccArray.length; i++) {
                this._occList.add(vOccArray[i]);
        }
    }

    /**
     * Sets the value of field 'RNTRC'. The field 'RNTRC' has the
     * following description: Registro Nacional de Transportadores
     * Rodoviários de Carga
     *
     * @param RNTRC the value of field 'RNTRC'.
     */
    public void setRNTRC(
            final String RNTRC) {
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
     * @return the unmarshaled com.mercurio.lms.cte.model.v400.Rodo
     */
    public static Rodo unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (Rodo) org.exolab.castor.xml.Unmarshaller.unmarshal(Rodo.class, reader);
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
