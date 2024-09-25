	<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<script>

	/**
	 * Faz um pre-carregamento da pagina para setar determinados parametros que nao sao setados em
	 * em seu comportamento natural
	 */
	function preLoadPage() {
		loadDataFromParents();
		enableButtons();
		
		var data = new Array();
		var sdo = createServiceDataObject("lms.coleta.manterManifestosColetaAction.getDataUsuario", "loadDataUsuario", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Carrega um array 'dataUsuario' com os dados do usuario em sessao
	 */
	var dataUsuario;
	function loadDataUsuario_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}
		dataUsuario = data;
		fillDataUsuario();
		onPageLoad();
	}

	/**
	 * Carrega os botoes
	 */
	function enableButtons() {
		document.getElementById("adicionar").disabled = false;
		document.getElementById("retornar").disabled = false;
		document.getElementById("consultar").disabled = false;
	}
	
	/**
	 * Carrega algums dados da tela pai
	 * obs: os dados sao setados a partir da aba de detalhamento
	 */
	function loadDataFromParents() {
		var tabGroup = getTabGroup(dialogArguments.window.document);
		var tabDet = tabGroup.getTab("cad");
		
		setElementValue("rotaColetaEntrega.idRotaColetaEntrega", tabDet.getFormProperty("rotaColetaEntrega.idRotaColetaEntrega"));
		setElementValue("rotaColetaEntrega.nrRota", tabDet.getFormProperty("rotaColetaEntrega.nrRota"));
		setElementValue("rotaColetaEntrega.dsRota", tabDet.getFormProperty("rotaColetaEntrega.dsRota"));
		setElementValue("idManifestoColeta", tabDet.getFormProperty("idManifestoColeta"));
		document.getElementById("idManifestoColeta").masterLink = "true";
	}
	
	/**
	 * Carrega os dados da filial do usuario logado para um campo hidden na tela
	 */
	function fillDataUsuario(){
		setElementValue("filialUsuario.idFilial", dataUsuario.filial.idFilial);
		setElementValue("filialUsuario.sgFilial", dataUsuario.filial.sgFilial);
		setElementValue("filialUsuario.pessoa.nmFantasia", dataUsuario.filial.pessoa.nmFantasia);
	}
</script>

