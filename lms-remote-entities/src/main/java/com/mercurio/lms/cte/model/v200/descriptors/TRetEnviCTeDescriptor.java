/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v200.descriptors;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import com.mercurio.lms.cte.model.v200.TRetEnviCTe;

/**
 * Class TRetEnviCTeDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class TRetEnviCTeDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _elementDefinition.
     */
    private boolean _elementDefinition;

    /**
     * Field _nsPrefix.
     */
    private java.lang.String _nsPrefix;

    /**
     * Field _nsURI.
     */
    private java.lang.String _nsURI;

    /**
     * Field _xmlName.
     */
    private java.lang.String _xmlName;

    /**
     * Field _identity.
     */
    private org.exolab.castor.xml.XMLFieldDescriptor _identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public TRetEnviCTeDescriptor() {
        super();
        _nsURI = "http://www.portalfiscal.inf.br/cte";
        _xmlName = "TRetEnviCTe";
        _elementDefinition = false;

        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors

        //-- _versao
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_versao", "versao", org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                TRetEnviCTe target = (TRetEnviCTe) object;
                return target.getVersao();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TRetEnviCTe target = (TRetEnviCTe) object;
                    target.setVersao( (java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setSchemaType("string");
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        //-- validation code for: _versao
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.addPattern("2\\.00");
            typeValidator.setWhiteSpace("preserve");
        }
        desc.setValidator(fieldValidator);
        //-- initialize element descriptors

        //-- _tpAmb
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.mercurio.lms.cte.model.v200.types.TAmb.class, "_tpAmb", "tpAmb", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                TRetEnviCTe target = (TRetEnviCTe) object;
                return target.getTpAmb();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TRetEnviCTe target = (TRetEnviCTe) object;
                    target.setTpAmb( (com.mercurio.lms.cte.model.v200.types.TAmb) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        handler = new org.exolab.castor.xml.handlers.EnumFieldHandler(com.mercurio.lms.cte.model.v200.types.TAmb.class, handler);
        desc.setImmutable(true);
        desc.setSchemaType("TAmb");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.portalfiscal.inf.br/cte");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: _tpAmb
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _cUF
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.mercurio.lms.cte.model.v200.types.TCodUfIBGE.class, "_cUF", "cUF", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                TRetEnviCTe target = (TRetEnviCTe) object;
                return target.getCUF();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TRetEnviCTe target = (TRetEnviCTe) object;
                    target.setCUF( (com.mercurio.lms.cte.model.v200.types.TCodUfIBGE) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        handler = new org.exolab.castor.xml.handlers.EnumFieldHandler(com.mercurio.lms.cte.model.v200.types.TCodUfIBGE.class, handler);
        desc.setImmutable(true);
        desc.setSchemaType("TCodUfIBGE");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.portalfiscal.inf.br/cte");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: _cUF
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _verAplic
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_verAplic", "verAplic", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                TRetEnviCTe target = (TRetEnviCTe) object;
                return target.getVerAplic();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TRetEnviCTe target = (TRetEnviCTe) object;
                    target.setVerAplic( (java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setSchemaType("string");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.portalfiscal.inf.br/cte");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: _verAplic
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.addPattern("[!-�]{1}[ -�]{0,}[!-�]{1}|[!-�]{1}");
            typeValidator.setWhiteSpace("preserve");
            typeValidator.setMaxLength(20);
            typeValidator.setMinLength(1);
        }
        desc.setValidator(fieldValidator);
        //-- _cStat
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_cStat", "cStat", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                TRetEnviCTe target = (TRetEnviCTe) object;
                return target.getCStat();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TRetEnviCTe target = (TRetEnviCTe) object;
                    target.setCStat( (java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setSchemaType("string");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.portalfiscal.inf.br/cte");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: _cStat
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.addPattern("[0-9]{3}");
            typeValidator.setWhiteSpace("preserve");
        }
        desc.setValidator(fieldValidator);
        //-- _xMotivo
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_xMotivo", "xMotivo", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                TRetEnviCTe target = (TRetEnviCTe) object;
                return target.getXMotivo();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TRetEnviCTe target = (TRetEnviCTe) object;
                    target.setXMotivo( (java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setSchemaType("string");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.portalfiscal.inf.br/cte");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: _xMotivo
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.addPattern("[!-�]{1}[ -�]{0,}[!-�]{1}|[!-�]{1}");
            typeValidator.setWhiteSpace("preserve");
            typeValidator.setMaxLength(255);
            typeValidator.setMinLength(1);
        }
        desc.setValidator(fieldValidator);
        //-- _infRec
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.mercurio.lms.cte.model.v200.InfRec.class, "_infRec", "infRec", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                TRetEnviCTe target = (TRetEnviCTe) object;
                return target.getInfRec();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TRetEnviCTe target = (TRetEnviCTe) object;
                    target.setInfRec( (com.mercurio.lms.cte.model.v200.InfRec) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new com.mercurio.lms.cte.model.v200.InfRec();
            }
        };
        desc.setSchemaType("com.mercurio.lms.cte.model.v200.InfRec");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.portalfiscal.inf.br/cte");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: _infRec
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getAccessMode.
     * 
     * @return the access mode specified for this class.
     */
    @Override()
    public org.exolab.castor.mapping.AccessMode getAccessMode(
    ) {
        return null;
    }

    /**
     * Method getIdentity.
     * 
     * @return the identity field, null if this class has no
     * identity.
     */
    @Override()
    public org.exolab.castor.mapping.FieldDescriptor getIdentity(
    ) {
        return _identity;
    }

    /**
     * Method getJavaClass.
     * 
     * @return the Java class represented by this descriptor.
     */
    @Override()
    public java.lang.Class getJavaClass(
    ) {
        return com.mercurio.lms.cte.model.v200.TRetEnviCTe.class;
    }

    /**
     * Method getNameSpacePrefix.
     * 
     * @return the namespace prefix to use when marshaling as XML.
     */
    @Override()
    public java.lang.String getNameSpacePrefix(
    ) {
        return _nsPrefix;
    }

    /**
     * Method getNameSpaceURI.
     * 
     * @return the namespace URI used when marshaling and
     * unmarshaling as XML.
     */
    @Override()
    public java.lang.String getNameSpaceURI(
    ) {
        return _nsURI;
    }

    /**
     * Method getValidator.
     * 
     * @return a specific validator for the class described by this
     * ClassDescriptor.
     */
    @Override()
    public org.exolab.castor.xml.TypeValidator getValidator(
    ) {
        return this;
    }

    /**
     * Method getXMLName.
     * 
     * @return the XML Name for the Class being described.
     */
    @Override()
    public java.lang.String getXMLName(
    ) {
        return _xmlName;
    }

    /**
     * Method isElementDefinition.
     * 
     * @return true if XML schema definition of this Class is that
     * of a global
     * element or element with anonymous type definition.
     */
    public boolean isElementDefinition(
    ) {
        return _elementDefinition;
    }

}
