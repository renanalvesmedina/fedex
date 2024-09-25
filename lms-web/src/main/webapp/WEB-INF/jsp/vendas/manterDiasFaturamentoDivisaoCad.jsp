<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.diaFaturamentoService" >
	<adsm:form action="/vendas/manterDiasFaturamentoDivisao" 
		onDataLoadCallBack="myOnDataLoad" idProperty="idDiaFaturamento">

		<adsm:hidden property="idFilial"/>
		<adsm:hidden property="divisaoCliente.idDivisaoCliente"/>
		<adsm:hidden property="divisaoCliente.dsDivisaoCliente"/>
		<adsm:hidden property="divisaoCliente.cliente.pessoa.nrIdentificacao"/>
		<adsm:hidden property="divisaoCliente.cliente.pessoa.nmPessoa"/>
		<adsm:hidden property="hasAccess" serializable="false"/>

		<adsm:complement required="true" labelWidth="20%" label="cliente" width="80%" separator="branco">
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				size="20"
				property="divisaoCliente.cliente.pessoa.nrIdentificacao"
				serializable="false"/>
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				size="30"
				property="divisaoCliente.cliente.pessoa.nmPessoa"
				serializable="false" />	
		</adsm:complement>

		<adsm:textbox 
			required="true" 
			property="divisaoCliente.dsDivisaoCliente" 
			size="51"
			label="divisao" 
			labelWidth="20%" 
			dataType="text" 
			width="45%" 
			serializable="false" 
			disabled="true"/>

		<adsm:combobox 
			boxWidth="100" 
			onchange="buscaServicos();" 
			property="tpModal" 
			label="modal" 
			domain="DM_MODAL"
			required="true" 
			labelWidth="15%" 
			width="20%"
			disabled="true"
			/>

		<adsm:combobox 
			boxWidth="100" 
			onchange="buscaServicos();" 
			property="tpAbrangencia" 
			label="abrangencia"
			domain="DM_ABRANGENCIA" 
			labelWidth="20%" 
			width="45%"
			disabled="true"
			/>

		<adsm:combobox 
			boxWidth="130" 
			property="servico.idServico" 
			label="servico" 
			optionLabelProperty="dsServico" 
			optionProperty="idServico"
			onlyActiveValues="true" 
			onchange="setModalAbrangencia(this)"
			service="lms.configuracoes.servicoService.find"  
			labelWidth="15%" 
			width="20%">
			<adsm:propertyMapping 
				relatedProperty="tpModal" 
				modelProperty="tpModal"/>
			<adsm:propertyMapping 
				relatedProperty="tpAbrangencia" 
				modelProperty="tpAbrangencia"/>
		</adsm:combobox>

		<adsm:combobox 
			boxWidth="80" 
			property="tpFrete" 
			label="tipoFrete" 
			labelWidth="20%" 
			width="80%" 
			domain="DM_TIPO_FRETE" />

		<adsm:combobox 
			boxWidth="80" 
			property="tpPeriodicidade" 
			domain="DM_PERIODICIDADE_FATURAMENTO" 
			label="periodicidade" 
			required="true"
			disabled="true"
			labelWidth="20%" 
			width="30%"/>

		<adsm:textbox 
			dataType="integer" 
			property="nrDiaFaturamento" 
			label="diaReferenciaFaturamento" 
			size="10" 
			maxLength="2" 
			labelWidth="30%" 
			width="20%"
			style="position: absolute;"
			disabled="true">
			<adsm:combobox 
				boxWidth="80" 
				property="tpDiaSemana" 
				domain="DM_DIAS_SEMANA"  
				style="visibility : hidden"
				disabled="true"/> 
        </adsm:textbox>

        <adsm:combobox 
			boxWidth="80" 
			property="tpPeriodicidadeSolicitado" 
			domain="DM_PERIODICIDADE_FATURAMENTO" 
			label="periodicidadeSolicitada" 
			onchange="javascript:configuraObjetoDiaCorte(true);" 
			required="true"
			labelWidth="20%" 
			width="30%"/>

		<adsm:textbox 
			dataType="integer" 
			property="nrDiaFaturamentoSolicitado" 
			label="diaReferenciaFaturamentoSol" 
			size="10" 
			maxLength="2" 
			labelWidth="30%" 
			width="20%"
			onchange="return validateDiaCorte(this);"
			style="position: absolute;">
			<adsm:combobox 
				boxWidth="80" 
				property="tpDiaSemanaSolicitado" 
				domain="DM_DIAS_SEMANA"  
				style="visibility : hidden"
				onchange="return validateTpDiaSemana();"/> 
        </adsm:textbox>

		<adsm:hidden property="tpPeriodicidadeAprovado" serializable="true"/>
        <adsm:hidden property="servicoValueAux"/>
        <adsm:hidden property="servicoValue"/>
        <adsm:hidden property="cameFromOutside" value="Y"/>
        <adsm:hidden property="servicoLabelAux"/>
        <adsm:hidden property="tpModalValueAux"/>
        <adsm:hidden property="tpAbrangenciaValueAux"/>

		<adsm:buttonBar>
			<adsm:button id="diasVencimento" caption="diasVencimento" action="/vendas/manterPrazoVencimentoDivisao" cmd="main" boxWidth="150">
				<adsm:linkProperty src="divisaoCliente.idDivisaoCliente" target="divisaoCliente.idDivisaoCliente"/>
				<adsm:linkProperty src="divisaoCliente.dsDivisaoCliente" target="divisaoCliente.dsDivisaoCliente"/>
				<adsm:linkProperty src="divisaoCliente.cliente.pessoa.nrIdentificacao" target="divisaoCliente.cliente.pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="divisaoCliente.cliente.pessoa.nmPessoa" target="divisaoCliente.cliente.pessoa.nmPessoa"/>
				<adsm:linkProperty src="servico.idServico" target="servico.idServico"/>
				<adsm:linkProperty src="tpModal" target="tpModal"/>
				<adsm:linkProperty src="tpAbrangencia" target="tpAbrangencia"/>
				<adsm:linkProperty src="idFilial" target="idFilial"/>
		</adsm:button>
			
			<adsm:storeButton id="storeButton"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>

