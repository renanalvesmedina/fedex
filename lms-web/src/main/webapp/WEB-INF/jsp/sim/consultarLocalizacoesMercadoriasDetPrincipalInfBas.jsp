<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="210"  idProperty="idDoctoServico" >
	
		<adsm:textbox width="10%" labelWidth="10%" size="7" dataType="integer" property="nrNotaFiscal" label="notaFiscal" disabled="true" mask="00000000"/>
		<adsm:textbox width="10%" labelWidth="10%" size="7" dataType="integer" property="qtVolumes" label="volumes" disabled="true"/>
		
		
		<adsm:textbox width="13%" size="7" dataType="integer" property="qtNotaFiscal" labelWidth="15%" label="quantidadeNotas" disabled="true"/>
		<adsm:textbox width="22%" labelWidth="10%" size="15" dataType="currency" property="psReal" label="pesoReal" unit="kg" disabled="true" mask="###,###,##0.000"/>
		
		<adsm:textbox width="16%" labelWidth="10%" size="15" dataType="currency" property="psAforado" label="pesoCubado" unit="kg" disabled="true" mask="###,###,##0.000"/>
		<adsm:textbox width="16%" size="15" dataType="currency" property="psReferenciaCalculo"  labelWidth="10%" label="pesoCalculo" unit="kg" disabled="true" mask="###,###,##0.000"/>

        
		<adsm:textbox width="30%" labelWidth="15%" size="25" dataType="text" style="text-align:right" property="valor" label="valorMercadoria2" disabled="true"/>
		<adsm:textbox width="16%" labelWidth="10%" size="7" dataType="text" property="tpDensidade" label="densidade" disabled="true"/>
		
		<adsm:textbox width="24%" size="25" dataType="text" property="dsNaturezaProduto" labelWidth="10%" label="natureza" disabled="true"/>
		<adsm:textbox disabled="true" property="sgFilialOriginal" label="documentoServicoOrigem" labelWidth="20%" width="20%" size="6" dataType="text" cellStyle="vertical-align:bottom;">
		<adsm:textbox disabled="true" property="nrDoctoServicoOriginal" dataType="integer" size="10"  mask="0000000000" />
		</adsm:textbox>

		<adsm:textbox label="remetente" property="nrIdentificacaoRem" labelWidth="10%" width="40%" size="20" maxLength="20" dataType="text"  disabled="true">
        <adsm:textbox size="30" property="nmPessoRem" dataType="text"  disabled="true"/>
        </adsm:textbox>
        
        <adsm:textbox label="destinatario" property="nrIdentificacaoDest" labelWidth="10%" width="40%" size="20" maxLength="20" dataType="text"  disabled="true" >
        <adsm:textbox size="30" dataType="text" property="nmPessoDest" disabled="true"/>
        </adsm:textbox>
        
        <adsm:textbox label="awb" property="nrAwb" labelWidth="10%" width="16%" size="10" dataType="integer"  disabled="true" mask="0000000">
        <adsm:textbox size="2" dataType="integer" property="dvAwb" disabled="true"/>
        </adsm:textbox>
        
        
		
        
       <adsm:textbox dataType="text" property="nmEmpresaCiaAerea" label="ciaAerea" labelWidth="10%" width="19%" disabled="true"/>
       
       <adsm:textbox disabled="true" property="sgFilialPedCol" label="pedidoColeta" labelWidth="15%" width="30%" size="6" dataType="text" cellStyle="vertical-align:bottom;">
		<adsm:textbox disabled="true" property="nrColeta" dataType="integer" size="10"  mask="0000000000" />
		</adsm:textbox>
		
		<adsm:textbox dataType="text" property="solicitacaoPriorizacao" size="10" label="solicitacaoPriorizacaoEmbarque" labelWidth="30%" width="70%" disabled="true"/>
		
		
		<adsm:section caption="entrega"/>
		
			<adsm:textbox dataType="text" property="dsRotaEntrega" label="rota" width="33%" labelWidth="15%" size="22" maxLength="10" disabled="true"/>
			
			<adsm:textbox dataType="JTDate" property="dtPrevEntrega" label="previsao" width="35%" size="8" maxLength="10" disabled="true">
	        <adsm:textbox dataType="integer" property="nrDiasEntrega" size="1" disabled="true" unit="dias"/>
	        </adsm:textbox>
	
			<adsm:textbox dataType="text" property="dhBaixa" label="dataEntrega" width="33%" labelWidth="15%" size="22" maxLength="10" disabled="true">
	        <adsm:textbox dataType="integer" property="qtdediasUteis"  size="1" disabled="true" unit="dias"/>
	        </adsm:textbox>
	
			<adsm:textbox dataType="integer" property="nrDiasBloqueio" label="bloqueio" size="2" maxLength="10" unit="dias" disabled="true"/>
			<adsm:textbox dataType="text" property="nmRecebedor" label="nomeRecebedor" size="25" width="33%" labelWidth="15%" disabled="true"/>
			<adsm:textbox dataType="text" property="programacao"  label="agendamento" labelWidth="15%" width="35%" size="35" disabled="true" />
			<adsm:textbox dataType="text" property="solicitacaoRetirada"  label="solicitacaoRetirada" labelWidth="15%" width="35%" size="10" disabled="true" />

		<adsm:section caption="localColeta" />
			<adsm:textbox dataType="text" property="edColeta" width="35%" size="48" label="endereco" disabled="true"/>
			<adsm:textbox dataType="text" property="municipioColeta" width="33%" labelWidth="15%"  size="35" label="municipio" disabled="true"/>
			<adsm:textbox dataType="text" property="nmUfColeta" label="uf" width="35%" size="35" disabled="true"/>
			<adsm:textbox dataType="text" property="paisColeta" label="pais" size="35" disabled="true" width="33%" labelWidth="15%"/>
		
		<adsm:section caption="localEntrega"/>
			<adsm:textarea property="dsEnderecoEntrega" labelWidth="15%" width="85%" label="local" disabled="true" maxLength="500" columns="120"/>
		</adsm:form>	
			
	  	    
	   
	 
</adsm:window>   
<script>

function findByIdDetalhamento(){
	
	var idDoctoServico =  parent.parent.document.getElementById("idDoctoServico").value;
	setElementValue("idDoctoServico",idDoctoServico);
	
       
   _serviceDataObjects = new Array();
   	addServiceDataObject(createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.findByIdDetailAbaPrincipal", "onDataLoadDetPrincipal", {idDoctoServico:idDoctoServico}));
  	xmit();
	
	
	
}

function onDataLoadDetPrincipal_cb(data, errorMessage, errorKey){
	if(errorMessage != undefined)
		alert(errorMessage);
	else{
		onDataLoad_cb(data);
	}	
}
</script>