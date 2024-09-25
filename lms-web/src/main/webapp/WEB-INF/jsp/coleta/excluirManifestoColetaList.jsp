<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>

	function loadData_cb(data, error) {
		onPageLoad_cb();
		
		changeDocumentWidgetType({
	   		documentTypeElement:document.getElementById("manifesto.tpManifesto"), 
	   		filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
	   		documentNumberElement:document.getElementById('manifesto.manifestoColeta.idManifestoColeta'), 
	   		documentGroup:'MANIFESTO',
	   		actionService:'lms.carregamento.excluirManifestoColetaAction'
	   		});
	   		
	   	//Deixa disable o objeto de filial da tag de manifesto...
	   	setDisabled("manifesto.filialByIdFilialOrigem.idFilial", true);
	   		
	   	var data = new Object();
		var sdo = createServiceDataObject("lms.carregamento.excluirManifestoColetaAction.findFilialUsuarioLogado", "findFilialUsuarioLogado", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Carrega a filial do ususario para a tag de manifesto.
	 */
	function findFilialUsuarioLogado_cb(data, error) {
		if (data._exception==undefined) {
		
			setElementValue("manifesto.filialByIdFilialOrigem.idFilial", data.idFilial);
			setElementValue("manifesto.filialByIdFilialOrigem.sgFilial", data.sgFilial);
			//Seta os elementos para masterLink para nao serem anulados...
			document.getElementById("manifesto.filialByIdFilialOrigem.idFilial").masterLink=true;
			document.getElementById("manifesto.filialByIdFilialOrigem.sgFilial").masterLink=true;
		
			changeDocumentWidgetFilial({
			 	filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
			 	documentNumberElement:document.getElementById('manifesto.manifestoColeta.idManifestoColeta')
			 	});			 	
		} else {
			alert(data._exception._message);
		}
		
		setFocusOnFirstFocusableField();
	}

</script>

<adsm:window title="programacaoColetas" service="lms.carregamento.excluirManifestoColetaAction" onPageLoadCallBack="loadData">

	<adsm:form action="/coleta/manterManifestosColeta" idProperty="idManifestoColeta" id="excluirManifestoColetaForm">
		
		<adsm:hidden property="manifesto.filialByIdFilialOrigem.pessoa.nmFantasia" />		
		<adsm:combobox property="manifesto.tpManifesto" 
					   label="manifesto" serializable="false" 
					   service="lms.carregamento.excluirManifestoColetaAction.findTipoManifesto" 
					   boxWidth="125"
					   optionProperty="value" 
					   optionLabelProperty="description"
					   onchange="" defaultValue="CO" disabled="true">

			<adsm:lookup dataType="text"
						 property="manifesto.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 action="" 
						 size="3" maxLength="3" picker="false" disabled="false" serializable="false"
						 onchange="" >						 	
			</adsm:lookup>

			<adsm:lookup dataType="integer" 
						 property="manifesto.manifestoColeta" 
						 idProperty="idManifestoColeta" 
						 criteriaProperty="nrManifestoOrigem" 
						 service=""
						 action="" 
						 onchange="return manifestoLookupChange();"
						 onDataLoadCallBack="manifestoColetaLookUp"
						 onPopupSetValue="manifestoColetaPopUp"
						 size="10" maxLength="8" mask="00000000" disabled="true" serializable="true" >						 
			</adsm:lookup>
		</adsm:combobox>
		
		<adsm:hidden property="controleCarga.idControleCarga" />
		<adsm:hidden property="controleCarga.tpStatusControleCarga" />
		<adsm:textbox property="controleCarga.filialByIdFilialOrigem.sgFilial" dataType="text" 
					  label="controleCarga" disabled="true"  size="3" maxLength="3" >
			<adsm:textbox dataType="integer" property="controleCarga.nrControleCarga" 
						  maxLength="8" size="8" mask="00000000" disabled="true"/>
		</adsm:textbox>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button id="reset" buttonType="reset" caption="limpar" onclick="limparView()"/>
		</adsm:buttonBar>
		
		<script>
			function lms_02079() {
				alert("<adsm:label key='LMS-02079'/>");
			}
			function lms_02080() {
				return confirm("<adsm:label key='LMS-02080'/>");
			}
		</script>
	</adsm:form>

	<adsm:grid property="manifestoColeta" idProperty="idPedidoColeta" selectionMode="none" 
			   onRowClick="disableGridClick" scrollBars="horizontal" gridHeight="235" title="coletas" rows="11"
			   onDataLoadCallBack="gridManifestoColeta">		
		<adsm:gridColumnGroup separatorType="PEDIDO_COLETA">
			<adsm:gridColumn title="coleta" property="sgFilial" width="45" />
			<adsm:gridColumn title="" property="nrColeta" width="80" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="rota" property="dsRota" width="140" />
		<adsm:gridColumn title="dadosColeta" property="dadosColeta" image="/images/popup.gif" openPopup="true" link="/coleta/consultarColetasDadosColeta.do?cmd=main" popupDimension="790,520" width="100" align="center" linkIdProperty="idPedidoColeta"/>
		<adsm:gridColumn title="cliente" property="nrIdentificacaoFormatado" width="120" align="right" />
		<adsm:gridColumn title="" property="nmPessoa" width="250" />
		<adsm:gridColumn title="endereco" property="enderecoComplemento" width="280" />
		<adsm:gridColumn title="telefone" property="nrTelefoneCliente" width="100" align="right" />
		<adsm:gridColumn title="volumes" property="qtTotalVolumesVerificado" width="80" align="right"/>
		<adsm:gridColumn title="peso" property="psTotalVerificado" width="100" align="right" unit="kg" dataType="decimal" mask="###,###,###,##0.000" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valor" property="sgMoeda" width="30" />
			<adsm:gridColumn title="" property="dsSimbolo" width="30" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" property="vlTotalVerificado" dataType="currency" width="100" align="right" />
		<adsm:gridColumn title="solicitacao" property="dhPedidoColeta" dataType="JTDateTimeZone" align="center" width="130" />
		<adsm:gridColumn title="previsaoColeta" property="dtPrevisaoColeta" dataType="JTDate" align="center" width="100" />
		<adsm:gridColumn title="horarioColeta" property="horarioColetaFormatado" width="130" align="center"/>
		<adsm:gridColumn title="contato" property="nmContatoCliente" width="150" />
		<adsm:gridColumn title="regiao" property="dsRegiaoColetaEntregaFil" width="170" />
		<adsm:gridColumn title="observacoes" property="obPedidoColeta" width="400" />
		
		<adsm:buttonBar> 
			<adsm:button caption="excluirManifesto" id="btnExcluirManifesto" onclick="removeManifestoColeta();" disabled="false"/>
		</adsm:buttonBar>
	</adsm:grid>	
</adsm:window>

<script>

	function manifestoLookupChange() {
		 var result = manifesto_manifestoColeta_nrManifestoOrigemOnChangeHandler();
		 
		 if (getElementValue("manifesto.manifestoColeta.nrManifestoOrigem")=="") {
		 	setElementValue("controleCarga.idControleCarga", "");
			setElementValue("controleCarga.nrControleCarga", "");
			setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", "");
			manifestoColetaGridDef.resetGrid();
		 }
		 
		 return result;
	}

	function manifestoColetaLookUp_cb(data, error){
	
		result = manifesto_manifestoColeta_nrManifestoOrigem_exactMatch_cb(data);
	
		if (result!=false) {
			findControleCarga(data[0].idManifestoColeta);
		}
	}
	
	function manifestoColetaPopUp(data){
		findControleCarga(data.idManifestoColeta);
	}	
	
	/**
	 * Realiza a pesquisa do controleCarga
	 *
	 * @param idControleCarga
	 */
	function findControleCarga(idManifestoColeta) {
		var data = new Object();
		data.idManifestoColeta = idManifestoColeta;
		var sdo = createServiceDataObject("lms.carregamento.excluirManifestoColetaAction.findControleCarga", "findControleCarga", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findControleCarga_cb(data, error) {			
		if (data._exception==undefined) {
			setElementValue("controleCarga.idControleCarga", data.controleCarga.idControleCarga);
			setElementValue("controleCarga.nrControleCarga", setFormat(document.getElementById("controleCarga.nrControleCarga"), data.controleCarga.nrControleCarga));
			setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", data.controleCarga.filialByIdFilialOrigem.sgFilial);
			setElementValue("controleCarga.tpStatusControleCarga", data.controleCarga.tpStatusControleCarga);
		
			//Gera o alert...
			if ((getElementValue("controleCarga.tpStatusControleCarga")!="GE") && (getElementValue("controleCarga.tpStatusControleCarga")!="EC") &&
				(getElementValue("controleCarga.tpStatusControleCarga")!="CP") && (getElementValue("controleCarga.tpStatusControleCarga")!="AE") && 
				(getElementValue("controleCarga.tpStatusControleCarga")!="PO")) {
				lms_02079();
			}
			
			populateGrid();
		} else {
			alert(data._exception._message);
		}
	}
	
	/**
	 * Popula a grid
	 */
	function populateGrid() {
		var data = new Object();
		data.idManifestoColeta = getElementValue("manifesto.manifestoColeta.idManifestoColeta");
		manifestoColetaGridDef.executeSearch(data, true);
	}
	
	function gridManifestoColeta_cb(data, error) {
		
		if ((getElementValue("controleCarga.tpStatusControleCarga")!="GE") && (getElementValue("controleCarga.tpStatusControleCarga")!="EC") &&
			(getElementValue("controleCarga.tpStatusControleCarga")!="CP") && (getElementValue("controleCarga.tpStatusControleCarga")!="AE") && 
			(getElementValue("controleCarga.tpStatusControleCarga")!="PO")) {
			
			setDisabled("btnExcluirManifesto", true);
		}
	}
	
	/** 
	 * Callback da grid. Seta o botao enable ou disabled dependendo do status do controle de carga.
	 */
	function removeManifestoColeta() {
	
		//Apenas por preucaucao...
		if ((getElementValue("controleCarga.tpStatusControleCarga")!="GE") && (getElementValue("controleCarga.tpStatusControleCarga")!="EC") &&
			(getElementValue("controleCarga.tpStatusControleCarga")!="CP") && (getElementValue("controleCarga.tpStatusControleCarga")!="AE") && 
			(getElementValue("controleCarga.tpStatusControleCarga")!="PO")) {
			
			lms_02079();
			setDisabled("btnExcluirManifesto", true);
			return false;
		}
		
		if (lms_02080()) {
			var data = new Object();
			data.idManifestoColeta = getElementValue("manifesto.manifestoColeta.idManifestoColeta");
			var sdo = createServiceDataObject("lms.carregamento.excluirManifestoColetaAction.removeManifestoColeta", "removeManifestoColeta", data);
			xmit({serviceDataObjects:[sdo]});
		}
	}
	
	function removeManifestoColeta_cb(data, error) {
		if (data._exception==undefined) {
			limparView();
			showSuccessMessage();
		} else {
			alert(data._exception._message);
		}
	}
	
	/**
	 * Limpa a tela
	 */
	function limparView() {
		cleanButtonScript(this.document);
		manifestoColetaGridDef.resetGrid();
	}
	
	/**
	 * Desabilita o click na grid
	 */
	function disableGridClick(){
		return false;
	}

</script>