<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.tributos.manterParametrosSubstituicaoTributariaAction">

	<adsm:form action="/tributos/manterParametrosSubstituicaoTributaria" idProperty="idParametroSubstituicaoTrib">
	
		<adsm:hidden property="tpSituacao" value="A" serializable="false"/>
  	    <adsm:lookup property="unidadeFederativa"
				     idProperty="idUnidadeFederativa" 
				     criteriaProperty="sgUnidadeFederativa"
					 service="lms.municipios.unidadeFederativaService.findLookup" 
					 dataType="text" 
					 maxLength="10"
					 required="true"
					 labelWidth="20%" 
					 width="10%" 
					 label="uf" 
					 size="5"
					 action="/municipios/manterUnidadesFederativas" 
					 minLengthForAutoPopUpSearch="2" 
					 exactMatch="false">
					 
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativa.siglaDescricao" modelProperty="siglaDescricao" />
			
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao" />
			
			<adsm:textbox dataType="text" 
					  property="unidadeFederativa.nmUnidadeFederativa" 
					  width="20%" 
					  size="20" 
					  serializable="false"
					  disabled="true" />
			
		</adsm:lookup>
		
		<adsm:hidden property="unidadeFederativa.siglaDescricao" serializable="false"/>			
		
		<adsm:textbox dataType="percent" 
					  label="percentualRetencao"
					  required="true"
					  property="pcRetencao" 
					  labelWidth="25%"
					  width="25%" 
					  size="20" 
					  minValue="0"
					  maxValue="100"/>
		
		<adsm:range label="vigencia" labelWidth="20%" width="30%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial"  required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
		
		<adsm:checkbox property="blEmbuteICMSParcelas" disabled="false" label="embuteIcmsParcelas" labelWidth="25%"  width="25%" />
		
		<adsm:checkbox property="blImpDadosCalcCTRC" disabled="false" label="imprimirDadosICMSCtrc" labelWidth="20%"  width="30%" />
		
		<adsm:checkbox property="blAplicarClientesEspeciais" disabled="false" label="blAplicarClientesEspeciais" labelWidth="25%"  width="25%" />
					  
		<adsm:checkbox property="blImprimeMemoCalcCte" disabled="false" label="imprimeMemoCalcCte" labelWidth="20%"  width="30%" />

		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:button caption="limpar" onclick="limpar()" disabled="false" id="btnLimpar"/>
			<adsm:removeButton/>
		</adsm:buttonBar>

   
	</adsm:form>
	
</adsm:window>
<script>

	function initWindow(evento){
		
		if (evento.name == "tab_click" || evento.name == "removeButton" ){
			limpar();
		}
	
		//desabilita os campos da tela quando vier da grid ou depois de salvar	
		if (evento.name == "gridRow_click" || evento.name == "storeButton"  ){
			desabilitaTodosCampos();
			if( evento.name == "storeButton" ){
				setFocus('btnLimpar',true,true);
			}
		}
		
		setDisabled('btnLimpar', false);
		
	}
	
	/**
 	* Function que limpa os campos da tela e desabilita todos os campos.
 	*
 	* chamado por: botão limpar
 	*/ 
	function limpar(){
		newButtonScript(document, true, {name:"newButton_click"});
		desabilitaTodosCampos(false);		
	}
	
	/**
 	* Function que desabilita todos os campos da tela e seta os valores default
 	*
 	* chamado por: limpar, initWindow
 	*/	
	function desabilitaTodosCampos(val){
		if (val == undefined){ 
			val = true;	
		}else if (val == false){
			setElementValue('pcRetencao',0);
			format(getElement('pcRetencao'));
		}
		setDisabled("unidadeFederativa.idUnidadeFederativa",val);
		setDisabled("pcRetencao",val);	
		setDisabled("dtVigenciaInicial",val);
		setDisabled("blEmbuteICMSParcelas",val);
		setDisabled("blImpDadosCalcCTRC",val);
		setDisabled("blImprimeMemoCalcCte",val);
		setDisabled("blAplicarClientesEspeciais",val);
		
		setFocusOnFirstFocusableField();				
	}
	
</script>