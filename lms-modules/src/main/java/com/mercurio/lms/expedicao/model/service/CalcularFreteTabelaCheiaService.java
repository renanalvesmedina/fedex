package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.DocumentoServicoFacade;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.Frete;
import com.mercurio.lms.expedicao.util.CalculoFreteUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.vendas.util.ConstantesVendas;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.calcularFreteTabelaCheiaService"
 */
public class CalcularFreteTabelaCheiaService {
	
	private ConhecimentoService conhecimentoService;
	private ConhecimentoNormalService conhecimentoNormalService;
	
	private DocumentoServicoFacade documentoServicoFacade;    
	private CalcularFreteService calcularFreteService;
	
	protected void executeConhecimentoCalculoFreteTabelaCheia(Long idConhecimento) {
		
		// Faço load do conhecimento através do ID, busco de Cto em Cto e não materializo todos os X ctos passados no rownum. 
		// A idéia de ir de pouco em pouco, em vez de trabalhar com um blocos enormes de dados gerando consumo de memória...
		Conhecimento conhecimento = conhecimentoService.findById(idConhecimento);

		if (conhecimento.getTpCtrcParceria() == null) {
			
			// Sessão para aplicar EVICT no CTO modificado...
			Session session = conhecimentoService.getConhecimentoDAO().getAdsmHibernateTemplate().getSessionFactory().getCurrentSession();
			
			CalculoFrete calculoFrete = new CalculoFrete();
			calculoFrete.setClienteBase(conhecimento.getClienteByIdClienteBaseCalculo());
			calculoFrete.setIdDivisaoCliente(conhecimento.getDivisaoCliente() != null ? conhecimento.getDivisaoCliente().getIdDivisaoCliente() : null);

			calculoFrete.setBlCalculaParcelas(Boolean.TRUE);
			calculoFrete.setBlCalculaServicosAdicionais(conhecimento.getBlServicosAdicionais());
			calculoFrete.setBlCalculoFreteTabelaCheia(Boolean.TRUE);
			calculoFrete.setIdServico(conhecimento.getServico().getIdServico());

			conhecimento.setTpCalculoPreco(new DomainValue(ConstantesExpedicao.CALCULO_NORMAL));
			conhecimento.getClienteByIdClienteRemetente().setTpCliente(new DomainValue(ConstantesVendas.CLIENTE_POTENCIAL));
			
			Frete frete = new Frete(); 
			frete.setConhecimento(conhecimento);
			frete.setCalculoFrete(calculoFrete);

			Map map = new HashMap();
			/*Configura os dados do calculo de frete atraves do conhecimento*/
			conhecimentoNormalService.configureCalculoFrete(frete.getConhecimento(),frete.getCalculoFrete());

			/*Executa o cálculo do frete*/			
			documentoServicoFacade.executeCalculoConhecimentoNacionalNormal(frete.getCalculoFrete());		

			/*Copia os dados do calculo de frete para o conhecimento*/
			CalculoFreteUtils.copyResult(frete.getConhecimento(),frete.getCalculoFrete());

			/*Retorna o map contendo todos os valores calculados do frete*/
			map = calcularFreteService.montarParcelasCalculo(frete.getConhecimento(),frete.getCalculoFrete());

			session.evict(frete.getConhecimento());

			//Após o calculo ter sido feito acima, conforme o map, busco o conhecimento em seu estado original, 
			//já que para calcular o frete a rotina realiza modificações/alterações dos dados do conhecimento
			conhecimento = conhecimentoService.findByIdInitLazyProperties(conhecimento.getIdDoctoServico(), false);
			
			//Após buscar o conhecimento original, apenas seto no vlFreteTabelaCheia o novo valor de calculo de frete 
			conhecimento.setVlFreteTabelaCheia((BigDecimal)map.get("vlTotalFrete"));
		} else {
			conhecimento.setVlFreteTabelaCheia(conhecimento.getVlLiquido());
		}

		//Gravo de cto em cto
		conhecimentoService.store(conhecimento);
	}
	
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	
	public void setConhecimentoNormalService(ConhecimentoNormalService conhecimentoNormalService) {
		this.conhecimentoNormalService = conhecimentoNormalService;
	}
	
	public void setCalcularFreteService(CalcularFreteService calcularFreteService) {
		this.calcularFreteService = calcularFreteService;
	}
	
	public void setDocumentoServicoFacade(DocumentoServicoFacade documentoServicoFacade) {
		this.documentoServicoFacade = documentoServicoFacade;
	}
}