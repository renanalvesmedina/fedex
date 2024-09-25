<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vendas.campoComplementarService">

	<adsm:form action="/vendas/manterCamposComplementares" idProperty="idCampoComplementar">

		<adsm:textbox dataType="text" label="nome" maxLength="60" property="nmCampoComplementar" size="43"/>
		
		<adsm:textbox dataType="text" label="descricao" maxLength="60" property="dsCampoComplementar" size="43"/>
		
		<adsm:combobox domain="DM_TIPO_CAMPO" label="tipo" property="tpCampoComplementar"/>
		
		<adsm:textbox dataType="text" label="formatacao" maxLength="20" property="dsFormatacao" size="23"/>
		
		<adsm:textbox dataType="integer" label="tamanho" maxLength="2" property="nrTamanho" size="5" minValue="1"/>
		
		<adsm:combobox domain="DM_SIM_NAO" label="opcional" property="blOpcional"/>
		
		<adsm:combobox domain="DM_STATUS" label="situacao" property="tpSituacao"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="campoComplementar"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idCampoComplementar" property="campoComplementar" gridHeight="200" unique="true" defaultOrder="nmCampoComplementar" rows="11"> 
		<adsm:gridColumn title="nome"       property="nmCampoComplementar" width="15%"/>
		<adsm:gridColumn title="descricao"  property="dsCampoComplementar" width="28%"/>
		<adsm:gridColumn title="tipo"       property="tpCampoComplementar" width="12%" isDomain="true"/>
		<adsm:gridColumn title="formatacao" property="dsFormatacao"        width="13%"/>
		<adsm:gridColumn title="tamanho"    property="nrTamanho"           width="10%" align="right"/>
		<adsm:gridColumn title="opcional"   property="blOpcional"          width="10%" renderMode="image-check"/>
		<adsm:gridColumn title="situacao"   property="tpSituacao"          width="12%" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>