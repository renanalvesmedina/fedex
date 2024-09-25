<%-- @ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="org.apache.commons.beanutils.*" --%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<script type="text/javascript">
	
	/**
	 * Verifica se a tela teve uma filial populada no campo filial
	 *
	 * @data dados vindos da service
	 * @error erros vindos da service
	 */
	function pageLoad(data, error){
		onPageLoad();
		
		if (getElementValue("filial.idFilial")=="") {
			var data = new Array();
			var sdo = createServiceDataObject("lms.coleta.manterManifestosColetaAction.getDataUsuario", "getDataUsuario", data);
    		xmit({serviceDataObjects:[sdo]});
    	}
	}
	
	/**
	 * Carrega um array 'dataUsuario' com os dados do usuario em sessao
	 *
	 * @data dados do usuario vindos da service
	 * @error 
	 */
	var dataUsuario;
	function getDataUsuario_cb(data, error) {
		dataUsuario = data;
		fillDataUsuario();
		document.getElementById('filial.idFilial').masterLink='true';
		document.getElementById('filial.sgFilial').masterLink='true';		
		document.getElementById('filial.pessoa.nmFantasia').masterLink='true';	
	}
	
	/**
	 * Carrega os dados da filial do usuario logado para um campo hidden na tela
	 */
	function fillDataUsuario(){
		setElementValue("filial.idFilial", dataUsuario.filial.idFilial);
		setElementValue("filial.sgFilial", dataUsuario.filial.sgFilial);
		setElementValue("filial.pessoa.nmFantasia", dataUsuario.filial.pessoa.nmFantasia);
	}
	
	function pageLoad_cb(data, error) {
		onPageLoad_cb(data, error);
		setDadosFilial();
	}
	
	function setDadosFilial() {
		var isLookup = window.dialogArguments && window.dialogArguments.window;
		var idFilialOrigem = getElementValue('filial.idFilial');
		var nmFantasia = getElementValue('filial.pessoa.nmFantasia');
		if (isLookup && idFilialOrigem &&(!nmFantasia)) {			
			var sdo = createServiceDataObject("lms.coleta.consultarManifestosColetaAction.findFilialById", "setDadosFilial", {idFilialOrigem:idFilialOrigem});
	    	xmit({serviceDataObjects:[sdo]});				
		}	
	}
	
	function setDadosFilial_cb(data, error) {
		if (error==undefined) {
			document.getElementById('filial.pessoa.nmFantasia').masterLink = 'true';
			setElementValue('filial.sgFilial', data.filial.sgFilial);
			setElementValue('filial.pessoa.nmFantasia', data.filial.pessoa.nmFantasia);
		}
	}
	
</script>

<adsm:window service="lms.coleta.consultarManifestosColetaAction" onPageLoad="pageLoad" onPageLoadCallBack="pageLoad">
	<adsm:form action="/coleta/consultarManifestosColeta">	
	
		<adsm:hidden property="tpStatusManifestoColeta" serializable="true" />
	
		<adsm:lookup dataType="text" property="filial" idProperty="idFilial" criteriaProperty="sgFilial" 
	   				 service="lms.coleta.consultarManifestosColetaAction.findLookupFilialByUsuario" 
	   				 action="/municipios/manterFiliais" 
					 label="filial" size="3" maxLength="3" labelWidth="17%" width="83%" disabled="true" picker="false">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:lookup dataType="integer" property="rotaColetaEntrega" idProperty="idRotaColetaEntrega" criteriaProperty="nrRota"
			 		 service="lms.coleta.consultarManifestosColetaAction.findLookupRotaColetaEntregaByFilial" 
			 		 action="/municipios/manterRotaColetaEntrega"
			  		 label="rotaColetaEntrega" labelWidth="17%" width="83%" maxLength="3" size="5" required="true">
			 <adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="filial.idFilial"/>
			 <adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="filial.sgFilial"/>
			 <adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="filial.pessoa.nmFantasia"/>
			 <adsm:propertyMapping modelProperty="dsRota" relatedProperty="rotaColetaEntrega.dsRota"/>
			 <adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" maxLength="30" size="30" serializable="false" disabled="true"/>
		</adsm:lookup>
		
					 
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="manifestoColetas"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="manifestoColetas" idProperty="idManifestoColeta" scrollBars="horizontal" gridHeight="240" rows="11" 
			   defaultOrder="controleCarga_.nrControleCarga:asc" selectionMode="none" >
		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA">
			<adsm:gridColumn property="controleCarga.filialByIdFilialOrigem.sgFilial" width="30" title="controleCargas"/>
			<adsm:gridColumn property="controleCarga.nrControleCarga" width="90" title="" dataType="integer" mask="00000000" align="right"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="dhGeracao" width="140" title="geracao" align="center" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="controleCarga.meioTransporteByIdTransportado.nrFrota" width="40" title="veiculo"/>
		<adsm:gridColumn property="controleCarga.meioTransporteByIdTransportado.nrIdentificador" width="80" title=""/>
		<adsm:gridColumn property="controleCarga.meioTransporteByIdSemiRebocado.nrFrota" width="40" title="semiReboque"/>
		<adsm:gridColumn property="controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador" width="80" title=""/>
		<adsm:gridColumnGroup separatorType="MANIFESTO">
			<adsm:gridColumn property="filial.sgFilial" width="30" title="manifesto"/>
			<adsm:gridColumn property="nrManifesto" width="70" mask="00000000" dataType="integer" title="" align="right"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="controleCarga.psTotalFrota" width="100" title="pesoTotal" unit="kg" dataType="decimal" mask="###,###,##0.000" align="right"/>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn property="controleCarga.moeda.sgMoeda" title="valorTotal" width="30"/>
			<adsm:gridColumn property="controleCarga.moeda.dsSimbolo" width="30" title="" dataType="text"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="controleCarga.vlTotalFrota" width="70" title="" dataType="currency" align="right"/>
		<adsm:gridColumn property="controleCarga.meioTransporteByIdTransportado.nrCapacidadeKg" width="175" title="capacidadeVeiculo" unit="kg" dataType="decimal" mask="###,###,##0.000" align="right"/>
	</adsm:grid>
	<adsm:buttonBar>
		<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" disabled="false" />		
	</adsm:buttonBar>
</adsm:window>

<script>

	function initWindow(eventObj) {
		if (eventObj.name == "resetButton") {
			fillDataUsuario();
		}
	}
</script> 
