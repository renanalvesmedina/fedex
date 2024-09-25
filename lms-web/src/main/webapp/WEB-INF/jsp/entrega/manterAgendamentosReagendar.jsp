<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="branco" service="lms.entrega.manterAgendamentosAction" onPageLoadCallBack="manterAgendamentoReagendar" >
	<adsm:form action="/entrega/manterAgendamentos" onDataLoadCallBack="formLoad" >

		<adsm:hidden property="blReagendamento" value="true"/>
		<adsm:hidden property="idAgendamentoEntrega"/>
		<adsm:hidden property="filial.idFilial"/>
		<adsm:hidden property="tpAgendamento"/>		
		<adsm:hidden property="documentoServicoAgendamento_doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
		
		<%--		
		<adsm:hidden property="nmContato"/>		
		--%>

		<adsm:i18nLabels>
			<adsm:include key="LMS-09124"/>
		</adsm:i18nLabels>
	
		<adsm:label key="branco" width="100%" style="height:5px;border:none"/>
	
		<adsm:section caption="reagendamento"/>
		
		<adsm:listbox label="chaveNfe" property="nrChaveNfe" optionProperty="" optionLabelProperty="nrChave" size="3" boxWidth="274" 
			labelWidth="20%" width="60%" labelStyle="vertical-align:top">
			<adsm:textbox property="nrChave" dataType="integer"	maxLength="44" serializable="false"	size="50" onchange="return findChaveNfe()" />
		</adsm:listbox>

		<adsm:listbox size="3" boxWidth="200" 
					  label="documentoServico"
					  labelStyle="vertical-align:top"					  
					  labelWidth="20%"
					  width="33%"					  
					  property="documentoServicoAgendamento"
					  optionProperty="idDoctoServico"
					  optionLabelProperty="nrDoctoServico" 
					  onContentChange="validaDoctoServico"
					  onchange="return changeListDoctoServico();">
					  
			<adsm:combobox property="doctoServico.tpDocumentoServico" serializable="true"
						   service="lms.entrega.manterAgendamentosAction.findTpDoctoServico" 
						   optionProperty="value" optionLabelProperty="description"
						   boxWidth="70"
						   onchange="return changeTpDoctoServico(this);"
						   renderOptions="true">

				<adsm:lookup dataType="text" serializable="true"
							 property="doctoServico.filialByIdFilialOrigem"
						 	 idProperty="idFilial" criteriaProperty="sgFilial" 
							 service="" 
							 popupLabel="pesquisarDocumentoServico"
							 disabled="true"
							 action="/municipios/manterFiliais" 
							 size="3" maxLength="3" picker="false" 
							 onchange="var r = changeDocumentWidgetFilial({
												 filialElement:document.getElementById('documentoServicoAgendamento_doctoServico.filialByIdFilialOrigem.idFilial'), 
												 documentNumberElement:document.getElementById('documentoServicoAgendamento_doctoServico.idDoctoServico')
											  	}); 							
									  	return r;"/>
				<adsm:hidden property="blBloqueado" value="N"/>
				<adsm:lookup dataType="integer"
							 property="doctoServico" 
							 popupLabel="pesquisarDocumentoServico"
							 idProperty="idDoctoServico" 
							 criteriaProperty="nrDoctoServico" 
							 service="" 
							 action="" 						 
							 size="12" 
							 maxLength="8" 
							 mask="00000000" 
							 serializable="true" 
							 disabled="true">
				</adsm:lookup>
			</adsm:combobox>		
		</adsm:listbox>

		<adsm:combobox service="lms.entrega.manterAgendamentosAction.findMotivoAgendamento" labelWidth="20%" width="30%" required="true" property="motivo"  label="motivo"  optionProperty="idMotivoAgendamento"  optionLabelProperty="dsMotivoAgendamento" onlyActiveValues="true" boxWidth="200" />
		<adsm:textbox dataType="JTDate" label="dataAgendamento" property="dtAgendamento" maxLength="50" disabled="false" labelWidth="20%" width="80%" required="true" />

		<adsm:combobox service="lms.entrega.manterAgendamentosAction.findTurnoByIdFilial" labelWidth="20%" width="30%" required="false" optionProperty="idTurno" optionLabelProperty="dsTurno" property="turno.idTurno" label="turno" onlyActiveValues="true"  onDataLoadCallBack="tuno" boxWidth="220" >
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
		</adsm:combobox>
		

		<adsm:range label="preferencia" labelWidth="20%" width="30%" >
			<adsm:textbox dataType="JTTime" property="hrPreferenciaInicial"/>
			<adsm:textbox dataType="JTTime" property="hrPreferenciaFinal"/>
		</adsm:range>
		
		<adsm:checkbox label="cartaoCredito" property="blCartao" disabled="false" labelWidth="20%" width="80%" />
		
		<adsm:textbox dataType="text" label="contato" size="35" property="nmContato" maxLength="60"  labelWidth="20%" width="30%" required="false"/>
		<adsm:textbox dataType="text" label="ddd" size="5" property="nrDdd" maxLength="5" labelWidth="5%" width="15%" required="true"/>
		<adsm:textbox dataType="text" label="telefone" size="10" property="nrTelefone" maxLength="10" labelWidth="10%" width="20%" required="true"/>
		<adsm:textbox dataType="text" label="ramal" size="15" property="nrRamal" maxLength="10" labelWidth="20%" width="30%" required="false"/>
		
		<adsm:textarea label="observacao" property="obAgendamentoEntrega" maxLength="500" style="width:463px" disabled="false" required="true" labelWidth="20%" width="80%" />
		<adsm:buttonBar freeLayout="false">
			<adsm:storeButton id="storeButton" callbackProperty="salvar" />
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
			
