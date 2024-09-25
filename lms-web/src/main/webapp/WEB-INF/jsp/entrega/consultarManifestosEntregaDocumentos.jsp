<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.entrega.consultarManifestosEntregaAction" >
	<adsm:form action="/entrega/consultarManifestosEntrega" idProperty="idManifestoEntregaDocumento" height="130"
			service="lms.entrega.consultarManifestosEntregaAction.findByIdManifestoEntregaDocumento"
			onDataLoadCallBack="dataLoadCustom" >
		
		<adsm:textbox dataType="text" property="filial_sgFilial"
				label="manifestoEntrega" size="3" labelWidth="18%" width="32%" disabled="true" >
			<adsm:textbox dataType="integer" property="nrManifestoEntrega"
				 size="8" mask="00000000" disabled="true" />
		</adsm:textbox>
		<adsm:textbox dataType="text" property="controleCarga_sgFilial"
				label="controleCarga" size="3" labelWidth="18%" width="32%" disabled="true" >
			<adsm:textbox dataType="integer" property="controleCarga_nrControleCarga"
					size="8" mask="00000000" disabled="true"/>
		</adsm:textbox>

		<adsm:hidden property="idDoctoServico" />
		<adsm:hidden property="tpDocumentoServico_value" />
		<adsm:textbox dataType="text" property="tpDocumentoServico_description"
				label="documentoServico" labelWidth="18%" width="32%" size="4" disabled="true" >
	        <adsm:textbox dataType="text" property="sgFilialDoctoServico" size="3" disabled="true" />
	        <adsm:textbox dataType="integer" property="nrDoctoServico"
	        		size="10" disabled="true" mask="0000000000" />
		</adsm:textbox>
		
		<adsm:textbox dataType="JTDateTimeZone" property="dhOcorrencia"
				label="dataHoraOcorrencia" labelWidth="18%" width="32%" disabled="true"/>

		<adsm:textbox dataType="text" property="nrIdentificacaoRemetente"
				label="remetente" disabled="true" size="18" labelWidth="18%" width="82%" >	
			<adsm:textbox dataType="text" property="nmPessoaRemetente" size="40" disabled="true" />
		</adsm:textbox>
		<adsm:textbox dataType="text" property="nrIdentificacaoDestinatario"
				label="destinatario" disabled="true" size="18" labelWidth="18%" width="82%" >
			<adsm:textbox dataType="text" property="nmPessoaDestinatario" size="40" disabled="true" />
		</adsm:textbox> 
		
		<adsm:textbox dataType="text" property="dsEnderecoEntrega"
				label="endereco" size="102" labelWidth="18%" width="82%" disabled="true" />

		<adsm:textbox dataType="text" property="tpSituacaoDocumento" size="38" label="situacaoDocumento" labelWidth="18%" width="32%"  disabled="true" />
		<adsm:textbox dataType="integer" property="qtVolumes"
				label="volumes" size="10" labelWidth="10%" width="32%" disabled="true" />

		<adsm:textbox dataType="decimal" property="psReferenciaCalculo" 
				label="peso" size="10" labelWidth="18%" width="32%" disabled="true" unit="kg"
				mask="###,###,###,###,##0.000" />
		<adsm:textbox dataType="currency" property="vlTotalDocServico"
				label="valor" size="10" labelWidth="10%" width="32%" disabled="true" mask="#,###,###,###,###,##0.00" />
				
		<adsm:textbox dataType="integer" property="cdOcorrenciaEntrega"
				label="ocorrencia" size="3" maxLength="10" labelWidth="18%" width="32%" disabled="true" mask="000" >
			<adsm:textbox dataType="text" property="dsOcorrenciaEntrega" size="30" disabled="true" />
		</adsm:textbox>
		
		<adsm:textbox dataType="text" property="nmRecebedor"
				label="recebedor" size="40" labelWidth="10%" width="28%" disabled="true"/>
				
		<adsm:textarea property="obManifestoEntregaDocumento"
				label="observacao" maxLength="120" rows="2" columns="104" labelWidth="18%" width="82%" disabled="true"/>

	</adsm:form>
	 
	<adsm:grid property="manifestoEntregaDocumento" idProperty="idManifestoEntregaDocumento"
			service="lms.entrega.consultarManifestosEntregaAction.findPaginatedDoctosServico"
			rowCountService="lms.entrega.consultarManifestosEntregaAction.getRowCountDoctosServico"
			selectionMode="none" unique="true" rows="8" gridHeight="173" scrollBars="horizontal" detailFrameName="doc" >
		
		<adsm:gridColumn title="documentoServico" 
					property="tpDocumentoServico" width="40" isDomain="true" />
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" property="sgFilialDoctoServico" width="60" />
			<adsm:gridColumn title=""  
					property="nrDoctoServico" width="60" dataType="integer" mask="0000000000" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn width="150" title="dataHoraOcorrencia" property="dhBaixa" dataType="JTDateTimeZone" />
		<adsm:gridColumnGroup customSeparator=" - " >
			<adsm:gridColumn title="ocorrencia" dataType="integer" align="left"
					property="cdOcorrenciaEntrega" width="150" mask="000" />
			<adsm:gridColumn title="" property="dsOcorrenciaEntrega" width="150" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn width="250" title="recebedor" property="nmRecebedor" dataType="text"/>
		
		<%--adsm:gridColumn title="identificacao"
				property="tpIdentificacaoRemetente" width="50" isDomain="true" />
		<adsm:gridColumn title="" 
				property="nrIdentificacaoRemetente" align="right" width="120"/>
		<adsm:gridColumn title="remetente" 
				property="nmPessoaRemetente" width="140" /--%>
		
		<adsm:gridColumn title="identificacao"
				property="tpIdentificacaoDestinatario" width="50" isDomain="true" />
		<adsm:gridColumn title="" 
				property="nrIdentificacaoDestinatario" align="right" width="120"/>
		<adsm:gridColumn title="destinatario" 
				property="nmPessoaDestinatario" width="180" />
		
		<adsm:gridColumn width="350" title="enderecoEntrega" property="dsEnderecoEntrega" />
		
		<adsm:gridColumn title="situacaoDocumento" property="tpSituacaoDocumento" width="320" isDomain="true" />
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>

