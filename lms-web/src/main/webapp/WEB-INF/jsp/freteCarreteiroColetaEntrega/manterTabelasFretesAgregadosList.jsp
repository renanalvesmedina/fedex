<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>
	
	function carregaDados_cb(data, error) {
		onPageLoad_cb(data, error);
	    var data = new Array();
	    var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.findInformacoesUsuarioLogado", "pageLoad",data);
	    xmit({serviceDataObjects:[sdo]});
	    if (getElementValue("idProcessoWorkflow") != "") {
			tabelaFreteColetaEntregaGridDef.detailGridRow("onDataLoad", getElementValue('idProcessoWorkflow'));
		}
	}

</script>

<adsm:window title="manterTabelasFretesAgregados" 
		service="lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction" 
		onPageLoadCallBack="carregaDados">
	
	<adsm:form action="/freteCarreteiroColetaEntrega/manterTabelasFretesAgregados">
	<adsm:hidden property="idProcessoWorkflow"/>
		<adsm:hidden property="idFilial"/>
		<adsm:hidden property="sgFilial"/>
		<adsm:hidden property="nmFantasia"/>
		<adsm:hidden property="idTabelaColetaEntrega"/>
		<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
				service="lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.findFilial" 
				dataType="text" label="filial" size="3"
				action="/municipios/manterFiliais" labelWidth="18%" width="32%" minLengthForAutoPopUpSearch="3"
				exactMatch="false"   disabled="false" required="true"   >
				
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia"  modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia"  disabled="true"   size="25"  />			
			
			<adsm:propertyMapping criteriaProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:hidden property="filial.empresa.tpEmpresa" value="M" serializable="false"/>

		</adsm:lookup>

		<adsm:combobox property="tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega" label="tipoTabela"
					   service="lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.findComboTipoTabelaColetaEntrega" 
					   optionProperty="idTipoTabelaColetaEntrega" optionLabelProperty="dsTipoTabelaColetaEntrega" 
					   labelWidth="18%" width="25%" boxWidth="160" 
					   />

		<adsm:combobox property="tpCalculo" 
			label="tpCalculo"
			domain="DM_TP_CALCULO_TABELA_COLETA_ENTREGA"
			labelWidth="18%" width="32%" boxWidth="150" required="false" onlyActiveValues="true"/>
			
		<adsm:combobox property="tipoMeioTransporte.idTipoMeioTransporte" 
			optionLabelProperty="dsTipoMeioTransporte"  
			optionProperty="idTipoMeioTransporte" 
			service="lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.findComboTpMeioTransporte" 
			label="tipoMeioTransporte" labelWidth="18%" width="32%" boxWidth="180"/>
					   		
		<adsm:range label="vigencia"  labelWidth="18%" width="32%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

 		<adsm:combobox property="vigentes" label="vigentes" domain="DM_SIM_NAO" defaultValue="S" labelWidth="18%" width="32%" />
 		
 		
		
		<adsm:lookup label="rota" size="3" maxLength="3" labelWidth="18%" width="82%" 
					 exactMatch="true"
					 dataType="integer" 
					 property="rotaColetaEntrega" 
					 idProperty="idRotaColetaEntrega" 
					 criteriaProperty="nrRota"
					 service="lms.coleta.consultaClientesColetaAction.findLookupRotaColetaEntrega"
					 action="/municipios/manterRotaColetaEntrega" >

			 <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
 		    <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial" />
 		    <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
 		    
        	<adsm:propertyMapping relatedProperty="rotaColetaEntrega.dsRota" modelProperty="dsRota" inlineQuery="false"/>
	        <adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" size="30" disabled="true" serializable="false"/>
        </adsm:lookup>
        
		<adsm:buttonBar freeLayout="true" >
			<adsm:findButton callbackProperty="tabelaFreteColetaEntrega" />
			<adsm:button caption="limpar" onclick="limpar_OnClick();" disabled="false" buttonType="newButton"/>
		</adsm:buttonBar>
		
	</adsm:form>

	<adsm:grid idProperty="idTabelaColetaEntrega"  selectionMode="check" 
				property="tabelaFreteColetaEntrega" unique="true" rows="10"
		       service="lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.findPaginatedCustom" 
		       rowCountService="lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.getRowCountFreteCarreteiroColetaEntregaCustom">
		<adsm:gridColumn width="15%" title="tipoMeioTransporte" property="tipoMeioTransporte.dsTipoMeioTransporte" />
		<adsm:gridColumn width="15%" title="tipoTabela" property="tipoTabelaColetaEntrega.dsTipoTabelaColetaEntrega" />
		
		<adsm:gridColumn width="15%" title="tpCalculo" property="tpCalculo" isDomain="true" />
		
		<adsm:gridColumn title="rota" property="rotaColetaEntrega.nrRota" dataType="integer" mask="0000" width="30" />
		<adsm:gridColumn title="" property="rotaColetaEntrega.dsRota" width="70"/>
		
		<adsm:gridColumn title="cliente" property="cliente.nrIdentificacaoFormatado" width="100" align="right" />
		<adsm:gridColumn title="" property="cliente.nmPessoa" width="100" />
		
		<adsm:gridColumn width="12%" title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate"/>
		<adsm:gridColumn width="11%" title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" />
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
	function pageLoad_cb(data, error) {
		//setElementValue("idFilial",data.filialId);
		setElementValue("idFilial",getNestedBeanPropertyValue(data,"filial.idFilial"));	
		setElementValue("sgFilial",getNestedBeanPropertyValue(data,"filial.sgFilial"));	
		setElementValue("nmFantasia",getNestedBeanPropertyValue(data, "pessoa.nmFantasia"));
		preencheFilial();
	}
	function preencheFilial(){
			setElementValue("filial.idFilial", getElementValue("idFilial"));
			setElementValue("filial.sgFilial", getElementValue("sgFilial"));
			setElementValue("filial.pessoa.nmFantasia", getElementValue("nmFantasia"));
	}
	function limpar_OnClick(){
		preencheFilial();
		resetValue('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega');
		resetValue('dtVigenciaInicial');
		resetValue('dtVigenciaFinal');
		resetValue('tipoMeioTransporte.idTipoMeioTransporte');
		resetValue('vigentes');
		setFocusOnFirstFocusableField();		
	}

	function initWindow(eventObj){
		if(eventObj.name == 'tab_click'){
		    var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("faixaPeso", true);
		}
	}
</script>