<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.pendencia.cancelarMDAAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/pendencia/cancelarMDA">
	
		<adsm:hidden property="origem"/>

		<adsm:lookup property="filialByIdFilialOrigem" idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 service="lms.pendencia.cancelarMDAAction.findLookupBySgFilial" 
					 action="/municipios/manterFiliais" 
					 onchange="return sgFilialOnChangeHandler();"
					 popupLabel="pesquisarFilial" 
					 onDataLoadCallBack="disableNrDoctoServico"
					 label="mda" dataType="text" labelWidth="20%" width="30%" 
					 size="5" maxLength="3" picker="false" serializable="false" disabled="true">			
			<adsm:lookup property="mda" idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico" 
						 service="lms.pendencia.cancelarMDAAction.findMdaByNrDoctoServicoByIdFilialOrigem" 
						 action="/pendencia/consultarMDA"
						 afterPopupSetValue="carregaDadosMda"
						 onDataLoadCallBack="carregaDadosMda" 
						 onchange="return checkValueMda(this.value)"						 
						 dataType="integer" maxLength="10" size="10" mask="00000000" >
				<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.idFilial" modelProperty="filialByIdFilialOrigem.idFilial" disable="true"/>
				<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.sgFilial" modelProperty="filialByIdFilialOrigem.sgFilial" disable="true"/>
				<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.idFilial" modelProperty="filialByIdFilialOrigem.idFilial" blankFill="false" />
				<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.sgFilial" modelProperty="filialByIdFilialOrigem.sgFilial" blankFill="false"/>
				<adsm:propertyMapping criteriaProperty="origem"	modelProperty="origem" />											
			</adsm:lookup>					 

		</adsm:lookup>	
		
		<adsm:textbox property="dhCancelamento" label="cancelamento" dataType="JTDateTimeZone" 
					  labelWidth="20%" width="30%" picker="false" disabled="true"/>

		<adsm:textbox property="filialOrigem.sgFilial"
					  label="filialOrigem" dataType="text" labelWidth="20%" width="80%"
					  size="5" maxLength="3" disabled="true" serializable="false">
			<adsm:textbox property="filialOrigem.pessoa.nmFantasia" 
						  dataType="text" size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox property="filialByIdFilialDestino.sgFilial"
					  label="filialDestino" dataType="text" labelWidth="20%" width="80%"
					  size="5" maxLength="3" disabled="true" serializable="false">
			<adsm:textbox property="filialByIdFilialDestino.pessoa.nmFantasia" 
						  dataType="text" size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox property="clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado"
					  label="remetente" dataType="text" labelWidth="20%" width="80%"
					  size="20" maxLength="20" disabled="true" serializable="false">
			<adsm:textbox property="clienteByIdClienteRemetente.pessoa.nmPessoa" 
						  dataType="text" size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox property="clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado"
					  label="destinatario" dataType="text" labelWidth="20%" width="80%"
					  size="20" maxLength="20" disabled="true" serializable="false">
			<adsm:textbox property="clienteByIdClienteDestinatario.pessoa.nmPessoa" 
						  dataType="text" size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox property="clienteByIdClienteConsignatario.pessoa.nrIdentificacaoFormatado"
					  label="consignatario" dataType="text" labelWidth="20%" width="80%"
					  size="20" maxLength="20" disabled="true" serializable="false">
			<adsm:textbox property="clienteByIdClienteConsignatario.pessoa.nmPessoa" 
						  dataType="text" size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:textbox>
	
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="cancelarMDA" id="cancelButton" onclick="cancelarRegistro(this.form)"/>
			<adsm:button caption="limpar" id="cleanButton" onclick="cleanButtonFunction()"/>
		</adsm:buttonBar>
		
		<script>
			var lms_17005 = '<adsm:label key="LMS-17005"/>';
		</script>
	</adsm:form>
	
	<adsm:grid property="itemMda" idProperty="idItemMda" gridHeight="180" selectionMode="none"
			   scrollBars="vertical" unique="true" onRowClick="gridRowClickDisable"
			   service="lms.pendencia.cancelarMDAAction.findPaginatedItemMda"
			   rowCountService="lms.pendencia.cancelarMDAAction.getRowCountItemMda">
		
		<adsm:gridColumn title="documentoServico" property="doctoServico.tpDocumentoServico" isDomain="true" width="30"/>
        <adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
        	<adsm:gridColumn title="" property="doctoServico.filialByIdFilialOrigem.sgFilial" width="30" />
            <adsm:gridColumn title="" property="doctoServico.nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>		
		<adsm:gridColumn title="descricaoMercadoria" property="dsMercadoria" width="305" />
		<adsm:gridColumn title="naturezaProduto" property="naturezaProduto.dsNaturezaProduto" width="150" />
		<adsm:gridColumn title="volumes" property="qtVolumes" width="80" align="right" />
		<adsm:gridColumn title="peso" property="psItem" dataType="decimal" mask="#,###,##0.000" align="right" unit="kg"/>
		
		<adsm:buttonBar/>
	</adsm:grid>	

</adsm:window>