<script type="text/javascript">
<!--
	var updatePeriodicidade = false;
	var updateFaturamento = false;

	function buscaServicos(){
		var modal = getElementValue("tpModal");
		var abrangencia = getElementValue("tpAbrangencia");
	    resetServicos({tpModal:modal,tpAbrangencia:abrangencia,tpSituacao:'A'});
	}

	function verificarIdDivisaoCliente(){
		if(hasValue(getElementValue("divisaoCliente.idDivisaoCliente"))){
			var sdo = createServiceDataObject("lms.vendas.manterDiasFaturamentoDivisaoAction.findByIdDivisaoCliente", "findByIdDivisaoCliente", {idDivisaoCliente:getElementValue("divisaoCliente.idDivisaoCliente")});
			xmit({serviceDataObjects:[sdo]});
		}
	}
	
	function findByIdDivisaoCliente_cb(data,erros) {
		if(erros!=undefined){
			alert(erros);
			return;
		}
		data = (data[0] != undefined) ? data[0] : data;
		var idDiaFaturamento = getNestedBeanPropertyValue(data, "idDiaFaturamento");
		if(idDiaFaturamento != undefined ){
			setDisabled("diasVencimento", false);	
		}else{
			setDisabled("diasVencimento", true);
		}
	}
	
	function resetServicos(criteria) {
		var sdo = createServiceDataObject("lms.configuracoes.servicoService.find", "servico", criteria);
      	xmit({serviceDataObjects:[sdo]});
	}

	function cleanCadTrash() {
		setElementValue("servicoValueAux","");
		setElementValue("servicoValue","");
		setElementValue("servicoLabelAux","");
		setElementValue("tpModalValueAux","");
		setElementValue("tpAbrangenciaValueAux","");
	}

	function initWindow(eventObj) {
		verificarIdDivisaoCliente();
		var event = eventObj.name;
		if(event == "newButton_click" 
			|| event == "tab_click"){
			resetServicos({tpSituacao:'A'});
			configuraObjetoDiaCorte();
			cleanCadTrash();
		}
		if(event == "newButton_click") {
			updateFaturamento = false;
			updatePeriodicidade = false;
		}
		if(event == "storeButton"){
			setElementValue("cameFromOutside", "Y");
			if(hasValue(getElementValue("tpPeriodicidade"))
				&& (hasValue(getElementValue("nrDiaFaturamento"))
					|| hasValue(getElementValue("tpDiaSemana")))) {
				updateFaturamento = false;
				updatePeriodicidade = false;
			}
			setDisabled("diasVencimento", false);	
		} 
		if (event == "tab_click" || event == "gridRow_click"){
			initWindowVal();
			setElementValue("idFilial",getElementValue(getTabGroup(document).getTab("pesq").getElementById("idFilial")));
			verifyFilialAnaliseCredito();
		}
    }

    function servico_cb(data,erros) {
    	servico_idServico_cb(data, erros);
		if(erros!=undefined)	{
			alert(erros);
			return;
		}
		var servicoValueAux = getElementValue("servicoValueAux");
		var servicoValue = getElementValue("servicoValue");
    	var modalValueAux = getElementValue("tpModalValueAux");
		var abrangenciaValueAux = getElementValue("tpAbrangenciaValueAux");
	    var modal = getElementValue("tpModal");
    	var abrangencia = getElementValue("tpAbrangencia");
	    if((modal==modalValueAux || modal=='') 
	    	&& (abrangencia==abrangenciaValueAux || abrangencia=='')) {
        	var contains = false;
	   	 	var e = getElement("servico.idServico");
	    	for (var i = 0; i < e.options.length; i++) {
	    		var o = e.options[i];
				if (o.value == servicoValueAux)	{
		    		contains = true;
		    		break;
				}
	    	}
	    	if(contains == false) {
				var o = new Option(getElementValue("servicoLabelAux"), getElementValue("servicoValueAux"));
				o._inactive = true;
				e.options.add(o);
	    	}
    	}
    	if(getElementValue("cameFromOutside") == "Y") {
	    	setElementValue("servico.idServico",servicoValue);
	    	setElementValue("cameFromOutside","N");
		}
	}

    function prepareAuxValues(data)	{
		var servico = getNestedBeanPropertyValue(data, "servico.idServico");
		setElementValue("servicoValue", servico);
		if(getElementValue("servicoValueAux") == "") {
			setElementValue("servicoValueAux", servico);
		}
		if(servico)	{
			var servicoLabel = getNestedBeanPropertyValue(data, "servico.dsServico");
			if(getElementValue("servicoLabelAux") == "") {
				setElementValue("servicoLabelAux",servicoLabel);
			}
		}
		var modal = getElementValue("tpModal");
		if(getElementValue("tpModalValueAux") == "") {
			setElementValue("tpModalValueAux", modal);
		}
		var abrangencia = getElementValue("tpAbrangencia");
		if(getElementValue("tpAbrangenciaValueAux") == "") {
			setElementValue("tpAbrangenciaValueAux", abrangencia);
		}
	}

	function setModalAbrangencia(e) {
		comboboxChange({e:e});
		if(getElementValue("servico.idServico") == getElementValue("servicoValueAux"))	{
			setElementValue("tpModal", getElementValue("tpModalValueAux"));
			setElementValue("tpAbrangencia", getElementValue("tpAbrangenciaValueAux"));
		}
	}

  	function myOnDataLoad_cb(data, error){
    	onDataLoad_cb(data, error);
      	buscaServicos();
      	prepareAuxValues(data);
		initWindowVal();
		updatePeriodicidade = false;
		updateFaturamento = false;
	}

	function verifyFilialAnaliseCredito() {
	    var sdo = createServiceDataObject("lms.vendas.manterClienteAction.verifyFilialAnaliseCredito", "verifyFilialAnaliseCredito", {idFilialComercial:getElementValue("idFilial")});
      	xmit({serviceDataObjects:[sdo]});
	}
	function verifyFilialAnaliseCredito_cb(data, error) {
		if(error != undefined) {
			alert(error);
			return;
		}
		setElementValue("hasAccess", data.hasAccess);
		configuraObjetoDiaCorte(false);
	}

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function initWindowVal(){
		if (getElementValue(getTabGroup(document).getTab("pesq").getElementById("permissao"))!="true") {
			setDisabled("storeButton", true);
			setDisabled("newButton", true);
			setDisabled("removeButton", true);
		}
	}

	function getHasAccess() {
		return (getElementValue("hasAccess") == "true" || getElementValue("hasAccess") == true); 
	}

  	/**
  	* Função para alterar os tipos de objetos conforme a opção selecionada no campo
  	* periodicidade
  	*/
  	function configuraObjetoDiaCorte(cleanDdCorte){
  		if(updatePeriodicidade || !hasValue(getElementValue("tpPeriodicidade")) || !hasValue(getElementValue("idDiaFaturamento")) || !getHasAccess()) {
			setElementValue("tpPeriodicidade", getElementValue("tpPeriodicidadeSolicitado"));
			updatePeriodicidade = true;
		}

	    var ddCorte = getElement("nrDiaFaturamento");
	    var nmDiaSemana = getElement("tpDiaSemana");

	    var dmPeriodicidade = getElementValue("tpPeriodicidadeSolicitado");
	    var ddCorteSolicitado = getElement("nrDiaFaturamentoSolicitado");
	    var nmDiaSemanaSolicitado = getElement("tpDiaSemanaSolicitado");
	    if(cleanDdCorte == true) {
	   	    resetValue("nrDiaFaturamentoSolicitado");
	   	    resetValue("tpDiaSemanaSolicitado");

	   	    if(!hasValue(getElementValue("idDiaFaturamento")) || !getHasAccess()) {
	   	    	resetValue("nrDiaFaturamento");
	   	    	resetValue("tpDiaSemana");
	   	    }
	   	}
	    switch (dmPeriodicidade){
	    	case "D" :
	     	    ddCorteSolicitado.style.visibility = 'visible';
	      	    ddCorteSolicitado.disabled = true;
	     	    nmDiaSemanaSolicitado.style.visibility = 'hidden';
	     	    nmDiaSemanaSolicitado.selectedIndex  = 0;

	     	    if(updateFaturamento || hasValue(getElementValue("nrDiaFaturamento")) || !hasValue(getElementValue("idDiaFaturamento")) || !getHasAccess()) {
	     	    	ddCorte.style.visibility = 'visible';
		     	    nmDiaSemana.style.visibility = 'hidden';
		     	    nmDiaSemana.selectedIndex  = 0;
	     	    }
	     	    break;
	     	case "S" :
	      	    ddCorteSolicitado.style.visibility = 'hidden';
	      	    ddCorteSolicitado.disabled = false;
	     		nmDiaSemanaSolicitado.style.visibility = 'visible';

	     		if(updateFaturamento || hasValue(getElementValue("tpDiaSemana")) || !hasValue(getElementValue("idDiaFaturamento")) || !getHasAccess()) {
		     		ddCorte.style.visibility = 'hidden';
		     		nmDiaSemana.style.visibility = 'visible';
		     	}
	     	    break;
	     	case "E" :
	     	    ddCorteSolicitado.style.visibility = 'visible';
	     	    ddCorteSolicitado.disabled = false;
	     	    nmDiaSemanaSolicitado.style.visibility = 'hidden';
	     	    nmDiaSemanaSolicitado.selectedIndex  = 0;

	     	    if(updateFaturamento || hasValue(getElementValue("nrDiaFaturamento")) || !hasValue(getElementValue("idDiaFaturamento")) || !getHasAccess()) {
		     	    ddCorte.style.visibility = 'visible';
		     	    nmDiaSemana.style.visibility = 'hidden';
		     	    nmDiaSemana.selectedIndex  = 0;
		     	}
	     	    break;    
	     	case "Q" :	 
	     	    ddCorteSolicitado.style.visibility = 'visible';
	     	    ddCorteSolicitado.disabled = false;
	     	    nmDiaSemanaSolicitado.style.visibility = 'hidden';
	     	    nmDiaSemanaSolicitado.selectedIndex  = 0;

				if(updateFaturamento || hasValue(getElementValue("nrDiaFaturamento")) || !hasValue(getElementValue("idDiaFaturamento")) || !getHasAccess()) {
					ddCorte.style.visibility = 'visible';
		     	    nmDiaSemana.style.visibility = 'hidden';
		     	    nmDiaSemana.selectedIndex  = 0;
		     	}
	     	    break;    
	     	case "M" :
	     	    ddCorteSolicitado.style.visibility = 'visible';
	     	    ddCorteSolicitado.disabled = false;
	     	    nmDiaSemanaSolicitado.style.visibility = 'hidden';
	     	    nmDiaSemanaSolicitado.selectedIndex  = 0;

	     	    if(updateFaturamento || hasValue(getElementValue("nrDiaFaturamento")) || !hasValue(getElementValue("idDiaFaturamento")) || !getHasAccess()) {
		     	    ddCorte.style.visibility = 'visible';
		     	    nmDiaSemana.style.visibility = 'hidden';
		     	    nmDiaSemana.selectedIndex  = 0;
				}
	     	    break; 
		}
  	} 

  	/**
  	* Valida valores minimos e máximos do campo dia corte
  	*/
  	function validateDiaCorte(obj){
  		if(!hasValue(getElementValue("nrDiaFaturamento")) || !hasValue(getElementValue("idDiaFaturamento")) || !getHasAccess()) {
			setElementValue("nrDiaFaturamento", getElementValue("nrDiaFaturamentoSolicitado"));
			updateFaturamento = true;
	 	}

     	var dmPeriodicidade = getElementValue("tpPeriodicidadeSolicitado"); 
     	var num = stringToNumber(getElementValue(obj));
     	var retorno = true;
     	switch (dmPeriodicidade){
     	 	case "E" :	 
			 	if(num < 1 || num > 10) { 	     	 	
             		alert('<adsm:label key="LMS-27011"/>');
             		retorno = false;
             	}
     	     	break;    
     	 	case "Q" :	 
     	 	 	if(num < 1 || num > 15) { 	     	 
     	     		alert('<adsm:label key="LMS-27012"/>'); 
     	     		retorno = false;
     	     	}
     	     	break;    
     	 	case "M" :	 
     	 	 	if(num < 1 || num > 31) { 	     	 
             		alert('<adsm:label key="LMS-27013"/>'); 
             		retorno = false;
             	}
     	     	break;    
     	}
     	if(retorno == false) {
       		resetValue("nrDiaFaturamentoSolicitado");
       		if(updateFaturamento) {
       			resetValue("nrDiaFaturamento");
       		}
       	}
  	 	return retorno; 
  	}

	function validateTpDiaSemana(){
  		if(updateFaturamento 
  			|| ("S" == getElementValue("tpPeriodicidade") && !hasValue(getElementValue("tpDiaSemana")))
  			|| !hasValue(getElementValue("idDiaFaturamento")) || !getHasAccess()) {
			setElementValue("tpDiaSemana", getElementValue("tpDiaSemanaSolicitado"));
			updateFaturamento = true;
	 	}
	 	return true;
	}

  /**
  * Função identica a que esta no validate.js
  * exceto pelo alert
  */
  function myValidateMinMaxValue(obj){
	var retorno = true;
	if ((obj.minValue != null && obj.minValue != '')){
		if (!compareData(obj.minValue,getElementValue(obj),obj.dataType,"",obj.mask)){
			retorno = false;
		}
	}
	if ((obj.maxValue != null && obj.maxValue != '')){
		if (!compareData(getElementValue(obj),obj.maxValue,obj.dataType,obj.mask,"")){
			retorno = false;
		}	
	}	
	return retorno;
  }
	
//-->
</script>
	</adsm:form>
</adsm:window>
