package com.mercurio.lms.expedicao.model.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.LiberacaoNotaNatura;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.dao.LiberacaoNotaNaturaDAO;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor do <code>id</code> informado abaixo deve ser 
 * utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.expedicao.liberacaoNotaNaturaService"
 */
public class LiberacaoNotaNaturaService extends CrudService<LiberacaoNotaNatura, Long> {
	
	private ConfiguracoesFacade configuracoesFacade;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	
	public LiberacaoNotaNatura findLiberacaoNotaNaturaForNotaFiscalConhecimento(Integer nrNotaFiscal, Long idCliente) {
		return getLiberacaoNotaNaturaDAO().findLiberacaoNotaNaturaForNotaFiscalConhecimento(nrNotaFiscal, idCliente);
	}
	
	/**
	 * @param idDoctoServico
	 * @param idFilialOrigem
	 * @param idClienteRemetente
	 * @param dhEmissao
	 * @author JonasFE
	 */
	public void validateTerraNaturaCTRCReentregaDevolucao(Long idDoctoServico, Long idFilialOrigem, Long idClienteRemetente, DateTime dhEmissao, String parametroReentregaDevolucao) {
		String dsIdsNatura = (String) configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_IDS_NATURA);
		
		//nao deve ser feito contains na string pois pode trazer resultados incorretos
		//hashset é a collection com melhor performance para o uso de contains
		Set<Long> idsNatura = new HashSet<Long>();
		try {
			for (String id : dsIdsNatura.split(";")) {
				idsNatura.add(Long.valueOf(id));
			}
		} catch (Exception e) {
			throw new IllegalStateException("PARAMETRO IDS_NATURA NAO ESTA NO FORMATO ID;ID;ID;ID");
		}
		
		if(idsNatura.contains(idClienteRemetente)){
			YearMonthDay dataImplantacao = (YearMonthDay) configuracoesFacade.getValorParametro(idFilialOrigem, ConstantesExpedicao.NM_PARAMETRO_DT_IMP_TERRA_NATURA);
			
			if(dataImplantacao != null && dataImplantacao.compareTo(dhEmissao.toYearMonthDay()) <= 0){
				List<NotaFiscalConhecimento> listNotaFiscalConhecimento = notaFiscalConhecimentoService.findByConhecimento(idDoctoServico);
				for (NotaFiscalConhecimento notaFiscalConhecimento : listNotaFiscalConhecimento) {
					LiberacaoNotaNatura liberacaoNotaNatura = findLiberacaoNotaNaturaForNotaFiscalConhecimento(notaFiscalConhecimento.getNrNotaFiscal(), notaFiscalConhecimento.getConhecimento().getClienteByIdClienteRemetente().getIdCliente());
					if(liberacaoNotaNatura == null){
						throw new BusinessException("LMS-04347");
					}
					String tpServicoReentregaNatura = (String) configuracoesFacade.getValorParametro(parametroReentregaDevolucao);
					if(!tpServicoReentregaNatura.equals(liberacaoNotaNatura.getTpServico())){
						throw new BusinessException("LMS-04347");
					}
					
					if(!"C".equals(liberacaoNotaNatura.getTpSituacao().getValue()) && !"L".equals(liberacaoNotaNatura.getTpSituacao().getValue())){
						throw new BusinessException("LMS-04347");
					}
				}
				
			}
		}
	}
	
	public void atualizaTerraNaturaReentregaDevolucaoDigitado(Conhecimento conhecimentoOriginal, Conhecimento conhecimentoNovo) {
		if(conhecimentoOriginal.getNotaFiscalConhecimentos() != null){
			for (NotaFiscalConhecimento nfc : conhecimentoOriginal.getNotaFiscalConhecimentos()) {
				LiberacaoNotaNatura liberacaoNotaNatura = findLiberacaoNotaNaturaForNotaFiscalConhecimento(nfc.getNrNotaFiscal(), conhecimentoOriginal.getClienteByIdClienteRemetente().getIdCliente());
				if(liberacaoNotaNatura != null){
					//Doc Serv Digitado
					liberacaoNotaNatura.setTpSituacao(new DomainValue("D"));
					liberacaoNotaNatura.setDoctoServico(conhecimentoNovo);
					store(liberacaoNotaNatura);
				}
			}
		}
	}
	
	/**
	 * Passa os registros em Liberacao_Nota_natura com ID_DOCTO_SERVICO = parametro para tp_situacao = 'E'
	 * 
	 * @param idDoctoServico
	 * @author JonasFE
	 */
	public void atualizaTerraNaturaEmitido(Long idDoctoServico) {
		getLiberacaoNotaNaturaDAO().atualizaTerraNaturaEmitido(idDoctoServico);
	}
	
	/**
	 * Passa os registros em Liberacao_Nota_natura com ID_DOCTO_SERVICO = parametro para tp_situacao = 'C'
	 * 
	 * @param idDoctoServico
	 * @author JonasFE
	 */
	public void atualizaTerraNaturaCancelado(Long idDoctoServico) {
		getLiberacaoNotaNaturaDAO().atualizaTerraNaturaCancelado(idDoctoServico);
	}

	public ResultSetPage findNotasNatura(TypedFlatMap criteria) {
		return getLiberacaoNotaNaturaDAO().findNotasNatura(criteria);
	}
	
	
	public Integer getRowCountNotasNatura(TypedFlatMap criteria) {
		return getLiberacaoNotaNaturaDAO().getRowCountNotasNatura(criteria);
	}
	
	public java.io.Serializable store(LiberacaoNotaNatura bean) {
		return super.store(bean);
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}
	
	public LiberacaoNotaNaturaDAO getLiberacaoNotaNaturaDAO() {
		return (LiberacaoNotaNaturaDAO) getDao();
	}
	
	public void setLiberacaoNotaNaturaDAO(LiberacaoNotaNaturaDAO dao) {
		setDao(dao);
	}

	
}