<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="emitirOciosidadeFrota" onPageLoadCallBack="pageLoad">
	<adsm:form action="/contratacaoVeiculos/emitirOciosidadeFrota">
		<adsm:i18nLabels>
			<adsm:include key="LMS-00013"/>
		</adsm:i18nLabels>

		<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
			service="lms.contratacaoveiculos.emitirOciosidadeFrotaAction.findFilial" dataType="text" label="filial" size="3"
			action="/municipios/manterFiliais" labelWidth="17%" width="80%" minLengthForAutoPopUpSearch="3"
			exactMatch="false" style="width:45px" disabled="false"
		>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="filial.siglaNomeFilial" modelProperty="siglaNomeFilial" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" />			
			<adsm:hidden property="filial.empresa.tpEmpresa" serializable="false" value="M"/>
			<adsm:propertyMapping criteriaProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:hidden property="filial.siglaNomeFilial"/>	
		</adsm:lookup>

		<adsm:combobox property="tpVinculo" label="tipoVinculo" domain="DM_TIPO_VINCULO_VEICULO" labelWidth="17%" width="83%" >
			<adsm:propertyMapping relatedProperty="dsTipoVinculo" modelProperty="description"/>
			<adsm:hidden property="dsTipoVinculo"/>
		</adsm:combobox>

		<adsm:lookup
			dataType="text"
			property="meioTransporteRodoviarioByIdMeioTransporte2"
			idProperty="idMeioTransporte"
			labelWidth="17%"
			service="lms.contratacaoveiculos.emitirOciosidadeFrotaAction.findMeioTransporteRodoviario"
			picker="false" cellStyle="vertical-align=bottom;"
			action="/contratacaoVeiculos/manterMeiosTransporte"
			cmd="rodo"
			criteriaProperty="meioTransporte.nrFrota"
			label="meioTransporte"
			width="33%"
			size="8"
			serializable="false"
			maxLength="6"
		>
			<adsm:propertyMapping
				criteriaProperty="meioTransporteRodoviarioByIdMeioTransporte.meioTransporte.nrIdentificador"
				modelProperty="meioTransporte.nrIdentificador"/>
			<adsm:propertyMapping
				relatedProperty="meioTransporteRodoviarioByIdMeioTransporte.idMeioTransporte"
				modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping
				relatedProperty="meioTransporteRodoviarioByIdMeioTransporte.meioTransporte.nrIdentificador"
				modelProperty="meioTransporte.nrIdentificador" />	
			<adsm:propertyMapping relatedProperty="nrIdentificador" modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="nrFrota"	modelProperty="meioTransporte.nrFrota" />
			<adsm:hidden property="nrIdentificador"/>

				<adsm:lookup
					dataType="text"
					property="meioTransporteRodoviarioByIdMeioTransporte"
					idProperty="idMeioTransporte"
					cellStyle="vertical-align=bottom;"
					service="lms.contratacaoveiculos.emitirOciosidadeFrotaAction.findMeioTransporteRodoviario"
					picker="true"
					maxLength="25"
					action="/contratacaoVeiculos/manterMeiosTransporte"
					cmd="rodo"
					criteriaProperty="meioTransporte.nrIdentificador"
					size="20"
				>
					<adsm:propertyMapping criteriaProperty="meioTransporteRodoviarioByIdMeioTransporte2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />
					<adsm:propertyMapping relatedProperty="meioTransporteRodoviarioByIdMeioTransporte2.idMeioTransporte"
						modelProperty="idMeioTransporte" />	
					<adsm:propertyMapping relatedProperty="meioTransporteRodoviarioByIdMeioTransporte2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />		
					<adsm:propertyMapping relatedProperty="nrIdentificador" modelProperty="meioTransporte.nrIdentificador" />
					<adsm:propertyMapping relatedProperty="nrFrota"	modelProperty="meioTransporte.nrFrota" />
					<adsm:hidden property="nrFrota"/>
				</adsm:lookup>
		</adsm:lookup>

		<adsm:combobox property="tpEvento" label="evento" domain="DM_EVENTO_MEIO_TRANSPORTE" labelWidth="17%" width="83%">
			<adsm:propertyMapping relatedProperty="dsEvento" modelProperty="description"/>
			<adsm:hidden property="dsEvento"/>
		</adsm:combobox>

		<adsm:textbox dataType="JTTime" mask="hhh:mm" property="hrTempoMinimo" size="6" maxLength="6" label="tempoMinimoNoEvento" labelWidth="17%" width="70%" unit="h" />

		<adsm:range label="periodo" labelWidth="17%" width="83%" required="true">
			<adsm:textbox dataType="JTDate" property="dtPeriodoInicial"/>
			<adsm:textbox dataType="JTDate" property="dtPeriodoFinal"/>
		</adsm:range>
		<adsm:combobox
			label="formatoRelatorio"
			labelWidth="17%"
			property="tpFormatoRelatorio"
			domain="DM_FORMATO_RELATORIO"
			defaultValue="pdf"
			required="true"
		/>
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contratacaoveiculos.emitirOciosidadeFrotaAction" />
			<adsm:resetButton/>
		</adsm:buttonBar>	
	</adsm:form>
</adsm:window>

<script language="javascript" type="text/javascript">
	var idFilialLogado;
	var sgFilialLogado;
	var nmFilialLogado;

	function pageLoad_cb(){
		onPageLoad_cb();
		getFilialUsuario();	
	}

	function initWindow(){
		setaValoresFilial();
	}

	function validateTab() {
		if (validateTabScript(document.forms)){
			if (getElementValue("filial.idFilial") == "" &&
				getElementValue("meioTransporteRodoviarioByIdMeioTransporte.idMeioTransporte") == "" &&
				getElementValue("tpEvento") == "" ){
				alert(i18NLabel.getLabel("LMS-00013")
							+ document.getElementById("filial.idFilial").label + ', ' 
							+ document.getElementById("meioTransporteRodoviarioByIdMeioTransporte.idMeioTransporte").label + ', ' 
							+ document.getElementById("tpEvento").label + ".");
				return false;
			}else return true; 
		}
		return false;
	}

	function setaValoresFilial() {
		setElementValue("filial.idFilial", idFilialLogado);
		setElementValue("filial.sgFilial", sgFilialLogado);
		setElementValue("filial.pessoa.nmFantasia", nmFilialLogado);		
		setElementValue("filial.siglaNomeFilial", sgFilialLogado + ' - ' + nmFilialLogado);
	}

	function getFilialUsuario() {
		var sdo = createServiceDataObject("lms.contratacaoveiculos.emitirOciosidadeFrotaAction.findFilialSessao","getFilialCallBack",null);
		xmit({serviceDataObjects:[sdo]});
	}

	function getFilialCallBack_cb(data,error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		if (data != undefined) {
			idFilialLogado = getNestedBeanPropertyValue(data,"idFilial");
			sgFilialLogado = getNestedBeanPropertyValue(data,"sgFilial");
			nmFilialLogado = getNestedBeanPropertyValue(data,"pessoa.nmFantasia");
			setaValoresFilial();
		}
	}
</script>