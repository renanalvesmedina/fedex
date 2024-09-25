<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.tributos.manterTipoTributacaoExportacaoUfAction">

	<adsm:form 	
		action="/tributos/manterTipoTributacaoExportacaoUf"
		idProperty="idTipoTributacaoUf" onDataLoadCallBack="myOnDataLoad">
	
		<adsm:hidden property="tpSituacao" value="A" serializable="false"/>
		
		<adsm:lookup 
  	    			property="unidadeFederativa"
				    idProperty="idUnidadeFederativa" 
				    criteriaProperty="sgUnidadeFederativa"
					service="lms.tributos.manterTipoTributacaoExportacaoUfAction.findLookupUF" 
					dataType="text" 
					labelWidth="20%" 
					maxLength="3"
					width="10%" 
					label="uf" 
					size="3"
					required="true"
					action="/municipios/manterUnidadesFederativas" 
					minLengthForAutoPopUpSearch="2" 
					exactMatch="false">
					

			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao" />				 
			
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativa.siglaDescricao" modelProperty="siglaDescricao" />
			

		<adsm:textbox
					dataType="text" 
					disabled="true"
					property="unidadeFederativa.nmUnidadeFederativa" 
					serializable="false"
					width="20%" 
					size="20"/>

			
		</adsm:lookup>
		
		<adsm:hidden
					property="unidadeFederativa.siglaDescricao" 
					serializable="false"/>					
					  
		<adsm:combobox 
					label="tipoTributacao" 
					property="tipoTributacaoIcms.idTipoTributacaoIcms" 
					service="lms.tributos.manterTipoTributacaoExportacaoUfAction.findComboTipoTributacaoIcms" 
					onlyActiveValues="true" 
					optionLabelProperty="dsTipoTributacaoIcms" 
					optionProperty="idTipoTributacaoIcms" 
					labelWidth="20%" 
					width="30%" 
					required="true"
					boxWidth="200"/>	  
					

		<adsm:combobox label="tipoFrete" property="tpTipoFrete" 
				   domain="DM_TIPO_FRETE"
				   labelWidth="20%" width="30%"
				   renderOptions="true"/>

		<adsm:combobox label="abrangencia" property="tpAbrangenciaUf" 
				   domain="DM_ABRANGENCIA_UF"
				   width="20%" labelWidth="20%"
				   renderOptions="true"/>

		<adsm:combobox property="blContribuinte" 
					label="tomadorContribuinte" labelWidth="20%" renderOptions="true"
					domain="DM_SIM_NAO" width="80%" required="true"/> 
  
					
		<adsm:range label="vigencia" labelWidth="20%" width="80%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
		
		<adsm:buttonBar>
			<adsm:storeButton id="btnSalvar" service="lms.tributos.manterTipoTributacaoExportacaoUfAction.storeTipoTributacaoExportacaoUf"/>
			<adsm:newButton id="btnLimpar"/>		
			<adsm:removeButton id="btnExcluir"/>
		</adsm:buttonBar>

	</adsm:form>
	
</adsm:window>

<script>
	
	function myOnDataLoad_cb(data, error){		
		onDataLoad_cb(data,error);

		verificaVigencias(data);
	}

	function verificaVigencias(data){
		
		if(data.comparaFinal != undefined && data.comparaFinal < 0){

			desabilitaTodosCampos();
			setDisabled("dtVigenciaFinal",true);
			setDisabled("btnSalvar",true);
			setDisabled("btnExcluir",true);			
			
		}else{

			if(data.comparaInicial > 0){
				desabilitaTodosCampos(false);
				setDisabled("unidadeFederativa.idUnidadeFederativa",true);
				setDisabled("tipoTributacaoIcms.idTipoTributacaoIcms",true);
			}else{
				desabilitaTodosCampos();
				setDisabled("btnExcluir",true);	
			}						
		}		
	}
	
	/**
 	* Function que desabilita todos os campos da tela e seta os valores default
 	*
 	* chamado por: limpar, initWindow
 	*/	
	function desabilitaTodosCampos(val){
		if (val == undefined){ 
			val = true;	
		}


		setDisabled("unidadeFederativa.idUnidadeFederativa",val);
		setDisabled("tipoTributacaoIcms.idTipoTributacaoIcms",val);
		setDisabled("dtVigenciaInicial",val);

		setDisabled("tpTipoFrete",val);
		setDisabled("tpAbrangenciaUf",val);
		setDisabled("blContribuinte",val);


		//ficam sempre desabilitados
		setDisabled("dtVigenciaFinal",false);

		setFocusOnFirstFocusableField();				
	}	
	
	/**
		Chamado ao iniciar a tela
	*/
	function initWindow(eventObj){
		setDisabled('btnLimpar', false);
	
		if (eventObj.name == "tab_click" || eventObj.name == "removeButton" || eventObj.name == "newButton_click"){
			desabilitaTodosCampos(false);
		}
	
		if(eventObj.name == "storeButton"){
			verificaVigencias(getElementValue("dtVigenciaInicial"),getElementValue("dtVigenciaFinal"));
		}
		
		//desabilita os campos da tela quando vier da grid ou depois de salvar	
		if (eventObj.name == "gridRow_click"){
			desabilitaTodosCampos();
			//todos os campos ficam desabilitados entao o foco vai para limpar
			setFocus(document.getElementById('btnLimpar'),true,true);
			
		}
	}
</script>