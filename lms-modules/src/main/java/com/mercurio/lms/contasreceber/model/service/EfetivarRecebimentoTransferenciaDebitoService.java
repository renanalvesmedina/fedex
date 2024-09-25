package com.mercurio.lms.contasreceber.model.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.ItemTransferencia;
import com.mercurio.lms.contasreceber.model.Transferencia;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * 
 * @spring.bean id="lms.contasreceber.efetivarRecebimentoTransferenciaDebitoService"
 */
public class EfetivarRecebimentoTransferenciaDebitoService {

	private TransferenciaService transferenciaService;
	private ItemTransferenciaService itemTransferenciaService;
	private DomainValueService domainValueService;
	private DevedorDocServFatService devedorDocServFatService;
	private ParametroGeralService parametroGeralService;

	/**
	 * Valida a Filial do Usuário logado com a Filial informada.<BR>
	 * @param idFilialDestino
	 * @return
	 */
	public boolean validateFilialUsuario(Long idFilial){
		if (idFilial == null) return false;
		
		Filial filial = SessionUtils.getFilialSessao();
		if (idFilial.equals(filial.getIdFilial())){
			return true;
		}		
		return false;
	}

	/**
	 * Verifica se a Situação da Transferencia é alguma das informadas.<BR>
	 * @param idTransferencia
	 * @return
	 */
	public boolean validateSituacaoTransferencia(Long idTransferencia, String[] situacoes){
		
		if (idTransferencia == null) return false;
		if (situacoes == null && situacoes.length < 1) return false;
		
		Transferencia transferencia = transferenciaService.findById(idTransferencia);
		String tpSituacaoTransferencia = transferencia.getTpSituacaoTransferencia().getValue();
		for (int i = 0; i < situacoes.length; i++){
			if (situacoes[i].equals(tpSituacaoTransferencia)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public java.io.Serializable findItemTransferenciaById(Long id){
		ItemTransferencia itemTransferencia = itemTransferenciaService.findById(id);
		
		TypedFlatMap map = new TypedFlatMap();
		
		map.put("responsavelAntigo.idResponsavelAntigo",itemTransferencia.getClienteByIdAntigoResponsavel().getIdCliente());
		
		String nrIdentificacao = itemTransferencia.getClienteByIdAntigoResponsavel().getPessoa().getNrIdentificacao();
		String tpIdentificacao = itemTransferencia.getClienteByIdAntigoResponsavel().getPessoa().getTpIdentificacao().getValue();
		
		map.put("responsavelAntigo.nrIdentificacao", com.mercurio.lms.util.FormatUtils.formatIdentificacao(tpIdentificacao, nrIdentificacao));
		map.put("responsavelAntigo.nmPessoa",itemTransferencia.getClienteByIdAntigoResponsavel().getPessoa().getNmPessoa());

		map.put("responsavelNovo.idResponsavelAntigo",itemTransferencia.getClienteByIdNovoResponsavel().getIdCliente());
		
		nrIdentificacao = itemTransferencia.getClienteByIdNovoResponsavel().getPessoa().getNrIdentificacao();
		tpIdentificacao = itemTransferencia.getClienteByIdNovoResponsavel().getPessoa().getTpIdentificacao().getValue();
		
		map.put("responsavelNovo.nrIdentificacao",com.mercurio.lms.util.FormatUtils.formatIdentificacao(tpIdentificacao, nrIdentificacao));
		map.put("responsavelNovo.nmPessoa",itemTransferencia.getClienteByIdNovoResponsavel().getPessoa().getNmPessoa());
		
		map.put("motivoTransferencia", itemTransferencia.getMotivoTransferencia().getDsMotivoTransferencia());
		map.put("doctoServico.tpDocumento", itemTransferencia.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico().getDescription());
		map.put("doctoServico.sgFilial", itemTransferencia.getDevedorDocServFat().getDoctoServico().getFilialByIdFilialOrigem().getSgFilial());
		map.put("doctoServico.nrDoctoServico", FormatUtils.formataNrDocumento(itemTransferencia.getDevedorDocServFat().getDoctoServico().getNrDoctoServico().toString()
				, itemTransferencia.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico().getValue().toString()));
		map.put("obItemTransferencia", itemTransferencia.getObItemTransferencia());
		
		if(itemTransferencia.getDivisaoClienteNovo() != null)
			map.put("dsDivisaoClienteNovo", itemTransferencia.getDivisaoClienteNovo().getDsDivisaoCliente());
		else
			map.put("dsDivisaoClienteNovo", null);
		
		if(itemTransferencia.getDivisaoClienteAntigo() != null)
			map.put("dsDivisaoClienteAntigo", itemTransferencia.getDivisaoClienteAntigo().getDsDivisaoCliente());
		else
			map.put("dsDivisaoClienteAntigo", null);
		
		return map;
	}
	
	/**
	 * Remove Itens da Transferencia, verificando se ainda há Itens.<BR>
	 * Se todos foram removidos, então a Situação da Transferencia será atualizada para 'Cancelada'.<BR> 
	 * @param list
	 */
	public void removeItensTransferencia(Map map) {
		TypedFlatMap tfm = (TypedFlatMap) map;
		Long idTransferencia = tfm.getLong("idTransferencia");

		itemTransferenciaService.removeByIds( (List) tfm.get("itens.ids") );
		
		//Verifica se há itens, atualizando para Cancelada a Situação da Transferencia.
		if (!transferenciaService.validateCountItensByMin(idTransferencia, Integer.valueOf(0))){
			Transferencia transferencia = transferenciaService.findById(idTransferencia);
			DomainValue cancelada = domainValueService.findDomainValueByValue("DM_STATUS_TRANSFERENCIA", "CA");
			transferencia.setTpSituacaoTransferencia(cancelada);
			transferenciaService.store(transferencia);
		}
		
	}
	
	/**
	 * Serviço que busca os Itens da Transferencia paginados.<BR>
	 * @param parameters
	 * @return
	 */
	public ResultSetPage findItensByTransferencia(TypedFlatMap parameters){
		ResultSetPage rs = transferenciaService.findItensByTransferencia(parameters);

		Iterator iter = rs.getList().iterator();
		while (iter.hasNext()){
			Map map = (Map) iter.next();
			String tpDocumentoServico = (String) ((Map)map.get("tpDocumentoServico")).get("value");
			String nrDoctoServico = ((Long) map.get("nrDoctoServico")).toString();
			map.put("nrDocumento", FormatUtils.formataNrDocumento(nrDoctoServico,tpDocumentoServico));			
			map.put("vlrFrete", map.get("vlrFrete"));
		}

		return rs;
	}

	public Integer getRowCountFromItensByTransferencia(TypedFlatMap map){
		return transferenciaService.getRowCountFromItensByTransferencia(map);
	}
	
	/**
	 * Atualiza a Transferencia para cancelá-la.<BR>
	 * @param idTransferencia
	 * @return
	 */
	public java.io.Serializable storeCancelarTransferenciaDebito(Long idTransferencia){
		//Atualiza transferencia para 'Cancelada'
		Transferencia transferencia = transferenciaService.findById(idTransferencia);
		
		/* Se a filial do usuário logado for diferente da filial origem da transferência
		 *   OU a situação da transferência for diferente de "PR" (pendente de recebimento),
		 * impedir a operação e exibir a mensagem LMS-36210 "Filial não é responsável pela cobrança do documento informado"
		 */
		Filial filialSessao = SessionUtils.getFilialSessao();
		boolean filialOrigem = transferencia.getFilialByIdFilialOrigem() != null && filialSessao.getIdFilial().equals(transferencia.getFilialByIdFilialOrigem().getIdFilial());
		if (! (filialOrigem && transferencia.getTpSituacaoTransferencia() != null && "PR".equals(transferencia.getTpSituacaoTransferencia().getValue()))) {
			throw new BusinessException("LMS-36210");
		}
		
		//  Jira LMS-5393
		/**
		 *  Se mês/ano da transferência for diferente do mês/ano da data atual
		 *  E mês/ano da transferência for diferente do mês/ano da competência do financeiro (parâmetro COMPETENCIA_FINANCEIRO)
		 *  OU mês/ano da transferência for igual ao mês/ano da competência do financeiro (parâmetro COMPETENCIA_FINANCEIRO)
		 *  E parâmetro IND_FECHTO_MENSAL_FINANCEIRO = “S”
		 *  Então exibir a mensagem LMS-36281 “Competência da transferência inválida para cancelamento”
		 */
		YearMonthDay dtAtual =JTDateTimeUtils.getDataAtual();
		String fechamento = String.valueOf(parametroGeralService.findSimpleConteudoByNomeParametro("IND_FECHTO_MENSAL_FINANCEIRO"));
		String paramCompetencia = parametroGeralService.findSimpleConteudoByNomeParametro("COMPETENCIA_FINANCEIRO");
		YearMonthDay competencia = JTDateTimeUtils.convertDataStringToYearMonthDay(paramCompetencia,"yyyy-MM-dd");
		
		if ( ( (transferencia.getDtEmissao().getMonthOfYear() != dtAtual.getMonthOfYear() || transferencia.getDtEmissao().getYear() != dtAtual.getYear()) 
				&& (transferencia.getDtEmissao().getMonthOfYear() != competencia.getMonthOfYear() || transferencia.getDtEmissao().getYear() != competencia.getYear()) )
			|| ( (transferencia.getDtEmissao().getMonthOfYear() == competencia.getMonthOfYear() || transferencia.getDtEmissao().getYear() == competencia.getYear()) 
				&& fechamento.equals("S")) ) {
			throw new BusinessException("LMS-36281");
		}
		
		DomainValue statusTransferencia = domainValueService.findDomainValueByValue("DM_STATUS_TRANSFERENCIA","CA");
		transferencia.setTpSituacaoTransferencia(statusTransferencia);
		transferenciaService.store(transferencia);
		return transferencia;
	}
	
	/**
	 * Método para recebimento da Transferência.<BR>
	 * @param map
	 * @return
	 */
	public java.io.Serializable storeTransferenciaDebito(Long idTransferencia){
		
		Transferencia transferencia = transferenciaService.findById(idTransferencia);
		
		/* Se a filial do usuário logado for diferente da filial de destino da transferência 
		 * 	OU a situação da transferência for diferente de "PR" (pendente de recebimento), 
		 * impedir a operação e exibir a mensagem LMS-36210 "Filial não é responsável pela cobrança do documento informado" */
			
		Filial filialSessao = SessionUtils.getFilialSessao();
		boolean filialDestino = transferencia.getFilialByIdFilialDestino() != null && filialSessao.getIdFilial().equals(transferencia.getFilialByIdFilialDestino().getIdFilial());
		if (! (filialDestino && transferencia.getTpSituacaoTransferencia() != null && "PR".equals(transferencia.getTpSituacaoTransferencia().getValue()))) {
			throw new BusinessException("LMS-36210");
		}
				
				Filial filial = transferencia.getFilialByIdFilialDestino();
				Iterator iter = transferencia.getItemTransferencias().iterator();

				//Atualizar cada Item da Transferencia em DevedorDocServFat
				while (iter.hasNext()){
					ItemTransferencia item = (ItemTransferencia) iter.next();
					DevedorDocServFat devedorDocServFat = item.getDevedorDocServFat();
					Cliente cliente = item.getClienteByIdNovoResponsavel();
					devedorDocServFat.setDivisaoCliente(item.getDivisaoClienteNovo());
					devedorDocServFat.setFilial(filial);
					devedorDocServFat.setCliente(cliente);
					devedorDocServFatService.store(devedorDocServFat);
				}

		
		//Atualiza Transferencia para 'Recebida'
		DomainValue statusTransferencia = domainValueService.findDomainValueByValue("DM_STATUS_TRANSFERENCIA","RE");
		transferencia.setTpSituacaoTransferencia(statusTransferencia);
		transferencia.setDtRecebimento(JTDateTimeUtils.getDataAtual());
		transferenciaService.store(transferencia);
		
		return transferencia;
	}

	public void setTransferenciaService(TransferenciaService transferenciaService) {
		this.transferenciaService = transferenciaService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setItemTransferenciaService(
			ItemTransferenciaService itemTransferenciaService) {
		this.itemTransferenciaService = itemTransferenciaService;
	}

	public void setDevedorDocServFatService(
			DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}
	
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
}
