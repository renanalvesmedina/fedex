/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Tipo Pedido de Concessão de Autorização da CT-e
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TEnviCTe implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _versao.
     */
    private java.lang.String _versao;

    /**
     * Field _idLote.
     */
    private java.lang.String _idLote;

    /**
     * Field _CTeList.
     */
    private java.util.List<com.mercurio.lms.cte.model.v103.CTe> _CTeList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TEnviCTe() {
        super();
        this._CTeList = new java.util.ArrayList<com.mercurio.lms.cte.model.v103.CTe>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vCTe
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCTe(
            final com.mercurio.lms.cte.model.v103.CTe vCTe)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._CTeList.size() >= 50) {
            throw new IndexOutOfBoundsException("addCTe has a maximum of 50");
        }

        this._CTeList.add(vCTe);
    }

    /**
     * 
     * 
     * @param index
     * @param vCTe
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCTe(
            final int index,
            final com.mercurio.lms.cte.model.v103.CTe vCTe)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._CTeList.size() >= 50) {
            throw new IndexOutOfBoundsException("addCTe has a maximum of 50");
        }

        this._CTeList.add(index, vCTe);
    }

    /**
     * Method enumerateCTe.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.mercurio.lms.cte.model.v103.CTe> enumerateCTe(
    ) {
        return java.util.Collections.enumeration(this._CTeList);
    }

    /**
     * Method getCTe.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.mercurio.lms.cte.model.v103.CTe
     * at the given index
     */
    public com.mercurio.lms.cte.model.v103.CTe getCTe(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._CTeList.size()) {
            throw new IndexOutOfBoundsException("getCTe: Index value '" + index + "' not in range [0.." + (this._CTeList.size() - 1) + "]");
        }

        return (com.mercurio.lms.cte.model.v103.CTe) _CTeList.get(index);
    }

    /**
     * Method getCTe.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.cte.model.v103.CTe[] getCTe(
    ) {
        com.mercurio.lms.cte.model.v103.CTe[] array = new com.mercurio.lms.cte.model.v103.CTe[0];
        return (com.mercurio.lms.cte.model.v103.CTe[]) this._CTeList.toArray(array);
    }

    /**
     * Method getCTeCount.
     * 
     * @return the size of this collection
     */
    public int getCTeCount(
    ) {
        return this._CTeList.size();
    }

    /**
     * Returns the value of field 'idLote'.
     * 
     * @return the value of field 'IdLote'.
     */
    public java.lang.String getIdLote(
    ) {
        return this._idLote;
    }

    /**
     * Returns the value of field 'versao'.
     * 
     * @return the value of field 'Versao'.
     */
    public java.lang.String getVersao(
    ) {
        return this._versao;
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
     * Method iterateCTe.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.mercurio.lms.cte.model.v103.CTe> iterateCTe(
    ) {
        return this._CTeList.iterator();
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
    public void removeAllCTe(
    ) {
        this._CTeList.clear();
    }

    /**
     * Method removeCTe.
     * 
     * @param vCTe
     * @return true if the object was removed from the collection.
     */
    public boolean removeCTe(
            final com.mercurio.lms.cte.model.v103.CTe vCTe) {
        boolean removed = _CTeList.remove(vCTe);
        return removed;
    }

    /**
     * Method removeCTeAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.cte.model.v103.CTe removeCTeAt(
            final int index) {
        java.lang.Object obj = this._CTeList.remove(index);
        return (com.mercurio.lms.cte.model.v103.CTe) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vCTe
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setCTe(
            final int index,
            final com.mercurio.lms.cte.model.v103.CTe vCTe)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._CTeList.size()) {
            throw new IndexOutOfBoundsException("setCTe: Index value '" + index + "' not in range [0.." + (this._CTeList.size() - 1) + "]");
        }

        this._CTeList.set(index, vCTe);
    }

    /**
     * 
     * 
     * @param vCTeArray
     */
    public void setCTe(
            final com.mercurio.lms.cte.model.v103.CTe[] vCTeArray) {
        //-- copy array
        _CTeList.clear();

        for (int i = 0; i < vCTeArray.length; i++) {
                this._CTeList.add(vCTeArray[i]);
        }
    }

    /**
     * Sets the value of field 'idLote'.
     * 
     * @param idLote the value of field 'idLote'.
     */
    public void setIdLote(
            final java.lang.String idLote) {
        this._idLote = idLote;
    }

    /**
     * Sets the value of field 'versao'.
     * 
     * @param versao the value of field 'versao'.
     */
    public void setVersao(
            final java.lang.String versao) {
        this._versao = versao;
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
     * com.mercurio.lms.cte.model.v103.TEnviCTe
     */
    public static com.mercurio.lms.cte.model.v103.TEnviCTe unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.TEnviCTe) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.TEnviCTe.class, reader);
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
