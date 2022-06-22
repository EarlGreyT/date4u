package de.earlgreyt.date4u.core.formdata;

import de.earlgreyt.date4u.core.formdata.validation.PasswordMatches;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@PasswordMatches
public class UserDTO {
  @Email
  @NotNull
  @NotBlank(message = "You have to provide an email address")
  private String email;
  @NotNull
  @NotBlank(message = "You have to provide a password")
  private String password;
  private String matchingPassword;
  @NotNull
  @NotBlank(message = " You have to provide a Nickname")
  @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Only letters a to z, A to Z, digits and underscores")
  private String nickname;


  public String getEmail() {
    return email;
  }

  public CharSequence getPassword() {
    return password;
  }

  public String getNickname() {
    return  nickname;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getMatchingPassword() {
    return matchingPassword;
  }

  public void setMatchingPassword(String matchingPassword) {
    this.matchingPassword = matchingPassword;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("UserDTO[")
        .append("Email: ")
        .append(email)
        .append(", nickname: ")
        .append(nickname)
        .append("]");
    return builder.toString();
  }
}
