<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.tipoPrecificacaoServicoAdicService">
	<adsm:form action="/configuracoes/manterTipoPrecificacaoServicosAdicionais" idProperty="idTipoPrecificacaoServicoA">
	
		<adsm:hidden property="idServicoAdicionalTmp" serializable="false"/>
	
		<adsm:combobox property="servicoAdicional.idServicoAdicional" label="servicoAdicional" autoLoad="false"
			   service="lms.configuracoes.servicoAdicionalService.findServicosAdicionaisAtivos" 
			   optionLabelProperty="dsServicoAdicional" optionProperty="idServicoAdicional" width="85%" required="true"/>
			   
        <adsm:combobox property="tpTipoPrecificacaoServicoA" label="tipoPrecificacao" width="85%" domain="DM_TIPO_PRECIFICACAO" required="true"/>

		<adsm:textbox label="vigencia" dataType="JTDate" property="dtVigencia" width="85%" required="true"/>
		
		<adsm:textbox label="custoPadrao" dataType="currency" property="vlCustoPadrao" unit="reais"  maxLength="14" size="10" minValue="0.01" />

        <adsm:buttonBar>
	   	  <adsm:storeButton/>
          <adsm:newButton/>
  		  <adsm:removeButton/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>
<script>

	/**
	* Esta função verifica se o campo idServicoAdicionalTmp possui valor 
	* e então seta o valor como padrão na combo de servicos adicionais
	*/
	function initWindow(eventObj){
		var tmp  = document.getElementById("idServicoAdicionalTmp");
		
		if(tmp != undefined && tmp.value != ""){			
			setElementValue("servicoAdicional.idServicoAdicional",tmp);
		}
	}


	/**
	* Sobrescrito para setar o foco no campo Tipo Precificação quando esta tela é chamada
	* pela tela de Manter Serviços Adicionais
	*/
	function onPageLoad_cb() {
	
		setMasterLink(this.document);
		
		if(document.getElementById("servicoAdicional.idServicoAdicional") != undefined && 
		   getElementValue("servicoAdicional.idServicoAdicional") != ""){		   
		   setFocus(document.getElementById("tpTipoPrecificacaoServicoA"));	   	   		   
		}		
		
		var data = new Array();	   		
		var tmp  = document.getElementById("idServicoAdicionalTmp");
		
		if(tmp != undefined && tmp.value != ""){
			setNestedBeanPropertyValue(data, "idServicoAdicional", tmp.value);			
		}
		
		var sdo = createServiceDataObject("lms.configuracoes.servicoAdicionalService.findServicosAdicionaisAtivos", 
										  "servicoAdicional_idServicoAdicional", 
										  data);
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