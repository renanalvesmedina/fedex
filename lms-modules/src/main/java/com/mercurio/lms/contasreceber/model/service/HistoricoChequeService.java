package com.mercurio.lms.contasreceber.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EmpresaCobranca;
import com.mercurio.lms.configuracoes.model.service.EmpresaCobrancaService;
import com.mercurio.lms.contasreceber.model.Alinea;
import com.mercurio.lms.contasreceber.model.Cheque;
import com.mercurio.lms.contasreceber.model.HistoricoCheque;
import com.mercurio.lms.contasreceber.model.LoteCheque;
import com.mercurio.lms.contasreceber.model.dao.HistoricoChequeDAO;
import com.mercurio.lms.contasreceber.model.param.MovimentoChequeParam;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.historicoChequeService"
 */
public class HistoricoChequeService extends CrudService<HistoricoCheque, Long> {

	// Históricos de cheques
	private static final String LUCROS_PERDAS = "LP";
	private static final String TRANSFERENCIA_MATRIZ = "TM";
	private static final String DEVOLUCAO_NAO_COBRADO_MATRIZ = "DC";
	private static final String REAPRESENTADO = "RE";
	private static final String RECEBIDO_FILIAL = "RF";
	private static final String TRANSFERENCIA_FILIAL_COBRANCA = "TF";
	private static final String RECEBIDO_MATRIZ = "RM";
	private static final String TRANSFERENCIA_JURIDICO = "TJ";
	private static final String DEVOLVIDO_BANCO = "DB";
	private static final String LIQUIDADO = "LI";
	private static final String TRANSFERENCIA_EMPRESA_COBRANCA = "TC";
	
	// Situações do lote de cheque
	private static final String LIQUIDADO_PARCIAL = "LP";
	private static final String LIQUIDADO_TOTAL = "LT";

	// Situações do cheque
	private static final String CHEQUE_ATIVO = "A";
	private static final String CHEQUE_LIQUIDADO = "L";
	private static final String CHEQUE_LUCROS_PERDAS = "P";
	private static final String CHEQUE_JURIDICO = "J";
	
	// Para tratamento de atualização de saldo
	private static final int ATUALIZA_SALDO_ENTRADA = 1;
	private static final int ATUALIZA_SALDO_SAIDA = -1;
	private static final int NAO_ATUALIZA_SALDO = 0;
	
	// Para tratamento de atualização do histórico como entrada ou saída
	private static final String TP_OPERACAO_ENTRADA = "C";
	private static final String TP_OPERACAO_SAIDA = "D";

	// Para tratamento de sucursal ou matriz
	private static final String TP_FILIAL_SUCURSAL = "SU";
	private static final String TP_FILIAL_MATRIZ = "MA";

	// Para tratamento de workflow
	private static final String SITUACAO_WORKFLOW_APROVADO = "A";
	private static final String SITUACAO_WORKFLOW_EM_APROVACAO = "E";
	private static final String SITUACAO_WORKFLOW_REPROVADO = "R";
	
	// Mensagens
	private static final String LMS_36073 = "LMS-36073";
	private static final String LMS_36170 = "LMS-36170";

	private EmpresaCobrancaService empresaCobrancaService;
	private SaldoChequeService saldoChequeService;
	private ChequeService chequeService;
	private WorkflowPendenciaService workflowPendenciaService;
	private ConfiguracoesFacade configuracoesFacade;
	private HistoricoFilialService historicoFilialService;
	private AlineaService alineaService;
	private FilialService filialService;

