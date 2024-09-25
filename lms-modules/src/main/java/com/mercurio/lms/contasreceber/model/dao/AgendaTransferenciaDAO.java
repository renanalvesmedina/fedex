package com.mercurio.lms.contasreceber.model.dao;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.AgendaTransferencia;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class AgendaTransferenciaDAO extends BaseCrudDao<AgendaTransferencia, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return AgendaTransferencia.class;
	}

	public Object findByIdSpecific(Serializable id) {
		SqlTemplate hql = mountHql((Long) id);

		hql
				.addProjection("new Map(at.id as idAgendaTransferencia, "
						+ "filori.id as filialByIdFilialOrigem_idFilial, "
						+ "filori.sgFilial as filialByIdFilialOrigem_sgFilial, "
						+ "filoripes.nmFantasia as filialByIdFilialOrigem_pessoa_nmFantasia, "
						+ "doc.tpDocumentoServico as devedorDocServFat_doctoServico_tpDocumentoServico, "
						+ "doc.nrDoctoServico as devedorDocServFat_doctoServico_nrDoctoServico, "
						+ "doc.idDoctoServico as devedorDocServFat_doctoServico_idDoctoServico, "
						+ "docfilori.id as devedorDocServFat_doctoServico_filialByIdFilialOrigem_idFilial, "
						+ "docfilori.sgFilial as devedorDocServFat_doctoServico_filialByIdFilialOrigem_sgFilial, "
						+ "dev.id as devedorDocServFat_idDevedorDocServFat, "
						+ "fildes.id as filialByIdFilialDestino_idFilial, "
						+ "fildes.sgFilial as filialByIdFilialDestino_sgFilial, "
						+ "fildespes.nmFantasia as filialByIdFilialDestino_pessoa_nmFantasia, "
						+ "clioripes.tpIdentificacao as devedorDocServFat_cliente_pessoa_tpIdentificacao, "
						+ "clioripes.nrIdentificacao as devedorDocServFat_cliente_pessoa_nrIdentificacao, "
						+ "clioripes.nmPessoa as devedorDocServFat_cliente_pessoa_nmPessoa, "
						+ "clides.id as cliente_idCliente, "
						+ "clidespes.tpIdentificacao as cliente_pessoa_tpIdentificacao, "
						+ "clidespes.nrIdentificacao as cliente_pessoa_nrIdentificacao, "
						+ "clidespes.nrIdentificacao as cliente_pessoa_nrIdentificacaoFormatado, "
						+ "clidespes.nmPessoa as cliente_pessoa_nmPessoa, "
						+ "mot.id as motivoTransferencia_idMotivoTransferencia, "
						+ "at.dtPrevistaTransferencia as dtPrevistaTransferencia, "
						+ "at.tpOrigem as tpOrigem, "
						+ "div.idDivisaoCliente as divisaoCliente_idDivisaoClienteTmp, "
						+ "divDev.dsDivisaoCliente as dsDivisaoCliente, "
						+ "at.obAgendaTransferencia as obAgendaTransferencia)");

		return this.getAdsmHibernateTemplate().find(hql.getSql(),
				hql.getCriteria());
	}

	public Integer getRowCount(Long idFilialOrigem, Long idFilialDestino,
			Long idClienteOrigem, Long idClienteDestino, String tpOrigem,
			Long idMotivo, Long idDevedorDocServFat, String tpDocumentoServico,
			Long idFilialOrigemDocumentoServico) {
		SqlTemplate hql = mountHql(null, idFilialOrigem, idFilialDestino,
				idClienteOrigem, idClienteDestino, tpOrigem, idMotivo,
				idDevedorDocServFat, tpDocumentoServico,
				idFilialOrigemDocumentoServico);

		return this.getAdsmHibernateTemplate().getRowCountForQuery(
				hql.getSql(), hql.getCriteria());
	}

	public ResultSetPage findPaginated(Long idFilialOrigem,
			Long idFilialDestino, Long idClienteOrigem, Long idClienteDestino,
			String tpOrigem, Long idMotivo, Long idDevedorDocServFat,
			String tpDocumentoServico, Long idFilialOrigemDocumentoServico,
			FindDefinition findDef) {
		SqlTemplate hql = mountHql(null, idFilialOrigem, idFilialDestino,
				idClienteOrigem, idClienteDestino, tpOrigem, idMotivo,
				idDevedorDocServFat, tpDocumentoServico,
				idFilialOrigemDocumentoServico);

		hql
				.addProjection("new Map(	 at.id as idAgendaTransferencia, "
						+ "docfilori.sgFilial as sgFilialOrigem, "
						+ "docfilpes.nmFantasia as nmFantasiaFilialOrigem, "
						+ "doc.nrDoctoServico as nrDocumentoServico, "
						+ "doc.tpDocumentoServico as tpDocumentoServico, "
						+ "fildes.sgFilial as sgFilialDestino, "
						+ "fildespes.nmFantasia as nmFantasiaFilialDestino, "
						+ "clioripes.nmPessoa as nmPessoaClienteOrigem, "
						+ "clidespes.nmPessoa as nmPessoaClienteDestino, "
						+ "div.dsDivisaoCliente as dsDivisaoCliente, "
						+ "clioripes.tpIdentificacao as tpIdentificacaoClienteOrigem, "
						+ "clidespes.tpIdentificacao as tpIdentificacaoClienteDestino, "
						+ "clioripes.nrIdentificacao as nrIdentificacaoClienteOrigem, "
						+ "clidespes.nrIdentificacao as nrIdentificacaoClienteDestino)");
		hql.addOrderBy("docfilori.sgFilial");

		hql.addOrderBy("doc.nrDoctoServico");
		return this.getAdsmHibernateTemplate().findPaginated(hql.getSql(),
				findDef.getCurrentPage(), findDef.getPageSize(),
				hql.getCriteria());
	}

	/**
	 * Retorna a lista de Agenda de Transferencia(excetoa atransferência em questão)do devedor informado.
	 * 
	 * @author Mickaël Jalbert 17/03/2006
	 * 
	 * @param Long
	 *            idDevedor
	 * @return List
	 */
	public List findByDevedorDocServFat(Long idDevedor, Long idAgendaTransferencia) {
		SqlTemplate hql = mountHql(null, null, null, null, null, null, null,
				idDevedor, null, null);
		hql.addProjection("at");
		hql.addCriteria("at.id", "<>", idAgendaTransferencia);
		return this.getAdsmHibernateTemplate().find(hql.getSql(),
				hql.getCriteria());
	}
	
	public String findSgFilialOrigemByIdDevedor(Long idDevedorDocServFat, Long idAgendaTransferencia){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("at.filialByIdFilialOrigem.sgFilial");
		hql.addInnerJoin(AgendaTransferencia.class.getName(), "at");
		hql.addCriteria("at.devedorDocServFat.id", "=", idDevedorDocServFat);
		hql.addCriteria("at.id", "<>", idAgendaTransferencia);
		
		List lstSgFilial = this.getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
		
		if (lstSgFilial.size() > 0){
			return (String)lstSgFilial.get(0);
		} else {
			return null;
		}
	}
	
	private SqlTemplate mountHql(Long idAgendaTransferencia) {
		SqlTemplate hql = mountHql(idAgendaTransferencia, null, null, null,
				null, null, null, null, null, null);
		return hql;
	}

	private SqlTemplate mountHql(Long idAgendaTransferencia,
			Long idFilialOrigem, Long idFilialDestino, Long idClienteOrigem,
			Long idClienteDestino, String tpOrigem, Long idMotivo,
			Long idDevedorDocServFat, String tpDocumentoServico,
			Long idFilialOrigemDocumentoServico) {

		SqlTemplate hql = new SqlTemplate();

		hql.addInnerJoin(AgendaTransferencia.class.getName(), "at");
		hql.addInnerJoin("at.cliente", "clides");
		hql.addInnerJoin("clides.pessoa", "clidespes");
		hql.addInnerJoin("at.motivoTransferencia", "mot");
		hql.addInnerJoin("at.devedorDocServFat", "dev");
		hql.addInnerJoin("at.filialByIdFilialOrigem", "filori");
		hql.addInnerJoin("filori.pessoa", "filoripes");
		hql.addInnerJoin("at.filialByIdFilialDestino", "fildes");
		hql.addInnerJoin("fildes.pessoa", "fildespes");
		hql.addInnerJoin("dev.cliente", "cliori");
		hql.addInnerJoin("cliori.pessoa", "clioripes");
		hql.addInnerJoin("dev.doctoServico", "doc");
		hql.addInnerJoin("doc.filialByIdFilialOrigem", "docfilori");
		hql.addInnerJoin("docfilori.pessoa", "docfilpes");
		hql.addLeftOuterJoin("at.divisaoCliente", "div");
		hql.addLeftOuterJoin("dev.divisaoCliente", "divDev");

		hql.addCriteria("at.id", "=", idAgendaTransferencia);
		hql.addCriteria("filori.id", "=", idFilialOrigem);
		hql.addCriteria("fildes.id", "=", idFilialDestino);
		hql.addCriteria("cliori.id", "=", idClienteOrigem);
		hql.addCriteria("clides.id", "=", idClienteDestino);
		hql.addCriteria("at.tpOrigem", "=", tpOrigem);
		hql.addCriteria("mot.id", "=", idMotivo);
		hql.addCriteria("dev.id", "=", idDevedorDocServFat);
		hql.addCriteria("doc.tpDocumentoServico", "=", tpDocumentoServico);
		hql.addCriteria("docfilori.id", "=", idFilialOrigemDocumentoServico);

		return hql;
	}

	/**
	 * Método responsável por buscar AgendaTranferencia de acordo com os filtros
	 * 
	 * @param idFilialOrigem
	 * @param tpOrigem
	 * @return Lista de AgendaTranferencia
	 */
	public List findAgendaTranferencia(Long idFilialOrigem, String tpOrigem) {

		SqlTemplate sql = new SqlTemplate();

		sql.addInnerJoin(AgendaTransferencia.class.getName(), "at");
		sql.addInnerJoin("FETCH at.motivoTransferencia", "mt");
		sql.addInnerJoin("FETCH at.devedorDocServFat", "ddsf");
		sql.addInnerJoin("FETCH at.cliente", "c");
		sql.addInnerJoin("FETCH at.filialByIdFilialOrigem", "fo");
		sql.addInnerJoin("FETCH at.filialByIdFilialDestino", "fd");
		sql.addInnerJoin("FETCH at.devedorDocServFat", "ddsf");
		sql.addInnerJoin("FETCH ddsf.doctoServico", "ds");

		sql.addCriteria("fo.idFilial", "=", idFilialOrigem);
		sql.addCriteria("at.tpOrigem", "=", tpOrigem);
		sql.addCriteria("at.dtPrevistaTransferencia", "<=", JTDateTimeUtils
				.getDataAtual());

		sql.addOrderBy("fd.sgFilial, ds.tpDocumentoServico");

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

}