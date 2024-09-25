package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.param.ConsultarUsuarioLMSParam;
import com.mercurio.lms.configuracoes.model.service.FuncionarioService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.CptComplexidade;
import com.mercurio.lms.vendas.model.CptFuncionario;
import com.mercurio.lms.vendas.model.CptMedida;
import com.mercurio.lms.vendas.model.CptTipoValor;
import com.mercurio.lms.vendas.model.CptValor;
import com.mercurio.lms.vendas.model.CptVeiculo;
import com.mercurio.lms.vendas.model.SegmentoMercado;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.CptFuncionarioService;
import com.mercurio.lms.vendas.model.service.CptMedidaService;
import com.mercurio.lms.vendas.model.service.CptTipoValorService;
import com.mercurio.lms.vendas.model.service.CptValorService;
import com.mercurio.lms.vendas.model.service.CptVeiculoService;
import com.mercurio.lms.vendas.model.service.SegmentoMercadoService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.manterValorAction"
 */

public class ManterValorAction extends CrudAction {

	private static final String COD_FUNCIONARIO = "F";
	private static final String COD_VEICULO = "I";
	private static final String COD_TEMPO = "T";
	private static final String COD_QUANTIDADE = "Q";
	private static final String COD_PERCENTUAL = "P";
	private static final String COD_VALOR = "V";
	private static final String LMS_01191 = "LMS-01191";
	private static final String NR_PLACA = "nrPlaca";
	private static final String SEGMENTO_MERCADO = "segmentoMercado";
	private static final String CPT_COMPLEXIDADE = "cptComplexidade";
	private static final String ID_CPT = "idCpt";
	private static final String NR_FROTA = "nrFrota";
	private static final String VL_MEDIDA = "vlMedida";
	private static final String DT_VIGENCIA_FINAL = "dtVigenciaFinal";
	private static final String DT_VIGENCIA_INICIAL = "dtVigenciaInicial";
	private static final String NM_FUNCIONARIO = "nmFuncionario";
	private static final String FUNCIONARIO_NR_MATRICULA = "funcionario.nrMatricula";
	private static final String FUNCIONARIO_ID_USUARIO = "funcionario.idUsuario";
	private static final String CPT_TIPO_VALOR = "cptTipoValor";
	private static final String CLIENTE_PESSOA_NM_PESSOA = "cliente.pessoa.nmPessoa";
	private static final String CLIENTE_PESSOA_NR_IDENTIFICACAO = "cliente.pessoa.nrIdentificacao";
	private static final String CLIENTE_ID_CLIENTE = "cliente.idCliente";
	private static final String CPT_VALOR = "CptValor";
	private static final String CPT_MEDIDA = "CptMedida";
	private static final String CPT_VEICULO = "CptVeiculo";
	private static final String CPT_FUNCIONARIO = "CptFuncionario";
	
	private MeioTransporteService 	meioTransporteService;
	private ClienteService 			clienteService;
	private UsuarioLMSService   	usuarioLMSService;   
	private CptValorService 		cptValorService;
	private CptTipoValorService 	cptTipoValorService; 
	private CptMedidaService 		cptMedidaService;
	private CptVeiculoService   	cptVeiculoService;
	private CptFuncionarioService 	cptFuncionarioService;
	private SegmentoMercadoService 	segmentoMercadoService;
	private FuncionarioService      funcionarioService;
	private UsuarioService 			usuarioService;
	
	
	/**
	 * Atrav�s dod parametros da tela obtem o objeto CPT
	 * @param criteria
	 * @return
	 */
	public Serializable findById(TypedFlatMap criteria) {
		
		TypedFlatMap tfm = new TypedFlatMap();
				
		Long   id  = getIdCPT(criteria);
		String cpt = getNmCPT(criteria);
		
		String identificador = criteria.getString(ID_CPT);
		tfm.put(ID_CPT, identificador);
		
		if(CPT_FUNCIONARIO.equals(cpt)){			
				findCPTFuncionario(tfm, id);			
		}else 
			if(CPT_VEICULO.equals(cpt)){			
				findCPTVeiculo(tfm, id);										
		}else 
			if(CPT_MEDIDA.equals(cpt)){			
				findCPTMedida(tfm, id);									
		}else{			
			findCPTValor(tfm, id);							
		}
		
		return tfm;
	}

