package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.NotaDebitoNacional;
import com.mercurio.lms.contasreceber.model.param.DevedorDocServFatLookupParam;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.util.SQLUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DevedorDocServFatLookUpDAO extends AdsmDao 
{
	
	/**
	 * Retorna a lista de devedor a partir dos dados informado montando os hql a 
	 * partir do tipo de codumento informado. O parametro tpDocumento é obrigatório!
	 * 
	 * @author Mickaël Jalbert
	 * 
	 * @param Long nrDocumento
	 * @param String tpDocumento
	 * @param Long idFilial
	 * @param Long idCliente
	 * @param String tpSituacaoAprovacao
	 * @return List
	 * */
	public List findDevedorDocServFat(DevedorDocServFatLookupParam devedorDocServFatLookupParam){
		SqlTemplate hql = null;
		StringBuffer strProjection = new StringBuffer();
		
		String tpDocumento = devedorDocServFatLookupParam.getTpDocumentoServico();
		
		
		if (tpDocumento.equals("CTR") || tpDocumento.equals("NFT")||
				tpDocumento.equals("CTE") || tpDocumento.equals("NTE")){
			hql = mountHql(Conhecimento.class, devedorDocServFatLookupParam);
			strProjection.append("esp.tpSituacaoConhecimento as tpSituacaoConhecimento, ");
		}
		if (tpDocumento.equals("CRT")){
			hql = mountHql(CtoInternacional.class, devedorDocServFatLookupParam);
		}
		if (tpDocumento.equals("NFS")||tpDocumento.equals("NSE")){
			hql = mountHql(NotaFiscalServico.class, devedorDocServFatLookupParam);
			strProjection.append("esp.tpSituacaoNf as tpSituacaoNf, ");
		}
		if (tpDocumento.equals("NDN")){
			hql = mountHql(NotaDebitoNacional.class, devedorDocServFatLookupParam);
		}			
		hql.addProjection("new Map(" +
				"dev.id as idDevedorDocServFat, " +
				"dev.vlDevido as vlDevido, " +
				"des.vlDesconto as vlDesconto, " +
				"des.idDesconto as idDesconto, " +
				"des.motivoDesconto.id as idMotivoDesconto, " +
				"dev.tpSituacaoCobranca as tpSituacaoCobranca, " +
				"div.idDivisaoCliente as idDivisaoCliente, " +
				"div.dsDivisaoCliente as dsDivisaoCliente, " +
				"des.tpSituacaoAprovacao as tpSituacaoAprovacao, " +
				"esp.vlTotalDocServico as vlTotalDocServico, " +
				"ser.id as idServico, " +
				"ser.tpModal as tpModal, " +
				"ser.tpAbrangencia as tpAbrangencia, " +				
				"pes.nrIdentificacao as nrIdentificacaoResponsavelAnterior, " +				
				"pes.nmPessoa as nmResponsavelAnterior, " +
				"pes.tpIdentificacao as tpIdentificacaoResponsavelAnterior, " +
				"esp.id as idDoctoServico, " +
				"fil.id as idFilial, " +
				"fil.sgFilial as sgFilial, " +
				"fil.pessoa.nmFantasia as nmFantasia, " +
				"esp.tpDocumentoServico as tpDocumentoServico, " +
				"esp.moeda.id as idMoeda, " +
				"moe.sgMoeda as sgMoeda, " +
				"moe.dsSimbolo as dsSimbolo, " +				
				"filo.idFilial as idFilialOrigem, " +
				"filo.sgFilial as sgFilialOrigem, " +
				"pesfilo.nmFantasia as nmFantasiaOrigem, " +
				"cli.idCliente as idCliente, " +
				strProjection +
				"esp.nrDoctoServico as nrDoctoServico,"+
				"esp.nrDoctoServico as doctoServico_nrDoctoServico)");
		
		hql.addLeftOuterJoin("dev.divisaoCliente", "div");
		
		return this.getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	
	
	/**
	 * Retorna a lista de devedor a partir dos dados informado montando os hql a 
	 * partir do tipo de codumento informado. O parametro tpDocumento é obrigatório!
	 * 
	 * @author Mickaël Jalbert
	 * 
	 * @param Long nrDocumento
	 * @param String tpDocumento
	 * @param Long idFilial
	 * @param Long idCliente
	 * @param String tpSituacaoAprovacao
	 * @return List
	 * */
	public ResultSetPage findPaginated(DevedorDocServFatLookupParam devedorDocServFatLookupParam, FindDefinition findDef){
		SqlTemplate hql = null;
		
		hql = mountHql(DoctoServico.class, devedorDocServFatLookupParam);
			
		hql.addProjection("new Map(" +
				"dev.id as idDevedorDocServFat, " +
				"dev.vlDevido as vlDevido, " +
				"des.vlDesconto as vlDesconto, " +
				"des.idDesconto as idDesconto, " +
				"des.motivoDesconto.id as idMotivoDesconto, " +
				"dev.tpSituacaoCobranca as tpSituacaoCobranca, " +
				"div.dsDivisaoCliente as dsDivisaoCliente, " +
				"des.tpSituacaoAprovacao as tpSituacaoAprovacao, " +
				"esp.vlTotalDocServico as vlTotalDocServico, " +
				"ser.id as idServico, " +
				"ser.tpModal as tpModal, " +
				"ser.tpAbrangencia as tpAbrangencia, " +
				"pes.tpIdentificacao as tpIdentificacaoResponsavelAnterior, " +				
				"pes.nrIdentificacao as nrIdentificacaoResponsavelAnterior, " +				
				"pes.nmPessoa as nmResponsavelAnterior, " +
				"pesfil.nmFantasia as nmFantasia, " +				
				"esp.id as idDoctoServico, " +
				"fil.id as idFilial, " +
				"fil.sgFilial as sgFilial, " +
				"fil.pessoa.nmFantasia as nmFantasia, " +
				"esp.tpDocumentoServico as tpDocumentoServico, " +
				"esp.moeda.id as idMoeda, " +
				"moe.sgMoeda as sgMoeda, " +
				"moe.dsSimbolo as dsSimbolo, " +
				"filo.idFilial as idFilialOrigem, " +
				"filo.sgFilial as sgFilialOrigem, " +
				"pesfilo.nmFantasia as nmFantasiaOrigem, " +
				"esp.nrDoctoServico as nrDoctoServico,"+
				"filCrobCli.sgFilial as sgFilialCobCli,"+
				"cli.idCliente as idCliente, " +
				"pesCrobCli.nmFantasia as  nmFantasiaFilialCobCli  )");
		
		hql.addLeftOuterJoin("dev.divisaoCliente", "div");
		
		hql.addOrderBy("filo.sgFilial");
    	hql.addOrderBy("esp.nrDoctoServico");
		
		return this.getAdsmHibernateTemplate().findPaginated(hql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),hql.getCriteria());
	}
	
	/**
	 * Retorna a lista de devedor a partir dos dados informado montando os hql a 
	 * partir do tipo de codumento informado. O parametro tpDocumento é obrigatório!
	 * 
	 * @author Mickaël Jalbert
	 * 
	 * @param Long nrDocumento
	 * @param String tpDocumento
	 * @param Long idFilial
	 * @param Long idCliente
	 * @param String tpSituacaoAprovacao
	 * @return List
	 * */
	public Integer getRowCount(DevedorDocServFatLookupParam devedorDocServFatLookupParam){
		SqlTemplate hql = null;

		hql = mountHql(DoctoServico.class, devedorDocServFatLookupParam);
		
		return this.getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(),hql.getCriteria());
	}	

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DevedorDocServFat.class;
    }
    
    private SqlTemplate mountHql(Class clazz, DevedorDocServFatLookupParam ddsfParam){

    	if (StringUtils.isNotBlank(ddsfParam.getTpFrete()) &&
        	(ddsfParam.getTpDocumentoServico().equals("CTR") || ddsfParam.getTpDocumentoServico().equals("NFT")||
        			ddsfParam.getTpDocumentoServico().equals("CTE") || ddsfParam.getTpDocumentoServico().equals(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA))){
        	clazz = Conhecimento.class;	
        }
    	
    	SqlTemplate hql = new SqlTemplate();    	
    	
    	hql.addInnerJoin(clazz.getName(), "esp");
    	hql.addInnerJoin("esp.devedorDocServFats", "dev");
    	hql.addInnerJoin("esp.moeda", "moe");
    	hql.addLeftOuterJoin("esp.servico", "ser");
    	hql.addInnerJoin("esp.filialByIdFilialOrigem", "filo");
    	hql.addInnerJoin("filo.pessoa", "pesfilo");
    	hql.addLeftOuterJoin("dev.descontos", "des");
    	hql.addInnerJoin("dev.cliente", "cli");
    	
    	hql.addInnerJoin("cli.filialByIdFilialCobranca", "filCrobCli");
    	hql.addInnerJoin("filCrobCli.pessoa", "pesCrobCli");
    	
    	hql.addInnerJoin("dev.filial", "fil");
    	hql.addInnerJoin("fil.pessoa", "pesfil");    	
    	hql.addInnerJoin("cli.pessoa", "pes");
    	
    	hql.addCriteria("filo.id", "=", ddsfParam.getIdFilial());			
    	
    	hql.addCriteria("cli.id", "=", ddsfParam.getIdCliente());
    	
    	hql.addCriteria("esp.nrDoctoServico", "=", ddsfParam.getNrDocumentoServico());
    	
    	hql.addCriteria("esp.tpDocumentoServico", "=", ddsfParam.getTpDocumentoServico());
    	
    	hql.addCriteria("dev.id", "=", ddsfParam.getIdDevedorDocServFat());
    	
    	hql.addCriteria("esp.id", "=", ddsfParam.getIdDocumentoServico());
    	
    	hql.addCriteria("des.tpSituacaoAprovacao", "=", ddsfParam.getTpSituacaoDesconto());
    	
    	hql.addCriteria("ser.id", "=", ddsfParam.getIdServico());
    	
    	hql.addCriteria("ser.tpModal", "=", ddsfParam.getTpModal());
    	
    	hql.addCriteria("ser.tpAbrangencia", "=", ddsfParam.getTpAbrangencia());
    	
    	hql.addCriteria("moe.id", "=", ddsfParam.getIdMoeda());
    	
    	hql.addCriteria("dev.divisaoCliente.id", "=", ddsfParam.getIdDivisaoCliente());

    	if (StringUtils.isNotBlank(ddsfParam.getTpFrete()) &&
    		(ddsfParam.getTpDocumentoServico().equals("CTR") || ddsfParam.getTpDocumentoServico().equals("NFT")||
    					ddsfParam.getTpDocumentoServico().equals("CTE") || ddsfParam.getTpDocumentoServico().equals(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA))){
    		hql.addCriteria("esp.tpFrete", "=", ddsfParam.getTpFrete());
    	}
    	
    	hql.addCustomCriteria("esp.nrDoctoServico > 0");
    	
    	hql.addCriteria("TRUNC(cast(esp.dhEmissao.value as date))", ">=", ddsfParam.getDtEmissaoInicial());
    	
    	hql.addCriteria("TRUNC(cast(esp.dhEmissao.value as date))", "<=", ddsfParam.getDtEmissaoFinal());
    	
    	//Situacao possíveis de devedor
    	if (ddsfParam.getTpSituacaoDevedorDocServFatValido() != null){
    		SQLUtils.joinExpressionsWithComma(ddsfParam.getTpSituacaoDevedorDocServFatValidoList(), hql, "dev.tpSituacaoCobranca");
    	}
    	
    	hql.addCriteria("dev.tpSituacaoCobranca", "=", ddsfParam.getTpSituacaoCobranca());
    	
    	return hql;
    }
    
    /**
     * LMS-3069
     * @param idDoctoServico
     * @return
     * @author WagnerFC
     */
    @SuppressWarnings("rawtypes")
	public Boolean validateDataEntregaCtrc(Long idDoctoServico){
    	StringBuilder hql = new StringBuilder();    	
    	hql.append(" SELECT 1 ");
    	hql.append("   FROM ").append(DoctoServico.class.getName()).append(" ds");
    	hql.append("   		,").append(Conhecimento.class.getName()).append(" cn");
    	hql.append("  WHERE ds.idDoctoServico = ").append(idDoctoServico.toString());
    	hql.append("    AND ds.idDoctoServico = cn.idDoctoServico ");
    	hql.append("    AND cn.tpFrete = 'F' ");
    	hql.append("    AND ds.nrDiasRealEntrega IS NULL ");
    	hql.append("    AND NOT EXISTS ");
    	hql.append(" (SELECT 1 ");
    	hql.append("    FROM ").append(EventoDocumentoServico.class.getName()).append(" eds");
    	hql.append("		 , ").append(Evento.class.getName()).append(" ev");
    	hql.append("   WHERE eds.doctoServico.idDoctoServico = ds.idDoctoServico ");
    	hql.append("     AND eds.evento.idEvento = ev.idEvento ");
    	hql.append("     AND eds.blEventoCancelado = 'N' ");
    	hql.append("     AND ev.cdEvento IN (7, 90, 91)) ");
    	hql.append("     AND NOT EXISTS ");
    	hql.append(" (SELECT 1 ");
    	hql.append("    FROM ").append(OcorrenciaDoctoServico.class.getName()).append(" ods");
    	hql.append("		, ").append(OcorrenciaPendencia.class.getName()).append(" op");
    	hql.append("   WHERE ods.doctoServico.idDoctoServico = ds.idDoctoServico ");
    	hql.append("     AND ods.ocorrenciaPendenciaByIdOcorBloqueio = op.idOcorrenciaPendencia ");
    	hql.append("     AND op.cdOcorrencia = 205) ");
		
 		List list = this.getAdsmHibernateTemplate().find(hql.toString());
		
		return !list.isEmpty();
    }

}