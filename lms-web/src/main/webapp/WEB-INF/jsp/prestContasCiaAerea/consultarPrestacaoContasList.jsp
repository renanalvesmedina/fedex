<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.prestcontasciaaerea.consultarPrestacaoContasAction">
	<adsm:form action="/prestContasCiaAerea/consultarPrestacaoContas">

		<adsm:i18nLabels>
			<adsm:include key="LMS-37001" />
		</adsm:i18nLabels>
		
		<adsm:hidden property="idGol"/>

		<adsm:combobox label="companhiaAerea" 
			property="empresa.idEmpresa" 
			service="lms.prestcontasciaaerea.consultarPrestacaoContasAction.findComboCompanhiaAerea" 
			optionLabelProperty="pessoa.nmPessoa" 
			optionProperty="idEmpresa" 
			required="true"
			labelWidth="20%" width="80%" onchange="ciaAereaFilialOnChange()" />

	   <adsm:lookup dataType="text"
					 property="filial" 
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial"
					 service="lms.prestcontasciaaerea.consultarPrestacaoContasAction.findLookupBySgFilial" 
					 action="/municipios/manterFiliais" 
					 label="filialPrestadoraContas" 
					 size="3" 
					 maxLength="3" 
					 required="true"
					 labelWidth="20%" width="39%"
					 exactMatch="true"
					 onDataLoadCallBack="ciaAereaFilialCallBack"
					 onPopupSetValue="ciaAereaFilialOnPopupSetValue" onchange="ciaAereaFilialOnChange();" >
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="37" maxLength="30" disabled="true"/>
		</adsm:lookup>
		<adsm:textbox label="moeda" property="dsSiglaSimbolo"  labelWidth="15%" dataType="text" width="26%" maxLength="15" disabled="true"/>

		<adsm:textbox dataType="JTDate" label="periodoVendas" labelWidth="20%" property="dtInicial" size="10" width="13%" onchange="dtInicialOnChange()" disabled="true"/> 
		<adsm:label key="ate" width="3%" style="border:none;"/>
	   	<adsm:textbox dataType="JTDate" property="dtFinal" size="10" width="23%" disabled="true"/>

		<adsm:textbox label="numeroCT" property="nrPrestacaoConta" labelWidth="15%" dataType="integer" width="20%" maxLength="15"
			onchange="nrPrestacaoContaOnChange()"/>

	<adsm:buttonBar freeLayout="true">
		<adsm:button caption="consultar" buttonType="findButton" onclick="consultar(this);" disabled="false"/>
		<adsm:button caption="limpar" onclick="limpar()" disabled="false" id="btnLimpar"/>

	</adsm:buttonBar>
	
		<script>
			var lms37001 = i18NLabel.getLabel("LMS-37001");
		</script>
	
	</adsm:form>
	
	<adsm:tabGroup selectedTab="0">
		<adsm:tab boxWidth="100" title="intervalosAWB" id="intervalosAWB"
			src="/prestContasCiaAerea/consultarPrestacaoContas" cmd="intervalo" height="312" disabled="true"/>
		<adsm:tab boxWidth="115" title="demonstrativoICMS" id="demonstrativoICMS"  onShow="myOnShow"
			src="/prestContasCiaAerea/consultarPrestacaoContas" cmd="demICMS" height="312" disabled="true"/>
		<adsm:tab boxWidth="115" title="demonstrativoVendas" id="demonstrativoVendas"  onShow="myOnShow"
			src="/prestContasCiaAerea/consultarPrestacaoContas" cmd="demVendas" height="312" disabled="true"/>
		<adsm:tab boxWidth="115" title="demonstrativoPrestacaoContas" id="demonstrativoPrestacaoContas" 
			src="/prestContasCiaAerea/consultarPrestacaoContas" cmd="demPrestContas" height="312" onShow="myOnShow" disabled="true"/>
		<adsm:tab boxWidth="115" title="awbsCanceladas" id="awbsCanceladas"  onShow="myOnShow"
			src="/prestContasCiaAerea/consultarPrestacaoContas" cmd="canceladas" height="312" disabled="true"/>
	</adsm:tabGroup>
</adsm:window>

