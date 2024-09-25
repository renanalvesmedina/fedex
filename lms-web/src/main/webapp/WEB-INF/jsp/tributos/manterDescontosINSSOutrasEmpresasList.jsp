<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.tributos.manterDescontosINSSOutrasEmpresasAction" >
	<adsm:form action="/tributos/manterDescontosINSSOutrasEmpresas">


		<adsm:i18nLabels>
			<adsm:include key="LMS-23004"/>
		</adsm:i18nLabels>


		<adsm:range label="dataEmissaoRecibo" width="85%">
			<adsm:textbox dataType="JTDate" property="dtEmissaoReciboInicial" />
			<adsm:textbox dataType="JTDate" property="dtEmissaoReciboFinal" /> 
		</adsm:range>
		



		<adsm:lookup service="lms.tributos.manterDescontosINSSOutrasEmpresasAction.findLookupProprietario" 
					 property="proprietario"
					 label="proprietario"
					 idProperty="idProprietario"
					 criteriaProperty="pessoa.nrIdentificacao"					 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"					 
					 dataType="text"
					 maxLength="20"
					 action="/contratacaoVeiculos/manterProprietarios"  
					 exactMatch="true"
					 minLengthForAutoPopUpSearch="5"
					 size="20"
					 afterPopupSetValue="verificaPessoaPopup"
					 onDataLoadCallBack="verificaPessoa"
					 width="45%">                    
			
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="proprietario.pessoa.nmPessoa"/>
 		    <adsm:textbox  size="31"  dataType="text" property="proprietario.pessoa.nmPessoa" disabled="true"/>   
		</adsm:lookup>




		<adsm:combobox label="tipoEmpresa" property="tpEmpresa" onchange="verificaTipoEmpresa(this.value);" domain="DM_TIPO_EMPRESA_INSS" width="85%"/>
		
		<adsm:lookup label="filial" 
     				 property="filial" 
     				 dataType="text"  
     				 service="lms.municipios.filialService.findLookupBySgFilial" 
     				 exactMatch="true"
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 size="3" 
					 maxLength="3" 
					 width="85%" 
					 action="/municipios/manterFiliais">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:textbox dataType="text" property="dsEmpresa" label="empresa" size="70" maxLength="60" width="85%"/>		
		
		<adsm:textbox dataType="text" property="nrRecibo" label="numeroRecibo" size="20" maxLength="20"/>
        <adsm:textbox dataType="currency" property="vlInss" label="valorINSS" minValue="0" maxLength="30" mask="#,###,###,###,###,##0.00"/>
        
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="DescontosInssCarreteiro"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idDescontoInssCarreteiro" property="DescontosInssCarreteiro" selectionMode="check" rows="8"
		defaultOrder="proprietario_pessoa_.nrIdentificacao,dtEmissaoRecibo,tpEmpresa" gridHeight="200">
		<adsm:gridColumn width="30%" title="proprietario" property="nmProprietario" />
		<adsm:gridColumn width="12%" title="dataEmissaoRecibo" property="dtEmissaoRecibo" dataType="JTDate"/>
		<adsm:gridColumn width="15%" title="tipoEmpresa" property="tpEmpresa" isDomain="true" />
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn width="39" title="filial" property="sgFilial" />
			<adsm:gridColumn width="156" title="" property="nmFilial" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn width="10%" title="numeroRecibo" property="nrRecibo" dataType="text" />
        <adsm:gridColumn width="10%" title="valorINSS" property="vlInss" dataType="currency" />
       
       <adsm:buttonBar>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar> 
	</adsm:grid>
</adsm:window>


<script>

	function verificaTipoEmpresa(tipo){
		
		setElementValue("nrRecibo", "");
		
		if(tipo == 'M'){
			document.getElementById("nrRecibo").dataType = "integer";
			document.getElementById("nrRecibo").maxChars = 20;
			document.getElementById("nrRecibo").mask = "####################";
		}else{
			document.getElementById("nrRecibo").dataType = "text";
			document.getElementById("nrRecibo").maxChars = 20;
			document.getElementById("nrRecibo").mask = '';
		}
	
	}
	
	function initWindow(evento){		
		if( evento.name == 'cleanButton_click' ){
			verificaTipoEmpresa("O");//O = Outros
		}
	}

	
	/* Trata o retorno da poup-up para verifica se o tpPessoa eh fisica, caso contrário mostra erro    */
	function verificaPessoaPopup(data){

		if( data != undefined ){
			if( data.pessoa.tpIdentificacao.value == 'CNPJ' ){
				alert(i18NLabel.getLabel('LMS-23004'));
				resetValue('proprietario.idProprietario');
				setFocus('proprietario.pessoa.nrIdentificacao');
				return false;
			}
		}
		
	}


	/* Trata o retorno da lookup para verifica se o tpPessoa eh fisica, caso contrário mostra erro    */
	function verificaPessoa_cb(data,error){
		
		var retorno = proprietario_pessoa_nrIdentificacao_exactMatch_cb(data);
		
		if( retorno == true ){
			verificaPessoaPopup(data[0]);
		}		
		
		return retorno;
		
	}


</script>