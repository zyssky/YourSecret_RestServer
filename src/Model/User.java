package Model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONException;
import org.json.JSONObject;

@Entity
@Table(name="user")
@XmlRootElement
public class User {
	
	@Id
	private String phoneNum;
	
	private String nickName;
	
	private String userIconPath;
	
	private String identifier;
	
	private String token;
	
	private Date tokenAvailDate;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Date getTokenAvailDate() {
		return tokenAvailDate;
	}

	public void setTokenAvailDate(Date tokenAvailDate) {
		this.tokenAvailDate = tokenAvailDate;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserIconPath() {
		return userIconPath;
	}

	public void setUserIconPath(String userIconPath) {
		this.userIconPath = userIconPath;
	}

	public JSONObject toJson() {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("nickName", nickName);
			jsonObject.put("phoneNum", phoneNum);
			jsonObject.put("token", token);
			jsonObject.put("userIconPath", userIconPath);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonObject;
	}

	
	

}