<script>

	function initWindow(eventObj){
		buscaDadosPadroes();
		loadFilialByUserLogado();
		setDisabled('btnLimpar', false);
	}
	
	/** Limpa os campos da tela e desabilita o calendario */ 
	function limpar(){
		cleanButtonScript();
		
		var documento = getCurrentDocument("dtInicial", null);
        var obj = getElement("dtInicial", documento);
         setDisabledCalendar(obj, true, documento);
         getTabGroup(this.document).selectedTab.childTabGroup.selectTab("intervalosAWB");
         desabilitarTabs();
	}
	
	function buscaDadosPadroes(){
	
		var dados = new Array();
        _serviceDataObjects = new Array();
            
        addServiceDataObject(createServiceDataObject("lms.prestcontasciaaerea.consultarPrestacaoContasAction.findDadosPadrao",
                                                     "retornoBuscaDadosPadrao",
                                                     dados));                                                     
        xmit(false);
	}
	
	/**
	*	Seta a moeda padrão do Brasil e o id da gol
	*/
	function retornoBuscaDadosPadrao_cb(data,erro){
		
		if( erro != undefined ){
			alert(erro);
			return false;		
		}
		setElementValue('dsSiglaSimbolo',data.moeda);
		setElementValue("idGol", data.idGol);
	}
		
	function loadFilialByUserLogado(){
		var dados = new Array();
		var sdo = createServiceDataObject("lms.prestcontasciaaerea.consultarPrestacaoContasAction.findFilialByUserLogado","retornoLoadFilialByUserLogado",dados);
        xmit({serviceDataObjects:[sdo]});
		
	}
	
	function retornoLoadFilialByUserLogado_cb(data,erro){
		if( erro != undefined ){
			alert(erro);
			return false;					
		}
		setElementValue("filial.idFilial", data.idFilial);
		setElementValue("filial.sgFilial", data.sgFilial);
		setElementValue("filial.pessoa.nmFantasia", data.pessoa.nmFantasia);					
	}
	
	function validateConsultar(eThis){
		//Deve informar o nrPrestacaoConta ou o periodo de vendas.
		if ( getElementValue("nrPrestacaoConta") == "" && (getElementValue("dtFinal") != "" && getElementValue("dtInicial") != "" ) ){
			return true;
		}
		
		if ( getElementValue("nrPrestacaoConta") != "" && (getElementValue("dtFinal") == "" && getElementValue("dtInicial") == "") ){
			return true;
		}

		alert(''+lms37001);
		
		return false;
	}

	function consultar(eThis){
	
		desabilitarTabs();
	
		if ( validateTabScript(eThis.form) == false ){
			return false;
		}
	
		if (!validateConsultar(eThis)) return;
		
		var filtro = {
			empresa				: { idEmpresa : getElementValue("empresa.idEmpresa") },
			filial				: { idFilial : getElementValue("filial.idFilial") },
			nrPrestacaoConta	: getElementValue("nrPrestacaoConta"),
			dtFinal				: getElementValue("dtFinal"),
			dtInicial			: getElementValue("dtInicial")
		};
		
		var sdo = createServiceDataObject("lms.prestcontasciaaerea.consultarPrestacaoContasAction.findPrestacaoConta","consulta",filtro);
        xmit({serviceDataObjects:[sdo]});
	
	}
	
	function desabilitarTabs(){
	
		var tab = getTabGroup(this.document).selectedTab.childTabGroup;
		
		tab.selectTab(0);

		var telaIntervalosAWB = tab.getTab("intervalosAWB").tabOwnerFrame;
		telaIntervalosAWB.intervalosAWBGridDef.resetGrid();
		tab.getTab("intervalosAWB").setDisabled(true);
		
		tab.getTab("demonstrativoICMS").setDisabled(true);
		tab.getTab("demonstrativoVendas").setDisabled(true);
		tab.getTab("demonstrativoPrestacaoContas").setDisabled(true);
		tab.getTab("awbsCanceladas").setDisabled(true);

	}
	
	function consulta_cb(data, error){

		if (error != undefined){
			alert(''+error);
			return;
		}

		var tab = getTabGroup(this.document).selectedTab.childTabGroup;

		var telaIntervalosAWB = tab.getTab("intervalosAWB").tabOwnerFrame;
		var telaDemonstrativoICMS = tab.getTab("demonstrativoICMS").tabOwnerFrame;
		var telaDemonstrativoVendas = tab.getTab("demonstrativoVendas").tabOwnerFrame;
		var telaDemonstrativoPrestacaoContas = tab.getTab("demonstrativoPrestacaoContas").tabOwnerFrame;
		var telaAwbsCanceladasICMS = tab.getTab("awbsCanceladas").tabOwnerFrame;

		if (data == undefined){
			return false;
		}
		
		var idPrestacaoConta = data.idPrestacaoConta;
		
		if (idPrestacaoConta == undefined){
			/*
			 * Caso nao encontre a prestação de contas, deve mostrar mensagem padrão na listagem.
			 * Para isto, é realizada uma busca -- que não retorna registros --, para popular a grid (vazia) e mostrar a mensagem.
			 */
			var filtro = {
				prestacaoConta : { idPrestacaoConta	: -1 }
			};
			tab.getTab("intervalosAWB").setDisabled("false");
			telaIntervalosAWB.intervalosAWBGridDef.executeSearch(filtro);

			return;
		}
		

		var filtro = {
			prestacaoConta : { idPrestacaoConta	: idPrestacaoConta }
		};

		tab.getTab("intervalosAWB").setDisabled("false");
		tab.getTab("demonstrativoICMS").setDisabled("false");
		tab.getTab("demonstrativoVendas").setDisabled("false");
		tab.getTab("demonstrativoPrestacaoContas").setDisabled("false");
		tab.getTab("awbsCanceladas").setDisabled("false");

		telaDemonstrativoICMS.setElementValue("prestacaoConta.idPrestacaoConta", idPrestacaoConta);
		telaDemonstrativoVendas.setElementValue("prestacaoConta.idPrestacaoConta", idPrestacaoConta);

		telaDemonstrativoPrestacaoContas.setElementValue("prestacaoConta.idPrestacaoConta", idPrestacaoConta);
		telaDemonstrativoPrestacaoContas.setElementValue("idEmpresa", getElementValue("empresa.idEmpresa"));
		telaDemonstrativoPrestacaoContas.setElementValue("idFilial", getElementValue("filial.idFilial"));

		telaAwbsCanceladasICMS.setElementValue("prestacaoConta.idPrestacaoConta", idPrestacaoConta);

		telaIntervalosAWB.intervalosAWBGridDef.executeSearch(filtro);
		tab.selectTab("intervalosAWB");

		// seta indicador da cia gol
		telaDemonstrativoVendas.setElementValue("isGol", (getElementValue("idGol")==getElementValue("empresa.idEmpresa")));
	}

