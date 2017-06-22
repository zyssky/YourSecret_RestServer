package Model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserResponse {

    public int code;

    public String message;

    public String token;

    public String userIconPath;

    public String nickName;

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserIconPath() {
		return userIconPath;
	}

	public void setUserIconPath(String userIconPath) {
		this.userIconPath = userIconPath;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
    
}