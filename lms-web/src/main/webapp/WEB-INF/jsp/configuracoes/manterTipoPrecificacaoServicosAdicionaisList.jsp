<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.tipoPrecificacaoServicoAdicService">
	<adsm:form action="/configuracoes/manterTipoPrecificacaoServicosAdicionais">
	
		<adsm:hidden property="idServicoAdicionalTmp" serializable="false"/>
	
		<adsm:combobox property="servicoAdicional.idServicoAdicional" label="servicoAdicional" autoLoad="false"
					   service="lms.configuracoes.servicoAdicionalService.find" 
					   optionLabelProperty="dsServicoAdicional" optionProperty="idServicoAdicional" width="85%"/>			
					   
        <adsm:combobox property="tpTipoPrecificacaoServicoA" label="tipoPrecificacao" width="85%" domain="DM_TIPO_PRECIFICACAO"/>
        
        <adsm:range label="vigencia">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/> 
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoPrecificacaoServicoAdic"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
    </adsm:form>	
    <adsm:grid gridHeight="200" idProperty="idTipoPrecificacaoServicoA" property="tipoPrecificacaoServicoAdic" 
    		   defaultOrder="servicoAdicional_.dsServicoAdicional, tpTipoPrecificacaoServicoA, dtVigencia" rows="12">
	    <adsm:gridColumn width="40%" title="servicoAdicional" property="servicoAdicional.dsServicoAdicional"/>
        <adsm:gridColumn width="40%" title="tipoPrecificacao" property="tpTipoPrecificacaoServicoA" isDomain="true"/>
   	    <adsm:gridColumn width="20%" title="vigencia" property="dtVigencia" dataType="JTDate"/>
        <adsm:buttonBar> 
		   <adsm:removeButton/>
    	</adsm:buttonBar>
    </adsm:grid>

</adsm:window>
<script>
	
	/**
	* Sobrescrito para setar o foco no campo Tipo Precificação quando esta tela é chamada
	* pela tela de Manter Serviços Adicionais
	* Verifica se o campo idServicoAdicionalTmp possui valor então adiciona ele para a pesquisa da combo de serviços adicionais
	*/
	function onPageLoad_cb() {
	
		var servico;
	
		setMasterLink(this.document);
		
		if(document.getElementById("servicoAdicional.idServicoAdicional") != undefined && 
		   getElementValue("servicoAdicional.idServicoAdicional") != ""){		   
		   setFocus(document.getElementById("tpTipoPrecificacaoServicoA"));	   	   		   
		}		
		
		var data = new Array();	   		
		var tmp  = document.getElementById("idServicoAdicionalTmp");
		
		if(tmp != undefined && tmp.value != ""){
			setNestedBeanPropertyValue(data, "idServicoAdicional", tmp.value);			
			servico = "lms.configuracoes.servicoAdicionalService.findServicosAdicionaisAtivos";
		} else {
			servico = "lms.configuracoes.servicoAdicionalService.find";
		}
		
		var sdo = createServiceDataObject(servico, "servicoAdicional_idServicoAdicional", data);
		xmit({serviceDataObjects:[sdo]}); 
		
		return true;
		
	}	
	
	/**
	* Função de retorno da combo de servico adicional.	
	* Seta o valor do campo idServicoAdicionalTmp como padrão após a pesquisa.
	* Este campo só estará preenchido quando a chamada vier do manter serviços adicionais
	*/
	function servicoAdicional_idServicoAdicional_cb(data) {
		
		comboboxLoadOptions({e:document.getElementById("servicoAdicional.idServicoAdicional"), data:data});
		
		var tmp  = document.getElementById("idServicoAdicionalTmp");
		
		if(tmp != undefined && tmp.value != ""){			
			setElementValue("servicoAdicional.idServicoAdicional",tmp);
		}
		
	}

</script>
