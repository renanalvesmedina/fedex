package com.mercurio.lms.carregamento.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.ControleCargaConhScan;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ControleCargaConhScanDAO extends BaseCrudDao<ControleCargaConhScan, Long> {

	public Integer findControleCargaConhScanByNrCodigoBarras(Long idDoctoServico, Long idControleCarga, Long idCarregamentoDescarga) {
		StringBuffer sql = new StringBuffer("select count(*) ")
		.append("  from ").append(this.persistentClass.getName()).append(" cccs ")
		.append("		join cccs.conhecimento conh ")
		.append("       join cccs.controleCarga cc ")
		.append("       left join cccs.carregamentoDescarga cd ");
		
		sql.append(" where conh.idDoctoServico = ? ")
		.append(" and cc.idControleCarga = ? ");	
		
		Object[] params = null;

		if (idCarregamentoDescarga != null) {
			sql.append(" and (cd is null or cd.idCarregamentoDescarga = ?) ");
			params = new Object[]{ idDoctoServico, idControleCarga , idCarregamentoDescarga};
		} else {
			sql.append(" and cd is null ");
			params = new Object[]{ idDoctoServico, idControleCarga };
		}

		return ((Long) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), params)).intValue();
	}

	private Map buildFindByControleCargaAndCarregamentoDescargaParameters(Long idControleCarga, Long idCarregamentoDescarga) {
		Map params = new HashMap();
		params.put("idControleCarga", idControleCarga);
		params.put("idCarregamentoDescarga", idCarregamentoDescarga);
		params.put("idFilial", SessionUtils.getFilialSessao().getIdFilial());
		return params;
	}
	
	public ResultSetPage findPaginatedByControleCargaAndCarregamentoDescarga(Long idControleCarga, Long idCarregamentoDescarga, FindDefinition findDef) {
		StringBuffer hql = buildFindByControleCargaAndCarregamentoHQL(idCarregamentoDescarga);
		Map params = buildFindByControleCargaAndCarregamentoDescargaParameters(idControleCarga, idCarregamentoDescarga);
				
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(hql.toString(), findDef.getCurrentPage(), findDef.getPageSize(), params);
		List list = rsp.getList();
		list = AliasToNestedMapResultTransformer.getInstance().transformListResult(list);
		rsp.setList(list);
		return rsp;
	}

	public StringBuffer buildFindByControleCargaAndCarregamentoHQL(Long idCarregamentoDescarga) {
		StringBuffer sql = new StringBuffer()
		.append("select new Map( ") 
		.append("				controleCargaConhScan.idControleCargaConhScan as idControleCargaConhScan, ")
		.append("				conhecimento.idDoctoServico as idDoctoServico, ")
		.append("				conhecimento.nrConhecimento as nrDoctoServico, ")
		.append("				conhecimento.dvConhecimento as dvDoctoServico, ")
		.append("				conhecimento.vlMercadoria as vlMercadoria, ")
		.append("				conhecimento.vlTotalDocServico as vlTotalDocServico, ")
		.append("				conhecimento.dtPrevEntrega as dtPrevEntrega, ")
		.append("				conhecimento.qtVolumes as qtVolumes, ")
		.append("				conhecimento.psReal as psReal, ")
		.append("				conhecimento.tpDocumentoServico as tpDocumento, ")
		.append("				(select dsi.dsRotaIroad from DoctoServicoIroad dsi where dsi.doctoServico.id = conhecimento.id)  as dsRotaIroad, ")
		.append("				moeda.sgMoeda as sgMoeda, ")
		.append("				moeda.dsSimbolo as dsSimbolo, ")
		.append("				moeda.sgMoeda as sgMoedaFrete, ")
		.append("				moeda.dsSimbolo as dsSimboloFrete, ")
		.append("				servico.sgServico as sgServico, ")
		.append("				filialOrigem.sgFilial as sgFilialOrigem, ")
		.append("				filialDestino.sgFilial as sgFilialDestino, ")
		.append("				pessoaFilialDestino.nmFantasia as nmFantasiaFilialDestino, ")
		.append("				pessoaClienteRemetente.nmPessoa as clienteRemetente, ")
		.append(" (case when ( ").append(getSqlFindManifestoByControleCargaAndDoctoServico()).append(" ) is NULL  then 'Documento não manifestado'  else '' end) as situacaoDocumento, ")
		.append("				pessoaClienteDestinatario.nmPessoa as clienteDestinatario ")
		.append("			) ")
		.append(getSqlFindPaginatedByControleCarga(idCarregamentoDescarga));
		return sql;
	}
	
	private Object getSqlFindManifestoByControleCargaAndDoctoServico() {
		return new StringBuffer()
		.append(" select manifesto.idManifesto  from ")
		.append(Conhecimento.class.getName())
		.append(" conhecimento2  ")
		.append(" left join conhecimento2.preManifestoDocumentos preManifesto ")
		.append(" left join preManifesto.manifesto manifesto ")
		.append(" left join manifesto.controleCarga controleCargaManifesto ")
		.append(" where controleCargaManifesto.idControleCarga = controleCarga.idControleCarga ")
		.append("and conhecimento2.idDoctoServico = conhecimento.idDoctoServico ")
		.append(" and ROWNUM = 1 ")
		.toString();
	}

	public Integer getRowCountByControleCargaAndCarregamentoDescarga(Long idControleCarga, Long idCarregamentoDescarga) {
		StringBuffer sql = new StringBuffer("select count(*) as rowcount ")
		.append(getSqlFindPaginatedByControleCarga(idCarregamentoDescarga));
		
		Map params = buildFindByControleCargaAndCarregamentoDescargaParameters(idControleCarga, idCarregamentoDescarga);
		
		
    	Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), params);
    	return result.intValue();
	}
	
	private StringBuffer getSqlFindPaginatedByControleCarga(Long idCarregamentoDescarga) {
		StringBuffer sql = new StringBuffer("  from ")
		.append(ControleCargaConhScan.class.getName())
		.append(" controleCargaConhScan ")
		
		.append("		join controleCargaConhScan.conhecimento conhecimento ")
		.append("		join controleCargaConhScan.controleCarga controleCarga ")
		.append("		left join controleCargaConhScan.carregamentoDescarga carregamentoDescarga ") // alteração left
		.append("		join conhecimento.servico servico ")
  		.append("		left join conhecimento.moeda moeda ")
  		.append("		left join conhecimento.filialByIdFilialOrigem filialOrigem ")
  		.append("		left join conhecimento.filialByIdFilialDestino filialDestino ")
  		.append("		left join filialDestino.pessoa pessoaFilialDestino ")
  		.append("		left join conhecimento.clienteByIdClienteRemetente cliRem ")
		.append("		left join conhecimento.clienteByIdClienteDestinatario cliDes ")
		.append("		left join cliRem.pessoa pessoaClienteRemetente ")
  		.append("		left join cliDes.pessoa pessoaClienteDestinatario ")
  		.append(" where controleCarga.idControleCarga = :idControleCarga ")
		.append(idCarregamentoDescarga != null?" and (carregamentoDescarga.idCarregamentoDescarga = :idCarregamentoDescarga or carregamentoDescarga.idCarregamentoDescarga is null) ":
				" and controleCargaConhScan.carregamentoDescarga is null "); 
		
		return sql;
	}
	
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ControleCargaConhScan.class;
	}
	
	public Long getRowCountDocCarregadoConferido(Long idControleCarga, Long idCarregamentoDescarga) {
		StringBuffer sql = new StringBuffer(" select count(con.id) from ")
		.append(Conhecimento.class.getName())
		.append(" con ")
		.append("	left join con.preManifestoDocumentos pmd ")
		.append("	left join pmd.manifesto man ")
		.append("	left join man.controleCarga cc_carg ")
		.append(" where cc_carg.id = ?")
		.append(" 	and con.id in(")
		.append("		select con2.id from ")
		.append(Conhecimento.class.getName())
		.append(" 			con2 ")
		.append("			left join con2.controleCargaConhScans cccs ")
		.append("			left join cccs.controleCarga cc_conf ")
		.append("			left join cccs.carregamentoDescarga carregamentoDescarga ")
		.append(" 		where cc_conf.id = ?")
		.append("		and carregamentoDescarga.id = ? )");
		return (Long) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idControleCarga, idControleCarga, idCarregamentoDescarga});
}
	
	public Long getRowCountDocCarregadoNaoConferido(Long idControleCarga, Long idCarregamentoDescarga) {
		StringBuffer sql = new StringBuffer(" select count(con.id) from ")
		.append(Conhecimento.class.getName())
		.append(" con ")
		.append("	left join con.preManifestoDocumentos pmd ")
		.append("	left join pmd.manifesto man ")
		.append("	left join man.controleCarga cc_carg ")
		.append(" where cc_carg.id = ?")
		.append(" 	and con.id not in(")
		.append("		select con2.id from ")
		.append(Conhecimento.class.getName())
		.append(" 			con2 ")
		.append("			left join con2.controleCargaConhScans cccs ")
		.append("			left join cccs.controleCarga cc_conf ")
		.append("			left join cccs.carregamentoDescarga carregamentoDescarga ")
		.append(" 		where cc_conf.id = ? ")
		.append("		and carregamentoDescarga.id = ? )");
				
		return (Long) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idControleCarga, idControleCarga, idCarregamentoDescarga});
	}
	
	public Long getRowCountDocNaoCarregadoConferido(Long idControleCarga, Long idCarregamentoDescarga) {
		StringBuffer sql = new StringBuffer(" select count(con.id)  from ")
		.append(Conhecimento.class.getName())
		.append(" con ")
		.append("	left join con.controleCargaConhScans cccs ")
		.append("	left join cccs.controleCarga cc_conf ")
		.append(" where cc_conf.id = ? ")
		.append(" and cccs.carregamentoDescarga.id = ? ")
		.append(" 	and con.id not in(")
		.append("		select con2.id from ")
		.append(Conhecimento.class.getName())
		.append(" 			con2 ")
		.append("			left join con2.preManifestoDocumentos pmd ")
		.append("			left join pmd.manifesto man ")
		.append("			left join man.controleCarga cc_carg ")
		.append(" 		where cc_carg.id = ?)");
				
		return (Long) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idControleCarga, idCarregamentoDescarga, idControleCarga});
	}

	
	public ControleCargaConhScan findControleCargaConhScan(Long nrCodigoBarras, Long idControleCarga, Long idCarregamentoDescarga) {
		DetachedCriteria dc = DetachedCriteria.forClass(ControleCargaConhScan.class, "controleCargaConhScan");
		dc.setFetchMode("controleCargaConhScan.conhecimento", FetchMode.JOIN);
		dc.createAlias("controleCargaConhScan.conhecimento", "conhecimento");
		dc.setFetchMode("controleCargaConhScan.controleCarga", FetchMode.JOIN);
		dc.createAlias("controleCargaConhScan.controleCarga", "cc");
		dc.setFetchMode("controleCargaConhScan.carregamentoDescarga", FetchMode.JOIN);
		dc.createAlias("controleCargaConhScan.carregamentoDescarga", "cd");
		dc.add(Restrictions.eq("cd.id", idCarregamentoDescarga));
		dc.add(Restrictions.eq("cc.id", idControleCarga));
		dc.add(Restrictions.eq("conhecimento.nrCodigoBarras", nrCodigoBarras));

		return (ControleCargaConhScan) getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	public List<ControleCargaConhScan> findByCarregamentoDescarga(Long idCarregamentoDescarga) {
		StringBuilder hql = new StringBuilder()
		.append("from " + getPersistentClass().getName() + " controleCargaConhScan ")
		.append("inner join fetch controleCargaConhScan.carregamentoDescarga carregamentoDescarga ")
		.append(" where carregamentoDescarga.id = ? ");
				
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idCarregamentoDescarga});
	}

	/**
	 * Busca ControleCargaConhScan pelo idControleCarga, sendo o CarregamentoDescarga null
	 * @param idControleCarga
	 * @return ControleCargaConhScan
	 */
	public List<ControleCargaConhScan> findByIdControleCarga(Long idControleCarga) {
		DetachedCriteria dc = DetachedCriteria.forClass(ControleCargaConhScan.class, "controleCargaConhScan");
		dc.add(Restrictions.eq("controleCargaConhScan.controleCarga.id", idControleCarga));
		return (List<ControleCargaConhScan>) getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
}