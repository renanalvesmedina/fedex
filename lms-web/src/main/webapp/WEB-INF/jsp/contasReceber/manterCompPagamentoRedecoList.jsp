<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>

	/**Ao carregar a tela obtem os parametros da URL e popula os dados
	do Redeco*/	
	function myOnPageLoad(){

		/**idRedeco*/
		var idRedeco = queryString("idRedeco");

		/**Faz a chamada ajax para obter informações do Redeco*/		
		var data = new Array();	
		setNestedBeanPropertyValue(data, "idRedeco", idRedeco);		
		
		var sdo = createServiceDataObject("lms.contasreceber.manterComposicaoPagamentoRedecoAction.obterRedeco", "obterRedeco", data);
		xmit({serviceDataObjects:[sdo]});

		/**Obtem a filial do usuario logado*/
		data = new Array();
		sdo  = createServiceDataObject("lms.contasreceber.manterComposicaoPagamentoRedecoAction.findFilialLogada", "findFilialLogada", data);
		xmit({serviceDataObjects:[sdo]});		
		
		/**Faz o carregamento padrão da tela*/
		onPageLoad();
	}

	/**Preenche o numero do Redeco*/
	function obterRedeco_cb(data,error){
		if(error != undefined){
			alert(error);
		}
		onDataLoad_cb(data);
	}

	/**Prenche a filial logada*/
	function findFilialLogada_cb(data,error){
		if(error != undefined){
			alert(error);
		}
		setElementValue("filialLogada",data.filialLogada);
	}
	
	/**Obtem o parametro através da url*/
	function queryString(parameter) {  		 
	   var loc = parent.location.search.substring(1, parent.location.search.length);   	 
	   var param_value = false;   	 
	   var params = loc.split("&");   	 
       for (i=0; i<params.length;i++) {   	 
          param_name = params[i].substring(0,params[i].indexOf('='));   
          if (param_name == parameter) {                                          	 
             param_value = params[i].substring(params[i].indexOf('=')+1)   
          }   
        }
        if (param_value) {   
           return param_value;   
        } else {   
           return "";   
        }   
	 }

	/**Limpa o formulário mantendo informações do Redeco*/
	function limparForm(){

		var idRedeco = getElementValue("redeco.idRedeco");			
		var nrRedeco = getElementValue("redeco.nrRedeco");
		var flRedeco = getElementValue("redeco.filial.sgFilial");
		newButtonScript();
		setElementValue("redeco.idRedeco",idRedeco);
		setElementValue("redeco.nrRedeco",leftPad(nrRedeco,10,"0"));		
		setElementValue("redeco.filial.sgFilial",flRedeco);
	}


	/**Preenche caracteres a esquerda em um determinado numero*/
	function leftPad(value, size, ch){
		
		var sz = size-value.length;
		if(sz < 0){
			return value;
		}		
		var result = "";
		for(var i=0; i<sz; i++){
			result = ch+result;	
		}
		return result + value;
	}	
	
</script>
<adsm:window 
	service="lms.contasreceber.manterComposicaoPagamentoRedecoAction" onPageLoad="myOnPageLoad">

	<adsm:form
		action="/contasReceber/manterCompPagamentoRedeco" idProperty="idComposicaoPagamentoRedeco">
		
		<adsm:hidden property="filialLogada"/>

		<!-- Redeco -->
		<adsm:hidden property="redeco.idRedeco"/>		
		<adsm:hidden property="redeco.tpSituacaoRedeco"/>		
		<adsm:hidden property="redeco.blDigitacaoConcluida"/>		
		
		<adsm:textbox  property="redeco.filial.sgFilial" labelWidth="19%"  
			dataType="text" width="6%" label="numeroRedeco" size="5"
			disabled="true"/>

		<adsm:textbox  
			property="redeco.nrRedeco" 
			dataType="integer" size="10" 
			width="10%" mask="0000000000" 
			disabled="true"/>

		<!-- Tipo -->
		<adsm:combobox property="tpComposicaoPagamentoRedeco" 
			label="tipo" domain="DM_TIPO_PAGAMENTO_REDECO" 
			labelWidth="19%" width="80%" />

		<!-- Banco -->
		<adsm:lookup 
			dataType="integer" 
			property="banco" 
			idProperty="idBanco" 
			service="lms.configuracoes.bancoService.findLookup" 
			label="banco" 
			size="5" 
			maxLength="3"
			labelWidth="19%" 
			width="9%"
			criteriaProperty="nrBanco" 
			serializable="true"
			action="/configuracoes/manterBancos">			

			<adsm:propertyMapping 
				modelProperty="nmBanco" 
				relatedProperty="banco.nmBanco"/> 
				
			<adsm:textbox 
				property="banco.nmBanco" 
				dataType="text" 
				size="45" 
				width="60%"
				maxLength="30" 
				disabled="true" 
				serializable="false"/>
				
		</adsm:lookup>
		
		<!-- Data do pagamento -->
        <adsm:range label="dataPagamento" labelWidth="19%" width="29%" maxInterval="31">
			<adsm:textbox dataType="JTDate" property="dtPagamentoInicial"/>
			<adsm:textbox dataType="JTDate" property="dtPagamentoFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="compPagamento"/>
			<adsm:button     id="btnLimpar" caption="limpar" buttonType="newButton" onclick="limparForm();"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idComposicaoPagamentoRedeco" defaultOrder="" selectionMode="check"  property="compPagamento" gridHeight="300" unique="true" rows="10">

        <adsm:gridColumn title="filial" property="filial.sgFilial" width="10%" />
        <adsm:gridColumn title="tipo" property="tpComposicaoPagamentoRedeco.description" width="15%" />
		<adsm:gridColumn title="banco" property="banco.nmBanco" width="30%" />
        <adsm:gridColumn title="dataPagamento" property="dtPagamento" width="10%"  dataType="date"/>
		<adsm:gridColumn title="valor" property="vlPagamento" width="10%" dataType="decimal" />
		<adsm:gridColumn title="observacao" property="obComposicaoPagamentoRedeco" width="25%" />
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>	
	</adsm:grid>
</adsm:window>
