/*
 *
 * (C) Copyright 2018 Ján Srniček (https://github.com/Marssmart)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.deer.dev.manager.mma.module.dto;

/**
 * Fighter representation
 */
public class Fighter {

  public static final String[] CSV_COLUMNS = new String[]
      {"firstName", "lastName", "profileLink", "ref"};

  public static final Integer[] RELEVANT_CSV_COL_INDEXES = new Integer[]{1, 2, 3, 4};

  private String firstName;
  private String lastName;
  private String profileLink;
  private long ref;

  public Fighter(String firstName, String lastName, String profileLink, long ref) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.profileLink = profileLink;
    this.ref = ref;
  }

  public Fighter() {
    this(null, null, null, 0);
  }

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

  public String getProfileLink() {
    return profileLink;
  }

  public void setProfileLink(String profileLink) {
    this.profileLink = profileLink;
  }

  public long getRef() {
    return ref;
  }

  public void setRef(long ref) {
    this.ref = ref;
  }

  @Override
  public String toString() {
    return "Fighter{" + ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", profileLink='" + profileLink + '\'' +
        ", ref='" + ref + '\'' +
        '}';
  }
}
