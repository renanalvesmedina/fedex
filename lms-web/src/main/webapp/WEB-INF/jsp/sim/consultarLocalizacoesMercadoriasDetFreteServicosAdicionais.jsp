<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	/**
	 * Como são usados divs, é necessário a função para gerar 100 colunas dentro da table no div.
	 */
	function geraColunas() {
		colunas = '<table class="Form" cellpadding="0" cellspacing="0" width="98%><tr>';
		for (i = 0 ; i < 33 ; i++) {
			colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td>';
			colunas += '<td><img src="lms/images/spacer.gif" width="8px" height="1px"></td><td><img src="lms/images/spacer.gif" width="8px" height="1px"></td>';
		}
		colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td></tr>';
		return colunas;
	}
	
	
	
</script>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="210" idProperty="idDoctoServico">
	<td colspan="100" >

	 <div id="IDArmazenagem" style="display:none;border:none;">
		<script>
			document.write(geraColunas());
		</script>	
			<adsm:section caption="armazenagem"/>
	    	
			<adsm:textbox dataType="integer" property="qtDiasArmazenagem" label="qtdeDias" size="25" disabled="true" labelWidth="20%" width="30%" style="text-align:right"/>
			<adsm:textbox dataType="integer" property="qtPaletesArmazenagem" label="quantidadePaletes" size="25" disabled="true" labelWidth="20%" width="30%" style="text-align:right"/>
		</table>
	  </div>
	  	
	  <div id="IDEscolta" style="display:none;border:none;">				
		<script>
			document.write(geraColunas());
		</script>	
			<adsm:section caption="escolta"/>
	    	
			<adsm:textbox dataType="integer" property="nrKmRodado" label="quilometragemRodada" size="25" disabled="true" labelWidth="20%" width="30%" style="text-align:right"/>
			<adsm:textbox dataType="integer" property="qtSegurancasAdicionais" label="quantidadeSegurancasAdicionais" size="25" disabled="true" labelWidth="20%" width="30%" style="text-align:right"/>
		</table>
	  </div>
	  
	  <div id="IDEstadiaVeiculo" style="display:none;border:none;">
		<script>
			document.write(geraColunas());
		</script>	
			<adsm:section caption="estadiaVeiculo"/>
	    	
			<adsm:textbox dataType="integer" property="qtDiasEstadiaVeiculo" label="qtdeDias" size="25" disabled="true" labelWidth="20%" width="80%" style="text-align:right"/>
		</table>
	  </div>
	  
	   <div id="IDPaletizacao" style="display:none;border:none;">
		<script>
			document.write(geraColunas());
		</script>
			<adsm:section caption="paletizacao"/>	
	    	
			<adsm:textbox dataType="integer" property="qtPaletesPaletizacao" label="quantidadePaletes" size="25" disabled="true" labelWidth="20%" width="80%" style="text-align:right"/>
		</table>
	  </div>
	  
	   <div id="IDReembolso" style="display:none;border:none;">
		<script>
			document.write(geraColunas());
		</script>
			<adsm:section caption="reembolso"/>		
	    	
			<adsm:textbox dataType="text" property="vlMercadoria" label="valorMercadoria" size="25" disabled="true" labelWidth="20%" width="30%" style="text-align:right"/>
			<adsm:textbox dataType="integer" property="qtCheques" label="qtdeCheques" size="25" disabled="true" labelWidth="15%" width="35%" style="text-align:right"/>
			<adsm:textbox dataType="text" property="dtCheque" label="dataPrimeiroCheque" size="25" disabled="true" labelWidth="20%" width="30%" />
		</table>
	  </div>
	   
	</adsm:form>   
</adsm:window> 
<script>
function findServicosAdicionaisFrete(){
	
	var idDoctoServico = parent.document.getElementById("idDoctoServico").value;
	var data = new Array();
	setNestedBeanPropertyValue(data,"idDoctoServico",idDoctoServico);
	
	_serviceDataObjects = new Array();
   	addServiceDataObject(createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.findServicosAdicionaisFrete", "onDataLoadServicosAdicionaisFrete", {idDoctoServico:idDoctoServico}));
  	xmit();
}

function onDataLoadServicosAdicionaisFrete_cb(data,exception){
	document.getElementById("IDArmazenagem").style.display = "block";
	document.getElementById("IDEscolta").style.display = "block";
	document.getElementById("IDReembolso").style.display = "block";
	document.getElementById("IDPaletizacao").style.display = "block";
	document.getElementById("IDEstadiaVeiculo").style.display = "block";
		
	var i;
	for(i = 0; i < data.length ; i++){
		
		if(document.getElementById(data[i].cdParcelaPreco)!= null){
		
			document.getElementById(data[i].cdParcelaPreco).style.display = "";
			
			if(data[i].qtPaletesPaletizacao != undefined)
				setElementValue("qtPaletesPaletizacao", data[i].qtPaletesPaletizacao);
				
			if(data[i].qtPaletesArmazenagem != undefined)
				setElementValue("qtPaletesArmazenagem", data[i].qtPaletesArmazenagem);
				
			if(data[i].qtCheques != undefined)
				setElementValue("qtCheques", data[i].qtCheques);
			
			if(data[i].dtPrimeiroCheque != undefined)
				setElementValue("dtCheque", data[i].dtPrimeiroCheque);
				
			if(data[i].vlMercadoria != undefined)
				setElementValue("vlMercadoria", data[i].vlMercadoria);
			
			if(data[i].qtDiasEstadiaVeiculo != undefined)
				setElementValue("qtDiasEstadiaVeiculo", data[i].qtDiasEstadiaVeiculo);
			
			if(data[i].qtDiasArmazenagem != undefined)
				setElementValue("qtDiasArmazenagem", data[i].qtDiasArmazenagem);
			
			if(data[i].nrKmRodado != undefined)
				setElementValue("nrKmRodado", data[i].nrKmRodado);
				
			if(data[i].qtSegurancasAdicionais != undefined)
				setElementValue("qtSegurancasAdicionais", data[i].qtSegurancasAdicionais);
		}	
	}
	
	
}


</script>  