<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.tributos.aliquotaInssPessoaFisicaService" >
	<adsm:form action="/tributos/manterAliquotasINSS" idProperty="idAliquotaInssPessoaFisica">
		<adsm:textbox dataType="JTDate" property="dtInicioVigencia" label="vigenciaInicial" size="10" labelWidth="22%" width="28%" required="true"/>
		<adsm:textbox dataType="decimal" property="pcAliquota" label="percentualContribuicao" minValue="0" maxValue="100" mask="##0.00" size="10" maxLength="5" labelWidth="22%" width="28%" required="true" onchange="calculaValorMaximoARecolher()"/>
		<adsm:textbox dataType="currency" property="vlSalarioBase" label="salarioBaseContribuicao" minValue="0" size="22" maxLength="15" labelWidth="22%" width="28%" required="true" onchange="calculaValorMaximoARecolher()"/>
		<adsm:textbox dataType="currency" property="vlMaximoRecolhimento" label="valorMaximoRecolher" minValue="0" size="22" maxLength="18" labelWidth="22%" width="28%" disabled="true"/>
		<adsm:textbox dataType="decimal" property="pcBaseCalcReduzida" label="percentualBaseCalcReduzida" minValue="0" maxValue="100" mask="##0.00" size="10" maxLength="5" labelWidth="22%" width="28%" required="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:button caption="limpar" onclick="limpar(this)" disabled="false" id="btnLimpar"/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
/**
 	* Function que limpa os campos da tela e desabilita todos os campos.
 	*
 	* chamado por: botão limpar
 	*/
	function limpar(elem){
		newButtonScript(elem.document, true, {name:'newButton_click'});
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
		}


		setDisabled("dtInicioVigencia",val);
		setDisabled("pcAliquota",val);
		setDisabled("vlSalarioBase",val);
		setDisabled("pcBaseCalcReduzida",val);

		//ficam sempre desabilitados
		setDisabled("vlMaximoRecolhimento",true);

		setFocusOnFirstFocusableField();				
	}	
	
	/**
		Chamado ao iniciar a tela
	*/
	function initWindow(eventObj){
	
		setDisabled('btnLimpar', false);
	
		if (eventObj.name == "tab_click" || eventObj.name == "removeButton" ){
			desabilitaTodosCampos(false);
		}
	
		//desabilita os campos da tela quando vier da grid ou depois de salvar	
		if (eventObj.name == "gridRow_click" || eventObj.name == "storeButton"  ){
			desabilitaTodosCampos();
			//todos os campos ficam desabilitados entao o foco vai para limpar
			setFocus(document.getElementById('btnLimpar'),true,true);
			
		}
	
	}


	
	/**
	* Busca os valores de percentual da aliquota e o valor salário base para fazer o cálculo do valor máximo a recolher
	*
	*/
	function calculaValorMaximoARecolher(){
	
		var percentual = getElementValue("pcAliquota");
		var valor      = getElementValue("vlSalarioBase");
		
		var dados = new Array();
		
		if( ( percentual != undefined && percentual != "" ) && ( valor != undefined && valor != "" ) ){
   
	      	setNestedBeanPropertyValue(dados, "pcAliquota", percentual);
    	  	setNestedBeanPropertyValue(dados, "vlSalarioBase", valor);
      	   
      		var sdo = createServiceDataObject("lms.tributos.aliquotaInssPessoaFisicaService.findValorMaximoARecolher",
        		                              "setaValor",
            		                          dados);
		    xmit({serviceDataObjects:[sdo]});
		      
		} else {
		
			setElementValue("vlMaximoRecolhimento","");
		
		}	
	
	}
	
	/**
	* Seta o valor máximo a recolher 
	* dados = Valor calculado
	*/
	function setaValor_cb(dados, erro){
	
		if( erro ){
			alert(erro);			
			return false;
		}
		
		setElementValue("vlMaximoRecolhimento",dados._value);
		format(document.getElementById("vlMaximoRecolhimento"));
		document.getElementById("vlMaximoRecolhimento").disabled = true;
		setFocus("pcBaseCalcReduzida");
	
	}
	
</script>