<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script type="text/javascript">
<!--
	function dataLoad_cb(data) {
		onDataLoad_cb(data);
		writeDataSession();
	}
	
	//Implementação da filial do usuário logado como padrão
	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	
	function dataSession_cb(data) {
		idFilial = getNestedBeanPropertyValue(data,"filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"filial.sgFilial");
		nmFilial = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
		writeDataSession();
	}
	 
	function writeDataSession() {
		if (idFilial != null &&
			sgFilial != null &&
			nmFilial != null) {
			setElementValue("filial.idFilial",idFilial);
			setElementValue("filial.sgFilial",sgFilial);
			setElementValue("filial.pessoa.nmFantasia",nmFilial);
		}
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click")
			writeDataSession();
	}
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.simularAplicarReajustesTabelasFreteHistoricoAction.findDataSession","dataSession",null);
		xmit({serviceDataObjects:[sdo]});
	}
	
//-->
</script>
<adsm:window title="simularAplicarReajustesTabelasFreteHistorico" service="lms.fretecarreteirocoletaentrega.simularAplicarReajustesTabelasFreteHistoricoAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/freteCarreteiroColetaEntrega/simularAplicarReajustesTabelasFreteHistorico" onDataLoadCallBack="dataLoad">
		<adsm:hidden property="tpMeioTransporte" value="R" serializable="false"/>

		<adsm:lookup property="filial" labelWidth="18%" dataType="text"
			maxLength="3" action="municipios/manterFiliais"
			service="lms.fretecarreteirocoletaentrega.simularAplicarReajustesTabelasFreteHistoricoAction.findLookupFilial"
			label="filial" idProperty="idFilial" criteriaProperty="sgFilial"
			required="true" width="32%" size="3" disabled="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia"
				relatedProperty="filial.pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia"
				size="30" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:range label="periodoSimulacao" labelWidth="18%" width="80%">
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial" />
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal" />
		</adsm:range>

		<adsm:combobox
			property="tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega"
			label="tipoTabela" labelWidth="18%" boxWidth="180"
			service="lms.fretecarreteirocoletaentrega.simularAplicarReajustesTabelasFreteHistoricoAction.findComboTipoTabelaColetaEntrega"
			optionProperty="idTipoTabelaColetaEntrega"
			optionLabelProperty="dsTipoTabelaColetaEntrega" width="80%" />

		<adsm:combobox property="tipoMeioTransporte.idTipoMeioTransporte"
			boxWidth="180" cellStyle="vertical-align:bottom;" labelWidth="18%"
			optionProperty="idTipoMeioTransporte"
			optionLabelProperty="dsTipoMeioTransporte" label="tipoMeioTransporte"
			width="80%"
			service="lms.fretecarreteirocoletaentrega.simularAplicarReajustesTabelasFreteHistoricoAction.findComboTipoMeioTransporte"/>


		<adsm:lookup dataType="text" property="meioTransporteRodoviario2" idProperty="idMeioTransporte" criteriaProperty="meioTransporte.nrFrota"
			service="lms.fretecarreteirocoletaentrega.simularAplicarReajustesTabelasFreteHistoricoAction.findLookupMeioTransporteRodoviario"
			picker="false" action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" label="meioTransporte" labelWidth="18%" width="8%"
			size="7" serializable="false" cellStyle="vertical-align:bottom" maxLength="6">

			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte" modelProperty="idMeioTransporte" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping criteriaProperty="tpMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte"/>
			<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"/>

		</adsm:lookup>

		<adsm:lookup dataType="text" property="meioTransporteRodoviario" idProperty="idMeioTransporte" size="25" cellStyle="vertical-align:bottom"
			service="lms.fretecarreteirocoletaentrega.simularAplicarReajustesTabelasFreteHistoricoAction.findLookupMeioTransporteRodoviario"
			action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador" width="69%" maxLength="25">

			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota"/>
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte" modelProperty="idMeioTransporte"/>
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota"/>
			<adsm:propertyMapping criteriaProperty="tpMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte"/>
			<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"/>

		</adsm:lookup> 

		<adsm:combobox property="blPercentual" label="tipoSimulacao" domain="DT_TIPO_SIMULACAO_REAJUSTE" labelWidth="18%" width="30%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="ParamSimulacaoHistorica" />
			<adsm:resetButton />
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid property="ParamSimulacaoHistorica" idProperty="idParamSimulacaoHistorica" unique="true" rows="8">
		<adsm:gridColumn title="descricao" property="dsParamSimulacaoHistorica" />
		<adsm:gridColumn width="20%" title="dataSimulacao" property="dhCriacao" dataType="JTDateTimeZone" />
		<adsm:gridColumn width="20%" title="tipoTabela" property="tipoTabelaColetaEntrega.dsTipoTabelaColetaEntrega" />
		<adsm:gridColumn width="20%" title="tipoSimulacao" property="tpSimulacao" isDomain="true" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>