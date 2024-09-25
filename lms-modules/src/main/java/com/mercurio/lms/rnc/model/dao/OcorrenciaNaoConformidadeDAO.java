package com.mercurio.lms.rnc.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.carregamento.model.CarregamentoDescargaVolume;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.rnc.model.NotaOcorrenciaNc;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class OcorrenciaNaoConformidadeDAO extends BaseCrudDao<OcorrenciaNaoConformidade, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return OcorrenciaNaoConformidade.class;
    }
    
	protected void initFindLookupLazyProperties(Map map) {
		map.put("motivoAberturaNc", FetchMode.JOIN);
		map.put("naoConformidade", FetchMode.JOIN);
		map.put("naoConformidade.filial", FetchMode.JOIN);
	}
	
	protected void initFindListLazyProperties(Map map) {
		map.put("motivoAberturaNc", FetchMode.JOIN);
		map.put("naoConformidade", FetchMode.JOIN);
		map.put("naoConformidade.filial", FetchMode.JOIN);
		map.put("filialByIdFilialLegado", FetchMode.JOIN);
	}
	
	protected void initFindByIdLazyProperties(Map map) {
		map.put("naoConformidade", FetchMode.JOIN);
		map.put("naoConformidade.ocorrenciaNaoConformidades", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("moeda", FetchMode.JOIN);
		map.put("motivoAberturaNc", FetchMode.JOIN);
		map.put("filialByIdFilialResponsavel", FetchMode.JOIN);
		map.put("filialByIdFilialResponsavel.pessoa", FetchMode.JOIN);
		map.put("naoConformidade", FetchMode.JOIN);
		map.put("naoConformidade.filial", FetchMode.JOIN);
		map.put("filialByIdFilialLegado", FetchMode.JOIN);
	}

	public Integer findMaxNrOcorrenciaNcByNaoConformidade(Long idNaoConformidade) {
		StringBuffer sql = new StringBuffer()
			.append("select max(ocorrencia.nrOcorrenciaNc) from ")
			.append(OcorrenciaNaoConformidade.class.getName()).append(" as ocorrencia ")
			.append("where ocorrencia.naoConformidade.id = ? ");

    	List result = getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idNaoConformidade});
    	if (result == null || result.isEmpty() || result.iterator().next() == null) {
    		return Integer.valueOf(1);
    	}
    	else
    		return Integer.valueOf(((Integer)result.iterator().next()).intValue() + 1);
    }
	
    /**
     * 
     * @param idOcorrenciaNaoConformidade
     */
    public void removeNotaOcorrenciaNc(Long idOcorrenciaNaoConformidade) {
    	String sql = "delete from " + NotaOcorrenciaNc.class.getName() + " as nonc " +
    			" where nonc.ocorrenciaNaoConformidade.id = :id";
    	getAdsmHibernateTemplate().removeById(sql, idOcorrenciaNaoConformidade);
    }
    
    /**
     * Retorna as ocorrencias de nao conformidade de acordo com o idNaoConformidade.
     * @param idNaoConformidade
     * @param tpStatusOcorrenciaNc
     * @return
     * @author luisfco
     */
    public List findOcorrenciasByIdNaoConformidade(Long idNaoConformidade, String tpStatusOcorrenciaNc) {
    	StringBuffer sql = new StringBuffer()
    	.append("from " + OcorrenciaNaoConformidade.class.getName() + " onc ")
    	.append("join fetch onc.filialByIdFilialResponsavel fil ")
    	.append("join fetch fil.empresa ")
    	.append("where onc.naoConformidade.idNaoConformidade = ? ");

    	List param = new ArrayList();
    	param.add(idNaoConformidade);

    	if (tpStatusOcorrenciaNc != null) {
    		sql.append("and onc.tpStatusOcorrenciaNc = ? ");
    		param.add(tpStatusOcorrenciaNc);
    	}
    	return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
    }

    /**
	 * LMS-3240
	 * 
	 * Busca todas as ocorrencias de acordo com o id da nao conformidade e os
	 * id's do motivo de abertura, que vão compor o IN da consulta
	 * 
	 * @param idNaoConformidade
	 *            referente ao id da nao conformidade
	 * @param idMotivoAberturaNC
	 *            informar os id's do motivo para compor o IN da consulta
	 * @author WagnerFC
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List<OcorrenciaNaoConformidade> findOcorreciasByIdNaoConformidadeAndMotivoAberturaNc(Long idNaoConformidade, Integer... idMotivoAberturaNc) {
    	if(idNaoConformidade == null || idMotivoAberturaNc.length == 0) {
    		return new ArrayList<OcorrenciaNaoConformidade>();
    	}
    	
    	StringBuilder ids = new StringBuilder();
    	
    	for(Integer idMotivo : idMotivoAberturaNc) {
    		if(ids.length() == 0) {
    			ids.append(idMotivo.toString());
    			continue;
    		}
    		
    		ids.append(", ");
    		ids.append(idMotivo.toString());
    	}
    	
    	StringBuilder sql = new StringBuilder();
    	sql.append(" FROM ").append(OcorrenciaNaoConformidade.class.getName()).append(" onc");
    	sql.append(" WHERE onc.naoConformidade.idNaoConformidade = ?");
    	sql.append(" AND onc.motivoAberturaNc.idMotivoAberturaNc IN");
    	sql.append(" (").append(ids.toString()).append(")");

    	List params = new ArrayList();
    	params.add(idNaoConformidade);
    	
    	return (List<OcorrenciaNaoConformidade>) getAdsmHibernateTemplate().find(sql.toString(), params.toArray());
    }

    /**
     * Obtém a menor Ocorrencia de Não Conformidade relacionada à não conformidade 
     * @param idNaoConformidade
     * @return
     */
    public OcorrenciaNaoConformidade findFirstOcorrenciaByIdNaoConformidade(Long idNaoConformidade) {
    	StringBuffer sb = new StringBuffer()
    	.append("from "+OcorrenciaNaoConformidade.class.getName()+" onc ")
    	.append(" left join fetch onc.controleCarga cc ")
    	.append(" left join fetch cc.filialByIdFilialOrigem ")
    	.append(" left join fetch cc.moeda")
    	.append(" left join fetch onc.manifesto man ")
    	.append(" left join fetch man.filialByIdFilialOrigem ")
    	.append(" left join fetch man.manifestoViagemNacional ")
    	.append(" left join fetch man.manifestoInternacional ")
    	.append(" left join fetch man.manifestoEntrega ")
    	.append("where onc.naoConformidade.id = ? ")
    	.append("  and onc.nrOcorrenciaNc = ")
    					.append("(select min(onc2.nrOcorrenciaNc) ")
    					.append(" from "+OcorrenciaNaoConformidade.class.getName()+" onc2 ")
    					.append(" where onc2.naoConformidade = onc.naoConformidade)");
    	
    	return (OcorrenciaNaoConformidade) getAdsmHibernateTemplate().findUniqueResult(sb.toString(), new Object[]{idNaoConformidade});
    }
    
    /**
	 * Método findById específico
	 */
    public List findOcorrenciaNaoConformidadeById(java.lang.Long id) {
		StringBuffer projecao = new StringBuffer()
			.append("new map(") 
	    	.append("onc.idOcorrenciaNaoConformidade as idOcorrenciaNaoConformidade, ")
	    	.append("onc.dhInclusao as dhInclusao, ")
	    	.append("onc.tpStatusOcorrenciaNc as tpStatusOcorrenciaNc, ")
	    	.append("onc.nrOcorrenciaNc as nrOcorrenciaNc, ")
	    	.append("onc.dsOcorrenciaNc as dsOcorrenciaNc, ")
	    	.append("onc.blCaixaReaproveitada as blCaixaReaproveitada, ")
	    	.append("onc.dsCaixaReaproveitada as dsCaixaReaproveitada, ")
	    	.append("onc.dsCausaNc as dsCausaNc, ")
	    	.append("onc.vlOcorrenciaNc as vlOcorrenciaNc, ")
	    	.append("onc.qtVolumes as qtVolumes, ")
	    	.append("onc.nrRncLegado as nrRncLegado, ")
	    	.append("filialLegado.sgFilial as filialByIdFilialLegado_sgFilial, ")
	    	.append("motivoAberturaNc.idMotivoAberturaNc as motivoAberturaNc_idMotivoAberturaNc, ")
	    	.append("motivoAberturaNc.dsMotivoAbertura as motivoAberturaNc_dsMotivoAbertura, ")
	    	.append("moeda.idMoeda as moeda_idMoeda, ")
	    	.append("moeda.dsSimbolo as moeda_dsSimbolo, ")
	    	.append("moeda.sgMoeda as moeda_sgMoeda, ")
	    	.append("usuario.idUsuario as usuario_idUsuario, ")
	    	.append("usuario.nmUsuario as usuario_nmUsuario, ")
	    	.append("causaNaoConformidade.idCausaNaoConformidade as causaNaoConformidade_idCausaNaoConformidade, ")
	    	.append("causaNaoConformidade.dsCausaNaoConformidade as causaNaoConformidade_dsCausaNaoConformidade, ")
	    	.append("descricaoPadraoNc.idDescricaoPadraoNc as descricaoPadraoNc_idDescricaoPadraoNc, ")
	    	.append("descricaoPadraoNc.dsPadraoNc as descricaoPadraoNc_dsPadraoNc, ")
	    	.append("empresa.idEmpresa as empresa_idEmpresa, ")
	    	.append("empresaPessoa.nrIdentificacao as empresa_pessoa_nrIdentificacao, ")
	    	.append("empresaPessoa.nmPessoa as empresa_pessoa_nmPessoa, ")
	    	.append("naoConformidade.idNaoConformidade as naoConformidade_idNaoConformidade, ")
	    	.append("naoConformidade.nrNaoConformidade as naoConformidade_nrNaoConformidade, ")
	    	.append("naoConformidade.dsMotivoAbertura as naoConformidade_dsMotivoAbertura, ")
	    	.append("doctoServico.idDoctoServico as naoConformidade_doctoServico_idDoctoServico, ")
	    	.append("doctoServico.nrDoctoServico as naoConformidade_doctoServico_nrDoctoServico, ")
	    	.append("doctoServico.tpDocumentoServico as naoConformidade_doctoServico_tpDocumentoServico, ")
	    	.append("filialOrigemDoctoServico.sgFilial as naoConformidade_doctoServico_filialByIdFilialOrigem_sgFilial, ")
	    	.append("naoConformidadeFilial.idFilial as naoConformidade_filial_idFilial, ")
	    	.append("naoConformidadeFilial.sgFilial as naoConformidade_filial_sgFilial, ")
	    	.append("filialResponsavel.idFilial as filialByIdFilialResponsavel_idFilial, ")
	    	.append("filialResponsavel.sgFilial as filialByIdFilialResponsavel_sgFilial, ")
	    	.append("filialPessoaResponsavel.nmFantasia as filialByIdFilialResponsavel_pessoa_nmFantasia, ")
	    	.append("controleCarga.idControleCarga as controleCarga_idControleCarga, ")
	    	.append("controleCarga.nrControleCarga as controleCarga_nrControleCarga, ")
	    	.append("controleCargaFilialOrigem.idFilial as controleCarga_filialByIdFilialOrigem_idFilial, ")
	    	.append("controleCargaFilialOrigem.sgFilial as controleCarga_filialByIdFilialOrigem_sgFilial, ")
	    	.append("meioTransporteByIdTransportado.nrFrota as controleCarga_meioTransporteByIdTransportado_nrFrota, ")
	    	.append("meioTransporteByIdTransportado.nrIdentificador as controleCarga_meioTransporteByIdTransportado_nrIdentificador, ")
	    	.append("meioTransporteByIdSemiRebocado.nrFrota as controleCarga_meioTransporteByIdSemiRebocado_nrFrota, ")
	    	.append("meioTransporteByIdSemiRebocado.nrIdentificador as controleCarga_meioTransporteByIdSemiRebocado_nrIdentificador, ")
	    	.append("manifesto.idManifesto as manifesto_idManifesto, ")
	    	.append("manifesto.tpManifesto as manifesto_tpManifesto, ")
	    	.append("manifesto.tpAbrangencia as manifesto_tpAbrangenciaViagem, ")
	    	.append("manifestoFilialByIdFilialOrigem.idFilial as manifesto_filialByIdFilialOrigem_idFilial, ")
	    	.append("manifestoFilialByIdFilialOrigem.sgFilial as manifesto_filialByIdFilialOrigem_sgFilial, ")
	    	.append("manifestoEntrega.nrManifestoEntrega as manifesto_manifestoEntrega_nrManifestoEntrega, ")
	    	.append("manifestoViagemNacional.nrManifestoOrigem as manifesto_manifestoViagemNacional_nrManifestoOrigem, ")
	    	.append("manifestoInternacional.nrManifestoInt as manifesto_manifestoInternacional_nrManifestoInt) ");

		StringBuffer from = new StringBuffer()
			.append(OcorrenciaNaoConformidade.class.getName())
			.append(" as onc ")
			.append("left join onc.moeda as moeda ")
			.append("left join onc.motivoAberturaNc as motivoAberturaNc ")
			.append("left join onc.naoConformidade as naoConformidade ")
			.append("left join naoConformidade.doctoServico as doctoServico ")
			.append("left join doctoServico.filialByIdFilialOrigem as filialOrigemDoctoServico ")
			.append("left join naoConformidade.filial as naoConformidadeFilial ")
			.append("left join onc.filialByIdFilialResponsavel as filialResponsavel ")
			.append("left join filialResponsavel.pessoa as filialPessoaResponsavel ")
			.append("left join onc.causaNaoConformidade as causaNaoConformidade ")
			.append("left join onc.descricaoPadraoNc as descricaoPadraoNc ")
			.append("left join onc.empresa as empresa ")
			.append("left join empresa.pessoa as empresaPessoa ")
			.append("left join onc.usuario as usuario ")
			.append("left join onc.controleCarga as controleCarga ")
			.append("left join controleCarga.filialByIdFilialOrigem as controleCargaFilialOrigem ")
			.append("left join controleCarga.meioTransporteByIdTransportado as meioTransporteByIdTransportado ")
			.append("left join controleCarga.meioTransporteByIdSemiRebocado as meioTransporteByIdSemiRebocado ")
			.append("left join onc.manifesto as manifesto ")
			.append("left join manifesto.filialByIdFilialOrigem as manifestoFilialByIdFilialOrigem ")
			.append("left join manifesto.manifestoEntrega as manifestoEntrega ")
			.append("left join manifesto.manifestoViagemNacional as manifestoViagemNacional ")
			.append("left join manifesto.manifestoInternacional as manifestoInternacional ")
			.append("left join onc.filialByIdFilialLegado as filialLegado ");

		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(projecao.toString());
		sql.addFrom(from.toString());
		sql.addCriteria("onc.id", "=", id);
		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }
    
    public Integer getRowCountOcorrenciasByIdNaoConformidade(Long idNaoConformidade) {
    	return getAdsmHibernateTemplate().getRowCountForQuery("from "+OcorrenciaNaoConformidade.class.getName()+" onc where onc.naoConformidade.id=?", new Object[]{idNaoConformidade});    	
    }
    

    public OcorrenciaNaoConformidade findByIdCustom(Long id) {
	    StringBuffer sql = new StringBuffer()
		    .append("from ")
			.append(OcorrenciaNaoConformidade.class.getName()).append(" as onc ")
			.append("inner join fetch onc.naoConformidade as naoConformidade ")
			.append("inner join fetch naoConformidade.filial as filialNaoConformidade ")
			.append("where ")
	    	.append("onc.id = ? ");
	    List list = getAdsmHibernateTemplate().find(sql.toString(), new Object[] {id});
	    return (OcorrenciaNaoConformidade)list.get(0);
    }

    /**
     * Solicitação CQPRO00004838 da integração.
     * Retorne uma instancia da classe OcorrenciaNaoConformidade conforme os parametros especificados.
     * @param nrRncLegado
     * @param filialByIdFilialLegado
     * @return
     */
	public OcorrenciaNaoConformidade findByNrRncLegadoIdFilialByIdFilialLegado(Integer nrRncLegado, Long filialByIdFilialLegado) {
		DetachedCriteria dc = DetachedCriteria.forClass(OcorrenciaNaoConformidade.class);
		dc.add(Restrictions.eq("filialByIdFilialLegado.id", filialByIdFilialLegado));
		dc.add(Restrictions.eq("nrRncLegado", nrRncLegado));
		return (OcorrenciaNaoConformidade)getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	
	public List findOcorrenciasNaoConformidadeAbertas(Long idNaoConformidade) {
	    StringBuffer sql = new StringBuffer()
		    .append("from ")
			.append(OcorrenciaNaoConformidade.class.getName()).append(" as onc ")
			.append("where ")
	    	.append("onc.naoConformidade.id = ? ")
	    	.append("and onc.tpStatusOcorrenciaNc = 'A' ");
	    List list = getAdsmHibernateTemplate().find(sql.toString(), new Object[] {idNaoConformidade});
	    return list;
	}
	
	/**
	 * Busca todas as Filiais responsáveis pelas OcorrenciasNaoConformidade de um dado DoctoServico.
	 * @param idDoctoServico
	 * @return
	 */
	public List findFiliaisResponsaveisOcorrenciasNaoConformidade(Long idDoctoServico){
		StringBuilder sql = new StringBuilder()
	    	.append("select distinct onc.filialByIdFilialResponsavel from ")
	    	.append(OcorrenciaNaoConformidade.class.getName()).append(" as onc ")
	    	.append("inner join onc.naoConformidade as naoConformidade ")
	    	.append("where ")
	    	.append("naoConformidade.doctoServico.id = ? ");
		return getAdsmHibernateTemplate().find(sql.toString(), new Object[] {idDoctoServico});
	}
	
	/**
	 * Busca uma List de OcorrenciaNaoConformidade que possuem disposição
	 * que por sua vez possui um motivo com BL_SOMENTE_AUTOMATICO = 'S'
	 * @param idDoctoServico
	 * @return
	 */
	public List findOcorrenciasNaoConformidadeSomenteAutomatico(Long idDoctoServico){
		StringBuilder sql = new StringBuilder()
			.append("select onc from ")
			.append(OcorrenciaNaoConformidade.class.getName()).append(" as onc ")
			.append("inner join onc.naoConformidade as nc ")
			.append("inner join onc.naoConformidade.doctoServico as ds ")
			.append("inner join onc.disposicoes as d ")
			.append("inner join d.motivoDisposicao as md ")
	    	.append("where ")
	    	.append("md.blSomenteAutomatico = ? ")
	    	.append("and ds.id = ? ");
	    	return getAdsmHibernateTemplate().find(sql.toString(), new Object[] {Boolean.TRUE, idDoctoServico});
	}

	
	/**
	 * Verifica de já exista o "motivo da ocorrencia" relacionado a "não conformidade" 
	 * 
	 * @param idNaoConformidade
	 * @param idMotivoAberturaNc
	 * @return true se já existir esta relação
	 */
	public Boolean validaTipoOcorrenciaJaUsado(Long idNaoConformidade, Long idMotivoAberturaNc){
		StringBuilder sql = new StringBuilder()
		.append("select onc from ")
		.append(OcorrenciaNaoConformidade.class.getName()).append(" as onc ")
    	.append("where ")
    	.append("onc.naoConformidade.idNaoConformidade = ? ")
    	.append("and onc.motivoAberturaNc.idMotivoAberturaNc = ? ")
    	.append("and onc.tpStatusOcorrenciaNc = ? ");
		return getAdsmHibernateTemplate().find(sql.toString(), new Object[] {idNaoConformidade, idMotivoAberturaNc, "A"}).size() > 0;
    }
	
	/**
	 * LMS - 4460
	 * Busca e retorna as Ocorrências de RNCs dos tipos Falta Total e Falta Parcial,
	 * existentes nos documentos oriundos do Controle de Carga passado por parâmetro
	 * @param idControleCarga Identificador do controle de carga
	 * @param tpScan Identificação do tipo de Picking dos volumes
	 * @param listaTipoRNC Listagem IDs RNCs aptas ao encerramento automático
	 * @return Lista de Ocorrências de RNCs
	 */
	public List<OcorrenciaNaoConformidade> buscaRNCsAptasEncerramento(Long idControleCarga, String[] tpScan, String listaTipoRNC) {

		HashMap<String, Object> mapParams = new HashMap<String, Object>();

		mapParams.put("idControleCarga", idControleCarga);
		mapParams.put("tpStatusNaoConformidade", "RNC");
		mapParams.put("tpStatusOcorrenciaNc", "A");
		mapParams.put("tpScan", tpScan);

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ocorrenciaNaoConformidade ");
		sql.append("FROM ").append(OcorrenciaNaoConformidade.class.getName()).append(" ocorrenciaNaoConformidade ");
		sql.append("inner join ocorrenciaNaoConformidade.naoConformidade as naoConformidade ");
		sql.append("inner join naoConformidade.doctoServico as doctoServico ");

		sql.append("inner join doctoServico.preManifestoDocumentos pmd ");
		sql.append("inner join pmd.manifesto man ");
		sql.append("inner join man.controleCarga cc ");

		sql.append("WHERE cc.idControleCarga = :idControleCarga ");

		sql.append("AND naoConformidade.doctoServico.idDoctoServico = pmd.doctoServico.idDoctoServico ");
		sql.append("AND naoConformidade.tpStatusNaoConformidade = :tpStatusNaoConformidade ");
		sql.append("AND ocorrenciaNaoConformidade.tpStatusOcorrenciaNc =  :tpStatusOcorrenciaNc");

		if (listaTipoRNC != null && !listaTipoRNC.isEmpty()) {
			sql.append(" AND ocorrenciaNaoConformidade.motivoAberturaNc.idMotivoAberturaNc IN");
			sql.append(" (").append(listaTipoRNC.replace(";", ",")).append(") ");
		}

		sql.append("AND doctoServico.qtVolumes = (");
		sql.append("SELECT COUNT(DISTINCT volumeNotaFiscal.idVolumeNotaFiscal) ");
		sql.append("FROM ").append(CarregamentoDescargaVolume.class.getName()).append(" carregamentoDescargaVolume ");
		sql.append("inner join carregamentoDescargaVolume.volumeNotaFiscal as volumeNotaFiscal ");
		sql.append("inner join carregamentoDescargaVolume.carregamentoDescarga as carregamentoDescarga ");
		sql.append("inner join carregamentoDescarga.filial as filial ");
		sql.append("inner join volumeNotaFiscal.notaFiscalConhecimento as notaFiscalConhecimento ");
		sql.append("inner join notaFiscalConhecimento.conhecimento as conhecimento ");
		sql.append("WHERE conhecimento.idDoctoServico = doctoServico.idDoctoServico ");
		sql.append("AND carregamentoDescargaVolume.tpScan IN (:tpScan) ");
		sql.append("AND filial.idFilial = doctoServico.filialByIdFilialDestino.idFilial ");
		sql.append("AND carregamentoDescargaVolume.dhOperacao.value > ocorrenciaNaoConformidade.dhInclusao.value ");
		sql.append(")");

		return (List<OcorrenciaNaoConformidade>) getAdsmHibernateTemplate().findByNamedParam(sql.toString(), mapParams);
	}
    	
	
	public List<Map<String, Object>> buscaDocumentosComVolumesNaoCarregados(Long idControleCarga, Long idFilial, Long cdLocalizacaoMercadoria, List<String> tiposRNC){
		StringBuilder sql = generateDocumentosComVolumesNaoCarregadosQuery();
		ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("ID_NOTA_FISCAL_CONHECIMENTO", Hibernate.LONG);
				sqlQuery.addScalar("ID_MANIFESTO", Hibernate.LONG);
				sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
			}
		};
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idControleCarga", idControleCarga);
		params.put("idFilial", idFilial);
		params.put("cdLocalizacaoMercadoria", cdLocalizacaoMercadoria);
		params.put("tiposRNC", tiposRNC);
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), params, confSql); 
	}
	
	public List<Map<String, Object>> buscaDocumentosComVolumesNaoDescarregados(Long idControleCarga, Long idFilial, List<Long> cdLocalizacaoMercadoria, List<String> tiposRNC){
		StringBuilder sql = generateDocumentosComVolumesNaoDescarregadosQuery();
		ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
				sqlQuery.addScalar("TP_DOCUMENTO_SERVICO", Hibernate.STRING);
				sqlQuery.addScalar("SG_FILIAL", Hibernate.STRING);
				sqlQuery.addScalar("NR_DOCTO_SERVICO", Hibernate.LONG);
				sqlQuery.addScalar("NR_SEQUENCIA", Hibernate.LONG);
				sqlQuery.addScalar("QT_VOLUMES", Hibernate.LONG);
				sqlQuery.addScalar("ID_VOLUME_NOTA_FISCAL", Hibernate.LONG);
				sqlQuery.addScalar("ID_MANIFESTO", Hibernate.LONG);
				sqlQuery.addScalar("CD_LOCALIZACAO_MERCADORIA", Hibernate.SHORT);
				sqlQuery.addScalar("ID_NOTA_FISCAL_CONHECIMENTO", Hibernate.LONG);
			}
		};
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idControleCarga", idControleCarga);
		params.put("idFilial", idFilial);
		params.put("cdLocalizacaoMercadoria", cdLocalizacaoMercadoria);
		params.put("tiposRNC", tiposRNC);
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), params, confSql); 
	}
	
	public List<Map<String, Object>> buscaDocumentosComVolumesSobra(Long idControleCarga){
        StringBuilder sql = generateDocumentosComVolumesSobraQuery();
        ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
            public void configQuery(SQLQuery sqlQuery) {                
                sqlQuery.addScalar("ID_NOTA_FISCAL_CONHECIMENTO", Hibernate.LONG);
                sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
            }
        };
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("idControleCarga", idControleCarga);
        
        return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), params, confSql); 
    }
	
	private StringBuilder generateDocumentosComVolumesNaoDescarregadosQuery() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT DOCT.ID_DOCTO_SERVICO, \n");
		sql.append("  DOCT.TP_DOCUMENTO_SERVICO, \n");
		sql.append("  F.SG_FILIAL, \n");
		sql.append("  DOCT.NR_DOCTO_SERVICO, \n");
		sql.append("  VOLU.NR_SEQUENCIA, \n");
		sql.append("  DOCT.QT_VOLUMES, \n");
		sql.append("  VOLU.ID_VOLUME_NOTA_FISCAL, \n");
		sql.append("  LOME.cd_localizacao_mercadoria, \n");
		sql.append("  MANI.ID_MANIFESTO, \n");
		sql.append("  VOLU.ID_NOTA_FISCAL_CONHECIMENTO \n");
		sql.append("FROM CONTROLE_CARGA CC , \n");
		sql.append("  MANIFESTO MANI , \n");
		sql.append("  pre_manifesto_volume PMV , \n");
		sql.append("  VOLUME_NOTA_FISCAL VOLU , \n");
		sql.append("  LOCALIZACAO_MERCADORIA LOME , \n");
		sql.append("  carregamento_descarga CD , \n");
		sql.append("  DOCTO_SERVICO DOCT , \n");
		sql.append("  filial f , \n");
		sql.append("  CLIENTE C , \n");
		sql.append("  SERVICO S \n");
		sql.append("WHERE 1=1 \n");
		
		sql.append("AND CC.ID_CONTROLE_CARGA            = :idControleCarga \n");
		sql.append("AND cc.id_controle_carga            = MANI.ID_CONTROLE_CARGA \n");
		sql.append("AND mani.id_filial_destino          = :idFilial \n");
		sql.append("AND CC.ID_CONTROLE_CARGA            = CD.id_controle_carga \n");
		
		// operação descarga na unidade destino
		sql.append("AND CD.tp_operacao                  = 'D'\n");
		
		// operação não está cancelada
		sql.append("AND CD.tp_status_operacao          != 'C' \n");
		
		sql.append("AND cd.id_filial                    = mani.id_filial_destino \n");
		sql.append("AND MANI.id_manifesto               = pmv.id_manifesto \n");
		sql.append("AND pmv.id_volume_nota_fiscal       = VOLU.id_volume_nota_fiscal \n");
		sql.append("AND VOLU.ID_LOCALIZACAO_FILIAL      = mani.id_filial_destino \n");
		sql.append("AND VOLU.ID_LOCALIZACAO_MERCADORIA  = LOME.ID_LOCALIZACAO_MERCADORIA \n");
		sql.append("AND LOME.CD_LOCALIZACAO_MERCADORIA IN (:cdLocalizacaoMercadoria) \n");
		
		// não existe registro de leitura de descarga fisica para o volume para a descarga em questão.
		sql.append("AND NOT EXISTS                                          \n");
		sql.append("  (SELECT 1 \n");
		sql.append("  FROM carreg_desc_volume CDV \n");
		sql.append("  WHERE CDV.id_carregamento_descarga = cd.id_carregamento_descarga \n");
		sql.append("  AND cdv.id_volume_nota_fiscal      = volu.id_volume_nota_fiscal \n");
		sql.append("  AND cdv.tp_scan                   != 'LM' \n");
		sql.append("  AND rownum                        <= 1 \n");
		sql.append("  ) \n");
		
		//apenas clientes com roterização
		sql.append("AND PMV.ID_DOCTO_SERVICO      = DOCT.ID_DOCTO_SERVICO \n");
		sql.append("AND DOCT.id_filial_origem     = f.id_filial \n");
		sql.append("AND C.ID_CLIENTE              = DOCT.ID_CLIENTE_REMETENTE \n");
		sql.append("AND (C.BL_LIBERA_ETIQUETA_EDI = 'N' OR C.BL_LIBERA_ETIQUETA_EDI  IS NULL) \n");
		
		//apenas manifestos rodoviarios
		sql.append("AND MANI.TP_MODAL = 'R' \n");
		
		//apenas documentos rodoviarios
		sql.append("AND DOCT.ID_SERVICO = S.ID_SERVICO \n");
		sql.append("AND S.TP_MODAL      = 'R' \n");
		
		//desconsiderar entrega parceira
		sql.append("AND (MANI.TP_MANIFESTO_ENTREGA IS NULL OR MANI.TP_MANIFESTO_ENTREGA   != 'EP') \n");
		
		//desconsiderar se já existir uma rnc de falta aberta para o documento de servico
		sql.append("AND NOT EXISTS \n");
		sql.append("  (SELECT 1 \n");
		sql.append("  FROM NAO_CONFORMIDADE NCON, \n");
		sql.append("    OCORRENCIA_NAO_CONFORMIDADE OCNC \n");
		sql.append("  WHERE NCON.ID_NAO_CONFORMIDADE   = OCNC.ID_NAO_CONFORMIDADE \n");
		sql.append("  AND NCON.ID_DOCTO_SERVICO        = DOCT.ID_DOCTO_SERVICO \n");
		sql.append("  AND OCNC.TP_STATUS_OCORRENCIA_NC = 'A' \n");
		sql.append("  AND OCNC.ID_MOTIVO_ABERTURA_NC  IN (:tiposRNC)\n");
		sql.append("  AND ROWNUM                      <= 1 \n");
		sql.append("  ) \n");
		
		sql.append("ORDER BY DOCT.TP_DOCUMENTO_SERVICO, F.SG_FILIAL, DOCT.NR_DOCTO_SERVICO, VOLU.NR_SEQUENCIA");
		return sql;
	}
	
	private StringBuilder generateDocumentosComVolumesNaoCarregadosQuery() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT  ");
		sql.append(" 	NOTA.ID_NOTA_FISCAL_CONHECIMENTO, ");
		sql.append(" 	MANI.ID_MANIFESTO, ");
		sql.append("    DOCT.ID_DOCTO_SERVICO ");
		sql.append(" FROM  ");
		sql.append(" 	MANIFESTO MANI, ");
		sql.append(" 	PRE_MANIFESTO_DOCUMENTO PMDO, ");
		sql.append(" 	DOCTO_SERVICO DOCT, ");
		sql.append(" 	NOTA_FISCAL_CONHECIMENTO NOTA, ");
		sql.append(" 	VOLUME_NOTA_FISCAL VOLU, ");
		sql.append(" 	LOCALIZACAO_MERCADORIA LOME ");
		sql.append(" WHERE  ");
		sql.append(" 	 	DOCT.ID_DOCTO_SERVICO = NOTA.ID_CONHECIMENTO ");
		sql.append(" 	AND NOTA.ID_NOTA_FISCAL_CONHECIMENTO = VOLU.ID_NOTA_FISCAL_CONHECIMENTO ");
		sql.append(" 	AND DOCT.ID_DOCTO_SERVICO = PMDO.ID_DOCTO_SERVICO ");
		sql.append(" 	AND MANI.ID_MANIFESTO = PMDO.ID_MANIFESTO ");
		sql.append(" 	AND MANI.ID_CONTROLE_CARGA = :idControleCarga ");
		//VERIFICA SE O VOLUME ESTÁ NA FILIAL DE ORIGEM DO MANIFESTO
		sql.append(" 	AND MANI.ID_FILIAL_ORIGEM = VOLU.ID_LOCALIZACAO_FILIAL ");
		sql.append(" 	AND VOLU.ID_LOCALIZACAO_FILIAL = :idFilial ");
		//VERIFICA SE O VOLUME NÃO FOI ENTREGUE
		sql.append(" 	AND VOLU.ID_LOCALIZACAO_MERCADORIA = LOME.ID_LOCALIZACAO_MERCADORIA ");
		sql.append(" 	AND LOME.CD_LOCALIZACAO_MERCADORIA <> :cdLocalizacaoMercadoria ");
		sql.append(" 	AND VOLU.TP_VOLUME <> 'M' ");
		// VERIFICA SE VOLUME NÃO ESTÁ NO PRÉ-MANIFESTO DO DOCUMENTO DE SERVIÇO
		sql.append(" 	AND NOT EXISTS( ");
		sql.append("	SELECT  ");
		sql.append(" 		1 ");
		sql.append(" 	FROM  ");
		sql.append(" 		MANIFESTO IMANI, ");
		sql.append(" 		PRE_MANIFESTO_VOLUME IPMVO, ");
		sql.append(" 		VOLUME_NOTA_FISCAL IVOLU ");
		sql.append(" 	WHERE  ");
		sql.append(" 			IVOLU.ID_VOLUME_NOTA_FISCAL = IPMVO.ID_VOLUME_NOTA_FISCAL ");
		sql.append(" 		AND IMANI.ID_MANIFESTO = IPMVO.ID_MANIFESTO ");
		sql.append(" 		AND IMANI.ID_MANIFESTO = MANI.ID_MANIFESTO ");
		sql.append(" 		AND IVOLU.ID_VOLUME_NOTA_FISCAL = VOLU.ID_VOLUME_NOTA_FISCAL ");
		sql.append(" 	)  ");
		// VERIFICA SE NÃO EXISTE RNC DE FALTA ABERTA PARA O DOCTO_SERVICO
		sql.append(" 	AND NOT EXISTS( ");
		sql.append(" 	SELECT  ");
		sql.append("  		1 ");
		sql.append(" 	FROM ");
		sql.append(" 		NAO_CONFORMIDADE NCON, ");
		sql.append("  		OCORRENCIA_NAO_CONFORMIDADE OCNC ");
		sql.append(" 	WHERE ");
		sql.append(" 			NCON.ID_NAO_CONFORMIDADE = OCNC.ID_NAO_CONFORMIDADE ");
		sql.append("  		AND NCON.ID_DOCTO_SERVICO = DOCT.ID_DOCTO_SERVICO ");
		sql.append(" 		AND OCNC.TP_STATUS_OCORRENCIA_NC = 'A' ");
		sql.append(" 		AND OCNC.ID_MOTIVO_ABERTURA_NC IN (:tiposRNC) ");
		sql.append(" 	) ");
		return sql;
	}
	
	private StringBuilder generateDocumentosComVolumesSobraQuery() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT  ");
        sql.append("  docto_servico.id_docto_servico as ID_DOCTO_SERVICO, ");
        sql.append("  nota_fiscal_conhecimento.ID_NOTA_FISCAL_CONHECIMENTO as ID_NOTA_FISCAL_CONHECIMENTO ");
        sql.append(" FROM ");
        sql.append(" carregamento_descarga, ");
        sql.append(" volume_sobra, ");
        sql.append(" volume_nota_fiscal, ");
        sql.append(" nota_fiscal_conhecimento, ");
        sql.append(" docto_servico, ");
        sql.append(" filial ");
        sql.append(" WHERE ");
        sql.append(" carregamento_descarga.tp_operacao = 'D' ");
        sql.append(" AND carregamento_descarga.id_controle_carga = :idControleCarga ");
        sql.append(" AND volume_sobra.id_carregamento_descarga = carregamento_descarga.id_carregamento_descarga ");
        sql.append(" AND volume_sobra.id_volume_nota_fiscal = volume_nota_fiscal.id_volume_nota_fiscal ");
        sql.append(" AND volume_nota_fiscal.id_nota_fiscal_conhecimento = nota_fiscal_conhecimento.id_nota_fiscal_conhecimento ");
        sql.append(" AND nota_fiscal_conhecimento.id_conhecimento = docto_servico.id_docto_servico ");
        sql.append(" AND docto_servico.id_filial_origem = filial.id_filial ");
        sql.append(" AND NOT EXISTS ( " + createSubquery() + "  ) ");
        
        return sql;
      }

      private String createSubquery() {
          StringBuilder sql = new StringBuilder();
          
          sql.append(" SELECT 1 FROM nao_conformidade ncon, ocorrencia_nao_conformidade ocnc ");
          sql.append(" WHERE ncon.id_nao_conformidade = ocnc.id_nao_conformidade ");
          sql.append(" AND ncon.id_docto_servico = nota_fiscal_conhecimento.id_conhecimento ");
          sql.append(" AND ocnc.tp_status_ocorrencia_nc = 'A' ");
          sql.append(" AND ocnc.id_motivo_abertura_nc = 14 ");
            
          return sql.toString();
      }
	
	
	
	
	public List<Map<String, Object>> buscaVolumesNaoManifestados(Long idNotaFiscalConhecimento, Long idFilial, Long idManifesto, List<Long> cdLocalizacaoMercadoria, boolean isDescarga){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = generateVolumesNaoManifestadosQuery();
		if(isDescarga){
			sql = generateVolumesNaoManifestadosNaDescargaQuery();
			params.put("cdLocalizacaoMercadoria", cdLocalizacaoMercadoria);
		}else{
			params.put("cdLocalizacaoMercadoria", cdLocalizacaoMercadoria.get(0));
		}
		ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {	
				sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
				sqlQuery.addScalar("TP_DOCUMENTO_SERVICO", Hibernate.STRING);
				sqlQuery.addScalar("SG_FILIAL", Hibernate.STRING);
				sqlQuery.addScalar("NR_DOCTO_SERVICO", Hibernate.LONG);
				sqlQuery.addScalar("NR_SEQUENCIA", Hibernate.LONG);
				sqlQuery.addScalar("QT_VOLUMES", Hibernate.LONG);
			}
		};
		
		params.put("idNotaFiscalConhecimento", idNotaFiscalConhecimento);
		params.put("idFilial", idFilial);
		params.put("idManifesto", idManifesto);
		
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), params, confSql); 
	}

	private StringBuilder generateVolumesNaoManifestadosQuery() {
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT DS.id_docto_servico, ");
		sql.append("   DS.tp_documento_servico, ");
		sql.append("   F.SG_FILIAL, ");
		sql.append("   DS.nr_docto_servico, ");
		sql.append("  decode(vnf.tp_volume, 'U', VNF.nr_sequencia, vnf.NR_SEQUENCIA_PALETE) as NR_SEQUENCIA, ");
		sql.append("  (select count(vnf.id_volume_nota_fiscal) from volume_nota_fiscal where id_nota_fiscal_conhecimento = :idNotaFiscalConhecimento and tp_volume <> 'M') as qt_volumes ");
		sql.append(" FROM docto_servico ds, ");
		sql.append("   filial f, ");
		sql.append("   volume_nota_fiscal vnf, ");
		sql.append("   nota_fiscal_conhecimento nfc, ");
		sql.append("   localizacao_mercadoria lm ");
		sql.append(" WHERE nfc.id_conhecimento           = ds.id_docto_servico ");
		sql.append(" AND ds.id_filial_origem             = f.id_filial ");
		sql.append(" AND nfc.id_nota_fiscal_conhecimento = :idNotaFiscalConhecimento ");
		sql.append(" AND vnf.id_localizacao_filial       = :idFilial ");
		sql.append(" AND vnf.id_nota_fiscal_conhecimento = nfc.id_nota_fiscal_conhecimento ");
		sql.append(" AND vnf.id_localizacao_mercadoria   = lm.id_localizacao_mercadoria ");
		sql.append(" AND lm.cd_localizacao_mercadoria   <> :cdLocalizacaoMercadoria ");
		sql.append(" AND vnf.TP_VOLUME <> 'M' ");
		sql.append(" AND NOT EXISTS ");
		sql.append(" (SELECT pmv.id_volume_nota_fiscal ");
		sql.append(" FROM pre_manifesto_volume pmv ");
		sql.append(" WHERE pmv.id_volume_nota_fiscal = vnf.id_volume_nota_fiscal ");
		sql.append(" AND pmv.id_manifesto            = :idManifesto) ");
		
		return sql;
	}
	
	private StringBuilder generateVolumesNaoManifestadosNaDescargaQuery() {
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT  DS.id_docto_servico, ");
		sql.append(" DS.tp_documento_servico, ");
		sql.append(" F.SG_FILIAL, ");
		sql.append(" DS.nr_docto_servico, ");
		sql.append(" VNF.nr_sequencia, ");
		sql.append(" NFC.qt_volumes, ");
		sql.append(" vnf.id_volume_nota_fiscal,lm.cd_localizacao_mercadoria ");
		sql.append(" FROM docto_servico ds, ");
		sql.append(" filial f, ");
		sql.append(" volume_nota_fiscal vnf, ");
		sql.append(" nota_fiscal_conhecimento nfc, ");
		sql.append(" localizacao_mercadoria lm ");
		sql.append(" WHERE nfc.id_conhecimento           = ds.id_docto_servico ");
		sql.append(" AND ds.id_filial_origem             = f.id_filial ");
		sql.append(" AND nfc.id_nota_fiscal_conhecimento = :idNotaFiscalConhecimento ");
		sql.append(" AND vnf.id_localizacao_filial       = :idFilial  ");
		sql.append(" AND vnf.id_nota_fiscal_conhecimento = nfc.id_nota_fiscal_conhecimento ");
		sql.append(" AND vnf.id_localizacao_mercadoria   = lm.id_localizacao_mercadoria ");
		sql.append(" AND lm.cd_localizacao_mercadoria   IN (:cdLocalizacaoMercadoria) ");
		sql.append(" AND NOT EXISTS ");
		sql.append(" (SELECT pmv.id_volume_nota_fiscal ");
		sql.append(" FROM pre_manifesto_volume pmv, manifesto m ");
		sql.append(" WHERE pmv.id_volume_nota_fiscal = vnf.id_volume_nota_fiscal ");
		sql.append(" AND pmv.id_manifesto            = m.id_manifesto ");
		sql.append(" AND pmv.id_manifesto            = :idManifesto  ");
		sql.append(" AND m.id_filial_origem          = :idFilial         "); 
		sql.append(" ) ");
		
		return sql;
	}
	
	public List<Map<String, Object>> buscaVolumesNaoManifestadosColeta(Long idNotaFiscalConhecimento, Long idFilial, Long idManifesto, List<Long> cdLocalizacaoMercadoria){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = generateVolumesNaoManifestadosNaDescargaColetaQuery();
		
		ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {	
				sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
				sqlQuery.addScalar("TP_DOCUMENTO_SERVICO", Hibernate.STRING);
				sqlQuery.addScalar("SG_FILIAL", Hibernate.STRING);
				sqlQuery.addScalar("NR_DOCTO_SERVICO", Hibernate.LONG);
				sqlQuery.addScalar("NR_SEQUENCIA", Hibernate.LONG);
				sqlQuery.addScalar("QT_VOLUMES", Hibernate.LONG);
			}
		};
		
		params.put("idNotaFiscalConhecimento", idNotaFiscalConhecimento);
		params.put("cdLocalizacaoMercadoria", cdLocalizacaoMercadoria);
		params.put("idFilial", idFilial);
		params.put("idManifesto", idManifesto);
		
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), params, confSql); 
	}
	
	private StringBuilder generateVolumesNaoManifestadosNaDescargaColetaQuery() {
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT  DS.id_docto_servico, ");
		sql.append(" DS.tp_documento_servico, ");
		sql.append(" F.SG_FILIAL, ");
		sql.append(" DS.nr_docto_servico, ");
		sql.append(" VNF.nr_sequencia, ");
		sql.append(" NFC.qt_volumes, ");
		sql.append(" vnf.id_volume_nota_fiscal,lm.cd_localizacao_mercadoria ");
		sql.append(" FROM docto_servico ds, ");
		sql.append(" filial f, ");
		sql.append(" volume_nota_fiscal vnf, ");
		sql.append(" nota_fiscal_conhecimento nfc, ");
		sql.append(" localizacao_mercadoria lm ");
		sql.append(" WHERE nfc.id_conhecimento           = ds.id_docto_servico ");
		sql.append(" AND ds.id_filial_origem             = f.id_filial ");
		sql.append(" AND nfc.id_nota_fiscal_conhecimento = :idNotaFiscalConhecimento ");
		sql.append(" AND vnf.id_localizacao_filial       = :idFilial  ");
		sql.append(" AND vnf.id_nota_fiscal_conhecimento = nfc.id_nota_fiscal_conhecimento ");
		sql.append(" AND vnf.id_localizacao_mercadoria   = lm.id_localizacao_mercadoria ");
		sql.append(" AND lm.cd_localizacao_mercadoria   IN (:cdLocalizacaoMercadoria) ");
		
		return sql;
	}
	
	
	
	
}