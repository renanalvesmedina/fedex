<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarTipoPagamentoPosto" service="lms.municipios.tipoPagamentoPostoService">
	<adsm:form action="/municipios/manterTipoPagamentoPosto">
		<adsm:hidden property="postoPassagem.idPostoPassagem"/>
		<adsm:textbox dataType="text" property="tpPosto" label="tipo" maxLength="10" size="35" disabled="true" labelWidth="18%" width="32%" serializable="false"/>
		<adsm:textbox dataType="text" property="localizacao" label="localizacaoMunicipio" maxLength="10" size="35" disabled="true" labelWidth="18%" width="32%" serializable="false"/>
		<adsm:textbox dataType="text" property="rodovia" label="rodovia" maxLength="10" size="35" disabled="true" labelWidth="18%" width="32%" serializable="false"/>
		<adsm:textbox dataType="text" property="sentido" label="sentidoCobranca" maxLength="10" size="35" disabled="true" labelWidth="18%" width="32%" serializable="false"/>
		
		<adsm:combobox service="lms.municipios.tipoPagamPostoPassagemService.find" boxWidth="198" optionLabelProperty="dsTipoPagamPostoPassagem" optionProperty="idTipoPagamPostoPassagem" property="tipoPagamPostoPassagem.idTipoPagamPostoPassagem" label="tipoPagamentoAceito" labelWidth="18%" width="32%"/>
		<adsm:textbox dataType="integer" property="nrPrioridadeUso" label="prioridadeUso" maxLength="2" size="3" labelWidth="18%" width="32%"/>
		<adsm:range label="vigencia" labelWidth="18%" width="82%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="TipoPagamentoPosto"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTipoPagamentoPosto" property="TipoPagamentoPosto" selectionMode="check" gridHeight="200" unique="true"
	 rows="11" defaultOrder="nrPrioridadeUso,tipoPagamPostoPassagem_.dsTipoPagamPostoPassagem,dtVigenciaInicial">
		<adsm:gridColumn title="tipoPagamentoAceito" property="tipoPagamPostoPassagem.dsTipoPagamPostoPassagem" width="45%" />
		<adsm:gridColumn title="prioridadeUso" property="nrPrioridadeUso" width="25%" align="right" />		
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="15%" />
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="15%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>