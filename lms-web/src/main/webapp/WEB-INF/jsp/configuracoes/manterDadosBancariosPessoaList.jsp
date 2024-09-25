<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.contaBancariaService" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/configuracoes/manterDadosBancariosPessoa" idProperty="idContaBancaria">
	
		<adsm:hidden property="pessoa.idPessoa"/>
		<adsm:label key="branco" style="width:0"/>
		<adsm:hidden property="labelPessoaTemp" serializable="false"/>		
        <td colspan="15" id="labelPessoa" class="FmLbRequerido"></td>

        <adsm:textbox property="pessoa.nrIdentificacao" serializable="false" dataType="text" size="20" maxLength="20" width="13%" disabled="true">
	        <adsm:textbox property="pessoa.nmPessoa" serializable="false" dataType="text" size="60" width="74%" disabled="true"/>        
        </adsm:textbox>
		
		<adsm:lookup dataType="integer" 
					 property="agenciaBancaria.banco" 
					 idProperty="idBanco"
					 service="lms.configuracoes.bancoService.findLookup" 
					 label="banco" size="5" maxLength="3"
					 criteriaProperty="nrBanco" 
					 onchange="return bancoChange(this);"
					 action="/configuracoes/manterBancos">	
			<!--  adsm:propertyMapping criteriaProperty="agenciaBancaria.banco.nmBanco" modelProperty="nmBanco" inlineQuery="false"/ -->		 			
			<adsm:propertyMapping modelProperty="nmBanco" formProperty="agenciaBancaria.banco.nmBanco"/> 
			<adsm:textbox property="agenciaBancaria.banco.nmBanco" dataType="text" size="25" maxLength="30" disabled="true" serializable="false"/>
		</adsm:lookup>		
		
		<adsm:lookup dataType="integer" 
					 property="agenciaBancaria" 
					 idProperty="idAgenciaBancaria"
					 service="lms.configuracoes.agenciaBancariaService.findLookup" 
					 label="agencia" maxLength="4" size="7" 
					 criteriaProperty="nrAgenciaBancaria"
					 onDataLoadCallBack="agenciaCallBack"
					 onPopupSetValue="agenciaPopUpSetValue"
					 action="/configuracoes/manterAgencias">			
			
			<adsm:propertyMapping criteriaProperty="agenciaBancaria.banco.idBanco" modelProperty="banco.idBanco" />
			<adsm:propertyMapping criteriaProperty="agenciaBancaria.banco.nmBanco" modelProperty="banco.nmBanco" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="agenciaBancaria.banco.nrBanco" modelProperty="banco.nrBanco" inlineQuery="false"/>
			
			<adsm:propertyMapping modelProperty="nmAgenciaBancaria" formProperty="agenciaBancaria.nmAgenciaBancaria"/> 
			
			<adsm:textbox property="agenciaBancaria.nmAgenciaBancaria" dataType="text" size="25" maxLength="30" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:complement label="numeroConta" width="35%">
			<adsm:textbox label="numeroConta" dataType="integer" property="nrContaBancaria" maxLength="15" size="14"/>
			<adsm:textbox dataType="text" property="dvContaBancaria" maxLength="2"  style="width:30px;"/>
		</adsm:complement>

		<adsm:combobox property="tpConta" label="tipoConta" domain="DM_TIPO_CONTA"/>		
		<adsm:textbox label="vigencia" dataType="JTDate" property="dtVigencia" width="35%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="contaBancaria"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid gridHeight="200" idProperty="idContaBancaria" property="contaBancaria" defaultOrder="agenciaBancaria_banco_.nmBanco, agenciaBancaria_.nrAgenciaBancaria, nrContaBancaria" rows="11"
	           onRowClick="myFindByIdCustomized" onRowDblClick="myFindByIdCustomized">
		<adsm:gridColumn width="30%" title="banco" property="agenciaBancaria.banco.nmBanco"/>
		<adsm:gridColumn width="10%" title="agencia" property="agenciaBancaria.nrAgenciaBancaria" align="right"/>
		<adsm:gridColumn width="17%" title="numeroConta" property="nrContaDvConta" align="left"/>
		<adsm:gridColumn width="15%" title="tipoConta" property="tpConta" isDomain="true"/>
		<adsm:gridColumn width="14%" title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate"/>
		<adsm:gridColumn width="14%" title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate"/>
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
	
	function bancoChange(e){

		if (e.value == e.previousValue)
		   return true;

		resetValue(document.getElementById("agenciaBancaria.idAgenciaBancaria"));

		return agenciaBancaria_banco_nrBancoOnChangeHandler();

	}
	
	function myOnPageLoad_cb(data, erro){
	      onPageLoad_cb();      
	      document.getElementById('labelPessoa').innerHTML=getElementValue("labelPessoaTemp")+":";
	}
	
	/** CAllBack da lookup de agenciaBancaria */
	function agenciaCallBack_cb(data, error){
		
		if(error != undefined){
			alert(error);
		}
		
		var retorno = agenciaBancaria_nrAgenciaBancaria_exactMatch_cb(data);
		
		if(retorno){
		
			setElementValue("agenciaBancaria.banco.nmBanco", data[0].banco.nmBanco);
			setElementValue("agenciaBancaria.banco.idBanco", data[0].banco.idBanco);
			setElementValue("agenciaBancaria.banco.nrBanco", data[0].banco.nrBanco);
			
		}
		
		return retorno;
	}
	
	/** OnPopUpSetValue da lookup de agenciaBancaria */
	function agenciaPopUpSetValue(data, error){
		
		setElementValue("agenciaBancaria.banco.nmBanco", data.banco.nmBanco);
		setElementValue("agenciaBancaria.banco.idBanco", data.banco.idBanco);
		setElementValue("agenciaBancaria.banco.nrBanco", data.banco.nrBanco);
		
		return true;
			
	}
	
	function myFindByIdCustomized(rowId){
         
		var dados = new Array();         
		setNestedBeanPropertyValue(dados, "idContaBancaria", rowId);
		
		var sdo = createServiceDataObject("lms.configuracoes.manterDadosBancariosPessoaAction.findByIdCustomized",
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
			
		contaBancariaGridDef.detailGridRow("setaDados", data);
		
	}
	
</script>