	/**
	 * Obtem CPT_Funcionario
	 * @param tfm
	 * @param id
	 */
	private void findCPTFuncionario(TypedFlatMap tfm, Long id) {
		CptFuncionario funcionario = (CptFuncionario)cptFuncionarioService.findById(id);
		
		Cliente cliente = funcionario.getCliente();
		if(cliente != null){
			tfm.put(CLIENTE_ID_CLIENTE, cliente.getIdCliente());
			tfm.put(CLIENTE_PESSOA_NR_IDENTIFICACAO, cliente.getPessoa().getNrIdentificacao());
			tfm.put(CLIENTE_PESSOA_NM_PESSOA, cliente.getPessoa().getNmPessoa());
		}
		
		CptTipoValor cptTipoValor = funcionario.getCptTipoValor();
		if(cptTipoValor != null){
			tfm.put(CPT_TIPO_VALOR, cptTipoValor.getIdCptTipoValor());
		}
		
		Usuario usuario = null; 
		if(funcionario.getNrMatricula() != null){

			usuario = usuarioService.findByNrMatricula(funcionario.getNrMatricula());
			
			tfm.put(FUNCIONARIO_ID_USUARIO, usuario.getIdUsuario());
			tfm.put(FUNCIONARIO_NR_MATRICULA, usuario.getNrMatricula());
			tfm.put(NM_FUNCIONARIO, usuario.getNmUsuario());
		}	
		
		tfm.put(DT_VIGENCIA_INICIAL, funcionario.getDtVigenciaInicial());
		tfm.put(DT_VIGENCIA_FINAL, funcionario.getDtVigenciaFinal());
		if(JTDateTimeUtils.MAX_YEARMONTHDAY.compareTo(funcionario.getDtVigenciaInicial()) == 0){
			tfm.put(DT_VIGENCIA_INICIAL, null);
		}
		if(JTDateTimeUtils.MAX_YEARMONTHDAY.compareTo(funcionario.getDtVigenciaFinal()) == 0){
			tfm.put(DT_VIGENCIA_FINAL, null);
		}
	}

	/**
	 * Obtem o cpt veiculo
	 * @param tfm
	 * @param id
	 */
	private void findCPTVeiculo(TypedFlatMap tfm, Long id) {
		CptVeiculo veiculo = (CptVeiculo) cptVeiculoService.findById(id);
		
		if(veiculo.getNrFrota() != null){
			TypedFlatMap parameter = new TypedFlatMap();
			parameter.put(NR_FROTA, veiculo.getNrFrota());
			tfm.put(NR_PLACA, this.findValidFrota(parameter).getString(NR_PLACA));				
			tfm.put(NR_FROTA, veiculo.getNrFrota());
		}
		
		Cliente cliente = veiculo.getCliente();
		if(cliente != null){
			tfm.put(CLIENTE_ID_CLIENTE, cliente.getIdCliente());
			tfm.put(CLIENTE_PESSOA_NR_IDENTIFICACAO, cliente.getPessoa().getNrIdentificacao());
			tfm.put(CLIENTE_PESSOA_NM_PESSOA, cliente.getPessoa().getNmPessoa());
		}					
		
		CptTipoValor cptTipoValor = veiculo.getCptTipoValor();
		if(cptTipoValor != null){
			tfm.put(CPT_TIPO_VALOR, cptTipoValor.getIdCptTipoValor());
		}
		
		tfm.put(DT_VIGENCIA_INICIAL, veiculo.getDtVigenciaInicial());
		tfm.put(DT_VIGENCIA_FINAL, veiculo.getDtVigenciaFinal());
		if(JTDateTimeUtils.MAX_YEARMONTHDAY.compareTo(veiculo.getDtVigenciaInicial()) == 0){
			tfm.put(DT_VIGENCIA_INICIAL, null);
		}
		if(JTDateTimeUtils.MAX_YEARMONTHDAY.compareTo(veiculo.getDtVigenciaFinal()) == 0){
			tfm.put(DT_VIGENCIA_FINAL, null);
		}
	}

