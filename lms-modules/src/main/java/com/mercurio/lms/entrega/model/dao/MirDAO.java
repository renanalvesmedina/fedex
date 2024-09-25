package com.mercurio.lms.entrega.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.model.DocumentoMir;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.Mir;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.ReciboReembolso;
import com.mercurio.lms.entrega.model.RegistroDocumentoEntrega;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MirDAO extends BaseCrudDao<Mir, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Mir.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("usuarioByIdUsuarioCriacao",FetchMode.JOIN);
		lazyFindById.put("usuarioByIdUsuarioRecebimento",FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialOrigem",FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialOrigem.pessoa",FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialDestino",FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialDestino.pessoa",FetchMode.JOIN);
	}

	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("filialByIdFilialOrigem",FetchMode.JOIN);
		lazyFindLookup.put("filialByIdFilialOrigem.pessoa",FetchMode.JOIN);
		lazyFindLookup.put("filialByIdFilialDestino",FetchMode.JOIN);
		lazyFindLookup.put("filialByIdFilialDestino.pessoa",FetchMode.JOIN);
	}

	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria, FindDefinition fDef) {
		SqlTemplate hql = this.getSqlTemplate(criteria);
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(),fDef.getCurrentPage(),fDef.getPageSize(),hql.getCriteria());
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		SqlTemplate hql = this.getSqlTemplate(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria());
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("MIR");

		StringBuffer hqlFrom = new StringBuffer()
				.append(getPersistentClass().getName()).append(" as MIR ")				
				.append(" inner join fetch MIR.filialByIdFilialOrigem as FO ")
				.append(" inner join fetch MIR.filialByIdFilialDestino as FD ")
				.append(" inner join fetch FO.pessoa as PO ")
				.append(" inner join fetch FD.pessoa as PD ")
				.append("  left join fetch MIR.usuarioByIdUsuarioCriacao as UC ")
				.append("  left join fetch MIR.usuarioByIdUsuarioRecebimento as UR ");

		hql.addFrom(hqlFrom.toString());

		hql.addCriteria("FO.idFilial","=", criteria.getLong("filialByIdFilialOrigem.idFilial"));
		hql.addCriteria("FD.idFilial","=", criteria.getLong("filialByIdFilialDestino.idFilial"));

		hql.addCriteria("MIR.nrMir","=",criteria.getInteger("nrMir"));
		hql.addCriteria("MIR.tpMir","=",criteria.getString("tpMir"));

		String tpDocumentoMir = criteria.getString("tpDocumentoMir");
		if (StringUtils.isNotBlank(tpDocumentoMir)) {
			hql.addCriteria("MIR.tpDocumentoMir","=",tpDocumentoMir);
			if (tpDocumentoMir.equals("R")) {
				Long idReciboReembolso = criteria.getLong("reciboReembolso.idDoctoServico");
				if (idReciboReembolso != null) {
					hql.addCustomCriteria("exists( select DocMir.id from "+DocumentoMir.class.getName() + " DocMir " +
							" where DocMir.mir.id = MIR.id " +
							"   and DocMir.reciboReembolso.id = ?)",idReciboReembolso);
				}
			} 
		}		  

		hql.addCriteria("trunc(cast(MIR.dhEmissao.value as date))",">=",criteria.getYearMonthDay("dhEmisssaoInicial"));
		hql.addCriteria("trunc(cast(MIR.dhEmissao.value as date))","<=",criteria.getYearMonthDay("dhEmisssaoFinal"));

		hql.addCriteria("trunc(cast(MIR.dhEnvio.value as date))",">=",criteria.getYearMonthDay("dhEnvioInicial"));
		hql.addCriteria("trunc(cast(MIR.dhEnvio.value as date))","<=",criteria.getYearMonthDay("dhEnvioFinal"));

		hql.addCriteria("trunc(cast(MIR.dhRecebimento.value as date))",">=",criteria.getYearMonthDay("dhRecebimentoInicial"));
		hql.addCriteria("trunc(cast(MIR.dhRecebimento.value as date))","<=",criteria.getYearMonthDay("dhRecebimentoFinal"));

		hql.addCriteria("UC.idUsuario","=",criteria.get("usuarioByIdUsuarioCriacao.idUsuario"));
		hql.addCriteria("UR.idUsuario","=",criteria.get("usuarioByIdUsuarioRecebimento.idUsuario"));

		hql.addOrderBy("FO.sgFilial");
		hql.addOrderBy("MIR.nrMir");

		return hql;
	} 

	/**
	 * @see com.mercurio.lms.entrega.model.service.MirService.findRegistroEntregaRecolhidoByDocto(Long idDoctoServico)
	 * @param idDoctoServico
	 * @return List
	 */
	public List findRegistroEntregaRecolhidoByDocto(Long idDoctoServico) {
		DetachedCriteria dc = DetachedCriteria.forClass(RegistroDocumentoEntrega.class,"RDE");

		dc.createAlias("RDE.doctoServico","DS");

		dc.add(Restrictions.eq("RDE.tpSituacaoRegistro","RR"));
		dc.add(Restrictions.eq("DS.id",idDoctoServico));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	/**
	 * Método responsável por salvar um MIR a partir do comprtamento DF2 da tela 'Manter MIR'.
	 * @param rotaViagem
	 * @param items
	 * @return Mir entidade que foi armazenada.
	 */
	public Mir storeWithItems(Mir mir, ItemList items) {
		super.store(mir);

		getAdsmHibernateTemplate().deleteAll(items.getRemovedItems());
		getAdsmHibernateTemplate().flush();

		getAdsmHibernateTemplate().saveOrUpdateAll(items.getNewOrModifiedItems());
		getAdsmHibernateTemplate().flush();

		return mir;
	}

	public List findDocsByMir(Long idMir, String tpDocumentoMir) {
		if (idMir == null)
			return new ArrayList(0);

		if (tpDocumentoMir.equals("R"))
			return this.findDocsByMirReembolos(idMir);
		else
			return this.findDocsByMirCoprovantes(idMir);
	}

	public List findDocsByMirReembolos(Long idMir) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("DocMir");

		StringBuffer hqlFrom = new StringBuffer()
		.append(DocumentoMir.class.getName() + " as DocMir ")
		.append(" inner join fetch DocMir.reciboReembolso as RR")
		.append(" inner join fetch RR.filial as F")
		.append(" inner join fetch F.pessoa")
		.append(" inner join fetch RR.clienteByIdClienteRemetente as CR")
		.append(" inner join fetch CR.pessoa as PR")
		.append("  left join fetch RR.clienteByIdClienteDestinatario as CD")
		.append("  left join fetch CD.pessoa as PD")
		.append(" inner join fetch RR.doctoServicoByIdDoctoServReembolsado as DR")
		.append(" inner join fetch DR.filialByIdFilialOrigem as FR")
		.append(" inner join fetch RR.moeda as M");

		hql.addFrom(hqlFrom.toString());

		hql.addCriteria("DocMir.mir.id","=",idMir);

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public List findDocsByMirCoprovantes(Long idMir) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("DocMir");

		StringBuffer hqlFrom = new StringBuffer()
		.append(DocumentoMir.class.getName() + " as DocMir ")
		.append(" inner join fetch DocMir.registroDocumentoEntrega as RDE")
		.append(" inner join fetch RDE.doctoServico as DS")
		.append(" inner join fetch DS.filialByIdFilialOrigem as F")
		.append(" inner join fetch F.pessoa")
		.append(" inner join fetch DS.clienteByIdClienteRemetente as CR")
		.append(" inner join fetch CR.pessoa as PR")
		.append("  left join fetch DS.clienteByIdClienteDestinatario as CD")
		.append("  left join fetch CD.pessoa as PD")
		.append(" inner join fetch RDE.tipoDocumentoEntrega as TDE");

		hql.addFrom(hqlFrom.toString());

		hql.addCriteria("DocMir.mir.id","=",idMir);

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public Integer getRowCountDocsByMir(Long idMir, String tpDocumentoMir) {
		if (tpDocumentoMir.equals("R"))
			return this.getRowCountDocsByMirReembolsos(idMir);
		else
			return this.getRowCountDocsByMirComprovantes(idMir);
	}

	public Integer getRowCountDocsByMirReembolsos(Long idMir) {
		DetachedCriteria dc = DetachedCriteria.forClass(DocumentoMir.class,"DocMir");
		dc.setProjection(Projections.count("DocMir.id"));
		dc.add(Restrictions.isNotNull("DocMir.reciboReembolso.id"));
		dc.add(Restrictions.eq("DocMir.mir.id",idMir));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	public Integer getRowCountDocsByMirComprovantes(Long idMir) {
		DetachedCriteria dc = DetachedCriteria.forClass(DocumentoMir.class,"DocMir");
		dc.setProjection(Projections.count("DocMir.id"));
		dc.add(Restrictions.isNotNull("DocMir.registroDocumentoEntrega.id"));
		dc.add(Restrictions.eq("DocMir.mir.id",idMir));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	public List findComprovantesPendentes(String tpMir, 
			Long idClienteRemetente,
			Long idClienteDestinatario,
			Long idFilialOrigem,
			Long idFilialDestino) {
		SqlTemplate hql = this.getSqlTemplateComprovantesPendentes(tpMir,idClienteRemetente,
				idClienteDestinatario,idFilialOrigem,idFilialDestino);

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public List findReembolsosPendentes(String tpMir, 
			Long idClienteRemetente,
			Long idClienteDestinatario,
			Long idFilialOrigem,
			Long idFilialDestino) {
		SqlTemplate hql = this.getSqlTemplateReembolsosPendentes(tpMir,idClienteRemetente,
				idClienteDestinatario,idFilialOrigem,idFilialDestino);

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	private SqlTemplate getSqlTemplateComprovantesPendentes(String tpMir, 
			Long idClienteRemetente,
			Long idClienteDestinatario,
			Long idFilialOrigem,
			Long idFilialDestino) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("RDE");

		StringBuffer hqlFrom = new StringBuffer()
		.append(RegistroDocumentoEntrega.class.getName() + "  as RDE ")
		.append(" inner join fetch RDE.doctoServico as DS ")
		.append(" inner join fetch DS.filialByIdFilialOrigem as F ")
		.append(" inner join fetch F.pessoa")
		.append(" inner join fetch DS.clienteByIdClienteRemetente as CR ")
		.append(" inner join fetch CR.pessoa as PR ")
		.append("  left join fetch DS.clienteByIdClienteDestinatario as CD ")
		.append("  left join fetch CD.pessoa as PD ")
		.append(" inner join fetch RDE.tipoDocumentoEntrega as TD ");

		hql.addCriteria("CR.id","=",idClienteRemetente);

		if (idClienteDestinatario != null) {
			hqlFrom.append(" inner join fetch DS.clienteByIdClienteDestinatario as CD ")
					.append("  inner join fetch CD.pessoa as PD ");
			hql.addCriteria("CD.id","=",idClienteDestinatario);
		} else {
			hqlFrom.append("  left join fetch DS.clienteByIdClienteDestinatario as CD ")
					.append("  left join fetch CD.pessoa as PD ");
		}

		// Entrega para Administrativo
		if (tpMir.equals("EA")) {

			hql.addCustomCriteria("not exists(select DocMir.id from " + 
					DocumentoMir.class.getName() + " as DocMir," +
					Mir.class.getName() + " as MIR " +
					" where DocMir.registroDocumentoEntrega.id = RDE.id " +
					"	and MIR.id = DocMir.mir.id " +
					"	and MIR.tpMir = ? " +
					" )", tpMir);

			hql.addCustomCriteria("exists (" +
					" select MED.id from " + ManifestoEntregaDocumento.class.getName() + " as MED, " +
											 OcorrenciaEntrega.class.getName() + " as OCOR, " +
											 ManifestoEntrega.class.getName() + " as ME " +
					" where MED.ocorrenciaEntrega.id = OCOR.id " +
					"	and MED.manifestoEntrega.id = ME.id " +
					"	and OCOR.tpOcorrencia = ? " +
					"	and MED.doctoServico.id = DS.id " +
					"	and ME.filial.id = ? " +
					")",new Object[]{"E",idFilialDestino});
			
			hql.addCriteria("RDE.tpSituacaoRegistro","=","RR");

		// Destino para Origem
		} else if (tpMir.equals("DO")) {

			hql.addCustomCriteria("not exists(select DocMir.id from " + 
					DocumentoMir.class.getName() + " as DocMir," +
					Mir.class.getName() + " as MIR " +
					" where DocMir.registroDocumentoEntrega.id = RDE.id " +
					"	and MIR.id = DocMir.mir.id " +
					"	and MIR.tpMir = ?" +
					" )", new Object[]{tpMir});

			hql.addCustomCriteria("exists(select DocMir.id from " + 
					DocumentoMir.class.getName() + " as DocMir," +
					Mir.class.getName() + " as MIR " +
					" where DocMir.registroDocumentoEntrega.id = RDE.id " +
					"	and MIR.id = DocMir.mir.id " +
					"	and MIR.tpMir = ? and MIR.dhRecebimento.value is not null" +
					" )", new Object[]{"EA"});

			hql.addCustomCriteria("DS.filialByIdFilialOrigem.id <> DS.filialByIdFilialDestino.id");
			hql.addCustomCriteria("DS.filialDestinoOperacional.id = ?",idFilialOrigem);
			hql.addCustomCriteria("DS.filialByIdFilialOrigem.id = ?",idFilialDestino);

		// Administrativo para Entrega
		} else if (tpMir.equals("AE")) {

			hql.addCustomCriteria("not exists(select DocMir.id from " + 
					DocumentoMir.class.getName() + " as DocMir," +
					Mir.class.getName() + " as MIR " +
					" where DocMir.registroDocumentoEntrega.id = RDE.id " +
					"	and MIR.id = DocMir.mir.id " +
					"	and MIR.tpMir = ?" +
					" )", new Object[]{tpMir});

			hql.addCustomCriteria("exists(select DocMir.id from " + 
					DocumentoMir.class.getName() + " as DocMir," +
					Mir.class.getName() + " as MIR " +
					" where DocMir.registroDocumentoEntrega.id = RDE.id " +
					"	and MIR.id = DocMir.mir.id " +
					"	and MIR.tpMir = ? and MIR.dhRecebimento.value is not null" +
					" )", new Object[]{"EA"});

			hql.addCustomCriteria("(DS.filialByIdFilialOrigem.id = DS.filialByIdFilialDestino.id " +
					"or exists(select DocMir.id from " + 
					DocumentoMir.class.getName() + " as DocMir," +
					Mir.class.getName() + " as MIR " +
					" where DocMir.registroDocumentoEntrega.id = RDE.id " +
					"	and MIR.id = DocMir.mir.id " +
					"	and MIR.tpMir = ? and MIR.dhRecebimento.value is not null" +
					" ))", new Object[]{"DO"});

			hql.addCriteria("DS.filialByIdFilialOrigem.id","=",idFilialOrigem);
		}

		hql.addFrom(hqlFrom.toString());

		return hql;
	}

	private SqlTemplate getSqlTemplateReembolsosPendentes(String tpMir, 
			Long idClienteRemetente,
			Long idClienteDestinatario,
			Long idFilialOrigem,
			Long idFilialDestino
	) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("RR");

		StringBuffer hqlFrom = new StringBuffer()
			.append(ReciboReembolso.class.getName() + " as RR ")
			.append(" inner join fetch RR.filial as F")
			.append(" inner join fetch F.pessoa")
			.append(" inner join fetch RR.clienteByIdClienteRemetente as CR ")
			.append(" inner join fetch CR.pessoa as PR ")
			.append(" inner join fetch RR.doctoServicoByIdDoctoServReembolsado as DR ")
			.append(" inner join fetch DR.filialByIdFilialOrigem as FR ")
			.append(" inner join fetch RR.moeda as M");

		hql.addCriteria("CR.id","=",idClienteRemetente);

		if (idClienteDestinatario != null) {
			hqlFrom.append(" inner join fetch RR.clienteByIdClienteDestinatario as CD ")
					.append(" inner join fetch CD.pessoa as PD ");
			hql.addCriteria("CD.id","=",idClienteDestinatario);
		} else {
			hqlFrom.append(" left join fetch RR.clienteByIdClienteDestinatario as CD ")
					.append(" left join fetch CD.pessoa as PD ");
		}

		// Entrega para Administrativo
		if (tpMir.equals("EA")) {
			hql.addCustomCriteria("RR.tpSituacaoRecibo in (?,?)");
			hql.addCriteriaValue("CR");
			hql.addCriteriaValue("CD");

			hql.addCustomCriteria("not exists(select DocMir.id from " + 
					DocumentoMir.class.getName() + " as DocMir" +
					" where DocMir.reciboReembolso.id = RR.id "+
					" )");

			hql.addCustomCriteria("exists (" +
					" select MED.id from " + ManifestoEntregaDocumento.class.getName() + " as MED, " +
											 OcorrenciaEntrega.class.getName() + " as OCOR, " +
											 ManifestoEntrega.class.getName() + " as ME " +
					" where MED.ocorrenciaEntrega.id = OCOR.id " +
					"	and MED.manifestoEntrega.id = ME.id " +
					"	and OCOR.tpOcorrencia = ? " +
					"	and MED.doctoServico.id = RR.doctoServicoByIdDoctoServReembolsado.id " +
					"	and ME.filial.id = ? " +
					")",new Object[]{"E",idFilialDestino});

		// Destino para Origem
		} else if (tpMir.equals("DO")) {

			hql.addCriteria("RR.tpSituacaoRecibo","=","CD");
			hql.addCriteria("RR.filialByIdFilialOrigem.id","=",idFilialDestino);
			hql.addCriteria("RR.filialDestinoOperacional.id","=",idFilialOrigem);
			hql.addCustomCriteria("RR.filialByIdFilialOrigem.id <> RR.filialByIdFilialDestino.id");

			hql.addCustomCriteria("not exists(select DocMir.id from " + 
					DocumentoMir.class.getName() + " as DocMir," +
					Mir.class.getName() + " as MIR " +
					" where DocMir.reciboReembolso.id = RR.id " +
					"	and MIR.id = DocMir.mir.id " +
					"	and MIR.tpMir = ?" +
					" )", new Object[]{tpMir});

			hql.addCustomCriteria("exists(select DocMir.id from " + 
					DocumentoMir.class.getName() + " as DocMir," +
					Mir.class.getName() + " as MIR " +
					" where DocMir.reciboReembolso.id = RR.id " +
					"	and MIR.id = DocMir.mir.id " +
					"	and MIR.tpMir = ? and MIR.dhRecebimento.value is not null" +
					" )", new Object[]{"EA"});

		// Administrativo para Entrega
		} else if (tpMir.equals("AE")) {
			hql.addCriteria("RR.tpSituacaoRecibo","=","CD");
			hql.addCriteria("RR.filialByIdFilialOrigem.id","=",idFilialOrigem);

			hql.addCustomCriteria("(RR.filialByIdFilialOrigem.id = RR.filialByIdFilialDestino.id " +
					"or exists(select DocMir.id from " + 
					DocumentoMir.class.getName() + " as DocMir," +
					Mir.class.getName() + " as MIR " +
					" where DocMir.reciboReembolso.id = RR.id " +
					"	and MIR.id = DocMir.mir.id " +
					"	and MIR.tpMir = ? and MIR.dhRecebimento.value is not null" +
					"))","DO");

			hql.addCustomCriteria("exists(select DocMir.id from " + 
					DocumentoMir.class.getName() + " as DocMir," +
					Mir.class.getName() + " as MIR " +
					" where DocMir.reciboReembolso.id = RR.id " +
					"	and MIR.id = DocMir.mir.id " +
					"	and MIR.tpMir = ? and MIR.dhRecebimento.value is not null" +
					" )", new Object[]{"EA"});

			hql.addCustomCriteria("not exists(select DocMir.id from " + 
					DocumentoMir.class.getName() + " as DocMir," +
					Mir.class.getName() + " as MIR " +
					" where DocMir.reciboReembolso.id = RR.id " +
					"	and MIR.id = DocMir.mir.id " +
					"	and MIR.tpMir = ?" +
					" )", new Object[]{tpMir});
		}

		hql.addFrom(hqlFrom.toString());

		return hql;
	}

	/**
	 * Retorna MIR para os parâmetros informados
	 * @param nrMir
	 * @param idFilialOrigem
	 * @return MIR
	 */
	public Mir findMir(Integer nrMir, Long idFilialOrigem) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.add(Restrictions.eq("nrMir",nrMir));
		dc.add(Restrictions.eq("filialByIdFilialOrigem.id",idFilialOrigem));
		return (Mir) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

}