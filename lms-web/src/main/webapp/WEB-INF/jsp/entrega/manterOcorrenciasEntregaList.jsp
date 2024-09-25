<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterOcorrenciasEntrega" service="lms.entrega.manterOcorrenciasEntregaAction" onPageLoadCallBack="trataComboTpOcorrencia" >
	<adsm:form action="/entrega/manterOcorrenciasEntrega" service="lms.entrega.manterOcorrenciasEntregaAction"   >
		
		<adsm:textbox dataType="integer" property="cdOcorrenciaEntrega" disabled="false" label="codigoOcorrencia" size="5" maxLength="3" labelWidth="20%" width="20%" />
		<adsm:textbox dataType="text" property="dsOcorrenciaEntrega" disabled="false" label="descricaoOcorrencia" maxLength="60" size="40" labelWidth="20%" width="40%" />
		
		<adsm:hidden property="isRecusa"  serializable="true"/>
		<adsm:combobox property="tpOcorrencia" domain="DM_TIPO_OCORRENCIA_ENTREGA" label="tipoOcorrencia"   labelWidth="20%" width="20%"/>
		
		<adsm:lookup property="evento" 
					 idProperty="idEvento" 
					 criteriaProperty="cdEvento"
					 service="lms.entrega.manterOcorrenciasEntregaAction.findLookupEventoAssociado"
					 action="/sim/manterEventosDocumentosServico" 
					 dataType="integer"  serializable="true" 
					 label="eventoAssociado" width="8%" size="3" labelWidth="20%" maxLength="3" > 
			<adsm:propertyMapping  relatedProperty="evento.dsEvento" modelProperty="dsEvento" />
			<adsm:textbox dataType="text" property="evento.dsEvento"  disabled="true" size="43" width="20%" serializable="false"/>
		</adsm:lookup>
		
		<adsm:combobox property="blDescontoDpe" label="descontoDPE"  domain="DM_SIM_NAO"  labelWidth="20%" width="20%"  />
		<adsm:combobox property="blOcasionadoMercurio"  domain="DM_SIM_NAO" label="ocasionadoPelaMercurio" labelWidth="20%" width="30%"/>

		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="20%"/>
		<adsm:combobox property="blContabilizarEntrega" label="contabilizarEntrega"  domain="DM_SIM_NAO"  labelWidth="20%" width="20%"  />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="OcorrenciaEntrega"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		</adsm:form>
		<adsm:grid idProperty="idOcorrenciaEntrega" property="OcorrenciaEntrega" selectionMode="check"  unique="true"  rows="9">

		<adsm:gridColumn width="6%"  title="codigo" property="cdOcorrenciaEntrega" dataType="integer"/>
		<adsm:gridColumn title="ocorrencia" property="dsOcorrenciaEntrega" />
		<adsm:gridColumn width="10%" title="tipo" property="tpOcorrencia"  isDomain="true" />
		<adsm:gridColumn width="30%" title="eventoAssociado" property="evento.dsEvento" />
		<adsm:gridColumn width="8%"  title="descontoDPE" property="blDescontoDpe" renderMode="image-check"/>
		<adsm:gridColumn width="10%" title="ocasionadoPelaMercurio" property="blOcasionadoMercurio" renderMode="image-check"/>	
		<adsm:gridColumn width="10%" title="situacao" property="tpSituacao.description" />
	
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>
<script type="text/javascript">

	/**
	* caso a tela seja chamada da tela de tratativa de ocorrencias (recusaTratativasCarta)
	* a combo tpOcorrencia deve mostrar somente "Recusas" e "Não Entregues"
	*/ 
	function trataComboTpOcorrencia_cb(data, error){
		onPageLoad_cb(data, error);
		if ( getElementValue("isRecusa") == 'R'  ){

			document.getElementById("cdOcorrenciaEntrega").value = "";
			document.getElementById("tpOcorrencia").required =  true;
			
			var data = new Object();
			var sdo = createServiceDataObject("lms.entrega.manterOcorrenciasEntregaAction.findDmTipoOcorrenciaEntrega", "tpOcorrencia" , data);
			xmit({serviceDataObjects:[sdo]});			
		}
	}
	       
</script>