//chamado ao clicar na pula	
	function ciaAereaFilialOnPopupSetValue(data)
	{	
		setElementValue("filial.idFilial", data.idFilial);
		setElementValue("filial.sgFilial", data.sgFilial);
		return ciaAereaFilialOnChange();
		}
// chamado no evento onchange
	function ciaAereaFilialOnChange()
	{
		if(getElementValue("filial.sgFilial") == "")
		{
			resetValue("filial.idFilial");
		}

		if(getElementValue("empresa.idEmpresa") != '' && getElementValue("filial.idFilial") != '')
		{
			setDisabled("dtInicial", false);		
		}
		else
		{
			setDisabled("dtInicial", true);
		}		
		return filial_sgFilialOnChangeHandler();
	}
	
// chamado quando se digita a sigla na lokup
	function ciaAereaFilialCallBack_cb(data, error)
	{
		if (error != undefined){
			alert(error);
			return false;
		} 
		if(data[0] != undefined){
			setElementValue("filial.idFilial", data[0].idFilial);
			setElementValue("filial.sgFilial", data[0].sgFilial);
			setElementValue("filial.pessoa.nmFantasia", data[0].pessoa.nmFantasia);
			if(getElementValue("empresa.idEmpresa") != '' && getElementValue("filial.idFilial") != '')
			{
				setDisabled("dtInicial", false);		
			}
			else
			{
				setDisabled("dtInicial", true);
				resetValue("dtInicial");
				resetValue("dtFinal");
			}
		}		
		var retorno = filial_sgFilial_exactMatch_cb(data);
		if (retorno == false){
			setFocus("filial.sgFilial");
		}
		return retorno;
	}
	
	function dtInicialOnChange(eThis){
		/*
		*	Busca a data final conforme a ciaAerea e a filial informada
		*/
		var dados = new Array();
		setNestedBeanPropertyValue(dados, "id_cia_filial_mercurio",getElementValue("empresa.idEmpresa"));
		setNestedBeanPropertyValue(dados, "id_filial",getElementValue("filial.idFilial"));
		setNestedBeanPropertyValue(dados, "dtInicial",getElementValue("dtInicial"));
        var sdo = createServiceDataObject("lms.prestcontasciaaerea.consultarPrestacaoContasAction.findDtFinalByFilialAndCiaAerea", "retornoBuscaDtFinal", dados);
				xmit({serviceDataObjects:[sdo]});
		}
	
	function retornoBuscaDtFinal_cb(data, error)
	{	
		if (error){
			alert(error);
			return false;
	}
		setElementValue('dtFinal',data.dtFinal);
	}
	
	function dtInicial_cb(data, error){
		if (error != undefined){
			alert(''+error);
			return;
		}
		if (data == undefined) return;
		if (data._value == undefined) return;
		
		setElementValue("dtFinal", data._value);
	}

	/**
	 * Ao alterar a prestação de contas, zera o periodo.
	 */
	function nrPrestacaoContaOnChange(){
		resetPeriodoVendas();
	}
	
	function resetPeriodoVendas(){
		
		var documento = getCurrentDocument("dtInicial", null);
        var obj = getElement("dtInicial", documento);
        setDisabledCalendar(obj, true, documento);

		resetValue("dtInicial");
		resetValue("dtFinal");
		setDisabled("dtInicial", true);
	}

</script>