<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.recepcaodescarga.consultarCargasViagemAction">
	<adsm:form action="/recepcaoDescarga/consultarCargasViagem" height="187">
		
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		
		<adsm:lookup label="filialOrigem" labelWidth="25%" width="75%"
		             property="filialOrigem"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.recepcaodescarga.consultarCargasViagemAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             required="false">
        	<adsm:propertyMapping relatedProperty="filialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
	        <adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
		    <adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
            <adsm:textbox dataType="text" property="filialOrigem.pessoa.nmFantasia" serializable="false" size="50" maxLength="50" disabled="true"/>
        </adsm:lookup>
		
		<adsm:lookup label="filialDestino" labelWidth="25%" width="75%"
		             property="filialDestino"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais"
		             service="lms.recepcaodescarga.consultarCargasViagemAction.findLookupFilial"
		             dataType="text"
		             size="3"
		             maxLength="3"
		             required="false">
        	<adsm:propertyMapping relatedProperty="filialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
	        <adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
		    <adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
            <adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" serializable="false" size="50" maxLength="50" disabled="true"/>
        </adsm:lookup>
        		
        <adsm:combobox label="servico" labelWidth="25%" width="75%"
					   property="servico.idServico" 
					   service="lms.recepcaodescarga.consultarCargasViagemAction.findComboServico" 
					   optionLabelProperty="dsServico" 
					   optionProperty="idServico" >
				<adsm:propertyMapping modelProperty="dsServico" relatedProperty="servico.dsServico" />
				<adsm:hidden property="servico.dsServico" />
		 </adsm:combobox> 
	
		<adsm:lookup label="controleCargas" labelWidth="25%" width="75%" size="3" maxLength="3" picker="false" serializable="false"
    		              popupLabel="pesquisarFilial"
    		              dataType="text" 
    		              property="controleCarga.filialByIdFilialOrigem"
    		              idProperty="idFilial" 
    		              criteriaProperty="sgFilial" 
					      service="lms.recepcaodescarga.consultarCargasViagemAction.findLookupFilial"
					      action="/municipios/manterFiliais"
					      onchange="return sgFilialOnChangeHandler();" 
					      onDataLoadCallBack="retornoControleCargaFilial">
			<adsm:lookup dataType="integer"
						  popupLabel="pesquisarControleCarga"
			              property="controleCarga" 
			              idProperty="idControleCarga" 
			              criteriaProperty="nrControleCarga"
						  service="lms.recepcaodescarga.consultarCargasViagemAction.findControleCarga" 
						  action="/carregamento/manterControleCargas"
						  maxLength="8" 
						  size="8"
						  mask="00000000"
						  onPopupSetValue="popupControleCarga">
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" disable="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" disable="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.idFilial" blankFill="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.sgFilial" blankFill="false" />

			</adsm:lookup>
		</adsm:lookup>
	
		<adsm:lookup label="meioTransporte" labelWidth="25%" width="75%" maxLength="6" size="6"
		             dataType="text" 
		             property="controleCarga.meioTransporteByIdTransportado2" 
		             idProperty="idMeioTransporte"
		             criteriaProperty="nrFrota"
					 service="lms.recepcaodescarga.consultarCargasViagemAction.findLookupMeioTransporte" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 serializable="false">

			<adsm:propertyMapping modelProperty="nrIdentificador"  criteriaProperty="controleCarga.meioTransporteByIdTransportado.nrIdentificador" disable="false"/> 
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="controleCarga.meioTransporteByIdTransportado.idMeioTransporte"/> 
			<adsm:propertyMapping modelProperty="nrIdentificador"  relatedProperty="controleCarga.meioTransporteByIdTransportado.nrIdentificador" disable="false"/> 
			
			<adsm:lookup dataType="text" 
			             property="controleCarga.meioTransporteByIdTransportado" 
			             idProperty="idMeioTransporte" 
			             criteriaProperty="nrIdentificador" 
						 service="lms.recepcaodescarga.consultarCargasViagemAction.findLookupMeioTransporte" 
						 action="/contratacaoVeiculos/manterMeiosTransporte" 
						 maxLength="25" size="25" picker="false">
				<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="controleCarga.meioTransporteByIdTransportado2.nrFrota"/> 
				<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="controleCarga.meioTransporteByIdTransportado2.idMeioTransporte"/> 
				<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="controleCarga.meioTransporteByIdTransportado2.nrFrota"/> 
			</adsm:lookup>
		</adsm:lookup>
	
		<adsm:hidden property="manifesto.filialByIdFilialOrigem.pessoa.nmFantasia" />	
	
		<adsm:combobox label="manifestoViagem" 
					   labelWidth="25%" width="75%" serializable="true" 
		               property="manifesto.tpManifesto" 
					   service="lms.recepcaodescarga.consultarCargasViagemAction.findTipoManifesto" 
					   optionProperty="value" 
					   optionLabelProperty="description"
					   onchange="limpaIdManifesto(); return changeDocumentWidgetType({
						   documentTypeElement:this, 
						   filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
						   documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional'), 
						   documentGroup:'MANIFESTO',
						   actionService:'lms.recepcaodescarga.consultarCargasViagemAction'
						   });" >

			<adsm:lookup dataType="text"
						 property="manifesto.filialByIdFilialOrigem"
					 	 idProperty="idFilial" 
					 	 criteriaProperty="sgFilial" 
						 service="" 
						 action="" 
						 size="3" 
						 maxLength="3" 
						 picker="false" 
						 disabled="true" 
						 serializable="false" 
						 onDataLoadCallBack="enableManifestoManifestoViagemNacioal"
						 onchange="limpaIdManifesto(); return changeDocumentWidgetFilial({
						 	filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
						 	documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional')
						 	}); "
						 >
						 <adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
		    			 <adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			</adsm:lookup>

			<adsm:lookup dataType="integer" 
						 onDataLoadCallBack="retornoManifesto"
						 property="manifesto.manifestoViagemNacional" 
						 idProperty="idManifestoViagemNacional" 
						 criteriaProperty="nrManifestoOrigem" 
						 service="" popupLabel="pesquisarManifesto"
						 action="" 
						 onPopupSetValue="manifestoNrManifestoOrigem_retornoPopup"
						 onchange="return manifestoNrManifestoOrigem_OnChange();"						 
						 size="10" 
						 maxLength="8" 
						 mask="00000000" 
						 disabled="true" 
						 serializable="true"/>
		</adsm:combobox>

		<adsm:hidden property="carregaDadosFilial" value="false" serializable="false"/>
		<adsm:hidden property="manifesto.idManifesto" />
		<adsm:hidden property="manifesto.tpStatusManifesto" />


		<adsm:range label="horarioChegadaPrevista" labelWidth="25%" width="75%" >
			<adsm:textbox dataType="JTDateTimeZone" property="chegadaPrevistaInicial" picker="true" />
			<adsm:textbox dataType="JTDateTimeZone" property="chegadaPrevistaFinal" picker="true"/>
		</adsm:range>

		<adsm:range label="horarioChegadaEstimada" labelWidth="25%" width="75%" >
			<adsm:textbox dataType="JTDateTimeZone" property="chegadaEstimadaInicial" picker="true" />
			<adsm:textbox dataType="JTDateTimeZone" property="chegadaEstimadaFinal" picker="true"/>
		</adsm:range>

		<adsm:checkbox property="emAtraso" label="emAtraso" labelWidth="25%" width="75%" />
		<adsm:checkbox property="naData" label="naData" labelWidth="25%" width="75%" />
		<adsm:checkbox property="emDia" label="emDia" labelWidth="25%" width="75%" />

        <adsm:section caption="opcaoRelatorio" width="100%" />

    	<adsm:combobox label="formatoRelatorio" labelWidth="25%" width="75%"
		               domain="DM_FORMATO_RELATORIO"
		               property="tpFormatoRelatorio"
		               defaultValue="pdf"
		               required="true" renderOptions="true"
		/>	

		<adsm:i18nLabels>
			<adsm:include key="LMS-03008"/>
		</adsm:i18nLabels>

		<adsm:buttonBar freeLayout="true">
			<adsm:button  caption="consultar" onclick="consultaCargasViagem()" buttonType="findButton"  />
			<adsm:button caption="limpar" id="btnLimpar" onclick="limpaTela()" buttonType="resetButton" />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="cargasViagem" 
	           idProperty="manifesto.idManifesto" 
	           selectionMode="none" 
	           gridHeight="80" 
	           unique="true"
	           scrollBars="horizontal"
	           service="lms.recepcaodescarga.consultarCargasViagemAction.findCargasEmViagem"
	           rowCountService="lms.recepcaodescarga.consultarCargasViagemAction.getRowCountCargasEmViagem"
	           onRowClick="disableRowClick"
	           rows="4">
	           
	    <adsm:gridColumnGroup separatorType="MANIFESTO">
			<adsm:gridColumn property="manifesto.filialByIdFilialOrigem.sgFilial" title="manifesto" width="40" />
			<adsm:gridColumn property="manifesto.nrManifesto" title="" width="60" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn property="manifesto.filialByIdFilialDestino.sgFilial" title="destino" width="80" />
		<adsm:gridColumn property="manifesto.dhEmissaoManifesto" dataType="JTDateTimeZone" title="emissao" width="115" />
	    
	    <adsm:gridColumnGroup separatorType="CONTROLE_CARGA">
			<adsm:gridColumn property="manifesto.controleCarga.filialByIdFilialOrigem.sgFilial" title="controleCargas" width="40" />
		    <adsm:gridColumn property="manifesto.controleCarga.nrControleCarga" title="" width="60" dataType="integer" mask="00000000"/>
		</adsm:gridColumnGroup>
	
		<adsm:gridColumn property="manifesto.controleCarga.meioTransporteByIdTransportado.nrFrota"		   title="meioTransporte" width="40" />
		<adsm:gridColumn property="manifesto.controleCarga.meioTransporteByIdTransportado.nrIdentificador" title="" width="100" />

		<adsm:gridColumn property="quantidadeDoctos"  title="qtdDocumentos"     width="130" align="right" />
		<adsm:gridColumn property="chegadaProgramada" title="chegadaPrevista" width="150" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="chegadaEstimada"   title="chegadaEstimada"   width="150" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="detalhe"           title="detalhe"           image="images/popup.gif" width="80" link="recepcaoDescarga/consultarCargasViagemDetalhe.do?cmd=main" linkIdProperty="manifesto.idManifesto" align="center"/>
		<adsm:gridColumn property="documentos"        title="documentos"        image="images/popup.gif" width="80" link="recepcaoDescarga/consultarCargasViagemDocumentosServico.do?cmd=main" linkIdProperty="manifesto.idManifesto" align="center"/>
		<adsm:gridColumn property="blProdutoPerigoso" title="produtosPerigosos" width="150" align="center"/>

		<adsm:buttonBar>
			<adsm:button caption="visualizarDetalhado"  onclick="relatorioDetalhado()"/>
			<adsm:button caption="visualizarResumido"  onclick="relatorioResumido()"/>
		</adsm:buttonBar>
		
	</adsm:grid>
