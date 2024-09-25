<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>
	
	/**
	 * Como são usados divs, é necessário a função para gerar 100 colunas dentro da table no div.
	 */
	function geraColunas() {
		colunas = '<table class="Form" cellpadding="0" cellspacing="0" width="98%><tr>';
		for (i = 0 ; i < 33 ; i++) {
			colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td>';
			colunas += '<td><img src="lms/images/spacer.gif" width="8px" height="1px"></td><td><img src="lms/images/spacer.gif" width="8px" height="1px"></td>';
		}
		colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td></tr>';

		return colunas;	
	}

</script>


<adsm:window service="lms.entrega.manterAgendamentosAction" onPageLoadCallBack="pageLoad" >
	<adsm:form action="/entrega/manterAgendamentos" idProperty="idAgendamentoEntrega" height="368" onDataLoadCallBack="formLoad">
	
	<adsm:i18nLabels>
		<adsm:include key="LMS-09105"/>
        <adsm:include key="LMS-09106"/>
		<adsm:include key="LMS-04400"/>
		<adsm:include key="LMS-09133"/>
		<adsm:include key="LMS-09131"/>
        <adsm:include key="LMS-09128"/>
   	</adsm:i18nLabels>
	
	<adsm:hidden property="idProcessoWorkflow" serializable="false"/>
	
	<td colspan="100" >
	
	<div id="principal" style="display:;border:none;">
	
	<script>
		document.write(geraColunas()); 
	</script>	
			
		<adsm:hidden property="apagarEmails" value="S"/>	
		<adsm:hidden property="tpSituacao" value="A"/>
		<adsm:hidden property="reagendamento.idMotivoReagendamento"/>
		<adsm:hidden property="reagendamento.idTurno"/>
		<adsm:hidden property="reagendamento.idAgendamentoEntrega"/>
		<adsm:hidden property="reagendamento.hrPreferenciaInicial"/>
		<adsm:hidden property="reagendamento.hrPreferenciaFinal"/>
		<adsm:hidden property="reagendamento.dtAgendamento"/>
		<adsm:hidden property="reagendamento.blCartao"/>
		<adsm:hidden property="reagendamento.nmContato"/>
		<adsm:hidden property="reagendamento.nrDdd"/>
		<adsm:hidden property="reagendamento.nrTelefone"/>
		<adsm:hidden property="reagendamento.nrRamal"/>
		<adsm:hidden property="reagendamento.obAgendamentoEntrega"/>
		<adsm:hidden property="usuarioByIdUsuarioCriacao.idUsuario"/>
		<adsm:hidden property="idFilialDestinoOperacional"/>
		<adsm:hidden property="empresa.tpEmpresa" value="M"/>
		
		<adsm:hidden property="tipoEmpresa"/>
		
		<adsm:hidden property="remetente.idCliente"/>
		<adsm:hidden property="remetente.pessoa.nrIdentificacao"/>
		<adsm:hidden property="remetente.pessoa.nmPessoa"/>

		<adsm:hidden property="filialOrigem.idFilial"/>
		<adsm:hidden property="filialOrigem.sgFilial"/>
		<adsm:hidden property="filialOrigem.pessoa.nmFantasia"/>
			
		<adsm:hidden property="cdLocalizacaoMercadoria"/>
			
        <adsm:lookup property="filial" 
        			 idProperty="idFilial" 
        			 criteriaProperty="sgFilial" 
        			 service="lms.entrega.manterAgendamentosAction.findLookupFilial" 
        			 dataType="text"  
        			 label="filialAgendamento"         			 
        			 size="3" 
        			 action="/municipios/manterFiliais" 
        			 minLengthForAutoPopUpSearch="3" 
        			 exactMatch="true" 
        			 maxLength="3"         			 
        			 required="true"
        			 labelWidth="17%" 
        			 onDataLoadCallBack="filialDataLoad"
        			 onchange="return onChangeFilial(this)"
        			 width="33%"
        			 autoTab="false"
        			 afterPopupSetValue="filialPopupSetValue" >  
			<%--adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa" /--%>
            <adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="25" maxLength="50" disabled="true"/>
        </adsm:lookup>

		<adsm:textbox dataType="text" label="registradoPor" size="25"
				property="usuarioByIdUsuarioCriacao.nmUsuario" maxLength="50" 
				labelWidth="17%" width="33%" required="true" disabled="true"/>

	</table>
	</div>
<%----------------------------------------------------------------------------------------------%>        
<%--Dados do agendamento------------------------------------------------------------------------%>
<%----------------------------------------------------------------------------------------------%>	
	<div id="dadosPrincipais" style="display:none;border:none;">
	<script>
		document.write(geraColunas());
	</script>

		<adsm:lookup label="destinatario" 
					 idProperty="idCliente" 
					 property="destinatario" 
					 criteriaProperty="pessoa.nrIdentificacao"
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.entrega.manterAgendamentosAction.findLookupDestinatario"
					 action="/vendas/manterDadosIdentificacao" 
					 dataType="text" 
					 exactMatch="true"
					 required="true"
					 size="20" 
					 onPopupSetValue="onPopUpDestinatario"
					 onDataLoadCallBack="habilitaConsultarDocumentoButtonDestinatario"
					 onchange="return onChangeDestinatario();"
					 maxLength="20" 
					 labelWidth="17%"			
					 width="83%" >			
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="destinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="destinatario.pessoa.nmPessoa" size="40" maxLength="50" disabled="true" />			
			<adsm:button caption="incluirDocumentos" id="consultarDocumentoButton" disabled="false" onclick="chamarConsultarDocumento()" />			
		</adsm:lookup>

		<adsm:combobox labelWidth="17%" 
					   width="60%" 
					   required="false" 
					   property="tpDocumento" 
					   label="tipoDocumento" 
					   boxWidth="200"
					   domain="DM_TP_DOCTO" 
					   renderOptions="true"
					   onchange="return changeTpDocumento(this);" /> 
					   
		<adsm:listbox label="chaveNfe" property="nrChaveNfe" optionProperty="idNotaFiscalColeta" optionLabelProperty="nrChave" size="3" boxWidth="274" 
			labelWidth="17%" width="50%" labelStyle="vertical-align:top" onContentChange="contentChange" >
			<adsm:textbox property="nrChave" dataType="integer"	maxLength="44" serializable="false"	size="50" onchange="return findChaveNfe()" />
		</adsm:listbox>

		<!-- ---------------------------------------- -->
		<adsm:hidden property="idDoctoServico" serializable="true"/>
		<adsm:hidden property="dsTpDocumentoServico" serializable="true"/>
		<adsm:hidden property="sgFilialOrigemDoctoServico" serializable="true"/>
		<adsm:hidden property="documentoServico_doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
		<adsm:hidden property="nrDoctoServico" serializable="true"/>

		<adsm:listbox size="3" boxWidth="200" 
					  label="documentoServico"
					  labelStyle="vertical-align:top"					  
					  labelWidth="17%"
					  width="33%"					  
					  property="documentoServico"
					  onContentChange="validaDoctoServico"
					  optionProperty="idDoctoServico"
					  optionLabelProperty="nrDoctoServico" 
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
												 filialElement:document.getElementById('documentoServico_doctoServico.filialByIdFilialOrigem.idFilial'), 
												 documentNumberElement:document.getElementById('documentoServico_doctoServico.idDoctoServico')
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
		
		<!-- ---------------------------------------- -->		
		
		<adsm:lookup label="notaFiscal" 
					 action="/expedicao/consultarNotaFiscalCliente" 
					 service="lms.entrega.manterAgendamentosAction.findLookupNotaFiscalCliente"
					 dataType="integer" 
					 property="notaFiscalCliente"  
					 idProperty="idNotaFiscalConhecimento" 
					 labelWidth="17%" 
					 width="33%" 
					 criteriaProperty="nrNotaFiscal" 
					 labelStyle="vertical-align:top"
					 mask="000000000" 
					 maxLength="30" 
					 exactMatch="false" 
					 disabled="true"
					 minLengthForAutoPopUpSearch="3"
					 popupLabel="pesquisarNotaFiscal"
					 onPopupSetValue="notaFiscalPopup">					 
					 			
				<adsm:propertyMapping criteriaProperty="destinatario.idCliente" modelProperty="destinatario.idCliente" />
				<adsm:propertyMapping criteriaProperty="destinatario.pessoa.nrIdentificacao" modelProperty="destinatario.pessoa.nrIdentificacao" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="destinatario.pessoa.nmPessoa" modelProperty="destinatario.pessoa.nmPessoa" inlineQuery="false"/>
				
				<adsm:propertyMapping criteriaProperty="filialDestino.idFilial" modelProperty="filialDestino.idFilial" />
				<adsm:propertyMapping criteriaProperty="filialDestino.sgFilial" modelProperty="filialDestino.sgFilial" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="filialDestino.pessoa.nmFantasia" modelProperty="filialDestino.pessoa.nmFantasia" inlineQuery="false"/>
				
				<adsm:propertyMapping criteriaProperty="documentoServico_doctoServico.idDoctoServico" modelProperty="doctoServico.idDoctoServico" />
				<adsm:propertyMapping criteriaProperty="documentoServico_doctoServico.tpDocumentoServico" modelProperty="doctoServico.tpDocumentoServico" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="documentoServico_doctoServico.filialByIdFilialOrigem.idFilial" modelProperty="doctoServico.filialByIdFilialOrigem.idFilial" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="documentoServico_doctoServico.filialByIdFilialOrigem.sgFilial" modelProperty="doctoServico.filialByIdFilialOrigem.sgFilial" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="documentoServico_doctoServico.nrDoctoServico" modelProperty="doctoServico.nrDoctoServico" inlineQuery="false"/>

				<adsm:propertyMapping criteriaProperty="documentoServico_doctoServico.filialByIdFilialOrigem.idFilial" modelProperty="filialOrigem.idFilial" />
				<adsm:propertyMapping criteriaProperty="documentoServico_doctoServico.filialByIdFilialOrigem.sgFilial" modelProperty="filialOrigem.sgFilial" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="documentoServico_doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="filialOrigem.pessoa.nmFantasia" inlineQuery="false"/>
					 
				<adsm:propertyMapping relatedProperty="documentoServico_doctoServico.idDoctoServico" modelProperty="idDoctoServico" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="documentoServico_doctoServico.nrDoctoServico" modelProperty="nrDoctoServico" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="documentoServico_doctoServico.tpDocumentoServico" modelProperty="tpDocumentoServico.value" blankFill="false"/>
				
				<adsm:propertyMapping relatedProperty="documentoServico_doctoServico.filialByIdFilialOrigem.idFilial" modelProperty="idFilialOrigem" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="documentoServico_doctoServico.filialByIdFilialOrigem.sgFilial" modelProperty="sgFilialOrigem" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="documentoServico_doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="nmFantasiaOrigem" blankFill="false"/>
					 
		</adsm:lookup>
		
        <adsm:lookup property="filialDestino" 
        			 idProperty="idFilial" 
        			 criteriaProperty="sgFilial" 
        			 service="lms.entrega.manterAgendamentosAction.findLookupFilial" 
        			 dataType="text"  
        			 label="filialDestino"         			 
        			 size="3" 
        			 action="/municipios/manterFiliais" 
        			 minLengthForAutoPopUpSearch="3" 
        			 exactMatch="true" 
        			 maxLength="3"         			 
        			 required="false"
        			 disabled="true"
        			 labelWidth="17%" 
        			 width="34%"
        			 autoTab="false">  
            <adsm:propertyMapping relatedProperty="filialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" size="25" maxLength="50" disabled="true"/>
        </adsm:lookup>

		<adsm:combobox labelWidth="17%" width="33%" required="true" property="tpAgendamento" label="tipoAgendamento" domain="DM_TIPO_AGENDAMENTO" onchange="return regraTipoAgendamento(null)"  onlyActiveValues="true" boxWidth="200" renderOptions="true"/>

		<adsm:textbox dataType="JTDateTimeZone" size="70" label="dataHoraContato" property="dhContato" maxLength="50" required="true" disabled="true" labelWidth="17%" width="33%"  />

		<adsm:textbox dataType="text" label="contato" size="35" property="nmContato" maxLength="60"  labelWidth="17%" width="33%" required="false"  />
		<adsm:textbox dataType="integer" label="ddd" size="5" property="nrDdd" maxLength="5" labelWidth="5%" width="15%" required="true"/>
		<adsm:textbox dataType="integer" label="telefone" size="10" property="nrTelefone" maxLength="10" labelWidth="10%" width="17%" required="true"/>
		<adsm:textbox dataType="integer" label="ramal" size="15" property="nrRamal" maxLength="10" labelWidth="17%" width="33%" required="false"/>
		
		<adsm:combobox labelWidth="17%" disabled="true" width="33%" required="true" property="tpSituacaoAgendamento" label="situacao" domain="DM_SITUACAO_AGENDA" onlyActiveValues="true" onDataLoadCallBack="tpSituacaoAgendamentoCB" boxWidth="150" renderOptions="true" />

		<adsm:textarea label="observacao" property="obAgendamentoEntrega" maxLength="500" style="width:463px" disabled="false" required="false" labelWidth="17%" width="83%" />

