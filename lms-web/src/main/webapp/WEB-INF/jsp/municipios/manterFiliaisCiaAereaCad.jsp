<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function setDisabledBotoes(valor) {
		setDisabled("__horarios",valor);
		setDisabled("__prioridades",valor);
		setDisabled("__contatos",valor);
		setDisabled("__enderecos",valor);
		setDisabled("__dados",valor);
		setDisabled("__inscricaoEstadual",valor);
	}
	
	/**
	 * Seta id da pessoa após inserir um registro.
	 * Desabilita tipo e número de identificação após inserir um registro.
	 */
	function filialCiaAereaLoad_cb(data, exception){
		
		onDataLoad_cb(data, exception);

		onDataLoadCallbackPessoaWidget({tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"), 
										numberElement:document.getElementById("pessoa.nrIdentificacao")});
		
		var idEmpresa = getNestedBeanPropertyValue(data, "empresa.idEmpresa");
		var dsEmpresa = getNestedBeanPropertyValue(data, "empresa.dsEmpresa");
		setComboBoxElementValue(document.getElementById("empresa.idEmpresa"), idEmpresa, idEmpresa, dsEmpresa);
	  	setElementValue("empresa.pessoa.nmPessoa",dsEmpresa);
	  		  		
		var idPessoa = getNestedBeanPropertyValue(data, "idFilialCiaAerea");
		if (idPessoa != undefined){
			setElementValue("pessoa.idPessoa", idPessoa);
			setDisabled("pessoa.tpIdentificacao", true );
			setDisabled("pessoa.nrIdentificacao", true );
			document.getElementById("empresa.idEmpresa").disabled = true;
			document.getElementById("empresa.idEmpresa").disabled = false;
			setDisabled("empresa.idEmpresa", true);
			setDisabled("aeroporto.idAeroporto", true);
			setDisabled("aeroporto.sgAeroporto", true);			
		}
		
		var dtVigenciaInicialDetalhe = getNestedBeanPropertyValue(data, "dtVigenciaInicial");
  		document.getElementById("dtVigenciaInicialDetalhe").value = dtVigenciaInicialDetalhe;
		
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		// se vigencia inicial > hoje
		if (acaoVigenciaAtual == 0) {
			estadoNovo();
			setDisabled(document,false);
			setDisabled("aeroporto.pessoa.nmPessoa",true);
			setDisabledBotoes(true);
			// setDisabled("empresa.idEmpresa", document.getElementById("empresa.idEmpresa").masterLink == "true");
			setDisabled("empresa.idEmpresa", true);
			setDisabled("aeroporto.idAeroporto", true);
			setDisabled("aeroporto.sgAeroporto", true);			
			setDisabled("pessoa.tpIdentificacao", true);
			setDisabled("pessoa.nrIdentificacao",true);
			setFocusOnFirstFocusableField(document); 
		// se vigencia inicial < hoje e final > hoje			
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(document,true);
			setDisabled("__botaoNovo",false);
			setDisabled("__botaoStore",false);
			setDisabled("__buttonBar:0.removeButton",true);
			setDisabled("pessoa.nmPessoa",false);
			setDisabled("pessoa.dsEmail",false);
			setDisabled("dtVigenciaFinal",false);
			setFocusOnFirstFocusableField(document); 
		
		// se vigencia < hoje (historico)
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(document,true);
			setDisabled("__botaoNovo",false);
			setFocusOnNewButton();
		}
		setDisabledBotoes(false);

				
	}
	
	// ############################################################
	// estado de novo
	// ############################################################
	function estadoNovo() {
		setDisabled(document,false);
		setDisabled("aeroporto.idAeroporto",document.getElementById("aeroporto.idAeroporto").masterLink == "true");
		setDisabled("aeroporto.pessoa.nmPessoa",true);
		setDisabled("pessoa.nrIdentificacao",true); 
		setDisabledBotoes(true);
		setDisabled("__buttonBar:0.removeButton",true);
		setDisabled("empresa.idEmpresa", document.getElementById("empresa.idEmpresa").masterLink == "true");
		setFocusOnFirstFocusableField(document);
	}
	
	
	// ############################################################
	// tratamento dos eventos da initWindow para <tab_click>, 
	// <gridRow_click>, <newButton_click> e/ou <removeButton_click> 
	// ############################################################
	function initWindow(eventObj) {

		if ((eventObj.name == "newButton_click") || (eventObj.name == "tab_click") || (eventObj.name == "removeButton")) {
			estadoNovo();
		}

		else if (eventObj.name == "gridRow_click" || eventObj.name == "storeButton") {
			setDisabled("empresa.idEmpresa", true);
			setDisabled("aeroporto.idAeroporto", true);
			setDisabled("aeroporto.sgAeroporto", true);			
			setDisabled("pessoa.tpIdentificacao", true);
			setDisabled("pessoa.nrIdentificacao",true);
		}  

    }
    
    function pageLoadFilialCiaAerea() {
   		onPageLoad();
 		initPessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa")
   				   ,tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
      			   ,numberElement:document.getElementById("pessoa.idPessoa")});
	}
	
	    