	/**
	 * Obtem CPTMedida
	 * @param tfm
	 * @param id
	 */
	private void findCPTMedida(TypedFlatMap tfm, Long id) {
		CptMedida medida = (CptMedida) cptMedidaService.findById(id);
		
		Cliente cliente = medida.getCliente();
		if(cliente != null){
			tfm.put(CLIENTE_ID_CLIENTE, cliente.getIdCliente());
			tfm.put(CLIENTE_PESSOA_NR_IDENTIFICACAO, cliente.getPessoa().getNrIdentificacao());
			tfm.put(CLIENTE_PESSOA_NM_PESSOA, cliente.getPessoa().getNmPessoa());
		}
					
		SegmentoMercado segmento = medida.getSegmentoMercado();
		if(segmento != null){
			tfm.put(SEGMENTO_MERCADO, segmento.getIdSegmentoMercado());
		}
		
		/*Cpt Complexidade*/
		if(medida.getCptComplexidade() != null){

			tfm.put(CPT_COMPLEXIDADE, medida.getCptComplexidade().getIdCptComplexidade());
			/*Cpt Tipo Valor*/
			if(medida.getCptComplexidade().getCptTipoValor() != null){
				tfm.put(CPT_TIPO_VALOR, medida.getCptComplexidade().getCptTipoValor().getIdCptTipoValor());
			}
		}		
		
		tfm.put(VL_MEDIDA, medida.getVlMedida());
		
		tfm.put(DT_VIGENCIA_INICIAL, medida.getDtVigenciaInicial());
		tfm.put(DT_VIGENCIA_FINAL, medida.getDtVigenciaFinal());
		if(JTDateTimeUtils.MAX_YEARMONTHDAY.compareTo(medida.getDtVigenciaInicial()) == 0){
			tfm.put(DT_VIGENCIA_INICIAL, null);
		}
		if(JTDateTimeUtils.MAX_YEARMONTHDAY.compareTo(medida.getDtVigenciaFinal()) == 0){
			tfm.put(DT_VIGENCIA_FINAL, null);
		}
	}

	/**
	 * Obtem o CPTValor
	 * @param tfm
	 * @param id
	 */
	private void findCPTValor(TypedFlatMap tfm, Long id) {
		CptValor valor =  (CptValor) cptValorService.findById(id);
		
		Cliente cliente = valor.getCliente();
		if(cliente != null){
			tfm.put(CLIENTE_ID_CLIENTE, cliente.getIdCliente());
			tfm.put(CLIENTE_PESSOA_NR_IDENTIFICACAO, cliente.getPessoa().getNrIdentificacao());
			tfm.put(CLIENTE_PESSOA_NM_PESSOA, cliente.getPessoa().getNmPessoa());
		}
					
		CptTipoValor cptTipoValor = valor.getCptTipoValor();
		if(cptTipoValor != null){
			tfm.put(CPT_TIPO_VALOR, cptTipoValor.getIdCptTipoValor());
		}			
		
		tfm.put("valor", valor.getVlValor());
		
		tfm.put(DT_VIGENCIA_INICIAL, valor.getDtVigenciaInicial());
		tfm.put(DT_VIGENCIA_FINAL, valor.getDtVigenciaFinal());
		if(JTDateTimeUtils.MAX_YEARMONTHDAY.compareTo(valor.getDtVigenciaInicial()) == 0){
			tfm.put(DT_VIGENCIA_INICIAL, null);
		}
		if(JTDateTimeUtils.MAX_YEARMONTHDAY.compareTo(valor.getDtVigenciaFinal()) == 0){
			tfm.put(DT_VIGENCIA_FINAL, null);
		}
	}
	
