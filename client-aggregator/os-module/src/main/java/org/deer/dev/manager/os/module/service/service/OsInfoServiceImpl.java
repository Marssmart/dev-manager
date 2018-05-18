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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.stream.Collectors;
import org.deer.dev.manager.os.module.service.dto.SystemProperty;
import org.deer.dev.manager.os.module.service.dto.SystemPropertyBuilder;
import org.springframework.stereotype.Service;

@Service
public class OsInfoServiceImpl implements OsInfoService {

  @Override
  public SystemProperty getSystemProperty(final String propertyName) {
    return new SystemPropertyBuilder()
        .setKey(propertyName)
        .setValue(System.getProperty(propertyName))
        .createSystemProperty();
  }

  @Override
  public Collection<SystemProperty> getAllSystemProperties() {
    return System.getProperties().stringPropertyNames()
        .stream()
        .map(this::getSystemProperty)
        .collect(Collectors.toList());
  }

  @Override
  public void setSystemProperty(SystemProperty systemProperty) {
    checkNotNull(systemProperty.getKey(), "Key is null");
    checkNotNull(systemProperty.getValue(), "Value is null");
    System.setProperty(systemProperty.getKey(), systemProperty.getValue());
  }
}
