package hateoas.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;

@Entity
@Data
public class WidgetDetail {

	@Id
	@GeneratedValue(generator="myGenerator")
	@GenericGenerator(name="myGenerator", strategy="foreign", parameters=@Parameter(value="widget", name = "property"))
	private Long widgetId;
	
	@Column(name="count")
	private Long count;
	
	@OneToOne
	@JoinColumn(name="widget_id")
	private Widget widget;
	
}
