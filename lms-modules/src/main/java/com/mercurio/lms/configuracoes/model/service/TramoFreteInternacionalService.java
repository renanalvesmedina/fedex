package com.mercurio.lms.configuracoes.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.DistrFreteInternacional;
import com.mercurio.lms.configuracoes.model.TramoFreteInternacional;
import com.mercurio.lms.configuracoes.model.dao.TramoFreteInternacionalDAO;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.TrechoCtoInt;
import com.mercurio.lms.tabelaprecos.model.ValorCruze;
import com.mercurio.lms.tabelaprecos.model.service.ValorCruzeService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.configuracoes.tramoFreteInternacionalService"
 */
public class TramoFreteInternacionalService extends CrudService<TramoFreteInternacional, Long> {
	private ValorCruzeService valorCruzeService;
	private DistrFreteInternacionalService distrFreteInternacionalService;

	/**
	 * Recupera uma instância de <code>TramoFreteInternacional</code> a partir
	 * do ID.
	 * 
	 * @param id
	 *            representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public TramoFreteInternacional findById(java.lang.Long id) {
		return (TramoFreteInternacional) super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 * 
	 * @param id
	 *            indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 * 
	 * @param ids
	 *            lista com as entidades que deverão ser removida.
	 * 
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 * 
	 * @param bean
	 *            entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(TramoFreteInternacional bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância
	 *            do DAO.
	 */
	public void setTramoFreteInternacionalDAO(
			TramoFreteInternacionalDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private TramoFreteInternacionalDAO getTramoFreteInternacionalDAO() {
		return (TramoFreteInternacionalDAO) getDao();
	}
	
	public List findTramosCtoInternacional(Long idFilialOrigem
										   ,Long idFilialDestino
										   ,Long idClienteRemetente){
		return getTramoFreteInternacionalDAO()
			   .findTramosCtoInternacional(idFilialOrigem
					   					   ,idFilialDestino
					   					   ,idClienteRemetente);
	}
	
	public List executeCalculoTrechosCtoInternacional(CtoInternacional ctoInternacional){
		Long idFilialOrigem = ctoInternacional.getFilialByIdFilialOrigem().getIdFilial();
		Long idFilialDestino = ctoInternacional.getFilialByIdFilialDestino().getIdFilial();
		Long idClienteRemetente = ctoInternacional.getClienteByIdClienteRemetente().getIdCliente();
		List trechosCtoInt = null;

		List tramos = findTramosCtoInternacional(idFilialOrigem, idFilialDestino, idClienteRemetente);

		if(tramos != null && !tramos.isEmpty()){
			trechosCtoInt = new ArrayList();
			BigDecimal totalRemetente = BigDecimalUtils.ZERO;
			BigDecimal totalDestinatario = BigDecimalUtils.ZERO;
			BigDecimal totalRemetenteDestinatario = BigDecimalUtils.ZERO;

			List valorCruzes = valorCruzeService.findByVigencia(JTDateTimeUtils.getDataAtual());//Pega todos os valores Cruze da vigencia atual
			BigDecimal psBruto = ctoInternacional.getPsReal();//Usado para pegar o Valor Cruze caso haja
			BigDecimal vlFrete = ctoInternacional.getVlTotalDocServico();//Valor setado apartir do calculo do CRT

			int size = tramos.size();
			long now = -System.currentTimeMillis();
			for(int i = 0; i < size; i++){
				TramoFreteInternacional tramoFreteInternacional = (TramoFreteInternacional) tramos.get(i);

				TrechoCtoInt trechoCtoInt = new TrechoCtoInt();
				trechoCtoInt.setIdTrechoCtoInt(Long.valueOf(--now));
				trechoCtoInt.setVlFreteRemetente(BigDecimalUtils.ZERO);
				trechoCtoInt.setVlFreteDestinatario(BigDecimalUtils.ZERO);

				BigDecimal pcFrete = tramoFreteInternacional.getPcFrete();

				BigDecimal vlValorCruze = BigDecimalUtils.ZERO;
				BigDecimal vlFreteCalculado = BigDecimalUtils.ZERO;

				if(i < (size - 1)){//Verifica se o proximo tramo possue ValorCruze
					TramoFreteInternacional tramoFreteInternacionalAux = (TramoFreteInternacional) tramos.get(i + 1);//Pega o próximo para saber se possui ValorCruze
					if(tramoFreteInternacionalAux.getBlCruze().booleanValue()){
						vlValorCruze = findVlCruze(psBruto, valorCruzes);
					}
				}

				if(!tramoFreteInternacional.getBlCruze().booleanValue()){
					vlFreteCalculado = vlFrete.multiply(BigDecimalUtils.percent(pcFrete));//Adiciona o percentual de frete
					vlFreteCalculado = vlFreteCalculado.subtract(vlValorCruze);//Subtrai o valor cruze caso houver
				} else {
					vlFreteCalculado = findVlCruze(psBruto, valorCruzes); 
				}

				totalRemetenteDestinatario = totalRemetenteDestinatario.add(vlFreteCalculado);//Totaliza os valores calculados

				if (tramoFreteInternacional.getBlTramoOrigem().booleanValue()) {
					trechoCtoInt.setVlFreteRemetente(vlFreteCalculado);
					totalRemetente = totalRemetente.add(vlFreteCalculado); 
				} else {
					trechoCtoInt.setVlFreteDestinatario(vlFreteCalculado);
					totalDestinatario = totalDestinatario.add(vlFreteCalculado);
				}

				trechoCtoInt.setTramoFreteInternacional(tramoFreteInternacional);

				trechosCtoInt.add(trechoCtoInt);
			}

			totalRemetenteDestinatario = totalRemetente.add(totalDestinatario);
		}

		return trechosCtoInt;
	}
	
	public BigDecimal executeCalculoVlFreteExterno(CtoInternacional ctoInternacional){
		BigDecimal vlFreteExterno = BigDecimalUtils.ZERO;
		List trechos = ctoInternacional.getTrechosCtoInternacional();

		if(trechos != null && !trechos.isEmpty()){
			BigDecimal vlRemetente = BigDecimalUtils.ZERO;
			BigDecimal vlDestinatario = BigDecimalUtils.ZERO;
			BigDecimal vlCruze = BigDecimalUtils.ZERO;

			for(int i = 0 ; i < trechos.size(); i++){
				TrechoCtoInt trechoCtoInt = (TrechoCtoInt) trechos.get(i);
				TramoFreteInternacional tramoFreteInternacional = trechoCtoInt.getTramoFreteInternacional();

				vlRemetente = vlRemetente.add(trechoCtoInt.getVlFreteRemetente());
				vlDestinatario = vlDestinatario.add(trechoCtoInt.getVlFreteDestinatario());
				
				if(tramoFreteInternacional.getBlCruze().booleanValue()){
					if(trechoCtoInt.getVlFreteRemetente() != null && !CompareUtils.eq(trechoCtoInt.getVlFreteRemetente(), BigDecimalUtils.ZERO)){
						vlCruze = trechoCtoInt.getVlFreteRemetente();  
					} else {
						vlCruze = trechoCtoInt.getVlFreteDestinatario();
					}
				}
			}

			if(CompareUtils.eq(vlRemetente, vlDestinatario)){
				vlFreteExterno = vlCruze;
			} else {
				DistrFreteInternacional distrFreteInternacional = distrFreteInternacionalService.findByCtoInternacional(ctoInternacional);//Pra pegar o pcFreteExterno
				vlFreteExterno = BigDecimalUtils.round(ctoInternacional.getVlTotalDocServico().multiply(BigDecimalUtils.percent(distrFreteInternacional.getPcFreteExterno())));
			}
		}

		return vlFreteExterno;
	}
	
	private BigDecimal findVlCruze(BigDecimal psBruto, List valorCruzes){
		if(valorCruzes != null && !valorCruzes.isEmpty()){
			for(Iterator it = valorCruzes.iterator(); it.hasNext();){
				ValorCruze valorCruze = (ValorCruze)it.next();
 				BigDecimal nrFaixaInicialPeso = valorCruze.getNrFaixaInicialPeso();
				BigDecimal nrFaixaFinalPeso = valorCruze.getNrFaixaFinalPeso();

				if(CompareUtils.between(psBruto, nrFaixaInicialPeso, nrFaixaFinalPeso)){
					return valorCruze.getVlCruze();
				}
			}
		}

		return BigDecimalUtils.ZERO;
	}

	//Setters
	public void setValorCruzeService(ValorCruzeService valorCruzeService) {
		this.valorCruzeService = valorCruzeService;
	}

	public void setDistrFreteInternacionalService(
			DistrFreteInternacionalService distrFreteInternacionalService) {
		this.distrFreteInternacionalService = distrFreteInternacionalService;
	}
	
	
}