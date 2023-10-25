package study.datajpa.domain;

import javax.persistence.Embeddable;

import lombok.ToString;

@Embeddable
@ToString
public class Address {

	private String city;
	private String zipcode;
	private String street;

	public Address(String city, String zipcode, String street) {
		this.city = city;
		this.zipcode = zipcode;
		this.street = street;
	}

	public Address() {
	}
}
