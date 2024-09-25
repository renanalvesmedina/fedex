<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.consultarSMPAction" >
	<adsm:form action="/sgr/manterSMP" height="150" idProperty="idSolicMonitPreventivo">

		<adsm:range label="periodo" labelWidth="17%" width="83%" >
			<adsm:textbox dataType="JTDateTimeZone" property="dataInicial" picker="true" />
			<adsm:textbox dataType="JTDateTimeZone" property="dataFinal" picker="true"/>
		</adsm:range>

		<adsm:lookup label="smp" labelWidth="17%" width="83%"
		             property="solicMonitPreventivo.filial"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.sgr.consultarSMPAction.findLookupFilial" 
		             dataType="text"
		             size="3"
		             picker="false" 
		             maxLength="3">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>

			<adsm:textbox dataType="integer"
			              property="solicMonitPreventivo.nrSmp"
						  maxLength="8" 
						  size="8"
						  mask="00000000"/>
			
		</adsm:lookup>


			
			<adsm:combobox property="moeda.idMoeda" 
						   service="lms.sgr.consultarSMPAction.findMoeda" 
						   optionProperty="idMoeda" 
						   optionLabelProperty="siglaSimbolo" 
						   label="valor"
						   labelWidth="17%" width="83%"
			>
			<adsm:range >			
				<adsm:textbox dataType="currency" property="vlFrotaMinimo" picker="true" />
				<adsm:textbox dataType="currency" property="vlFrotaMaximo" picker="true"/>
			</adsm:range>
		
		</adsm:combobox>


		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>		
		
		<adsm:lookup label="origemTrecho" labelWidth="17%" width="83%"
		             property="controleTrecho.filialByIdFilialOrigem"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.sgr.consultarSMPAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3">

			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
					             
        	<adsm:propertyMapping relatedProperty="controleTrecho.filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="controleTrecho.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="true" size="50" maxLength="50" disabled="true"/>
        </adsm:lookup>
		
		<adsm:lookup label="destinoTrecho" labelWidth="17%" width="83%"
		             property="controleTrecho.filialByIdFilialDestino"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.sgr.consultarSMPAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3">
		             
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
					             
        	<adsm:propertyMapping relatedProperty="controleTrecho.filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="controleTrecho.filialByIdFilialDestino.pessoa.nmFantasia" serializable="true" size="50" maxLength="50" disabled="true"/>
        </adsm:lookup>

		<adsm:lookup label="meioTransporte" labelWidth="17%" width="83%" maxLength="6" size="6"
		             dataType="text" 
		             property="meioTransporteByIdTransportado2" 
		             idProperty="idMeioTransporte"
		             criteriaProperty="nrFrota"
					 service="lms.sgr.manterSMPAction.findLookupMeioTransporte" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 serializable="false"
					 picker="false"
					 >

			<adsm:propertyMapping modelProperty="nrIdentificador"  criteriaProperty="meioTransporteByIdTransportado.nrIdentificador" disable="false"/> 
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado.idMeioTransporte"/> 
			<adsm:propertyMapping modelProperty="nrIdentificador"  relatedProperty="meioTransporteByIdTransportado.nrIdentificador" disable="false"/> 
			
			<adsm:lookup dataType="text" 
			             property="meioTransporteByIdTransportado" 
			             idProperty="idMeioTransporte"
			             criteriaProperty="nrIdentificador" 
						 service="lms.sgr.manterSMPAction.findLookupMeioTransporte" 
						 action="/contratacaoVeiculos/manterMeiosTransporte" 
						 maxLength="25" size="25" picker="true">
				<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporteByIdTransportado2.nrFrota"/> 
				<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteByIdTransportado2.idMeioTransporte"/> 
				<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteByIdTransportado2.nrFrota"/> 
			</adsm:lookup>
		</adsm:lookup>
		
		<adsm:lookup  label="controleCargas" labelWidth="17%" width="83%" size="3" maxLength="3" picker="false" serializable="false"
    		              dataType="text" 
    		              property="controleCarga.filialByIdFilialOrigem"
    		              idProperty="idFilial" 
    		              criteriaProperty="sgFilial" 
					      service="lms.sgr.consultarSMPAction.findLookupFilial"
					      action="/municipios/manterFiliais"
					      onchange="return sgFilialOnChangeHandler();"
					      popupLabel="pesquisarFilial"
					      onDataLoadCallBack="disableNrControleCarga">
					      
			<adsm:lookup  dataType="integer"
			              property="controleCarga" 
			              idProperty="idControleCarga" 
			              criteriaProperty="nrControleCarga"
			              popupLabel="pesquisarControleCarga"
						  service="lms.sgr.consultarSMPAction.findControleCarga" 
						  onPopupSetValue="onControleCargaPopupSetValue"
						  action="/carregamento/manterControleCargas"
						  maxLength="8" 
						  size="8"
						  mask="00000000">
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial"/>
			</adsm:lookup>
		</adsm:lookup>

		<adsm:combobox label="tipoExigencia" labelWidth="17%" width="83%" 
			   		   property="tipoExigenciaGerRisco.idTipoExigenciaGerRisco" 
			           optionLabelProperty="dsTipoExigenciaGerRisco" 
			           optionProperty="idTipoExigenciaGerRisco" 
			           service="lms.sgr.consultarSMPAction.findTipoExigenciaGerRisco" 
			           onchange=" return onChangeTipoExigencia(this)"
		/>

		<adsm:combobox label="exigencia" labelWidth="17%" width="83%"
		               optionLabelProperty="dsResumida" 
					   optionProperty="idExigenciaGerRisco" 
					   property="exigenciaGerRisco.idExigenciaGerRisco" 
					   service="lms.sgr.consultarSMPAction.findExigenciaGerRisco">
			 <adsm:propertyMapping modelProperty="tipoExigenciaGerRisco.idTipoExigenciaGerRisco" criteriaProperty="tipoExigenciaGerRisco.idTipoExigenciaGerRisco" />
		</adsm:combobox>
		
		<adsm:lookup label="rota" labelWidth="17%" width="83%"
		        	 size="4" 
		        	 maxLength="4" 
					 exactMatch="true"
					 dataType="integer" 
					 property="rotaIdaVolta" 
					 idProperty="idRotaIdaVolta" 
					 criteriaProperty="nrRota"
					 service="lms.sgr.consultarSMPAction.findLookupRotaIdaVolta"
					 action="/municipios/consultarRotas"
					 cmd="idaVolta">
 		    <adsm:propertyMapping modelProperty="rota.dsRota" relatedProperty="rotaIdaVolta.rota.dsRota" />
        	<adsm:textbox dataType="text" property="rotaIdaVolta.rota.dsRota" disabled="true" size="30" serializable="false"/>
        </adsm:lookup>
        
        <adsm:checkbox property="blMostrarCancelados" label="mostrarCancelados" labelWidth="17%" width="83%" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="solicMonitPreventivos"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idSolicMonitPreventivo" 
			   property="solicMonitPreventivos" 
			   selectionMode="none" 
			   gridHeight="130" 
			   unique="true"
			   scrollBars="horizontal"
			   rows="6"
			   onRowClick="disableRowClick"
			   service="lms.sgr.consultarSMPAction.findPaginatedSMP"
			   rowCountService="lms.sgr.consultarSMPAction.getRowCountSMP">

		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn property="filial.sgFilial" title="smp" width="50" dataType="text" align="left"/>
			<adsm:gridColumn property="nrSmp" title="" width="100" dataType="integer" mask="00000000" align="left"/>
		</adsm:gridColumnGroup>
		
		
	    <adsm:gridColumnGroup separatorType="CONTROLE_CARGA">
			<adsm:gridColumn property="controleTrecho.controleCarga.filialByIdFilialOrigem.sgFilial" title="controleCargas" width="50" />
		    <adsm:gridColumn property="controleTrecho.controleCarga.nrControleCarga" title="" width="100"  dataType="integer" mask="00000000"/>
		</adsm:gridColumnGroup>		
		
		<adsm:gridColumn property="dhGeracao" title="dataGeracao" align="center" dataType="JTDateTimeZone" width="120"/>
		
		<adsm:gridColumn property="moedaPais.moeda.siglaSimbolo" title="valor" width="50" dataType="text"/>
		<adsm:gridColumn property="vlSmp" title="" width="110" align="right" dataType="decimal"/>
		
		<adsm:gridColumn property="meioTransporteByIdMeioTransporte.nrFrota" title="meioTransporte" width="70" />
		<adsm:gridColumn property="meioTransporteByIdMeioTransporte.nrIdentificador" title="" width="80" />
		
		<adsm:gridColumn property="controleTrecho.controleCarga.tpControleCarga" title="tipoOperacao" width="150" isDomain="true" />
		
		<adsm:gridColumn property="controleTrecho.filialByIdFilialOrigem.sgFilial" title="origem" width="50" />
		<adsm:gridColumn property="controleTrecho.filialByIdFilialDestino.sgFilial" title="destino" width="50" />

		<adsm:gridColumn property="controleTrecho.controleCarga.rota.dsRota" title="rota" width="150" />

		<adsm:gridColumn property="visualizarSMP" title="visualizarSMP" image="/images/printer.gif" width="100" reportName="lms.sgr.consultarSMPAction" linkIdProperty="idSolicMonitPreventivo" align="center" />
		<adsm:gridColumn property="conteudoVeiculo" title="conteudoVeiculo" image="images/popup.gif" width="180" link="/sgr/consultarConteudosVeiculoManifestos.do?cmd=main" linkIdProperty="idSolicMonitPreventivo" align="center"/>		

		<adsm:buttonBar freeLayout="false"/>
		
	</adsm:grid>
