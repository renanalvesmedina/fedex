<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	
	function carregaDados_cb(data, error) {
		onPageLoad_cb(data, error);
	    var data = new Array();
	    var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.emitirTabelaFretesAction.findInformacoesUsuarioLogado", "pageLoad",data);
	    xmit({serviceDataObjects:[sdo]});
	}

	function pageLoad_cb(data, error) {
		//setElementValue("idFilial",data.filialId);
		setElementValue("filial.idFilial",getNestedBeanPropertyValue(data,"filial.idFilial"));	
		setElementValue("filial.sgFilial",getNestedBeanPropertyValue(data,"filial.sgFilial"));	
		setElementValue("filial.pessoa.nmFantasia",getNestedBeanPropertyValue(data, "pessoa.nmFantasia"));
		document.getElementById("meioTransporte.nrIdentificador").serializable = true;
		document.getElementById("filial.sgFilial").serializable = true;
		document.getElementById("meioTransporte2.nrFrota").serializable = true;
	}
	
	function changeCombo(field) {
		comboboxChange({e:field});
		if (field.selectedIndex > 0) {
			var data = field.data[field.selectedIndex - 1];
			setElementValue("dtVigenciaInicialR",setFormat(document.getElementById("dtVigenciaInicialR"),getNestedBeanPropertyValue(data,"dtVigenciaInicial")));
			setElementValue("dtVigenciaFinalR",setFormat(document.getElementById("dtVigenciaFinalR"),getNestedBeanPropertyValue(data,"dtVigenciaFinal")));
			resetValue("filial.idFilial");
		}else{
			resetValue("dtVigenciaInicialR");
			resetValue("dtVigenciaFinalR");
		}		
	}
	
	function resetRegionalCallBack_cb(data){
		filial_sgFilial_exactMatch_cb(data);
 	 	if(data != undefined && data.length > 0){
			resetValue(document.getElementById("regional.id"));
			resetValue("dtVigenciaInicialR");
			resetValue("dtVigenciaFinalR");
  		}
	}
	
	function resetRegional(data){
		if(data != undefined){
	  		resetValue(document.getElementById("regional.id"));
	  		resetValue("dtVigenciaInicialR");
			resetValue("dtVigenciaFinalR");
  		}
  		return true;
	}
	