	/**
	 * Salva a tabela CPT 
	 * @param parameters
	 * @return
	 */
	public Serializable store(TypedFlatMap parameters) {

		Cpt cpt = this.populateCPTData(parameters);

		String tpValor = cpt.getCptTipoValor().getTpGrupoValor().getValue();		
		
		if(COD_VALOR.equals(tpValor) || COD_PERCENTUAL.equals(tpValor)){						
			parameters.put(ID_CPT, this.storeCPTValor(cpt).getIdCptValor());			
		}else 
			if(COD_QUANTIDADE.equals(tpValor) || COD_TEMPO.equals(tpValor)){			
				parameters.put(ID_CPT, this.storeCPTMedida(cpt).getIdCptMedida());						
		}else 
			if(COD_VEICULO.equals(tpValor)){
				parameters.put(ID_CPT, this.storeCPTVeiculo(cpt).getIdCptVeiculo());						
		}else 
			if(COD_FUNCIONARIO.equals(tpValor)){
				parameters.put(ID_CPT, this.storeCPTFuncionario(cpt).getIdCptFuncionario());										
		}
		
		return parameters;
	}
	
	
	/**
	 * Popula o obejto CPT com os dados passados pela tela
	 * @param parameters
	 * @return
	 */
	private Cpt populateCPTData(TypedFlatMap parameters) {

		Cpt cpt = new Cpt();
		
		/*ID*/
		Long idCpt = getIdCPT(parameters);
		cpt.setId(idCpt);
		
		/*Cliente*/
		Cliente cliente = new Cliente();
		cliente.setIdCliente(parameters.getLong(CLIENTE_ID_CLIENTE));
		cpt.setCliente(cliente);

		/*Segmento Mercado*/
		SegmentoMercado segmentoMercado = new SegmentoMercado();
		segmentoMercado.setIdSegmentoMercado(parameters.getLong(SEGMENTO_MERCADO));
		cpt.setSegmento(segmentoMercado);		
		
		/*CptTipoValor*/		
		if(LongUtils.hasValue(parameters.getLong(CPT_TIPO_VALOR))){
			CptTipoValor cptTipoValor = (CptTipoValor)cptTipoValorService.findById(parameters.getLong(CPT_TIPO_VALOR));
			cpt.setCptTipoValor(cptTipoValor);
		}
				
		/*Complexidade*/
		CptComplexidade complexidade = new CptComplexidade();
		complexidade.setIdCptComplexidade(parameters.getLong(CPT_COMPLEXIDADE));
		cpt.setCptComplexidade(complexidade);		
		
		/*Funcionario*/		
		if(LongUtils.hasValue(parameters.getLong(FUNCIONARIO_ID_USUARIO))){
			Usuario usuario = usuarioService.findById(parameters.getLong(FUNCIONARIO_ID_USUARIO));
			cpt.setNrMatricula(usuario.getNrMatricula());
		}		

		/*Frota*/
		cpt.setNrFrota(parameters.getString(NR_FROTA));
								
		YearMonthDay dtInicial = parameters.getYearMonthDay(DT_VIGENCIA_INICIAL);
		if(dtInicial == null){
			dtInicial = JTDateTimeUtils.MAX_YEARMONTHDAY;
		}
		cpt.setDtVigenciaInicial(dtInicial);
		
		YearMonthDay dtFinal = parameters.getYearMonthDay(DT_VIGENCIA_FINAL);
		if(dtFinal == null){
			dtFinal = JTDateTimeUtils.MAX_YEARMONTHDAY;				
		}
		cpt.setDtVigenciaFinal(dtFinal);
		
		/*Valores - Percentual e Valor*/
		cpt.setValor(BigDecimalUtils.defaultBigDecimal(parameters.getBigDecimal("valor")));		
		cpt.setPercentual(BigDecimalUtils.defaultBigDecimal(parameters.getBigDecimal("percentual")));
		
		return cpt;
		
	}