</script>
<adsm:window service="lms.municipios.filialCiaAereaService" onPageLoadCallBack="filiaisCiaAerea_pageLoad" onPageLoad="pageLoadFilialCiaAerea">
	<adsm:form action="/municipios/manterFiliaisCiaAerea" idProperty="idFilialCiaAerea"
			 service="lms.municipios.filialCiaAereaService.findByIdDetalhamento" onDataLoadCallBack="filialCiaAereaLoad" >

		<adsm:hidden property="labelPessoa" serializable="false" value="Filial de cia aérea"/>
		<adsm:hidden property="tpSituacaoAero" value="A"/>
		
		<adsm:hidden property="idEmpresaTemp" />
	    <adsm:combobox label="ciaAerea" property="empresa.idEmpresa" service="lms.municipios.empresaService.findCiaAerea"
	    		onchange="onCiaAereaChange();"
	    		optionLabelProperty="pessoa.nmPessoa" optionProperty="idEmpresa" labelWidth="17%" width="33%" required="true" onlyActiveValues="true" boxWidth="200"/>
	    <adsm:hidden property="empresa.pessoa.nmPessoa" serializable="true"/>

		<adsm:lookup label="aeroporto" service="lms.municipios.aeroportoService.findLookup" action="municipios/manterAeroportos" 
			 	 	 dataType="text" property="aeroporto" idProperty="idAeroporto" labelWidth="15%" width="35%" size="3" 
			 	 	 maxLength="3" criteriaProperty="sgAeroporto" required="true">
 	 	    <adsm:propertyMapping relatedProperty="aeroporto.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
 	 	    <adsm:propertyMapping criteriaProperty="tpSituacaoAero" modelProperty="tpSituacao"/>
			<adsm:textbox property="aeroporto.pessoa.nmPessoa" dataType="text" size="30" maxLength="30" disabled="true"/>	 	    	      
	 	</adsm:lookup>
	 	
	 	<adsm:hidden property="pessoa.tpPessoa" value="J"/>
		
		<adsm:complement label="identificacao" labelWidth="17%" width="33%" required="true" >
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad" />
			<adsm:lookup definition="IDENTIFICACAO_PESSOA" 
					service="lms.municipios.filialCiaAereaService.validateIdentificacao" 
					onDataLoadCallBack="pessoaCallback" />
		</adsm:complement>
		<adsm:textbox dataType="text" property="pessoa.nmPessoa" label="razaoSocial" maxLength="50" size="30" required="true" labelWidth="15%" width="35%"/>
				
		<adsm:textbox dataType="email" property="pessoa.dsEmail" label="email" size="30" maxLength="60" labelWidth="17%" width="33%"/>
		<adsm:textbox label="codigoFornecedor" dataType="integer" property="cdFornecedor" size="15" maxLength="10" width="55%" labelWidth="17%" required="true"/>
		<adsm:checkbox property="blTaxaTerrestre" label="taxaTerrestre" labelWidth="17%" width="33%"/>
		<adsm:checkbox property="blImprimeMinuta" label="imprimeMinuta" labelWidth="15%" width="35%"/>

		<adsm:range label="vigencia" labelWidth="17%" width="83%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" />
             <adsm:hidden property="dtVigenciaInicialDetalhe" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>

		<adsm:buttonBar lines="2" >
		    <adsm:button caption="inscricoesEstaduais" id="__inscricaoEstadual" action="/configuracoes/manterInscricoesEstaduais" cmd="main">
				<adsm:linkProperty src="pessoa.idPessoa" target="pessoa.idPessoa"/>			
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>						
			</adsm:button>
			<adsm:button caption="horariosAtendimento" action="municipios/manterHorariosAtendimentoFilialCiaAerea"
					cmd="main" boxWidth="155" id="__horarios" >
				<adsm:linkProperty src="idFilialCiaAerea" target="filialCiaAerea.idFilialCiaAerea" disabled="true"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="filialCiaAerea.pessoa.nmPessoa" disabled="true"/>
				<adsm:linkProperty src="pessoa.tpIdentificacao" target="filialCiaAerea.pessoa.tpIdentificacao" disabled="true"/>
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="filialCiaAerea.pessoa.nrIdentificacao" disabled="true"/>
				<adsm:linkProperty src="empresa.idEmpresa" target="filialCiaAerea.empresa.idEmpresa" disabled="true"/>
				<adsm:linkProperty src="empresa.pessoa.nmPessoa" target="filialCiaAerea.empresa.pessoa.nmPessoa" disabled="true"/>
				<adsm:linkProperty src="aeroporto.idAeroporto" target="filialCiaAerea.aeroporto.idAeroporto" disabled="true"/>						
				<adsm:linkProperty src="aeroporto.pessoa.nmPessoa" target="filialCiaAerea.aeroporto.pessoa.nmPessoa" disabled="true"/>						
				<adsm:linkProperty src="aeroporto.sgAeroporto" target="filialCiaAerea.aeroporto.sgAeroporto" disabled="true"/>						
			</adsm:button>
			 
			<%-- 
			<adsm:button caption="prioridadeEmbarqueFilial" action="municipios/manterFiliaisCiaAereaFiliais"
					cmd="main" boxWidth="365" breakBefore="true" id="__prioridades" >
				<adsm:linkProperty src="idFilialCiaAerea" target="filialCiaAerea.idFilialCiaAerea" disabled="true"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="filialCiaAerea.pessoa.nmPessoa" disabled="true"/>
				<adsm:linkProperty src="pessoa.tpIdentificacao" target="filialCiaAerea.pessoa.tpIdentificacao" disabled="true"/>
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="filialCiaAerea.pessoa.nrIdentificacao" disabled="true"/>
				<adsm:linkProperty src="empresa.idEmpresa" target="ciaFilialMercurio.empresa.idEmpresa" />
				<adsm:linkProperty src="empresa.pessoa.nmPessoa" target="ciaFilialMercurio.empresa.pessoa.nmPessoa" />
				<adsm:linkProperty src="aeroporto.idAeroporto" target="filialCiaAerea.aeroporto.idAeroporto" disabled="true"/>						
				<adsm:linkProperty src="aeroporto.pessoa.nmPessoa" target="filialCiaAerea.aeroporto.pessoa.nmPessoa" disabled="true"/>						
				<adsm:linkProperty src="aeroporto.sgAeroporto" target="filialCiaAerea.aeroporto.sgAeroporto" disabled="true"/>						
			</adsm:button>
			--%>
			 
			<adsm:button caption="ciasAereasFiliaisButton" action="municipios/manterCiasAereasFiliais"
					cmd="main" boxWidth="260" breakBefore="true" id="__prioridades" >
				<adsm:linkProperty src="empresa.idEmpresa" target="empresa.idEmpresa" />
				<adsm:linkProperty src="empresa.pessoa.nmPessoa" target="empresa.pessoa.nmPessoa" />
			</adsm:button>
			
			<adsm:button caption="enderecos" action="configuracoes/manterEnderecoPessoa" cmd="main" boxWidth="72" id="__enderecos" >
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp" disabled="true"/>
				<adsm:linkProperty src="pessoa.idPessoa" target="pessoa.idPessoa" disabled="true"/>
				<adsm:linkProperty src="pessoa.tpIdentificacao" target="pessoa.tpIdentificacao" disabled="true"/>
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" disabled="true"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" disabled="true"/>
			</adsm:button>
			
			<adsm:button caption="contatos" action="configuracoes/manterContatos" cmd="main" boxWidth="64" id="__contatos" >
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp" disabled="true"/>
				<adsm:linkProperty src="pessoa.idPessoa" target="pessoa.idPessoa" disabled="true"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" disabled="true"/>
				<adsm:linkProperty src="pessoa.tpIdentificacao" target="pessoa.tpIdentificacao" disabled="true"/>
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" disabled="true"/>
			</adsm:button>
			<adsm:button caption="dadosBancarios" action="configuracoes/manterDadosBancariosPessoa" cmd="main" boxWidth="105" id="__dados" >		
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp" disabled="true"/>
				<adsm:linkProperty src="pessoa.idPessoa" target="pessoa.idPessoa" disabled="true"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" disabled="true"/>
				<adsm:linkProperty src="pessoa.tpIdentificacao" target="pessoa.tpIdentificacao" disabled="true"/>
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" disabled="true"/>
			</adsm:button>			
			<adsm:button caption="salvar" buttonType="storeButton" id="__botaoStore"
					service="lms.municipios.filialCiaAereaService.storeMap" callbackProperty="customStore" />
			<adsm:button caption="novo" buttonType="newButton" id="__botaoNovo"
					onclick="newButtonScript(this.document); estadoNovo();" />
			<adsm:removeButton service="lms.municipios.filialCiaAereaService.removefilialCiaAereaById"/>
		</adsm:buttonBar>
		<script type="text/javascript">
		var labelPessoaTemp = '<adsm:label key="empresa"/>';
		</script> 
	</adsm:form>