</adsm:window>
<script type="text/javascript">

function consultaCargasViagem() {
	var filialOrigem = getElementValue('filialOrigem.sgFilial');
	var filialDestino = getElementValue('filialDestino.sgFilial');
	
	if (filialOrigem=="" && filialDestino=="") {
		alert(i18NLabel.getLabel("LMS-03008"));
		setFocus(document.getElementById('filialOrigem.sgFilial'));
		return false;
	}

	findButtonScript('cargasViagem', document.forms[0]);
}

function limpaTela(doc){
	cleanButtonScript(this.document);
	setDisabled('manifesto.filialByIdFilialOrigem.sgFilial', true);
}

function popupControleCarga(data) {
	setDisabled('controleCarga.idControleCarga', false);
}

function initWindow() {
	disableNrControleCarga(true);
	setDisabled("manifesto.manifestoViagemNacional.idManifestoViagemNacional", true);

	document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").serializable = "true";
	document.getElementById("controleCarga.nrControleCarga").serializable = "true";
	document.getElementById("controleCarga.meioTransporteByIdTransportado2.nrFrota").serializable = "true";
	document.getElementById("controleCarga.meioTransporteByIdTransportado.nrIdentificador").serializable = "true";
	document.getElementById("manifesto.filialByIdFilialOrigem.sgFilial").serializable = "true";
	document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem").serializable = "true";
	document.getElementById("filialOrigem.sgFilial").serializable = "true";
	document.getElementById("filialDestino.sgFilial").serializable = "true";
	cargasViagemGridDef.resetGrid();
}

