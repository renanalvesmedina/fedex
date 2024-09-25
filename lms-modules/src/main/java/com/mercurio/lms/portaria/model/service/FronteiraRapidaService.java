package com.mercurio.lms.portaria.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.portaria.model.AcaoIntegracao;
import com.mercurio.lms.portaria.model.AcaoIntegracaoEvento;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.GrupoEconomico;


/**
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.portaria.fronteiraRapidaService"
 */
public class FronteiraRapidaService {
	private ManifestoService manifestoService;
	private ConhecimentoService conhecimentoService;
	private AcaoIntegracaoService acaoIntegracaoService;
	private AcaoIntegracaoEventosService acaoIntegracaoEventosService;
	private DoctoServicoService doctoServicoService;

	private static final String TP_DOCUMENTO_CONTROLE_CARGA = "CCA";
	private static final String TP_DOCUMENTO_MANIFESTO_VIAGEM = "MAV";
	private static final String TP_DOCUMENTO_MANIFESTO_CRT = "CRT";
	
	public long getNrAgrupador() {
		return getAcaoIntegracaoEventosService().findLastAgrupador() + 1;
	}

	private List<AgrupamentoInner> findAcoesIntegracao(Long idDocumento, Long idFilialLogada, String tpDocumento) {
		List<AgrupamentoInner> agrupamento = new ArrayList<AgrupamentoInner>();
		
		if(TP_DOCUMENTO_CONTROLE_CARGA.equals(tpDocumento)){
			//passo 
			// pesquisa pelos manifestos onde e tpManifesto = V (viagem)
			List<Manifesto> listaManifestos = manifestoService.findManifestoByIdControleCarga( idDocumento, idFilialLogada, null, "V");
			
 			for (Manifesto manifesto : listaManifestos) {
				
 				/** Otimização LMS-817 */
 	    		final ProjectionList projection = Projections.projectionList()
 	    			.add(Projections.property("ds.id"), "idDoctoServico")
 	    			.add(Projections.property("ge.id"), "clienteByIdClienteDestinatario.grupoEconomico.idGrupoEconomico");
				
 	    		final Map<String, String> alias = new HashMap<String, String>();
 	    		alias.put("ds.clienteByIdClienteDestinatario", "cd");
 	    		alias.put("cd.grupoEconomico", "ge");
				
 	    		final List<Criterion> criterion = new ArrayList<Criterion>();
 	    		criterion.add(Restrictions.isNotNull("ge.id"));

 	    		final List<DoctoServico> doctoServicoList = doctoServicoService.findDoctoServicoByIdManifesto(manifesto.getIdManifesto(), projection, alias, criterion);

				GrupoEconomico grupoEconomico = null;
				for (DoctoServico doctoServico : doctoServicoList) {
					grupoEconomico = doctoServico.getClienteByIdClienteDestinatario().getGrupoEconomico();
					if(grupoEconomico != null && grupoEconomico.getIdGrupoEconomico() != null){
						break;
					}
				}
				
				AgrupamentoInner agrupamentoInner = new AgrupamentoInner();
				agrupamentoInner.lAcaoIntegracaoManifesto = findAcoesIntegracao(manifesto, grupoEconomico);
 
				List<Conhecimento> conhecimentos = conhecimentoService.findConhecimentosByIdManifestoViagemNacional(manifesto.getIdManifesto(), null);
				for (Conhecimento conhecimento : conhecimentos) {
					List<AcaoIntegracaoEvento> le = buscarAcaoIntegracaoPorEvento(conhecimento.getIdDoctoServico(),
																			conhecimento.getFilialByIdFilialOrigem(),
																			conhecimento.getFilialByIdFilialDestino(),
																			conhecimento.getServico().getTpModal(),
																			TP_DOCUMENTO_MANIFESTO_CRT,
																			conhecimento.getClienteByIdClienteRemetente().getGrupoEconomico(),
																			conhecimento.getClienteByIdClienteRemetente());
							
					agrupamentoInner.addAcaoIntegracaoConhecimento(le);
				}
				if (agrupamentoInner.isValid()){
					agrupamento.add(agrupamentoInner);
				}
			}
		}
		
		return agrupamento;
	}

	public List<AcaoIntegracaoEvento> findAcoesIntegracao(Manifesto manifesto, GrupoEconomico grupoEconomico) {
		return buscarAcaoIntegracaoPorEvento(
				manifesto.getIdManifesto(),
				manifesto.getFilialByIdFilialOrigem(),
				manifesto.getFilialByIdFilialDestino(),
				manifesto.getTpModal(),
				TP_DOCUMENTO_MANIFESTO_VIAGEM,
				grupoEconomico,
				null);
	}

