package hateoas.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString(exclude= {"widgetDetail", "category", "properties"})
public class Widget {

	@Id
	@Column
	@GeneratedValue
	private Long id;

	@Column
	private String name;

	@OneToOne(mappedBy="widget", cascade=CascadeType.ALL)
	private WidgetDetail widgetDetail;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private Category category;

	@ManyToMany
	private List<Property> properties;
	
	public Widget() {
		super();
	}
	
	public Widget(String name) {
		super();
		this.name = name;
	}
}
