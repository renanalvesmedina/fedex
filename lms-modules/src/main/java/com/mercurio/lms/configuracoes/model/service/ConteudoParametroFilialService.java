package com.mercurio.lms.configuracoes.model.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.dao.ConteudoParametroFilialDAO;
import com.mercurio.lms.municipios.model.Filial;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.conteudoParametroFilialService"
 */
public class ConteudoParametroFilialService extends CrudService<ConteudoParametroFilial, Long> {

	/**
	 * 
	 * @param idParametroFilial
	 * @param idFilial
	 * @return
	 * 
	 */
	//Removido o deprecated pois este metodo é usado para atualizar o parametro da ultima padrem gerada automaticamente.
	public ConteudoParametroFilial findByIdParametroFilialIdFilial(Long idParametroFilial, Long idFilial)
	{
		Map map = new HashMap(2);
		map.put("parametroFilial.idParametroFilial",idParametroFilial);
		map.put("filial.idFilial",idFilial);
		List l = find(map);
		if(!l.isEmpty())
		{
			return (ConteudoParametroFilial)l.get(0);
		}
        return null;
	}

	/**
	 * Recupera uma instância de <code>ConteudoParametroFilial</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public ConteudoParametroFilial findById(java.lang.Long id) {
        return (ConteudoParametroFilial)super.findById(id);
    }
    
    /**
     * Retorna o <code>ConteudoParametroFilial</code> a partir do id da filial e da chave do parametro.
     * @deprecated 
     * @see ConteudoParametroFilialService#findByNomeParametro(Long, String)
     * @see ConteudoParametroFilialService#findConteudoByNomeParametro(Long, String, boolean)
     * */
    public ConteudoParametroFilial findConteudoParametroFilialByNmParametro(Long idFilial, String nmParametro) {  
    	return findByNomeParametro(idFilial, nmParametro, false);
    } 

    
    /**
     * Obtem ConteudoParametroFilial sem tratamento de exceções
     *  
     * @param idFilial
     * @param nmParametro
     * @return ConteudoParametroFilial
     */
    public ConteudoParametroFilial findConteudoParametroFilial(Long idFilial, String nmParametro, String vlConteudo) {    	
    	return this.getConteudoParametroFilialDAO().findConteudoParametroFilial(idFilial, nmParametro, vlConteudo);
    } 
    
    /**
     * Retorna o <code>ConteudoParametroFilial</code> a partir do id da filial e da chave do parametro.
     * 
     * @param idFilial identificador da filial
     * @param nomeParametro nome do parametro
     * @param realizaLock realiza lock pessimista no registro
     * @throws BusinessException Com a chave LMS-27050 caso não seja localizado um ConteudoParametroFilial com nome informado.
     * @return o ConteudoParametroFilial com o nome informado
     */
    public ConteudoParametroFilial findByNomeParametro(Long idFilial, String nomeParametro, boolean realizaLock) {
    	return findByNomeParametro(idFilial, nomeParametro, realizaLock, false);
    } 

    
    /**
     * Retorna o <code>ConteudoParametroFilial</code> a partir do id da filial e da chave do parametro.
     * 
     * @param idFilial identificador da filial
     * @param nomeParametro nome do parametro
     * @param realizaLock realiza lock pessimista no registro
     * @param bypassException não exibe a exception caso não encontre o parametro para a filial
     * @throws BusinessException Com a chave LMS-27050 caso não seja localizado um ConteudoParametroFilial com nome informado. Caso o parametro 
     * @return o ConteudoParametroFilial com o nome informado
     */
    public ConteudoParametroFilial findByNomeParametro(Long idFilial, String nomeParametro, boolean realizaLock, boolean bypassException) {

    	ConteudoParametroFilial conteudoParametroFilial = this.getConteudoParametroFilialDAO().findByNomeParametro(idFilial, nomeParametro, realizaLock);
    	
    	if (conteudoParametroFilial==null && !bypassException) {
    		throw new BusinessException("LMS-27050", new Object[] {nomeParametro});
    	}
    	return conteudoParametroFilial;
    } 
    
    
    
