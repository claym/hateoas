package hateoas.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Data;

@Entity
@Data
public class Property {

	@Id
	@Column
	@GeneratedValue
	private Long id;
	
	@Column
	private String name;
	
	@Column
	private String value;
	
	@ManyToMany
	private List<Widget> widgets;
	
	public Property() {
		super();
	}
	
	public Property(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	
}
