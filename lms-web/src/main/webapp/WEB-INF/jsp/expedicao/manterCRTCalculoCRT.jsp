<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	onPageLoad="myOnPageLoadCallBack"
	service="lms.expedicao.manterCRTCalculoCRTAction">

	<adsm:form
		action="/expedicao/manterCRTCalculoCRT">

		<adsm:section
			caption="calculoCRT"
			width="70"/>

		<adsm:combobox
			required="true"
			domain="DM_DEVEDOR_CRT"
			property="responsavelFrete"
			onDataLoadCallBack="responsavelFreteDataLoad"
			onchange="changeResponsavel(this)"
			label="responsavelFrete"
			width="20%"
			labelWidth="15%">
		</adsm:combobox>

		<adsm:combobox
			service="lms.expedicao.manterCRTCalculoCRTAction.findDivisaoCliente"
			property="divisaoCliente.idDivisaoCliente"
			optionLabelProperty="dsDivisaoCliente"
			optionProperty="idDivisaoCliente"
			required="true"
			label="divisao"
			labelWidth="15%"
			disabled="true"
			onDataLoadCallBack="divisaoCliente"
			width="20%">
			<adsm:propertyMapping
				criteriaProperty="responsavelFrete"
				modelProperty="responsavelFrete"/>
		</adsm:combobox>

		<adsm:label
			key="branco"
			style="border:none;"
			width="1%"/>

		<adsm:textbox
			property="tabelaPreco.dsDescricao"
			dataType="text"
			disabled="true"
			label="tabela"
			size="20"
			width="20%"
			labelWidth="15%"/>

		<adsm:textbox
			property="moeda.sgMoeda"
			dataType="text"
			disabled="true"
			label="moeda"
			size="5"
			width="20%"
			labelWidth="15%"/>

		<adsm:hidden
			property="moeda.dsSimbolo"/>

		<adsm:textbox
			label="percentualAforo"
			property="percentualAforo"
			dataType="decimal"
			onchange="setValorCubagem(this)"
			size="5"
			labelWidth="15%"
			width="85%"/>
	</adsm:form>

	<adsm:grid
		title="calculoFrete"
		property="parcela"
		idProperty="idParcela"
		onRowClick="nothing"
		showRowIndex="false"
		showGotoBox="false"
		showPagging="false" 
		showTotalPageCount="false"
		selectionMode="none"
		autoAddNew="false"
		scrollBars="vertical"
		gridHeight="50">

		<adsm:gridColumn
			dataType="text"
			property="nmParcela"
			title="parcela"
			width="240"/>

		<adsm:gridColumn
			dataType="currency"
			property="vlParcela"
			title="valor"
			unit="reais"
			align="right"/>

	</adsm:grid>

	<adsm:form
		action="/expedicao/digitarDadosNotaNormalCalculoCTRC"
		id="form2" >
		<adsm:label
			key="branco"
			width="30%"/>
		<adsm:textbox
			label="desconto"
			property="vlDesconto"
			dataType="decimal"
			size="17"
			labelWidth="16%"
			width="34%"
			disabled="true"/>
		<adsm:label
			key="branco"
			width="30%"/>
		<adsm:textbox
			label="totalFreteReais"
			property="vlTotalFrete"
			dataType="decimal"
			size="17"
			labelWidth="16%"
			width="34%"
			disabled="true"/>
	</adsm:form>

	<adsm:grid
		title="calculoServicos"
		property="servico"
		idProperty="idServico" 
		onRowClick="nothing"
		showRowIndex="false"
		showGotoBox="false"
		showPagging="false"
		showTotalPageCount="false"
		selectionMode="none"
		autoAddNew="false"
		gridHeight="40"
		scrollBars="vertical">

		<adsm:gridColumn
			dataType="text"
			property="nmServico"
			title="servicoAdicional"
			width="240"/>

		<adsm:gridColumn
			dataType="currency"
			property="vlServico"
			title="valor"
			unit="reais"
			align="right"/>

	</adsm:grid>

	<adsm:form
		action="/expedicao/digitarDadosNotaNormalCalculoCTRC"
		id="form3" >
		<adsm:label
			key="branco"
			width="23%"/>

		<adsm:textbox
			label="totalServicos"
			property="totalServicos"
			dataType="decimal"
			size="28"
			labelWidth="20%"
			disabled="true"
			width="50%"/>

		<adsm:label
			key="branco"
			width="23%"/>

		<adsm:textbox
			label="totalCRT"
			property="totalCRT"
			dataType="decimal"
			size="28"
			labelWidth="20%"
			disabled="true"
			width="50%"/>

  		<adsm:buttonBar
  			lines="2"
  			freeLayout="true">

  			<adsm:button
  				disabled="false"
  				id="btnCalcularFrete"
  				onclick="calcularFrete()"
				caption="calcularFrete"
				boxWidth="90"/>

			<adsm:button
				caption="cancelarTudo"
				id="btnCancelarTudo"
				disabled="false"
				onclick="cancelarTudo()"
				boxWidth="90"/>

			<adsm:button
				id="btnVoltarDetalhamento"
				disabled="false"
				caption="voltarDetalhamento"
				onclick="voltarDetalhamento()"
				boxWidth="150"/>

			<adsm:button
				id="btnGravarValores"
				disabled="true"
				onclick="storeInSession()"
				caption="gravarValores"/>

			<adsm:button
				id="btnFechar"
				disabled="true"
				onclick="fechar()"
				caption="fechar"/>

		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	var StateWindowListener = {/* Variavel que define o retorno da popup.*/
		VOLTAR_DETALHAMENTO : 'VOLTAR_DETALHAMENTO'
		,CANCELAR_TUDO : 'CANCELAR_TUDO'
		,CALCULO_SALVO : 'CALCULO_SALVO'
		,VISUALIZACAO : 'VISUALIZACAO'

		//Recebe o estado da window
		,setState : function(state){
			this.state = state;
			switch(state){
				case this.VISUALIZACAO:
					populateWindow();
					setDisabled(document, true);
					setDisabled('btnFechar', false);
					setFocus('btnFechar', false);
					break;
				case this.VOLTAR_DETALHAMENTO:
				case this.CALCULO_SALVO:
					this.sgMoeda = getElementValue('moeda.sgMoeda');
					this.dsSimbolo = getElementValue('moeda.dsSimbolo');
				case this.CANCELAR_TUDO:
					fechar();
					break;
			}
		}
		,sgMoeda : ''
		,dsSimbolo : ''
		,vlCubagem : ''
		,state : 'VOLTAR_DETALHAMENTO'
	};
	/************************************************************\
	*
	\************************************************************/
	function calcularFrete(){
		var objForm = document.forms[0];

		if(validateTabScript(objForm) && isMaiorZero('percentualAforo')){
			var args = buildFormBeanFromForm(objForm);
			var service = "lms.expedicao.manterCRTCalculoCRTAction.calcularFrete";
			var sdo = createServiceDataObject(service, "calcularFrete", args);

			xmit({serviceDataObjects:[sdo]});
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function isMaiorZero(nmObj){
		var o = getElement(nmObj);
		var v = getElementValue(o);
		if(v != '' && v <= 0){
			alert("O campo '" + o.label + "' deve ser maior que zero.");
			setFocus(o, false);
			return false;
		}
		return true;
	}
	/************************************************************\
	*
	\************************************************************/
	function calcularFrete_cb(data, error, errorType){
		var blFill = fillComponents(data, error, errorType);
		if(blFill == true) {
			setDisabled('btnGravarValores', false);
			setFocus('btnGravarValores', false);
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function fillComponents(data, error, errorType){
		if(error && error != undefined){
			alert(error);
			setDisabled('btnGravarValores', true);
			return false;
		}
		
		if(data && data != undefined) {
			setElementValue("tabelaPreco.dsDescricao", data.dsDescricaoTabela);
			setElementValue("moeda.sgMoeda", data.sgMoeda);
			setElementValue("moeda.dsSimbolo", data.dsSimbolo);

			if(data.percentualAforo) setElementValue("percentualAforo", setFormat('percentualAforo', data.percentualAforo));

			populateGridParcelas(data, error);
			populateGridServicos(data, error);
		}

		return true;
	}
	/************************************************************\
	*
	\************************************************************/
	function populateGridParcelas(data, error){
		if(data && data != undefined && data.parcelasFrete){
			if(data.parcelasFrete && data.parcelasFrete.length > 0) {
				parcelaGridDef.resetGrid();
				parcelaGridDef.onDataLoad_cb(data.parcelasFrete, error);
				
				setElementValue("vlDesconto", setFormat("vlDesconto",  data.vlDesconto));
				setElementValue("vlTotalFrete", setFormat("vlTotalFrete", data.vlTotalFrete));
			}
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function populateGridServicos(data, error){
		if(data && data != undefined && data.servicos){
			if(data.parcelasFrete && data.servicos.length > 0) {
				servicoGridDef.resetGrid();
				servicoGridDef.onDataLoad_cb(data.servicos, error);
			}
			setElementValue("totalServicos", setFormat("totalServicos",  data.vlTotalServico));
			setElementValue("totalCRT", setFormat("totalCRT", data.vlTotalCrt));
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function setValorCubagem(objCaller){
		var args = {percentualAforo : getElementValue('percentualAforo')};
		var service = "lms.expedicao.manterCRTCalculoCRTAction.updateVlCubagem";
		var sdo = createServiceDataObject(service, "setValorCubagem", args);
		xmit({serviceDataObjects:[sdo]});
	}
	/************************************************************\
	*
	\************************************************************/
	function setValorCubagem_cb(data){
		if(data && data != undefined){
			StateWindowListener.vlCubagem = data.vlCubagem;
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function populateWindow(){
		var service = 'lms.expedicao.manterCRTCalculoCRTAction.findCtoInternacionalInSession';
		var sdo = createServiceDataObject(service, 'populateWindow', {});
		xmit({serviceDataObjects:[sdo]});
	}
	/************************************************************\
	*
	\************************************************************/
	function populateWindow_cb(data, error, errorType){
		fillComponents(data, error, errorType);
	}
	/************************************************************\
	*
	\************************************************************/
	function voltarDetalhamento(){
		removeInSession();
		fechar();
	}
	/************************************************************\
	*
	\************************************************************/
	function storeInSession(){
		var objForm = document.forms[0];
		if(validateTabScript(objForm) && isMaiorZero('percentualAforo')){
			var args = buildFormBeanFromForm(objForm);
			var service = 'lms.expedicao.manterCRTCalculoCRTAction.storeInSession';
			var sdo = createServiceDataObject(service, 'storeInSession', args);
			xmit({serviceDataObjects:[sdo]});
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function storeInSession_cb(data, error){
		if(error && error != undefined){
			alert(error);
			setDisabled('btnCancelarTudo', false);
			return false;
		}
		StateWindowListener.sgMoeda = getElementValue('moeda.sgMoeda');
		StateWindowListener.setState("CALCULO_SALVO");
	}
	/************************************************************\
	*
	\************************************************************/
	function removeInSession(){
		var service = 'lms.expedicao.manterCRTCalculoCRTAction.removeInSession';
		var sdo = createServiceDataObject(service, 'removeInSession', {});
		xmit({serviceDataObjects:[sdo]});
	}
	/************************************************************\
	*
	\************************************************************/
	function removeInSession_cb(data, error){
		if(error && error != undefined){
			alert(error);
			return false;
		}
		setDisabled('btnCancelarTudo', false);
	}
	/************************************************************\
	*
	\************************************************************/
	function nothing(){ return false; }
	/************************************************************\
	*
	\************************************************************/
	function fechar(){ self.close(); }
	/************************************************************\
	*
	\************************************************************/
	function responsavelFreteDataLoad_cb(data, error){
		if(error && error != undefined){
			alert(error);
			return false;
		}
		responsavelFrete_cb(data);
		var modo = dialogArguments.MODO_TELA;

		if(modo != 'INCLUSAO' && !dialogArguments.isModified){
			StateWindowListener.setState(StateWindowListener.VISUALIZACAO);
		} else {
			removeInSession();
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function cancelarTudo(){
		var service = 'lms.expedicao.manterCRTCalculoCRTAction.cancelarTudo';
		var sdo = createServiceDataObject(service, 'cancelarTudo', {});
		xmit({serviceDataObjects:[sdo]});
	}
	/************************************************************\
	*
	\************************************************************/
	function cancelarTudo_cb(data, error){
		if(error && error != undefined){
			alert(error);
			return false;
		}
		StateWindowListener.setState("CANCELAR_TUDO");
	}
	/************************************************************\
	*
	\************************************************************/
	function myOnPageLoadCallBack(){
		onPageLoad();
		document.body.onunload = function(){
			window.returnValue = StateWindowListener;
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function divisaoCliente_cb(data, error){
		if(error && error != undefined){
			alert(error);
			return false;
		}
		divisaoCliente_idDivisaoCliente_cb(data);
		var objCmb = getElement('divisaoCliente.idDivisaoCliente');

		if(data && data.length == 1){
			setDisabled(objCmb, true);
			objCmb.options[1].selected = true;
		} else {
			setDisabled(objCmb, false);
		}
	}
	
	function changeResponsavel(obj){
		comboboxChange({e:obj});
		if(getElementValue(obj) == '') setDisabled('divisaoCliente.idDivisaoCliente', true);
	}
</script>