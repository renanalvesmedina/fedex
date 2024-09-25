package com.mercurio.lms.tracking;

import java.io.Serializable;
import java.math.BigDecimal;

public class Depot implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long code;
	private String description;
	private String acronym;

	private String street;
	private String addressDescription;
	private String addressNumber;
	private String addressComplement;
	private String district;
	private String zipCode;
	private String email;
	private String phoneAreaCode;
	private String phoneNumber;
	private String faxAreaCode;
	private String faxNumber;
	private BigDecimal nrLatitude;
	private BigDecimal nrLongitude;
	private Integer nrQuality;

	private City city;
	private State state;

	public Depot() {
	}

	public Depot(Long code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getAddressDescription() {
		return addressDescription;
	}

	public void setAddressDescription(String addressDescription) {
		this.addressDescription = addressDescription;
	}

	public String getAddressNumber() {
		return addressNumber;
	}

	public void setAddressNumber(String addressNumber) {
		this.addressNumber = addressNumber;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getAddressComplement() {
		return addressComplement;
	}

	public void setAddressComplement(String addressComplement) {
		this.addressComplement = addressComplement;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneAreaCode() {
		return phoneAreaCode;
	}

	public void setPhoneAreaCode(String phoneAreaCode) {
		this.phoneAreaCode = phoneAreaCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFaxAreaCode() {
		return faxAreaCode;
	}

	public void setFaxAreaCode(String faxAreaCode) {
		this.faxAreaCode = faxAreaCode;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public BigDecimal getNrLatitude() {
		return nrLatitude;
	}

	public void setNrLatitude(BigDecimal nrLatitude) {
		this.nrLatitude = nrLatitude;
	}

	public BigDecimal getNrLongitude() {
		return nrLongitude;
	}

	public void setNrLongitude(BigDecimal nrLongitude) {
		this.nrLongitude = nrLongitude;
	}

	public Integer getNrQuality() {
		return nrQuality;
	}

	public void setNrQuality(Integer nrQuality) {
		this.nrQuality = nrQuality;
	}
}
