package com.mercurio.lms.tributos.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.tributos.model.AliquotaIcms;
import com.mercurio.lms.tributos.model.dao.AliquotaIcmsDAO;

/**
 * Classe de serviï¿½o para CRUD:   
 *
 * 
 * Nï¿½o inserir documentaï¿½ï¿½o apï¿½s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviï¿½o.
 * @spring.bean id="lms.tributos.aliquotaIcmsService"
 */
public class AliquotaIcmsService extends CrudService<AliquotaIcms, Long> {
	
	private ParametroGeralService parametroGeralService;

    protected AliquotaIcms beforeStore(AliquotaIcms bean) {
		if ( this.findExisteVigenciaAliquotaIva((AliquotaIcms)bean)) {
			throw new BusinessException("LMS-00047");
		}
		
		validaTipoTributacaoIcms((AliquotaIcms)bean);

		return super.beforeStore(bean);
	}

    private void validaTipoTributacaoIcms(AliquotaIcms icms) {
    	
    	ParametroGeral parametroGeralNormal = parametroGeralService.findByNomeParametro("ID_TIPO_TRIBUTACAO_NORMAL",false);
    	ParametroGeral parametroGeralST     = parametroGeralService.findByNomeParametro("ID_TIPO_TRIBUTACAO_ST",false);
    	
    	if( (icms.getTipoTributacaoIcms().getIdTipoTributacaoIcms().compareTo(Long.valueOf(parametroGeralNormal.getDsConteudo())) == 0) ||
    		(icms.getTipoTributacaoIcms().getIdTipoTributacaoIcms().compareTo(Long.valueOf(parametroGeralST.getDsConteudo())) == 0) ){
    		if( icms.getPcAliquota().compareTo(new BigDecimal(0)) == 0 ){
    			throw new BusinessException("LMS-23020");
    		}
    	}
		
	}

	private boolean findExisteVigenciaAliquotaIva(AliquotaIcms bean){
    	return this.getAliquotaIcmsDAO().findExisteVigencia(bean);
    }
	
	/**
	 * Recupera uma instï¿½ncia de <code>AliquotaIcms</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instï¿½ncia que possui o id informado.
	 */
    public AliquotaIcms findById(java.lang.Long id) {
        return (AliquotaIcms)super.findById(id);
    }

    public List findAlquotaByEmbasamento(Long idEmbasamento){
    	return getAliquotaIcmsDAO().findAlquotasByEmbasamento(idEmbasamento);
    }    
    
	/**
	 * Apaga uma entidade atravï¿½s do Id.
	 *
	 * @param id indica a entidade que deverï¿½ ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga vï¿½rias entidades atravï¿½s do Id.
	 *
	 * @param ids lista com as entidades que deverï¿½o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrï¿½rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(AliquotaIcms bean) {
        return super.store(bean);
    }
    
    /**
	 * Busca AlicotaIcms por UF Origem/UF Destino
	 * @author Andrï¿½ Valadas
	 * @since 05/05/2009
	 * 
	 * @return
	 */
    public AliquotaIcms findAliquotaIcms(Long idUfOrigem, Long idUfDestino, YearMonthDay dtVigencia){
    	return getAliquotaIcmsDAO().findAliquotaIcms(idUfOrigem, idUfDestino, null, null, null, dtVigencia);
    }
    public AliquotaIcms findAliquotaIcms(Long idUfOrigem, Long idUfDestino, String tpSituacaoTribRemetente, String tpSituacaoTribDestinatario, String tpFrete, YearMonthDay dtVigencia){
    	return getAliquotaIcmsDAO().findAliquotaIcms(idUfOrigem, idUfDestino, tpSituacaoTribRemetente, tpSituacaoTribDestinatario, tpFrete, dtVigencia);
    }

    public AliquotaIcms findByRegiaoGeograficaDestinoVigente(Long idUfOrigem,Long idUfDestino, Long idPais, String tpSituacaoTribRemetente,
    		String tpSituacaoTribDestinatario, String tpFrete,YearMonthDay dtVigencia){
    	return getAliquotaIcmsDAO().findByRegiaoGeograficaDestinoVigente(idUfOrigem, idUfDestino, idPais, tpSituacaoTribRemetente, tpSituacaoTribDestinatario, tpFrete, dtVigencia);
    }

    public List findAliquotaIcmsVigente(Long idUfOrigem, Long idUfDestino, String tpSituacaoTribRemetente, String tpSituacaoTribDestinatario, String tpFrete){
    	return getAliquotaIcmsDAO().findAliquotaIcmsVigente(idUfOrigem, idUfDestino, tpSituacaoTribRemetente, tpSituacaoTribDestinatario, tpFrete);
    }
    