	/**
	 * Salva CPT_Funcioonario
	 * 
	 * @param cpt
	 * @return
	 */
	private CptFuncionario storeCPTFuncionario(Cpt cpt) {
		
		CptFuncionario funcionario = new CptFuncionario();
		funcionario.setIdCptFuncionario(cpt.getId());
		funcionario.setCptTipoValor(cpt.getCptTipoValor());
		funcionario.setCliente(cpt.getCliente());
		funcionario.setDtVigenciaInicial(cpt.getDtVigenciaInicial());
		funcionario.setDtVigenciaFinal(cpt.getDtVigenciaFinal());			
		funcionario.setNrMatricula(cpt.getNrMatricula());
		
		/*Verifica se j� possui um registro cadastrado*/
		validCPT(cpt.getCliente().getIdCliente(), cpt.getId(), cpt.getNrMatricula(), CPT_FUNCIONARIO);					
		
		/*Verifica se a data de vigencia inicial for menor ou igual a data atual
		atualiza o campo BL_VIGENCIA_INICIAL com S*/
		Boolean comparaInicial = JTDateTimeUtils.comparaData(cpt.getDtVigenciaInicial(), JTDateTimeUtils.getDataAtual()) <= 0;
		funcionario.setBlVigenciaInicial(Boolean.FALSE);
		if(comparaInicial){
			funcionario.setBlVigenciaInicial(Boolean.TRUE);
		}	
		
		/*Verifica se a data de vigencia final for menor ou igual a data atual ou n�o informada
		atualiza o campo BL_VIGENCIA_FINAL com S*/
		Boolean comparaFinal = JTDateTimeUtils.comparaData(cpt.getDtVigenciaFinal(), JTDateTimeUtils.getDataAtual()) <= 0;
		funcionario.setBlVigenciaFinal(Boolean.FALSE);
		if(comparaFinal){
			funcionario.setBlVigenciaFinal(Boolean.TRUE);					
		}				
		
		/*Atualiza a tabela PFCOMPL com o numero da matricula e o idCliente*/
		if(comparaInicial && !comparaFinal){					
			funcionarioService.updateCODCLIDED(cpt.getNrMatricula(), StringUtils.leftPad(String.valueOf(cpt.getCliente().getIdCliente()), 8, '0'));
		}				
		
		if(comparaFinal){					
			funcionarioService.updateCODCLIDED(cpt.getNrMatricula(), null);
		}	
		
		/*Salva CPTFuncionario*/
		funcionario.setIdCptFuncionario(LongUtils.getLong(cptFuncionarioService.store(funcionario)));
		
		return funcionario;
	}

	/**
	 * Salva a tabela CPT_Veiculo
	 * 
	 * @param cpt
	 * @return
	 */
	private CptVeiculo storeCPTVeiculo(Cpt cpt) {
				
		CptVeiculo veiculo = new CptVeiculo();
		veiculo.setIdCptVeiculo(cpt.getId());
		veiculo.setCliente(cpt.getCliente());
		veiculo.setCptTipoValor(cpt.getCptTipoValor());
		veiculo.setDtVigenciaInicial(cpt.getDtVigenciaInicial());
		veiculo.setDtVigenciaFinal(cpt.getDtVigenciaFinal());				
		veiculo.setNrFrota(cpt.getNrFrota());
		
		/*Verifica se ja possui registro cadastrado*/
		validCPT(cpt.getCliente().getIdCliente(), cpt.getId(), cpt.getNrFrota(), CPT_VEICULO);
		
		/*Verifica se a data de vigencia inicial for menor ou igual a data atual
		atualiza o campo BL_VIGENCIA_INICIAL com S*/
		Boolean comparaInicial = JTDateTimeUtils.comparaData(cpt.getDtVigenciaInicial(), JTDateTimeUtils.getDataAtual()) <= 0;
		veiculo.setBlVigenciaInicial(Boolean.FALSE);
		if(comparaInicial){
			veiculo.setBlVigenciaInicial(Boolean.TRUE);
		}
		
		/*Verifica se a data de vigencia final for menor ou igual a data atual ou n�o informada
		atualiza o campo BL_VIGENCIA_FINAL com S*/
		Boolean comparaFinal = JTDateTimeUtils.comparaData(cpt.getDtVigenciaFinal(), JTDateTimeUtils.getDataAtual()) <= 0;
		veiculo.setBlVigenciaFinal(Boolean.FALSE);
		if(comparaFinal || cpt.getDtVigenciaFinal().compareTo(JTDateTimeUtils.MAX_YEARMONTHDAY) == 0){
			veiculo.setBlVigenciaFinal(Boolean.TRUE);					
		}
		
		/*Atualiza a tabela F1201 com o numero da frota e o idCliente*/
		if(comparaInicial && !comparaFinal){					
			meioTransporteService.updateFAUNIT(cpt.getNrFrota(), StringUtils.leftPad(String.valueOf(cpt.getCliente().getIdCliente()), 8, '0'));
		}
		
		if(comparaFinal){					
			meioTransporteService.updateFAUNIT(cpt.getNrFrota(), null);
		}		
		
		/*Salva CPTVeiculo*/		
		veiculo.setIdCptVeiculo(LongUtils.getLong(cptVeiculoService.store(veiculo)));
		
		return veiculo;
	}

