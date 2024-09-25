package com.mercurio.lms.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.WeakHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.core.util.Property;
import com.mercurio.adsm.framework.model.Domain;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.StringI18n;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
*
* Classe implementado para corrigir problema especifico de manter filiais.
* 
**/

public class LMSReflectionUtils {
	
	public static final Logger log = LogManager.getLogger(com.mercurio.adsm.core.util.ReflectionUtils.class);
	
	//Cache de BeanInfos utilizado para minimizar perdas de performance com reflection
	//WeakHashMap usado por questaoes de GC em ambiente Multi-ClassLoader
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map cachedBeanInfos = Collections.synchronizedMap(new WeakHashMap());
	 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Object operateNestedBeanProperty(byte operation, Object o, String fullQualifierName, Object value, Class rootClass) {
	
	    if (!((operation == com.mercurio.adsm.core.util.ReflectionUtils.GET_NESTED_BEAN_PROPERTY) || (operation == com.mercurio.adsm.core.util.ReflectionUtils.SET_NESTED_BEAN_PROPERTY_VALUE) ||
	            (operation == com.mercurio.adsm.core.util.ReflectionUtils.GET_NESTED_BEAN_PROPERTY_VALUE))) {	        
	        throw new IllegalArgumentException("Argumento operation inválido. "+operation);
	    }
	    
	    if (!(o instanceof TypedFlatMap)) {
			StringTokenizer st = new StringTokenizer(fullQualifierName, ":.");
			String qualifierName = null;
			int qualifierIndex = -1;
			Object parentO = null;
			
			Class clazz = (!(o instanceof List) ? o.getClass() : (null != rootClass ? rootClass : HashMap.class));
			while (st.hasMoreElements()) {
				String lastQualifierName = qualifierName;
				qualifierName = (String) st.nextElement();
				
				if ("".equals(qualifierName))
					continue;
				
				int lastQualifierIndex = qualifierIndex;
				qualifierIndex = -1;
				try {
					qualifierIndex = Integer.parseInt(qualifierName);
				}
				catch (NumberFormatException e) { /* properly swallowed */ }
				
				if (o == null) {
					if (qualifierIndex == -1) {
						try {
							o = clazz.newInstance();
						} catch (Throwable t) {
							throw new InfrastructureException(t);
						}
					} else {
						o = new ArrayList();
					}
				
					if (!(parentO instanceof List)) {
						com.mercurio.adsm.core.util.ReflectionUtils.setBeanPropertyValue(parentO, lastQualifierName, o);
					} else {
						((List) parentO).add(lastQualifierIndex, o);
					}
				}
				
				parentO = o;
				if (!(parentO instanceof List)) {
					o = com.mercurio.adsm.core.util.ReflectionUtils.getBeanPropertyValue(parentO, qualifierName);
	
					if (!(parentO instanceof Map)) {
						// FIX: why the ", new Class[0]" argument was added???
					    // modificado para utilizar java.beans.Introspector
						PropertyDescriptor pd = com.mercurio.adsm.core.util.ReflectionUtils.getPropertyDescriptor(parentO, qualifierName);
						if (pd == null) {
							o = null;
							break;
						}
						// se não achar um método de leitura quebra o while
						Method getterAccessor = pd.getReadMethod();
						if (getterAccessor == null) {
							o = null;
							break;
						}
	
						clazz = getterAccessor.getReturnType();
						if (List.class.isAssignableFrom(clazz)) {
							clazz = com.mercurio.adsm.core.util.ReflectionUtils.getParametrizedClass(getterAccessor);
						}
					}
					else {
						clazz = HashMap.class;
					}
				}
				else {
					try {
						o = ((List) parentO).get(qualifierIndex);
					}
					catch (IndexOutOfBoundsException e) {
						o = null;
					}
				}
			}
			
			if (com.mercurio.adsm.core.util.ReflectionUtils.SET_NESTED_BEAN_PROPERTY_VALUE == operation) {
				o = value;
				if (!(parentO instanceof List)) {
					com.mercurio.adsm.core.util.ReflectionUtils.setBeanPropertyValue(parentO, qualifierName, value);
				} else {
					((List) parentO).add(qualifierIndex, value);
				}
				
				return null;
			}
			else if (com.mercurio.adsm.core.util.ReflectionUtils.GET_NESTED_BEAN_PROPERTY == operation) {
				Property p = new Property();
				p.setClazz(clazz);
				p.setName(qualifierName);
				p.setParametrizedClass(rootClass);
				p.setValue(o);
				
				return p;
			}
			else
				return o;
	    }
	    else {
	    	if (com.mercurio.adsm.core.util.ReflectionUtils.SET_NESTED_BEAN_PROPERTY_VALUE == operation) {
	    		com.mercurio.adsm.core.util.ReflectionUtils.setBeanPropertyValue(o, fullQualifierName, value);
	    		
	    		return null;
			}
			else if (com.mercurio.adsm.core.util.ReflectionUtils.GET_NESTED_BEAN_PROPERTY == operation) {
				value = ((TypedFlatMap) o).get(fullQualifierName);
				Property p = new Property();
				p.setClazz(value.getClass());
				p.setName(fullQualifierName);
				p.setParametrizedClass(rootClass);
				p.setValue(value);
				
				return p;
			}
			else
				return ((TypedFlatMap) o).get(fullQualifierName);
	    }
	}
	
	

