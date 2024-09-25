package com.mercurio.lms.configuracoes.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.AgendaAtualizacaoRobo;
import com.mercurio.lms.configuracoes.model.dao.AgendaAtualizacaoRoboDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.agendaAtualizacaoRoboService"
 */
public class AgendaAtualizacaoRoboService extends CrudService<AgendaAtualizacaoRobo, Long> {

	private ParametroGeralService parametroGeralService;
	
	/**
	 * 
	 */
	public ResultSetPage findPaginated(Map criteria) {
		
		List<String> included = new ArrayList<String>();
		included.add("idAgendaAtualizacaoRobo");
		included.add("nrVersao");
		included.add("nrVersaoSOM");
		included.add("dhAtualizacao");
		included.add("dsObservacoes");
		
		ResultSetPage rsp = super.findPaginated(criteria);
		rsp.setList((List) ReflectionUtils.copyAndFilterNestedBean(rsp.getList(), included));
		return rsp;
	}

	@Override
	public Long store(AgendaAtualizacaoRobo bean) {
		Long ultimaVersao = getAgendaAtualizacaoRoboDAO().getUltimaVersao();
		if(ultimaVersao != null && ultimaVersao >= bean.getNrVersao()) {
			throw new BusinessException("LMS-04267");
		}
		
		Integer tmpMinAtualizacao = ((BigDecimal) parametroGeralService.findConteudoByNomeParametro("TMP_MINIMO_ATUALIZACAO_ROBO", false)).intValue();
		DateTime dhMinAtualizacao = JTDateTimeUtils.getDataHoraAtual().plusHours(tmpMinAtualizacao);
		if(dhMinAtualizacao.compareTo(bean.getDhAtualizacao()) > 0) {
			throw new BusinessException("LMS-04266", new Object[]{tmpMinAtualizacao});
		}
		
		Long r = (Long) super.store(bean); 
		return r;
	}
	
	public AgendaAtualizacaoRobo findUltimaAtualizacao(Long versaoAtual) {
		return getAgendaAtualizacaoRoboDAO().getUltimaAtualizacao(versaoAtual);
	}
	
	/**
	 * 
	 */
	public Integer getRowCount(Map criteria) {
		return getAgendaAtualizacaoRoboDAO().getRowCount(criteria);
	}
	
	/**
	 * Recupera uma inst�ncia de <code>ServidorFilial</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public AgendaAtualizacaoRobo findById(Long id) {
		return (AgendaAtualizacaoRobo) super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(Long id) {
		List<Long> ids = new ArrayList<Long>(1);
		ids.add(id);
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}
	
	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private AgendaAtualizacaoRoboDAO getAgendaAtualizacaoRoboDAO() {
		return (AgendaAtualizacaoRoboDAO) getDao();
	}
	
	/**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setAgendaAtualizacaoRoboDAO(AgendaAtualizacaoRoboDAO dao) {
        setDao( dao );
    }

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
}