</script>
<adsm:window title="emitirTabelaFretes" 
service="lms.fretecarreteirocoletaentrega.emitirTabelaFretesAction" onPageLoadCallBack="carregaDados">
	<adsm:form action="/freteCarreteiroColetaEntrega/emitirTabelaFretes" height="370"  >
	
		<adsm:combobox property="regional.id" label="regional"  onchange="changeCombo(this);"
				service="lms.contratacaoveiculos.emitirLocalizacaoMeioTransporteAction.findComboRegional"
				optionLabelProperty="siglaDescricao" optionProperty="idRegional" labelWidth="20%" width="83%" boxWidth="205" >		
			<adsm:propertyMapping relatedProperty="regional.desc" modelProperty="siglaDescricao"/>
		</adsm:combobox>
		<adsm:hidden property="regional.desc"/>
		
		<adsm:range label="vigencia" labelWidth="20%">
			<adsm:textbox size="12" property="dtVigenciaInicialR" dataType="JTDate" picker="false" disabled="true"/>
			<adsm:textbox size="12" property="dtVigenciaFinalR" dataType="JTDate" picker="false" disabled="true"/>
		</adsm:range>
		
		
		<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
				service="lms.fretecarreteirocoletaentrega.emitirTabelaFretesAction.findFilial" 
				dataType="text" label="filial" size="3"
				action="/municipios/manterFiliais" labelWidth="20%" width="32%" minLengthForAutoPopUpSearch="3"
				exactMatch="false"   disabled="false" 
				onPopupSetValue="resetRegional"
			    onDataLoadCallBack="resetRegionalCallBack">
				
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia"  modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia"  disabled="true" size="25"  />			
		</adsm:lookup>		
		
 
		<adsm:combobox property="tipoCarreteiro.value" onchange="verificaSelecionado();" domain="DM_TIPO_TABELA_COLETA_ENTREGA" label="tabelaDe"   optionLabelProperty="" optionProperty="" labelWidth="20%" width="80%" required="true">
			<adsm:propertyMapping relatedProperty="tipoCarreteiro.desc" modelProperty="description"/>
			<adsm:hidden property="tipoCarreteiro.desc"/>
		</adsm:combobox>


		<adsm:combobox property="tipoMeioTransporte.idTipoMeioTransporte" disabled="true"
				optionProperty="idTipoMeioTransporte" optionLabelProperty="dsTipoMeioTransporte"
				service="lms.fretecarreteirocoletaentrega.emitirTabelaFretesAction.findComboTpMeioTransporte"
				label="tipoMeioTransporte" labelWidth="20%" width="30%" boxWidth="205" cellStyle="vertical-align:bottom;" >
            <adsm:propertyMapping modelProperty="dsTipoMeioTransporte" relatedProperty="tipoMeioTransporte.dsTipoMeioTransporte"/>
        </adsm:combobox>
		<adsm:hidden property="tipoMeioTransporte.dsTipoMeioTransporte"/>
		
		<adsm:combobox property="tipoTabela.idTipoTabela" disabled="true" 
				service="lms.fretecarreteirocoletaentrega.emitirTabelaFretesAction.findComboTipoTabela" 
				label="tipoTabela" optionLabelProperty="dsTipoTabelaColetaEntrega" optionProperty="idTipoTabelaColetaEntrega" 
				labelWidth="20%" width="80%" boxWidth="205" >
			<adsm:propertyMapping modelProperty="dsTipoTabelaColetaEntrega" relatedProperty="tipoTabela.dsTipoTabelaColetaEntrega"/>
			<adsm:hidden property="tipoTabela.dsTipoTabelaColetaEntrega"/>
		</adsm:combobox>

		<adsm:lookup dataType="text" property="proprietario" idProperty="idProprietario"
				service="lms.fretecarreteirocoletaentrega.emitirTabelaFretesAction.findLookupProprietario"
				action="/contratacaoVeiculos/manterProprietarios"
				label="proprietario" labelWidth="20%" width="19%" size="20" maxLength="20" 
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				disabled="true"
				> 
			<adsm:propertyMapping relatedProperty="pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping relatedProperty="proprietario.nrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado" />
			<adsm:textbox dataType="text" property="pessoa.nmPessoa" size="35" width="55%" disabled="true" />
		</adsm:lookup>

		<adsm:hidden property="proprietario.nrIdentificacao" serializable="true"/>
		<adsm:lookup dataType="text" property="meioTransporte2" idProperty="idMeioTransporte"
				service="lms.fretecarreteirocoletaentrega.emitirTabelaFretesAction.findLookupMeioTransp" picker="false" maxLength="6"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" criteriaProperty="nrFrota"
				label="meioTransporte" labelWidth="20%" width="9%" size="8" disabled="true">
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao"
					modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false" />
			<adsm:propertyMapping criteriaProperty="pessoa.nmPessoa"
					modelProperty="proprietario2.pessoa.nmPessoa" inlineQuery="false"/>						
					
			<adsm:propertyMapping criteriaProperty="proprietario.idProprietario"
					modelProperty="proprietario.idProprietario"/>						
					
			<adsm:propertyMapping criteriaProperty="meioTransporte.nrIdentificador"
					modelProperty="nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte"
					modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping relatedProperty="meioTransporte.nrIdentificador"
					modelProperty="nrIdentificador" />
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporte" idProperty="idMeioTransporte"
				service="lms.fretecarreteirocoletaentrega.emitirTabelaFretesAction.findLookupMeioTransp" maxLength="25"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" criteriaProperty="nrIdentificador"
				width="72%" size="20" labelWidth="20%" disabled="true">
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao"
					modelProperty="proprietario.pessoa.nrIdentificacao"  inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="pessoa.nmPessoa"
					modelProperty="proprietario2.pessoa.nmPessoa"  inlineQuery="false"/>						
			<adsm:propertyMapping criteriaProperty="proprietario.idProprietario"
					modelProperty="proprietario.idProprietario"/>						
			<adsm:propertyMapping criteriaProperty="meioTransporte2.nrFrota"
					modelProperty="nrFrota" disable="false"/>
			<adsm:propertyMapping relatedProperty="meioTransporte2.idMeioTransporte"
					modelProperty="idMeioTransporte" />	
			<adsm:propertyMapping relatedProperty="meioTransporte2.nrFrota"
					modelProperty="nrFrota" />	
		</adsm:lookup>

   		<adsm:combobox property="tpFormatoRelatorio" label="formatoRelatorio" labelWidth="20%" 
					   required="true" defaultValue="pdf" domain="DM_FORMATO_RELATORIO" width="80%" >
		</adsm:combobox>
						
		<adsm:range label="vigencia" labelWidth="20%" width="80%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/> 
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
		</adsm:range>
		<adsm:buttonBar>
				<adsm:reportViewerButton service="lms.fretecarreteirocoletaentrega.emitirTabelaFretesAction"/>
				<adsm:button id="botaoLimpar" caption="limpar" onclick="limpar_OnClick();" disabled="false" buttonType="resetButton"/>
		</adsm:buttonBar>	
	</adsm:form>
