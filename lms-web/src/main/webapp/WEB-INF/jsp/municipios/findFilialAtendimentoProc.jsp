<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.ppeService">

	<adsm:form action="/municipios/findFilialAtendimento">	
	
		<adsm:lookup property="municipio" idProperty="idMunicipio" criteriaProperty="nmMunicipio" 
			service="lms.municipios.atualizarTrocaFiliaisAction.findMunicipio"   dataType="text"  
			labelWidth="17%"  label="municipio" size="30"
			action="/municipios/manterMunicipios" minLengthForAutoPopUpSearch="3" exactMatch="false" maxLength="60" >
		</adsm:lookup>

		<adsm:combobox property="idServico" label="servico" service="lms.configuracoes.servicoService.find" optionLabelProperty="dsServico" optionProperty="idServico" 
					    width="80%" labelWidth="17%"/>		
		
	
		<adsm:checkbox property="blIndicativoColeta" label="coleta" labelWidth="17%" width="60%"/>

		<adsm:textbox dataType="JTDate" label="data" labelWidth="17%" property="dtConsulta"/>

		<adsm:textbox dataType="text" label="cep" labelWidth="17%" property="cep"/>
		
		<adsm:lookup property="cliente" criteriaProperty="pessoa.nrIdentificacao" idProperty="idCliente" service="lms.vendas.clienteService.findLookup" 
					dataType="text"  label="cliente" size="20" maxLength="20" labelWidth="17%" width="84%" 
					action="/vendas/manterDadosIdentificacao" exactMatch="false">
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />			
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:lookup property="remetente" criteriaProperty="pessoa.nrIdentificacao" idProperty="idClienteRemetente" service="lms.vendas.clienteService.findLookup" 
					dataType="text"  label="remetente" size="20" maxLength="20" labelWidth="17%" width="84%" 
					action="/vendas/manterDadosIdentificacao" exactMatch="false">
			<adsm:propertyMapping relatedProperty="remetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />			
			<adsm:textbox dataType="text" property="remetente.pessoa.nmPessoa" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:combobox property="idSegmentoMercado"  label="segmentoMercado" service="lms.vendas.segmentoMercadoService.find" optionLabelProperty="dsSegmentoMercado" optionProperty="idSegmentoMercado" 
					    width="80%" labelWidth="17%"/>		
	
		<adsm:lookup property="unidadeFederativa" idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
					 service="lms.municipios.unidadeFederativaService.findLookup" dataType="text" 
					 labelWidth="17%" width="32%" label="uf" size="2" maxLength="2" 
					 action="/municipios/manterUnidadesFederativas" minLengthForAutoPopUpSearch="2" exactMatch="false">
		</adsm:lookup>

		<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
				service="lms.municipios.filialService.findLookupFilial" dataType="text" label="filial" size="3"
				action="/municipios/manterFiliais" labelWidth="17%" width="80%" minLengthForAutoPopUpSearch="3"
				exactMatch="false" style="width:45px" disabled="false">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmPessoa" size="30" disabled="true" />			
		</adsm:lookup>

		<adsm:textbox dataType="text" label="filial" labelWidth="17%" property="idFilialAtendimento" disabled="true"/>

		<adsm:buttonBar>
			<adsm:button caption="consultar" id="consultar" service="lms.municipios.ppeService.findFilialAtendimentoMunicipioTeste" callbackProperty="consultar"/>
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
			setElementValue("idFilialAtendimento", data._value);

	}

</script>