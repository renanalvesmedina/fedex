<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	onPageLoad="myOnLoadPage"
	service="lms.expedicao.manterCRTGastosAction">

	<adsm:form
		action="/expedicao/manterCRTGastos">

		<adsm:section
			caption="gastos15"
			width="54"/>

			<adsm:label
				key="branco"
				style="border:none;"
				width="1%"/>

			<adsm:textbox
				property="dsMoeda"
				dataType="text"
				required="false"
				size="7"
				serializable="false"
				labelWidth="15%"
				label="moeda"
				width="80%"
				disabled="true"/>
			
			<adsm:hidden
				property="moeda.dsSimbolo"/>

			<adsm:hidden
				property="moeda.sgMoeda"/>

			<adsm:grid
				property="trechosCtoInt"
				idProperty="idTrechoCtoInt"
				gridHeight="90"
				unique="true"
				showPagging="false"
				selectionMode="none"
				onRowClick="nothing"
				width="373"
				rows="4"
				onValidate="recalcularGastos"
				scrollBars="vertical">

				<adsm:gridColumn
					property="tramoFreteInternacional.dsTramoFreteInternacional"
					title="trecho"
					width="177"/>

				<adsm:editColumn
					property="vlFreteRemetente"
					title="valorRemetente"
					field="TextBox"
					dataType="decimal"
					width="98" />

				<adsm:editColumn
					property="vlFreteDestinatario"
					title="valorDestinatario"
					field="TextBox"
					dataType="decimal"
					width="98"/>

			</adsm:grid>

			<adsm:label
				key="branco"
				width="2%"/>

			<adsm:textbox
				label="total"
				property="vlTotalRemetente"
				dataType="decimal"
				size="13"
				disabled="true"
				labelWidth="24%"
				width="13%"/>

			<adsm:textbox
				property="vlTotalDestinatario"
				dataType="decimal"
				size="13"
				disabled="true"
				width="63%"/>

			<adsm:textbox
				label="totalGeral"
				property="vlTotalGeral"
				dataType="decimal"
				size="13"
				disabled="true"
				labelWidth="37%"
				width="63%"/>

			<adsm:label
				key="espacoBranco"
				style="border:none;"
				width="100%"/>

			<adsm:label
				key="branco"
				width="2%"/>

			<adsm:textbox
				label="totalFreteExterno19"
				property="vlFreteExterno"
				dataType="decimal"
				size="13"
				disabled="true"
				labelWidth="35%"
				width="63%"/>

			<adsm:buttonBar
				lines="2"
				freeLayout="true">

				<adsm:button
					id="btnSalvarCrt"
					disabled="false"
					onclick="store()"
					caption="salvarCRT"/>

				<adsm:button
					id="btnVoltarDetalhamento"
					disabled="false"
					caption="voltarDetalhamento"
					boxWidth="140"
					onclick="voltarDetalhamento()"/>

				<adsm:button
					id="btnCancelarTudo"
					onclick="cancelarTudo()"
					disabled="false"
					boxWidth="110"
					caption="cancelarTudo"/>

				<adsm:button
					id="btnFechar"
					onclick="fechar()"
					disabled="true"
					caption="fechar"/>

			</adsm:buttonBar>

	</adsm:form>