function relatorioResumido() {
	reportButtonScript('lms.recepcaodescarga.consultarCargasViagemAction.executeRelatorioResumido', 'openPdf', document.forms[0]);
}


function relatorioDetalhado() {
	reportButtonScript('lms.recepcaodescarga.consultarCargasViagemAction.executeRelatorioDetalhado', 'openPdf', document.forms[0]);
}

/**
 * Função cancelar o click na grid
 */
function disableRowClick() {
	return false;
}    
		
/**
 * Controla o objeto de controle carga
 */	
function sgFilialOnChangeHandler() {
	var r = controleCarga_filialByIdFilialOrigem_sgFilialOnChangeHandler();
	
	if (getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial")=="") {
		disableNrControleCarga(true);
		resetValue("controleCarga.idControleCarga");
	} else {
		disableNrControleCarga(false);
	}
	return r;
}

function retornoControleCargaFilial_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = controleCarga_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data)
	if (r == true) {
		setDisabled('controleCarga.idControleCarga', false);
		setFocus(document.getElementById("controleCarga.nrControleCarga"));
	}
	return r;
}


function disableNrControleCarga(disable) {
	setDisabled(document.getElementById("controleCarga.nrControleCarga"), disable);
}

    		
/**
 * Funcoes para limpar o nrIndentificador dos veiculos
 */
