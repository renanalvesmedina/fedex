package com.mercurio.lms.coleta.model.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.OcorrenciaColeta;
import com.mercurio.lms.coleta.model.dao.OcorrenciaColetaDAO;
import com.mercurio.lms.coleta.model.util.ConstantesOcorrenciaColeta;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.ocorrenciaColetaService"
 */
public class OcorrenciaColetaService extends CrudService<OcorrenciaColeta, Long> {
	private static final String TP_EVENTO_COLETA_CANCELADA = "CA"; 


	/**
	 * Recupera uma instância de <code>OcorrenciaColeta</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
	public OcorrenciaColeta findById(java.lang.Long id) {
		return (OcorrenciaColeta)super.findById(id);
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
	public java.io.Serializable store(OcorrenciaColeta bean) {
		return super.store(bean);
	}

	public java.io.Serializable storeOcorrenciasColeta(TypedFlatMap criteria) {
		/*
		 Validação para evitar o cadastro ou edição de uma ocorrenciaColeta com o mesmo código
		 CQPRO00023203
		 */
		List<OcorrenciaColeta> ocorrenciasColetas = getOcorrenciaColetaDAO().findOcorrenciaColetaByCodigo(criteria.getShort("codigo"));

		for (OcorrenciaColeta ocorrenciaColeta : ocorrenciasColetas) {
			getDao().getAdsmHibernateTemplate().evict(ocorrenciaColeta);
			if(ocorrenciaColeta.getCodigo().equals(criteria.getShort("codigo")))
			{
				if(!ocorrenciaColeta.getIdOcorrenciaColeta().equals(criteria.getLong("idOcorrenciaColeta")))
				{
					throw new BusinessException("LMS-02090");
				}
			}
				
		}
		
		OcorrenciaColeta ocorrenciaColeta = new OcorrenciaColeta();
		ocorrenciaColeta.setIdOcorrenciaColeta(criteria.getLong("idOcorrenciaColeta"));
		ocorrenciaColeta.setTpEventoColeta(criteria.getDomainValue("tpEventoColeta"));
		ocorrenciaColeta.setDsDescricaoResumida(criteria.getVarcharI18n("dsDescricaoResumida"));
		ocorrenciaColeta.setCodigo(criteria.getShort("codigo"));
		ocorrenciaColeta.setBlIneficienciaFrota(criteria.getBoolean("blIneficienciaFrota"));
		ocorrenciaColeta.setDsDescricaoCompleta(criteria.getVarcharI18n("dsDescricaoCompleta"));
		ocorrenciaColeta.setTpSituacao(criteria.getDomainValue("tpSituacao"));
		return super.store(ocorrenciaColeta);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setOcorrenciaColetaDAO(OcorrenciaColetaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private OcorrenciaColetaDAO getOcorrenciaColetaDAO() {
		return (OcorrenciaColetaDAO) getDao();
	}

	/**
	 * Método que busca uma lista de ocorrencias de coleta que tenham
	 * o tpEventoColeta do tipo cancelada.
	 * Este método é usado em cancelarColeta
	 * @param criteria
	 * @return List
	 * @author Rodrigo Antunes
	 */
	public List findOcorrenciaColetaByTpEventoColetaCancelada(Map<String, Object> criteria) {
		// Apenas para garantir que criteria não virá null
		// ele sempre vem com tpSituacao setado.
		if(criteria==null) {
			criteria = new HashMap<String, Object>();
		}
		criteria.put("tpEventoColeta",TP_EVENTO_COLETA_CANCELADA);
		List<String> campoOrdenacao = new ArrayList<String>();
		campoOrdenacao.add("dsDescricaoCompleta:asc");
		return getOcorrenciaColetaDAO().findListByCriteria(criteria, campoOrdenacao);
	}

	/**
	 * Retorna as ocorrencias de coleta para um certo tipo de evento de coleta
	 * passado por parâmetro ("CA"=cancelada, "SO"=solicitação, ...).
	 * @param strTpEventoColeta String que representa o evento de coleta ("CA"=cancelada, "SO"=solicitação, ...).
	 * @return List
	 */
	 public List<OcorrenciaColeta> findOcorrenciaColetaByTpEventoColeta(String strTpEventoColeta){
		return getOcorrenciaColetaDAO().findOcorrenciaColetaByTpEventoColeta(strTpEventoColeta);
	}
	 
	 public List<OcorrenciaColeta> findOcorrenciaColetaByTpEventoColetaRetornoColeta() {
		List<OcorrenciaColeta> l = getOcorrenciaColetaDAO().findOcorrenciaColetaByTpEventoColeta(ConstantesOcorrenciaColeta.TP_EVENTO_COLETA_RETORNO_COLETA);
		
		Collections.sort(l, new Comparator<OcorrenciaColeta>() {
			@Override
			public int compare(OcorrenciaColeta er1, OcorrenciaColeta er2) {
				return er1.getCodigo().compareTo(er2.getCodigo());
			}
		});
		
		return l;
	 }

	/**
	 * Localiza uma lista de resultados a partir dos critérios de busca 
	 * informados. Permite especificar regras de ordenação.
	 * 
	 * @param criterions Critérios de busca.
	 * @param lista com criterios de ordenação. Deve ser uma java.lang.String no formato
	 * 	<code>nomePropriedade:asc</code> ou <code>associacao_.nomePropriedade:desc</code>.
	 * A utilização de <code>asc</code> ou <code>desc</code> é opcional sendo o padrão <code>asc</code>.
	 * @return Lista de resultados sem paginação.
	 */ 
	public List findListByCriteria(Map criteria, List campoOrdenacao) {
		return getOcorrenciaColetaDAO().findListByCriteria(criteria, campoOrdenacao);
	}
	
		
	
}