<script type="text/javascript">
	// Se a tela for chamada pelo botão emitir de Abrir MDA.
	var u = new URL(parent.location.href);
   	var origem = u.parameters["origem"];
   	setElementValue("origem", origem);

 
	/**
	 * Função chamada quando a tela é inicializada
	 */
	function initWindow(eventObj) {
		setDisabled("cleanButton", false);
		if(eventObj.name == "tab_load" || eventObj.name == "cleanButton_click") {
			//setDisabled(document.getElementById("mda.nrDoctoServico"), true, undefined, true);
			setDisabled("cancelButton", true);
		}
		if(getElementValue("origem") == "consultarMda") {
			populaTela();
		} else {
			setElementValue("origem", "cancelarMda");
		}	
		if(eventObj.name == "tab_load" || eventObj.name == 'cleanButton_click'){
			loadDadosSessao();
	}
	}

	//Chama o servico que retorna os dados do usuario logado 
	function loadDadosSessao(){
		var data = new Array();
		var sdo = createServiceDataObject("lms.pendencia.cancelarMDAAction.findDadosSessao",
					"preencheDadosSessao",data);
		xmit({serviceDataObjects:[sdo]});
	}

	//Funcao de callback do servico que retorna os dados do usuario logado. 
	function preencheDadosSessao_cb(data, exception){
		if (exception == null){
			setElementValue("filialByIdFilialOrigem.idFilial", getNestedBeanPropertyValue(data, "idFilial"));
			setElementValue("filialByIdFilialOrigem.sgFilial", getNestedBeanPropertyValue(data, "sgFilial"));
		}
	}

	/**
	 * Função que popula a tela.
	 */
	function populaTela() {
		setDisabled("mda.idDoctoServico", false);				
		//setDisabled("filialByIdFilialOrigem.idFilial", false);
		setElementValue("filialByIdFilialOrigem.idFilial", u.parameters["filialByIdFilialOrigem.idFilial"]);
		var sgFilial = document.getElementById("filialByIdFilialOrigem.sgFilial");
		setElementValue(sgFilial, u.parameters["filialByIdFilialOrigem.sgFilial"]);
		setElementValue("mda.idDoctoServico", u.parameters["idDoctoServico"]);
		var nrDoctoServico = document.getElementById("mda.nrDoctoServico");
		setElementValue(nrDoctoServico, u.parameters["nrDoctoServico"]);
   		lookupChange({e:document.getElementById("mda.idDoctoServico"), forceChange:true});
	 }
	 
	function pageLoad_cb(data, error){
		onPageLoad_cb();
		if (error){
			alert(error);
			return false;
		}
		if(getElementValue("origem") == "consultarMda") {
			//setDisabled("filialByIdFilialOrigem.idFilial", false);
		}
	}

	/**
	 * Controla o objeto de DoctoServico
	 */	
	function sgFilialOnChangeHandler() {	
		if (getElementValue("filialByIdFilialOrigem.sgFilial") == "") {
			//setDisabled(document.getElementById("mda.nrDoctoServico"), true, undefined, true);	
			resetValue(this.document);
			itemMdaGridDef.resetGrid();
			setDisabled("cancelButton", true);
		} else {
			//setDisabled(document.getElementById("mda.nrDoctoServico"), false, undefined, true);	
		}
		return lookupChange({e:document.forms[0].elements["filialByIdFilialOrigem.idFilial"]});
	}
	
	function disableNrDoctoServico_cb(data, error) {
		if (data.length == 0) {
			setDisabled(document.getElementById("mda.nrDoctoServico"), false, undefined, true);	
		}
		return lookupExactMatch({e:document.getElementById("filialByIdFilialOrigem.idFilial"), data:data});
	}
	
	/**
	 * Verifica o atual valor do campo de nrControleCarga
	 */
	function checkValueMda(valor) {		
		mda_nrDoctoServicoOnChangeHandler();
		
		if (valor == "") {			
			var idFilial = getElementValue("filialByIdFilialOrigem.idFilial");
   	        var sgFilial = getElementValue("filialByIdFilialOrigem.sgFilial");
       	    resetValue(this.document);       	    
       	    itemMdaGridDef.resetGrid();
       	    setDisabled("cancelButton", true);
           	setElementValue("filialByIdFilialOrigem.idFilial", idFilial);
            setElementValue("filialByIdFilialOrigem.sgFilial", sgFilial);
		}		
		return true;
	}	

	/**
	 * Função chamada no retorno dos dados da pop-up na lookup de MDA.
	 */
	function carregaDadosMda(data) {
		setDisabled("mda.idDoctoServico", false);
		lookupChange({e:document.getElementById("mda.idDoctoServico"), forceChange:true});
	}

	/**
	 * Função chamada no retorno dos dados de callback na lookup de MDA.
	 */	
	function carregaDadosMda_cb(data) {
		mda_nrDoctoServico_exactMatch_cb(data);		
		if(data[0]) {
			setDisabled("cancelButton", true);
			if(data[0].tpStatusMda.value == "E" || data[0].tpStatusMda.value == "G") {
				if(data[0].blManifesto) {
					setDisabled("cancelButton", false);
				}
			} 
			
			if(data[0].dhCancelamento) {
				setElementValue("dhCancelamento", setFormat(document.getElementById("dhCancelamento"), data[0].dhCancelamento));			
			} else {
				resetValue("dhCancelamento");
			}
			
			setElementValue("filialOrigem.sgFilial", data[0].filialByIdFilialOrigem.sgFilial);
			setElementValue("filialOrigem.pessoa.nmFantasia", data[0].filialByIdFilialOrigem.pessoa.nmFantasia);

			setElementValue("filialByIdFilialOrigem.idFilial", data[0].filialByIdFilialOrigem.idFilial);
			setElementValue("filialByIdFilialOrigem.sgFilial", data[0].filialByIdFilialOrigem.sgFilial);
		
			if(data[0].filialByIdFilialDestino) {
				setElementValue("filialByIdFilialDestino.sgFilial", data[0].filialByIdFilialDestino.sgFilial);
				setElementValue("filialByIdFilialDestino.pessoa.nmFantasia", data[0].filialByIdFilialDestino.pessoa.nmFantasia);
			} else {
				setElementValue("filialByIdFilialDestino.sgFilial", "");
				setElementValue("filialByIdFilialDestino.pessoa.nmFantasia", "");		
			}
			if(data[0].clienteByIdClienteRemetente) {
				setElementValue("clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado", data[0].clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado);
				setElementValue("clienteByIdClienteRemetente.pessoa.nmPessoa", data[0].clienteByIdClienteRemetente.pessoa.nmPessoa);
			} else {
				setElementValue("clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado", "");
				setElementValue("clienteByIdClienteRemetente.pessoa.nmPessoa", "");		
			}
			if(data[0].clienteByIdClienteDestinatario) {
				setElementValue("clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado", data[0].clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado);
				setElementValue("clienteByIdClienteDestinatario.pessoa.nmPessoa", data[0].clienteByIdClienteDestinatario.pessoa.nmPessoa);
			} else {
				setElementValue("clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado", "");
				setElementValue("clienteByIdClienteDestinatario.pessoa.nmPessoa", "");
			}
			if(data[0].clienteByIdClienteConsignatario) {
				setElementValue("clienteByIdClienteConsignatario.pessoa.nrIdentificacaoFormatado", data[0].clienteByIdClienteConsignatario.pessoa.nrIdentificacaoFormatado);
				setElementValue("clienteByIdClienteConsignatario.pessoa.nmPessoa", data[0].clienteByIdClienteConsignatario.pessoa.nmPessoa);
			} else {
				setElementValue("clienteByIdClienteConsignatario.pessoa.nrIdentificacaoFormatado", "");
				setElementValue("clienteByIdClienteConsignatario.pessoa.nmPessoa", "");
			}

	    	carregaGrid();	    	
		} else {
			var idFilial = getElementValue("filialByIdFilialOrigem.idFilial");
   	        var sgFilial = getElementValue("filialByIdFilialOrigem.sgFilial");
       	    resetValue(this.document);
       	    itemMdaGridDef.resetGrid();
           	setElementValue("filialByIdFilialOrigem.idFilial", idFilial);
            setElementValue("filialByIdFilialOrigem.sgFilial", sgFilial);
			setFocus(document.getElementById("mda.nrDoctoServico"));
		}			
	}
	
	
	/**
	 * Função que salva o registro em questão.
	 */
	function cancelarRegistro(form) {
		if(validateForm(form)) {
			if(window.confirm(lms_17005)) {
				storeButtonScript("lms.pendencia.cancelarMDAAction.store", "cancelarRegistro", form);
			}
		}
	}
		
	/**
	 * Função de CallBack de salvar.
	 */
	function cancelarRegistro_cb(data, erros, errorMsg, eventObj) {
		store_cb(data, erros, errorMsg, eventObj);
		if(!erros) {
			setElementValue("dhCancelamento", setFormat(document.getElementById("dhCancelamento"), data.dhCancelamento));
			setDisabled("cancelButton", true);
		} else {
			alert(erros);
		}
	}	

	/**
	 * Função que pesquisa os dados da grid.
	 */
	function carregaGrid() {	
		var fb = buildFormBeanFromForm(this.document.forms[0], 'LIKE_END'); 
		itemMda_cb(fb);	
	}	
	
	/**
	 * Limpa dados da tela.
	 */
	function cleanButtonFunction() {
		//setDisabled(document.getElementById("mda.nrDoctoServico"), true, undefined, true);	
		resetValue(this.document);
		itemMdaGridDef.resetGrid();
		setDisabled("cancelButton", true);
		setFocusOnFirstFocusableField();		
	}
	
	/**
	 * Método vazio.
	 * Caso necessite alguma ação no retorno da geração do evento, adicionar aqui.
	 */
	function gerarEventoDoctoServico_cb(data, error){
	}
	
	/**
	 * Desabilita o click na row
	 */
	function gridRowClickDisable() {
		return false;
	}
	

</script>