	/**
	 * Recupera uma instância de <code>HistoricoCheque</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public HistoricoCheque findById(java.lang.Long id) {
        return (HistoricoCheque)super.findById(id);
    }
 
    public List find(MovimentoChequeParam param) {
    	return this.getHistoricoChequeDAO().find(param);
    }
    
    public Integer getRowCount(MovimentoChequeParam param) {
    	return this.getHistoricoChequeDAO().getRowCount(param);
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
    
    private boolean beforeStoreHistoricoCheque(HistoricoCheque historicoChequeOld, HistoricoCheque historicoChequeNew, YearMonthDay dtReapresentacao, Long idAlinea) {
    	String tpHistCheOld = historicoChequeOld.getTpHistoricoCheque().getValue();
    	String tpHistCheNew = historicoChequeNew.getTpHistoricoCheque().getValue();

    	int atualizaSaldo = 0;
    	
		// Se tem uma pendência em aprovação, dá exception para o usuário
		if (historicoChequeOld.getCheque().getPendencia() != null && historicoChequeOld.getCheque().getPendencia().getTpSituacaoPendencia().getValue().equals(SITUACAO_WORKFLOW_EM_APROVACAO)){
			throw new BusinessException(LMS_36170);
		}

    	//DB - 'Devolvido pelo Banco'    	
    	if (tpHistCheNew.equals(DEVOLVIDO_BANCO)) {
    		atualizaSaldo = validateDevolvidoBanco(historicoChequeOld, tpHistCheOld, idAlinea);

    	//EN - 'Devolução de cheque não cobrado para a Matriz'    	
    	} else if (tpHistCheNew.equals(DEVOLUCAO_NAO_COBRADO_MATRIZ)) {
    		
    		Integer ret = validateDevolucaoNaoCobradoMatriz(historicoChequeOld, tpHistCheOld);
    		// se retorno for null, deve retornar false para
    		// não gerar o histórico do cheque
    		if (ret==null) {
    			return false;
    		} else {
    			atualizaSaldo = ret.intValue();
    		}
    	
    	//RE - 'Reapresentado'
    	} else if (tpHistCheNew.equals(REAPRESENTADO)){
    		atualizaSaldo = validateReapresentado(historicoChequeOld, dtReapresentacao, tpHistCheOld);			

    	//RF - 'Recebido pela filial'
    	}else if (tpHistCheNew.equals(RECEBIDO_FILIAL)){
    		atualizaSaldo = validateRecebidoFilial(tpHistCheOld);

    	//RM - 'Recebido pela Matriz'
    	} else if (tpHistCheNew.equals(RECEBIDO_MATRIZ)){
    		atualizaSaldo = validateRecebidoMatriz(tpHistCheOld);

    	//TJ - 'Transferência para o Jurídico'
    	} else if (tpHistCheNew.equals(TRANSFERENCIA_JURIDICO)){
    		atualizaSaldo = validateTrasferenciaJuridico(historicoChequeOld, tpHistCheOld);

       	//LI - 'Liquidado'
    	} else if (tpHistCheNew.equals(LIQUIDADO)){
    		atualizaSaldo = validateLiquidado(historicoChequeOld, tpHistCheOld);
    		
       	//LP - 'Lucros e perdas'
    	} else if (tpHistCheNew.equals(LUCROS_PERDAS)){
    		atualizaSaldo = validateLucrosPerdas(historicoChequeOld, tpHistCheOld);

   		//TC - 'Transferência para a empresa de cobrança'
    	} else if (tpHistCheNew.equals(TRANSFERENCIA_EMPRESA_COBRANCA)){
    		
    		atualizaSaldo = validateTransferenciaEmpresaCobranca(historicoChequeOld, tpHistCheOld);			
    	
   		//TF - 'Transferência para a filial de cobrança'
    	} else if (tpHistCheNew.equals(TRANSFERENCIA_FILIAL_COBRANCA)){
    		
    		// Caso a filial da sessão do usuário seja Matriz, não permitir esta ação
    		if ( historicoFilialService.validateFilialUsuarioMatriz(historicoChequeNew.getCheque().getFilial().getIdFilial()) ){
    			throw new BusinessException("LMS-36073");
    		}
    		
    		atualizaSaldo = validateTransferenciaFilialCobranca(historicoChequeOld, tpHistCheOld);

   		//TM - 'Transferência para a Matriz'
    	} else if (tpHistCheNew.equals(TRANSFERENCIA_MATRIZ)){
    		atualizaSaldo = validateTransferenciaMatriz(tpHistCheOld);

    	}    	
    	
    	// Fazer update do saldo cheque com os novos cheques
    	if (atualizaSaldo!=NAO_ATUALIZA_SALDO) {
    		// Se for uma entrada
    		if (atualizaSaldo==ATUALIZA_SALDO_ENTRADA) {
    			historicoChequeNew.setTpOperacao(new DomainValue(TP_OPERACAO_ENTRADA));
       		// Se for uma saída
    		} else {
    			historicoChequeNew.setTpOperacao(new DomainValue(TP_OPERACAO_SAIDA));
    		}
    		this.saldoChequeService.updateSaldoCheque(historicoChequeNew.getCheque().getMoedaPais(), historicoChequeNew.getCheque().getVlCheque(), atualizaSaldo);
    	}
    	
    	return true;
    }

	/**
	 * @param tpHistCheOld
	 */
	private int validateTransferenciaMatriz(String tpHistCheOld) {

		//O último histórico do cheque deve ser “Recebido pela filial”. Caso seja violada a regra exibir a mensagem LMS-36073
		if (!tpHistCheOld.equals(RECEBIDO_FILIAL)){
			throw new BusinessException(LMS_36073);
		}
		
		return NAO_ATUALIZA_SALDO;

	}

