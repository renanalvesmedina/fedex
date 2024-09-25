package com.mercurio.lms.contasreceber.model.service;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.util.DataVencimentoService;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.service.ManifestoViagemNacionalService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DiaFaturamento;
import com.mercurio.lms.vendas.model.service.DiaFaturamentoService;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import org.joda.time.YearMonthDay;

import java.util.List;


/**
 * Classe de serviço para CRUD:
 * <p>
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 *
 * @spring.bean id="lms.contasreceber.gerarBoletoReciboManifestoService"
 */
public class GerarBoletoReciboManifestoService {
	public static final String FRETE_FOB = "F";
	public static final String PERIODICIDADE_DIARIA = "D";
	private static final String TIPO_COBRANCA_BANCO_BOLETO = "3";
	private static final String TIPO_COBRANCA_BANCO_FATURA_BOLETO = "4";
	private GerarFaturaManifestoService gerarFaturaManifestoService;
	private DiaFaturamentoService diaFaturamentoService;
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
	private DevedorDocServFatService devedorDocServFatService;
	private ManifestoService manifestoService;
	private FaturaService faturaService;
	private BoletoService boletoService;
	private ManifestoViagemNacionalService manifestoViagemNacionalService;
	private AgendaTransferenciaService agendaTransferenciaService;
	private TransferenciaService transferenciaService;
	private DataVencimentoService dataVencimentoService;
	private DescontoService descontoService;

	public void generateFaturamentoManifestoEntrega(Manifesto manifesto, ManifestoEntregaDocumento med, Conhecimento conhecimento) {

		//Se o manifesto é válido e que o tipo de frete do conhecimento for 'FOB'
		if (validateManifestoEntrega(manifesto) && FRETE_FOB.equals(conhecimento.getTpFrete().getValue())) {

			//Salvar o manifesto
			manifestoEntregaDocumentoService.store(med);
			manifestoEntregaDocumentoService.flush();
			//Buscar o devedor
			DevedorDocServFat devedorDocServFat = devedorDocServFatService.findByManifestoEntregaDocumento(med.getIdManifestoEntregaDocumento());
			if (devedorDocServFat == null) {
				return;
			}

			DoctoServico doctoServico = devedorDocServFat.getDoctoServico();
			Boolean blEmiteBoletoCliDestino = doctoServico.getClienteByIdClienteRemetente().getBlEmiteBoletoCliDestino();


			Cliente cliente = devedorDocServFat.getCliente();

			if (verificaExistenciaAgenciaOuTransferenciaOuDesconto(devedorDocServFat)) {
				return;
			}

			//Indica se o devedor está pendente de cobrança
			preparaGeracaoFatura(manifesto, med, devedorDocServFat, blEmiteBoletoCliDestino, cliente);
			manifestoEntregaDocumentoService.store(med);
		}
	}

	private boolean verificaExistenciaAgenciaOuTransferenciaOuDesconto(DevedorDocServFat devedorDocServFat) {

		List descontosPendentes = descontoService.findPendentesByDevedorDocServFat(devedorDocServFat.getIdDevedorDocServFat());
		if (descontosPendentes != null && !descontosPendentes.isEmpty()) {
			return true;
		}

		List listaa = agendaTransferenciaService.findByDevedorDocServFat(devedorDocServFat.getIdDevedorDocServFat());
		List listat = transferenciaService.findByIdDevedorDocServFat(devedorDocServFat.getIdDevedorDocServFat());

		Boolean blAgendaTransfer = listaa != null ? !listaa.isEmpty() : false;
		Boolean blAgenda = listat != null ? !listat.isEmpty() : false;

		//Encontrou uma AGENDA_TRANSFERENCIA ou TRANSFERENCIA? Ler o próximo documento.
		if (blAgendaTransfer || blAgenda) {
			return true;
        }
		return false;
	}