</adsm:window>
<script>
	function initWindow(evento){
		desabilitaTodosCampos();
	}
	

	function limpar_OnClick()
	{
		
		cleanButtonScript(undefined,undefined,undefined);
		

	    var data = new Array();
	    var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.emitirTabelaFretesAction.findInformacoesUsuarioLogado", "pageLoad",data);
	    xmit({serviceDataObjects:[sdo]});	
	}
	function desabilitaTodosCampos() {
		setDisabled("tipoTabela.idTipoTabela",true);
		setDisabled("tipoMeioTransporte.idTipoMeioTransporte",true);
		setDisabled("meioTransporte2.idMeioTransporte",true);
		setDisabled("meioTransporte.idMeioTransporte",true);
		setDisabled("proprietario.idProprietario",true);
		resetValue("meioTransporte2.idMeioTransporte");
		resetValue("meioTransporte.idMeioTransporte");
		resetValue("proprietario.idProprietario");
		resetValue("tipoTabela.idTipoTabela");
		resetValue("tipoMeioTransporte.idTipoMeioTransporte");
	}

	function verificaSelecionado(){
		if (getElementValue("tipoCarreteiro.value") == 'A'){
			agregadoSelecionado();
		} else if (getElementValue("tipoCarreteiro.value") == 'E'){
			eventualSelecionado();
		} else {
			outrosSelecionado();
		}
		comboboxChange({e:document.getElementById("tipoCarreteiro.value")});
		
	}
	
	function agregadoSelecionado() {
		// Ativando campos
		setDisabled("tipoTabela.idTipoTabela",false);
		setDisabled("tipoMeioTransporte.idTipoMeioTransporte",false);
		// Desativando campos
		setDisabled("meioTransporte2.idMeioTransporte",true);
		setDisabled("meioTransporte.idMeioTransporte",true);
		setDisabled("proprietario.idProprietario",true);
		resetValue("meioTransporte2.idMeioTransporte");
		resetValue("meioTransporte.idMeioTransporte");
		resetValue("proprietario.idProprietario");
	}
	function eventualSelecionado() {
		// Ativando campos
		setDisabled("tipoTabela.idTipoTabela",true);
		setDisabled("tipoMeioTransporte.idTipoMeioTransporte",true);
		resetValue("tipoTabela.idTipoTabela");
		resetValue("tipoMeioTransporte.idTipoMeioTransporte");
		// Desativando campos
		setDisabled("meioTransporte2.idMeioTransporte",false);
		setDisabled("meioTransporte.idMeioTransporte",false);
		setDisabled("proprietario.idProprietario",false);
	}
	function outrosSelecionado() {
		desabilitaTodosCampos();
	}
	
	
	
</script>