	/**
	 * @param tpHistCheOld
	 */
	private int validateTransferenciaFilialCobranca(HistoricoCheque historicoChequeOld, String tpHistCheOld) {

		//O último histórico do cheque deve ser “Devolvido pelo banco” ou "Recebido Matriz". Caso seja violada a regra exibir a mensagem LMS-36073
		if (!tpHistCheOld.equals(DEVOLVIDO_BANCO) && !tpHistCheOld.equals(RECEBIDO_MATRIZ)){
			throw new BusinessException(LMS_36073);
		}

		// Se o último histórico for "Devolvido Banco"
		if (tpHistCheOld.equals(DEVOLVIDO_BANCO)){
			// deve existir um histórico reapresentado
			List lstSituacoes = new ArrayList();
			lstSituacoes.add(REAPRESENTADO);
			if (!validadeExisteHistorico(historicoChequeOld.getCheque().getIdCheque(), lstSituacoes)){
				throw new BusinessException(LMS_36073);
			}
		}

		// Saldo deve ser atualizado com uma saida
		return ATUALIZA_SALDO_SAIDA;

	}

	/**
	 * @param tpHistCheOld
	 */
	private int validateTransferenciaEmpresaCobranca(HistoricoCheque historicoChequeOld, String tpHistCheOld) {

		//O último histórico do cheque deve ser “Devolvido pelo banco” ou "Recebido Matriz". Caso seja violada a regra exibir a mensagem LMS-36073
		if (!tpHistCheOld.equals(DEVOLVIDO_BANCO) && !tpHistCheOld.equals(RECEBIDO_MATRIZ)){
			throw new BusinessException(LMS_36073);
		}
		
		// Se o último histórico for "Devolvido Banco"
		if (tpHistCheOld.equals(DEVOLVIDO_BANCO)){
			// deve existir um histórico reapresentado
			List lstSituacoes = new ArrayList();
			lstSituacoes.add(REAPRESENTADO);
			if (!validadeExisteHistorico(historicoChequeOld.getCheque().getIdCheque(), lstSituacoes)){
				throw new BusinessException(LMS_36073);
			}
		}

		// Saldo deve ser atualizado com uma saida
		return ATUALIZA_SALDO_SAIDA;
	}

