<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function carregaDados_cb(data, error) {
		onPageLoad_cb(data, error);
	    var data = new Array();
	    var sdo = createServiceDataObject("lms.sim.manterPedidosComprasAction.findInformacoesUsuarioLogado", "pageLoad",data);
	    xmit({serviceDataObjects:[sdo]});
	}
	
 

</script>
<adsm:window title="manterPedidosCompras" 
		service="lms.sim.manterPedidosComprasAction" 
		onPageLoadCallBack="carregaDados">
	<adsm:form action="/sim/manterPedidosCompras" height="105"> 
		<adsm:i18nLabels>
                <adsm:include key="LMS-00013"/>
                <adsm:include key="remetente"/>
                <adsm:include key="destinatario"/>
                <adsm:include key="pedido"/>
                <adsm:include key="pedidoInternacional"/>
                <adsm:include key="periodoEmissao"/>
                <adsm:include key="notaFiscal"/>
    	</adsm:i18nLabels>
    	
		<adsm:hidden property="idPedidoCompra"/>
		<adsm:hidden property="idUsuario"/>
		<adsm:hidden property="idFilial"/>
		<adsm:hidden property="sgFilial"/>
		<adsm:hidden property="nmFantasia"/>
		<adsm:hidden property="empresa.tpEmpresa" value="M" serializable="false"/>
		<adsm:lookup label="filialInclusao" labelWidth="15%" dataType="text" size="3" maxLength="3" width="77%"
				     service="lms.sim.manterPedidosComprasAction.findLookupFilial" property="filial" 
				     idProperty="idFilial" 
					 criteriaProperty="sgFilial" action="/municipios/manterFiliais" required="true">
  		            <adsm:propertyMapping criteriaProperty="empresa.tpEmpresa"       modelProperty="empresa.tpEmpresa"/>
					<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
					<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="35" disabled="true"/> 
		</adsm:lookup>

		<adsm:lookup idProperty="idCliente"  
					 criteriaProperty="pessoa.nrIdentificacao"
					 property="remetente" 
					 service="lms.sim.manterPedidosComprasAction.findLookupCliente" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/vendas/manterDadosIdentificacao"  dataType="text" 
					 label="remetente"  size="20" maxLength="20" width="75%" labelWidth="15%" >	
			<adsm:propertyMapping relatedProperty="remetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="remetente.pessoa.nmPessoa" 
						  size="50" maxLength="50" disabled="true" />
								  
		</adsm:lookup>
		
		
		<adsm:lookup idProperty="idCliente"  
					 criteriaProperty="pessoa.nrIdentificacao"
					 property="destinatario" 
					 service="lms.sim.manterPedidosComprasAction.findLookupCliente" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/vendas/manterDadosIdentificacao"  dataType="text" 
					 label="destinatario"  size="20" maxLength="20" width="75%" labelWidth="15%" >	
			<adsm:propertyMapping relatedProperty="destinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="destinatario.pessoa.nmPessoa" 
						  size="50" maxLength="50" disabled="true" />
								  
		</adsm:lookup>		

		<adsm:textbox dataType="integer" property="nrPedido" label="pedido" labelWidth="15%" size="10"  maxLength="10"/>
		<adsm:textbox dataType="text" property="nrPedidoInternacional" label="pedidoInternacional" size="20" labelWidth="15%" maxLength="20" />
 

		<adsm:combobox domain="DM_TIPO_ORIGEM_PEDIDO_COMPRA" property="tpOrigem" label="origem" labelWidth="15%" />
		<adsm:textbox dataType="text" property="fatura" label="fatura" labelWidth="15%"  maxLength="20"/>


		<adsm:textbox dataType="text" property="notaFiscal" label="notaFiscal" labelWidth="15%"  maxLength="20"/>
		<adsm:range label="periodoEmissao" labelWidth="15%" >
			<adsm:textbox dataType="JTDate" property="dataHoraInicial" cellStyle="vertical-Align:bottom;"/>
			<adsm:textbox dataType="JTDate" property="dataHoraFinal" cellStyle="vertical-Align:bottom;"/>
       </adsm:range>

		
		

       	
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" id="__buttonBar:0.findButton" disabled="false" onclick="validateDados();" buttonType="findButton"/>
			<%--adsm:findButton callbackProperty="pedidoCompra"/--%>
			<adsm:button caption="limpar" onclick="limpar_OnClick();" disabled="false" buttonType="newButton"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idPedidoCompra"    gridHeight="163"
				property="pedidoCompra"      scrollBars="horizontal" rows="8"
		       service="lms.sim.manterPedidosComprasAction.findPaginatedCustom" 
		       rowCountService="lms.sim.manterPedidosComprasAction.getRowCount">

		<adsm:gridColumn dataType="text" width="50"  title="identificacaoRemetente" property="tipoIdRemetente.description" />
		<adsm:gridColumn dataType="text" width="125" title="" property="idRemetente" align="right"/>
		<adsm:gridColumn dataType="text" width="160" title="remetente" property="remetente"/>	
				
		<adsm:gridColumn dataType="text" width="50"  title="identificacaoDestinatario" property="tipoIdDestinatario.description" />
		<adsm:gridColumn dataType="text" width="125" title="" property="idDestinatario" align="right"/>		
		<adsm:gridColumn dataType="text" width="160" title="destinatario" property="destinatario"/>	
				
		<adsm:gridColumn dataType="integer" property="nrPedido" title="pedido" width="100"/>
		<adsm:gridColumn dataType="text" property="nrPedidoInternacional" title="pedidoInternacional" width="100" />
		<adsm:gridColumn dataType="text" property="origem.description" title="origem" width="50"/>
		<adsm:gridColumn dataType="text" property="fatura" title="fatura" width="50"/>
		<adsm:gridColumn dataType="text" property="notaFiscal" title="notaFiscal" width="70"/>
		<adsm:gridColumn width="70"  title="pagamentoCartao" property="cartao" renderMode="image-check"/>
		<adsm:gridColumn width="150" title="dataHoraEmissao" property="emissao" align="center" dataType="JTDateTimeZone"/>
		<adsm:gridColumn width="150" title="dataPrevisaoEntregaBrasil" property="entregaBrasil" dataType="JTDate"/>
		<adsm:gridColumn width="150" title="dataPrevisaoEntregaExterior" property="entregaExterior" dataType="JTDate"/>
		
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
    </adsm:grid>