	private void preparaGeracaoFatura(Manifesto manifesto, ManifestoEntregaDocumento med, DevedorDocServFat devedorDocServFat, Boolean blEmiteBoletoCliDestino, Cliente cliente) {
		Boolean blPendente = "P".equals(devedorDocServFat.getTpSituacaoCobranca().getValue()) || "C".equals(devedorDocServFat.getTpSituacaoCobranca().getValue());

		if(clienteTpCobrancaBoleto(cliente)) {
			//Se o tipo de cliente é 'Eventual' ou 'Potencial' ou possui uma divisão
			if (ConstantesVendas.CLIENTE_EVENTUAL.equals(cliente.getTpCliente().getValue())
					|| ConstantesVendas.CLIENTE_POTENCIAL.equals(cliente.getTpCliente().getValue())
					|| devedorDocServFat.getDivisaoCliente() == null
					) {
				//Buscar ou gerar a fatura do devedor
				geraFaturaClienteNormal(manifesto, med, devedorDocServFat, blEmiteBoletoCliDestino, blPendente);
				//Senão é 'Especial'
			} else {
				geraFaturaClienteEspecial(manifesto, med, devedorDocServFat, blPendente);
			}
		}
	}

	private boolean clienteTpCobrancaBoleto(Cliente cliente) {
		String tpCobrancaCliente = cliente.getTpCobranca().getValue();
		return TIPO_COBRANCA_BANCO_BOLETO.equals(tpCobrancaCliente)  || TIPO_COBRANCA_BANCO_FATURA_BOLETO.equals(tpCobrancaCliente);
	}

	private void geraFaturaClienteNormal(Manifesto manifesto, ManifestoEntregaDocumento med, DevedorDocServFat devedorDocServFat, Boolean blEmiteBoletoCliDestino, Boolean blPendente) {
		Fatura fatura = generateFaturaEntrega(devedorDocServFat, manifesto.getManifestoEntrega(), blPendente);

		//Se não teve problema na busca da fatura
		if (fatura != null) {
            //Se o cliente emita boleto ao destino ou se não emite recibo de frete
            if (blEmiteBoletoCliDestino || Boolean.FALSE.equals(manifesto.getFilialByIdFilialOrigem().getBlEmiteReciboFrete())) {
                generateBoleto(fatura, med, blPendente);
                med.setTpDocumentoCobranca(new DomainValue("B"));
            }
            faturaService.storeBasic(fatura);
        }
	}

	private void geraFaturaClienteEspecial(Manifesto manifesto, ManifestoEntregaDocumento med, DevedorDocServFat devedorDocServFat, Boolean blPendente) {
		Fatura fatura = null;
		//Se a filial emite boleto de faturamento e que o dia de faturamento do cliente for 'Diario'
		if (Boolean.TRUE.equals(manifesto.getFilialByIdFilialOrigem().getBlEmiteBoletoFaturamento()) && possuiDiaFaturamentoComPeriodicidadeDiaria(devedorDocServFat)) {
            //Buscar ou gerar a fatura do devedor
            fatura = generateFaturaEntrega(devedorDocServFat, manifesto.getManifestoEntrega(), blPendente);
        }
		//Se não teve problema na busca da fatura
		if (fatura != null) {
            generateBoleto(fatura, med, blPendente);
            med.setTpDocumentoCobranca(new DomainValue("B"));
            faturaService.storeBasic(fatura);
        }
	}

	private boolean possuiDiaFaturamentoComPeriodicidadeDiaria(DevedorDocServFat devedorDocServFat) {
		List<DiaFaturamento> diasFaturamento =  diaFaturamentoService.findByDivisaoTpPeriodicidade(devedorDocServFat.getDivisaoCliente().getIdDivisaoCliente(), PERIODICIDADE_DIARIA);
		for (DiaFaturamento diaFaturamento : diasFaturamento) {
			if(diaFaturamento.getTpFrete() == null || FRETE_FOB.equals(diaFaturamento.getTpFrete().getValue())) {
				return true;
			}
		}
		return false;
	}

	public void generateFaturamentoManifestoViagem(Manifesto manifesto, ManifestoNacionalCto mnc) {

		//Se o manifesto é válido e que o tipo de frete do conheciment for 'FOB' e que (o tipo de CTRC parceria for 'Master' OU que o cliente de redespacho for diferente de nulo)
		if (validateManifestoViagem(manifesto)) {
			manifestoService.storeManifesto(manifesto);

			DevedorDocServFat devedorDocServFat = devedorDocServFatService.findByManifestoNacionalCto(mnc.getIdManifestoNacionalCto());
			if (devedorDocServFat != null) {
				//Indica se o devedor está pendente de cobrança
				Boolean blPendente = "P".equals(devedorDocServFat.getTpSituacaoCobranca().getValue()) || "C".equals(devedorDocServFat.getTpSituacaoCobranca().getValue());
				generateFaturaViagem(devedorDocServFat, manifesto, blPendente);
			}
		}
	}