<adsm:window title="adicionarColetasManifestos" service="lms.coleta.manterManifestosColetaAction" onPageLoad="preLoadPage">
	<adsm:form action="/coleta/manterManifestosColeta" idProperty="idPedidoColeta">
	
		<adsm:hidden property="idManifestoColeta"/>
		<adsm:lookup dataType="integer" property="rotaColetaEntrega" idProperty="idRotaColetaEntrega" criteriaProperty="nrRota"
			 		 service="lms.coleta.manterManifestosColetaAction.findLookupByFilialUsuario" action="/municipios/manterRotaColetaEntrega"
			  		 label="rotaColetaEntrega" labelWidth="20%" width="80%" maxLength="3" size="3">
			 <adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="filialUsuario.idFilial" disable="true"/>
			 <adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="filialUsuario.sgFilial" disable="true"/>
			 <adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="filialUsuario.pessoa.nmFantasia" disable="true"/>
			  
			 <adsm:propertyMapping modelProperty="dsRota" relatedProperty="rotaColetaEntrega.dsRota"/>
			 <adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" disabled="true" maxLength="30" size="30"/>
		</adsm:lookup>
					 
		<adsm:lookup dataType="text" property="filialUsuario"  idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.coleta.manterManifestosColetaAction.findLookupBySgFilial" action="/municipios/manterFiliais" 
					 onchange="return sgFilialOnChangeHandler();" onDataLoadCallBack="disableNrColeta"
					 label="coleta" labelWidth="20%" width="30%" size="3" maxLength="3" picker="false" serializable="true" disabled="true">
			<adsm:lookup dataType="integer" property="pedidoColeta" idProperty="idPedidoColeta" criteriaProperty="nrColeta"
						 action="/coleta/consultarColetas" cmd="pesq" service="lms.coleta.manterManifestosColetaAction.findLookupPedidoColeta" 
						 onDataLoadCallBack="loadNrColeta" onPopupSetValue="enableNrColeta" exactMatch="false" size="15" maxLength="10" mask="00000000">

				<adsm:propertyMapping modelProperty="tpSCAberto" criteriaProperty="chkAberta"/>
				<adsm:propertyMapping modelProperty="tpSCTransmitida" criteriaProperty="chkTransmitida"/>
				<adsm:propertyMapping modelProperty="tpSCManifestada" criteriaProperty="chkManifestdada"/>
				<adsm:propertyMapping modelProperty="tpSCExecutada" criteriaProperty="chkExecutada"/>
				<adsm:propertyMapping modelProperty="tpSCCancelada" criteriaProperty="chkCancelada"/>
						 
				<adsm:propertyMapping modelProperty="filialByIdFilialResponsavel.idFilial" criteriaProperty="filialUsuario.idFilial" disable="true"/>      
				<adsm:propertyMapping modelProperty="filialByIdFilialResponsavel.sgFilial" criteriaProperty="filialUsuario.sgFilial" disable="true"/>      
				<adsm:propertyMapping modelProperty="filialByIdFilialResponsavel.pessoa.nmFantasia" criteriaProperty="filialUsuario.pessoa.nmFantasia" disable="true"/>
			</adsm:lookup>
		</adsm:lookup>
		
		<adsm:hidden property="filialUsuario.pessoa.nmFantasia" serializable="false"/>
		
		<adsm:hidden property="chkAberta" value="true" serializable="false"/>
		<adsm:hidden property="chkTransmitida" value="false" serializable="false"/>
		<adsm:hidden property="chkManifestdada" value="false" serializable="false"/>
		<adsm:hidden property="chkExecutada" value="false" serializable="false"/>
		<adsm:hidden property="chkCancelada" value="false" serializable="false"/>
					 
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="pedidosColeta" id="consultar"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		<script>
			function lms_02009() {
				alert("<adsm:label key='LMS-02009'/>");
			}
			function lms_02010() {
				alert("<adsm:label key='LMS-02010'/>");
			}
		</script>
	</adsm:form>
	<adsm:grid property="pedidosColeta" idProperty="idPedidoColeta" scrollBars="horizontal"
			   service="lms.coleta.manterManifestosColetaAction.findAdicionarColetaPaginated"	
			   rowCountService="lms.coleta.manterManifestosColetaAction.getRowCountAdicionarColeta" gridHeight="280"
			   defaultOrder="nrColeta:asc" onRowClick="disableGridClick" rows="14">
		<adsm:gridColumnGroup separatorType="PEDIDO_COLETA">	   
			<adsm:gridColumn property="sgFilial" title="coleta" width="30"/>
			<adsm:gridColumn property="nrColeta" title="" mask="00000000" dataType="integer" width="70" align="right"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="nmPessoa" title="cliente" width="200" />
		<adsm:gridColumn property="dsRota" title="rota" width="100" />	   
		<adsm:gridColumn property="enderecoComComplemento" title="endereco" width="300" />
		<adsm:gridColumn property="qtTotalVolumesVerificado" title="volumes" width="70" align="right"/>
		<adsm:gridColumn property="psTotalVerificado" title="peso" width="70" unit="kg" dataType="decimal" mask="###,###,##0.000"/>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn property="sgMoeda" title="valor" width="30"/>
			<adsm:gridColumn property="dsSimbolo" title="" width="30"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="vlTotalVerificado" title="" width="70" dataType="currency"/>
		<adsm:gridColumn property="dtPrevisaoColeta" title="previsaoColeta" width="100"  dataType="JTDate"/>
		<adsm:gridColumn title="dadosColeta" property="dadosColeta" image="/images/popup.gif" openPopup="true" link="/coleta/consultarColetasDadosColeta.do?cmd=main" popupDimension="790,520" width="100" align="center" linkIdProperty="idPedidoColeta"/>
		<adsm:gridColumn title="alterarValores" property="alterarValores" image="/images/popup.gif" openPopup="true" link="javascript:alterarValores" width="100" align="center" />
	</adsm:grid>
	<adsm:buttonBar> 
		<adsm:button id="adicionar" caption="adicionar" onclick="updatePedidoColeta()"/> 
		<adsm:button id="retornar" caption="retornar" onclick="returnToParent()"/> 
	</adsm:buttonBar>
