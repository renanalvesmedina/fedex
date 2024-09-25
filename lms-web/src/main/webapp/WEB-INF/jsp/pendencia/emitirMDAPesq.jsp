<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.pendencia.emitirMDAAction">
	<adsm:form action="/pendencia/emitirMDA">
	
		<adsm:hidden property="origem"/>
	
		<adsm:lookup property="filialByIdFilialOrigem" idProperty="idFilial" 
					 criteriaProperty="sgFilial"
					 popupLabel="pesquisarFilial"
					 required="true"
					 service="lms.pendencia.emitirMDAAction.findLookupBySgFilial" 
					 action="/municipios/manterFiliais"
					 onchange="return sgFilialOnChangeHandler();" 
					 onDataLoadCallBack="disableNrDoctoServico"
					 label="mda" dataType="text" labelWidth="15%" width="85%" 
					 size="5" maxLength="3" picker="false" serializable="false" disabled="true">			
			<adsm:lookup property="mda" idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico"
						 service="lms.pendencia.emitirMDAAction.findMdaByNrDoctoServicoByIdFilialOrigem" 
						 action="/pendencia/consultarMDA"
						 onPopupSetValue="carregaDadosMda"
						 onDataLoadCallBack="carregaDadosMda"
						 onchange="return checkValueMda(this.value)"						 
						 dataType="integer" maxLength="10" size="10" mask="00000000">
				<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.idFilial"
											modelProperty="filialByIdFilialOrigem.idFilial" disable="false"/>
				<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.sgFilial"
											modelProperty="filialByIdFilialOrigem.sgFilial" disable="false"/>
				<adsm:propertyMapping criteriaProperty="origem"	modelProperty="origem"/>
													
			</adsm:lookup>

		</adsm:lookup>	

		<adsm:buttonBar freeLayout="false">
			<adsm:button buttonType="reportViewerButton" id="btnVisualizar" caption="visualizar" onclick="abreRelatorio()" disabled="false" />
			<adsm:resetButton caption="limpar"/>
		</adsm:buttonBar>					
	</adsm:form>
	
</adsm:window>

<script type="text/javascript">

	// Se a tela for chamada pelo botão emitir de Abrir MDA.
	var u = new URL(parent.location.href);
   	var origem = u.parameters["origem"];
   	var objOrigem = document.getElementById("origem");
   	setElementValue(objOrigem, origem);
   	objOrigem.masterLink = "true";

	/**
	 * Função chamada ao iniciar a página.
	 */
	function initWindow(eventObj) {
		document.getElementById("mda.nrDoctoServico").required = "true";
		//setDisabled(document.getElementById("mda.nrDoctoServico"), true, undefined, true);
		if(getElementValue("origem") == "abrirMda" || getElementValue("origem") == "consultarMda") {
			populaTela(eventObj);
		} else {
			setElementValue("origem", "emitirMda");
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
	 function populaTela(eventObj) {
		setDisabled("mda.idDoctoServico", false);
		setElementValue("filialByIdFilialOrigem.idFilial", u.parameters["filialByIdFilialOrigem.idFilial"]);
		setElementValue(document.getElementById("filialByIdFilialOrigem.sgFilial"), u.parameters["filialByIdFilialOrigem.sgFilial"]);
		setElementValue("mda.idDoctoServico", u.parameters["idDoctoServico"]);
		setElementValue(document.getElementById("mda.nrDoctoServico"), u.parameters["nrDoctoServico"]);
   		lookupChange({e:document.getElementById("mda.idDoctoServico"), forceChange:true});	 
   		setDisabled("mda.idDoctoServico", true);
   		setDisabled("filialByIdFilialOrigem.idFilial", true);
   		if (eventObj.name != "cleanButton_click"){
   			abreRelatorio();
   		}
   		
	 }	

	/**
	 * Controla o objeto de DoctoServico
	 */	
	function sgFilialOnChangeHandler() {	
		if (getElementValue("filialByIdFilialOrigem.sgFilial") == "") {
			setDisabled(document.getElementById("mda.nrDoctoServico"), true, undefined, true);
			resetValue("mda.idDoctoServico");
		} else {
			setDisabled(document.getElementById("mda.nrDoctoServico"), false, undefined, true);
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
	 * Função chamada no retorno dos dados de callback na lookup de MDA.
	 */	
	function carregaDadosMda(data) {
		setDisabled(document.getElementById("mda.nrDoctoServico"), false, undefined, true);	
		setElementValue("filialByIdFilialOrigem.idFilial", data.filialByIdFilialOrigem.idFilial);
		setElementValue("filialByIdFilialOrigem.sgFilial", data.filialByIdFilialOrigem.sgFilial);
	}

	/**
	 * Função chamada no retorno dos dados de callback na lookup de MDA.
	 */	
	function carregaDadosMda_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}
		mda_nrDoctoServico_exactMatch_cb(data);		
		if(data[0] == undefined) {
			resetValue("mda.idDoctoServico");
			setFocus(document.getElementById("mda.idDoctoServico"));
		}
	}

	/**
	 * Verifica o atual valor do campo de nrDoctoServico
	 */
	function checkValueMda(valor) {		
		mda_nrDoctoServicoOnChangeHandler();
		
		if (valor == "") {			
			var idFilial = getElementValue("filialByIdFilialOrigem.idFilial");
   	        var sgFilial = getElementValue("filialByIdFilialOrigem.sgFilial");
       	    resetValue(this.document);
           	setElementValue("filialByIdFilialOrigem.idFilial", idFilial);
            setElementValue("filialByIdFilialOrigem.sgFilial", sgFilial);
		}		
		return true;
	}

	/**
	 * Chama método que gera relatório.
	 */
	function abreRelatorio() {
		//LMS-3571 - alterada a ordem do processo de emissão, primeiro atualiza os dados para depois abrir o relatório no callback de emissao. 
		//Foi removida a mensagem de confirmação
		var sdo = createServiceDataObject("lms.pendencia.emitirMDAAction.atualizaDados", "emissao", buildFormBeanFromForm(this.document.forms[0]));
		xmit({serviceDataObjects:[sdo]});
	}

	/**
	 * Abre o relatório e pede se deseja emitir (atualizar status).
	 */
	function abreRelatorio_cb(strFile, error) {
		if (error){
			alert(error);
			return false;
		}
		openReportWithLocator(strFile._value, error);
		//mostra a mensagem de sucesso após abrir o relatório
		showSuccessMessage();
	}
	
	/**
	 * CallBack da emissao (processo) do relatório.
	 */
	 function emissao_cb(data, error){
	 	if (error){
	 		alert(error);
	 		return false;
	 	}
	 	executeReportWithCallback('lms.pendencia.emitirMDAAction', 'abreRelatorio', document.forms[0]);	 	
	 } 

	
</script>