    /**
     * Retorna o conteúdo de um parâmetro geral do sistema de acordo com o tipo do formato.  
     * 
     * @param nomeParametro nome do parametro
     * @param realizaLock informa se deve ser feito lock pessimista no registro
     * @return retorna o valor tipado no formato do parametro. retorna <code>null</code> caso o valor seja <code>null</code>.
     * @throws java.lang.IllegalStateException caso o valor do parametro seja diferente de <code>null</code> e a conversão para o formato do parâmetro não seja realizada com sucesso.
     */
    public Object findConteudoByNomeParametroWithException(Long idFilial, String nomeParametro, boolean realizaLock) {
		Object retorno = findConteudoByNomeParametro(idFilial, nomeParametro, false);
		
		if (retorno == null) {
			throw new BusinessException("LMS-00067", new Object[]{idFilial, nomeParametro});
		} else {
			return retorno;
		}    	
    }    
    
    /**
     * Retorna o conteúdo de um parâmetro geral do sistema de acordo com o tipo do formato.  
     * 
     * @param nomeParametro nome do parametro
     * @param realizaLock informa se deve ser feito lock pessimista no registro
     * @return retorna o valor tipado no formato do parametro. retorna <code>null</code> caso o valor seja <code>null</code>.
     * @throws java.lang.IllegalStateException caso o valor do parametro seja diferente de <code>null</code> e a conversão para o formato do parâmetro não seja realizada com sucesso.
     */
    public Object findConteudoByNomeParametro(Long idFilial, String nomeParametro, boolean realizaLock) {

    	ConteudoParametroFilial conteudoParametroFilial = getConteudoParametroFilialDAO().findByNomeParametro(idFilial, nomeParametro, realizaLock);
    	
    	if (conteudoParametroFilial == null) {
    		if (log.isDebugEnabled()) {
    			log.debug("Parâmetro de filial não encontrado. idFilial="+idFilial+" - nomeParametro='"+nomeParametro+"' - retornando null para método chamador.");
    		}
    		return null;
    	}
    	
    	Class paramClass = getParametroFilialFormatoClazz(conteudoParametroFilial);
    	
		String valueStr = conteudoParametroFilial.getVlConteudoParametroFilial();
		
		Object value = null;
		if (valueStr != null) {
			value = ReflectionUtils.getConverterInstance().convert(valueStr, paramClass);
			if (value == null) {
				throw new IllegalStateException("Não foi possivel converter o valor do parametro filial '"+nomeParametro+"' para o tipo de classe informado: "+valueStr+" classe: "+paramClass.getName());
			}
		}
		
    	return value;
    }

	private Class getParametroFilialFormatoClazz(ConteudoParametroFilial conteudoParametroFilial) {
		Class paramClass = null;
    	char tpFormato = conteudoParametroFilial.getParametroFilial().getTpFormato().getValue().charAt(0);
    	
    	switch (tpFormato) {
		case 'A':
			paramClass = String.class;
			break;
		case 'N':
			paramClass = BigDecimal.class;
			break;
		case 'D':
			paramClass = YearMonthDay.class;
			break;

		default:
			throw new IllegalArgumentException("tpFormato '"+tpFormato+"' não reconhecido para o parâmetro filial: "+conteudoParametroFilial.getParametroFilial().getNmParametroFilial());
		}
    	
		return paramClass;
	}
    
