<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.telefoneContatoService" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/configuracoes/manterTelefonesContatos">
		<adsm:label key="branco" style="width='0'"/>
		<adsm:hidden property="labelPessoaTemp" serializable="false"/>
		<adsm:hidden property="idContatoTemp" serializable="false"/>		
		<adsm:hidden property="telefoneEndereco.pessoa.idPessoa" serializable="true"/>				

 		<adsm:label key="branco" style="width:0"/>
		<td colspan="15" id="labelPessoa" class="FmLbRequerido"></td>
        <adsm:textbox property="telefoneEndereco.pessoa.nrIdentificacao" serializable="false" dataType="integer" size="20" maxLength="20" width="85%" disabled="true">
	        <adsm:textbox property="telefoneEndereco.pessoa.nmPessoa" serializable="false" dataType="text" size="60" disabled="true"/>
        </adsm:textbox>


		<adsm:combobox property="contato.idContato" autoLoad="false" optionLabelProperty="nmContato" optionProperty="idContato" service="lms.configuracoes.contatoService.find" style="width:230px" label="contato"/>
		<adsm:combobox property="telefoneEndereco.idTelefoneEndereco" autoLoad="false" optionLabelProperty="dddTelefone" optionProperty="idTelefoneEndereco" service="lms.configuracoes.telefoneEnderecoService.find" label="telefone"/>
		<adsm:textbox dataType="text" property="nrRamal" onchange="return verificaBrancos(this);" label="ramal" maxLength="10" size="10"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="telefoneContato"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTelefoneContato" property="telefoneContato" defaultOrder="contato_.nmContato, telefoneEndereco_.nrTelefone, nrRamal" rows="12" 
	           onRowClick="myFindByIdCustomized" onRowDblClick="myFindByIdCustomized">
		<adsm:gridColumn width="34%" title="contato" dataType="text" property="contato.nmContato" />
		<adsm:gridColumn width="33%" title="telefone" dataType="text" property="telefoneEndereco.dddTelefone"/>
		<adsm:gridColumn width="33%" title="ramal" dataType="text" property="nrRamal"/>
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>		
	</adsm:grid>
</adsm:window>
<script>
	function myOnPageLoad_cb(data, erro){
		onPageLoad_cb();
		
		document.getElementById('labelPessoa').innerHTML=getElementValue("labelPessoaTemp")+":";
		
		addServiceDataObject(createServiceDataObject("lms.configuracoes.contatoService.find", "contato.idContato", {pessoa:{idPessoa:getElementValue('telefoneEndereco.pessoa.idPessoa')}}));
		addServiceDataObject(createServiceDataObject("lms.configuracoes.telefoneEnderecoService.find", "telefoneEndereco.idTelefoneEndereco", {pessoa:{idPessoa:getElementValue('telefoneEndereco.pessoa.idPessoa')}}));
		xmit({onXmitDone:"atribuirValorCombos"});	
	}

	/**
	* Verifica se o campo passado por parâmetro foi preenchido com espaços em branco.	
	* Se afirmativo, não deixa o usuário sair do campo até que os espaços sejam retirados.
	*/
	function verificaBrancos(elemento){
		if(elemento.value != null && trim(elemento.value) == ""){
			setFocus(elemento);
			elemento.select();
			return false;
		} 
		return true;
	}

	function atribuirValorCombos_cb(){
		setElementValue('contato.idContato',getElementValue('idContatoTemp'));
	}
	
	function myFindByIdCustomized(rowId){
         
		var dados = new Array();         
		setNestedBeanPropertyValue(dados, "idTelefoneContato", rowId);
		
		var sdo = createServiceDataObject("lms.configuracoes.manterTelefonesContatosAction.findByIdCustomized",
 	                                      "retornoFindById",
		                                  {idTelefoneContato: rowId});
		xmit({serviceDataObjects:[sdo]});
		
		return false;
		
	}
	
	/**
	*	Método de retorno do findById
	*	Lança uma exception se essa ocorrer ou seta os dados retornados da pesquisa
	*/
	function retornoFindById_cb(data, erro){
	
		if( erro != undefined ){
			alert(erro);
			setFocusOnFirstFocusableField();
			return false;
		}	
		
		var documentCad = getTabGroup(this.document).getTab("cad").tabOwnerFrame.document;
			
		telefoneContatoGridDef.detailGridRow("setaDados", data);
		
	}
</script>