<%--Envio de mensagens -------------------------------------------------------------------------------------------------------------%>	
	
	<adsm:textbox property="dsEmailTomador"
			label="emailDoTomador"
			dataType="email"
			size="50"
			maxLength="60"
			labelWidth="17%"
			width="40%"/>
			
	<adsm:textbox
			property="dhEnvio"
			label="dataHoraEnvio"
			dataType="JTDateTimeZone"
			labelWidth="17%"
			width="20%"
			disabled="true"
			serializable="false"/>
			
	<adsm:textbox property="dsEmailDestinatario" 
			label="emailDoDestinatario"
			dataType="email"
			size="50"
			maxLength="60"
			labelWidth="17%"
			width="40%"/>

	</table>
	</div>


<%--agrupamento TentativaAgendamento-------------------------------------------------------------------------------------------------------------%>	

	<div id="agrupamentoTentativaAgendamento" style="display:;border:none;">
	<script>
		document.write(geraColunas());
	</script>

		<adsm:section caption="tentativaAgendamento"/>					
		<adsm:textarea label="ocorrencia" property="obTentativa" maxLength="500" style="width:463px" required="true" labelWidth="17%" width="83%" />			

	</table>
	</div>

<%--agrupamento Agendamento----------------------------------------------------------------------------------------------------------------------%>	

	<div id="agrupamentoAgendamento" style="display:;border:none;">
	<script>
		document.write(geraColunas());
	</script>

		<adsm:section caption="agendamento"/>
		<adsm:textbox dataType="JTDate" label="dataAgendamento" property="dtAgendamento" maxLength="50" disabled="false" labelWidth="17%" width="83%" required="true" />

		<adsm:combobox service="lms.entrega.manterAgendamentosAction.findTurnosVigentes" labelWidth="17%" width="33%" required="false" optionProperty="idTurno" optionLabelProperty="dsTurno" property="turno.idTurno" label="turno" onlyActiveValues="true" boxWidth="210">
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
		</adsm:combobox>

		<adsm:range label="preferencia" labelWidth="17%" width="33%" >
			<adsm:textbox dataType="JTTime" property="hrPreferenciaInicial"/>
			<adsm:textbox dataType="JTTime" property="hrPreferenciaFinal"/>
		</adsm:range>
			
		<adsm:checkbox label="cartaoCredito" property="blCartao" disabled="false" labelWidth="17%" width="83%" />

	</table>
	</div>


<%--agrupamento Cancelamento-------------------------------------------------------------------------------------------------------------%>	

	<div id="agrupamentoCancelamento" style="display:;border:none;">
	<script>
		document.write(geraColunas());
	</script>

		<adsm:section caption="cancelamento" />
		<adsm:combobox service="lms.entrega.manterAgendamentosAction.findMotivoAgendamento" disabled="true" labelWidth="17%" width="33%" required="false" property="motivoAgendamentoByIdMotivoCancelamento.idMotivoAgendamento"  label="motivo"  optionProperty="idMotivoAgendamento"  optionLabelProperty="dsMotivoAgendamento" onlyActiveValues="true" boxWidth="200"/>
		<adsm:textarea label="observacao" property="obCancelamento" maxLength="500" style="width:463px" disabled="true" required="false" labelWidth="17%" width="83%" />

	</table>
	</div>

<%--Buttons--------------------------------------------------------------------------------------------------------------------------------------%>	
	
		<adsm:buttonBar>
			<adsm:button id="reenviarCarta" caption="reenviarCarta" onclick="reenviarCarta_onClick();" disabled="true"/>
		
			<adsm:button id="enviarEmail" caption="enviarEmail" onclick="enviarEmail_onClick();" disabled="true"/>
			
			<adsm:button id="reagendamentoButton" caption="reagendamento" onclick="reagendamento()"/>
			<adsm:button id="cancelarButton" caption="cancelar" onclick="cancelamento()"/>
		
			<adsm:storeButton id="storeButton" callbackProperty="storeCallBack"/>
			<adsm:newButton/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>  


