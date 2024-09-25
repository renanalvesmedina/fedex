<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<script type="text/javascript">
	/**
	 * Carrega dados do usuario
	 */
	function loadDataObjects() {	
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
</script>

<adsm:window service="lms.coleta.manterManifestosColetaAction" onPageLoad="loadDataObjects">
	<adsm:form action="/coleta/manterManifestosColeta" idProperty="idManifestoColeta" >
	
		<adsm:hidden property="filialUsuario.idFilial" serializable="false"/>
		<adsm:hidden property="filialUsuario.sgFilial" serializable="false"/>
		<adsm:hidden property="filialUsuario.pessoa.nmFantasia" serializable="false"/>
	
		<adsm:textbox dataType="text" property="controleCarga.filialByIdFilialOrigem.sgFilial" 
					  label="controleCargas" labelWidth="20%" width="30%" maxLength="3" size="3" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="controleCarga.nrControleCarga" disabled="true" serializable="false" maxLength="8" size="8"/>
		</adsm:textbox>
		
		<adsm:textbox dataType="text" property="filial.sgFilial" label="manifesto" labelWidth="20%" width="30%" maxLength="3" size="3" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="nrManifesto" maxLength="8" size="8" disabled="true" serializable="false"/>
		</adsm:textbox>
		<adsm:textbox dataType="text" property="controleCarga.meioTransporteByIdTransportado.nrFrota" label="meioTransporte" labelWidth="20%" width="30%" maxLength="6" size="6" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="controleCarga.meioTransporteByIdTransportado.nrIdentificador" disabled="true" serializable="false" 
						  maxLength="25" size="25"/>
		</adsm:textbox>
		<adsm:textbox dataType="text" property="controleCarga.meioTransporteByIdSemiRebocado.nrFrota" label="semiReboque" labelWidth="20%" width="30%" maxLength="6" size="6" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador" disabled="true" serializable="false" 
						  maxLength="25" size="25"/>
		</adsm:textbox>
		
		<adsm:hidden property="active" value="A" serializable="false"/>
		
	</adsm:form>
	
	<adsm:grid property="pedidosColeta" idProperty="idPedidoColeta"
			   service="lms.coleta.manterManifestosColetaAction.findPaginatedPedidosColeta"	
			   rowCountService="lms.coleta.manterManifestosColetaAction.getRowCountPedidosColeta"
			   gridHeight="270" onRowClick="disableGridClick" defaultOrder="nrColeta:asc" autoSearch="false" scrollBars="horizontal" rows="13">
		<adsm:gridColumnGroup separatorType="PEDIDO_COLETA">
			<adsm:gridColumn property="filialByIdFilialResponsavel.sgFilial" title="coleta" width="30"/>
			<adsm:gridColumn property="nrColeta" title="" width="70" mask="00000000" dataType="integer" align="right"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="cliente.pessoa.nmPessoa" title="cliente" width="200" />
		<adsm:gridColumn property="rotaColetaEntrega.dsRota" title="rota" width="100" />
		<adsm:gridColumn property="enderecoComComplemento" title="endereco" width="300" />
		<adsm:gridColumn property="qtTotalVolumesVerificado" title="volumes" width="70" align="right"/>
		<adsm:gridColumn property="psTotalVerificado" title="peso" width="70" unit="kg" dataType="decimal" mask="###,###,##0.000"/>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn property="moeda.sgMoeda" title="valor" width="30"/>
			<adsm:gridColumn property="moeda.dsSimbolo" title="" width="30"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="vlTotalVerificado" title="" width="70" dataType="currency"/>
		<adsm:gridColumn property="dtPrevisaoColeta" title="previsaoColeta" width="100"  dataType="JTDate"/>
		<adsm:gridColumn title="dadosColeta" property="dadosColeta" image="/images/popup.gif" openPopup="true" link="/coleta/consultarColetasDadosColeta.do?cmd=main" popupDimension="790,520" width="100" align="center" linkIdProperty="idPedidoColeta"/>
		<adsm:gridColumn title="alterarValores" property="alterarValores" image="/images/popup.gif" openPopup="true" link="javascript:alterarValores" width="100" align="center" />
		<adsm:buttonBar>
			<adsm:button id="emitirRelatorioColetasAbertas" caption="emitirRelatorioColetasAbertas" action="/coleta/emitirColetasAbertas" cmd="main">
				<adsm:linkProperty src="filialUsuario.idFilial" target="filial.idFilial" />
				<adsm:linkProperty src="filialUsuario.sgFilial" target="filial.sgFilial" />
				<adsm:linkProperty src="filialUsuario.pessoa.nmFantasia" target="filial.pessoa.nmFantasia" disabled="true"/>
			</adsm:button>
			<adsm:button id="adicionarColetas" caption="adicionarColetas" onclick="abrirAdicionarColeta();"/>
			<adsm:button id="removerColetas" caption="remover" onclick="removeDataFromGrid();"/>
		</adsm:buttonBar> 
	</adsm:grid>
</adsm:window>

<script>

	//Seta o servico de remocao da grid manualmente...
	pedidosColetaGridDef.removeService = "lms.coleta.manterManifestosColetaAction.removeByIdsPedidoColeta";

	var manifestoColeta = new Array();
	function initWindow(eventObj) {
		document.getElementById("emitirRelatorioColetasAbertas").disabled = false;
		document.getElementById("adicionarColetas").disabled = false;
		document.getElementById("removerColetas").disabled = false;
		fillDataUsuario();
		if (eventObj.name == "removeButton_grid" || eventObj.name == "newButton_click"){
			novaDescricaoPadrao();
		} else if (eventObj.name == "tab_click") {
			resetValue("idManifestoColeta");
			preparaDescricaoPadrao();
		} else if (eventObj.name == "storeButton") {
			populaGrid();
		}
	}
	
	/**
	 * Faz o controle do carregamento dos objetos da "master link"
	 */
	function novaDescricaoPadrao() {
		populaDadosMaster();
		setDefaultFieldsValues();
	}
	
	/**
	 * Re-carrega a grid apos se clicar em 'alterar valores'
	 */
	function alterarValores(id){
		showModalDialog('coleta/alteracaoValoresColeta.do?cmd=main&idPedidoColeta='+id ,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:770px;dialogHeight:200px;');
		populaGrid();
		salvaColetas();
	}
	
	function preparaDescricaoPadrao() {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		var idManifestoColeta = tabDet.getFormProperty("idManifestoColeta");
		
		if (idManifestoColeta != undefined && idManifestoColeta != '') {
			setManifestoColetaBean(tabDet);				
			novaDescricaoPadrao();
			populaGrid();
		}
	}
		
	function populaGrid() {
		var idManifestoColetaValue = getElementValue("idManifestoColeta");
		pedidosColetaGridDef.executeSearch({manifestoColeta:{idManifestoColeta:idManifestoColetaValue}}, true);
	}
		
	/**
	 * Carrega os objetos estaticos da tela.
	 */
	function populaDadosMaster() {
		setElementValue("idManifestoColeta", getNestedBeanPropertyValue(manifestoColeta, "manifestoColeta.idManifestoColeta"));
		setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", getNestedBeanPropertyValue(manifestoColeta, "manifestoColeta.controleCarga.filialByIdFilialOrigem.sgFilial"));
		setElementValue("controleCarga.nrControleCarga", getNestedBeanPropertyValue(manifestoColeta, "manifestoColeta.controleCarga.nrControleCarga"));
		setElementValue("filial.sgFilial", getNestedBeanPropertyValue(manifestoColeta, "manifestoColeta.filial.sgFilial"));
		setElementValue("nrManifesto", getNestedBeanPropertyValue(manifestoColeta, "manifestoColeta.nrManifesto"));
		setElementValue("controleCarga.meioTransporteByIdTransportado.nrFrota", getNestedBeanPropertyValue(manifestoColeta, "manifestoColeta.controleCarga.meioTransporteByIdTransportado.nrFrota"));
		setElementValue("controleCarga.meioTransporteByIdTransportado.nrIdentificador", getNestedBeanPropertyValue(manifestoColeta, "manifestoColeta.controleCarga.meioTransporteByIdTransportado.nrIdentificador"));
		setElementValue("controleCarga.meioTransporteByIdSemiRebocado.nrFrota", getNestedBeanPropertyValue(manifestoColeta, "manifestoColeta.controleCarga.meioTransporteByIdSemiRebocado.nrFrota"));
		setElementValue("controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador", getNestedBeanPropertyValue(manifestoColeta, "manifestoColeta.controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador"));
	}
	
	/**
	 * Monta o objeto de manifesto coleta.
	 */
	function setManifestoColetaBean(tab) {
		setNestedBeanPropertyValue(manifestoColeta, "manifestoColeta.idManifestoColeta", tab.getFormProperty("idManifestoColeta"));
		setNestedBeanPropertyValue(manifestoColeta, "manifestoColeta.controleCarga.filialByIdFilialOrigem.sgFilial", tab.getFormProperty("controleCarga.filialByIdFilialOrigem.sgFilial"));
		setNestedBeanPropertyValue(manifestoColeta, "manifestoColeta.controleCarga.nrControleCarga", tab.getFormProperty("controleCarga.nrControleCarga"));
		setNestedBeanPropertyValue(manifestoColeta, "manifestoColeta.filial.sgFilial", tab.getFormProperty("filial.sgFilial"));
		setNestedBeanPropertyValue(manifestoColeta, "manifestoColeta.nrManifesto", tab.getFormProperty("nrManifesto"));
		setNestedBeanPropertyValue(manifestoColeta, "manifestoColeta.controleCarga.meioTransporteByIdTransportado.nrFrota", tab.getFormProperty("controleCarga.meioTransporteByIdTransportado.nrFrota"));
		setNestedBeanPropertyValue(manifestoColeta, "manifestoColeta.controleCarga.meioTransporteByIdTransportado.nrIdentificador", tab.getFormProperty("controleCarga.meioTransporteByIdTransportado.nrIdentificador"));
		setNestedBeanPropertyValue(manifestoColeta, "manifestoColeta.controleCarga.meioTransporteByIdSemiRebocado.nrFrota", tab.getFormProperty("controleCarga.meioTransporteByIdSemiRebocado.nrFrota"));
		setNestedBeanPropertyValue(manifestoColeta, "manifestoColeta.controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador", tab.getFormProperty("controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador"));
	}
	
	/**
	 * Abre uma popUp com a janela de abertura de coleta
	 */
	function abrirAdicionarColeta(){
		showModalDialog('coleta/manterManifestosColeta.do?cmd=adicionar', window,
						'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:510px;');
		salvaColetas();
	}
	
	/**
	 * Remove os dados da Grid e atualiza os dados do controle ce carga
	 */
	function removeDataFromGrid(){
		pedidosColetaGridDef.removeByIds("lms.coleta.manterManifestosColetaAction.removeByIdsPedidoColeta", "removeDataFromGrid");
	}	
	
	function removeDataFromGrid_cb(data, error){
		if (error){
			alert(error);
			return false;
		}			
		if (data._exception==undefined) {
			salvaColetas();
		} else {
			alert(data._exception._message);
		}
	}
	
	/**
	 * Chama o metodo de salvamento de coletas na service.
	 */
	function salvaColetas() {
		var idManifestoColetaValue = getElementValue("idManifestoColeta");
		var sdo = createServiceDataObject("lms.coleta.manterManifestosColetaAction.storePedidoColeta", "salvaColetas", {manifestoColeta:{idManifestoColeta:idManifestoColetaValue}});
    	xmit({serviceDataObjects:[sdo]});
	}
	
	function salvaColetas_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}	
		showSuccessMessage();
		populaGrid();
	}
	
	/**
	 * Carrega os dados da filial do usuario logado para um campo hidden na tela
	 */
	function fillDataUsuario(){
		setElementValue("filialUsuario.idFilial", dataUsuario.filial.idFilial);
		setElementValue("filialUsuario.sgFilial", dataUsuario.filial.sgFilial);
		setElementValue("filialUsuario.pessoa.nmFantasia", dataUsuario.filial.pessoa.nmFantasia);
	}
	
	/**
	 * Desabilita o click na grid
	 */
	function disableGridClick(){
		return false;
	}
</script>