<script>

	var turnoGlobal = 0;
	
	function tuno_cb(data) {
		turno_idTurno_cb(data);
		setElementValue("turno.idTurno", turnoGlobal);
	}

	function salvar_cb(data,error,key) {
		if (error) {
			alert(error);
		} else {
			if(!data.agendamentoPorNFe || data.agendamentoPorNFe == null || data.agendamentoPorNFe == ""){
				var idRetornoPCEDestinatario;
				var idRetornoPCERemetente;
					
				var i=0
				
				var array = new Array();
				
				array = getNestedBeanPropertyValue(data,"list");
				
				for (i=0; i < array.length; i++)	{

					idRetornoPCEDestinatario = getNestedBeanPropertyValue(array[i],"idRetornoPCEDestinatario");
					idRetornoPCERemetente = getNestedBeanPropertyValue(array[i],"idRetornoPCERemetente");

					if (idRetornoPCEDestinatario != null && idRetornoPCEDestinatario != "") {
						if (key != undefined && key.substr(0,3) == "PCE") {
							var explode = key.split("_");
							showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + explode[2] + '&cmd=pop',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
						}else if (key == "LMS-01097") {
							var explode = error.split("-");
							showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + explode[3] + '&cmd=pop',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
						}else if (error) {
							alert(error);						
						} else {
							showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + idRetornoPCEDestinatario + '&cmd=pop',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
						}
					} 
					
					if (idRetornoPCERemetente != null && idRetornoPCERemetente != "") {
						if (key != undefined && key.substr(0,3) == "PCE") {
							var explode = key.split("_");
							showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + explode[2] + '&cmd=pop',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
						}else if (key == "LMS-01097") {
							var explode = error.split("-");
							showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + explode[3] + '&cmd=pop',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
						}else if (error) {
							alert(error);
						} else {
							showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + idRetornoPCERemetente + '&cmd=pop',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
						}
					} 
				
				}
			}
			
			var sdo = createServiceDataObject("lms.entrega.manterAgendamentosAction.findById", "loadBasicData", {id:getNestedBeanPropertyValue(data,"idAgendamentoEntrega")});
	    	xmit({serviceDataObjects:[sdo]});
	    	
		}
	}

	function loadBasicData_cb(data) {
		window.dialogArguments.formLoad_cb(data);
		self.close();
	}
	
	// LMS-5949 - reagendamento com NF-e
	function loadNotas_cb(data) {
		if(data != undefined && data.chavesNfe != undefined && data.chavesNfe.length != 0){
			setDisabled("nrChaveNfe", false);	
			setDisabled("nrChaveNfe_nrChave", false);
			
			for (var i = 0;  i < data.chavesNfe.length;  i++) {
				setElementValue("nrChaveNfe_nrChave", data.chavesNfe[i]);
				nrChaveNfeListboxDef.insertOrUpdateOption();
			}
			setDisabled("nrChaveNfe", true);	
			setDisabled("nrChaveNfe_nrChave", true);
			
			setDisabled("documentoServicoAgendamento", true);
			setDisabled("documentoServicoAgendamento_doctoServico.filialByIdFilialOrigem.idFilial", true);
	 		setDisabled("documentoServicoAgendamento_doctoServico.filialByIdFilialOrigem.sgFilial", true);
	 		setDisabled("documentoServicoAgendamento_doctoServico.tpDocumentoServico", true);
			setDisabled("documentoServicoAgendamento_doctoServico.idDoctoServico", true);  
		}
	}

	function manterAgendamentoReagendar_cb() {
		onPageLoad_cb();
		
		setDisabled("nrChaveNfe", true);	
		setDisabled("nrChaveNfe_nrChave", true);
		
		setDisabled("btnFechar",false);
		setElementValue("idAgendamentoEntrega", getElementValue(window.dialogArguments.document.forms[0].elements["idAgendamentoEntrega"]));
		setElementValue("tpAgendamento", getElementValue(window.dialogArguments.document.forms[0].elements["tpAgendamento"]));
		setElementValue("filial.idFilial", getElementValue(window.dialogArguments.document.forms[0].elements["filial.idFilial"]));
		setElementValue("nmContato", getElementValue(window.dialogArguments.document.forms[0].elements["nmContato"]));
		setElementValue("nrDdd", getElementValue(window.dialogArguments.document.forms[0].elements["nrDdd"]));
		setElementValue("nrTelefone", getElementValue(window.dialogArguments.document.forms[0].elements["nrTelefone"]));
		setElementValue("nrRamal", getElementValue(window.dialogArguments.document.forms[0].elements["nrRamal"]));

		
		// LMS-5949 - reagendamento com NF-e
		var idAg = getElementValue("idAgendamentoEntrega");
		var sdo = createServiceDataObject("lms.entrega.manterAgendamentosAction.findById", "loadNotas", {idAg:idAg});
	    xmit({serviceDataObjects:[sdo]});
		
		var data = getElementValue(window.dialogArguments.document.forms[0].elements["documentoServico"]);
		documentoServicoAgendamentoListboxDef.renderOptions({documentoServicoAgendamento:data});

		notifyElementListeners({e:document.getElementById("filial.idFilial")});	

		regraTipoAgendamento();
		regraReagendamento();
		
		// LMS 4599 -- Se for empresa parceira desabilita o componente de docto de servico
		var url = new URL(document.location.href);
		var tipoEmp = url.parameters['tpEmpresa'];
	 	if(tipoEmp != null && tipoEmp == 'P'){	
	 		setDisabled("documentoServicoAgendamento", true);
	 		setDisabled("documentoServicoAgendamento_doctoServico.filialByIdFilialOrigem.idFilial", true);
	 		setDisabled("documentoServicoAgendamento_doctoServico.filialByIdFilialOrigem.sgFilial", true);
	 		setDisabled("documentoServicoAgendamento_doctoServico.tpDocumentoServico", true);
			setDisabled("documentoServicoAgendamento_doctoServico.idDoctoServico", true);  
		}
		
	}
	
	function regraTipoAgendamento() {
		document.getElementById("turno.idTurno").required = new String(false);
		setDisabled("turno.idTurno", false);
		setDisabled("blCartao", false);
		setDisabled("dtAgendamento", false);
		setDisabled("hrPreferenciaInicial", false);
		setDisabled("hrPreferenciaFinal", false);
		document.getElementById("hrPreferenciaInicial").required = new String(false);
		document.getElementById("hrPreferenciaFinal").required = new String(false);
		
		if (getElementValue("tpAgendamento") == "TA") {
			
			setElementValue("turno.idTurno", "");
			setElementValue("hrPreferenciaInicial", "");
			setElementValue("hrPreferenciaFinal", "");				
			
			if (getElementValue("idAgendamentoEntrega") == null || getElementValue("idAgendamentoEntrega") == "" || getElementValue("idAgendamentoEntrega") == undefined ) {
				setElementValue("tpSituacaoAgendamento", "F");
				tpSituacaoAgendamentoGlobal = 'F';
			}
		} else if (getElementValue(window.dialogArguments.document.forms[0].elements["tpAgendamento"]) == "AT") {
			document.getElementById("turno.idTurno").required = new String(true);
			setDisabled("hrPreferenciaInicial", true);
			setDisabled("hrPreferenciaFinal", true);
			setElementValue("hrPreferenciaInicial", "");
			setElementValue("hrPreferenciaFinal", "");
		} else	if (getElementValue(window.dialogArguments.document.forms[0].elements["tpAgendamento"]) == "AH") {
			setDisabled("turno.idTurno", true);
			setElementValue("turno.idTurno", "");
			document.getElementById("hrPreferenciaInicial").required = new String(true);
			document.getElementById("hrPreferenciaFinal").required = new String(true);
		}		
	}
	
	function regraReagendamento() {
		var reagendamento = getElementValue(window.dialogArguments.document.forms[0].elements["reagendamento.idAgendamentoEntrega"]);
		
		if ( reagendamento != null && reagendamento != "") {

			turnoGlobal = getElementValue(window.dialogArguments.document.forms[0].elements["reagendamento.idTurno"]);
			setElementValue("motivo", getElementValue(window.dialogArguments.document.forms[0].elements["reagendamento.idMotivoReagendamento"]));

			setElementValue("hrPreferenciaInicial", setFormat("hrPreferenciaInicial", getElementValue(window.dialogArguments.document.forms[0].elements["reagendamento.hrPreferenciaInicial"])));
			setElementValue("hrPreferenciaFinal", setFormat("hrPreferenciaFinal", getElementValue(window.dialogArguments.document.forms[0].elements["reagendamento.hrPreferenciaFinal"])));
			setElementValue("dtAgendamento", setFormat("dtAgendamento", getElementValue(window.dialogArguments.document.forms[0].elements["reagendamento.dtAgendamento"])));
			setElementValue("blCartao", setFormat("blCartao", getElementValue(window.dialogArguments.document.forms[0].elements["reagendamento.blCartao"])));
			setElementValue("obAgendamentoEntrega", setFormat("obAgendamentoEntrega", getElementValue(window.dialogArguments.document.forms[0].elements["reagendamento.obAgendamentoEntrega"])));
			setElementValue("nmContato", setFormat("nmContato", getElementValue(window.dialogArguments.document.forms[0].elements["reagendamento.nmContato"])));
			setElementValue("nrDdd", setFormat("nrDdd", getElementValue(window.dialogArguments.document.forms[0].elements["reagendamento.nrDdd"])));
			setElementValue("nrTelefone", setFormat("nrTelefone", getElementValue(window.dialogArguments.document.forms[0].elements["reagendamento.nrTelefone"])));
			setElementValue("nrRamal", setFormat("nrRamal", getElementValue(window.dialogArguments.document.forms[0].elements["reagendamento.nrRamal"])));
		
			setDisabled(document.forms[0], true);
			setDisabled("btnFechar", false);
		} else {
			setDisabled("storeButton", false);
		}
					
	}
	
	function changeListDoctoServico() {
		var url = new URL(document.location.href);
		var tipoEmp = url.parameters['tpEmpresa'];
		// Habilita campos se estamos inserindo um agendamento.
		if (getElementValue("idAgendamentoEntrega") == "" && tipoEmp != "P") {
			setDisabled("documentoServicoAgendamento_doctoServico.filialByIdFilialOrigem.idFilial",false);
			setDisabled("documentoServicoAgendamento_doctoServico.idDoctoServico",false);
		}
		return true;
	}

	function changeTpDoctoServico(field) {
		
		var flag = changeDocumentWidgetType({
							   documentTypeElement:document.getElementById("documentoServicoAgendamento_doctoServico.tpDocumentoServico"), 
							   filialElement:document.getElementById('documentoServicoAgendamento_doctoServico.filialByIdFilialOrigem.idFilial'), 
							   documentNumberElement:document.getElementById('documentoServicoAgendamento_doctoServico.idDoctoServico'), 
							   documentGroup:'DOCTOSERVICE',
							   parentQualifier:'documentoServicoAgendamento_doctoServico',
							   actionService:'lms.entrega.manterAgendamentosAction'});

		resetValue(document.getElementById("documentoServicoAgendamento_doctoServico.idDoctoServico"));
		
		if (field.value != '') {
			changeDocumentWidgetFilial({
									 	filialElement:document.getElementById('documentoServicoAgendamento_doctoServico.filialByIdFilialOrigem.idFilial'), 
									 	documentNumberElement:document.getElementById('documentoServicoAgendamento_doctoServico.idDoctoServico')
								  		});
		}
		
		document.getElementById("documentoServicoAgendamento").selectedIndex = -1;
		
		return flag;
	}
	
	function validaDoctoServico(eventObj){
		var data = getElementValue("documentoServicoAgendamento");
		
		//valida se esta adicionando um documento do agendamento original
		if (eventObj.name == 'modifyButton_click') {
			//pega o id do documento de servico a ser adicionado
			var idDoctoServico = getElementValue("documentoServicoAgendamento_doctoservico.idDoctoServico");
			
			//pega os documentos do agendamento
			var data = getElementValue(window.dialogArguments.document.forms[0].elements["documentoServico"]);
			if (data!=undefined && data.length > 0){
				var found = false;
				for (var index = 0; index < data.length;index++){
					if (idDoctoServico == data[index].doctoServico.idDoctoServico){
						found = true;
						break;
					}
				}
				
				if (!found){
					alert(i18NLabel.getLabel("LMS-09124"));
					return false;	
				}
			}
				
		}
		
		
	}
	
</script>