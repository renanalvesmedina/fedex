<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.prestcontasciaaerea.desmarcarPrestacoesContasAction" onPageLoadCallBack="myPageLoadCallBack">
	<adsm:form action="/prestContasCiaAerea/desmarcarPrestacoesContas">
	
		<adsm:hidden property="tpEmpresa" serializable="false" value="C"/>

		<adsm:combobox label="companhiaAerea" autoLoad="false"
					   property="empresa.idEmpresa"
					   optionLabelProperty="pessoa.nmPessoa"
					   optionProperty="idEmpresa"
					   boxWidth="200"
					   service="lms.prestcontasciaaerea.desmarcarPrestacoesContasAction.findComboEmpresas"
					   required="true"/>

		<adsm:textbox label="numeroCT"
		              property="numeroCT"
		              dataType="text"   
		              maxLength="15" 
		              size="15"
		              required="true"/>

		<adsm:buttonBar>
			<adsm:button caption="desmarcarPrestacoesContas" onclick="desmarcarPrestacaoConta(this)" disabled="false" buttonType="storeButton"/>
			<adsm:resetButton id="resetButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	/**
	* Método que sobrescreve o onPageLoad_cb para preencher os dados da combo de cia aérea
	*
	*/
	function myPageLoadCallBack_cb(data,erro){
	
		onPageLoad_cb(data,erro);
	
		var tpEmpresa = getElementValue("tpEmpresa");
		
		var dados = new Array();
		
		setNestedBeanPropertyValue(dados, "empresa.tpEmpresa", tpEmpresa);
		
		var sdo = createServiceDataObject("lms.prestcontasciaaerea.desmarcarPrestacoesContasAction.findComboEmpresas",
                                          "empresa_idEmpresa",
                                          dados);
	    xmit({serviceDataObjects:[sdo]});
	    
	    setFocusOnFirstFocusableField(document);
	
	}

	/**
	* Método chamado ao clicar em 'Desmarcar prestacação de contas'
	* Chama o método updatePrestacaoContas da desmarcarPrestacaoContasAction
	*/
	function desmarcarPrestacaoConta(element){
	
		var valid = true;
		var tab = getTab(element.form.document);
		// apenas executa a valida??o se achar uma tab na tela.
		if (tab != null) {
			valid = tab.validate({name:"storeButton_click"});
		} else { // se n?o possuir uma tab ent?o valida diretamente a tela, deve ser uma popup
			valid = validateTabScript(element.form);
		}
		// apenas prossegue se a valida??o dos dados foi realizada com sucesso.
		if (valid == false) {
			return false;
		}
	
		var idCompanhiaAerea = getElementValue("empresa.idEmpresa");
		var nrCt = getElementValue("numeroCT");
		
		var dados = new Array();
   
      	setNestedBeanPropertyValue(dados, "idCompanhiaAerea", idCompanhiaAerea);
		setNestedBeanPropertyValue(dados, "numeroCT", nrCt);
   
      	var sdo = createServiceDataObject("lms.prestcontasciaaerea.desmarcarPrestacoesContasAction.updatePrestacaoContas",
                                          "verificaErro",
                                          dados);
	    xmit({serviceDataObjects:[sdo]});
	
	}
	
	/**
	* Método de retorno após o processo terminar.	
	* Gera a mensagem de erro se for o caso e/ou a mensagem de sucesso !
	*/
	function verificaErro_cb(dados,erro){
	
		if( erro != undefined ){
			alert(erro);
			setFocusOnFirstFocusableField(document);
			return;
		}			

		showSuccessMessage();
		setFocusOnFirstFocusableField(document);		
			
	}

</script>