</adsm:window>
<script type="text/javascript">
	var StateWindowListener = {/* Variavel que define o retorno da popup.*/
		VISUALIZACAO : 'VISUALIZACAO'
		,VOLTAR_DETALHAMENTO : 'VOLTAR_DETALHAMENTO'
		,CANCELAR_TUDO : 'CANCELAR_TUDO'
		,CRT_SALVO : 'CRT_SALVO'
		,DEFAULT : 'VOLTAR_DETALHAMENTO'

		//Recebe o estado da window
		,setState : function(state){
			this.state = state;

			switch(state){
				case this.DEFAULT:
				case this.VOLTAR_DETALHAMENTO:
					fechar();
					break;
				case this.VISUALIZACAO:
					setDisabled(document, true);
					setDisabled('btnFechar', false);
					setFocus('btnFechar', false);
					break;

				case this.CANCELAR_TUDO:
				case this.CRT_SALVO:
					fechar();
					break;
			}
		}
		,state : 'VOLTAR_DETALHAMENTO'
		,nrCrt : ''
		,dhInclusao : ''
		,nmUsuario : ''
	};
	/************************************************************\
	*
	\************************************************************/
	function myOnLoadPage(){
		onPageLoad();
		
		var modo = dialogArguments.MODO_TELA;
		if(modo != 'INCLUSAO' && !dialogArguments.isModified){
			setDisabled(document, true);
			setDisabled('btnFechar', false);
			setFocus('btnFechar', false);
	
			StateWindowListener.setState(StateWindowListener.VISUALIZACAO);
		}

		document.body.onunload = function(){
			window.returnValue = StateWindowListener;
		}

		var service = 'lms.expedicao.manterCRTGastosAction.findCtoInternacionalInSession';
		var sdo = createServiceDataObject(service, 'myOnLoadPage', {});
		xmit({serviceDataObjects:[sdo]});
	}
	/************************************************************\
	*
	\************************************************************/
	function myOnLoadPage_cb(data, error){
		if(error && error != undefined){
			alert(error);
			return false;
		}

		if(data && data != undefined){
			setElementValue('moeda.sgMoeda', data.moeda.sgMoeda);
			setElementValue('moeda.dsSimbolo', data.moeda.dsSimbolo);
			setElementValue('dsMoeda', data.moeda.sgMoeda + ' ' + data.moeda.dsSimbolo);
			setTotalizadores_cb(data);
			populateGrid(data.trechoCtoInt, error);
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function populateGrid(trechosCtoInt, error){
		trechosCtoIntGridDef.resetGrid();
		trechosCtoIntGridDef.onDataLoad_cb(trechosCtoInt, error);
	}
	/************************************************************\
	*
	\************************************************************/
	function recalcularGastos(rowId, column, objChanged){
		var trechoCtoIntChanged = trechosCtoIntGridDef.gridState.data[rowId];
		var valor = getElementValue(objChanged);
		trechoCtoIntChanged[column] = valor;
		
		if(parseFloat(valor) != 0){
			var targetName = (objChanged.id.indexOf('Remetente') != -1) ? 'vlFreteDestinatario' : 'vlFreteRemetente';
			var idObjToChange = 'trechosCtoInt:'+ rowId +'.' + targetName;
			setElementValue(idObjToChange, setFormat(idObjToChange, '0'));
			trechoCtoIntChanged[targetName] = '0';
		}

		var service = 'lms.expedicao.manterCRTGastosAction.recalculateTotals';
		var sdo = createServiceDataObject(service, 'setTotalizadores', trechoCtoIntChanged);
		xmit({serviceDataObjects:[sdo]});
	}
	/************************************************************\
	*
	\************************************************************/
	function setTotalizadores_cb(data){
		if(data && data != undefined){
			setElementValue('vlFreteExterno', setFormat('vlFreteExterno', data.vlFreteExterno));
			setElementValue('vlTotalGeral', setFormat('vlTotalGeral', data.vlTotalGeral));
			setElementValue('vlTotalRemetente', setFormat('vlTotalRemetente', data.vlTotalRemetente));
			setElementValue('vlTotalDestinatario', setFormat('vlTotalDestinatario', data.vlTotalDestinatario));
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function voltarDetalhamento(){
		var service = 'lms.expedicao.manterCRTGastosAction.removeInSession';
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

		StateWindowListener.setState(StateWindowListener.VOLTAR_DETALHAMENTO);
	}
	/************************************************************\
	*
	\************************************************************/
	function cancelarTudo(){
		var service = 'lms.expedicao.manterCRTGastosAction.cancelarTudo';
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
	function fechar(){
		self.close();
	}
	/************************************************************\
	*
	\************************************************************/
	function store(){
		var service = 'lms.expedicao.manterCRTAction.store';
		var sdo = createServiceDataObject(service, 'store', {});
		xmit({serviceDataObjects:[sdo]});
	}
	/************************************************************\
	*
	\************************************************************/
	function store_cb(data, error){
		if(error && error != undefined){
			alert(error);
			return false;
		}
		StateWindowListener.nrCrt = data.nrCrt;
		StateWindowListener.dhInclusao = data.dhInclusao;
		StateWindowListener.nmUsuario = data.nmUsuario;
		StateWindowListener.idDoctoServico = data.idDoctoServico;
		StateWindowListener.setState("CRT_SALVO");
	}
	/************************************************************\
	*
	\************************************************************/
	function nothing(){return false;}
</script>