	/**
	 * Salva a tabela CPT_Medida
	 * @param cpt
	 * @return
	 */
	private CptMedida storeCPTMedida(Cpt cpt) {
		
		CptMedida cptMedida = new CptMedida();
		cptMedida.setIdCptMedida(cpt.getId());											
		cptMedida.setCptComplexidade(cpt.getCptComplexidade());								
		cptMedida.setDtVigenciaInicial(cpt.getDtVigenciaInicial());				
		cptMedida.setDtVigenciaFinal(cpt.getDtVigenciaFinal());
		
		if(cpt.getCliente() != null && cpt.getCliente().getIdCliente() != null){
			cptMedida.setCliente(cpt.getCliente());
		}
		
		cptMedida.setSegmentoMercado(cpt.getSegmento());
		cptMedida.setVlMedida(cpt.getValor());
		
		/*Verifica se j� possue registro*/
		validCPT(cpt.getCliente().getIdCliente(), cpt.getId(), cpt.getCptTipoValor().getIdCptTipoValor(), CPT_MEDIDA);
		
		/*Salva CPTMedida*/
		cptMedida.setIdCptMedida(LongUtils.getLong(cptMedidaService.store(cptMedida)));	
		
		return cptMedida;
	}

	/**
	 * Salva a tabela CPT VALOR
	 * @param cpt
	 * @return Long
	 */
	private CptValor storeCPTValor(Cpt cpt) {
		
		CptValor cptValor = new CptValor();
		cptValor.setIdCptValor(cpt.getId());			
		cptValor.setCliente(cpt.getCliente());			
		cptValor.setCptTipoValor(cpt.getCptTipoValor());
		cptValor.setDtVigenciaInicial(cpt.getDtVigenciaInicial());
		cptValor.setDtVigenciaFinal(cpt.getDtVigenciaFinal());
		
		/*Seta o valor ou percentual dependendo do tipo de valor*/
		cptValor.setVlValor(cpt.getPercentual());
		if(BigDecimalUtils.hasValue(cpt.getValor())){
			cptValor.setVlValor(cpt.getValor());
		}
		
		/*Verifica se j� possue registro*/
		validCPT(cpt.getCliente().getIdCliente(),cpt.getId(),cptValor.getVlValor(),CPT_VALOR);
		
		/*Salva CPTValor*/
		cptValor.setIdCptValor(LongUtils.getLong(cptValorService.store(cptValor)));
		
		return cptValor;
	}

	@Override
	public ResultSetPage findPaginated(Map criteria) {		
		return getCptValorService().findPaginated(criteria);
	}
	
	@Override
	public Integer getRowCount(Map criteria) {
		return getCptValorService().getRowCount(criteria);
	}
	
	/**
	 * Vverifica se existe determinado registro na tabela CPT selecionada
	 * 
	 * @param idCliente
	 * @param dataValue
	 * @return
	 */
	private void validCPT(Long idCliente, Long idData, Object dataValue, String nmCPT){

		if(CPT_VALOR.equals(nmCPT)){
			BigDecimal valor = BigDecimalUtils.getBigDecimal(dataValue);
			if(idData == null){			
				if(cptValorService.findValorCliente(idCliente,valor)){
					throw new BusinessException(LMS_01191);
				}
			}else{
				CptValor cpt = (CptValor)cptValorService.findById(idData);
				if(!valor.equals(cpt.getVlValor().setScale(2)) 
						&& cptValorService.findValorCliente(idCliente,valor)){
					throw new BusinessException(LMS_01191);
				}
			}
		}else 
			if(CPT_MEDIDA.equals(nmCPT)){
				Long idCptTipoValor = LongUtils.getLong(dataValue);
				if(idData == null){
					if(cptMedidaService.findComplexidadeCliente(idCliente, idCptTipoValor)){						
						throw new BusinessException(LMS_01191);
					}
				}else{
					CptMedida cpt = (CptMedida) cptMedidaService.findById(idData);					
					if(!idCptTipoValor.equals(cpt.getCptComplexidade().getCptTipoValor().getIdCptTipoValor())
							&& cptMedidaService.findComplexidadeCliente(idCliente, idCptTipoValor)){
						throw new BusinessException(LMS_01191);
					}	
				}
			}else 
				if(CPT_VEICULO.equals(nmCPT)){
					String nrFrota = dataValue.toString();
					if(idData == null){
						if(cptVeiculoService.findFrotaCliente(idCliente, nrFrota)){
							throw new BusinessException(LMS_01191);							
						}
					}else{						
						CptVeiculo cpt = (CptVeiculo) cptVeiculoService.findById(idData);
						if(!nrFrota.equals(cpt.getNrFrota()) 
								&& cptVeiculoService.findFrotaCliente(idCliente, nrFrota)){
							throw new BusinessException(LMS_01191);
						}
					}
				}else{
					String nrMatricula = dataValue.toString();
					if(idData == null){
						if(cptFuncionarioService.findFuncionarioCliente(idCliente, nrMatricula)){
							throw new BusinessException(LMS_01191);							
						}
					}else{						
						CptFuncionario cpt = (CptFuncionario) cptFuncionarioService.findById(idData);
						if(!nrMatricula.equals(cpt.getNrMatricula()) 
								&& cptFuncionarioService.findFuncionarioCliente(idCliente, nrMatricula)){
							throw new BusinessException(LMS_01191);
						}
					}
				}			

	}
	
