<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.tipoEnderecoPessoaService" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/configuracoes/manterTiposEndereco" idProperty="idTipoEnderecoPessoa">
		
		<adsm:hidden property="enderecoPessoa.pessoa.idPessoa"/>
		<adsm:label key="branco" style="width:0"/>
		<adsm:hidden property="labelPessoaTemp" serializable="false"/>		
        <td colspan="15" id="labelPessoa" class="FmLbRequerido"></td>

        <adsm:textbox property="enderecoPessoa.pessoa.nrIdentificacaoFormatado" serializable="false" dataType="text" size="20" maxLength="20" width="13%" disabled="true">
        	<adsm:textbox property="enderecoPessoa.pessoa.nmPessoa" serializable="false" dataType="text" size="60" width="74%" disabled="true"/>
        </adsm:textbox>

		<adsm:hidden property="enderecoPessoa.idEnderecoPessoa"/>
		<adsm:textbox property="enderecoPessoa.enderecoCompleto" dataType="text" size="100" disabled="true" serializable="false" label="endereco" width="85%"/>

		<adsm:combobox property="tpEndereco" 
					   optionProperty="value" 
					   autoLoad="false"
					   optionLabelProperty="description"
					   label="tipoEndereco" 
					   service="lms.configuracoes.tipoEnderecoPessoaService.findTipoEnderecoPessoa"> 
		</adsm:combobox>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoEnderecoPessoa"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idTipoEnderecoPessoa" property="tipoEnderecoPessoa" gridHeight="200" unique="true" defaultOrder="tpEndereco"
	           onRowClick="myFindByIdCustomized" onRowDblClick="myFindByIdCustomized">
		<adsm:gridColumn title="tipoEndereco" property="tpEndereco" width="100%" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
	function myOnPageLoad_cb(dados, erros){
      onPageLoad_cb(dados, erros);
      document.getElementById('labelPessoa').innerHTML=getElementValue("labelPessoaTemp")+":";
      
      var dados = new Array();
	  setNestedBeanPropertyValue(dados, "idPessoa", getElementValue("enderecoPessoa.pessoa.idPessoa"));
	  var sdo = createServiceDataObject("lms.configuracoes.tipoEnderecoPessoaService.findTipoEnderecoPessoa",
	                                             "tpEndereco",
	                                             dados);
 	  xmit({serviceDataObjects:[sdo]});
 	  
	}
	
	function myFindByIdCustomized(rowId){
         
		var dados = new Array();         
		setNestedBeanPropertyValue(dados, "idTipoEnderecoPessoa", rowId);
		
		var sdo = createServiceDataObject("lms.configuracoes.tipoEnderecoPessoaService.findByIdCustomized",
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
			
		tipoEnderecoPessoaGridDef.detailGridRow("myDataLoad_cb", data);
		
	}
</script>
