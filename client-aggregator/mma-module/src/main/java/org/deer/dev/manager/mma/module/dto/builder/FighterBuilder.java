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

package org.deer.dev.manager.mma.module.dto.builder;

import org.deer.dev.manager.mma.module.dto.Fighter;

public class FighterBuilder {

  private String firstName;
  private String lastName;
  private String profileLink;
  private String ref;

  public FighterBuilder setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public FighterBuilder setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public FighterBuilder setProfileLink(String profileLink) {
    this.profileLink = profileLink;
    return this;
  }

  public FighterBuilder setRef(String ref) {
    this.ref = ref;
    return this;
  }

  public Fighter createFighter() {
    return new Fighter(firstName, lastName, profileLink, ref);
  }
}