	/**
	 * @param historicoChequeOld
	 * @param tpHistCheOld
	 */
	private int validateLucrosPerdas(HistoricoCheque historicoChequeOld, String tpHistCheOld) {

		// O último histórico do cheque deve ser “Devolvido pelo Banco” ou "Recebido pela Matriz”, 
		// "Transferencia para o Juridico" ou "Transferencia para empresa de cobrança". 
		// Caso seja violada a regra exibir a mensagem LMS-36073
		if (!tpHistCheOld.equals(DEVOLVIDO_BANCO) && !tpHistCheOld.equals(RECEBIDO_MATRIZ) &&
			!tpHistCheOld.equals(TRANSFERENCIA_JURIDICO) && !tpHistCheOld.equals(TRANSFERENCIA_EMPRESA_COBRANCA)){
			throw new BusinessException(LMS_36073);
		}    		
		
		// Se o último histórico for "Recebido Matriz"
		if (tpHistCheOld.equals(RECEBIDO_MATRIZ)){
			// a filial da sessão não pode ser uma sucursal
			if (historicoFilialService.validateFilialByTpFilial(SessionUtils.getFilialSessao().getIdFilial(), "SU")) {
				throw new BusinessException(LMS_36073);
			}
		}

		// Se o último histórico for "Devolvido Banco"
		if (tpHistCheOld.equals(DEVOLVIDO_BANCO)){
			// deve existir histórico anterior "Reapresentado"
			List lstSituacoes = new ArrayList();
			lstSituacoes.add(REAPRESENTADO);
			if (!validadeExisteHistorico(historicoChequeOld.getCheque().getIdCheque(), lstSituacoes)){
				throw new BusinessException(LMS_36073);
			}
		}
		
		//Trocar a situação para 'Lucros e perdas'
		atualizaSituacaoCheque(historicoChequeOld.getCheque(), CHEQUE_LUCROS_PERDAS);
		
		// Atualizar o lote do cheque
		atualizaLoteCheque(historicoChequeOld.getCheque().getLoteCheque());

		// Se historico anterior foi "Recebido Matriz" ou "Devolvido Banco"
		if (tpHistCheOld.equals(RECEBIDO_MATRIZ) || tpHistCheOld.equals(DEVOLVIDO_BANCO)) {
			// Saldo deve ser atualizado com uma saida
			return ATUALIZA_SALDO_SAIDA;			
		} else {
			// Não atualiza saldo
			return NAO_ATUALIZA_SALDO;
		}
	
	}

	/**
	 * retorna true se existe um histórico de uma das situações no cheque
	 * @param lstSituacoes
	 * @return
	 */
	public boolean validadeExisteHistorico(Long idCheque, List lstSituacoes) {
		return getHistoricoChequeDAO().validadeExisteHistorico(idCheque, lstSituacoes);
	}

	/**
	 * @param historicoChequeOld
	 * @param tpHistCheOld
	 */
	private int validateLiquidado(HistoricoCheque historicoChequeOld, String tpHistCheOld) {

		//O último histórico do cheque deve ser “Recebido pela Matriz”, "Reapresentado" 
		//“Transferência para empresa de cobrança”, “Lucros e Perdas” ou 
		//“Transferência para o jurídico”. Caso seja violada a regra exibir a mensagem LMS-36073
		if (!tpHistCheOld.equals(RECEBIDO_MATRIZ) && !tpHistCheOld.equals(REAPRESENTADO) && 
			!tpHistCheOld.equals(LUCROS_PERDAS) && !tpHistCheOld.equals(TRANSFERENCIA_EMPRESA_COBRANCA) && 
			!tpHistCheOld.equals(TRANSFERENCIA_JURIDICO)){
			throw new BusinessException(LMS_36073);
		}
		
		// Se historico anterior for "Lucros e Perdas"
		if (tpHistCheOld.equals(LUCROS_PERDAS)) {
			// não pode existir histórico anterior "Transferencia para o Juridico" ou "Transferencia para empresa de cobrança"
			List lstSituacoes = new ArrayList();
			lstSituacoes.add(TRANSFERENCIA_JURIDICO);
			lstSituacoes.add(TRANSFERENCIA_EMPRESA_COBRANCA);
			if (validadeExisteHistorico(historicoChequeOld.getCheque().getIdCheque(), lstSituacoes)){
				throw new BusinessException(LMS_36073);
			}
		}
		
		//Trocar a situação do cheque para 'Liquidado'
		atualizaSituacaoCheque(historicoChequeOld.getCheque(), CHEQUE_LIQUIDADO);
		
		// Atualiza o lote do cheque
		atualizaLoteCheque(historicoChequeOld.getCheque().getLoteCheque());

		// Se historico anterior foi "Recebido Matriz"
		if (tpHistCheOld.equals(RECEBIDO_MATRIZ)) {
			// Saldo deve ser atualizado com uma saida
			return ATUALIZA_SALDO_SAIDA;			
		} else {
			// Não atualiza saldo
			return NAO_ATUALIZA_SALDO;
		}
	
	}

