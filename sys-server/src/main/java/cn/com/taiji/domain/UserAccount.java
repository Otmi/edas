package cn.com.taiji.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity(name = "UserAccount")
@Table(name = "USER_ACCOUNT")
public class UserAccount implements Serializable {
	private static final long serialVersionUID = 1724450140216701197L;

	@Id
	@Pattern(regexp = "[A-Za-z0-9_\\-]+")
	@Column(name = "CODE", nullable = false, length = 50)
	private String code;

	@Column(name = "NAME", nullable = false, length = 50)
	private String name;

	@Column(name = "PASSWORD", nullable = false, length = 64)
	private String password;

	@Column(name = "ENABLED", nullable = false)
	private boolean enabled;

	@Column(name = "EMAIL", length = 50)
	private String email;

	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = DigestUtils.sha1Hex(StringUtils.trimToEmpty(password));
		// this.password = DigestUtils.sha256Hex(StringUtils.trimToEmpty(password));
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}



	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getCode()).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof UserAccount))
			return false;
		final UserAccount cast = (UserAccount) other;
		return new EqualsBuilder().append(this.getCode(), cast.getCode())
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("code", code).append("name", name).append("enabled", enabled)
				.toString();
	}

}