/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100a;

/**
 * Dados do Veículo com a Tração
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class VeicTracao implements java.io.Serializable {

    /**
     * Código interno do veículo 
     */
    private java.lang.String cInt;

    /**
     * Placa do veículo 
     */
    private java.lang.String placa;

    /**
     * RENAVAM do veículo 
     */
    private java.lang.String RENAVAM;

    /**
     * Tara em KG
     */
    private java.lang.String tara;

    /**
     * Capacidade em KG
     */
    private java.lang.String capKG;

    /**
     * Capacidade em M3
     */
    private java.lang.String capM3;

    /**
     * Proprietários do Veículo.
     * Só preenchido quando o veículo não pertencer à empresa
     * emitente do MDF-e
     */
    private com.mercurio.lms.mdfe.model.v100a.Prop prop;

    /**
     * Informações do(s) Condutor(s) do veículo
     */
    private java.util.Vector<com.mercurio.lms.mdfe.model.v100a.Condutor> condutorList;

    /**
     * Tipo de Rodado
     */
    private com.mercurio.lms.mdfe.model.v100a.types.TpRodType tpRod;

    /**
     * Tipo de Carroceria
     */
    private com.mercurio.lms.mdfe.model.v100a.types.TpCarType tpCar;

    /**
     * UF em que veículo está licenciado
     */
    private com.mercurio.lms.mdfe.model.v100a.types.TUf UF;

    public VeicTracao() {
        super();
        this.condutorList = new java.util.Vector<com.mercurio.lms.mdfe.model.v100a.Condutor>();
    }

    /**
     * 
     * 
     * @param vCondutor
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCondutor(final com.mercurio.lms.mdfe.model.v100a.Condutor vCondutor) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.condutorList.size() >= 10) {
            throw new IndexOutOfBoundsException("addCondutor has a maximum of 10");
        }

        this.condutorList.addElement(vCondutor);
    }

    /**
     * 
     * 
     * @param index
     * @param vCondutor
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCondutor(final int index,final com.mercurio.lms.mdfe.model.v100a.Condutor vCondutor) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.condutorList.size() >= 10) {
            throw new IndexOutOfBoundsException("addCondutor has a maximum of 10");
        }

        this.condutorList.add(index, vCondutor);
    }

    /**
     * Method enumerateCondutor.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.mdfe.model.v100a.Condutor elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v100a.Condutor> enumerateCondutor() {
        return this.condutorList.elements();
    }

    /**
     * Returns the value of field 'cInt'. The field 'cInt' has the
     * following description: Código interno do veículo 
     * 
     * @return the value of field 'CInt'.
     */
    public java.lang.String getCInt() {
        return this.cInt;
    }

    /**
     * Returns the value of field 'capKG'. The field 'capKG' has
     * the following description: Capacidade em KG
     * 
     * @return the value of field 'CapKG'.
     */
    public java.lang.String getCapKG() {
        return this.capKG;
    }

    /**
     * Returns the value of field 'capM3'. The field 'capM3' has
     * the following description: Capacidade em M3
     * 
     * @return the value of field 'CapM3'.
     */
    public java.lang.String getCapM3() {
        return this.capM3;
    }

    /**
     * Method getCondutor.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.mdfe.model.v100a.Condutor at the given index
     */
    public com.mercurio.lms.mdfe.model.v100a.Condutor getCondutor(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.condutorList.size()) {
            throw new IndexOutOfBoundsException("getCondutor: Index value '" + index + "' not in range [0.." + (this.condutorList.size() - 1) + "]");
        }

        return condutorList.get(index);
    }

    /**
     * Method getCondutor.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v100a.Condutor[] getCondutor() {
        com.mercurio.lms.mdfe.model.v100a.Condutor[] array = new com.mercurio.lms.mdfe.model.v100a.Condutor[0];
        return this.condutorList.toArray(array);
    }

    /**
     * Method getCondutorCount.
     * 
     * @return the size of this collection
     */
    public int getCondutorCount() {
        return this.condutorList.size();
    }

    /**
     * Returns the value of field 'placa'. The field 'placa' has
     * the following description: Placa do veículo 
     * 
     * @return the value of field 'Placa'.
     */
    public java.lang.String getPlaca() {
        return this.placa;
    }

    /**
     * Returns the value of field 'prop'. The field 'prop' has the
     * following description: Proprietários do Veículo.
     * Só preenchido quando o veículo não pertencer à empresa
     * emitente do MDF-e
     * 
     * @return the value of field 'Prop'.
     */
    public com.mercurio.lms.mdfe.model.v100a.Prop getProp() {
        return this.prop;
    }

    /**
     * Returns the value of field 'RENAVAM'. The field 'RENAVAM'
     * has the following description: RENAVAM do veículo 
     * 
     * @return the value of field 'RENAVAM'.
     */
    public java.lang.String getRENAVAM() {
        return this.RENAVAM;
    }

    /**
     * Returns the value of field 'tara'. The field 'tara' has the
     * following description: Tara em KG
     * 
     * @return the value of field 'Tara'.
     */
    public java.lang.String getTara() {
        return this.tara;
    }

    /**
     * Returns the value of field 'tpCar'. The field 'tpCar' has
     * the following description: Tipo de Carroceria
     * 
     * @return the value of field 'TpCar'.
     */
    public com.mercurio.lms.mdfe.model.v100a.types.TpCarType getTpCar() {
        return this.tpCar;
    }

    /**
     * Returns the value of field 'tpRod'. The field 'tpRod' has
     * the following description: Tipo de Rodado
     * 
     * @return the value of field 'TpRod'.
     */
    public com.mercurio.lms.mdfe.model.v100a.types.TpRodType getTpRod() {
        return this.tpRod;
    }

    /**
     * Returns the value of field 'UF'. The field 'UF' has the
     * following description: UF em que veículo está licenciado
     * 
     * @return the value of field 'UF'.
     */
    public com.mercurio.lms.mdfe.model.v100a.types.TUf getUF() {
        return this.UF;
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
    public void removeAllCondutor() {
        this.condutorList.clear();
    }

    /**
     * Method removeCondutor.
     * 
     * @param vCondutor
     * @return true if the object was removed from the collection.
     */
    public boolean removeCondutor(final com.mercurio.lms.mdfe.model.v100a.Condutor vCondutor) {
        boolean removed = condutorList.remove(vCondutor);
        return removed;
    }

    /**
     * Method removeCondutorAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v100a.Condutor removeCondutorAt(final int index) {
        java.lang.Object obj = this.condutorList.remove(index);
        return (com.mercurio.lms.mdfe.model.v100a.Condutor) obj;
    }

    /**
     * Sets the value of field 'cInt'. The field 'cInt' has the
     * following description: Código interno do veículo 
     * 
     * @param cInt the value of field 'cInt'.
     */
    public void setCInt(final java.lang.String cInt) {
        this.cInt = cInt;
    }

    /**
     * Sets the value of field 'capKG'. The field 'capKG' has the
     * following description: Capacidade em KG
     * 
     * @param capKG the value of field 'capKG'.
     */
    public void setCapKG(final java.lang.String capKG) {
        this.capKG = capKG;
    }

    /**
     * Sets the value of field 'capM3'. The field 'capM3' has the
     * following description: Capacidade em M3
     * 
     * @param capM3 the value of field 'capM3'.
     */
    public void setCapM3(final java.lang.String capM3) {
        this.capM3 = capM3;
    }

    /**
     * 
     * 
     * @param index
     * @param vCondutor
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setCondutor(final int index,final com.mercurio.lms.mdfe.model.v100a.Condutor vCondutor) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.condutorList.size()) {
            throw new IndexOutOfBoundsException("setCondutor: Index value '" + index + "' not in range [0.." + (this.condutorList.size() - 1) + "]");
        }

        this.condutorList.set(index, vCondutor);
    }

    /**
     * 
     * 
     * @param vCondutorArray
     */
    public void setCondutor(final com.mercurio.lms.mdfe.model.v100a.Condutor[] vCondutorArray) {
        //-- copy array
        condutorList.clear();

        for (int i = 0; i < vCondutorArray.length; i++) {
                this.condutorList.add(vCondutorArray[i]);
        }
    }

    /**
     * Sets the value of field 'placa'. The field 'placa' has the
     * following description: Placa do veículo 
     * 
     * @param placa the value of field 'placa'.
     */
    public void setPlaca(final java.lang.String placa) {
        this.placa = placa;
    }

    /**
     * Sets the value of field 'prop'. The field 'prop' has the
     * following description: Proprietários do Veículo.
     * Só preenchido quando o veículo não pertencer à empresa
     * emitente do MDF-e
     * 
     * @param prop the value of field 'prop'.
     */
    public void setProp(final com.mercurio.lms.mdfe.model.v100a.Prop prop) {
        this.prop = prop;
    }

    /**
     * Sets the value of field 'RENAVAM'. The field 'RENAVAM' has
     * the following description: RENAVAM do veículo 
     * 
     * @param RENAVAM the value of field 'RENAVAM'.
     */
    public void setRENAVAM(final java.lang.String RENAVAM) {
        this.RENAVAM = RENAVAM;
    }

    /**
     * Sets the value of field 'tara'. The field 'tara' has the
     * following description: Tara em KG
     * 
     * @param tara the value of field 'tara'.
     */
    public void setTara(final java.lang.String tara) {
        this.tara = tara;
    }

    /**
     * Sets the value of field 'tpCar'. The field 'tpCar' has the
     * following description: Tipo de Carroceria
     * 
     * @param tpCar the value of field 'tpCar'.
     */
    public void setTpCar(final com.mercurio.lms.mdfe.model.v100a.types.TpCarType tpCar) {
        this.tpCar = tpCar;
    }

    /**
     * Sets the value of field 'tpRod'. The field 'tpRod' has the
     * following description: Tipo de Rodado
     * 
     * @param tpRod the value of field 'tpRod'.
     */
    public void setTpRod(final com.mercurio.lms.mdfe.model.v100a.types.TpRodType tpRod) {
        this.tpRod = tpRod;
    }

    /**
     * Sets the value of field 'UF'. The field 'UF' has the
     * following description: UF em que veículo está licenciado
     * 
     * @param UF the value of field 'UF'.
     */
    public void setUF(final com.mercurio.lms.mdfe.model.v100a.types.TUf UF) {
        this.UF = UF;
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
     * com.mercurio.lms.mdfe.model.v100a.VeicTracao
     */
    public static com.mercurio.lms.mdfe.model.v100a.VeicTracao unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100a.VeicTracao) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100a.VeicTracao.class, reader);
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
