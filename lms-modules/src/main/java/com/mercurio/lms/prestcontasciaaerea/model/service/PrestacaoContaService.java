package com.mercurio.lms.prestcontasciaaerea.model.service;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.util.DateTimeUtils;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.prestcontasciaaerea.model.FaturamentoCiaAerea;
import com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta;
import com.mercurio.lms.prestcontasciaaerea.model.dao.PrestacaoContaDAO;
import com.mercurio.lms.tributos.model.CalcularPisCofinsCsllIrInss;
import com.mercurio.lms.tributos.model.service.CalcularIssService;
import com.mercurio.lms.tributos.model.service.CalcularPisCofinsCsllIrInssService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.prestcontasciaaerea.prestacaoContaService"
 */
public class PrestacaoContaService extends CrudService<PrestacaoConta, Long> {
	private ConfiguracoesFacade configuracoesFacade;
	private EnderecoPessoaService enderecoPessoaService;
	private CalcularPisCofinsCsllIrInssService calcularPisCofinsCsllIrInssService;
	private CalcularIssService calcularIssService;
	private PessoaService pessoaService;
	private ValorPrestacaoContaService valorPrestacaoContaService;
	private FaturamentoCiaAereaService faturamentoCiaAereaService;
	/**
	 * Busca valor do IRRF Sobre Comissão, pela Prestacao de Conta informada.<BR>
	 * Para empresa Varig é realizado o cálculo no Pis/Cofins/Csll/Ir/Inss, retornando o valor do imposto do IR.<BR>
	 * Para empresa GOL ou TAM é realizado o cálculo do ISS, retornando o valor do ISS.<BR>
	 *@author Robson Edemar Gehl
	 * @return
	 */
	public BigDecimal findIRRFSobreComissao(Long idPrestacaoConta){
		PrestacaoConta prestacaoConta = findById(idPrestacaoConta);

		Long idFilial = prestacaoConta.getCiaFilialMercurio().getFilial().getIdFilial();
		Long idEmpresa = prestacaoConta.getCiaFilialMercurio().getEmpresa().getIdEmpresa();
		BigDecimal vlComissao = findComissaoSobreFrete(idPrestacaoConta, idFilial, idEmpresa);

		// Se não tem comissão, retorna null
		if(vlComissao == null)
			return null;

		BigDecimal idServicoTributo = (BigDecimal) configuracoesFacade.getValorParametro("ID_AGENCIAMENTO_CARGA_AEREA");

		//Busca dos parametros a serem utilizados na regra
		BigDecimal idVarig = (BigDecimal) configuracoesFacade.getValorParametro("ID_VARIG");	
		BigDecimal idGol = (BigDecimal) configuracoesFacade.getValorParametro("ID_GOL");	
		BigDecimal idTam = (BigDecimal) configuracoesFacade.getValorParametro("ID_TAM");

		if(idEmpresa.equals(idVarig.longValue())) {
			List list = calcularPisCofinsCsllIrInssService.calcularPisCofinsCsllIrInssPessoaJudirica(
				idFilial,
				"I",
				"ME",
				null, 
				idServicoTributo.longValue(),
				prestacaoConta.getDtEmissao(), 
				vlComissao 
			);

			Iterator iter = list.iterator();
			CalcularPisCofinsCsllIrInss inss = null;
			while (iter.hasNext()) {
				inss = (CalcularPisCofinsCsllIrInss) iter.next();
				if ("IR".equals(inss.getTpImpostoCalculado())){
					break;
				}
			}
			if(inss != null) {
				return inss.getVlImposto();
			}
		} else if (idEmpresa.equals(idGol.longValue()) || idEmpresa.equals(idTam.longValue())) {
			EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idFilial);

			Map map = calcularIssService.calcularIss(
				idFilial,
				enderecoPessoa.getMunicipio().getIdMunicipio(), 
				enderecoPessoa.getMunicipio().getIdMunicipio(), 
				null, 
				idServicoTributo.longValue(),
				prestacaoConta.getDtEmissao(), 
				vlComissao
			);
			if (map != null && map.containsKey("vlIss")){
				return (BigDecimal) map.get("vlIss");
			}
		}
		return null;
	}
	
	/**
	 * Busca Valor da Comissão Sobre Frete.<BR>
	 * Quando a Filial não for Manaus e a Empresa não for GOL, o Valor do Frete é o somatório do Valor do Tipo de Prestacao de Conta, 
	 * onde o Tipo do Valor é Frete.<BR> 
	 *@author Robson Edemar Gehl
	 * @param idFilial
	 * @param idEmpresa
	 * @return Total Frete * PC_COMISSAO_AEREO (Parâmetro geral) / 100
	 */
	public BigDecimal findComissaoSobreFrete(Long idPrestacaoConta, Long idFilial, Long idEmpresa){
		BigDecimal idGol = (BigDecimal) configuracoesFacade.getValorParametro("ID_GOL");	
		Pessoa pessoa = pessoaService.findById(idFilial);

		//Manaus
		if ( !("95591723011587".equals(pessoa.getNrIdentificacao()) && idEmpresa.equals(idGol.longValue()))){
			BigDecimal vlFrete = null;
			Iterator totais = valorPrestacaoContaService.findTotaisByTpValor(idPrestacaoConta).iterator();
			while (totais.hasNext()) {
				Map vpc = (Map) totais.next();
				if (vpc.get("tpValor").equals("FR")) {
					vlFrete = (BigDecimal) vpc.get("vlTipoPrestacaoConta");
					vlFrete = vlFrete.multiply(faturamentoCiaAereaService.findPcComissaoCiaAerea(idEmpresa, idFilial).divide(BigDecimal.valueOf(100)));
					break;
				}
			}

			BigDecimal pcComissaoAereo = (BigDecimal) configuracoesFacade.getValorParametro("PC_COMISSAO_AEREO");	
			if (vlFrete != null && pcComissaoAereo != null) {
				return vlFrete.setScale(2,BigDecimal.ROUND_DOWN).multiply(pcComissaoAereo.setScale(2)).divide(new BigDecimal(100), BigDecimal.ROUND_DOWN);	
			}
		}
		return null;
	}

	/**
	 * Calcula o valor total a pagar para o Agente.<BR>
	 * @author Robson Edemar Gehl
	 * @param vlFrete
	 * @param vlIRRF
	 * @param vlComissaoFrete
	 * @return Total Frete + IRRF Sobre Comissão - Comissão sobre frete
	 */
	public BigDecimal findValorPagarAgente(BigDecimal vlFrete, BigDecimal vlIRRF, BigDecimal vlComissaoFrete){
		if (vlFrete != null && vlIRRF != null && vlComissaoFrete != null){
			return vlFrete.add(vlIRRF).subtract(vlComissaoFrete).setScale(2);
		}
		return null;
	}

	public YearMonthDay findDtFinalPeriodo(Long idEmpresaCiaAerea, Long idFilial, YearMonthDay dtinicio, String tpEmissao){
		FaturamentoCiaAerea bean = faturamentoCiaAereaService.findFaturamentoCiaAereaVigente(idEmpresaCiaAerea, idFilial);
		
		if(bean == null){
			throw new BusinessException("LMS-37005");
		}
		
		YearMonthDay dtReturn = dtinicio;
		
		if(bean.getTpPeriodicidade().getValue().equals("D")) {
			dtReturn = dtinicio;
		} else if(bean.getTpPeriodicidade().getValue().equals("S")) {
			if((DateTimeUtils.getNroDiaSemana(dtinicio.toDateMidnight().toDate())-1) != bean.getDdFaturamento())
			{
				throw new BusinessException("LMS-37007");
			}
			dtReturn = dtReturn.plusDays(6);
		} else if(bean.getTpPeriodicidade().getValue().equals("E")){
			boolean isInvalid = true;
			int day = dtinicio.getDayOfMonth();
			for(int i=0; i<3; i++)	
			{
				if(bean.getDdFaturamento() == day){
					
					isInvalid = false;					
				}
				else{
					day += 10;
				}
			}
			if(isInvalid){
				throw new BusinessException("LMS-37007");
			}
			if(dtinicio.getDayOfMonth() >= 20){
				dtReturn = new YearMonthDay(dtinicio.getYear(),dtinicio.getMonthOfYear(),bean.getDdFaturamento());
				dtReturn = dtReturn.plusMonths(1).minusDays(1);				
			}
			else{
				dtReturn = dtReturn.plusDays(10);				
			}
		} else if(bean.getTpPeriodicidade().getValue().equals("Q")) {
			boolean isInvalid = true;
			int day = dtinicio.getDayOfMonth();
			for(int i=0; i<2; i++)	
			{
				if(bean.getDdFaturamento() == day){
					
					isInvalid = false;					
				}
				else{
					day += 15;
				}
			}
			if(isInvalid){
				throw new BusinessException("LMS-37007");
			}
			if(bean.getDdFaturamento() == dtinicio.getDayOfMonth()){
				dtReturn = dtReturn.plusDays(14);
			}
			else{
				dtReturn = new YearMonthDay(dtinicio.getYear(),dtinicio.getMonthOfYear(),bean.getDdFaturamento());
				dtReturn = dtReturn.plusMonths(1).minusDays(1);								
			}			
			
		} else if(bean.getTpPeriodicidade().getValue().equals("M")) {
			if(bean.getDdFaturamento() != dtinicio.getDayOfMonth()){
				throw new BusinessException("LMS-37007");
			}
			dtReturn = new YearMonthDay(dtinicio.getYear(),dtinicio.getMonthOfYear(),bean.getDdFaturamento());
			dtReturn = dtReturn.plusMonths(1).minusDays(1);				
		}		
		return dtReturn;
	}	
	
	/**
	 * Busca as Prestacoes de Conta pelos parametros informados.<BR>
	 * @author Robson Edemar Gehl
	 * @param idCiaAerea
	 * @param idFilial
	 * @param dtInicial
	 * @param dtFinal
	 */
	public List findPrestacaoContaByUnique(Long idCiaAerea, Long idFilial, Long nrPrestacaoConta, YearMonthDay dtInicial, YearMonthDay dtFinal){
		return getPrestacaoContaDAO().findPrestacaoContaByUnique(idCiaAerea, idFilial, nrPrestacaoConta, dtInicial, dtFinal);
	}

	/**
	 * Recupera uma instância de <code>PrestacaoConta</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public PrestacaoConta findById(java.lang.Long id) {
		return (PrestacaoConta)super.findById(id);
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

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(PrestacaoConta bean) {
		return super.store(bean);
	}

	/**
	 * Busca o id da prestação de contas que deve ser excluída
	 * @param idEmpresa
	 * @param nrPrestacaoConta
	 * @return Id da prestacao de contas
	 */
	public PrestacaoConta findDesmarcar(Long idEmpresa, Long nrPrestacaoConta){
		Long idFilialUserLogged = SessionUtils.getFilialSessao().getIdFilial();
		
		return this.getPrestacaoContaDAO().findPrestacaoContaDesmarcar(idEmpresa, nrPrestacaoConta, idFilialUserLogged);
		
	}

	private final PrestacaoContaDAO getPrestacaoContaDAO() {
		return (PrestacaoContaDAO) getDao();
	}
	public void setPrestacaoContaDAO(PrestacaoContaDAO dao) {
		setDao( dao );
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setCalcularPisCofinsCsllIrInssService(CalcularPisCofinsCsllIrInssService calcularPisCofinsCsllIrInssService) {
		this.calcularPisCofinsCsllIrInssService = calcularPisCofinsCsllIrInssService;
	}
	public void setCalcularIssService(CalcularIssService calcularIssService) {
		this.calcularIssService = calcularIssService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setValorPrestacaoContaService(ValorPrestacaoContaService valorPrestacaoContaService) {
		this.valorPrestacaoContaService = valorPrestacaoContaService;
	}

	public void setFaturamentoCiaAereaService(FaturamentoCiaAereaService faturamentoCiaAereaService) {
		this.faturamentoCiaAereaService = faturamentoCiaAereaService;
	}
}