	private List<AcaoIntegracaoEvento> buscarAcaoIntegracaoPorEvento(Long idDocumento,
															Filial filialByIdFilialOrigem, 
															Filial filialByIdFilialDestino,
															DomainValue tpModal, 
															String tpDocumento, 
															GrupoEconomico grupoEconomico,
															Cliente cliente) {
		
		List<AcaoIntegracao> lacaointegracao = getAcaoIntegracaoService().findAcaoIntegracaoPorEvento(filialByIdFilialOrigem, 
																filialByIdFilialDestino, 
																tpModal, 
																tpDocumento, 
																grupoEconomico, 
																cliente);
		
		List<AcaoIntegracaoEvento> listInner = new ArrayList<AcaoIntegracaoEvento>();
		for (AcaoIntegracao acaoIntegracao : lacaointegracao) {
			AcaoIntegracaoEvento acaoIntegracaoEventos = new AcaoIntegracaoEvento();
			acaoIntegracaoEventos.setNrDocumento(idDocumento);
			acaoIntegracaoEventos.setAcaoIntegracao(acaoIntegracao);
			acaoIntegracaoEventos.setTpDocumento(acaoIntegracao.getTpDocumento());
			acaoIntegracaoEventos.setDhGeracaoTzr(JTDateTimeUtils.getDataHoraAtual().toString());
			acaoIntegracaoEventos.setDsInformacao(acaoIntegracao.getDsAcaoIntegracao());
			listInner.add(acaoIntegracaoEventos);
		}
		
		
		return listInner;
	}

	public ManifestoService getManifestoService() {
		return manifestoService;
	}
	
	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
	
	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setAcaoIntegracaoService(AcaoIntegracaoService acaoIntegracaoService) {
		this.acaoIntegracaoService = acaoIntegracaoService;
	}

	public AcaoIntegracaoService getAcaoIntegracaoService() {
		return acaoIntegracaoService;
	}

	public void setAcaoIntegracaoEventosService(
			AcaoIntegracaoEventosService acaoIntegracaoEventosService) {
		this.acaoIntegracaoEventosService = acaoIntegracaoEventosService;
	}

	public AcaoIntegracaoEventosService getAcaoIntegracaoEventosService() {
		return acaoIntegracaoEventosService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}
	
	
}
class AgrupamentoInner{
	List<AcaoIntegracaoEvento> lAcaoIntegracaoManifesto = new ArrayList<AcaoIntegracaoEvento>();
	Map<AcaoIntegracao, List<AcaoIntegracaoEvento>> lAcaoIntegracaoConhecimentos = new HashMap<AcaoIntegracao, List<AcaoIntegracaoEvento>>();
	
	public void setAgrupador(Long agrupador) {
		
		// percorre todas integracoes de manifestos e seta o agrupador
		for (AcaoIntegracaoEvento acaoIntegracaoEventos: lAcaoIntegracaoManifesto) {
			acaoIntegracaoEventos.setNrAgrupador(agrupador);
			agrupador++;
			
			// se existe integracao de conhecimento do mesmo  tipo (acaointegracao), agrupa todos com o mesmo numero, que o incremento do agrupador de manifesto
			if(lAcaoIntegracaoConhecimentos.containsKey(acaoIntegracaoEventos.getAcaoIntegracao())){
				for (AcaoIntegracaoEvento acaoIntegracaoEventosConhecimento : lAcaoIntegracaoConhecimentos.get(acaoIntegracaoEventos.getAcaoIntegracao())) {
					acaoIntegracaoEventos.setNrAgrupador(agrupador);
				}
				agrupador++;
			}			
		}
		
		// percorre integracao de conhecimento procurando algum que nao tenha sido preenchido
		FOR_INTEGRACAO_EVENTOS:
		for (List<AcaoIntegracaoEvento> acaoIntegracaoEventos  : lAcaoIntegracaoConhecimentos.values()) {
			for (AcaoIntegracaoEvento acaoIntegracaoEvento : acaoIntegracaoEventos) {
				if(acaoIntegracaoEvento.getNrAgrupador() == null){
					acaoIntegracaoEvento.setNrAgrupador(agrupador);
				}else{
					continue FOR_INTEGRACAO_EVENTOS;
				}
			}
			agrupador++;
		}
		
	}
	
	public boolean isValid() {
		return !this.lAcaoIntegracaoManifesto.isEmpty() || !this.lAcaoIntegracaoConhecimentos.isEmpty();
	}

	// separa os eventos pelo acao_integracao.
	public void addAcaoIntegracaoConhecimento(List<AcaoIntegracaoEvento> lacaointegracaoConhecimento){
		for (AcaoIntegracaoEvento acaoIntegracaoEventosConhecimento : lacaointegracaoConhecimento) {
			List<AcaoIntegracaoEvento> laie = null;
			if(lAcaoIntegracaoConhecimentos.containsKey(acaoIntegracaoEventosConhecimento.getAcaoIntegracao())){
				laie = lAcaoIntegracaoConhecimentos.get(acaoIntegracaoEventosConhecimento.getAcaoIntegracao());
			} else {
				laie = new ArrayList<AcaoIntegracaoEvento>();
				lAcaoIntegracaoConhecimentos.put(acaoIntegracaoEventosConhecimento.getAcaoIntegracao(), laie);
			}
			
			laie.add(acaoIntegracaoEventosConhecimento);
		}
	}
	
}