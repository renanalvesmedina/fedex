<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.tributos.manterServicosISSMunicipioAction">
	<adsm:form action="/tributos/manterServicosISSMunicipio" idProperty="idIssMunicipioServico">
		
		<adsm:hidden property="statusAtivo" serializable="false" value="A"/>
		
		<adsm:lookup property="municipio" 
					 idProperty="idMunicipio"
					 criteriaProperty="nmMunicipio"
					 label="municipio" 
					 service="lms.tributos.manterServicosISSMunicipioAction.findMunicipiosLookup"
					 dataType="text"
					 labelWidth="15%" 
					 width="35%" 
					 size="35" 
					 maxLength="60"
					 action="/municipios/manterMunicipios"
					 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"
					 required="true">			
			<adsm:propertyMapping criteriaProperty="statusAtivo" modelProperty="tpSituacao" inlineQuery="true" />
		</adsm:lookup>
			
		<adsm:combobox label="servicoMunicipio" 
					   property="servicoMunicipio.idServicoMunicipio"
					   optionLabelProperty="nrServicoDsServicoMunicipio" 
					   optionProperty="idServicoMunicipio"
					   service="lms.tributos.manterServicosISSMunicipioAction.findServicosMunicipioCombo" 
					   boxWidth="230"
					   labelWidth="17%" 
					   width="32%"
					   onlyActiveValues="true">
			<adsm:propertyMapping criteriaProperty="municipio.idMunicipio" modelProperty="municipio.idMunicipio"/>
		</adsm:combobox>	
			
		<adsm:combobox label="servicoAdicional"
				   	   property="servicoAdicional.idServicoAdicional" 				   	   
				       service="lms.tributos.manterServicosISSMunicipioAction.findServicosAdicionaisCadCombo" 
				       optionLabelProperty="dsServicoAdicional" 
				       optionProperty="idServicoAdicional" 
				       labelWidth="15%" 
				       width="35%"
				       boxWidth="244"/>
			
		<adsm:combobox label="outroServico" 
					   property="servicoTributo.idServicoTributo"
					   optionLabelProperty="dsServicoTributo" 
					   optionProperty="idServicoTributo" 
					   service="lms.tributos.manterServicosISSMunicipioAction.findServicosTributosAtivosCombo" 
					   boxWidth="230"
					   labelWidth="17%" 
					   width="32%"/>

		<adsm:textbox label="diaRecolhimento" 
					  dataType="integer"
					  property="ddRecolhimento" 
					  size="10" 
					  maxLength="2" 
					  labelWidth="15%"
					  width="35%"
					  minValue="1"
					  maxValue="31"/>

		<adsm:combobox label="formaPagamento" 
					   property="tpFormaPagamento"
					   domain="DM_FORMA_PGTO_ISS" 
					   labelWidth="17%" 
					   width="33%"/>

		<adsm:buttonBar>
			<adsm:button caption="manterAliquotasINSSBotao" action="tributos/manterAliquotasISS" cmd="main">
				<adsm:linkProperty src="idIssMunicipioServico" target="issMunicipioServico.idIssMunicipioServico"/>
				<adsm:linkProperty src="municipio.idMunicipio" target="issMunicipioServico.municipio.idMunicipio"/>
				<adsm:linkProperty src="municipio.nmMunicipio" target="issMunicipioServico.municipio.nmMunicipio"/>
				<adsm:linkProperty src="servicoMunicipio.idServicoMunicipio" target="issMunicipioServico.servicoMunicipio.idServicoMunicipio"/>				
				<adsm:linkProperty src="servicoAdicional.idServicoAdicional" target="issMunicipioServico.servicoAdicional.idServicoAdicional"/>								
				<adsm:linkProperty src="servicoTributo.idServicoTributo" target="issMunicipioServico.servicoTributo.idServicoTributo"/>
			</adsm:button>
			<adsm:button caption="salvar" id="storeButton" onclick="verificaCampos(this)" buttonType="storeButton" disabled="false"/>
			<adsm:newButton id="btnNovo"/>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>
<script>
	
	/**
	* Regra 3.2 - O Campo Servico Adicional ou o Campo Outro Serviço deve ser informado
	*
	*/
	function verificaCampos(elemento){
		var tab = getTab(elemento.form.document);
		// apenas executa a validação se achar uma tab na tela.
		if (tab != null) {
			var valid = tab.validate({name:"storeButton_click"});
		
			// apenas prossegue se a validação dos dados foi realizada com sucesso.
			if (valid == false) {
				return false;
			}
		}
	
		var idServicoAdicional = getElementValue("servicoAdicional.idServicoAdicional");
		var idServicoTributo   = getElementValue("servicoTributo.idServicoTributo");
	
		if( ( idServicoAdicional == null || idServicoAdicional == "" ) && 
		    ( idServicoTributo   == null || idServicoTributo   == "" ) || 
		    ( idServicoTributo   != null && idServicoTributo   != "" ) && 
		    ( idServicoAdicional != null && idServicoAdicional != "" ))
		    {
		    alert(parent.i18NLabel.getLabel("LMS-27044"));
		    return false;
		} else {
			storeButtonScript('lms.tributos.manterServicosISSMunicipioAction.store', 'store', elemento.form);	
		}
	
	}
	
	/** Caso seja click na grid ou no storeButton, desabilita os campos abaixo. Caso contrário os habilita */
	function initWindow(eventObj){
		if(eventObj.name == "gridRow_click" || eventObj.name == "storeButton"){
			setDisabled("municipio.idMunicipio", true);
			setDisabled("servicoAdicional.idServicoAdicional", true);
			setDisabled("servicoTributo.idServicoTributo", true);			
		}else{
			setDisabled("municipio.idMunicipio", false);
			setDisabled("servicoAdicional.idServicoAdicional", false);
			setDisabled("servicoTributo.idServicoTributo", false);		
		}
		
		if( eventObj.name == 'storeButton'){
			setFocus('btnNovo',true,true);
		} else {
			setFocusOnFirstFocusableField(document);
		}
		
	}
</script>