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

package org.deer.dev.manager.os.module.service.service;

import java.util.Collection;
import org.deer.dev.manager.os.module.service.dto.SystemProperty;

/**
 * Service providing information about system that node is running on
 */
public interface OsInfoService {

  /**
   * Retrieves a system property
   *
   * @param propertyName name of the property to retrieve
   * @return if such property exist, return its value, null otherwise
   */
  SystemProperty getSystemProperty(String propertyName);

  /**
   * Retrieves all system properties
   *
   * @return {@link Collection} of all system properties
   */
  Collection<SystemProperty> getAllSystemProperties();

  void setSystemProperty(final SystemProperty systemProperty);
}
