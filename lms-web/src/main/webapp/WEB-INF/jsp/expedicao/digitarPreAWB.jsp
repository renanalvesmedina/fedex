<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="digitarPreAWB" type="main">
	<adsm:tabGroup selectedTab="0">
	
		<adsm:tab 
			title="listagem" 
			id="pesq" 
			src="/expedicao/digitarPreAWB" 
			cmd="list"/>
			
		<adsm:tab 
			title="consolidacaoCargaTitulo" 
			id="consolidacaoCargas" 
			src="/expedicao/digitarPreAWB" 
			cmd="consolidacaoCargas" 
			onHide="hide"/>
			
		<adsm:tab 
			title="geracaoManutencaoPreAWB" 
			id="geracaoManutencao" 
			src="/expedicao/digitarPreAWB" 
			cmd="geracaoManutencao" 
			boxWidth="200"
			onHide="hide" />
			
	</adsm:tabGroup>
</adsm:window>