</adsm:window>

<script>
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click"){
			fillDataUsuario();
			pedidosColetaGridDef.resetGrid();
		}
	}
	
	/**
	 * Faz a validacao dos campos de filtro da tela antes de efetuar a pesquisa
	 * onde somente um dos campos pode ser preenchido.
	 */
	function findPedidosColeta() {
		if (((getElementValue("rotaColetaEntrega.idRotaColetaEntrega")!="") && (getElementValue("pedidoColeta.idPedidoColeta")=="")) || 
		    ((getElementValue("rotaColetaEntrega.idRotaColetaEntrega")=="") && (getElementValue("pedidoColeta.idPedidoColeta")!=""))){
		    
		    if (getElementValue("rotaColetaEntrega.idRotaColetaEntrega")!="") {
		    	var idValue = getElementValue("rotaColetaEntrega.idRotaColetaEntrega");
				pedidosColetaGridDef.executeSearch({rotaColetaEntrega:{idRotaColetaEntrega:idValue}}, true);
		    } else {
		    	var idValue = getElementValue("pedidoColeta.idPedidoColeta");
				pedidosColetaGridDef.executeSearch({idPedidoColeta:idValue}, true);
		    }
		} else {
			lms_02009();
		}
	}

	/**
	 * Re-carrega a grid apos se clicar em 'alterar valores'
	 */
	function alterarValores(id){
		showModalDialog('coleta/alteracaoValoresColeta.do?cmd=main&idPedidoColeta='+id ,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:770px;dialogHeight:200px;');
		populaGrid();
	}

	/**
	 * Controla o evento de adicionar ids da grid e disparo de atualizacao para 
	 * a camada de servico.
	 */
	function updatePedidoColeta() {
	    if (pedidosColetaGridDef.getSelectedIds().ids.length>0) {
	    	var pedidoColeta = new Array();
	    	var idsPedidoColeta = pedidosColetaGridDef.getSelectedIds().ids;
	    	
	    	setNestedBeanPropertyValue(pedidoColeta, "idsPedidoColeta", idsPedidoColeta);
			setNestedBeanPropertyValue(pedidoColeta, "idManifestoColeta", getElementValue("idManifestoColeta"));
	    	
			var sdo = createServiceDataObject("lms.coleta.manterManifestosColetaAction.updateAdicionarColeta", "updatePedidoColeta", pedidoColeta);
   			xmit({serviceDataObjects:[sdo]});
		} else {
			lms_02010();
		}
	}
	
	function updatePedidoColeta_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}	
		showSuccessMessage();
		populaGrid();
	}
	
	/**
	 * Popula a grid de acordo com os dados do filtro
	 */
	function populaGrid() {
		var fb = buildFormBeanFromForm(this.document.forms[0], 'LIKE_END'); 
		pedidosColeta_cb(fb);
	}
	
	/**
	 * Controla o objeto de nao conformidade 
	 */	
	function sgFilialOnChangeHandler() {
		if (getElementValue("filialUsuario.sgFilial")=="") {
			disableNrColeta(true);
			resetValue("pedidoColeta.idPedidoColeta");
		} else {
			disableNrColeta(false);
		}
		return lookupChange({e:document.forms[0].elements["filialUsuario.idFilial"]});
	}
	
	function disableNrColeta_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}	
		if (data.length==0) disableNrColeta(false);
		return lookupExactMatch({e:document.getElementById("filialUsuario.idFilial"), data:data});
	}
	
	function loadNrColeta_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}	
		pedidoColeta_nrColeta_exactMatch_cb(data);
		if (data[0]!=undefined) {
			document.getElementById("filialUsuario.sgFilial").value=data[0].filialByIdFilialResponsavel.sgFilial;
		}
	}
	
	function disableNrColeta(disable) {
		document.getElementById("pedidoColeta.nrColeta").disabled = disable;
	}
	
	function enableNrColeta(data){
		disableNrColeta(false);
	}
	
	/**
	 * fecha a atual janela
	 */
	function returnToParent(){
		self.close();
	}
	 
	/**
	 * Desabilita o click na grid
	 */
	function disableGridClick(){
		return false;
	}
</script>