	/**
	 * Recupera BeanInfo de uma determinada classe armazenado em cachedBeanInfos
	 * caso nao exista, adiciona. Este metodo trabalha com WeakReferences para maximizar a 
	 * eficiencia do GarbageCollector em ambiente multi-classloader
	 * @param bean
	 * @return
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private static BeanInfo retrieveBeanInfo(Object bean) {
		BeanInfo beanInfo;
		try {
			Class clazz = bean instanceof Class? (Class)bean : bean.getClass();
			Object value = cachedBeanInfos.get(clazz);
			if (value instanceof Reference) {
				Reference ref = (Reference) value;
				beanInfo = (BeanInfo) ref.get();
			}
			else {
				beanInfo = (BeanInfo) value;
			}
			
			if (beanInfo == null) {
				beanInfo = Introspector.getBeanInfo(clazz, Object.class);
				boolean cacheSafe = isCacheSafe(clazz);
				if (log.isDebugEnabled()) {
					log.debug("Classe '" + clazz.getName() + (!cacheSafe ? "nao " : "") + " e cache-safe");
				}
				if (cacheSafe) {
					cachedBeanInfos.put(clazz, beanInfo);
				}else {
					cachedBeanInfos.put(clazz, new WeakReference(beanInfo));
				}
			}else {
				if (log.isDebugEnabled()) {
					//log.debug("Usando BeanInfo em cache para [" + clazz.getName() + "]");
				}
			}
			
		} catch (IntrospectionException e) {
			throw new InfrastructureException(e);
		}
		return beanInfo;
	}

	/**
	 * Determina se a classe pertence ao mesmo classloader de ReflectionUtils ou nao
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static boolean isCacheSafe(Class clazz) {
		ClassLoader cur = LMSReflectionUtils.class.getClassLoader();
		ClassLoader target = clazz.getClassLoader();
		if (target == null || cur == target) {
			return true;
		}
		while (cur != null) {
			cur = cur.getParent();
			if (cur == target) {
				return true;
			}
		}
		return false;
	}
	
	
	
	/**
	 * Este método verifica se o valor possui a mesma classe da classe de argumento passada, caso
	 * contrário, tenta criar a partir da representação String do value uma instancia da classe de argumento.
	 * 
     * @param value
     * @param argumentClass
     * @return
     * @throws IOException
     */
    @SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	private static Object createBeanPropertyValue(Object value, Class argumentClass) throws IOException {
        // se for uma instancia de DomainValue que o método espera converte o String nessa classe.
        if ((value != null) && (!argumentClass.isAssignableFrom(value.getClass()))) {
            if (DomainValue.class.isAssignableFrom(argumentClass)) {
                String s = String.valueOf(value);
                value = new DomainValue(s, null, Boolean.TRUE);
            } else if (StringI18n.class.isAssignableFrom(argumentClass)) {
                String s = String.valueOf(value);
                if (s != null && !"".equals(s)) {
                    value = new StringI18n(s);
                } else {
                    value = null;
                }
            
            } else if (VarcharI18n.class.isAssignableFrom(argumentClass)) {    
            	String s = String.valueOf(value);
                if (s != null && !"".equals(s)) {
                    value = new VarcharI18n(s);
            } else {
                    value = null;
                }
                
            } else {
                String str = String.valueOf(value);
                if (byte[].class.isAssignableFrom(argumentClass)) {
                    value = Base64Util.decode(str);
                } else {
                	if (Boolean.class.isAssignableFrom(argumentClass)) {
                        if (value != null && "S".equals(value)) {
                            value = "true";
                        }
                    }
                	// tratamento normal para classe de tipos primitivos do java
        	        value = com.mercurio.adsm.core.util.ReflectionUtils.toObject(str, argumentClass);
                }
            }
        }
        return value;
    }


