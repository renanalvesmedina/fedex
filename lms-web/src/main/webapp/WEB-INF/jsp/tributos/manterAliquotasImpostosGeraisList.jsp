<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.tributos.manterAliquotasImpostosGeraisAction">
	<adsm:form 
		action="/tributos/manterAliquotasImpostosGerais"
		idProperty="idAliquotaContribuico"
	>

		<adsm:i18nLabels>
			<adsm:include key="LMS-27044"/>
			<adsm:include key="LMS-23006"/>
		</adsm:i18nLabels>

		<adsm:combobox 
			property="tpImposto" 
			label="tipoImposto" 
			domain="DM_TIPO_IMPOSTO" 
		/>

		<adsm:range 
			label="vigencia" 
		>
	        <adsm:textbox 
	        	dataType="JTDate" 
	        	property="dtVigencia"
	        />
        </adsm:range>

		<adsm:textbox 
			property="vlPiso"
			label="valorPiso"
			dataType="currency" 
			minValue="0"
			size="22"
		/>
		
		<adsm:textbox 
			property="pcAliquota"
			label="percentualAliquota" 
			dataType="percent" 
			maxValue="100"
			minValue="0"
			size="5"
			maxLength="5"
		/>

		<adsm:combobox 
			label="servicoAdicional" 
			property="servicoAdicional.idServicoAdicional" 
			optionLabelProperty="dsServicoAdicional" 
			optionProperty="idServicoAdicional" 
			service="lms.tributos.manterAliquotasImpostosGeraisAction.findServicoAdicional"
			boxWidth="244"
		/>
		
		<adsm:combobox 
			label="outroServico" 
			property="servicoTributo.idServicoTributo" 
			optionLabelProperty="dsServicoTributo" 
			optionProperty="idServicoTributo" 
			service="lms.tributos.manterAliquotasImpostosGeraisAction.findServicoTributo" 
			boxWidth="244"
		/>

        <adsm:lookup size="20" maxLength="20" width="85%"
					 idProperty="idFacade"
					 property="facade" 
					 criteriaProperty="pessoa.nrIdentificacao"
					 relatedCriteriaProperty="nrIdentificacaoFormatado" 
					 action="/configuracoes/manterPessoas" 
					 afterPopupSetValue="verificaPessoaPopup"
					 onDataLoadCallBack="verificaPessoa"
					 service="lms.tributos.manterAliquotasImpostosGeraisAction.findLookupPessoa" 
					 dataType="text" 
					 label="pessoa">
			<adsm:propertyMapping relatedProperty="pessoa.nmPessoa" modelProperty="nmPessoa"/>
			<adsm:propertyMapping relatedProperty="pessoa.idPessoa" modelProperty="idPessoa"/>
			<adsm:textbox size="30" maxLength="30" disabled="true" dataType="text" property="pessoa.nmPessoa"/>
		</adsm:lookup>
		<adsm:hidden property="pessoa.idPessoa" serializable="true"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="aliquotasImpostosGerais"/>
			<adsm:resetButton />
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid selectionMode="check" idProperty="idAliquotaContribuico" property="aliquotasImpostosGerais" gridHeight="200" unique="true" 
	           defaultOrder="tpImposto,dtVigenciaInicial, servicoAdicional_.dsServicoAdicional, servicoTributo_.dsServicoTributo, pessoa_.nmPessoa">
		<adsm:gridColumn property="tpImposto" title="tipoImposto" width="10%" isDomain="true"/>
		<adsm:gridColumn property="dtVigenciaInicial" title="vigenciaInicial" width="12%" dataType="JTDate"/>
		<adsm:gridColumn property="dtVigenciaFinal" title="vigenciaFinal" width="12%" dataType="JTDate"/>
		<adsm:gridColumn property="pcAliquota" title="percentualAliquota" dataType="percent" width="12%"/>
		<adsm:gridColumn property="servicoAdicional.dsServicoAdicional" title="servicoAdicional" width="20%" />
		<adsm:gridColumn property="servicoTributo.dsServicoTributo" title="outroServico" width="15%"/>
	    <adsm:gridColumn property="pessoa.nmPessoa" title="pessoa" width="18%"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>
<script>

	function verificaPessoaPopup(data){

		if( data != undefined ){
			if( data.tpPessoa.value != 'J' ){
				alert(i18NLabel.getLabel('LMS-23006'));
				resetValue('facade.idFacade');
				setFocus('facade.pessoa.nrIdentificacao');
				return false;
			}
		}
		
	}
	
	function verificaPessoa_cb(data,error){
		
		var retorno = facade_pessoa_nrIdentificacao_exactMatch_cb(data);
		
		if( retorno == true ){
			verificaPessoaPopup(data[0]);
		}		
		
		return retorno;
		
	}
	
</script>