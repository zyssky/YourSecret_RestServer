package Model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ArticalResponse {
	private int code;
	
	private String message;
	
	private String articalClientUuid;
	
	private String articalHref;
	
	private String imageUri;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getArticalClientUuid() {
		return articalClientUuid;
	}

	public void setArticalClientUuid(String articalClientUuid) {
		this.articalClientUuid = articalClientUuid;
	}

	public String getArticalHref() {
		return articalHref;
	}

	public void setArticalHref(String articalHref) {
		this.articalHref = articalHref;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}
	
	
}
