package hateoas.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Category {
	
	@Id
	@Column
	@GeneratedValue
	public Long id;
	
	@Column
	public String name;
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<Widget> widgets;
	
	public Category() {
		super();
	}
	
	public Category(String name) {
		super();
		this.name = name;
	}
	
}
