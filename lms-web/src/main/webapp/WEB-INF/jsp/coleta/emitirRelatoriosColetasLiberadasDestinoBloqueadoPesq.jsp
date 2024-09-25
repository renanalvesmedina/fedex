<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.coleta.emitirRelatoriosColetasLiberadasDestinoBloqueadoAction">
	<adsm:form action="/coleta/emitirRelatoriosColetasLiberadasDestinoBloqueado">

		<adsm:range label="periodo" width="85%" required="true">
			<adsm:textbox dataType="JTDate" property="dhInicial"/>
			<adsm:textbox dataType="JTDate" property="dhFinal"/>
		</adsm:range>
		
		<adsm:lookup property="municipio" idProperty="idMunicipio" criteriaProperty="nmMunicipio" dataType="text" 
					 service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupMunicipio" action="/municipios/manterMunicipios" 
					 label="municipio" size="60" maxLength="60" width="85%" minLengthForAutoPopUpSearch="3" exactMatch="false" criteriaSerializable="true"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.coleta.emitirRelatoriosColetasLiberadasDestinoBloqueadoAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

	function initWindow(eventObj) {
		if (eventObj.name == "tab_load"){
			loadBasicData();
		} else if (eventObj.name == "cleanButton_click"){
			loadBasicData();
		}
	}
	
	/**
	 * Carrega dados basicos da tela.
	 */
	function loadBasicData() {
		var data = new Object();
		
		var sdo = createServiceDataObject("lms.coleta.emitirRelatoriosColetasLiberadasDestinoBloqueadoAction.getBasicData", "loadBasicData", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Callback da funcao anterior
	 *
	 * @param data
	 * @param error
	 */
	function loadBasicData_cb(data, error) {
		setElementValue("dhInicial", setFormat(document.getElementById("dhInicial"), data.dhInicial));
		setElementValue("dhFinal", setFormat(document.getElementById("dhFinal"), data.dhFinal));
			
		setElementValue("municipio.idMunicipio", data.idMunicipio);
		setElementValue("municipio.nmMunicipio", data.nmMunicipio);
	}

</script>