<script>
		document.getElementById("idProcessoWorkflow").masterLink = "true";
		
		function contentChange(event){
			if (event.name == "deleteButton_afterClick"){
				if(nrChaveNfeListboxDef.getData().length == 0){
					document.getElementById("destinatario.pessoa.nrIdentificacao").value = "";
					document.getElementById("destinatario.pessoa.nmPessoa").value = "";
					document.getElementById("destinatario.idCliente").value = "";
				}
			}
		}
		
		function storeCallBack_cb(data, error, key) {		
			if (error != undefined && error != "" && error != null) {
				alert(error);
				if (key == "LMS-09080") {
					setFocus("nmContato");
				} else if (key == "LMS-09067") {
					setFocus("dtAgendamento");
				}
				
			} else {
				store_cb(data, error);
				
				if(data.blNFE == "true" && (data.tpSituacaoAgendamento=="A" || data.tpAgendamento == "TA")){
					setDisabled('enviarEmail', false);
				} else{
					setDisabled('enviarEmail', true);
					setDisabled('dsEmailTomador', true);
					setDisabled('dsEmailDestinatario', true);
				}
			}
		}
		
		// LMS-3252
		function changeTpDocumento(field){
			var tpDocumentoValue = field.value;
		
			switch (tpDocumentoValue){
		    	case "NFE" :
		    		resetValue("documentoServico_doctoServico.filialByIdFilialOrigem.idFilial");
		    		resetValue("documentoServico_doctoServico.filialByIdFilialOrigem.sgFilial");
		    		resetValue("documentoServico_doctoServico.tpDocumentoServico");
		    		resetValue("documentoServico_doctoServico.nrDoctoServico");
		    		resetValue("documentoServico_doctoServico.idDoctoServico");
		    		resetValue("documentoServico");
		    		resetValue("notaFiscalCliente.idNotaFiscalConhecimento");
		    		resetValue("destinatario.pessoa.nrIdentificacao");
		    		resetValue("destinatario.pessoa.nmPessoa");
		    		resetValue("destinatario.idCliente");
		    		resetValue("filialDestino.sgFilial");
		    		resetValue("filialDestino.pessoa.nmFantasia");

		    		setDisabled("documentoServico_doctoServico.filialByIdFilialOrigem.idFilial", true);
		    		setDisabled("documentoServico_doctoServico.filialByIdFilialOrigem.sgFilial", true);
		    		setDisabled("documentoServico_doctoServico.tpDocumentoServico", true);
		    		setDisabled("documentoServico_doctoServico.nrDoctoServico", true);
		    		setDisabled("documentoServico_doctoServico.idDoctoServico", true);
		    		setDisabled("documentoServico", true);
		    		setDisabled("notaFiscalCliente.idNotaFiscalConhecimento", true);
		    		setDisabled("nrChaveNfe", false);	
		    		setDisabled("nrChaveNfe_nrChave", false);
		    		setDisabled('consultarDocumentoButton', true);
		    		setDisabled('dsEmailTomador', true);
		    		setDisabled('dsEmailDestinatario', true);
		    		
		    		resetValue("dsEmailTomador");
		    		resetValue("dsEmailDestinatario");

		    		
	    			break;
		    	case "CTRC" :
		    		resetValue("nrChaveNfe");
		    		resetValue("nrChaveNfe_nrChave");
		    		resetValue("destinatario.pessoa.nrIdentificacao");
		    		resetValue("destinatario.pessoa.nmPessoa");
		    		resetValue("destinatario.idCliente");
		    		
		    		setDisabled("nrChaveNfe", true);	
		    		setDisabled("nrChaveNfe_nrChave", true);
		    		setDisabled("documentoServico_doctoServico.tpDocumentoServico", false);
		    		setDisabled("documentoServico", false);
		    		setDisabled("notaFiscalCliente.idNotaFiscalConhecimento", false);
		    		setDisabled('consultarDocumentoButton', false);
		    		setDisabled('dsEmailTomador', false);
		    		setDisabled('dsEmailDestinatario', false);
		    		
		    		break;
		     	default:
	   	    }
		}
		
		function reenviarCarta_onClick(){
			var idAgendamentoEntrega = getElementValue("idAgendamentoEntrega");

			var sdo = createServiceDataObject("lms.entrega.manterAgendamentosAction.generateReenvioCarta", "reenviarCarta_onClick", {idAgendamentoEntrega:idAgendamentoEntrega});
			xmit({serviceDataObjects:[sdo]});
		}
		
		/**
		 * Callback de retorno da busca dos itens da NFe
		 */
		function reenviarCarta_onClick_cb(data, error, errorMsg, eventObj){
			if (error != undefined) {
				alert(error);
			} else {
				setDisabled("reenviarCarta", true);
			}
		}

		
		function enviarEmail_onClick(){
			if(nrChaveNfeListboxDef.getData().length == 0){
				alert("LMS-09131 - " + i18NLabel.getLabel("LMS-09131"));		
				return;
			}

			var sdo = createServiceDataObject("lms.entrega.manterAgendamentosAction.montaParametroUrl", "montaParametroUrl", { nrChaves:nrChaveNfeListboxDef.getData(), idAgendamentoEntrega:getElementValue('idAgendamentoEntrega') });
		   	xmit({serviceDataObjects:[sdo]});
		}
		
		function montaParametroUrl_cb(data, error){
			if (error != undefined) {
				alert(error);
				return;
			}
			else{
				showModalDialog('coleta/enviarEmailAgendamento.do?cmd=main&param='+data.parametroUrl+'&idAgendamentoEntrega='+getElementValue('idAgendamentoEntrega') + '&dtAgendamento=' + getElementValue('dtAgendamento'), window, 'unardorned:no;scroll:no;resizable:yes;status:no;center=no;help:no;dialogWidth:760px;dialogHeight:475px;');
			}
		}
		
		/**
		 * Busca a chave nfe verificando sua autenticidade e buscando seu remetente e seus itens correspondentes
		 */
		function findChaveNfe(){
			var chaveNfe = getElementValue("nrChaveNfe_nrChave");
			
			if(validateChaveNfe(chaveNfe)){
				var sdo = createServiceDataObject("lms.entrega.manterAgendamentosAction.findDestinatarioNfe", "findDestinatarioNfe", {chaveNfe:chaveNfe, chaveBox:nrChaveNfeListboxDef.getData()});
			   	xmit({serviceDataObjects:[sdo]});
			}
		}
		
		/**
		 * Callback de retorno da busca dos itens da NFe
		 */
		function findDestinatarioNfe_cb(data, error, errorMsg, eventObj){
			if (error != undefined) {
				alert(error);
				setFocus(document.getElementById("nrChaveNfe_nrChave"), true);
				setElementValue("nrChaveNfe_nrChave","");
			}
			else{
				if(data != undefined && data.cliente != undefined && data.cliente.pessoa != undefined && data.cliente != null 
						&& data.cliente.pessoa.nrIdentificacaoFormatado != undefined) {	
					document.getElementById("destinatario.pessoa.nrIdentificacao").value = data.cliente.pessoa.nrIdentificacaoFormatado;
					document.getElementById("destinatario.pessoa.nmPessoa").value = data.cliente.pessoa.nmPessoa;
					document.getElementById("destinatario.idCliente").value = data.cliente.idCliente;
				}
				
				nrChaveNfeListboxDef.insertOrUpdateOption();
			}
		}

		function validateChaveNfe(chaveNfe) {
			if(chaveNfe.length >= 44){
				if(!validateDigitoVerificadorNfe(chaveNfe)){
					return false;
				}
			}else{
				alert("LMS-04400 - " + i18NLabel.getLabel("LMS-04400"));
				setElementValue("nrChaveNfe_nrChave","");
				return false;
			}
			
			return true;
		}
	  	
		/**
		 * Valida o digito verificador da Chave Nfe
		 */
		function validateDigitoVerificadorNfe(chaveNfe){
			var dvChaveNfe = chaveNfe.substring(chaveNfe.length - 1, chaveNfe.length);
			var chave = chaveNfe.substring(0, chaveNfe.length - 1);	
			var calculoChaveNfe = modulo11(chave);
			
			if(dvChaveNfe != (calculoChaveNfe)){
				alert("LMS-04400 - " + i18NLabel.getLabel("LMS-04400"));
				return false;
			}
			
			return true;
		}
		
		function modulo11(chave){
			var n = new Array();
			var peso = 2;
			var soma = 0;

			n = chave.split('');
			
			for (var i = n.length-1; i >= 0; i--) {
				var value = n[i];
				soma = soma + value * peso++;
				if(peso == 10){
					peso = 2;
				}
			}
			
			var mod = soma % 11;
			var dv;
			
			if(mod == 0 || mod == 1){
				dv = 0;
			} else {
				dv = 11 - mod;
			}
			
			return dv
		}
		
		function chamarConsultarDocumento() {
			if (getElementValue("filial.idFilial") != null 	&& getElementValue("filial.idFilial") != "" && getElementValue("destinatario.idCliente") != null && getElementValue("destinatario.idCliente") != "") {
				showModalDialog('entrega/manterAgendamentos.do?cmd=consultarDocumentos',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
			}
		}

<%-------------- Início Regra do PCE -------------------------------------------------------------------------------------------------%>
		function validateDocumentosRepitidos(list) {
			var elemList = getElementValue("documentoServico");
			
			if (elemList != undefined) {
				for (var i = 0; i < list.length; i++) {
					for (var j = 0; j < elemList.length; j++) {
						var id1 = list[i].idDoctoServico;
						var id2 = elemList[j].doctoServico.idDoctoServico;
						
						if (id1 == id2) {
							return false;
						}
					}
				}
			}
			
			return true;
		}
		
                       
		function setArrayDocumentoServico(list) {
			var data = new Array();
			
			var idFilialDestinoOperacionalAux = 0;
			
			for (var i=0; i < list.length; i++)	{
			
				data.push( {doctoServico:{filialByIdFilialOrigem:{idFilial:list[i].idFilial, 
						   										  sgFilial:list[i].sgFilial, 
						   										  nmFilial:list[i].nmFilial
						   										 },
						   				  nrDoctoServico:list[i].nrDoctoServico, 
						   				  idDoctoServico:list[i].idDoctoServico,
						   				  tpDocumentoServico:{value:list[i].tpDocumentoServicoValue, 
						   				 					  description:list[i].tpDocumentoServico
						   				 					 }
						   				 }
						   });
						   
				if (getNestedBeanPropertyValue(list[i],"sgFilialDestinoOperacional") != null && getNestedBeanPropertyValue(list[i],"sgFilialDestinoOperacional") != undefined) {
					setElementValue("filialDestino.sgFilial", getNestedBeanPropertyValue(list[i],"sgFilialDestinoOperacional"));
					setElementValue("filialDestino.pessoa.nmFantasia", getNestedBeanPropertyValue(list[i],"nmFilialDestinoOperacional"));
				}
				
				if (getNestedBeanPropertyValue(list[i],"idFilialDestinoOperacional") != null && getNestedBeanPropertyValue(list[i],"idFilialDestinoOperacional") != undefined) {
					idFilialDestinoOperacionalAux = getNestedBeanPropertyValue(list[i],"idFilialDestinoOperacional");
				}
					
				setNestedBeanPropertyValue(list[i],"idFilialAgendamento",getElementValue("filial.idFilial"));
			}
			documentoServicoListboxDef.renderOptions({documentoServico:data});
			
			if (idFilialDestinoOperacionalAux != 0 && idFilialDestinoOperacionalAux != null) {
				setElementValue("idFilialDestinoOperacional", idFilialDestinoOperacionalAux);
				notifyElementListeners({e:document.getElementById("idFilialDestinoOperacional")});
			}

			var sdo = createServiceDataObject("lms.entrega.manterAgendamentosAction.verificaExistenciaPce", "loadBasicData", {list:list});
	    	xmit({serviceDataObjects:[sdo]});
		}
                        
                        
		function loadBasicData_cb(data,error,key) {
			if (error != undefined) {
				alert(error);
				return;
			}
		
			var array = new Array();
			var idRetornoPCEDestinatario;
			var idRetornoPCERemetente;
			var idFilialDestinoOperacionalAux = 0;
			var nrNotaFiscal = " ";
			var idDoctoServico;

			for (var i=0; i < data.length; i++)	{
				idDoctoServico = getNestedBeanPropertyValue(data[i],"idDoctoServico");

				if ( getNestedBeanPropertyValue(data[i],"erro") != null ) {
					 alert(getNestedBeanPropertyValue(data[i],"erro") + " - "+ i18NLabel.getLabel(getNestedBeanPropertyValue(data[i],"erro")));
					 clearList(document.getElementById('documentoServico'), getNestedBeanPropertyValue(data[i],"idDoctoServico"));
					 break;
				}

				if (getElementValue("destinatario.idCliente") == "" || getElementValue("destinatario.idCliente") == null || getElementValue("destinatario.idCliente") == undefined) {
				
					setElementValue("destinatario.idCliente", getNestedBeanPropertyValue(data[i],"idClienteDestinatario"));
					setElementValue("destinatario.pessoa.nrIdentificacao", getNestedBeanPropertyValue(data[i],"nrIdentificacaoFormatadoDestinatario"));
					setElementValue("destinatario.pessoa.nmPessoa", getNestedBeanPropertyValue(data[i],"nmPessoaDestinatario"));
					
					forcedChange = true;
					lookupChange({e:document.getElementById("destinatario.idCliente"), forceChange:true});

				}

				if (getNestedBeanPropertyValue(data[i],"sgFilialDestinoOperacional") != null && getNestedBeanPropertyValue(data[i],"sgFilialDestinoOperacional") != undefined) {

					setElementValue("filialDestino.idFilial", getNestedBeanPropertyValue(data[i],"idFilialDestinoOperacional"));
					setElementValue("filialDestino.sgFilial", getNestedBeanPropertyValue(data[i],"sgFilialDestinoOperacional"));
					setElementValue("filialDestino.pessoa.nmFantasia", getNestedBeanPropertyValue(data[i],"nmFilialDestinoOperacional"));
					
				}
				
				if (getNestedBeanPropertyValue(data[i],"idFilialDestinoOperacional") != null && getNestedBeanPropertyValue(data[i],"idFilialDestinoOperacional") != undefined) {
					idFilialDestinoOperacionalAux = getNestedBeanPropertyValue(data[i],"idFilialDestinoOperacional");
				}
				
				idRetornoPCEDestinatario = getNestedBeanPropertyValue(data[i],"idRetornoPCEDestinatario");
				idRetornoPCERemetente = getNestedBeanPropertyValue(data[i],"idRetornoPCERemetente");

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
			//TODO: lms-5466
			buscarEmailsTomadorDestinatario(idDoctoServico);
			
			if (idFilialDestinoOperacionalAux != 0 && idFilialDestinoOperacionalAux != null) {
				setElementValue("idFilialDestinoOperacional", idFilialDestinoOperacionalAux);
				notifyElementListeners({e:document.getElementById("idFilialDestinoOperacional")});
			}				
			
		}		
		
		function buscarEmailsTomadorDestinatario(idDoctoServico){
			var data = new Object();
	   		data.idDoctoServico = idDoctoServico;
	   		
	   		var sdo = createServiceDataObject("lms.entrega.manterAgendamentosAction.findEmailsTomadorDestinatario","buscarEmailsTomadorDestinatario",data);
			xmit({serviceDataObjects:[sdo]});
		}
		
		function buscarEmailsTomadorDestinatario_cb(data) {
			if (data.dsEmailTomador != null && data.dsEmailTomador != "" && data.dsEmailTomador != undefined) {
				setElementValue("dsEmailTomador", data.dsEmailTomador);
				setDisabled("dsEmailTomador", true);
			}
			if (data.dsEmailDestinatario != null && data.dsEmailDestinatario != "" && data.dsEmailDestinatario != undefined) {
				setElementValue("dsEmailDestinatario", data.dsEmailDestinatario);
				setDisabled("dsEmailDestinatario", true);
			}
		}
<%-------------- Fim Regra do PCE -------------------------------------------------------------------------------------------------%>

		//SETANDO A FILIAL COM A FILIAL DO USUARIO LOGADO
		function pageLoad_cb(data) {
			onPageLoad_cb(data);			
			if (getElementValue("idProcessoWorkflow") != "") {
				var form = document.forms[0];
				var sdo = createServiceDataObject(form.service,form.onDataLoadCallBack,{id:getElementValue("idProcessoWorkflow")});
				xmit({serviceDataObjects:[sdo]});
			} else {			
				
				var tabGroup = getTabGroup(this.document);	 
			   	var tabGroupProperties = tabGroup.getTab("cad").properties;
			   	var tpEmpresa = tabGroupProperties.tipoEmpresaSessao;
				// LMS - 4599 -- Se o tipo da empresa da sessão for do tipo parceira, então executa
				// o if passando o parametro 'data' na requisição
			   	if(tpEmpresa != null && tpEmpresa == "P"){
			   		
			   		var data = new Object();
			   		data.idDoctoServico = tabGroupProperties.idDoctoServico;
			   		data.idManifestoEntregaDocumento = tabGroupProperties.idManifestoEntregaDocumento;
			   		data.tpEmpresaSessao = tabGroupProperties.tipoEmpresaSessao;
			   		
			   		var sdo = createServiceDataObject("lms.entrega.manterAgendamentosAction.findDataSession","dataSession",data);
					xmit({serviceDataObjects:[sdo]});
					desabilitaConsultarDocumentoButton(true);
					
			   	}else{
			   		var sdo = createServiceDataObject("lms.entrega.manterAgendamentosAction.findDataSession","dataSession",null);
					xmit({serviceDataObjects:[sdo]});
					desabilitaConsultarDocumentoButton(true);
			   	}
			}		
		}

		var idFilial = null;
		var sgFilial = null;
		var nmFilial = null;
		var dhContato = null;
		
		var usuarioByIdUsuarioCriacao_nmUsuario = null;
		var usuarioByIdUsuarioCriacao_idUsuario = null;
	
		function dataSession_cb(data) {
			tpEmpresa = getNestedBeanPropertyValue(data,"tipoEmpresa");
			idFilial = getNestedBeanPropertyValue(data,"filial.idFilial");
			sgFilial = getNestedBeanPropertyValue(data,"filial.sgFilial");
			nmFilial = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
			dhContato = setFormat(document.getElementById("dhContato"),getNestedBeanPropertyValue(data,"dhContato"));
			
			usuarioByIdUsuarioCriacao_nmUsuario = getNestedBeanPropertyValue(data,"usuarioByIdUsuarioCriacao.nmUsuario");
			usuarioByIdUsuarioCriacao_idUsuario = getNestedBeanPropertyValue(data,"usuarioByIdUsuarioCriacao.idUsuario");

			writeDataSessionFilial();
			writeDataSession();
			
			// LMS - 4599 --- Se o usuario vier pela tela de registrar baixas de entregas parceiras
		    // traz os campos pré carregados e desabilitados
			var tmEmpresaSessao = getNestedBeanPropertyValue(data,"tipoEmpresaSessao"); 
			var tabGroup = getTabGroup(this.document);	
		   	var tabGroupProperties = tabGroup.getTab("cad").properties;
		   	
		    if(tmEmpresaSessao != null && tmEmpresaSessao == "P"){
				var tabPesq = tabGroup.getTab("pesq").setDisabled(true);
				tabGroup.selectTab("cad" , {name:'tab_click'});							
			
					// verifica se possui o id do agendamento, se tiver carrega o formulario com os dados
 					if(getNestedBeanPropertyValue(data,"idAgendamentoEntrega") != null){
 						setElementValue("idAgendamentoEntrega", getNestedBeanPropertyValue(data,"idAgendamentoEntrega"));
 						var idAg = getElementValue("idAgendamentoEntrega");
 						
 						var sdo = createServiceDataObject("lms.entrega.manterAgendamentosAction.findById", "formLoad", {idAg:idAg});
 				    	xmit({serviceDataObjects:[sdo]});
 					}else{
 						// Se for um novo agendamento, Traz os seguintes campos de acordo com o documento escolhido na tela de registrar baixas parceiras
 						setElementValue("filial.idFilial", getNestedBeanPropertyValue(data,"idFilialAgendamento"));
 						setElementValue("filial.sgFilial", getNestedBeanPropertyValue(data,"sgFilialAgendamento"));
 						setElementValue("filial.pessoa.nmFantasia", getNestedBeanPropertyValue(data,"nmFilialAgendamento"));
 						
 						setElementValue("destinatario.idCliente", getNestedBeanPropertyValue(data,"idDestinatario"));
 					    setElementValue("destinatario.pessoa.nrIdentificacao", getNestedBeanPropertyValue(data,"nrDestinatario")); 
 						setElementValue("destinatario.pessoa.nmPessoa", getNestedBeanPropertyValue(data,"destinatario")); 
 						
 						setElementValue('documentoServico_doctoServico.idDoctoServico', getNestedBeanPropertyValue(data,"idDoctoServico"));
 					    setElementValue("documentoServico_doctoServico.tpDocumentoServico", getNestedBeanPropertyValue(data,"tipoDocto"));
 					    setElementValue("documentoServico_doctoServico.filialByIdFilialOrigem.sgFilial", getNestedBeanPropertyValue(data,"sgDoctoServico"));
 					    setElementValue("documentoServico_doctoServico.nrDoctoServico", getNestedBeanPropertyValue(data,"nrDoctoServico"));
 					    
 					    documentoServicoListboxDef.insertOrUpdateOption();
 					    			    
 					    setElementValue("filialDestino.idFilial", getNestedBeanPropertyValue(data,"idFilialDestino"));
 					    setElementValue("filialDestino.sgFilial", getNestedBeanPropertyValue(data,"sgFilialDestino"));
 					    setElementValue("filialDestino.pessoa.nmFantasia", getNestedBeanPropertyValue(data,"nmFilialDestino")); 
 					    
 					    // Desabilita os campos a seguir
 						    setDisabled('filial.idFilial', true);
 							setDisabled('destinatario.idCliente', true);
 							setDisabled('tpDocumento', true);
 							setDisabled('nrChaveNfe', true);
 							
 							setDisabled('notaFiscalCliente.idNotaFiscalConhecimento', true);
 							
 							setDisabled("documentoServico", true);			
 							setDisabled("documentoServico_doctoServico.idDoctoServico", true);
 							setDisabled("documentoServico_doctoServico.filialByIdFilialOrigem.idFilial", true);
 				    		setDisabled("documentoServico_doctoServico.filialByIdFilialOrigem.sgFilial", true);
 				    		setDisabled("documentoServico_doctoServico.tpDocumentoServico", true);
 				    		setDisabled("documentoServico_doctoServico.nrDoctoServico", true);
 				    		
 				    		document.getElementById("documentoServico").removeAttribute("onchange");
 				    		
 							setDisabled('filialDestino.idFilial', true);
 					}
				   	
		    }
		
		    // teste para garantir que se o usuario vier pela tela manter agendamentos
		    // desabilita os campos, e traz a aba detalhamento selecionada 
		    if(tpEmpresa != null && tpEmpresa == "P"){
		    	var tabPesq = tabGroup.getTab("pesq").setDisabled(true);
				tabGroup.selectTab("cad" , {name:'tab_click'});	
		    	// Desabilita os campos a seguir
			    setDisabled('filial.idFilial', true);
				setDisabled('destinatario.idCliente', true);
				setDisabled('tpDocumento', true);
				setDisabled('nrChaveNfe', true);
				
				setDisabled('notaFiscalCliente.idNotaFiscalConhecimento', true);
							
				setDisabled("documentoServico_doctoServico.filialByIdFilialOrigem.idFilial", true);
	    		setDisabled("documentoServico_doctoServico.filialByIdFilialOrigem.sgFilial", true);
	    		setDisabled("documentoServico_doctoServico.tpDocumentoServico", true);
	    		setDisabled("documentoServico_doctoServico.nrDoctoServico", true);
	    		setDisabled("documentoServico_doctoServico.idDoctoServico", true);
	    		setDisabled("documentoServico", true);
			
				setDisabled('filialDestino.idFilial', true);
		    }
			
		    var idsMonitoramentoCCT = getIdsMonitoramentoCCT();
		    if(idsMonitoramentoCCT != null && idsMonitoramentoCCT != '' && idsMonitoramentoCCT != undefined){
		    	var sdo = createServiceDataObject("lms.entrega.manterAgendamentosAction.findDadosMonitoramentoCCT", "formLoad", {idsMonitoramentoCCT:idsMonitoramentoCCT});
		    	xmit({serviceDataObjects:[sdo]});
		    }
		}

		function writeDataSessionFilial() {
			if (idFilial != null &&
				sgFilial != null &&
				nmFilial != null) {
				setElementValue("filial.idFilial",idFilial);
				setElementValue("filial.sgFilial",sgFilial);
				setElementValue("filial.pessoa.nmFantasia",nmFilial);
				
				notifyElementListeners({e:document.getElementById("filial.idFilial")});
				
			}
		}
		
		function writeDataSession() {
			if (usuarioByIdUsuarioCriacao_idUsuario != null) {
				setElementValue("usuarioByIdUsuarioCriacao.idUsuario",usuarioByIdUsuarioCriacao_idUsuario);
			}
			if (usuarioByIdUsuarioCriacao_nmUsuario != null) {
				setElementValue("usuarioByIdUsuarioCriacao.nmUsuario",usuarioByIdUsuarioCriacao_nmUsuario);
			}
			if (dhContato != null) {
				setElementValue("dhContato",dhContato);
			}
			
		}
		
		/**
		 * initWindowAdapter também trata o evento 'change_filial', o qual é passado no change da
		 * lookup de filial.
		 * Ignora evento 'newItemButton_click'
		 */
		function initWindowAdapter(eventObjName) {
			if (eventObjName == "newItemButton_click") {
				return;
			}

			setDisabled(document, false);
			setDisabled('filialDestino.idFilial', true);
			setDisabled('filialDestino.pessoa.nmFantasia', true);
			setDisabled('motivoAgendamentoByIdMotivoCancelamento.idMotivoAgendamento', true);
			setDisabled('obCancelamento', true);
			setDisabled('filial.idFilial', true);
			setDisabled('filial.pessoa.nmFantasia', true);
			setDisabled('usuarioByIdUsuarioCriacao.nmUsuario', true);
			setDisabled('destinatario.pessoa.nmPessoa', true);
			setDisabled('dhContato', true);
			setDisabled('tpSituacaoAgendamento', true);
			setDisabled('enviarEmail', true);
			setDisabled("nrChaveNfe", true);	
    		setDisabled("nrChaveNfe_nrChave", true);
    		setDisabled("tpDocumento", true);
    		setDisabled("dhEnvio", true);
			setDisabled("reenviarCarta", true);
			
    		if (eventObjName == "gridRow_click") {
    			setDisabled("dsEmailTomador", true);
    			setDisabled("dsEmailDestinatario", true);
    		}
			
			if (eventObjName == "newButton_click" || eventObjName == "tab_click" ) {
				writeDataSessionFilial();
				writeDataSession();
				
				setDisabled("tpDocumento", false);
				setElementValue("tpDocumento", "CTRC");
				changeTpDocumento(document.getElementById("tpDocumento"));
			} 
			else if (eventObjName == "change_filial" || eventObjName == "load_filial") {
				writeDataSession();
			}
			
			if (eventObjName == "newButton_click"
					|| eventObjName == "tab_click"
					|| eventObjName == "change_filial"
					|| eventObjName == "load_filial") {
				showAgrupamentoCancelamento(false);
				
				setElementValue("tpSituacaoAgendamento", "A");
				tpSituacaoAgendamentoGlobal = getElementValue("tpSituacaoAgendamento");
				setDisabled('filial.idFilial', false);
				showAgrupamentoDadosPrincipais(getElementValue("filial.idFilial") != "");
			}

			regraTipoAgendamento(eventObjName);
			situacaoAgendamento();

			if (eventObjName == "storeButton") {
			   	setFocus('__buttonBar:0.newButton', false);
				setDisabled('filial.idFilial', true);

				if (getElementValue("idAgendamentoEntrega") != null && getElementValue("idAgendamentoEntrega") != "" ) {
					setDisabled("storeButton", true);
			   	} else {
					setDisabled("storeButton", false);
			   	}				
			}
			
			setDisabled('documentoServico_doctoServico.idDoctoServico', true);
			setDisabled('documentoServico_doctoServico.filialByIdFilialOrigem.idFilial', true);
			
			if (eventObjName == "newButton_click"
					|| eventObjName == "tab_click"
					|| eventObjName == "change_filial"
					|| eventObjName == "load_filial") {
				setDisabled('documentoServico_doctoServico.tpDocumentoServico', false);
				setDisabled('notaFiscalCliente.idNotaFiscalConhecimento', false);
				
				if ((eventObjName == "change_filial" || eventObjName == "load_filial")
						&& getElementValue("filial.idFilial") == "") {
					setFocus('storeButton', false);
				} else {
					setFocusOnFirstFocusableField();
				}
			
				if (eventObjName == "load_filial") {
					document.getElementById("filial.idFilial").masterLink = undefined;
					document.getElementById("filial.sgFilial").masterLink = undefined;
					document.getElementById("filial.pessoa.nmFantasia").masterLink = undefined;
					setDisabled("filial.idFilial",false);
					setDisabled("filial.sgFilial",false);
				}
			
			} else {
				setDisabled('documentoServico_doctoServico.tpDocumentoServico', true);
				setDisabled('notaFiscalCliente.idNotaFiscalConhecimento', true);
			}
		}
		
		function initWindow(eventObj) {
			var idsMonitoramentoCCT = getIdsMonitoramentoCCT();
			var idAgendamentoEntrega = getElementValue("idAgendamentoEntrega");
			
			// se a tela for chamada da tela de monitoramento do cct, deve ter um comportamento diferenciado, e deve ter comportamento normal após salvar.
			if(idsMonitoramentoCCT != '' && idsMonitoramentoCCT != null && idsMonitoramentoCCT != undefined && (idAgendamentoEntrega == '' || idAgendamentoEntrega == undefined)){
				initWindowAdapter('tab_click');
			} else {
				initWindowAdapter(eventObj.name);
			}
		}
		
		function getIdsMonitoramentoCCT() {
			var url = new URL(parent.location.href);
			return url.parameters["idsMonitoramentoCCT"];
		}
		
		function desabilitaConsultarDocumentoButton(disable) {
			setDisabled('consultarDocumentoButton', disable);
			if (!disable) {
				setFocus("consultarDocumentoButton", false);
			}
		}				
		
		var forcedChange = undefined;
		function habilitaConsultarDocumentoButtonDestinatario_cb(data){
			if(data != undefined && data.length == 1) {
				desabilitaConsultarDocumentoButton(false);
				if (forcedChange == undefined) {
					clearList(document.getElementById('documentoServico'), null);
				} else {
					forcedChange = undefined;
				}
	  		}
	  		return destinatario_pessoa_nrIdentificacao_exactMatch_cb(data);
	  	}
	
		function onPopUpDestinatario(data){
			desabilitaConsultarDocumentoButton(false);
			clearList(document.getElementById('documentoServico'), null);
	 		return true;
		}
		
		function onChangeDestinatario(){
			var saiCampo = destinatario_pessoa_nrIdentificacaoOnChangeHandler();
			if (getElementValue("destinatario.pessoa.nrIdentificacao") == "") {
				desabilitaConsultarDocumentoButton(true);
				clearList(document.getElementById('documentoServico'), null);
				
				resetValue("idFilialDestinoOperacional");
				resetValue("filialDestino.idFilial");
				resetValue("filialDestino.sgFilial");
				resetValue("filialDestino.pessoa.nmFantasia");
				
				notifyElementListeners({e:document.getElementById("idFilialDestinoOperacional")});
				
				saiCampo = true;
			}
			return saiCampo;
		}
		
		/*
		 * Seta o campo Filial de agendamento como masterlink.
		 * O campo deixará de ser masterlink na function initWindowAdapter q é chamada no final.
		 * A function newButtonScript limpará os demais campos da tela.
		 */
		function treatFilialBehavior() {
			document.getElementById("filial.idFilial").masterLink = "true";
			document.getElementById("filial.sgFilial").masterLink = "true";
			document.getElementById("filial.pessoa.nmFantasia").masterLink = "true";
			
			newButtonScript(document, true, {name:'newItemButton_click'});
			//document.getElementById("filial.idFilial").masterLink = undefined;
			initWindowAdapter("load_filial");
		}
		
		/*
		 * Data load callback da lookup de Filial de agendamento.
		 */
		function filialDataLoad_cb(data) {
			var retorno = filial_sgFilial_exactMatch_cb(data);
			if(data != undefined && data.length == 1) {
				treatFilialBehavior();
	  		}
			return retorno;
		}
		
		function filialPopupSetValue(d1,d2) {
			treatFilialBehavior();
			return true;
		}
		
		function onChangeFilial(field){
			var saiCampo = filial_sgFilialOnChangeHandler();
			setElementValue("destinatario.idCliente", "");
			setElementValue("destinatario.pessoa.nrIdentificacao", "");
			setElementValue("destinatario.pessoa.nmPessoa", "");
			
			var data1 = new Array();
			var data2 = new Array();
				
   			setNestedBeanPropertyValue(data2,"documentoServico",data1); 
     		fillFormWithFormBeanData(document.forms[0].tabIndex, data2);			
			
			desabilitaConsultarDocumentoButton(true);
			
			if (field.value == "") {
				newButtonScript(document, true, {name:'newItemButton_click'});
				initWindowAdapter("change_filial");
			}
			
			return saiCampo;
		}		
		
		function situacaoAgendamento() {		
			setDisabled('tpAgendamento', false);
			setDisabled('destinatario.idCliente', false);
			setDisabled('cancelarButton', true);
			setDisabled('reagendamentoButton', true);
			setDisabled('storeButton', false);
			
			if (getElementValue("idAgendamentoEntrega") != null && getElementValue("idAgendamentoEntrega") != "" ) {

				setDisabled('documentoServico', false);
								
				disableInformacaoAgendamento(false);
			
				if (getElementValue("tpSituacaoAgendamento") == "A" || getElementValue("tpSituacaoAgendamento") == "R" || getElementValue("tpSituacaoAgendamento") == "C" || getElementValue("tpSituacaoAgendamento") == "F") {
					setDisabled('reagendamentoButton', false);
				
					if (getElementValue("tpSituacaoAgendamento") == "A") {
						setDisabled('nmContato', true);
						setDisabled('nrDdd', true);
						setDisabled('nrTelefone', true);
						setDisabled('nrRamal', true);
						setDisabled('obAgendamentoEntrega', true);
					
						setDisabled('destinatario.idCliente', true);
						setDisabled('documentoServico', true);
						setDisabled('tpAgendamento', true);
						disableInformacaoAgendamento(true);
						setDisabled('cancelarButton', false);
						
					} else if (getElementValue("tpSituacaoAgendamento") == "R" || getElementValue("tpSituacaoAgendamento") == "C" || getElementValue("tpSituacaoAgendamento") == "F" ) {
						setDisabled(document, true);						
						setDisabled('__buttonBar:0.newButton', false);
						setDisabled('storeButton', true);
						setDisabled('reagendamentoButton', false);
						
						if (getElementValue("tpSituacaoAgendamento") == "C" || getElementValue("tpSituacaoAgendamento") == "F" ) {
							setDisabled('reagendamentoButton', true);
						}
						
						setFocus('__buttonBar:0.newButton', false);
					} else {					
						setDisabled('storeButton', true);
						setDisabled('reagendamentoButton', true);
					}
			   }
				if (getElementValue("cdLocalizacaoMercadoria") != "1") {
			    	if (getElementValue("tpSituacaoAgendamento") == "A" || getElementValue("tpSituacaoAgendamento") == "R") {
						setDisabled('reagendamentoButton', false); 
					}
				}
			} else {
				setDisabled('reagendamentoButton', true);
			}
		}
		
		function disableInformacaoAgendamento(disable) {
			setDisabled("dtAgendamento", disable);
			setDisabled("turno.idTurno", disable);
			setDisabled("hrPreferenciaInicial", disable); 
			setDisabled("hrPreferenciaFinal", disable);
			setDisabled("blCartao", disable);
		}

		/**
		 * Altera visibilidade do div dadosPrincipais.
		 * param true torna visível
		 * param false torna invisível
		 * 
		 * Quando é invisível, os campos não são mais obrigatórios.
		 */
		function showAgrupamentoDadosPrincipais(valueBoolean) {
			var value = (valueBoolean) ? "" : "none";
			document.getElementById("dadosPrincipais").style.display = value;
		}

		/**
		 * Altera visibilidade AgrupamentoTentativaAgendamento.
		 * param true torna visível
		 * param false torna invisível
		 * 
		 * Quando é invisível, os campos não são mais obrigatórios.
		 */
		function showAgrupamentoTentativaAgendamento(valueBoolean) {
			var value = (valueBoolean) ? "" : "none";

			document.getElementById("agrupamentoTentativaAgendamento").style.display = value;
			document.getElementById("obTentativa").required = new String(valueBoolean);
			
		}

		/**
		 * Altera visibilidade AgrupamentoAgendamento.
		 * param true torna visível
		 * param false torna invisível
		 * 
		 * Quando é invisível, os campos não são mais obrigatórios.
		 */
		function showAgrupamentoAgendamento(valueBoolean) {
			var value = (valueBoolean) ? "" : "none";

			document.getElementById("agrupamentoAgendamento").style.display = value;
			document.getElementById("dtAgendamento").required = new String(valueBoolean);
		}

		function showAgrupamentoCancelamento(valueBoolean) {
			var value = (valueBoolean) ? "" : "none";

			document.getElementById("agrupamentoCancelamento").style.display = value;
		}

		function regraTipoAgendamento(eventObjName) {

			showAgrupamentoTentativaAgendamento(false);
		
			showAgrupamentoAgendamento(true);
			
			document.getElementById("turno.idTurno").required = new String(false);			
			setDisabled("turno.idTurno", false);
			setDisabled("blCartao", false);
			setDisabled("dtAgendamento", false);
			setDisabled("hrPreferenciaInicial", false);
			setDisabled("hrPreferenciaFinal", false);
			document.getElementById("hrPreferenciaInicial").required = new String(false);
			document.getElementById("hrPreferenciaFinal").required = new String(false);

			if (eventObjName == null || (eventObjName != "newButton_click" && eventObjName != "tab_click") ) {
				setElementValue("tpSituacaoAgendamento", tpSituacaoAgendamentoGlobal);
			}

			if (getElementValue("idAgendamentoEntrega") == null || getElementValue("idAgendamentoEntrega") == "" || getElementValue("idAgendamentoEntrega") == undefined ) {
				setElementValue("tpSituacaoAgendamento", "A");
				tpSituacaoAgendamentoGlobal = 'A';
			}

			if (getElementValue("tpAgendamento") == "TA") {
			
				setElementValue("turno.idTurno", "");
				setElementValue("hrPreferenciaInicial", "");
				setElementValue("hrPreferenciaFinal", "");				
			
				showAgrupamentoTentativaAgendamento(true);
				showAgrupamentoAgendamento(false);
				if (getElementValue("idAgendamentoEntrega") == null || getElementValue("idAgendamentoEntrega") == "" || getElementValue("idAgendamentoEntrega") == undefined ) {
					setElementValue("tpSituacaoAgendamento", "F");
					tpSituacaoAgendamentoGlobal = 'F';
				}
			} else	if (getElementValue("tpAgendamento") == "AT") {
				document.getElementById("turno.idTurno").required = new String(true);
				setDisabled("hrPreferenciaInicial", true);
				setDisabled("hrPreferenciaFinal", true);
				
				setElementValue("hrPreferenciaInicial", "");
				setElementValue("hrPreferenciaFinal", "");				
				
			} else	if (getElementValue("tpAgendamento") == "AH") {
				setDisabled("turno.idTurno", true);
				setElementValue("turno.idTurno", "");
				document.getElementById("hrPreferenciaInicial").required = new String(true);
				document.getElementById("hrPreferenciaFinal").required = new String(true);
			} else	if (getElementValue("tpAgendamento") == "RE") {

			} else	if (getElementValue("tpAgendamento") == "DP") {
				<%-- Permitir preenchimento nos campos turno e preferência, essa regra está no início dessa function --%>
			} else {
				showAgrupamentoTentativaAgendamento(false);
				showAgrupamentoAgendamento(false);
			}
			
		}
		
		function reagendamento() {
			showModalDialog('entrega/manterAgendamentos.do?cmd=reagendar&tpEmpresa='+tpEmpresa, window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:370px;');
		}

		function cancelamento() {
			showModalDialog('entrega/manterAgendamentos.do?cmd=cancelar',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:250px;');
		}

		var tpSituacaoAgendamentoGlobal = 0;

		function tpSituacaoAgendamentoCB_cb(data) {
			tpSituacaoAgendamento_cb(data);
			setElementValue("tpSituacaoAgendamento", tpSituacaoAgendamentoGlobal);
		}

		function formLoad_cb(data, error) {
			onDataLoad_cb(data);
			// LMS-3252
			if(data != undefined && data.chavesNfe != undefined && data.chavesNfe.length != 0){
				setDisabled("nrChaveNfe", false);	
				setDisabled("nrChaveNfe_nrChave", false);
				
				nrChaveNfeListboxDef.cleanRelateds();
				resetValue("nrChaveNfe");

				for (var i = 0;  i < data.chavesNfe.length;  i++) {
					setElementValue("nrChaveNfe_nrChave", data.chavesNfe[i]);
					nrChaveNfeListboxDef.insertOrUpdateOption();
				}
				
				setDisabled("nrChaveNfe", true);	
				setDisabled("nrChaveNfe_nrChave", true);
			}
			
			tpSituacaoAgendamentoGlobal = getNestedBeanPropertyValue(data,"tpSituacaoAgendamento");

			var array = new Array();
			var iterator = new Array();
			var i=0;
			var str = new String();
			var nrNotaFiscal = " ";
			iterator = getNestedBeanPropertyValue(data,"agendamentoDoctoServicos");
			if (iterator != null) {
			
				var dData = new Array();

				for (var i=0; i < iterator.length; i++)	{			
					dData.push(iterator[i]);			
				}
				
				clearList(document.getElementById('documentoServico'), null);
				
				documentoServicoListboxDef.renderOptions({documentoServico:dData});				
			}

		   	desabilitaConsultarDocumentoButton(true);
		   
		   	regraTipoAgendamento(null);
		   	situacaoAgendamento();		   	


			var dhEnvio = getElementValue("dhEnvio");
			setDisabled("reenviarCarta", true);
			if (getElementValue("idAgendamentoEntrega") != null && getElementValue("idAgendamentoEntrega") != "" ) {
				showAgrupamentoCancelamento(true);				
				setDisabled('filial.idFilial', true);
				setDisabled("storeButton", true);
				if (dhEnvio != null && dhEnvio != "" && dhEnvio != undefined) {
					setDisabled("reenviarCarta", false);
    			}
		   	} else {
				showAgrupamentoCancelamento(false);
				setDisabled('filial.idFilial', false);
				setDisabled("storeButton", false);
		   	}
		   	
			if (getElementValue("idProcessoWorkflow") != "") {
				setDisabled(document, true);
			}
			
			// Desabilita campos de Documento de serviço
			setDisabled("documentoServico_doctoServico.filialByIdFilialOrigem.idFilial", true);
			setDisabled("documentoServico_doctoServico.idDoctoServico", true);
			
			showAgrupamentoDadosPrincipais(true);
			setFocusOnNewButton();
			
			if(data != undefined && data.chavesNfe != undefined && data.chavesNfe.length != 0 && (data.tpSituacaoAgendamento=="A" || data.tpAgendamento == "TA")){
				setDisabled("enviarEmail", false);
			}	
			
			var idsMonitoramentoCCT = getIdsMonitoramentoCCT();
		    if(idsMonitoramentoCCT != null && idsMonitoramentoCCT != '' && idsMonitoramentoCCT != undefined){
		    	preencherFomularioParaMonitoramentoCCT(data, error);
		    }
		}		
		
		//LMS-5950
		function preencherFomularioParaMonitoramentoCCT(data, error){
			setDisabled("enviarEmail", true);
			
			if (error != undefined) {
				alert(error);
				
			} else if(data != undefined && data.chavesNfeCCT != undefined && data.chavesNfeCCT.length != 0){
				
				setElementValue("tpDocumento", "NFE");
				changeTpDocumento(document.getElementById("tpDocumento"));
				nrChaveNfeListboxDef.cleanRelateds();
				resetValue("nrChaveNfe");
				
				for (var i = 0;  i < data.chavesNfeCCT.length;  i++) {
					setElementValue("nrChaveNfe_nrChave", data.chavesNfeCCT[i]);
					nrChaveNfeListboxDef.insertOrUpdateOption();
				}
				
				document.getElementById("destinatario.pessoa.nrIdentificacao").value = data.cliente.pessoa.nrIdentificacaoFormatado;
				document.getElementById("destinatario.pessoa.nmPessoa").value = data.cliente.pessoa.nmPessoa;
				document.getElementById("destinatario.idCliente").value = data.cliente.idCliente;
			}
		}
		
		function onDataLoadCancelar_cb(data, exception) {
			if (exception != undefined) {
				alert(exception);
			} else {
				tpSituacaoAgendamentoGlobal = getNestedBeanPropertyValue(data,"tpSituacaoAgendamento");
			    onDataLoad_cb(data);
				regraTipoAgendamento(null);
				situacaoAgendamento();
			}
		   	setFocus('__buttonBar:0.newButton', false);
		}				
		
		function zeroEsquerda(nrDoctoServico, tamanho) {
			var str = new String();
			str = nrDoctoServico;
		
			if (str != null && str.length < tamanho) {
				
				var j = 0;
				var tamanho = tamanho - str.length;
				for (j=0; j < tamanho; j++) {
					str = "0" + str;
				}
				
			}
			return str;
		}


<%-- DOCTO SERVICO --%>

	function changeTpDoctoServico(field) {
	
		var flag = changeDocumentWidgetType({
							   documentTypeElement:document.getElementById("documentoServico_doctoServico.tpDocumentoServico"), 
							   filialElement:document.getElementById('documentoServico_doctoServico.filialByIdFilialOrigem.idFilial'), 
							   documentNumberElement:document.getElementById('documentoServico_doctoServico.idDoctoServico'), 
							   documentGroup:'DOCTOSERVICE',
							   parentQualifier:'documentoServico_doctoServico',
							   actionService:'lms.entrega.manterAgendamentosAction'});
	
		var pms = document.getElementById("documentoServico_doctoServico.idDoctoServico").propertyMappings;
		pms[pms.length] = {modelProperty:"idDoctoServico", relatedProperty:"idDoctoServico"};		
		
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.idCliente", relatedProperty:"remetente.idCliente" };
		
		pms[pms.length] = {modelProperty:"filialByIdFilialDestino.idFilial", criteriaProperty:"filialDestino.idFilial", inlineQuery:true};
		pms[pms.length] = {modelProperty:"filialByIdFilialDestino.sgFilial", criteriaProperty:"filialDestino.sgFilial"};
		pms[pms.length] = {modelProperty:"filialByIdFilialDestino.pessoa.nmFantasia", criteriaProperty:"filialDestino.pessoa.nmFantasia"};
		
		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.idCliente", criteriaProperty:"destinatario.idCliente", inlineQuery:true};
		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.pessoa.nrIdentificacao", criteriaProperty:"destinatario.pessoa.nrIdentificacao"};
		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.pessoa.nmPessoa", criteriaProperty:"destinatario.pessoa.nmPessoa"};

		resetValue('idDoctoServico');
		
		if (field.value != '') {
			changeDocumentWidgetFilial({
									 	filialElement:document.getElementById('documentoServico_doctoServico.filialByIdFilialOrigem.idFilial'), 
									 	documentNumberElement:document.getElementById('documentoServico_doctoServico.idDoctoServico')
								  		});
		}
		
		setElementValue("documentoServico_doctoServico.filialByIdFilialOrigem.idFilial", getElementValue("filialOrigem.idFilial"));
		setElementValue("documentoServico_doctoServico.filialByIdFilialOrigem.sgFilial", getElementValue("filialOrigem.sgFilial"));
		setElementValue("documentoServico_doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia", getElementValue("filialOrigem.pessoa.nmFantasia"));
				
		if (getElementValue("filialOrigem.idFilial") != ''){
			setDisabled("documentoServico_doctoServico.filialByIdFilialOrigem.idFilial", true);
			setDisabled("documentoServico_doctoServico.idDoctoServico", false);
		}
		
		document.getElementById("documentoServico").selectedIndex = -1;
		
		return flag;
	}
 
<%-- DOCTO SERVICO --%>

	var varIdDoctoServico = undefined;
	function validaDoctoServico(eventObj) {		
		
		if (eventObj.name == 'modifyButton_click') {
			varIdDoctoServico = getElementValue("documentoServico_doctoServico.idDoctoServico");
			if (varIdDoctoServico == "") {
				return false;
			}
		} else if (eventObj.name == 'modifyButton_afterClick') {
			if (varIdDoctoServico != "") { 
				var array = new Array(1);
	
				array[0] = {idClienteDestinatario:getElementValue("destinatario.idCliente"),
							idClienteRemetente:getElementValue("remetente.idCliente"),
							idDoctoServico:varIdDoctoServico,
							idFilialAgendamento:getElementValue("filial.idFilial")};
	
				var sdo = createServiceDataObject("lms.entrega.manterAgendamentosAction.verificaExistenciaPce", "loadBasicData", {list:array});
		    	xmit({serviceDataObjects:[sdo]});
	    	}
		}
		
		if (eventObj.name == 'modifyButton_afterClick' ||
				eventObj.name == 'cleanButton_afterClick' ||
				eventObj.name == 'deleteButton_afterClick') {
			setDisabled("documentoServico_doctoServico.filialByIdFilialOrigem.idFilial",true);
			setDisabled("documentoServico_doctoServico.idDoctoServico",true);
		}
		
		if (eventObj.name == 'cleanButton_afterClick') {
			document.getElementById("documentoServico").selectedIndex = -1;
		}
		
		/* Ao deletar todos os documentos de serviço da lista, deve-se limpar o cliente
		 * destinatário e seus 'dependentes'.
		 */
		if (eventObj.name == "deleteButton_afterClick") {
			var nroDocsRestantes = document.getElementById("documentoServico").length;
			if (nroDocsRestantes == 0) {
				resetValue("destinatario.idCliente");
				onChangeDestinatario();
			}
		}
	}
	
	function changeListDoctoServico() {
		
		// Habilita campos se estamos inserindo um agendamento.
		// LMS-4599 -- Incluida a condição de ser diferente de empresa Parceira, para no caso do Agendamento, permanecer com os
		// campos do listbox desabilitados e apenas carregar o documento
		if (getElementValue("idAgendamentoEntrega") == ""  && tpEmpresa != "P") {
			setDisabled("documentoServico_doctoServico.filialByIdFilialOrigem.idFilial",false);
			setDisabled("documentoServico_doctoServico.idDoctoServico",false);
		}
		

		
		return true;
	}

	function setaEstadoDoctoServico(){
		resetValue("doctoServico.idDoctoServico");
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
		
		if (getElementValue("doctoServico.tpDocumentoServico") != '')
			setDisabled("doctoServico.idDoctoServico", false);
	
	}

	function clearList(object, idDoctoServico){
		documentoServicoListboxDef.cleanRelateds();	
		
		setDisabled("documentoServico_doctoServico.filialByIdFilialOrigem.sgFilial", true);
		setDisabled("documentoServico_doctoServico.nrDoctoServico", true);
		
		
		for(var i= object.options.length; i >= 0; i--) {
			if (idDoctoServico != null) {
				if (object.options[i] != null && object.options[i].data.doctoServico.idDoctoServico == idDoctoServico) {
					object.options.remove(i);
					break;
				}
			} else {
				object.options.remove(i);
			}
		}
		clearEmails();
	}
	
	function clearEmails() {
		if ("S" == getElementValue('apagarEmails')) {
			resetValue("dsEmailTomador");
			resetValue("dsEmailDestinatario");
		}
		setElementValue("apagarEmails", "S");
	}
	
	function notaFiscalPopup(data) {
		// Desabilita campos de Documento de serviço
		setDisabled("documentoServico_doctoServico.filialByIdFilialOrigem.idFilial", false);
		setDisabled("documentoServico_doctoServico.idDoctoServico", false);
	}
	
	
--></script>