	/**
	 * Remove o registro dependendo do tipo de tabela 
	 * 
	 * @param tfm
	 */
	public void removeById(TypedFlatMap tfm) {	
		
		String nmCPT = getNmCPT(tfm);
		Long   idCPT = getIdCPT(tfm);
		
		if(CPT_VALOR.equals(nmCPT)){
			cptValorService.removeById(idCPT);			
		}else 
			if(CPT_MEDIDA.equals(nmCPT)){
			cptMedidaService.removeById(idCPT);
		}else 
			if(CPT_VEICULO.equals(nmCPT)){
			cptVeiculoService.removeById(idCPT);
		}else{
			cptFuncionarioService.removeById(idCPT);							
		}
		
	}
	
	/**
	 * Remove ums lista de registros
	 * @param tfm
	 */
    public void removeByIds(TypedFlatMap tfm) {
    	List list = (List)tfm.get("ids");
    	for(Object o : list){
    		tfm.put(ID_CPT, String.valueOf(o));  
    		removeById(tfm);
    	}
    }	
	
    /**
     * Utilizado pela lokup de cliente
     * 
     * @param criteria
     * @return
     */
	public List findClienteLookup(TypedFlatMap criteria) {
		return clienteService.findLookupCliente(criteria.getString("pessoa.nrIdentificacao"));
	}	
	
	/**
	 * Utilizado pelo campo de usuario
	 * 
	 * @param tfm
	 * @return
	 */
	public List findLookupUsuarioFuncionario(TypedFlatMap tfm){
		ConsultarUsuarioLMSParam cup = new ConsultarUsuarioLMSParam();
		cup.setNrMatricula(tfm.getString("nrMatricula"));
		cup.setNmUsuario(tfm.getString("nmUsuario"));
		cup.setTpCategoriaUsuario(tfm.getString("tpCategoriaUsuario"));

        return usuarioLMSService.findLookupSistema(cup);
	}		

	/**
	 * Atrav�s do  numero da frota , � verificado na tabela F1201
	 * se a mesma existe 
	 * 
	 * Caso existir seta o numero da placa 
	 * 
	 * @param map
	 * @return
	 */
	public TypedFlatMap findValidFrota(TypedFlatMap map){
		
		String nrFrota = map.getString(NR_FROTA);
		List list = meioTransporteService.findValidFrota(nrFrota);
		
		if(list!= null && !list.isEmpty()){
			Object[] record = (Object[])list.get(0); 
			map.put(NR_FROTA, String.valueOf(record[0]).trim());
			map.put(NR_PLACA, String.valueOf(record[1]).trim());
		}
		
		return map;
	} 
	
	/**
	 * Obtem os identificadores do registro 
	 *  
	 * @param tfm
	 * @return
	 */
	private String[] getIdentificador(TypedFlatMap tfm){
		String identificador = tfm.getString(ID_CPT);
		if(StringUtils.isNotBlank(identificador)){
			return identificador.split("_");					
		}
		return null;
	}
	
	/**
	 * Obtem o id da tabela CPT
	 *  
	 * @param tfm
	 * @return
	 */
	private Long getIdCPT(TypedFlatMap tfm){
		if(StringUtils.isBlank(tfm.getString("idCpt"))){
			return null;
		}
		return Long.valueOf(getIdentificador(tfm)[0]);
	}
	
