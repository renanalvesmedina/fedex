<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script type="text/javascript">
<!--
	function dataLoad_cb(data) {
		setElementValue("controleCarga.filial.sgFilial",getNestedBeanPropertyValue(data,"CC_FI_SG"));
		setElementValue("controleCarga.nrControleCarga",setFormat(document.getElementById("controleCarga.nrControleCarga"),getNestedBeanPropertyValue(data,"CC_NR")));
		setElementValue("manifestoEntrega.filial.sgFilial",getNestedBeanPropertyValue(data,"MANI_SG_FILIAL"));
		setElementValue("manifestoEntrega.nrManifestoEntrega",setFormat(document.getElementById("manifestoEntrega.nrManifestoEntrega"),getNestedBeanPropertyValue(data,"MANI_NR_MANIFESTO")));
		setElementValue("rotaColetaEntrega.nrRota",setFormat(document.getElementById("rotaColetaEntrega.nrRota"),getNestedBeanPropertyValue(data,"ROTA_NR")));
		setElementValue("rotaColetaEntrega.dsRota",getNestedBeanPropertyValue(data,"ROTA_DS"));
		setElementValue("meioTransporte2.nrFrota",getNestedBeanPropertyValue(data,"MT_NR_FROTA"));
		setElementValue("meioTransporte.nrIdentificador",getNestedBeanPropertyValue(data,"MT_NR_IDENT"));
		setElementValue("meioTransporte2SemiReboque.nrFrota",getNestedBeanPropertyValue(data,"MTSR_NR_FROTA"));
		setElementValue("meioTransporteSemiReboque.nrIdentificador",getNestedBeanPropertyValue(data,"MTSR_NR_IDENT"));
		setElementValue("idManifesto",getNestedBeanPropertyValue(data,"MANI_ID_MANIFESTO"));
		setElementValue("tpStatusManifesto",getNestedBeanPropertyValue(data,"TP_STATUS_MANIFESTO.description"));
		setElementValue("tpManifesto",getNestedBeanPropertyValue(data,"TP_MANIFESTO.description"));
		
		var tpManifestoEntrega = getNestedBeanPropertyValue(data,"MANI_TP_MANIFESTO_ENTREGA");
		setDisabled("closeManifesto",false);
		if (tpManifestoEntrega == "PR" || tpManifestoEntrega == "EP") {
			document.getElementById("divGrid").style.display = "none";
			document.getElementById("divCliente").style.display = "";
			setElementValue("clienteConsig.nrIdentificacao",getNestedBeanPropertyValue(data,"CLI_NR"));
			setElementValue("clienteConsig.nmPessoa",getNestedBeanPropertyValue(data,"CLI_NOME"));
		}else{
			document.getElementById("divGrid").style.display = "";
			document.getElementById("divCliente").style.display = "none";
			DoctoServicoGridDef.executeSearch({idManifesto:getElementValue("idManifesto")});
		}
	}
	
	function rowGridClick(id) {
		var data = DoctoServicoGridDef.findById(id);
		
		var URL = "/entrega/fecharManifestoEntregas.do?cmd=confirmacao" +
					"&remetente.nrIdentificacao=" + getNestedBeanPropertyValue(data,"CR_NR") +
					"&remetente.nmPessoa=" + getNestedBeanPropertyValue(data,"CR_NM") +
					"&remetente.id=" + getNestedBeanPropertyValue(data,"CR_ID") +
					"&destinatario.nrIdentificacao=" + getNestedBeanPropertyValue(data,"C_NR") +
					"&destinatario.nmPessoa=" + getNestedBeanPropertyValue(data,"C_NM") +
					"&destinatario.id=" + getNestedBeanPropertyValue(data,"C_ID") +
					"&doctoServico.tpDocumentoServico=" + getNestedBeanPropertyValue(data,"DS_TP.description") +
					"&doctoServico.sgFilial=" + getNestedBeanPropertyValue(data,"DS_FI") +
					"&doctoServico.id=" + getNestedBeanPropertyValue(data,"idDoctoServico") +
					"&tpSituacaoDocumento.value=" + getNestedBeanPropertyValue(data,"PENDENCIA.value") +
					"&idManifestoEntregaDocumento=" + getNestedBeanPropertyValue(data,"idManifestoEntregaDocumento") +
					"&doctoServico.nrDoctoServico=" + getNestedBeanPropertyValue(data,"DS_NR") + 
					"&blRetencaoComprovanteEnt=" + getNestedBeanPropertyValue(data,"blRetencaoComprovanteEnt");

		showModalDialog(URL,window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:385px;');
		return false;
	}

	/**
	 * FECHAMENTO DO MANIFESTO
	 */
	function fecharManifesto() {
		var sdo = createServiceDataObject("lms.entrega.fecharManifestoEntregasAction.executeFecharManifesto","fecharManifesto",{e:getElementValue("idManifesto")});
		xmit({serviceDataObjects:[sdo]});
	}
	
	function fecharManifesto_cb(data,exception) {
		if (exception != undefined)
			alert(exception);
		else{
			alert(i18NLabel.getLabel("LMS-09083"));
			getTabGroup(document).selectTab(0);
			getTabGroup(document).setDisabledTab("pendencias",true);
		}
	}
	
	
	/**
	 * Funções relacionada a aparecer o campo
	 */
	 function generateColumns() {
		colunas = '<table class="Form" cellpadding="0" cellspacing="0" width="98%><tr>';
		for (i = 0 ; i < 33 ; i++) {
			colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td>';
			colunas += '<td><img src="lms/images/spacer.gif" width="8px" height="1px"></td><td><img src="lms/images/spacer.gif" width="8px" height="1px"></td>';
		}
		colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td></tr>';
		return colunas;
	}
	 
//-->
</script>
<adsm:window service="lms.entrega.fecharManifestoEntregasAction">
	<adsm:form action="entrega/fecharManifestoEntregas" height="428"
		onDataLoadCallBack="dataLoad" idProperty="idManifesto">
	
	<td colspan="100" >

	<div id="principal" style="display:;border:none;">
	<script>
		document.write(generateColumns());
	</script>
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-09083"/>
		</adsm:i18nLabels>

		<adsm:textbox dataType="text" property="controleCarga.filial.sgFilial"
			label="controleCargas" disabled="true" size="3" labelWidth="16%"
			width="34%" serializable="false">
			<adsm:textbox dataType="integer"
				property="controleCarga.nrControleCarga" mask="00000000"
				disabled="true" size="10" serializable="false" />
		</adsm:textbox>

		<adsm:textbox dataType="integer" property="rotaColetaEntrega.nrRota"
			mask="000" labelWidth="16%" label="rota" size="3" width="34%"
			serializable="false" disabled="true">
			<adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota"
				size="30" disabled="true" serializable="false" />
		</adsm:textbox>
		

		<adsm:textbox label="manifestoEntrega" labelWidth="16%" width="34%"
			dataType="text" disabled="true" size="3"
			property="manifestoEntrega.filial.sgFilial" serializable="false">
			<adsm:textbox dataType="integer"
				property="manifestoEntrega.nrManifestoEntrega" size="10"
				mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:textbox label="tipo" labelWidth="16%" width="34%"
			dataType="text" disabled="true" size="25"
			property="tpManifesto" serializable="false">
		</adsm:textbox>
		

		<adsm:textbox dataType="text" property="meioTransporte2.nrFrota"
			label="meioTransporte" labelWidth="16%" width="34%" size="8"
			serializable="false" disabled="true">
			<adsm:textbox dataType="text"
				property="meioTransporte.nrIdentificador" size="20"
				serializable="false" disabled="true" />
		</adsm:textbox>


		<adsm:textbox dataType="text"
			property="meioTransporte2SemiReboque.nrFrota" label="semiReboque"
			labelWidth="16%" width="34%" size="8" serializable="false"
			disabled="true">
			<adsm:textbox dataType="text"
				property="meioTransporteSemiReboque.nrIdentificador" size="20"
				disabled="true" />
		</adsm:textbox>
		
		
		<adsm:textbox label="situacao" labelWidth="16%" width="34%"
			dataType="text" disabled="true" size="25"
			property="tpStatusManifesto" serializable="false">
		</adsm:textbox>
		
	</table>
	</div>

	<div id="divCliente" style="display:none;border:none">
	<script type="text/javascript">
		document.write(generateColumns());
	</script>
	
		<adsm:textbox dataType="text" 
				property="clienteConsig.nrIdentificacao" label="consignatario"
				labelWidth="16%" width="84%" size="18" serializable="false"
				disabled="true">
			<adsm:textbox dataType="text"
				property="clienteConsig.nmPessoa" size="30"
				disabled="true" />
		</adsm:textbox>
		
	</table>
	</div>
	
	<div id="divGrid" style="display:">
	<script>
		document.write(generateColumns());
	</script>
	
	<adsm:grid property="DoctoServico" idProperty="idDoctoServico"
			title="documentosPendentes" autoSearch="false"
			service="lms.entrega.fecharManifestoEntregasAction.findPaginatedPendencias"
			unique="true" onRowClick="rowGridClick"
			rowCountService="lms.entrega.fecharManifestoEntregasAction.getRowCountPendencias"
			rows="11" selectionMode="none" >
		<adsm:gridColumn title="documentoServico" property="DS_TP" width="55"
			isDomain="true" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="" property="DS_FI" width="50" />
			<adsm:gridColumn title="" property="DS_NR" dataType="integer"
				mask="00000000" width="110" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn width="70" title="identificacao" property="C_TP"
			isDomain="true" align="left" />
		<adsm:gridColumn width="130" title="" property="C_NR" align="right" />
		<adsm:gridColumn width="200" title="destinatario" property="C_NM"
			align="left" />
		<adsm:gridColumn width="" title="pendencia" property="dsPendencia" />
	</adsm:grid>
	
	</table>
	</div>
	
		<adsm:buttonBar>
			<adsm:button caption="fecharManifesto" id="closeManifesto" onclick="fecharManifesto()" />
		</adsm:buttonBar>
	</adsm:form>
	
	</td></tr></table>

</adsm:window>