	/**
	 * @param historicoChequeOld
	 * @param tpHistCheOld
	 */
	private int validateTrasferenciaJuridico(HistoricoCheque historicoChequeOld, String tpHistCheOld) {

		//O último histórico do cheque deve ser “Lucros e Perdas”. Caso seja violada a regra exibir a mensagem LMS-36073
		if (!tpHistCheOld.equals(LUCROS_PERDAS)){
			throw new BusinessException(LMS_36073);
		}
		
		// não pode existir histórico anterior "Transferencia para o Juridico" ou "Transferencia para empresa de cobrança"
		List lstSituacoes = new ArrayList();
		lstSituacoes.add(TRANSFERENCIA_JURIDICO);
		lstSituacoes.add(TRANSFERENCIA_EMPRESA_COBRANCA);
		if (validadeExisteHistorico(historicoChequeOld.getCheque().getIdCheque(), lstSituacoes)){
			throw new BusinessException(LMS_36073);
		}

		//Atribuir a situação 'Jurídico' para o cheque
		atualizaSituacaoCheque(historicoChequeOld.getCheque(), CHEQUE_JURIDICO);

		// Não atualiza saldo
		return NAO_ATUALIZA_SALDO;

	}

	/**
	 * @param tpHistCheOld
	 */
	private int validateRecebidoMatriz(String tpHistCheOld) {

		//O último histórico do cheque deve ser “Transferência para a Matriz” ou “Devolução de Cheque não Cobrado para a Matriz”. Caso seja violada a regra exibir a mensagem LMS-36073
		if (!tpHistCheOld.equals(TRANSFERENCIA_MATRIZ) && !tpHistCheOld.equals(DEVOLUCAO_NAO_COBRADO_MATRIZ)){
			throw new BusinessException(LMS_36073);
		}

		// Saldo deve ser atualizado com uma entrada
		return ATUALIZA_SALDO_ENTRADA;

	}

	/**
	 * @param tpHistCheOld
	 */
	private int validateRecebidoFilial(String tpHistCheOld) {

		//O último histórico do cheque deve ser “Transferência para a filial de cobrança”. Caso seja violada a regra exibir a mensagem LMS-36073
		if (!tpHistCheOld.equals(TRANSFERENCIA_FILIAL_COBRANCA)){
			throw new BusinessException(LMS_36073);
		}
		
		// Não atualiza saldo
		return NAO_ATUALIZA_SALDO;

	}

	/**
	 * @param historicoChequeOld
	 * @param dtReapresentacao
	 * @param tpHistCheOld
	 */
	private int validateReapresentado(HistoricoCheque historicoChequeOld, YearMonthDay dtReapresentacao, String tpHistCheOld) {

		//O último histórico do cheque deve ser “Devolvido pelo banco”. Caso seja violada a regra exibir a mensagem LMS-36073
		if (!tpHistCheOld.equals(DEVOLVIDO_BANCO)){
			throw new BusinessException(LMS_36073);
		}
		
		// não pode existir histórico anterior "Reapresentado"
		List lstSituacoes = new ArrayList();
		lstSituacoes.add(REAPRESENTADO);
		if (validadeExisteHistorico(historicoChequeOld.getCheque().getIdCheque(), lstSituacoes)){
			throw new BusinessException(LMS_36073);
		}

		//Atribuir a data de reapresentação para o cheque
		Cheque cheque = historicoChequeOld.getCheque();
		cheque.setDtReapresentacao(dtReapresentacao);
		this.chequeService.store(cheque);
		
		// Saldo deve ser atualizado com uma saida
		return ATUALIZA_SALDO_SAIDA;

	}

	/**
	 * @param historicoChequeOld
	 * @param tpHistCheOld
	 */
	private Integer validateDevolucaoNaoCobradoMatriz(HistoricoCheque historicoChequeOld, String tpHistCheOld) {

		//O último histórico do cheque deve ser “Recebido pela filial”. Caso seja violada a regra exibir a mensagem LMS-36073
		if (!tpHistCheOld.equals(RECEBIDO_FILIAL)){
			throw new BusinessException(LMS_36073);
		}    		
		
		// Se não tem pendencia de workflow associado ou
		// se a pendência está reprovada, gerar uma pendencia 
		if (historicoChequeOld.getCheque().getPendencia() == null || historicoChequeOld.getCheque().getPendencia().getTpSituacaoPendencia().getValue().equals(SITUACAO_WORKFLOW_REPROVADO)){

			Cheque cheque = historicoChequeOld.getCheque(); 
			
			//Gerar pendencia e atribuir dentro do cheque   			
			cheque.setPendencia(generatePendenciaWorkflow(cheque));
			
			// Seta a situação para em aprovação
			cheque.setTpSituacaoAprovacao(new DomainValue("E"));

			this.chequeService.store(cheque);

			// retorna null para que o histórico do cheque não seja gerado
			// somente gera o workflow e atualizar a pendência do cheque
			return null;
		
		}
		
		// Não atualiza saldo
		return Integer.valueOf(NAO_ATUALIZA_SALDO);

	}

