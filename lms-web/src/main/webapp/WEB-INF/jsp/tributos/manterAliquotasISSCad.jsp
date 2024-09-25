<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.tributos.manterAliquotasISSAction">
	<adsm:form action="/tributos/manterAliquotasISS"
		idProperty="idAliquotaIssMunicipioServ">

		<adsm:hidden property="issMunicipioServico.idIssMunicipioServico"
			serializable="true" />
		<adsm:hidden property="issMunicipioServico.municipio.idMunicipio"
			serializable="true" />
		<adsm:hidden
			property="issMunicipioServico.servicoMunicipio.idServicoMunicipio"
			serializable="true" />
		<adsm:hidden
			property="issMunicipioServico.servicoAdicional.idServicoAdicional"
			serializable="true" />
		<adsm:hidden
			property="issMunicipioServico.servicoTributo.idServicoTributo"
			serializable="true" />

		<adsm:textbox dataType="text"
			property="issMunicipioServico.municipio.nmMunicipio"
			label="municipio" size="45" maxLength="60" width="35%"
			disabled="true" serializable="false" />

		<adsm:textbox dataType="text"
			property="issMunicipioServico.servicoMunicipio.dsServicoMunicipio"
			label="servicoMunicipio" size="50" maxLength="60" width="35%"
			disabled="true" serializable="false" />

		<adsm:textbox dataType="text"
			property="issMunicipioServico.servicoAdicional.dsServicoAdicional"
			label="servicoAdicional" size="45" maxLength="60" width="35%"
			disabled="true" serializable="false" />

		<adsm:textbox dataType="text"
			property="issMunicipioServico.servicoTributo.dsServicoTributo"
			label="outroServico" size="50" maxLength="60" width="35%"
			disabled="true" serializable="false" />

		<adsm:range label="vigencia">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
		</adsm:range>

		<adsm:checkbox label="emiteNFServico" property="blEmiteNfServico" 
			labelWidth="17%" width="33%" />

		<adsm:textbox label="percentualAliquota" required="true" size="10"
			maxLength="5" dataType="decimal" property="pcAliquota" minValue="0"
			maxValue="100" mask="##0.00" />
			
		<adsm:checkbox label="retencaoTomadorServico" property="blRetencaoTomadorServico"
			labelWidth="17%" width="33%" />	

		<adsm:textbox label="percentualEmbute" required="true"
			size="10" maxLength="5" dataType="decimal" property="pcEmbute"
			width="77%" minValue="0" maxValue="100" disabled="true" mask="##0.00" />
			
			
		<adsm:textarea columns="45" rows="4" label="observacao"  maxLength="500" property="obAliIssMunServ" />

		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton/>
			<adsm:removeButton />
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>
<script>
	
	/**
 	* Function que desabilita todos os campos da tela e seta os valores default
 	*
 	* chamado por: limpar, initWindow
 	*/	
	function desabilitaTodosCampos(val){
		if (val == undefined){ 
			val = true;	
		}else if (val == false){
			setElementValue("pcAliquota", "0,00");
			setElementValue("pcEmbute", "0,00");
		}


		setDisabled("dtVigenciaInicial",val);
		setDisabled("blEmiteNfServico",val);
		setDisabled("pcAliquota",val);
		setDisabled("blRetencaoTomadorServico",val);		
		setDisabled("obAliIssMunServ",val);		
		

		//ficam sempre desabilitados
		setDisabled("issMunicipioServico.municipio.nmMunicipio",true);
		setDisabled("issMunicipioServico.servicoMunicipio.dsServicoMunicipio",true);
		setDisabled("issMunicipioServico.servicoAdicional.dsServicoAdicional",true);	
		setDisabled("issMunicipioServico.servicoTributo.dsServicoTributo",true);

		//conforme solicitacao do Joelson esse campo sempre ira ficar desabilitado
		setDisabled("pcEmbute",true);					
	}	
	
	
	

	/**
	* Método que seta os dados vindos da tela de Manter Serviços de ISS do Município
	* Estes dados estão na tela de listagem.
	*/
	function initWindow(eventObj){	
		if( eventObj.name != "gridRow_click" ){				
			setElementValue("issMunicipioServico.municipio.idMunicipio", 
							getTabGroup(this.document).getTab("pesq").getElementById("issMunicipioServico.municipio.idMunicipio"));		
			setElementValue("issMunicipioServico.municipio.nmMunicipio", 
							getTabGroup(this.document).getTab("pesq").getElementById("issMunicipioServico.municipio.nmMunicipio"));		
							
							
			setElementValue("issMunicipioServico.servicoMunicipio.idServicoMunicipio", 
							getTabGroup(this.document).getTab("pesq").getElementById("issMunicipioServico.servicoMunicipio.idServicoMunicipio"));		
			setElementValue("issMunicipioServico.servicoMunicipio.dsServicoMunicipio", 
							getTabGroup(this.document).getTab("pesq").getElementById("issMunicipioServico.servicoMunicipio.dsServicoMunicipio"));		
							
			setElementValue("issMunicipioServico.servicoAdicional.idServicoAdicional", 
							getTabGroup(this.document).getTab("pesq").getElementById("issMunicipioServico.servicoAdicional.idServicoAdicional"));		
			setElementValue("issMunicipioServico.servicoAdicional.dsServicoAdicional", 
							getTabGroup(this.document).getTab("pesq").getElementById("issMunicipioServico.servicoAdicional.dsServicoAdicional"));		
							
			setElementValue("issMunicipioServico.servicoTributo.idServicoTributo", 
							getTabGroup(this.document).getTab("pesq").getElementById("issMunicipioServico.servicoTributo.idServicoTributo"));				
			setElementValue("issMunicipioServico.servicoTributo.dsServicoTributo", 
							getTabGroup(this.document).getTab("pesq").getElementById("issMunicipioServico.servicoTributo.dsServicoTributo"));				
							

											
		}	
		// setar os percentuais das alíquotas em 0						
		if ((eventObj.name == "newButton_click") || (eventObj.name == "tab_click")){
			setElementValue("pcAliquota",'0,00');	
			setElementValue("pcEmbute",'0,00');				
			setElementValue("blEmiteNfServico",'S');
			desabilitaTodosCampos(false);
			setFocusOnFirstFocusableField();										
		}
		
		
		if (eventObj.name == "tab_click" || eventObj.name == "removeButton" ){
			desabilitaTodosCampos(false);
			setFocusOnFirstFocusableField();
		}
	
		//desabilita os campos da tela quando vier da grid ou depois de salvar	
		if (eventObj.name == "gridRow_click" || eventObj.name == "storeButton"  ){
			desabilitaTodosCampos(true);
		}
	}
	
</script>