    /**
     * Método implementado para corrigir problema especifico de manter filiais
     * copiando também os valores vazios do objeto source
     * 
     * Copia os valores de um objeto para outro objeto, se o destino ou a origem
     * for uma instancia de <code>java.util.Map</code> e o outro parâmetro for um objeto
     * difernte de <code>java.util.Map</code> a copia é feita pelos nomes das propriedades da
     * classe.
     * A copia é recursiva, isto é, todos os atributos são copiados inclusive classes aninhadas.
     * 
     * @param dest Objeto de destino.
     * @param src Objeto de origem.
     * @deprecated
     */
	@SuppressWarnings("rawtypes")
	public static void copyNestedBean(Object dest, Object src) {
		
		long ini = System.currentTimeMillis();
		copyNestedBean(dest, null, src, new ArrayList(), null, "");
		dumpStack(ini);
	}

	private static void dumpStack(long ini) {
		long time = System.currentTimeMillis()-ini;
		if (time >= 100) {
			log.warn("ReflectionUtils time: " + (time) +" ms !!!");
			if(time >= 5000) {
				final String stackTrace = redirectStack();
				log.warn("\tVerifique a rotina apresentada no Stacktrace apresentado abaixo ele esta consumindo mais de 5(cinco) segundos apenas na RefletionUtils: '"+ stackTrace + "'");
			}
		}
	}