	/**
	 * Obtem o nome da tabela CPT
	 * 
	 * @param tfm
	 * @return
	 */
	private String getNmCPT(TypedFlatMap tfm){
		return getIdentificador(tfm)[1];
	}
	

	public UsuarioLMSService getUsuarioLMSService() {
		return usuarioLMSService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	public CptValorService getCptValorService() {
		return cptValorService;
	}

	public void setCptValorService(CptValorService cptValorService) {
		this.cptValorService = cptValorService;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}


	public MeioTransporteService getMeioTransporteService() {
		return meioTransporteService;
	}


	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}

	public CptTipoValorService getCptTipoValorService() {
		return cptTipoValorService;
	}

	public void setCptTipoValorService(CptTipoValorService cptTipoValorService) {
		this.cptTipoValorService = cptTipoValorService;
	}

	public SegmentoMercadoService getSegmentoMercadoService() {
		return segmentoMercadoService;
	}

	public void setSegmentoMercadoService(
			SegmentoMercadoService segmentoMercadoService) {
		this.segmentoMercadoService = segmentoMercadoService;
	}

	public CptMedidaService getCptMedidaService() {
		return cptMedidaService;
	}

	public void setCptMedidaService(CptMedidaService cptMedidaService) {
		this.cptMedidaService = cptMedidaService;
	}

	public CptVeiculoService getCptVeiculoService() {
		return cptVeiculoService;
	}

	public void setCptVeiculoService(CptVeiculoService cptVeiculoService) {
		this.cptVeiculoService = cptVeiculoService;
	}

	public CptFuncionarioService getCptFuncionarioService() {
		return cptFuncionarioService;
	}

	public void setCptFuncionarioService(CptFuncionarioService cptFuncionarioService) {
		this.cptFuncionarioService = cptFuncionarioService;
	}

	public FuncionarioService getFuncionarioService() {
		return funcionarioService;
	}

	public void setFuncionarioService(FuncionarioService funcionarioService) {
		this.funcionarioService = funcionarioService;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}	
		
	public class Cpt{
		
		private Long id;
		private String  nrMatricula;
		private String  nrFrota;
		private Cliente cliente;
		private CptTipoValor cptTipoValor;
		private SegmentoMercado segmento;
		private CptComplexidade cptComplexidade;
		private YearMonthDay dtVigenciaInicial;		
		private YearMonthDay dtVigenciaFinal;	
		private BigDecimal valor;
		private BigDecimal percentual;
				
		public Cpt() {
			
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getNrMatricula() {
			return nrMatricula;
		}

		public void setNrMatricula(String nrMatricula) {
			this.nrMatricula = nrMatricula;
		}

		public String getNrFrota() {
			return nrFrota;
		}

		public void setNrFrota(String nrFrota) {
			this.nrFrota = nrFrota;
		}

		public Cliente getCliente() {
			return cliente;
		}

		public void setCliente(Cliente cliente) {
			this.cliente = cliente;
		}

		public CptTipoValor getCptTipoValor() {
			return cptTipoValor;
		}

		public void setCptTipoValor(CptTipoValor cptTipoValor) {
			this.cptTipoValor = cptTipoValor;
		}

		public SegmentoMercado getSegmento() {
			return segmento;
		}

		public void setSegmento(SegmentoMercado segmento) {
			this.segmento = segmento;
		}

		public CptComplexidade getCptComplexidade() {
			return cptComplexidade;
		}

		public void setCptComplexidade(CptComplexidade cptComplexidade) {
			this.cptComplexidade = cptComplexidade;
		}

		public YearMonthDay getDtVigenciaInicial() {
			return dtVigenciaInicial;
		}

		public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
			this.dtVigenciaInicial = dtVigenciaInicial;
		}

		public YearMonthDay getDtVigenciaFinal() {
			return dtVigenciaFinal;
		}

		public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
			this.dtVigenciaFinal = dtVigenciaFinal;
		}

		public BigDecimal getValor() {
			return valor;
		}

		public void setValor(BigDecimal valor) {
			this.valor = valor;
		}

		public BigDecimal getPercentual() {
			return percentual;
		}

		public void setPercentual(BigDecimal percentual) {
			this.percentual = percentual;
		}
		
	}
	
}
