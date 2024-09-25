/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 * Tipo Dados Unidade de Transporte
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TUnidadeTransp implements java.io.Serializable {

    /**
     * Tipo da Unidade de Transporte
     */
    private com.mercurio.lms.mdfe.model.v300.types.TtipoUnidTransp tpUnidTransp;

    /**
     * Identificação da Unidade de Transporte
     */
    private java.lang.String idUnidTransp;

    /**
     * Lacres das Unidades de Transporte
     */
    private java.util.Vector<com.mercurio.lms.mdfe.model.v300.LacUnidTransp> lacUnidTranspList;

    /**
     * Informações das Unidades de Carga (Containeres/ULD/Outros)
     */
    private java.util.Vector<com.mercurio.lms.mdfe.model.v300.InfUnidCarga> infUnidCargaList;

    /**
     * Quantidade rateada (Peso,Volume)
     */
    private java.lang.String qtdRat;

    public TUnidadeTransp() {
        super();
        this.lacUnidTranspList = new java.util.Vector<com.mercurio.lms.mdfe.model.v300.LacUnidTransp>();
        this.infUnidCargaList = new java.util.Vector<com.mercurio.lms.mdfe.model.v300.InfUnidCarga>();
    }

    /**
     * 
     * 
     * @param vInfUnidCarga
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfUnidCarga(final com.mercurio.lms.mdfe.model.v300.InfUnidCarga vInfUnidCarga) throws java.lang.IndexOutOfBoundsException {
        this.infUnidCargaList.addElement(vInfUnidCarga);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfUnidCarga
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfUnidCarga(final int index,final com.mercurio.lms.mdfe.model.v300.InfUnidCarga vInfUnidCarga) throws java.lang.IndexOutOfBoundsException {
        this.infUnidCargaList.add(index, vInfUnidCarga);
    }

    /**
     * 
     * 
     * @param vLacUnidTransp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacUnidTransp(final com.mercurio.lms.mdfe.model.v300.LacUnidTransp vLacUnidTransp) throws java.lang.IndexOutOfBoundsException {
        this.lacUnidTranspList.addElement(vLacUnidTransp);
    }

    /**
     * 
     * 
     * @param index
     * @param vLacUnidTransp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addLacUnidTransp(final int index,final com.mercurio.lms.mdfe.model.v300.LacUnidTransp vLacUnidTransp) throws java.lang.IndexOutOfBoundsException {
        this.lacUnidTranspList.add(index, vLacUnidTransp);
    }

    /**
     * Method enumerateInfUnidCarga.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.mdfe.model.v300.InfUnidCarga elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v300.InfUnidCarga> enumerateInfUnidCarga() {
        return this.infUnidCargaList.elements();
    }

    /**
     * Method enumerateLacUnidTransp.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.mdfe.model.v300.LacUnidTransp elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v300.LacUnidTransp> enumerateLacUnidTransp() {
        return this.lacUnidTranspList.elements();
    }

    /**
     * Returns the value of field 'idUnidTransp'. The field
     * 'idUnidTransp' has the following description:
     * Identificação da Unidade de Transporte
     * 
     * @return the value of field 'IdUnidTransp'.
     */
    public java.lang.String getIdUnidTransp() {
        return this.idUnidTransp;
    }

    /**
     * Method getInfUnidCarga.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.mdfe.model.v300.InfUnidCarga at the given
     * index
     */
    public com.mercurio.lms.mdfe.model.v300.InfUnidCarga getInfUnidCarga(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.infUnidCargaList.size()) {
            throw new IndexOutOfBoundsException("getInfUnidCarga: Index value '" + index + "' not in range [0.." + (this.infUnidCargaList.size() - 1) + "]");
        }

        return infUnidCargaList.get(index);
    }

    /**
     * Method getInfUnidCarga.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v300.InfUnidCarga[] getInfUnidCarga() {
        com.mercurio.lms.mdfe.model.v300.InfUnidCarga[] array = new com.mercurio.lms.mdfe.model.v300.InfUnidCarga[0];
        return this.infUnidCargaList.toArray(array);
    }

    /**
     * Method getInfUnidCargaCount.
     * 
     * @return the size of this collection
     */
    public int getInfUnidCargaCount() {
        return this.infUnidCargaList.size();
    }

    /**
     * Method getLacUnidTransp.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.mdfe.model.v300.LacUnidTransp at the given
     * index
     */
    public com.mercurio.lms.mdfe.model.v300.LacUnidTransp getLacUnidTransp(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.lacUnidTranspList.size()) {
            throw new IndexOutOfBoundsException("getLacUnidTransp: Index value '" + index + "' not in range [0.." + (this.lacUnidTranspList.size() - 1) + "]");
        }

        return lacUnidTranspList.get(index);
    }

    /**
     * Method getLacUnidTransp.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v300.LacUnidTransp[] getLacUnidTransp() {
        com.mercurio.lms.mdfe.model.v300.LacUnidTransp[] array = new com.mercurio.lms.mdfe.model.v300.LacUnidTransp[0];
        return this.lacUnidTranspList.toArray(array);
    }

    /**
     * Method getLacUnidTranspCount.
     * 
     * @return the size of this collection
     */
    public int getLacUnidTranspCount() {
        return this.lacUnidTranspList.size();
    }

    /**
     * Returns the value of field 'qtdRat'. The field 'qtdRat' has
     * the following description: Quantidade rateada (Peso,Volume)
     * 
     * @return the value of field 'QtdRat'.
     */
    public java.lang.String getQtdRat() {
        return this.qtdRat;
    }

    /**
     * Returns the value of field 'tpUnidTransp'. The field
     * 'tpUnidTransp' has the following description: Tipo da
     * Unidade de Transporte
     * 
     * @return the value of field 'TpUnidTransp'.
     */
    public com.mercurio.lms.mdfe.model.v300.types.TtipoUnidTransp getTpUnidTransp() {
        return this.tpUnidTransp;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid() {
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
    public void marshal(final java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
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
    public void marshal(final org.xml.sax.ContentHandler handler) throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }

    /**
     */
    public void removeAllInfUnidCarga() {
        this.infUnidCargaList.clear();
    }

    /**
     */
    public void removeAllLacUnidTransp() {
        this.lacUnidTranspList.clear();
    }

    /**
     * Method removeInfUnidCarga.
     * 
     * @param vInfUnidCarga
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfUnidCarga(final com.mercurio.lms.mdfe.model.v300.InfUnidCarga vInfUnidCarga) {
        boolean removed = infUnidCargaList.remove(vInfUnidCarga);
        return removed;
    }

    /**
     * Method removeInfUnidCargaAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v300.InfUnidCarga removeInfUnidCargaAt(final int index) {
        java.lang.Object obj = this.infUnidCargaList.remove(index);
        return (com.mercurio.lms.mdfe.model.v300.InfUnidCarga) obj;
    }

    /**
     * Method removeLacUnidTransp.
     * 
     * @param vLacUnidTransp
     * @return true if the object was removed from the collection.
     */
    public boolean removeLacUnidTransp(final com.mercurio.lms.mdfe.model.v300.LacUnidTransp vLacUnidTransp) {
        boolean removed = lacUnidTranspList.remove(vLacUnidTransp);
        return removed;
    }

    /**
     * Method removeLacUnidTranspAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v300.LacUnidTransp removeLacUnidTranspAt(final int index) {
        java.lang.Object obj = this.lacUnidTranspList.remove(index);
        return (com.mercurio.lms.mdfe.model.v300.LacUnidTransp) obj;
    }

    /**
     * Sets the value of field 'idUnidTransp'. The field
     * 'idUnidTransp' has the following description:
     * Identificação da Unidade de Transporte
     * 
     * @param idUnidTransp the value of field 'idUnidTransp'.
     */
    public void setIdUnidTransp(final java.lang.String idUnidTransp) {
        this.idUnidTransp = idUnidTransp;
    }

    /**
     * 
     * 
     * @param index
     * @param vInfUnidCarga
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfUnidCarga(final int index,final com.mercurio.lms.mdfe.model.v300.InfUnidCarga vInfUnidCarga) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.infUnidCargaList.size()) {
            throw new IndexOutOfBoundsException("setInfUnidCarga: Index value '" + index + "' not in range [0.." + (this.infUnidCargaList.size() - 1) + "]");
        }

        this.infUnidCargaList.set(index, vInfUnidCarga);
    }

    /**
     * 
     * 
     * @param vInfUnidCargaArray
     */
    public void setInfUnidCarga(final com.mercurio.lms.mdfe.model.v300.InfUnidCarga[] vInfUnidCargaArray) {
        //-- copy array
        infUnidCargaList.clear();

        for (int i = 0; i < vInfUnidCargaArray.length; i++) {
                this.infUnidCargaList.add(vInfUnidCargaArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vLacUnidTransp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setLacUnidTransp(final int index,final com.mercurio.lms.mdfe.model.v300.LacUnidTransp vLacUnidTransp) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.lacUnidTranspList.size()) {
            throw new IndexOutOfBoundsException("setLacUnidTransp: Index value '" + index + "' not in range [0.." + (this.lacUnidTranspList.size() - 1) + "]");
        }

        this.lacUnidTranspList.set(index, vLacUnidTransp);
    }

    /**
     * 
     * 
     * @param vLacUnidTranspArray
     */
    public void setLacUnidTransp(final com.mercurio.lms.mdfe.model.v300.LacUnidTransp[] vLacUnidTranspArray) {
        //-- copy array
        lacUnidTranspList.clear();

        for (int i = 0; i < vLacUnidTranspArray.length; i++) {
                this.lacUnidTranspList.add(vLacUnidTranspArray[i]);
        }
    }

    /**
     * Sets the value of field 'qtdRat'. The field 'qtdRat' has the
     * following description: Quantidade rateada (Peso,Volume)
     * 
     * @param qtdRat the value of field 'qtdRat'.
     */
    public void setQtdRat(final java.lang.String qtdRat) {
        this.qtdRat = qtdRat;
    }

    /**
     * Sets the value of field 'tpUnidTransp'. The field
     * 'tpUnidTransp' has the following description: Tipo da
     * Unidade de Transporte
     * 
     * @param tpUnidTransp the value of field 'tpUnidTransp'.
     */
    public void setTpUnidTransp(final com.mercurio.lms.mdfe.model.v300.types.TtipoUnidTransp tpUnidTransp) {
        this.tpUnidTransp = tpUnidTransp;
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
     * com.mercurio.lms.mdfe.model.v300.TUnidadeTransp
     */
    public static com.mercurio.lms.mdfe.model.v300.TUnidadeTransp unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.TUnidadeTransp) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.TUnidadeTransp.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
