package com.mercurio.lms.sim.model.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.model.AgendamentoDoctoServico;
import com.mercurio.lms.entrega.model.AgendamentoEntrega;
import com.mercurio.lms.entrega.model.ChequeReembolso;
import com.mercurio.lms.entrega.model.ReciboReembolso;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DadosComplemento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.model.ServicoEmbalagem;
/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LMComplementoDAO extends AdsmDao {
	
	public List findPaginatedComplEmbalagens(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map("+PropertyVarcharI18nProjection.createProjection("emb.dsEmbalagem")+" as dsEmbalagem, " +
				"emb.nrAltura as nrAltura, " +
				"emb.nrLargura as nrLargura, " +
				"emb.nrComprimento as nrComprimento, " +
				"se.nrQuantidade as nrQuantidade, " +
				"se.idServicoEmbalagem as idServicoEmbalagem)");
		
		hql.addFrom(ServicoEmbalagem.class.getName()+" se " +
				"join se.embalagem emb " +
				"join se.doctoServico ds ");
		
		hql.addRequiredCriteria("ds.idDoctoServico","=",idDoctoServico);
		
		hql.addOrderBy(OrderVarcharI18n.hqlOrder("emb.dsEmbalagem",LocaleContextHolder.getLocale()));
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	public boolean findEmbalagensAba(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("1");
		hql.addFrom(ServicoEmbalagem.class.getName()+" se " +
				"join se.doctoServico ds ");
		
		hql.addCriteria("ds.idDoctoServico","=",idDoctoServico);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria()).size()>0;
	}
	
	public List findPaginatedComplObservacoes(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(ods.dsObservacaoDoctoServico as dsObservacaoDoctoServico, " +
				"ods.idObservacaoDoctoServico as idObservacaoDoctoServico)");
		
		hql.addFrom(ObservacaoDoctoServico.class.getName()+" ods " +
					"join ods.doctoServico ds ");
		
		hql.addRequiredCriteria("ds.idDoctoServico","=",idDoctoServico);
		
		hql.addOrderBy("ods.blPrioridade","desc");
		hql.addOrderBy("ods.dsObservacaoDoctoServico","asc");
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	public boolean findReembolsoAba(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("1");
		hql.addFrom(ReciboReembolso.class.getName()+ " rr " +
				"left outer join rr.doctoServicoByIdDoctoServReembolsado dsr " );
		
		hql.addCriteria("dsr.idDoctoServico","=",idDoctoServico);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria()).size()>0;
	}
	
	public List findReembolsoByIdReembolsado(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(rr.nrReciboReembolso as nrReciboReembolso, " +
				"filOr.sgFilial as sgFilialReemb, " +
				"filOr.idFilial as idFilialReemb, " +
				"pesOr.nmFantasia as nmFantasiaFilReemb, " +
				"rr.dhEmissao as dhEmissao, " +
				"rr.vlReembolso as vlReembolso, " +
				"moeda.dsSimbolo as dsSimbolo, " +
				"rr.vlAplicado as vlAplicado, " +
				"pesRem.tpIdentificacao as tpIdentificacaoRem, " +
				"pesRem.nrIdentificacao as nrIdentificacaoRem, " +
				"pesRem.nmPessoa as nmPessoaRem, " +
				"pesDest.tpIdentificacao as  tpIdentificacaoDest, " +
				"pesDest.nrIdentificacao as nrIdentificacaoDest, " +
				"pesDest.nmPessoa as nmPessoaDest, " +
				"rr.idDoctoServico as idDoctoServicoReemb)");
		
		hql.addFrom(ReciboReembolso.class.getName()+ " rr " +
				"join rr.moeda moeda " +
				"left outer join rr.doctoServicoByIdDoctoServReembolsado dsr " +
				"join rr.filialByIdFilialOrigem filOr " +
				"left outer join filOr.pessoa pesOr " +
				"left outer join rr.clienteByIdClienteRemetente rem " +
				"left outer join rem.pessoa pesRem " +
				"left outer join rr.clienteByIdClienteDestinatario dest " +
				"left outer join dest.pessoa pesDest " );
		
		hql.addRequiredCriteria("dsr.idDoctoServico","=",idDoctoServico);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	public List findPaginatedChequesByIdReembolso(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(ch.nrBanco as nrBanco, " +
				"ch.nrAgencia as nrAgencia, " +
				"ch.nrCheque as nrCheque, " +
				"ch.vlCheque as vlCheque, " +
				"ch.dtCheque as dtCheque, " +
				"m.sgMoeda as sgMoeda, " +
				"m.dsSimbolo as dsSimbolo)");
		
		hql.addFrom(ChequeReembolso.class.getName()+ " ch " +
				"join ch.reciboReembolso rr " +
				"left join rr.moeda m" );
		
		hql.addRequiredCriteria("rr.idDoctoServico","=",idDoctoServico);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	public List findPaginatedDadosCompl(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map((case when trc.dsTipoRegistroComplemento is not null then trc.dsTipoRegistroComplemento else 'Dados do cliente' end)","descricao");
		hql.addProjection("(case when ids.dsCampo is not null then ids.dsCampo else idc.dsCampo end)","informacao");
		hql.addProjection("dc.idDadosComplemento","idDadosComplemento");
		hql.addProjection("dc.dsValorCampo","conteudo)");
		
		hql.addFrom(DadosComplemento.class.getName()+ " dc " +
				"left outer join dc.conhecimento conh " +
				"left outer join dc.informacaoDocServico ids " +
				"left outer join ids.tipoRegistroComplemento trc " +
				"left outer join dc.informacaoDoctoCliente idc ");
		
				
		hql.addCriteria("conh.idDoctoServico","=",idDoctoServico);
		hql.addCustomCriteria("dc.idDadosComplemento not in (select dco.idDadosComplemento from NfDadosComp nfdc left outer join nfdc.dadosComplemento dco where dco.idDadosComplemento = dc.idDadosComplemento)");
		
		hql.addOrderBy("case when trc.dsTipoRegistroComplemento is not null then trc.dsTipoRegistroComplemento else 'Dados do cliente' end");
		hql.addOrderBy("case when ids.dsCampo is not null then ids.dsCampo else idc.dsCampo end");
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	public boolean findDadosComplAba(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("1");
		hql.addFrom(DadosComplemento.class.getName()+ " dc " +
				"left outer join dc.conhecimento conh " );
		hql.addCriteria("conh.idDoctoServico","=",idDoctoServico);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria()).size()>0;
	}
	
	public List findPaginatedComplNF(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(nfc.nrNotaFiscal","nrNotaFiscal");
		hql.addProjection("(case when trc.dsTipoRegistroComplemento is not null then trc.dsTipoRegistroComplemento else 'Dados do cliente' end)","descricao");
		hql.addProjection("(case when ids.dsCampo is not null then ids.dsCampo else idc.dsCampo end)","informacao");
		hql.addProjection("dc.dsValorCampo","conteudo)");
		
		hql.addFrom(NotaFiscalConhecimento.class.getName()+ " nfc " +
				"join nfc.conhecimento conh " +
				"left outer join nfc.nfDadosComps nfDados " +
				"left outer join nfDados.dadosComplemento dc " +
				"left join dc.informacaoDocServico ids " +
				"left join ids.tipoRegistroComplemento trc " +
				"left join dc.informacaoDoctoCliente idc ");
		
				
		hql.addCriteria("conh.idDoctoServico","=",idDoctoServico);
		
		hql.addOrderBy("nfc.nrNotaFiscal");
		hql.addOrderBy("case when trc.dsTipoRegistroComplemento is not null then trc.dsTipoRegistroComplemento else 'Dados do cliente' end");
		hql.addOrderBy("case when ids.dsCampo is not null then ids.dsCampo else idc.dsCampo end");
		
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
		
	public List findRastreamentoMirEnt(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map" +
				"('Mir do administrativo para a entrega' as evento, "+
				"mir.dhEmissao as dhEmissao, " +
				"foMir.sgFilial as sgFilial, " +
				"mir.nrMir as nrDocto , " +
				"mir.dhEnvio as dhEnvio, " +
				"soMir.dsSetor as origem, " +
				"mir.dhRecebimento as dhRecebimento, " +
				"sdMir.dsSetor as destino)");
		
		
		hql.addFrom(ReciboReembolso.class.getName()+ " rr " +
				"left outer join rr.doctoServicoByIdDoctoServReembolsado reembolsado " +
				"left outer join rr.documentoMirs dm " +
				"left outer join dm.mir mir " +
				"left outer join mir.filialByIdFilialOrigem foMir " +
				"left outer join mir.setorByIdSetorOrigem soMir " +
				"left outer join mir.setorByIdSetorDestino sdMir ");
				
		
		hql.addCriteria("mir.tpMir","=","AE");
		hql.addCriteria("reembolsado.idDoctoServico","=", idDoctoServico);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	
	public List findRastreamentoMirDestOri(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map" +
				"('Mir do destino para origem' as evento, "+
				"mir.dhEmissao as dhEmissao, " +
				"foMir.sgFilial as sgFilial, " +
				"mir.nrMir as nrDocto , " +
				"mir.dhEnvio as dhEnvio, " +
				"soMir.dsSetor as origem, " +
				"mir.dhRecebimento as dhRecebimento, " +
				"sdMir.dsSetor as destino)");
		
		
		hql.addFrom(ReciboReembolso.class.getName()+ " rr " +
				"left outer join rr.doctoServicoByIdDoctoServReembolsado reembolsado " +
				"left outer join rr.documentoMirs dm " +
				"left outer join dm.mir mir " +
				"left outer join mir.filialByIdFilialOrigem foMir " +
				"left outer join mir.setorByIdSetorOrigem soMir " +
				"left outer join mir.setorByIdSetorDestino sdMir ");
				
		
		hql.addCriteria("mir.tpMir","=","DO");
		hql.addCriteria("reembolsado.idDoctoServico","=", idDoctoServico);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	public List findRastreamentoMirEntregaAdm(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map" +
				"('Mir da entrega para o administrativo' as evento, "+
				"mir.dhEmissao as dhEmissao, " +
				"foMir.sgFilial as sgFilial, " +
				"mir.nrMir as nrDocto , " +
				"mir.dhEnvio as dhEnvio, " +
				"soMir.dsSetor as origem, " +
				"mir.dhRecebimento as dhRecebimento, " +
				"sdMir.dsSetor as destino)");
		
		
		hql.addFrom(ReciboReembolso.class.getName()+ " rr " +
				"left outer join rr.doctoServicoByIdDoctoServReembolsado reembolsado " +
				"left outer join rr.documentoMirs dm " +
				"left outer join dm.mir mir " +
				"left outer join mir.filialByIdFilialOrigem foMir " +
				"left outer join mir.setorByIdSetorOrigem soMir " +
				"left outer join mir.setorByIdSetorDestino sdMir ");
				
		
		hql.addCriteria("mir.tpMir","=","EA");
		hql.addCriteria("reembolsado.idDoctoServico","=", idDoctoServico);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	public List findRastreamentoReembDest(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map" +
				"('Recebimento do reembolso no destinatário' as evento, " +
				"filMe.sgFilial as sgFilial, " +
				"me.nrManifestoEntrega as nrDocto, " +
				"med.dhOcorrencia as dhRecebimento)");
		 
		hql.addFrom(ReciboReembolso.class.getName()+ " rr " +
				"left outer join rr.doctoServicoByIdDoctoServReembolsado reembolsado " +
				"left outer join reembolsado.manifestoEntregaDocumentos med " +
				"left outer join med.manifestoEntrega me " +
				"left outer join me.filial filMe " +
				"left outer join med.ocorrenciaEntrega oe ");
		
		hql.addCriteria("oe.tpOcorrencia","=","E");
		hql.addCriteria("reembolsado.idDoctoServico","=", idDoctoServico);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	
	public List findRastreamentoMercadoriaME(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map " +
				"('Mercadoria no manifesto de entrega' as evento, " +
				"man.dhEmissaoManifesto as dhEmissao, " +
				"filMe.sgFilial as sgFilial, " +
				"me.nrManifestoEntrega as nrDocto)");
		
		
		hql.addFrom(ReciboReembolso.class.getName()+ " rr " +
				"left outer join rr.doctoServicoByIdDoctoServReembolsado reembolsado " +
				"left outer join reembolsado.manifestoEntregaDocumentos med " +
				"left outer join med.manifestoEntrega me " +
				"left outer join me.manifesto man " +
				"left outer join me.filial filMe");
		
		hql.addCustomCriteria("man.dhEmissaoManifesto.value is not null and man.dhEmissaoManifesto.value = " +
				"(select min(mani.dhEmissaoManifesto.value) from ManifestoEntregaDocumento medoc " +
				"join medoc.manifestoEntrega ment " +
				"join ment.manifesto mani " +
				"join medoc.doctoServico ds " +
				"where ds.idDoctoServico = reembolsado.idDoctoServico)");
		
		hql.addCriteria("reembolsado.idDoctoServico","=", idDoctoServico);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	public boolean findAgendamentosAba(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("1");
		hql.addFrom(AgendamentoDoctoServico.class.getName()+ " ads " +
				"join ads.doctoServico ds " +
				"join ads.agendamentoEntrega ae ");
		
		hql.addCriteria("ds.idDoctoServico","=",idDoctoServico);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria()).size()>0;
		
	}
	
	
	public List findAgendamentosByDoctoServico(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(fil.sgFilial as sgFilial, "+
				"pes.nmFantasia as nmFantasia, " +
				"ae.tpAgendamento as tpAgendamento, " +
				"uscriacao.nmUsuario as nmUsuarioCriacao, " +
				"ae.dhContato as dhContato, " +
				"ae.nmContato as nmContato, " +
				"ae.nrDdd as nrDdd, " +
				"ae.nrTelefone as nrTelefone, " +
				"ae.nrRamal as nrRamal, " +
				"ae.tpSituacaoAgendamento as tpSituacaoAgendamento, " +
				"ae.obAgendamentoEntrega as obAgendamentoEntrega, " +
				"ae.dtAgendamento as dtAgendamento, " +
				"turno.dsTurno as dsTurno, " +
				"ae.hrPreferenciaInicial as hrPreferenciaInicial, " +
				"ae.hrPreferenciaFinal as hrPreferenciaFinal, " +
				"ae.blCartao as blCartao, " +
				//""+PropertyVarcharI18nProjection.createProjection("mac.dsMotivoAgendamento")+" as dsMotivoAgendamento, " +
				"(case when "+PropertyVarcharI18nProjection.createProjection("mac.dsMotivoAgendamento")+" is not null then "+PropertyVarcharI18nProjection.createProjection("mac.dsMotivoAgendamento")+" else "+PropertyVarcharI18nProjection.createProjection("mreag.dsMotivoAgendamento")+" end) as  dsMotivoAgendamento, " +
				"ae.dhCancelamento as dhCancelamento, " +
				"uscancel.nmUsuario as nmUsuarioCancel)");
		
		hql.addFrom(AgendamentoDoctoServico.class.getName()+ " ads " +
				"join ads.doctoServico ds " +
				"join ads.agendamentoEntrega ae " +
				"join ae.filial fil " +
				"left outer join fil.pessoa pes " +
				"left outer join ae.usuarioByIdUsuarioCriacao uscriacao " +
				"left outer join ae.turno turno " +
				"left outer join ae.motivoAgendamentoByIdMotivoReagendamento mreag " +
				"left outer join ae.motivoAgendamentoByIdMotivoCancelamento mac " +
				"left outer join ae.usuarioByIdUsuarioCancelamento uscancel");
		
		hql.addCriteria("ds.idDoctoServico","=",idDoctoServico);
		
		hql.addCustomCriteria("ae.idAgendamentoEntrega = (select max(aent.idAgendamentoEntrega) " +
				"from AgendamentoDoctoServico adser " +
				"join adser.doctoServico dser " +
				"join adser.agendamentoEntrega aent " +
				"where dser.idDoctoServico = ? )");
		
		hql.addCriteriaValue(idDoctoServico);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
		
	}
	
	
	public ResultSetPage findPaginatedAgendamentosByDoctoServico(Long idDoctoServico, FindDefinition findDef){
		SqlTemplate hql = mountsqlAgendamentosByDoctoServico(idDoctoServico);
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), hql.getCriteria());
		
	}
	
	public SqlTemplate mountsqlAgendamentosByDoctoServico(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(ae.tpAgendamento as tpAgendamento, " +
				"ae.idAgendamentoEntrega as idAgendamentoEntrega, " +
				"ae.dhContato as dhContato, " +
				"ae.nmContato as nmContato, " +
				"ae.tpSituacaoAgendamento as tpSituacaoAgendamento, " +
				" ae.dtAgendamento as dtAgendamento, "+
				"turno.dsTurno as dsTurno, " +
				"ae.hrPreferenciaInicial as hrPreferenciaInicial, "+
				"ae.hrPreferenciaFinal as  hrPreferenciaFinal, " +
				"ae.blCartao as blCartao)");
				
		hql.addFrom(AgendamentoDoctoServico.class.getName()+ " ads " +
				"join ads.doctoServico ds " +
				"join ads.agendamentoEntrega ae " +
				"left outer join ae.turno turno");
		
		hql.addCriteria("ds.idDoctoServico","=",idDoctoServico);
		hql.addOrderBy("ae.dhContato.value","desc");
		return hql;
	}
		
	public Integer getRowCountAgendamentosByDoctoServico(TypedFlatMap criteria){
		SqlTemplate hql = mountsqlAgendamentosByDoctoServico(criteria.getLong("idDoctoServico"));
		Integer rowCount = getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria());
    	return rowCount;
	}
	
	public List findAgendamentoByIdAgendamento(Long idAgendamento){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(fil.sgFilial as sgFilial, " +
				"pes.nmFantasia as nmFantasia, " +
				"ae.tpAgendamento as tpAgendamento, " +
				"uscriacao.nmUsuario as nmUsuarioCriacao, " +
				"ae.dhContato as dhContato, " +
				"ae.nmContato as nmContato, " +
				"ae.nrDdd as nrDdd, " +
				"ae.nrTelefone as nrTelefone, " +
				"ae.nrRamal as nrRamal, " +
				"ae.tpSituacaoAgendamento as tpSituacaoAgendamento, " +
				"ae.obAgendamentoEntrega as obAgendamentoEntrega, " +
				"ae.dtAgendamento as dtAgendamento, " +
				"turno.dsTurno as dsTurno, " +
				"ae.hrPreferenciaInicial as hrPreferenciaInicial, " +
				"ae.hrPreferenciaFinal as hrPreferenciaFinal, " +
				"ae.blCartao as blCartao, " +
				"(case when "+PropertyVarcharI18nProjection.createProjection("mreag.dsMotivoAgendamento")+" is not null then "+PropertyVarcharI18nProjection.createProjection("mreag.dsMotivoAgendamento")+" else "+PropertyVarcharI18nProjection.createProjection("mcan.dsMotivoAgendamento")+" end) as dsMotivoAgendamento, " +
				"ae.dhCancelamento as dhCancelamento, " +
				"uscancel.nmUsuario as nmUsuarioCancel)");
		
		hql.addFrom(AgendamentoEntrega.class.getName()+ " ae " +
				"join ae.filial fil " +
				"left outer join fil.pessoa pes " +
				"join ae.usuarioByIdUsuarioCriacao uscriacao " +
				"left outer join ae.turno turno " +
				"left outer join ae.motivoAgendamentoByIdMotivoReagendamento mreag " +
				"left outer join ae.motivoAgendamentoByIdMotivoCancelamento mcan " +
				"left outer join ae.usuarioByIdUsuarioCancelamento uscancel");
		
		hql.addCriteria("ae.idAgendamentoEntrega","=",idAgendamento);
		
				
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
		
	}
	
	
	 public Object findByIdDSByLocalizacaoMercadoriaPrincipal(TypedFlatMap criteria){
	    	SqlTemplate sql = new SqlTemplate();
	    	
	    	ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
				public void configQuery(SQLQuery sqlQuery) {
					sqlQuery.addScalar("qtVolumes", Hibernate.INTEGER);
					sqlQuery.addScalar("psReal", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("psAforado", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("psReferenciaCalculo", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("dsEnderecoEntrega", Hibernate.STRING);
					sqlQuery.addScalar("sgFilialOriginal", Hibernate.STRING);
					sqlQuery.addScalar("nrDoctoServicoOriginal", Hibernate.LONG);
					sqlQuery.addScalar("sgFilialPedCol", Hibernate.STRING);
					sqlQuery.addScalar("nrColeta", Hibernate.LONG);
					sqlQuery.addScalar("nmPessoRem", Hibernate.STRING);
					sqlQuery.addScalar("nrIdentificacaoRem", Hibernate.STRING);
					sqlQuery.addScalar("nmPessoDest", Hibernate.STRING);
					sqlQuery.addScalar("nrIdentificacaoDest", Hibernate.STRING);
					sqlQuery.addScalar("nrNotaFiscal", Hibernate.INTEGER);
					sqlQuery.addScalar("qtNotaFiscal", Hibernate.INTEGER);
					sqlQuery.addScalar("tpDensidade", Hibernate.STRING);
					sqlQuery.addScalar("dsNaturezaProduto", Hibernate.STRING);
					sqlQuery.addScalar("nrAwb", Hibernate.STRING);
					sqlQuery.addScalar("dvAwb", Hibernate.INTEGER);
					sqlQuery.addScalar("nmEmpresaCiaAerea", Hibernate.STRING);
					
								
				}
			};
	    	
			//PROJECAO DOCUMENTO DE SERVICO
	    	sql.addProjection("DS.DH_INCLUSAO" , "dhInclusao");
	    	sql.addProjection("DS.DH_ALTERACAO" , "dhAlteracao");
	    	
	    	sql.addProjection("CONH.TP_CTRC_PARCERIA" , "tpParceria");
	    	sql.addProjection("CONH.BL_INDICADOR_EDI" , "blIndicador");
	    	    
	    	    	
	    	//*******************************************FROM*************************************************************
	    	
	        //DOCUMENTO DE SERVICO
	    	sql.addFrom("DOCTO_SERVICO", "DS");
	    	
	    	//CONHECIMENTO
	    	sql.addFrom("CONHECIMENTO","CONH");
	    	sql.addFrom("FILIAL","FIL");
	    	sql.addFrom("PESSOA","PES");
	    	
	    	sql.addJoin("DS.ID_DOCTO_SERVICO","CONH.ID_CONHECIMENTO(+)");
	    	
	    	
	    	
			return getAdsmHibernateTemplate().findByIdBySql(sql.getSql(),sql.getCriteria(), configSql);
		}
	    
	
	 public List findComplementosOutros(Long idDoctoServico){
			SqlTemplate hql = new SqlTemplate();
			hql.addProjection("new Map(ds.dhInclusao as dhInclusao, " +
					"usInc.nmUsuario as nmUsuarioInclusao, " +
					"fo.sgFilial as filialInc, " +
					"fo.sgFilial as filialAlt, " +
					"pes.nmFantasia as nmFantasiaInc, " +
					"pes.nmFantasia as nmFantasiaAlt, " +
					"ds.dhAlteracao as dhAlteracao, " +
					"usAlt.nmUsuario as nmUsuarioAlteracao)");
			
			hql.addFrom(DoctoServico.class.getName()+" ds " +
					"join ds.usuarioByIdUsuarioInclusao usInc " +
					"join ds.filialByIdFilialOrigem fo " +
					"left outer join fo.pessoa pes " +
					"left join ds.usuarioByIdUsuarioAlteracao usAlt");
			
			hql.addCriteria("ds.idDoctoServico","=", idDoctoServico);
			
			return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
			
	}
	
	public List findComplementosOutrosIndicadorCooperacao(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(conh.tpCtrcParceria as tpCtrcParceria, conh.idDoctoServico as idConhecimento, " +
				"conh.blIndicadorEdi as blIndicadorEdi)");
		hql.addFrom(Conhecimento.class.getName()+" conh ");
		hql.addCriteria("conh.idDoctoServico","=",idDoctoServico);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
		
		
	}
}