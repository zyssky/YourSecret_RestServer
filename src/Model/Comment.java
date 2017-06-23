package Model;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="comment")
@XmlRootElement
public class Comment {
	
	public Comment() {
		
	}
	
	public Comment(String content, String articalHref, String iconPath, String nickName,String authorId) {
		// TODO Auto-generated constructor stub
		this.articalHref = articalHref;
		this.content = content;
		this.iconPath = iconPath;
		this.nickName = nickName;
		this.authorId = authorId;
		date = new Date().getTime();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String articalHref;
	
	private String content;
	
	private long date;
	
	private String nickName;
	
	private String iconPath;
	
	private String authorId;
	
	private String title;
    
	private String imageUri;
    
	private String introduction;
	
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getArticalHref() {
		return articalHref;
	}

	public void setArticalHref(String articalHref) {
		this.articalHref = articalHref;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	
	
	

}
