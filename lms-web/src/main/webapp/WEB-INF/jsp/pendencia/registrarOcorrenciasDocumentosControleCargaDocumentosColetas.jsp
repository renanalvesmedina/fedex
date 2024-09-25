<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript"> 
	/**
	 * Carrega os dados da tela pai
	 */
	function loadWindowData_cb(data, error) {	
		onPageLoad_cb(data, error);
		var parentWindow = dialogArguments.window.document;
		
		//carrega os objetos
		setElementValue("controleCarga.idControleCarga", parentWindow.getElementById("controleCarga.idControleCarga").value);
		setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", parentWindow.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").value);
		setElementValue("controleCarga.nrControleCarga", parentWindow.getElementById("controleCarga.nrControleCarga").value);
				
		//busca os dados da URL
		var url = new URL(parent.location.href);
		var idManifesto = url.parameters["idManifesto"];
		var sgFilial = url.parameters["sgFilial"];
		var nrManifesto = url.parameters["nrManifesto"];		
		
		setElementValue("manifestoColeta.idManifestoColeta", idManifesto);
		setElementValue("manifestoColeta.filial.sgFilial", sgFilial);
		setElementValue("manifestoColeta.nrManifesto", setFormat(document.getElementById("manifestoColeta.nrManifesto"), nrManifesto));	
		
		carregaGrid();
	}
</script>

<adsm:window service="lms.pendencia.registrarOcorrenciasDocumentosControleCargaDocumentosColetasAction" onPageLoadCallBack="loadWindowData">
	<adsm:form action="/pendencia/registrarOcorrenciasDocumentosControleCarga">
		<adsm:hidden property="controleCarga.idControleCarga"/>
		<adsm:textbox dataType="text" property="controleCarga.filialByIdFilialOrigem.sgFilial" 
					  label="controleCargas" labelWidth="17%" width="33%" size="3" maxLength="3"
					  disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="controleCarga.nrControleCarga" maxLength="8" 
						  size="8" mask="00000000" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:hidden property="manifestoColeta.idManifestoColeta"/>		
		<adsm:textbox dataType="text" property="manifestoColeta.filial.sgFilial" 
					 label="manifesto" size="3" maxLength="3" disabled="true" serializable="false">
			<adsm:textbox dataType="integer" property="manifestoColeta.nrManifesto" maxLength="8" 
						  size="8" mask="00000000" disabled="true" serializable="false"/>
		</adsm:textbox>
				
	</adsm:form>

	<adsm:grid property="pedidoColeta" idProperty="idPedidoColeta" unique="true" scrollBars="both" gridHeight="320" 
			   selectionMode="none" showPagging="true" showTotalPageCount="true" onRowClick="onRowClick" >
		<adsm:gridColumnGroup separatorType="PEDIDO_COLETA">
			<adsm:gridColumn property="sgFilial" dataType="text" title="coleta" width="30"/>
			<adsm:gridColumn property="nrColeta" dataType="integer" title="" width="60" mask="00000000"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="nmPessoa" dataType="text" title="cliente" width="150" />
		<adsm:gridColumn property="sgFilialDestino" dataType="text" title="destino" width="60"/>		
		<adsm:gridColumn property="dhPedidoColeta" dataType="JTDateTimeZone" align="center" title="solicitacao" width="120"/>
		<adsm:gridColumn property="psMercadoria" title="peso" unit="kg" dataType="decimal" mask="###,###,##0.000" width="100" align="right" />
		<adsm:gridColumn property="qtVolumes" title="volumes" width="100" align="right" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn property="sgMoeda" dataType="text" title="valor" width="30"/>
			<adsm:gridColumn property="dsSimbolo" dataType="text" title="" width="30"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="vlMercadoria" dataType="currency" title="" width="80"/>
		<adsm:gridColumn property="tpPedidoColeta" isDomain="true" dataType="text" title="tipoColeta" width="150" />
		<adsm:gridColumn property="tpModoPedidoColeta" isDomain="true" title="modoColeta" width="100" />
		<adsm:gridColumn property="tpStatusColeta" isDomain="true" title="status" width="130" />		
		<adsm:gridColumn property="nmUsuario" title="funcionario" width="200" />
		<adsm:gridColumn title="dadosColeta" property="dadosColeta" image="/images/popup.gif" openPopup="true" link="/coleta/consultarColetasDadosColeta.do?cmd=main" popupDimension="790,520" width="100" align="center" linkIdProperty="idPedidoColeta"/>
		<adsm:gridColumn title="dadosCliente" property="dadosCliente" image="/images/popup.gif" width="100" link="/coleta/consultarColetasDadosCliente.do?cmd=main" linkIdProperty="idCliente" align="center"/>
		<adsm:gridColumn title="eventosColeta" property="eventosColeta" image="/images/popup.gif" openPopup="true" link="coleta/consultarEventosColeta.do?cmd=pesq&popUp=true" popupDimension="790,520" width="100" align="center" linkIdProperty="idPedidoColeta"/>

		<adsm:buttonBar>
			<adsm:button caption="fechar" id="closeButton" onclick="window.close()" disabled="false"/>
		</adsm:buttonBar>
	</adsm:grid>	

</adsm:window>

<script type="text/javascript">
	
	/**
	 * Função chamada ao iniciar a página.
	 */
	function initWindow(eventObj) {
		setDisabled("closeButton", false);
	}
	
	/**
	 * Função que carrega os dados para a grid.
	 */
	function carregaGrid() {	
		var fb = buildFormBeanFromForm(this.document.forms[0], 'LIKE_END'); 
		pedidoColeta_cb(fb);	
	}
	
	function onRowClick() {
		return false;
	}
</script>