</adsm:window>
<script>
	function validateDados() {
		var ret = validateTabScript(document.forms) 
		if(ret == true)
				validatePeriodo();
		return false;
	}
	
	function validatePeriodo(){
		if(getElementValue("dataHoraInicial") != "" && getElementValue("dataHoraFinal")!= ""){
			var params = {
			    dataHoraInicial:getElementValue("dataHoraInicial"), 
				dataHoraFinal:getElementValue("dataHoraFinal")};
			var sdo = createServiceDataObject("lms.sim.manterPedidosComprasAction.validatePeriodo", 
				"validatePeriodo", params);
			xmit({serviceDataObjects:[sdo]});
		}else
		 	findButtonScript('pedidoCompra', document.forms[0]);
	}

	function validatePeriodo_cb(dados, erros) {
		if(erros) {
			alert(erros);
			setFocus(document.getElementById("dataHoraFinal"));
			return false;
		}
		findButtonScript('pedidoCompra', document.forms[0]);
	}
	
	
	document.getElementById("idFilial").masterLink = true;
	document.getElementById("sgFilial").masterLink = true;
	document.getElementById("nmFantasia").masterLink = true;
	function limpar_OnClick(){
	cleanButtonScript(undefined,undefined,undefined);
		preencheFilial();
	}
	 function pageLoad_cb(data, error) {
		setElementValue("idUsuario",getNestedBeanPropertyValue(data, "idUsuarioSessao"));
		setElementValue("idFilial",getNestedBeanPropertyValue(data,"filial.idFilial"));	
		setElementValue("sgFilial",getNestedBeanPropertyValue(data,"filial.sgFilial"));	
		setElementValue("nmFantasia",getNestedBeanPropertyValue(data, "pessoa.nmFantasia"));
		preencheFilial();
	}
	function preencheFilial(){
		setElementValue("filial.idFilial",getElementValue("idFilial"));	
		setElementValue("filial.sgFilial",getElementValue("sgFilial"));
		setElementValue("filial.pessoa.nmFantasia",getElementValue("nmFantasia")); 
	} 
	function validateTab() {
		if (validateTabScript(document.forms)) {
			if (
					(getElementValue("nrPedidoInternacional") != "") ||
					(getElementValue("nrPedido") != "") ||
					(getElementValue("destinatario.idCliente") != "") ||
					(getElementValue("remetente.idCliente") != "") || (getElementValue("dataHoraInicial") != "" || getElementValue("dataHoraFinal") != "") || (getElementValue("notaFiscal") != "") ) 
				{
					return true;
				} else {
					
					alert(i18NLabel.getLabel("LMS-00013") 
								+ i18NLabel.getLabel("remetente") + ", "
								+ i18NLabel.getLabel("destinatario")+ ", "
								+ i18NLabel.getLabel("pedido")+ ", "
								+ i18NLabel.getLabel("pedidoInternacional")+ ", "
								+ i18NLabel.getLabel("notaFiscal")+ ", "
								+ i18NLabel.getLabel("periodoEmissao")+ " . "
								);
					return false;
                }
            }
		return false;
    }	
</script>