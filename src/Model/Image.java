package Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="image")
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String articalHref;
	
	private String subPath;

	public Image(String articalHref, String path) {
		// TODO Auto-generated constructor stub
		this.articalHref = articalHref;
		this.subPath = path;
	}

	public String getArticalHref() {
		return articalHref;
	}

	public void setArticalHref(String articalHref) {
		this.articalHref = articalHref;
	}

	public String getSubPath() {
		return subPath;
	}

	public void setSubPath(String subPath) {
		this.subPath = subPath;
	}
	
}
