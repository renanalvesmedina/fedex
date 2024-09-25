/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100a;

/**
 * Informações do modal Rodoviário
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Rodo implements java.io.Serializable {

    /**
     * Registro Nacional de Transportadores Rodoviários de Carga
     */
    private java.lang.String RNTRC;

    /**
     * Código Identificador da Operação de Transporte
     */
    private java.util.Vector<java.lang.String> CIOTList;

    /**
     * Dados do Veículo com a Tração
     */
    private com.mercurio.lms.mdfe.model.v100a.VeicTracao veicTracao;

    /**
     * Dados dos reboques
     */
    private java.util.Vector<com.mercurio.lms.mdfe.model.v100a.VeicReboque> veicReboqueList;

    /**
     * Informações de Vale Pedágio
     */
    private com.mercurio.lms.mdfe.model.v100a.ValePed valePed;

    /**
     * Código de Agendamento no porto
     */
    private java.lang.String codAgPorto;

    public Rodo() {
        super();
        this.CIOTList = new java.util.Vector<java.lang.String>();
        this.veicReboqueList = new java.util.Vector<com.mercurio.lms.mdfe.model.v100a.VeicReboque>();
    }

    /**
     * 
     * 
     * @param vCIOT
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCIOT(final java.lang.String vCIOT) throws java.lang.IndexOutOfBoundsException {
        this.CIOTList.addElement(vCIOT);
    }

    /**
     * 
     * 
     * @param index
     * @param vCIOT
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCIOT(final int index,final java.lang.String vCIOT) throws java.lang.IndexOutOfBoundsException {
        this.CIOTList.add(index, vCIOT);
    }

    /**
     * 
     * 
     * @param vVeicReboque
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addVeicReboque(final com.mercurio.lms.mdfe.model.v100a.VeicReboque vVeicReboque) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.veicReboqueList.size() >= 3) {
            throw new IndexOutOfBoundsException("addVeicReboque has a maximum of 3");
        }

        this.veicReboqueList.addElement(vVeicReboque);
    }

    /**
     * 
     * 
     * @param index
     * @param vVeicReboque
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addVeicReboque(final int index,final com.mercurio.lms.mdfe.model.v100a.VeicReboque vVeicReboque) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.veicReboqueList.size() >= 3) {
            throw new IndexOutOfBoundsException("addVeicReboque has a maximum of 3");
        }

        this.veicReboqueList.add(index, vVeicReboque);
    }

    /**
     * Method enumerateCIOT.
     * 
     * @return an Enumeration over all java.lang.String elements
     */
    public java.util.Enumeration<? extends java.lang.String> enumerateCIOT() {
        return this.CIOTList.elements();
    }

    /**
     * Method enumerateVeicReboque.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.mdfe.model.v100a.VeicReboque elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v100a.VeicReboque> enumerateVeicReboque() {
        return this.veicReboqueList.elements();
    }

    /**
     * Method getCIOT.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the java.lang.String at the given index
     */
    public java.lang.String getCIOT(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.CIOTList.size()) {
            throw new IndexOutOfBoundsException("getCIOT: Index value '" + index + "' not in range [0.." + (this.CIOTList.size() - 1) + "]");
        }

        return (java.lang.String) CIOTList.get(index);
    }

    /**
     * Method getCIOT.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public java.lang.String[] getCIOT() {
        java.lang.String[] array = new java.lang.String[0];
        return this.CIOTList.toArray(array);
    }

    /**
     * Method getCIOTCount.
     * 
     * @return the size of this collection
     */
    public int getCIOTCount() {
        return this.CIOTList.size();
    }

    /**
     * Returns the value of field 'codAgPorto'. The field
     * 'codAgPorto' has the following description: Código de
     * Agendamento no porto
     * 
     * @return the value of field 'CodAgPorto'.
     */
    public java.lang.String getCodAgPorto() {
        return this.codAgPorto;
    }

    /**
     * Returns the value of field 'RNTRC'. The field 'RNTRC' has
     * the following description: Registro Nacional de
     * Transportadores Rodoviários de Carga
     * 
     * @return the value of field 'RNTRC'.
     */
    public java.lang.String getRNTRC() {
        return this.RNTRC;
    }

    /**
     * Returns the value of field 'valePed'. The field 'valePed'
     * has the following description: Informações de Vale
     * Pedágio
     * 
     * @return the value of field 'ValePed'.
     */
    public com.mercurio.lms.mdfe.model.v100a.ValePed getValePed() {
        return this.valePed;
    }

    /**
     * Method getVeicReboque.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.mdfe.model.v100a.VeicReboque at the given
     * index
     */
    public com.mercurio.lms.mdfe.model.v100a.VeicReboque getVeicReboque(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.veicReboqueList.size()) {
            throw new IndexOutOfBoundsException("getVeicReboque: Index value '" + index + "' not in range [0.." + (this.veicReboqueList.size() - 1) + "]");
        }

        return veicReboqueList.get(index);
    }

    /**
     * Method getVeicReboque.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v100a.VeicReboque[] getVeicReboque() {
        com.mercurio.lms.mdfe.model.v100a.VeicReboque[] array = new com.mercurio.lms.mdfe.model.v100a.VeicReboque[0];
        return this.veicReboqueList.toArray(array);
    }

    /**
     * Method getVeicReboqueCount.
     * 
     * @return the size of this collection
     */
    public int getVeicReboqueCount() {
        return this.veicReboqueList.size();
    }

    /**
     * Returns the value of field 'veicTracao'. The field
     * 'veicTracao' has the following description: Dados do
     * Veículo com a Tração
     * 
     * @return the value of field 'VeicTracao'.
     */
    public com.mercurio.lms.mdfe.model.v100a.VeicTracao getVeicTracao() {
        return this.veicTracao;
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
    public void removeAllCIOT() {
        this.CIOTList.clear();
    }

    /**
     */
    public void removeAllVeicReboque() {
        this.veicReboqueList.clear();
    }

    /**
     * Method removeCIOT.
     * 
     * @param vCIOT
     * @return true if the object was removed from the collection.
     */
    public boolean removeCIOT(final java.lang.String vCIOT) {
        boolean removed = CIOTList.remove(vCIOT);
        return removed;
    }

    /**
     * Method removeCIOTAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public java.lang.String removeCIOTAt(final int index) {
        java.lang.Object obj = this.CIOTList.remove(index);
        return (java.lang.String) obj;
    }

    /**
     * Method removeVeicReboque.
     * 
     * @param vVeicReboque
     * @return true if the object was removed from the collection.
     */
    public boolean removeVeicReboque(final com.mercurio.lms.mdfe.model.v100a.VeicReboque vVeicReboque) {
        boolean removed = veicReboqueList.remove(vVeicReboque);
        return removed;
    }

    /**
     * Method removeVeicReboqueAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v100a.VeicReboque removeVeicReboqueAt(final int index) {
        java.lang.Object obj = this.veicReboqueList.remove(index);
        return (com.mercurio.lms.mdfe.model.v100a.VeicReboque) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vCIOT
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setCIOT(final int index,final java.lang.String vCIOT) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.CIOTList.size()) {
            throw new IndexOutOfBoundsException("setCIOT: Index value '" + index + "' not in range [0.." + (this.CIOTList.size() - 1) + "]");
        }

        this.CIOTList.set(index, vCIOT);
    }

    /**
     * 
     * 
     * @param vCIOTArray
     */
    public void setCIOT(final java.lang.String[] vCIOTArray) {
        //-- copy array
        CIOTList.clear();

        for (int i = 0; i < vCIOTArray.length; i++) {
                this.CIOTList.add(vCIOTArray[i]);
        }
    }

    /**
     * Sets the value of field 'codAgPorto'. The field 'codAgPorto'
     * has the following description: Código de Agendamento no
     * porto
     * 
     * @param codAgPorto the value of field 'codAgPorto'.
     */
    public void setCodAgPorto(final java.lang.String codAgPorto) {
        this.codAgPorto = codAgPorto;
    }

    /**
     * Sets the value of field 'RNTRC'. The field 'RNTRC' has the
     * following description: Registro Nacional de Transportadores
     * Rodoviários de Carga
     * 
     * @param RNTRC the value of field 'RNTRC'.
     */
    public void setRNTRC(final java.lang.String RNTRC) {
        this.RNTRC = RNTRC;
    }

    /**
     * Sets the value of field 'valePed'. The field 'valePed' has
     * the following description: Informações de Vale Pedágio
     * 
     * @param valePed the value of field 'valePed'.
     */
    public void setValePed(final com.mercurio.lms.mdfe.model.v100a.ValePed valePed) {
        this.valePed = valePed;
    }

    /**
     * 
     * 
     * @param index
     * @param vVeicReboque
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setVeicReboque(final int index,final com.mercurio.lms.mdfe.model.v100a.VeicReboque vVeicReboque) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.veicReboqueList.size()) {
            throw new IndexOutOfBoundsException("setVeicReboque: Index value '" + index + "' not in range [0.." + (this.veicReboqueList.size() - 1) + "]");
        }

        this.veicReboqueList.set(index, vVeicReboque);
    }

    /**
     * 
     * 
     * @param vVeicReboqueArray
     */
    public void setVeicReboque(final com.mercurio.lms.mdfe.model.v100a.VeicReboque[] vVeicReboqueArray) {
        //-- copy array
        veicReboqueList.clear();

        for (int i = 0; i < vVeicReboqueArray.length; i++) {
                this.veicReboqueList.add(vVeicReboqueArray[i]);
        }
    }

    /**
     * Sets the value of field 'veicTracao'. The field 'veicTracao'
     * has the following description: Dados do Veículo com a
     * Tração
     * 
     * @param veicTracao the value of field 'veicTracao'.
     */
    public void setVeicTracao(final com.mercurio.lms.mdfe.model.v100a.VeicTracao veicTracao) {
        this.veicTracao = veicTracao;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.mdfe.model.v100a.Rod
     */
    public static com.mercurio.lms.mdfe.model.v100a.Rodo unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100a.Rodo) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100a.Rodo.class, reader);
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