	/**
	 * @param historicoChequeOld
	 * @param tpHistCheOld
	 */
	private int validateDevolvidoBanco(HistoricoCheque historicoChequeOld, String tpHistCheOld, Long idAlinea) {

		// Cheque para ser alterada a alínea e a situação do cheque (a situação apenas quando anterior "Liquidado"
		Cheque cheque = historicoChequeOld.getCheque();

		//O último histórico do cheque deve ser “Liquidado” ou “Reapresentado”. Caso seja violada a regra exibir a mensagem LMS-36073
		if (!tpHistCheOld.equals(LIQUIDADO) && !tpHistCheOld.equals(REAPRESENTADO)){
			throw new BusinessException(LMS_36073);
		}
		
		// Se a situação anterior for "Liquidado"
		if (tpHistCheOld.equals(LIQUIDADO)) {
			
			// não pode existir histórico anterior "Reapresentado", 
			// "Transferência para empresa de Cobrança" ou "Lucros e Perdas"
			List lstSituacoes = new ArrayList();
			lstSituacoes.add(REAPRESENTADO);
			lstSituacoes.add(TRANSFERENCIA_EMPRESA_COBRANCA);
			lstSituacoes.add(LUCROS_PERDAS);
			if (validadeExisteHistorico(historicoChequeOld.getCheque().getIdCheque(), lstSituacoes)){
				throw new BusinessException(LMS_36073);
			}
			
			// Atualiza o lote para LIQUIDADO PARCIAL 
			atualizaLoteCheque(historicoChequeOld.getCheque().getLoteCheque(), LIQUIDADO_PARCIAL);
			
			//Trocar a situação do cheque para 'Ativo' será atualizado abaixo
			cheque.setTpSituacaoCheque(new DomainValue(CHEQUE_ATIVO));
		
		} 
		
		// Atualiza a alinea de devolução do cheque
		Alinea alinea = alineaService.findById(idAlinea);
		cheque.setAlinea(alinea);
		this.chequeService.store(cheque);
		
		// Saldo deve ser atualizado com uma entrada
		return ATUALIZA_SALDO_ENTRADA;
	
	}
    
    /**
     * Edenilson
     * Atualiza a situação do cheque 
     * @param cheque
     * @param situacao
     */
    private void atualizaSituacaoCheque(Cheque cheque, String situacao){
		cheque.setTpSituacaoCheque(new DomainValue(situacao));
		this.chequeService.store(cheque);
    }
    
    /**
     * Edenilson
     * Atualiza a situação do lote de cheque 
     * @param loteCheque
     * @param situacao
     */
    private void atualizaLoteCheque(LoteCheque loteCheque, String situacao){
		loteCheque.setTpSituacaoLoteCheque(new DomainValue(situacao));
		// Chama direto o DAO para não realizar as validações do beforeStore do lote
		getHistoricoChequeDAO().store(loteCheque);
    	
    }

    /**
     * Edenilson
     * Atualiza a situação do lote de cheque de acordo com os cheques restantes 
     * @param loteCheque
     */
    private void atualizaLoteCheque(LoteCheque loteCheque){
		List lstChequeAtivo = this.chequeService.findAtivoByLoteCheque(loteCheque.getIdLoteCheque());
		//Se não tem (lista vazia)
		if (lstChequeAtivo.isEmpty()){
			//Trocar a situação do lote cheque para 'Liquidaddo Total' 
			atualizaLoteCheque(loteCheque, LIQUIDADO_TOTAL);
		//Se existe cheque ativo ainda
		} else {
			//Trocar a situação do lote cheque para 'Liquidaddo Parcial'    			
			atualizaLoteCheque(loteCheque, LIQUIDADO_PARCIAL);
		}
    }

