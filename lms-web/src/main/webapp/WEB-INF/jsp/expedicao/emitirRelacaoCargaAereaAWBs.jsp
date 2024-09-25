<script type="text/javascript">
	function initWindow(event) {
		setDisabled("salvarButton", false);
		if(event.name == "tab_click" || event.name == "tab_load") {
			awbGridDef.executeSearch({}, true);
			copyCiaAerea();
			copyEmpresa();
		}
	}

	function copyCiaAerea() {
		var frame = parent.document.frames["pesq_iframe"];
		var obj = document.getElementById("ciaAerea.filial.idFilial");
		obj.masterLink = "true";
		setElementValue(obj, frame.getIdCiaAerea());
	}

	function copyEmpresa() {
		var frame = parent.document.frames["pesq_iframe"];
		var obj = document.getElementById("empresa.idEmpresa");
		obj.masterLink="true";
		setElementValue(obj, frame.getIdEmpresa());
		
		//var obj2 = document.getElementById("empresa.sgEmpresa");
		//obj2.masterLink="true";
		setElementValue("empresa.sgEmpresa", frame.getSgEmpresa());
	}


	function enableLookupAwb() {
		var nrAwb = getElementValue("awb.nrAwb");
		if(nrAwb == "") {
			setDisabled("awb.idAwb", true);
		} else {
			setDisabled("awb.idAwb", false);
			setFocus(document.getElementById("awb.idAwb"));
		}
		return true;
	}
</script>

