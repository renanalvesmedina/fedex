<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.visualizarPrealertasAction">

	<adsm:form id="formPreAlerta" action="/expedicao/visualizarPrealertas">
		<adsm:hidden property="idEmpresa" serializable="true"/>
		<adsm:hidden property="idAeroporto" serializable="true"/>
		<adsm:lookup
			action="/municipios/manterAeroportos"
			service="lms.expedicao.digitarAWBCiasAereasAction.findAeroporto"
			dataType="text"
			property="aeroportoByIdAeroportoDestino"
			idProperty="idAeroporto"
			criteriaProperty="sgAeroporto"
			serializable="false"
			label="aeroportoDestino"
			size="3"
			required="true"
			maxLength="3"
			labelWidth="17%"
			width="33%">
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="aeroportoByIdAeroportoDestino.pessoa.nmPessoa"/>
			<adsm:propertyMapping
				modelProperty="idAeroporto"
				relatedProperty="idAeroporto"/>
			<adsm:textbox
				dataType="text"
				property="aeroportoByIdAeroportoDestino.pessoa.nmPessoa" 
				serializable="false"
				size="25"
				maxLength="45"
				disabled="true"/>
		</adsm:lookup>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="preAlerta"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
		idProperty="idPreAlerta" 
		property="preAlerta"
		onRowClick="showDetailsAWB"
		rows="13"
		gridHeight="250"
		unique="true"
		scrollBars="horizontal" >
		<adsm:gridColumn title="numeroPrealerta" property="nrPreAlerta" width="120" align="right" />
		<adsm:gridColumn title="filialOrigem" property="awb.filialByIdFilialOrigem.sgFilial" width="100" />
		<adsm:gridColumn title="aeroporto" property="awb.aeroportoByIdAeroportoOrigem.sgAeroporto" width="70" />
		<adsm:gridColumn title="awb" property="awbFormatado" width="120" align="left" />
		<adsm:gridColumn title="numeroVoo" property="dsVoo" width="80"/>
		<adsm:gridColumn title="dataPrevistaSaida" property="dhSaida" dataType="JTDateTimeZone" width="140" align="center"/>
		<adsm:gridColumn title="dataPrevistaChegada" property="dhChegada" dataType="JTDateTimeZone" width="160" align="center"/>
		<adsm:gridColumn title="confirmado" property="blVooConfirmado" renderMode="image-check" width="80" />
		<adsm:gridColumn title="qtdeVolumes" property="awb.qtVolumes" width="110" align="right"/>
		<adsm:gridColumn title="peso" property="awb.psTotal" width="70" unit="kg" align="right" dataType="decimal" mask="#,###,###,###,##0.000"/>
		<adsm:gridColumn title="pesoCubado" property="awb.psCubado" width="120" unit="kg" align="right" dataType="decimal" mask="#,###,###,###,##0.000"/>

		<adsm:buttonBar>
			<adsm:button 
				caption="confirmar"
				onclick="recebimentoPreAlerta();"
				buttonType="removeButtonGrid"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script type="text/javascript">
	function initWindow(eventObj) {
		defaultParameters();
	}

	/*
	* Dados Default da Tela.
	*/
	function defaultParameters() {
		var sdo = createServiceDataObject("lms.expedicao.visualizarPrealertasAction.findDefaultParameters", "defaultParameters", {});
		xmit({serviceDataObjects:[sdo]});
	}

	function defaultParameters_cb(data,erro) {
		if(erro!=undefined) {
			alert(erro);
			return;
		}
		/* Aeroporto do Usuario */
		setElementValue("idAeroporto", data.idAeroporto);
		setElementValue("aeroportoByIdAeroportoDestino.idAeroporto", data.idAeroporto)
		setElementValue("aeroportoByIdAeroportoDestino.sgAeroporto", data.sgAeroporto)
		setElementValue("aeroportoByIdAeroportoDestino.pessoa.nmPessoa", data.nmAeroporto)
	}

	function showDetailsAWB(pk) {
		var preAlerta = preAlertaGridDef.getDataRowById(pk);
		var nrAwb = preAlerta.awb;
		var idPessoa = preAlerta.idEmpresa;

		parent.parent.redirectPage('expedicao/consultarAWBs.do?cmd=main&nrAwb='+nrAwb+'&ciaFilialMercurio.empresa.idEmpresa='+idPessoa);
		return false;
	}

	function recebimentoPreAlerta() {
		var filter = preAlertaGridDef.getSelectedIds();
		var sdo = createServiceDataObject("lms.expedicao.visualizarPrealertasAction.recebimentoPreAlerta", "recebimentoPreAlerta", filter);
		xmit({serviceDataObjects:[sdo]});
	}

	function recebimentoPreAlerta_cb(data, error) {
		store_cb(data, error);
		findButtonScript('preAlerta', getElement("formPreAlerta"));
	}
</script>