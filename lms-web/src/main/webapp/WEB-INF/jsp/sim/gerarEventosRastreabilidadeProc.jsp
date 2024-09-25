<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/sim/gerarEventosRastreabilidade" id="form">
				
			<adsm:combobox label="documentoServico" labelWidth="18%" width="77%"  required="true"
					   property="tpDocumentoServico" 
					   service="lms.sim.gerarEventosRastreabilidadeAction.findTipoDocumentoServico"
					   optionProperty="value" optionLabelProperty="description"	
					   onchange="var r = changeDocumentWidgetType({
						   documentTypeElement:this, 
						   filialElement:document.getElementById('filialByIdFilialOrigem.idFilial'), 
						   documentNumberElement:document.getElementById('conhecimento.idConhecimento'), 
						   documentGroup:'DOCUMENTO',
						   actionService:'lms.sim.gerarEventosRastreabilidadeAction'
					   }); habilitaDesabilitaFilial(); return r; ">

		
			<adsm:lookup dataType="text"
						 property="filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service=""  
						 action="/municipios/manterFiliais" 
						 size="3" maxLength="3" picker="false" disabled="true" serializable="true"
						 onchange="var r = changeDocumentWidgetFilial({
							 filialElement:document.getElementById('filialByIdFilialOrigem.idFilial'), 
							 documentNumberElement:document.getElementById('conhecimento.idConhecimento')
							 }); return r;"
						 />
			
			<adsm:lookup dataType="integer" 
						 property="conhecimento" 
						 idProperty="idConhecimento" 
						 criteriaProperty="nrConhecimento"
						 popupLabel="pesquisarDocumentoServico"		
						 onPopupSetValue="popupDoctoServico"		 
						 service="" 
						 action="" 
						 size="10" maxLength="8" mask="00000000" serializable="true" disabled="true" />																		 
						 
			</adsm:combobox>
			
			
		<adsm:hidden property="filialByIdFilialOrigem.sgFilial"/>
		<adsm:hidden property="filialByIdFilialOrigem.pessoa.nmFantasia"/>
			
		<adsm:hidden property="nrDocumento"/>
		<adsm:hidden property="tpMic_mic" value="M" serializable="false"/>
		<adsm:hidden property="tpMic_mid" value="D" serializable="false"/>
		<adsm:hidden property="tpDocumento_ctr" value="CTR" serializable="false"/>
		<adsm:hidden property="tpDocumento_ntt" value="NTT" serializable="false"/>
		<adsm:hidden property="empresa.tpEmpresa" value="M" serializable="false"/>
		
		<adsm:lookup property="evento" service="lms.sim.gerarEventosRastreabilidadeAction.findLookupEventos" 
		 			action="/sim/manterEventosDocumentosServico" dataType="integer" idProperty="idEvento" criteriaProperty="cdEvento" serializable="true"
					label="evento" width="9%" size="5" labelWidth="18%" required="true" maxLength="3">
				<adsm:propertyMapping relatedProperty="evento.dsEvento" modelProperty="dsEvento"/>
				<adsm:propertyMapping relatedProperty="evento.codigoEvento" modelProperty="cdEvento"/>
				<adsm:textbox dataType="text" width="40%" size="50" disabled="true" serializable="false" property="evento.dsEvento"/>
				<adsm:hidden property="evento.codigoEvento"/>
		</adsm:lookup>
		
		<adsm:textbox dataType="JTDateTimeZone" property="dhEvento" label="dataHoraEvento" width="20%" labelWidth="18%" required="true"/>

		<adsm:combobox property="dsTimezone" label="fusoHorario" labelWidth="18%" width="29%" service="lms.sim.gerarEventosRastreabilidadeAction.findTimeZones" optionLabelProperty="value" optionProperty="value" required="true" onDataLoadCallBack="timeZonesLoad" />
		
		<adsm:textarea maxLength="600" property="dsObservacao" label="complemento" width="82%" labelWidth="18%" rows="2" columns="80"/>
		
   		<adsm:textbox dataType="JTDateTimeZone" property="dhInclusao" label="dataHoraInclusao" width="20%" labelWidth="18%" disabled="true" />
   		
		<adsm:textbox dataType="text" property="usuarioInclusao" label="usuarioInclusao" width="40%" size="45" labelWidth="18%" required="true" disabled="true"/>

		<adsm:buttonBar>
			<adsm:button caption="incluir" id="incluir" service="lms.sim.gerarEventosRastreabilidadeAction.execute" callbackProperty="incluir"/>
			<adsm:resetButton id="resetButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	
	function incluir_cb(data, exception){
		if (exception != null)
			alert(exception);
		else {
			showSuccessMessage();
			setFocus(document.getElementById("resetButton"), false);
		}
	}
	
	function initWindow(evt){
		setDisabled("incluir", false);
		setDisabled("filialByIdFilialOrigem.idFilial", true);
		setDisabled("conhecimento.idConhecimento", true);	
		loadUsuarioLogado();
		setDefaultTimeZone();
	}

	function loadUsuarioLogado(){
		var data = new Array();
		var sdo = createServiceDataObject("lms.sim.gerarEventosRastreabilidadeAction.findDadosUsuarioLogado",
				"preencheUsuario",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function preencheUsuario_cb(data){
		setElementValue("usuarioInclusao", getNestedBeanPropertyValue(data,"usuarioInclusao"));
		setElementValue("dhInclusao",getNestedBeanPropertyValue(data,"dhInclusao"));
	}
	
	function popupDoctoServico(data){
		setDisabled("conhecimento.idConhecimento", false);
	}
	
	function habilitaDesabilitaFilial(){
		if (getElementValue("tpDocumentoServico") == "PED" ||
			getElementValue("tpDocumentoServico") == "PEI" ||
			getElementValue("tpDocumentoServico") == "AWB") {
			
			setDisabled("filialByIdFilialOrigem.idFilial", true);
			setDisabled("conhecimento.idConhecimento", false);	
		}
	}
	
	function timeZonesLoad_cb(data,exception) {
		if (exception != undefined) {
			alert(exception);
			return;
		}
		dsTimezone_cb(data,exception);
		setDefaultTimeZone();
	}
	
	function setDefaultTimeZone() {
		var sdo = createServiceDataObject("lms.sim.gerarEventosRastreabilidadeAction.getDefaultTimeZone","setDefaultTimeZoneCallback",{});
		xmit({serviceDataObjects:[sdo]});
	}
	
	function setDefaultTimeZoneCallback_cb(data, exception) {
		if (exception != undefined) {
			alert(exception);
			return;
		}
		setElementValue("dsTimezone", getNestedBeanPropertyValue(data,"dsTimezone"));
	}
	
</script>