    public boolean findExisteVigenciaAliquota(AliquotaIcms bean){
    	return this.getAliquotaIcmsDAO().findExisteVigencia(bean);
    }

    /**
	 * Verifica se existe aliquota de ICMS vigente baseado apenas nas UFs de
	 * origem e destino.
	 * 
	 * @param idUfOrigem
	 *            identificador da UF de origem
	 * @param idUfDestino
	 *            identificador da UF de destino
	 * @param dtVigencia
	 *            data para verificar a vigência
	 * @return <code>true</code> caso exista aliquota vigente e
	 *         <code>false</code> caso contrário
	 * @author Luis Carlos Poletto
	 */
	public Boolean findExistsAliquotaIcms(Long idUfOrigem, Long idUfDestino, YearMonthDay dtVigencia) {
		return getAliquotaIcmsDAO().findExistsAliquotaIcms(idUfOrigem, idUfDestino, dtVigencia);
	}

    /**
	 * Busca AlicotaIcms por UF Origem/REGIAO GEOGRAFICA Destino
	 * @author Andrï¿½ Valadas
	 * @since 05/05/2009
	 * 
	 * @return
	 */
	public AliquotaIcms findAliquotaIcms(Long idUfOrigem, Long idUfDestino, Long idPais, YearMonthDay dtVigencia){
		return getAliquotaIcmsDAO().findAliquotaIcms(idUfOrigem, idUfDestino, idPais, dtVigencia);
	}

	/**
	 * Busca AlicotaIcms por UF Origem/UF Destino ou Regiao Geografica Destino
	 * @author Andrï¿½ Valadas
	 * @since 05/05/2009
	 * 
	 * @return
	 */
	public AliquotaIcms findAliquotaIcms(Long idUfOrigem, YearMonthDay dtVigencia){
		return getAliquotaIcmsDAO().findAliquotaIcms(idUfOrigem, null, null, null, dtVigencia);
	}

	/**
	 * Busca AlicotaIcms por UF Origem/UF Destino ou Regiao Geografica Destino por Situacao Tributaria
	 * @author Andrï¿½ Valadas
	 * @since 05/05/2009
	 * 
	 * @return
	 */
	public AliquotaIcms findAliquotaIcms(Long idUfOrigem, String tpSituacaoTribRemetente, String tpSituacaoTribDestinatario, String tpFrete, YearMonthDay dtVigencia){
		return getAliquotaIcmsDAO().findAliquotaIcms(idUfOrigem, tpSituacaoTribRemetente, tpSituacaoTribDestinatario, tpFrete, dtVigencia);
	}
	
	/**
	 * Busca AliquotaIcms por Regiao Geografica e Situacao Tributaria
	 * @author Andrï¿½ Valadas
	 * @since 05/05/2009
	 * 
	 * @return
	 */
    public AliquotaIcms findAliquotaIcms(Long idUfOrigem, Long idUfDestino, Long idPais, String tpSituacaoTribRemetente, String tpSituacaoTribDestinatario, String tpFrete, YearMonthDay dtVigencia){
    	return getAliquotaIcmsDAO().findAliquotaIcms(idUfOrigem, idUfDestino, idPais, tpSituacaoTribRemetente, tpSituacaoTribDestinatario, tpFrete, dtVigencia);
    }

    public AliquotaIcms findByUfAndSituacao(Long idUfOrigem, Long idUfDestino, String tpSituacaoTribRemetente, String tpSituacaoTribDestinatario, String tpFrete, YearMonthDay dtVigencia){
    	return getAliquotaIcmsDAO().findByUfAndSituacao(idUfOrigem, idUfDestino, tpSituacaoTribRemetente, tpSituacaoTribDestinatario, tpFrete, dtVigencia);
    }

    /**
     * Atribui o DAO responsï¿½vel por tratar a persistï¿½ncia dos dados deste serviï¿½o.
     * 
     * @param Instï¿½ncia do DAO.
     */
    public void setAliquotaIcmsDAO(AliquotaIcmsDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviï¿½o que ï¿½ responsï¿½vel por tratar a persistï¿½ncia dos dados deste serviï¿½o.
     *
     * @return Instï¿½ncia do DAO.
     */
    private AliquotaIcmsDAO getAliquotaIcmsDAO() {
        return (AliquotaIcmsDAO) getDao();
    }
    
    
    public ResultSetPage findPaginated(Map criteria) {
    	ResultSetPage rsp = getAliquotaIcmsDAO().findPaginated(criteria, 
    			FindDefinition.createFindDefinition(criteria));
    	return rsp;
    }

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
   }