<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.expedicao.emitirRelacaoCargaAereaAction" onPageLoadCallBack="oplcb">
	<adsm:form action="/expedicao/emitirRelacaoCargaAerea" idProperty="idAwb">

		<adsm:hidden property="ciaAerea.filial.idFilial" serializable="false"/>
		<adsm:hidden property="empresa.idEmpresa" serializable="false"/>
		<adsm:hidden property="awb.dhEmissao" serializable="false"/>
		<adsm:hidden property="awb.dvAwb" serializable="false"/>
		<adsm:hidden property="awb.dsVooPrevisto" serializable="false"/>
		<adsm:hidden property="moeda.sgMoeda" serializable="false"/>
		<adsm:hidden property="moeda.dsSimbolo" serializable="false"/>
		<adsm:hidden property="awb.vlFrete" serializable="false"/>

		<adsm:combobox label="awb" property="awb.tpStatusAwb"
					   domain="DM_LOOKUP_AWB" disabled="true" serializable="false"
					   defaultValue="E" renderOptions="true">
					   
		    <adsm:textbox property="empresa.sgEmpresa" dataType="text" size="3" disabled="true" serializable="true" maxLength="3"/>
			<adsm:lookup
				service="lms.expedicao.emitirRelacaoCargaAereaAction.findNroAwb"
				action="/expedicao/consultarAWBs"
				property="awb"
				idProperty="idAwb"
				criteriaProperty="nrAwb"
				dataType="text"
				size="12"
				cmd="list"
				maxLength="11"
				required="true"
				onPopupSetValue="validadeAwb"
			>
				<adsm:propertyMapping
					criteriaProperty="awb.tpStatusAwb"
					modelProperty="tpStatusAwb"
				/>
				<adsm:propertyMapping
					criteriaProperty="awb.nrAwb"
					modelProperty="nrAwb"
				/>
				<adsm:propertyMapping
					criteriaProperty="empresa.idEmpresa"
					modelProperty="ciaFilialMercurio.empresa.idEmpresa"
				/>

				<adsm:propertyMapping
					criteriaProperty="ciaAerea.filial.idFilial"
					modelProperty="ciaFilialMercurio.idCiaFilialMercurio"
				/>

				<adsm:propertyMapping
					relatedProperty="awb.dhEmissao"
					modelProperty="dhEmissao"
				/>
				<adsm:propertyMapping
					relatedProperty="awb.dvAwb"
					modelProperty="dvAwb"
				/>
				<adsm:propertyMapping
					relatedProperty="awb.dsVooPrevisto"
					modelProperty="dsVooPrevisto"
				/>
				<adsm:propertyMapping
					relatedProperty="moeda.sgMoeda"
					modelProperty="moeda.sgMoeda"
				/>
				<adsm:propertyMapping
					relatedProperty="moeda.dsSimbolo"
					modelProperty="moeda.dsSimbolo"
				/>
				<adsm:propertyMapping
					relatedProperty="awb.vlFrete"
					modelProperty="vlFrete"
				/>

			</adsm:lookup>
		</adsm:combobox>
		<adsm:buttonBar freeLayout="true">
			<%--adsm:button caption="limparSessao" id="limparSessaoButton" onclick="return limparSessao();" disabled="false"/--%>
			<adsm:button caption="salvarAWB" id="salvarButton" onclick="return salvarAwb();" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
		property="awb"
		idProperty="idAwb"
		detailFrameName="awb"
		rows="12"
		gridHeight="250"
		unique="true"
		service="lms.expedicao.emitirRelacaoCargaAereaAction.findPaginatedAwb"
		rowCountService="lms.expedicao.emitirRelacaoCargaAereaAction.getRowCountAwb"
		autoSearch="false"
		onRowClick="desabilitaDetalhamento">

		<adsm:gridColumn title="awb" property="dvNrAwb" width="25%" align="left"/>
		<adsm:gridColumn title="dataHoraDeEmissao" dataType="JTDateTimeZone" property="dhEmissao" width="25%" align="center"/>
		<adsm:gridColumn title="vooPrevisto" property="dsVooPrevisto" width="25%"/>

		<adsm:gridColumnGroup customSeparator=" ">
				<adsm:gridColumn property="sgMoeda" dataType="text" title="totalFrete" width="30"/>
				<adsm:gridColumn property="dsSimbolo" dataType="text" title="" width="30"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" dataType="currency" property="vlFrete" width="80" align="right"/>

		<adsm:buttonBar>
			<adsm:removeButton caption="excluirAWB"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script language="javascript" type="text/javascript">
	document.getElementById("awb.nrAwb").required = "true";

	function limparSessao_cb(dados, error) {
		if(error) {
			alert(error);
			return;
		}
	}

	function limparSessao() {
		var sdo = createServiceDataObject("lms.expedicao.emitirRelacaoCargaAereaAction.removeAWBs", "limparSessao", {});
		xmit({serviceDataObjects:[sdo]});
	}

	function salvarAwb() {
		if(validateTabScript(document.forms)) {
			var idAwb = getElementValue("awb.idAwb");
			var nrAwb = getElementValue("awb.nrAwb");
			var dvAwb = getElementValue("awb.dvAwb");
			var dhEmissao = getElementValue("awb.dhEmissao");
			var dsVooPrevisto = getElementValue("awb.dsVooPrevisto");
			var sgMoeda = getElementValue("moeda.sgMoeda");
			var dsSimbolo = getElementValue("moeda.dsSimbolo");
			var vlFrete = getElementValue("awb.vlFrete");
			var sgEmpresa = getElementValue("empresa.sgEmpresa");
	
			var sdo = createServiceDataObject("lms.expedicao.emitirRelacaoCargaAereaAction.salvaAwbSessao", "salvarAwb", {idAwb:idAwb, nrAwb:nrAwb, dvAwb:dvAwb, dhEmissao:dhEmissao, dsVooPrevisto:dsVooPrevisto, sgMoeda:sgMoeda, dsSimbolo:dsSimbolo, vlFrete:vlFrete, sgEmpresa:sgEmpresa});
			xmit({serviceDataObjects:[sdo]});
		}
	}

	function salvarAwb_cb(dados,erro){
		if(erro!=undefined){
			alert(erro);
			return;
		}
		// Atualiza a Grid para mostrar o novo Awb
		awbGridDef.executeSearch({}, true);
		newButtonScript(document);
		copyEmpresa();
		setFocus(document.getElementById("awb.nrAwb"));
	}

	var idFilialUsuario;
	function ajustaFilial_cb(dados, error) {
		if(error) {
			alert(error);
			return;
		}
		idFilialUsuario = getNestedBeanPropertyValue(dados, "idFilialUsuarioLogado");
	}
	
	function oplcb_cb(){
		var sdo = createServiceDataObject("lms.expedicao.emitirRelacaoCargaAereaAction.findFilialUsuarioLogado", "ajustaFilial", {});
		xmit({serviceDataObjects:[sdo]});
	}
	
	function desabilitaDetalhamento(data){
		return false;
	}

	function validadeAwb(data) {
		var nrAwb = data.nrAwb;
		if(nrAwb != "") {
			var sdo = createServiceDataObject("lms.expedicao.emitirRelacaoCargaAereaAction.validadeAwb", "validadeAwb", {nrAwb:nrAwb});
			xmit({serviceDataObjects:[sdo]});
		}
	}

	function validadeAwb_cb(data, error) {
		if(error) {
			newButtonScript(document);
			alert(error);
			return false;
		}
		return true;
	}
</script>