    /**
     * Salva um novo historico cheque com a nova situação informada 
     * para cada historico cheque informado.
     * 
     * @author Mickaël Jalbert
     * @since 24/03/2006
     * 
     * @param List idsHistorico
     * @param DomainValue tpHistoricoCheque
     * @param String obHistoricoCheque
     * @param Long idEmpresaCobranca
     * @param YearMonthDay dtReapresentacao
     * @return Serializable
     * */
    public java.io.Serializable storeHistoricoCheque(List idsHistorico, DomainValue tpHistoricoCheque, String obHistoricoCheque, Long idEmpresaCobranca, YearMonthDay dtReapresentacao, Long idAlinea) {
    	EmpresaCobranca empresaCobranca = null;
    	
    	//Se o idEmpresaCobranca foi informado, buscar a empresa cobrança persistente e atribui 
    	//ele no historico cheque.     	
    	if (idEmpresaCobranca != null){
    		empresaCobranca = this.empresaCobrancaService.findById(idEmpresaCobranca);
    	}
    				
    	//Por cada historico selecionado, tem que aplicar as regras especificas.
    	for (Iterator iter = idsHistorico.iterator(); iter.hasNext();){
    		Long idHistoricoCheque = (Long)(iter.next());
    		
    		//Buscar o historicoCheque informado
    		HistoricoCheque historicoChequeOld = this.findById(idHistoricoCheque);
			
			//Montar um novo historicoCheque a partir daquele informado
			HistoricoCheque historicoChequeNew = this.prepareNewHistoricoCheque(historicoChequeOld, tpHistoricoCheque, obHistoricoCheque, empresaCobranca);
			
			// Se da chamada de validação retornar false, significa que gerou workflow e o histórico
			// não deve ser gerado nesse momento
			if (this.beforeStoreHistoricoCheque(historicoChequeOld, historicoChequeNew, dtReapresentacao, idAlinea)) {
				this.store(historicoChequeNew);
			}
    	}
        return null;
    }
   

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */    
    public java.io.Serializable store(HistoricoCheque bean) {
    	return super.store(bean);
    }
    
	/**
	 * Método que recebe um historicoCheque velho e devolve um novo
	 * historico a partir dos paramêtros informado.
	 * 
	 * @author Mickaël Jalbert
	 * 20/03/2006
	 *   
	 * @param HistoricoCheque historicoCheque
	 * @param DomainValue tpHistoricoCheque
	 * @param String obHistoricoCheque
	 * @param EmpresaCobranca empresaCobranca
	 * @return HistoricoCheque
	 */ 
    private HistoricoCheque prepareNewHistoricoCheque(HistoricoCheque historicoCheque, DomainValue tpHistoricoCheque, String obHistoricoCheque, EmpresaCobranca empresaCobranca){
    	HistoricoCheque historicoChequeNovo = new HistoricoCheque();
    	
    	historicoChequeNovo.setDhHistoricoCheque(JTDateTimeUtils.getDataHoraAtual());
    	historicoChequeNovo.setTpHistoricoCheque(tpHistoricoCheque);
		historicoChequeNovo.setObHistoricoCheque(obHistoricoCheque);

		//O cheque é o mesmo que o historicoCheque anterior
		historicoChequeNovo.setCheque(historicoCheque.getCheque());
		//Usuário da sessão
		historicoChequeNovo.setUsuario(SessionUtils.getUsuarioLogado());

		// Buscar a filial da sessão, caso a mesma seja uma sucursal
    	// Caso contrário, buscar a filial matriz da empresa da sessão
    	Filial filial = SessionUtils.getFilialSessao(); 
    	if (!historicoFilialService.validateFilialByTpFilial(SessionUtils.getFilialSessao().getIdFilial(), TP_FILIAL_SUCURSAL)) {
        	filial = filialService.findFilialByIdEmpresaTpFilial(SessionUtils.getEmpresaSessao().getIdEmpresa(), TP_FILIAL_MATRIZ); 
    	}
		historicoChequeNovo.setFilial(filial);

		//Se for informado, a empresa de cobrança informada
		historicoChequeNovo.setEmpresaCobranca(empresaCobranca);    	
    	return historicoChequeNovo;
    }
    
