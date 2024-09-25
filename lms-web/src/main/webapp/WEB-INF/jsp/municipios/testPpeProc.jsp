<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.ppeService">

	<adsm:form action="/municipios/testPpe">	
	
		<adsm:textbox dataType="JTDate" label="data" labelWidth="17%" property="dtConsulta"/>
	
		<adsm:lookup property="municipioOrigem" idProperty="idMunicipio" criteriaProperty="nmMunicipio" 
			service="lms.municipios.atualizarTrocaFiliaisAction.findMunicipio"   dataType="text"  
			labelWidth="17%"  label="origem" size="30"
			action="/municipios/manterMunicipios" minLengthForAutoPopUpSearch="3" exactMatch="false" maxLength="60" >
		</adsm:lookup>

		<adsm:lookup property="municipioDestino" idProperty="idMunicipio" criteriaProperty="nmMunicipio" 
			service="lms.municipios.atualizarTrocaFiliaisAction.findMunicipio"   dataType="text"  
			labelWidth="17%"  label="destino" size="30"
			action="/municipios/manterMunicipios" minLengthForAutoPopUpSearch="3" exactMatch="false" maxLength="60" >
		</adsm:lookup>
		
		<adsm:combobox property="idServico" label="servico" service="lms.configuracoes.servicoService.find" optionLabelProperty="dsServico" optionProperty="idServico" 
					    width="80%" labelWidth="17%"/>		
				
		<adsm:textbox dataType="text" label="cep" labelWidth="17%" property="cep"/>
		
		<adsm:lookup property="cliente" criteriaProperty="pessoa.nrIdentificacao" idProperty="idCliente" service="lms.vendas.clienteService.findLookup" 
					dataType="text"  label="cliente" size="20" maxLength="20" labelWidth="17%" width="84%" 
					action="/vendas/manterDadosIdentificacao" exactMatch="false">
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />			
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:combobox property="idSegmentoMercado"  label="segmentoMercado" service="lms.vendas.segmentoMercadoService.find" optionLabelProperty="dsSegmentoMercado" optionProperty="idSegmentoMercado" 
					    width="80%" labelWidth="17%"/>		
		
		<adsm:textbox dataType="text" label="resultado" labelWidth="17%" property="resultado" disabled="true"/>

		<adsm:buttonBar>
			<adsm:button caption="consultar" disabled="false" id="consultar" service="lms.municipios.ppeService.executeTesteMCD" callbackProperty="consultar"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	function initWindow(evt){
		setDisabled("consultar", false);
	}

	function consultar_cb(data, error){
		if (error != undefined){
			alert(error);
		}
		
		if (data != undefined)
			setElementValue("resultado", data._value);

	}

</script>