</adsm:window>
<script type="text/javascript">

function initWindow(event) {
	if (event.name == "tab_load") {
		setElementValue('blMostrarCancelados', false);
	}
	disableNrControleCarga(true);
	disableComboExigencia(true);
}

function onChangeTipoExigencia(combo) {
	var tipoExigencia = combo.value;
	
	if(tipoExigencia!=undefined && tipoExigencia!="" ) {
		disableComboExigencia(false);	
	} else {
		disableComboExigencia(true);
	}
	return comboboxChange({e:combo});
}

function onControleCargaPopupSetValue(data) {
	setDisabled('controleCarga.nrControleCarga', false);
	setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial', data.filialByIdFilialOrigem.sgFilial);
	setElementValue('controleCarga.filialByIdFilialOrigem.idFilial', data.filialByIdFilialOrigem.idFilial);
}

function disableRowClick() {
	return false;
}

function disableComboExigencia(disable) {
	setDisabled(document.getElementById("exigenciaGerRisco.idExigenciaGerRisco"), disable);
}

/**
 * Controla o objeto de controle carga
 */	
function sgFilialOnChangeHandler() {
	if (getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial")=="") {
		disableNrControleCarga(true);
		resetValue("controleCarga.idControleCarga");
	} else {
		disableNrControleCarga(false);
	}
	return lookupChange({e:document.forms[0].elements["controleCarga.filialByIdFilialOrigem.idFilial"]});
}

function disableNrControleCarga_cb(data, error) {
	if (data.length==0) {
	    disableNrControleCarga(false);
	}
	return lookupExactMatch({e:document.getElementById("controleCarga.filialByIdFilialOrigem.idFilial"), data:data});
}

function disableNrControleCarga(disable) {
	setDisabled(document.getElementById("controleCarga.nrControleCarga"), disable);
}

</script>

