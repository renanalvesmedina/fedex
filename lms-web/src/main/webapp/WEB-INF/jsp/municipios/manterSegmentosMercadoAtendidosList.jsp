<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.municipioFilialSegmentoService">
	<adsm:form action="/municipios/manterSegmentosMercadoAtendidos" idProperty="idMunicipioFilialSegmento">
		<adsm:hidden property="municipioFilial.idMunicipioFilial"/>
		
		<adsm:textbox dataType="text" property="municipioFilial.filial.sgFilial"
				label="filial" labelWidth="19%" width="81%" size="3" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="municipioFilial.filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
		</adsm:textbox>
		<adsm:textbox dataType="text" property="municipioFilial.municipio.nmMunicipio" label="municipio" labelWidth="19%" width="81%" size="30" disabled="true" serializable="false" />
		<adsm:textbox property="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text" label="uf" labelWidth="19%" width="31%" disabled="true" size="2" serializable="false">
			<adsm:textbox dataType="text" property="municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>
		<adsm:textbox dataType="text" property="municipioFilial.municipio.unidadeFederativa.pais.nmPais" label="pais" labelWidth="18%" width="32%" size="30" disabled="true" serializable="false"/>
		<adsm:checkbox property="municipioFilial.municipio.blDistrito" label="indDistrito" labelWidth="19%" width="31%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" property="municipioFilial.municipio.municipioDistrito.nmMunicipio" label="municDistrito" labelWidth="18%" width="32%" size="30" disabled="true" serializable="false" />

		<adsm:combobox property="segmentoMercado.idSegmentoMercado" optionLabelProperty="dsSegmentoMercado" optionProperty="idSegmentoMercado"
				label="segmentoMercado" service="lms.vendas.segmentoMercadoService.find" labelWidth="19%" width="31%" boxWidth="172" />
		<adsm:range label="vigencia" labelWidth="18%" width="32%"  >
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="municipioFilialSegmento"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idMunicipioFilialSegmento" property="municipioFilialSegmento" selectionMode="check" rows="10" unique="true" defaultOrder="segmentoMercado_.dsSegmentoMercado, dtVigenciaInicial">
		<adsm:gridColumn title="segmentoMercado" property="segmentoMercado.dsSegmentoMercado" width="50%" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="25%" dataType="JTDate" />
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="25%" dataType="JTDate"/>

		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>