function meioTransporteByIdTransportadoNrFrotaOnChangeHandler() {
 	controleCarga_meioTransporteByIdTransportado_nrFrotaOnChangeHandler();
 	if (document.getElementById("controleCarga.meioTransporteByIdTransportado2.nrFrota").value=="") {
 		document.getElementById("controleCarga.meioTransporteByIdTransportado.idMeioTransporte").value="";
 		document.getElementById("controleCarga.meioTransporteByIdTransportado.nrIdentificador").value="";
 	}
}
// Funções referentes as tags do manifesto

/**
 * Quando o "Manifesto" for informado
 */
function retornoManifesto_cb(data) {
	var r = manifesto_manifestoViagemNacional_nrManifestoOrigem_exactMatch_cb(data);
	if (r == true) {
		var idManifesto = getElementValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
		setElementValue('manifesto.idManifesto', idManifesto);
//		buscarManifesto(idManifesto);
	}
}


function enableManifestoManifestoViagemNacioal_cb(data) {
   var r = manifesto_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
   if (r == true) {
      setDisabled("manifesto.manifestoViagemNacional.idManifestoViagemNacional", false);
      setFocus(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
   }
}

function manifestoNrManifestoOrigem_retornoPopup(data) {
	if (data != undefined) {
		if (data.idManifestoViagemNacional && data.idManifestoViagemNacional!=""){
			setElementValue("manifesto.idManifesto", data.idManifestoViagemNacional);
		} else if (data.idManifestoViagemInternacional && data.idManifestoViagemInternacional!=""){
			//FIXME Verificar se funciona quando manifesto internacional entrar em produção no LMS
			setElementValue("manifesto.idManifesto", data.idManifestoViagemInternacional);
		}
	}
}

function manifestoNrManifestoOrigem_OnChange() {
	var r = manifesto_manifestoViagemNacional_nrManifestoOrigemOnChangeHandler();
	if (r == true && getElementValue("manifesto.manifestoViagemNacional.nrManifestoOrigem")=="") {
		limpaIdManifesto();
	}
	return r;
}

function limpaIdManifesto(){
	resetValue("manifesto.idManifesto");
}

/**
 * Busca os dados relacionados ao manifesto.
 */
function buscarManifesto(idManifesto) {
	var sdo = createServiceDataObject("lms.rnc.abrirRNCAction.findManifestoByRNC", "resultado_buscarManifesto", {idManifesto:idManifesto});
    xmit({serviceDataObjects:[sdo]});
}

/**
 * Povoa os campos com os dados retornados da busca em manifesto
 */
function resultado_buscarManifesto_cb(data, error) {
	if (data != undefined) {
/*		var idFilialUsuario = getNestedBeanPropertyValue(data, "ocorrenciaNaoConformidade.1.idFilial");
		var idFilialOrigem = getNestedBeanPropertyValue(data,"0:filialByIdFilialDestino.idFilial");
		var idFilialDestino = getNestedBeanPropertyValue(data,"0:filialByIdFilialOrigem.idFilial");
		if (idFilialUsuario != idFilialOrigem && idFilialUsuario != idFilialDestino) {
			alert(lms_12004);
			resetValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional');
			setFocus(document.getElementById('manifesto.manifestoViagemNacional.nrManifestoOrigem'));
			return false;
		}

		setElementValue('controleCarga.idControleCarga', getNestedBeanPropertyValue(data,"0:controleCarga.idControleCarga"));
		setElementValue('controleCarga.nrControleCarga', getNestedBeanPropertyValue(data,"0:controleCarga.nrControleCarga"));
		setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial', getNestedBeanPropertyValue(data,"0:controleCarga.filialByIdFilialOrigem.sgFilial"));

		setElementValue('controleCarga.meioTransporteByIdTransportado.nrFrota', getNestedBeanPropertyValue(data,"0:controleCarga.meioTransporteByIdTransportado.nrFrota"));
		setElementValue('controleCarga.meioTransporteByIdTransportado.nrIdentificador', getNestedBeanPropertyValue(data,"0:controleCarga.meioTransporteByIdTransportado.nrIdentificador"));

		setElementValue('controleCarga.meioTransporteByIdSemiRebocado.nrFrota', getNestedBeanPropertyValue(data,"0:controleCarga.meioTransporteByIdSemiRebocado.nrFrota"));
		setElementValue('controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador', getNestedBeanPropertyValue(data,"0:controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador"));

		format(document.getElementById("controleCarga.nrControleCarga"));
		desabilitaControleCarga(true);
*/		
	}
}
</script>
