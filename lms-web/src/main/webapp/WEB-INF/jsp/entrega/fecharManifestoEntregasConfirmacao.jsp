<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<script type="text/javascript">
<!--
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		var url = new URL(document.location.href); 
		setElementValue("remetente.nrIdentificacao",url.parameters["remetente.nrIdentificacao"]);
		setElementValue("remetente.nmPessoa",url.parameters["remetente.nmPessoa"]);
		setElementValue("remetente.id",url.parameters["remetente.id"]);
		setElementValue("destinatario.nrIdentificacao",url.parameters["destinatario.nrIdentificacao"]);
		setElementValue("destinatario.nmPessoa",url.parameters["destinatario.nmPessoa"]);
		setElementValue("destinatario.id",url.parameters["destinatario.id"]);
		setElementValue("doctoServico.tpDocumentoServico",url.parameters["doctoServico.tpDocumentoServico"]);
		setElementValue("doctoServico.sgFilial",url.parameters["doctoServico.sgFilial"]);
		setElementValue("doctoServico.id",url.parameters["doctoServico.id"]);
		setElementValue("doctoServico.nrDoctoServico",setFormat(document.getElementById("doctoServico.nrDoctoServico"),url.parameters["doctoServico.nrDoctoServico"]));
		setElementValue("tpSituacaoDocumento",url.parameters["tpSituacaoDocumento.value"]);
		setElementValue("idManifestoEntregaDocumento",url.parameters["idManifestoEntregaDocumento"]);
		
		/* O campo blRetencaoComprovanteEnt somente será visível se o mesmo possuir um valor 'nulo'.
		   Como é um checkbox, o mesmo será submetido com valor true ou false.
		   Se ele não for exibido, terá serializable false, e não será tratado posteriormente na camada Model. */
		var blRetencaoComprovanteEnt = url.parameters["blRetencaoComprovanteEnt"];
				
		var blExibirRetencao = blRetencaoComprovanteEnt == "undefined" || blRetencaoComprovanteEnt == "";
		document.getElementById("retencao").style.display = (blExibirRetencao) ? "" : "none";
		document.getElementById("blRetencaoComprovanteEnt").serializable = blExibirRetencao;
				
		var tpSituacao = getElementValue("tpSituacaoDocumento");
		  
		document.getElementById("infoBaixas").style.display = ((enabledBaixa(tpSituacao)) ? "" : "none");
		document.getElementById("comproEntrega").style.display = ((enabledEntrega(tpSituacao)) ? "" : "none");
		document.getElementById("reembolso").style.display = ((enabledReembolso(tpSituacao)) ? "" : "none");
		
		if (enabledBaixa(tpSituacao)) {
			document.getElementById("ocorrenciaEntrega.cdOcorrenciaEntrega").value = "1";
			setFocus("ocorrenciaEntrega.cdOcorrenciaEntrega");
			lookupChange({e:document.forms[0].elements["ocorrenciaEntrega.idOcorrenciaEntrega"],forceChange:true});
		}
		if (enabledEntrega(tpSituacao) || enabledReembolso(tpSituacao)) {
			var sdo = createServiceDataObject("lms.entrega.fecharManifestoEntregasAction.findData", "dataLoadSamples", {idDoctoServico:getElementValue("doctoServico.id")});
	        xmit({serviceDataObjects:[sdo]});
		}

		var sdo2 = createServiceDataObject("lms.entrega.fecharManifestoEntregasAction.findDataUltimaOcorrenciaManifesto", "dataLoadSamples", {idDoctoServico:getElementValue("doctoServico.id")});
        xmit({serviceDataObjects:[sdo2]});
		
		document.getElementById("ocorrenciaEntrega.cdOcorrenciaEntrega").serializable = "true";
		
		validateTpEntregaParcial();
	}
	
	function enabledBaixa(tpSituacao) {
		return (tpSituacao == "PBRC" || tpSituacao == "PBRE" || tpSituacao == "PBCO" || tpSituacao == "PBAI");
	}
	function enabledEntrega(tpSituacao) {
		return (tpSituacao == "PBRC" || tpSituacao == "PRCO" || tpSituacao == "PBCO" || tpSituacao == "PCOM");
	}
	function enabledReembolso(tpSituacao) {
		return (tpSituacao == "PBRC" || tpSituacao == "PRCO" || tpSituacao == "PBRE" || tpSituacao == "PREC");
	}
	
	function generateColumns() {
		colunas = '<table class="Form" cellpadding="0" cellspacing="0" width="98%><tr>';
		for (i = 0 ; i < 33 ; i++) {
			colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td>';
			colunas += '<td><img src="lms/images/spacer.gif" width="8px" height="1px"></td><td><img src="lms/images/spacer.gif" width="8px" height="1px"></td>';
		}
		colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td></tr>';
		return colunas;
	}
	
	
	/**
	 * FUNCOES RELATIVAS AO CONFIRMAR
	 */
	var isValidRemetente = true;
	var isValidDestinatario = true;
	var isValidReembolso = false;
	var isValidEntrega = false;
	var CONFIRM_COMPROVANTE_ENTREGA = "COMPROVANTE_ENTREGA_CONFIRMADO";
	var	CONFIRM_REEMBOLSO = "REEMBOLSO_CONFIRMADO";
	
	 function storeDocto_cb(data,error,key) {
		 				 
		if(data != undefined && getNestedBeanPropertyValue(data,"LMS-09143") == "true" ){
			alert("LMS-09143 -" + i18NLabel.getLabel("LMS-09143"));
		}
		 
		if (key != undefined && key.substr(0,3) == "PCE") {
			var explode = key.split("_");
			if (explode[1] == "R")
				isValidRemetente = false;
			else
				isValidDestinatario = false;
			showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + explode[2] + '&cmd=pop',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
		}else if (key == "LMS-01097") {
			var explode = error.split("-");
			showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + explode[3] + '&cmd=pop',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');			
		}else{
			if (error)
				alert(error);
			else{
				if (getNestedBeanPropertyValue(data,CONFIRM_COMPROVANTE_ENTREGA) == "true" && !isValidEntrega) {
					if (confirm(i18NLabel.getLabel("LMS-09057"))) {
						isValidEntrega = true;
						storeDocto(data);
					}
					return;					
				}
				if (getNestedBeanPropertyValue(data,CONFIRM_REEMBOLSO) == "true" && !isValidReembolso) {
					if (confirm(i18NLabel.getLabel("LMS-09060"))) {
						isValidReembolso = true;
						storeDocto(data);
					}
					return;
				}
				dialogArguments.window.DoctoServicoGridDef.executeLastSearch();
				self.close();
			}
		}
	}
	
	function dataLoadSamples_cb(data) {
		fillFormWithFormBeanData(document.forms[0].tabIndex, data);
	}
	
	
	function storeDocto(data) {
		setNestedBeanPropertyValue(data,"isValidExistPceRemetente",isValidRemetente);
		setNestedBeanPropertyValue(data,"isValidExistPceDestinatario",isValidDestinatario);		
		
		var sdo = createServiceDataObject("lms.entrega.fecharManifestoEntregasAction.store","storeDocto",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function storeDoctoClick(data) {
		storeDocto(buildFormBeanFromForm(document.forms[0]));	
		isValidEntrega = false;
		isValidReembolso = false;
	}
	
	/**
	 *	LOOKUP DE OCORRENCIA DE ENTREGA
	 */
	 
	 function dataLoadOcorrencia_cb(data) {
		ocorrenciaEntrega_cdOcorrenciaEntrega_exactMatch_cb(data);
		if (data != undefined && data.length == 1)
			validateTpOcorrencia(getNestedBeanPropertyValue(data[0],"tpOcorrencia.value"));
	}

	function changeOcorrencia() {
		var flag = ocorrenciaEntrega_cdOcorrenciaEntregaOnChangeHandler();
		if (getElementValue("ocorrenciaEntrega.idOcorrenciaEntrega") == "") {
			validateTpOcorrencia("");	
		}
		
		validateTpEntregaParcial();
				
		return flag;
	}
	
	function validateTpEntregaParcial() {
		if (getElementValue("ocorrenciaEntrega.cdOcorrenciaEntrega") == 1) {
			setDisabled("tpEntregaParcial",false);
		} else {
			setDisabled("tpEntregaParcial",true);
			resetValue("tpEntregaParcial");
		}
	}

	function setPopUpOcorrencia(data) {
		validateTpOcorrencia(getNestedBeanPropertyValue(data,"tpOcorrencia.value"));
		
		if (getNestedBeanPropertyValue(data,"cdOcorrenciaEntrega") == 1) { // Entrega realizada
			setDisabled("tpEntregaParcial",false);
		} else {
			setDisabled("tpEntregaParcial",true);
			resetValue("tpEntregaParcial");
		}
		
		return true;
	}

	function validateTpOcorrencia(tpOcorrencia) {
		var tpSituacao = getElementValue("tpSituacaoDocumento");
		if (tpOcorrencia == "" || tpOcorrencia == "E") {
			document.getElementById("comproEntrega").style.display = ((enabledEntrega(tpSituacao)) ? "" : "none");
			document.getElementById("reembolso").style.display = ((enabledReembolso(tpSituacao)) ? "" : "none");
		}else{
			document.getElementById("comproEntrega").style.display = "none";
			document.getElementById("reembolso").style.display = "none";
		}
	}
	
	function dataLoadOcorrenciaEntrega_cb(data) {
		dataLoadOcorrencia_cb(data);
		setFocus("ocorrenciaEntrega.cdOcorrenciaEntrega");
		document.getElementById("ocorrenciaEntrega.idOcorrenciaEntrega").callBack = "dataLoadOcorrencia";
	}
//-->
</script>
<adsm:window service="lms.entrega.fecharManifestoEntregasAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="entrega/fecharManifestoEntregas" height="340">
		
		<adsm:hidden property="tpSituacao" value="A" serializable="false"/>
		<adsm:hidden property="tpSituacaoDocumento"/>
		<adsm:hidden property="idManifestoEntregaDocumento"/>
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-09057"/>
			<adsm:include key="LMS-09060"/>
			<adsm:include key="LMS-09143"/>
		</adsm:i18nLabels>

		<adsm:textbox property="doctoServico.tpDocumentoServico" dataType="text" size="5" label="documentoServico" labelWidth="17%" width="83%" serializable="false" disabled="true">
			<adsm:textbox dataType="text" property="doctoServico.sgFilial" size="3" disabled="true" serializable="false"/>
			<adsm:textbox dataType="integer" property="doctoServico.nrDoctoServico" size="12" mask="00000000" serializable="false" disabled="true"/>
			<adsm:hidden property="doctoServico.id"/>
		</adsm:textbox>

		<adsm:textbox dataType="text" property="remetente.nrIdentificacao" size="17" width="33%" label="remetente" disabled="true" labelWidth="17%" serializable="false">
			<adsm:textbox dataType="text" property="remetente.nmPessoa" size="23" disabled="true" serializable="false"  />
			<adsm:hidden property="remetente.id"/>
		</adsm:textbox>
		<adsm:textbox dataType="text" property="destinatario.nrIdentificacao" size="17" width="33%" label="destinatario" disabled="true" labelWidth="17%">
			<adsm:textbox dataType="text" property="destinatario.nmPessoa" size="23" disabled="true" serializable="false"/>
			<adsm:hidden property="destinatario.id"/>
		</adsm:textbox>


	<tr>
		<td colspan="100">
			<div id="infoBaixas" style="display:none;border:none">
				<script type="text/javascript">
					<!--
					document.write(generateColumns());
					//-->
				</script>

					<adsm:section caption="informacoesBaixa"/>
			
					<adsm:lookup service="lms.entrega.fecharManifestoEntregasAction.findLookupOcorrenciaEntrega" dataType="integer" property="ocorrenciaEntrega" 
				 		criteriaProperty="cdOcorrenciaEntrega" idProperty="idOcorrenciaEntrega" label="ocorrencia" labelWidth="17%" width="33%" required="true"
				 		exactMatch="true" minLengthForAutoPopUpSearch="3" size="3" maxLength="3" action="/entrega/manterOcorrenciasEntrega"
				 		onDataLoadCallBack="dataLoadOcorrenciaEntrega" onPopupSetValue="setPopUpOcorrencia" onchange="return changeOcorrencia();">
						<adsm:propertyMapping modelProperty="dsOcorrenciaEntrega" relatedProperty="ocorrenciaEntrega.dsOcorrenciaEntrega"/>
						<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao"/>
						<adsm:propertyMapping modelProperty="tpOcorrencia.value" relatedProperty="ocorrenciaEntrega.tpOcorrencia.value"/>						
						<adsm:textbox dataType="text" size="30" maxLength="60" property="ocorrenciaEntrega.dsOcorrenciaEntrega" disabled="true" serializable="false"/>
						<adsm:hidden property="ocorrenciaEntrega.tpOcorrencia.value" serializable="true"/>
					</adsm:lookup>
			
					<adsm:textbox dataType="JTDateTimeZone" property="dhOcorrencia"
							width="33%" label="dataHoraOcorrencia" labelWidth="17%" disabled="false"/>
							
					<adsm:combobox domain="DM_TIPO_ENTREGA_PARCIAL" label="entrega" labelWidth="17%" width="34%" boxWidth="200" property="tpEntregaParcial" renderOptions="true" />
					
					<adsm:textbox dataType="text" property="nmRecebedor"
							size="35" width="83%" maxLength="60" label="recebedor" labelWidth="17%"/>

					<adsm:textarea property="obManifesto" label="observacao"
							maxLength="400" rows="2" columns="97"  labelWidth="17%" width="83%"/>
				</table>
			</div>

			<div id="comproEntrega" style="display:none;border:none">
				<script type="text/javascript">
					<!--
					document.write(generateColumns());
					//-->
				</script>

					<adsm:section caption="comprovanteEntrega"/>
						<adsm:checkbox property="blRecolhido" label="recolhido" width="83%" labelWidth="17%"/>
			
						<adsm:textbox dataType="integer" property="nrRecolhido" size="19" width="83%" maxLength="20" label="numero" labelWidth="17%"/>
			
						<adsm:textarea property="obRecolhido" label="observacao" maxLength="120" rows="2" columns="97" labelWidth="17%" width="83%"/>
				</table>
			</div>

			<div id="reembolso" style="display:none;border:none">
				<script type="text/javascript">
					<!--
					document.write(generateColumns());
					//-->
				</script>

					<adsm:section caption="reembolso"/>
			
						<adsm:checkbox property="blCheques" label="chequesRecebidos" labelWidth="17%"/>
			
						<adsm:textarea property="obCheques" label="observacao" maxLength="120" rows="2" columns="97" labelWidth="17%" width="83%"/>
				</table>
			</div>
			
			<div id="retencao" style="display:none;border:none">
				<script type="text/javascript">
					<!--
					document.write(generateColumns());
					//-->
				</script>
				<adsm:section caption="retencao" />
				
					<adsm:checkbox property="blRetencaoComprovanteEnt" label="retencaoComprovanteEntrega"
							labelWidth="24%" width="76%" serializable="false"/>
				
				</table>
			</div>
		</td>

		<adsm:buttonBar>
				<adsm:button caption="salvarDocumento" disabled="false" onclick="storeDoctoClick()"/>
				<adsm:button caption="cancelar" disabled="false" onclick="self.close()"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>