	@SuppressWarnings("static-access")
	private static String redirectStack() {
		PrintStream origOut = System.out;
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		String stackTrace = "";
		try{
			PrintStream ps = new PrintStream(bOut, true);
			System.setOut(ps);
			ps.println("Logging");
			Thread.currentThread().dumpStack();
			ps.flush();
			stackTrace = new String(bOut.toByteArray());
		} catch (Throwable e) {
		}finally{
			try{
				bOut.close();
			}catch(Throwable ignored){}
			System.setOut(origOut);
		}
		return stackTrace;
	}
    
	
	/**
	 * 
	 * Este método verifica se o valor possui a mesma classe da classe de argumento passada, caso
	 * contrário, tenta criar a partir da representação String do value uma instancia da classe de argumento.
	 * 
	 * @param dest
	 * @param innerDestClass
	 * @param src
	 * @param alreadyCopied
	 * @param includedProperties
	 * @param fullQualifierName
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	private static void copyNestedBean(Object dest, Class innerDestClass, Object src, List alreadyCopied, List includedProperties, String fullQualifierName) {
		if (!(src instanceof List)) {
			List properties = Collections.EMPTY_LIST;

			if (dest instanceof Map )
			    properties = com.mercurio.adsm.core.util.ReflectionUtils.getProperties(src, true);
			else if ((src != null) && (!com.mercurio.adsm.core.util.ReflectionUtils.isBasicType(src.getClass())) && (src instanceof Map)) {
			    properties = com.mercurio.adsm.core.util.ReflectionUtils.getProperties(dest, false);
			    try {
			    	getPropertiesValues(properties, (Map)src);
                } catch (Throwable e) {
			        throw new InfrastructureException(e);
                }
			} else {
			    properties = com.mercurio.adsm.core.util.ReflectionUtils.getProperties(dest, false);
			}
			
			for (Iterator it = properties.iterator(); it.hasNext();) {
				Property p = (Property) it.next();
				Class propertyClazz = p.getClazz();
				if (propertyClazz == null) continue; // se a property não possui um tipo então desconsidera ela na copia.
                if (org.hibernate.proxy.LazyInitializer.class.isAssignableFrom(propertyClazz) ||
				        net.sf.cglib.proxy.Callback[].class.isAssignableFrom(propertyClazz)   ) {
				    // ignora as propriedades do Callback gerado pelo CGLib no Hibernate
				    continue;
				}
				String qualifierName = p.getName();
				
				if (includedProperties != null && !com.mercurio.adsm.core.util.ReflectionUtils.isNestedContained(includedProperties, fullQualifierName + (!"".equals(fullQualifierName) ? "." : "") + qualifierName))
				    continue;

				Object innerSrcValue = com.mercurio.adsm.core.util.ReflectionUtils.getNestedBeanPropertyValue(src, qualifierName, null);
				if (!alreadyCopied.contains(innerSrcValue)) {
					alreadyCopied.add(innerSrcValue);
				} else {
					continue;
				}
				
				Object innerDestValue = null;
				// se for um tipo basico já retorna o valor direto
				if (com.mercurio.adsm.core.util.ReflectionUtils.isBasicType(propertyClazz)) {
				    // trata uma situacao de erro do desenvolvedor aonde ele informou uma propriedade do tipo Domain ao inves de DomainValue na
				    // classe para representar o valor do dominio.
				    if ((innerSrcValue == null) && (Domain.class.isAssignableFrom(propertyClazz))) {
				        throw new IllegalArgumentException("Tipo de dado inválido para a propriedade '"+p.getName()+"' deveria ser 'com.mercurio.adsm.framework.model.DomainValue'.");
				    }
					if ((innerSrcValue != null) && 
					        (StringI18n.class.isAssignableFrom(innerSrcValue.getClass()))) {
					    innerDestValue = ((StringI18n)innerSrcValue).getValue();
					
					} else if ( ( innerSrcValue != null ) && 
							(VarcharI18n.class.isAssignableFrom(innerSrcValue.getClass()))) {					    
						innerDestValue = ((VarcharI18n)innerSrcValue).getValue();
					    
					} else if ( ( innerSrcValue != null ) && 
						        (Enum.class.isAssignableFrom(innerSrcValue.getClass()))) {
					    innerDestValue = ((Enum)innerSrcValue).name();
					} else {
					    innerDestValue = innerSrcValue;
					}
				} else if (byte[].class.isAssignableFrom(propertyClazz)) { 
				    try {
 				        if (byte[].class.isAssignableFrom(innerSrcValue.getClass())) {
				            innerDestValue = Base64Util.encode((byte[])innerSrcValue); 
				        } else {
				            innerDestValue = innerSrcValue;
				        }
				    } catch (Throwable t) {
				        throw new InfrastructureException(t);
				    }
				}
				else {
				    // caso contrário navega pela classe para copiar as propriedades
				    // internas da classe.
					//Class destInnerClassAnt = destInnerClass;
					innerDestClass = null;
					if (List.class.isAssignableFrom(propertyClazz)) {
						if (innerSrcValue != null) {
							innerDestValue = new ArrayList(((Collection)innerSrcValue).size());
								
							if (!(dest instanceof Map)) {
								Method m = null;
								PropertyDescriptor pd = com.mercurio.adsm.core.util.ReflectionUtils.getPropertyDescriptor(dest.getClass(), qualifierName);
								m = pd.getReadMethod();
								innerDestClass = com.mercurio.adsm.core.util.ReflectionUtils.getParametrizedClass(m);
								
							} else {
								if (!(dest instanceof TypedFlatMap))
									innerDestClass = HashMap.class;
								else
									innerDestClass = TypedFlatMap.class;
							}
						}
					} else {
						try {
							if (dest instanceof Map) {
								if (!(dest instanceof TypedFlatMap))
									innerDestValue = new HashMap();
								else
									innerDestValue = new TypedFlatMap();
							} else {
								Property pDest = null;
								if (Map.class.isAssignableFrom(propertyClazz)) {
									pDest = (Property) operateNestedBeanProperty(com.mercurio.adsm.core.util.ReflectionUtils.GET_NESTED_BEAN_PROPERTY, dest, qualifierName, null, null);
								} else {
									pDest = p;
								}
								innerDestValue = pDest.getClazz().newInstance();
							}
						} catch (Throwable t) {
							throw new InfrastructureException(t);
						}
					}
					
					if (innerSrcValue != null) {
						copyNestedBean(innerDestValue, innerDestClass, innerSrcValue, alreadyCopied, includedProperties, fullQualifierName + (!"".equals(fullQualifierName) ? "." : "") + qualifierName);
						
						if (!com.mercurio.adsm.core.util.ReflectionUtils.isNotNullBean(innerDestValue)) {
						    innerDestValue = null;
						}
					}
				} 
				alreadyCopied.remove(innerSrcValue);
				
				// se for uma lista verifica se para as classes que estão adicionadas nela se não existe
				// um atributo que referencia a classe atual da recursão para criar o vinculo entre elas
				if ((!Map.class.isAssignableFrom(dest.getClass())) && (innerDestValue != null) && 
				    (java.util.List.class.isAssignableFrom(innerDestValue.getClass()))) {
				    Class parametrizedClass = p.getParametrizedClass();
				    if (parametrizedClass == null) {
				        throw new IllegalStateException("Deve ser especificado para o método 'get<Propriedade>' da propriedade: "+p.getName()+" na classe "+propertyClazz+" o meta-atributo '@@ParametrizedAttribute' quando o retorno for 'java.util.List'.");
				    }
				    Class destClass = dest.getClass();
				    Method[] methods = parametrizedClass.getMethods();
				    Method setterMethod = null;
				    for (int i = 0; i < methods.length; i++) {
                        Method m = methods[i];
                        Class[] params = m.getParameterTypes();
                        if ((params != null) && (params.length==1)) {
                            if (destClass.isAssignableFrom(params[0])) {
                                // localiza o metodo que atribui o valor na classe
                                setterMethod = m;
                                break;
                            }
                        }
                    }
				    if (setterMethod != null) {
				        for (Iterator iter = ((List) innerDestValue).iterator(); iter.hasNext();) {
                            Object element = (Object) iter.next();
                            try {
                                setterMethod.invoke(element, new Object[] {dest});
                            } catch (Exception e) {
                                throw new InfrastructureException(e);
                            }
                        }
				    }
				}
				
				com.mercurio.adsm.core.util.ReflectionUtils.setNestedBeanPropertyValue(dest, qualifierName, innerDestValue, null);
			}
		}
		else {
		    if (!List.class.isAssignableFrom(dest.getClass())) {
		        throw new IllegalArgumentException("Para copiar instancias de list na origem o destino deve sempre ser outra instancia de java.util.List");
		    }
			List l = (List) src;
			for (Iterator it = l.iterator(); it.hasNext();) {
				Object innerSrc = it.next();
				Object innerDest = null;
				try {
					if (null == innerDestClass)
						innerDest = innerSrc.getClass().newInstance();
					else if (!com.mercurio.adsm.core.util.ReflectionUtils.isBasicType(innerDestClass))
						if (TypedFlatMap.class.equals(innerDestClass))
							innerDest = new TypedFlatMap();
						else if (Map.class.equals(innerDestClass))
							innerDest = new HashMap();
						else
							innerDest = innerDestClass.newInstance();
				} catch (Throwable t) {
					throw new InfrastructureException(t);
				}
				
				// é necessario este teste porque mesmo StringI18n e DomainValue sendo tipos basicos
				// eles devem retornar de forma aninhada como um Map
				if (StringI18n.class.isAssignableFrom(innerSrc.getClass()) ||
						VarcharI18n.class.isAssignableFrom(innerSrc.getClass()) ||
				    DomainValue.class.isAssignableFrom(innerSrc.getClass()) ||
				    !com.mercurio.adsm.core.util.ReflectionUtils.isBasicType(innerSrc.getClass())) {
				    
					copyNestedBean(innerDest, null, innerSrc, alreadyCopied, includedProperties, fullQualifierName);
				} else {
					innerDest = innerSrc;
				}
				alreadyCopied.remove(innerSrc);
				
				((List) dest).add(innerDest);
			}
		}
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private static List getFullQualifierNamesInner(Object o, String parentQualifierName, List qualifierNames) {
		
		if (o instanceof Map) {
			for (Iterator it = ((Map) o).keySet().iterator(); it.hasNext();) {
				String keyName = (String) it.next();
				getFullQualifierNamesInner(((Map) o).get(keyName), (parentQualifierName != null ? parentQualifierName + "." : "") + keyName, qualifierNames);
			}
		} else if (o instanceof List) {
			int i = 0;
			for (Iterator it = ((List) o).iterator(); it.hasNext(); i++) {
				Object value = it.next();
				getFullQualifierNamesInner(value, (parentQualifierName != null ? parentQualifierName : "") + ":" + i, qualifierNames);
			}
		} else if (null == o || com.mercurio.adsm.core.util.ReflectionUtils.isBasicType(o.getClass())) {
			qualifierNames.add(parentQualifierName);
		}
		
		return qualifierNames;
	}
	
	@SuppressWarnings({ "rawtypes", "unused", "unchecked"})
	private static void flatMap(Map flatMap, Object currentNode, String parentFullQualifier) {
		Iterator it = null;
		if (currentNode instanceof Map)
			it = ((Map) currentNode).entrySet().iterator();
		else if (currentNode instanceof List)
			it = ((List) currentNode).iterator();
		else
			return;
		
		while (it.hasNext()) {
			String key = null;
			Object value = null;
			char qualifierSeparator = 0;
			if (currentNode instanceof Map) {
				Map.Entry e = (Map.Entry) it.next();
				key = (String) e.getKey();
				value = e.getValue();
				qualifierSeparator = '.';
			}

			if (value instanceof Map)
				flatMap(flatMap, value, (parentFullQualifier != null ? parentFullQualifier + qualifierSeparator : "") + key);
			else if (value instanceof List) {
				List newValue = new ArrayList();
				for (Iterator it1 = ((List) value).iterator(); it1.hasNext();) {
					value = it1.next();
					if (value != null && !com.mercurio.adsm.core.util.ReflectionUtils.isBasicType(value.getClass())) {
						Map dest = new TypedFlatMap();
						flatMap(dest, value, null);
						value = dest;
					}
					newValue.add(value);
				}
				flatMap.put((parentFullQualifier != null ? parentFullQualifier + qualifierSeparator : "") + key, newValue);
			}
			else
				flatMap.put((parentFullQualifier != null ? parentFullQualifier + qualifierSeparator : "") + key, value);
		}
	}
	
	/**
	 * Popula os valores de um conjunto de propriedades a partir de uma Map.
	 * Remove da lista de propriedades valores que estejam nulos no <code>mapValues</code>
	 * @throws Throwable
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void getPropertiesValues(List properties, Map mapValues) throws Throwable {
	    if (mapValues != null) {
	        for (Iterator iterProps = properties.iterator(); iterProps.hasNext();) {
                Property property = (Property) iterProps.next();
                Object value = mapValues.get(property.getName());
                // se o tipo da property for diferente do valor que esta no map constroi o valor
                if (value != null && !property.getClazz().isAssignableFrom(value.getClass())) {
                    value = createBeanPropertyValue(value, property.getClazz());
                }
                property.setValue(value);
            }
	    }
	}
	

}