	/**
	 * Se é válido, retorna true.

	 * @author Mickaël Jalbert
	 * @since 01/11/2006
	 *
	 * Pedido pelo Rodrigo, o campo vem nulo agora (tem a ver com o pré-manifesto do GT3)
	 */
	private Boolean validateManifestoEntrega(Manifesto manifesto) {

		//Se o tipo de manifesto é de 'Entrega' e a filial do manifesto não emite boleto e a filial do manifesto não emite recibo
		if (!"E".equals(manifesto.getTpManifesto().getValue()) ||
				!manifesto.getFilialByIdFilialOrigem().getBlEmiteBoletoEntrega().equals(Boolean.FALSE) ||
				!manifesto.getFilialByIdFilialOrigem().getBlEmiteReciboFrete().equals(Boolean.FALSE)) {
			return true;
		}

		return false;
	}

	/**
	 * Se é válido, retorna true.
	 */
	private Boolean validateManifestoViagem(Manifesto manifesto) {
		Boolean blRetorno = Boolean.TRUE;

		//Se o tipo de abrangência do manifesto é diferente de 'nacional'
		if (!"N".equals(manifesto.getTpAbrangencia().getValue())) {
			blRetorno = Boolean.FALSE;
		}

		//Se o tipo de manifesto é de 'Viagem' e a filial do manifesto não tem cedente
		if ("V".equals(manifesto.getTpManifesto().getValue()) && manifesto.getFilialByIdFilialOrigem().getCedenteByIdCedente() == null) {
			blRetorno = Boolean.FALSE;
		}

		return blRetorno;
	}

	private Fatura generateFaturaEntrega(DevedorDocServFat devedorDocServFat, ManifestoEntrega manifestoEntrega, Boolean blPendente) {
		Fatura fatura;

		//Se o devedorestá pendente de cobrança
		if (blPendente) {
			//Chamar o generateFatura
			fatura = gerarFaturaManifestoService.storeFaturaWithDevedorDocServFat(new Fatura(), devedorDocServFat);
		} else {
			//Atualizar a fatura com o manifesto
			fatura = faturaService.findByDevedorDocServFat(devedorDocServFat.getIdDevedorDocServFat());
		}

		if (fatura != null) {
			//Se o manifesto de entrega de origem está nulo, setar ele
			if (fatura.getManifestoEntregaOrigem() == null) {
				fatura.setManifestoEntregaOrigem(manifestoEntrega);
				//Indica que foi gerada pelo manifesto de entrega
				fatura.setTpOrigem(new DomainValue("E"));
			}
			fatura.setManifestoEntrega(manifestoEntrega);
		}

		return fatura;
	}

	private void generateFaturaViagem(DevedorDocServFat devedorDocServFat, Manifesto manifesto, Boolean blPendente) {
		Fatura fatura;

		//Se o devedorestá pendente de cobrança
		if (blPendente) {
			YearMonthDay dtVencimento;

			//Verifica se o cliente é eventual ou potencial
			if ("EP".indexOf(devedorDocServFat.getCliente().getTpCliente().getValue()) >= 0) {
				dtVencimento = calcularPrazoVctoCliEventualBoletoManifesto(devedorDocServFat.getDoctoServico());
			} else {
				dtVencimento = calcularPrazoVctoCliEspecialBoletoManifesto(devedorDocServFat.getDoctoServico(), devedorDocServFat);
			}

			//Chamar o generateFatura
			fatura = gerarFaturaManifestoService.storeFaturaWithDevedorDocServFat(new Fatura(), devedorDocServFat);
			//LMS-1013
			if (fatura == null) {
				return;
			}
			faturaService.flush();
			fatura.setDtVencimento(dtVencimento);
			fatura.setCedente(manifesto.getFilialByIdFilialOrigem().getCedenteByIdCedente());
			fatura.setManifestoOrigem(manifesto);

			generateBoleto(fatura);
		} else {
			//Atualizar a fatura com o manifesto
			fatura = faturaService.findByDevedorDocServFat(devedorDocServFat.getIdDevedorDocServFat());

		}

		if (fatura != null &&
				manifesto.getFilialByIdFilialOrigem().getIdFilial().equals(fatura.getFilialByIdFilial().getIdFilial())) {
				fatura.setManifesto(manifesto);
		}

		faturaService.store(fatura);
	}

