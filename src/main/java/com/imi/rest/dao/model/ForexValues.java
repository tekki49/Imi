package com.imi.rest.dao.model;

// Generated 25 Nov, 2015 2:42:46 PM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ForexValues generated by hbm2java
 */
@Entity
@Table(name = "forex_values", catalog = "ump_resource_mgr")
public class ForexValues implements java.io.Serializable {

	private Integer id;
	private String name;
	private Double value;

	public ForexValues() {
	}

	public ForexValues(String name, Double value) {
		this.name = name;
		this.value = value;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "value", precision = 22, scale = 0)
	public Double getValue() {
		return this.value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

}
