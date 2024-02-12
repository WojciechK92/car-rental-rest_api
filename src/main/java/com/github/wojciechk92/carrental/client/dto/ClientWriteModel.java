package com.github.wojciechk92.carrental.client.dto;

import com.github.wojciechk92.carrental.client.Client;

public class ClientWriteModel {
  private String firstName;
  private String lastName;
  private String email;
  private int tel;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int getTel() {
    return tel;
  }

  public void setTel(int tel) {
    this.tel = tel;
  }

  public Client toClient() {
    return new Client(firstName, lastName,email, tel);
  }

}
