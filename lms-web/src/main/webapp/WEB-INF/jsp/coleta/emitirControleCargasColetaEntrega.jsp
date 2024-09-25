<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="emitirControleCargasColetaEntrega" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="emitirControleCargas" id="emissao" src="/coleta/emitirControleCargasColetaEntrega" cmd="emissao" 
		          boxWidth="160" />

		<adsm:tab title="equipeColetaEntregaTitulo" id="equipe" src="/coleta/emitirControleCargasColetaEntrega" cmd="equipe" 
				  boxWidth="160" masterTabId="emissao" copyMasterTabProperties="true" disabled="true" />

		<adsm:tab title="postosPassagemTitulo" id="postos" src="/coleta/emitirControleCargasColetaEntrega" cmd="postos" 
				  boxWidth="160" masterTabId="emissao" copyMasterTabProperties="true" disabled="true" />
				  
		<adsm:tab title="riscosTitulo" id="riscos" src="/coleta/emitirControleCargasColetaEntrega" cmd="riscos" 
				  boxWidth="160" masterTabId="emissao" copyMasterTabProperties="true" disabled="true" />
	</adsm:tabGroup>
</adsm:window>
