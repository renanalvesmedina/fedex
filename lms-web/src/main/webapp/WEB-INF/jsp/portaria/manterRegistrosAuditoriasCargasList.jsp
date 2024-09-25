<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterRegistrosAuditoriasCargas" service="lms.portaria.manterRegistrosAuditoriasCargasAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/portaria/manterRegistrosAuditoriasCargas" height="122" idProperty="idRegistroAuditoria" >
						
		<adsm:hidden property="idProcessoWorkflow"/>
		
		<adsm:lookup service="lms.portaria.manterRegistrosAuditoriasCargasAction.findLookupFilial" dataType="text"
				property="filial" criteriaProperty="sgFilial" label="filial" size="3" maxLength="3"
				width="82%" labelWidth="18%" action="/municipios/manterFiliais" idProperty="idFilial" disabled="true"
				onchange="return filialOnChange(this)" exactMatch="true"
				required="true">
			<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
        	<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
        	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
        	<adsm:hidden property="controleCarga.filialByIdFilialOrigem.empresa.tpEmpresa" value="M" serializable="false"/>
        </adsm:lookup>
		
		<adsm:textbox dataType="integer" property="nrRegistroAuditoria" label="numeroRegistro" mask="0000000000" size="10" 
					  maxLength="8" labelWidth="18%" width="32%" cellStyle="vertical-Align:bottom;" />	
 
		<adsm:range label="dataRegistro" labelWidth="18%" width="30%" >
			<adsm:textbox dataType="JTDate" property="dtRegistroInicial" cellStyle="vertical-align:bottom;"/> 
			<adsm:textbox dataType="JTDate" property="dtRegistroFinal" cellStyle="vertical-align:bottom;"/>
		</adsm:range>

		
		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.nmFilial" />
        <adsm:lookup dataType="text" 
			        property="controleCarga.filialByIdFilialOrigem"  
			        idProperty="idFilial" 
			        criteriaProperty="sgFilial" 
					service="lms.portaria.manterRegistrosAuditoriasCargasAction.findLookupFilial" 
					action="/municipios/manterFiliais" 					
					label="controleCargas" 
					width="82%" 
					labelWidth="18%" 
					size="3" 
					maxLength="3" 
					picker="false"
					popupLabel="pesquisarFilial"					
					serializable="false"
					onDataLoadCallBack="filialCCDataLoad"	
					onPopupSetValue="filialCCPopup"
					onchange="return sgFilialOnChangeHandler(this)">			
			<adsm:lookup dataType="integer" 
						 property="controleCarga" 
						 idProperty="idControleCarga" 
						 criteriaProperty="nrControleCarga" 
						 service="lms.portaria.manterRegistrosAuditoriasCargasAction.findLookupControleCarga" 
						 action="/carregamento/manterControleCargas" 
						 cmd="list" mask="00000000"	popupLabel="pesquisarControleCarga"						 
						 maxLength="8" size="8" 
						 disabled="true">

				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" inlineQuery="false"/>
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" />
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" criteriaProperty="controleCarga.filialByIdFilialOrigem.nmFilial" inlineQuery="false"/>
	        	<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota" modelProperty="meioTransporteByIdTransportado.nrFrota"/>
	        	<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador" modelProperty="meioTransporteByIdTransportado.nrIdentificador"/>
	        	<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte" modelProperty="meioTransporteByIdTransportado.idMeioTransporte"/>
	        	<adsm:propertyMapping relatedProperty="semiReboque2.meioTransporte.nrFrota" modelProperty="meioTransporteByIdSemiRebocado.nrFrota"/>
	        	<adsm:propertyMapping relatedProperty="semiReboque.meioTransporte.nrIdentificador" modelProperty="meioTransporteByIdSemiRebocado.nrIdentificador"/>
	        	<adsm:propertyMapping relatedProperty="semiReboque.idMeioTransporte" modelProperty="meioTransporteByIdSemiRebocado.idMeioTransporte"/>
			</adsm:lookup>		
			<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:propertyMapping relatedProperty="controleCarga.filialByIdFilialOrigem.nmFilial"
					modelProperty="pessoa.nmFantasia" />
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporteRodoviario2" idProperty="idMeioTransporte"
				service="lms.portaria.manterRegistrosAuditoriasCargasAction.findLookupMeioTransp" picker="false"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota"
				label="meioTransporte" labelWidth="18%"
				width="32%" size="8" serializable="false" maxLength="6" cellStyle="vertical-Align:bottom" >
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte"
					modelProperty="idMeioTransporte" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />		
		  
			<adsm:lookup dataType="text" property="meioTransporteRodoviario" idProperty="idMeioTransporte"
					service="lms.portaria.manterRegistrosAuditoriasCargasAction.findLookupMeioTransp" picker="true" maxLength="25"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador"
					size="20" cellStyle="vertical-Align:bottom" >
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte"
						modelProperty="idMeioTransporte" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />
			</adsm:lookup>
        </adsm:lookup>
        
		<adsm:lookup dataType="text" property="semiReboque2" idProperty="idMeioTransporte"
				service="lms.portaria.manterRegistrosAuditoriasCargasAction.findLookupMeioTransp" picker="false"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota"
				label="semiReboque" labelWidth="18%"
				width="32%" size="8" serializable="false" maxLength="6" cellStyle="vertical-Align:bottom" >
			<adsm:propertyMapping criteriaProperty="semiReboque.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="semiReboque.idMeioTransporte"
					modelProperty="idMeioTransporte" />
			<adsm:propertyMapping relatedProperty="semiReboque.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />		
		
			<adsm:lookup dataType="text" property="semiReboque" idProperty="idMeioTransporte"
					service="lms.portaria.manterRegistrosAuditoriasCargasAction.findLookupMeioTransp" picker="true" maxLength="25"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador"
					size="20" cellStyle="vertical-Align:bottom" >
				<adsm:propertyMapping criteriaProperty="semiReboque2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping relatedProperty="semiReboque2.idMeioTransporte"
						modelProperty="idMeioTransporte" />
				<adsm:propertyMapping relatedProperty="semiReboque2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />
			</adsm:lookup>
        </adsm:lookup>

		<adsm:combobox property="tpResultado" domain="DM_RESULTADO_AUDITORIA" label="resultado" labelWidth="18%" width="32%" />
		
		<adsm:combobox property="meioTransporteLiberado" label="meioTransporteLiberado"  domain="DM_SIM_NAO" labelWidth="18%" width="32%" cellStyle="vertical-Align:bottom;" />
		
		<adsm:range label="dataLiberacao" labelWidth="18%" width="82%" >
			<adsm:textbox dataType="JTDate" property="dtLiberacaoInicial" /> 
			<adsm:textbox dataType="JTDate" property="dtLiberacaoFinal"/>
		</adsm:range>
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" buttonType="findButton" onclick="consultar()" />
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="registroAuditoria" idProperty="idRegistroAuditoria"  selectionMode="none" unique="true" 
			  rows="8" scrollBars="horizontal" gridHeight="161" 
			  rowCountService="lms.portaria.manterRegistrosAuditoriasCargasAction.getRowCountCustom" service="lms.portaria.manterRegistrosAuditoriasCargasAction.findPaginatedCustom">
		<adsm:gridColumnGroup customSeparator=" "> 
			<adsm:gridColumn property="filial.sgFilial"   width="80"  title="numeroRegistro"/>
			<adsm:gridColumn width="70" title="" dataType="integer" mask="00000000" property="nrRegistroAuditoria" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn width="150" title="dataHoraRegistro" property="dhRegistroAuditoria" dataType="JTDateTimeZone" align="center" />
		
		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA">
			<adsm:gridColumn width="80" title="controleCarga" property="controleCarga.filialByIdFilialOrigem.sgFilial" align="left"/>				
			<adsm:gridColumn width="70" title="" property="controleCarga.nrControleCarga" dataType="integer" mask="00000000"/>
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="meioTransporte" property="meioTransporteRodoviario.nrFrota" width="65"/>
		<adsm:gridColumn title="" property="meioTransporteRodoviario.nrIdentificador" width="65" />
		<adsm:gridColumn title="semiReboque" property="semiReboque.nrFrota" width="65"/>
		<adsm:gridColumn title="" property="semiReboque.nrIdentificador"  width="65" />
		<adsm:gridColumn width="100" title="resultado" property="tpResultado" isDomain="true" />
		<adsm:gridColumn width="190" title="meioTransporteLiberado" property="meioTransporteLiberado" align="center" renderMode="image-check" />
		<adsm:gridColumn width="120" title="dataHoraLiberacao" property="dhLiberacao" dataType="JTDateTimeZone" align="center" />		
		<adsm:buttonBar>
			<%--adsm:removeButton/ EXCLUIDO CONFORME SANDRA --%>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	var idFilial;
	var sgFilial;
	var nmFilial;


	function consultar(){
		var tab = getTab(document);
		
		if (tab.validate({name:"findButton_click"})) {
			if (getElementValue("controleCarga.idControleCarga") != "" ||
				getElementValue("meioTransporteRodoviario.idMeioTransporte") != "" ||
				getElementValue("semiReboque.idMeioTransporte") != "" ||
				getElementValue("nrRegistroAuditoria") != "" ||
				(getElementValue("dtRegistroInicial") != "" && getElementValue("dtRegistroFinal") != "") ||		
				(getElementValue("dtLiberacaoInicial") != "" && getElementValue("dtLiberacaoFinal") != "")) {
				
				findButtonScript('registroAuditoria', document.getElementById("form_idRegistroAuditoria"))
				
			} else {
				
				var msg = parent.i18NLabel.getLabel("LMS-00013") + " " +
						  parent.i18NLabel.getLabel("numeroRegistro") + ", " +	
						  parent.i18NLabel.getLabel("controleCarga") + ", " +
						  parent.i18NLabel.getLabel("meioTransporte") + ", " +
						  parent.i18NLabel.getLabel("semiReboque") + ", " +
						  parent.i18NLabel.getLabel("dataRegistro") + ", " +
						  parent.i18NLabel.getLabel("dataLiberacao") + ".";
	
				alert(msg);
			}
		}
	}

	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		if (document.getElementById("idProcessoWorkflow").value == "") {		
			findFilialUsuario();
		}
	}
	
	function initWindow(evt){
		if (evt.name == 'cleanButton_click' || evt.name == 'tab_load') {
			setDisabled("controleCarga.idControleCarga", true);
		}
		
		if (evt.name == 'cleanButton_click'){
			setaValoresFilial();
		}
	}
	
	function findFilialUsuario() {
		var data = new Array();
		var sdo = createServiceDataObject("lms.portaria.manterRegistrosAuditoriasCargasAction.findFilialUsuario", "findFilialUsuario", data);
		xmit({serviceDataObjects:[sdo]});
	}
		
	function findFilialUsuario_cb(data,error) {

		if (error != undefined) {
			alert(error);
			return false;
		}
		
		if (data != undefined) {
			idFilial = getNestedBeanPropertyValue(data,"filial.idFilial");
			sgFilial= getNestedBeanPropertyValue(data,"filial.sgFilial");
			nmFilial = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
			setaValoresFilial();
		}
	}
	
	function setaValoresFilial() {	
		setElementValue("filial.idFilial", idFilial);
		setElementValue("filial.sgFilial", sgFilial);
		setElementValue("filial.pessoa.nmFantasia", nmFilial);		
	}
	
	function filialOnChange(obj){
		var retorno = filial_sgFilialOnChangeHandler();
		if (obj.value == ''){
			resetValue("controleCarga.idControleCarga");
			disableNrControleCarga(true);
		}
		return retorno;
	}
	
	

	/**************************************************************************************************************************
	 * Funções referentes ao comportamento da lookup de controle de carga
	 **************************************************************************************************************************/	
	function sgFilialOnChangeHandler(obj) {
		var retorno = controleCarga_filialByIdFilialOrigem_sgFilialOnChangeHandler();
		
		if (obj.value == '') {
			disableNrControleCarga(true);
			resetValue("controleCarga.idControleCarga");
		} 
		
		return retorno;
	}
	
	function filialCCDataLoad_cb(data){
		if (data != undefined && data.length == 1){
			resetValue("controleCarga.idControleCarga");
			disableNrControleCarga(false);
		}
		return controleCarga_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	}
	
	function filialCCPopup(data){
		if (data != undefined){
			resetValue("controleCarga.idControleCarga");
			disableNrControleCarga(false);
		}
		
		return true;
	}

	function disableNrControleCarga(disable) {
		setDisabled("controleCarga.idControleCarga", disable);
	}

	function disableNrControleCarga_cb(data, error) {
		if (data.length==0) disableNrControleCarga(false);
		return lookupExactMatch({e:document.getElementById("controleCarga.filialByIdFilialOrigem.idFilial"), data:data});
	}

	
	
</script>