<script type="text/javascript">
<!--

	function getElementValueFromCad(property) {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		return tabDet.getFormProperty(property);
	}

	var idManifestoEntregaFlag = -1;	
	function tabShowCustom() {
		var idManifestoEntrega = getElementValueFromCad("idManifestoEntrega");
		
		newButtonScript(document, true, {name:'newButton_click'});
		
		if (idManifestoEntrega != undefined && idManifestoEntrega != "" && idManifestoEntrega != idManifestoEntregaFlag) {
			idManifestoEntregaFlag = idManifestoEntrega;
			
			document.getElementById("filial_sgFilial").masterLink = true;
			document.getElementById("nrManifestoEntrega").masterLink = true;
			document.getElementById("controleCarga_sgFilial").masterLink = true;
			document.getElementById("controleCarga_nrControleCarga").masterLink = true;
			
			setElementValue("filial_sgFilial",getElementValueFromCad("filial_sgFilial"));
			setElementValue("nrManifestoEntrega",getElementValueFromCad("nrManifestoEntrega"));
			setElementValue("controleCarga_sgFilial",getElementValueFromCad("controleCarga_sgFilial"));
			setElementValue("controleCarga_nrControleCarga",getElementValueFromCad("controleCarga_nrControleCarga"));
			
			var data = new Array();
			setNestedBeanPropertyValue(data, "idManifestoEntrega", idManifestoEntrega);
			manifestoEntregaDocumentoGridDef.executeSearch(data,undefined,undefined,true);
		}
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") {
			setFocusGrid();
		}
	}
	
	function dataLoadCustom_cb(data,error) {
		onDataLoad_cb(data,error);
		}
	
	function setFocusGrid() {
		if (manifestoEntregaDocumentoGridDef.gridState.rowCount>0) {
			var rowObj = document.getElementById(manifestoEntregaDocumentoGridDef.property+"_row:" +
					manifestoEntregaDocumentoGridDef.navigate.currentRow);
			rowObj.onmouseover();
			
			var dataTable = document.getElementById(manifestoEntregaDocumentoGridDef.property+".dataTable");
			if (dataTable.disabled != true) {
				setFocus(dataTable,true);
			}
		}
	}
//-->
</script>