    /**
     * Retorna o valor do conteudo a partir do id da filial e da chave do parametro.
     * @deprecated @see ConteudoParametroFilialService#findConteudoByNomeParametro(Long, String, boolean)
     * */
    public String findValorConteudo(Long idFilial, String nmParametro) {  
    	Object value = findConteudoByNomeParametro(idFilial, nmParametro, false);
    	String strValue = null;
    	if (value != null) {
    		strValue = value.toString();
    	}
    	return strValue;
    }    

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ConteudoParametroFilial bean) {
        return super.store(bean);
    }
    
    public ResultSetPage findPaginated(Map criteria) {
        
        return super.findPaginated(criteria);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setConteudoParametroFilialDAO(ConteudoParametroFilialDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ConteudoParametroFilialDAO getConteudoParametroFilialDAO() {
        return (ConteudoParametroFilialDAO) getDao();
    }

	/**
	 * Incrementa o valor de um parametro geral podendo ser especificado se deve ser realizado
	 * lock pessimista no registro. Caso o parametro possua um valor <code>null</code> ou vazio
	 * inicia em um (1) a sequencia.
	 *
	 * @param idFilial identificador da filial
	 * @param nomeParametro nome do parâmetro a ser incrementado.
	 * @param lockParametro informa se deve ser feito lock pessimista.
	 * @return numero do parâmetro incrementado de mais um (1). Valor inicial é igual um (1).
	 */
	public Long generateProximoValorParametroSequencial(Long idFilial, String nomeParametro, boolean lockParametro) {
		return generateValorParametroSequencial(idFilial, nomeParametro, lockParametro, +1);
	}

	/**
	 * Decrementa o valor de um parametro geral podendo ser especificado se deve ser realizado
	 * lock pessimista no registro. Caso o parametro possua um valor <code>null</code> ou vazio
	 * inicia em um (-1) a sequencia.
	 *
	 * @param idFilial identificador da filial
	 * @param nomeParametro nome do parâmetro a ser incrementado.
	 * @param lockParametro informa se deve ser feito lock pessimista.
	 * @return numero do parâmetro decrementado de menos um (1). Valor inicial é igual um (-1).
	 */
	public Long generateValorParametroSequencialAnterior(Long idFilial, String nomeParametro, boolean lockParametro) {
		return generateValorParametroSequencial(idFilial, nomeParametro, lockParametro, -1);
	}

	/**
	 * Gera o parametro sequencial negativo para números de conhecimentos temporários.
	 * Correção de performance para a issue LMS-968.
	 * @return proximo numero da sequencia.
	 */
	public Long generateNrConhecimentoTmp() {
		return getConteudoParametroFilialDAO().generateNrConhecimentoTmp();
	}

	private Long generateValorParametroSequencial(Long idFilial, String nomeParametro, boolean lockParametro, long incremento) {
		return getConteudoParametroFilialDAO().generateValorParametroSequencial(idFilial, nomeParametro, incremento);
			}

	/**
	 * Armazena o valor de um parametro de filial. Este método não efetua lock pessimista
	 * no registro.
	 * 
	 * @param idFilial identificar do filial
	 * @param nomeParametro nome do parametro
	 * @param valorParametro valor do parametro
	 * @throws ClassCastException caso o tipo de dados do valor informado para o parametro seja incompativel com o formato do parametro. 
	 */
	public void storeValorParametro(Long idFilial, String nomeParametro, Object valorParametro) {
		
		ConteudoParametroFilial conteudoParamFilial = this.findByNomeParametro(idFilial, nomeParametro, false);
		
		if (valorParametro != null) {
			Class paramClass = getParametroFilialFormatoClazz(conteudoParamFilial);
	    	if (!paramClass.isAssignableFrom(valorParametro.getClass())) {
	    		throw new ClassCastException("Não é possivel salvar o parametro com a classe informada, esperava: "+paramClass.getName()+" recebeu: "+valorParametro.getClass());
	    	}
		}
		
		// FIX: ver se formato de armazenamento do parametro será sempre o 
		// retorno do .toString() para cada tipo de parametro
		String valorStr = "";
		if (valorParametro != null) {
			valorStr = valorParametro.toString();
		}
		conteudoParamFilial.setVlConteudoParametroFilial(valorStr);
		
		store(conteudoParamFilial);
	}
	
	public Filial findFilialByConteudoParametro(String nomeParametro, String valorConteudo){
	    return this.getConteudoParametroFilialDAO().findFilialByConteudoParametro(nomeParametro, valorConteudo);
	}
	
	public List<Filial> findFilialByNmParametro(String nomeParametro){
		return this.getConteudoParametroFilialDAO().findFilialByNmParametro(nomeParametro);
	}
	
}