	/**
	 * Calcular Prazo de Vencimento
	 *
	 * @param doctoServico
	 * @param devedorDocServ
	 * @return
	 */
	private YearMonthDay calcularPrazoVctoCliEspecialBoletoManifesto(DoctoServico doctoServico, DevedorDocServFat devedorDocServ) {
		YearMonthDay dtVencimento = dataVencimentoService.generateDataVencimento(
				doctoServico.getFilialByIdFilialOrigem().getIdFilial(),
				devedorDocServ.getDivisaoCliente().getIdDivisaoCliente(),
				"F",
				JTDateTimeUtils.getDataAtual(),
				devedorDocServ.getDoctoServico().getServico().getTpModal().getValue(),
				devedorDocServ.getDoctoServico().getServico().getTpAbrangencia().getValue(),
				devedorDocServ.getDoctoServico().getServico().getIdServico());

		if (doctoServico.getNrDiasPrevEntrega() != null) {
			dtVencimento = dtVencimento.plusDays(doctoServico.getNrDiasPrevEntrega());
		}

		return dtVencimento;
	}

	/**
	 * Calcula Prazo de Vencimento
	 *
	 * @param doctoServico
	 * @return
	 */
	private YearMonthDay calcularPrazoVctoCliEventualBoletoManifesto(DoctoServico doctoServico) {
		Integer diasDeslocamento = 0;
		if (doctoServico.getFilialByIdFilialOrigem().getNrPrazoCobranca() != null){
			diasDeslocamento = doctoServico.getFilialByIdFilialOrigem().getNrPrazoCobranca().intValue();
		}

		YearMonthDay dtVencimento = JTDateTimeUtils.getDataAtual();
		if (doctoServico.getDtPrevEntrega() != null) {
			dtVencimento = doctoServico.getDtPrevEntrega().plusDays(diasDeslocamento.intValue());
		}

		return dtVencimento;
	}

	private void generateBoleto(Fatura fatura, ManifestoEntregaDocumento manifestoEntregaDocumento, Boolean blPendente) {
		//Se o devedorestá pendente de cobrança
		if (blPendente) {
			//Chamar o generateBoleto
			generateBoleto(fatura);
		}

		//Atualizar o tipo de documento de cobrança do manifesto
		manifestoEntregaDocumento.setTpDocumentoCobranca(new DomainValue("B"));
	}

	private void generateBoleto(Fatura fatura) {
		boletoService.generateBoletoDeFatura(fatura);
	}

	public void setGerarFaturaManifestoService(GerarFaturaManifestoService gerarFaturaManifestoService) {
		this.gerarFaturaManifestoService = gerarFaturaManifestoService;
	}

	public void setDiaFaturamentoService(DiaFaturamentoService diaFaturamentoService) {
		this.diaFaturamentoService = diaFaturamentoService;
	}

	public void setDevedorDocServFatService(DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
		this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public AgendaTransferenciaService getAgendaTransferenciaService() {
		return agendaTransferenciaService;
	}

	public void setAgendaTransferenciaService(AgendaTransferenciaService agendaTransferenciaService) {
		this.agendaTransferenciaService = agendaTransferenciaService;
	}

	public TransferenciaService getTransferenciaService() {
		return transferenciaService;
	}

	public void setTransferenciaService(TransferenciaService transferenciaService) {
		this.transferenciaService = transferenciaService;
	}

	public void setDataVencimentoService(DataVencimentoService dataVencimentoService) {
		this.dataVencimentoService = dataVencimentoService;
	}

	public void setDescontoService(DescontoService descontoService) {
		this.descontoService = descontoService;
	}

	public ManifestoViagemNacionalService getManifestoViagemNacionalService() {
		return manifestoViagemNacionalService;
	}

	public void setManifestoViagemNacionalService(ManifestoViagemNacionalService manifestoViagemNacionalService) {
		this.manifestoViagemNacionalService = manifestoViagemNacionalService;
	}

	public ManifestoService getManifestoService() {
		return manifestoService;
	}

	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
}