    /**
	 * Gera uma pendência em cima do cheque caso a situação do historico cheque seja 
	 * 'Devolução de cheque não cobrado para a Matriz'
	 *
	 * @author Mickaël Jalbert
	 * 21/03/2006
	 *
	 * @param Cheque cheque
	 * @return Pendencia
	 */
    private Pendencia generatePendenciaWorkflow(Cheque cheque){
    	String strDescricao = this.configuracoesFacade.getMensagem("LMS-36068");
    	strDescricao = strDescricao.replaceAll("&banco", cheque.getNrBanco().toString());
    	strDescricao = strDescricao.replaceAll("&agencia", cheque.getNrAgencia().toString());
    	strDescricao = strDescricao.replaceAll("&conta", cheque.getNrContaCorrente());
    	strDescricao = strDescricao.replaceAll("&cheque", cheque.getNrCheque().toString());
    	
    	return this.workflowPendenciaService.generatePendencia(cheque.getFilial().getIdFilial(), ConstantesWorkflow.NR3607_DEVOLUCAO_CHEQUE_MTZ, cheque.getIdCheque(), strDescricao, JTDateTimeUtils.getDataHoraAtual());
    }
    
	/**	 
	 * Rotina para Aprovação da devolução de cheque não cobrado para a matriz 
	 * @param idsProcesso Identificadores dos processos que geraram o workflow
	 * @param tpsSituacao Situação da aprovação de cada processso
	 */
    public String executeWorkflow(List idsProcesso, List tpsSituacao) {
    	
        if (idsProcesso == null || tpsSituacao == null || idsProcesso.size() != tpsSituacao.size()){
        	return null;
        }
        
        // Situação de aprovação do workflow
        String tpSituacao = (String)tpsSituacao.get(0);
        
        for (int i = 0; i < idsProcesso.size(); i++){
        	Long idCheque = (Long) idsProcesso.get(i);
        	
        	// Carrega o cheque para editar a situação de aprovação. 
        	Cheque cheque = chequeService.findChequeById(idCheque); 
        	cheque.setTpSituacaoAprovacao(new DomainValue(tpSituacao));
        	chequeService.store(cheque);
        	
    		Long idHistorico = findByCheque(idCheque);
    		List idsHist = new ArrayList();
    		idsHist.add(idHistorico);
    		
    		if ( tpSituacao.equals(SITUACAO_WORKFLOW_APROVADO) ) {
    			storeHistoricoCheque(idsHist,new DomainValue(DEVOLUCAO_NAO_COBRADO_MATRIZ), null, null, null, null);
    		}
    			
        }        
        return null;
    }
    
    /**
	 * Retorna os últimos historicos de cada cheque do lote cheque informado.
	 * 
	 * Mickaël Jalbert
	 * 21/03/2006
	 * 
	 * @param Long idLoteCheque
	 * @return List
	 **/
    public Long findByCheque(Long idCheque){
    	return this.getHistoricoChequeDAO().findByCheque(idCheque);
    }

    /**
	 * Retorna o último historico do cheque informado.
	 * 
	 * Edenilson
	 * 18/07/2006
	 * 
	 * @param Long idCheque
	 * @return Long
	 **/
    public List findByLoteCheque(Long idLoteCheque){
    	return this.getHistoricoChequeDAO().findByLoteCheque(idLoteCheque);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setHistoricoChequeDAO(HistoricoChequeDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private HistoricoChequeDAO getHistoricoChequeDAO() {
        return (HistoricoChequeDAO) getDao();
    }

	public void setEmpresaCobrancaService(
			EmpresaCobrancaService empresaCobrancaService) {
		this.empresaCobrancaService = empresaCobrancaService;
	}

	public void setSaldoChequeService(SaldoChequeService saldoChequeService) {
		this.saldoChequeService = saldoChequeService;
	}

	public void setChequeService(ChequeService chequeService) {
		this.chequeService = chequeService;
	}

	public void setWorkflowPendenciaService(
			WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setHistoricoFilialService(
			HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}

	public void setAlineaService(AlineaService alineaService) {
		this.alineaService = alineaService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

   }