</adsm:window>

<script>
    document.getElementById("labelPessoa").masterLink = "true";
    document.getElementById("pessoa.nrIdentificacao").serializable = true;
    
    function filiaisCiaAerea_pageLoad_cb(data, error){
		onPageLoad_cb(data, error);
		setElementValue(document.getElementById("labelPessoa"),labelPessoaTemp);
		changeTypePessoaWidget(
			{tpTipoElement:document.getElementById("pessoa.tpPessoa"),
			 tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'),
			 numberElement:document.getElementById('pessoa.nrIdentificacao'),tabCmd:'cad'});
			 
		if (getElementValue("idEmpresaTemp") != "") {
			setComboBoxElementValue(document.getElementById("empresa.idEmpresa"), getElementValue("idEmpresaTemp"),
					getElementValue("idEmpresaTemp"), getElementValue("empresa.pessoa.nmPessoa"));
		}
	}
	function uf_Popup(data){		
		setDisabled("inscricaoEstadual.nrInscricaoEstadual", false);
		return true;
	}	
	function uf_DataLoad_cb(data, error){
		inscricaoEstadual_unidadeFederativa_sgUnidadeFederativa_exactMatch_cb(data, error);
	}
	function uf_onChange(obj){
		document.getElementById("inscricaoEstadual.nrInscricaoEstadual").value = "";
		
		if (obj.value != '') {
			setDisabled("inscricaoEstadual.nrInscricaoEstadual", false);			
		} else {
			setDisabled("inscricaoEstadual.nrInscricaoEstadual", true);
		}
		return inscricaoEstadual_unidadeFederativa_sgUnidadeFederativaOnChangeHandler();
	}
	
	function onCiaAereaChange() {
		if (document.getElementById("empresa.idEmpresa").value != "" ) {
		    var combo = document.getElementById("empresa.idEmpresa");
		    var dsCombo = combo.options[combo.selectedIndex].text;
		    setElementValue("empresa.pessoa.nmPessoa", dsCombo);
		}
	}
	
	function pessoaCallback_cb(data,error) {
		if (error != undefined) {
			alert(error);
			resetValue("pessoa.nrIdentificacao");
			setFocus(document.getElementById('pessoa.nrIdentificacao'));
			return false;
		}
		return pessoa_nrIdentificacao_exactMatch_cb(data);
	}
	
	/**
	 * Valida a pessoa de acordo com o retorno do método na service.
	 */
	function validaIdPessoa_cb(data,exception) {
		if (exception != undefined) {
			alert(exception);
			reiniciaValores();
			resetValue(document.getElementById("pessoa.nrIdentificacao"));
			setFocus(document.getElementById("pessoa.nrIdentificacao"));
			return false;
		}
		
		//Pessoa não cadastrada na especialização
		if (data.idPessoa != undefined){
			setElementValue("pessoa.idPessoa",data.idPessoa);
			setElementValue("pessoa.nmPessoa",data.nmPessoa);
			setElementValue("pessoa.tpIdentificacao",data.tpIdentificacao);
			setElementValue("pessoa.tpPessoa",data.tpPessoa);
			setElementValue("pessoa.dsEmail",data.dsEmail);
			return true;
		}
	
		//Pessoa não existe
		//Então reiniciar valores
		reiniciaValores();
		
		return true;	
	}
	
	/**
	 * Reseta campos com valores de pessoa.
	 */
	function reiniciaValores(){
		resetValue(document.getElementById("pessoa.idPessoa"));
		resetValue(document.getElementById("pessoa.nmPessoa"));
		resetValue(document.getElementById("pessoa.tpPessoa"));
		resetValue(document.getElementById("pessoa.dsEmail"));
	}
	
	function customStore_cb(data,error) {
		store_cb(data,error);
		if (error != undefined) {
			return false;
		}
		
		var dtVigenciaInicialDetalhe = getNestedBeanPropertyValue(data, "dtVigenciaInicial");
  		document.getElementById("dtVigenciaInicialDetalhe").value = dtVigenciaInicialDetalhe;
		if (data != undefined && ((document.getElementById('idFilialCiaAerea').value) !=
								  (getNestedBeanPropertyValue(data,':idFilialCiaAerea'))))
			document.getElementById('idFilialCiaAerea').value =
					getNestedBeanPropertyValue(data,':idFilialCiaAerea');
		filialCiaAereaLoad_cb(data);
		setFocusOnNewButton();
	}
	
</script>